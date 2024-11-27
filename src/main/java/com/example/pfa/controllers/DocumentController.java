package com.example.pfa.controllers;


import com.example.pfa.entities.DocumentEntity;
import com.example.pfa.exception.FileStorageException;
import com.example.pfa.services.DocumentService;
import com.example.pfa.services.SecurityService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentController {
    @Autowired
    private final DocumentService documentService;
    @Autowired
    private final SecurityService securityService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentEntity> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("utilisateurId") Long utilisateurId) {
        try {
            securityService.checkUserAuthorization(utilisateurId);
            DocumentEntity document = documentService.saveDocument(file, utilisateurId);
            return ResponseEntity.ok(document);
        } catch (IOException | AccessDeniedException e) {
            throw new FileStorageException("Échec du téléversement du fichier", e);
        }
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) throws AccessDeniedException {
        DocumentEntity document = documentService.getDocumentById(documentId);
        securityService.checkUserAuthorization((long) document.getUtilisateur().getId());

        Resource resource = documentService.loadFileAsResource(document.getNomDocument());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + document.getNomDocument() + "\"")
                .body(resource);
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<DocumentEntity>> getDocumentsByUtilisateur(@PathVariable Long utilisateurId) throws AccessDeniedException {
        securityService.checkUserAuthorization(utilisateurId);
        List<DocumentEntity> documents = documentService.getDocumentsByUtilisateur(utilisateurId);
        return ResponseEntity.ok(documents);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) throws AccessDeniedException {
        DocumentEntity document = documentService.getDocumentById(documentId);
        securityService.checkUserAuthorization(document.getUtilisateur().getId());
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
