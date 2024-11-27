package com.example.pfa.controllers;

import com.example.pfa.config.JwtAuthenticationFilter;
import com.example.pfa.dto.AuthenticationResponse;
import com.example.pfa.dto.LoginForm;
import com.example.pfa.dto.RegisterRequest;
import com.example.pfa.entities.Role;
import com.example.pfa.entities.Rolename;
import com.example.pfa.entities.UserEntity;
import com.example.pfa.repository.DemandeDeCreditRepo;
import com.example.pfa.repository.RoleRepo;
import com.example.pfa.repository.UserRepo;
import com.example.pfa.security.JwtService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class AuthController {
    @Autowired
    private JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepository;
    private final DemandeDeCreditRepo demandeDeCreditRepo;

    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepository, DemandeDeCreditRepo demandeDeCreditRepo,
                          RoleRepo roleRepository, PasswordEncoder passwordEncoder, JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.demandeDeCreditRepo = demandeDeCreditRepo;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @PostConstruct
    public void createDefaultAdminAccount() {
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            UserEntity adminUser = new UserEntity();
            adminUser.setNom("admin");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setPrenom("Admin");
            adminUser.setCreationDate(new Date());
            Role adminRole = roleRepository.findByRolename(Rolename.ADMIN)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setRolename(Rolename.ADMIN);
                        return roleRepository.save(role);
                    });



            adminRole.setUserManagement(true);
            adminRole.setDemandeManagement(true);


            adminUser.getRoles().add(adminRole);

            userRepository.save(adminUser);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginForm.getEmail(),
                            loginForm.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = ((UserEntity) userDetails).getEmail();
            String token = jwtService.generateToken(authentication);
            System.out.println("Generated JWT token: " + token);

            Optional<UserEntity> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                AuthenticationResponse authResponseDTO = new AuthenticationResponse(token, user);
                return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }



    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerDto) {
        if (userRepository.existsByNom(registerDto.getNom())) {
            return ResponseEntity.badRequest().body("Username is taken!");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already registered!");
        }

       UserEntity user = new UserEntity();
        user.setNom(registerDto.getNom());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setTelephone(registerDto.getTelephone());
        user.setAdresse(registerDto.getAdresse());
        user.setPrenom (registerDto.getPrenom());
        user.setCreationDate(new Date());
        Role defaultRole = roleRepository.findByRolename(Rolename.USER)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setRolename(Rolename.USER);
                    return roleRepository.save(role);
                });


        user.getRoles().add(defaultRole);
        userRepository.save(user);
        return ResponseEntity.ok(user);


    }
}