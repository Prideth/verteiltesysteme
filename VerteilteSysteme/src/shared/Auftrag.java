package shared;
import server.Connection;

public class Auftrag {
  public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

private Connection connection;
}
