package application.modele.potions;

import application.modele.effet.Renforcer;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.personnages.Joueur;

public class PotionDegat extends Potion {
	
	private final Renforcer effet = new Renforcer(getJoueur());

	public PotionDegat(Joueur joueur) {
		super(joueur);
	}

	@Override
	public void utiliser(int val) {
		effet.appliquerEffet();
		getJoueur().getInventaire().getItems().remove(this);
	}
}