package org.densoft.authdemo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtService {

    @Value("${jwt.secret.key}")
    private String JWT_KEY;

    @Value("${spring.application.name}")
    private String APPLICATION_NAME;


    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        String authorities = getAuthorities(authentication.getAuthorities());

        return Jwts.builder().setIssuer(APPLICATION_NAME).setSubject("JWT Token")
                .claim("email", email)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 3000000))
                .signWith(getSecretKey())
                .compact();
    }

    private String getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    }

    public Authentication validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = String.valueOf(claims.get("email"));
        String authorities = String.valueOf(claims.get("authorities"));
        List<GrantedAuthority> authoritiesList = Arrays.stream(authorities.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(username, null, authoritiesList);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
    }


}
