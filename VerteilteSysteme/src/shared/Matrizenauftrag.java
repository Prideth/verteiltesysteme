package shared;

public class Matrizenauftrag {
	private int[] zeile;
	private int[] spalte;
	private int ergebnis;

	public int[] getZeile() {
		return zeile;
	}

	public Matrizenauftrag(int[] zeile, int[] spalte) {
		super();
		this.zeile = zeile;
		this.spalte = spalte;
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
