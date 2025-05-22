package com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record LibroRequestDTO(
        @NotBlank(message = "El titulo no debe estar vacio")
        String titulo,
        @NotBlank(message = "El autor no debe estar vacio")
        String autor,
        @NotNull(message = "La cantidad de paginas no debe ser nula")
        Integer cantidadPaginas,
        @NotBlank(message = "El contenido no debe estar vacio")
        String contenido,
        @NotNull(message = "El ID no debe ser nulo")
        Set<Long> categoriasIds
) {
}
