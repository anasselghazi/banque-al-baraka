package com.solubank.transactions.service;

import com.solubank.transactions.dao.CompteDAO;
import com.solubank.transactions.dao.TransactionDAO;
import com.solubank.transactions.entity.Transaction;
import com.solubank.transactions.entity.TypeTransaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final CompteDAO compteDAO;

    public TransactionService(TransactionDAO transactionDAO, CompteDAO compteDAO) {
        this.transactionDAO = transactionDAO;
        this.compteDAO = compteDAO;
    }

    // ----- Listage -----

    public List<Transaction> listerParCompte(int idCompte) {
        return transactionDAO.trouverParCompte(idCompte).stream()
                .sorted(Comparator.comparing(Transaction::date))
                .collect(Collectors.toList());
    }

    public List<Transaction> listerParClient(int idClient) {
        List<Integer> idsComptes = compteDAO.trouverParClient(idClient).stream()
                .map(c -> c.getId())
                .toList();

        return transactionDAO.trouverTous().stream()
                .filter(t -> idsComptes.contains(t.idCompte()))
                .sorted(Comparator.comparing(Transaction::date))
                .collect(Collectors.toList());
    }

    // ----- Filtres -----

    public List<Transaction> filtrerParMontant(BigDecimal min, BigDecimal max) {
        return transactionDAO.trouverTous().stream()
                .filter(t -> t.montant().compareTo(min) >= 0 && t.montant().compareTo(max) <= 0)
                .collect(Collectors.toList());
    }

    public List<Transaction> filtrerParType(TypeTransaction type) {
        return transactionDAO.trouverTous().stream()
                .filter(t -> t.type() == type)
                .collect(Collectors.toList());
    }

    public List<Transaction> filtrerParPeriode(LocalDate debut, LocalDate fin) {
        return transactionDAO.trouverTous().stream()
                .filter(t -> !t.date().toLocalDate().isBefore(debut) && !t.date().toLocalDate().isAfter(fin))
                .collect(Collectors.toList());
    }

    public List<Transaction> filtrerParLieu(String lieu) {
        return transactionDAO.trouverTous().stream()
                .filter(t -> t.lieu().equalsIgnoreCase(lieu))
                .collect(Collectors.toList());
    }

    // ----- Groupement -----

    public Map<TypeTransaction, List<Transaction>> grouperParType() {
        return transactionDAO.trouverTous().stream()
                .collect(Collectors.groupingBy(Transaction::type));
    }

    public Map<YearMonth, List<Transaction>> grouperParMois() {
        return transactionDAO.trouverTous().stream()
                .collect(Collectors.groupingBy(t -> YearMonth.from(t.date())));
    }

    // ----- Statistiques -----

    public BigDecimal moyenneParCompte(int idCompte) {
        List<Transaction> transactions = transactionDAO.trouverParCompte(idCompte);
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = transactions.stream()
                .map(Transaction::montant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(transactions.size()), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal totalParClient(int idClient) {
        return listerParClient(idClient).stream()
                .map(Transaction::montant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}