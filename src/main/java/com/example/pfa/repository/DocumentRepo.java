package com.example.pfa.repository;


import com.example.pfa.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DocumentRepo extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findByUtilisateurId(Long userId);
    List<DocumentEntity> findByDemandeCreditId(Long demandeId);

}
