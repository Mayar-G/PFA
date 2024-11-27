package com.example.pfa.dto;


import com.example.pfa.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String type = "Bearer";
    private int id;
    private String nom;
    private String email;
    private String role;
    private UserEntity user;
    public AuthenticationResponse(String token, UserEntity user) {
        this.token = token;
        this.user = user;
    }
}
