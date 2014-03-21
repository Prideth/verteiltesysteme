package worker;

import java.io.*;
import java.net.*;

import shared.Matrizenauftrag;
import shared.Skalarauftrag;
import shared.Workcheck;

public class Worker {
	final static int PORT = 6666;
	final static String IP = "localhost";

	public Worker worker;
	private static Socket socket;
	private static Object inputObject;
	private static ObjectInputStream input;
	private static ObjectOutputStream output;
	private static boolean connected = false;
	public boolean working;
	
	SkalarThread test;

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
		if (inputObject instanceof Matrizenauftrag) {
			Matrizenauftrag mauftrag = (Matrizenauftrag) inputObject;
			MatrixThread test = new MatrixThread(worker, mauftrag);
			test.start();
		} else if (inputObject instanceof Skalarauftrag){
			Skalarauftrag sauftrag = (Skalarauftrag) inputObject;
			SkalarThread test = new SkalarThread(worker, sauftrag);
			test.start();
		} else if (inputObject instanceof Workcheck){
			Workcheck check = (Workcheck) inputObject;
			check.setWorking(working);
			out(check);
		}
	}

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
	
	
	
	
	
	
	
}