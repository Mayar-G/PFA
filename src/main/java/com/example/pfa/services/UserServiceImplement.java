package com.example.pfa.services;


import com.example.pfa.entities.Role;
import com.example.pfa.entities.Rolename;
import com.example.pfa.entities.UserEntity;
import com.example.pfa.repository.RoleRepo;
import com.example.pfa.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRepo ur;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public UserEntity updateUser(UserEntity user, int idUser) {
        UserEntity existingUser = ur.findById((long) idUser).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setNom(user.getNom());
        existingUser.setPrenom(user.getPrenom());
        existingUser.setEmail(user.getEmail());
        return ur.save(existingUser);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return ur.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(Long idUser) {
        ur.deleteById(idUser);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return ur.findAll();
    }

    @Override
    public ResponseEntity<UserEntity> login(String email, String password) {
        UserEntity user = ur.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @Override
    public UserEntity signUp(UserEntity user) {
        if (ur.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Email already in use!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepo.findByNom(String.valueOf(Rolename.USER)).get();
        user.getRoles().add(userRole);
        return ur.save(user);
    }

    @Override
    public void createDefaultAdmin() {
        String adminEmail = "admin@admin.com";
        if (!ur.existsByEmail(adminEmail)) {
            UserEntity admin = new UserEntity();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin_password"));
            admin.setNom("Admin");
            admin.setPrenom("Admin");



            ur.save(admin);
        }
    }

}
