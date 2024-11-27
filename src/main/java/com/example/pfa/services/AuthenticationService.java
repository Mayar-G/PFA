package com.example.pfa.services;

import com.example.pfa.dto.AuthenticationRequest;
import com.example.pfa.dto.AuthenticationResponse;
import com.example.pfa.dto.RegisterRequest;
public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
}
