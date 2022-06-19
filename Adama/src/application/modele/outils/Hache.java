package application.modele.outils;

import application.modele.Carte;
import application.modele.exception.ErreurInventairePlein;
import application.modele.personnages.Joueur;
import application.modele.ressources.Bois;

public class Hache extends Outil {

	private final static int DEGATS=10;

	/**
	 * Créer un outil hache
	 * @param env l'environnement dans lequel elle se trouve
	 */
	public Hache(Carte carte, Joueur joueur) {
		super(carte, joueur, DEGATS);
	}

	/**
	 * Permet d'utiliser la hache elle fait des dégat au bloc de Bois viser et ceux au-dessus
	 * @param lieu l'indice où sont donné les coups de hache
	 * @throws ErreurInventairePlein 
	 */
	@Override
	public void utiliser(int lieu) throws ErreurInventairePlein {
		int largeur=Carte.LARGEUR;
		try {
			if (super.getCarte().emplacement(lieu-largeur) instanceof Bois)
				this.utiliser(lieu-largeur);

		} catch(IndexOutOfBoundsException e) {
			System.err.println("Pas du bois");
		}//TODO faire en sorte que l'on récupère plus que un bois si récursivité arrive
		if (super.getCarte().emplacement(lieu) instanceof Bois)
			super.getJoueur().getInventaire().ajouter(super.getCarte().attaquerBloc(lieu, DEGATS));
	}
}