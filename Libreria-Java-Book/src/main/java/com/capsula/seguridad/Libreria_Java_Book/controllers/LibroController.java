package com.capsula.seguridad.Libreria_Java_Book.controllers;

import com.capsula.seguridad.Libreria_Java_Book.dtos.ApiResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO.LibroRequestDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO.LibroResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.exceptions.ApplicationException;
import com.capsula.seguridad.Libreria_Java_Book.services.LibroService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/libro")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping("/crear")
    public ResponseEntity<ApiResponseDTO<LibroResponseDTO>> crearLibro(@Valid @RequestBody LibroRequestDTO libroRequestDTO) {
        LibroResponseDTO libroResponseDTO = libroService.crearLibro(libroRequestDTO);
        String mensaje = "Libro creado exitosamente";
        return new ResponseEntity<>(new ApiResponseDTO<>(true, mensaje, libroResponseDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarLibro(@PathVariable Long id) {
        libroService.borrarLibro(id);
        String message = "El Libro fue Eliminado Exitosamente";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/encontrarConTitulo/{titulo}")
    public ResponseEntity<ApiResponseDTO<LibroResponseDTO>> encontrarLibroPorTitulo(@PathVariable String titulo) {
        LibroResponseDTO libroResponseDTO = libroService.encontrarLibroPorTitulo(titulo);
        String mensaje = "Libro con Titulo: " + titulo + " Encontrado";
        return new ResponseEntity<>(new ApiResponseDTO<>(true, mensaje, libroResponseDTO), HttpStatus.OK);
    }

    @GetMapping("/encontrarConAutor/{autor}")
    public ResponseEntity<ApiResponseDTO<LibroResponseDTO>>  encontrarLibroPorAutor(@PathVariable String autor) {
        LibroResponseDTO libroResponseDTO = libroService.encontrarLibroPorAutor(autor);
        String mensaje = "Libro con Autor: "+ autor + " Encontrado";
        return new ResponseEntity<>(new ApiResponseDTO<>(true, mensaje, libroResponseDTO), HttpStatus.OK);

    }
}
