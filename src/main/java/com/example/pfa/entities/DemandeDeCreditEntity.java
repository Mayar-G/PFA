package com.example.pfa.entities;


import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeDeCreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemande;

    @Column(nullable = false)
    private Float montantDemande;

    @Enumerated(EnumType.STRING)
    private EtatDemande etatDemande = EtatDemande.EN_ATTENTE_DOCUMENTS;

    @Column(nullable = false)
    private LocalDateTime dateSoumission = LocalDateTime.now();

    @Column(nullable = false)
    private String typeCredit;

    private double tauxInteret;
    private double revenuMensuelDeclare;
    private double chargesMensuelles;
    @Column(nullable = false)
    private int duree;

    @ManyToOne
    @JoinColumn(name = "idUtilisateur")
    private UserEntity utilisateur;  // Association avec l'utilisateur qui fait la demande

    @OneToMany(mappedBy = "demandeCredit", cascade = CascadeType.ALL)
    private List<DocumentEntity> documents;

    public void soumettre() {
        this.dateSoumission = LocalDateTime.now();

        this.etatDemande = EtatDemande.SOUMISE;

    }


    public boolean estComplete() {
        return documents != null && !documents.isEmpty() &&
                montantDemande > 0 &&
                duree > 0 &&
                typeCredit != null &&
                !typeCredit.isEmpty() &&
                revenuMensuelDeclare > 0;
    }



    // Méthodes getter et setter pour 'montantDemande' et 'utilisateur' (lombok génère ces méthodes)
    public Long getId() {
        return idDemande;
    }

    public void setId(Long id) {
        this.idDemande = id;
    }

    public UserEntity getUser() {
        return utilisateur;
    }

    public void setUser(UserEntity user) {
        this.utilisateur = user;
    }
    public Float getMontantDemande() {
        return montantDemande;
    }

    public void setMontantDemande(Float montantDemande) {
        this.montantDemande = montantDemande;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public UserEntity getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UserEntity utilisateur) {
        this.utilisateur = utilisateur;
    }


}
