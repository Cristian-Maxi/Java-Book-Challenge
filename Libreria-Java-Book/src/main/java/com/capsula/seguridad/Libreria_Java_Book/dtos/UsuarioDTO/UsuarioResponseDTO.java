package com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO;

import java.util.Set;

public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String email,
        String rol,
        Set<String> categorias,
        Set<String> autores
) {
}
