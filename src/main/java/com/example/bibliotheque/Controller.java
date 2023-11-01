package com.example.bibliotheque;

import javafx.stage.Stage;

import java.sql.Connection;

//Classe abstraite permettant de n'utiliser qu'un seul connecteur lors de l'utilisation de l'application. Evite de devoir se reconnecter à chaque requête.
public abstract class Controller {

    private Stage stage = new Stage();
    static Connection c = BDDConnector.getConnection();
}
