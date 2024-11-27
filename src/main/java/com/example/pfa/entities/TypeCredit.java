package com.example.pfa.entities;


public enum TypeCredit { PERSONNEL("Crédit personnel"),
    AUTO("Crédit auto"),
    TRAVAUX("Crédit travaux"),
    EDUCATION("Crédit éducation");

    private final String libelle;

    TypeCredit(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}