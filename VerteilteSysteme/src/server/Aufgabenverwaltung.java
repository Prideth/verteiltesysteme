package server;

import java.util.ArrayList;
import java.util.List;
import shared.Aufgabe;

/**
 *
 * @author Andreas
 */
public class Aufgabenverwaltung {
    
    private List<Aufgabe> aufgaben = new ArrayList<Aufgabe>();
    
    public Aufgabenverwaltung(){
    	
    }
    
    
    public void add(Aufgabe newAufgabe){
    	aufgaben.add(newAufgabe);
    }
    
    public void remove(Aufgabe delAufgabe){
    	aufgaben.remove(delAufgabe);
    }
    
    
}
