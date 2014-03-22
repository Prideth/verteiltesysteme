package server;

import java.util.LinkedList;
import java.util.List;
import shared.Matrizenmultiplikation;
import shared.Skalarprodukt;
import shared.Status;

/**
 *
 * @author Andreas
 */
public class Threadverwalter {

    private Listener clientlistener;
    private Listener workerListener;
    private Aufgabenverwaltung aVerwaltung = null;
    private List<Threadaufgabe> threadAufgabeList = null;
    
    
    Threadverwalter(){
       throw new UnsupportedOperationException("It is not supported to use threadverwalter without client and workerlistener."); 
    }
    
    Threadverwalter(Listener listenerClient, Listener listenerWorker) {
        this.clientlistener = listenerClient;
        this.workerListener = listenerWorker; 
        aVerwaltung = new Aufgabenverwaltung();
        threadAufgabeList= new LinkedList<Threadaufgabe>();
        run();
    }
    
    
    private void run(){
        while(true){
            checkClientInput();
            checkWorkerInput();
        
        }
    }
    
    private void checkClientInput(){
        for(int i= 0; i< clientlistener.getConnections(); i++){
            Connection client = clientlistener.getConnection(i);
            Object[] lastInput = client.getLastInput();
            if(lastInput != null){
                if(lastInput[0] instanceof Matrizenmultiplikation){
                    ((Matrizenmultiplikation)lastInput[0]).setClient(client);
                    aVerwaltung.add((Matrizenmultiplikation)lastInput[0]);
                    threadAufgabeList.add(new Threadaufgabe((Matrizenmultiplikation)lastInput[0], new Matrixverwalter()));
                } else if ( lastInput[0] instanceof Skalarprodukt){
                    ((Skalarprodukt)lastInput[0]).setClient(client);
                    aVerwaltung.add((Skalarprodukt)lastInput[0]);
                    threadAufgabeList.add(new Threadaufgabe((Skalarprodukt)lastInput[0], new Skalarverwalter()));
                } else if ( lastInput instanceof Status){
                    aVerwaltung.getStatus(((Status)lastInput[0]));
                }
            }
        }
    }
    
    private void checkWorkerInput(){
        for(int i= 0; i< workerListener.getConnections(); i++){ 
            
        }
    }
    
    private void clientOutput(String client){
        for(int i=0; i < clientlistener.getConnections(); i++){
            if(clientlistener.getConnection(i).){
                
            }
        }
    }
    
    
   
    
}
