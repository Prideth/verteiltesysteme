package client;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import shared.Matrizenmultiplikation;
import shared.Skalarprodukt;
import shared.Status;
import shared.Error;

public class SocketThread extends Thread {
	final static int PORT = 5555;
	final static String IP = "localhost";
	
	private static Client client;
	public static Socket socket;
	private static Object inputObject;
	private static ObjectInputStream input;
	private static ObjectOutputStream output;
	private static boolean connected = false;

	public SocketThread(Client client) {
		this.client = client;
	}

	public void run() {
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

	private static void analyseInput() {
		if (inputObject instanceof Matrizenmultiplikation) {
			Matrizenmultiplikation ergebnisMatrize = (Matrizenmultiplikation) inputObject;
			if(ergebnisMatrize.getId() == client.getJobNummer()) {
				Object[][] objectErgebnis = ergebnisMatrize.getMatrixErgebnis();
				
				//int[][] integerErgebnis = new int[objectErgebnis[0].length][objectErgebnis.length];
				client.setErgebnisMatrize(objectErgebnis);
			}
		} else if (inputObject instanceof Skalarprodukt) {
			Skalarprodukt ergebnisSkalar = (Skalarprodukt) inputObject;
			if(ergebnisSkalar.getId() == client.getJobNummer()) {
				Object objectErgebnis = ergebnisSkalar.getErgebnis();
				client.textField_1.setText(String.valueOf(objectErgebnis));
			}
		} else if (inputObject instanceof Status) {
			Status ergebnisStatus = (Status) inputObject;
			if(ergebnisStatus.getId() == client.getJobNummer()) {
				int ergebnis = ergebnisStatus.getErgebnis();
				if(ergebnis == -1) {
					JOptionPane.showMessageDialog(
							client.frmVerteilteBerechnung,
							"Status konnte nicht abgefragt werden. Aufgabe wurde nicht gefunden.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(
							client.frmVerteilteBerechnung,
							"Status: " + ergebnis + " %",
							"Info", JOptionPane.INFORMATION_MESSAGE);
				}	
			}
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