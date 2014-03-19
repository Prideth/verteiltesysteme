package client;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JCheckBox;

import shared.Matrizenmultiplikation;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Client {

	private JFrame frmVerteilteBerechnung;
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	private JTable table_3;
	private JTextField textField;
	private JTable table_4;
	private JTextField textField_1;

	private SocketThread socketThread;

	public int jobNummer;
	public int[][] inhaltMatrizeA;
	public int[][] inhaltMatrizeB;
	public int[][] inhaltVektorA;
	public int[][] inhaltVektorB;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frmVerteilteBerechnung.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Client() {
		jobNummer = (int) ((Math.random()) * 1000 + 1);
		inhaltMatrizeA = null;
		inhaltMatrizeB = null;
		inhaltVektorA = null;
		inhaltVektorB = null;

		initialize();

		socketThread = new SocketThread();
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
				chooser.showDialog(null, "Datei auswählen");
				String path = chooser.getSelectedFile().getAbsolutePath();
				if (path != null) {
					BufferedReader br;
					try {
						int anzahlZeilen = 0;
						int anzahlSpalten = 0;
						boolean firstTime = true;
						br = new BufferedReader(new FileReader(path));
						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							if (firstTime) {
								anzahlSpalten = line.toString().split(";").length;
								firstTime = false;
							}
							anzahlZeilen = anzahlZeilen + 1;
						}
						br.close();

						String[] spalten = new String[anzahlSpalten];
						for (int k = 0; k < spalten.length; k++) {
							spalten[k] = k + ".";
						}
						inhaltMatrizeA = new int[anzahlZeilen][anzahlSpalten];

						br = new BufferedReader(new FileReader(path));
						int i = 0;
						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							String[] s = line.toString().split(";");
							for (int j = 0; j < anzahlSpalten; j++) {
								inhaltMatrizeA[i][j] = Integer.parseInt(s[j]);
							}
							i++;
						}
						br.close();

						/*DefaultTableModel tabellenmodellMatrizeA = new DefaultTableModel(
								inhaltMatrizeA, spalten);*/

						//table_1.setModel(tabellenmodellMatrizeA);
						//table_1.repaint();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		panel.add(btnNewButton1, BorderLayout.SOUTH);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, BorderLayout.CENTER);

		table_1 = new JTable();
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
				chooser.showDialog(null, "Datei auswählen");
				String path = chooser.getSelectedFile().getAbsolutePath();
				if (path != null) {
					BufferedReader br;
					try {
						int anzahlZeilen = 0;
						int anzahlSpalten = 0;
						boolean firstTime = true;
						br = new BufferedReader(new FileReader(path));
						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							if (firstTime) {
								anzahlSpalten = line.toString().split(";").length;
								firstTime = false;
							}
							anzahlZeilen = anzahlZeilen + 1;
						}
						br.close();

						String[] spalten = new String[anzahlSpalten];
						for (int k = 0; k < spalten.length; k++) {
							spalten[k] = k + ".";
						}
						inhaltMatrizeB = new int[anzahlZeilen][anzahlSpalten];

						br = new BufferedReader(new FileReader(path));
						int i = 0;
						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							String[] s = line.toString().split(";");
							for (int j = 0; j < anzahlSpalten; j++) {
								inhaltMatrizeB[i][j] = Integer.parseInt(s[j]);
							}
							i++;
						}
						br.close();

						/*DefaultTableModel tabellenmodellMatrizeB = new DefaultTableModel(
								inhaltMatrizeB, spalten);*/

						//table.setModel(tabellenmodellMatrizeB);
						//table.repaint();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		panel_1.add(btnNewButton, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
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
				chooser.showDialog(null, "Datei auswählen");
				String path = chooser.getSelectedFile().getAbsolutePath();
				if (path != null) {
					BufferedReader br;
					try {
						int anzahlZeilen = 0;
						int anzahlSpalten = 1;

						br = new BufferedReader(new FileReader(path));

						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							anzahlZeilen = anzahlZeilen + 1;
						}
						br.close();

						String[] spalten = { "1." };
						inhaltVektorA = new int[anzahlZeilen][anzahlSpalten];

						br = new BufferedReader(new FileReader(path));
						int i = 0;
						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							String[] s = line.toString().split(";");
							for (int j = 0; j < anzahlSpalten; j++) {
								inhaltVektorA[i][j] = Integer.parseInt(s[j]);
							}
							i++;
						}
						br.close();

						/*DefaultTableModel tabellenmodellVektorA = new DefaultTableModel(
								inhaltVektorA, spalten);*/

						//table_2.setModel(tabellenmodellVektorA);
						//table_2.repaint();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		panel_3.add(btnNewButton2, BorderLayout.SOUTH);

		JScrollPane scrollPane_2 = new JScrollPane();
		panel_3.add(scrollPane_2, BorderLayout.CENTER);

		table_2 = new JTable();
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
				chooser.showDialog(null, "Datei auswählen");
				String path = chooser.getSelectedFile().getAbsolutePath();
				if (path != null) {
					BufferedReader br;
					try {
						int anzahlZeilen = 0;
						int anzahlSpalten = 1;

						br = new BufferedReader(new FileReader(path));

						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							anzahlZeilen = anzahlZeilen + 1;
						}
						br.close();

						String[] spalten = { "1." };
						inhaltVektorB = new int[anzahlZeilen][anzahlSpalten];

						br = new BufferedReader(new FileReader(path));
						int i = 0;
						for (String line = br.readLine(); line != null; line = br
								.readLine()) {
							String[] s = line.toString().split(";");
							for (int j = 0; j < anzahlSpalten; j++) {
								inhaltVektorB[i][j] = Integer.parseInt(s[j]);
							}
							i++;
						}
						br.close();

						/*DefaultTableModel tabellenmodellVektorB = new DefaultTableModel(
								inhaltVektorB, spalten);*/

						//table_3.setModel(tabellenmodellVektorB);
						//table_3.repaint();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		panel_4.add(btnNewButton_1, BorderLayout.SOUTH);

		JScrollPane scrollPane_3 = new JScrollPane();
		panel_4.add(scrollPane_3, BorderLayout.CENTER);

		table_3 = new JTable();
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

		JCheckBox chckbxMatrizenmultiplikation = new JCheckBox(
				"Matrizenmultiplikation");
		panel_6.add(chckbxMatrizenmultiplikation);

		JCheckBox chckbxSkalarprodukt = new JCheckBox("Skalarprodukt");
		panel_6.add(chckbxSkalarprodukt);

		JScrollPane scrollPane_4 = new JScrollPane();
		panel_2.add(scrollPane_4, BorderLayout.CENTER);

		table_4 = new JTable();
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
				chooser.showDialog(null, "Datei auswählen");
				String path = chooser.getSelectedFile().getPath();
				
				String zeilenTrennTeichen = System.getProperty("line.separator");
				PrintWriter pWriter = null;
		        try {
		        	StringBuffer sb = new StringBuffer("");
		            pWriter = new PrintWriter(new FileWriter(path));
		            for (int i = 0; i < inhaltMatrizeA.length; i++) {
						int[] s = inhaltMatrizeA[i];
						for(int k = 0; k < s.length; k++) {
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
		});
		panel_8.add(btnNewButton_2, BorderLayout.WEST);

		JButton btnNewButton_3 = new JButton("Berechnen");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String client = socketThread.socket.getInetAddress() + ":" + socketThread.socket.getLocalPort();
				int worker = 3;
				Matrizenmultiplikation matrizenmull = new Matrizenmultiplikation(jobNummer, worker, client, inhaltMatrizeA, inhaltMatrizeB);
				socketThread.out(matrizenmull);
			}
		});
		panel_8.add(btnNewButton_3, BorderLayout.CENTER);

		JButton btnStatusAbfragen = new JButton("Status abfragen");
		panel_8.add(btnStatusAbfragen, BorderLayout.EAST);

		/* ######################################################### */
	}
}