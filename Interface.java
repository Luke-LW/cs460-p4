import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Interface {
    private enum Entity {USER, MESSAGE, CONVERSATION, WORKSPACE, PERSONA, USER_PROMPT, INVOICE, SUPPORT_TICKET, AGENT};

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
            "9: Exit\n" +
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

    private final static String commandError = "Error: Unrecognized Command. Try again:\n";

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

        // start interface
        boolean init = true;
        Scanner keyboard = new Scanner(System.in);
        while (true) {
            if (init) System.out.println(mainInterface);
            int input = keyboard.nextInt();
            keyboard.nextLine();

            switch (input) {
                case 1:
                    manageUserAcct(keyboard, dbconn);
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
                    System.exit(0);
                    break;

                default:
                    System.out.println(commandError);
                    init = false;
                    break;
            }
        }
    }

    // Handle user account management
    private static void manageUserAcct(Scanner keyboard, Connection dbconn) {
        System.out.println(userAcctInterface);
        int count;
        String query, statement;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {
                case 1: // Add user account
                    String email = promptUserForStr(addUserEmailPrompt, keyboard);
                    String username = promptUserForStr(addUserNamePrompt, keyboard);
                    String password = promptUserForStr(addUserPasswordPrompt, keyboard);
                    statement = String.format("INSERT INTO mngo.Person VALUES (%d, %s, %s, %s)", 1, username, password, email);
                    executeStmt(statement, dbconn);
                    return;
                
                case 2: // Update user account
                    query = "SELECT * FROM mngo1.Person";
                    System.out.println(query);
                    count = executeQuery(query, dbconn, Entity.USER);
                    if (count == 0)
                        System.err.println("There are no users to select.");
                    else {
                        int userId = promptUserForInt(selectUserForUpdatePrompt, keyboard);
                    }
                    return;

                case 3: // Delete user account
                    query = "SELECT * FROM mngo1.Person";
                    System.out.println(query);
                    count = executeQuery(query, dbconn, Entity.USER);
                    if (count == 0)
                        System.err.println("There are no users to select.");
                    else {
                        int userId = promptUserForInt(selectUserForDeletePrompt, keyboard);
                    }
                    return;

                case 4: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle conversation and message management
    private static void manageMsg(Scanner keyboard, Connection dbconn) {
        System.out.println(manageConvoInterface);
        String statement, query;
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {
                case 1: // New conversation
                    String title = promptUserForStr(addConvoTitlePrompt, keyboard);
                    statement = String.format("INSERT INTO mngo1.Conversation VALUES (%d, %s)", 1, title);
                    executeStmt(statement, dbconn);
                    return;
                
                case 2: // Add messages to conversation
                    query = "SELECT * FROM mngo1.Conversation";
                    count = executeQuery(query, dbconn, Entity.CONVERSATION);
                    if (count == 0) {
                        System.err.println("There are no conversations to select.");
                        return;
                    }
                    else {
                        int cid = promptUserForInt(selectConvoForUpdatePrompt, keyboard);
                    }

                    query = "SELECT * FROM mngo1.Message";
                    count = executeQuery(query, dbconn, Entity.MESSAGE);
                    if (count == 0) {
                        System.err.println("There are no messages to select.");
                    }
                    else {
                        int mid = promptUserForInt(selectMessageForConvoPrompt, keyboard);
                    }
                    return;

                case 3: // Update message feedback
                    query = "SELECT * FROM mngo1.Message";
                    count = executeQuery(query, dbconn, Entity.MESSAGE);
                    if (count == 0) {
                        System.err.println("There are no messages to select.");
                    }
                    else {
                        int mid = promptUserForInt(selectMessageForFeedbackPrompt, keyboard);
                    }
                    return;

                case 4: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle workspace management
    private static void manageWorkspace(Scanner keyboard, Connection dbconn) {
        System.out.println(workspaceInterface);
        String statement, query;
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {
                case 1: // New workspace
                    String privacy = promptUserForStr(addWorkspacePrivacyPrompt, keyboard);
                    statement = String.format("INSERT INTO mngo1.Workspace VALUES (%d, %s)", 1, privacy);
                    executeStmt(statement, dbconn);
                    return;
                
                case 2: // Modify workspace
                    query = "SELECT * FROM mngo1.Workspace";
                    count = executeQuery(query, dbconn, Entity.WORKSPACE);
                    if (count == 0) {
                        System.err.println("There are no workspaces to select.");
                    }
                    else {
                        int wid = promptUserForInt(selectWorkspacePrompt, keyboard);
                    }
                    return;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle persona management
    private static void managePersona(Scanner keyboard, Connection dbconn) {
        System.out.println(personaInterface);
        String statement, query;
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {
                case 1: // New persona
                    String name = promptUserForStr(addPersonaNamePrompt, keyboard);
                    String personality = promptUserForStr(addPersonaPersonalityPrompt, keyboard);
                    statement = String.format("INSERT INTO mngo1.Persona VALUES (%d, %s, %s)", 1, name, personality);
                    executeStmt(statement, dbconn);
                    return;
                
                case 2: // delete persona
                    query = "SELECT * FROM mngo1.Persona";
                    count = executeQuery(query, dbconn, Entity.PERSONA);
                    if (count == 0) {
                        System.err.println("There are no personas to select.");
                    }
                    else {
                        int wid = promptUserForInt(selectPersonaToDeletePrompt, keyboard);
                    }
                    return;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle prompt library management
    private static void managePromptLibrary(Scanner keyboard, Connection dbconn) {
        System.out.println(promptInterface);
        String statement, query;
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {
                case 1: // New prompt template
                    String instructions = promptUserForStr(addPromptInstructionPrompt, keyboard);
                    String privacy = promptUserForStr(addPromptPrivacyPrompt, keyboard);
                    statement = String.format("INSERT INTO mngo1.UserPrompt VALUES (%d, %s, %s)", 1, instructions, privacy);
                    executeStmt(statement, dbconn);
                    return;
                
                case 2: // Update prompt template
                    query = "SELECT * FROM mngo1.UserPrompt";
                    count = executeQuery(query, dbconn, Entity.USER_PROMPT);
                    if (count == 0) {
                        System.err.println("There are no prompt templates to select.");
                    }
                    else {
                        int upid = promptUserForInt(selectPromptPrompt, keyboard);
                    }
                    return;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle supscription management
    private static void manageSubs(Scanner keyboard, Connection dbconn) {
        System.out.println(subscriptionInterface);
        String query;
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {
                case 1: // Upgrade user tier
                    query = "SELECT * FROM mngo1.Person";
                    count = executeQuery(query, dbconn, Entity.USER);
                    if (count == 0) {
                        System.err.println("There are no users to select.");
                    }
                    else {
                        int userId = promptUserForInt(selectUserForUpgradePrompt, keyboard);
                    }
                    return;
                
                case 2: // Check user limit
                    query = "SELECT * FROM mngo1.Person";
                    count = executeQuery(query, dbconn, Entity.USER);
                    if (count == 0) {
                        System.err.println("There are no users to select.");
                    }
                    else {
                        int userId = promptUserForInt(selectUserForLimitCheck, keyboard);
                    }
                    return;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle billing management
    private static void manageBilling(Scanner keyboard, Connection dbconn) {
        System.out.println(billingInterface);
        String statement, query;
        int count;

        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {
                case 1: // Generate invoice
                    System.out.println("Not yet implemented");
                    return;
                
                case 2: // Mark invoice as paid
                    query = "SELECT * FROM mngo1.Invoice";
                    count = executeQuery(query, dbconn, Entity.INVOICE);
                    if (count == 0) {
                        System.err.println("There are no invoices to select.");
                    }
                    else {
                        int invId = promptUserForInt(selectInvoiceToPayPrompt, keyboard);
                    }
                    return;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle support ticket management
    private static void manageTickets(Scanner keyboard, Connection dbconn) {
        System.out.println(supportInterface);
        String statement, query;
        int count;
        
        while (true) {
            int input = keyboard.nextInt();
            keyboard.nextLine();
            switch (input) {
                case 1: // New ticket
                    String topic = promptUserForStr(addTicketTopicPrompt, keyboard);
                    statement = String.format("INSERT INTO mngo1.Ticket VALUES (%d, %d, %s, %s, %d, %d)", 1, 0, "Unresolved", topic, 1, 1);
                    executeStmt(statement, dbconn);
                    return;
                
                case 2: // Assign ticket to agent
                    query = "SELECT * FROM mngo1.Ticket";
                    count = executeQuery(query, dbconn, Entity.SUPPORT_TICKET);
                    if (count == 0) {
                        System.err.println("There are no tickets to select.");
                        return;
                    }
                    else {
                        int tid = promptUserForInt(selectTicketForAgentPrompt, keyboard);
                    }

                    query = "SELECT * FROM mngo1.Agent";
                    count = executeQuery(query, dbconn, Entity.AGENT);
                    if (count == 0) {
                        System.err.println("There are no agents to select.");
                    }
                    else {
                        int aid = promptUserForInt(selectAgentPrompt, keyboard);
                    }
                    return;

                case 3: // Complete ticket
                    query = "SELECT * FROM mngo1.Ticket";
                    count = executeQuery(query, dbconn, Entity.SUPPORT_TICKET);
                    if (count == 0) {
                        System.err.println("There are no tickets to select.");
                    }
                    else {
                        int tid = promptUserForInt(selectTicketForResolvePrompt, keyboard);
                    }
                    return;
            
                case 4: // back
                    return;

                default:
                    System.out.println(commandError);
            }
        }
    }

    // Executes a SQL statement. Returns boolean depending on success.
    private static boolean executeStmt(String statement, Connection dbconn) {
        try {
            Statement stmt = dbconn.createStatement();
            stmt.executeUpdate("COMMIT");
            System.out.println("DEBUG: This is the query you were going to execute:\n" + statement);
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    // Executes a SQL query and returns the amount of tuples returned.
    // Additionally prints out some of the data from the tuples depending on which entity was selected
    private static int executeQuery(String query, Connection dbconn, Entity entity) {
        int count = 0;
        try {
            Statement stmt = dbconn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                count++;
                switch (entity) {
                    case Entity.USER:
                        System.out.printf("%d: (name: %s, pass %s, email: %s)",
                            rs.getInt("userId"), rs.getString("username"), rs.getString("pwd"), rs.getString("email"));
                        break;

                    case Entity.MESSAGE:
                        
                        break;

                    case Entity.CONVERSATION:
                        break;
                }
            }
            return count;
            
        } catch (SQLException e) {
        }
        return count;
    }

    // Prompts the user for a string using the prompt param
    private static String promptUserForStr(String prompt, Scanner keyboard) {
        boolean syntaxError = false;
        System.out.print(prompt);
        String input = keyboard.next();
        if (input.length() > 255 || input.length() < 1) 
            syntaxError = true;

        while (syntaxError) {
            System.out.println();
            System.err.print("Error: String must be between length of 1 & 255. Please try again: ");
            input = keyboard.next();
            if (input.length() <= 255 && input.length() >= 1)
                syntaxError = false;
        }
        return input;
    }

    // Prompts the user for an int
    private static int promptUserForInt(String prompt, Scanner keyboard) {
        boolean syntaxError = false;
        System.out.print(prompt);
        int input = -1;
        try {
            input = keyboard.nextInt();
            keyboard.nextLine();
        } catch (InputMismatchException e) {
            syntaxError = true;
        }

        while (syntaxError) {
            System.err.print("Error: Please input an integer: ");
            try {
                input = keyboard.nextInt();
                keyboard.nextLine();
                syntaxError = false;
            } catch (InputMismatchException e) {}
        }
        return input;
    }

}