package application.modele.potions;

import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.personnages.Joueur;

public class PotionVie extends Potion {
	
	public static final int NOMBRE_PV_RESTAURER = 3;
	
	public PotionVie(Joueur joueur) {
		super(joueur);
	}

	@Override
	public void utiliser(int val) {
		super.getJoueur().incrementerPv(NOMBRE_PV_RESTAURER);
		getJoueur().getInventaire().getItems().remove(this);
	}

}
