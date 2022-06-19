package application.controleur;

import application.modele.personnages.Pnj;
import application.vue.PersonnageVue;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class IaControleur {
	
	private Pnj pnj;
	private PersonnageVue pnjSprite;

	public IaControleur(Pnj pnj, PersonnageVue pnjSprite) {
		this.pnj = pnj;
		this.pnjSprite = pnjSprite; 
	}

	public Pnj getPnj() {
		return this.pnj;
	}
	
	public PersonnageVue getPersonnageVue () {
		return this.pnjSprite;
	}
	
	public void orienterPnjSprite() {
		if (this.pnj.getDirection())//Orienter vers la droite
			this.pnjSprite.orrientationSpriteDroite();
		else
			this.pnjSprite.orrientationSpriteGauche();
			
	}
}
