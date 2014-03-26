package shared;

import java.io.Serializable;

import server.Connection;

public class Skalarprodukt extends Aufgabe implements Serializable {
	private static final long serialVersionUID = -3307064007787409269L;
	private int[] skalarA;
	private int[] skalarB;
	private Object[] zwischenErgebnis;
	private Object ergebnis;

	public Skalarprodukt(int id, int worker, Connection client, int[] skalarA,
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

	public Object[] getZwischenErgebnis() {
		return zwischenErgebnis;
	}

	public void setZwischenErgebnis(Object[] zwischenErgebnis) {
		this.zwischenErgebnis = zwischenErgebnis;
	}

	public Object getErgebnis() {
		return ergebnis;
	}

	public void setStelle(int stelle, Integer zahl) {
		zwischenErgebnis[stelle] = zahl;
	}

	public Object getStelle(int stelle) {
		return zwischenErgebnis[stelle];
	}

	public void setErgebnis(Object ergebnis) {
		this.ergebnis = ergebnis;
	}

	@Override
	public int getStatus() {
		double gesamt = 0;
		double fertig = 0;
		if(zwischenErgebnis == null)
			return 0;
		for (int i = 0; i < zwischenErgebnis.length; i++) {
			if (zwischenErgebnis[i] instanceof Integer)
				fertig++;
			gesamt++;
		}
		if (ergebnis instanceof Integer)
			fertig++;
		gesamt++;
		return (int) (fertig * 100 / gesamt);
	}
}
