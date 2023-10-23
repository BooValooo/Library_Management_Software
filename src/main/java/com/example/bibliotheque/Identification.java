package com.example.bibliotheque;

import java.sql.*;

public class Identification {
    Integer utilisateurId = null;
    String utilisateurMail = null;
    String hashMdp = null;



    public String getMdp(Integer utilisateurId, Connection c) {

        try {
            String sql = "SELECT Hash_MdP FROM Identification WHERE Utilisateur_id = ?";
            PreparedStatement prep_stmt = c.prepareStatement(sql);
            prep_stmt.setInt(1, utilisateurId);

            ResultSet rs = prep_stmt.executeQuery();
            String mdp = null;

            if (rs.next()) {
                mdp = rs.getString("Hash_MdP");
            }

            rs.close();
            return mdp;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }
}

