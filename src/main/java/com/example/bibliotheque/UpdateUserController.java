package com.example.bibliotheque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateUserController extends Controller{
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
    protected TextField tpsEmpruntInput;
    @FXML
    protected TextField nbEmpruntsInput;
    @FXML
    protected ChoiceBox<String> categorieInput;
    private Utilisateur selectedUtilisateur = null;

    protected void setSelectedUtilisateur(Utilisateur u) {selectedUtilisateur=u;}
    protected void setUtilisateursController(UtilisateursController u) {utilisateursController=u;}
    protected void setStage(Stage s) {stage=s;}

    protected void initializeInput() {
        nomInput.setText(selectedUtilisateur.getNom());
        prenomInput.setText(selectedUtilisateur.getPrenom());
        mailInput.setText(selectedUtilisateur.getMail());
        telephoneInput.setText(selectedUtilisateur.getTelephone());
        tpsEmpruntInput.setText(selectedUtilisateur.getDureeMaxEmprunt().toString());
        nbEmpruntsInput.setText(selectedUtilisateur.getNombreMaxEmprunt().toString());

        // Configuration des choix de la ChoiceBox
        ObservableList<String> categories = FXCollections.observableArrayList("Bibliothécaire", "Client"); // Raccourci pas fou, il faudrait aller chercher dans la BDD les différentes catégories pour faire ça proprement
        categorieInput.setItems(categories);

        // Préselection d'une valeur
        categorieInput.setValue(selectedUtilisateur.getCategorie());
    }

    @FXML
    protected void onValidationClick() throws SQLException {
        String nom = nomInput.getText();
        String prenom = prenomInput.getText();
        String mail = mailInput.getText();
        String tel = telephoneInput.getText();
        int tpsEmprunt = Integer.valueOf(tpsEmpruntInput.getText());  // Manque de robustesse
        int nombreEmprunt = Integer.valueOf(nbEmpruntsInput.getText());
        String categorie = categorieInput.getValue();
        int categorieId = 2;

        if (categorie.equals("Bibliothécaire")) {categorieId=1;} // Pareil, raccourci pas fou, mais ça a le mérite d'être rapide et de m'éviter des requêtes SQL

        Utilisateur utilisateurAJour = new Utilisateur();
        utilisateurAJour.setId(selectedUtilisateur.id);
        utilisateurAJour.setNom(nom);
        utilisateurAJour.setPrenom(prenom);
        utilisateurAJour.setMail(mail);
        utilisateurAJour.setTelephone(tel);
        utilisateurAJour.setDureeMaxEmprunt(tpsEmprunt);
        utilisateurAJour.setNombreMaxEmprunt(nombreEmprunt);
        utilisateurAJour.setCategorieId(categorieId);

        int succes = utilisateurAJour.setUser(c);
        if (succes>0) {afficherMessageInfo("Succès","Profil mis à jour", "Le profil de l'utilisateur " + utilisateurAJour.id + " a bien été mis à jour");}
        else {afficherMessageErreur("Echec","Profil non mis à jour", "Le profil de l'utilisateur n'a pas pu être mis à jour. Avez vous rempli correctement toutes les informations nécessaires ?");}
        utilisateursController.majTableViewUtilisateurs();
        stage.close();
    }
}
