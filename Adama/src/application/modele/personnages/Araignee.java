package application.modele.personnages;

import application.modele.Environnement;
import application.modele.exception.ErreurInventairePlein;
import application.modele.ressources.Venin;
import application.modele.ressources.Fils;

public class Araignee extends Ennemis {
	
	private static final int[] TAILLE = {2,1};
	private static final int TAUX_APPARITION_FILS = 70;
	private static final int TAUX_APPARITION_ANTIVENIN = 75;
	
	public Araignee(int pv, int x, int y, int vitesseDeplacement, Environnement environnement, int degat) throws ErreurInventairePlein {
		super(pv, x, y, vitesseDeplacement, environnement, TAILLE, degat);
		int lootFils = (int)Math.random()*101;
		int lootANTIPOISON = (int)Math.random()*101;
		if(lootFils <= TAUX_APPARITION_FILS)
			super.getInventaire().ajouter(new Fils(0));
		if(lootANTIPOISON <= TAUX_APPARITION_ANTIVENIN)
			super.getInventaire().ajouter(new Venin(1));
	}

	public Araignee(Environnement environnement, int degat) {
		super(environnement, TAILLE, degat);
		// TODO Auto-generated constructor stub
	}
	
	public void agir() {
		
	}

}
