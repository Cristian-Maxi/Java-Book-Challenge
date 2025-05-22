package com.capsula.seguridad.Libreria_Java_Book.repositories;

import com.capsula.seguridad.Libreria_Java_Book.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
}
