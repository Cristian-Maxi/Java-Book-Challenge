package com.capsula.seguridad.Libreria_Java_Book.services.impl;

import com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO.LibroRequestDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO.LibroResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.exceptions.AccessDeniedException;
import com.capsula.seguridad.Libreria_Java_Book.exceptions.ApplicationException;
import com.capsula.seguridad.Libreria_Java_Book.mappers.LibroMapper;
import com.capsula.seguridad.Libreria_Java_Book.models.Categoria;
import com.capsula.seguridad.Libreria_Java_Book.models.Libro;
import com.capsula.seguridad.Libreria_Java_Book.models.Usuario;
import com.capsula.seguridad.Libreria_Java_Book.repositories.CategoriaRepository;
import com.capsula.seguridad.Libreria_Java_Book.repositories.LibroRepository;
import com.capsula.seguridad.Libreria_Java_Book.repositories.UsuarioRepository;
import com.capsula.seguridad.Libreria_Java_Book.services.LibroService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroMapper libroMapper;
    private final CacheManager cacheManager;

    public LibroServiceImpl(LibroRepository libroRepository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository, LibroMapper libroMapper, CacheManager cacheManager) {
        this.libroRepository = libroRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.libroMapper = libroMapper;
        this.cacheManager = cacheManager;
    }

    @Caching(put = {
            @CachePut(value = "librosPorTitulo", key = "#result.titulo"),
            @CachePut(value = "librosPorAutor", key = "#result.autor")
    })
    @Transactional(rollbackFor = Exception.class)
    @Override
    public LibroResponseDTO crearLibro(LibroRequestDTO libroRequestDTO) {
        Optional<Libro> libroExistente = libroRepository.findByTituloAndAutor(libroRequestDTO.titulo(), libroRequestDTO.autor());

        if (libroExistente.isPresent()) {
            throw new ApplicationException("titulo/autor", "Ya existe un libro con este título y autor.");
        }

        Libro libro = libroMapper.toEntity(libroRequestDTO);
        if (libroRequestDTO.categoriasIds() != null && !libroRequestDTO.categoriasIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(libroRequestDTO.categoriasIds());

            if (categorias.size() != libroRequestDTO.categoriasIds().size()) {
                throw new ApplicationException("categorias", "Algunas categorías no fueron encontradas.");
            }

            libro.setCategorias(new HashSet<>(categorias));
        }
        libroRepository.save(libro);
        return libroMapper.toResponseDTO(libro);
    }

    @Transactional
    @Override
    public void borrarLibro(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El ID del libro no fue encontrado"));
        libroRepository.delete(libro);

        if (cacheManager.getCache("librosPorTitulo") != null) {
            cacheManager.getCache("librosPorTitulo").evict(libro.getTitulo());
        }
        if (cacheManager.getCache("librosPorAutor") != null) {
            cacheManager.getCache("librosPorAutor").evict(libro.getAutor());
        }
    }

    @Override
    public LibroResponseDTO encontrarLibroPorTitulo(String titulo) {
        Libro libro = encontrarLibroPorTituloDesdeCache(titulo);
        validarAccesoACategorias(libro);
        validarAccesoAAutor(libro);
        return libroMapper.toResponseDTO(libro);
    }

    @Cacheable(value = "librosPorTitulo", key = "#titulo")
    public Libro encontrarLibroPorTituloDesdeCache(String titulo) {
        return libroRepository.findLibroByTitulo(titulo)
                .orElseThrow(() -> new EntityNotFoundException("El Titulo del libro no fue encontrado"));
    }

    @Override
    public LibroResponseDTO encontrarLibroPorAutor(String autor) {
        Libro libro = encontrarLibroPorAutorDesdeCache(autor);
        validarAccesoACategorias(libro);
        validarAccesoAAutor(libro);
        return libroMapper.toResponseDTO(libro);
    }

    @Cacheable(value = "librosPorAutor", key = "#autor")
    public Libro encontrarLibroPorAutorDesdeCache(String autor) {
        return libroRepository.findLibroByAutor(autor)
                .orElseThrow(() -> new EntityNotFoundException("El Autor del libro no fue encontrado"));
    }

    private void validarAccesoACategorias(Libro libro) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Set<String> categoriasUsuario = usuario.getCategoriasFavoritas().stream()
                .map(Categoria::getNombre)
                .collect(Collectors.toSet());

        Set<String> categoriasLibro = libro.getCategorias().stream()
                .map(Categoria::getNombre)
                .collect(Collectors.toSet());

        boolean sinInterseccion = Collections.disjoint(categoriasUsuario, categoriasLibro);

        if (sinInterseccion) {
            throw new AccessDeniedException("No tienes permiso para acceder a este libro. Las categorías no coinciden con tus preferencias.");
        }
    }

    private void validarAccesoAAutor(Libro libro) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Set<String> autoresUsuario = usuario.getAutoresFavoritos();
        String autorDelLibro = libro.getAutor();

        if (!autoresUsuario.contains(autorDelLibro)) {
            throw new AccessDeniedException("No tienes permiso para acceder a libros del autor: " + autorDelLibro);
        }
    }
}