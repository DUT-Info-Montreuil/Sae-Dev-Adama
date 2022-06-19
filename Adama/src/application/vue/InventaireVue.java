package application.vue;

import javafx.scene.image.Image;

public class InventaireVue {
	
	public InventaireVue() {
		
	}
	
	/**
	 * cette méthode renvoie l'image correspondant à la class de ajout
	 * @param nomClass
	 * @return
	 */
	public Image chargerImage(String nomClass) {
		Image image = null;
		String chemin = "ressource/";
		String extention = ".png";
		switch (nomClass) {
		
		//chargement Ressource
		case "Pierre":
			image = new Image(chemin + "pierreIcone" + extention);
			break;
		case "Terre":
			image = new Image(chemin + "terreIcone" + extention);
			break;
		case "Bois":
			image = new Image(chemin + "boisIcone" + extention);
			break;
		case "PlanteMedicinal":
			image = new Image(chemin + "planteMedicinaleIcone" + extention);
			break;
		case "PlanteHercule":
			image = new Image(chemin + "planteHerculeIcone" + extention);
			break;
		case "PlanteDeNike":
			image = new Image(chemin + "planteNikéIcone" + extention);
			break;
		
		//chargement outils
		case "Hache":
			image = new Image(chemin + "Hache" + extention);
			break;
		case "Pioche":
			image = new Image(chemin + "Pioche" + extention);
			break;
		case "Pelle":
			image = new Image(chemin + "Pelle" + extention);
			break;
		case "Seau":
			image = new Image(chemin + "seau_vide" + extention);
			break;
		
		//chargement Potion
		case "PotionDegat":
			image = new Image(chemin + "potionDegat" + extention);
			break;
		case "PotionVie":
			image = new Image(chemin + "potionVie" + extention);
			break;
		case "PotionVitesse":
			image = new Image(chemin + "potionVitesse" + extention);
			break;
		case "AntiPoison":
			image = new Image(chemin + "potionAntipoison" + extention);
			break;
		
		//Chargement Arme
		case "Epee":
			image = new Image(chemin + "epee" + extention);
			break;
		case "Arc":
			image = new Image (chemin + "arc" + extention);
			break;
		case "Fleche":
			image = new Image(chemin + "fleche" + extention);
			break;
		default:
			break;
		}
		return image;
	}
}