package com.solubank.transactions.entity;

import java.math.BigDecimal;

public sealed abstract class Compte permits CompteCourant, CompteEpargne {

    protected final int id;
    protected final String numero;
    protected final BigDecimal solde;
    protected final int idClient;

    protected Compte(int id, String numero, BigDecimal solde, int idClient) {
        this.id = id;
        this.numero = numero;
        this.solde = solde;
        this.idClient = idClient;
    }

    public int getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public int getIdClient() {
        return idClient;
    }

    @Override
    public String toString() {
        return "Compte[id=%d, numero=%s, solde=%s, idClient=%d]"
                .formatted(id, numero, solde, idClient);
    }
}