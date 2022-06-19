package application.modele;

import java.io.IOException;

import java.util.ArrayList;

import application.modele.armes.Arme;
import application.modele.effet.Renforcer;
import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.exception.TailleMapException;
import application.modele.personnages.Cerf;
import application.modele.personnages.Joueur;
import application.modele.personnages.Personnage;
import application.modele.personnages.Pnj;
import application.modele.personnages.Slime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Environnement {

	private ObservableList<Personnage> personnages;
	private Carte carte;

	public Environnement() throws TailleMapException, IOException {
		this.carte = new Carte();
		this.personnages = FXCollections.observableArrayList();
	}
	
	public void initJeu() {
		new Joueur(420, 576, this);
		Checkpoint checkpoint = new Checkpoint(340,576,this);
		this.getJoueur().setCheckpoint(checkpoint);
	}
	
	public void tourDejeu() {
		if (personnages.size()<50)
			ajouter(faireSpawner());
		
		personnages.forEach(pnj -> {
			if (pnj instanceof Pnj)
				try {
					((Pnj)pnj).agir();
				} catch (ErreurObjetIntrouvable e) {
					e.printStackTrace();
				}
		});
		
		personnages.forEach(perso -> perso.gravite());
		
		if(personnages.get(0).estMort()) {
			System.out.println("Game over");
			personnages.get(0).incrementerPv(7);
			((Joueur) personnages.get(0)).teleporterToCheckpoint();
			System.out.println("Respawn");
		}
	}
	
	private ArrayList<Pnj> faireSpawner() {
		ArrayList<Pnj> vontSpawner = new ArrayList<Pnj>();
		double chanceSpwan = Math.random();
		int x = (int) (Math.random()*Carte.TAILLE_BLOC*Carte.LARGEUR);
		int y = this.carte.aMemeLeSol(x);
		if (chanceSpwan<0.001) {
			vontSpawner.add(new Slime(x, y-Carte.TAILLE_BLOC*Slime.TAILLE[1], this));
		}
		else if (chanceSpwan<0.003) {
			y = y - Carte.TAILLE_BLOC*Cerf.TAILLE[1];
			if(this.carte.emplacement(x, y, Cerf.TAILLE)==null)
				vontSpawner.add(new Cerf(x, y, this));
		}
		return vontSpawner;
	}
	
	public void ajouter(ArrayList<Pnj> pnjs) {
		this.personnages.addAll(pnjs);
	}
	
	public void ajouter(Personnage personnage) {
		this.personnages.add(personnage);
	}

	public void supprimer(Personnage personnage) {
		this.personnages.remove(personnage);
	}

	public void supprimer(int indice) {
		this.personnages.remove(indice);
	}

	public Joueur getJoueur() {
		for (int i = 0; i < this.getPersonnages().size() ; i++) {
			if(this.getPersonnages().get(i) instanceof Joueur) {
				return (Joueur) this.getPersonnages().get(i);
			}
		}
		return null;
	}

	public ObservableList<Personnage> getPersonnages(){
		return this.personnages;
	}

	public Carte getCarte(){
		return this.carte;
	}

	public void attaquerPersonnages(Joueur attaquant) throws ErreurInventairePlein {
		Personnage perso;
		Arme armePorte = ((Arme)attaquant.getObjetEquiper());
		boolean touche = false;
		int xMin;
		int xMax;
		for (int i=this.personnages.size()-1; i>=0; i--) {
			perso = this.personnages.get(i);
			int j =0;
			while (!touche && perso.getTaille()[0]>j) {
				if (attaquant.getDirection()) {
					xMin = attaquant.getX() + Carte.TAILLE_BLOC + 1;
					xMax = xMin + armePorte.getPorter();
					if (perso.getX()+j*Carte.TAILLE_BLOC<xMin && perso.getX()+j*Carte.TAILLE_BLOC>xMax)
						touche = true;
				}
				else {
					xMin = attaquant.getX() - 1;
					xMax = xMin - armePorte.getPorter();
					if (perso.getX()+j*Carte.TAILLE_BLOC>xMin && perso.getX()+j*Carte.TAILLE_BLOC<xMax)
						touche = true;
				}
				j++;
			}
			if (touche)
				perso.decrementerPv(armePorte.getDegat()+armePorte.getDegat()*((Renforcer)perso.getEffets().get(2)).getPourcenetageRenforcement());
			if (perso.estMort()) {
				perso.meurt();
				this.supprimer(i);
			}
			touche = false;
		}
	}
}