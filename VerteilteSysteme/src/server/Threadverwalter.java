package server;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import shared.Auftrag;
import shared.Matrizenauftrag;
import shared.Matrizenmultiplikation;
import shared.Skalarauftrag;
import shared.Skalarprodukt;
import shared.Status;
import shared.Skalarverwaltung;
import shared.Matrizenverwaltung;

public class Threadverwalter extends Thread {

	private volatile Listener clientListener;
	private volatile Listener workerListener;
	private volatile Aufgabenverwaltung aVerwaltung = null;
	private volatile List<Threadaufgabe> threadAufgabeList = null;
	private volatile Workerverwaltung workerVerwaltung;
	private volatile Threadaufgabe aufgabe;

	Threadverwalter(Listener listenerClient, Listener listenerWorker,
			Workerverwaltung workerVerwaltung) {
		this.clientListener = listenerClient;
		this.workerListener = listenerWorker;
		this.workerVerwaltung = workerVerwaltung;
		aVerwaltung = new Aufgabenverwaltung();
		threadAufgabeList = new LinkedList<Threadaufgabe>();
	}

	public void run() {
		while (true) {
			checkClientInput();
			// checkWorkerInput();
		}
	}

	private void checkClientInput() {
		for (int i = 0; i < clientListener.getConnections(); i++) {
			Connection client = clientListener.getConnection(i);
			Object[] lastInput = client.getLastInput();

			if (lastInput[0] instanceof Matrizenmultiplikation) {
				/*
				 * ((Matrizenmultiplikation) lastInput[0]).setClient(client);
				 * aVerwaltung.add((Matrizenmultiplikation) lastInput[0]);
				 * aufgabe = new Threadaufgabe( (Matrizenmultiplikation)
				 * lastInput[0], new Matrizenverwaltung(
				 * (Matrizenmultiplikation) lastInput[0], client));
				 * threadAufgabeList.add(aufgabe); Matrizenverwaltung mv =
				 * ((Matrizenverwaltung) aufgabe .getVerwalter()); mv.splitt();
				 * Auftrag a = mv.getNextAuftrag(); while (a != null) {
				 * Connection c = workerVerwaltung.checkFreeWorker(); if (c !=
				 * null) { aufgabe.addWorker(c); workerOutput(c, a); a =
				 * mv.getNextAuftrag(); } }
				 */
			} else if (lastInput[0] instanceof Skalarprodukt) {

				((Skalarprodukt) lastInput[0]).setClient(client);
				aVerwaltung.add((Skalarprodukt) lastInput[0]);
				aufgabe = new Threadaufgabe((Skalarprodukt) lastInput[0],
						new Skalarverwaltung((Skalarprodukt) lastInput[0],
								client));
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

			} else if (lastInput[0] instanceof Status) {
				int status = aVerwaltung.getStatus((client));
				((Status) lastInput[0]).setErgebnis(status);
				clientOutput(client, lastInput[0]);
			}

			if (lastInput[0] != null) {
				client.setLastInput();
			}
		}
	}

	private void checkWorkerInput() {
		new Thread() {
			@Override
			public void run() {
				for (int i = 0; i < workerListener.getConnections(); i++) {
					Connection worker = workerListener.getConnection(i);
					Object[] lastInput = worker.getLastInput();
					if (lastInput != null) {
						if (lastInput[0] instanceof Matrizenauftrag) {
							workerVerwaltung.unlockWorker(worker);
							for (Iterator<Threadaufgabe> iterator = threadAufgabeList
									.iterator(); iterator.hasNext();) {
								Threadaufgabe aktuelleAufgabe;
								aktuelleAufgabe = iterator.next();
								if (aktuelleAufgabe.contains(worker)) {
									if (((Matrizenverwaltung) aktuelleAufgabe.getVerwalter()).empfangeergebnis(
											((Matrizenauftrag) lastInput[0])
													.getErgebnis(), worker)) {
										clientOutput(
												aktuelleAufgabe.getAufgabe().getClient(),((Matrizenverwaltung) aktuelleAufgabe.getVerwalter()).getMatrizenmultiplikation());
									}

									aktuelleAufgabe.removeWorker(worker);

								}
							}

						} else if (lastInput[0] instanceof Skalarauftrag) {
							workerVerwaltung.unlockWorker(worker);
							for (Iterator<Threadaufgabe> iterator = threadAufgabeList
									.iterator(); iterator.hasNext();) {
								Threadaufgabe aktuelleAufgabe;
								aktuelleAufgabe = iterator.next();
								if (aktuelleAufgabe.contains(worker)) {
									if (((Skalarauftrag) lastInput[0])
											.isAddieren()) {
										Skalarprodukt endergebnis = ((Skalarverwaltung) aktuelleAufgabe
												.getVerwalter())
												.empfangegebnis((Skalarauftrag) lastInput[0]);
										clientOutput(aktuelleAufgabe
												.getAufgabe().getClient(),
												endergebnis);
									} else {
										Auftrag tempauftrag = ((Skalarverwaltung) aktuelleAufgabe
												.getVerwalter())
												.empfangezwischenergebnis(
														worker,
														((Skalarauftrag) lastInput[0]));
										if (tempauftrag instanceof Skalarauftrag) {
											Connection c = workerVerwaltung
													.checkFreeWorker();
											workerOutput(c, tempauftrag);
											aktuelleAufgabe.addWorker(c);

										}
									}
									aktuelleAufgabe.removeWorker(worker);

								}
							}
						}
					}
				}
			}
		}.start();

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
}