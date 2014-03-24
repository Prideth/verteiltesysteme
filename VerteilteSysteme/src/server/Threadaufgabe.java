
package server;

import shared.Aufgabe;
import shared.Verwalter;

/**
 *
 * @author Andreas
 */
public class Threadaufgabe {
    private Aufgabe aufgabe;
    private Verwalter verwalter;
    
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
}
