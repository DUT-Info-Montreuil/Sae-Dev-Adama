package application.modele;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
		ajouter(new Joueur(420, 576, this));
		Checkpoint checkpoint = new Checkpoint(340,576,this);
		this.getJoueur().setCheckpoint(checkpoint);
	}
	
	public void tourDejeu() {
		if (personnages.size()<200)
			ajouter(faireSpawner());
		personnages.forEach(pnj -> {
			if (pnj instanceof Pnj)
				try {
					((Pnj)pnj).agir();
				} catch (ErreurObjetIntrouvable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		});
		personnages.forEach(perso -> perso.gravite());
		if(personnages.get(0).estMort() ) {
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


	public Personnage emplacement(int x, int y) {
		int indiceDansMap = (x/Carte.HAUTEUR + (y/Carte.HAUTEUR * Carte.LARGEUR));
		return this.personnages.get(indiceDansMap);
	}

	public Personnage emplacement(int indice) {
		return this.personnages.get(indice);
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

	public void attaquerPersonnages(int lieu, int degat) throws ErreurInventairePlein {
		this.personnages.get(lieu).decrementerPv(degat);
		if (this.personnages.get(lieu).estMort()) {
			this.personnages.get(lieu).meurt();
			this.supprimer(lieu);
		}
	}
}