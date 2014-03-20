package shared;

import java.io.Serializable;

public class Skalarauftrag implements Serializable {
	private static final long serialVersionUID = 4245958107417693443L;
	private boolean addieren = false;
	private int[] daten;
	private int ergebnis;

	public Skalarauftrag(boolean addieren, int[] daten) {
		super();
		this.addieren = addieren;
		this.daten = daten;
	}

	public boolean isAddieren() {
		return addieren;
	}

	public void setAddieren(boolean addieren) {
		this.addieren = addieren;
	}

	public int[] getDaten() {
		return daten;
	}

	public void setDaten(int[] daten) {
		this.daten = daten;
	}
	
	public int getErgebnis() {
		return ergebnis;
	}
	
	public void setErgebnis(int ergebnis){
		this.ergebnis = ergebnis;
	}
}
