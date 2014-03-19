package shared;

import java.io.Serializable;

public class Matrizenmultiplikation extends Aufgabe implements Serializable {
	private static final long serialVersionUID = -6439674567738452423L;
	private int[][] matrixA;
	private int[][] matrixB;
	private int[][] matrixErgebnis;
	private int status;

	public Matrizenmultiplikation(int id, int worker, String client,
			int[][] matrixA, int[][] matrixB) {
		super(id, worker, client);
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.status = 0;
	}

	public int[][] getMatrixA() {
		return matrixA;
	}

	public void setMatrixA(int[][] matrixA) {
		this.matrixA = matrixA;
	}

	public int[][] getMatrixB() {
		return matrixB;
	}

	public void setMatrixB(int[][] matrixB) {
		this.matrixB = matrixB;
	}

	public int[][] getMatrixErgebnis() {
		return matrixErgebnis;
	}

	public void setMatrixErgebnis(int[][] matrixErgebnis) {
		this.matrixErgebnis = matrixErgebnis;
	}

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
}
