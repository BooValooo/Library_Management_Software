package com.example.bibliotheque;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;

//Classe abstraite permettant de n'utiliser qu'un seul connecteur lors de l'utilisation de l'application. Evite de devoir se reconnecter à chaque requête.
public abstract class Controller {

    private Stage stage = new Stage();
    static Connection c = BDDConnector.getConnection();
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
}
