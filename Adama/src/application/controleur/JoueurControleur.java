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
				if (perso.getSaut())
					perso.sauterEnDirection(false);
				else
					perso.gauche();
				break;
			case "d":
				persoVue.orrientationSpriteDroite();
				if (perso.getSaut())
					perso.sauterEnDirection(true);
				else
					perso.droite();
				break;
			case "z":
				if(!perso.touchePasY(false))
					perso.sauter();
				break;
			case "s":
				break;
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
}
