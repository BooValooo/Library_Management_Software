package com.example.bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchController extends Controller {
    @FXML
    private Button rechercherButton;
    @FXML
    private TextField titreInput;
    @FXML
    private TextField auteurInput;
    @FXML
    private TextField genreInput;
    @FXML
    private CheckBox livresDisponibles;
    protected ClientMainController mainController;

    @FXML
    protected void onRechercherClick() throws SQLException {
        String titre = titreInput.getText();
        String auteur = auteurInput.getText();
        String genre = genreInput.getText();
        Boolean isLivresDisponiblesSelected = livresDisponibles.isSelected();
        String livresDispos = "0";
        if (isLivresDisponiblesSelected) {livresDispos = "1";}


        // Requête pour récupérer tous les livres respectant la recherche
        String query = "SELECT DISTINCT e.ISBN, e.Titre, e.Année_Edition, e.Mot_Clé_1, e.Editeur, l.Disponible, l.Id FROM Edition AS e " +
                "JOIN Association_Auteurs_Edition AS aae ON e.ISBN = aae.ISBN " +
                "JOIN Auteur AS a ON aae.Auteur_Id = a.Id " +
                "JOIN Livre AS l ON l.ISBN = e.ISBN " +
                "WHERE ((e.Titre LIKE '%" + titre +"%') AND (a.Nom LIKE '%" +auteur+ "%') AND (l.Disponible >= " + livresDispos + ") AND (e.Mot_Clé_1 LIKE '%" +genre+ "%' OR e.Mot_Clé_2 LIKE '%"+genre+"%' OR e.Mot_Clé_3 LIKE '%" +genre+ "%' OR e.Mot_Clé_4 LIKE '%" +genre+ "%' OR e.Mot_Clé_5 LIKE '%" +genre+ "%'))";
        ResultSet resultSet = c.createStatement().executeQuery(query);

        List<Livre> livres = new ArrayList<>();

        // Création de la liste de livres à partir du résultat de la requête
        while (resultSet.next()) {
            Livre livre = new Livre();
            livre.setId(resultSet.getInt("Id"));
            livre.setDisponible(resultSet.getBoolean("Disponible"));
            livre.setTitre(resultSet.getString("Titre"));
            livre.setIsbn(resultSet.getInt("ISBN"));
            livre.setMotCle1(resultSet.getString("Mot_Clé_1"));
            livre.setAnneeEdition(resultSet.getInt("Année_Edition"));
            livre.setEditeur(resultSet.getString("Editeur"));
            livre.setAuteurs(livre.getAuthors(c));


            livres.add(livre);
        }

        // Création d'une ObservableList à partir de la liste de livres
        ObservableList<Livre> livresObservable = FXCollections.observableArrayList(livres);

        mainController.tableViewLivres.setItems(livresObservable);
    }
}
