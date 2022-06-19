package application.controleur;

import application.modele.exception.ErreurInventairePlein;
import application.modele.armes.Epee;
import application.modele.personnages.Joueur;
import application.vue.JoueurVue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class JoueurControleur {

	private Joueur perso;
	private JoueurVue persoVue;
	private boolean messageDejaVu;

	public JoueurControleur(Joueur perso, JoueurVue persoVue) {
		this.perso=perso;
		this.persoVue=persoVue;
		this.messageDejaVu=false;
	}

	public void touchePresse(String touchePresse) {
		switch (touchePresse) {
			case "q":
				persoVue.orrientationSpriteGauche();
				perso.setEnDeplacement(true);
				perso.gauche();
				break;
			case "d":
				persoVue.orrientationSpriteDroite();
				perso.setEnDeplacement(true);
				perso.droite();
				break;
			case "z":
				if(!perso.touchePasY(false))
					if(perso.getEnDeplacement())
						perso.sauterEnDirection(perso.getDirection());
					else
						perso.sauter();
				break;
			case "s":
				break;
				
		
			
			case "c":
				Epee epee = new Epee();
				perso.equiper(epee);
				persoVue.setSprite("ressource/persoEpeeRanger.png");
				break;
				
			case "v":
				persoVue.setSprite("ressource/persoEpeeLever.png");
				
	
			}	
	}

	public void sourisPresse(String click, int emplacement) {
		switch (click) {
		case "PRIMARY":
			try {
				perso.utiliserMain(emplacement);
			} catch (ErreurInventairePlein e) {
				// TODO Alert
				if (!messageDejaVu) {
					Label message = new Label(e.getMessage());
					Alert a = new Alert(AlertType.WARNING, e.getMessage(), ButtonType.CLOSE);
					a.setTitle("Inventaire Plein");
					a.setHeaderText("Votre Inventaire est plein");
					a.getDialogPane().setPrefWidth(400);
					a.show();
					messageDejaVu=true;
				}
			}
			break;
		default:
			break;
		}
	}

	public void toucheRelache(String touchePresse) {
		switch (touchePresse) {
		case "q":
			perso.setEnDeplacement(false);
			perso.gauche();
			break;
		case "d":
			perso.setEnDeplacement(false);
			perso.droite();
			break;
		}
		
	}
}
