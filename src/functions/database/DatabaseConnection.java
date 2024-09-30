package functions.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_PORT = "jdbc:mysql://localhost:3306";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/heartstone_db";
    private static final String DB_DRV = "com.mysql.cj.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASSWD = "";
    private static Connection connection = null;

    public static void databaseConnection() {
        try {
            connectToDatabase();
            System.out.println("OK: Estas connectat amb la BBDD");
        } catch (Exception e1) {
            System.err.println("WARNING: No s'ha pogut connectar a la BBDD. S'esta probant a crear.");
            //e1.printStackTrace();
            try {
                createDatabase();
                connectToDatabase();
                System.out.println("OK: S'ha creat la BBDD correctament");
            } catch (Exception e2) {
                System.err.println("ERROR: No s'ha pogut crear la BBDD.");
                e2.printStackTrace();
            }
        }
    }

    public static void createDatabase() throws ClassNotFoundException, SQLException, IOException {
        Class.forName(DB_DRV);
        try (Connection conn = DriverManager.getConnection(DB_PORT, DB_USER, DB_PASSWD)) {
            runSqlScript(conn, "dependencies/000_create_database_tables.sql");
        }
    }

    public static void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRV);
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
    }

    public static void createConnection() {
        databaseConnection();
    }

    private static void runSqlScript(Connection conn, String filePath) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Statement stmt = conn.createStatement()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String[] sqlStatements = sb.toString().split(";");
            for (String sqlStatement : sqlStatements) {
                if (!sqlStatement.trim().isEmpty()) {
                    stmt.execute(sqlStatement);
                }
            }
        }
    }

    public static void checkAndReconnect() {
        try {
            if (connection == null || connection.isClosed()) {
                createConnection();
                System.out.println("INFO: S'ha reconnectat amb la BBDD");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR: No s'ha pogut reconnectar amb la BBDD");
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("OK: Disconnected from the database.");
            } catch (SQLException ex) {
                System.err.println("ERROR: Could not disconnect from the database.");
                ex.printStackTrace();
            }
        }
    }
}
