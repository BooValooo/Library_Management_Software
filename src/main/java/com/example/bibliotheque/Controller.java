package com.example.bibliotheque;

import javafx.stage.Stage;

import java.sql.Connection;

public abstract class Controller {

    private Stage stage = new Stage();
    static Connection c = BDDConnector.getConnection();
}
