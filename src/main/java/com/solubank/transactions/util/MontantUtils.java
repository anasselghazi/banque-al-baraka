package com.solubank.transactions.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Optional;

public class MontantUtils {

    private MontantUtils() {
    }

    public static String formaterMontant(BigDecimal montant) {
        BigDecimal arrondi = montant.setScale(2, RoundingMode.HALF_UP);
        return String.format(Locale.FRANCE, "%,.2f DH", arrondi);
    }

    public static Optional<BigDecimal> parserMontant(String texte) {
        try {
            BigDecimal montant = new BigDecimal(texte.trim().replace(",", "."));
            if (montant.compareTo(BigDecimal.ZERO) <= 0) {
                return Optional.empty();
            }
            return Optional.of(montant);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}