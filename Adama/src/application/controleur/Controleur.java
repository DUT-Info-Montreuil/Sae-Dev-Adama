package application.controleur;



import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.modele.Carte;
import application.modele.Environnement;
import application.modele.Item;
import application.modele.effet.Accelerer;
import application.modele.effet.Empoisoner;
import application.modele.effet.Ralentir;
import application.modele.effet.Renforcer;
import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.ErreurObjetCraftable;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.outils.Hache;
import application.modele.outils.Pelle;
import application.modele.outils.Pioche;
import application.modele.outils.Sceau;
import application.modele.personnages.Joueur;
import application.modele.personnages.Personnage;
import application.modele.potions.Potion;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
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
    private Label nbPVResant;
    @FXML
	private TilePane inventaire;
	@FXML
	private Label nbPVMax;
	@FXML
	private TilePane craft;
	
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
	private IaControleur ia;
	private ArrayList<IaControleur> iaControleurs;
	private PersonnageVue nouveauPnjVue;
	private Sceau sceau;

	@FXML
	void ouvrirInventaire(ActionEvent event) {
		ouvrirInventaire();
	}

	private void ouvrirInventaire() {
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
			ImageView image = (ImageView) event.getTarget();
			if(image!=null) {
				int indiceDansInventaire = Integer.parseInt(image.getId());
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
					if(objetAEquiper instanceof Potion) {
						perso.utiliserMain(-1);
					}
					else {
						image.getParent().setStyle(newStyle);
						image.getParent().applyCss();
					}
				}
			}
		}catch(Exception e) {
			System.err.println("Merci de ne pas cliquer sur le bord gris claire");
		}
	}

	@FXML
	void craft(MouseEvent event) {
		String objetAFrabriquer = ((ImageView) event.getTarget()).getId(); //J'ai mis comme Id le type de l'objet à fabriquer
		try {
			perso.craft(objetAFrabriquer);
			if(objetAFrabriquer.equals("Sceau")) 
				//Si on est ici c'est que le sceau a ete fabriquer. C'est donc le dernier item de l'inventaire
				this.sceau = (Sceau) perso.getInventaire().getItem(perso.getInventaire().getTaille()-1); 
			//Je le récupere pour savoir si c'est il est plein ou non
			
		} catch (ErreurObjetCraftable e) {
			Alert a = new Alert(AlertType.WARNING, e.getMessage(), ButtonType.CLOSE);
			a.setTitle(objetAFrabriquer + " non fabricable !");
			a.setHeaderText("Vous n'avez pas les materiaux neccessaire au craft de " + objetAFrabriquer);
			a.getDialogPane().setPrefWidth(400);
			a.show();
		} catch (ErreurInventairePlein e) {
			Alert a = new Alert(AlertType.WARNING, e.getMessage(), ButtonType.CLOSE);
			a.setTitle("Inventaire Plein");
			a.setHeaderText("Votre Inventaire est plein");
			a.getDialogPane().setPrefWidth(400);
			a.show();
		} catch (ErreurObjetIntrouvable e) {
			
		}
	}

	@FXML
	void toucheRelache(KeyEvent event) throws ErreurInventairePlein {
		String touchePresse = event.getCode().toString().toLowerCase();
		persoControleur.toucheRelache(touchePresse);
	}
		
	@FXML
	void touchePresse(KeyEvent event) throws ErreurInventairePlein {
		String touchePresse = event.getCode().toString().toLowerCase();
		switch (touchePresse) {
		case "e":
			ouvrirInventaire();
			event.consume();
			break;
		case "f":
			ouvrirCraft();
			event.consume();
			break;
		default:
			persoControleur.touchePresse(touchePresse);
			break;
		}
	}

	public void ouvrirCraft() {
		if(!craft.isVisible()) {
			craft.setDisable(false);
			craft.setVisible(true);
		}
		else {
			craft.setDisable(true);
			craft.setVisible(false);
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
		iaControleurs = new ArrayList<>();
		
		
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
						iaControleurs.add(ia);
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
		try {
			perso.getInventaire().ajouter(new Hache(carte,perso));
			perso.getInventaire().ajouter(new Pelle(carte,perso));
			perso.getInventaire().ajouter(new Pioche(carte,perso));
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
					//Au bout de temps de remplissage si le sceau est vide il se rempli
					if (sceau != null && temps%Sceau.TEMPS_REMPLISSAGE==0 && !sceau.EstRempli() && temps!=0) {
						sceau.remplir();
						System.err.println("J'ai rempli mon sceau");
					}
					if(perso.getEffets().get(0)!=null) { //Si le joueur est empoisonner
						if(temps%Empoisoner.INTERVALLE_DEGAT==0&&temps!=0)
							perso.degat();
						if(temps%Empoisoner.DUREE == 0 &&temps!=0)
							perso.SupprimerEffet(0);
					}
					if(perso.getEffets().get(1)!=null && temps%Ralentir.DUREE== 0 && temps!=0) { //Si le joueur est ralenti
							perso.SupprimerEffet(1);
					}
					if(perso.getEffets().get(2)!=null && temps%Renforcer.DUREE== 0 && temps!=0) { //Si le joueur a un bonus d'attaque
						perso.SupprimerEffet(2);
					}
					if(perso.getEffets().get(3)!=null && temps%Accelerer.DUREE== 0 && temps!=0) { //Si le joueur a un bonus de vitesse
						perso.SupprimerEffet(3);
					}
					temps++;
				}));
		gameLoop.getKeyFrames().add(kf);
	}
}