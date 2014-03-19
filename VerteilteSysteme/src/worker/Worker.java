package worker;

import java.io.*;
import java.net.*;

public class Worker {
	final static int PORT = 6666;
	final static String IP = "localhost";

	public Worker worker;
	private static Socket socket;
	private static Object inputObject;
	private static ObjectInputStream input;
	private static ObjectOutputStream output;
	private static boolean connected = false;

	public static void main(String[] args) {
		new Worker();
	}

	public Worker() {
		worker = this;

		while (true) {
			isConnected();
			readInput();
			analyseInput();
		}
	}

	private static void isConnected() {
		if (connected == false) {
			while (true) {
				try {
					socket = new Socket(IP, PORT);
					input = new ObjectInputStream(socket.getInputStream());
					output = new ObjectOutputStream(socket.getOutputStream());

					if (socket.isConnected()) {
						connected = true;
						break;
					}
				} catch (UnknownHostException e) {
				} catch (IOException e) {
				}
			}
		}
	}

	private static void readInput() {
		inputObject = null;
		try {
			inputObject = input.readObject();
		} catch (Exception e) {
			connected = false;
			close();
		}
	}

	private void analyseInput() {
		/*if (inputObject instanceof SerializedFile) {
			serializedFile();
		}*/
	}

	/*private static void serializedFile() {
		SerializedFile file = (SerializedFile) inputObject;

		String directory = file.getFilePath();
		File f = new File(directory);
		if (f.isDirectory()) {
		} else {
			f.mkdir();
		}

		file.save(directory);
		file = null;
		f = null;
	}*/

	private static void close() {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException e1) {
		}

		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException e1) {
		}

		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e1) {
		}
	}

	public boolean out(Object object) {
		try {
			output.writeObject(object);
			output.flush();
			return true;
		} catch (IOException e) {
			close();
			connected = false;
			return false;
		}
	}
	
	
	
	public int matrixmultiplikation(int [] zeile, int [] spalte){
		int result = 0;
		for(int i = 0; i<= zeile.length; i++){
			result += zeile[i] * spalte[i];
		}
			
		
		return result;
 	}
	
	
	public int sklara(int [] zahl, boolean addieren){
		int ergebnis;
		
		if(addieren = false)
		{
			ergebnis= 1;
			
			for(int i = 0; i< zahl.length; i++){
			
				ergebnis *= zahl[i];
			}
		}else{
			ergebnis = 0;
			
			for(int i = 0; i< zahl.length; i++){
				ergebnis += zahl[i];
			}
		}
			
		return ergebnis;
	}
}