package application.modele.outils;

import application.modele.Carte;
import application.modele.Environnement;
import application.modele.exception.ErreurInventairePlein;
import application.modele.personnages.Joueur;
import application.modele.ressources.Ressource;
import application.modele.ressources.Terre;

public class Pelle extends Outil {

	private final static int DEGATS=10;
//	private int x;
//	private int y;
	
	/**
	 * Créer un outil pelle
	 * @param env l'environnement dans lequel elle se trouve
	 */
	public Pelle(Carte carte, Joueur joueur) {
		super(carte, joueur, DEGATS);
	}
	
	/**
	 * Permet d'utiliser la pelle sur le bloc visé
	 * @param lieu indice du bloc visé
	 * @throws ErreurInventairePlein 
	 */
	@Override
	public void utiliser(int lieu) throws ErreurInventairePlein {
		if (super.getCarte().emplacement(lieu) instanceof Terre)
			super.getJoueur().getInventaire().ajouter(super.getCarte().attaquerBloc(lieu, DEGATS));
	}
}
