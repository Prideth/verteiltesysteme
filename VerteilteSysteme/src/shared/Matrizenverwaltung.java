package shared;

import server.Connection;

public class Matrizenverwaltung {
	private Connection auftraggeber;      //Client von dem der Auftrag gestellt wird
	private Matrizenmultiplikation matrizenmultiplikation;  //
	private Object[][] auftraege;		  //Teilaufgaben die erzeugt werden; ist ein Object Array, 
                                       	  //da solange ein Worker am Object arbeitet die Connection
	                                      //dieses Workers gespeichert wird.
	
	//Konstruktor 
	//ihm wird die aufgabe übergeben und der Client der den Auftrag stellt
	public Matrizenverwaltung(Matrizenmultiplikation matrizenmultiplikation, Connection auftraggeber) {
		super();
		this.matrizenmultiplikation = matrizenmultiplikation;
		this.auftraggeber = auftraggeber;
	}
	
	public void splitt(){
		Object[][] matrixErgebnis = matrizenmultiplikation.getMatrixErgebnis();
		int[][] MatrixA 		  = matrizenmultiplikation.getMatrixA();
		int[][] MatrixB 		  = matrizenmultiplikation.getMatrixB();
		
		for( int i=0; i < matrixErgebnis.length; i++){
			for( int j=0; j<  matrixErgebnis[i].length; j++){
				int[] spalte = new int[MatrixA.length];
				int[] zeile  = new int[MatrixB[i].length];
				for (int k=0; (k < MatrixA.length && k < MatrixB[i].length); k++){
					spalte[k]	=	MatrixA[i+k][j];
					zeile[k] 	=	MatrixB[i][j+k];
				}
				matrixErgebnis[i][j] = new Matrizenauftrag(zeile, spalte);
			}
		}
	
	}
	
	
	
	//funtion die das empfangene endergebnis an den auftragssteller zurück sendet
	public void empfangeergebnis(int ergebnis,Connection connection){
		boolean fertig = true;
		for (Object k : auftraege){
			if (k == connection)
				k = ergebnis;
			if (!(k instanceof Integer))
				fertig = false;
		}
		if (fertig){
		  Object[][] ergebnismatrix = new Object[1][1];
		  ergebnismatrix[0][0] = ergebnis;
		  this.matrizenmultiplikation.setMatrixErgebnis(ergebnismatrix);
		  //TODO sende ergebnis an client
		}
	}
	
}
