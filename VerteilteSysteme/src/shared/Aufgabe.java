package shared;

import server.Connection;

public class Aufgabe {
	private int id;
	private int worker;
	private Connection client;

	public Aufgabe(int id, int worker, Connection client) {
		super();
		this.id = id;
		this.worker = worker;
		this.client = client;
	}

	public int getId() {
		return id;
	}
	
	public int getWorker() {
		return worker;
	}

	public Connection getClient() {
		return client;
	}
	
	public void setClient(Connection client){
		this.client = client;
	}
	
	
        
        
    public int getStatus() {
		return 0;
	}
}
