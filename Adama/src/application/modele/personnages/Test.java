package application.modele.personnages;

import java.io.IOException;

import application.modele.Environnement;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.exception.TailleMapException;

public class Test {

	public static void main(String[] args) throws TailleMapException, IOException, ErreurObjetIntrouvable {
		Environnement env = new Environnement();
		Joueur perso  = new Joueur(320, 0, env);
		Slime monstre = new Slime(320,330, env);
		boolean succes = true;
		for(int valX = 0; valX < 100 && succes; valX++){
			for (int valY = 0 ;valY < 100 && succes; valY++) {
				if(monstre.estPrèsDuJoueur(valX, valY) != monstre.estPrèsDuJoueur(valX, valY)) {
					System.out.println("Erreur");
					succes = false;
				}
			}
			
		}
		System.out.println("tout marche");
	

	}//System.out.println(monstre.estPrèsDuJoueur(valX, valY) && monstre.estPrèsDuJoueurReturn(valX, valY));

}
