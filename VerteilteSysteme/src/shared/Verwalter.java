package shared;

import server.Connection;

public class Verwalter {
	private Object aufgabe = null;
	
	public Verwalter(Object aufgabe) {
		super();
		this.aufgabe = aufgabe;
	}

	public void analysiereaufgabe() {
		// TODO Auto-generated method stub
		if (this.aufgabe instanceof Matrizenmultiplikation)
			bearbeiteMatrix((Matrizenmultiplikation)this.aufgabe);
		else if (this.aufgabe instanceof Skalarprodukt)
			bearbeiteSkalar((Skalarprodukt)this.aufgabe);
		else
			System.err.println("Aufgabenformat fehlerhaft");
	}

	public void bearbeiteSkalar(Skalarprodukt skalar) {
		
		
	}
	
	private void bearbeiteMatrix(Matrizenmultiplikation matrix){
		
	}
}
