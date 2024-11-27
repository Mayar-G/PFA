package com.example.pfa.services;


import com.example.pfa.entities.AdministrateurEntity;
import com.example.pfa.entities.DemandeDeCreditEntity;
import com.example.pfa.entities.UserEntity;
import org.springframework.data.domain.Page;

import java.util.List;

import java.util.List;
public interface AdministrateurService {
    AdministrateurEntity addAdministrateur(AdministrateurEntity administrateur);
    AdministrateurEntity updateAdministrateur(AdministrateurEntity administrateur, int idAdministrateur);
    AdministrateurEntity getAdministrateurById(int idAdministrateur);
    void deleteAdministrateur(int idAdministrateur);
    List<AdministrateurEntity> getAllAdministrateurs();
    UserEntity addUser(UserEntity user);
    List<DemandeDeCreditEntity> getAllDemandes();
    DemandeDeCreditEntity approuverDemande(Long demandeId);
    DemandeDeCreditEntity rejeterDemande(Long demandeId);
    Page<UserEntity> getUtilisateursPageable(int page, int size);
    Page<DemandeDeCreditEntity> getDemandesPageable(int page, int size);


}
