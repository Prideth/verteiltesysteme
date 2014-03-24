package shared;

public class Aufgabe {
	private int id;
	private int worker;
	private String client;

	public Aufgabe(int id, int worker, String client) {
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

	public String getClient() {
		return client.toString();
	}
        
        
        public int getStatus() {
		return 0;
	}
}
