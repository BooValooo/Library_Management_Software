package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utilisateur {
    Integer id = null;
    String nom = null;
    String prenom = null;
    String mail = null;
    String telephone = null;
    Integer categorieId = null;
    String categorie = null;
    Integer dureeMaxEmprunt = null;
    Integer nombreMaxEmprunt = null;

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Integer getDureeMaxEmprunt() {
        return dureeMaxEmprunt;
    }

    public void setDureeMaxEmprunt(Integer dureeMaxEmprunt) {
        this.dureeMaxEmprunt = dureeMaxEmprunt;
    }

    public Integer getNombreMaxEmprunt() {
        return nombreMaxEmprunt;
    }

    public void setNombreMaxEmprunt(Integer nombreMaxEmprunt) {
        this.nombreMaxEmprunt = nombreMaxEmprunt;
    }

    // Permet de récupérer la date (en String) de retour maximale pour un emprunt connaissant l'id d'un utilisateur
    protected String getDateLimiteEmprunt(Connection c, LocalDate dateDebutEmprunt) throws SQLException {
        String query = "SELECT Durée_Maximale_Emprunt FROM Utilisateur WHERE Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        Integer duree = null;
        if (rs.next()) {
            duree = rs.getInt("Durée_Maximale_Emprunt");
        }
        rs.close();

        LocalDate dateLimiteEmprunt = dateDebutEmprunt.plusDays(duree);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String dateFormatee = dateLimiteEmprunt.format(formatter);
        return dateFormatee;
    }

    // Permet de récupérer le nombre maximal d'emprunts autorisé d'un utilisateur connaissant son Id
    protected int getNombreMaxEmprunts(Connection c) throws SQLException {
        String query = "SELECT Nombre_Maximal_Emprunt From Utilisateur WHERE Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        Integer nombreEmprunts = null;
        if (rs.next()) {
            nombreEmprunts = rs.getInt("Nombre_Maximal_Emprunt");
        }
        rs.close();
        return nombreEmprunts;
    }

    // Retourne le nombre d'emprunts en cours d'un utilisateur
    protected int getNombreEmprunts(Connection c) throws SQLException {
        String query = "SELECT COUNT(*) FROM Emprunt WHERE Utilisateur_Id = ? AND Rendu = 0";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        Integer nombreEmprunts = null;
        if (rs.next()) {
            nombreEmprunts = rs.getInt(1);
        }
        rs.close();
        return nombreEmprunts;
    }

    protected String getCategorieName(Connection c) throws SQLException {
        String query = "SELECT c.Nom FROM Catégorie AS c JOIN Utilisateur AS u ON u.Catégorie_Id = c.Id WHERE u.Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        String categorieName = null;
        if (rs.next()) {
            categorieName = rs.getString("Nom");
        }
        rs.close();
        return categorieName;
    }

    protected int setUser(Connection c) throws SQLException {
        String query = "UPDATE Utilisateur SET Nom = ?, Prénom = ?, Mail = ?, Téléphone = ?, Catégorie_Id = ?, Durée_Maximale_Emprunt = ?, Nombre_Maximal_Emprunt = ? WHERE Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);

        prepStmt.setString(1, this.nom);
        prepStmt.setString(2, this.prenom);
        prepStmt.setString(3, this.mail);
        prepStmt.setString(4, this.telephone);
        prepStmt.setInt(5, this.categorieId);
        prepStmt.setInt(6, this.dureeMaxEmprunt);
        prepStmt.setInt(7, this.nombreMaxEmprunt);
        prepStmt.setInt(8, this.id);

        int rowsAffected = prepStmt.executeUpdate();

        return rowsAffected;
    }
}
