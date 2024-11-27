package com.example.pfa.controllers;


import com.example.pfa.entities.SystemeBancaireEntity;
import com.example.pfa.services.SystemeBancaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/systemes-bancaires")
public class SystemeBancaireController {

    @Autowired
    private SystemeBancaireService systemeBancaireService;

    @PostMapping
    public SystemeBancaireEntity addSystemeBancaire(@RequestBody SystemeBancaireEntity systemeBancaire) {
        return systemeBancaireService.addSystemeBancaire(systemeBancaire);
    }

    @PutMapping("/{id}")
    public SystemeBancaireEntity updateSystemeBancaire(@RequestBody SystemeBancaireEntity systemeBancaire, @PathVariable int id) {
        return systemeBancaireService.updateSystemeBancaire(systemeBancaire, id);
    }

    @GetMapping("/{id}")
    public SystemeBancaireEntity getSystemeBancaireById(@PathVariable int id) {
        return systemeBancaireService.getSystemeBancaireById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSystemeBancaire(@PathVariable int id) {
        systemeBancaireService.deleteSystemeBancaire(id);
    }

    @GetMapping
    public List<SystemeBancaireEntity> getAllSystemeBancaires() {
        return systemeBancaireService.getAllSystemeBancaires();
    }
}
