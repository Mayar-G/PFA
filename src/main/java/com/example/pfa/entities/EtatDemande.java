package com.example.pfa.entities;


public enum EtatDemande {
    EN_COURS_ANALYSE("En cours d'analyse"),
    EN_ATTENTE_DOCUMENTS("En attente de documents complémentaires"),
    APPROUVE("Approuvé"),
    REJETE("Rejeté"),
    SOUMISE("Soumise");
    private final String libelle;

    EtatDemande(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
