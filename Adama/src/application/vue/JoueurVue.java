package application.vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class JoueurVue extends PersonnageVue {

	public JoueurVue() {
		super("perso");
	}
	
	public void sautVue(int temps) {
		if (temps<1) 
			this.getSprite().setImage(new Image("ressource/persoAccroupi.jpg"));
		else if (temps==10) 
			this.getSprite().setImage(new Image("ressource/perso.png"));
	}
}
