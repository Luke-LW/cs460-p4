import java.sql.*;
import java.util.Scanner;

public class Interface {
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

    private final static String manageConvoInterface =
        "\f----------------------------------\n" +
        "Manage Conversations & Messages\n" +
        "1: Start a new conversation\n" +
        "2: Add messages to a conversation\n" +
        "3: Update message feedback\n" +
        "4: Back\n" +
        "----------------------------------\n";

    private final static String workspaceInterface =
        "\f------------------------\n" +
        "Manage Workspaces\n" +
        "1: Create workspace\n" +
        "2: Modify workspace\n" +
        "3: Back\n" +
        "------------------------\n";

    private final static String personaInterface =
        "\f------------------------\n" +
        "Manage Personas\n" +
        "1: Create persona\n" +
        "2: Delete persona\n" +
        "3: Back\n" + 
        "------------------------\n";

    private final static String promptInterface =
        "\f------------------------\n" +
        "Prompt Library\n" +
        "1: Add prompt template\n" +
        "2: Delete prompt template\n" +
        "3: Back\n" +
        "------------------------\n";

    private final static String subscriptionInterface =
        "\f------------------------\n" +
        "Subcription Tracking\n" +
        "1: Upgrade user's subscription tier\n" +
        "2: Check user's limit\n" +
        "3: Back\n" +
        "------------------------\n";

    private final static String billingInterface =
        "\f------------------------\n" +
        "Billing Operations\n" +
        "1: Generate monthly invoice\n" +
        "2: Mark invoice as paid\n" +
        "3: Back\n" +
        "------------------------\n"; 

    private final static String supportInterface =
        "\f------------------------\n" +
        "Create Support Tickets\n" +
        "1: Create ticket\n" +
        "2: Assign ticket\n" +
        "3: Mark ticket as resolved\n" +
        "4: Back\n" +
        "------------------------\n";

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

            switch (input) {
                case 1:
                    manageUserAcct(keyboard);
                    init = true;
                    break;
                
                case 2:
                    manageMsg(keyboard);
                    init = true;
                    break;

                case 3:
                    manageWorkspace(keyboard);
                    init = true;
                    break;

                case 4:
                    managePersona(keyboard);
                    init = true;
                    break;

                case 5:
                    managePromptLibrary(keyboard);
                    init = true;
                    break;

                case 6:
                    manageSubs(keyboard);
                    init = true;
                    break;

                case 7:
                    manageBilling(keyboard);
                    init = true;
                    break;

                case 8:
                    manageTickets(keyboard);
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
    private static void manageUserAcct(Scanner keyboard) {
        System.out.println(userAcctInterface);

        while (true) {
            int input = keyboard.nextInt();
            switch (input) {
                case 1: // Add user account
                    break;
                
                case 2: // Update user account
                    break;

                case 3: // Delete user account
                    break;

                case 4: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle conversation and message management
    private static void manageMsg(Scanner keyboard) {
        System.out.println(manageConvoInterface);

        while (true) {
            int input = keyboard.nextInt();
            switch (input) {
                case 1: // New conversation
                    break;
                
                case 2: // Add messages to conversation
                    break;

                case 3: // Update message feedback
                    break;

                case 4: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle workspace management
    private static void manageWorkspace(Scanner keyboard) {
        System.out.println(workspaceInterface);

        while (true) {
            int input = keyboard.nextInt();
            switch (input) {
                case 1: // New workspace
                    break;
                
                case 2: // Modify workspace
                    break;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle persona management
    private static void managePersona(Scanner keyboard) {
        System.out.println(personaInterface);

        while (true) {
            int input = keyboard.nextInt();
            switch (input) {
                case 1: // New persona
                    break;
                
                case 2: // delete persona
                    break;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle prompt library management
    private static void managePromptLibrary(Scanner keyboard) {
        System.out.println(promptInterface);

        while (true) {
            int input = keyboard.nextInt();
            switch (input) {
                case 1: // New prompt template
                    break;
                
                case 2: // Delete prompt template
                    break;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle supscription management
    private static void manageSubs(Scanner keyboard) {
        System.out.println(subscriptionInterface);

        while (true) {
            int input = keyboard.nextInt();
            switch (input) {
                case 1: // Upgrade user tier
                    break;
                
                case 2: // Check user limit
                    break;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle billing management
    private static void manageBilling(Scanner keyboard) {
        System.out.println(billingInterface);

        while (true) {
            int input = keyboard.nextInt();
            switch (input) {
                case 1: // Generate invoice
                    break;
                
                case 2: // Mark invoice as paid
                    break;

                case 3: // back
                    return;
            
                default:
                    System.out.println(commandError);
            }
        }
    }

    // Handle support ticket management
    private static void manageTickets(Scanner keyboard) {
        System.out.println(supportInterface);
        
        while (true) {
            int input = keyboard.nextInt();
            switch (input) {
                case 1: // New ticket
                    break;
                
                case 2: // Assign ticket to agent
                    break;

                case 3: // Complete ticket
                    break;
            
                case 4: // back
                    return;

                default:
                    System.out.println(commandError);
            }
        }
    }

}