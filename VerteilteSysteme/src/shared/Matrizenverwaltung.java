package shared;

import server.Connection;
import shared.Matrizenauftrag;
import shared.Matrizenmultiplikation;

public class Matrizenverwaltung implements Verwalter {
	private Connection auftraggeber; // Client von dem der Auftrag gestellt wird

	public Connection getAuftraggeber() {
		return auftraggeber;
	}

	public Matrizenmultiplikation getMatrizenmultiplikation() {
		return matrizenmultiplikation;
	}

	private Matrizenmultiplikation matrizenmultiplikation; //
	private Object[][] auftraege; // Teilaufgaben die erzeugt werden; ist ein
									// Object Array,
									// da solange ein Worker am Object arbeitet
									// die Connection
									// dieses Workers gespeichert wird.
	private int auftragszeile = 0;
	private int auftragsspalte = 0;

	// Konstruktor
	// ihm wird die aufgabe übergeben und der Client der den Auftrag stellt
	public Matrizenverwaltung(Matrizenmultiplikation matrizenmultiplikation,
			Connection auftraggeber) {
		super();
		this.matrizenmultiplikation = matrizenmultiplikation;
		this.auftraggeber = auftraggeber;
	}

	public void splitt() {
		Object[][] matrixErgebnis = matrizenmultiplikation.getMatrixErgebnis();
		int[][] MatrixA = matrizenmultiplikation.getMatrixA();
		int[][] MatrixB = matrizenmultiplikation.getMatrixB();
		if (matrixErgebnis == null) {
			// System.out.println(matrixErgebnis);
		} else {
			for (int i = 0; i < matrixErgebnis.length; i++) {
				for (int j = 0; j < matrixErgebnis[i].length; j++) {
					int[] spalte = new int[MatrixA.length];
					int[] zeile = new int[MatrixB[i].length];
					for (int k = 0; (k < MatrixA.length && k < MatrixB[i].length); k++) {
						spalte[k] = MatrixA[i + k][j];
						zeile[k] = MatrixB[i][j + k];
					}
					matrixErgebnis[i][j] = new Matrizenauftrag(zeile, spalte);
				}
			}
			this.matrizenmultiplikation.setMatrixErgebnis(matrixErgebnis);
		}
	}

	public Matrizenauftrag getNextAuftrag() {
		if (auftragszeile < this.matrizenmultiplikation.getMatrixErgebnis()[0].length) {
			if (auftragsspalte < this.matrizenmultiplikation
					.getMatrixErgebnis().length) {
				Matrizenauftrag ma = (Matrizenauftrag) this.matrizenmultiplikation
						.getMatrixErgebnis()[auftragsspalte][auftragszeile];
				auftragsspalte++;
				return ma;
			}
			auftragsspalte = 0;
			auftragszeile++;
		}
		return null;
	}

	// funtion die das empfangene endergebnis an den auftragssteller zurück
	// sendet
	public boolean empfangeergebnis(int ergebnis, Connection connection) {
		boolean fertig = true;
		for (Object k : auftraege) {
			if (k == connection)
				k = ergebnis;
			if (!(k instanceof Integer))
				fertig = false;
		}

		return fertig;
	}

}
