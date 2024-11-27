package com.example.pfa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginForm {

    @Email(message = "L'email n'est pas valide")
    @NotEmpty(message = "L'email est requis")
    private String email;

    @NotEmpty(message = "Le mot de passe est requis")
    private String password;

}