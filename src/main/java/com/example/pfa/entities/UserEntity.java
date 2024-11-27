package com.example.pfa.entities;



import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    @Column(unique = true)
    private String email;
    private String adresse;
    private String telephone;
    private String password;
    private Date creationDate;


    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<DemandeDeCreditEntity> demandes;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<DocumentEntity> documents;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRolename().name()));
        }
        return authorities;
    }

}

