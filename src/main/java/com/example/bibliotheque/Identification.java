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


    public Integer getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Integer utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getUtilisateurMail() {
        return utilisateurMail;
    }

    public void setUtilisateurMail(String utilisateurMail) {
        this.utilisateurMail = utilisateurMail;
    }

    public String getHashMdp() {
        return hashMdp;
    }

    public void setHashMdp(String hashMdp) {
        this.hashMdp = hashMdp;
    }

    //Permet de récupérer le hash du MdP d'un utilisateur ayant donné son Id
    public String getMdp(Connection c) {

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
    public String getMdp(String utilisateurMail, Connection c) {

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
}

