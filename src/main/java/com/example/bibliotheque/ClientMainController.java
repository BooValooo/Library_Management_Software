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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClientMainController extends Controller {
    protected final Stage stage = new Stage();
    @FXML
    private VBox vbox;
    @FXML
    protected TableView<Livre> tableViewLivres;
    private AuthController authController = null;
    private SearchController searchController = null;
    private EmpruntsController empruntsController = null;
    private EditPasswordController editPasswordController = null;

    @FXML
    //Initialise la tableView
    protected void initalizeTableViewLivres() throws SQLException {
        //Création de colonnes pour la tableView
        TableColumn<Livre, Integer> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIsbn()));

        TableColumn<Livre, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitre()));

        TableColumn<Livre, String> auteursCol = new TableColumn<>("Auteurs");
        auteursCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuteursAsString())); // Utilisation d'une méthode pour obtenir la représentation en String des auteurs

        TableColumn<Livre, Integer> anneeEditionCol = new TableColumn<>("Année d'édition");
        anneeEditionCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAnneeEdition()));

        TableColumn<Livre, Integer> anneeParutionCol = new TableColumn<>("Année de première parution");
        anneeParutionCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAnneePremiereParution()));

        TableColumn<Livre, String> motCle1Col = new TableColumn<>("Genre principal"); //Le genre principal correspond au mot clé 1
        motCle1Col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMotCle1()));

        TableColumn<Livre, String> editeurCol = new TableColumn<>("Editeur");
        editeurCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEditeur()));

        TableColumn<Livre,String> dispoCol = new TableColumn<>("Livre disponible");
        dispoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDisponibleString()));


        // Ajout des colonnes à la TableView
        tableViewLivres.getColumns().addAll(isbnCol, titreCol, auteursCol, anneeEditionCol, anneeParutionCol, motCle1Col, editeurCol, dispoCol);

        majTableViewLivres();
    }

    protected void majTableViewLivres() throws SQLException {
        // Requête pour récupérer les livres
        String query = "SELECT l.Id, e.ISBN, e.Titre, e.Année_Edition, e.Mot_Clé_1, e.Editeur, l.Disponible, l.Année_Première_Parution FROM Edition AS e JOIN Livre AS l ON e.ISBN = l.ISBN";
        ResultSet resultSet = c.createStatement().executeQuery(query);

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

        // MàJ de la tableView
        tableViewLivres.setItems(livresObservable);
    }


    /* Menu Profil */
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

        //Ferme la session de manière effective
        Controller.utilisateurId = null;

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
    //Ouvre une nouvelle vue sur laquelle l'utilisateur peut éditer son mot de passe
    protected void  onModifierMdpClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editPasswordView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();

        editPasswordController = fxmlLoader.getController();
        stage.setTitle("Modification du mot de passe");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage
    }

    /* Menu Retour */

    @FXML
    protected void onRetourClick() {
        afficherMessageInfo("Notice","Rendre un livre","Pour rendre un livre, rendez-vous dans le menu Profil -> Consulter les emprunts, puis réaliser un clic droit sur le livre que vous souhaitez rendre.");
    }


    /* Menu Emprunter */

    @FXML
    protected void onEmprunterClick() {
        afficherMessageInfo("Notice","Emprunter un livre","Pour emprunter un livre, Réalisez un clic droit sur son titre sur la page principale de l'application. Vous pouvez rechercher le livre que vous souhaitez ou bien n'afficher que les livres immédiatement disponibles dans le menu Rechercher.");
    }
    @FXML
    //Ouvre une fenêtre permettant à l'utilisateur de consulter ses emprunts en cours et passés
    protected void onEmpruntsClick() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("empruntsView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        empruntsController = fxmlLoader.getController();
        empruntsController.setClientMainController(this);
        empruntsController.initalizeTableViewEmprunts();
        stage.setTitle("Emprunts");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage
    }


    // Permet d'emprunter un livre
    @FXML
    protected void empruntClick() {

        // Crée un menu contextuel
        ContextMenu contextMenu = new ContextMenu();
        MenuItem emprunterLivre = new MenuItem("Emprunter");
        contextMenu.getItems().addAll(emprunterLivre);

        // Définit un gestionnaire d'événements pour afficher le menu contextuel lors du clic droit
        tableViewLivres.setOnContextMenuRequested(event -> {
            contextMenu.show(tableViewLivres, event.getScreenX(), event.getScreenY());
        });

        // Associe des actions aux éléments du menu
        emprunterLivre.setOnAction(e -> {
            Livre selectedLivre = tableViewLivres.getSelectionModel().getSelectedItem();
            Utilisateur user = new Utilisateur();
            user.id = utilisateurId;
            if (!selectedLivre.disponible) {afficherMessageErreur("Erreur", "Livre non disponible", "Ce livre n'est pas disponible actuellement.");
                return;}
            else {
                try {
                    if (user.getNombreEmprunts(c) >= user.getNombreMaxEmprunts(c)) { // Refus si trop grand nombre d'emprunts
                        afficherMessageErreur("Erreur", "Nombre maximal d'emprunts autorisés atteint", "Si vous souhaitez emprunter un nouveau livre, veuillez d'abord en rendre un, ou bien veuillez prendre contact avec un bibliothécaire.");
                        return;
                    }
                    else {
                        // Obtenir la date du jour
                        LocalDate dateDuJour = LocalDate.now();

                        // Formater la date en "aaaa-MM-jj"
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
                        String dateFormatee = dateDuJour.format(formatter);

                        // Obtenir la date de retour
                        String dateFin = user.getDateLimiteEmprunt(c,dateDuJour);

                        selectedLivre.emprunt(c, utilisateurId, selectedLivre.id, dateFormatee, dateFin);

                        majTableViewLivres();
                        afficherMessageInfo("Succès","Livre emprunté", "Vous avez emprunté le livre " + selectedLivre.titre + " avec succès.");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        // Ferme le menu contextuel
        tableViewLivres.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    if (event.getButton() == MouseButton.PRIMARY) { // Vérifiez que le clic est un clic gauche
                        contextMenu.hide(); // Ferme le menu contextuel
                    }
                });
    }

    /* Menu Rechercher */
    @FXML
    //Ouvre une vue de recherche de livre
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

    }
}






