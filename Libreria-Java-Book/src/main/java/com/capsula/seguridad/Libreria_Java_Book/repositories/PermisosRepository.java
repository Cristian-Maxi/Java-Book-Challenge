package com.capsula.seguridad.Libreria_Java_Book.repositories;

import com.capsula.seguridad.Libreria_Java_Book.models.Permisos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisosRepository extends JpaRepository<Permisos,Long> {
    Optional<Permisos> findByNombrePermiso(String permisoNombre);
}
