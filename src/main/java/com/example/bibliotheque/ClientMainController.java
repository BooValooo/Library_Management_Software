package com.example.bibliotheque;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientMainController extends Controller {
    private Stage stage = new Stage();
    @FXML
    private MenuItem closeMenu;
    @FXML
    private MenuItem logOutMenu;
    @FXML
    private VBox vbox;
    @FXML
    protected TableView<Livre> tableViewLivres;
    private AuthController authController = null;
    private SearchController searchController = null;

    @FXML
    //Initialise la tableView
    protected void initalizeTableView() throws SQLException {
        //Création de colonnes pour la tableView
        TableColumn<Livre, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitre()));

        TableColumn<Livre, String> auteursCol = new TableColumn<>("Auteurs");
        auteursCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuteursAsString())); // Utilisation d'une méthode pour obtenir la représentation en String des auteurs

        TableColumn<Livre, Integer> anneeEditionCol = new TableColumn<>("Année d'édition");
        anneeEditionCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAnneeEdition()));

        TableColumn<Livre, String> motCle1Col = new TableColumn<>("Genre");
        motCle1Col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMotCle1()));

        TableColumn<Livre, String> editeurCol = new TableColumn<>("Editeur");
        editeurCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEditeur()));

        // Ajout des colonnes à la TableView
        tableViewLivres.getColumns().addAll(titreCol, auteursCol, anneeEditionCol, motCle1Col, editeurCol);


        // Requête pour récupérer les livres
        String query = "SELECT ISBN, Titre, Année_Edition, Mot_Clé_1, Editeur FROM Edition";
        ResultSet resultSet = c.createStatement().executeQuery(query);

        List<Livre> livres = new ArrayList<>();

        // Création de la liste de livres à partir du résultat de la requête
        while (resultSet.next()) {
            Livre livre = new Livre();
            livre.setTitre(resultSet.getString("titre"));
            livre.setIsbn(resultSet.getInt("ISBN"));
            livre.setMotCle1(resultSet.getString("Mot_Clé_1"));
            livre.setAnneeEdition(resultSet.getInt("Année_Edition"));
            livre.setEditeur(resultSet.getString("Editeur"));
            livre.setAuteurs(livre.getAuthors(c));


            livres.add(livre);
        }

        // Création d'une ObservableList à partir de la liste de livres
        ObservableList<Livre> livresObservable = FXCollections.observableArrayList(livres);

        // MàJ de la tableView
        tableViewLivres.setItems(livresObservable);
    }
    @FXML
    //Affiche les livres de la bibliothèque
    protected void onTestClick() throws SQLException {

            // Requête pour récupérer tous les livres
            String query = "SELECT ISBN, Titre, Année_Edition, Mot_Clé_1, Editeur FROM Edition";
            ResultSet resultSet = c.createStatement().executeQuery(query);

            List<Livre> livres = new ArrayList<>();

            // Création de la liste de livres à partir du résultat de la requête
            while (resultSet.next()) {
                Livre livre = new Livre();
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

            // MàJ de la tableView
            tableViewLivres.setItems(livresObservable);

    }

    //Affiche les livres qui sont le résultat d'une recherche (voir SearchController)
    protected void majLivres(ObservableList<Livre> livresObservable) {
        tableViewLivres.setItems(livresObservable);
    }

    @FXML
    //Ferme l'application
    protected void onCloseClick() {
        System.exit(0);
    }
    @FXML
    //Déconnecte l'utilisateur et retourne sur la page d'authentification
    protected void onLogOutClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("authView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        authController = fxmlLoader.getController();
        stage.setTitle("Authentification");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage


        Stage currentStage = (Stage) vbox.getScene().getWindow();
        currentStage.close();          //close the current stage
    }

    @FXML
    //Ouvre une vue de recherche de livre, ferme la vue actuelle
    protected void onSearchClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        searchController = fxmlLoader.getController();
        searchController.mainController = this;
        stage.setTitle("Recherche d'un ouvrage");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage


        /*
        Stage currentStage = (Stage) vbox.getScene().getWindow();
        currentStage.close();          //close the current stage
         */
    }
}






