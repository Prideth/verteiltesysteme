package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Listener extends Thread {
	private Vector<Connection> connections;
	private int maxConnections;
	private int port;
	private ServerSocket serverSocketOne;

	public Listener(int port, int maxConnections) throws IOException {
		if (port != 0) {
			serverSocketOne = new ServerSocket(port);
		}

		this.port = port;
		this.maxConnections = maxConnections;
		connections = new Vector<Connection>(maxConnections);
	}

	synchronized void addConnection(Socket s) {
		if (connections.size() >= maxConnections) {
			try {
				s.close();
			} catch (Exception e) {
			}
		} else {
			Connection c = new Connection(s);
			connections.addElement(c);
			c.start();
		}
	}

	public int getPort() {
		return port;
	}

	public synchronized int getMaxConnections() {
		return maxConnections;
	}

	public Connection getConnection(int i) {
		return ((Connection) connections.elementAt(i));
	}

	public int getConnections() {
		int counter = 0;
		for (int i = 0; i < connections.size(); i++) {
			if ((Connection) connections.elementAt(i) != null) {
				counter++;
			}
		}
		return counter;
	}

	public synchronized String[][] printConnections() {
		String[][] daten = new String[maxConnections][3];
		for (int i = 0; i < connections.size(); i++) {
			Connection c = (Connection) connections.elementAt(i);

			daten[i][0] = c.socket.getInetAddress().getHostAddress();
			daten[i][1] = Integer.toString(c.socket.getPort());
			daten[i][2] = Integer.toString(c.socket.getLocalPort());

		}
		return daten;
	}

	public void close() {
		try {
			if (port != 0) {
				serverSocketOne.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refresh() {
		for (int i = 0; i < connections.size(); i++) {
			Connection c = (Connection) connections.elementAt(i);
			if (!c.isAlive()) {
				connections.removeElementAt(i);
			}
		}
	}

	public void run() {
		while (true) {
			try {
				if (port != 0) {
					Socket connection = serverSocketOne.accept();
					addConnection(connection);
				}
			} catch (IOException e) {
			}
		}
	}
}