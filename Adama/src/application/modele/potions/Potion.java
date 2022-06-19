package application.modele.potions;

import application.modele.Item;
import application.modele.personnages.Joueur;

public abstract class Potion implements Item {
	private Joueur joueur;

	public Potion(Joueur joueur) {
		this.joueur = joueur;
	}

	public Joueur getJoueur() {
		return joueur;
	}
	
	
	
	
}
