package com.capsula.seguridad.Libreria_Java_Book.services;

import com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO.LibroRequestDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO.LibroResponseDTO;

public interface LibroService {
    LibroResponseDTO crearLibro(LibroRequestDTO libroRequestDTO);
    void borrarLibro(Long id);
    LibroResponseDTO encontrarLibroPorTitulo(String titulo);
    LibroResponseDTO encontrarLibroPorAutor(String autor);
}
