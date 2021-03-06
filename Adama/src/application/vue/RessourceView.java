package application.vue;

import application.modele.Carte;
import application.modele.Environnement;
import application.modele.ressources.Bois;
import application.modele.ressources.Pierre;
import application.modele.ressources.Ressource;
import application.modele.ressources.Terre;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RessourceView extends ImageView {

	private Ressource resource;
	private Environnement env;

	public RessourceView(Ressource res, Environnement env) {
		this.resource=res;
		this.env=env;
		choixTuile(blockPourVal(this.env.getCarte().getBlockMap().indexOf(resource), Carte.LARGEUR));
	}
	
	public int blockPourVal(int indice, int largeur) {
		int val;
		if (resource==null)
			val=0;
		else if (resource instanceof Terre && indice>largeur && env.getCarte().getBlockMap().get(indice-largeur)==null)
			val=1;				
		else if (resource instanceof Pierre)
			val=3;
		else if(resource instanceof Bois)
			val = 4;
		else
			val=2;
		return val;
	}
	public void choixTuile(int val) {
		Image img = null;
		switch(val){
		case 0:
			img = new Image("ressource/bleu.jpeg");
			break;
		case 1:
			img = new Image("ressource/herbe.jpeg");
			break;
		case 2:
			img = new Image("ressource/marron.jpeg");
			break;
		case 3:
			img = new Image("ressource/Granit.jpeg");
			break;
		case 4:
			img = new Image("ressource/blocBois.jpg");
			break;
		default:
			System.out.println("La valeur" + val + " ne correpond à aucune tuiles !");
//			img = new Image("ressource/bleu.jpeg");
			break;
		}
		super.setImage(img);
	}
}
