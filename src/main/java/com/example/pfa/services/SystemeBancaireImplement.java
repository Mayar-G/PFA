package com.example.pfa.services;


import com.example.pfa.entities.DemandeDeCreditEntity;
import com.example.pfa.entities.SystemeBancaireEntity;
import com.example.pfa.repository.SystemeBancaireRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class SystemeBancaireImplement implements SystemeBancaireService {
    @Autowired
    SystemeBancaireRepo sbr;

    @Override
    public SystemeBancaireEntity addSystemeBancaire(SystemeBancaireEntity systemeBancaire) {
        return sbr.save(systemeBancaire);
    }

    @Override
    public SystemeBancaireEntity updateSystemeBancaire(SystemeBancaireEntity systemeBancaire, int idSystemeBancaire) {
        SystemeBancaireEntity sb = sbr.findById(idSystemeBancaire).orElseThrow(() -> new RuntimeException("SystemeBancaire not found"));
        // Mettez à jour les champs nécessaires
        return sbr.save(sb);
    }

    @Override
    public SystemeBancaireEntity getSystemeBancaireById(int idSystemeBancaire) {
        return sbr.findById(idSystemeBancaire).orElseThrow(() -> new RuntimeException("SystemeBancaire not found"));
    }

    @Override
    public void deleteSystemeBancaire(int idSystemeBancaire) {
        sbr.deleteById(idSystemeBancaire);
    }

    @Override
    public List<SystemeBancaireEntity> getAllSystemeBancaires() {
        return sbr.findAll();
    }

    @Override
    public boolean verifierInformationsFinancieres(DemandeDeCreditEntity demande) {
        // Vérification du score de crédit
        double scoreCredit = calculerScoreCredit(demande);
        if (scoreCredit < 600) { // Score minimum généralement accepté
            return false;
        }

        // Vérification du ratio dette/revenu
        double ratioDetteRevenu = calculerRatioDetteRevenu(demande);
        if (ratioDetteRevenu > 0.43) { // Ratio maximum généralement accepté
            return false;
        }

        // Vérification du montant minimum et maximum
        if (demande.getMontantDemande() < 1000 || demande.getMontantDemande() > 1000000) {
            return false;
        }

        // Vérification de la durée du prêt
        if (demande.getDuree() < 6 || demande.getDuree() > 360) { // Entre 6 mois et 30 ans
            return false;
        }

        // Vérification des documents requis
        if (!verifierDocumentsRequis(demande)) {
            return false;
        }

        // Vérification de la capacité de remboursement
        if (!verifierCapaciteRemboursement(demande)) {
            return false;
        }

        // Vérification de l'historique des paiements
        if (!verifierHistoriquePaiements(demande.getUtilisateur().getId())) {
            return false;
        }

        return true;
    }

    private double calculerScoreCredit(DemandeDeCreditEntity demande) {
        // Logique de calcul du score de crédit basée sur plusieurs facteurs
        double score = 700; // Score de base

        // Facteurs qui peuvent affecter le score
        if (demande.getRevenuMensuelDeclare() > 5000) {
            score += 50;
        }
        // Si vous avez d'autres critères pour améliorer le score, ajoutez-les ici
        return score;
    }

    private double calculerRatioDetteRevenu(DemandeDeCreditEntity demande) {
        double revenuMensuel = demande.getRevenuMensuelDeclare(); // Revenu mensuel de la demande de crédit
        double totalDettesActuelles = demande.getChargesMensuelles(); // Charges mensuelles de la demande de crédit
        double nouvelleMensualite = calculerMensualite(
                demande.getMontantDemande(),
                demande.getDuree(),
                demande.getTauxInteret()
        );

        return (totalDettesActuelles + nouvelleMensualite) / revenuMensuel;
    }

    private boolean verifierDocumentsRequis(DemandeDeCreditEntity demande) {
        // Liste des documents requis
        List<String> documentsRequis = Arrays.asList(
                "relevesBancaires",
                "justificatifRevenu",
                "bulletinsSalaire",
                "avisImposition"
        );

        // Vérifier si tous les documents requis sont présents
        return demande.getDocuments().stream()
                .map(doc -> doc.getType())  // Remplacez 'getType()' par le bon getter dans DocumentEntity
                .collect(Collectors.toList())
                .containsAll(documentsRequis);
    }

    private boolean verifierCapaciteRemboursement(DemandeDeCreditEntity demande) {
        double revenuMensuel = demande.getRevenuMensuelDeclare();
        double chargesTotal = demande.getChargesMensuelles();
        double mensualiteCredit = calculerMensualite(
                demande.getMontantDemande(),
                demande.getDuree(),
                demande.getTauxInteret()
        );

        // Vérifier si le reste à vivre est suffisant
        double resteAVivre = revenuMensuel - (chargesTotal + mensualiteCredit);
        double seuilMinimumResteAVivre = 1000; // Montant minimum à adapter selon vos critères

        return resteAVivre >= seuilMinimumResteAVivre;
    }

    private double calculerMensualite(double montant, int duree, double tauxAnnuel) {
        double tauxMensuel = tauxAnnuel / 12;
        return (montant * tauxMensuel) / (1 - Math.pow(1 + tauxMensuel, -duree));
    }

    private boolean verifierHistoriquePaiements(Long userId) {
        // Vérification de l'historique des paiements dans la base de données
        // Cette méthode pourrait vérifier s'il y a eu des retards de paiement
        return true; // À implémenter selon vos besoins
    }


}

