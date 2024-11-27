package com.example.pfa.services;


import com.example.pfa.entities.UserEntity;
import com.example.pfa.repository.DemandeDeCreditRepo;
import com.example.pfa.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final UserRepo utilisateurRepository;
    @Autowired
    private DemandeDeCreditRepo demandeDeCreditRepository;

    public void checkUserAuthorization(Long utilisateurId) throws AccessDeniedException {
        // Vérification de l'autorisation
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Non autorisé");
        }

        // Récupérer l'utilisateur connecté
        String username = authentication.getName();
        UserEntity utilisateur = utilisateurRepository.findByNom(getCurrentUser().getNom())
                .orElseThrow(() -> new AccessDeniedException("Utilisateur non trouvé"));

        // Vérifier si l'utilisateur a les droits
        if (!utilisateur.getId().equals(Long.valueOf(utilisateurId)) && !hasAdminRole(authentication)) {
            throw new AccessDeniedException("Non autorisé à accéder à ces ressources");
        }
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public UserEntity getCurrentUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Non autorisé");
        }

        String nom = authentication.getName();
        return utilisateurRepository.findByNom(getCurrentUser().getNom())
                .orElseThrow(() -> new AccessDeniedException("Utilisateur non trouvé"));
    }

}
