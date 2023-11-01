package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Permet de se connecter à la BDD de la bibliothèque.
//l'url peut être modifié pour changer de BDD
public class BDDConnector {
    private static String url = "jdbc:sqlite:/home/administrateur/Documents/travail/Supervision de capteurs/tp-bibliotheque/src/Database_bibliothèque";
    private static String driverName = "org.sqlite.JDBC";
    private static Connection con;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        BDDConnector.url = url;
    }

    public static String getDriverName() {
        return driverName;
    }

    public static void setDriverName(String driverName) {
        BDDConnector.driverName = driverName;
    }

    public static Connection getCon() {
        return con;
    }

    public static void setCon(Connection con) {
        BDDConnector.con = con;
    }

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


