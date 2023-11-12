package com.example.bibliotheque;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Identification {
    Integer utilisateurId = null;
    String utilisateurMail = null;
    String hashMdp = null;


    //Permet de récupérer le hash du MdP d'un utilisateur ayant donné son Id
    protected String getMdp(Connection c) {

        try {
            String sql = "SELECT Hash_MdP FROM Identification WHERE Utilisateur_id = ?";
            PreparedStatement prep_stmt = c.prepareStatement(sql);
            prep_stmt.setInt(1, this.utilisateurId);

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

    //Permet de récupérer le hash du MdP d'un utilisateur ayant donné son mail
    protected String getMdp(String utilisateurMail, Connection c) {

        try {
            String sql = "SELECT Hash_MdP FROM Identification WHERE Utilisateur_Mail = ?";
            PreparedStatement prep_stmt = c.prepareStatement(sql);
            prep_stmt.setString(1, utilisateurMail);

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

    // Récupère l'id d'un utilisateur connaissant son mail
    protected Integer getId(Connection c) {
        try {
            String sql = "SELECT Utilisateur_Id FROM Identification WHERE Utilisateur_Mail = ?";
            PreparedStatement prep_stmt = c.prepareStatement(sql);
            prep_stmt.setString(1, this.utilisateurMail);

            ResultSet rs = prep_stmt.executeQuery();
            Integer id = null;

            if (rs.next()) {
                id = rs.getInt("Utilisateur_Id");
            }

            rs.close();
            prep_stmt.close();
            return id;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    //Les 2 fonctions suivantes permettent de hasher une chaîne de caractères (pour ne pas stocker des MdP en clair)
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    // Permet d'ajouter le mdp d'un utilisateur dans la BDD (mdp hashé)
    protected void setMdpBdd(Connection c, String mdp) throws SQLException, NoSuchAlgorithmException {
        String query = "UPDATE Identification SET Hash_Mdp = ? WHERE Utilisateur_Id = ?";
        PreparedStatement prep_stmt = c.prepareStatement(query);
        prep_stmt.setInt(2, this.utilisateurId);
        String mdpHashe = toHexString(getSHA(mdp));
        prep_stmt.setString(1,mdpHashe);
        prep_stmt.executeUpdate();
        prep_stmt.close();
    }

    // Retourne la catégorie d'un utilisateur (pour permettre d'ouvrir la page admin ou la page client)
    protected int getCategorie(Connection c) throws SQLException {
        String query = "SELECT Catégorie_Id FROM Utilisateur AS u JOIN Identification AS i ON i.Utilisateur_Id = u.Id " +
                "WHERE Utilisateur_Id = ?";
        PreparedStatement prep_stmt = c.prepareStatement(query);
        prep_stmt.setInt(1, this.utilisateurId);
        ResultSet rs = prep_stmt.executeQuery();
        Integer cat = null;

        if (rs.next()) {
            cat = rs.getInt("Catégorie_Id");
        }

        rs.close();
        prep_stmt.close();
        return cat;
    }
}

