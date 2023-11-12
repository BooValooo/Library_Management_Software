package com.example.bibliotheque;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;

//Classe abstraite permettant de se connecter à la BDD et de n'utiliser qu'un seul connecteur lors de l'utilisation de l'application. Evite de devoir se reconnecter à chaque requête.
//l'url peut être modifié pour changer de BDD
public abstract class Controller {

    private static String url = "jdbc:sqlite:/home/administrateur/Documents/travail/Supervision de capteurs/tp-bibliotheque/src/Database_bibliothèque";
    private static String driverName = "org.sqlite.JDBC";
    static Connection c = getConnection();
    static Integer utilisateurId = null;

    // Pour afficher des pop-ups d'erreur depuis n'importe quel controller
    public void afficherMessageErreur(String titre, String entete, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        String css = this.getClass().getResource("styles.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);
        alert.showAndWait();
    }


    public void afficherMessageInfo(String titre, String entete, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);
        String css = this.getClass().getResource("styles.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);
        alert.showAndWait();
    }

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                c = DriverManager.getConnection(url);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
        System.out.println("Database opened successfully");
        return c;
    }

     /*  Fonctions qui pourraient être utiles si je décidais de gérer plusieurs bases de données (par exemple, système de gestion de différentes bibliothèques)

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


     */

}
