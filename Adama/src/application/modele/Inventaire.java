package application.modele;

import java.util.ArrayList;

import application.modele.armes.Fleche;
import application.modele.exception.ErreurInventairePlein;
import application.modele.exception.ErreurObjetIntrouvable;
import application.modele.potions.Potion;
import application.modele.ressources.Ressource;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventaire {
	private ObservableList<Item> items;
	private IntegerProperty tailleMaxProperty;


	public Inventaire(int taille) {
		this.items = FXCollections.observableArrayList();
		this.tailleMaxProperty = new SimpleIntegerProperty(taille);
	}

	public ObservableList<Item> getItems() {
		return this.items;
	}

	public Item getItem(int indice) {
		return this.items.get(indice);
	}

	public final int getTailleMax() {
		return this.tailleMaxProperty.getValue();
	}

	public final void setTailleMax(int val) {
		this.tailleMaxProperty.setValue(val);
	}

	public final IntegerProperty tailleMaxProperty() {
		return this.tailleMaxProperty;
	}

	public void augmenterTailleMax(int val) {
		this.setTailleMax(this.getTailleMax() + val);
	}

	public int getTaille() {
		return this.items.size();
	}

	public void ajouter(Item item) throws ErreurInventairePlein {
		if(!estPlein()) {
			if(item instanceof Ressource) {
				Ressource res = (Ressource) memeRessource(item, true);
				if(res == null) {
					this.items.add(item);
				}
				else
					res.incrementerNombreProperty();
			}
			else {
				this.items.add(item);
			}
		}
		else {
			throw new ErreurInventairePlein("Tous ce que vous récolterez sera détruit.\nVous devriez videz vos poche pour récolter de nouveau.");
		}
	}

	public boolean estDansInventaire(Item item) {
		for(int i = 0; i < this.getTaille() ; i++) {
			if(this.items.get(i) == item) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Si item a un objet du meme type que lui dans items la méthode le renvoie
	 * @param item
	 * @param ajout pour savoir si on utilise pour ajouter une ressource ou pour en trouver une pour le craft
	 * @return
	 */
	public Item memeRessource (Item item, boolean ajout) {
		String nomClassRes = "";
		String nomClassItem = item.getClass().getSimpleName();
		for(Item res : items) {
			nomClassRes = res.getClass().getSimpleName();
			if(item instanceof Ressource) {
				if(ajout && nomClassItem == nomClassRes && ((Ressource) res).getNombre()<Ressource.TAILLE_MAX_STACK)
					return res;
				if(!ajout && nomClassItem == nomClassRes && ((Ressource) res).getNombre()<=Ressource.TAILLE_MAX_STACK)
					return res;
			}
			else {
				if (!ajout && nomClassItem == nomClassRes)
					return res;
			}
		}
		return null;
	}

	public boolean estDansInventaire(Item item, int indice) {
		for(int i = 0; i < this.getTaille() ; i++) {
			if(this.items.get(i) == item && i == indice) {
				return true;
			}
		}
		return false;
	}

	public int indiceDansInventaire(Item item) throws ErreurObjetIntrouvable {
		for(int i = 0; i < this.getTaille() ; i++) {
			if(this.items.get(i) == item) {
				return i;
			}
		}
		throw new ErreurObjetIntrouvable(item.getClass().getSimpleName(), "Inventaire.items"); 
	}

	public void supprimer(Item item) throws ErreurObjetIntrouvable {
		if(item instanceof Ressource) {
			Ressource res = (Ressource) items.get(indiceDansInventaire(item));
			if(res == null) {
				this.items.remove(item);
			}
			else {
				res.decrementerNombre();
				if(res.getNombre()==0)
					items.remove(res);
			}
		}
		else if(item instanceof Potion)
			items.remove(item);

	}

	public void supprimer(int indice) {
		if(this.items.get(indice) instanceof Ressource) {
			Ressource res = ((Ressource) this.items.get(indice));
			res.decrementerNombre();
			if(res.getNombre()==0)
				items.remove(res);
		}
		else if(this.items.get(indice) instanceof Potion)
			items.remove(indice);
	}

	public void remplacer(Item item, int indice) { // objet ramassé qui remplace un objet présent dans l'inventaire qui sera jeté
		this.items.add(indice, item);
	}

	public Item setItem(Item item, int indice) {
		return this.items.set(indice, item);
	}

	public void echanger(Item item1, int indice1, Item item2, int indice2) {// échanger  deux objets déjà dans l'inventaire
		this.remplacer(item1, indice2);
		this.remplacer(item2, indice1);
	}

	public boolean estPlein() {
		return this.getTaille() >= this.getTailleMax();
	}

	public void transferer(Item item, Inventaire inventaireSource) throws ErreurInventairePlein, ErreurObjetIntrouvable { // terminer inventaire, environnement(inventaire), ressources,faire boolean pause, saut
		this.ajouter(item);
		inventaireSource.supprimer(item); // indice plus précis mais je ne sais pas si je vais y avoir accès
	}

	public void ajouterInventaire(Inventaire inventaireSource) throws ErreurInventairePlein, ErreurObjetIntrouvable {
		for (int i = 0 ; i < inventaireSource.getTaille() ; i++) {
			this.transferer(inventaireSource.getItem(i), inventaireSource);

		}
	}

	public Fleche getFleche() throws ErreurObjetIntrouvable {
		for(int i = 0; i < this.getTaille() ; i++) {
			if(this.items.get(i) instanceof Fleche) {
				return (Fleche) this.items.get(i);
			}
		}
		throw new ErreurObjetIntrouvable("Fleche", "Inventaire.Items");
	}


	public ArrayList<Fleche> getFleches() throws ErreurObjetIntrouvable {
		ArrayList<Fleche> fleches = new ArrayList<Fleche>();
		for(int i = 0; i < this.getTaille() ; i++) {
			if(this.items.get(i) instanceof Fleche) {
				fleches.add((Fleche) this.items.get(i));
			}
		}
		if(fleches.size() == 0) {
			throw new ErreurObjetIntrouvable("Fleche", "Inventaire.Items");
		}
		else {
			return fleches;
		}
	}

	@Override
	public String toString() {
		return items.toString();
	}



}
