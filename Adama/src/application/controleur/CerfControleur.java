package application.controleur;



import application.modele.Carte;
import application.modele.Environnement;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.personnages.Cerf;
import application.vue.CerfVue;

public class CerfControleur {

	private Cerf cerf;
	private CerfVue cerfVue;
	private boolean saut;
	private int tempsSaut;

	public CerfControleur(Cerf cerf2, CerfVue cerfVue2) {
		this.cerf=cerf2;
		this.cerfVue=cerfVue2;
		saut = false;
		tempsSaut = 0;
	}

	

	public void agir() throws ErreurObjetIntrouvable {
		if(cerf.estPrèsDuJoueur(Carte.TAILLE_BLOC * 5, 32)) {
			System.out.println("cerf : " + cerf.ouSeTrouveLeJoueur());
			if(cerf.ouSeTrouveLeJoueur()) { // si le joueur se trouve à sa droite
				cerfVue.orrientationSpriteGauche();
				cerf.gauche();
				if(cerf.touchePasX(false)) {
					cerf.sauter();
				}
			}
			
			
			else {
				cerfVue.orrientationSpriteDroite();
				cerf.droite();
				if(!cerf.touchePasX(true)) {
					cerf.sauter();
				}
			}
			
			
		}
	}


	
	/*if(cerf.ouSeTrouveLeJoueur()) {// si le joueur se trouve à sa droite
				cerfVue.orrientationSpriteGauche();
				cerf.gauche();
				if(!cerf.toucheX(true)) {
					cerf.sauter();
				}
			}
			else {
				cerfVue.orrientationSpriteDroite();
				cerf.droite();
				if(!cerf.toucheX(false)) {
					cerf.sauter();
				}
			 } */

	public boolean isSaut() {
		return saut;
	}

	public void setSaut(boolean saut) {
		this.saut = saut;
	}

	public int getTempsSaut() {
		return tempsSaut;
	}
	
	public void incremterTempsSaut() {
		this.tempsSaut+=1;
	}
	
	public void reinisialiseTempsSaut() {
		this.tempsSaut = 0;
	}

}
