package com.example.pfa.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private Rolename rolename;
    private String nom;
    private boolean userManagement;
    private boolean demandeManagement;
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();
}
