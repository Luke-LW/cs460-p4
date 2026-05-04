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
 *       Purpose:   
 * 
 *    Description:  
 *                  
 * 
 *   Requirements:  Java 25 or earlier
 *        Compile:  javac Database.java
 *         Usage:   java Database 
 *    Input Files:  None. 
 */

import java.sql.*;

public class Database {
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
            dbconn.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        // remove the tables if they already exist
        System.out.println("Dropping tables...");

        for (String query : Setup.DropTables) {
            try {
                Statement stmt = dbconn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(query);
                System.out.println(e);
            }
        }

        // create the tables
        System.out.println("Creating tables...");
        for (String query : Setup.CreateTables) {
            try {
                Statement stmt = dbconn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(query);
                System.out.println(e);
            }
        }

        // create the data
        System.out.println("Creating Data...");
        for (String query : Setup.CreateData) {
            try {
                Statement stmt = dbconn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(query);
                System.out.println(e);
            }
        }
    }


}

