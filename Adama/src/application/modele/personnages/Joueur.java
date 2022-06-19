package application.modele.personnages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import application.modele.exception.ErreurPasDobjetCraftable;
import application.modele.outils.Sceau;
import application.modele.potions.AntiPoison;
import application.modele.potions.Potion;
import application.modele.potions.PotionDegat;
import application.modele.potions.PotionVie;
import application.modele.potions.PotionVitesse;
import application.modele.Carte;
import application.modele.Checkpoint;
import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.Item;
import application.modele.armes.Arc;
import application.modele.armes.Arme;
import application.modele.armes.Epee;
import application.modele.armes.Fleche;
import application.modele.armes.Poing;
import application.modele.exception.ErreurArmeEtOutilPasJetable;
import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.exception.ErreurObjetInvalide;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import application.modele.ressources.Ressource;
import application.modele.ressources.Terre;

public class Joueur extends Personnage {

	private final static int MAX_PV = 7;
	private IntegerProperty faimProperty;
	private static final int MAX_FAIM = 7;
	private Item objetEquiper;
	private Inventaire inventaireRaccourci;
	private final Poing POING =  new Poing();
	private final static int VITESSE_COURRIR = 20;
	private final static int VITESSE_MARCHE = 10;
	private final static int VITESSE_ACCROUPIE = 5;
	private BooleanProperty estAccroupiProperty;
	private final static int[] TAILLE = {1,2};
	private boolean invincibiliter;



	public Joueur(int pv,int x, int y,
			Environnement environnement, int faim, Inventaire inventaire,
			Item objetEquiper, Inventaire inventaireRaccourci, int hauteurSaut) { 
		super(pv, x, y, VITESSE_MARCHE, environnement,inventaire, hauteurSaut, TAILLE);
		this.faimProperty = new SimpleIntegerProperty(faim);
		this.objetEquiper = objetEquiper;
		this.inventaireRaccourci = inventaireRaccourci;
		this.estAccroupiProperty = new SimpleBooleanProperty(false);
		this.invincibiliter = false;
	}

	public Joueur(int x, int y, Environnement environnement) {
		super(MAX_PV, x, y, 5, environnement, TAILLE );
		this.faimProperty = new SimpleIntegerProperty(MAX_FAIM);
		this.objetEquiper = this.POING;
		this.inventaireRaccourci = new Inventaire(10);
		this.estAccroupiProperty = new SimpleBooleanProperty(false);
		this.invincibiliter = false;
	}
	
	public boolean estInvincible() {
		return this.invincibiliter;
	}
	
	public void setInvincibiliter(boolean val) {
		this.invincibiliter = val;
	}

	public final int getFaim() {
		return this.faimProperty.getValue();
	}

	public final void setFaim(int val) {
		this.faimProperty.setValue(val);
	}

	public final IntegerProperty faimProperty() {
		return this.faimProperty;
	}

	public final boolean getEstAccroupi() {
		return this.estAccroupiProperty.getValue();
	}

	public final void setEstAccroupi(Boolean val) {
		this.estAccroupiProperty.setValue(val);
	}

	public final void setEstAccroupi() {
		this.estAccroupiProperty.setValue(!this.getEstAccroupi());
	}

	public final BooleanProperty estAccroupiProperty() {
		return this.estAccroupiProperty;
	}

	public void incrementerNourriture() {
		this.setFaim(this.getFaim() + 1 );
	}

	public void manger(int nourriture) {
		for (int i = 0; this.getFaim() < Joueur.MAX_FAIM && i < nourriture ; i++) {
			this.incrementerNourriture();
		}
	}

	public void decrementerNourriture() {
		this.setFaim(this.getFaim() - 1 );
	}

	public Inventaire getInventaireRaccourci() {
		return this.inventaireRaccourci;
	}


	public void remplacerObjetRaccourci(int indice, Item item) {
		this.inventaireRaccourci.remplacer(item, indice);
	}

	public void ajouterObjetRaccourci(Item item) throws ErreurInventairePlein {
		this.inventaireRaccourci.ajouter(item);
	}

	public void equiper(Item item) {
		this.objetEquiper = item;
	}

	public void desequiper() {
		this.objetEquiper = this.POING;
	}

	public void marcher() {
		this.setVitesseDeplacement(VITESSE_MARCHE);
	}


	public void courrir() {
		this.setVitesseDeplacement(VITESSE_COURRIR);
	}
	
	public void utiliserMain(int emplacement) throws ErreurInventairePlein, ErreurArmeEtOutilPasJetable, ErreurObjetIntrouvable {
		if (objetEquiper instanceof Potion) {
			String potion = objetEquiper.getClass().getSimpleName();
			switch (potion) {
			case "PotionVie":
				incrementerPv(PotionVie.getNombrePvRestaurer());
				break;
			case "PotionVitesse":
//				super.Deplacement(objetEquiper.getDuree());
				break;
			case "AntiPoison":
				super.SupprimerEffet(0);
			default:
				break;
			}
		}
		if(this.getDirection()) {
			emplacement = this.getX() + Carte.TAILLE_BLOC;
		}
		else {
			emplacement = this.getX() + Carte.TAILLE_BLOC;
		}
		if(this.objetEquiper instanceof Arme) {
			this.getEnvironnement().attaquerPersonnages(emplacement, this.getArmeEquiper().getDegat());
		}
		this.objetEquiper.utiliser(emplacement);
		
		if (objetEquiper instanceof Terre) {
			Carte carte = this.getEnvironnement().getCarte();
			if(carte.getBlockMap().get(emplacement)== null) {
				carte.getBlockMap().remove(emplacement);
				carte.getBlockMap().add(emplacement, (Terre)this.objetEquiper);
				//			carte.getBlockMap().set(emplacement, (Terre)this.objetEquiper);
				super.getInventaire().supprimer(objetEquiper);
				if(!super.getInventaire().estDansInventaire(objetEquiper))
					this.desequiper();
			}
		}
	
	}


	public void accroupie() {
	
	}

	public void setModeDeplacement() { // TODO trouver un meilleure nom 
		if(this.getVitesseDeplacement() != VITESSE_COURRIR) {
			this.setVitesseDeplacement(VITESSE_COURRIR);
		}
		else {
			this.setVitesseDeplacement(VITESSE_MARCHE);
			if(this.getEstAccroupi()) {
				this.setEstAccroupi();
			}
		}
	}

	public boolean estUneArmeOuUnOutil(Item item) { 
		return item instanceof Arme || item instanceof Ressource;
	}

	public void attaquer() throws ErreurObjetInvalide {
		if(this.objetEquiper instanceof Arme) {
			if (!((Arme)this.objetEquiper).estEnRecharge()) {
				((Arme)this.objetEquiper).attaquer();
				((Arme)this.objetEquiper).enRecharge();
			}
		}
		else {
			throw new ErreurObjetInvalide(this.objetEquiper);
		}
	}



	public Item craft (ArrayList<Item> items) throws ErreurPasDobjetCraftable, ErreurObjetInvalide { 
		String pierre = "Pierre";
		String bois = "Bois";
		String planteDeNike = "PlanteDeNike";
		String planteHercule = "¨PlanteHercule";
		String planteMedicinale = "PlanteMedicinale";
		String fils = "Fils";
		String antiPoison = "AntiPoison";
		Sceau PossedeSeau = null;
		String seau = null;
		int indiceSaut = items.indexOf(PossedeSeau);
		if(indiceSaut != -1);
			PossedeSeau = (application.modele.outils.Sceau) super.getInventaire().getItems().get(indiceSaut);
			if(PossedeSeau.EstRempli())
				seau = "Seau";
		String plante = "Plante";

		Item item;
		Map<String, Integer> recette = new HashMap<String, Integer>();

		recette.put(pierre, 0); 
		recette.put(bois, 0);
		recette.put(plante, 0);

		int k = 0;

		try {
			for(k = 0 ; k < items.size() ; k ++) { 
				recette.put(items.get(k).getClass().getSimpleName(), recette.get(items.get(k).getClass().getSimpleName()) + 1);
			}
		}catch (java.lang.NullPointerException e) { 
			throw new ErreurObjetInvalide(items.get(k));
		}

		if(recette.get(bois) == 3 && recette.get(plante) == 1) {  // 3 bois et 1 plante crée un arc
			item = new Arc(this.getInventaire()); // TODO revoir recette pour l'arc (remplacer la plante part de la ficelle)
		}

		else if(recette.get(bois) == 2 && recette.get(pierre) == 1) { // 2 bois et 1 pierre crée une épée
			item = new Epee();
		}


		else if(recette.get(bois) == 1 && recette.get(pierre) == 1) { // 1 de bois et 1 pierre crée une flèche
			item = new Fleche();
		}
		else if(recette.get(bois) == 5)
			return new Sceau(getEnvironnement().getCarte(), this);

		else if (seau != null && recette.get(planteDeNike) == 2)
			return new PotionVitesse();
		else if (seau != null && recette.get(planteHercule) == 2)
			return new PotionDegat();

		else if (seau != null && recette.get(planteMedicinale) == 3)
			return new PotionVie();
		else if (seau != null && recette.get(planteMedicinale) == 2 && recette.get(antiPoison) == 1)
			return new AntiPoison();
		else {
			throw new ErreurPasDobjetCraftable();
		}

		return item;
	}

	public void jeter(Item item) throws ErreurArmeEtOutilPasJetable, ErreurObjetIntrouvable {
		if(!this.estUneArmeOuUnOutil(item)) {
			this.getInventaire().supprimer(item);
		}
		else {
			throw new ErreurArmeEtOutilPasJetable();
		}

	}

	public void soin() {
		this.setPv(this.getPv() + 1);
	}

	public void incrementerPv(int nourriture) {
		for (int i = 0; this.getPv() < Joueur.MAX_PV && i < nourriture ; i++) {
			this.soin();
		}
	}

	public void teleporterToCheckpoint() {
		this.setX(this.getCheckpoint().getX());
		this.setY(this.getCheckpoint().getY());
	}

	public Item getObjetEquiper() {
		return objetEquiper;
	}	

	public Arme getArmeEquiper() {
		if(this.objetEquiper instanceof Arme) {
			return (Arme) this.objetEquiper;
		}
		else {
			return this.POING;
		}
	}
	
	public  void agir() throws ErreurObjetIntrouvable{
		
	}
}