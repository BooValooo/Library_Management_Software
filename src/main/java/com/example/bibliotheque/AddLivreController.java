package com.example.bibliotheque;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.Vector;

// Permet de créer un nouveau livre. N'est pas très robuste (très sensible aux erreurs d'input de l'utilisateur) mais remplit son rôle.
public class AddLivreController extends Controller {
    private AdminMainController adminMainController;
    private Stage stage;
    @FXML
    private TextField titreInput;

    @FXML
    private TextField isbnInput;

    @FXML
    private TextField editeurInput;

    @FXML
    private TextField anneeEditionInput;

    @FXML
    private TextField anneeParutionInput;

    @FXML
    private TextField motsClesInput;

    @FXML
    private TextField prenomsInput;

    @FXML
    private TextField nomsInput;

    @FXML
    private TextField datesNaissanceInput;


    protected void setAdminMainController(AdminMainController a) {
        adminMainController = a;
    }

    protected void setStage(Stage s) {
        stage = s;
    }


    @FXML
    private void onValiderClick() throws SQLException {
        // Récupération des valeurs des TextField lors du clic sur le bouton Valider
        String titre = titreInput.getText();
        String isbn = isbnInput.getText();
        String editeur = editeurInput.getText();
        String anneeEdition = anneeEditionInput.getText();
        String anneeParution = anneeParutionInput.getText();
        String motsCles = motsClesInput.getText();
        String prenoms = prenomsInput.getText();
        String noms = nomsInput.getText();
        String datesNaissance = datesNaissanceInput.getText();

        // Séparer les informations en utilisant la virgule comme séparateur
        String[] motsClesArray = motsCles.split(",");
        String[] prenomsArray = prenoms.split(",");
        String[] nomsArray = noms.split(",");
        String[] datesNaissanceArray = datesNaissance.split(",");
        Vector<Integer> idsAuteurs = new Vector<Integer>();

        // Initialisation d'un auteur
        Auteur auteur = new Auteur();

        // Ajout des auteurs dans la BDD si besoin
        for (int i = 0; i < nomsArray.length; i++) {
            auteur.setNom(nomsArray[i].trim()); // Gère les espaces vides
            auteur.setPrenom(prenomsArray[i].trim());
            auteur.setAnneeNaissance(Integer.valueOf(datesNaissanceArray[i].trim()));
            idsAuteurs.add(auteur.ajouter(c));
        }

        // Initialisation du livre à créer
        Livre livre = new Livre();
        livre.setIsbn(Integer.valueOf(isbn));
        livre.setTitre(titre);
        livre.setEditeur(editeur);
        livre.setAnneePremiereParution(Integer.valueOf(anneeParution));
        livre.setAnneeEdition(Integer.valueOf(anneeEdition));
        // Gestion des mots clés
        for (int i = 0; i < motsClesArray.length && i < 5; i++) {
            switch (i) {
                case 0:
                    livre.setMotCle1(motsClesArray[i].trim());
                    break;
                case 1:
                    livre.setMotCle2(motsClesArray[i].trim());
                    break;
                case 2:
                    livre.setMotCle3(motsClesArray[i].trim());
                    break;
                case 3:
                    livre.setMotCle4(motsClesArray[i].trim());
                    break;
                case 4:
                    livre.setMotCle5(motsClesArray[i].trim());
                    break;
            }
        }

        livre.ajoute(c,idsAuteurs);
        afficherMessageInfo("Succès","Nouveau livre", "Vous avez rajouté un nouveau livre à la bibliothèque avec succès.");
        adminMainController.majTableViewLivres();
        stage.close();
    }
}
