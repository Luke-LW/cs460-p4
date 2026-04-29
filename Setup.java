public class Setup {

    // export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}

    // Accounts
    public static final String PersonTable = 
        "CREATE TABLE mngo1.Person (" +
        "userId NUMBER PRIMARY KEY, " +
        "username VARCHAR2(255) NOT NULL, " +
        "pwd VARCHAR2(255) NOT NULL, " +
        "email VARCHAR2(255) NOT NULL)";
    public static final String PersonTrigger = "";
    public static final String[] PersonData = {
        "INSERT INTO Person VALUES ('1', 'alice', 'password1', 'nothing')",
        "INSERT INTO Person VALUES ('23', 'bob', 'password2', 'nothing')",
        "INSERT INTO Person VALUES ('344', 'charlie', 'password3', 'nothing')"
    };
    public static final String PersonDrop = 
        "DROP TABLE Person";

    // Language
    public static final String LanguageTable = "" + 
        "CREATE TABLE mngo1.Language (" + 
        "lid NUMBER PRIMARY KEY, " +
        "language VARCHAR2(255) )";
    public static final String LanguageTrigger = "";
    public static final String[] LanguageData = {
        "INSERT INTO mngo1.Language VALUES ('1', 'English')",
        "INSERT INTO mngo1.Language VALUES ('2', 'Spanish')",
        "INSERT INTO mngo1.Language VALUES ('3', 'Fremch')"
    };
    public static final String LanguageDrop = 
        "DROP TABLE mngo1.Language";

    // Billing Record
    public static final String BillingRecordTable = "" + 
        "CREATE TABLE mngo1.BillingRecord (" + 
        "bid NUMBER PRIMARY KEY, " +
        "payaddress VARCHAR2(255), " +
        "paymethod VARCHAR2(255) )";
    public static final String BillingRecordTrigger = "";
    public static final String[] BillingRecordData = {
        "INSERT INTO mngo1.BillingRecord VALUES ('1', 'Fake Place', 'Cash')",
        "INSERT INTO mngo1.BillingRecord VALUES ('2', 'Park Place', 'Mugging')",
        "INSERT INTO mngo1.BillingRecord VALUES ('3', 'CSC Place', 'Academics')"
    };
    public static final String BillingRecordDrop = 
        "DROP TABLE mngo1.BillingRecord";

    // Invoice
    public static final String InvoiceTable = "" + 
        "CREATE TABLE mngo1.Invoice (" + 
        "invid NUMBER PRIMARY KEY, " +
        "status VARCHAR2(255), " +
        "amount NUMBER, " +
        "month DATE )";
    public static final String InvoiceTrigger = "";
    public static final String[] InvoiceData = {
        "INSERT INTO mngo1.Invoice VALUES ('1', 'Paid', 131.23, TO_DATE('07-13-23', 'MM-DD-RR'))",
        "INSERT INTO mngo1.Invoice VALUES ('2', 'Unpaid', 13.33, TO_DATE('08-13-23', 'MM-DD-RR'))",
        "INSERT INTO mngo1.Invoice VALUES ('3', 'Void', 99.99, TO_DATE('09-13-23', 'MM-DD-RR'))"
    };
    public static final String InvoiceDrop = 
        "DROP TABLE mngo1.Invoice";
    
    // Membership
    public static final String MembershipTable = "" + 
        "CREATE TABLE mngo1.Membership (" + 
        "mtid NUMBER PRIMARY KEY, " +
        "hasPro NUMBER, " +
        "tier NUMBER, " +
        "limit NUMBER )";
    public static final String MembershipTrigger = "";
    public static final String[] MembershipData = {
        "INSERT INTO mngo1.Membership VALUES ('1', 1, 3, 2000)",
        "INSERT INTO mngo1.Membership VALUES ('2', 0, 2, 200)",
        "INSERT INTO mngo1.Membership VALUES ('3', 0, 1, 20)"
    };
    public static final String MembershipDrop = 
        "DROP TABLE mngo1.Membership";

    // Ticket
    public static final String TicketTable = "" + 
        "CREATE TABLE mngo1.Ticket (" + 
        "tid NUMBER PRIMARY KEY, " +
        "duration NUMBER, " +
        "outcome VARCHAR2(255), " +
        "topic VARCHAR2(255) )";
    public static final String TicketTrigger = "";
    public static final String[] TicketData = {
        "INSERT INTO mngo1.Ticket VALUES ('1', 230, 'Resolved', 'idk')",
        "INSERT INTO mngo1.Ticket VALUES ('2', 89, 'Escalated', 'friends')",
        "INSERT INTO mngo1.Ticket VALUES ('3', 6010, 'Waiting', 'Bananas')"
    };
    public static final String TicketDrop = 
        "DROP TABLE mngo1.Ticket";
    
    // Agent
    public static final String AgentTable = "" + 
        "CREATE TABLE mngo1.Agent (" + 
        "aid NUMBER PRIMARY KEY, " +
        "name VARCHAR(255))";
    public static final String AgentTrigger = "";
    public static final String[] AgentData = {
        "INSERT INTO mngo1.Agent VALUES ('1', 'Minh')",
        "INSERT INTO mngo1.Agent VALUES ('2', 'Derek')",
        "INSERT INTO mngo1.Agent VALUES ('3', 'Luke')"
    };
    public static final String AgentDrop = 
        "DROP TABLE mngo1.Agent";
}
