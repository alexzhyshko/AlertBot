package bot.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.context.annotation.Component;
import application.context.reader.PropertyReader;

@Component
public class ConnectionManager {
	private static String DB_USER;
	private static String DB_PASSWORD;
	private static String DB_CONNECTION_URL;

	
	public ConnectionManager() {
	    DB_USER = System.getenv("DB_USER");
	    DB_PASSWORD = System.getenv("DB_PASSWORD");
	    DB_CONNECTION_URL = System.getenv("DB_CONNECTION_URL");
	}
	
	public static Connection createConnection() {
		try {
			return DriverManager.getConnection(DB_CONNECTION_URL, DB_USER, DB_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
