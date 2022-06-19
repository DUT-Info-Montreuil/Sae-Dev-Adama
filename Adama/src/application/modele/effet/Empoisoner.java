package application.modele.effet;

import application.modele.personnages.Personnage;

public class Empoisoner extends Effet {

	public static final int DUREE = 3529; // correspond a 1 minute
	public static final int INTERVALLE_DEGAT = 588; //correpond au temps entre lequelle on prend des degats (=1 minute)
	
	public Empoisoner(Personnage perso) {
		super(DUREE, perso);
	}


	@Override
	public void appliquerEffet() {
		getPerso().ajouterEffet(this);
		
	}

}
