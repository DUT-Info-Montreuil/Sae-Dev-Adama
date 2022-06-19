package application.modele;

import application.modele.exception.ErreurInventairePlein;

/**
 * Interface qui est au-dessus de Ressource, Potion, Outil, Arme 
 * @author jberguig
 *
 */
public interface  Item {
	
	public void utiliser(int val) throws ErreurInventairePlein;	
	
}