package server;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import shared.Aufgabe;

/**
 *
 * @author Andreas
 */
public class Aufgabenverwaltung {
    
    private List<Aufgabe> aufgaben;
    
    public Aufgabenverwaltung(){
    	aufgaben = new LinkedList<Aufgabe>();
    }
    
    
    public void add(Aufgabe newAufgabe){
        removeAll(newAufgabe.getClient());
    	aufgaben.add(newAufgabe);
    }
    
    public void remove(Aufgabe delAufgabe){
    	aufgaben.remove(delAufgabe);
    }
    
    public void removeAll(Connection client){
        for ( Iterator<Aufgabe> iterator = aufgaben.iterator(); iterator.hasNext();){
            Aufgabe aktuelleAufgabe;
            aktuelleAufgabe = iterator.next();
            if(aktuelleAufgabe.getClient().equals(client)){
                remove(aktuelleAufgabe);
            }
        }
    }
    
    public int getStatus(Connection client){
        for ( Iterator<Aufgabe> iterator = aufgaben.iterator(); iterator.hasNext(); ){
            Aufgabe aktuelleAufgabe;
            aktuelleAufgabe = iterator.next();
            if(aktuelleAufgabe.getClient().equals(client)){
                return aktuelleAufgabe.getStatus();
            }
        }
        return -1;
    }
    
    public List<Aufgabe> getAufgaben(String client){
        List<Aufgabe> clientAufgaben = new LinkedList<Aufgabe>();
        for ( Iterator<Aufgabe> iterator = aufgaben.iterator(); iterator.hasNext(); ){
            Aufgabe aktuelleAufgabe;
            aktuelleAufgabe = iterator.next();
            if(aktuelleAufgabe.getClient().equals(client)){
                clientAufgaben.add(aktuelleAufgabe);
            }
        }
        return clientAufgaben;
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
