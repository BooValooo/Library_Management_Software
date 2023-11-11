package com.example.bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddUserController extends Controller{
    UtilisateursController utilisateursController = null;
    Stage stage = null;
    @FXML
    protected TextField nomInput;
    @FXML
    protected TextField prenomInput;
    @FXML
    protected TextField mailInput;
    @FXML
    protected TextField telephoneInput;
    @FXML
    protected ChoiceBox<String> categorieInput;

    // Configurer les choix de la ChoiceBox
    protected void initializeCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList("Bibliothécaire", "Client");
        categorieInput.setItems(categories);
    }

    protected void setUtilisateursController(UtilisateursController u) {
        utilisateursController = u;
    }

    protected void setStage(Stage s) {
        stage = s;
    }

    @FXML
    // Permet de créer un nouvel utilisateur dont le nombre max d'emprunts et la durée des emprunts sont ceux de la catégorie sélectionnée.
    protected void onValidationClick() throws SQLException {
        String nom = nomInput.getText();
        String prenom = prenomInput.getText();
        String mail = mailInput.getText();
        String tel = telephoneInput.getText();
        String categorie = categorieInput.getValue();
        int categorieId = 2;

        if (categorie.equals("Bibliothécaire")) {
            categorieId = 1;
        }

        Categorie cat = new Categorie();
        cat.setId(categorieId);

        int tpsEmprunt = cat.getDuréeMaxEmpruntRef(c);
        int nombreEmprunt = cat.getNombreMaxEmpruntRef(c);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setMail(mail);
        utilisateur.setTelephone(tel);
        utilisateur.setDureeMaxEmprunt(tpsEmprunt);
        utilisateur.setNombreMaxEmprunt(nombreEmprunt);
        utilisateur.setCategorieId(categorieId);

        utilisateur.addUser(c);
        afficherMessageInfo("Succès","Profil créé",  "Un nouveau profil vient bien d'être créé.");
        utilisateursController.majTableViewUtilisateurs();
        stage.close();
    }
}

