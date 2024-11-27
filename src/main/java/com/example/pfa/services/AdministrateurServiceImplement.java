package com.example.pfa.services;


import com.example.pfa.entities.*;
import com.example.pfa.exception.ResourceNotFoundException;
import com.example.pfa.repository.AdministrateurRepo;
import com.example.pfa.repository.DemandeDeCreditRepo;
import com.example.pfa.repository.RoleRepo;
import com.example.pfa.repository.UserRepo;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministrateurServiceImplement implements AdministrateurService {
    @Autowired
    AdministrateurRepo ar;
    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final DemandeDeCreditRepo demandeRepository;
    private final SystemeBancaireService systemeBancaireService;
    private final EmailService emailService;
    @Override
    public AdministrateurEntity addAdministrateur(AdministrateurEntity administrateur) {
        return ar.save(administrateur);
    }

    @Override
    public AdministrateurEntity updateAdministrateur(AdministrateurEntity administrateur, int idAdministrateur) {
        AdministrateurEntity a = ar.findById(idAdministrateur).orElseThrow(() -> new RuntimeException("Administrateur not found"));
        a.setNom(administrateur.getNom());
        a.setPrenom(administrateur.getPrenom());
        a.setEmail(administrateur.getEmail());
        return ar.save(a);
    }

    @Override
    public AdministrateurEntity getAdministrateurById(int idAdministrateur) {
        return ar.findById(idAdministrateur).orElseThrow(() -> new RuntimeException("Administrateur not found"));
    }

    @Override
    public void deleteAdministrateur(int idAdministrateur) {
        ar.deleteById(idAdministrateur);
    }

    @Override
    public List<AdministrateurEntity> getAllAdministrateurs() {
        return ar.findAll();
    }

    @Override
    public List<DemandeDeCreditEntity> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    @Transactional
    public DemandeDeCreditEntity approuverDemande(Long demandeId) {
        DemandeDeCreditEntity demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande non trouvée avec l'id: " + demandeId));

        // Vérifier les informations financières
        boolean verificationOk = systemeBancaireService.verifierInformationsFinancieres(demande);

        if (verificationOk) {
            // Si la vérification est réussie, mettre à jour l'état de la demande
            demande.setEtatDemande(EtatDemande.APPROUVE);
            DemandeDeCreditEntity demandeUpdated = demandeRepository.save(demande);

            // Envoyer un email à l'utilisateur pour lui notifier l'approbation de la demande
            try {
                emailService.envoyerEmailStatusDemande(demande.getUtilisateur().getEmail(),
                        demande.getUtilisateur().getNom(),
                        "APPROUVE",
                        String.valueOf(demande.getId()));
            } catch (MessagingException e) {
                e.printStackTrace(); // Gérer les erreurs d'envoi d'email
            }

            return demandeUpdated;
        } else {
            System.out.println("La vérification des informations financières a échoué");
            return null;
        }
    }



    @Override
    @Transactional
    public DemandeDeCreditEntity rejeterDemande(Long demandeId) {
        DemandeDeCreditEntity demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande non trouvée avec l'id: " + demandeId));

        // Exemple : Si EtatDemande est un enum
        demande.setEtatDemande(EtatDemande.REJETE);

        DemandeDeCreditEntity demandeUpdated = demandeRepository.save(demande);

        // Envoyer un email à l'utilisateur pour lui notifier du rejet de la demande
        try {
            emailService.envoyerEmailStatusDemande(demande.getUtilisateur().getEmail(),
                    demande.getUtilisateur().getNom(),
                    "REJETE",  // Nouveau statut
                    String.valueOf(demande.getId()));  // Numéro de la demande
        } catch (MessagingException e) {
            e.printStackTrace(); // Gérer l'erreur d'envoi d'email
        }

        return demandeUpdated;
    }


    @Override
    public Page<UserEntity> getUtilisateursPageable(int page, int size) {
        return userRepo.findAll(PageRequest.of(page, size));
    }


    @Override
    public Page<DemandeDeCreditEntity> getDemandesPageable(int page, int size) {
        return demandeRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public UserEntity addUser(UserEntity user) {
        Role role = roleRepo.findByNom(String.valueOf(Rolename.USER)).get();
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
