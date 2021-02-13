package bot.schema_init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import application.context.annotation.Component;

@Component
public class SchemaInitializer {
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String MYSQL_CONNECTION_URL;

    private static final int DB_STARTUP_WAIT_TIME_MS = 15000;

    public SchemaInitializer() {
        DB_USER = System.getenv("DB_USER");
        DB_PASSWORD = System.getenv("DB_PASSWORD");
        MYSQL_CONNECTION_URL = System.getenv("MYSQL_CONNECTION_URL");
    }

    public static void initialize() {
        try {
            System.out.printf("[INFO] %s Waiting %dms. for DB to start%n",
                    LocalDateTime.now().toString(), DB_STARTUP_WAIT_TIME_MS);
            Thread.sleep(DB_STARTUP_WAIT_TIME_MS);
            Connection checkCon = createConnection(MYSQL_CONNECTION_URL + "alertbot");
            System.out.printf("[INFO] %s DB Schema already exists, no need to init%n", LocalDateTime.now().toString());
        } catch (CommunicationsException commExc) {

        } catch (Exception exception) {
            System.out.printf("[INFO] %s DB Schema does not exist, initializing%n", LocalDateTime.now().toString());
            try (Connection con = createConnection()) {
                ScriptRunner sr = new ScriptRunner(con);
                try (Reader reader = new BufferedReader(new InputStreamReader(
                        SchemaInitializer.class.getClassLoader().getResourceAsStream("ddl.sql"), "UTF-8"))) {
                    sr.runScript(reader);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static Connection createConnection() {
        try {
            return createConnection(MYSQL_CONNECTION_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private static Connection createConnection(String conUrl) throws SQLException {
        return DriverManager.getConnection(conUrl, DB_USER, DB_PASSWORD);
    }

}
