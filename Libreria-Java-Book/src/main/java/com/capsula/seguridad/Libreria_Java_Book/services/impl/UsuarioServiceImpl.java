package com.capsula.seguridad.Libreria_Java_Book.services.impl;

import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioRequestDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioUpdateDTO;
import com.capsula.seguridad.Libreria_Java_Book.exceptions.ApplicationException;
import com.capsula.seguridad.Libreria_Java_Book.mappers.UsuarioMapper;
import com.capsula.seguridad.Libreria_Java_Book.models.Categoria;
import com.capsula.seguridad.Libreria_Java_Book.models.Roles;
import com.capsula.seguridad.Libreria_Java_Book.models.Usuario;
import com.capsula.seguridad.Libreria_Java_Book.repositories.CategoriaRepository;
import com.capsula.seguridad.Libreria_Java_Book.repositories.RolesRepository;
import com.capsula.seguridad.Libreria_Java_Book.repositories.UsuarioRepository;
import com.capsula.seguridad.Libreria_Java_Book.services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolesRepository rolesRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolesRepository rolesRepository, CategoriaRepository categoriaRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.rolesRepository = rolesRepository;
        this.categoriaRepository = categoriaRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        if (usuarioRepository.existsByEmail(usuarioRequestDTO.email())) {
            throw new ApplicationException("email", "El email ya existe en la base de datos.");
        }
        String encodedPassword = passwordEncoder.encode(usuarioRequestDTO.contrasenia());
        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);
        usuario.setContrasenia(encodedPassword);
        String rolNombre = usuarioRequestDTO.email().contains("@admin") ? "ADMIN" : "CLIENT";
        Roles rol = rolesRepository.findByNombreRol(rolNombre)
                .orElseThrow(() -> new ApplicationException("rol", "El rol '" + rolNombre + "' no existe."));
        usuario.setRoles(rol);

        if (usuarioRequestDTO.categoriasIds() != null && !usuarioRequestDTO.categoriasIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(usuarioRequestDTO.categoriasIds());

            if (categorias.size() != usuarioRequestDTO.categoriasIds().size()) {
                throw new ApplicationException("categorias", "Algunas categorías no fueron encontradas.");
            }

            usuario.setCategoriasFavoritas(new HashSet<>(categorias));
            usuario.setAutoresFavoritos(usuarioRequestDTO.autores());
        }

        usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> traerUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findByIsEnabledTrue();
        return usuarioMapper.toResponseListDTO(usuarios);
    }

    @Transactional
    @Override
    public UsuarioResponseDTO actualizarUsuario(UsuarioUpdateDTO usuarioUpdateDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioUpdateDTO.id())
                .orElseThrow(() -> new EntityNotFoundException("ID de Usuario no encontrado"));
        if(usuarioUpdateDTO.nombre() != null && !usuarioUpdateDTO.nombre().isBlank()) {
            usuario.setNombre(usuarioUpdateDTO.nombre());
        }
        if(usuarioUpdateDTO.apellido() != null && !usuarioUpdateDTO.apellido().isBlank()) {
            usuario.setApellido(usuarioUpdateDTO.apellido());
        }
        usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    @Override
    public void borrarUsuarios(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El ID del usuario no fue encontrado"));
        usuario.setEnabled(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioResponseDTO encontrarUsuarioConEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("El email del usuario: " + email + " no fue encontrado."));
        return usuarioMapper.toResponseDTO(usuario);
    }
}
