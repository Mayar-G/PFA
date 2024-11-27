package com.example.pfa.controllers;


import com.example.pfa.entities.LoginRequest;

import com.example.pfa.entities.UserEntity;

import com.example.pfa.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    private  UserService us;

    @PostConstruct
    public void initAdmin() {
        us.createDefaultAdmin();
    }


    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signUp(@RequestBody UserEntity user) {
        UserEntity newUser = us.signUp(user);
        return ResponseEntity.ok(newUser);
    }



    @PutMapping("/upUser/{id}")
    public UserEntity upUser(@RequestBody UserEntity user, @PathVariable int id) {

        return us.updateUser(user, id);
    }

    @GetMapping("/getByEmail")
    public UserEntity getByEmail(@RequestParam("email") String email) {
        return us.getUserByEmail(email);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        us.deleteUser(id);

    }

    @GetMapping("/getAllUsers")
    public List<UserEntity> getAll() {

        return us.getAllUsers();
    }



    @PostMapping("/signin")
    public ResponseEntity<UserEntity> signIn(@RequestBody LoginRequest loginRequest) {
        return us.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
}




