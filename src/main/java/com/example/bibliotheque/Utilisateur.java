package com.example.bibliotheque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utilisateur {
    Integer id = null;
    String nom = null;
    String prenom = null;
    String mail = null;
    String telephone = null;
    Integer categorieId = null;
    String categorie = null;
    Integer dureeMaxEmprunt = null;
    Integer nombreMaxEmprunt = null;
    String listeRouge = null;
    String dateListeRouge = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setCategorieId(Integer categorieId) {
        this.categorieId = categorieId;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Integer getDureeMaxEmprunt() {
        return dureeMaxEmprunt;
    }

    public void setDureeMaxEmprunt(Integer dureeMaxEmprunt) {
        this.dureeMaxEmprunt = dureeMaxEmprunt;
    }

    public Integer getNombreMaxEmprunt() {
        return nombreMaxEmprunt;
    }

    public void setNombreMaxEmprunt(Integer nombreMaxEmprunt) {
        this.nombreMaxEmprunt = nombreMaxEmprunt;
    }

    public String getListeRouge() {
        return listeRouge;
    }

    public void setListeRouge(String listeRouge) {
        this.listeRouge = listeRouge;
    }

    public String getDateListeRouge() {
        return dateListeRouge;
    }

    public void setDateListeRouge(String dateListeRouge) {
        this.dateListeRouge = dateListeRouge;
    }

    // Récupère l'id d'un utilisateur connaissant son mail
    protected Integer getId(Connection c) throws SQLException {
        String sql = "SELECT Id FROM Utilisateur WHERE Mail = ?";
        PreparedStatement prep_stmt = c.prepareStatement(sql);
        prep_stmt.setString(1, this.mail);

        ResultSet rs = prep_stmt.executeQuery();
        Integer id = null;

        if (rs.next()) {
            id = rs.getInt("Id");
        }

        rs.close();
        prep_stmt.close();
        return id;
    }

    // Permet de récupérer la date (en String) de retour maximale pour un emprunt connaissant l'id d'un utilisateur
    protected String getDateLimiteEmprunt(Connection c, LocalDate dateDebutEmprunt) throws SQLException {
        String query = "SELECT Durée_Maximale_Emprunt FROM Utilisateur WHERE Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        Integer duree = null;
        if (rs.next()) {
            duree = rs.getInt("Durée_Maximale_Emprunt");
        }
        rs.close();
        prepStmt.close();

        LocalDate dateLimiteEmprunt = dateDebutEmprunt.plusDays(duree);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String dateFormatee = dateLimiteEmprunt.format(formatter);
        return dateFormatee;
    }

    // Permet de récupérer le nombre maximal d'emprunts autorisé d'un utilisateur connaissant son Id
    protected int getNombreMaxEmprunts(Connection c) throws SQLException {
        String query = "SELECT Nombre_Maximal_Emprunt From Utilisateur WHERE Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        Integer nombreEmprunts = null;
        if (rs.next()) {
            nombreEmprunts = rs.getInt("Nombre_Maximal_Emprunt");
        }
        rs.close();
        prepStmt.close();
        return nombreEmprunts;
    }

    // Retourne le nombre d'emprunts en cours d'un utilisateur
    protected int getNombreEmprunts(Connection c) throws SQLException {
        String query = "SELECT COUNT(*) FROM Emprunt WHERE Utilisateur_Id = ? AND Rendu = 0";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        Integer nombreEmprunts = null;
        if (rs.next()) {
            nombreEmprunts = rs.getInt(1);
        }
        rs.close();
        prepStmt.close();
        return nombreEmprunts;
    }

    // Renvoie la catégorie d'un utilisateur
    protected String getCategorieName(Connection c) throws SQLException {
        String query = "SELECT c.Nom FROM Catégorie AS c JOIN Utilisateur AS u ON u.Catégorie_Id = c.Id WHERE u.Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1, this.id);

        ResultSet rs = prepStmt.executeQuery();
        String categorieName = null;
        if (rs.next()) {
            categorieName = rs.getString("Nom");
        }
        rs.close();
        prepStmt.close();
        return categorieName;
    }

    // Permet de modifier le profil d'un utilisateur, ainsi que ses credentials si son email a été modifié. Met automatiquement à jour la date de rendu des emprunts
    protected int setUser(Connection c) throws SQLException {
        // Met à jour la table Utilisateur
        String query = "UPDATE Utilisateur SET Nom = ?, Prénom = ?, Mail = ?, Téléphone = ?, Catégorie_Id = ?, Durée_Maximale_Emprunt = ?, Nombre_Maximal_Emprunt = ? WHERE Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);

        prepStmt.setString(1, this.nom);
        prepStmt.setString(2, this.prenom);
        prepStmt.setString(3, this.mail);
        prepStmt.setString(4, this.telephone);
        prepStmt.setInt(5, this.categorieId);
        prepStmt.setInt(6, this.dureeMaxEmprunt);
        prepStmt.setInt(7, this.nombreMaxEmprunt);
        prepStmt.setInt(8, this.id);

        int rowsAffected = prepStmt.executeUpdate();
        prepStmt.close();

        // Met à jour la table d'identification (pour la connection à l'appli)
        String queryTwo = "UPDATE Identification SET Utilisateur_Mail = ? WHERE Utilisateur_Id = ?";
        PreparedStatement prepStmtTwo = c.prepareStatement(queryTwo);

        prepStmtTwo.setString(1,this.mail);
        prepStmtTwo.setInt(2,this.id);

        prepStmtTwo.executeUpdate();
        prepStmtTwo.close();

        // Récupère des données qui vont nous permettre d'update les emprunts de l'utilisateur dont on vient de modifier le profil
        String queryGet = "SELECT * FROM Emprunt WHERE (Utilisateur_Id = ? AND Rendu = 0)";
        PreparedStatement prepStmtGet = c.prepareStatement(queryGet);

        prepStmtGet.setInt(1,this.id);

        ResultSet rs = prepStmtGet.executeQuery();

        // Met à jour la table des emprunts (pour modifier la date de rendu exigée)
        String queryThree = "UPDATE Emprunt SET Date_Limite_Rendu = ? WHERE Id = ?";
        PreparedStatement prepStmtThree = c.prepareStatement(queryThree);

        LocalDate dateDebut = null;
        String dateRetour = null;
        Emprunt emprunt = new Emprunt();
        while (rs.next()) {
            emprunt.setId(rs.getInt("Id"));
            emprunt.setDateDebut(rs.getString("Date_Début"));

            dateDebut = LocalDate.parse(emprunt.getDateDebut());
            dateRetour = this.getDateLimiteEmprunt(c,dateDebut);

            prepStmtThree.setString(1,dateRetour);
            prepStmtThree.setInt(2,emprunt.getId());

            prepStmtThree.executeUpdate();
        }

        rs.close();
        prepStmtThree.close();
        return rowsAffected;
    }

    // Permet d'ajouter un utilisateur dans la BDD, et lui crée un mot de passe automatique (1234)
    protected void addUser(Connection c) throws SQLException {
        String query = "INSERT INTO Utilisateur (Nom, Prénom, Mail, Téléphone, Catégorie_Id, Durée_Maximale_Emprunt, Nombre_Maximal_Emprunt) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement prepStmt = c.prepareStatement(query);

        prepStmt.setString(1, this.nom);
        prepStmt.setString(2, this.prenom);
        prepStmt.setString(3, this.mail);
        prepStmt.setString(4, this.telephone);
        prepStmt.setInt(5, this.categorieId);
        prepStmt.setInt(6, this.dureeMaxEmprunt);
        prepStmt.setInt(7, this.nombreMaxEmprunt);

        prepStmt.executeUpdate();
        prepStmt.close();

        String queryTwo = "INSERT INTO Identification (Utilisateur_Id, Utilisateur_Mail, Hash_MdP) VALUES (?,?,?)";
        PreparedStatement prepStmtTwo = c.prepareStatement(queryTwo);

        prepStmtTwo.setInt(1,this.getId(c));
        prepStmtTwo.setString(2,this.mail);
        prepStmtTwo.setString(3,"03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4");

        prepStmtTwo.executeUpdate();
        prepStmtTwo.close();
    }

    // Retourne la date de radiation d'un utilisateur, s'il a été radié.
    protected String getDateListeRouge(Connection c) throws SQLException {
        String query = "SELECT Date_De_Radiation FROM Liste_Rouge WHERE Utilisateur_Id = ?";
        PreparedStatement prepStmt = c.prepareStatement(query);
        prepStmt.setInt(1,this.id);
        ResultSet rs = prepStmt.executeQuery();
        String date = "";
        if (rs.next()) {
            date = rs.getString("Date_De_Radiation");
        }
        rs.close();
        prepStmt.close();
        return date;
    }

    // Rajoute un utilisateur sur la liste rouge
    protected void addListeRouge(Connection c) throws SQLException {
        String query = "INSERT INTO Liste_Rouge (Utilisateur_Id, Date_De_Radiation) VALUES (?,?)";
        PreparedStatement prepStmt = c.prepareStatement(query);

        prepStmt.setInt(1, this.id);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String dateFormatee = date.format(formatter);

        prepStmt.setString(2,dateFormatee);

        prepStmt.executeUpdate();
        prepStmt.close();
    }
}
