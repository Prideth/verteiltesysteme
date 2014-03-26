
package server;

import java.util.List;

import shared.Aufgabe;
import shared.Verwalter;

/**
 *
 * @author Andreas
 */
public class Threadaufgabe {
    private Aufgabe aufgabe;
    private Verwalter verwalter;
    private List<Connection> worker;
    
    public Threadaufgabe(Aufgabe aufgabe, Verwalter verwalter){
        this.aufgabe = aufgabe;
        this.verwalter = verwalter;
    }
    
    public Aufgabe getAufgabe(){
        return aufgabe;
    }
    
    public Verwalter getVerwalter(){
        return verwalter;
    }

	public List<Connection> getWorker() {
		return worker;
	}

	public void addWorker(Connection worker) {
		this.worker.add(worker);
	}
	
	public void removeWorker(Connection worker){
		this.worker.remove(worker);
	}
	
	public boolean contains(Connection worker){
		if(this.worker.contains(worker)){
			return true;
		}
		return false;
	}
    
    
}
