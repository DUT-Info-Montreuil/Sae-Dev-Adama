package application.controleur;



import java.net.URL;
import java.util.ResourceBundle;

import application.modele.Carte;
import application.modele.Checkpoint;
import application.modele.Environnement;
import application.modele.Item;
import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.outils.Hache;
import application.modele.outils.Pelle;
import application.modele.outils.Pioche;
import application.modele.outils.Sceau;
import application.modele.personnages.Cerf;
import application.modele.personnages.Joueur;
import application.modele.personnages.Personnage;
import application.modele.personnages.Slime;
import application.modele.ressources.Ressource;
import application.modele.ressources.Terre;
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
	private Cerf cerf;
	private Sceau seau;
	private Slime monstre;
	
	private PersonnageVue cerfVue;
	private PersonnageVue monstreVue;
	
	
	private boolean toucherM = false;
	private int tempsToucherM = 0 ;
	
	private boolean toucherJ = false;
	private int tempsToucherJ = 0 ;
	
	private boolean toucherE = false;
	private int tempsToucherE = 0 ;
	
	private Personnage ennemie;
	
	
	


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
			int indiceDansMap = (x/Carte.TAILLE_BLOCK) + ((y/Carte.TAILLE_BLOCK) * Carte.LARGEUR);
			persoControleur.sourisPresse(click, indiceDansMap);
		}
		else {
			persoControleur.sourisPresse(click, env.getCarte().getBlockMap().indexOf(cible));//a voir si problème avec click sur bouton
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
				
			
		
				case "m":
					monstre.meurt();
					monstreVue.getSprite().setVisible(false);
					System.out.println("You Win");
					break;
					
				case "p":
					if(monstre.estMort()) {
						monstre.setPv(15);
						monstre.setX(perso.getCheckpoint().getX());
						monstre.setY(perso.getCheckpoint().getY());
						monstreVue.getSprite().setVisible(true);
						System.out.println("Respawn du slime");
						System.out.println("You Win");
					}
					break;
					
		
						
						
					
						
					
				case "l":
					cerf.meurt();
					cerfVue.getSprite().setVisible(false);
					System.out.println("Good Meal");
					break;
					
				case "o":
					if(cerf.estMort()) {
						cerf.setPv(15);
						cerf.setX(perso.getCheckpoint().getX());
						cerf.setY(perso.getCheckpoint().getY());
						System.out.println("Respawn du cerf");
						break;
					}
						
					
						
					case "i":
						if(perso.estMort()) {
							perso.incrementerPv(7);
							perso.teleporterToCheckpoint();
							persoVue.getSprite().setVisible(true);
							System.out.println("Respawn");
						}
						break;
					
					case "k":
						perso.meurt();
						persoVue.getSprite().setVisible(false);
						System.out.println("Game Over");
						break;
						
				
						
					case "w":
						perso.equiper(new Terre(0));
						
					case "a":
						toucherE = true;
						
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
						persoVue.getSprite().setFitHeight(64);
						persoVue.getSprite().setFitWidth(32);
					}
					else {
						nouveauPnjVue = new PersonnageVue(nouveau.getClass().getSimpleName());
						this.plateau.getChildren().add(nouveauPnjVue.getSprite());
						IaControleur p = new IaControleur((Pnj)  nouveau, nouveauPnjVue);
						nouveauPnjVue.getSprite().xProperty().bind(nouveau.xProperty());
						nouveauPnjVue.getSprite().yProperty().bind(nouveau.yProperty());
						nouveauPnjVue.getSprite().setFitHeight(nouveau.getTaille()[0]*Carte.TAILLE_BLOCK);
						nouveauPnjVue.getSprite().setFitHeight(nouveau.getTaille()[1]*Carte.TAILLE_BLOCK);
					}
				}
			}});

	//////////Ajout des listener aux deux liste de l'environement
		env.getCarte().getBlockMap().addListener(listResssourceListener);
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
			perso.getInventaire().ajouter(new Hache(carte, perso));
			perso.getInventaire().ajouter(new Pelle(carte, perso));
			perso.getInventaire().ajouter(new Pioche(carte, perso));
			perso.getInventaire().ajouter(seau);
		} catch (ErreurInventairePlein e) {
			System.out.println("Plein");
		}

////////////Gameloop
		initAnimation();
		gameLoop.play();
	}

	private void initAnimation() {
		gameLoop = new Timeline();
		temps=0;
//		timeRespawn = -1;
//		valRecul = 0;
		gameLoop.setCycleCount(Timeline.INDEFINITE);

		
		KeyFrame kf = new KeyFrame(Duration.seconds(0.017),
				(ev -> {
					env.tourDejeu();
					if (temps%Sceau.getTempsRemplissage()==0 && !seau.EstRempli() && temps!=0) {
						seau.remplir();
					}
					if(temps==100)
						System.out.println("ok");
					env.getPersonnages().forEach(pnj -> {
						if (pnj instanceof Pnj)
							try {
								((Pnj)pnj).agir();
							} catch (ErreurObjetIntrouvable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					});
					env.getPersonnages().forEach(pj -> pj.gravite());
//					if(!cerf.estMort()) {
//						try {
//							this.cerfControleur.agir();
//						} catch (ErreurObjetIntrouvable e1) {
//							// TODO Auto-generated catch block
//							System.out.println("Pas d'ennemie");
//						}
//					}
////
////							
////					
//					try {
//						if(!monstre.estMort()) {
//							monstreControleur.agir();
//							if(monstreControleur.getTempsSaut() < 8 &&monstreControleur.isSaut()) {
//			//						perso.monter(4);
//									System.out.println(2);
//									monstre.monter(4);
//									monstre.translationX(-2);
//									monstreControleur.incremterTempsSaut();
//							}
//					if(temps==100)
//						System.out.println("ok");
//					
//					else if(temps==251)
//						System.out.println("Toto");
//					else if(temps>300)
//						perso.equiper(new Terre(0));
					
//					if(!cerf.estMort()) {
//						try {
//							this.cerf.agir();
//						} catch (ErreurObjetIntrouvable e1) {
//							// TODO Auto-generated catch block
//							System.out.println("Pas d'ennemie");
//						}
//					}
//
//							
//							if(monstreControleur.getTempsSaut() >= 8) {
//								if(monstre.ouSeTrouveLeJoueur()) {
//									valRecul = -2;
//								}
//								else {
//									valRecul = 2;
//								}
//								
//								System.out.println(valRecul);
//								for (int k = 0 ; k < 32 ; k++) {
//									perso.translationX(valRecul);
//								}
//								
//								monstreControleur.reinisialiseTempsSaut();
//								monstreControleur.setSaut(false);
//								this.toucherJ = true;
//							}
//							
//							
//							if(monstreControleur.getTempsSaut()== 32 && monstreControleur.isSaut()) {
//								System.out.println("0k");
//								monstreControleur.setSaut(false);
//								monstreControleur.reinisialiseTempsSaut();
//							}
//						}
//					}catch (ErreurObjetIntrouvable  e) {
//						System.out.println("Pas d'ennemie");
//					}
//					
//					if(perso.estMort() && timeRespawn ==  -1) {
//						persoVue.getSprite().setVisible(false);
//						System.out.println("Game over");
//						timeRespawn =  0;
//					}
//					
//					if(timeRespawn > 1) {
//						perso.incrementerPv(7);
//						perso.teleporterToCheckpoint();
//						persoVue.getSprite().setVisible(true);
//						System.out.println("Respawn");
//						timeRespawn =  -1;
//					try {
//						if(!monstre.estMort()) {
//							monstre.agir();
//							if(monstre.getTempsSaut() < 8 && monstre.isSaut()) {
//			//						perso.monter(4);
//									System.out.println(2);
//									monstre.monter(4);
//									monstre.translationX(-2);
//									monstreControleur.incremterTempsSaut();
//							}
//							
//							if(monstreControleur.getTempsSaut() >= 8) {
//								if(monstre.ouSeTrouveLeJoueur()) {
//									valRecul = -2;
//								}
//								else {
//									valRecul = 2;
//								}
//								
//								System.out.println(valRecul);
//								for (int k = 0 ; k < 32 ; k++) {
//									perso.translationX(valRecul);
//								}
//								monstre.getEnvironnement().getJoueur().decrementerPv(2);
//								
//								monstreControleur.reinisialiseTempsSaut();
//								monstreControleur.setSaut(false);
//								this.toucherJ = true;
//							}
//							
//							
//							if(monstreControleur.getTempsSaut()== 32 && monstreControleur.isSaut()) {
//								System.out.println("0k");
//								monstreControleur.setSaut(false);
//								monstreControleur.reinisialiseTempsSaut();
//							}
//						}
//					}catch (ErreurObjetIntrouvable  e) {
//						System.out.println("Pas d'ennemie");
//					}
					
					if(perso.estMort() ) {
						persoVue.getSprite().setVisible(false);
						System.out.println("Game over");
						perso.incrementerPv(7);
						perso.teleporterToCheckpoint();
						persoVue.getSprite().setVisible(true);
						System.out.println("Respawn");
					}
					
					
					
//					if(!perso.estMort()) {
//						if(persoControleur.getTempsSaut()<30&&persoControleur.isSaut()) {
//								perso.monter(4);
//								//perso.sauter();
//								persoControleur.incremterTempsSaut();
//							}	
//							
//							
//							
//						else if(persoControleur.getTempsSaut()==30&&persoControleur.isSaut()) {
//							persoControleur.setSaut(false);
//							persoControleur.reinisialiseTempsSaut();
//						}
//					}
					if(tempsToucherM%2==0 && tempsToucherM <50 && toucherM) {
						monstreVue.getSprite().setVisible(false);
					}
					
					if(tempsToucherM%2 ==1 && tempsToucherM < 50 && toucherM) {
						if(toucherM) {
							monstreVue.getSprite().setVisible(true);
						}
					}
					
					if(tempsToucherJ%2==0 && tempsToucherJ < 50 && toucherJ) {
						persoVue.getSprite().setVisible(false);
					}
					
					if(tempsToucherJ%2 ==1 && tempsToucherJ < 50 && toucherJ) {
						if(toucherJ) {
							persoVue.getSprite().setVisible(true);
						}
					}
					
					if(toucherE && tempsToucherE == 0) {
						Personnage ennemie;
						try {
							System.out.println("Porter : " + perso.getArmeEquiper().getPorter());
							ennemie = perso.estPrèsDunPerso(perso.getArmeEquiper().getPorter() * Carte.TAILLE_BLOCK, 1);
							System.out.println("marche");
							if(ennemie.ouSeTrouveLeJoueur()) {
								ennemie.translationX(Carte.TAILLE_BLOCK  * 2);
							}
							else {
								ennemie.translationX(-Carte.TAILLE_BLOCK  * 2);
							}
							ennemie.decrementerPv(perso.getArmeEquiper().getDegat());
							System.out.println(ennemie.getPv());
							if(ennemie.estMort()) {
								if(ennemie instanceof Cerf) {
									cerfVue.getSprite().setVisible(false);
							
								}
								if(ennemie instanceof Slime) {
									monstreVue.getSprite().setVisible(false);
								}
							}
						}catch (ErreurObjetIntrouvable | ErreurInventairePlein e) {
							System.out.println("Pas d'ennemie");
						}

					}
					
					if(tempsToucherE == 1 && toucherE) {
						persoVue.setSprite("ressource/persoEpeeLever.png");
					}
					
					if(tempsToucherE >= 10 && toucherE) {
						persoVue.setSprite("ressource/persoEpeeRanger.png");
					}
					
					System.out.println("x : " + perso.getX() + "y : " + perso.getY());
					
					if(temps%2==0)

//					}
//					
//					if(!perso.estMort()) {
//						if(persoControleur.getTempsSaut()<30&&persoControleur.isSaut()) {
//								perso.monter(4);
//								//perso.sauter();
//								persoControleur.incremterTempsSaut();
//							}	
//							
//							
//							
//						else if(persoControleur.getTempsSaut()==30&&persoControleur.isSaut()) {
//							persoControleur.setSaut(false);
//							persoControleur.reinisialiseTempsSaut();
//						}
//					}
//					if(tempsToucherM%2==0 && tempsToucherM <50 && toucherM) {
//						monstreVue.getSprite().setVisible(false);
//					}
//					
//					if(tempsToucherM%2 ==1 && tempsToucherM < 50 && toucherM) {
//						if(toucherM) {
//							monstreVue.getSprite().setVisible(true);
//						}
//					}
//					
//					if(tempsToucherJ%2==0 && tempsToucherJ < 50 && toucherJ) {
//						persoVue.getSprite().setVisible(false);
//					}
//					
//					if(tempsToucherJ%2 ==1 && tempsToucherJ < 50 && toucherJ) {
//						if(toucherJ) {
//							persoVue.getSprite().setVisible(true);
//						}
//					}
//					
//					if(temps%2==0)
////						
//						for(Personnage personnage : env.getPersonnages()) {
//							if(!personnage.estMort()) {
//								personnage.gravite();
//							}
//							else if (!personnage.estEnDehorsMap()) {
//								personnage.meurt();
//								if(personnage instanceof Cerf) {
//									cerfVue.getSprite().setVisible(false);
//							
//								}
//								if(personnage instanceof Slime) {
//									monstreVue.getSprite().setVisible(false);
//								}
//								
//							}
//						}


//					temps++;
//					if(toucherM) {
//						tempsToucherM ++;
//					}
//					if(tempsToucherM> 50) {
//						toucherM = false;
//						tempsToucherM =0;
//					}
//					if(toucherJ) {
//						tempsToucherJ ++;
//					}
//					if(tempsToucherJ> 50) {
//						toucherJ = false;
//						tempsToucherJ =0;
//					}
//					if(timeRespawn >= 0) {
//						timeRespawn++;
//					}
//					
						for(Personnage personnage : env.getPersonnages()) {
							if(!personnage.estMort()) {
								personnage.gravite();
							}
							else if (!personnage.estEnDehorsMap(0,0)) {
								try {
									personnage.meurt();
								} catch (ErreurInventairePlein e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(personnage instanceof Cerf) {
									cerfVue.getSprite().setVisible(false);
							
								}
								if(personnage instanceof Slime) {
									monstreVue.getSprite().setVisible(false);
								}
								
							}
						}
					temps++;
					if(toucherM) {
						tempsToucherM ++;
					}
					if(tempsToucherM> 50) {
						toucherM = false;
						tempsToucherM =0;
					}
					
					if(tempsToucherE> perso.getArmeEquiper().getTempsRecharge() * 10 ) { // temps de recharge arme
						toucherE = false;
						tempsToucherE =0;
					}
					
					if(toucherE) {
						tempsToucherE ++;
					}
					
					
					if(toucherJ) {
						tempsToucherJ ++;
					}
					if(tempsToucherJ> 50) {
						toucherJ = false;
						tempsToucherJ =0;
					}
					
					
				})
				);
		gameLoop.getKeyFrames().add(kf);
	}
}