package com.capsula.seguridad.Libreria_Java_Book.mappers;

import com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO.LibroRequestDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.LibrosDTO.LibroResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.models.Categoria;
import com.capsula.seguridad.Libreria_Java_Book.models.Libro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LibroMapper {
    LibroMapper INSTANCE = Mappers.getMapper(LibroMapper.class);

    @Mapping(source = "categorias", qualifiedByName = "mapCategorias", target = "categorias")
    LibroResponseDTO toResponseDTO(Libro libro);

    Libro toEntity(LibroRequestDTO libroRequstDTO);

    @Named("mapCategorias")
    static Set<String> mapCategorias(Set<Categoria> categorias) {
        return categorias.stream().map(Categoria::getNombre).collect(Collectors.toSet());
    }
}