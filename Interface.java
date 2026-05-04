/*
 *         Names:   Minh Ngo 
                    Luke Livesay-Wright
                    Derek Hoshaw
 *        Course:   CSC 460
 *    Assignment:   Program 4
 *    Instructor:   Lester McCann
 *            TA:   James Shen, Muhammad Bilal
 *      Due Date:   May 5th, 2026
 *
 *       Purpose:   Provide a User Interface for performing the neccessary database operations
 *                  for the DBMS of the AI messaging platform.
 * 
 *    Description:  The Interface centers around a series of menus that the user can navigate 
 *                  through with the numpad (1-10) to perform the required operations for managing the database. 
 *                  The main menu contains the 8 neccessary functionalities as decribed in the spec
 *                  Each functionality/operation calls its own helper method to perform that operation
 *                  Each helper method has its own secondary menu and prompts to guide the user through performing that operation 
 *                  sometimes needing further input to get addional information required for that operation.
 * 
 *                  All printing is done through the series of static strings defined at the top of the class
 *                  where we call each string to print the corresponding menu or prompt when we need it.
 *                  Each menu/submenu is a switch statement that takes user input and calls
 *                  the corresponding helper method for the operation the user wants to perform.
 *                  To perform operations we format and execute SQL statements based on user input and the operation they want to perform.
 *                  To get user input we have dedicated helper methods to validate the input
 *                  We connect to the database via the JDBC driver (followed from Prog3)
 *                  and the credentials provided in OracleUser.java 
 *     
 *   Requirements:  Java 25 or earlier
 *        Compile:  javac Interface.java
 *         Usage:   java Interface 
 *    Input Files:  None. 
 */

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Interface {
    private enum Entity {
        USER, MESSAGE, CONVERSATION, WORKSPACE, PERSONA, USER_PROMPT, INVOICE, SUPPORT_TICKET, AGENT, BILLING_RECORD, 
        SPECIAL_QUERY_1, SPECIAL_QUERY_2, SPECIAL_QUERY_3, SPECIAL_QUERY_4, SUBSCRIPTION_TIER, RATING_VALUE, LANGUAGE
    };

    private final static String mainInterface =
            "\f----------------------------------------------------\n" +
            "Type a number to access a certain functionality:\n" +
            "1: Manage user accounts\n" +
            "2: Manage conversations & messages\n" +
            "3: Manage workspaces\n" +
            "4: Manage personas\n" +
            "5: Prompt library\n" +
            "6: Subscription tracking\n" +
            "7: Billing operations\n" +
            "8: Create support ticket\n" +
            "9: Queries\n" +
            "10: Exit\n" +
            "----------------------------------------------------\n";

    private final static String userAcctInterface =
        "\f------------------------\n" + 
        "Manage User Accounts:\n" +
        "1: Add account\n" +
        "2: Update account\n" +
        "3: Delete account\n" +
        "4: update address\n" +
        "5: update payment method\n" +
        "6: Back\n" +
        "------------------------\n";
    private final static String addUserEmailPrompt =
        "Enter an email for the user: ";
    private final static String addUserNamePrompt =
        "Enter a name for the user: ";
    private final static String addUserPasswordPrompt =
        "Enter a password for the user: ";
    private final static String selectUserForUpdatePrompt =
        "Select a user to update: ";
    private final static String selectUserForDeletePrompt = 
        "Select a user to delete: ";
    private final static String addUserLanguagePrompt =
        "Select a language id for the user: ";


    private final static String manageConvoInterface =
        "\f----------------------------------\n" +
        "Manage Conversations & Messages\n" +
        "1: Start a new conversation\n" +
        "2: Add messages to a conversation\n" +
        "3: Update message feedback\n" +
        "4: Back\n" +
        "----------------------------------\n";
    private final static String addConvoTitlePrompt = 
        "Enter a title for the conversation: ";
    private final static String selectConvoForUpdatePrompt =
        "Select a conversation to add messages to: ";
    private final static String selectConvoToFindMessagePrompt =
        "Select a conversation to search the methods within: ";
    private final static String selectMessageForFeedbackPrompt =
        "Select one of the following messages to adjust its feedback: ";

    private final static String workspaceInterface =
        "\f------------------------\n" +
        "Manage Workspaces\n" +
        "1: Create workspace\n" +
        "2: Modify workspace\n" +
        "3: View workspace members\n" +
        "4: Add conversation to workspace\n" +
        "5: Back\n" +
        "------------------------\n";
    private final static String addWorkspacePrivacyPrompt =
        "Select a privacy option for this workspace (private/public): ";
    private final static String selectWorkspacePrompt =
        "Select a workspace: ";
    private final static String modifyWorkspaceInterface =
        "\nWhat would you like to do?\n" +
        "1: Change privacy setting\n" +
        "2: Change owner\n" +
        "3: Add member\n" +
        "4: Back\n";

    private final static String personaInterface =
        "\f------------------------\n" +
        "Manage Personas\n" +
        "1: Create persona\n" +
        "2: Delete persona\n" +
        "3: Back\n" + 
        "------------------------\n";
    private final static String addPersonaNamePrompt =
        "Enter a name for this persona: ";
    private final static String addPersonaPersonalityPrompt =
        "Enter a personality for this persona: ";
    private final static String selectPersonaToDeletePrompt =
        "Select a persona to delete: ";

    private final static String promptInterface =
        "\f------------------------\n" +
        "Prompt Library\n" +
        "1: Add prompt template\n" +
        "2: Update prompt template privacy\n" +
        "3: Update prompt template instruction\n" +
        "4: Back\n" +
        "------------------------\n";
    private final static String addPromptPrivacyPrompt = 
        "Select a privacy option for this prompt (private/public): ";
    private final static String addPromptInstructionPrompt =
        "Enter the instructions for this prompt: ";
    private final static String selectPromptPrompt =
        "Select a prompt template to update privacy: ";
    private final static String selectPromptInstructionPrompt =
        "Select a prompt template to update instruction: ";
    private final static String updatePromptPrivacyPrompt =
        "What is the new privacy ('public', 'private'): ";
    private final static String updatePromptInstructionPrompt = 
        "What is the new instruction: ";

    private final static String subscriptionInterface =
        "\f------------------------\n" +
        "Subcription Tracking\n" +
        "1: Upgrade user's subscription tier\n" +
        "2: Check user's limit\n" +
        "3: Back\n" +
        "------------------------\n";
    private final static String selectUserForUpgradePrompt =
        "Select a user to upgrade their subscription tier: ";
    private final static String selectUserForLimitCheck = 
        "Select a user to see how close they are to their message limit: ";

    private final static String billingInterface =
        "\f------------------------\n" +
        "Billing Operations\n" +
        "1: Generate monthly invoice\n" +
        "2: Mark invoice as paid\n" +
        "3: Back\n" +
        "------------------------\n"; 
    private final static String selectInvoiceToPayPrompt = 
        "Select an invoice to mark as paid: ";
    private final static String selectBillingRecordAddressPrompt =
        "Select a billing record to update the address: ";
    private final static String updateBillingAddress =
        "What is the new address: ";
    private final static String selectBillingRecordMethodPrompt = 
        "Select a billing record to update the pay method: ";
    private final static String updateBillingMethod =
        "What is the new pay method ('credit', 'debit', 'cash', 'check', 'other'): ";


    private final static String supportInterface =
        "\f------------------------\n" +
        "Create Support Tickets\n" +
        "1: Create ticket\n" +
        "2: Assign ticket\n" +
        "3: Change a ticket status\n" +
        "4: Back\n" +
        "------------------------\n";
    private final static String addTicketTopicPrompt =
        "Enter a reason for creating the support ticket: ";
    private final static String selectTicketForAgentPrompt =
        "Select a ticket to assign to an agent: ";
    private final static String selectAgentPrompt =
        "Select an agent to assign to the ticket: ";
    private final static String selectTicketForStatusPrompt =
        "Select a ticket to change status: ";
    private final static String selectTicketStatus =
        "Select a status (0: Waiting, 1: Escalated, 2: Resolved): ";

    private final static String queryInterface =
        "\f----------------------------\n" +
        "Select a query to answer\n" +
        "1: For a given user, list all their bookmarked messages across all conversations\n" +
        "2: List all users who have unpaid invoices\n" +
        "3: Identify the most helpful persona\n" +
        "4: (Custom query) Identify the most active users\n" +
        "5: Back\n" +
        "----------------------------\n";

    private final static String updateUserEmailPrompt = 
        "Enter new email for the user: ";

    private final static String commandError = "Error: Unrecognized Command. Try again:\n";



    public static void main(String[] args) {
        // Connect to the database using the credentials provided in OracleUser.java and JDBC Driver
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

        boolean init = true;
        int input = 0;
        Scanner keyboard = new Scanner(System.in);

            // At this point we have connected to the database so we can start the interface loop

        while (true) {
            // Print the main menu interface if this is first time through the loop or their was no errors in previous loop iteration.
            if (init) System.out.println(mainInterface);
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }

                // The user now has the option to select 1 of the 8 operations to perform
                // the required functionality on the database. Based on their input
                // The switch statement will call the corresponding helper method to 
                // perform that operation. Each helper method has its own secondart prompts
                // To guide the user through the process of performing the operation they selected.
                // To exit the program, the user can input 9 from the main menu to exit.
                // To exit any opeation submenu, the user can input the option to go back which will be the last option in each submenu

            switch (input) {
                case 1:
                    manageUserAcct(keyboard, dbconn); // For example, to manage user accounts, the manageUserAcct helper method is called
                    init = true;
                    break;
                
                case 2:
                    manageMsg(keyboard, dbconn);
                    init = true;
                    break;

                case 3:
                    manageWorkspace(keyboard, dbconn);
                    init = true;
                    break;

                case 4:
                    managePersona(keyboard, dbconn);
                    init = true;
                    break;

                case 5:
                    managePromptLibrary(keyboard, dbconn);
                    init = true;
                    break;

                case 6:
                    manageSubs(keyboard, dbconn);
                    init = true;
                    break;

                case 7:
                    manageBilling(keyboard, dbconn);
                    init = true;
                    break;

                case 8:
                    manageTickets(keyboard, dbconn);
                    init = true;
                    break;

                case 9:
                    chooseQuery(keyboard, dbconn);
                    init = true;
                    break;

                case 10:
                    System.exit(0);
                    break;

                default:
                    System.out.println(commandError);
                    init = false;
                    break;
            }
        }
    }

    /*---------------------------------------------------------------------
    |  Method manageUserAcct(keyboard, dbconn)
    |
    |  Purpose: manage user accounts, including creating, updating, and deleting accounts
    |
    |  Pre-condition: User is connected to the database and selected UI option 1 to manage user accounts
    |  Post-condition: User accounts are managed successfully based on further input
    |
    |  Parameters:
    |      Scanner keybaord - used to get user input for managing user accounts
    |      Connection dbconn - used to execute SQL statements to manage user accounts in the database
    |      
    |  Returns: None.
    *-------------------------------------------------------------------*/
    private static void manageUserAcct(Scanner keyboard, Connection dbconn) {
        System.out.println(userAcctInterface);
        int count;
        boolean exit;
        String query, statement;
        int input = 0;
        // Every helper function for operations will infintiy loop until the user exists
        while (true) {
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }
            
                // We must have a secondary prompt to get the correct user operation
                // Their are 3 operations: add, update, delete. 
                // Each operation requires the user to input different information, 
                // so we need to prompt them for that information after they select 
                // the operation they want to perform, 
                // we use helper method promptUserForStr and promptUserForInt to get the additional information

            switch (input) {
                case 1: // Add user account
                    String email = promptUserForStr(addUserEmailPrompt, keyboard);
                    String username = promptUserForStr(addUserNamePrompt, keyboard);
                    String password = promptUserForStr(addUserPasswordPrompt, keyboard);
                    // Prompt the user to select a language from the available options
                    query = "SELECT * FROM mngo1.Language";
                    count = executeQuery(query, dbconn, Entity.LANGUAGE);
                    if (count == 0) {
                        System.err.println("There are no languages to select");
                    }
                    else {
                        int lid = promptUserForInt(addUserLanguagePrompt, keyboard, dbconn, Entity.LANGUAGE);
                        // Get the next userId to use for this user
                        String idQuery = "SELECT MAX(userId) FROM mngo1.Person";
                        int newId = getNextId(idQuery, dbconn);
                        // format and execute the SQL statement to add a user account with the provided information
                        statement = String.format("INSERT INTO mngo1.Person VALUES (%d, '%s', '%s', '%s', 50, %d, 1)", newId, username, password, email, lid);
                        executeStmt(statement, dbconn);
                        System.out.println("User account created successfully.");

                        // also get them a new billing record
                        idQuery = "SELECT MAX(brid) FROM mngo1.BillingRecord";
                        int newBrid = getNextId(idQuery, dbconn);
                        statement = String.format("INSERT INTO mngo1.BillingRecord VALUES (%d, NULL, NULL, %d)", newBrid, newId);
                        executeStmt(statement, dbconn);
                    }
                    // Hold the screen to see result before returning to main menu
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;
                
                case 2: // Update user account
                    // Get all users and print out some of their information so the user can select which account to update based on that information
                    query = "SELECT * FROM mngo1.Person";
                    count = executeQuery(query, dbconn, Entity.USER);
                    // If there are no users, we can't update anything, so we print an error message and return to the previous menu
                    if (count == 0)
                        System.err.println("There are no users to select.");
                    else {
                        // If user exists to update, call helper method to prompt user for specific user 
                        int userId = promptUserForInt(selectUserForUpdatePrompt, keyboard, dbconn, Entity.USER);
                        
                            // At this point we have found the user the user wants to update,
                            // so we can prompt them for the new information for that user
                        
                        String newEmail = promptUserForStr(updateUserEmailPrompt, keyboard);
                        statement = String.format("UPDATE mngo1.Person SET email = '%s' WHERE userId = %d", newEmail, userId);
                        executeStmt(statement, dbconn);
                        System.out.println("User account updated successfully.");
                    }
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;

                case 3: // Delete user account
                    query = "SELECT * FROM mngo1.Person";
                    count = executeQuery(query, dbconn, Entity.USER);
                    if (count == 0) {
                        System.err.println("There are no users to select.");
                    } else {
                        int userId = promptUserForInt(selectUserForDeletePrompt, keyboard, dbconn, Entity.USER);
                        
                        // Check constraints
                        String unpaidCheck = String.format(
                            "SELECT COUNT(*) FROM mngo1.Invoice i " +
                            "WHERE i.status = 'unpaid' AND i.brid IN " +
                            "(SELECT brid FROM mngo1.BillingRecord WHERE userId = %d)", userId);
                        
                        String ticketCheck = String.format(
                            "SELECT COUNT(*) FROM mngo1.Ticket " +
                            "WHERE userId = %d AND outcome = 'Waiting'", userId);
                        
                        try {
                            Statement checkStmt = dbconn.createStatement();
                            ResultSet rs1 = checkStmt.executeQuery(unpaidCheck);
                            rs1.next();
                            int unpaidCount = rs1.getInt(1);
                            
                            ResultSet rs2 = checkStmt.executeQuery(ticketCheck);
                            rs2.next();
                            int openTicketCount = rs2.getInt(1);
                            
                            if (unpaidCount > 0 || openTicketCount > 0) {
                                System.err.println("Cannot delete user: has unpaid invoices or open support tickets.");
                            } else {
                                // NEW: Explicitly delete messages (and any other dependent data)
                                String deleteMessages = "DELETE FROM mngo1.Message WHERE cid IN " +
                                                      "(SELECT cid FROM mngo1.Conversation WHERE userId = " + userId + ")";
                                executeStmt(deleteMessages, dbconn);
                                
                                // Optional: delete conversations too (cascades should handle most, but explicit is safer)
                                String deleteConvos = "DELETE FROM mngo1.Conversation WHERE userId = " + userId;
                                executeStmt(deleteConvos, dbconn);
                                
                                statement = String.format("DELETE FROM mngo1.Person WHERE userId = %d", userId);
                                executeStmt(statement, dbconn);
                                System.out.println("User account and all related messages/conversations deleted successfully.");
                            }
                        } catch (SQLException e) {
                            System.err.println("Error during deletion: " + e.getMessage());
                        }
                    }
                    
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) exit = false;
                        else System.err.println("Please enter 1 to return to the main menu.");
                    }
                    return;
                case 4: // update address
                    // Get all users and print out some of their information so the user can select which account to update based on that information
                    query = "SELECT * FROM mngo1.BillingRecord";
                    count = executeQuery(query, dbconn, Entity.BILLING_RECORD);
                    // If there are no billing records, skip
                    if (count == 0)
                        System.err.println("There are no billing records to select.");
                    else {
                        // If user exists to update, call helper method to prompt user for specific user 
                        int brid = promptUserForInt(selectBillingRecordAddressPrompt, keyboard, dbconn, Entity.BILLING_RECORD);
                        
                        // prompt them for a new pay address
                        
                        String newAddress = promptUserForStr(updateBillingAddress, keyboard);
                        statement = String.format("UPDATE mngo1.BillingRecord SET payaddress = '%s' WHERE brid = %d", newAddress, brid);
                        executeStmt(statement, dbconn);
                        System.out.println("User account updated successfully.");
                    }
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;
                case 5: // update pay method
                    // Get all users and print out some of their information so the user can select which account to update based on that information
                    query = "SELECT * FROM mngo1.BillingRecord";
                    count = executeQuery(query, dbconn, Entity.BILLING_RECORD);
                    // If there are no billing records, skip
                    if (count == 0)
                        System.err.println("There are no billing records to select.");
                    else {
                        // If user exists to update, call helper method to prompt user for specific user 
                        int brid = promptUserForInt(selectBillingRecordMethodPrompt, keyboard, dbconn, Entity.BILLING_RECORD);
                        
                        // prompt them for a new pay address
                        
                        String newAddress = promptUserForStr(updateBillingMethod, keyboard);
                        statement = String.format("UPDATE mngo1.BillingRecord SET paymethod = '%s' WHERE brid = %d", newAddress, brid);
                        executeStmt(statement, dbconn);
                        System.out.println("User account updated successfully.");
                    }
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;
                case 6: // back to main menu
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    /*---------------------------------------------------------------------
    |  Method manageMsg(keyboard, dbconn)
    |
    |  Purpose: manage conversations and messages, including starting new conversations, 
                adding messages to conversations, and updating message feedback
    |
    |  Pre-condition: User is connected to the database and selected UI option 2 to manage messages
    |  Post-condition: User conversations and messages are managed successfully based on further input
    |
    |  Parameters:
    |      Scanner keybaord - used to get user input for managing conversations and messages
    |      Connection dbconn - used to execute SQL statements to manage conversations and messages in the database
    |      
    |  Returns: None.
    *-------------------------------------------------------------------*/
    private static void manageMsg(Scanner keyboard, Connection dbconn) {
        System.out.println(manageConvoInterface);
        String statement, query;
        int count;
        boolean exit;
        int input = 0;

        while (true) {
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }
            switch (input) {

                    // We must have a secondary prompt to get the correct message operation
                    // Their are 3 operations: new conversation, add messages to conversation, update message feedback. 
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage the conversations and messages in the database based on user input

                case 1: // New conversation
                    {
                        String title = promptUserForStr(addConvoTitlePrompt, keyboard);

                        String userQuery = "SELECT * FROM mngo1.Person";
                        executeQuery(userQuery, dbconn, Entity.USER);
                        int userId = promptUserForInt("Enter userId: ", keyboard, dbconn, Entity.USER);
                        
                        String personaQuery = "SELECT * FROM mngo1.Persona";
                        executeQuery(personaQuery, dbconn, Entity.PERSONA);
                        System.out.println("\nSelect persona (or 0 for none):");
                        // Allow 0 for no persona
                        int pid = 0;
                        try {
                            pid = keyboard.nextInt();
                            keyboard.nextLine();
                            if (pid != 0) {
                                // Verify persona exists if not 0
                                String verifyPid = "SELECT * FROM mngo1.Persona WHERE pid = " + pid;
                                Statement stmt = dbconn.createStatement();
                                ResultSet rs = stmt.executeQuery(verifyPid);
                                if (!rs.next()) {
                                    System.err.println("Warning: Persona " + pid + " not found. Setting to NULL.");
                                    pid = 0; // or handle as needed
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Invalid persona input, defaulting to NULL.");
                            pid = 0;
                            keyboard.nextLine();
                        }

                        String idQuery = "SELECT MAX(cid) FROM mngo1.Conversation";
                        int newId = getNextId(idQuery, dbconn);
                        // format and execute the SQL statement to add a conversation with the provided title
                        statement = String.format("INSERT INTO mngo1.Conversation VALUES (%d, '%s', %d, %d)", newId, title, userId, (pid == 0 ? "NULL" : pid));
                        executeStmt(statement, dbconn);
                        System.out.println("Conversation created successfully.");
                        exit = true;
                        System.out.println("\nEnter 1 to return to main menu");
                        while (exit){
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            }
                            else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        }
                        return;
                    }
                
                case 2: // Add messages to conversation
                    {
                        // Get all conversations to find which conversation to add messages to
                        query = "SELECT * FROM mngo1.Conversation";
                        count = executeQuery(query, dbconn, Entity.CONVERSATION);
                        if (count == 0) {
                            System.err.println("There are no conversations to select.");
                            return;
                        } else {
                            // At this point we have found the conversation the user wants
                            // So we perform a similar operation to add a message to the conversation
                            int cid = promptUserForInt(selectConvoForUpdatePrompt, keyboard, dbconn, Entity.CONVERSATION);
                            String msg = promptUserForStr("Enter message: ", keyboard);
                            String sender = promptUserForStr("Enter sender (user/ai): ", keyboard);
                            String mid = "SELECT NVL (MAX(mid), 0) + 1 FROM mngo1.Message WHERE cid = " + cid;
                            int newId = getNextId(mid, dbconn);
                            
                            // attempt to insert message
                            statement = String.format(
                                "INSERT INTO mngo1.Message (mid, cid, message, sender, rating, timestamp) " +
                                "VALUES (%d, %d, '%s', '%s', 0, SYSDATE)", 
                                newId, cid, msg, sender);

                            // execute: but if user hits message limit, it will fail
                            boolean success = executeStmt(statement, dbconn);
                            
                            // if the user sent this, decrement messagesLeft
                            if (sender.equals("user") && success) {
                                statement = String.format(
                                    "UPDATE mngo1.Person SET messagesLeft = messagesLeft - 1 " +
                                    "WHERE userId = (SELECT userId FROM mngo1.Conversation WHERE cid = %d)", cid);
                                executeStmt(statement, dbconn);
                            }
                            System.out.println("Message added to conversation successfully.");
                            exit = true;
                            System.out.println("\nEnter 1 to return to main menu");
                            while (exit){
                                input = keyboard.nextInt();
                                keyboard.nextLine();
                                if (input == 1) {
                                    exit = false;
                                }
                                else {
                                    System.err.println("Please enter 1 to return to the main menu.");
                                }
                            }
                            return;
                        }
                    }
                case 3: // Update message feedback
                    {
                        // Get all conversations to find which conversation to add messages to
                        query = "SELECT * FROM mngo1.Conversation";
                        count = executeQuery(query, dbconn, Entity.CONVERSATION);
                        if (count == 0) {
                            System.err.println("There are no conversations to select.");
                            return;
                        }
                        int cid = promptUserForInt(selectConvoToFindMessagePrompt, keyboard, dbconn, Entity.CONVERSATION);
                        
                        // Get all messages to find which message to update feedback for
                        query = "SELECT * FROM mngo1.Message WHERE cid = " + cid;
                        count = executeQuery(query, dbconn, Entity.MESSAGE);
                        if (count == 0) {
                            System.err.println("There are no messages to select.");
                        }
                        else {
                            int mid = promptUserForInt(selectMessageForFeedbackPrompt, keyboard, dbconn, Entity.MESSAGE);
                            int rating = promptUserForInt("Enter Rating (Thumbs Up = 1/Down = -1): ", keyboard, dbconn, Entity.RATING_VALUE);
                            String ratingText = promptUserForOptStr("Feedback Text: ", keyboard);
                            statement = String.format(
                                "UPDATE mngo1.Message SET rating = %d, ratingText = '%s' " +
                                "WHERE mid = %d AND cid = %d", rating, ratingText, mid, cid);
                            executeStmt(statement, dbconn);
                            System.out.println("Feedback updated.");
                        }
                        exit = true;
                        System.out.println("\nEnter 1 to return to main menu");
                        while (exit){
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            }
                            else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        }
                        return;
                    }

                case 4: // back to main menu
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    /*---------------------------------------------------------------------
    |  Method manageWorkspace(keyboard, dbconn)
    |
    |  Purpose: manage workspaces, including creating new workspaces and modifying existing workspaces
    |
    |  Pre-condition: User is connected to the database and selected UI option 3 to manage workspaces
    |  Post-condition: User workspaces are managed successfully based on further input
    |
    |  Parameters:
    |      Scanner keybaord - used to get user input for managing workspaces
    |      Connection dbconn - used to execute SQL statements to manage user workspaces in the database
    |      
    |  Returns: None.
    *-------------------------------------------------------------------*/
    private static void manageWorkspace(Scanner keyboard, Connection dbconn) {
        System.out.println(workspaceInterface);
        String statement, query;
        boolean exit;
        int count;
        int input = 0;
        String userQuery = "SELECT * FROM mngo1.Person";

        while (true) {
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }
            switch (input) {

                    // We must have a secondary prompt to get the correct operation
                    // Their are 2 operations: create workspace, modify workspace.
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage the workspaces

                case 1: // New workspace
                    // Prompt user for privacy setting of workspace, then format and execute SQL statement to create a workspace with that privacy setting
                    String privacy = promptUserForStr(addWorkspacePrivacyPrompt, keyboard);
                    String widQuery = "SELECT NVL(MAX(wid), 0) + 1 FROM mngo1.Workspace";
                    int newWid = getNextId(widQuery, dbconn);
                    executeQuery(userQuery, dbconn, Entity.USER);
                    int ownerId = promptUserForInt("Enter owner userId: ", keyboard, dbconn, Entity.USER);
                        
                    statement = String.format("INSERT INTO mngo1.Workspace VALUES (%d, '%s', %d)", newWid, privacy, ownerId);
                    executeStmt(statement, dbconn);
                    System.out.println("Workspace created successfully.");
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;
                
                case 2: // Modify workspace
                    // Get all workspaces to find which workspace to modify
                    query = "SELECT * FROM mngo1.Workspace";
                    count = executeQuery(query, dbconn, Entity.WORKSPACE);
                    if (count == 0) {
                        System.err.println("There are no workspaces to select.");
                    }
                    else {
                        // Prompt user for which workspace to modify.
                        int wid = promptUserForInt(selectWorkspacePrompt, keyboard, dbconn, Entity.WORKSPACE);
                        System.out.println(modifyWorkspaceInterface);
                        int choice = keyboard.nextInt();
                        keyboard.nextLine();

                        if (choice == 1) { // change privacy
                            String newPrivacy = promptUserForStr("Enter new privacy (public/private): ", keyboard);
                            statement = String.format(
                                "UPDATE mngo1.Workspace SET privacy = '%s' WHERE wid = %d", 
                                newPrivacy, wid);
                            executeStmt(statement, dbconn);
                        } 
                        else if (choice == 2) { // change owner
                            executeQuery(userQuery, dbconn, Entity.USER);
                       
                            int newOwner = promptUserForInt("Enter new owner userId: ", keyboard, dbconn, Entity.USER);
                            statement = String.format(
                                "UPDATE mngo1.Workspace SET ownerId = %d WHERE wid = %d", 
                                newOwner, wid);
                            executeStmt(statement, dbconn);
                        }
                        else if (choice == 3) { // add a member
                            executeQuery(userQuery, dbconn, Entity.USER);
                            int newMember = promptUserForInt("Enter new member userId: ", keyboard, dbconn, Entity.USER);
                            statement = String.format(
                                "INSERT INTO mngo1.UserWorkspace VALUES (%d, %d)", newMember, wid
                            );
                            boolean success = executeStmt(statement, dbconn);
                            if (!success) {
                                System.err.println("User already a member");
                                return;
                            }
                        }
                        else {
                            return;
                        }
                        System.out.println("Workspace modified successfully.");
                    }
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;

                case 3: // check members in a workspace
                    // Get all workspaces
                    query = "SELECT * FROM mngo1.Workspace";
                    count = executeQuery(query, dbconn, Entity.WORKSPACE);
                    if (count == 0) {
                        System.err.println("There are no workspaces to select.");
                    }
                    else {
                        // Prompt user for which workspace to modify.
                        int wid = promptUserForInt(selectWorkspacePrompt, keyboard, dbconn, Entity.WORKSPACE);
                        statement = String.format("SELECT p.username FROM mngo1.Person p JOIN mngo1.UserWorkspace uw ON p.userId = uw.userId WHERE uw.wid = %d", wid);
                        executeStmt(statement, dbconn);
                    }
                    return;
                case 4: // Add conversation to workspace
                    {
                        // Show all workspaces
                        String wsQuery = "SELECT * FROM mngo1.Workspace";
                        executeQuery(wsQuery, dbconn, Entity.WORKSPACE);

                        int wid = promptUserForInt("Select workspace to add conversation to: ", 
                                                 keyboard, dbconn, Entity.WORKSPACE);

                        // Show all conversations
                        String convoQuery = """
                            SELECT c.cid, c.title, c.userId, c.pid, p.username 
                            FROM mngo1.Conversation c 
                            JOIN mngo1.Person p ON c.userId = p.userId 
                            ORDER BY c.cid
                            """;
                        executeQuery(convoQuery, dbconn, Entity.CONVERSATION);

                        int cid = promptUserForInt("Select conversation to add: ", 
                                                 keyboard, dbconn, Entity.CONVERSATION);

                        try {
                            Statement stmt = dbconn.createStatement();

                            // Get owner of the conversation
                            ResultSet rs = stmt.executeQuery(
                                "SELECT userId FROM mngo1.Conversation WHERE cid = " + cid);
                            if (!rs.next()) {
                                System.err.println("Conversation not found.");
                                break;
                            }
                            int convoOwnerId = rs.getInt("userId");

                            // Get owner of the workspace
                            rs = stmt.executeQuery(
                                "SELECT ownerId FROM mngo1.Workspace WHERE wid = " + wid);
                            if (!rs.next()) {
                                System.err.println("Workspace not found.");
                                break;
                            }
                            int workspaceOwnerId = rs.getInt("ownerId");

                            // Check if user is member of workspace OR is the owner
                            String memberCheck = """
                                SELECT COUNT(*) FROM mngo1.UserWorkspace 
                                WHERE wid = """ + wid + " AND userId = " + convoOwnerId;

                            rs = stmt.executeQuery(memberCheck);
                            rs.next();
                            int isMember = rs.getInt(1);

                            boolean canAdd = (isMember > 0) || (convoOwnerId == workspaceOwnerId);

                            if (!canAdd) {
                                System.err.println("Cannot add conversation.");
                                System.err.println("User " + convoOwnerId + " is not a member of workspace " + wid + 
                                                 " and is not the owner.");
                                break;
                            }

                            // Add to junction table
                            String insertStmt = String.format(
                                "INSERT INTO mngo1.ConversationWorkspace (cid, wid) VALUES (%d, %d)", 
                                cid, wid);

                            executeStmt(insertStmt, dbconn);
                            System.out.println("Conversation " + cid + " added to workspace " + wid);

                        } catch (SQLException e) {
                            if (e.getMessage().toLowerCase().contains("unique") || 
                                e.getMessage().toLowerCase().contains("primary key")) {
                                System.err.println("This conversation is already in that workspace.");
                            } else {
                                System.err.println("Database error: " + e.getMessage());
                            }
                        }
                    }
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        try {
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            } else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        } catch (InputMismatchException e) {
                            keyboard.nextLine();
                            System.err.println("Please enter 1.");
                        }
                    }
                    return;

                case 5: // back to main menu
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    /*---------------------------------------------------------------------
    |  Method managePersona (keyboard, dbconn)
    |
    |  Purpose: manage personas, including creating new personas and deleting existing personas
    |
    |  Pre-condition: User is connected to the database and selected UI option 4 to manage personas
    |  Post-condition: User personas are managed successfully based on further input
    |
    |  Parameters:
    |      Scanner keyboard - used to get user input for managing personas
    |      Connection dbconn - used to execute SQL statements to manage user personas in the database
    |      
    |  Returns: None.
    *-------------------------------------------------------------------*/
    private static void managePersona(Scanner keyboard, Connection dbconn) {
        System.out.println(personaInterface);
        String statement, query;
        boolean exit;
        int count;
        int input = 0;

        while (true) {
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }
            switch (input) {

                    // We must have a secondary prompt to get the correct operation
                    // Their are 2 operations: create persona, delete persona.
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage the personas

                case 1: // New persona
                    {
                        // Prompt user for name and personality strings for the new persona
                        String name = promptUserForStr(addPersonaNamePrompt, keyboard);
                        String personality = promptUserForStr(addPersonaPersonalityPrompt, keyboard);
                        // Get next persona ID
                        String pidQuery = "SELECT NVL(MAX(pid), 0) + 1 FROM mngo1.Persona";
                        int newPid = getNextId(pidQuery, dbconn);
                        // Format and execute SQL statement to create new persona with provided inputs
                        statement = String.format(
                            "INSERT INTO mngo1.Persona (pid, name, personality) " +
                            "VALUES (%d, '%s', '%s')", 
                            newPid, name, personality);
                        executeStmt(statement, dbconn);
                        System.out.println("Persona created successfully with ID: " + newPid);
                        exit = true;
                        System.out.println("\nEnter 1 to return to main menu");
                        while (exit){
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            }
                            else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        }
                        return;   
                    }

                case 2: // delete persona
                    {
                        // Show all current personas
                        query = "SELECT * FROM mngo1.Persona";
                        count = executeQuery(query, dbconn, Entity.PERSONA);
                        if (count == 0) {
                            System.err.println("There are no personas to select.");
                        }
                        else {
                            int pid = promptUserForInt(selectPersonaToDeletePrompt, keyboard, dbconn, Entity.PERSONA);

                            // Check how many conversations are using this persona
                            String checkQuery = 
                                "SELECT COUNT(*) AS convo_count " +
                                "FROM mngo1.Conversation " +
                                "WHERE pid = " + pid;

                            try {
                                Statement stmt = dbconn.createStatement();
                                ResultSet rs = stmt.executeQuery(checkQuery);
                                rs.next();
                                int convoCount = rs.getInt("convo_count");

                                if (convoCount > 5) {
                                    System.err.println("Cannot delete persona: It is used in " + convoCount + 
                                                     " conversations (more than 5 allowed).");
                                } 
                                else {
                                    statement = "DELETE FROM mngo1.Persona WHERE pid = " + pid;
                                    executeStmt(statement, dbconn);
                                    System.out.println("Persona deleted successfully.");
                                }
                            } catch (SQLException e) {
                                System.err.println("Error checking persona usage: " + e.getMessage());
                            }
                        }
                    }
                    
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        try {
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            }
                            else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        } catch (InputMismatchException e) {
                            keyboard.nextLine();
                            System.err.println("Please enter 1.");
                        }
                    }
                    return;

                case 3: // back to main menu
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

        /*---------------------------------------------------------------------
        |  Method managePromptLibrary(keyboard, dbconn)
        |
        |  Purpose: manages the prompt library, including adding new prompt templates and updating existing prompt templates
        |
        |  Pre-condition: User is connected to the database and selected UI option 5 to manage prompt library
        |  Post-condition: Prompt templates are managed successfully based on further input
        |
        |  Parameters:
        |      Scanner keyboard - used to get user input for managing prompt templates
        |      Connection dbconn - used to execute SQL statements to manage prompt templates in the database
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void managePromptLibrary(Scanner keyboard, Connection dbconn) {
        System.out.println(promptInterface);
        String statement, query;
        boolean exit;
        int count;
        int input = 0;

        while (true) {
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }
            switch (input) {

                    // We must have a secondary prompt to get the correct operation
                    // Their are 2 operations: create prompt template, update prompt template.
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage the prompt templates

                case 1: // New prompt template
                    // Prompt user for instructions and privacy setting for the new prompt template
                    String instructions = promptUserForStr(addPromptInstructionPrompt, keyboard);
                    String privacy = promptUserForStr(addPromptPrivacyPrompt, keyboard);
                    int userId = 1; // temp
                    String idQuery = "SELECT MAX(upid) FROM mngo1.UserPrompt";
                    int newId = getNextId(idQuery, dbconn);
                    // Format and execute SQL statement to create new prompt template with provided inputs
                    statement = String.format("INSERT INTO mngo1.UserPrompt VALUES (%d, '%s', '%s', %d)", newId, instructions, privacy, userId);
                    executeStmt(statement, dbconn);
                    System.out.println("Prompt template created successfully with ID: " + newId);
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;
                
                case 2: // Update prompt template privacy
                    // Get all prompt templates to find which prompt template to update
                    query = "SELECT * FROM mngo1.UserPrompt";
                    count = executeQuery(query, dbconn, Entity.USER_PROMPT);
                    if (count == 0) {
                        System.err.println("There are no prompt templates to select.");
                    }
                    else {
                        // Prompt user for which prompt template to update
                        int upid = promptUserForInt(selectPromptPrompt, keyboard, dbconn, Entity.USER_PROMPT);
                        String newPrivacy = promptUserForStr(updatePromptPrivacyPrompt, keyboard);
                        statement = String.format("UPDATE mngo1.UserPrompt SET privacy = '%s' WHERE upid = %d", newPrivacy, upid);
                        executeStmt(statement, dbconn);
                        System.out.println("Successfully updated privacy.");
                    }
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;
                
                case 3:
                    // Get all prompt templates to find which prompt template to update
                    query = "SELECT * FROM mngo1.UserPrompt";
                    count = executeQuery(query, dbconn, Entity.USER_PROMPT);
                    if (count == 0) {
                        System.err.println("There are no prompt templates to select.");
                    }
                    else {
                        // Prompt user for which prompt template to update
                        int upid = promptUserForInt(selectPromptInstructionPrompt, keyboard, dbconn, Entity.USER_PROMPT);
                        String newInstruction = promptUserForStr(updatePromptInstructionPrompt, keyboard);
                        statement = String.format("UPDATE mngo1.UserPrompt SET instructions = '%s' WHERE upid = %d", newInstruction, upid);
                        executeStmt(statement, dbconn);
                        System.out.println("Successfully updated instruction.");
                    }
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;

                case 4: // back to main menu
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    /*---------------------------------------------------------------------
    |  Method manageSubs(keyboard, dbconn)
    |
    |  Purpose: manages subscriptions, including upgrading user subscription tiers and checking how close users are to their message limits
    |
    |  Pre-condition: User is connected to the database and selected UI option 6 to manage subscriptions
    |  Post-condition: Subscriptions are managed successfully based on further input
    |
    |  Parameters:
    |      Scanner keyboard - used to get user input for managing subscriptions
    |      Connection dbconn - used to execute SQL statements to manage subscriptions in the database
    |
    |  Returns: None.
    *-------------------------------------------------------------------*/
    private static void manageSubs(Scanner keyboard, Connection dbconn) {
        System.out.println(subscriptionInterface);
        String query;
        boolean exit;
        int count;
        int input = 0;

        while (true) {
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }
            switch (input) {

                    // We must have a secondary prompt to get the correct operation
                    // Their are 2 operations: upgrade user subscription tier, check user's message limit.
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage subscriptions 

                case 1: // Upgrade user tier
                    {
                        // Get all users to find which user to upgrade
                        query = "SELECT * FROM mngo1.Person";
                        count = executeQuery(query, dbconn, Entity.USER);
                        if (count == 0) {
                            System.err.println("There are no users to select.");
                        }
                        else {
                            // Prompt user for which user to upgrade
                            int userId = promptUserForInt(selectUserForUpgradePrompt, keyboard, dbconn, Entity.USER);
                            int newTier = promptUserForInt("Enter new tier (1-3): ", keyboard, dbconn, Entity.SUBSCRIPTION_TIER);
                            
                            String statement = String.format(
                                "UPDATE mngo1.Person SET mtid = %d, messagesLeft = (SELECT messageLimit FROM mngo1.Membership WHERE mtid = %d) WHERE userId = %d", newTier, newTier, userId);
                            
                            executeStmt(statement, dbconn);
                            System.out.println("Subscription upgraded.");
                            }
                        exit = true;
                        System.out.println("\nEnter 1 to return to main menu");
                        while (exit){
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            }
                            else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        }
                        return;
                    }
                
                case 2: // Check user limit
                    {
                        // Get all users to find which user to check message limit for
                        query = "SELECT * FROM mngo1.Person";
                        count = executeQuery(query, dbconn, Entity.USER);
                        if (count == 0) {
                            System.err.println("There are no users to select.");
                        }
                        else {
                            // Prompt user for which user to check message limit for
                            int userId = promptUserForInt(selectUserForLimitCheck, keyboard, dbconn, Entity.USER);
                            String statement = String.format(
                                "SELECT messagesLeft FROM mngo1.Person WHERE userId = %d", userId
                            );
                            executeStmt(statement, dbconn);
                        }
                        exit = true;
                        System.out.println("\nEnter 1 to return to main menu");
                        while (exit){
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            }
                            else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        }
                        return;
                    }
                case 3: // back to main menu
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

        /*---------------------------------------------------------------------
        |  Method manageBilling(keyboard, dbconn)
        |
        |  Purpose: manages billing, including generating invoices and marking invoices as paid
        |
        |  Pre-condition: User is connected to the database and selected UI option 7 to manage billing
        |  Post-condition: Billing is managed successfully based on further input
        |
        |  Parameters:
        |      Scanner keyboard - used to get user input for managing billing
        |      Connection dbconn - used to execute SQL statements to manage billing in the database
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void manageBilling(Scanner keyboard, Connection dbconn) {
        System.out.println(billingInterface);
        String statement, query;
        boolean exit;
        int count;
        int input = 0;

        while (true) {
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }
            switch (input) {

                    // We must have a secondary prompt to get the correct operation
                    // Their are 2 operations: genearte invoice, mark invoice as paid.
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage billing

                case 1: // Generate invoice
                    query = "SELECT * FROM mngo1.Person";
                    count = executeQuery(query, dbconn, Entity.USER);
                    // If there are no users, we can't update anything, so we print an error message and return to the previous menu
                    if (count == 0) {
                        System.err.println("There are no users to select.");
                        return;
                    }

                    int userIdForInvoice = promptUserForInt("Enter userId to generate invoice for: ", keyboard, dbconn, Entity.USER);
                    // Simple invoice generation
                    String invQuery = "SELECT NVL(MAX(invid), 0) + 1 FROM mngo1.Invoice";
                    int newInvId = getNextId(invQuery, dbconn);
                    
                    statement = String.format(
                        "INSERT INTO mngo1.Invoice (invid, status, amount, month, brid) " +
                        "VALUES (%d, 'unpaid', 9.99, SYSDATE, " +
                        "(SELECT brid FROM mngo1.BillingRecord WHERE userId = %d))", 
                        newInvId, userIdForInvoice);
                    
                    executeStmt(statement, dbconn);
                    System.out.println("Monthly invoice generated.");
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;
                
                case 2: // Mark invoice as paid
                    // Get all invoices to find which invoice to mark as paid
                    query = "SELECT * FROM mngo1.Invoice";
                    count = executeQuery(query, dbconn, Entity.INVOICE);
                    if (count == 0) {
                        System.err.println("No invoices found.");
                        return;
                    }
                    
                    int invId = promptUserForInt(selectInvoiceToPayPrompt, keyboard, dbconn, Entity.INVOICE);
                    statement = "UPDATE mngo1.Invoice SET status = 'paid' WHERE invid = " + invId;
                    executeStmt(statement, dbconn);
                    System.out.println("Invoice marked as paid.");
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;

                case 3: // back to main menu
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    /*---------------------------------------------------------------------
    |  Method manageTickets(keyboard, dbconn)
    |
    |  Purpose: manages tickets, including creating support tickets, assigning tickets to agents, and marking tickets as resolved
    |
    |  Pre-condition: User is connected to the database and selected UI option 8 to manage tickets
    |  Post-condition: Tickets are managed successfully based on further input
    |
    |  Parameters:
    |      Scanner keyboard - used to get user input for managing tickets
    |      Connection dbconn - used to execute SQL statements to manage tickets in the database
    |
    |  Returns: None.
    *-------------------------------------------------------------------*/
    private static void manageTickets(Scanner keyboard, Connection dbconn) {
        System.out.println(supportInterface);
        String statement, query;
        boolean exit;
        int count;
        int input = 0;
        
        while (true) {
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }
            switch (input) {

                    // We must have a secondary prompt to get the correct operation
                    // Their are 3 operations: Create ticket, assign ticket to agent, mark ticket as resolved.
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage tickets

                case 1: // New ticket
                    {
                        // Prompt user for topic of support ticket, then format and execute SQL statement to create a new support ticket with that topic
                        String topic = promptUserForStr(addTicketTopicPrompt, keyboard);
                        query = "SELECT * FROM mngo1.Person";
                        count = executeQuery(query, dbconn, Entity.USER);

                        int userId = promptUserForInt("Enter userId creating the ticket: ", keyboard, dbconn, Entity.USER);
                        
                        String tidQuery = "SELECT NVL(MAX(tid), 0) + 1 FROM mngo1.Ticket";
                        int newTid = getNextId(tidQuery, dbconn);
                        
                        statement = String.format(
                            "INSERT INTO mngo1.Ticket (tid, duration, outcome, topic, userId, aid) " +
                            "VALUES (%d, 0, 'Waiting', '%s', %d, NULL)", 
                            newTid, topic, userId);
                        
                        executeStmt(statement, dbconn);
                        System.out.println("Support ticket created.");
                        exit = true;
                        System.out.println("\nEnter 1 to return to main menu");
                        while (exit){
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            }
                            else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        }
                        return;
                    }
                
                case 2: // Assign ticket to agent
                    {
                        // Get all tickets to find which ticket to assign to an agent
                        query = "SELECT * FROM mngo1.Ticket";
                        count = executeQuery(query, dbconn, Entity.SUPPORT_TICKET);
                        if (count == 0) {
                            System.err.println("There are no tickets to select.");
                            return;
                        }
                        int tid = promptUserForInt(selectTicketForAgentPrompt, keyboard, dbconn, Entity.SUPPORT_TICKET);

                        // At this point we have selected the ticket we want to assign
                        // So we perform a similar operation to assign an agent to the ticket

                        // Get all agents to find which agent to assign the ticket to
                        query = "SELECT * FROM mngo1.Agent";
                        count = executeQuery(query, dbconn, Entity.AGENT);
                        if (count == 0) {
                            System.err.println("There are no agents to select.");
                        }
                        else {
                            // Prompt user for which agent to assign the ticket to
                            
                            int aid = promptUserForInt(selectAgentPrompt, keyboard, dbconn, Entity.AGENT);
                            statement = "UPDATE mngo1.Ticket SET aid = " + aid + " WHERE tid = " + tid;
                            executeStmt(statement, dbconn);
                            System.out.println("Ticket assigned to agent.");
                        }
                        exit = true;
                        System.out.println("\nEnter 1 to return to main menu");
                        while (exit){
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            }
                            else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        }
                        return;
                    }

                case 3: // Complete ticket
                    {
                        // Get all tickets to find which ticket to mark as resolved
                        query = "SELECT * FROM mngo1.Ticket";
                        count = executeQuery(query, dbconn, Entity.SUPPORT_TICKET);
                        if (count == 0) {
                            System.err.println("There are no tickets to select.");
                        }
                        else {
                            // Prompt user for which ticket to mark as resolved
                            int tid = promptUserForInt(selectTicketForStatusPrompt, keyboard, dbconn, Entity.SUPPORT_TICKET);
                            int status = promptUserForInt(selectTicketStatus, keyboard, dbconn, Entity.SUPPORT_TICKET);
                            String statusStr;
                            switch (status) {
                                case 0:
                                    statusStr = "Waiting";
                                    break;
                                case 1:
                                    statusStr = "Escalated";
                                    break;
                                case 2:
                                    statusStr = "Resolved";
                                    break;
                                default:
                                    System.err.println("Not a valid status to set.");
                                    return;
                                
                            }
                            statement = "UPDATE mngo1.Ticket SET outcome = '" + statusStr + "', duration = 30 WHERE tid = " + tid;
                            executeStmt(statement, dbconn);
                            System.out.println("Ticket marked as resolved.");
                        }
                        exit = true;
                        System.out.println("\nEnter 1 to return to main menu");
                        while (exit){
                            input = keyboard.nextInt();
                            keyboard.nextLine();
                            if (input == 1) {
                                exit = false;
                            }
                            else {
                                System.err.println("Please enter 1 to return to the main menu.");
                            }
                        }
                        return;
                    }
                case 4: // back to main menu
                    return;

                default:
                    System.out.println(commandError);
            }
        }
    }

    /*---------------------------------------------------------------------
    |  Method chooseQuery(keyboard, dbconn)
    |
    |  Purpose: allows you to choose a query to answer based on the data in the DB.
    |
    |  Pre-condition: User is connected to the database and selected UI option 9 to choose a query
    |  Post-condition: The query has executed successfully and the 
    |
    |  Parameters:
    |      Scanner keyboard - used to get user input for selecting a query
    |      Connection dbconn - used to execute SQL queries in the database
    |
    |  Returns: None.
    *-------------------------------------------------------------------*/
    private static void chooseQuery(Scanner keyboard, Connection dbconn) {
        System.out.println(queryInterface);
        String query;
        boolean exit;
        int count;
        int input = 0;

        while (true) {
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
                keyboard.nextLine();
                System.err.println("Please input an integer.");
                continue;
            }
            switch (input) {

                    // We now need to prompt the user again to ask them to select a query to execute.
                    // Query 1: For a given user, list all their bookmarked messages across all conversations, including
                    //          the conversation title and timestamp.
                    // Query 2: List all users who have unpaid invoices, including their email, the total amount owed, and
                    //          the date of their last conversation.
                    // Query 3: Identify the most helpful persona: list the persona name that has received the highest percentage
                    //          of "thumbs up" feedback across all conversations linked to it.
                    // (Custom query)
                    // Query 4: Find the top 5 users with the most messages sent, including their username, subscription tier,
                    //          total messages sent, and average message length.

                case 1: // Query 1
                    // Get all users to select one to execute the query on
                    query = "SELECT * FROM mngo1.Person";
                    count = executeQuery(query, dbconn, Entity.USER);
                    if (count == 0) {
                        System.err.println("There are no users to select.");
                    }
                    else {
                        // Prompt user for which user to execute query on
                        int userId = promptUserForInt("\nSelect user to find bookmarks for: ", keyboard, dbconn, Entity.USER);
                        // Now execute query 1 on the selected user
                        query = """
                                SELECT p.userId, p.username, c.title, m.message, m.timestamp
                                FROM mngo1.Bookmark b 
                                JOIN mngo1.Message m ON b.mid = m.mid AND b.cid = m.cid 
                                JOIN mngo1.Conversation c ON m.cid = c.cid 
                                JOIN mngo1.Person p ON b.userId = p.userId 
                                WHERE b.userId = """ + userId + """
                                ORDER BY m.timestamp DESC
                                """;
                        System.out.println("\nBookmarks for userId " + userId + ":");
                        count = executeQuery(query, dbconn, Entity.SPECIAL_QUERY_1);
                    }
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;
                
                case 2: // Query 2
                    query = """
                            SELECT p.userId, p.username, p.email, 
                                SUM(i.amount) AS total_amount_owed, 
                                MAX(m.timestamp) AS last_conversation_date
                            FROM mngo1.Person p
                            JOIN mngo1.BillingRecord br ON p.userId = br.userId
                            JOIN mngo1.Invoice i ON br.brid = i.brid
                            LEFT JOIN mngo1.Conversation c ON p.userId = c.userId
                            LEFT JOIN mngo1.Message m ON c.cid = m.cid
                            WHERE i.status = 'unpaid'
                            GROUP BY p.userId, p.username, p.email
                            HAVING SUM(i.amount) > 0
                            ORDER BY total_amount_owed DESC
                            """;
                    System.out.println("\nUsers with unpaid invoices:");
                    count = executeQuery(query, dbconn, Entity.SPECIAL_QUERY_2);
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;

                case 3: // Query 3
                    query = """
                            SELECT persona_name, thumbs_up_count, total_feedback,
                                ROUND(100.0 * thumbs_up_count / NULLIF(total_feedback, 0), 2) AS thumbs_up_percentage
                            FROM (
                                WITH PersonaStats AS (
                                    SELECT p.pid, p.name AS persona_name,
                                        SUM(CASE WHEN m.rating = 1 THEN 1 ELSE 0 END) AS thumbs_up_count,
                                        COUNT(CASE WHEN m.rating IN (1, -1) THEN 1 END) AS total_feedback
                                    FROM mngo1.Persona p
                                    JOIN mngo1.Conversation c ON p.pid = c.pid
                                    JOIN mngo1.Message m ON c.cid = m.cid
                                    WHERE m.sender = 'ai'
                                    AND m.rating IS NOT NULL
                                    GROUP BY p.pid, p.name
                                )
                                SELECT persona_name, thumbs_up_count, total_feedback
                                FROM PersonaStats
                                ORDER BY ROUND(100.0 * thumbs_up_count / NULLIF(total_feedback, 0), 2) DESC, total_feedback DESC
                            )
                            WHERE ROWNUM = 1
                            """;
                    System.out.println("\n");
                    count = executeQuery(query, dbconn, Entity.SPECIAL_QUERY_3);
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;

                case 4: // Query 4
                    int tier = promptUserForInt("Select membership tier (1-3): ", keyboard, dbconn, Entity.SUBSCRIPTION_TIER);
                    query = """
                            SELECT username, membership_tier, total_messages_sent,
                                avg_message_length_chars, num_conversations, last_message_date
                            FROM (
                                SELECT p.username, mbr.tier AS membership_tier,
                                    COUNT(m.mid) AS total_messages_sent,
                                    ROUND(AVG(LENGTH(m.message)), 2) AS avg_message_length_chars,
                                    COUNT(DISTINCT c.cid) AS num_conversations,
                                    MAX(m.timestamp) AS last_message_date
                                FROM mngo1.Person p
                                JOIN mngo1.Membership mbr ON p.mtid = mbr.mtid
                                JOIN mngo1.Conversation c ON p.userId = c.userId
                                JOIN mngo1.Message m ON c.cid = m.cid
                                WHERE m.sender = 'user' AND mbr.tier = """ + tier + """
                                GROUP BY p.username, mbr.tier
                                ORDER BY COUNT(m.mid) DESC, ROUND(AVG(LENGTH(m.message)), 2) DESC
                            )
                            WHERE ROWNUM <= 5
                            """;
                    String tierLabel = (tier == 1) ? "Free" : (tier == 2) ? "Plus" : "Enterprise";
                    System.out.println("\nTop 5 most active users in the " + tierLabel + " tier:");                    
                    count = executeQuery(query, dbconn, Entity.SPECIAL_QUERY_4);
                    exit = true;
                    System.out.println("\nEnter 1 to return to main menu");
                    while (exit){
                        input = keyboard.nextInt();
                        keyboard.nextLine();
                        if (input == 1) {
                            exit = false;
                        }
                        else {
                            System.err.println("Please enter 1 to return to the main menu.");
                        }
                    }
                    return;

                case 5: // Back to main menu
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    /*---------------------------------------------------------------------
    |  Method executeStmt(statement, dbconn)
    |
    |  Purpose: executues a SQL statement 
    |
    |  Pre-condition: User is connected to the database and a valid SQL statement is provided as input
    |  Post-condition: SQL statement is executed successfully against the database
    |
    |  Parameters:
    |      String statement - a valid SQL statement to execute against the database
    |      Connection dbconn - used to execute the provided SQL statement against the database
    |      
    |  Returns: boolean - true if the statement executed successfully, false if there was an error executing the statement
    *-------------------------------------------------------------------*/
    private static boolean executeStmt(String statement, Connection dbconn) {
        try {
            Statement stmt = dbconn.createStatement();
            boolean hasResultSet = stmt.execute(statement);
            if (hasResultSet) {
                ResultSet rs = stmt.getResultSet();
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= cols; i++) {
                        System.out.print(md.getColumnName(i) + "=" + rs.getObject(i) + " ");
                    }
                    System.out.println();
                }
            }

            // System.out.println("Executed statement: " + statement);
            return true;

        } catch (SQLException e) {
            System.err.println("Error executing statement: " + statement);
            System.err.println(e);
            return false;
        }
    }

    /*---------------------------------------------------------------------
    |  Method executeQuery(query, dbconn, entity)
    |
    |  Purpose: Executes a SQL query and returns the number of rows returned
    |           Additionally prints out some of the data from the returned rows depending on which entity is being queried
    |
    |  Pre-condition: User is connected to the database and a valid SQL query is provided as input, along with the matching type of entity being queried
    |  Post-condition: The SQL query is executed successfully against the database, 
    |                  the relevant data from the returned rows is printed, and the number of rows returned is provided as output
    |
    |  Parameters:
    |      String query - the SQL query to execute
    |      Connection dbconn - used to execute the provided SQL query against the database
    |      Entity entity - the type of entity being queried
    |
    |  Returns: int - the number of rows returned by the query
    *-------------------------------------------------------------------*/
    private static int executeQuery(String query, Connection dbconn, Entity entity) {
        int count = 0; // Counts the number of rows returned by the query
        try {
            Statement stmt = dbconn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            // Go through each row returned by the query and print out some data depending on the type of entity being queried
            while (rs.next()) {
                count++;
                switch (entity) {
                    case USER:
                        System.out.printf("%d: (name: '%s', pass '%s', email: '%s')\n",
                            rs.getInt("userId"), rs.getString("username"), rs.getString("pwd"), rs.getString("email")
                          );
                        break;

                    case MESSAGE:
                        System.out.printf("%d: (message: %s, sender: %s, rating: %d, ratingText: %s, cid: %d)\n",
                            rs.getInt("mid"), rs.getString("message"), rs.getString("sender"), rs.getInt("rating"), rs.getString("ratingText"), rs.getInt("cid")
                          );
                        break;

                    case CONVERSATION:
                        System.out.printf("%d: (title: %s, userId: %d, pid: %d)\n",
                            rs.getInt("cid"), rs.getString("title"), rs.getInt("userId"), rs.getInt("pid")
                          );
                        break;

                    case WORKSPACE:
                        System.out.printf("%d: (privacy: %s, userId: %d)\n",
                            rs.getInt("wid"), rs.getString("privacy"), rs.getInt("ownerId")
                          );
                        break;

                    case PERSONA:
                        System.out.printf("%d: (name: %s, personality: %s)\n",
                            rs.getInt("pid"), rs.getString("name"), rs.getString("personality")
                        );
                        break;

                    case USER_PROMPT:
                        System.out.printf("%d: (instructions: %s, privacy: %s, userId: %d)\n",
                            rs.getInt("upid"), rs.getString("instructions"), rs.getString("privacy"), rs.getInt("userId")
                        );
                        break;

                    case BILLING_RECORD:
                        System.out.printf("%d: (payaddress: %s, paymethod: %s, userId: %d)\n",
                            rs.getInt("brid"), rs.getString("payaddress"), rs.getString("paymethod"), rs.getInt("userId"));
                        break;

                    case INVOICE:
                        System.out.printf("%d: (status: %s, amount: %d)\n",
                            rs.getInt("invid"), rs.getString("status"), rs.getInt("amount")
                        );
                        break;

                    case SUPPORT_TICKET:
                        System.out.printf("%d: (duration: %d, outcome: %s, topic: %s, userId: %d, aid: %d)\n",
                            rs.getInt("tid"), rs.getInt("duration"), rs.getString("outcome"), rs.getString("topic"), rs.getInt("userId"), rs.getInt("aid")
                        );
                        break;

                    case AGENT:
                        System.out.printf("%d: (name: %s)\n",
                            rs.getInt("aid"), rs.getString("name")
                        );
                        break;

                    case LANGUAGE:
                        System.out.printf("%d: (language: %s)\n",
                          rs.getInt("lid"), rs.getString("language")
                        );
                        break;

                    case SPECIAL_QUERY_1:   // Special format for query 1
                        System.out.printf("%d: (user: %s, conversation: %s, message: %s, timestamp: %s)\n",
                            rs.getInt("userId"), rs.getString("username"), rs.getString("title"), rs.getString("message"), rs.getString("timestamp")
                        );
                        break;

                    case SPECIAL_QUERY_2:   // Special format for query 2
                        System.out.printf("%d: (user: %s, total owed: %.2f, last conversation: %s)\n",
                            rs.getInt("userId"), rs.getString("username"), rs.getDouble("total_amount_owed"), rs.getString("last_conversation_date")
                        );
                        break;

                    case SPECIAL_QUERY_3:   // Special format for query 3
                        System.out.printf("Most helpful persona: %s, Thumbs up: %.2f %%\n",
                            rs.getString("persona_name"), rs.getDouble("thumbs_up_percentage")
                        );
                        break;

                    case SPECIAL_QUERY_4:   // Special format for query 4
                        System.out.printf("%d: (user: %s, tier: %d, messages: %d, avg length: %.2f chars, conversations: %d, last: %s)\n",
                            rs.getRow(), rs.getString("username"), rs.getInt("membership_tier"), rs.getInt("total_messages_sent"), rs.getDouble("avg_message_length_chars"), rs.getInt("num_conversations"), rs.getString("last_message_date")
                        );
                        break;
                }
            }
            return count;
            
        } catch (SQLException e) {
            System.err.println("Error occurred while executing query: " + query);
            System.err.println(e);
        }
        return count;
    }

    /*---------------------------------------------------------------------
    |  Method promptUserForStr(prompt, keyboard)
    |
    |  Purpose: prompts the user for a string input and validates the input 
    |
    |  Pre-condition: User needs to input a string in response to a prompt
    |  Post-condition: A valid string input is returned based on the provided prompt and user input
    |
    |  Parameters:
    |      String prompt - the prompt to display to the user when asking for input (from constants defined at the top of the file)
    |      Scanner keyboard - the scanner object used to read user input
    |
    |  Returns: string - the user input to the scanner, between 1 and 255 characters in length
    *-------------------------------------------------------------------*/
    private static String promptUserForStr(String prompt, Scanner keyboard) {
        String msg = prompt;
        String input = "";

        // Repeatedly prompt user until they provide a valid input string between 1 and 255 characters in length
        while (true) {
            System.out.println();
            System.out.print(msg);
            input = keyboard.nextLine().strip();
            if (input.length() > 255 || input.length() < 1) {
                msg = "String must be between length of 1 & 255. Please try again: ";
            }
            else break;
        }
        // Once we have valid input, we return that input string
        return input;
    }

    /*---------------------------------------------------------------------
    |  Method promptUserForOptStr(prompt, keyboard)
    |
    |  Purpose: prompts the user for a string input and validates the input 
    |
    |  Pre-condition: User needs to input a string in response to a prompt
    |  Post-condition: A valid string input is returned based on the provided prompt and user input
    |
    |  Parameters:
    |      String prompt - the prompt to display to the user when asking for input (from constants defined at the top of the file)
    |      Scanner keyboard - the scanner object used to read user input
    |
    |  Returns: string - the user input to the scanner, between 0 and 255 characters in length
    *-------------------------------------------------------------------*/
    private static String promptUserForOptStr(String prompt, Scanner keyboard) {
        String msg = prompt;
        String input = "";

        // Repeatedly prompt user until they provide a valid input string between 0 and 255 characters in length
        while (true) {
            System.out.println();
            System.out.print(msg);
            input = keyboard.nextLine().strip();
            if (input.length() > 255) {
                msg = "String must be between length of 0 & 255. Please try again: ";
            }
            else break;
        }
        // Once we have valid input, we return that input string
        return input;
    }

    /*---------------------------------------------------------------------
    |  Method promptUserForInt(prompt, keyboard)
    |
    |  Purpose: Prompts the user for an integer input. Often used for selecting a pk of an entry.
    |
    |  Pre-condition: User needs to input a integer in response to a prompt
    |  Post-condition: An integer is returned based on the provided prompt and user input
    |
    |  Parameters:
    |      String prompt - the prompt to display to the user when asking for input (from constants defined at the top of the file)
    |      Scanner keyboard - the scanner object used to read user input
    |      Connection dbconn - the connection to the database used to verify the user's selection
    |      Entity entity - the type of entity whose ID you are trying to select
    |
    |  Returns: the selected integer
    *-------------------------------------------------------------------*/
    private static int promptUserForInt(String prompt, Scanner keyboard, Connection dbconn, Entity entity) {
        String msg = prompt;             // The message that prints when an error arises
        int input = -1;              // Init input
        String tableId = "";
        String pk = "";
        boolean firstLine = true;    // Do not print newline on first line

        // Select the correct table id and pk for the type of operation
        switch (entity) {
          case USER:
              tableId = "mngo1.Person";
              pk = "userId";
              break;
          
          case CONVERSATION:
              tableId = "mngo1.Conversation";
              pk = "cid";
              break;
          
          case WORKSPACE:
              tableId = "mngo1.Workspace";
              pk = "wid";
              break;
          
          case USER_PROMPT:
              tableId = "mngo1.UserPrompt";
              pk = "upid";
              break;
          
          case PERSONA:
              tableId = "mngo1.Persona";
              pk = "pid";
              break;

          case BILLING_RECORD:
              tableId = "mngo1.BillingRecord";
              pk = "brid";
              break;
          
          case INVOICE:
              tableId = "mngo1.Invoice";
              pk = "invid";
              break;
          
          case MESSAGE:
              tableId = "mngo1.Message";
              pk = "mid";
              break;
          
          case AGENT:
              tableId = "mngo1.Agent";
              pk = "aid";
              break;
          
          case SUPPORT_TICKET:
              tableId = "mngo1.Ticket";
              pk = "tid";
              break;
          
          case LANGUAGE:
              tableId = "mngo1.Language";
              pk = "lid";
              break;

          default:
              System.err.println("Unknown table id: " + entity);
              break;
        }

        // Repeatedly prompt user until they provide a valid integer input 
        while (true) {
            if (firstLine) firstLine = false;
            else System.out.println();
            System.out.print(msg);

            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
            } catch (InputMismatchException e) {
              msg = "Please input an integer: ";
              keyboard.nextLine();
              continue;
            }

            // Check integer constraints
            if (entity == Entity.SUBSCRIPTION_TIER && input >= 1 && input <= 3) {
                break;
            }   
            else if (entity == Entity.SUBSCRIPTION_TIER) {
                msg = "Value must be between 1-3: ";
                continue;
            }
                
            if (entity == Entity.RATING_VALUE && (input == 1 || input == -1)) {
                break;
            }
            else if (entity == Entity.RATING_VALUE) {
                msg = "Value must be -1 or 1: ";
                continue;
            }

            try {
                String query = String.format("SELECT * FROM %s WHERE %s = %d",
                tableId, pk, input);
                Statement stmt = dbconn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next())
                    break;
                else 
                    msg = "The selected number does not correspond to an entry. Try again: ";
            } catch (SQLException e) {
                msg = "There was an error verifying your selection, please try again: ";
                System.err.println(e);
            }
        }
        // Once we have valid input, we return that input integer
        return input;
    }

        /*---------------------------------------------------------------------
        |  Method getNextId(query, dbconn)
        |
        |  Purpose: generates the next ID for a new entry in a table 
        |
        |  Pre-condition: User needs to input a integer in response to a prompt
        |  Post-condition: A new ID is generated and returned based on the provided SQL query
        |
        |  Parameters:
        |      String query - the SQL query to execute
        |      Connection dbconn - the database connection
        |
        |  Returns: int - the next ID for a new entry in a table based on the provided SQL query and database connection
        *-------------------------------------------------------------------*/
    private static int getNextId(String query, Connection dbconn) {
        try {
            Statement stmt = dbconn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error getting next ID");
        }
        return 1;
    }
}