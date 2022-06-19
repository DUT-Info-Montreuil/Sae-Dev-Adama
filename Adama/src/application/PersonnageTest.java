package application;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;


import application.modele.Environnement;
import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.TailleMapException;
import application.modele.personnages.Joueur;

class PersonnageTest {

	@org.junit.jupiter.api.Test
	public final void testEstMort() throws TailleMapException, IOException {
		Environnement env = new Environnement();
		Joueur joueur = new Joueur(5,5,env); // x = 5, y = 5, pv = 7
		while(joueur.getPv() > 0) {
			assertFalse(joueur.estMort());
			joueur.degat();
		}
		assertTrue(joueur.estMort());
	}
	
	
	@org.junit.jupiter.api.Test
	public final void testdecrementerPvPositif() throws TailleMapException, IOException, ErreurInventairePlein {
		Environnement env = new Environnement();
		Joueur joueur = new Joueur(5,5,env);
		joueur.decrementerPv(6);
		assertEquals(1,joueur.getPv());
	}
	
	@org.junit.jupiter.api.Test
	public final void testdecrementerPvJuste() throws TailleMapException, IOException, ErreurInventairePlein {
		Environnement env = new Environnement();
		Joueur joueur = new Joueur(5,5,env);
		joueur.decrementerPv(7);
		assertEquals(0,joueur.getPv());
	}
	
	@org.junit.jupiter.api.Test
	public final void testdecrementerPvNegatif() throws TailleMapException, IOException, ErreurInventairePlein {
		Environnement env = new Environnement();
		Joueur joueur = new Joueur(5,5,env);
		joueur.decrementerPv(10);
		assertEquals(0,joueur.getPv());
	}
	
	
	@org.junit.jupiter.api.Test
	public final void testEstEnDehorsMap() throws TailleMapException, IOException {
		Environnement env = new Environnement();
		Joueur joueur = new Joueur(5,5,env);
		assertFalse(joueur.estEnDehorsMap(0,0));
	}
	
	@org.junit.jupiter.api.Test
	public final void testEstEnDehorsMapXnegatif() throws TailleMapException, IOException {
		Environnement env = new Environnement();
		Joueur joueur = new Joueur(5,5,env);
		joueur.setX(-5);
		assertTrue(joueur.estEnDehorsMap(0,0));
	}
	
	@org.junit.jupiter.api.Test
	public final void testEstEnDehorsMapYnegatif() throws TailleMapException, IOException {
		Environnement env = new Environnement();
		Joueur joueur = new Joueur(5,5,env);
		joueur.setX(2000);
		assertTrue(joueur.estEnDehorsMap(0,0));
	}
}