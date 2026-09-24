package com.solubank.transactions;

import com.solubank.transactions.dao.DBConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Banque Al Baraka - Analyse des Transactions Bancaires");

        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connexion a la base de donnees reussie !");
        } catch (Exception e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }
}