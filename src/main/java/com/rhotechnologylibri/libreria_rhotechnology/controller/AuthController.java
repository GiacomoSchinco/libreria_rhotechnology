package com.rhotechnologylibri.libreria_rhotechnology.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.AuthRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.User;
import com.rhotechnologylibri.libreria_rhotechnology.exception.DuplicateResourceException;
import com.rhotechnologylibri.libreria_rhotechnology.exception.UnauthorizedException;
import com.rhotechnologylibri.libreria_rhotechnology.repository.UserRepository;
import com.rhotechnologylibri.libreria_rhotechnology.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Utente", "username", request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Utente registrato");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO request) {
        User found = userRepository.findByUsername(request.getUsername()).orElse(null);

        // stesso messaggio per utente inesistente e password errata: non rivelare quali username esistono
        if (found == null || !passwordEncoder.matches(request.getPassword(), found.getPassword())) {
            throw new UnauthorizedException("Credenziali errate");
        }

        String token = jwtService.generateToken(found.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public Map<String, String> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Autenticazione richiesta: token mancante o non valido");
        }
        return Map.of("username", authentication.getName());
    }
}
