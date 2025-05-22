package com.capsula.seguridad.Libreria_Java_Book.controllers;

import com.capsula.seguridad.Libreria_Java_Book.dtos.ApiResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.AutenticacionDTO.DatosAutenticacion;
import com.capsula.seguridad.Libreria_Java_Book.dtos.AutenticacionDTO.JWTTokenDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioRequestDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.exceptions.ApplicationException;
import com.capsula.seguridad.Libreria_Java_Book.services.AuthService;
import com.capsula.seguridad.Libreria_Java_Book.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    public AutenticacionController(UsuarioService usuarioService, AuthService authService) {
        this.usuarioService = usuarioService;
        this.authService = authService;
    }

    @PostMapping("/crear")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> crearUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.crearUsuario(usuarioRequestDTO);
        return new ResponseEntity<>(new ApiResponseDTO<>(true, "Usuario Creado", usuarioResponseDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDTO> autenticarUsuario(@RequestBody @Valid DatosAutenticacion datosAutenticacion){
        JWTTokenDTO jwtTokenDTO = authService.autenticar(datosAutenticacion);
        return ResponseEntity.ok(jwtTokenDTO);
    }
}
