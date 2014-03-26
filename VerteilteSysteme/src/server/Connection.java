package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JFrame;

import java.net.Socket;

public class Connection extends Thread {
	public JFrame frmAdministration;
	public Socket socket;
	private Object inputObject;
	private Object[] deliever = new Object[3];
	private static ObjectInputStream input;
	private static ObjectOutputStream output;

	public void run() {
		while (true) {
			readInput();
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