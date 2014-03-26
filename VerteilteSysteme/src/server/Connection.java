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
	private Workerverwaltung workerVerwaltung;
	private Threadaufgabe aufgabe;

	public void run() {
		while (true) {
			readInput();
			//analyseInput();
		}
	}

	public Object[] getLastInput() {
		return deliever;
	}
	
	public void setLastInput() {
		deliever[0] = null;
	}

	public Connection(Socket socket) {
		this.socket = socket;
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
	}
	
	public void analyseInput() {
		if (lastInput instanceof Matrizenmultiplikation) {
			
			  ((Matrizenmultiplikation) lastInput).setClient(this);
			  aVerwaltung.add((Matrizenmultiplikation) lastInput);
			  aufgabe = new Threadaufgabe( (Matrizenmultiplikation)
			  lastInput, new Matrizenverwaltung(
			  (Matrizenmultiplikation) lastInput, this));
			  threadAufgabeList.add(aufgabe); Matrizenverwaltung mv =
			  ((Matrizenverwaltung) aufgabe .getVerwalter()); mv.splitt();
			  Auftrag a = mv.getNextAuftrag(); while (a != null) {
			  Connection c = workerVerwaltung.checkFreeWorker(); if (c !=
			  null) { aufgabe.addWorker(c); workerOutput(c, a); a =
			  mv.getNextAuftrag(); } }
			 
		} else if (lastInput instanceof Skalarprodukt) {

			((Skalarprodukt) lastInput).setClient(this);
			aVerwaltung.add((Skalarprodukt) lastInput);
			aufgabe = new Threadaufgabe((Skalarprodukt) lastInput,
					new Skalarverwaltung((Skalarprodukt) lastInput,
							this));
			threadAufgabeList.add(aufgabe);
			Skalarverwaltung mv = ((Skalarverwaltung) aufgabe
					.getVerwalter());
			mv.splitt();
			Auftrag a = mv.getnextAuftrag();
			while (a != null) {
				Connection c = workerVerwaltung.checkFreeWorker();
				if (c != null) {
					aufgabe.addWorker(c);
					workerOutput(c, a);
					a = mv.getnextAuftrag();
				}
			}

		} else if (lastInput instanceof Status) {
			int status = aVerwaltung.getStatus((this));
			((Status) lastInput).setErgebnis(status);
			writeMsg(lastInput);
		}else if (lastInput instanceof Matrizenauftrag) {
			workerVerwaltung.unlockWorker(this);
			for (Iterator<Threadaufgabe> iterator = threadAufgabeList
					.iterator(); iterator.hasNext();) {
				Threadaufgabe aktuelleAufgabe;
				aktuelleAufgabe = iterator.next();
				if (aktuelleAufgabe.contains(this)) {
					if (((Matrizenverwaltung) aktuelleAufgabe.getVerwalter()).empfangeergebnis(
							((Matrizenauftrag) lastInput)
									.getErgebnis(), this)) {
						clientOutput(
								aktuelleAufgabe.getAufgabe().getClient(),((Matrizenverwaltung) aktuelleAufgabe.getVerwalter()).getMatrizenmultiplikation());
					}

					aktuelleAufgabe.removeWorker(this);

				}
			}

		} else if (lastInput instanceof Skalarauftrag) {
			workerVerwaltung.unlockWorker(this);
			for (Iterator<Threadaufgabe> iterator = threadAufgabeList
					.iterator(); iterator.hasNext();) {
				Threadaufgabe aktuelleAufgabe;
				aktuelleAufgabe = iterator.next();
				if (aktuelleAufgabe.contains(this)) {
					if (((Skalarauftrag) lastInput)
							.isAddieren()) {
						Skalarprodukt endergebnis = ((Skalarverwaltung) aktuelleAufgabe
								.getVerwalter())
								.empfangegebnis((Skalarauftrag) lastInput);
						clientOutput(aktuelleAufgabe
								.getAufgabe().getClient(),
								endergebnis);
					} else {
						Auftrag tempauftrag = ((Skalarverwaltung) aktuelleAufgabe
								.getVerwalter())
								.empfangezwischenergebnis(
										this,
										((Skalarauftrag) lastInput));
						if (tempauftrag instanceof Skalarauftrag) {
							Connection c = workerVerwaltung
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