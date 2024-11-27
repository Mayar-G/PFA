package com.example.pfa.services;


import com.example.pfa.entities.DocumentEntity;
import io.jsonwebtoken.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    DocumentEntity saveDocument(MultipartFile file, Long utilisateurId);
    DocumentEntity save(MultipartFile file, Long demandeId);
    List<DocumentEntity> findByDemandeCredit(Long demandeId);
    Resource loadFileAsResource(String fileName);
    List<DocumentEntity> getDocumentsByUtilisateur(Long utilisateurId);
    void deleteDocument(Long documentId);
    DocumentEntity getDocumentById(Long documentId);
    boolean isDocumentOwner(Long documentId, Long utilisateurId);

}
