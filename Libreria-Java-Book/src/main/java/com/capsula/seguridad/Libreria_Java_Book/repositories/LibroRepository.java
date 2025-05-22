package com.capsula.seguridad.Libreria_Java_Book.repositories;

import com.capsula.seguridad.Libreria_Java_Book.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findLibroByTitulo(String titulo);
    Optional<Libro> findLibroByAutor(String autor);
    Optional<Libro> findByTituloAndAutor(String titulo, String autor);
}