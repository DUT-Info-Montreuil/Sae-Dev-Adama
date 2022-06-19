package application.modele.personnages;

import application.modele.outils.Sceau;
import application.modele.potions.Potion;
import application.modele.potions.PotionDegat;
import application.modele.potions.PotionVie;
import application.modele.potions.PotionVitesse;
import application.modele.Carte;
import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.Item;
import application.modele.armes.Arme;
import application.modele.armes.Epee;
import application.modele.armes.Poing;
import application.modele.exception.ErreurArmeEtOutilPasJetable;
import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.exception.ErreurObjetInvalide;
import application.modele.exception.ErreurObjetCraftable;
import application.modele.potions.Remede;
import application.modele.ressources.Venin;
import application.modele.ressources.Bois;
import application.modele.ressources.Pierre;
import application.modele.ressources.PlanteDeNike;
import application.modele.ressources.PlanteHercule;
import application.modele.ressources.PlanteMedicinale;
import application.modele.ressources.Ressource;
import application.modele.ressources.Terre;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

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

	public void accroupie() {
		this.setVitesseDeplacement(VITESSE_ACCROUPIE);
	}

	public void courrir() {
		this.setVitesseDeplacement(VITESSE_COURRIR);
	}

	public void utiliserMain(int emplacement) throws ErreurInventairePlein, ErreurArmeEtOutilPasJetable, ErreurObjetIntrouvable {
		if(this.objetEquiper instanceof Arme) {
			this.getEnvironnement().attaquerPersonnages(this);
		}
		this.objetEquiper.utiliser(emplacement);
		if (objetEquiper instanceof Terre) {
			Carte carte = this.getEnvironnement().getCarte();
			if(carte.getBlocMap().get(emplacement) == null) {
				carte.getBlocMap().remove(emplacement);
				carte.getBlocMap().add(emplacement, (Terre)this.objetEquiper);
				//			carte.getBlockMap().set(emplacement, (Terre)this.objetEquiper);
				super.getInventaire().supprimer(objetEquiper);
				if(!super.getInventaire().estDansInventaire(objetEquiper))
					this.desequiper();
			}
		}
	
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

	/**
	 * Cherche dans l'inventaire du joueur si il a les materiaux necessaire au caft
	 * Si c'est le cas on ajoute l'objet dans l'inventaire et on le supprime les matériaux
	 * sinon on envoie l'erreur ErreurObjetCraftable.
	 * @param ObjetAFabriquer
	 * @throws ErreurObjetCraftable Si le joueur n'a pas les ressources neccessaire au craft
	 * @throws ErreurInventairePlein
	 * @throws ErreurObjetIntrouvable
	 */
	public void craft (String ObjetAFabriquer) throws ErreurObjetCraftable, ErreurInventairePlein, ErreurObjetIntrouvable {
		Inventaire inventaire = super.getInventaire();
		switch (ObjetAFabriquer) {
		case "Epee":
			Bois bois = (Bois) inventaire.memeRessource(new Bois(-1), false);
			Pierre pierre = (Pierre) inventaire.memeRessource(new Pierre(-1), false);
			if(bois == null || pierre == null || bois.getNombre()<2 && pierre.getNombre()<1) {
				throw new ErreurObjetCraftable();
			}
			else {
				inventaire.ajouter(new Epee());
				inventaire.supprimer(bois);
				inventaire.supprimer(bois);
				inventaire.supprimer(pierre);
			}
			break;
		case "Sceau":
			bois = (Bois) inventaire.memeRessource(new Bois(-1), false);
			if(bois == null || bois.getNombre()<5) {
				throw new ErreurObjetCraftable();
			}
			else {
				inventaire.ajouter(new Sceau(getEnvironnement().getCarte(),this));
				inventaire.supprimer(bois);
				inventaire.supprimer(bois);
				inventaire.supprimer(bois);
				inventaire.supprimer(bois);
				inventaire.supprimer(bois);
			}
			break;
		case "PotionVie":
			Sceau sceau = (Sceau) inventaire.memeRessource(new Sceau(getEnvironnement().getCarte(),this), false);
			PlanteMedicinale med = (PlanteMedicinale) inventaire.memeRessource(new PlanteMedicinale(-1), false);
			if(sceau == null || med == null || !sceau.EstRempli()||med.getNombre()<3) {
				throw new ErreurObjetCraftable();
			}
			else {
				inventaire.ajouter(new PotionVie(this));
				inventaire.supprimer(med);
				inventaire.supprimer(med);
				inventaire.supprimer(med);
				sceau.vider();
			}
			break;
		case "PotionDegat":
			Sceau s = (Sceau) inventaire.memeRessource(new Sceau(getEnvironnement().getCarte(),this), false);
			PlanteHercule her = (PlanteHercule) inventaire.memeRessource(new PlanteHercule(-1),false);
			if(s == null || her == null || !s.EstRempli()||her.getNombre()<2) {
				throw new ErreurObjetCraftable();
			}
			else {
				inventaire.ajouter(new PotionDegat(this));
				inventaire.supprimer(her);
				inventaire.supprimer(her);
				s.vider();
			}
			break;

		case "PotionVitesse":
			Sceau sc = (Sceau) inventaire.memeRessource(new Sceau(getEnvironnement().getCarte(),this), false);
			PlanteDeNike nike = (PlanteDeNike) inventaire.memeRessource(new PlanteDeNike(-1), false);
			if(sc == null || nike == null || !sc.EstRempli()||nike.getNombre()<3) {
				throw new ErreurObjetCraftable();
			}
			else {
				inventaire.ajouter(new PotionVitesse(this));
				inventaire.supprimer(nike);
				inventaire.supprimer(nike);
				inventaire.supprimer(nike);
				sc.vider();
			}
			break;

		case "Remede":
			Venin venin = (Venin) inventaire.memeRessource(new Venin(-1), false);
			PlanteMedicinale medicinal = (PlanteMedicinale) inventaire.memeRessource(new PlanteMedicinale(-1), false);
			if(venin == null || medicinal == null || venin.getNombre()<1 ||medicinal.getNombre()<3) {
				throw new ErreurObjetCraftable();
			}
			else {
				inventaire.ajouter(new Remede(this));
				inventaire.supprimer(medicinal);
				inventaire.supprimer(medicinal);
				inventaire.supprimer(venin);
			}
			break;
		default:
			break;
		}
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
}