package application.modele.outils;

import application.modele.Carte;
import application.modele.personnages.Joueur;
import application.modele.ressources.Eau;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Sceau extends Outil {

	private BooleanProperty estRempli;
	public final static int TEMPS_REMPLISSAGE = 17647; //correspond à environ 5 minutes
	private Eau eau;

	public Sceau(Carte carte, Joueur joueur) {
		super(carte, joueur, TEMPS_REMPLISSAGE);
		this.estRempli = new SimpleBooleanProperty(false);
		eau = null;
	}

	@Override
	public void utiliser(int val) {
		if(!EstRempli())
			this.vider();
	}

	public void remplir() {
		eau = new Eau();
		setEstRempli(true);
	}

	public void vider() {
		eau = null;
		setEstRempli(false);
	}


	public Eau getEau() {
		return eau;
	}

	public boolean EstRempli() {
		return estRempli.getValue();
	}

	public BooleanProperty EstRempliProperty() {
		return estRempli;
	}

	public void setEstRempli(boolean a) {
		estRempli.set(a);
	}	
}