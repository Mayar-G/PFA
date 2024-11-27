package com.example.pfa.controllers;



import com.example.pfa.entities.DemandeDeCreditEntity;
import com.example.pfa.entities.DocumentEntity;
import com.example.pfa.entities.EtatDemande;
import com.example.pfa.entities.UserEntity;
import com.example.pfa.services.DemandeCreditService;
import com.example.pfa.services.DocumentService;
import com.example.pfa.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/demandes-credit")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DemandeCreditController {
    @Autowired
    private DemandeCreditService demandeCreditService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private DocumentService documentService;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<DemandeDeCreditEntity> creerDemande(@RequestBody DemandeDeCreditEntity demande) throws AccessDeniedException {
        UserEntity utilisateur = securityService.getCurrentUser();
        demande.setUser(utilisateur);
        DemandeDeCreditEntity nouvelleDemande = demandeCreditService.save(demande);
        return new ResponseEntity<>(nouvelleDemande, HttpStatus.CREATED);
    }

    // Récupérer toutes les demandes de crédit de l'utilisateur
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<DemandeDeCreditEntity>> getMesDemandes(@RequestParam(required = false) EtatDemande etat) throws AccessDeniedException {
        UserEntity utilisateur = securityService.getCurrentUser();
        List<DemandeDeCreditEntity> demandes;
        if (etat != null) {
            demandes = demandeCreditService.findByUserAndEtat(utilisateur, etat);
        } else {
            demandes = demandeCreditService.findByUser(utilisateur);
        }
        return ResponseEntity.ok(demandes);
    }

    // Récupérer une demande de crédit spécifique
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_BANKER', 'ROLE_ADMIN')")
    public ResponseEntity<DemandeDeCreditEntity> getDemande(@PathVariable Long id) {
        try {
            securityService.checkUserAuthorization(id);  // Vérifier les autorisations de l'utilisateur
            Optional<DemandeDeCreditEntity> demande = demandeCreditService.findById(id);
            return demande.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Accès refusé
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Autres erreurs
        }
    }

    // Modifier l'état d'une demande de crédit (réservé aux banquiers)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_BANKER')")
    public ResponseEntity<DemandeDeCreditEntity> updateEtatDemande(@PathVariable Long id, @RequestParam EtatDemande nouvelEtat) {
        Optional<DemandeDeCreditEntity> demandeUpdated = demandeCreditService.updateEtat(id, nouvelEtat);
        return demandeUpdated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Ajouter des documents à une demande de crédit
    @PostMapping("/{id}/documents")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<DocumentEntity>> ajouterDocuments(@PathVariable Long id, @RequestParam("files") List<MultipartFile> files) throws AccessDeniedException {
        UserEntity utilisateur = securityService.getCurrentUser();
        try {
            securityService.checkUserAuthorization(id); // Vérifier si l'utilisateur est autorisé à ajouter des documents
            List<DocumentEntity> documents = new ArrayList<>();
            for (MultipartFile file : files) {
                DocumentEntity document = documentService.save(file, id);
                documents.add(document);
            }
            return ResponseEntity.ok(documents);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Accès refusé
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Erreur interne
        }
    }

    // Récupérer les documents associés à une demande de crédit
    @GetMapping("/{id}/documents")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_BANKER')")
    public ResponseEntity<List<DocumentEntity>> getDocuments(@PathVariable Long id) {
        try {
            securityService.checkUserAuthorization(id);
            List<DocumentEntity> documents = documentService.findByDemandeCredit(id);
            return ResponseEntity.ok(documents);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Accès refusé
        }
    }

    // Supprimer une demande de crédit
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> annulerDemande(@PathVariable Long id) {
        try {
            securityService.checkUserAuthorization(id); // Vérifier l'autorisation
            demandeCreditService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }
    }

    // Statistiques des demandes de crédit
    @GetMapping("/statistiques")
    @PreAuthorize("hasRole('ROLE_BANKER')")
    public ResponseEntity<Map<String, Long>> getStatistiques() {
        Map<String, Long> stats = demandeCreditService.getStats();
        return ResponseEntity.ok(stats);
    }

    // Rechercher des demandes de crédit avec des critères
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_BANKER')")
    public ResponseEntity<Page<DemandeDeCreditEntity>> searchDemandes(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) EtatDemande etat,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<DemandeDeCreditEntity> demandes = demandeCreditService.search(keyword, etat, pageRequest);
        return ResponseEntity.ok(demandes);
    }
}
