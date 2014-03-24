package shared;

import java.io.Serializable;

public class Status extends Auftrag implements Serializable {
	private static final long serialVersionUID = -9105381234844412376L;
	private int jobNummer;
	private int ergebnis;
	
	public Status(int jobNummer) {
        super();
		this.jobNummer = jobNummer;
	}
	
	public int getJobNummer() {
		return jobNummer;
	}
	
	public int getErgebnis() {
		return ergebnis;
	}
	
	public void setErgebnis(int ergebnis) {
		this.ergebnis = ergebnis;
	}
}
