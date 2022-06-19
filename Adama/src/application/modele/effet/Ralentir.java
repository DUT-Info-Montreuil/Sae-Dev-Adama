package application.modele.effet;

import application.modele.personnages.Personnage;

public class Ralentir extends Effet {

	public static final int DUREE = 6000; // correspond a 1 minute 30
	
	public Ralentir(Personnage personnage) {
		super(DUREE, personnage);
	}

	@Override
	public void appliquerEffet() {
		super.getPerso().ajouterEffet(this);
		
	}

}
