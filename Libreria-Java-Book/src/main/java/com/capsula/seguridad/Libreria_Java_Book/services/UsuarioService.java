package com.capsula.seguridad.Libreria_Java_Book.services;

import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioRequestDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioUpdateDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioRequestDTO);
    List<UsuarioResponseDTO> traerUsuarios();
    UsuarioResponseDTO actualizarUsuario(UsuarioUpdateDTO usuarioUpdateDTO);
    void borrarUsuarios(Long id);
    UsuarioResponseDTO encontrarUsuarioConEmail(String email);
}
