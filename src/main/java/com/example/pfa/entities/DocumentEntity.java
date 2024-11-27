package com.example.pfa.entities;


import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocument;
    @Column(nullable = false)
    private String nomDocument;
    @Column(nullable = false)
    private String typeDocument;
    @Setter
    @Getter
    private String fileName;
    @Column(nullable = false)
    private String cheminFichier;
    private Long demandeDeCreditId;
    @Column(nullable = false)
    private LocalDateTime dateTeleversement = LocalDateTime.now();

    private Long tailleFichier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private UserEntity utilisateur;
    @ManyToOne
    @JoinColumn(name = "demande_credit_id")
    private DemandeDeCreditEntity demandeCredit;
    public String getType() {
        return typeDocument;
    }
}