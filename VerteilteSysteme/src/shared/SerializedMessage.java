package shared;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerializedMessage implements Serializable {
	private static final long serialVersionUID = -2882713700650642843L;
	
	private String message;
	private String date;
	
	public SerializedMessage(String message) {
		this.message = message;
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm.ss");
		date = dateFormat.format(now);
	}

	public String getMessage() {
		return message;
	}
	
	public String getDate() {
		return date;
	}
}