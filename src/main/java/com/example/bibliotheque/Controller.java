package com.example.bibliotheque;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.util.Iterator;

//Classe abstraite permettant de n'utiliser qu'un seul connecteur lors de l'utilisation de l'application. Evite de devoir se reconnecter à chaque requête.
public abstract class Controller {

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

    public void afficherMessageSucces(String titre, String entete, String contenu) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY); // Pour éviter les boutons de fermeture de la fenêtre
        alert.setTitle(titre);
        alert.setHeaderText(entete);
        alert.setContentText(contenu);

        Iterator<ButtonType> iterator = alert.getButtonTypes().iterator();
        while (iterator.hasNext()) {
            ButtonType bouton = iterator.next();
            if (bouton.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                iterator.remove(); // Supprime le bouton "Annuler"
            }
        }

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

}
