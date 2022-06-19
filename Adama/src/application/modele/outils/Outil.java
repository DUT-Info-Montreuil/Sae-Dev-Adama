package application.modele.outils;

import application.modele.Carte;
import application.modele.Item;
import application.modele.personnages.Joueur;

/**
 * Est au-dessus de Hache, Pelle, Pioche
 * @author jberguig
 *
 */
public abstract class Outil implements Item{
	
	private Carte carte;
	private Joueur joueur;
	private int effet; //correspond au dégat infligé au bloc ou au temps de remplisage du sceau
	
	/**
	 * Crée un outil (soit une hache, soit une pelle, soit une pioche
	 * @param env L'environnement dans lequel elle setrouve et avec lequel elle intéragie.
	 * @param effet est la valeur nécessaire à l'utilisation(le temps, les dégats, la vitesse,...)
	 */
	public Outil(Carte carte, Joueur joueur, int effet) {
		this.carte=carte;
		this.joueur=joueur;
		this.effet=effet;
	}
	
	public Carte getCarte() {
		return carte;
	}
	
	public Joueur getJoueur() {
		return joueur;
	}
}
