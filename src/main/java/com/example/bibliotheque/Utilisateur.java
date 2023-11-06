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
    Integer telephone = null;
    Integer categorieId = null;
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

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public Integer getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
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
        prepStmt.setInt(1,this.id);

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
}
