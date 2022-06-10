package application.modele.personnages;

import java.io.IOException;

import application.modele.Checkpoint;
import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.effet.Effet;
import application.modele.effet.Ralentir;
import application.modele.ressources.Bois;
import application.modele.ressources.Plante;
import application.modele.ressources.Ressource;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Classe abstraite qui est au-dessus de Joueur et de PNJ
 * Elle a trois attributs qui sont ecoutables : pvProperty, xProperty, yProperty
 * Elle a une vitesse de déplacement, une hauteur de saut, un environement, une taille(tab[0]=tailleEnX, tab[1]=tailleEnY)
 * Et un inventaire(les pnj en ont également un qui aura les drops que l'on récupérera en les tuant)
 * @author jberguig
 *
 */
public abstract class Personnage {

	private static int compteur = 0;
	private IntegerProperty pvProperty;
	private IntegerProperty xProperty;
	private IntegerProperty yProperty;
	private int vitesseDeplacement;
	private int id;
	private Environnement environnement;
	private Inventaire inventaire;
	private int hauteurSaut;
	private int hauteurMaxSaut;
	private int[] taille;
	private Checkpoint checkpoint;
	private boolean saut;

	private int longueurSaut;

	private ObservableList<Effet> effets;


	public Personnage(int pv, int x, int y, int vitesseDeplacement, Environnement environnement,Inventaire inventaire, int hauteurSaut, int[] taille, int longueurSaut, Checkpoint checkpoint){
		this.pvProperty = new SimpleIntegerProperty(pv);
		this.xProperty = new SimpleIntegerProperty(x);
		this.yProperty = new SimpleIntegerProperty(y);
		this.vitesseDeplacement = vitesseDeplacement;
		this.environnement = environnement;
		this.inventaire = inventaire;
		this.hauteurSaut = hauteurSaut;
		this.taille = taille;


		this.longueurSaut = longueurSaut;
		this.environnement.ajouter(this);

		this.hauteurMaxSaut = this.hauteurSaut;
		this.environnement.ajouter(this);


		this.checkpoint = checkpoint;
		this.id=compteur;
		compteur++;
		this.effets = FXCollections.observableArrayList();
		effets.addAll(null, null, null, null); //Chaque valeur correspond à un effet different
	}


	public Personnage(int pv, int x, int y, int vitesseDeplacement, Environnement environnement, int[] taille){
		this.pvProperty = new SimpleIntegerProperty(pv);
		this.xProperty = new SimpleIntegerProperty(x);
		this.yProperty = new SimpleIntegerProperty(y);
		this.vitesseDeplacement = vitesseDeplacement;
		this.environnement = environnement;
		this.inventaire = new Inventaire(20);
		this.hauteurSaut = 64;
		this.hauteurMaxSaut = this.hauteurSaut;
		this.taille = taille;
		this.environnement.ajouter(this);
		this.checkpoint = new Checkpoint(x,y,environnement);
		this.effets = FXCollections.observableArrayList();
		effets.addAll(null, null, null, null);
	}

	
	

	public void setCheckpoint(Checkpoint checkpoint) {
		this.checkpoint = checkpoint;
	}
	
	



	/**
	 * Diminue les PV
	 * @param degat nombre de pv perdue
	 */
	public void degat() {
		this.setPv(this.getPv() - 1);
	}

	public void decrementerPv(int degat) throws ErreurInventairePlein {
		for (int i = 0; this.getPv() > 0 && i < degat ; i++) {
			this.degat();
		}
		if(this.estMort()) {
			this.meurt();
		}
	}

	public Checkpoint getCheckpoint() {
		return this.checkpoint;
	}


	/**
	 * Effectue un mouvement vers le haut si le param est négative le joueur descend
	 * @param val nombre dont l'on monte ou descend
	 */
	public void translationY(int val) {
		this.yProperty.setValue(this.getY() - val);
	}

	/**
	 * Permet d'effectuer une translationY de val si toucheY est true
	 * @param val
	 * @ failed or interrupted I/O operation
	 */
	public void monter(int val) {
		if(this.toucheY(true))
			translationY(val);
	}

	/**
	 * Permet d'effectuer une translationY de -val si toucheY est true
	 * @param val
	 * @ failed or interrupted I/O operation
	 */
	public void descendre(int val) {
		for (int i=0; i<val; i++)
			if(this.toucheY(false))
				translationY(-1);

	}

	/**
	 * Permet de savoir si on ne touche rien au niveau des y au dessus du perso si auDessus est true est en dessous sinon
	 *
	 *
	 * @param auDessus si il est vrai on vérifie les collision audessus du personnage sinon en dessous
	 * @return 	true si il peut passer (pas de colission, touche un arbre, touche une plante)
	 * 			false si il ne peut pas passer (si il touche un autre bloc)
	 */
//	public boolean toucheY(boolean auDessus) {
//		boolean gaucheTuile=true;
//		boolean droiteSprite;
//		Ressource blocAEmplacement;
//		int i=0;
//		if(auDessus) {
//			while (i<taille[0] && gaucheTuile) {
//				blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+32*i+2, this.getY()-32);
//				gaucheTuile = ( blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
//				i++;
//			}
//			blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+32*taille[0]-2, this.getY()-32);
//			droiteSprite = (blocAEmplacement ==null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
//		}
//		else {
//
//			while (i<taille[0] && gaucheTuile) {
//				blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+32*i+2, this.getY()+64);
//				gaucheTuile = ( blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
//				i++;
//			}
//			blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+32*taille[0]-2, this.getY()+64);
//			droiteSprite = (blocAEmplacement ==null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
//
//		}
//		return (gaucheTuile && droiteSprite);
//	}
	
	//
	
//	public boolean toucheY(boolean auDessus) {
//		boolean gauche; // this.getHauteur() * 32 pour 64
//		boolean droite; // this.getLargeur() * 32 pour 32
//		if(auDessus) {
//			gauche = this.environnement.getCarte().emplacement(this.getX()+1, this.getY()-32)==null || this.environnement.getCarte().emplacement(this.getX()+1, this.getY()-32) instanceof Bois;
//			droite = this.environnement.getCarte().emplacement(this.getX()+31, this.getY()-32)==null || this.environnement.getCarte().emplacement(this.getX()+31, this.getY()-32) instanceof Bois;
//		}
//		else {
//			gauche = this.environnement.getCarte().emplacement(this.getX()+1, this.getHauteur() * 32)==null || this.environnement.getCarte().emplacement(this.getX()+1, this.getY()+this.getHauteur() * 32) instanceof Bois;
//			droite = this.environnement.getCarte().emplacement(this.getX()+31, this.getY()+ this.getHauteur() * 32)==null || this.environnement.getCarte().emplacement(this.getX()+31, this.getY()+this.getHauteur() * 32) instanceof Bois;
//		}
//		return (gauche && droite) && !((gauche || droite) && !(gauche && droite));
//	}
	
	public boolean toucheY(boolean auDessus) {
		boolean gaucheTuile = true;
		boolean droiteSprite;
		Ressource blocAEmplacement;
		int i = 0;
		if(auDessus) {
			while (i<taille[0] && gaucheTuile) {
				blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+32*i+2, this.getY()+64);
				gaucheTuile = ( blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
				i++;
			}
			blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+32*taille[0]-2, this.getY()+64);
			droiteSprite = (blocAEmplacement ==null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
		}
		else {

			while (i<taille[0] && gaucheTuile) {
				blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+32*i+2, this.getY()+64);
				gaucheTuile = ( blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
				i++;
			}
			blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+32*taille[0]-2, this.getY()+64);
			droiteSprite = (blocAEmplacement ==null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
		}
		return (gaucheTuile && droiteSprite);
	}
	
	public int getHauteur() {
		return this.taille[1];
	}
	
	public int getLargeur() {
		return this.taille[0];
	}

	/**
	 * Sauter permet uniquement de sauter de la hauteur du saut du personnage.
	 * Et est donc différent de monter car un perso pourrait être projeter par une attaque
	 * a une valeur plus haute/basse que celle de son saut.
	 */
	public void sauter() {
		saut = true;
		this.monter(this.hauteurSaut);
		hauteurSaut++;
		System.out.println("je saute");
		if (hauteurSaut>=32) { //12
			this.saut = false;
			hauteurSaut = hauteurMaxSaut;
		}
	}



	public void descendre() {
		this.monter(-this.hauteurSaut);
	}

	/**
	 * Permet de faire un saut en fonction du paramètre d'entrée direction
	 * @param direction : true pour droite, false pour gauche
	 */

	public void sauterEnDirection(boolean direction) { //TODO a finir saut en direction
		this.sauter();

		int i = 0;
		if(direction) {
			while(i < this.hauteurMaxSaut) {
				this.translationX(-1);
				i++;
			}
		}
		else {
			while(i < this.hauteurMaxSaut) {
				this.translationX(1);
				i++;
			}



		}
	}


	public void translationX(int val) {
		if(toucheX(true)) {
			this.xProperty.setValue(this.getX() - val);
		}
	}

	public void droite() {
		if(toucheX(true))
			this.translationX(-vitesseDeplacement);
	}

	public void gauche() {
		if(toucheX(false))
			this.translationX(vitesseDeplacement);
	}


	/**
	 * Permet de savoir si on ne touche rien au niveau des x à droite du perso si aDroite est true est à gauche sinon
	 *
	 * @param aDroite si il est vrai on vérifie les collision à droite du personnage sinon à gauche
	 * @return 	true si il peut passer (pas de colission, touche un arbre, touche une plante)
	 * 			false si il ne peut pas passer (si il touche un autre bloc)
	 * 
	 */
//	public boolean toucheX(boolean aDroite) {
//		boolean hautTuileTouchePas = true;
//		boolean basTuileTouchePas;
//		Ressource blocAEmplacement;
//		int i = 0;
//		if(aDroite) {
//			while (i<taille[1] && hautTuileTouchePas) {
//				blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+33, this.getY()+32*i);
//				hautTuileTouchePas = (blocAEmplacement == null || blocAEmplacement instanceof Bois|| blocAEmplacement instanceof Plante);
//				i++;
//			}
//			blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+33, this.getY()+32*taille[1]-1);
//			basTuileTouchePas = (blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
//		}
//		else {
//			while (i<taille[1] && hautTuileTouchePas) {
//				blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()-1, this.getY()+32*i);
//				hautTuileTouchePas = (blocAEmplacement == null || blocAEmplacement instanceof Bois|| blocAEmplacement instanceof Plante);
//				i++;
//			}
//			blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()-1, this.getY()+32*taille[1]-1);
//			basTuileTouchePas = (blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
//		}
//		return (hautTuileTouchePas && basTuileTouchePas);
//	}
	

	public boolean toucheX(boolean aDroite) {
		boolean hautTuileTouchePas = true;
		boolean basTuileTouchePas  ;
		Ressource blocAEmplacement;
		int i = 0;
		if(aDroite) {
			while (i<taille[1] &&  hautTuileTouchePas) {
				blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+33, this.getY()+32 *i);
				 hautTuileTouchePas = ( blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
				i++;
			}
			blocAEmplacement = this.environnement.getCarte().emplacement(this.getX()+33, this.getY()+32 * taille[1] -1);
			basTuileTouchePas = ( blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
		}
		else {
			while (i<taille[1] &&  hautTuileTouchePas) {
				blocAEmplacement = this.environnement.getCarte().emplacement(this.getX() - 1, this.getY()+32 *i);
				 hautTuileTouchePas = ( blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
				i++;
			}
			blocAEmplacement = this.environnement.getCarte().emplacement(this.getX() - 1, this.getY()+32 * taille[1] -1);
			basTuileTouchePas = ( blocAEmplacement == null || blocAEmplacement instanceof Bois || blocAEmplacement instanceof Plante);
		}
		return (hautTuileTouchePas && basTuileTouchePas);
	}
	/**
	 * Ajoute un effet à la liste effets en fonction de quelle type d'effet il s'agit
	 * Si effet est Empoisonner 1er position
	 * Si effet est Ralentir 2eme position
	 * Si effet est Renfoncer 3eme position
	 * Si effet est Accelerer 4eme position
	 * @param effet
	 */
	public void ajouterEffet(Effet effet) {
		String quelleEffet = effet.getClass().getSimpleName();
		switch (quelleEffet) {
		case "Empoisoner":
			effets.add(0, effet);
			break;
		case "Ralentir":
			effets.add(1, effet);
		case "Renforcer":
			effets.add(2, effet);
			break;
		case "Accelerer":
			effets.add(3, effet);
			break;
		default:
			break;
		}
	}

	/**
	 * Remplace l'effet a l'indice indice par null
	 * @param indice
	 */
	public void SupprimerEffet(int indice) {
		effets.set(indice, null);
	}


	public boolean estMort() {
		return this.getPv() == 0;
	}

	/**
	 * Sera utile quand potion implementé
	 * @param vitesseBonus
	 * @return
	 */
	public int Deplacement(int vitesseBonus, boolean accelerer) {
		if(accelerer)
			return this.vitesseDeplacement*(1 + (vitesseBonus/100));
		return this.vitesseDeplacement*(1 - (vitesseBonus/100));
	}

	public void perdreRessources() { // Lorsque mort perd ses ressources
		for(int i = 0 ; i < this.inventaire.getTaille(); i++) {
			if(this.inventaire.getItem(i) instanceof Ressource) {
				this.inventaire.supprimer(i);

			}
		}
	}

	public void gravite() {
		this.descendre(2);
		if (this.saut)
			sauter();
	}

	public final int getPv() {
		return this.pvProperty.getValue();
	}

	public final void setPv(int val) {
		this.pvProperty.setValue(val);
	}

	public final IntegerProperty pvProperty() {
		return this.pvProperty;
	}

	public final int getX() {
		return this.xProperty.getValue();
	}

	public final void setX(int val) {
		this.xProperty.setValue(val);
	}

	public final IntegerProperty xProperty() {
		return this.xProperty;
	}

	public final int getY() {
		return this.yProperty.getValue();
	}

	public final void setY(int val) {
		this.yProperty.setValue(val);
	}

	public final IntegerProperty yProperty() {
		return this.yProperty;
	}

	public final int getHauteurSaut() {
		return this.hauteurSaut;
	}

	public final void setHauteurSaut(int val) {
		this.hauteurSaut = val;
	}

	public final int getHauteurMaxSaut() {
		return this.hauteurMaxSaut;
	}

	public final void setHauteurMaxSaut(int val) {
		this.hauteurMaxSaut = val;
	}

	public boolean estEnDehorsMap() {
		return this.getX() < 0 || this.getY() > 0;
	}


	public boolean estEnLaire() throws IOException {
		int[] taille = {1,2};//provisoire
		return this.environnement.getCarte().emplacement(this.getX(), this.getY(), taille)==null;
    }

	public void setVitesseDeplacement(int vitesseDeplacement) {
		this.vitesseDeplacement = vitesseDeplacement;
	}

	public int getVitesseDeplacement() {
		return this.vitesseDeplacement;
	}

	public Environnement getEnvironnement() {
		return this.environnement;
	}

	public Inventaire getInventaire() {
		return this.inventaire;
	}

	public int[] getTaille() {
		return taille;
	}
	


	public boolean getSaut() {
		return saut;
	}

	public void setSaut(boolean saut) {
		this.saut = saut;
	}

	public void meurt() throws ErreurInventairePlein {
		this.setX(-320);
		this.setY(-320);
		if(!this.estMort()) {
			this.setPv(0);
		}
		this.perdreRessources();

	}

	/**
	 * Vérifie si le joueur se trouve à droite ou à gauche du personnage
	 * @return Retourne true si le joueur se trouve à la droite du personnage false sinon
	 * @throws ErreurObjetIntrouvable Survient si aucune instance de la classe Joueur est présente dans Environnement.personnages
	 */
	public boolean ouSeTrouveLeJoueur() throws ErreurObjetIntrouvable { // peut-être à mettre dans Personnage
		return this.getEnvironnement().getJoueur().getX() > this.getX();
	}

	/**
	 * Vérifie si le joueur se trouve dans un rayon correspondant à la taille du saut en  blocs autour du personnage
	 * @return Retourne true si le joueur est à porter du personnage , false sinon
	 * @throws ErreurObjetIntrouvable Survient si aucune instance de la classe Joueur est présente dans la carte
	 */
	public boolean estAporterDuJoueur() throws ErreurObjetIntrouvable { // peut-être à mettre dans Personnage
		Joueur joueur = this.getEnvironnement().getJoueur();
		return this.getX() - this.vitesseDeplacement*this.hauteurMaxSaut <= joueur.getX()  && this.getX() >= joueur.getX() || this.getX() + this.vitesseDeplacement*this.hauteurMaxSaut >= joueur.getX()  && this.getX() <= joueur.getX();
	}

	/**
	 * Vérifie si le joueur se trouve dans un rayon de val blocs autour du personnage, val étant la varaiable passé en paramètre
	 * @param val : valeur correspondant au rayon du cercle qui définit si le joueur est à portee
	 * @return Retourne true si le joueur est près du personnage , false sinon
	 * @throws ErreurObjetIntrouvable Survient si aucune instance de la classe Joueur est présente dans la carte
	 */

	public boolean estPrèsDuJoueur(int valX, int valY) throws ErreurObjetIntrouvable { // peut-être à mettre dans Personnage
		Joueur joueur = this.getEnvironnement().getJoueur(); // faire abscisse 
		return this.getX() - valX <= joueur.getX()  && this.getX() >= joueur.getX() && (this.getY() == joueur.getY() || (this.getY() + valY  <= joueur.getY() && this.getY() >= joueur.getY()) || this.getY() - valY <= joueur.getY()&& this.getY() >= joueur.getY())
				|| this.getX() + valX >= joueur.getX()  && this.getX() < joueur.getX() && (this.getY() == joueur.getY() || (this.getY() - valY  >= joueur.getY()  && this.getY() <= joueur.getY()) || this.getY() + valY >= joueur.getY() && this.getY() <= joueur.getY());
	}

	
	
	
	
	public boolean estPrèsDuJoueurBased(int valX, int valY) throws ErreurObjetIntrouvable { // peut-être à mettre dans Personnage
		Joueur joueur = this.getEnvironnement().getJoueur(); // faire abscisse 
		return this.getX() - valX <= joueur.getX()  && this.getX() >= joueur.getX()
				|| this.getX() + valX >= joueur.getX()  && this.getX() < joueur.getX() ;
	}
	
	public boolean estPrèsDuJoueur2(int valX, int valY) throws ErreurObjetIntrouvable { // peut-être à mettre dans Personnage
		Joueur joueur = this.getEnvironnement().getJoueur(); // faire abscisse 
		return this.getX() - valX <= joueur.getX()  && this.getX() >= joueur.getX() && this.getY() - valY <= joueur.getY()  && this.getY() >= joueur.getY()
				|| this.getX() + valX <= joueur.getX()  && this.getX() >= joueur.getX() && this.getY() + valY <= joueur.getY()  && this.getY() >= joueur.getY();
	}

	


	public boolean estSurLeJoueur() throws ErreurObjetIntrouvable { // peut-être à mettre dans Personnage
		return this.getEnvironnement().getJoueur().getX() + 16 == this.getX() && this.getEnvironnement().getJoueur().getY()  == this.getY() ||
				this.getEnvironnement().getJoueur().getX() - 16 == this.getX() && this.getEnvironnement().getJoueur().getY()  == this.getY() ||
				this.getEnvironnement().getJoueur().getX() + 16 == this.getX() && this.getEnvironnement().getJoueur().getY() + 16 == this.getY() ||
				this.getEnvironnement().getJoueur().getX() + 16 == this.getX() && this.getEnvironnement().getJoueur().getY() -16  == this.getY() ||
				this.getEnvironnement().getJoueur().getX()  == this.getX() && this.getEnvironnement().getJoueur().getY() + 16 == this.getY() ||
				this.getEnvironnement().getJoueur().getX()  == this.getX() && this.getEnvironnement().getJoueur().getY() -16  == this.getY();

	}

	

	public int getId() {
		return id;
	}
}
