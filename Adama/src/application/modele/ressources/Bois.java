package application.modele.ressources;

public class Bois extends Ressource {

	public Bois(boolean posable, int x, int y, int indice) {
		super(posable, x, y, indice);

	}


	public Bois(int indice) {
		super(false, 5,5, indice);
	}


	@Override
	public Ressource utiliser(int val) {
		return null;

	}


}
