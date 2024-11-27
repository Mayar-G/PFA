package com.example.pfa.services;


import java.util.List;


import com.example.pfa.entities.UserEntity;
import org.springframework.http.ResponseEntity;

public interface UserService {



    UserEntity updateUser(UserEntity user, int idUser);
    UserEntity getUserByEmail(String email);



    void deleteUser(Long idUser);

    List<UserEntity> getAllUsers();


    ResponseEntity<UserEntity> login(String email, String password);

    UserEntity signUp(UserEntity user);
    void createDefaultAdmin();


}
