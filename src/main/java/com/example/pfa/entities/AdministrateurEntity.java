package com.example.pfa.entities;



import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrateurEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAdmin;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;

    public void gererDemandes() {
        // Logique de gestion des demandes
    }

    public void approuverDemande() {
        // Logique d'approbation de demande
    }

    public void rejeterDemande() {
        // Logique de rejet de demande
    }
}
