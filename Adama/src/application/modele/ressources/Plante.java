package application.modele.ressources;

public abstract class Plante extends Ressource {

	private int tempsRepousse;
	
	public Plante(int x, int y, int indice) {
		super(false, x, y, indice);
		this.tempsRepousse = 0;
	}
	
	
	public Plante(int indice) {
		super(false, 5,5, indice);
	}	
	
	public int getTempsRepousse() {
		return tempsRepousse;
	}
	
	public void setTempsRepousse(int tempsRepousse) {
		this.tempsRepousse = tempsRepousse;
	}
	
	public void incrementerTempsRepousse() {
		tempsRepousse+=1;
	}
	
	public void reinisialiséTempsRepousse() {
		tempsRepousse = 0;
	}
}
