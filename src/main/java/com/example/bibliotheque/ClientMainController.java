package com.example.bibliotheque;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMainController extends Controller {
    private Stage stage = new Stage();
    @FXML
    private MenuItem closeMenu;
    @FXML
    private MenuItem logOutMenu;
    @FXML
    private Button findBookButton;
    @FXML
    private VBox vbox;
    @FXML
    private Button testButton;
    private AuthController authController = null;
    private SearchController searchController = null;


    @FXML
    protected void onCloseClick() {
        System.exit(0);
    }
    @FXML
    protected void onLogOutClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("authView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        authController = fxmlLoader.getController();
        stage.setTitle("Authentification");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage


        Stage currentStage = (Stage) findBookButton.getScene().getWindow();
        currentStage.close();          //close the current stage
    }

    @FXML
    protected void onSearchClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        searchController = fxmlLoader.getController();
        stage.setTitle("Recherche d'un ouvrage");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage


        Stage currentStage = (Stage) findBookButton.getScene().getWindow();
        currentStage.close();          //close the current stage
    }

    @FXML
    protected void onTestClick() {
        Edition ed = new Edition();
        ed.isbn=123456;
        System.out.println(ed.getAuthors(c));
    }

    }


