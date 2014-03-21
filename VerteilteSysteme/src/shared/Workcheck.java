package shared;

import java.io.Serializable;

public class Workcheck implements Serializable{

	private static final long serialVersionUID = -2053800578085108839L;
	private boolean working;
	
	public Workcheck(){
	}
	public boolean getWorking(){
		return working;
	}
	
	public void setWorking(boolean working){
		this.working = working;
	}
}
