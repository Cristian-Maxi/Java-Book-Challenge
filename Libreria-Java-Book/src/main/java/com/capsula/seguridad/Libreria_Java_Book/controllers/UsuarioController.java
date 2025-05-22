package com.capsula.seguridad.Libreria_Java_Book.controllers;

import com.capsula.seguridad.Libreria_Java_Book.dtos.ApiResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioUpdateDTO;
import com.capsula.seguridad.Libreria_Java_Book.exceptions.ApplicationException;
import com.capsula.seguridad.Libreria_Java_Book.services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> actualizarUsuario(@Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.actualizarUsuario(usuarioUpdateDTO);
        String message = "El Usuario fue Actualizado Exitosamente";
        return new ResponseEntity<>(new ApiResponseDTO<>(true, message, usuarioResponseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        usuarioService.borrarUsuarios(id);
        String message = "El Usuario fue Eliminado Exitosamente";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/traerUsuarios")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> traerUsuarios() {
        try {
            List<UsuarioResponseDTO> listaUsuarios = usuarioService.traerUsuarios();
            if (listaUsuarios.isEmpty()) {
                return new ResponseEntity<>(new ApiResponseDTO<>(false, "No se Encontraron Usuarios", listaUsuarios), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponseDTO<>(true, "Usuarios Registrados", listaUsuarios), HttpStatus.OK);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(" Ocurrio un Error " + e.getMessage());
        }
    }

    @GetMapping("/encontrarConEmail/{email}")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> encontrarUsuario(@PathVariable String email) {
        try {
            UsuarioResponseDTO usuarioResponseDTO = usuarioService.encontrarUsuarioConEmail(email);
            return new ResponseEntity<>(new ApiResponseDTO<>(true, "Usuario Encontrado", usuarioResponseDTO), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
