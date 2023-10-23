package com.example.bibliotheque;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.bibliotheque.Identification.getSHA;
import static com.example.bibliotheque.Identification.toHexString;

public class AuthController extends Controller{
    private Stage stage = new Stage();
    @FXML
    private TextField utilisateurIdInput;
    @FXML
    private PasswordField utilisateurMdpInput;
    @FXML
    private Button validerButton;
    private ClientMainController mainController = null;



    @FXML
    protected void onValidationClick() throws NoSuchAlgorithmException {
        Identification cred = new Identification();
        cred.utilisateurMail = utilisateurIdInput.getText();
        cred.hashMdp = utilisateurMdpInput.getText();
        String hash = toHexString(getSHA(cred.hashMdp));
        if (hash.equals(cred.getMdp(cred.utilisateurMail, c))) {
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientMainView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                mainController = fxmlLoader.getController();
                stage.setTitle("Scène 1");
                stage.setScene(new Scene(root1));
                stage.show();       //open the new stage


                Stage currentStage = (Stage) validerButton.getScene().getWindow();
                currentStage.close();          //close the current stage

            }
            catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Authentification échouée");
        }
    }
}
