package com.solubank.transactions.dao;

import com.solubank.transactions.entity.Compte;
import com.solubank.transactions.entity.CompteCourant;
import com.solubank.transactions.entity.CompteEpargne;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompteDAO {

    public Compte ajouter(Compte compte) {
        String sql = """
                INSERT INTO compte (numero, solde, id_client, type_compte, decouvert_autorise, taux_interet)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, compte.getNumero());
            ps.setBigDecimal(2, compte.getSolde());
            ps.setInt(3, compte.getIdClient());

            if (compte instanceof CompteCourant courant) {
                ps.setString(4, "COURANT");
                ps.setBigDecimal(5, courant.getDecouvertAutorise());
                ps.setNull(6, java.sql.Types.NUMERIC);
            } else if (compte instanceof CompteEpargne epargne) {
                ps.setString(4, "EPARGNE");
                ps.setNull(5, java.sql.Types.NUMERIC);
                ps.setBigDecimal(6, epargne.getTauxInteret());
            } else {
                throw new IllegalArgumentException("Type de compte inconnu.");
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return compte instanceof CompteCourant c
                            ? new CompteCourant(id, c.getNumero(), c.getSolde(), c.getIdClient(), c.getDecouvertAutorise())
                            : new CompteEpargne(id, compte.getNumero(), compte.getSolde(), compte.getIdClient(),
                                    ((CompteEpargne) compte).getTauxInteret());
                }
            }
            throw new SQLException("Aucun id genere pour le nouveau compte.");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du compte : " + e.getMessage(), e);
        }
    }

    public Optional<Compte> trouverParId(int id) {
        String sql = "SELECT * FROM compte WHERE id = ?";
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
            throw new RuntimeException("Erreur lors de la recherche du compte : " + e.getMessage(), e);
        }
    }

    public Optional<Compte> trouverParNumero(String numero) {
        String sql = "SELECT * FROM compte WHERE numero = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapper(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du compte : " + e.getMessage(), e);
        }
    }

    public List<Compte> trouverParClient(int idClient) {
        String sql = "SELECT * FROM compte WHERE id_client = ? ORDER BY id";
        List<Compte> resultats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultats.add(mapper(rs));
                }
            }
            return resultats;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des comptes : " + e.getMessage(), e);
        }
    }

    public List<Compte> trouverTous() {
        String sql = "SELECT * FROM compte ORDER BY id";
        List<Compte> resultats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultats.add(mapper(rs));
            }
            return resultats;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des comptes : " + e.getMessage(), e);
        }
    }

    public boolean mettreAJourSolde(int idCompte, BigDecimal nouveauSolde) {
        String sql = "UPDATE compte SET solde = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, nouveauSolde);
            ps.setInt(2, idCompte);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise a jour du solde : " + e.getMessage(), e);
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM compte WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du compte : " + e.getMessage(), e);
        }
    }

    private Compte mapper(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String numero = rs.getString("numero");
        BigDecimal solde = rs.getBigDecimal("solde");
        int idClient = rs.getInt("id_client");
        String typeCompte = rs.getString("type_compte");

        if ("COURANT".equals(typeCompte)) {
            return new CompteCourant(id, numero, solde, idClient, rs.getBigDecimal("decouvert_autorise"));
        } else {
            return new CompteEpargne(id, numero, solde, idClient, rs.getBigDecimal("taux_interet"));
        }
    }
}