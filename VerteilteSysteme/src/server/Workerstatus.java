package server;

public class Workerstatus {
	private Connection connection;
	private boolean locked;

	public Workerstatus(Connection connection) {
		super();
		this.connection = connection;
		this.locked = false;
	}

	public void lock() {
		this.locked = true;
	}

	public void unlock() {
		this.locked = false;
	}

	public Connection getConnection() {
		return connection;
	}

	public boolean isLocked() {
		return locked;
	}
}
