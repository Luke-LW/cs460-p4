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
 *       Purpose:   Sets up all necessary tables in our database to run our queries.
 * 
 *    Description:  <tablename>
 *                  <description>
 * 
 *                  -- Fields --
 *                    @field <field> [(PK) | (CK) | (FK)]
 * 
 *                  -- Relationships -- (1-M, 1-1, M-M relationships)
 *                      @relationship <tablename1> -- <relation> --> <tablename2>
 * 
 *                  -- Constraints --
 *                      @constraint <constraint>
 *                  
 *   Requirements:  Java 25 or earlier
 *        Compile:  javac Setup.java
 *         Usage:   java Setup 
 *    Input Files:  None. 
 */


public class Setup {

    // export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}

    /**
     * Person (Users)
     * This table holds all of our users (different from agents).
     * 
     * -- Fields --
     * @field userId (PK)
     * @field username
     * @field pwd
     * @field email
     * @field lid (FK)
     * @field mtid (FK)
     * 
     * -- Relationships --
     * @relationship Person -- 1 --> Language
     * @relationship Language --> 0+ --> Person
     * -- Constraints --
     * @constraint Emails must be unique
     * @constraint usernames must be unique
     */
    public static final String PersonTable = 
        "CREATE TABLE mngo1.Person (" +
        "userId NUMBER PRIMARY KEY, " +
        "username VARCHAR2(255) NOT NULL, " +
        "pwd VARCHAR2(255) NOT NULL, " +
        "email VARCHAR2(255) NOT NULL, " +
        // Foreign Key
        "lid NUMBER NOT NULL, " +
        "mtid NUMBER NOT NULL, " +
        "CONSTRAINT fk1 FOREIGN KEY (lid) REFERENCES mngo1.Language(lid), " +
        "CONSTRAINT fk135 FOREIGN KEY (mtid) REFERENCES mngo1.Membership(mtid), " +
        "CONSTRAINT uni1 UNIQUE (email), " +
        "CONSTRAINT uni2 UNIQUE (username))";
    public static final String PersonDrop = 
        "DROP TABLE Person CASCADE CONSTRAINTS PURGE";
    public static final String PersonData =
        "INSERT ALL\n" +
        "\tINTO mngo1.Person VALUES (1, 'Minh', 'abc', 'thatboy@arizona.edu', 1, 4)\n" +
        "\tINTO mngo1.Person VALUES (2, 'Luke', 'abc', 'coolkid@arizona.edu', 2, 5)\n" +
        "\tINTO mngo1.Person VALUES (3, 'Derek', 'abc', 'funguy@arizona.edu', 3, 6)\n" +
        "SELECT 1 FROM dual";

    /**
     * Language
     * A table with predetermined values that hold the language UI
     * configurations that users can choose from for their experience.
     * 
     * -- Fields --
     * @field lid (PK)
     * @field langauge
     * 
     * -- Relationships --
     * @relationship Language -- 0+ --> Person
     * @relationship Person -- 1 --> Language
     * 
     * -- Constraints --
     */
    public static final String LanguageTable = "" + 
        "CREATE TABLE mngo1.Language (" + 
        "lid NUMBER PRIMARY KEY, " +
        "language VARCHAR2(255) NOT NULL)";
    public static final String LanguageDrop = 
        "DROP TABLE mngo1.Language CASCADE CONSTRAINTS PURGE";
    public static final String LanguageData =
        "INSERT ALL\n" +
        "\tINTO mngo1.Language VALUES (1, 'French')\n" +
        "\tINTO mngo1.Language VALUES (2, 'English')\n" +
        "\tINTO mngo1.Language VALUES (3, 'Spanish')\n" +
        "SELECT 1 FROM dual";
    /**
     * BillingRecord
     * Holds the Billing Data that is used to pay for Invoices.
     * It is linked to a user upon user creation.
     * 
     * -- Fields --
     * @field brid (PK)
     * @field payaddress
     * @field paymethod
     * 
     * -- Relationships --
     * @relationship BillingRecord -- 1 --> Person
     * @relationship Person -- 1 --> BillingRecord
     * @relationship BillingRecord -- 0+ --> Invoice
     * @relationship Invoice -- 1 --> BillingRecord
     * 
     * -- Constraints --
     * @constraint paymethod must be ('credit', 'debit', 'check', 'cash', 'other')
     */
    public static final String BillingRecordTable =
        "CREATE TABLE mngo1.BillingRecord (" + 
        "brid NUMBER PRIMARY KEY, " +
        "payaddress VARCHAR2(255), " +
        "paymethod VARCHAR2(255), " +
        // foreign key
        "userId NUMBER NOT NULL, " +
        "CONSTRAINT fk2 FOREIGN KEY (userId) REFERENCES mngo1.Person(userId) ON DELETE CASCADE, " +
        "CONSTRAINT chk1 CHECK (paymethod IN ('credit', 'debit', 'check', 'cash', 'other')))";
    public static final String BillingRecordDrop = 
        "DROP TABLE mngo1.BillingRecord CASCADE CONSTRAINTS PURGE";
    public static final String BillingRecordData =
        "INSERT ALL\n" +
        "\tINTO mngo1.BillingRecord VALUES (1, 'overworld', 'cash', 1)\n" +
        "\tINTO mngo1.BillingRecord VALUES (2, 'netherworld', 'credit', 2)\n" +
        "\tINTO mngo1.BillingRecord VALUES (3, 'the end', 'other', 3)\n" +
        "SELECT 1 FROM dual";

    /**
     * Invoice
     * These are the actual payment dues that generate monthly
     * for users based on their Membership. Works with the
     * BillingRecord to figure out how to pay the invoice.
     * 
     * -- Fields --
     * @field invid (PK)
     * @field status
     * @field amount
     * @field month
     * @field brid (FK)
     * 
     * -- Relationships --
     * @relationship Invoice -- 1 --> BillingRecord
     * @relationship BillingRecord -- 0+ --> Invoice
     * 
     * -- Constraints --
     * @constraint status must be ('unpaid', 'paid')
     */
    public static final String InvoiceTable =
        "CREATE TABLE mngo1.Invoice (" + 
        "invid NUMBER PRIMARY KEY, " +
        "status VARCHAR2(255) NOT NULL, " +
        "amount NUMBER NOT NULL, " +
        "month DATE NOT NULL, " +
        // foreign key
        "brid NUMBER NOT NULL, " +
        "CONSTRAINT fk3 FOREIGN KEY (brid) REFERENCES mngo1.BillingRecord(brid) ON DELETE CASCADE, " +
        "CONSTRAINT chk2 CHECK (status IN ('unpaid', 'paid')))";
    public static final String InvoiceDrop = 
        "DROP TABLE mngo1.Invoice CASCADE CONSTRAINTS PURGE";
    public static final String InvoiceData = 
        "INSERT ALL\n" +
        "\tINTO mngo1.Invoice VALUES (1, 'unpaid', 53.99, TO_DATE('07-01-2025', 'MM-DD-YYYY'), 1)\n" +
        "\tINTO mngo1.Invoice VALUES (2, 'unpaid', 2.00, TO_DATE('03-01-2025', 'MM-DD-YYYY'), 2)\n" +
        "\tINTO mngo1.Invoice VALUES (3, 'paid', 9393.33, TO_DATE('05-01-2025', 'MM-DD-YYYY'), 3)\n" +
        "SELECT 1 FROM dual";
    
    /**
     * Membership
     * This is the subscription that users have, determining
     * the level of access they get to an AI chatbot. It will
     * determine the limit of their Messages, and the pay amount
     * for their Invoices.
     * 
     * -- Fields --
     * @field mtid (PK)
     * @field hasPro
     * @field tier
     * @field messageLimit
     * 
     * -- Relationships --
     * @relationship Membership -- 0+ --> Person
     * @relationship Person -- 1 --> Membership
     * 
     * -- Constraints --
     * @constraint hasPro must be (0, 1)
     */
    public static final String MembershipTable =
        "CREATE TABLE mngo1.Membership (" + 
        "mtid NUMBER PRIMARY KEY, " +
        "hasPro NUMBER NOT NULL, " +
        "tier NUMBER NOT NULL, " +
        "messageLimit NUMBER NOT NULL, " +
        "CONSTRAINT chk3 CHECK (hasPro IN (0, 1)))";
    public static final String MembershipDrop = 
        "DROP TABLE mngo1.Membership CASCADE CONSTRAINTS PURGE";
    public static final String MembershipData =
        "INSERT ALL\n" +
        "\tINTO mngo1.Membership VALUES (4, 0, 3, 100)\n" +
        "\tINTO mngo1.Membership VALUES (5, 1, 5, 1000)\n" +
        "\tINTO mngo1.Membership VALUES (6, 0, 1, 5)\n" +
        "SELECT 1 FROM dual";

    /**
     * Ticket
     * This is our Support Ticket system, allowing users to post their
     * concerns about the AI chatbot, subscription, or honestly anything.
     * Agents can assign themselves to a ticket to resolve them, or
     * escalate them.
     * 
     * -- Fields --
     * @field tid (PK)
     * @field duration  # in minutes
     * @field outcome
     * @field topic
     * @field userId (FK)
     * @field aid (FK)
     * 
     * -- Relationships --
     * @relationship Ticket -- 1 --> Person
     * @relationship Person -- 0+ --> Ticket
     * @relationship Ticket -- 0 | 1 --> Agent
     * @relationship Agent -- 0+ --> Ticket
     * 
     * -- Constraints --
     * @constraint outcome must be ('Resolved', 'Escalated', 'Waiting')
     */
    public static final String TicketTable =
        "CREATE TABLE mngo1.Ticket (" + 
        "tid NUMBER PRIMARY KEY, " +
        "duration NUMBER NOT NULL, " +
        "outcome VARCHAR2(255) NOT NULL, " +
        "topic VARCHAR2(255) NOT NULL, " + 
        // foreign keys
        "userId NUMBER NOT NULL, " +
        "aid NUMBER, " +
        "CONSTRAINT fk4 FOREIGN KEY (userId) REFERENCES mngo1.Person(userId), " +
        "CONSTRAINT fk5 FOREIGN KEY (aid) REFERENCES mngo1.Agent(aid) ON DELETE SET NULL, " +
        "CONSTRAINT chk4 CHECK (outcome IN ('Resolved', 'Escalated', 'Waiting')))";
    public static final String TicketDrop = 
        "DROP TABLE mngo1.Ticket CASCADE CONSTRAINTS PURGE";
    public static final String TicketData = 
        "INSERT ALL\n" +
        "\tINTO mngo1.Ticket VALUES (1, 13, 'Escalated', 'robots',  1, 1)\n" +
        "\tINTO mngo1.Ticket VALUES (2, 153, 'Resolved', 'stack overflow', 2, 2)\n" +
        "\tINTO mngo1.Ticket VALUES (3, 9393, 'Waiting', 'the meaning of life', 3, NULL)\n" +
        "SELECT 1 FROM dual";
    
    /**
     * Agent
     * These are a few of our unpaid employees. Their only job is
     * to resolve the ticket issues that users have.
     * 
     * -- Fields --
     * @field aid (PK)
     * @field name
     * 
     * -- Relationships --
     * @relationship Agent -- 0+ --> Ticket
     * @relationship Ticket -- 0 | 1 --> Agent
     * 
     * -- Constraints --
     */
    public static final String AgentTable =
        "CREATE TABLE mngo1.Agent (" + 
        "aid NUMBER PRIMARY KEY, " +
        "name VARCHAR2(255))";
    public static final String AgentDrop = 
        "DROP TABLE mngo1.Agent CASCADE CONSTRAINTS PURGE";
    public static final String AgentData =
        "INSERT ALL\n" +
        "\tINTO mngo1.Agent VALUES (1, 'Bob')\n" +
        "\tINTO mngo1.Agent VALUES (2, 'Kevin')\n" +
        "\tINTO mngo1.Agent VALUES (3, 'Steward')\n" +
        "SELECT 1 FROM dual";

    /**
     * Workspace
     * This is the Google Drive-like system that allows users
     * to collaborate with their AI stuff, which includes sharing
     * Prompt Templates and Conversations.
     * 
     * -- Fields --
     * @field wid (PK)
     * @field privacy
     * @field ownerId
     * 
     * -- Relationships --
     * @relationship Workspace -- 1+ --> Person
     * @relationship Person -- 0+ --> Workspace
     * @relationship Workspace -- 0+ --> Conversation
     * @relationship Conversation -- 0+ --> Workspace
     * @relationship Workspace -- 0+ --> UserPrompt
     * @relationship UserPrompt -- 0+ --> Workspace
     * @relationship Workspace -- 0+ --> PromptCategory
     * @relationship PromptCategory -- 1 --> Workspace
     * @relationship Workspace -- 0+ --> TemplatePrompt
     * @relationship TemplatePrompt -- 0+ --> Workspace
     * 
     * -- Constraints --
     * @constraint privacy is either 'public' or 'private'
     */
    public static final String WorkspaceTable =
        "CREATE TABLE mngo1.Workspace (" +
        "wid NUMBER PRIMARY KEY, " +
        "privacy VARCHAR(255) NOT NULL, " +
        "ownerId NUMBER NOT NULL, " + 
        "CONSTRAINT chk10 CHECK (privacy in ('public', 'private')), " +
        "CONSTRAINT fk6 FOREIGN KEY (ownerId) REFERENCES mngo1.Person(userId) ON DELETE CASCADE)";
    public static final String WorkspaceDrop =
        "DROP TABLE mngo1.Workspace CASCADE CONSTRAINTS PURGE";
    public static final String WorkspaceData = 
        "INSERT ALL\n" +
        "\tINTO mngo1.Workspace VALUES (1, 'public', 1)\n" +
        "\tINTO mngo1.Workspace VALUES (2, 'private', 3)\n" +
        "\tINTO mngo1.Workspace VALUES (3, 'private', 3)\n" +
        "SELECT 1 FROM dual";
    

    /**
     * TemplatePrompt
     * This template prompt is a copyable prompt that users can
     * utlize to start their own UserPrompt.
     * 
     * 
     * -- Fields --
     * @field tpid (PK)
     * @field prompt
     * 
     * -- Relationships --
     * @relationship TemplatePrompt -- 0+ --> Workspace
     * @relationship Workspace -- 1+ --> TemplatePrompt
     * 
     * -- Constraints --
     */
    public static final String TemplatePromptTable =
        "CREATE TABLE mngo1.TemplatePrompt (" +
        "tpid NUMBER PRIMARY KEY, " +
        "prompt VARCHAR(255) NOT NULL )";
    public static final String TemplatePromptDrop =
        "DROP TABLE mngo1.TemplatePrompt CASCADE CONSTRAINTS PURGE";
    public static final String TemplatePromptData =
        "INSERT ALL\n" +
        "\tINTO mngo1.TemplatePrompt VALUES (1, 'teach me sql queries')\n" +
        "\tINTO mngo1.TemplatePrompt VALUES (2, 'show me what an ER diagram is')\n" +
        "\tINTO mngo1.TemplatePrompt VALUES (3, 'what is relational calculus')\n" +
        "SELECT 1 FROM dual";

    /**
     * UserPrompt
     * This is a template prompt copied for the user to edit.
     * They can use this in a conversation to automatically
     * set up a prompt for the AI.
     * 
     * 
     * -- Fields --
     * @field upid (PK)
     * @field privacy
     * @field instructions
     * @field userId (FK)
     * 
     * -- Relationships --
     * @relationship UserPrompt -- 0+ --> Workspace
     * @relationship Workspace -- 0+ --> UserPrompt
     * @relationship UserPrompt -- 0+ --> Conversation
     * @relationship Workspace -- 0 | 1 --> UserPrompt
     * 
     * -- Constraints --
     * @constranit privacy is either 'public' or 'private'
     */
    public static final String UserPromptTable =
        "CREATE TABLE mngo1.UserPrompt (" +
        "upid NUMBER PRIMARY KEY, " +
        "instructions VARCHAR(255) NOT NULL, " +
        "privacy VARCHAR(255) NOT NULL, " +
        // foreign key
        "userId NUMBER NOT NULL, " +
        "CONSTRAINT chk11 CHECK (privacy in ('public', 'private')), " +
        "CONSTRAINT fk7 FOREIGN KEY (userId) REFERENCES mngo1.Person(userId) ON DELETE CASCADE)";
    public static final String UserPromptDrop = 
        "DROP TABLE mngo1.UserPrompt CASCADE CONSTRAINTS PURGE";
    public static final String UserPromptData =
        "INSERT ALL\n" +
        "\tINTO mngo1.UserPrompt VALUES (1, 'show me chens notation', 'private', 1)\n" +
        "\tINTO mngo1.UserPrompt VALUES (2, 'how do I insert all', 'public', 2)\n" +
        "\tINTO mngo1.UserPrompt VALUES (3, 'how do i do or statments', 'public', 3)\n" +
        "SELECT 1 FROM dual";
    /**
     * PromptCategory
     * This is a group of UserPrompts that users decide to group
     * together since they may have similarities. Theses are only
     * stored in a workspace
     * 
     * 
     * -- Fields --
     * @field pcid (PK)
     * @field categoryname
     * @field wid (FK)
     * 
     * -- Relationships --
     * @relationship PromptCategory -- 0+ --> UserPrompt
     * @relationship UserPrompt -- 0+ --> PromptCategory
     * @relationship PromptCategory -- 1 --> Workspace
     * @relationship Workspace -- 0+ --> PromptCategory
     * 
     * -- Constraints --
     */
    public static final String PromptCategoryTable =
        "CREATE TABLE mngo1.PromptCategory (" +
        "pcid NUMBER PRIMARY KEY, " +
        "categoryname VARCHAR(255) NOT NULL, " +
        "wid NUMBER NOT NULL, " + 
        "CONSTRAINT fk8 FOREIGN KEY (wid) REFERENCES mngo1.Workspace(wid) ON DELETE CASCADE)";
    public static final String PromptCategoryDrop = 
        "DROP TABLE mngo1.PromptCategory CASCADE CONSTRAINTS PURGE";
    public static final String PromptCategoryData =
        "INSERT ALL\n" +
        "\tINTO mngo1.PromptCategory VALUES (1, 'sql prompts', 3)\n" +
        "\tINTO mngo1.PromptCategory VALUES (2, 'main', 3)\n" +
        "\tINTO mngo1.PromptCategory VALUES (3, 'pancakes', 1)\n" +
        "SELECT 1 FROM dual";

    /**
     * Conversation
     * This is the primary tale responsible for holding the
     * contents of a conversation made between a user and
     * an AI.
     * 
     * 
     * -- Fields --
     * @field cid (PK)
     * @field title
     * @field pid (FK)
     * @field userId (FK)
     * 
     * -- Relationships --
     * @relationship Conversation -- 0 | 1 --> Persona
     * @relationship Persona -- 0+ --> Conversation
     * @relationship Conversation -- 0+ --> Message
     * @relationship Message -- 1 --> Conversation
     * @relationship Conversation -- 0+ --> Workspace
     * @relationship Workspace -- 0+ --> Conversation
     * 
     * -- Constraints --
     */
    public static final String ConversationTable =
        "CREATE TABLE mngo1.Conversation (" + 
        "cid NUMBER PRIMARY KEY, " +
        "title VARCHAR2(255), " +
        // foreign keys
        "userId NUMBER NOT NULL, " +
        "pid NUMBER NOT NULL, " +
        "CONSTRAINT fk9 FOREIGN KEY (userId) REFERENCES mngo1.Person(userId) ON DELETE CASCADE, " +
        "CONSTRAINT fk10 FOREIGN KEY (pid) REFERENCES mngo1.Persona(pid) ON DELETE SET NULL)";
    public static final String ConversationDrop = 
        "DROP TABLE mngo1.Conversation CASCADE CONSTRAINTS PURGE";
    public static final String ConversationData =
        "INSERT ALL\n" +
        "\tINTO mngo1.Conversation VALUES (1, 'therapy', 1, 1)\n" +
        "\tINTO mngo1.Conversation VALUES (2, 'fun games', 2, 3)\n" +
        "\tINTO mngo1.Conversation VALUES (3, 'Confusion', 3, 1)\n" +
        "SELECT 1 FROM dual";

    /**
     * Persona
     * This is a set of predefined set of Personas that users
     * can add to their conversation to change the personality
     * of the AI.
     * 
     * -- Fields --
     * @field pid (PK)
     * @field name
     * @field personality
     * 
     * -- Relationships --
     * @relationship Persona -- 0+ --> Conversation
     * @relationship Conversation -- 0 | 1 --> Persona
     * 
     * -- Constraints --
     */
    public static final String PersonaTable =
        "CREATE TABLE mngo1.Persona (" + 
        "pid NUMBER PRIMARY KEY, " +
        "name VARCHAR2(255) NOT NULL, " +
        "personality VARCHAR2(255) NOT NULL )";
    public static final String PersonaDrop = 
        "DROP TABLE mngo1.Persona CASCADE CONSTRAINTS PURGE";
    public static final String PersonaData =
        "INSERT ALL\n" +
        "\tINTO mngo1.Persona VALUES (1, 'siri', 'super annoying')\n" +
        "\tINTO mngo1.Persona VALUES (2, 'angry gemini', 'very aggressive')\n" +
        "\tINTO mngo1.Persona VALUES (3, 'Mccann', 'Happy Tuesday everbody')\n" +
        "SELECT 1 FROM dual";

    /**
     * Message
     * This is a single output of text that either comes from
     * the user or from the AI in a conversation. Users will be able to rate those
     * messages, give feedback, and even bookmark it.
     * 
     * -- Fields --
     * @field mid (PK)
     * @field message
     * @field sender
     * @field rating
     * @field ratingText
     * @field timestamp
     * @field cid (FK)
     * 
     * -- Relationships --
     * @relationship Message -- 0 | 1 --> Bookmark
     * @relationship Bookmark -- 1 --> Message
     * @relationship Message -- 1 --> Conversation
     * @relationship Conversation -- 0+ --> Message
     * 
     * -- Constraints --
     * @constraint the field 'sender' can only be ('user', 'ai')
     * @constraint the field 'rating' can only be (0, 1)
     */
    public static final String MessageTable =
        "CREATE TABLE mngo1.Message (" + 
        "mid NUMBER NOT NULL, " +
        "cid NUMBER NOT NULL, " +
        "message VARCHAR2(1000) NOT NULL, " +
        "sender VARCHAR2(255) NOT NULL, " +
        "ratingText VARCHAR2(255), " +
        "rating NUMBER NOT NULL, " +
        "timestamp DATE, " + 
        "CONSTRAINT pk16 PRIMARY KEY (mid, cid), " +
        "CONSTRAINT fk11 FOREIGN KEY (cid) REFERENCES mngo1.Conversation(cid) ON DELETE CASCADE, " +
        "CONSTRAINT chk5 CHECK(sender IN ('user', 'ai')), " +
        "CONSTRAINT chk6 CHECK(rating IN (-1, 0, 1)))";
    public static final String MessageDrop = 
        "DROP TABLE mngo1.Message CASCADE CONSTRAINTS PURGE";
    public static final String MessageData =
        "INSERT ALL\n" +
        "\tINTO mngo1.Message VALUES (1, 1, 'whats my favorite number', 'user', NULL, 0, TO_DATE('2025-08-13 14:32:10', 'YYYY-MM-DD HH24:MI:SS'))\n" +
        "\tINTO mngo1.Message VALUES (1, 2, 'how can I win chess', 'user', 'okay AI', 1,  TO_DATE('2025-08-14 1:39:11', 'YYYY-MM-DD HH24:MI:SS'))\n" +
        "\tINTO mngo1.Message VALUES (1, 3, 'tell me about yourself mccann', 'user', 'cool teacher', 1, TO_DATE('2025-08-15 14:31:10', 'YYYY-MM-DD HH24:MI:SS'))\n" +
        "\tINTO mngo1.Message VALUES (2, 1, 'your favorite number is 3', 'ai', NULL, 0, TO_DATE('2025-08-13 14:32:11', 'YYYY-MM-DD HH24:MI:SS'))\n" +
        "\tINTO mngo1.Message VALUES (2, 2, 'idk you tell me', 'ai', 'well i guess that makes sense', 1,  TO_DATE('2025-08-14 1:39:12', 'YYYY-MM-DD HH24:MI:SS'))\n" +
        "\tINTO mngo1.Message VALUES (2, 3, 'I am a professor', 'ai', 'cool teacher', 1, TO_DATE('2025-08-15 14:31:13', 'YYYY-MM-DD HH24:MI:SS'))\n" +
        "SELECT 1 FROM dual";

    /**
     * Bookmark
     * This is a bookmarking feature that allows users to mark
     * certain messages in their conversations to quickly
     * view.
     * 
     * -- Fields --
     * @field bid (PK)
     * @field mid (FK)
     * @field cid (FK)
     * @field userId (FK)
     * 
     * -- Relationships --
     * @relationship Bookmark -- 1 --> Person
     * @relationship User -- 0+ --> Bookmark
     * @relationship Bookmark -- 1 --> Message
     * @relationship Message -- 0 | 1 --> Bookmark
     * 
     * -- Constraints --
     */
    public static final String BookmarkTable =
        "CREATE TABLE mngo1.Bookmark (" + 
        "bid NUMBER NOT NULL, " +
        // foreign key
        "mid NUMBER NOT NULL, " + 
        "cid NUMBER NOT NUll, " + 
        "userId NUMBER NOT NULL, " +
        // constraints
        "CONSTRAINT pk15 PRIMARY KEY (bid, mid, cid), " +
        "CONSTRAINT fk12 FOREIGN KEY (userId) REFERENCES mngo1.Person(userId) ON DELETE CASCADE, " +
        "CONSTRAINT fk13 FOREIGN KEY (mid, cid) REFERENCES mngo1.Message(mid, cid) ON DELETE CASCADE) ";
    public static final String BookmarkDrop = 
        "DROP TABLE mngo1.Bookmark CASCADE CONSTRAINTS PURGE";
    public static final String BookmarkData =
        "INSERT ALL\n" +
        "\tINTO mngo1.Bookmark(bid, mid, cid, userId) VALUES (1, 1, 1, 1)\n" +
        "\tINTO mngo1.Bookmark(bid, mid, cid, userId) VALUES (1, 2, 1, 2)\n" +
        "SELECT 1 FROM dual";
    
    /**
     * These are special tables meant to properly manage
     * our many-to-many relationships.
     */
    
    /**
     * UserWorkspace
     * This handles the relationship of Users being members
     * of multiple workspaces, as well as workspaces have multiple
     * members.
     * 
     * -- Fields --
     * @field userId (CK) (FK)
     * @field wid (CK) (FK)
     * 
     * -- Constraints --
     */
    public static final String UserWorkspaceTable =
        "CREATE TABLE mngo1.UserWorkspace (" + 
        "userId NUMBER NOT NULL, " +
        "wid NUMBER NOT NUll, " +
        "CONSTRAINT pk11 PRIMARY KEY (userId, wid), " +
        "CONSTRAINT fk14 FOREIGN KEY (userId) REFERENCES mngo1.Person(userId) ON DELETE CASCADE, " +
        "CONSTRAINT fk15 FOREIGN KEY (wid) REFERENCES mngo1.Workspace(wid) ON DELETE CASCADE)";
    public static final String UserWorkspaceDrop = 
        "DROP TABLE mngo1.UserWorkspace CASCADE CONSTRAINTS PURGE";
    public static final String UserWorkspaceData =
        "INSERT ALL\n" +
        "\tINTO mngo1.UserWorkspace VALUES (1, 1)\n" +
        "\tINTO mngo1.UserWorkspace VALUES (1, 2)\n" +
        "\tINTO mngo1.UserWorkspace VALUES (1, 3)\n" +
        "\tINTO mngo1.UserWorkspace VALUES (3, 3)\n" +
        "\tINTO mngo1.UserWorkspace VALUES (3, 2)\n" +
        "SELECT 1 FROM dual";

    /**
     * UserPromptWorkspace
     * This handles the relationship of Users Prompts being
     * shared to multiple workspaces, and workspaces having
     * multiple user prompts.
     * 
     * -- Fields --
     * @field upid (CK) (FK)
     * @field wid (CK) (FK)
     * 
     * -- Constraints --
     */
    public static final String UserPromptWorkspaceTable =
        "CREATE TABLE mngo1.UserPromptWorkspace (" + 
        "upid NUMBER NOT NULL, " +
        "wid NUMBER NOT NULL, " + 
        "CONSTRAINT pk10 PRIMARY KEY (upid, wid), " +
        "CONSTRAINT fk16 FOREIGN KEY (upid) REFERENCES mngo1.UserPrompt(upid) ON DELETE CASCADE, " +
        "CONSTRAINT fk17 FOREIGN KEY (wid) REFERENCES mngo1.Workspace(wid) ON DELETE CASCADE)";
    public static final String UserPromptWorkspaceDrop = 
        "DROP TABLE mngo1.UserPromptWorkspace CASCADE CONSTRAINTS PURGE";
    public static final String UserPromptWorkspaceData =
        "INSERT ALL\n" +
        "\tINTO mngo1.UserPromptWorkspace VALUES (1, 1)\n" +
        "\tINTO mngo1.UserPromptWorkspace VALUES (1, 3)\n" +
        "\tINTO mngo1.UserPromptWorkspace VALUES (3, 1)\n" +
        "SELECT 1 FROM dual";
    
    /**
     * TemplatePromptWorkspace
     * This handles the relationship of Template Prompts being
     * in multiple workspaces, and workspaces having
     * multiple template prompts.
     * 
     * -- Fields --
     * @field tpid (CK) (FK)
     * @field wid (CK) (FK)
     * 
     * -- Constraints --
     */
    public static final String TemplatePromptWorkspaceTable =
        "CREATE TABLE mngo1.TemplatePromptWorkspace (" + 
        "tpid NUMBER NOT NULL, " +
        "wid NUMBER NOT NUll, " +
        "CONSTRAINT pk12 PRIMARY KEY (tpid, wid), " +
        "CONSTRAINT fk18 FOREIGN KEY (tpid) REFERENCES mngo1.TemplatePrompt(tpid) ON DELETE CASCADE, " +
        "CONSTRAINT fk19 FOREIGN KEY (wid) REFERENCES mngo1.Workspace(wid) ON DELETE CASCADE)";
    public static final String TemplatePromptWorkspaceDrop = 
        "DROP TABLE mngo1.TemplatePromptWorkspace CASCADE CONSTRAINTS PURGE";
    public static final String TemplatePromptWorkspaceData =
        "INSERT ALL\n" +
        "\tINTO mngo1.TemplatePromptWorkspace VALUES (1, 1)\n" +
        "\tINTO mngo1.TemplatePromptWorkspace VALUES (2, 1)\n" +
        "\tINTO mngo1.TemplatePromptWorkspace VALUES (3, 1)\n" +
        "\tINTO mngo1.TemplatePromptWorkspace VALUES (1, 2)\n" +
        "\tINTO mngo1.TemplatePromptWorkspace VALUES (2, 2)\n" +
        "\tINTO mngo1.TemplatePromptWorkspace VALUES (3, 2)\n" +
        "\tINTO mngo1.TemplatePromptWorkspace VALUES (1, 3)\n" +
        "\tINTO mngo1.TemplatePromptWorkspace VALUES (2, 3)\n" +
        "\tINTO mngo1.TemplatePromptWorkspace VALUES (3, 3)\n" +
        "SELECT 1 FROM dual";

    /**
     * ConversationWorkspace
     * This handles the relationship of Conversations being
     * added to multiple workspaces, and workspaces having
     * multiple conversations
     * 
     * -- Fields --
     * @field cid (CK) (FK)
     * @field wid (CK) (FK)
     * 
     * -- Constraints --
     */
    public static final String ConversationWorkspaceTable =
        "CREATE TABLE mngo1.ConversationWorkspace (" + 
        "cid NUMBER NOT NULL, " +
        "wid NUMBER NOT NUll, " +
        "CONSTRAINT pk131 PRIMARY KEY (cid, wid), " +
        "CONSTRAINT fk201 FOREIGN KEY (cid) REFERENCES mngo1.Conversation(cid) ON DELETE CASCADE, " +
        "CONSTRAINT fk211 FOREIGN KEY (wid) REFERENCES mngo1.Workspace(wid) ON DELETE CASCADE)";
    public static final String ConversationWorkspaceDrop = 
        "DROP TABLE mngo1.ConversationWorkspace CASCADE CONSTRAINTS PURGE";
    public static final String ConversationWorkspaceData =
        "INSERT ALL\n" +
        "\tINTO mngo1.ConversationWorkspace VALUES (1, 1)\n" +
        "\tINTO mngo1.ConversationWorkspace VALUES (1, 3)\n" +
        "\tINTO mngo1.ConversationWorkspace VALUES (3, 2)\n" +
        "SELECT 1 FROM dual";

    /**
     * PromptCategoryUserPrompt
     * This handles the relationship of User Prompts being
     * added to multiple prompt categories and prompt
     * categories having multiple user prompts.
     * 
     * -- Fields --
     * @field pcid (CK) (FK)
     * @field upid (CK) (FK)
     * 
     * -- Constraints --
     */
    public static final String PromptCategoryUserPromptTable =
        "CREATE TABLE mngo1.PromptCategoryUserPrompt (" + 
        "pcid NUMBER NOT NULL, " +
        "upid NUMBER NOT NUll, " + 
        "CONSTRAINT pk14 PRIMARY KEY (pcid, upid), " +
        "CONSTRAINT fk22 FOREIGN KEY (pcid) REFERENCES mngo1.PromptCategory(pcid) ON DELETE CASCADE, " +
        "CONSTRAINT fk23 FOREIGN KEY (upid) REFERENCES mngo1.UserPrompt(upid) ON DELETE CASCADE)";
    public static final String PromptCategoryUserPromptDrop = 
        "DROP TABLE mngo1.PromptCategoryUserPrompt CASCADE CONSTRAINTS PURGE";
    public static final String PromptCategoryUserPromptData =
        "INSERT ALL\n" +
        "\tINTO mngo1.PromptCategoryUserPrompt VALUES (1, 1)\n" +
        "\tINTO mngo1.PromptCategoryUserPrompt VALUES (1, 2)\n" +
        "\tINTO mngo1.PromptCategoryUserPrompt VALUES (3, 3)\n" +
        "SELECT 1 FROM dual";

    /**
     * These are all our triggers needed to automatically update/insert/delete data
     */

    /**
     * @trigger Deleting a user must fail if there are unpaid invoices or open support tickets.
     */
    public static final String UnpaidInvoiceTrigger = 
        // Trigger before delete on a user
        "CREATE OR REPLACE TRIGGER prevent_user_delete " +
        "BEFORE DELETE ON mngo1.Person " +
        "FOR EACH ROW " +
        // For each user being deleted, declare variables to count unpaid invoices and open tickets
        "DECLARE " +
        "   unpaid_count NUMBER; " +
        "   open_ticket_count NUMBER; " +
        // Begin the trigger
        "BEGIN " +
            // Check for unpaid invoices by joining the Invoice and BillingRecord tables
        "   SELECT COUNT(*) INTO unpaid_count " +
        "   FROM mngo1.Invoice i " +
        "   JOIN mngo1.BillingRecord b ON i.brid = b.brid " +
        "   WHERE b.userId = :OLD.userId " +
        "   AND i.status = 'unpaid'; " +
        "" +
            // Check for open support tickets in the Ticket table
        "   SELECT COUNT(*) INTO open_ticket_count " +
        "   FROM mngo1.Ticket " +
        "   WHERE userId = :OLD.userId " +
        "   AND outcome = 'Waiting'; " +
        "" +
            // If there are any unpaid invoices or open tickets, raise an error to prevent deletion
        "   IF unpaid_count > 0 OR open_ticket_count > 0 THEN " +
        "       RAISE_APPLICATION_ERROR(-20001, " +
        "       'Cannot delete user: has unpaid invoices or open support tickets.'); " +
        "   END IF; " +
        "END;";
    
    /**
     * @trigger When a user adds a UserPrompt, check that
     *      they are actually a member of that workspace in:
     *          UserWorkspace
     */
    public static final String unknownUserPromptInWorkspaceTrigger = 
        // Trigger before insert into UserPrompt
        "CREATE OR REPLACE TRIGGER enforce_workspace_userprompt " +
        "BEFORE INSERT ON mngo1.UserPrompt " +
        "FOR EACH ROW " +
        // For each UserPrompt being added, declare a variable to count the number of workspace
        "DECLARE " +
        "   member_count NUMBER; " +
        // Begin the trigger
        "BEGIN " +
            // Check that the user adding the UserPrompt is a member of the workspace
            // they are trying to add to (member_count should be 1 if they are a member, 0 if not)
        "   SELECT COUNT(*) INTO member_count " +
        "   FROM mngo1.Workspace " +
        "   WHERE userId = :NEW.userId " +
        "   AND wid = (SELECT wid FROM mngo1.UserPrompt WHERE upid = :NEW.upid); " +
        // If the member count is 0, it means the user is not a member of the workspace, so raise an error to prevent insertion
        "   IF member_count = 0 THEN " +
        "       RAISE_APPLICATION_ERROR(-20002, " +
        "       'User is not a member of the specified workspace.'); " +
        "   END IF; " +
        "END;";
    
    /**
     * @trigger When a user adds a Conversation, check that
     *      they are actually a member of that workspace in:
     *          UserWorkspace
     */
    public static final String unknownConversationInWorkspaceTrigger = 
        // Trigger before insert into Conversation
        "CREATE OR REPLACE TRIGGER enforce_workspace_conversation " +
        "BEFORE INSERT ON mngo1.Conversation " +
        "FOR EACH ROW " +
        // For each Conversation being added, declare a variable to count the number of workspace
        "DECLARE " +
        "   member_count NUMBER; " +
        // Begin the trigger
        "BEGIN " +
            // Check that the user adding the Conversation is a member of the workspace
            // they are trying to add to (member_count should be 1 if they are a member, 0 if not)
        "   SELECT COUNT(*) INTO member_count " +
        "   FROM mngo1.Workspace " +
        "   WHERE userId = :NEW.userId " +
        "   AND wid = (SELECT wid FROM mngo1.Conversation WHERE cid = :NEW.cid); " +
        // If the member count is 0, it means the user is not a member of the workspace, so raise an error to prevent insertion
        "   IF member_count = 0 THEN " +
        "       RAISE_APPLICATION_ERROR(-20002, " +
        "       'User is not a member of the specified workspace.'); " +
        "   END IF; " +
        "END;";

    
    /**
     * @trigger when a persona is 'ATTEMPTING' to be deleted, check that no more
     *      than 5 conversations using it (abort deletion if so). Then set the
     *      persona of any conversations using it to NULL
     */
    public static final String deletePersonaTrigger = 
        // Trigger on personal deletion
        "CREATE OR REPLACE TRIGGER prevent_persona_delete " +
        "BEFORE DELETE ON mngo1.Persona " +
        "FOR EACH ROW " +
        // For each persona being deleted, declare a variable to count the number of conversations using that persona
        "DECLARE " +
        "   convo_count NUMBER; " +
        // Begin the trigger
        "BEGIN " +
            // Get how many conversations are using this persona
        "   SELECT COUNT(*) INTO convo_count " +
        "   FROM mngo1.Conversation " +
        "   WHERE pid = :OLD.pid; " +
        // If the persona is being used by more than 5 conversations, raise an error to prevent deletion
        "   IF convo_count > 5 THEN " +
        "       RAISE_APPLICATION_ERROR(-20003, " +
        "       'Cannot delete persona: more than 5 conversations are using it.'); " +
        "   END IF; " +
        // If their are 5 of fewer conversation using the persona, allow deletion but 
        // Set any conversations using that personaID to have NULL pid
        "   UPDATE mngo1.Conversation " +
        "   SET pid = NULL " +
        "   WHERE pid = :OLD.pid; " +
        "END;";

    


    public static final String[] DropTables = {
        PersonDrop, LanguageDrop, BillingRecordDrop, InvoiceDrop,
        MembershipDrop, TicketDrop, AgentDrop, WorkspaceDrop, TemplatePromptDrop,
        UserPromptDrop, PromptCategoryDrop, ConversationDrop, PersonaDrop,
        MessageDrop, BookmarkDrop, UserWorkspaceDrop, TemplatePromptWorkspaceDrop,
        ConversationWorkspaceDrop, PromptCategoryUserPromptDrop, UserPromptWorkspaceDrop
    };

    public static final String[] CreateTables = {
        AgentTable, LanguageTable, MembershipTable, PersonaTable, TemplatePromptTable,
        PersonTable, TicketTable, BillingRecordTable, InvoiceTable, ConversationTable, MessageTable,
        BookmarkTable, WorkspaceTable, UserPromptTable, UserPromptWorkspaceTable,
        ConversationWorkspaceTable, PromptCategoryTable, PromptCategoryUserPromptTable,
        TemplatePromptWorkspaceTable, UserWorkspaceTable,

        UnpaidInvoiceTrigger, unknownUserPromptInWorkspaceTrigger, unknownConversationInWorkspaceTrigger, deletePersonaTrigger
    };

    public static final String[] CreateData = {
        AgentData, LanguageData, MembershipData, PersonaData, TemplatePromptData,
        PersonData, TicketData, BillingRecordData, InvoiceData, ConversationData, MembershipData,
        BookmarkData, WorkspaceData, UserPromptData, UserPromptWorkspaceData,
        ConversationWorkspaceData, PromptCategoryData, PromptCategoryUserPromptData,
        TemplatePromptWorkspaceData, UserWorkspaceData
    };
}
