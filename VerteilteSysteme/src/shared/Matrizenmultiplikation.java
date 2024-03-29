package shared;

import java.io.Serializable;

import server.Connection;

/**
 * 
 * 
 * 
 * @author Andreas
 */
public class Matrizenmultiplikation extends Aufgabe implements Serializable {
	private static final long serialVersionUID = -6439674567738452423L;
	private int[][] matrixA;
	private int[][] matrixB;
	private Object[][] matrixErgebnis;

	public Matrizenmultiplikation(int id, int worker, Connection client,
			int[][] matrixA, int[][] matrixB) {
		super(id, worker, client);
		this.matrixA = matrixA;
		this.matrixB = matrixB;
		this.matrixErgebnis = new Object[matrixA.length][matrixB[0].length];
		for(int i = 0; i < matrixA.length; i++){
			for(int j=0; j < matrixB[i].length; j++){
				matrixErgebnis[i][j] = new EmptyObject();
			}
		}
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
		if(matrixErgebnis == null)
			System.exit( 0 );
		return matrixErgebnis;
	}

	public void setMatrixErgebnis(Object[][] matrixErgebnis) {
		this.matrixErgebnis = matrixErgebnis;
	}

        public void setStelle(int spalte, int zeile, Integer zahl){
            matrixErgebnis[spalte][zeile] = zahl;
        }
        
        public Object getStelle(int spalte, int zeile){
            return matrixErgebnis[spalte][zeile];
        }
        
        
        /**
         * 
         * Berechnet die Prozentzahl wie viel schon fertiggestellt wurde
         * 
         * @return liefert zurück zu wievie Prozent es schon fertig gestellt ist
         */
        @Override
	public int getStatus() {
		double gesamt = 0;
		double fertig = 0;
		
		for(int i=0; i < matrixErgebnis.length; i++){
			for (int j=0; j < matrixErgebnis[i].length; j++){
				if(matrixErgebnis[i][j] instanceof Integer)
					fertig++;
				
				gesamt++;
			}
		}
		
		return (int) (fertig*100/gesamt);
	}

}
