package com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO;

import jakarta.validation.constraints.*;

import java.util.Set;

public record UsuarioRequestDTO(
        @NotBlank(message = "El nombre no debe estar vacio")
        String nombre,
        @NotBlank(message = "El apellido no debe estar vacio")
        String apellido,
        @Email @NotBlank(message = "El email no debe estar vacio")
        String email,
        @Size(min = 6, message = "La contrasenia debe tener al menos 6 caracteres")
        @NotNull(message = "La contrasenia no debe ser nula")
        String contrasenia,
        @NotNull(message = "El ID no debe ser nulo")
        Set<Long> categoriasIds,
        @NotEmpty(message = "El campo Autores no puede estar vacío")
        Set<String> autores
) {
}
