package com.example.bibliotheque;

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
}
