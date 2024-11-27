package com.example.pfa.repository;


import com.example.pfa.entities.Role;
import com.example.pfa.entities.Rolename;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {
    Optional<Role> findByRolename(Rolename rolename);

    Optional<Role> findByNom(String nom);
}
