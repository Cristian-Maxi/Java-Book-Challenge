package com.capsula.seguridad.Libreria_Java_Book.repositories;

import com.capsula.seguridad.Libreria_Java_Book.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByIsEnabledTrue();
}
