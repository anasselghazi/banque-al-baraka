package com.solubank.transactions.dao;

import com.solubank.transactions.entity.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAO {

    public Client ajouter(Client client) {
        String sql = "INSERT INTO client (nom, email) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.nom());
            ps.setString(2, client.email());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Client(rs.getInt(1), client.nom(), client.email());
                }
            }
            throw new SQLException("Aucun id genere pour le nouveau client.");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du client : " + e.getMessage(), e);
        }
    }

    public Optional<Client> trouverParId(int id) {
        String sql = "SELECT id, nom, email FROM client WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapper(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du client : " + e.getMessage(), e);
        }
    }

    public List<Client> rechercherParNom(String nom) {
        String sql = "SELECT id, nom, email FROM client WHERE LOWER(nom) LIKE LOWER(?)";
        List<Client> resultats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nom + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultats.add(mapper(rs));
                }
            }
            return resultats;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du client : " + e.getMessage(), e);
        }
    }

    public List<Client> trouverTous() {
        String sql = "SELECT id, nom, email FROM client ORDER BY nom";
        List<Client> resultats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultats.add(mapper(rs));
            }
            return resultats;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des clients : " + e.getMessage(), e);
        }
    }

    public boolean modifier(Client client) {
        String sql = "UPDATE client SET nom = ?, email = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, client.nom());
            ps.setString(2, client.email());
            ps.setInt(3, client.id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du client : " + e.getMessage(), e);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM client WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du client : " + e.getMessage(), e);
        }
    }

    private Client mapper(ResultSet rs) throws SQLException {
        return new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("email"));
    }
}