package com.example.bibliotheque;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//Représente l'objet physique livre, manifestation de l'objet conceptuel édition
public class Livre extends Edition {

    Integer id = null;

    Boolean disponible = null;

    Integer AnneePremiereParution = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public String getDisponibleString() {if (this.disponible) {return("oui");} else {return("non");}}

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Integer getAnneePremiereParution() {
        return AnneePremiereParution;
    }

    public void setAnneePremiereParution(Integer anneePremiereParution) {
        AnneePremiereParution = anneePremiereParution;
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

    // Permet de gérer l'emprunt d'un livre
    protected void emprunt(Connection c, Integer utilisateurId, Integer livreId, String dateDebut, String dateLimiteRendu) throws SQLException {
        String query = "UPDATE Livre SET Disponible = 0 WHERE Id = ?";
        PreparedStatement prep_stmt = c.prepareStatement(query);
        prep_stmt.setInt(1, this.id);
        prep_stmt.executeUpdate();

        String queryTwo = "INSERT INTO Emprunt (Utilisateur_Id, Livre_Id, Date_Début, Date_Limite_Rendu, Rendu) VALUES (?, ?, ?, ?, 0)";
        PreparedStatement preparedStatement = c.prepareStatement(queryTwo);
        preparedStatement.setInt(1, utilisateurId);
        preparedStatement.setInt(2, livreId);
        preparedStatement.setString(3, dateDebut);
        preparedStatement.setString(4, dateLimiteRendu);
        preparedStatement.executeUpdate();
    }

    // Supprime un livre de la base de donnée, ainsi que toutes les lignes qui y font référence
    protected void supprime(Connection c) throws SQLException {
        String query = "DELETE FROM Emprunt WHERE Livre_Id = ?";
        PreparedStatement prep_stmt = c.prepareStatement(query);
        prep_stmt.setInt(1, this.id);
        prep_stmt.executeUpdate();

        String queryTwo = "DELETE FROM Livre WHERE Id = ?";
        PreparedStatement prep_stmtTwo = c.prepareStatement(queryTwo);
        prep_stmtTwo.setInt(1, this.id);
        prep_stmtTwo.executeUpdate();
    }
}
