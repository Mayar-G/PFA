package com.example.pfa.repository;

import com.example.pfa.entities.DemandeDeCreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DemandeDeCreditRepo extends JpaRepository<DemandeDeCreditEntity, Long> {
    List<DemandeDeCreditEntity> findByUtilisateurId(Long utilisateurId);
    Optional<DemandeDeCreditEntity> findById(Long id);// Méthode pour récupérer les demandes par utilisateur
}
