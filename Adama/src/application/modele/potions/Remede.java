package application.modele.potions;

import application.modele.personnages.Joueur;

public class Remede extends Potion {

	public Remede(Joueur joueur) {
		super(joueur);
	}

	public void utiliser(int val) {
		getJoueur().SupprimerEffet(0);
		getJoueur().getInventaire().getItems().remove(this);
	}
}
