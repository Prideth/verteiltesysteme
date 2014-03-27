package client;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Panel;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JCheckBox;

import shared.Matrizenmultiplikation;
import shared.Skalarprodukt;
import shared.Status;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Client {

	private static Client window;
	private static Client client;
	private static SocketThread socketThread;

	private int anzahlWorker;
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	private JTable table_3;
	private JTextField textField;
	private JTable table_4;
	private JCheckBox chckbxMatrizenmultiplikation;
	private JCheckBox chckbxSkalarprodukt;

	public JFrame frmVerteilteBerechnung;
	public JTextField textField_1;
	public int jobNummer;
	public int[][] inhaltMatrizeA;
	public int[][] inhaltMatrizeB;
	public int[] inhaltVektorA;
	public int[] inhaltVektorB;
	public int[][] ergebnisMatrize;
	private int matrizeLaenge;
	private int vektorLaenge;
	private volatile static String statusMatrize;
	private volatile static String statusVektor;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Client();
					window.frmVerteilteBerechnung.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
	}

	public Client() {
		client = this;

		jobNummer = (int) ((Math.random()) * 10000 + 1);
		anzahlWorker = 1;
		inhaltMatrizeA = null;
		inhaltMatrizeB = null;
		inhaltVektorA = null;
		inhaltVektorB = null;
		ergebnisMatrize = null;

		initialize();

		socketThread = new SocketThread(client);
		socketThread.start();
	}

	private void initialize() {
		frmVerteilteBerechnung = new JFrame();
		frmVerteilteBerechnung.setTitle("Verteilte Berechnung");
		frmVerteilteBerechnung.setBounds(100, 100, 639, 464);
		frmVerteilteBerechnung.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVerteilteBerechnung.setLocationRelativeTo(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmVerteilteBerechnung.getContentPane().add(tabbedPane,
				BorderLayout.CENTER);

		/* ######################################################### */

		// MATRIZENMULTIPLIKATION
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(.5d);
		tabbedPane.addTab("Matrizenmultiplikation", null, splitPane, null);

		// MATRIZE LINKS
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton1 = new JButton("Matrize laden");
		btnNewButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int rueckgabeWert = chooser
						.showDialog(null, "Datei auswaehlen");
				if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					BufferedReader br = null;
					BufferedReader br2 = null;
					inhaltMatrizeB = null;
					String[][] matrizeB = null;
					DefaultTableModel tabellenmodellMatrizeB = new DefaultTableModel(
							matrizeB, null);

					table.setModel(tabellenmodellMatrizeB);
					table.repaint();
					try {
						int anzahlZeilen = 0;
						int anzahlSpalten = 0;
						boolean firstTime = true;
						br = new BufferedReader(new FileReader(path));
						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							if (firstTime) {
								anzahlSpalten = line.toString().trim()
										.split(";").length;
								firstTime = false;
								anzahlZeilen = anzahlZeilen + 1;
							} else {
								if (anzahlSpalten != line.toString().trim()
										.split(";").length) {
									throw new Exception();
								}

								anzahlZeilen = anzahlZeilen + 1;
							}
						}
						br.close();
						matrizeLaenge = anzahlSpalten;

						if (anzahlSpalten >= 200 || anzahlZeilen >= 200) {
							throw new Exception();
						}

						String[] spalten = new String[anzahlSpalten];
						for (int k = 0; k < spalten.length; k++) {
							spalten[k] = k + ".";
						}
						inhaltMatrizeA = new int[anzahlZeilen][anzahlSpalten];

						br2 = new BufferedReader(new FileReader(path));
						int i = 0;
						for (String line = br2.readLine(); line != null; line = br2
								.readLine()) {
							String[] s = line.toString().trim().split(";");
							for (int j = 0; j < anzahlSpalten; j++) {
								inhaltMatrizeA[i][j] = Integer.parseInt(s[j]);
							}
							i++;
						}
						br2.close();

						String[][] matrizeA = new String[anzahlZeilen][anzahlSpalten];
						for (int j = 0; j < anzahlZeilen; j++) {
							for (int k = 0; k < anzahlSpalten; k++) {
								matrizeA[j][k] = String
										.valueOf(inhaltMatrizeA[j][k]);
							}
						}
						DefaultTableModel tabellenmodellMatrizeA = new DefaultTableModel(
								matrizeA, spalten);

						table_1.setModel(tabellenmodellMatrizeA);
						table_1.repaint();
					} catch (Exception e) {
						JOptionPane
								.showMessageDialog(
										frmVerteilteBerechnung,
										"Es ist ein Fehler aufgetreten. ÃœberprÃ¼fen Sie die Eingabedatei und versuchen Sie es erneut.",
										"Error", JOptionPane.ERROR_MESSAGE);
					} finally {
						try {
							br.close();
							br2.close();
						} catch (IOException e) {
						}
					}
				}
			}
		});
		panel.add(btnNewButton1, BorderLayout.SOUTH);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, BorderLayout.CENTER);

		table_1 = new JTable();
		table_1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_1.setEnabled(false);
		scrollPane_1.setViewportView(table_1);

		// MATRIZE RECHTS
		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("Matrize laden");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int rueckgabeWert = chooser
						.showDialog(null, "Datei auswaehlen");
				if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					BufferedReader br = null;
					BufferedReader br2 = null;
					try {
						if (inhaltMatrizeA == null) {
							throw new Exception();
						}
						int anzahlZeilen = 0;
						int anzahlSpalten = 0;
						boolean firstTime = true;
						br = new BufferedReader(new FileReader(path));
						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							if (firstTime) {
								anzahlSpalten = line.toString().trim()
										.split(";").length;
								firstTime = false;
								anzahlZeilen = anzahlZeilen + 1;
							} else {
								if (anzahlSpalten != line.toString().trim()
										.split(";").length) {
									throw new Exception();
								}
								anzahlZeilen = anzahlZeilen + 1;
							}
						}
						br.close();
						if (anzahlZeilen != matrizeLaenge) {
							throw new Exception();
						}

						if (anzahlSpalten >= 200 || anzahlZeilen >= 200) {
							throw new Exception();
						}

						String[] spalten = new String[anzahlSpalten];
						for (int k = 0; k < spalten.length; k++) {
							spalten[k] = k + ".";
						}
						inhaltMatrizeB = new int[anzahlZeilen][anzahlSpalten];

						br2 = new BufferedReader(new FileReader(path));
						int i = 0;
						for (String line = br2.readLine(); line != null; line = br2
								.readLine()) {
							String[] s = line.toString().trim().split(";");
							for (int j = 0; j < anzahlSpalten; j++) {
								inhaltMatrizeB[i][j] = Integer.parseInt(s[j]);
							}
							i++;
						}
						br2.close();

						String[][] matrizeB = new String[anzahlZeilen][anzahlSpalten];
						for (int j = 0; j < anzahlZeilen; j++) {
							for (int k = 0; k < anzahlSpalten; k++) {
								matrizeB[j][k] = String
										.valueOf(inhaltMatrizeB[j][k]);
							}
						}
						DefaultTableModel tabellenmodellMatrizeB = new DefaultTableModel(
								matrizeB, spalten);

						table.setModel(tabellenmodellMatrizeB);
						table.repaint();
					} catch (Exception e) {
						JOptionPane
								.showMessageDialog(
										frmVerteilteBerechnung,
										"Es ist ein Fehler aufgetreten. Ueberpruefen Sie die Eingabedatei und versuchen Sie es erneut.",
										"Error", JOptionPane.ERROR_MESSAGE);
					} finally {
						try {
							br.close();
							br2.close();
						} catch (Exception e) {
						}
					}
				}
			}
		});
		panel_1.add(btnNewButton, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setEnabled(false);
		scrollPane.setViewportView(table);

		/* ######################################################### */

		// SKALARPRODUKT
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(.5d);
		tabbedPane.addTab("Skalarprodukt", null, splitPane_1, null);

		// SKALAR LINKS
		JPanel panel_3 = new JPanel();
		splitPane_1.setLeftComponent(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton2 = new JButton("Vektor laden");
		btnNewButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				int rueckgabeWert = chooser
						.showDialog(null, "Datei auswaehlen");
				if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					BufferedReader br = null;
					BufferedReader br2 = null;
					inhaltVektorB = null;
					String[][] vektorB = null;
					DefaultTableModel tabellenmodellVektorB = new DefaultTableModel(
							vektorB, null);

					table_3.setModel(tabellenmodellVektorB);
					table_3.repaint();
					try {
						int anzahlZeilen = 0;

						br = new BufferedReader(new FileReader(path));

						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							if (1 != line.toString().trim().split(";").length) {
								throw new Exception();
							}
							anzahlZeilen = anzahlZeilen + 1;
						}
						br.close();

						vektorLaenge = anzahlZeilen;

						if (anzahlZeilen >= 200) {
							throw new Exception();
						}

						String[] spalten = { "1." };
						inhaltVektorA = new int[anzahlZeilen];

						br2 = new BufferedReader(new FileReader(path));
						int i = 0;
						for (String line = br2.readLine(); line != null; line = br2
								.readLine()) {
							String[] s = line.toString().split(";");
							for (int j = 0; j < 1; j++) {
								inhaltVektorA[i] = Integer.parseInt(s[j]);
							}
							i++;
						}
						br2.close();

						String[][] vektorA = new String[anzahlZeilen][1];
						for (int j = 0; j < anzahlZeilen; j++) {
							for (int k = 0; k < 1; k++) {
								vektorA[j][k] = String
										.valueOf(inhaltVektorA[j]);
							}
						}
						DefaultTableModel tabellenmodellVektorA = new DefaultTableModel(
								vektorA, spalten);

						table_2.setModel(tabellenmodellVektorA);
						table_2.repaint();
					} catch (Exception e1) {
						JOptionPane
								.showMessageDialog(
										frmVerteilteBerechnung,
										"Es ist ein Fehler aufgetreten. Ueberpruefen Sie die Eingabedatei und versuchen Sie es erneut.",
										"Error", JOptionPane.ERROR_MESSAGE);
					} finally {
						try {
							br.close();
							br2.close();
						} catch (IOException e1) {
						}
					}
				}
			}
		});
		panel_3.add(btnNewButton2, BorderLayout.SOUTH);

		JScrollPane scrollPane_2 = new JScrollPane();
		panel_3.add(scrollPane_2, BorderLayout.CENTER);

		table_2 = new JTable();
		table_2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_2.setEnabled(false);
		scrollPane_2.setViewportView(table_2);

		// SKALAR RECHTS
		JPanel panel_4 = new JPanel();
		splitPane_1.setRightComponent(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_1 = new JButton("Vektor laden");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				int rueckgabeWert = chooser
						.showDialog(null, "Datei auswaehlen");
				if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					BufferedReader br = null;
					BufferedReader br2 = null;
					try {
						if (inhaltVektorA == null) {
							throw new Exception();
						}
						int anzahlZeilen = 0;

						br = new BufferedReader(new FileReader(path));

						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							if (1 != line.toString().trim().split(";").length) {
								throw new Exception();
							}
							anzahlZeilen = anzahlZeilen + 1;
						}
						br.close();

						if (anzahlZeilen != vektorLaenge) {
							throw new Exception();
						}

						if (anzahlZeilen >= 200) {
							throw new Exception();
						}

						String[] spalten = { "1." };
						inhaltVektorB = new int[anzahlZeilen];

						br2 = new BufferedReader(new FileReader(path));
						int i = 0;
						for (String line = br2.readLine(); line != null; line = br2
								.readLine()) {
							String[] s = line.toString().split(";");
							for (int j = 0; j < 1; j++) {
								inhaltVektorB[i] = Integer.parseInt(s[j]);
							}
							i++;
						}
						br2.close();

						String[][] vektorB = new String[anzahlZeilen][1];
						for (int j = 0; j < anzahlZeilen; j++) {
							for (int k = 0; k < 1; k++) {
								vektorB[j][k] = String
										.valueOf(inhaltVektorB[j]);
							}
						}
						DefaultTableModel tabellenmodellVektorB = new DefaultTableModel(
								vektorB, spalten);

						table_3.setModel(tabellenmodellVektorB);
						table_3.repaint();
					} catch (Exception e1) {
						JOptionPane
								.showMessageDialog(
										frmVerteilteBerechnung,
										"Es ist ein Fehler aufgetreten. Ueberpruefen Sie die Eingabedatei und versuchen Sie es erneut.",
										"Error", JOptionPane.ERROR_MESSAGE);
					} finally {
						try {
							br.close();
							br2.close();
						} catch (Exception e1) {
						}
					}
				}
			}
		});
		panel_4.add(btnNewButton_1, BorderLayout.SOUTH);

		JScrollPane scrollPane_3 = new JScrollPane();
		panel_4.add(scrollPane_3, BorderLayout.CENTER);

		table_3 = new JTable();
		table_3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_3.setEnabled(false);
		scrollPane_3.setViewportView(table_3);

		/* ######################################################### */

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Ergebnisse", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));

		Panel panel_5 = new Panel();
		panel_2.add(panel_5, BorderLayout.NORTH);
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel lblAnzahlWorker = new JLabel("Anzahl Worker:");
		panel_5.add(lblAnzahlWorker, BorderLayout.WEST);

		textField = new JTextField();
		textField.setText("1");
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (Integer.valueOf(textField.getText().trim()) <= 0) {
						JOptionPane.showMessageDialog(null,
								"Bitte gib eine Zahl groesser als 0 ein.",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						anzahlWorker = Integer.valueOf(textField.getText()
								.trim());
					}
				} catch (Exception e) {
					JOptionPane
							.showMessageDialog(
									null,
									"Fehler bei der Eingabe. Geben Sie nur Zahlen ein.",
									"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_5.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);

		JLabel lblErgebnisMaritzenmultiplikation = new JLabel(
				"Ergebnis Maritzenmultiplikation:");
		panel_5.add(lblErgebnisMaritzenmultiplikation, BorderLayout.SOUTH);

		Panel panel_6 = new Panel();
		panel_5.add(panel_6, BorderLayout.NORTH);
		panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblBerechnung = new JLabel("Berechnen:");
		panel_6.add(lblBerechnung);

		chckbxMatrizenmultiplikation = new JCheckBox("Matrizenmultiplikation");
		panel_6.add(chckbxMatrizenmultiplikation);

		chckbxSkalarprodukt = new JCheckBox("Skalarprodukt");
		panel_6.add(chckbxSkalarprodukt);

		JScrollPane scrollPane_4 = new JScrollPane();
		panel_2.add(scrollPane_4, BorderLayout.CENTER);

		table_4 = new JTable();
		table_4.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_4.setEnabled(false);
		scrollPane_4.setViewportView(table_4);

		Panel panel_7 = new Panel();
		JLabel lblNewLabel = new JLabel("Ergebniss Skalarprodukt:");
		panel_2.add(panel_7, BorderLayout.SOUTH);
		panel_7.setLayout(new BorderLayout(0, 0));
		panel_7.add(lblNewLabel, BorderLayout.WEST);

		textField_1 = new JTextField();
		panel_7.add(textField_1, BorderLayout.CENTER);
		textField_1.setColumns(10);

		Panel panel_8 = new Panel();
		panel_7.add(panel_8, BorderLayout.SOUTH);
		panel_8.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_2 = new JButton("Matrize exportieren");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				int rueckgabeWert = chooser
						.showDialog(null, "Datei auswaehlen");
				if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getPath();

					String zeilenTrennTeichen = System
							.getProperty("line.separator");
					PrintWriter pWriter = null;
					try {
						StringBuffer sb = new StringBuffer("");
						pWriter = new PrintWriter(new FileWriter(path));
						for (int i = 0; i < ergebnisMatrize.length; i++) {
							int[] s = ergebnisMatrize[i];
							for (int k = 0; k < s.length; k++) {
								sb.append(Integer.toString(s[k]) + ";");
							}
							sb.append(zeilenTrennTeichen);
						}
						pWriter.println(sb);
					} catch (IOException ioe) {
						ioe.printStackTrace();
					} finally {
						if (pWriter != null)
							pWriter.flush();
						pWriter.close();
					}
				}
			}
		});
		panel_8.add(btnNewButton_2, BorderLayout.WEST);

		JButton btnNewButton_3 = new JButton("Berechnen");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				jobNummer = (int) ((Math.random()) * 10000 + 1);
				try {
					if (chckbxMatrizenmultiplikation.isSelected()) {
						if (inhaltMatrizeA != null && inhaltMatrizeB != null) {
							Matrizenmultiplikation matrizenmull = new Matrizenmultiplikation(
									jobNummer, anzahlWorker, null,
									inhaltMatrizeA, inhaltMatrizeB);
							if (socketThread.connected) {
								socketThread.out(matrizenmull);
								//test();
							} else {
								JOptionPane
										.showMessageDialog(
												frmVerteilteBerechnung,
												"Der Matrizenauftrag konnte nicht versendet werden, bitte versuchen Sie es erneut.",
												"Warnung",
												JOptionPane.WARNING_MESSAGE);
							}
						} else {
							JOptionPane
									.showMessageDialog(
											frmVerteilteBerechnung,
											"Der Matrizenauftrag konnte nicht erstellt werden, da noch keine Daten eingelesen wurden.",
											"Warnung",
											JOptionPane.WARNING_MESSAGE);
						}
					}
				} catch (Exception ioe) {
					JOptionPane
							.showMessageDialog(
									frmVerteilteBerechnung,
									"Es ist ein fehler aufgetreten möglicherweise besteht keine Internet-Verbindung zum Server.",
									"Warnung", JOptionPane.WARNING_MESSAGE);
				}

				try {
					if (chckbxSkalarprodukt.isSelected()) {
						if (inhaltVektorA != null && inhaltVektorB != null) {
							Skalarprodukt skalarmull = new Skalarprodukt(
									jobNummer, anzahlWorker, null,
									inhaltVektorA, inhaltVektorB);
							if (socketThread.connected) {
								socketThread.out(skalarmull);
								//test2();
							} else {
								JOptionPane
										.showMessageDialog(
												frmVerteilteBerechnung,
												"Der Skalarauftrag konnte nicht versendet werden, bitte versuchen Sie es erneut.",
												"Warnung",
												JOptionPane.WARNING_MESSAGE);
							}
						} else {
							JOptionPane
									.showMessageDialog(
											frmVerteilteBerechnung,
											"Der Skalarauftrag konnte nicht erstellt werden, da noch keine Daten eingelesen wurden.",
											"Warnung",
											JOptionPane.WARNING_MESSAGE);
						}
					}
				} catch (Exception ioe) {
					JOptionPane
							.showMessageDialog(
									frmVerteilteBerechnung,
									"Es ist ein fehler aufgetreten moelicherweise besteht keine Internet-Verbindung zum Server.",
									"Warnung", JOptionPane.WARNING_MESSAGE);
				}

				if (!chckbxSkalarprodukt.isSelected()
						&& !chckbxMatrizenmultiplikation.isSelected()) {
					JOptionPane
							.showMessageDialog(
									frmVerteilteBerechnung,
									"Es wurde kein Algorithmus zur berechnung ausgewählt.",
									"Warnung", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		panel_8.add(btnNewButton_3, BorderLayout.CENTER);

		JButton btnStatusAbfragen = new JButton("Status abfragen");
		btnStatusAbfragen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Status status = new Status(jobNummer);
				if (socketThread.connected) {
					socketThread.out(status);
					/*System.out
							.println("Matrizenmultiplikation: " + statusMatrize
									+ " Skalarprodukt: " + statusVektor);*/
				} else {
					JOptionPane
							.showMessageDialog(
									frmVerteilteBerechnung,
									"Eine Stautsabfrage konnte nicht versendet werden, bitte versuchen Sie es erneut.",
									"Warnung", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		panel_8.add(btnStatusAbfragen, BorderLayout.EAST);
	}

	public void test() {
		new Thread() {
			@Override
			public void run() {
				int fertig = 0;
				int gesamt = inhaltMatrizeA.length * inhaltMatrizeB[0].length;

				int[][] ergebnismatrix = null;
				if (inhaltMatrizeA[0].length == inhaltMatrizeB.length) {
					int zeilenm1 = inhaltMatrizeA.length;
					int spaltenm1 = inhaltMatrizeA[0].length;
					int spalenm2 = inhaltMatrizeB[0].length;
					ergebnismatrix = new int[zeilenm1][spalenm2];
					for (int i = 0; i < zeilenm1; i++) {
						for (int j = 0; j < spalenm2; j++) {
							ergebnismatrix[i][j] = 0;
							for (int k = 0; k < spaltenm1; k++) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException ex) {
									Thread.currentThread().interrupt();
								}
								ergebnismatrix[i][j] += inhaltMatrizeA[i][k]
										* inhaltMatrizeB[k][j];
							}
							fertig++;
							statusMatrize = fertig + "/" + gesamt;
						}
					}
				}
				setErgebnisMatrize(ergebnismatrix);
			}
		}.start();
	}

	public void test2() {
		new Thread() {
			@Override
			public void run() {
				int fertig = 0;
				int gesamt = inhaltVektorA.length;
				int skalarprodukt = 0;
				for (int i = 0; i < inhaltVektorA.length; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					skalarprodukt = skalarprodukt + inhaltVektorA[i]
							* inhaltVektorB[i];
					statusVektor = fertig + "/" + gesamt;
				}

				setSkalarproduktErgebnis(skalarprodukt);
			}
		}.start();
	}

	public int getJobNummer() {
		return jobNummer;
	}

	public void setErgebnisMatrize(int[][] ergebnisMatrize) {
		this.ergebnisMatrize = ergebnisMatrize;

		String[] spalten = new String[ergebnisMatrize.length];
		for (int k = 0; k < spalten.length; k++) {
			spalten[k] = k + ".";
		}

		String[][] ergebnisMatrizeString = new String[ergebnisMatrize.length][ergebnisMatrize[0].length];
		for (int j = 0; j < ergebnisMatrize.length; j++) {
			for (int k = 0; k < ergebnisMatrize[0].length; k++) {
				ergebnisMatrizeString[j][k] = String
						.valueOf(ergebnisMatrize[j][k]);
			}
		}

		DefaultTableModel tabellenmodellErgebnisMatrize = new DefaultTableModel(
				this.ergebnisMatrize, spalten);
				ergebnisMatrizeString, spalten);

		table_4.setModel(tabellenmodellErgebnisMatrize);
		table_4.repaint();
	}

	public void setSkalarproduktErgebnis(int ergebnis) {
		textField_1.setText(String.valueOf(ergebnis));
	}
}