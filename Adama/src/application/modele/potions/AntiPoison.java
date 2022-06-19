package application.modele.potions;

import application.modele.personnages.Joueur;

public class AntiPoison extends Potion {

	public AntiPoison(Joueur joueur) {
		super(joueur);
	}

	@Override
	public void utiliser(int val) {

	}
}
