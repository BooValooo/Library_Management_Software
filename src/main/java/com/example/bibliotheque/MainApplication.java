package com.example.bibliotheque;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.example.bibliotheque.Identification.getSHA;
import static com.example.bibliotheque.Identification.toHexString;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, NoSuchAlgorithmException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("authView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 400);
        scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
        stage.setTitle("Authentification");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {launch();
    }
}