package shared;

import java.io.Serializable;

import server.Connection;

public class Auftrag implements Serializable {
	private static final long serialVersionUID = -6616371758397984686L;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	private Connection connection;
}
