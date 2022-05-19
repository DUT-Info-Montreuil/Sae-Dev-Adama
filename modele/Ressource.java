package application.modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Ressource implements Item {
	private boolean posable;
	private int nombre;
	private int durabiliter;
	private IntegerProperty xProperty;
	private IntegerProperty yProperty;
	
	public Ressource(int nombre, int x, int y, boolean posable) {
		this.posable = posable;
		this.nombre = nombre;
		this.durabiliter = -1;
		this.xProperty = new SimpleIntegerProperty(x);
		this.yProperty = new SimpleIntegerProperty(y);
	}
	
	public Ressource(int nombre, int durabiliter, int x, int y, boolean posable) {
		this.posable = posable;
		this.nombre = nombre;
		this.durabiliter = durabiliter;
		this.xProperty = new SimpleIntegerProperty(x);
		this.yProperty = new SimpleIntegerProperty(y);
	}
	
	public final int getX() {
		return this.xProperty.getValue();
	}
	
	public final void setX(int val) {
		this.xProperty.setValue(val);
	}
	
	public final IntegerProperty xProperty() {
		return this.xProperty;
	}
	
	public final int getY() {
		return this.yProperty.getValue();
	}
	
	public final void setY(int val) {
		this.yProperty.setValue(val);
	}
	
	public final IntegerProperty yProperty() {
		return this.yProperty;
	}
	
	public void setNombre(int val) {
		this.nombre = val;
	}
	
	public void augmenterNombre(int val) {
		this.nombre += val;
	}
	
	public void diminuerNombre(int val) {
		augmenterNombre(-val);
	}
	
	public abstract void utiliser();
	
	public boolean estPosable() {
		return this.posable;
	}
	
	public int getNombre() {
		return this.nombre;
	}
	
	
	public void prendreDegat(int degat) {
		this.durabiliter -= degat;
	}
	
	
	public boolean estCasser() {
		return this.durabiliter <= 0;
	}
}