package com.capsula.seguridad.Libreria_Java_Book.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasenia;
    @Column(name = "is_enabled")
    private boolean isEnabled = true;
    @Column(name = "account_No_Expired")
    private boolean accountNoExpired = true;
    @Column(name = "account_No_Locked")
    private boolean accountNoLocked = true;
    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired = true;
    @ManyToOne //Muchos usuarios pueden tener el mismo ROL
    @JoinColumn(name = "rol_id")
    private Roles roles;
    @ManyToMany
    @JoinTable(
            name = "usuario_categoria",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categoriasFavoritas = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuarios_autores", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "autor")
    private Set<String> autoresFavoritos = new HashSet<>();
}