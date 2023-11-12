package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Edition {
    Integer isbn = null;
    String editeur = null;
    Integer anneeEdition = null;
    String titre = null;
    String motCle1 = "";
    String motCle2 = "";
    String motCle3 = "";
    String motCle4 = "";
    String motCle5 = "";
    Vector<String> auteurs = null;


    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public Integer getAnneeEdition() {
        return anneeEdition;
    }

    public void setAnneeEdition(Integer anneeEdition) {
        this.anneeEdition = anneeEdition;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMotCle1() {
        return motCle1;
    }

    public void setMotCle1(String motCle1) {
        this.motCle1 = motCle1;
    }

    public void setMotCle2(String motCle2) {
        this.motCle2 = motCle2;
    }

    public void setMotCle3(String motCle3) {
        this.motCle3 = motCle3;
    }

    public void setMotCle4(String motCle4) {
        this.motCle4 = motCle4;
    }

    public void setMotCle5(String motCle5) {
        this.motCle5 = motCle5;
    }

    public void setAuteurs(Vector<String> auteurs) {
        this.auteurs = auteurs;
    }

    //Permet de récupérer les auteurs d'un livre dont on connaît l'ISBN
    public Vector<String> getAuthors(Connection c) {

        try {
            String sql = "SELECT Prénom, Nom FROM Auteur JOIN Association_Auteurs_Edition " +
                    "ON Auteur.Id = Association_Auteurs_Edition.Auteur_Id " +
                    "JOIN Edition ON Edition.ISBN = Association_Auteurs_Edition.ISBN " +
                    "WHERE Edition.ISBN = ?";
            PreparedStatement prep_stmt = c.prepareStatement(sql);
            prep_stmt.setInt(1, this.isbn);

            ResultSet rs = prep_stmt.executeQuery();
            Vector<String> auteurs = new Vector<String>();


            while ( rs.next() ) {
                String prenom = rs.getString("Prénom");
                String nom  = rs.getString("Nom");
                auteurs.add(prenom + " " + nom);
            }
            rs.close();
            prep_stmt.close();
            this.auteurs = auteurs;
            return auteurs;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    //Convertit le Vector<String> en un seul String
    public String getAuteursAsString() {
        if (auteurs == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String auteur : auteurs) {
            sb.append(auteur).append(", ");
        }

        // Suppression de la virgule et l'espace en trop à la fin
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }

    // Vérifie si une édition avec un ISBN donné existe déjà
    public Boolean existeDeja(Connection c) throws SQLException {
        String sql = "SELECT * FROM Edition WHERE ISBN = ?";
        PreparedStatement preparedStatement = c.prepareStatement(sql);
        preparedStatement.setInt(1,this.isbn);
        ResultSet rs = preparedStatement.executeQuery();
        return (rs.next());
    }
}

