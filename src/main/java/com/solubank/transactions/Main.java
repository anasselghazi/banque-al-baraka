package com.solubank.transactions;

import com.solubank.transactions.dao.CompteDAO;
import com.solubank.transactions.dao.TransactionDAO;
import com.solubank.transactions.entity.TypeTransaction;
import com.solubank.transactions.service.TransactionService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println("Banque Al Baraka - Test TransactionService");

        TransactionService service = new TransactionService(new TransactionDAO(), new CompteDAO());

        System.out.println("\nMoyenne compte 1 : " + service.moyenneParCompte(1));
        System.out.println("Total transactions client 1 (Yasmine) : " + service.totalParClient(1));

        System.out.println("\nGroupement par type :");
        service.grouperParType().forEach((type, list) ->
                System.out.println("  " + type + " : " + list.size() + " transactions"));

        System.out.println("\nFiltre montant > 10000 :");
        service.filtrerParMontant(BigDecimal.valueOf(10000), BigDecimal.valueOf(999999))
                .forEach(t -> System.out.println("  " + t));
    }
}