package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Edition {
    Integer isbn = null;
    String editeur = null;
    Integer anneeEdition = null;
    String titre = null;
    Integer motCle1Id = null;
    Integer motCle2Id = null;
    Integer motCle3Id = null;
    Integer motCle4Id = null;
    Integer motCle5Id = null;


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

                System.out.println( "NAME = " + prenom + " " + nom );
                System.out.println();
                auteurs.add(prenom + " " + nom);

            }
            rs.close();

            return auteurs;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }
}
