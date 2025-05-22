package com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO;

import jakarta.validation.constraints.NotNull;

public record UsuarioUpdateDTO(
        @NotNull(message = "El ID no debe ser nulo|")
        Long id,
        String nombre,
        String apellido
) {
}
