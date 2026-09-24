package com.solubank.transactions.util;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private ValidationUtils() {
    }

    public static boolean estEmailValide(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static int lireEntier(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String ligne = sc.nextLine().trim();
            try {
                return Integer.parseInt(ligne);
            } catch (NumberFormatException e) {
                System.out.println("Valeur invalide, veuillez entrer un nombre entier.");
            }
        }
    }

    public static BigDecimal lireMontant(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String ligne = sc.nextLine();
            Optional<BigDecimal> montant = MontantUtils.parserMontant(ligne);
            if (montant.isPresent()) {
                return montant.get();
            }
            System.out.println("Montant invalide, veuillez entrer un nombre positif (ex. 1500.00).");
        }
    }

    public static String lireTexteNonVide(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String ligne = sc.nextLine().trim();
            if (!ligne.isEmpty()) {
                return ligne;
            }
            System.out.println("La valeur ne peut pas etre vide.");
        }
    }
}