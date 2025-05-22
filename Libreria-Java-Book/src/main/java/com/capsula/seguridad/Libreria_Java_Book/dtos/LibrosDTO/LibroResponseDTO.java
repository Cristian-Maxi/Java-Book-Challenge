package com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO;

import java.util.Set;

public record LibroResponseDTO(
        Long id,
        String titulo,
        String autor,
        Integer cantidadPaginas,
        String contenido,
        Set<String> categorias
) {
}
