package com.example.bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpruntsAdminController extends EmpruntsController{

    // Met à jour la tableView des emprunts d'un utilisateur donné
    protected void majTableViewEmprunts(Utilisateur user) throws SQLException {

        // Requête pour récupérer les emprunts (en cours ou tous)
        String query = "SELECT e.Id, e.Utilisateur_Id, e.Livre_Id, e.Date_Début, e.Date_Limite_Rendu, e.Rendu, e.Date_Rendu, l.ISBN FROM Emprunt AS e " +
                "JOIN Livre AS l ON l.Id = e.Livre_Id WHERE e.Utilisateur_Id = ? ";
        PreparedStatement prep_stmt = c.prepareStatement(query);
        prep_stmt.setInt(1, user.id);
        ResultSet resultSet = prep_stmt.executeQuery();

        // Création de la liste d'emprunts à partir du résultat de la requête
        List<Emprunt> emprunts = new ArrayList<>();

        while (resultSet.next()) {
            Emprunt emprunt = new Emprunt();
            Livre livre = new Livre();
            livre.setId(resultSet.getInt("Livre_Id"));
            livre.setTitre(livre.getTitre(c));
            emprunt.setId(resultSet.getInt("Id"));
            emprunt.setUtilisateurId(resultSet.getInt("Utilisateur_Id"));
            emprunt.setIsbn(resultSet.getInt("ISBN"));
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

    // Met à jour la tableView des emprunts d'un livre donné
    protected void majTableViewEmprunts(Livre livreSelectionne) throws SQLException {

        // Requête pour récupérer les emprunts (en cours ou tous)
        String query = "SELECT e.Id, e.Utilisateur_Id, e.Livre_Id, e.Date_Début, e.Date_Limite_Rendu, e.Rendu, e.Date_Rendu, l.ISBN FROM Emprunt AS e " +
                "JOIN Livre AS l ON l.Id = e.Livre_Id WHERE l.Id = ? ";
        PreparedStatement prep_stmt = c.prepareStatement(query);
        prep_stmt.setInt(1, livreSelectionne.id);
        ResultSet resultSet = prep_stmt.executeQuery();

        // Création de la liste d'emprunts à partir du résultat de la requête
        List<Emprunt> emprunts = new ArrayList<>();

        while (resultSet.next()) {
            Emprunt emprunt = new Emprunt();
            Livre livre = new Livre();
            livre.setId(resultSet.getInt("Livre_Id"));
            livre.setTitre(livre.getTitre(c));
            emprunt.setId(resultSet.getInt("Id"));
            emprunt.setIsbn(resultSet.getInt("ISBN"));
            emprunt.setUtilisateurId(resultSet.getInt("Utilisateur_Id"));
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
}
