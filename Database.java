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
        
        try {
            System.out.println("Dropping tables...");
            Statement stmt = dbconn.createStatement();
            stmt.executeUpdate(Setup.PersonDrop);
            stmt.executeUpdate(Setup.LanguageDrop);
            stmt.executeUpdate(Setup.BillingRecordDrop);
            stmt.executeUpdate(Setup.InvoiceDrop);
            stmt.executeUpdate(Setup.MembershipDrop);
            stmt.executeUpdate(Setup.TicketDrop);
            stmt.executeUpdate(Setup.AgentDrop);
            stmt.executeUpdate(Setup.ConversationDrop);
            stmt.executeUpdate(Setup.PersonaDrop);
            stmt.executeUpdate(Setup.MessageDrop);
            stmt.executeUpdate(Setup.BookmarkDrop);
            stmt.executeUpdate(Setup.WorkspaceDrop);
            stmt.executeUpdate(Setup.TemplatePromptDrop);
            stmt.executeUpdate(Setup.UserPromptDrop);
            stmt.executeUpdate(Setup.PromptCategoryDrop);

            stmt.executeUpdate(Setup.UserWorkspaceDrop);
            stmt.executeUpdate(Setup.UserPromptWorkspaceDrop);
            stmt.executeUpdate(Setup.TemplatePromptWorkspaceDrop);
            stmt.executeUpdate(Setup.ConversationwWorkspaceDrop);
            stmt.executeUpdate(Setup.PromptCategoryUserPromptDrop);
            // commit
            stmt.executeUpdate("COMMIT");
        } catch (SQLException e) {
            // do nothing
        }

        // create the tables
        try {
            System.out.println("Creating tables...");
            Statement stmt = dbconn.createStatement();
            stmt.executeUpdate(Setup.PersonTable);
            stmt.executeUpdate(Setup.LanguageTable);
            stmt.executeUpdate(Setup.BillingRecordTable);
            stmt.executeUpdate(Setup.InvoiceTable);
            stmt.executeUpdate(Setup.MembershipTable);
            stmt.executeUpdate(Setup.TicketTable);
            stmt.executeUpdate(Setup.AgentTable);
            stmt.executeUpdate(Setup.ConversationTable);
            stmt.executeUpdate(Setup.PersonaTable);
            stmt.executeUpdate(Setup.MessageTable);
            stmt.executeUpdate(Setup.BookmarkTable);
            stmt.executeUpdate(Setup.WorkspaceTable);
            stmt.executeUpdate(Setup.TemplatePromptTable);
            stmt.executeUpdate(Setup.UserPromptTable);
            stmt.executeUpdate(Setup.PromptCategoryTable);

            stmt.executeUpdate(Setup.UserWorkspaceTable);
            stmt.executeUpdate(Setup.UserPromptWorkspaceTable);
            stmt.executeUpdate(Setup.TemplatePromptWorkspaceTable);
            stmt.executeUpdate(Setup.ConversationwWorkspaceTable);
            stmt.executeUpdate(Setup.PromptCategoryUserPromptTable);
            // commit
            stmt.executeUpdate("COMMIT");
        } catch (SQLException e) {
            System.out.println("Error creating tables");
            e.printStackTrace();
            return;
        }
    }


}

