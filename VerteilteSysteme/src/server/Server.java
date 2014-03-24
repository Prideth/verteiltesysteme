package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSplitPane;

public class Server extends JFrame {
	private static final long serialVersionUID = 7401311911630855003L;

	private final static String[] COLUMNN_NAMES = { "IP-Adresse", "Local Port",
			"Connection Port" };

	private final static int refreshTime = 5000;
	private final static int maxConnections = 100;
	private final static int portClient = 5555;
	private final static int portWorker = 6666;

	private Server server;
	private DefaultTableModel tableModelClient;
	private DefaultTableModel tableModelWorker;
	private Listener listenerClient;
	private Listener listenerWorker;
        private Threadverwalter threadverwalter;
	private Timer timer;
	private int selectedRowClient;
	private int selectedRowWorker;

	private JLabel connectionCounter;
	private JLabel lblMaxConnections;
	private JLabel listeningOnPort;
	JTable connectionTableClient;
	JTable connectionTableWorker;

	private JSplitPane splitPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;

	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			System.exit(-1);
		}
	}

	public Server() {
		setResizable(false);
		server = this;

		try {
			listenerClient = new Listener(portClient, maxConnections);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(server,
					"Cannot creat Listener with port " + portClient, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		listenerClient.start();

		try {
			listenerWorker = new Listener(portWorker, maxConnections);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(server,
					"Cannot creat Listener with port " + portWorker, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		listenerWorker.start();
                threadverwalter = new Threadverwalter(listenerClient, listenerWorker);

		initialize();

		refreshTimer();
	}

	private void initialize() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Verteilte Systeme Server - Version 1.2.1");
		setBounds(100, 100, 1024, 512);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));

		// BorderLayout-SOUTH
		connectionCounter = new JLabel("Connections: ");
		connectionCounter.setFont(new Font("Tahoma", Font.PLAIN, 12));

		listeningOnPort = new JLabel("Listening on Ports: "
				+ listenerClient.getPort() + ", " + listenerWorker.getPort());
		listeningOnPort.setHorizontalAlignment(SwingConstants.CENTER);
		listeningOnPort.setFont(new Font("Tahoma", Font.PLAIN, 12));

		lblMaxConnections = new JLabel("Max Connections: " + maxConnections);
		lblMaxConnections.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel subBorderLayout = new JPanel();
		getContentPane().add(subBorderLayout, BorderLayout.SOUTH);
		subBorderLayout.setLayout(new BorderLayout(0, 0));

		subBorderLayout.add(connectionCounter, BorderLayout.WEST);
		subBorderLayout.add(listeningOnPort, BorderLayout.CENTER);
		subBorderLayout.add(lblMaxConnections, BorderLayout.EAST);

		splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		// BorderLayout-CENTER
		Object[][] connectionDisplay = {};
		tableModelClient = new DefaultTableModel(connectionDisplay,
				COLUMNN_NAMES);
		tableModelWorker = new DefaultTableModel(connectionDisplay,
				COLUMNN_NAMES);

		connectionTableClient = new JTable();
		connectionTableClient.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					selectedRowClient = connectionTableClient.getSelectedRow();
					Connection c = listenerClient
							.getConnection(selectedRowClient);
					c.frmAdministration.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
		tableModelClient = new DefaultTableModel(connectionDisplay,
				COLUMNN_NAMES);

		connectionTableClient.setModel(tableModelClient);
		connectionTableClient
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		connectionTableClient.setPreferredScrollableViewportSize(new Dimension(
				500, 50));
		connectionTableClient.setFillsViewportHeight(true);
		scrollPane_1 = new JScrollPane(connectionTableClient);
		splitPane.setLeftComponent(scrollPane_1);

		connectionTableWorker = new JTable();
		connectionTableWorker.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					selectedRowWorker = connectionTableWorker.getSelectedRow();
					Connection c = listenerWorker
							.getConnection(selectedRowWorker);
					c.frmAdministration.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
		tableModelWorker = new DefaultTableModel(connectionDisplay,
				COLUMNN_NAMES);

		connectionTableWorker.setModel(tableModelWorker);
		connectionTableWorker
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		connectionTableWorker.setPreferredScrollableViewportSize(new Dimension(
				500, 50));
		connectionTableWorker.setFillsViewportHeight(true);
		scrollPane_2 = new JScrollPane(connectionTableWorker);
		splitPane.setRightComponent(scrollPane_2);

		setVisible(true);
	}

	public void refreshTimer() {
		timer = new Timer(refreshTime, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Aktualisiere connections
				listenerClient.refresh();
				listenerWorker.refresh();

				// Aktuallisiere connectionTable
				tableModelClient = new DefaultTableModel(
						listenerClient.printConnections(), COLUMNN_NAMES);
				connectionTableClient.setModel(tableModelClient);
				tableModelWorker = new DefaultTableModel(
						listenerWorker.printConnections(), COLUMNN_NAMES);
				connectionTableWorker.setModel(tableModelWorker);

				// Aktuallisiere connectionCounter
				connectionCounter.setText("Connections: "
						+ (listenerClient.getConnections() + listenerWorker
								.getConnections()));

				// Setze makierten connectionTable eintrag
				connectionTableClient.setRowSelectionInterval(
						selectedRowClient, selectedRowClient);
				connectionTableWorker.setRowSelectionInterval(
						selectedRowWorker, selectedRowWorker);
			}
		});

		timer.start();
	}
}