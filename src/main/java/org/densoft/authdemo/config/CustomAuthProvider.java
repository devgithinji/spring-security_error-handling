package org.densoft.authdemo.config;

import org.densoft.authdemo.repository.UserRepo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthProvider(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        return userRepo.findByEmail(email).map(user -> {
            if (!passwordEncoder.matches(password, user.getPassword()))
                throw new BadCredentialsException("Invalid username or password");
            List<SimpleGrantedAuthority> simpleGrantedAuthorities = getGrantedAuthorities(user.getRoles());
            return new UsernamePasswordAuthenticationToken(email, password, simpleGrantedAuthorities);
        }).orElseThrow(() -> new BadCredentialsException("No user not found"));
    }

    private List<SimpleGrantedAuthority> getGrantedAuthorities(String roles) {
        return Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
