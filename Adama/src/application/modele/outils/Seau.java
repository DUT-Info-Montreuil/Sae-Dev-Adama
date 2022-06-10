package application.modele.outils;

import application.modele.Environnement;
import application.modele.ressources.Eau;
import application.modele.ressources.Ressource;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Seau extends Outil {

	private BooleanProperty estRempli;
	private final static int TEMPS_REMPLISSAGE = 17647; //correspond à environ 5 minutes
	private Eau eau;

	public Seau(Environnement env) {
		super(env, TEMPS_REMPLISSAGE);
		this.estRempli = new SimpleBooleanProperty(false);
		eau = null;
	}

	@Override
	public Ressource utiliser(int val) {
		if(!EstRempli())
			this.vider();
		return eau;

	}

	public void remplir() {
		eau = new Eau();
		setEstRempli(true);
	}

	public void vider() {
		eau = null;
		setEstRempli(false);
	}


	public static int getTempsRemplissage() {
		return TEMPS_REMPLISSAGE;
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

