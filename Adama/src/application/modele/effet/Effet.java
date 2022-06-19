package application.modele.effet;

import application.modele.personnages.Personnage;

public abstract class Effet {
	
	private int durée;
	private Personnage perso;
	
	public Effet(int durée, Personnage perso) {
		this.durée = durée;
		this.perso = perso;
	}
	
	
	
	public abstract void appliquerEffet();

	public Personnage getPerso() {
		return perso;
	}
}
