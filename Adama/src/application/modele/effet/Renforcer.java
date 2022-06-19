package application.modele.effet;

import application.modele.personnages.Personnage;

public class Renforcer extends Effet {

	private static final int POURCENTAGE_RENFORCEMENT = 75;
	public static final int DUREE = 8823; // correspond a 2 munites 30
	
	public Renforcer(Personnage perso) {
		super(DUREE, perso);
	}

	@Override
	public void appliquerEffet() {
		super.getPerso().ajouterEffet(this);
	}

	public int getPourcenetageRenforcement() {
		return POURCENTAGE_RENFORCEMENT;
	}
}
