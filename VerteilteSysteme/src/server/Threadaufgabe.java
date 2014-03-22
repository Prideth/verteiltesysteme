/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import shared.Aufgabe;

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
    
    public Aufgabe getAzfgabe(){
        return aufgabe;
    }
    
    public Verwalter getVerwalter(){
        return verwalter;
    }
}
