package com.example.bibliotheque;

import java.sql.Date;

public class Emprunt {
    Date date = null;
    Integer livreId = null;
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

    public void setRendu(Boolean rendu) {
        this.rendu = rendu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
