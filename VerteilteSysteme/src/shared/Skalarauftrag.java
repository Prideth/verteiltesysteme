package shared;

public class Skalarauftrag {
	private boolean addieren = false;
	private int[] daten;

	public Skalarauftrag(boolean addieren, int[] daten) {
		super();
		this.addieren = addieren;
		this.daten = daten;
	}

	public boolean isAddieren() {
		return addieren;
	}

	public void setAddieren(boolean addieren) {
		this.addieren = addieren;
	}

	public int[] getDaten() {
		return daten;
	}

	public void setDaten(int[] daten) {
		this.daten = daten;
	}

}
