package application.modele;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.exception.TailleMapException;
import application.modele.ressources.Bois;
import application.modele.ressources.Pierre;
import application.modele.ressources.PlanteDeNike;
import application.modele.ressources.PlanteHercule;
import application.modele.ressources.PlanteMedicinale;
import application.modele.ressources.Ressource;
import application.modele.ressources.Terre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Cette classe contient 3 constante qui sont la hauteur et la largeur de la map en nombre de blocs et la taille de chaque bloc.
 * Elle peut lancer deux exception : TailleMapException, IOException
 * Elle possède un constructeur
 * 
 * @author jberguig
 *
 */
public class Carte {

	private BufferedReader map;
	public final static int HAUTEUR = 32;
	public final static int LARGEUR = 60;
	public final static int TAILLE_BLOCK = 32;
	private ObservableList<Ressource> blocMap;
	public Carte() throws TailleMapException, IOException {
		this.map = Csv.ouvrir("mapsTest.csv");
		this.blocMap = FXCollections.observableArrayList();
		creerListeBlock();
	}
	
	/**
	 * Calcule un indice dans la map en divisant les x et les y par la taille du bloc et en multipliant
	 * les y par la Largeur de la Carte puisque la structure de données est linéaire. 
	 * Et enfin de faire la somme des deux pour retourner le bloc a cette indice
	 * @param x position sur l'axe des x
	 * @param y position sur l'axe des y
	 * @return le bloc à indiceMap
	 */
	
	public BufferedReader getMap(){
		System.out.println(map);
		return this.map;
	}
	
	

	public int getHauteur() {
		return HAUTEUR;
	}

	public int getLargeur() {
		return LARGEUR;
	}
	
	public Ressource emplacement(int x, int y) {
		int indiceDansMap = (x/TAILLE_BLOCK) + ((y/TAILLE_BLOCK) * LARGEUR);
		return this.blocMap.get(indiceDansMap);
	}
	
	public Ressource emplacement(int x, int y, int[] taille) {
		int indiceDansMap = (x/TAILLE_BLOCK)+(taille[0]/2) + ((y/TAILLE_BLOCK) * LARGEUR)+taille[1];
		return this.blocMap.get(indiceDansMap);
	}
	
	/**
	 * 
	 * @param indice emplacement du bloc dans la liste
	 * @return le bloc visée
	 */
	public Ressource emplacement(int indice) {
		return this.blocMap.get(indice);
	}

	/**
	 * Permet de créer a partir d'un fichier CSV la liste de tos les blocks de la maps le ciel est null
	 * @throws TailleMapException
	 * @throws IOException
	 */
	public void creerListeBlock() throws TailleMapException, IOException{
		String ligne;
		char suivant;
		int x = 0;
		int y = 0;
		ligne = this.map.readLine();
		//System.out.println(ligne.length());
		while(ligne!=null) {
			for (int indice=0; indice<ligne.length(); indice+=2) {
				suivant=ligne.charAt(indice);
				switch (suivant) {
					case '2':
						blocMap.add(new Terre(true, x*TAILLE_BLOCK, y*TAILLE_BLOCK, x+(y*((ligne.length()+1)/2))));
						break;
					case '3':
						blocMap.add(new Bois(false, x*TAILLE_BLOCK, y*TAILLE_BLOCK, x+(y*((ligne.length()+1)/2))));
						break;
					case '4':
						blocMap.add(new Pierre(false, x*TAILLE_BLOCK, y*TAILLE_BLOCK, x+(y*((ligne.length()+1)/2))));
						break;
					case '5':
						blocMap.add(new PlanteDeNike(x*TAILLE_BLOCK, y*TAILLE_BLOCK, x+(y*((ligne.length()+1)/2))));
						break;
					case '6':
						blocMap.add(new PlanteHercule(x*TAILLE_BLOCK, y*TAILLE_BLOCK, x+(y*((ligne.length()+1)/2))));
						break;
					case '7':
						blocMap.add(new PlanteMedicinale(x*TAILLE_BLOCK, y*TAILLE_BLOCK, x+(y*((ligne.length()+1)/2))));
						break;
					default://tous las chiffres de tuile avec lesquelles on ne peut intéragir (ciel, nuage,...)
						blocMap.add(null);
						break;
				}	
				x++;
			}
			if (x!=LARGEUR)
				throw new TailleMapException("Problème de Largeur a la hauteur " + y + " : "+x+" a la place des "+LARGEUR+" demandés.");
			x=0;
			y++;
			ligne = this.map.readLine();
		}
		if(y!=HAUTEUR)
			throw new TailleMapException("Problème de Hauteur : "+y+" a la place des "+HAUTEUR+" demandés.");
//		System.out.println(blockMap.size());
	}
	
	/**
	 * detruit le bloc qui se trouve a indice et la remplace par null
	 * @param indice
	 * @throws ErreurInventairePlein 
	 */
	public Ressource detruireBlock(int indice) {
		Ressource blocDetruit = this.blocMap.remove(indice);
		this.blocMap.add(indice, null);
		return blocDetruit;
	}

	/**
	 * Si on trouve des blocs dans blockMap avec x et y en dehors de la map ils sont détruit.
	 * @throws ErreurInventairePlein 
	 * 
	 */
	public void ressourceEnDehorsMap() throws ErreurInventairePlein {
		for(int i = 0 ; i < this.getBlockMap().size(); i++) {
			if(this.blocMap.get(i).estEnDehorsMap()){
				this.detruireBlock(i);
			}
		}
	}

	/**
	 * Permet de faire des degats à des blocs
	 * @param indice l'endroit où se trouve le bloc dans la liste 
	 * @param val de combien il est attaquée
	 * @return 
	 */
	public Ressource attaquerBloc(int indice, int val) {
		this.blocMap.get(indice).prendreDegat(val);
		if (this.blocMap.get(indice).estDetruit())
			return detruireBlock(indice);
		return null;			
	}

	/**
	 * 
	 * @return la blocMap
	 */
	public ObservableList<Ressource> getBlockMap() {
		return blocMap;
	}	
}

