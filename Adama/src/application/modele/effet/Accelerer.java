package application.modele.effet;

import application.modele.personnages.Personnage;

public class Accelerer extends Effet {
	
	public static final int POURCENTAGE_ACCELERATION = 75;
	public static final int DUREE = 8823; // correspond a 2 munites 30
	
	public Accelerer (Personnage perso) {
		super(DUREE, perso);
	}
	
	@Override
	public void appliquerEffet() {
		super.getPerso().ajouterEffet(this);
		
		
	}

}
