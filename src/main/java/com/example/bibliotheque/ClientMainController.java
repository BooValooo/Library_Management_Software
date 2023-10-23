package com.example.bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientMainController extends Controller{
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}