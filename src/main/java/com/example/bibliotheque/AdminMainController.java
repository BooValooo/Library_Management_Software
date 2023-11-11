package com.example.bibliotheque;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMainController extends ClientMainController{
    private SearchAdminController searchAdminController = null;






    /* Menu Rechercher */
    @FXML @Override
    //Ouvre une vue de recherche de livre
    protected void onSearchClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchAdminView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        searchAdminController = fxmlLoader.getController();
        searchAdminController.mainController = this;
        stage.setTitle("Recherche d'un ouvrage");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();       //open the new stage

    }
}
