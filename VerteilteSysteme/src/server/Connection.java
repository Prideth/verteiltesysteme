package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JFrame;

import shared.Auftrag;
import shared.Matrizenauftrag;
import shared.Matrizenmultiplikation;
import shared.Matrizenverwaltung;
import shared.Skalarauftrag;
import shared.Skalarprodukt;
import shared.Skalarverwaltung;
import shared.Status;

import java.net.Socket;
import java.util.Iterator;
import java.util.List;

public class Connection extends Thread {
	public JFrame frmAdministration;
	public Socket socket;
	private Object inputObject;
	private Object[] deliever = new Object[3];
	private static ObjectInputStream input;
	private static ObjectOutputStream output;
	private Object lastInput;
	private Aufgabenverwaltung aVerwaltung = null;
	private List<Threadaufgabe> threadAufgabeList = null;
	private Workerverwaltung workerverwaltung;
	private Threadaufgabe aufgabe;

	public void run() {
		while (true) {
			readInput();
			analyseInput();
		}
	}

	public Object[] getLastInput() {
		return deliever;
	}
	
	public void setLastInput() {
		deliever[0] = null;
	}

	public Connection(Socket socket, Workerverwaltung workerverwaltung) {
		this.socket = socket;
		this.workerverwaltung = workerverwaltung;
		try {
			OutputStream os = socket.getOutputStream();
			os.flush();
			InputStream is = socket.getInputStream();

			output = new ObjectOutputStream(new BufferedOutputStream(os));
			output.flush();
			input = new ObjectInputStream(new BufferedInputStream(is));
		} catch (IOException e) {
		}
	}

	public void readInput() {
		deliever[1] = this;
		inputObject = null;
		try {
			inputObject = input.readObject();
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		}
		deliever[0] = inputObject;
		lastInput	= inputObject;
	}
	
	public void analyseInput() {
		if (inputObject instanceof Matrizenmultiplikation) {
			
			  ((Matrizenmultiplikation) inputObject).setClient(this);
			  aVerwaltung.add((Matrizenmultiplikation) inputObject);
			  aufgabe = new Threadaufgabe( (Matrizenmultiplikation)
					  inputObject, new Matrizenverwaltung(
			  (Matrizenmultiplikation) inputObject, this));
			  threadAufgabeList.add(aufgabe); Matrizenverwaltung mv =
			  ((Matrizenverwaltung) aufgabe .getVerwalter()); mv.splitt();
			  Auftrag a = mv.getNextAuftrag(); while (a != null) {
			  Connection c = workerverwaltung.checkFreeWorker(); if (c !=
			  null) { aufgabe.addWorker(c); workerOutput(c, a); a =
			  mv.getNextAuftrag(); } }
			 
		} else if (inputObject instanceof Skalarprodukt) {

			((Skalarprodukt) inputObject).setClient(this);
			aVerwaltung.add((Skalarprodukt) inputObject);
			aufgabe = new Threadaufgabe((Skalarprodukt) inputObject,
					new Skalarverwaltung((Skalarprodukt) inputObject,
							this));
			threadAufgabeList.add(aufgabe);
			Skalarverwaltung mv = ((Skalarverwaltung) aufgabe
					.getVerwalter());
			mv.splitt();
			Auftrag a = mv.getnextAuftrag();
			while (a != null) {
				Connection c = workerverwaltung.checkFreeWorker();
				if (c != null) {
					aufgabe.addWorker(c);
					workerOutput(c, a);
					a = mv.getnextAuftrag();
				}
			}

		} else if (inputObject instanceof Status) {
			int status = aVerwaltung.getStatus((this));
			((Status) inputObject).setErgebnis(status);
			writeMsg(inputObject);
		}else if (inputObject instanceof Matrizenauftrag) {
			workerverwaltung.unlockWorker(this);
			for (Iterator<Threadaufgabe> iterator = threadAufgabeList
					.iterator(); iterator.hasNext();) {
				Threadaufgabe aktuelleAufgabe;
				aktuelleAufgabe = iterator.next();
				if (aktuelleAufgabe.contains(this)) {
					if (((Matrizenverwaltung) aktuelleAufgabe.getVerwalter()).empfangeergebnis(
							((Matrizenauftrag) inputObject)
									.getErgebnis(), this)) {
						clientOutput(
								aktuelleAufgabe.getAufgabe().getClient(),((Matrizenverwaltung) aktuelleAufgabe.getVerwalter()).getMatrizenmultiplikation());
					}

					aktuelleAufgabe.removeWorker(this);

				}
			}

		} else if (inputObject instanceof Skalarauftrag) {
			workerverwaltung.unlockWorker(this);
			for (Iterator<Threadaufgabe> iterator = threadAufgabeList
					.iterator(); iterator.hasNext();) {
				Threadaufgabe aktuelleAufgabe;
				aktuelleAufgabe = iterator.next();
				if (aktuelleAufgabe.contains(this)) {
					if (((Skalarauftrag) inputObject)
							.isAddieren()) {
						Skalarprodukt endergebnis = ((Skalarverwaltung) aktuelleAufgabe
								.getVerwalter())
								.empfangegebnis((Skalarauftrag) inputObject);
						clientOutput(aktuelleAufgabe
								.getAufgabe().getClient(),
								endergebnis);
					} else {
						Auftrag tempauftrag = ((Skalarverwaltung) aktuelleAufgabe
								.getVerwalter())
								.empfangezwischenergebnis(
										this,
										((Skalarauftrag) inputObject));
						if (tempauftrag instanceof Skalarauftrag) {
							Connection c = workerverwaltung
									.checkFreeWorker();
							workerOutput(c, tempauftrag);
							aktuelleAufgabe.addWorker(c);

						}
					}
					aktuelleAufgabe.removeWorker(this);

				}
			}
		}
	}
	
	
	private void clientOutput(final Connection client, final Object output) {
		new Thread() {
			@Override
			public void run() {
				client.writeMsg(output);
			}
		}.start();
	}

	private void workerOutput(final Connection worker, final Object output) {
		new Thread() {
			@Override
			public void run() {
				worker.writeMsg(output);
			}
		}.start();
	}
	

	@SuppressWarnings("deprecation")
	public boolean writeMsg(Object msg) {
		boolean status = false;
		try {
			output.writeObject(msg);
			output.flush();
			status = true;
		} catch (IOException e) {
			close();
			stop();
		}
		return status;
	}

	private void close() {
		try {
			if (input != null)
				input.close();
		} catch (Exception e) {
		}

		try {
			if (output != null)
				output.close();
		} catch (Exception e) {
		}

		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {
		}
	}
}