package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Categorie {
    Integer id = null;
    String nom = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Récupère le nombre d'emprunts max de référence pour une catégorie dont on connait l'id
    protected Integer getNombreMaxEmpruntRef(Connection c) throws SQLException {
        String query = "SELECT Nombre_Max_Emprunt_Ref From Catégorie WHERE Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        Integer nombreEmprunts = null;
        if (rs.next()) {
            nombreEmprunts = rs.getInt("Nombre_Max_Emprunt_Ref");
        }
        rs.close();
        prepStmt.close();
        return nombreEmprunts;
    }

    // Récupère la durée d'emprunt de référence pour une catégorie dont on connait l'id
    protected Integer getDuréeMaxEmpruntRef(Connection c) throws SQLException {
        String query = "SELECT Durée_Max_Emprunt_Ref From Catégorie WHERE Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        Integer dureeEmprunts = null;
        if (rs.next()) {
            dureeEmprunts = rs.getInt("Durée_Max_Emprunt_Ref");
        }
        rs.close();
        prepStmt.close();
        return dureeEmprunts;
    }
}
