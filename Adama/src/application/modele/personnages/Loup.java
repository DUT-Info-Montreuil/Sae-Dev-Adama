package application.modele.personnages;

import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.exception.ErreurInventairePlein;
import application.modele.ressources.Bois;

public class Loup extends Ennemis {
	
	private static final int[] TAILLE = {1,1};
	private static final int PV = 18;
	private static final int DEGAT = 2;
	private static final Inventaire INVENTAIRE = new Inventaire(new Bois(0));
	private static final int HAUTEUR_SAUT = 5;
	private static final int VITESSE_DEPLACEMENT = 5;
	
	public Loup(int x, int y, Environnement environnement) throws ErreurInventairePlein {
		super(PV, x, y, VITESSE_DEPLACEMENT, environnement, INVENTAIRE, HAUTEUR_SAUT, TAILLE, DEGAT);
	}
	@Override
	public void agir() {

	}
}
