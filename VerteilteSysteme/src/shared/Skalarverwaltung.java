package shared;

import server.Connection;

//Klasse zum aufteilen und verarbeiten einer Skalarproduktaufgabe
public class Skalarverwaltung {
	private Connection auftraggeber; // Client von dem der Auftrag gestellt wird
	private Skalarprodukt skalarprodukt; // Skalarprodukt das verarbeitet werden
											// soll
	private Object[] auftraege; // Teilaufgaben die erzeugt werden; ist ein
								// Object Array,
								// da solange ein Worker am Object arbeitet die
								// Connection
								// dieses Workers gespeichert wird.
	int maxworker;
	int currentworker = 0;
	boolean multiplikationfertig = false;

	// Konstruktor
	// ihm wird die aufgabe übergeben und der Client der den Auftrag stellt
	public Skalarverwaltung(Skalarprodukt skalarprodukt, Connection auftraggeber) {
		super();
		this.skalarprodukt = skalarprodukt;
		this.auftraggeber = auftraggeber;
		this.maxworker = this.skalarprodukt.getWorker();
	}

	// Funktion die dafür Verantwortlich ist die gestellte Aufgabe in einzelne
	// Teilaufgaben zu zerteilen.
	// diese Teilaufgaben werden in das auftreage array geschrieben
	public void splitt() {
		int size;
		int[] auftrag = new int[2];
		if (this.skalarprodukt.getSkalarA().length < this.skalarprodukt
				.getSkalarB().length) {
			size = this.skalarprodukt.getSkalarB().length;
		} else {
			size = this.skalarprodukt.getSkalarA().length;
		}
		auftraege = new Object[size];
		for (int i = 0; i < size; i++) {
			if (size < this.skalarprodukt.getSkalarA().length)
				auftrag[0] = this.skalarprodukt.getSkalarA()[i];
			else
				auftrag[0] = 0;
			if (size < this.skalarprodukt.getSkalarA().length)
				auftrag[1] = this.skalarprodukt.getSkalarB()[i];
			else
				auftrag[1] = 0;
			auftraege[i] = auftrag;
		}
	}
	
	// Function die die teilaufgaben aus dem auftreage array an die ihm
	// übergebenen workerconnections weiter gibt
	// nachdem eine tailaufgabe versendet wurde, wird an die stelle der aufgabe
	// im array die workerconnection geschrieben an die die aufgabe weiter
	// gegeben wurde
	public void sendauftraege(Connection[] workerconnections) {
		Connection freeworker = null;
		while (!multiplikationfertig) {
			while (this.currentworker <= this.maxworker) {
				for (Connection k : workerconnections) {
					//TODO 
					// sende freizeichenanfrage an worker
					// if (k freier worker){
					// freeworker = k;
					// }
				}
				int[] daten = new int[2];
				for (int i = 0; i < auftraege.length; i++)
					daten = (int[]) auftraege[i];
				Skalarauftrag auftrag = new Skalarauftrag(false, daten);
				if (freeworker != null){
				  freeworker.writeMsg(auftrag);
				  this.currentworker++;
				}
			}
		}
	}
	
	public Object getnextAuftrag(){
		
		return ;
		
		
	}

	// funktion die das ergebnis der berechnung zusammen mit der
	// workerconnection übergeben bekommt
	// anhand dieser workerconnection wird nachgeschaut welche teilaufgabe
	// erledigt wurde und das ergebnis an die passende stelle eingetragen
	// nachdem alle connections durch ergebnisse ersetzt wurden, wird die
	// sendeproduktauftrag funktion aufgerufen
	public void empfangezwischenergebnis(Connection connection, Skalarauftrag  ergebnis) {
		boolean addieren = false;
		for (int i = 0; i < auftraege.length; i++) {
			if (auftraege[i] == connection) {
				auftraege[i] = ergebnis.getErgebnis();
				this.currentworker--;
			}
		}
		for (Object k : auftraege) {
			addieren = true;
			if (!(k instanceof Integer)) {
				addieren = false;
				this.multiplikationfertig = true;
			}
		}
		if (addieren)
			sendproduktauftrag(connection);
	}

	// funktion die die teilergebnise zum zusammenrechnen an einen worker weiter
	// gibt
	public void sendproduktauftrag(Connection connection) {
		int[] daten = new int[auftraege.length];
		for (int i = 0; i < auftraege.length; i++)
			daten[i] = (int) auftraege[i];
		Skalarauftrag auftrag = new Skalarauftrag(true, daten);
		connection.writeMsg(auftrag);
	}

	// funtion die das empfangene endergebnis an den auftragssteller zurück
	// sendet
	public void empfangegebnis(Connection connection, Skalarauftrag  ergebnis) {
		this.skalarprodukt.setErgebnis(ergebnis.getErgebnis());
		connection.writeMsg(this.skalarprodukt);
	}

}
