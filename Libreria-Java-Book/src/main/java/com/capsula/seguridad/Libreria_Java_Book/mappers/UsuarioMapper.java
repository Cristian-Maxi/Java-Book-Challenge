package com.capsula.seguridad.Libreria_Java_Book.mappers;

import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioRequestDTO;
import com.capsula.seguridad.Libreria_Java_Book.dtos.UsuarioDTO.UsuarioResponseDTO;
import com.capsula.seguridad.Libreria_Java_Book.models.Categoria;
import com.capsula.seguridad.Libreria_Java_Book.models.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(source = "categoriasFavoritas", qualifiedByName = "mapCategorias", target = "categorias")
    @Mapping(source = "autoresFavoritos", target = "autores")
    @Mapping(source = "roles.nombreRol", target = "rol")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO);

    @Mapping(source = "categoriasFavoritas", qualifiedByName = "mapCategorias", target = "categorias")
    @Mapping(source = "autoresFavoritos", target = "autores")
    @Mapping(source = "roles.nombreRol", target = "rol")
    List<UsuarioResponseDTO> toResponseListDTO(List<Usuario> usuarios);

    @Named("mapCategorias")
    static Set<String> mapCategorias(Set<Categoria> categorias) {
        return categorias.stream().map(Categoria::getNombre).collect(Collectors.toSet());
    }
}