package com.example.pfa.controllers;



import com.example.pfa.entities.AdministrateurEntity;
import com.example.pfa.entities.UserEntity;
import com.example.pfa.services.AdministrateurService;
import com.example.pfa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrateurs")
public class AdministrateurController {
    @Autowired
    private AdministrateurService as;
    @PostMapping("/add")
    public AdministrateurEntity addAdministrateur(@RequestBody AdministrateurEntity administrateur){
        return as.addAdministrateur(administrateur);
    }
    @PostMapping("/addUser")
    public UserEntity add(@RequestBody UserEntity user) {
        return as.addUser(user);
    }
    @PutMapping("/update/{id}")
    public AdministrateurEntity updateAdministrateur(@RequestBody AdministrateurEntity administrateur,@PathVariable int id ){

        return as.updateAdministrateur(administrateur, id);
    }
    @GetMapping("/getAdministrateurById")
    public AdministrateurEntity getAdministrateurById(@RequestParam("id") int id ){
        return as.getAdministrateurById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAdministrateur(@PathVariable int id){
        as.deleteAdministrateur(id); ;
    }
    @GetMapping("/getAllAdministrateurs")
    public List<AdministrateurEntity> getAllAdministrateurs(){
        return as.getAllAdministrateurs();
    }

}
