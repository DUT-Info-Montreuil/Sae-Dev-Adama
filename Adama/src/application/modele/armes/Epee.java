package application.modele.armes;

import application.modele.Environnement;
import application.modele.Inventaire;
import application.modele.exception.ErreurInventairePlein;
import application.modele.personnages.Personnage;

public class Epee extends Arme{
	public final static int DEGAT = 5;
	public final static int PORTER = 32;
	public final static int TEMPSRECHARGE = 2;
	
	public Epee() {
		super(DEGAT, PORTER, TEMPSRECHARGE);
		
	}
	
	public String toString() {
		return "Epee";
	}

	@Override
	public void utiliser(int val) {
		
	}
}