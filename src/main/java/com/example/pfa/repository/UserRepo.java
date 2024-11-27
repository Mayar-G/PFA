package com.example.pfa.repository;


import com.example.pfa.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {



    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByNom(String nom);
    boolean existsByNom(String nom);
    Optional<UserEntity> findById(Long utilisateurId);

    boolean existsByEmail(String email);
}
