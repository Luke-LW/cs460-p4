public class Setup {

    // export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}

    // Accounts
    public static final String PersonTable = 
        "CREATE TABLE mngo1.Person (" +
        "userId NUMBER PRIMARY KEY, " +
        "username VARCHAR2(255) NOT NULL, " +
        "pwd VARCHAR2(255) NOT NULL, " +
        "email VARCHAR2(255) NOT NULL)";
    public static final String PersonTrigger = "SELECT 1 FROM dual";
    public static final String[] PersonData = {
        "INSERT INTO Person VALUES (1, 'alice', 'password1', 'nothing')",
        "INSERT INTO Person VALUES (23, 'bob', 'password2', 'nothing')",
        "INSERT INTO Person VALUES (344, 'charlie', 'password3', 'nothing')"
    };
    public static final String PersonDrop = 
        "DROP TABLE Person";

    // Language
    public static final String LanguageTable = "" + 
        "CREATE TABLE mngo1.Language (" + 
        "lid NUMBER PRIMARY KEY, " +
        "language VARCHAR2(255) )";
    public static final String LanguageTrigger = "SELECT 1 FROM dual";
    public static final String[] LanguageData = {
        "INSERT INTO mngo1.Language VALUES (1, 'English')",
        "INSERT INTO mngo1.Language VALUES (2, 'Spanish')",
        "INSERT INTO mngo1.Language VALUES (3, 'Fremch')"
    };
    public static final String LanguageDrop = 
        "DROP TABLE mngo1.Language";

    // Billing Record
    public static final String BillingRecordTable =
        "CREATE TABLE mngo1.BillingRecord (" + 
        "bid NUMBER PRIMARY KEY, " +
        "payaddress VARCHAR2(255), " +
        "paymethod VARCHAR2(255) )";
    public static final String BillingRecordTrigger = "SELECT 1 FROM dual";
    public static final String[] BillingRecordData = {
        "INSERT INTO mngo1.BillingRecord VALUES (1, 'Fake Place', 'Cash')",
        "INSERT INTO mngo1.BillingRecord VALUES (2, 'Park Place', 'Mugging')",
        "INSERT INTO mngo1.BillingRecord VALUES (3, 'CSC Place', 'Academics')"
    };
    public static final String BillingRecordDrop = 
        "DROP TABLE mngo1.BillingRecord";

    // Invoice
    public static final String InvoiceTable =
        "CREATE TABLE mngo1.Invoice (" + 
        "invid NUMBER PRIMARY KEY, " +
        "status VARCHAR2(255), " +
        "amount NUMBER, " +
        "month DATE )";
    public static final String InvoiceTrigger = "SELECT 1 FROM dual";
    public static final String[] InvoiceData = {
        "INSERT INTO mngo1.Invoice VALUES (1, 'Paid', 131.23, TO_DATE('07-13-23', 'MM-DD-RR'))",
        "INSERT INTO mngo1.Invoice VALUES (2, 'Unpaid', 13.33, TO_DATE('08-13-23', 'MM-DD-RR'))",
        "INSERT INTO mngo1.Invoice VALUES (3, 'Void', 99.99, TO_DATE('09-13-23', 'MM-DD-RR'))"
    };
    public static final String InvoiceDrop = 
        "DROP TABLE mngo1.Invoice";
    
    // Membership
    public static final String MembershipTable =
        "CREATE TABLE mngo1.Membership (" + 
        "mtid NUMBER PRIMARY KEY, " +
        "hasPro NUMBER, " +
        "tier NUMBER, " +
        "limit NUMBER )";
    public static final String MembershipTrigger = "SELECT 1 FROM dual";
    public static final String[] MembershipData = {
        "INSERT INTO mngo1.Membership VALUES (1, 1, 3, 2000)",
        "INSERT INTO mngo1.Membership VALUES (2, 0, 2, 200)",
        "INSERT INTO mngo1.Membership VALUES (3, 0, 1, 20)"
    };
    public static final String MembershipDrop = 
        "DROP TABLE mngo1.Membership";

    // Ticket
    public static final String TicketTable =
        "CREATE TABLE mngo1.Ticket (" + 
        "tid NUMBER PRIMARY KEY, " +
        "duration NUMBER, " +
        "outcome VARCHAR2(255), " +
        "topic VARCHAR2(255) )";
    public static final String TicketTrigger = "SELECT 1 FROM dual";
    public static final String[] TicketData = {
        "INSERT INTO mngo1.Ticket VALUES (1, 230, 'Resolved', 'idk')",
        "INSERT INTO mngo1.Ticket VALUES (2, 89, 'Escalated', 'friends')",
        "INSERT INTO mngo1.Ticket VALUES (3, 6010, 'Waiting', 'Bananas')"
    };
    public static final String TicketDrop = 
        "DROP TABLE mngo1.Ticket";
    
    // Agent
    public static final String AgentTable =
        "CREATE TABLE mngo1.Agent (" + 
        "aid NUMBER PRIMARY KEY, " +
        "name VARCHAR2(255))";
    public static final String AgentTrigger = "SELECT 1 FROM dual";
    public static final String[] AgentData = {
        "INSERT INTO mngo1.Agent VALUES (1, 'Minh')",
        "INSERT INTO mngo1.Agent VALUES (2, 'Derek')",
        "INSERT INTO mngo1.Agent VALUES (3, 'Luke')"
    };
    public static final String AgentDrop = 
        "DROP TABLE mngo1.Agent";

    // Workspace
    public static final String WorkspaceTrigger = "SELECT 1 FROM dual";
    public static final String[] WorkspaceData = {
        "INSERT INTO mngo1.Workspace VALUES ('1', 'private')",
        "INSERT INTO mngo1.Workspace VALUES ('2', 'public')"
    };
    public static final String WorkspaceDrop =
        "DROP TABLE mngo1.Workspace";

    // Template Prompt
    public static final String TemplatePromptTrigger = "SELECT 1 FROM dual";
    public static final String[] TemplatePromptData = {
        "INSERT INTO mngo1.TemplatePrompt VALUES ('1', 'List popular restaurants in Tucson')",
        "INSERT INTO mngo1.TemplatePrompt VALUES ('2', 'Summarize this document')"
    };
    public static final String TemplatePromptDrop =
        "DROP TABLE mngo1.TemplatePrompt";

    // User Prompt
    public static final String UserPromptTrigger = "SELECT 1 FROM dual";
    public static final String[] UserPromptData = {
        "INSERT INTO mngo1.UserPrompt VALUES ('1', 'Hello this is my first prompt')"
    };
    public static final String UserPromptDrop = 
        "DROP TABLE mngo1.UserPrompt";

    // Prompt category
    public static final String PromptCategoryTrigger = "SELECT 1 FROM dual";
    public static final String[] PromptCategoryData = {
        "INSERT INTO mngo1.PromptCategory VALUES ('1', 'Useful prompts')",
        "INSERT INTO mngo1.PromptCategory VALUES ('2', 'Useless prompts')"
    };
    public static final String promptCategoryDrop = 
        "DROP TABLE mngo1.PromptCategory";

    // Conversation
    public static final String ConversationTable =
        "CREATE TABLE mngo1.Conversation (" + 
        "cid NUMBER PRIMARY KEY, " +
        "title VARCHAR2(255))";
    public static final String ConversationTrigger = "SELECT 1 FROM dual";
    public static final String[] ConversationData = {
        "INSERT INTO mngo1.Conversation VALUES (1, 'true love')",
        "INSERT INTO mngo1.Conversation VALUES (2, 'books')",
        "INSERT INTO mngo1.Conversation VALUES (3, 'polygons')"
    };
    public static final String ConversationDrop = 
        "DROP TABLE mngo1.Conversation";

    // Persona
    public static final String PersonaTable =
        "CREATE TABLE mngo1.Persona (" + 
        "pid NUMBER PRIMARY KEY, " +
        "name VARCHAR2(255), " +
        "personality VARCHAR2(255))";
    public static final String PersonaTrigger = "SELECT 1 FROM dual";
    public static final String[] PersonaData = {
        "INSERT INTO mngo1.Persona VALUES (1, 'Siri', 'annoying')",
        "INSERT INTO mngo1.Persona VALUES (2, 'ChatGPT', 'confusing')",
        "INSERT INTO mngo1.Persona VALUES (3, 'Claude', 'dont ask')"
    };
    public static final String PersonaDrop = 
        "DROP TABLE mngo1.Persona";

    // Message
    public static final String MessageTable =
        "CREATE TABLE mngo1.Message (" + 
        "mid NUMBER NOT NULL, " +
        "cid NUMBER NOT NULL, " +
        "message VARCHAR2(10000) NOT NULL, " +
        "sender VARCHAR2(255) NOT NULL, " +
        "ratingText VARCHAR2(255), " +
        "rating NUMBER NOT NULL, " +
        "timestamp DATE, " + 
        "title VARCHAR(255), " +
        "CONSTRAINT m_pk PRIMARY KEY (mid, cid), " +
        "CONSTRAINT fk_c FOREIGN KEY (cid)\nREFERENCES mngo1.Conversation(cid) ON DELETE SET NULL)";
    public static final String MessageTrigger = "SELECT 1 FROM dual";
    public static final String[] MessageData = {
        "INSERT INTO mngo1.Message VALUES (1, 1, 'hello world', 'user', NULL, 0, NULL, 'hello')"
    };
    public static final String MessageDrop = 
        "DROP TABLE mngo1.Message";

    // Bookmark
    public static final String BookmarkTable =
        "CREATE TABLE mngo1.Bookmark (" + 
        "bid NUMBER NOT NULL, " +
        "mid NUMBER NOT NULL, " + 
        "cid NUMBER NOT NUll, " + 
        "CONSTRAINT b_m PRIMARY KEY (mid, bid, cid), " +
        "CONSTRAINT fk_m FOREIGN KEY (mid, cid)\nREFERENCES mngo1.Message(mid, cid) ON DELETE SET NULL) ";
    public static final String BookmarkTrigger = "SELECT 1 FROM dual";
    public static final String[] BookmarkData = {
        "INSERT INTO mngo1.Bookmark VALUES (1, 1)",
        "INSERT INTO mngo1.Bookmark VALUES (2, 2)",
        "INSERT INTO mngo1.Bookmark VALUES (3, 3)"
    };
    public static final String BookmarkDrop = 
        "DROP TABLE mngo1.Conversation";
    
    // Workspace
    // Template Prompt
    // User Prompt
    // Prompt Category
}
