package application.vue;

import javafx.geometry.NodeOrientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PersonnageVue {

	private ImageView sprite;


	public PersonnageVue(String nom) {
		this.sprite = new ImageView("ressource/"+nom+".png");
	}

	public ImageView getSprite() {
		return sprite;
	}

	public void setSprite(String image) {
		this.sprite.setImage(new Image(image));
	}

	public void orrientationSpriteGauche() {
		if (sprite.effectiveNodeOrientationProperty().get()==NodeOrientation.RIGHT_TO_LEFT)
			sprite.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
	}

	public void orrientationSpriteDroite() {
		if (sprite.effectiveNodeOrientationProperty().get()==NodeOrientation.LEFT_TO_RIGHT)
			sprite.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	}

}