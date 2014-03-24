package server;

import java.util.LinkedList;
import java.util.List;

import shared.Matrizenauftrag;
import shared.Matrizenmultiplikation;
import shared.Matrizenverwaltung;
import shared.Skalarprodukt;
import shared.Status;

/**
 *
 * @author Andreas
 */
public class Threadverwalter {

    private volatile Listener clientListener;
    private volatile Listener workerListener;
    private volatile Aufgabenverwaltung aVerwaltung = null;
    private volatile List<Threadaufgabe> threadAufgabeList = null;
    
    
    Threadverwalter(){
       throw new UnsupportedOperationException("It is not supported to use threadverwalter without client and workerlistener."); 
    }
    
    Threadverwalter(Listener listenerClient, Listener listenerWorker) {
        this.clientListener = listenerClient;
        this.workerListener = listenerWorker; 
        aVerwaltung = new Aufgabenverwaltung();
        threadAufgabeList= new LinkedList<Threadaufgabe>();
        run();
    }
    
    
    private void run(){
        new Thread() {
            @Override public void run() {
                while(true){
                    checkClientInput();
                    checkWorkerInput();
                } 
            }
        }.start();
    }
    
    private void checkClientInput(){
        new Thread() {
            @Override public void run() {
                for(int i= 0; i< clientListener.getConnections(); i++){
                    Connection client = clientListener.getConnection(i);
                    Object[] lastInput = client.getLastInput();
                    if(lastInput != null){
                        if(lastInput[0] instanceof Matrizenmultiplikation){
                            ((Matrizenmultiplikation)lastInput[0]).setClient(client);
                            aVerwaltung.add((Matrizenmultiplikation)lastInput[0]);
                            threadAufgabeList.add(new Threadaufgabe((Matrizenmultiplikation)lastInput[0], new Matrizenverwaltung((Matrizenmultiplikation)lastInput[0], client)));
                        } else if ( lastInput[0] instanceof Skalarprodukt){
                            ((Skalarprodukt)lastInput[0]).setClient(client);
                            aVerwaltung.add((Skalarprodukt)lastInput[0]);
                            threadAufgabeList.add(new Threadaufgabe((Skalarprodukt)lastInput[0], new Skalarverwaltung((Skalarprodukt)lastInput[0]),client));
                        } else if ( lastInput[0] instanceof Status){
                            int status = aVerwaltung.getStatus((client));
                            ((Status) lastInput[0]).setErgebnis(status);
                            clientOutput(client, lastInput[0]);

                        }
                    }
                }
            }
       }.start();
        
    }
    
    private void checkWorkerInput(){
          new Thread() {
            @Override public void run() {
                for(int i= 0; i< workerListener.getConnections(); i++){
                    Connection worker = workerListener.getConnection(i);
                    Object[] lastInput = worker.getLastInput();
                    if(lastInput != null){
                        if(lastInput[0] instanceof Matrizenauftrag){
                            ((Matrizenauftrag)lastInput[0]).setClient(worker);
                            threadAufgabeList.add(new Threadaufgabe((Matrizenauftrag)lastInput[0], new Matrixverwalter()));
                        } 
                    }
                }
            }
       }.start();
       
    }
    
    private void clientOutput(final Connection client, final Object output){
        new Thread() {
            @Override public void run() {
                client.writeMsg(output);
            }
       }.start();
    }
    
    private void workerOutput(final Connection worker, final Object output){
      new Thread() {
            @Override public void run() {
                worker.writeMsg(output);
            }
       }.start();
    }
    
    
   
    
}