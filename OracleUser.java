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
 *       Purpose:   Provide login credentials for group access to the Oracle database
 * 
 *    Description:  Provides strings for Oracle login credentials which are used
 *                  in Interface.java's main method to connect to the database via JDBC driver.
 *                  
 *   Requirements:  Java 25 or earlier
 *        Compile:  javac OracleUser.java
 *         Usage:   java OracleUser 
 *    Input Files:  None. 
 */

public class OracleUser {
    public static final String oracle = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
    public static final String username = "mngo1";
    public static final String password = "a5948";
}
