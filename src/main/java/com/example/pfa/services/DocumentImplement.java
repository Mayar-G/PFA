package com.example.pfa.services;


import com.example.pfa.entities.DocumentEntity;
import com.example.pfa.entities.UserEntity;
import com.example.pfa.exception.FileStorageException;
import com.example.pfa.exception.ResourceNotFoundException;
import com.example.pfa.repository.DemandeDeCreditRepo;
import com.example.pfa.repository.DocumentRepo;
import com.example.pfa.repository.UserRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentImplement implements DocumentService {
    @Autowired
    private final DocumentRepo documentRepository;
    @Autowired
    private final UserRepo utilisateurRepository;
    @Autowired
    private final DemandeDeCreditRepo demandeCreditRepository;

    @Value("${app.upload.dir:${user.home}/uploads}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new FileStorageException("Impossible de créer le répertoire de téléversement", e);
        }
    }
    @Override
    @Transactional
    public DocumentEntity saveDocument(MultipartFile file, Long utilisateurId) {
        // Vérifier si l'utilisateur existe
        UserEntity utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + utilisateurId));

        // Générer un nom de fichier unique
        String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "_" + file.getOriginalFilename());

        // Vérifier si le nom du fichier est valide
        if (fileName.contains("..")) {
            throw new FileStorageException("Le nom du fichier contient un chemin invalide " + fileName);
        }

        // Créer le chemin complet
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);

        // Copier le fichier
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("Impossible de stocker le fichier " + fileName, ex);
        }

        // Créer l'entité Document
        DocumentEntity document = DocumentEntity.builder()
                .nomDocument(fileName)
                .typeDocument(determinerTypeDocument(file.getContentType()))
                .dateTeleversement(LocalDateTime.now())
                .cheminFichier(targetLocation.toString())
                .tailleFichier(file.getSize())
                .utilisateur(utilisateur)
                .build();

        return documentRepository.save(document);
    }



    @Override
    public DocumentEntity save(MultipartFile file, Long demandeId) {

        DocumentEntity document = new DocumentEntity();
        document.setFileName(file.getOriginalFilename());
        document.setDemandeDeCreditId(demandeId);  // Liez le document à la demande de crédit

        return documentRepository.save(document);
    }
    @Override
    public List<DocumentEntity> findByDemandeCredit(Long demandeId) {
        return documentRepository.findByDemandeCreditId(demandeId);  // Recherchez les documents associés à la demande de crédit
    }
    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Fichier non trouvé " + fileName);
            }
        } catch (MalformedURLException | FileNotFoundException ex) {
            throw new FileStorageException("Fichier non trouvé " + fileName, ex);
        }
    }

    @Override
    public List<DocumentEntity> getDocumentsByUtilisateur(Long utilisateurId) {
        return documentRepository.findByUtilisateurId(utilisateurId);
    }

    @Override
    @Transactional
    public void deleteDocument(Long documentId) {
        DocumentEntity document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé avec l'id: " + documentId));


        try {
            Path filePath = Paths.get(document.getCheminFichier());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Erreur lors de la suppression du fichier physique", e);
            throw new FileStorageException("Impossible de supprimer le fichier physique", e);
        }


        documentRepository.delete(document);
    }

    @Override
    public DocumentEntity getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé avec l'id: " + documentId));
    }

    @Override
    public boolean isDocumentOwner(Long documentId, Long utilisateurId) {
        DocumentEntity document = getDocumentById(documentId);
        return document.getUtilisateur().getId().equals(utilisateurId);
    }

    private String determinerTypeDocument(String contentType) {
        if (contentType == null) {
            return "INCONNU";
        }

        switch (contentType.toLowerCase()) {
            case "application/pdf":
                return "PDF";
            case "image/jpeg":
            case "image/png":
                return "IMAGE";
            case "application/msword":
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return "WORD";
            case "application/vnd.ms-excel":
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return "EXCEL";
            default:
                return "AUTRE";
        }
    }
}
