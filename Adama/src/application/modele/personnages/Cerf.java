package application.modele.personnages;

import application.modele.Carte;
import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.ressources.Terre;

public class Cerf extends Pnj {


	public static final int[] TAILLE = {2,2};
	private static final int VITESSE = 10;
	private static final int PV = 10;
	private static final Inventaire INVENTAIRE = new Inventaire(new Terre(0));
	private static final int HAUTEUR_SAUT = 14;

	public Cerf(Environnement environnement) {
		super(PV, 10, 10, VITESSE, environnement, INVENTAIRE, HAUTEUR_SAUT, TAILLE);
	}

	public Cerf(int x,int y,Environnement environnement) {
		super(PV, x, y, VITESSE, environnement, INVENTAIRE, HAUTEUR_SAUT, TAILLE);
	}

	public void agir() throws ErreurObjetIntrouvable  {
		if (this.estPrèsDuJoueur(Carte.TAILLE_BLOC*5, TAILLE[1]*Carte.TAILLE_BLOC)) {
			if(this.ouSeTrouveLeJoueur()) {// si le joueur se trouve à sa droite
				this.gauche();
				if(!this.touchePasX(false))
					this.sauter();
			}
			else {
				this.droite();
				if(!this.touchePasX(true))
					this.sauter();
			}
		}
	}
}
