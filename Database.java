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
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        // remove the tables if they already exist
        try {
            Statement stmt = dbconn.createStatement();
            stmt.executeUpdate(Setup.PersonDrop);
            stmt.executeUpdate(Setup.LanguageDrop);
            stmt.executeUpdate(Setup.BillingRecordDrop);
            // commit
            stmt.executeUpdate("COMMIT");
        } catch (SQLException e) {
            // do nothing
        }

        // create the tables
        try {
            Statement stmt = dbconn.createStatement();
            stmt.executeUpdate(Setup.PersonTable);
            stmt.executeUpdate(Setup.LanguageTable);
            stmt.executeUpdate(Setup.BillingRecordTable);
            // commit
            stmt.executeUpdate("COMMIT");
        } catch (SQLException e) {
            System.out.println("Error creating tables");
            e.printStackTrace();
            return;
        }

        try {
            Statement stmt = dbconn.createStatement();
            stmt.executeUpdate(Setup.PersonTrigger);
            stmt.executeUpdate(Setup.LanguageTrigger);
            stmt.executeUpdate(Setup.BillingRecordTrigger);
            // commit
            stmt.executeUpdate("COMMIT");
        } catch (SQLException e) {
            System.out.println("Error creating triggers");
            e.printStackTrace();
            return;
        }

        // create sample data
        try {
            Statement stmt = dbconn.createStatement();
            for (String query: Setup.PersonData) {
                stmt.executeUpdate(query);
            }
            for (String query: Setup.LanguageData) {
                stmt.executeUpdate(query);
            }
            for (String query: Setup.BillingRecordData) {
                stmt.executeUpdate(query);
            }
            // commit
            stmt.executeUpdate("COMMIT");
        } catch (SQLException e) {
            System.out.println("Error creating sample data");
            e.printStackTrace();
        }

    }


}

