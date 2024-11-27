package com.example.pfa.entities;


import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemeBancaireEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSysteme;
    private String nomSysteme;
    private String etatVerification;

    public void verifierInformationsFinancieres() {
        // Logique de vérification des informations financières
    }

    public void communiquerEtatDemande() {
        // Logique de communication de l'état de la demande
    }
}
