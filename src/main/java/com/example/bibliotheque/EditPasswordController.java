package com.example.bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static com.example.bibliotheque.Identification.getSHA;
import static com.example.bibliotheque.Identification.toHexString;

public class EditPasswordController extends Controller{

    @FXML
    private PasswordField currentMdpInput;
    @FXML
    private PasswordField newMdpInput;
    @FXML
    private PasswordField newMdpInputTwo;

    @FXML
    protected void onValidationClick() throws NoSuchAlgorithmException, SQLException {
        Identification cred = new Identification();
        cred.utilisateurId = utilisateurId;
        cred.hashMdp = currentMdpInput.getText();
        String hash = toHexString(getSHA(cred.hashMdp));
        String mdp1 = newMdpInput.getText();
        String mdp2 = newMdpInputTwo.getText();
        if (cred.getMdp(c).equals(hash) && mdp1.equals(mdp2)) {
            cred.setMdpBdd(c,mdp1);
            afficherMessageInfo("Succès","Mot de passe mis à jour", "Votre mot de passe a bien été mis à jour avec succès.");
        ;}
        else if (mdp1.equals(mdp2)){
            afficherMessageErreur("Erreur","Mot de passe non modifié","Votre mot de passe n'a pas pu être modifié. Le mot de passe actuel que vous avez saisi est-il correct ?");
        }
        else {
            afficherMessageErreur("Erreur", "Mot de passe non modifié", "Les deux saisies de votre nouveau mot de passe sont-elles identiques ?");
        }
    }
}
