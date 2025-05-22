package com.capsula.seguridad.Libreria_Java_Book.services.impl;

import com.capsula.seguridad.Libreria_Java_Book.models.Permisos;
import com.capsula.seguridad.Libreria_Java_Book.models.Usuario;
import com.capsula.seguridad.Libreria_Java_Book.repositories.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + email + " no existe en la base de datos."));

        Set<Permisos> permisos = usuario.getRoles().getPermisos();

        List<GrantedAuthority> authorities = permisos.stream()
                .map(permiso -> new SimpleGrantedAuthority(permiso.getNombrePermiso()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRoles().getNombreRol()));

        return new User(
                usuario.getEmail(),
                usuario.getContrasenia(),
                usuario.isEnabled(),
                usuario.isAccountNoExpired(),
                usuario.isCredentialNoExpired(),
                usuario.isAccountNoLocked(),
                authorities);
    }
}