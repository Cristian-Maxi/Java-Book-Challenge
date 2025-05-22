package com.capsula.seguridad.Libreria_Java_Book.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(SecurityFilter securityFilter, UserDetailsService userDetailsService) {
        this.securityFilter = securityFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        //Swagger Endpoints
                        .requestMatchers(HttpMethod.GET,"/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        //Autenticacion Endpoints
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        //Usuarios Endpoints
                        .requestMatchers(HttpMethod.PUT, "/api/usuario/actualizar").hasAnyRole("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuario/eliminar/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuario/traerUsuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/usuario/encontrarConEmail/{email}").hasRole("ADMIN")

                        //Libros Endpoints
                        .requestMatchers(HttpMethod.POST, "/api/libro/crear").hasAuthority("libro.crear")
                        .requestMatchers(HttpMethod.DELETE, "/api/libro/eliminar/{id}").hasAuthority("libro.eliminar")
                        .requestMatchers(HttpMethod.GET, "/api/encontrarConTitulo/{titulo}").hasAuthority("libro.leer")
                        .requestMatchers(HttpMethod.GET, "/api/encontrarConAutor/{autor}").hasAuthority("libro.leer")

                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .cors(httpSecurityCorsConfigurer -> corsConfigurationSource())
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-User-Authorities", "X-User-Name"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
