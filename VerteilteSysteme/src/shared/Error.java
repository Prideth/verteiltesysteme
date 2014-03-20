package shared;

import java.io.Serializable;

public class Error implements Serializable {
	private static final long serialVersionUID = 5798996063927290125L;
	private int jobNummer;
	private String text;
	
	Error(int jobNummer, String text) {
		this.jobNummer = jobNummer;
		this.text = text;
	}
	
	public int getJobNummer() {
		return jobNummer;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setJobNummer(int jobNummer) {
		this.jobNummer = jobNummer;
	}
}
