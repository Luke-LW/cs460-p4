/**
 * Minh Ngo
 * Database.java
 * This class is responsible for setting up the database that
 * we will be using for our project. It will create the tables and
 * insert some sample data into the tables.
 */
import java.sql.*;

public class Database {
    public static void main(String[] args) {
        String oracle = OracleUser.oracle;
        String username = OracleUser.username;
        String password = OracleUser.password;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver not found");
            e.printStackTrace();
            return;
        }

        Connection dbconn = null;
        try {
            dbconn = DriverManager.getConnection(oracle, username, password);
            dbconn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        // remove the tables if they already exist
        System.out.println("Dropping tables...");

        for (String query : Setup.DropTables) {
            try {
                Statement stmt = dbconn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(query);
                System.out.println(e);
            }
        }

        // create the tables
        System.out.println("Creating tables...");
        for (String query : Setup.CreateTables) {
            try {
                Statement stmt = dbconn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(query);
                System.out.println(e);
            }
        }
    }


}

