package com.example.bibliotheque;

import java.sql.*;

//Représente l'objet physique livre, manifestation de l'objet conceptuel édition
public class Livre extends Edition {

    Integer id = null;

    Boolean disponible = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }


    //Donne le titre d'un ouvrage connaissant son Id
    protected String getTitre(Connection c) {
        try {
            String sql = "SELECT Titre FROM Livre AS l JOIN Edition AS e ON l.ISBN = e.ISBN WHERE l.Id = ?";
            PreparedStatement prep_stmt = c.prepareStatement(sql);
            prep_stmt.setInt(1, this.id);

            ResultSet rs = prep_stmt.executeQuery();
            String titre = null;

            if (rs.next()) {
                titre = rs.getString("Titre");
            }

            rs.close();
            return titre;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    //Fonction de test, à effacer
    public static void main(String args[] ) {
        Connection c = null;
        Statement stmt = null;

        c = BDDConnector.getConnection();
        try {
            stmt = c.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "INSERT INTO Livre (Id, ISBN)" + "VALUES (2,123457)";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Table edited successfully");
    }
}
