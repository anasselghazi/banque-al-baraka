package com.solubank.transactions.entity;

import java.math.BigDecimal;

public final class CompteEpargne extends Compte {

    private final BigDecimal tauxInteret;

    public CompteEpargne(int id, String numero, BigDecimal solde, int idClient, BigDecimal tauxInteret) {
        super(id, numero, solde, idClient);
        this.tauxInteret = tauxInteret;
    }

    public BigDecimal getTauxInteret() {
        return tauxInteret;
    }

    @Override
    public String toString() {
        return "CompteEpargne[id=%d, numero=%s, solde=%s, idClient=%d, tauxInteret=%s]"
                .formatted(id, numero, solde, idClient, tauxInteret);
    }
}