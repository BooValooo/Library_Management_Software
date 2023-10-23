package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class BDDConnector {
    private static String url = "jdbc:sqlite:/home/administrateur/Documents/travail/Supervision de capteurs/tp-bibliotheque/src/Database_bibliothèque";
    private static String driverName = "org.sqlite.JDBC";
    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
        System.out.println("Database opened successfully");
        return con;
    }
}


