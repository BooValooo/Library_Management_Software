package com.example.bibliotheque;

import java.sql.*;

public class Identification {
    Integer utilisateurId = null;
    String utilisateurMail = null;
    String hashMdp = null;



    public String getMdp(Integer utilisateurId) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/home/administrateur/Documents/travail/Supervision de capteurs/tp-bibliotheque/src/Database_bibliothèque");
            System.out.println("Opened database successfully");

            String sql = "SELECT Hash_MdP FROM Identification WHERE Utilisateur_id =?";
            PreparedStatement prep_stmt = c.prepareStatement(sql);
            prep_stmt.setInt(1,utilisateurId);
            ResultSet rs = stmt.executeQuery(sql);
            String mdp = rs.getString("Hash_MdP");
            rs.close();
            stmt.close();
            c.close();
            return mdp;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return null;
    }
}

