package com.solubank.transactions.service;

import com.solubank.transactions.dao.ClientDAO;
import com.solubank.transactions.dao.CompteDAO;
import com.solubank.transactions.entity.Client;
import com.solubank.transactions.entity.Compte;
import com.solubank.transactions.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ClientService {

    private final ClientDAO clientDAO;
    private final CompteDAO compteDAO;

    public ClientService(ClientDAO clientDAO, CompteDAO compteDAO) {
        this.clientDAO = clientDAO;
        this.compteDAO = compteDAO;
    }

    public Client ajouterClient(String nom, String email) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas etre vide.");
        }
        if (!ValidationUtils.estEmailValide(email)) {
            throw new IllegalArgumentException("Email invalide : " + email);
        }
        return clientDAO.ajouter(new Client(0, nom.trim(), email.trim()));
    }

    public boolean modifierClient(Client client) {
        if (!ValidationUtils.estEmailValide(client.email())) {
            throw new IllegalArgumentException("Email invalide : " + client.email());
        }
        return clientDAO.modifier(client);
    }

    public boolean supprimerClient(int id) {
        return clientDAO.supprimer(id);
    }

    public Optional<Client> rechercherParId(int id) {
        return clientDAO.trouverParId(id);
    }

    public List<Client> rechercherParNom(String nom) {
        return clientDAO.rechercherParNom(nom);
    }

    public List<Client> listerClients() {
        return clientDAO.trouverTous();
    }

    public BigDecimal soldeTotal(int idClient) {
        List<Compte> comptes = compteDAO.trouverParClient(idClient);
        return comptes.stream()
                .map(Compte::getSolde)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long nombreComptes(int idClient) {
        return compteDAO.trouverParClient(idClient).stream().count();
    }
}