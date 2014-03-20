package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import shared.Aufgabe;

/**
 *
 * @author Andreas
 */
public class Aufgabenverwaltung {
    
    private List<Aufgabe> aufgaben = new LinkedList<Aufgabe>();
    
    public Aufgabenverwaltung(){
    	
    }
    
    
    public void add(Aufgabe newAufgabe){
    	aufgaben.add(newAufgabe);
    }
    
    public void remove(Aufgabe delAufgabe){
    	aufgaben.remove(delAufgabe);
    }
    
    
    public Aufgabe deliefer(){
        Aufgabe fertigeAufgabe = null;
        for ( Iterator<Aufgabe> iterator = aufgaben.iterator(); iterator.hasNext(); ){
            Aufgabe aktuelleAufgabe;
            aktuelleAufgabe = iterator.next();
            if(aktuelleAufgabe.getStatus()== 100){
                fertigeAufgabe = aktuelleAufgabe;
            }
        }
        aufgaben.remove(fertigeAufgabe);
        return fertigeAufgabe;
    }
    
    
    
    
    
    
    
}
