package application.modele.outils;

import application.modele.Carte;
import application.modele.Environnement;
import application.modele.exception.ErreurInventairePlein;
import application.modele.personnages.Joueur;
import application.modele.ressources.Pierre;

public class Pioche extends Outil {

	private final static int DEGATS=10;
	
	/**
	 * Créer un outil pioche
	 * @param env l'environnement dans lequel elle se trouve
	 */
	public Pioche(Carte carte, Joueur joueur) {
		super(carte, joueur, DEGATS);
	}

	/**
	 * Permet d'utiliser la pioche sur le bloc visé
	 * @param lieu indice du bloc visé
	 * @throws ErreurInventairePlein 
	 */
	public void utiliser(int lieu) throws ErreurInventairePlein {
		if (super.getCarte().emplacement(lieu) instanceof Pierre)
			super.getJoueur().getInventaire().ajouter(super.getCarte().attaquerBloc(lieu, DEGATS));
	}
}
