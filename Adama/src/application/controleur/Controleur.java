package application.controleur;



import java.net.URL;
import java.util.ResourceBundle;

import application.modele.Carte;
import application.modele.Environnement;
import application.modele.Item;
import application.modele.armes.Epee;
import application.modele.exception.ErreurInventairePlein;
import application.modele.outils.Hache;
import application.modele.outils.Pelle;
import application.modele.outils.Pioche;
import application.modele.outils.Sceau;
import application.modele.personnages.Joueur;
import application.modele.personnages.Personnage;
import application.modele.ressources.Ressource;
import application.vue.EnvironnementVue;
import application.vue.JoueurVue;
import application.modele.personnages.Pnj;
import application.vue.PersonnageVue;
import application.vue.RessourceView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

public class Controleur implements Initializable{

	@FXML
	private Pane plateau;
	@FXML
	private TilePane carte;
	@FXML
	private Button boutonInventaire;
    @FXML
    private Label nbPVResant;
    @FXML
	private TilePane inventaire;

	private Timeline gameLoop;
	private int temps;
	private InventaireControleur invControleur;
	private Joueur perso;
	private JoueurVue persoVue;
	private JoueurControleur persoControleur;
	private Environnement env;
	private EnvironnementVue envVue;
	private ListChangeListener<Ressource> listResssourceListener;
	private ListChangeListener<Personnage> listPersonnageListener;
	private PersonnageVue nouveauPnjVue;
	private Sceau seau;	
	private IaControleur ia;

	@FXML
	void ouvrirInventaire(ActionEvent event) {
		ouvrirInventaire();
	}

	private void ouvrirInventaire() {
		System.out.println("Bonjour");
		if(!inventaire.isVisible()) {
			inventaire.setDisable(false);
			inventaire.setVisible(true);
		}
		else {
			inventaire.setDisable(true);
			inventaire.setVisible(false);
		}
	}

	@FXML
	void sourisPresse(MouseEvent event) {
		String click = event.getButton().name();
		int x = (int) event.getSceneX();
		int y = (int) event.getSceneY();
		Ressource cible = env.getCarte().emplacement(x, y);
		if (cible==null) {
			int indiceDansMap = (x/Carte.TAILLE_BLOC) + ((y/Carte.TAILLE_BLOC) * Carte.LARGEUR);
			persoControleur.sourisPresse(click, indiceDansMap);;
		}
		else {
			persoControleur.sourisPresse(click, env.getCarte().getBlocMap().indexOf(cible));
		}
	}

	@FXML
	void equiper(MouseEvent event) {
		try {
			ImageView ev = (ImageView) event.getTarget();
			int indiceDansInventaire = Integer.parseInt(ev.getId());
			Item objetAEquiper = perso.getInventaire().getItem(indiceDansInventaire);
			String newStyle = "-fx-background-color: #bbbbbb;-fx-border-style:solid;-fx-border-color:red;-fx-border-width:5px;";
			String oldStyle = "-fx-background-color: #bbbbbb;-fx-border-style:none;-fx-border-width:0px;";
			for (Node a : inventaire.getChildren())
				if(a.getStyle().equals(newStyle)) {
					a.setStyle(oldStyle);
					a.applyCss();
				}
			if(objetAEquiper.equals(perso.getObjetEquiper())) {
				perso.desequiper();
			}
			else {
				perso.equiper(objetAEquiper);
				ev.getParent().setStyle(newStyle);
				ev.getParent().applyCss();
			}

		}catch(Exception e) {
			System.err.println("Merci de ne pas cliquer sur le bord gris claire");
		}

	}

	@FXML
	void touchePresse(KeyEvent event) throws ErreurInventairePlein {
		String touchePresse = event.getCode().toString().toLowerCase();
		/*
		 * TODO
		 * Mettre un switch pour gérer les action qui nécessite un wait (ex: pause avec echap)
		 * et en default persoControleur.touchePresse(touchePresse)
		 */

		//		System.out.println(touchePresse);
		switch (touchePresse) {
		case "e":
			ouvrirInventaire();
			break;						
		default:
			persoControleur.touchePresse(touchePresse);
			break;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			env =new Environnement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		envVue = new EnvironnementVue(env, carte);

///////////List Listener

	//////////Bloc de la carte
		listResssourceListener = (cs -> {
			while(cs.next()) {
				int indiceBloc;
				for (Ressource ancien : cs.getRemoved()) {
					if (ancien!=null) {
						indiceBloc = ancien.getIndice();
						carte.getChildren().set(indiceBloc, new RessourceView(null, env));
					}
				}
				for (Ressource nouveau : cs.getAddedSubList()) {
					if (nouveau != null) {
						indiceBloc = nouveau.getIndice();
						carte.getChildren().set(indiceBloc, new RessourceView(nouveau, env));
					}
				}
			}});

	//////////Personnages dans l'environnement
		listPersonnageListener = (pc -> {
			while(pc.next()) {
				for (Personnage mort : pc.getRemoved()) {
					this.plateau.getChildren().remove(mort);
				}
				for (Personnage nouveau : pc.getAddedSubList()) {
					if (nouveau instanceof Joueur) {
						perso = (Joueur) nouveau;
						persoVue = new JoueurVue();
						this.plateau.getChildren().add(persoVue.getSprite());
						persoControleur = new JoueurControleur((Joueur)nouveau, persoVue);
						persoVue.getSprite().xProperty().bind(nouveau.xProperty());
						persoVue.getSprite().yProperty().bind(nouveau.yProperty());
						persoVue.getSprite().setFitWidth(nouveau.getTaille()[0]*Carte.TAILLE_BLOC);
						persoVue.getSprite().setFitHeight(nouveau.getTaille()[1]*Carte.TAILLE_BLOC);
					}
					else {
						nouveauPnjVue = new PersonnageVue(nouveau.getClass().getSimpleName());
						this.plateau.getChildren().add(nouveauPnjVue.getSprite());
						ia = new IaControleur((Pnj)nouveau, nouveauPnjVue);
						nouveauPnjVue.getSprite().xProperty().bind(nouveau.xProperty());
						nouveauPnjVue.getSprite().yProperty().bind(nouveau.yProperty());
						nouveauPnjVue.getSprite().setFitWidth(nouveau.getTaille()[0]*Carte.TAILLE_BLOC);
						nouveauPnjVue.getSprite().setFitHeight(nouveau.getTaille()[1]*Carte.TAILLE_BLOC);
					}
				}
			}});

	//////////Ajout des listener aux deux liste de l'environement
		env.getCarte().getBlocMap().addListener(listResssourceListener);
		env.getPersonnages().addListener(listPersonnageListener);

		env.initJeu();


///////////Création du menu

	////////Ajout des PV et bind au Sprite du Joueur
		nbPVResant.textProperty().bind(perso.pvProperty().asString());
		invControleur = new InventaireControleur(inventaire);
		perso.getInventaire().getItems().addListener(invControleur);

		Carte carte = env.getCarte();
		seau = new Sceau(carte, perso);
		try {
			perso.getInventaire().ajouter(new Hache(carte,perso));
			perso.getInventaire().ajouter(new Pelle(carte,perso));
			perso.getInventaire().ajouter(new Pioche(carte,perso));
			perso.getInventaire().ajouter(seau);
			perso.getInventaire().ajouter(new Epee());
		} catch (ErreurInventairePlein e) {
			System.out.println("Plein");
		}
		/*
		 * Test
		 */

////////////Lancement du Jeu
	
	////////////Gameloop
		initAnimation();
		gameLoop.play();
	}

	private void initAnimation() {
		gameLoop = new Timeline();
		temps=0;
		gameLoop.setCycleCount(Timeline.INDEFINITE);

		
		KeyFrame kf = new KeyFrame(Duration.seconds(0.017),
				(ev -> {
					env.tourDejeu();
					if (temps%Sceau.getTempsRemplissage()==0 && !seau.EstRempli() && temps!=0) {
						seau.remplir();
					}
					temps++;
				}));
		gameLoop.getKeyFrames().add(kf);
	}
}