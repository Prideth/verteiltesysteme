package shared;

import java.io.Serializable;

public class Matrizenauftrag extends Auftrag implements Serializable {
	private static final long serialVersionUID = 7495702832362550668L;
	private int[] zeile;
	private int[] spalte;
	private int ergebnis;

	public Matrizenauftrag(int[] zeile, int[] spalte) {
		super();
		this.zeile = zeile;
		this.spalte = spalte;
	}
	
	public int[] getZeile() {
		return zeile;
	}

	public void setZeile(int[] zeile) {
		this.zeile = zeile;
	}

	public int[] getSpalte() {
		return spalte;
	}

	public void setSpalte(int[] spalte) {
		this.spalte = spalte;
	}

	public int getErgebnis() {
		return ergebnis;
	}

	public void setErgebnis(int ergebnis) {
		this.ergebnis = ergebnis;
	}
}
