package com.capsula.seguridad.Libreria_Java_Book.repositories;

import com.capsula.seguridad.Libreria_Java_Book.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByNombreRol(String nombre);
}