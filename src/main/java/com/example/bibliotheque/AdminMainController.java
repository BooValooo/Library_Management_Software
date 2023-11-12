package com.example.bibliotheque;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminMainController extends ClientMainController {
    private SearchAdminController searchAdminController = null;
    private UtilisateursController utilisateursController = null;
    private EmpruntsAdminController empruntsAdminController = null;

    /* Menu Supprimer */
    @FXML
    protected void onSupprimerClick() {
        afficherMessageInfo("Notice","Supprimer un livre", "Pour retirer un livre de la bibliothèque, réalisez un clic droit dessus, et sélectionnez 'Supprimer'.");
    }


    /* Menu Rechercher */
    @FXML
    @Override
    //Ouvre une vue de recherche de livre
    protected void onSearchClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchAdminView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        searchAdminController = fxmlLoader.getController();
        searchAdminController.mainController = this;
        stage.setTitle("Recherche d'un ouvrage");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage

    }

    /* Menu Ajouter */

    @FXML
    protected void onAjouterUtilisateurClick() {
        afficherMessageInfo("Notice","Créer un nouveau profil utilisateur", "Pour créer un nouveau profil d'utilisateur, rendez-vous dans le menu 'Utilisateurs', puis cliquez sur 'Ajouter et modifier'.");
    }

    /* Menu Utilisateurs */
    @FXML
    protected void onUtilisateursClick() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("utilisateursView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        utilisateursController = fxmlLoader.getController();
        utilisateursController.initializeTableViewUtilisateurs();
        stage.setTitle("Données sur les utilisateurs");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage
    }

    @FXML
    @Override
    // Actions possibles à réaliser après un clic droit (après sélection d'un livre)
    protected void empruntClick() {

        // Crée un menu contextuel
        ContextMenu contextMenu = new ContextMenu();
        MenuItem emprunterLivre = new MenuItem("Emprunter");
        MenuItem historiqueEmprunts = new MenuItem("Historique des emprunts");
        MenuItem supprimerLivre = new MenuItem("Supprimer");
        contextMenu.getItems().addAll(emprunterLivre, historiqueEmprunts, supprimerLivre);

        // Définit un gestionnaire d'événements pour afficher le menu contextuel lors du clic droit
        tableViewLivres.setOnContextMenuRequested(event -> {
            contextMenu.show(tableViewLivres, event.getScreenX(), event.getScreenY());
        });

        // Associe des actions aux éléments du menu
        emprunterLivre.setOnAction(e -> {
            Livre selectedLivre = tableViewLivres.getSelectionModel().getSelectedItem();
            Utilisateur user = new Utilisateur();
            user.id = utilisateurId;
            try {
                user.setDateListeRouge(user.getDateListeRouge(c));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (!selectedLivre.disponible) {
                afficherMessageErreur("Erreur", "Livre non disponible", "Ce livre n'est pas disponible actuellement.");
                return;
            } else {
                try {
                    if (user.getNombreEmprunts(c) >= user.getNombreMaxEmprunts(c)) { // Refus si trop grand nombre d'emprunts
                        afficherMessageErreur("Erreur", "Nombre maximal d'emprunts autorisés atteint", "Si vous souhaitez emprunter un nouveau livre, veuillez d'abord en rendre un, ou bien veuillez prendre contact avec un bibliothécaire.");
                        return;
                    } else if (!user.getDateListeRouge().equals("")) {
                        afficherMessageErreur("Erreur", "Utilisateur sur liste rouge", "Vous avez été radié(e) de cette bibliothèque.");
                    } else {
                        // Obtenir la date du jour
                        LocalDate dateDuJour = LocalDate.now();

                        // Formater la date en "aaaa-MM-jj"
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
                        String dateFormatee = dateDuJour.format(formatter);

                        // Obtenir la date de retour
                        String dateFin = user.getDateLimiteEmprunt(c, dateDuJour);

                        selectedLivre.emprunt(c, utilisateurId, selectedLivre.id, dateFormatee, dateFin);

                        majTableViewLivres();
                        afficherMessageInfo("Succès", "Livre emprunté", "Vous avez emprunté le livre " + selectedLivre.titre + " avec succès.");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        historiqueEmprunts.setOnAction(e -> {
            Livre selectedLivre = tableViewLivres.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("empruntsAdminView.fxml"));
            Parent root1 = null;
            try {
                root1 = (Parent) fxmlLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            empruntsAdminController = fxmlLoader.getController();
            try {
                empruntsAdminController.initalizeTableViewEmprunts();
                empruntsAdminController.majTableViewEmprunts(selectedLivre);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            stage.setTitle("Historique des emprunts du livre n° " + selectedLivre.id);
            Scene scene = new Scene(root1);
            scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();       //open the new stage

        });

        supprimerLivre.setOnAction(e -> {
            Livre selectedLivre = tableViewLivres.getSelectionModel().getSelectedItem();
            try {
                selectedLivre.supprime(c);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            afficherMessageInfo("Succès","Livre supprimé", "Le livre n° "+selectedLivre.id +" a bien été supprimé avec succès.");
            try {
                majTableViewLivres();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });

        // Ferme le menu contextuel
        tableViewLivres.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    if (event.getButton() == MouseButton.PRIMARY) { // Vérifiez que le clic est un clic gauche
                        contextMenu.hide(); // Ferme le menu contextuel
                    }
                });
    }
}
