package com.example.bibliotheque;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EmpruntsController extends Controller {

    private Stage stage = new Stage();
    @FXML
    private CheckBox empruntsEnCours;
    @FXML
    private TableView<Emprunt> tableViewEmprunts;


    protected void initalizeTableViewEmprunts() throws SQLException {
        //Création de colonnes pour la tableView
        TableColumn<Emprunt, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitre()));

        TableColumn<Emprunt, String> dateCol = new TableColumn<>("Date de l'emprunt");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateDebut()));

        TableColumn<Emprunt, String> dateLimiteCol = new TableColumn<>("Date limite de retour");
        dateLimiteCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDateFinPrevue()));

        TableColumn<Emprunt, String> renduCol = new TableColumn<>("Rendu");
        renduCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRenduString()));

        TableColumn<Emprunt, String> dateRetourCol = new TableColumn<>("Date de retour (si rendu)");
        dateRetourCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateFinReelle()));


        // Ajout des colonnes à la TableView
        tableViewEmprunts.getColumns().addAll(titreCol, dateCol, dateLimiteCol, renduCol, dateRetourCol);


        majTableViewEmprunts();
    }

    // Mise à jour de la tableView
    protected void majTableViewEmprunts() throws SQLException {
        Boolean isEmpruntsEnCoursSelected = empruntsEnCours.isSelected();
        String tousLesEmprunts = "1";
        if (isEmpruntsEnCoursSelected) {
            tousLesEmprunts = "0";
        }

        // Requête pour récupérer les emprunts (en cours ou tous)
        String query = "SELECT Id, Livre_Id, Date_Début, Date_Limite_Rendu, Rendu, Date_Rendu FROM Emprunt WHERE Utilisateur_Id = ? AND Rendu <= " + tousLesEmprunts;
        PreparedStatement prep_stmt = c.prepareStatement(query);
        prep_stmt.setInt(1, this.utilisateurId);
        ResultSet resultSet = prep_stmt.executeQuery();

        // Création de la liste d'emprunts à partir du résultat de la requête
        List<Emprunt> emprunts = new ArrayList<>();

        while (resultSet.next()) {
            Emprunt emprunt = new Emprunt();
            Livre livre = new Livre();
            livre.setId(resultSet.getInt("Livre_Id"));
            livre.setTitre(livre.getTitre(c));
            emprunt.setId(resultSet.getInt("Id"));
            emprunt.setLivreId(resultSet.getInt("Livre_Id"));
            emprunt.setTitre(livre.getTitre());
            emprunt.setDateDebut(resultSet.getString("Date_Début"));
            emprunt.setDateFinPrevue(resultSet.getString("Date_Limite_Rendu"));
            emprunt.setRendu(resultSet.getInt("Rendu"));
            emprunt.setDateFinReelle(resultSet.getString("Date_Rendu"));

            emprunts.add(emprunt);
        }

        // Création d'une ObservableList à partir de la liste d'emprunts
        ObservableList<Emprunt> empruntsObservable = FXCollections.observableArrayList(emprunts);

        // MàJ de la tableView
        tableViewEmprunts.setItems(empruntsObservable);
    }

    @FXML
    protected void onTousLesEmpruntsClick() throws SQLException {
        majTableViewEmprunts();
    }

    // Ouvre un menu contextuel qui permet de rendre un livre ou de déclarer une perte
    @FXML
    protected void retourClick() {

        // Crée un menu contextuel
        ContextMenu contextMenu = new ContextMenu();
        MenuItem rendreLivre = new MenuItem("Rendre");
        MenuItem perdreLivre = new MenuItem("Déclarer perdu");
        contextMenu.getItems().addAll(rendreLivre, perdreLivre);

        // Définit un gestionnaire d'événements pour afficher le menu contextuel lors du clic droit
        tableViewEmprunts.setOnContextMenuRequested(event -> {
            contextMenu.show(tableViewEmprunts, event.getScreenX(), event.getScreenY());
        });

        // Associe des actions aux éléments du menu
        rendreLivre.setOnAction(e -> {
            Emprunt selectedEmprunt = tableViewEmprunts.getSelectionModel().getSelectedItem();
            selectedEmprunt.setUtilisateurId(this.utilisateurId);
            if (selectedEmprunt.rendu) {afficherMessageErreur("Erreur", "Emprunt déjà rendu", "Cet emprunt a déjà été marqué comme rendu.");
                return;}
            else {
                try {
                    selectedEmprunt.retour(c);
                    majTableViewEmprunts();
                    afficherMessageInfo("Succès", "Livre Rendu", "Vous avez rendu le livre " + selectedEmprunt.titre + " avec succès.");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        perdreLivre.setOnAction(e -> {
            // A compléter
        });

        // Ferme le menu contextuel
        tableViewEmprunts.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    if (event.getButton() == MouseButton.PRIMARY) { // Vérifiez que le clic est un clic gauche
                        contextMenu.hide(); // Ferme le menu contextuel
                    }
                });
    }

}



