package com.example.pfa.services;


import com.example.pfa.entities.DemandeDeCreditEntity;
import com.example.pfa.entities.EtatDemande;
import com.example.pfa.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DemandeCreditService {
    DemandeDeCreditEntity addDemandeCredit(DemandeDeCreditEntity demandeCredit);

    void deleteById(Long id);
    void deleteDemandeCredit(int idDemandeCredit);

    List<DemandeDeCreditEntity> getAllDemandeCredits();

    DemandeDeCreditEntity soumettreDemande(DemandeDeCreditEntity credit, Long utilisateurId);

    Optional<DemandeDeCreditEntity> getDemande(Long demandeId);

    List<DemandeDeCreditEntity> getDemandesUtilisateur(Long utilisateurId);

    DemandeDeCreditEntity updateEtatDemande(Long demandeId, EtatDemande nouvelEtat);

    void verifierInformationsFinancieres(Long demandeId);

    DemandeDeCreditEntity save(DemandeDeCreditEntity demande);
    List<DemandeDeCreditEntity> findByUserAndEtat(UserEntity user, EtatDemande etat);
    List<DemandeDeCreditEntity> findByUser(UserEntity user);
    Optional<DemandeDeCreditEntity> findById(Long id);
    Optional<DemandeDeCreditEntity> updateEtat(Long id, EtatDemande etat);
    Map<String, Long> getStats();
    Page<DemandeDeCreditEntity> search(String keyword, EtatDemande etat, Pageable pageable);
}
