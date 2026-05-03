/**
 * Setup.java
 * This sets up all the necessary tables in our database to run
 * our queries.
 * 
 * 
 * Each description follows:
 * 
 * <tablename>
 * <description>
 * 
 * -- Fields --
 * @field <field> [(PK) | (CK) | (FK)]
 * 
 * -- Relationships -- (1-M, 1-1, M-M relationships)
 * @relationship <tablename1> -- <relation> --> <tablename2>
 * 
 * -- Constraints --
 * @constraint <constraint>
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
        "lid NUMBER NOT NULL)"
        ;
    public static final String PersonDrop = 
        "DROP TABLE Person CASCADE CONSTRAINTS";

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
        "language VARCHAR2(255) )";
    public static final String LanguageDrop = 
        "DROP TABLE mngo1.Language CASCADE CONSTRAINTS";

    /**
     * BillingRecord
     * Holds the Billing Data that is used to pay for Invoices.
     * It is linked to a user upon user creation.
     * 
     * -- Fields --
     * @field bid (PK)
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
        "bid NUMBER PRIMARY KEY, " +
        "payaddress VARCHAR2(255), " +
        "paymethod VARCHAR2(255), " +
        // foreign key
        "userId NUMBER NOT NULL)";
    public static final String BillingRecordDrop = 
        "DROP TABLE mngo1.BillingRecord CASCADE CONSTRAINTS";

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
        "status VARCHAR2(255), " +
        "amount NUMBER, " +
        "month DATE, " +
        // foreign key
        "brid NUMBER NOT NULL)";
    public static final String InvoiceDrop = 
        "DROP TABLE mngo1.Invoice CASCADE CONSTRAINTS";
    
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
     * @field limit
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
        "hasPro NUMBER, " +
        "tier NUMBER, " +
        "limit NUMBER )";
    public static final String MembershipDrop = 
        "DROP TABLE mngo1.Membership CASCADE CONSTRAINTS";

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
        "duration NUMBER, " +
        "outcome VARCHAR2(255), " +
        "topic VARCHAR2(255), " + 
        // foreign keys
        "userId NUMBER NOT NULL" +
        "aid NUMBER)";
    public static final String TicketDrop = 
        "DROP TABLE mngo1.Ticket CASCADE CONSTRAINTS";
    
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
        "DROP TABLE mngo1.Agent CASCADE CONSTRAINTS";

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
     */
    public static final String WorkspaceTable =
        "CREATE TABLE mngo1.Workspace (" +
        "wid NUMBER PRIMARY KEY, " +
        "privacy VARCHAR(255) NOT NULL )";
    public static final String WorkspaceDrop =
        "DROP TABLE mngo1.Workspace CASCADE CONSTRAINTS";

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
        "DROP TABLE mngo1.TemplatePrompt CASCADE CONSTRAINTS";

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
     */
    public static final String UserPromptTable =
        "CREATE TABLE mngo1.UserPrompt (" +
        "upid NUMBER PRIMARY KEY, " +
        "instructions VARCHAR(255) NOT NULL, " +
        "privacy VARCHAR(255) NOT NULL, " +
        // foreign key
        "userId NUMBER NOT NULL";
    public static final String UserPromptDrop = 
        "DROP TABLE mngo1.UserPrompt CASCADE CONSTRAINTS";

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
        "categoryname VARCHAR(255) NOT NULL )";
    public static final String PromptCategoryDrop = 
        "DROP TABLE mngo1.PromptCategory CASCADE CONSTRAINTS";

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
        "pid NUMBER NOT NULL )";
    public static final String ConversationDrop = 
        "DROP TABLE mngo1.Conversation CASCADE CONSTRAINTS";

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
        "name VARCHAR2(255), " +
        "personality VARCHAR2(255) )";
    public static final String PersonaDrop = 
        "DROP TABLE mngo1.Persona CASCADE CONSTRAINTS";

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
        "title VARCHAR(255), " +
        "CONSTRAINT m_pk PRIMARY KEY (mid, cid), " +
        "CONSTRAINT fk_c FOREIGN KEY (cid)\nREFERENCES mngo1.Conversation(cid) ON DELETE SET NULL)";
    public static final String MessageDrop = 
        "DROP TABLE mngo1.Message CASCADE CONSTRAINTS";

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
        "mid NUMBER NOT NULL, " + 
        "cid NUMBER NOT NUll, " + 
        // foreign key
        "userId NUMBER NOT NULL, " +
        // constraints
        "CONSTRAINT b_m PRIMARY KEY (mid, bid, cid), " +
        "CONSTRAINT fk_m FOREIGN KEY (mid, cid)\nREFERENCES mngo1.Message(mid, cid) ON DELETE SET NULL) ";
    public static final String BookmarkDrop = 
        "DROP TABLE mngo1.Bookmark CASCADE CONSTRAINTS";
    
    /**
     * These are special tables meant to properly manage
     * our many-to-many relationships.
     */
    
    /**
     * UserWorkspace
     * This handles the relationship of Users being owner/members
     * of multiple workspaces, as well as workspaces have multiple
     * members.
     * 
     * -- Fields --
     * @field userId (CK)
     * @field wid (CK)
     * 
     * -- Constraints --
     */
    public static final String UserWorkspaceTable =
        "CREATE TABLE mngo1.UserWorkspace (" + 
        "userId NUMBER NOT NULL, " +
        "wid NUMBER NOT NUll)";
    public static final String UserWorkspaceDrop = 
        "DROP TABLE mngo1.UserWorkspace CASCADE CONSTRAINTS";

    /**
     * UserPromptWorkspace
     * This handles the relationship of Users Prompts being
     * shared to multiple workspaces, and workspaces having
     * multiple user prompts.
     * 
     * -- Fields --
     * @field upid (CK)
     * @field wid (CK)
     * 
     * -- Constraints --
     */
    public static final String UserPromptWorkspaceTable =
        "CREATE TABLE mngo1.UserPromptWorkspace (" + 
        "upid NUMBER NOT NULL, " +
        "wid NUMBER NOT NUll)";
    public static final String UserPromptWorkspaceDrop = 
        "DROP TABLE mngo1.UserPromptWorkspace CASCADE CONSTRAINTS";
    
    /**
     * TemplatePromptWorkspace
     * This handles the relationship of Template Prompts being
     * in multiple workspaces, and workspaces having
     * multiple template prompts.
     * 
     * -- Fields --
     * @field upid (CK)
     * @field wid (CK)
     * 
     * -- Constraints --
     */
    public static final String TemplatePromptWorkspaceTable =
        "CREATE TABLE mngo1.TemplatePromptWorkspace (" + 
        "upid NUMBER NOT NULL, " +
        "wid NUMBER NOT NUll)";
    public static final String TemplatePromptWorkspaceDrop = 
        "DROP TABLE mngo1.TemplatePromptWorkspace CASCADE CONSTRAINTS";

    /**
     * ConversationWorksapce
     * This handles the relationship of Conversations being
     * added to multiple workspaces, and workspaces having
     * multiple conversations
     * 
     * -- Fields --
     * @field cid (CK)
     * @field wid (CK)
     * 
     * -- Constraints --
     */
    public static final String ConversationwWorkspaceTable =
        "CREATE TABLE mngo1.ConversationwWorkspace (" + 
        "upid NUMBER NOT NULL, " +
        "wid NUMBER NOT NUll)";
    public static final String ConversationwWorkspaceDrop = 
        "DROP TABLE mngo1.ConversationwWorkspace CASCADE CONSTRAINTS";

    /**
     * PromptCategoryUserPrompt
     * This handles the relationship of User Prompts being
     * added to multiple prompt categories and prompt
     * categories having multiple user prompts.
     * 
     * -- Fields --
     * @field pcid (CK)
     * @field upid (CK)
     * 
     * -- Constraints --
     */
    public static final String PromptCategoryUserPromptTable =
        "CREATE TABLE mngo1.PromptCategoryUserPrompt (" + 
        "upid NUMBER NOT NULL, " +
        "wid NUMBER NOT NUll)";
    public static final String PromptCategoryUserPromptDrop = 
        "DROP TABLE mngo1.PromptCategoryUserPrompt CASCADE CONSTRAINTS";



    /**
     * These are all our triggers needed to automatically update/insert/delete data
     */

    /**
     * @trigger Deleting a user must fail if there are unpaid invoices or open support tickets.
     */
    public static final String UnpaidInvoiceTrigger = "";

    /**
     * @trigger Must automatically be created every month given the Membership of a user.
     */
    public static final String updateMembershipsTrigger = "";

    /**
     * @trigger Does not create an invoice if the user's Membership is tier 0.
     */
    public static final String noInvoiceTrigger = "";

    /**
     * @trigger When a user is deleted, their workspaces, conversations, bookmarks, messages,
     *          and user prompts must also be deleted. All Many-to-Many relationship table
     *          entries containing the workspace id must also be deleted:
     *          ConversationWorkspace
     *          TemplatePromptWorksapce
     *          UserWorkspace
     *          UserPromptWorkspace
     *          Finally, any existing upid in other people's conversations must be NULL'ed
     */
    public static final String deleteUserTrigger = "";
    
    /**
     * @trigger When a user adds a UserPrompt or a Conversation, check that
     *      they are actually a member of that workspace in:
     *          UserWorkspace
     */
    public static final String unknownUserInWorkspaceTrigger = "";

    /**
     * @trigger when a workspace is deleted, all of its prompt categories are deleted.
     *      Its link to Userprompts must also be deleted:
     *          PromptCategoryUserPrompt
     */
    public static final String deleteWorkspaceTrigger = "";

    /**
     * @trigger when a conversation is deleted, all messages in that conversation is deleted.
     *      Any references added to it through the workspace must be deleted:
     *          ConversationWorkspace
     */
    public static final String deleteConversationTrigger = "";
    
    /**
     * @trigger when a message is deleted, its bookmark must be deleted.
     */
    public static final String deleteMessageTrigger = "";
    
    /**
     * @trigger when a persona is 'ATTEMPTING' to be deleted, check that no more
     *      than 5 conversations using it (abort deletion if so). Then set the
     *      persona of any conversations using it to NULL
     */
    public static final String deletePersonaTrigger = "";

    



}
