package org.densoft.authdemo.controller;

import org.densoft.authdemo.dto.LoginDTO;
import org.densoft.authdemo.dto.RegisterDTO;
import org.densoft.authdemo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        String token = authService.login(loginDTO);

        Map<String, String> body = new HashMap<>();
        body.put("message", "logged in successfully");
        body.put("token", token);

        return ResponseEntity.ok(body);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {

        authService.register(registerDTO);

        Map<String, String> body = new HashMap<>();
        body.put("message", "user created successfully");

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }
}
