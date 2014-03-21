package worker;

import shared.Skalarauftrag;

class SkalarThread extends Thread {
	
	private Worker worker;
	private Skalarauftrag skalar;
	
	public SkalarThread(Worker worker, Skalarauftrag skalar) {
		super();
		this.worker = worker;
		this.skalar = skalar;
	}

	public void run() {
		worker.working = true;
		
		int ergebnis = sklara(skalar.getDaten(), skalar.isAddieren());
		skalar.setErgebnis(ergebnis);
		worker.out(skalar);
		
		worker.working = false;
	}
	
	
	public int sklara(int [] zahl, boolean addieren){
		int ergebnis;
		
		if(addieren = false)
		{
			ergebnis= 1;
			
			for(int i = 0; i< zahl.length; i++){
			
				ergebnis *= zahl[i];
			}
		}else{
			ergebnis = 0;
			
			for(int i = 0; i< zahl.length; i++){
				ergebnis += zahl[i];
			}
		}
			
		return ergebnis;
	}
	
}