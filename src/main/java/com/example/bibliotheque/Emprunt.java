package com.example.bibliotheque;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Emprunt {
    Integer id = null;
    String dateDebut = null;
    String dateFinPrevue = null;
    String dateFinReelle = null;
    Integer livreId = null;
    String titre = null;

    Integer UtilisateurId = null;
    Boolean rendu = null;


    public Integer getId() {return id;}

    public void setId(Integer i) {this.id = i;}

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

    protected void retour(Connection c) throws SQLException {
        String query = "UPDATE Livre SET Disponible = 1 WHERE Id = ?";
        PreparedStatement prep_stmt = c.prepareStatement(query);
        prep_stmt.setInt(1, this.livreId);
        prep_stmt.executeUpdate();

        String queryTwo = "UPDATE Emprunt SET Rendu = 1, Date_Rendu = ? WHERE Id = ?";
        PreparedStatement prep_stmtTwo = c.prepareStatement(queryTwo);
        prep_stmtTwo.setInt(2, this.id);

        // Obtenir la date du jour
        LocalDate dateDuJour = LocalDate.now();

        // Formater la date en "aaaa-MM-jj"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String dateFormatee = dateDuJour.format(formatter);


        prep_stmtTwo.setString(1,dateFormatee);
        prep_stmtTwo.executeUpdate();
    }
}
