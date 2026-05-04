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
 *    Description:  The Iterface centers around a series of menus that the user can navigate 
 *                  through with the numpad (1-9)to perform the required operations for managing the database. 
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
        USER, MESSAGE, CONVERSATION, WORKSPACE, PERSONA, USER_PROMPT, INVOICE, SUPPORT_TICKET, AGENT,
        SPECIAL_QUERY_1, SPECIAL_QUERY_2, SPECIAL_QUERY_3, SPECIAL_QUERY_4
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
        "4: Back\n" +
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
        "Select a language id (0: Spanish, 1:English, 2: French)";


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
    private final static String selectMessageForConvoPrompt =
        "Select one of the following messages to add to the conversation: ";
    private final static String selectMessageForFeedbackPrompt =
        "Select one of the following messages to adjust its feedback: ";

    private final static String workspaceInterface =
        "\f------------------------\n" +
        "Manage Workspaces\n" +
        "1: Create workspace\n" +
        "2: Modify workspace\n" +
        "3: Back\n" +
        "------------------------\n";
    private final static String addWorkspacePrivacyPrompt =
        "Select a privacy option for this workspace: ";
    private final static String selectWorkspacePrompt =
        "Select a workspace to modify: ";
    private final static String modifyWorkspaceInterface =
        "\nWhat would you like to do?\n" +
        "1: Change privacy setting\n" +
        "2: Change owner\n" +
        "3: Back\n";

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
        "2: Update prompt template\n" +
        "3: Back\n" +
        "------------------------\n";
    private final static String addPromptPrivacyPrompt = 
        "Select a privacy option for this prompt: ";
    private final static String addPromptInstructionPrompt =
        "Enter the instructions for this prompt: ";
    private final static String selectPromptPrompt =
        "Select a prompt template to update: ";

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

    private final static String supportInterface =
        "\f------------------------\n" +
        "Create Support Tickets\n" +
        "1: Create ticket\n" +
        "2: Assign ticket\n" +
        "3: Mark ticket as resolved\n" +
        "4: Back\n" +
        "------------------------\n";
    private final static String addTicketTopicPrompt =
        "Enter a reason for creating the support ticket: ";
    private final static String selectTicketForAgentPrompt =
        "Select a ticket to assign to an agent: ";
    private final static String selectAgentPrompt =
        "Select an agent to assign to the ticket: ";
    private final static String selectTicketForResolvePrompt =
        "Select a ticket to resolve: ";

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
        Scanner keyboard = new Scanner(System.in);

            // At this point we have connected to the database so we can start the interface loop

        while (true) {
            // Print the main menu interface if this is first time through the loop or their was no errors in previous loop iteration.
            if (init) System.out.println(mainInterface);
            int input = keyboard.nextInt();
            keyboard.nextLine();

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
        String query, statement;
        // Every helper function for operations will infintiy loop until the user exists
        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            
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
                    int lid = promptUserForInt(addUserLanguagePrompt, keyboard);
                    // Get the next userId to use for this user
                    String idQuery = "SELECT MAX(userId) FROM mngo1.Person";
                    int newId = getNextId(idQuery, dbconn);
                    // format and execute the SQL statement to add a user account with the provided information
                    statement = String.format("INSERT INTO mngo1.Person VALUES (%d, '%s', '%s', '%s', %d, 4)", newId, username, password, email, lid);
                    executeStmt(statement, dbconn);
                    System.out.println("User account created successfully.");
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
                        int userId = promptUserForInt(selectUserForUpdatePrompt, keyboard);
                        
                            // At this point we have found the user the user wants to update,
                            // so we can prompt them for the new information for that user
                        
                        String newEmail = promptUserForStr(updateUserEmailPrompt, keyboard);
                        statement = String.format("UPDATE mngo1.Person SET email = '%s' WHERE userId = %d", newEmail, userId);
                        executeStmt(statement, dbconn);
                        System.out.println("User account updated successfully.");
                    }
                    return;

                case 3: // Delete user account
                    // Similar to update user account, we need to get all users 
                    query = "SELECT * FROM mngo1.Person";
                    System.out.println(query);
                    count = executeQuery(query, dbconn, Entity.USER);
                    if (count == 0)
                        System.err.println("There are no users to select.");
                    else {
                        // If the user exists , call helper method to prompt user for specific user to delete
                        int userId = promptUserForInt(selectUserForDeletePrompt, keyboard);
                        statement = String.format("DELETE FROM mngo1.Person WHERE userId = %d", userId);
                        executeStmt(statement, dbconn);
                        System.out.println("User account deleted successfully.");
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

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {

                    // We must have a secondary prompt to get the correct message operation
                    // Their are 3 operations: new conversation, add messages to conversation, update message feedback. 
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage the conversations and messages in the database based on user input

                case 1: // New conversation
                    {
                        String title = promptUserForStr(addConvoTitlePrompt, keyboard);
                        int userId = 1;
                        int pid = 1;
                        // format and execute the SQL statement to add a conversation with the provided title
                        statement = String.format("INSERT INTO mngo1.Conversation VALUES ('%s', %d, %d)", title, userId, pid);
                        executeStmt(statement, dbconn);
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

                            int cid = promptUserForInt(selectConvoForUpdatePrompt, keyboard);

                            String msg = promptUserForStr("Enter message: ", keyboard);
                            String sender = promptUserForStr("Enter sender (user/ai): ", keyboard);
                            String mid = "SELECT NVL (MAX(mid), 0) + 1 FROM mngo1.Message WHERE cid = " + cid;
                            int newId = getNextId(mid, dbconn);

                            statement = String.format(
                                "INSERT INTO mngo1.Message (mid, cid, message, sender, rating, timestamp) " +
                                "VALUES (%d, %d, '%s', '%s', 0, SYSDATE)", 
                                newId, cid, msg, sender);
                            executeStmt(statement, dbconn);
                            System.out.println("Message added to conversation successfully.");
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
                        int cid = promptUserForInt(selectConvoForUpdatePrompt, keyboard);
                        
                        // Get all messages to find which message to update feedback for
                        query = "SELECT * FROM mngo1.Message WHERE cid = " + cid;
                        count = executeQuery(query, dbconn, Entity.MESSAGE);
                        if (count == 0) {
                            System.err.println("There are no messages to select.");
                        }
                        else {
                            int mid = promptUserForInt(selectMessageForFeedbackPrompt, keyboard);
                            int rating = promptUserForInt("Enter Rating (Thumbs Up = 1/Down = -1): ", keyboard);
                            String ratingText = promptUserForStr("Feedback Text: ", keyboard);
                            statement = String.format(
                                "UPDATE mngo1.Message SET rating = %d, ratingText = '%s' " +
                                "WHERE mid = %d AND cid = %d", rating, ratingText, mid, cid);
                            executeStmt(statement, dbconn);
                            System.out.println("Feedback updated.");
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
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
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
                    statement = String.format("INSERT INTO mngo1.Workspace VALUES (%d, '%s')", newWid, privacy);
                    executeStmt(statement, dbconn);
                    System.out.println("Workspace created successfully.");
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
                        int wid = promptUserForInt(selectWorkspacePrompt, keyboard);
                        System.out.println(modifyWorkspaceInterface);
                        int choice = keyboard.nextInt();
                        keyboard.nextLine();

                        if (choice == 1) {
                            String newPrivacy = promptUserForStr("Enter new privacy (public/private): ", keyboard);
                            statement = String.format(
                                "UPDATE mngo1.Workspace SET privacy = '%s' WHERE wid = %d", 
                                newPrivacy, wid);
                        } 
                        else if (choice == 2) {
                            int newOwner = promptUserForInt("Enter new owner userId: ", keyboard);
                            statement = String.format(
                                "UPDATE mngo1.Workspace SET ownerId = %d WHERE wid = %d", 
                                newOwner, wid);
                        } 
                        else {
                            return;
                        }
                        
                        executeStmt(statement, dbconn);
                        System.out.println("Workspace modified successfully.");
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
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
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
                        return;    
                    }

                case 2: // delete persona
                    {
                        // Get all personas to find which persona to delete
                        query = "SELECT * FROM mngo1.Persona";
                        count = executeQuery(query, dbconn, Entity.PERSONA);
                        if (count == 0) {
                            System.err.println("There are no personas to select.");
                        }
                        else {
                            // Prompt user for which persona to delete
                            int pid = promptUserForInt(selectPersonaToDeletePrompt, keyboard);
                            statement = "DELETE FROM mngo1.Persona WHERE pid = " + pid;
                            executeStmt(statement, dbconn);
                            System.out.println("Persona deleted successfully.");
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
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
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
                    // Format and execute SQL statement to create new prompt template with provided inputs
                    statement = String.format("INSERT INTO mngo1.UserPrompt VALUES ('%s', '%s', %d)", instructions, privacy, userId);
                    executeStmt(statement, dbconn);
                    return;
                
                case 2: // Update prompt template
                    // Get all prompt templates to find which prompt template to update
                    query = "SELECT * FROM mngo1.UserPrompt";
                    count = executeQuery(query, dbconn, Entity.USER_PROMPT);
                    if (count == 0) {
                        System.err.println("There are no prompt templates to select.");
                    }
                    else {
                        // Prompt user for which prompt template to update
                        int upid = promptUserForInt(selectPromptPrompt, keyboard);
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
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
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
                            int userId = promptUserForInt(selectUserForUpgradePrompt, keyboard);
                            int newTier = promptUserForInt("Enter new tier (1-3): ", keyboard);
                            
                            String statement = String.format(
                                "UPDATE mngo1.Membership SET tier = %d, hasPro = 1 " +
                                "WHERE mtid = (SELECT mtid FROM mngo1.Person p " +
                                "JOIN mngo1.Membership m ON p.userId = ? WHERE p.userId = %d)", // Simplified
                                newTier, userId);  // Note: You may need to adjust based on actual FKs
                            
                            executeStmt(statement, dbconn);
                            System.out.println("Subscription upgraded.");
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
                            int userId = promptUserForInt(selectUserForLimitCheck, keyboard);
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
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {

                    // We must have a secondary prompt to get the correct operation
                    // Their are 2 operations: genearte invoice, mark invoice as paid.
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage billing

                case 1: // Generate invoice
                    int userIdForInvoice = promptUserForInt("Enter userId to generate invoice for: ", keyboard);
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
                    return;
                
                case 2: // Mark invoice as paid
                    // Get all invoices to find which invoice to mark as paid
                    query = "SELECT * FROM mngo1.Invoice";
                    count = executeQuery(query, dbconn, Entity.INVOICE);
                    if (count == 0) {
                        System.err.println("No invoices found.");
                        return;
                    }
                    
                    int invId = promptUserForInt(selectInvoiceToPayPrompt, keyboard);
                    statement = "UPDATE mngo1.Invoice SET status = 'paid' WHERE invid = " + invId;
                    executeStmt(statement, dbconn);
                    System.out.println("Invoice marked as paid.");
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
        int count;
        
        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {

                    // We must have a secondary prompt to get the correct operation
                    // Their are 3 operations: Create ticket, assign ticket to agent, mark ticket as resolved.
                    // Each operation calls the secondary helper method to perform that operation in coordination
                    // with a SQL statement that is executed to manage tickets

                case 1: // New ticket
                    {
                        // Prompt user for topic of support ticket, then format and execute SQL statement to create a new support ticket with that topic
                        String topic = promptUserForStr(addTicketTopicPrompt, keyboard);
                        int userId = promptUserForInt("Enter userId creating the ticket: ", keyboard);
                        
                        String tidQuery = "SELECT NVL(MAX(tid), 0) + 1 FROM mngo1.Ticket";
                        int newTid = getNextId(tidQuery, dbconn);
                        
                        statement = String.format(
                            "INSERT INTO mngo1.Ticket (tid, duration, outcome, topic, userId, aid) " +
                            "VALUES (%d, 0, 'Waiting', '%s', %d, NULL)", 
                            newTid, topic, userId);
                        
                        executeStmt(statement, dbconn);
                        System.out.println("Support ticket created.");
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
                        int tid = promptUserForInt(selectTicketForAgentPrompt, keyboard);

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
                            
                            int aid = promptUserForInt(selectAgentPrompt, keyboard);
                            statement = "UPDATE mngo1.Ticket SET aid = " + aid + " WHERE tid = " + tid;
                            executeStmt(statement, dbconn);
                            System.out.println("Ticket assigned to agent.");
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
                            int tid = promptUserForInt(selectTicketForResolvePrompt, keyboard);
                            statement = "UPDATE mngo1.Ticket SET outcome = 'Resolved', duration = 30 WHERE tid = " + tid;
                            executeStmt(statement, dbconn);
                            System.out.println("Ticket marked as resolved.");
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
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
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
                        int userId = promptUserForInt(selectUserForUpgradePrompt, keyboard);
                        // Now execute query 1 on the selected user
                        query = "SELECT b.userId, u.username, c.title, m.message, m.timestamp " +
                                "FROM mngo1.Bookmark b " +
                                "JOIN mngo1.Message m ON b.mid = m.mid " +
                                "JOIN mngo1.Conversation c ON m.cid = c.cid " +
                                "JOIN mngo1.User u ON b.userId = u.userId " +
                                "WHERE b.userId = " + userId;
                        System.out.println(query);
                        //count = executeQuery(query, dbconn, Entity.SPECIAL_QUERY_1);
                    }
                    return;
                
                case 2: // Query 2
                    query = "SELECT u.userId, u.email, " +
                            "    SUM(i.amount) AS total_amount_owed," +
                            "    MAX(msg.last_conversation_date) AS last_conversation_date" +
                            "FROM mngo1.User u" +
                            "JOIN mngo1.BillingRecord br ON u.userId = br.userId" +
                            "JOIN mngo1.Invoice i ON br.brid = i.brid" +
                            "LEFT JOIN (" +
                            "    SELECT c.userId, MAX(m.timestamp) AS last_conversation_date" +
                            "    FROM mngo1.Message m" +
                            "    JOIN mngo1.Conversation c ON m.cid = c.cid" +
                            "    GROUP BY c.userId" +
                            ") msg ON u.userId = msg.userId" +
                            "WHERE i.status = 'Unpaid'" +
                            "GROUP BY u.userId, u.email" +
                            "HAVING SUM(i.amount) > 0";
                    //count = executeQuery(query, dbconn, Entity.SPECIAL_QUERY_2);
                    return;

                case 3: // Query 3
                    query = "WITH PersonaStats AS (" +
                            "    SELECT" +
                            "        p.pid, p.name AS persona_name, COUNT(*) AS total_ai_messages, " +
                            "        SUM(CASE WHEN m.rating = 1 THEN 1 ELSE 0 END) AS positive_ratings, " +
                            "        SUM(CASE WHEN m.rating = -1 THEN 1 ELSE 0 END) AS negative_ratings" +
                            "    FROM mngo1.Persona p" +
                            "    JOIN mngo1.Conversation c ON p.pid = c.pid" +
                            "    JOIN mngo1.Message m      ON c.cid = m.cid" + 
                            "    WHERE m.sender = 'AI'" +
                            "      AND m.rating IS NOT NULL " +
                            "      AND m.rating IN (1, -1)" +
                            "    GROUP BY p.pid, p.name" +
                            ")" +
                            "SELECT" +
                            "    persona_name, thumbs_up_count, total_feedback," +
                            "    ROUND(100.0 * thumbs_up_count / NULLIF(total_feedback, 0), 2) AS thumbs_up_percentage" +
                            "FROM PersonaStats" +
                            "ORDER BY thumbs_up_percentage DESC, total_feedback DESC" +
                            "FETCH FIRST 1 ROW ONLY";
                    //count = executeQuery(query, dbconn, Entity.SPECIAL_QUERY_3);
                    return;

                case 4: // Query 4
                    query = "SELECT" +
                            "    u.username, mbr.tier AS membership_tier, COUNT(*) AS total_messages_sent," +
                            "    ROUND(AVG(LENGTH(m.message)), 2) AS avg_message_length_chars," +
                            "    MAX(m.timestamp) AS last_message_date, COUNT(DISTINCT c.cid) AS num_conversations" +
                            "FROM mngo1.User u" +
                            "JOIN mngo1.Membership mbr ON u.mid = mbr.mid" + 
                            "JOIN mngo1.Conversation c ON u.userId = c.userId" + 
                            "JOIN mngo1.Message m ON c.cid = m.cid" + 
                            "WHERE m.sender = 'User'" + 
                            "GROUP BY u.username, mbr.tier" + 
                            "ORDER BY total_messages_sent DESC, avg_message_length_chars DESC" + 
                            "FETCH FIRST 5 ROWS ONLY";
                    //count = executeQuery(query, dbconn, Entity.SPECIAL_QUERY_4);
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
            System.out.println("Executing: " + statement + "\n\n");
            stmt.execute(statement);
            System.out.println("Executed statement: " + statement);
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
                        System.out.printf("%d: (name: '%s', pass '%s', email: '%s')",
                            rs.getInt("userId"), rs.getString("username"), rs.getString("pwd"), rs.getString("email"));
                        break;

                    case MESSAGE:
                        System.out.printf("%d: (content: '%s', feedback: '%s')",
                            rs.getInt("messageId"), rs.getString("content"), rs.getString("feedback"));
                        break;

                    case CONVERSATION:
                        System.out.printf("%d: (title: '%s')",
                            rs.getInt("conversationId"), rs.getString("title"));
                        break;

                    case WORKSPACE:
                        break;

                    case PERSONA:
                        break;

                    case USER_PROMPT:
                        break;

                    case INVOICE:
                        break;

                    case SUPPORT_TICKET:
                        break;

                    case AGENT:
                        break;

                    case SPECIAL_QUERY_1:   // Special format for query 1
                        break;

                    case SPECIAL_QUERY_2:   // Special format for query 2
                        break;

                    case SPECIAL_QUERY_3:   // Special format for query 3
                        break;

                    case SPECIAL_QUERY_4:   // Special format for query 4
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
        boolean syntaxError = false; // To validate user input
        System.out.print(prompt);    // Print the provided prompt to the user
        String input = keyboard.next();
        // If the input is not in valid length, mark as syntax error to prompt user again
        if (input.length() > 255 || input.length() < 1) 
            syntaxError = true;
        // Repeatedly prompt user until they provide a valid input string between 1 and 255 characters in length
        while (syntaxError) {
            System.out.println();
            System.err.print("Error: String must be between length of 1 & 255. Please try again: ");
            input = keyboard.next();
            if (input.length() <= 255 && input.length() >= 1)
                syntaxError = false;
        }
        // Once we have valid input, we return that input string
        return input;
    }

        /*---------------------------------------------------------------------
        |  Method promptUserForInt(prompt, keyboard)
        |
        |  Purpose: 
        |
        |  Pre-condition: User needs to input a integer in response to a prompt
        |  Post-condition: An integer is returned based on the provided prompt and user input
        |
        |  Parameters:
        |      String prompt - the prompt to display to the user when asking for input (from constants defined at the top of the file)
        |      Scanner keyboard - the scanner object used to read user input
        |
        |  Returns: 
        *-------------------------------------------------------------------*/
    private static int promptUserForInt(String prompt, Scanner keyboard) {
        boolean syntaxError = false; // To validate user input
        System.out.print(prompt);    // Print the provided prompt to the user
        int input = -1;              // Init input to check for type mismatch on the input
        try {
            input = keyboard.nextInt();
            keyboard.nextLine();
        } catch (InputMismatchException e) {
            // If the user input is a mismatch (not an integer), we mark it as a syntax error to prompt the user again
            syntaxError = true;
        }
        // Repeatedly prompt user until they provide a valid integer input 
        while (syntaxError) {
            System.err.print("Error: Please input an integer: ");
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
                syntaxError = false;
            } catch (InputMismatchException e) {}
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