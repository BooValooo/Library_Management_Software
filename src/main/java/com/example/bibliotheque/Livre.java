package com.example.bibliotheque;

import java.sql.*;
import java.util.Vector;

//Représente l'objet physique livre, manifestation de l'objet conceptuel édition
public class Livre extends Edition {

    Integer id = null;

    Boolean disponible = null;

    Integer anneePremiereParution = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public String getDisponibleString() {
        if (this.disponible) {
            return ("oui");
        } else {
            return ("non");
        }
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Integer getAnneePremiereParution() {
        return anneePremiereParution;
    }

    public void setAnneePremiereParution(Integer anneePremiereParution) {
        this.anneePremiereParution = anneePremiereParution;
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
            prep_stmt.close();
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
        prep_stmt.close();

        String queryTwo = "INSERT INTO Emprunt (Utilisateur_Id, Livre_Id, Date_Début, Date_Limite_Rendu, Rendu) VALUES (?, ?, ?, ?, 0)";
        PreparedStatement preparedStatement = c.prepareStatement(queryTwo);
        preparedStatement.setInt(1, utilisateurId);
        preparedStatement.setInt(2, livreId);
        preparedStatement.setString(3, dateDebut);
        preparedStatement.setString(4, dateLimiteRendu);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    // Supprime un livre de la base de données, ainsi que toutes les lignes qui y font référence
    protected void supprime(Connection c) throws SQLException {
        String query = "DELETE FROM Emprunt WHERE Livre_Id = ?";
        PreparedStatement prep_stmt = c.prepareStatement(query);
        prep_stmt.setInt(1, this.id);
        prep_stmt.executeUpdate();
        prep_stmt.close();

        String queryTwo = "DELETE FROM Livre WHERE Id = ?";
        PreparedStatement prep_stmtTwo = c.prepareStatement(queryTwo);
        prep_stmtTwo.setInt(1, this.id);
        prep_stmtTwo.executeUpdate();
        prep_stmtTwo.close();
    }

    // Ajoute un livre à la base de données, et relie la table d'édition à la table d'auteur
    protected void ajoute(Connection c, Vector<Integer> idsAuteurs) throws SQLException {
        // Crée le livre (élément physique)
        String queryOne = "INSERT INTO Livre (ISBN, Disponible) VALUES (?,1)";
        PreparedStatement prep_stmt = c.prepareStatement(queryOne);
        prep_stmt.setInt(1, this.isbn);
        prep_stmt.executeUpdate();
        prep_stmt.close();

        // Si l'édition n'est pas dans la BDD, la crée
        if (!this.existeDeja(c)) {
            String query2 = "INSERT INTO Edition (ISBN, Editeur, Année_Edition, Année_Première_Parution, Titre, Mot_clé_1, Mot_clé_2, Mot_clé_3, Mot_clé_4, Mot_clé_5) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement prep2 = c.prepareStatement(query2);
            prep2.setInt(1,this.isbn);
            prep2.setString(2,this.editeur);
            prep2.setInt(3,this.anneeEdition);
            prep2.setInt(4,this.anneePremiereParution);
            prep2.setString(5,this.titre);
            prep2.setString(6,this.motCle1);
            prep2.setString(7,this.motCle2);
            prep2.setString(8,this.motCle3);
            prep2.setString(9,this.motCle4);
            prep2.setString(10,this.motCle5);
            prep2.executeUpdate();
            prep2.close();

            String query3 = "INSERT INTO Association_Auteurs_Edition (Auteur_Id, ISBN) VALUES (?,?)";
            PreparedStatement prep3 = c.prepareStatement(query3);
            for (Integer idAuteur : idsAuteurs) {
                prep3.setInt(1,idAuteur);
                prep3.setInt(2,this.isbn);
                prep3.executeUpdate();
            }
            prep3.close();
        }
    }
}
