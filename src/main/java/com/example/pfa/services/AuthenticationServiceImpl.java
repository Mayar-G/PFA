package com.example.pfa.services;


import com.example.pfa.entities.Rolename;
import com.example.pfa.entities.Role;
import com.example.pfa.dto.AuthenticationRequest;
import com.example.pfa.dto.AuthenticationResponse;
import com.example.pfa.dto.RegisterRequest;
import com.example.pfa.entities.UserEntity;
import com.example.pfa.repository.RoleRepo;
import com.example.pfa.repository.UserRepo;
import com.example.pfa.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor

public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final RoleRepo roleRepo;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("L'email est déjà utilisé.");
        }

        Role userRole = roleRepo.findByNom(Rolename.USER.name())
                .orElseThrow(() -> new RuntimeException("Role non trouvé"));

        UserEntity user = UserEntity.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .adresse(request.getAdresse())
                .telephone(request.getTelephone())
                .roles(Collections.singleton(userRole)) // Passez un Set<Role>
                .build();

        userRepo.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
