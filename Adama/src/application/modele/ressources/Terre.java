package application.modele.ressources;

public class Terre extends Ressource {

	
	public Terre(boolean posable, int x, int y, int indice) {
		super( posable, x, y, indice);
	}
	
	public Terre(int indice) {
		super(true,5,5, indice);
	}

	
	public String toString() {
		return "Terre";
	}

	@Override
	public void utiliser(int val) {
		this.setIndice(val);
	}
}