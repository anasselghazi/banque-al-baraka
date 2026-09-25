package com.solubank.transactions.entity;

import java.math.BigDecimal;

public final class CompteCourant extends Compte {

    private final BigDecimal decouvertAutorise;

    public CompteCourant(int id, String numero, BigDecimal solde, int idClient, BigDecimal decouvertAutorise) {
        super(id, numero, solde, idClient);
        this.decouvertAutorise = decouvertAutorise;
    }

    public BigDecimal getDecouvertAutorise() {
        return decouvertAutorise;
    }

    @Override
    public String toString() {
        return "CompteCourant[id=%d, numero=%s, solde=%s, idClient=%d, decouvertAutorise=%s]"
                .formatted(id, numero, solde, idClient, decouvertAutorise);
    }
}