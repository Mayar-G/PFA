package com.example.pfa.repository;


import com.example.pfa.entities.AdministrateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrateurRepo extends JpaRepository<AdministrateurEntity, Integer> {
}
