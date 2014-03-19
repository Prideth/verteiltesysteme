package shared;

import java.io.Serializable;

public class Matrizenmultiplikation extends Aufgabe implements Serializable {
	private static final long serialVersionUID = -6439674567738452423L;
	private int[][] matrixA;
	private int[][] matrixB;
	private Object[][] matrixErgebnis;

	public Matrizenmultiplikation(int id, int worker, String client,
			int[][] matrixA, int[][] matrixB) {
		super(id, worker, client);
		this.matrixA = matrixA;
		this.matrixB = matrixB;
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

	public Object[][] getMatrixErgebnis() {
		return matrixErgebnis;
	}

	public void setMatrixErgebnis(Object[][] matrixErgebnis) {
		this.matrixErgebnis = matrixErgebnis;
	}

	public int getStatus() {
		double gesamt = 0;
		double fertig = 0;
		
		for(int i=0; i < matrixErgebnis.length; i++){
			for (int j=0; j < matrixErgebnis.length; j++){
				//evtl double besser!!
				if(matrixErgebnis[i][j] instanceof Integer)
					fertig++;
				
				gesamt++;
			}
		}
		
		return (int) (fertig*100/gesamt);
	}

}
