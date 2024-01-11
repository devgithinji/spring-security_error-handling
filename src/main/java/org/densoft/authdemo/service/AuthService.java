package org.densoft.authdemo.service;

import org.densoft.authdemo.dto.LoginDTO;
import org.densoft.authdemo.dto.RegisterDTO;
import org.densoft.authdemo.model.User;
import org.densoft.authdemo.repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserRepo userRepo,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        return jwtService.generateToken(authentication);
    }

    public void register(RegisterDTO registerDTO) {
        userRepo.findByEmailOrPhoneNumber(registerDTO.getEmail(), registerDTO.getPhoneNumber()).ifPresent(user -> {
            throw new RuntimeException("user found with similar details");
        });

        User user = new User(
                registerDTO.getName(),
                registerDTO.getEmail(),
                registerDTO.getPhoneNumber(),
                passwordEncoder.encode(registerDTO.getPassword()),
                "ROLE_USER");

        userRepo.save(user);
    }


}
