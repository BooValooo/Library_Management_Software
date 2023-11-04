package com.example.bibliotheque;

import java.sql.Date;

public class Emprunt {
    String dateDebut = null;
    String dateFinPrevue = null;
    String dateFinReelle = null;
    Integer livreId = null;
    String titre = null;

    Integer UtilisateurId = null;
    Boolean rendu = null;


    public Integer getLivreId() {
        return livreId;
    }

    public void setLivreId(Integer livreId) {
        this.livreId = livreId;
    }

    public Integer getUtilisateurId() {
        return UtilisateurId;
    }

    public void setUtilisateurId(Integer utilisateurId) {
        UtilisateurId = utilisateurId;
    }

    public Boolean getRendu() {
        return rendu;
    }

    public String getRenduString() {if (rendu) {return("oui");} else {return("non");}}


    public void setRendu(Boolean rendu) {
        this.rendu = rendu;
    }

    public void setRendu(Integer rendu) {
        if (rendu==0) {this.rendu = false;} else {this.rendu = true;}
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String date) {
        this.dateDebut = date;
    }

    public String getDateFinPrevue() {
        return dateFinPrevue;
    }

    public void setDateFinPrevue(String dateFinPrevue) {
        this.dateFinPrevue = dateFinPrevue;
    }

    public String getDateFinReelle() {
        return dateFinReelle;
    }

    public void setDateFinReelle(String dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
