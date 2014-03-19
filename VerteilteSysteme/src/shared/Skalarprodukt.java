package shared;

import java.io.Serializable;

public class Skalarprodukt extends Aufgabe implements Serializable {
	private static final long serialVersionUID = -8732035671190447859L;
	private int[] skalarA;
	private int[] skalarB;
	private int[] zwischenErgebnis;
	private double ergebnis;

	public Skalarprodukt(int id, int worker, String client, int[] skalarA,
			int[] skalarB) {
		super(id, worker, client);
		this.skalarA = skalarA;
		this.skalarB = skalarB;
	}

	public int[] getSkalarA() {
		return skalarA;
	}

	public void setSkalarA(int[] skalarA) {
		this.skalarA = skalarA;
	}

	public int[] getSkalarB() {
		return skalarB;
	}

	public void setSkalarB(int[] skalarB) {
		this.skalarB = skalarB;
	}

	public int[] getZwischenErgebnis() {
		return zwischenErgebnis;
	}

	public void setZwischenErgebnis(int[] zwischenErgebnis) {
		this.zwischenErgebnis = zwischenErgebnis;
	}

	public double getErgebnis() {
		return ergebnis;
	}

	public void setErgebnis(double ergebnis) {
		this.ergebnis = ergebnis;
	}
}
