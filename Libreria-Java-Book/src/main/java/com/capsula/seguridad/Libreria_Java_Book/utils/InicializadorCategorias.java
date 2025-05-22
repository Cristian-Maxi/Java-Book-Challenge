package com.capsula.seguridad.Libreria_Java_Book.utils;

import com.capsula.seguridad.Libreria_Java_Book.models.Categoria;
import com.capsula.seguridad.Libreria_Java_Book.repositories.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InicializadorCategorias implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;

    public InicializadorCategorias(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> categoriasPorDefecto = List.of("Terror", "Ciencia Ficción", "Historia", "Infantil", "Drama");

        for (String nombreCategoria : categoriasPorDefecto) {
            categoriaRepository.findByNombre(nombreCategoria)
                    .orElseGet(() -> categoriaRepository.save(new Categoria(null, nombreCategoria)));
        }

        System.out.println("Categorías inicializadas correctamente.");
    }
}