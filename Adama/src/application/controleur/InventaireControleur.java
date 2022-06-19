package application.controleur;

import application.modele.Inventaire;
import application.modele.Item;
import application.modele.outils.Sceau;
import application.modele.ressources.Ressource;
import application.vue.InventaireVue;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

public class InventaireControleur implements ListChangeListener<Item> {

	private static int compteur =0;
	private TilePane inv;
	private InventaireVue invVue;

	public InventaireControleur(TilePane inventaire) {
		inv = inventaire;
		invVue = new InventaireVue();
	}

	@Override
	public void onChanged(Change<? extends Item> c) {
		Image image = null;
		if (compteur==20) {
			compteur=0;
		}
		while(c.next()) {
			for(Item ajout : c.getAddedSubList()) {
				image = invVue.chargerImage(ajout.getClass().getSimpleName());
				Pane tuile = (Pane) inv.getChildren().get(compteur);
				ImageView b = ((ImageView) tuile.getChildren().get(0));
				Label nombre = (Label) tuile.getChildren().get(1);
				nombre.setVisible(true);
				if(ajout instanceof Sceau) {
					nombre.setText("1");
					ChangeListener<Boolean> ecouteurDeSceau =  ((obs,old,nouv)-> {
						if(((Sceau) ajout).EstRempli())
							b.setImage(new Image("ressource/sceau_plein.png"));
						else
							b.setImage(new Image("ressource/sceau_vide.png"));
					});
					((Sceau) ajout).EstRempliProperty().addListener(ecouteurDeSceau);

				}
				else if(ajout instanceof Ressource) {
					nombre.textProperty().bind(((Ressource) ajout).nombreProperty().asString());

				}
				else {
					nombre.setText("1");
				}
				b.setImage(image);
				Inventaire inventaire = new Inventaire(20);
				inventaire.setItems((ObservableList<Item>) c.getList());
				compteur = inventaire.getTaille();
			}
			//Si c est supprimer, on refait l'inventaire
			if (c.wasRemoved()) {
				ObservableList<Item> inventaireModele = (ObservableList<Item>) c.getList();
				for(compteur = 0; compteur<inv.getChildren().size();compteur++) {
					Pane tuile = (Pane) inv.getChildren().get(compteur);
					tuile.setStyle("-fx-background-color: #bbbbbb;-fx-border-style:none;-fx-border-width:0px;");
					ImageView img = (ImageView) tuile.getChildren().get(0);
					Label nombre = (Label) tuile.getChildren().get(1);
					if(compteur<inventaireModele.size()) {
						Item objet = inventaireModele.get(compteur);
						image = invVue.chargerImage(objet.getClass().getSimpleName());
						if(objet instanceof Ressource) {
							nombre.textProperty().unbind();
							nombre.textProperty().bind(((Ressource) objet).nombreProperty().asString());
						}
						else {
							nombre.textProperty().unbind();
							nombre.setText("1");
						}
					}
					else {
						image = null;
						nombre.setVisible(false);
						nombre.textProperty().unbind();
					}
					img.setImage(image);
				}
				compteur = inventaireModele.size();
				for(Item suppression : c.getRemoved()) {
					Pane tuile = (Pane) inv.getChildren().get(c.getFrom());
					tuile.setStyle("-fx-background-color: #bbbbbb;-fx-border-style:none;-fx-border-width:0px;");
					ImageView img = (ImageView) tuile.getChildren().get(0);
					img.setImage(null);
				}

			}
		}
	}
}