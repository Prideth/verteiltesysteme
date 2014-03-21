package worker;

import shared.Matrizenauftrag;

class MatrixThread extends Thread {
	
	private Worker worker;
	private Matrizenauftrag matrix;
	
	public MatrixThread(Worker worker, Matrizenauftrag matrix) {
		super();
		this.worker = worker;
		this.matrix = matrix;
	}

	public void run() {
		worker.working = true;
		
		int ergebnis = matrixmultiplikation(matrix.getZeile(), matrix.getSpalte());
		matrix.setErgebnis(ergebnis);
		worker.out(matrix);
		
		worker.working = false;
	}
	
	public int matrixmultiplikation(int [] zeile, int [] spalte){
		int result = 0;
		
		for(int i = 0; i<= zeile.length; i++){
			result += zeile[i] * spalte[i];
		}
			
		
		return result;
 	}
}