package shared;

public class Matrizenmultiplikation extends Aufgabe {
	private int[][] matrix_eins;
	private int[][] matrix_zwei;
	private Object[][] matrix_ergebnis;

	public Matrizenmultiplikation(int worker, int[][] matrix_eins,
			int[][] matrix_zwei) {
		super(worker);
		this.matrix_eins = matrix_eins;
		this.matrix_zwei = matrix_zwei;
	}

	public int[][] getMatrix_eins() {
		return matrix_eins;
	}

	public void setMatrix_eins(int[][] matrix_eins) {
		this.matrix_eins = matrix_eins;
	}

	public int[][] getMatrix_zwei() {
		return matrix_zwei;
	}

	public void setMatrix_zwei(int[][] matrix_zwei) {
		this.matrix_zwei = matrix_zwei;
	}

	public Object[][] getMatrix_ergebnis() {
		return matrix_ergebnis;
	}

	public void setMatrix_ergebnis(Object[][] matrix_ergebnis) {
		this.matrix_ergebnis = matrix_ergebnis;
	}

}
