package com.example.bibliotheque;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateursController extends Controller{
    @FXML
    TableView<Utilisateur> tableViewUtilisateurs ;
    private SearchUserController searchUserController;
    private UpdateUserController updateUserController;
    private AddUserController addUserController;
    private EmpruntsAdminController empruntsAdminController;
    private Stage stage = new Stage();

    // Initialise l'affichage des utilisateurs.
    protected void initializeTableViewUtilisateurs() throws SQLException {
        //Création de colonnes pour la tableView
        TableColumn<Utilisateur, Integer> idCol = new TableColumn<>("Id");
        idCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));

        TableColumn<Utilisateur, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));

        TableColumn<Utilisateur, String> mailCol = new TableColumn<>("Mail");
        mailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMail()));

        TableColumn<Utilisateur, String> telCol = new TableColumn<>("Tél");
        telCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));

        TableColumn<Utilisateur, String> catCol = new TableColumn<>("Catégorie");
        catCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategorie()));

        TableColumn<Utilisateur, Integer> nbEmpruntsCol = new TableColumn<>("Nombre d'emprunts en cours");
        nbEmpruntsCol.setCellValueFactory(data -> {
            try {
                return new SimpleObjectProperty<>(data.getValue().getNombreEmprunts(c));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        TableColumn<Utilisateur, Integer> tpsEmpruntCol = new TableColumn<>("Durée maximale d'emprunt");
        tpsEmpruntCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDureeMaxEmprunt()));

        TableColumn<Utilisateur, Integer> maxEmpruntsCol = new TableColumn<>("Nombre maximal d'emprunts");
        maxEmpruntsCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNombreMaxEmprunt()));

        TableColumn<Utilisateur, String> listeRougeCol = new TableColumn<>("Liste rouge");
        listeRougeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getListeRouge()));

        TableColumn<Utilisateur, String> dateListeRougeCol = new TableColumn<>("Date de radiation");
        dateListeRougeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDateListeRouge()));


        // Ajout des colonnes à la TableView
        tableViewUtilisateurs.getColumns().addAll(idCol, nomCol, prenomCol, mailCol, telCol, catCol, nbEmpruntsCol, maxEmpruntsCol, tpsEmpruntCol, listeRougeCol, dateListeRougeCol);

        majTableViewUtilisateurs();
    }

    // Met à jour l'affichage
    protected void majTableViewUtilisateurs() throws SQLException {
        // Requête pour récupérer les utilisateurs
        String query = "SELECT * FROM Utilisateur";
        ResultSet resultSet = c.createStatement().executeQuery(query);

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
            utilisateur.setDateListeRouge(utilisateur.getDateListeRouge(c));
            if (utilisateur.getDateListeRouge().equals("")) {
                utilisateur.setListeRouge("non");
            }
            else {utilisateur.setListeRouge("oui");}

            utilisateurs.add(utilisateur);
        }

        // Création d'une ObservableList à partir de la liste d'utilisateurs
        ObservableList<Utilisateur> livresObservable = FXCollections.observableArrayList(utilisateurs);

        // MàJ de la tableView
        tableViewUtilisateurs.setItems(livresObservable);
    }

    // Ouvre un menu contextuel qui permet de modifier le profil d'un utilisateur ou de le blacklister
    @FXML
    protected void onUtilisateurClick() {

        // Crée un menu contextuel
        ContextMenu contextMenu = new ContextMenu();
        MenuItem modifierProfil = new MenuItem("Modifier le profil");
        MenuItem listeRouge = new MenuItem("Mettre sur liste rouge");
        MenuItem historiqueEmprunts = new MenuItem("Historique des emprunts");
        contextMenu.getItems().addAll(modifierProfil,listeRouge,historiqueEmprunts);

        // Définit un gestionnaire d'événements pour afficher le menu contextuel lors du clic droit
        tableViewUtilisateurs.setOnContextMenuRequested(event -> {
            contextMenu.show(tableViewUtilisateurs, event.getScreenX(), event.getScreenY());
        });

        // Associe des actions aux éléments du menu
        modifierProfil.setOnAction(e -> {
            Utilisateur selectedUtilisateur = tableViewUtilisateurs.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("updateUserView.fxml"));
            Parent root1 = null;
            try {
                root1 = (Parent) fxmlLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            updateUserController = fxmlLoader.getController();
            updateUserController.setUtilisateursController(this);
            updateUserController.setSelectedUtilisateur(selectedUtilisateur);
            updateUserController.initializeInput();
            updateUserController.setStage(stage);
            stage.setTitle("Modification du profil d'un utilisateur");
            Scene scene = new Scene(root1);
            scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();       //open the new stage
        });

        listeRouge.setOnAction(e -> {
            Utilisateur selectedUtilisateur = tableViewUtilisateurs.getSelectionModel().getSelectedItem();
            try {
                selectedUtilisateur.addListeRouge(c);
                afficherMessageInfo("Succès","Utilisateur radié", "L'utilisateur " + selectedUtilisateur.id + " a bien été radié.");
                majTableViewUtilisateurs();
            }
            catch (SQLException ex) {
                afficherMessageErreur("Erreur","Utilisateur non radié","L'utilisateur sélectionné n'a pas pu être radié.");
            }

        });

        historiqueEmprunts.setOnAction(e -> {
            Utilisateur selectedUtilisateur = tableViewUtilisateurs.getSelectionModel().getSelectedItem();
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
                empruntsAdminController.majTableViewEmprunts(selectedUtilisateur);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            stage.setTitle("Historique des emprunts de l'utilisateur n° " + selectedUtilisateur.id);
            Scene scene = new Scene(root1);
            scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();       //open the new stage

        });

        // Ferme le menu contextuel
        tableViewUtilisateurs.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    if (event.getButton() == MouseButton.PRIMARY) { // Vérifiez que le clic est un clic gauche
                        contextMenu.hide(); // Ferme le menu contextuel
                    }
                });
    }

    /* Menu RECHERCHER */

    @FXML
    protected void onRechercherClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchUserView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        searchUserController = fxmlLoader.getController();
        searchUserController.setUtilisateursController(this);
        stage.setTitle("Recherche d'un utilisateur");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage
    }


    /* Menu LISTE ROUGE */

    @FXML
    protected void onListeRougeClick() {
        afficherMessageInfo("Notice","Radier un utilisateur","Pour radier un utilisateur, réalisez un clic droit sur son nom sur la page Utilisateurs de l'application, puis sélectionnez 'Mettre sur liste rouge'.");
    }

    /* Menu AJOUTER ET MODIFIER */

    @FXML
    protected void onAjouterClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addUserView.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        addUserController = fxmlLoader.getController();
        addUserController.setUtilisateursController(this);
        addUserController.setStage(stage);
        addUserController.initializeCategories();
        stage.setTitle("Création d'un utilisateur");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage
    }

    @FXML
    protected void onModifierClick() {
        afficherMessageInfo("Notice","Modifier le profil d'un utilisateur","Pour modifier le profil d'un utilisateur, réalisez un clic droit sur son nom sur la page Utilisateurs de l'application, puis sélectionnez 'Modifier le profil'.");
    }

    @FXML
    protected void onHistoriqueClick() {
        afficherMessageInfo("Notice","Accéder à l'historique des emprunts d'un utilisateur","Pour Accéder à l'historique des emprunts d'un utilisateur, réalisez un clic droit sur son nom sur la page Utilisateurs de l'application, puis sélectionnez 'Historique des emprunts'.");
    }
}
