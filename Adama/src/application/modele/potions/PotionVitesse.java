package application.modele.potions;

import application.modele.effet.Accelerer;
import application.modele.personnages.Joueur;

public class PotionVitesse extends Potion {
	
	private final Accelerer effet = new Accelerer(getJoueur());

	public PotionVitesse(Joueur joueur) {
		super(joueur);
	}

	@Override
	public void utiliser(int val) {
		effet.appliquerEffet();
		getJoueur().getInventaire().getItems().remove(this);
	}
}
