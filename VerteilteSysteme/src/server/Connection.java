package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
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
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public void run() {
		while (true) {
			readInput();
			analyseInput();
		}
	}
        
        public Object[] getLastInput(){
            return deliever;
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

		initializeGUI();
	}

	private void initializeGUI() {
		frmAdministration = new JFrame();
		frmAdministration.setTitle("Administration - "
				+ socket.getInetAddress().getHostAddress());
		frmAdministration.setResizable(true);
		frmAdministration.setBounds(100, 100, 720, 480);
		frmAdministration.setLocationRelativeTo(null);
	}

	public Object[] readInput() {
                deliever[1] = this;
		inputObject = null;
		try {
			inputObject = input.readObject();
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		}
                deliever[0] = inputObject;
                return deliever;
	}

	public void analyseInput() {
		/*if (inputObject instanceof SerializedFile) {
			SerializedFile file = (SerializedFile) inputObject;

			String userDirectory = System.getProperty("user.home")
					+ "\\Desktop\\Downloads\\";
			File f = new File(userDirectory);
			if (f.isDirectory()) {
			} else {
				f.mkdir();
			}

			file.save(userDirectory);
			file = null;
			f = null;
		} else if (inputObject instanceof SerializedMessage) {
			//SerializedMessage message = (SerializedMessage) inputObject;
			//String oldText = recievedText.getText();
		}*/
	}

	@SuppressWarnings("deprecation")
	public boolean writeMsg(Object msg) {
		boolean status = false;
		try {
			output.writeObject(msg);
			output.flush();
			status = true;
		} catch (IOException e) {
			frmAdministration.setVisible(false);
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

/*
SerializedCommand msg = new SerializedCommand("deleteFile", path);
writeMsg(msg);
actionUpload = new AbstractAction("Upload") {
private static final long serialVersionUID = 4044398634184641494L;

public void actionPerformed(ActionEvent e) {
	if (folderTree.m_display.getText().equals("")) {
		JOptionPane.showMessageDialog(frmAdministration,
				"Chose a folder", "Info",
				JOptionPane.INFORMATION_MESSAGE);
	} else {
		JFileChooser chooser = new JFileChooser();
		chooser.showDialog(null, "Chose file");
		String path = chooser.getSelectedFile().getAbsolutePath();
		String name = chooser.getSelectedFile().getName();
		if (path != null) {

			SerializedFile upload = new SerializedFile("\\" + name,
					folderTree.m_display.getText());
			upload.readFile(path);

			writeMsg(upload);
		} else {
			JOptionPane.showMessageDialog(frmAdministration,
					"Chose a folder", "Info",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
};
*/