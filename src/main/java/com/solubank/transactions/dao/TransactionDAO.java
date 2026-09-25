package com.solubank.transactions.dao;

import com.solubank.transactions.entity.Transaction;
import com.solubank.transactions.entity.TypeTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAO {

    public Transaction ajouter(Transaction t) {
        String sql = """
                INSERT INTO transaction (date_transaction, montant, type, lieu, id_compte)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(t.date()));
            ps.setBigDecimal(2, t.montant());
            ps.setString(3, t.type().name());
            ps.setString(4, t.lieu());
            ps.setInt(5, t.idCompte());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Transaction(rs.getInt(1), t.date(), t.montant(), t.type(), t.lieu(), t.idCompte());
                }
            }
            throw new SQLException("Aucun id genere pour la nouvelle transaction.");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la transaction : " + e.getMessage(), e);
        }
    }

    public Transaction ajouter(Transaction t, Connection conn) throws SQLException {
        String sql = """
                INSERT INTO transaction (date_transaction, montant, type, lieu, id_compte)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(t.date()));
            ps.setBigDecimal(2, t.montant());
            ps.setString(3, t.type().name());
            ps.setString(4, t.lieu());
            ps.setInt(5, t.idCompte());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Transaction(rs.getInt(1), t.date(), t.montant(), t.type(), t.lieu(), t.idCompte());
                }
            }
            throw new SQLException("Aucun id genere pour la nouvelle transaction.");
        }
    }

    public Optional<Transaction> trouverParId(int id) {
        String sql = "SELECT * FROM transaction WHERE id = ?";
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
            throw new RuntimeException("Erreur lors de la recherche de la transaction : " + e.getMessage(), e);
        }
    }

    public List<Transaction> trouverParCompte(int idCompte) {
        String sql = "SELECT * FROM transaction WHERE id_compte = ? ORDER BY date_transaction";
        List<Transaction> resultats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCompte);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultats.add(mapper(rs));
                }
            }
            return resultats;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des transactions : " + e.getMessage(), e);
        }
    }

    public List<Transaction> trouverTous() {
        String sql = "SELECT * FROM transaction ORDER BY date_transaction";
        List<Transaction> resultats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultats.add(mapper(rs));
            }
            return resultats;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des transactions : " + e.getMessage(), e);
        }
    }

    public boolean modifier(Transaction t) {
        String sql = "UPDATE transaction SET date_transaction = ?, montant = ?, type = ?, lieu = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(t.date()));
            ps.setBigDecimal(2, t.montant());
            ps.setString(3, t.type().name());
            ps.setString(4, t.lieu());
            ps.setInt(5, t.id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de la transaction : " + e.getMessage(), e);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM transaction WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la transaction : " + e.getMessage(), e);
        }
    }

    private Transaction mapper(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("id"),
                rs.getTimestamp("date_transaction").toLocalDateTime(),
                rs.getBigDecimal("montant"),
                TypeTransaction.valueOf(rs.getString("type")),
                rs.getString("lieu"),
                rs.getInt("id_compte")
        );
    }
}