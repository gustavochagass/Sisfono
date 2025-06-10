
package com.SisfonoGroup.Sisfono.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;


@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration:3600000}") // Valor padrão de 1 hora
    private long validityInMilliseconds;

    private Key secretKey; // Para armazenar a chave segura


    private Key getSecretKey() {
        if (this.secretKey == null) {
            // Gere uma chave segura a partir da sua string secreta
            this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
        }
        return this.secretKey;
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // Adiciona a role como um claim
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // Use a chave segura
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token); // Use parserBuilder
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Logar o erro para depuração
            System.err.println("Erro na validação do token JWT: " + e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody(); // Use parserBuilder
        String role = claims.get("role", String.class);
        // Garanta que a role seja prefixada com "ROLE_" para o Spring Security
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, Collections.singletonList(authority));
    }
}