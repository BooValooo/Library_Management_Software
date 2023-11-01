package com.example.bibliotheque;

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
}
