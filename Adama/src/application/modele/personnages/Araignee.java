package application.modele.personnages;

import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.exception.ErreurInventairePlein;
import application.modele.ressources.AntiVenin;
import application.modele.ressources.Bois;
import application.modele.ressources.Fils;

public class Araignee extends Ennemis {
	
	private static final int[] TAILLE = {1,1};
	private static final int PV = 18;
	private static final int DEGAT = 2;
	private static final int TAUX_APPARITION_FILS = 70;
	private static final int TAUX_APPARITION_ANTIVENIN = 75;
	private static final Inventaire INVENTAIRE = new Inventaire(null);
	private static final int HAUTEUR_SAUT = 5;
	private static final int VITESSE_DEPLACEMENT = 5;
	
	public Araignee(int x, int y, Environnement environnement) throws ErreurInventairePlein {
		super(PV, x, y, VITESSE_DEPLACEMENT, environnement, INVENTAIRE, HAUTEUR_SAUT, TAILLE, DEGAT);
		int lootFils = (int)Math.random()*101;
		int lootANTIPOISON = (int)Math.random()*101;
		if(lootFils <= TAUX_APPARITION_FILS)
			super.getInventaire().ajouter(new Fils(0));
		if(lootANTIPOISON <= TAUX_APPARITION_ANTIVENIN)
			super.getInventaire().ajouter(new AntiVenin(1));
	}

	
	public void agir() {
		
	}

}
