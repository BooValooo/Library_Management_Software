package com.example.bibliotheque;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpruntsController extends Controller{

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


        // Requête pour récupérer les emprunts
        String query = "SELECT Livre_Id, Date_Début, Date_Limite_Rendu, Rendu, Date_Rendu FROM Emprunt WHERE Utilisateur_Id = ?";
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
        Boolean isEmpruntsEnCoursSelected = empruntsEnCours.isSelected();
        String tousLesEmprunts = "1";
        if (isEmpruntsEnCoursSelected) {tousLesEmprunts = "0";}

        // Requête pour récupérer les emprunts (en cours ou tous)
        String query = "SELECT Livre_Id, Date_Début, Date_Limite_Rendu, Rendu, Date_Rendu FROM Emprunt WHERE Utilisateur_Id = ? AND Rendu <= " + tousLesEmprunts;
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
}



