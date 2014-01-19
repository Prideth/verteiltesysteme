package shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SerializedFile implements Serializable {
	private static final long serialVersionUID = 8011853478112411865L;

	private String fileName;
	private String abolutPath;
	private byte[] buffer;

	public SerializedFile(String fileName, String abolutPath) {
		this.fileName = fileName;
		this.abolutPath = abolutPath;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return abolutPath;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void save(String filePath) {
		File fileDest = new File(filePath + fileName);
		byte[] bytes = getBuffer();
		FileOutputStream fos;

		try {
			fos = new FileOutputStream(fileDest);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void readFile(String filePath) {
		File file = new File(filePath);
		FileInputStream in;
		
		try {
			in = new FileInputStream(file);
			buffer = new byte[in.available()];
			in.read(buffer);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
