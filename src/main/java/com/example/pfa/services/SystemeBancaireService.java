package com.example.pfa.services;


import com.example.pfa.entities.DemandeDeCreditEntity;
import com.example.pfa.entities.SystemeBancaireEntity;

import java.util.List;

public interface SystemeBancaireService {
    SystemeBancaireEntity addSystemeBancaire(SystemeBancaireEntity systemeBancaire);
    SystemeBancaireEntity updateSystemeBancaire(SystemeBancaireEntity systemeBancaire, int idSystemeBancaire);
    SystemeBancaireEntity getSystemeBancaireById(int idSystemeBancaire);
    void deleteSystemeBancaire(int idSystemeBancaire);
    List<SystemeBancaireEntity> getAllSystemeBancaires();

    boolean verifierInformationsFinancieres(DemandeDeCreditEntity demande);
}
