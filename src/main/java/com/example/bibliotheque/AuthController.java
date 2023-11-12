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
    private ClientMainController clientMainController = null;
    private AdminMainController adminMainController = null;



    @FXML
    //Ouverture de la vue principale de la bibliothèque si les identifiants sont corrects (il faudra différencier 2 cas : bibliothécaire et autre)
    protected void onValidationClick() throws NoSuchAlgorithmException, SQLException, IOException {
        Identification cred = new Identification();
        cred.utilisateurMail = utilisateurIdInput.getText();
        cred.hashMdp = utilisateurMdpInput.getText();
        String hash = toHexString(getSHA(cred.hashMdp));
        cred.utilisateurId = cred.getId(c);
        Utilisateur user = new Utilisateur();
        user.setId(cred.utilisateurId);
        if (hash.equals(cred.getMdp(cred.utilisateurMail, c))) {
            if (!user.getDateListeRouge(c).equals("")) {afficherMessageErreur("Erreur","Utilisateur sur liste rouge","Vous avez été radié(e) de cette bibliothèque.");}
            else if (cred.getCategorie(c) == 2) { //Charge la page pour les clients
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientMainView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                clientMainController = fxmlLoader.getController();

                //Permet de créer une session active pour l'utilisateur qui vient de se connecter
                Controller.utilisateurId = cred.utilisateurId;

                //Permet l'affichage des colonnes de la tableView des livres
                clientMainController.initalizeTableViewLivres();

                stage.setTitle("Bienvenue");
                Scene scene = new Scene(root1);
                scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
                stage.setScene(scene);
                stage.show();       //open the new stage

                Stage currentStage = (Stage) validerButton.getScene().getWindow();
                currentStage.close();          //close the current stage
            }
            else if (cred.getCategorie(c) == 1) { //Charge la page pour les admins
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminMainView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                adminMainController = fxmlLoader.getController();

                //Permet de créer une session active pour l'utilisateur qui vient de se connecter
                Controller.utilisateurId = cred.utilisateurId;

                //Permet l'affichage des colonnes de la tableView des livres
                adminMainController.initalizeTableViewLivres();

                stage.setTitle("Bienvenue");
                Scene scene = new Scene(root1);
                scene.getStylesheets().add(MainApplication.class.getResource("styles.css").toExternalForm());
                stage.setScene(scene);
                stage.show();       //open the new stage

                Stage currentStage = (Stage) validerButton.getScene().getWindow();
                currentStage.close();          //close the current stage
            }
        }
         else {
            afficherMessageErreur("Erreur", "Authentification échouée", "Si vous ne vous rappelez plus de vos identifiants, veuillez contacter un bibliothécaire.");
        }
    }
}
