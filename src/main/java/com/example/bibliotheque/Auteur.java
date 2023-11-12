package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Auteur {
    Integer id = null;
    String prenom = null;
    String nom = null;
    Integer anneeNaissance = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAnneeNaissance(Integer anneeNaissance) {
        this.anneeNaissance = anneeNaissance;
    }

    // Vérifie si un auteur est déjà dans la BDD (c'est une approximation, on fait le postulat qu'un auteur est complètement déterminé par le triplet (nom,prénom,année de naissance))
    protected Boolean existeDeja(Connection c) throws SQLException {
        String sql = "SELECT * FROM Auteur WHERE (Nom = ? AND Prénom = ? AND Année_de_naissance = ?)";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        preparedStatement.setString(1,this.nom);
        preparedStatement.setString(2,this.prenom);
        preparedStatement.setInt(3,this.anneeNaissance);
        ResultSet rs = preparedStatement.executeQuery();
        return (rs.next());
    }

    // Ajoute un nouvel auteur à la BDD, ne fait rien si l'auteur y est déjà, retourne l'id de l'auteur dans les 2 cas.
    protected int ajouter(Connection c) throws SQLException {
        if (!this.existeDeja(c)) {
            String query = "INSERT INTO Auteur (Nom, Prénom, Année_de_naissance) VALUES (?,?,?)";
            PreparedStatement preparedStatement = c.prepareStatement(query);
            preparedStatement.setString(1,this.nom);
            preparedStatement.setString(2,this.prenom);
            preparedStatement.setInt(3,this.anneeNaissance);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }

        String sql = "SELECT Id FROM Auteur WHERE (Nom = ? AND Prénom = ? AND Année_de_naissance = ?)";
        PreparedStatement prepStmt = c.prepareStatement(sql);
        prepStmt.setString(1,this.nom);
        prepStmt.setString(2,this.prenom);
        prepStmt.setInt(3,this.anneeNaissance);
        ResultSet rs = prepStmt.executeQuery();
        int id = rs.getInt("Id");
        rs.close();
        prepStmt.close();
        return id;
    }

}
