package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Categorie {
    Integer id = null;
    String nom = null;
    Integer dureeMaxEmpruntRef = null;
    Integer nombreMaxEmpruntRef = null;

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

    public Integer getDureeMaxEmpruntRef() {
        return dureeMaxEmpruntRef;
    }

    public void setDureeMaxEmpruntRef(Integer dureeMaxEmpruntRef) {
        this.dureeMaxEmpruntRef = dureeMaxEmpruntRef;
    }

    public Integer getNombreMaxEmpruntRef() {
        return nombreMaxEmpruntRef;
    }

    public void setNombreMaxEmpruntRef(Integer nombreMaxEmpruntRef) {
        this.nombreMaxEmpruntRef = nombreMaxEmpruntRef;
    }

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
        return nombreEmprunts;
    }

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
        return dureeEmprunts;
    }
}
