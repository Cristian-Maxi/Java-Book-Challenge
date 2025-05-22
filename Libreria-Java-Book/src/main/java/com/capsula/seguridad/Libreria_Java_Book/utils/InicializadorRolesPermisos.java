package com.capsula.seguridad.Libreria_Java_Book.utils;

import com.capsula.seguridad.Libreria_Java_Book.models.Permisos;
import com.capsula.seguridad.Libreria_Java_Book.models.Roles;
import com.capsula.seguridad.Libreria_Java_Book.repositories.PermisosRepository;
import com.capsula.seguridad.Libreria_Java_Book.repositories.RolesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InicializadorRolesPermisos implements CommandLineRunner {

    private final PermisosRepository permisosRepository;
    private final RolesRepository rolesRepository;

    public InicializadorRolesPermisos(PermisosRepository permisosRepository, RolesRepository rolesRepository) {
        this.permisosRepository = permisosRepository;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> permisos = List.of("libro.leer", "libro.crear", "libro.eliminar");

        for (String permisoNombre : permisos) {
            permisosRepository.findByNombrePermiso(permisoNombre)
                    .orElseGet(() -> permisosRepository.save(new Permisos(null, permisoNombre)));
        }

        if (rolesRepository.findByNombreRol("ADMIN").isEmpty()) {
            Set<Permisos> todosLosPermisos = new HashSet<>(permisosRepository.findAll());
            Roles rolAdmin = new Roles();
            rolAdmin.setNombreRol("ADMIN");
            rolAdmin.setPermisos(todosLosPermisos);
            rolesRepository.save(rolAdmin);
        }

        if (rolesRepository.findByNombreRol("CLIENT").isEmpty()) {
            Permisos permisoRead = permisosRepository.findByNombrePermiso("libro.leer")
                    .orElseThrow(() -> new RuntimeException("Permiso libro.leer no encontrado"));

            Roles rolClient = new Roles();
            rolClient.setNombreRol("CLIENT");
            rolClient.setPermisos(Set.of(permisoRead));
            rolesRepository.save(rolClient);
        }
        System.out.println("Permisos y roles inicializados correctamente.");
    }
}
