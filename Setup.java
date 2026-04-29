public class Setup {

    // User table  (uid, username, password, email)
    public static final String User = 
        "CREATE TABLE User (\n" +
        "\tuid NUMBER PRIMARY KEY,\n" +
        "\tusername VARCHAR2(255) NOT NULL,\n" +
        "\tpassword VARCHAR2(255) NOT NULL,\n" +
        "\temail VARCHAR2(255) NOT NULL\n" +
        ")";
    // no trigger
    public static final String UserTrigger = "";
    public static final String UserData = 
        "INSERT INTO User (username, password, email) VALUES ('alice', 'password1');\n" +
        "INSERT INTO User (username, password, email) VALUES ('bob', 'password2');\n" +
        "INSERT INTO User (username, password, email) VALUES ('charlie', 'password3');\n";
    public static final String UserDrop = 
        "DROP TABLE User";

    // Subscription table   (uid, sid, tier, expiration_date)
    public static final String Subscription = 
        "CREATE TABLE Subscription (\n" +
        "\tuid NUMBER PRIMARY KEY,\n" +
        "\tsid NUMBER PRIMARY KEY,\n" +
        "\ttier NUMBER NOT NULL,\n" +
        "\texpiration_date DATE NOT NULL,\n" +
        "\tFOREIGN KEY (uid) REFERENCES User(uid)\n" +
        ")";
    public static final String SubscriptionTrigger =
        "CREATE OR REPLACE TRIGGER SubscriptionTrigger\n" +
        "BEFORE INSERT ON Subscription\n" +
        "FOR EACH ROW\n" +
        "BEGIN\n" +
        "\tSELECT Subscription_seq.NEXTVAL INTO :new.sid FROM dual;\n" +
        "END;";
    public static final String SubscriptionData = 
        "INSERT INTO Subscription (uid, tier, expiration_date) VALUES (1, 1, SYSDATE + 365);\n" +
        "INSERT INTO Subscription (uid, tier, expiration_date) VALUES (2, 2, SYSDATE + 365);\n" +
        "INSERT INTO Subscription (uid, tier, expiration_date) VALUES (3, 3, SYSDATE + 365);\n";
    public static final String SubscriptionDrop = 
        "DROP TABLE Subscription";
    
    // Conversation table   (cid, uid)
    public static final String Conversation =
        "CREATE TABLE Conversation (\n" +
        "\tcid NUMBER PRIMARY KEY,\n" +
        "\tuid NUMBER PRIMARY KEY,\n" +
        "\tFOREIGN KEY (uid) REFERENCES User(uid)\n" +
        ")";
    public static final String ConversationTrigger =
        "CREATE OR REPLACE TRIGGER ConversationTrigger\n" +
        "BEFORE INSERT ON Conversation\n" +
        "FOR EACH ROW\n" +
        "BEGIN\n" +
        "\tSELECT Conversation_seq.NEXTVAL INTO :new.cid FROM dual;\n" +
        "END;";
    public static final String ConversationData = 
        "INSERT INTO Conversation (uid) VALUES (1);\n" +
        "INSERT INTO Conversation (uid) VALUES (2);\n" +
        "INSERT INTO Conversation (uid) VALUES (3);\n";
    public static final String ConversationDrop = 
        "DROP TABLE Conversation";
    
    // Message table     (mid, cid, uid, message, feedback, timestamp)
    public static final String Message =
        "CREATE TABLE Message (\n" +
        "\tmid NUMBER PRIMARY KEY,\n" +
        "\tcid NUMBER PRIMARY KEY,\n" +
        "\tuid NUMBER PRIMARY KEY,\n" +
        "\tmessage VARCHAR2(255) NOT NULL,\n" +
        "\tfeedback VARCHAR2(255) NOT NULL,\n" +
        "\ttimestamp TIMESTAMP NOT NULL,\n" +
        "\tFOREIGN KEY (cid) REFERENCES Conversation(cid),\n" +
        "\tFOREIGN KEY (uid) REFERENCES User(uid)\n" +
        ")";
    public static final String MessageTrigger =
        "CREATE OR REPLACE TRIGGER MessageTrigger\n" +
        "BEFORE INSERT ON Message\n" +
        "FOR EACH ROW\n" +
        "BEGIN\n" +
        "\tSELECT Message_seq.NEXTVAL INTO :new.mid FROM dual;\n" +
        "END;";
    public static final String MessageData = 
        "INSERT INTO Message (cid, uid, message, feedback, timestamp) VALUES (1, 1, 'Hello, this is Alice!', 'Good', SYSTIMESTAMP);\n" +
        "INSERT INTO Message (cid, uid, message, feedback, timestamp) VALUES (1, 2, 'Hi Alice, this is Bob!', 'Good', SYSTIMESTAMP);\n" +
        "INSERT INTO Message (cid, uid, message, feedback, timestamp) VALUES (1, 3, 'Hey Alice and Bob, this is Charlie!', 'Good', SYSTIMESTAMP);\n";
    public static final String MessageDrop = 
        "DROP TABLE Message";
}
