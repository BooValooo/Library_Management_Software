package com.example.bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Pour afficher les utilisateurs souhaités
public class SearchUserController extends Controller{

    @FXML
    protected TextField idInput;
    @FXML
    protected TextField nomInput;
    @FXML
    protected TextField prenomInput;
    protected UtilisateursController utilisateursController;

    protected void setUtilisateursController(UtilisateursController u) {
        utilisateursController = u;
    }
    @FXML
    protected void onRechercherClick() throws SQLException {
        String idString = (idInput.getText());
        String nom = nomInput.getText();
        String prenom = prenomInput.getText();
        ResultSet resultSet = null;

        // Requête pour récupérer tous les utilisateurs respectant la recherche
        if (idString.equals("")) {
            String query = "SELECT * FROM Utilisateur WHERE (Nom LIKE '%" + nom + "%') AND (Prénom LIKE '%" + prenom + "%')";
            PreparedStatement prepStmt = c.prepareStatement(query);
            resultSet = prepStmt.executeQuery();
        }
        else {
            String query = "SELECT * FROM Utilisateur WHERE (Id = ?)";
            PreparedStatement prepStmt = c.prepareStatement(query);
            int id = Integer.valueOf(idString);
            prepStmt.setInt(1, id);
            resultSet = prepStmt.executeQuery();
        }

        List<Utilisateur> utilisateurs = new ArrayList<>();

        // Création de la liste d'utilisateurs à partir du résultat de la requête
        while (resultSet.next()) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(resultSet.getInt("Id"));
            utilisateur.setNom(resultSet.getString("Nom"));
            utilisateur.setPrenom(resultSet.getString("Prénom"));
            utilisateur.setMail(resultSet.getString("Mail"));
            utilisateur.setTelephone(resultSet.getString("Téléphone"));
            utilisateur.setCategorieId(resultSet.getInt("Catégorie_Id"));
            utilisateur.setCategorie(utilisateur.getCategorieName(c));
            utilisateur.setNombreMaxEmprunt(resultSet.getInt("Nombre_Maximal_Emprunt"));
            utilisateur.setDureeMaxEmprunt(resultSet.getInt("Durée_Maximale_Emprunt"));

            utilisateurs.add(utilisateur);
        }

        // Création d'une ObservableList à partir de la liste d'utilisateurs
        ObservableList<Utilisateur> livresObservable = FXCollections.observableArrayList(utilisateurs);

        // MàJ de la tableView
        utilisateursController.tableViewUtilisateurs.setItems(livresObservable);
    }
}
