package application.modele.armes;

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