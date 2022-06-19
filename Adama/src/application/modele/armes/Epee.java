package application.modele.armes;

import application.modele.Carte;
import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.exception.ErreurInventairePlein;
import application.modele.personnages.Personnage;

public class Epee extends Arme{
	public final static int DEGAT = 5;
	public final static int PORTER = 2;
	public final static int TEMPSRECHARGE = 2;
	
	public Epee() {
		super(DEGAT, PORTER, TEMPSRECHARGE);
		
	}


	public void attaquer(Inventaire inventaire, Environnement environnement, int lieu) throws ErreurInventairePlein {
		int largeur = Carte.LARGEUR;
		if (environnement.emplacement(lieu) instanceof Personnage)
			environnement.attaquerPersonnages(lieu, this.getDegat());
		if (environnement.emplacement(lieu) instanceof Personnage)
			environnement.attaquerPersonnages(lieu-largeur, this.getDegat());
		if (environnement.emplacement(lieu) instanceof Personnage)
			environnement.attaquerPersonnages(lieu+largeur, this.getDegat());
		
	}
	
	public String toString() {
		return "Epee";
	}

	@Override
	public void utiliser(int val) {
		attaquer();
	}
}