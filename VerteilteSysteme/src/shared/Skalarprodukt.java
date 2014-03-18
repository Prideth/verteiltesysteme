package shared;

public class Skalarprodukt extends Aufgabe {
	private int[] skalar_eins;
	private int[] skalar_zwei;
	private Object[] zwischenergebnis;
	private Object ergebnis;

	public Skalarprodukt(int worker, int[] skalar_eins, int[] skalar_zwei) {
		super(worker);
		this.skalar_eins = skalar_eins;
		this.skalar_zwei = skalar_zwei;
	}

	public int[] getSkalar_eins() {
		return skalar_eins;
	}

	public void setSkalar_eins(int[] skalar_eins) {
		this.skalar_eins = skalar_eins;
	}

	public int[] getSkalar_zwei() {
		return skalar_zwei;
	}

	public void setSkalar_zwei(int[] skalar_zwei) {
		this.skalar_zwei = skalar_zwei;
	}

	public Object[] getZwischenergebnis() {
		return zwischenergebnis;
	}

	public void setZwischenergebnis(Object[] zwischenergebnis) {
		this.zwischenergebnis = zwischenergebnis;
	}

	public Object getErgebnis() {
		return ergebnis;
	}

	public void setErgebnis(Object ergebnis) {
		this.ergebnis = ergebnis;
	}

}
