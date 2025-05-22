package com.capsula.seguridad.Libreria_Java_Book.services.impl;

import com.capsula.seguridad.Libreria_Java_Book.dtos.AutenticacionDTO.DatosAutenticacion;
import com.capsula.seguridad.Libreria_Java_Book.dtos.AutenticacionDTO.JWTTokenDTO;
import com.capsula.seguridad.Libreria_Java_Book.services.AuthService;
import com.capsula.seguridad.Libreria_Java_Book.services.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public JWTTokenDTO autenticar(DatosAutenticacion datosAutenticacion) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(datosAutenticacion.email(), datosAutenticacion.contrasenia())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenService.generateToken(authentication);
        return new JWTTokenDTO(token);
    }
}
