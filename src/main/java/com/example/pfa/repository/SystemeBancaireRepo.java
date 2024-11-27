package com.example.pfa.repository;


import com.example.pfa.entities.SystemeBancaireEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemeBancaireRepo extends JpaRepository<SystemeBancaireEntity, Integer> {
}
