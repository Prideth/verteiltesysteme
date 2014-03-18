package client;

import java.io.*;
import java.net.*;
import shared.SerializedFile;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.BorderLayout;
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
import javax.swing.JCheckBox;

public class Client {
	final static int PORT = 5555;
	final static String IP = "localhost";

	private static Socket socket;
	private static Object inputObject;
	private static ObjectInputStream input;
	private static ObjectOutputStream output;
	private static boolean connected = false;

	private JFrame frmVerteilteBerechnung;
	private JTable table;
	private JTable table_1;
	DefaultTableModel tabellenmodellGruppeA;
	private JTable table_2;
	private JTable table_3;
	private JTextField textField;
	private JTable table_4;
	private JTextField textField_1;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frmVerteilteBerechnung.setVisible(true);

					/*while (true) {
						isConnected();
						readInput();
						analyseInput();
					}*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Client() {
		initialize();
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

				// Spalten anlegen
				String spalten[] = { "Mannschaft", "Punkte" };

				// Datenmodelle anlegen
				String gruppeA[][] = { { "Die Gelben Uboote", "9.6" },
						{ "FatFighters", "8.1" }, { "Biertrinker", "6.2" } };

				tabellenmodellGruppeA = new DefaultTableModel(gruppeA, spalten);

				table_1.setModel(tabellenmodellGruppeA);
				table_1.repaint();
			}
		});
		panel.add(btnNewButton1, BorderLayout.SOUTH);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, BorderLayout.CENTER);

		table_1 = new JTable();
		scrollPane_1.setViewportView(table_1);

		// MATRIZE RECHTS
		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("Matrize laden");
		panel_1.add(btnNewButton, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
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
		panel_3.add(btnNewButton2, BorderLayout.SOUTH);

		JScrollPane scrollPane_2 = new JScrollPane();
		panel_3.add(scrollPane_2, BorderLayout.CENTER);

		table_2 = new JTable();
		scrollPane_2.setViewportView(table_2);

		// SKALAR RECHTS
		JPanel panel_4 = new JPanel();
		splitPane_1.setRightComponent(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_1 = new JButton("Vektor laden");
		panel_4.add(btnNewButton_1, BorderLayout.SOUTH);

		JScrollPane scrollPane_3 = new JScrollPane();
		panel_4.add(scrollPane_3, BorderLayout.CENTER);

		table_3 = new JTable();
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
		panel_8.add(btnNewButton_2, BorderLayout.WEST);

		JButton btnNewButton_3 = new JButton("Berechnen");
		panel_8.add(btnNewButton_3, BorderLayout.CENTER);
		
		JButton btnStatusAbfragen = new JButton("Status abfragen");
		panel_8.add(btnStatusAbfragen, BorderLayout.EAST);

		/* ######################################################### */
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
		if (inputObject instanceof SerializedFile) {
			serializedFile();
		}
	}

	private static void serializedFile() {
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