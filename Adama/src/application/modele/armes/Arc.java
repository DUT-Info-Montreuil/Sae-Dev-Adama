package application.modele.armes;

import java.util.ArrayList;

import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.personnages.Personnage;

public class Arc extends Arme {
	private Fleche fleche;
	private ArrayList<Fleche> carquois;
	
	
	public Arc(Inventaire inventaire) {
		super(1, 1, 2);
		this.carquois = new ArrayList<Fleche>();
	}
	
	
	public void bander() throws ErreurObjetIntrouvable {
		this.fleche = carquois.remove(0);
	}
	
	public void debander() throws ErreurInventairePlein {
		this.carquois.add(this.fleche);
		this.fleche = null;
		
	}
	
	public void tirer(Environnement environnement, boolean direction) throws ErreurInventairePlein {
		int distance = 0;
		int lieu = 0;
		int largeur = environnement.getCarte().getLargeur();
		//environnement.getCarte().getPoubelle().ajouter(this.fleche);
		while(this.getPorter() >= distance) {
			fleche.tirer(direction);
			lieu = fleche.getX();
			distance += this.fleche.getVitesse();
//			if (environnement.emplacement(lieu) instanceof Personnage)
//				environnement.attaquerPersonnages(lieu, this.fleche.getDegat());
//			if (environnement.emplacement(lieu-largeur) instanceof Personnage)
//				environnement.attaquerPersonnages(lieu-largeur, this.fleche.getDegat());
//			if (environnement.emplacement(lieu+largeur) instanceof Personnage)
//				environnement.attaquerPersonnages(lieu+largeur, this.fleche.getDegat());
		}
		
		this.fleche.detruire();
		this.fleche = null;
	}
	
	
	public String toString() {
		return "Arc";
	}

	@Override
	public void utiliser(int val) {
		
	}
	
	
	
}