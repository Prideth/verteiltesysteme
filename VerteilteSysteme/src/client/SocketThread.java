package client;
import java.io.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketThread extends Thread {
	final static int PORT = 5555;
	final static String IP = "localhost";
	
	public static Socket socket;
	private static Object inputObject;
	private static ObjectInputStream input;
	private static ObjectOutputStream output;
	private static boolean connected = false;

	public SocketThread() {
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
		/*if (inputObject instanceof SerializedFile) {
			serializedFile();
		}*/
	}

	/*private static void serializedFile() {
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
	}*/

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