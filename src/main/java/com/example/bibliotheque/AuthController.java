package com.example.bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthController {
    private Stage stage = new Stage();
    @FXML
    private TextField utilisateurIdInput;
    @FXML
    private PasswordField utilisateurMdpInput;
    @FXML
    private Button validerButton;


    @FXML
    protected void onValidationClick() {
        Identification cred = new Identification();
        cred.utilisateurId = Integer.valueOf(utilisateurIdInput.getText());
        cred.hashMdp = utilisateurMdpInput.getText();
        if (cred.hashMdp == cred.getMdp(cred.utilisateurId)) {
            System.out.println("Authentification réussie");
        } else {
            System.out.println("Authentification échouée");
        }
    }
}
