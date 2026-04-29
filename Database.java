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
            stmt.executeUpdate(Setup.UserDrop);
            stmt.executeUpdate(Setup.SubscriptionDrop);
            stmt.executeUpdate(Setup.ConversationDrop);
            stmt.executeUpdate(Setup.MessageDrop);
            // commit
            stmt.executeUpdate("COMMIT");
        } catch (SQLException e) {
            // do nothing
        }

        // create the tables
        try {
            Statement stmt = dbconn.createStatement();
            stmt.executeUpdate(Setup.User);
            stmt.executeUpdate(Setup.Subscription);
            stmt.executeUpdate(Setup.Conversation);
            stmt.executeUpdate(Setup.Message);
            // commit
            stmt.executeUpdate("COMMIT");
        } catch (SQLException e) {
            System.out.println("Error creating tables");
            e.printStackTrace();
            return;
        }

        // create triggers
        try {
            Statement stmt = dbconn.createStatement();
            stmt.executeUpdate(Setup.UserTrigger);
            stmt.executeUpdate(Setup.SubscriptionTrigger);
            stmt.executeUpdate(Setup.ConversationTrigger);
            stmt.executeUpdate(Setup.MessageTrigger);
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
            stmt.executeUpdate(Setup.UserData);
            stmt.executeUpdate(Setup.SubscriptionData);
            stmt.executeUpdate(Setup.ConversationData);
            stmt.executeUpdate(Setup.MessageData);
            // commit
            stmt.executeUpdate("COMMIT");
        } catch (SQLException e) {
            System.out.println("Error creating sample data");
            e.printStackTrace();
            return;
        }

    }


}

