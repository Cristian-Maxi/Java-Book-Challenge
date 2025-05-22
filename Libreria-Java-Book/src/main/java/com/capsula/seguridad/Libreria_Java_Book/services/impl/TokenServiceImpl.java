package com.capsula.seguridad.Libreria_Java_Book.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.capsula.seguridad.Libreria_Java_Book.models.Categoria;
import com.capsula.seguridad.Libreria_Java_Book.models.Usuario;
import com.capsula.seguridad.Libreria_Java_Book.repositories.UsuarioRepository;
import com.capsula.seguridad.Libreria_Java_Book.security.variablesEnv.SecretKeyConfig;
import com.capsula.seguridad.Libreria_Java_Book.services.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {
    private final SecretKeyConfig secretKeyConfig;
    private final UsuarioRepository usuarioRepository;

    public TokenServiceImpl(SecretKeyConfig secretKeyConfig, UsuarioRepository usuarioRepository) {
        this.secretKeyConfig = secretKeyConfig;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public String generateToken(Authentication authentication) {
        try{
            String username = authentication.getName();

            Usuario userEntity = usuarioRepository .findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no Encontrado"));

            List<String> authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            List<String> scope = authorities.stream()
                    .filter(auth -> !auth.startsWith("ROLE_"))
                    .toList();

            System.out.println("Generating token...");
            Algorithm algorithm = Algorithm.HMAC256(secretKeyConfig.getSECRET_KEY());
            return JWT.create()
                    .withIssuer("Java-Library")
                    .withSubject(username)
                    .withClaim("id", userEntity.getId())
                    .withClaim("authorities", authorities)
                    .withClaim("scope", scope)
                    .withClaim("roles", List.of(userEntity.getRoles().getNombreRol()))
                    .withClaim("categorias", userEntity.getCategoriasFavoritas().stream().map(Categoria::getNombre).toList())
                    .withExpiresAt(Date.from(generateExpirationDate()))
                    .sign(algorithm);

        }catch (JWTCreationException e){
            throw new RuntimeException("Error al crear el token");
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }

    @Override
    public String getUsernameFromToken(String token) {
        if (token == null) {
            throw new RuntimeException("Null Token");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKeyConfig.getSECRET_KEY());
            DecodedJWT verifier = JWT.require(algorithm)
                    .withIssuer("Java-Library")
                    .build()
                    .verify(token);
            return verifier.getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Error verifying token: " + e.getMessage());
        }
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }

    public List<String> getAuthoritiesFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("authorities").asList(String.class);
    }

}