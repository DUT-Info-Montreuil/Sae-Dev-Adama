package application.modele.personnages;

import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.exception.ErreurObjetIntrouvable;

public abstract class Pnj extends Personnage {

	public Pnj(int pv, int x, int y, int vitesseDeplacement, Environnement environnement, Inventaire inventaire, int hauteurSaut, int[] taille) {
		super(pv, x, y, vitesseDeplacement, environnement, inventaire, hauteurSaut, taille);
	}
	
	public abstract void agir() throws ErreurObjetIntrouvable;

}
