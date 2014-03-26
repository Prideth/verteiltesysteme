package server;

public class Workerverwaltung {
	private Workerstatus[] workerstatus;

	public Workerverwaltung() {
		super();
	}
	
	public void initial (Listener workerlistener){
		int i = workerlistener.getConnections();
		workerstatus = new Workerstatus[i];
		for (int y=0; y < i; y++){
			Workerstatus ws = new Workerstatus(workerlistener.getConnection(y));
			workerstatus[y]= ws;
		}
	}
	
	public void updateWorkerList (Listener workerlistener){
		Workerstatus[] tempworkerstatuslist = this.workerstatus;
		initial(workerlistener);
		for (int i = 0; i < tempworkerstatuslist.length; i++){
			for (int y = 0 ; y < workerstatus.length; y++){
				if (tempworkerstatuslist[i].getConnection() == workerstatus[y].getConnection()){
					if (tempworkerstatuslist[i].isLocked())
						workerstatus[y].lock();
				}
			}
		}
	}
	
	public Connection checkFreeWorker(){
		for (Workerstatus e: workerstatus){
			if (!e.isLocked()){
				e.lock();
				return e.getConnection();
			}
		}
		return null;
	}

	public void unlockWorker(Connection connection){
		for (Workerstatus e: workerstatus){
			if (e.getConnection() == connection){
				e.unlock();
				break;
			}
		}
	}

}
