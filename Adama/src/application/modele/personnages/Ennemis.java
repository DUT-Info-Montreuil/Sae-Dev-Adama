package application.modele.personnages;

import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.exception.ErreurObjetIntrouvable;

public abstract class Ennemis extends Pnj{
	private int degat;

	public Ennemis(int pv, int x, int y, int vitesseDeplacement, Environnement environnement, Inventaire inventaire, int hauteurSaut, int[] taille, int degat) {
		super(pv, x, y, vitesseDeplacement, environnement, inventaire, hauteurSaut, taille);
		this.degat = degat;
	}

	

	
	public int getDegat() {
		return this.degat;
	}

	public void attaquer() throws ErreurObjetIntrouvable {
		if(this.ouSeTrouveLeJoueur()) {
			this.droite();
		}
	}

	public void sauterSurJoueur() throws ErreurObjetIntrouvable {
		this.sauterEnDirection(this.ouSeTrouveLeJoueur());
	}
}
