package com.example.pfa.services;


import com.example.pfa.entities.DemandeDeCreditEntity;
import com.example.pfa.entities.EtatDemande;
import com.example.pfa.entities.UserEntity;
import com.example.pfa.exception.ResourceNotFoundException;
import com.example.pfa.repository.DemandeDeCreditRepo;
import com.example.pfa.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DemandeCreditImplement implements DemandeCreditService {

    private final DemandeDeCreditRepo demandeCreditRepository;
    private final UserRepo utilisateurRepository;

    @Override
    public DemandeDeCreditEntity addDemandeCredit(DemandeDeCreditEntity demandeCredit) {
        if (demandeCredit == null) {
            throw new IllegalArgumentException("La demande de crédit ne peut pas être nulle");
        }
        return demandeCreditRepository.save(demandeCredit);
    }

    @Override
    public void deleteDemandeCredit(int idDemandeCredit) {
        if (!demandeCreditRepository.existsById((long) idDemandeCredit)) {
            throw new ResourceNotFoundException("Demande de crédit non trouvée");
        }
        demandeCreditRepository.deleteById((long) idDemandeCredit);
    }

    @Override
    public List<DemandeDeCreditEntity> getAllDemandeCredits() {
        return demandeCreditRepository.findAll();
    }

    @Override
    public DemandeDeCreditEntity soumettreDemande(DemandeDeCreditEntity credit, Long utilisateurId) {
        UserEntity utilisateur = utilisateurRepository.findById(utilisateurId).get();

        credit.setUtilisateur(utilisateur);  // Associe la demande à l'utilisateur
        return demandeCreditRepository.save(credit);
    }

    @Override
    public Optional<DemandeDeCreditEntity> getDemande(Long demandeId) {
        return demandeCreditRepository.findById(demandeId);

    }

    @Override
    public List<DemandeDeCreditEntity> getDemandesUtilisateur(Long utilisateurId) {
        return demandeCreditRepository.findByUtilisateurId(utilisateurId);
    }

    @Override
    public DemandeDeCreditEntity updateEtatDemande(Long demandeId, EtatDemande nouvelEtat) {
        DemandeDeCreditEntity demande = demandeCreditRepository.findById(demandeId).get();

        demande.setEtatDemande(nouvelEtat);  // Mise à jour de l'état de la demande
        return demandeCreditRepository.save(demande);
    }

    @Override
    public void verifierInformationsFinancieres(Long demandeId) {
        DemandeDeCreditEntity demande = demandeCreditRepository.findById(demandeId).get();

        // Logique de vérification des informations financières de la demande
        if (demande.getMontantDemande() <= 0) {
            throw new IllegalArgumentException("Le montant de la demande doit être positif");
        }

        // Ajouter d'autres vérifications financières si nécessaire
    }

    @Override
    public DemandeDeCreditEntity save(DemandeDeCreditEntity demande) {
        return null;
    }

    @Override
    public List<DemandeDeCreditEntity> findByUserAndEtat(UserEntity user, EtatDemande etat) {
        return List.of();
    }

    @Override
    public List<DemandeDeCreditEntity> findByUser(UserEntity user) {
        return List.of();
    }

    @Override
    public Optional<DemandeDeCreditEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<DemandeDeCreditEntity> updateEtat(Long id, EtatDemande etat) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        Optional<DemandeDeCreditEntity> demande = demandeCreditRepository.findById(id);
        if (demande.isPresent()) {
            demandeCreditRepository.deleteById(id);
        } else {
            throw new RuntimeException("Demande de crédit non trouvée avec l'ID : " + id);
        }
    }

    @Override
    public Map<String, Long> getStats() {
        return Map.of();
    }

    @Override
    public Page<DemandeDeCreditEntity> search(String keyword, EtatDemande etat, Pageable pageable) {
        return null;
    }
}
