package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

//Représente l'objet physique livre, manifestation de l'objet conceptuel édition
public class Livre extends Edition {

    Integer id = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //Fonction de test, à effacer
    public static void main(String args[] ) {
        Connection c = null;
        Statement stmt = null;

        c = BDDConnector.getConnection();
        try {
            stmt = c.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "INSERT INTO Livre (Id, ISBN)" + "VALUES (2,123457)";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Table edited successfully");
    }
}
