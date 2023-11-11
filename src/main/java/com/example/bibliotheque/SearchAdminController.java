package com.example.bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchAdminController extends SearchController{
    @FXML
    private CheckBox livresEnRetard;

    @FXML @Override
    protected void onRechercherClick() throws SQLException {
        String titre = titreInput.getText();
        String auteur = auteurInput.getText();
        String genre = genreInput.getText();
        Boolean isLivresDisponiblesSelected = livresDisponibles.isSelected();
        Boolean isLivresEnRetardSelected = livresEnRetard.isSelected();
        String livresDispos = "0";
        ResultSet resultSet = null;
        if (isLivresDisponiblesSelected) {livresDispos = "1";}


        // Requête pour récupérer tous les livres respectant la recherche
        if (!isLivresEnRetardSelected) {
            String query = "SELECT DISTINCT e.ISBN, e.Titre, e.Année_Edition, e.Mot_Clé_1, e.Editeur, l.Disponible, l.Id, l.Année_Première_Parution FROM Edition AS e " +
                    "JOIN Association_Auteurs_Edition AS aae ON e.ISBN = aae.ISBN " +
                    "JOIN Auteur AS a ON aae.Auteur_Id = a.Id " +
                    "JOIN Livre AS l ON l.ISBN = e.ISBN " +
                    "WHERE ((e.Titre LIKE '%" + titre + "%') AND (a.Nom LIKE '%" + auteur + "%') AND (l.Disponible >= " + livresDispos + ") AND (e.Mot_Clé_1 LIKE '%" + genre + "%' OR e.Mot_Clé_2 LIKE '%" + genre + "%' OR e.Mot_Clé_3 LIKE '%" + genre + "%' OR e.Mot_Clé_4 LIKE '%" + genre + "%' OR e.Mot_Clé_5 LIKE '%" + genre + "%'))";
            resultSet = c.createStatement().executeQuery(query);
        }

        else {
            String query = "SELECT DISTINCT e.ISBN, e.Titre, e.Année_Edition, e.Mot_Clé_1, e.Editeur, l.Disponible, l.Id, l.Année_Première_Parution FROM Edition AS e " +
                    "JOIN Association_Auteurs_Edition AS aae ON e.ISBN = aae.ISBN " +
                    "JOIN Auteur AS a ON aae.Auteur_Id = a.Id " +
                    "JOIN Livre AS l ON l.ISBN = e.ISBN " +
                    "JOIN Emprunt AS em ON l.Id = em.Livre_Id "+
                    "WHERE ((em.Rendu = 0) AND (? > em.Date_Limite_Rendu) AND (e.Titre LIKE '%" + titre + "%') AND (a.Nom LIKE '%" + auteur + "%') AND (l.Disponible >= " + livresDispos + ") AND (e.Mot_Clé_1 LIKE '%" + genre + "%' OR e.Mot_Clé_2 LIKE '%" + genre + "%' OR e.Mot_Clé_3 LIKE '%" + genre + "%' OR e.Mot_Clé_4 LIKE '%" + genre + "%' OR e.Mot_Clé_5 LIKE '%" + genre + "%'))";
            PreparedStatement prepStmt = c.prepareStatement(query);

            // Obtenir la date du jour
            LocalDate dateDuJour = LocalDate.now();

            // Formater la date en "aaaa-MM-jj"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            String dateFormatee = dateDuJour.format(formatter);

            prepStmt.setString(1,dateFormatee);
            resultSet = prepStmt.executeQuery();
        }

        List<Livre> livres = new ArrayList<>();

        // Création de la liste de livres à partir du résultat de la requête
        while (resultSet.next()) {
            Livre livre = new Livre();
            livre.setIsbn(resultSet.getInt("ISBN"));
            livre.setId(resultSet.getInt("Id"));
            livre.setDisponible(resultSet.getBoolean("Disponible"));
            livre.setTitre(resultSet.getString("Titre"));
            livre.setIsbn(resultSet.getInt("ISBN"));
            livre.setMotCle1(resultSet.getString("Mot_Clé_1"));
            livre.setAnneeEdition(resultSet.getInt("Année_Edition"));
            livre.setEditeur(resultSet.getString("Editeur"));
            livre.setAnneePremiereParution(resultSet.getInt("Année_Première_Parution"));
            livre.setAuteurs(livre.getAuthors(c));


            livres.add(livre);
        }

        // Création d'une ObservableList à partir de la liste de livres
        ObservableList<Livre> livresObservable = FXCollections.observableArrayList(livres);

        mainController.tableViewLivres.setItems(livresObservable);

    }
}
