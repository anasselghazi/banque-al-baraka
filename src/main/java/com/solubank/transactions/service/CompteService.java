package com.solubank.transactions.service;

import com.solubank.transactions.dao.ClientDAO;
import com.solubank.transactions.dao.CompteDAO;
import com.solubank.transactions.entity.Compte;
import com.solubank.transactions.entity.CompteCourant;
import com.solubank.transactions.entity.CompteEpargne;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CompteService {

    private final CompteDAO compteDAO;
    private final ClientDAO clientDAO;

    public CompteService(CompteDAO compteDAO, ClientDAO clientDAO) {
        this.compteDAO = compteDAO;
        this.clientDAO = clientDAO;
    }

    public CompteCourant creerCompteCourant(int idClient, String numero, BigDecimal solde, BigDecimal decouvert) {
        verifierClientExiste(idClient);
        CompteCourant compte = new CompteCourant(0, numero, solde, idClient, decouvert);
        return (CompteCourant) compteDAO.ajouter(compte);
    }

    public CompteEpargne creerCompteEpargne(int idClient, String numero, BigDecimal solde, BigDecimal taux) {
        verifierClientExiste(idClient);
        CompteEpargne compte = new CompteEpargne(0, numero, solde, idClient, taux);
        return (CompteEpargne) compteDAO.ajouter(compte);
    }

    public boolean mettreAJourSolde(int idCompte, BigDecimal nouveauSolde) {
        if (nouveauSolde == null) {
            throw new IllegalArgumentException("Le solde ne peut pas etre nul.");
        }
        return compteDAO.mettreAJourSolde(idCompte, nouveauSolde);
    }

    public List<Compte> rechercherParClient(int idClient) {
        return compteDAO.trouverParClient(idClient);
    }

    public Optional<Compte> rechercherParNumero(String numero) {
        return compteDAO.trouverParNumero(numero);
    }

    public Optional<Compte> compteSoldeMax() {
        return compteDAO.trouverTous().stream()
                .max(Comparator.comparing(Compte::getSolde));
    }

    public Optional<Compte> compteSoldeMin() {
        return compteDAO.trouverTous().stream()
                .min(Comparator.comparing(Compte::getSolde));
    }

    private void verifierClientExiste(int idClient) {
        clientDAO.trouverParId(idClient)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable, id=" + idClient));
    }
}