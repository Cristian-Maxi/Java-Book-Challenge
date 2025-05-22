package com.capsula.seguridad.Libreria_Java_Book.services;

import com.capsula.seguridad.Libreria_Java_Book.dtos.AutenticacionDTO.DatosAutenticacion;
import com.capsula.seguridad.Libreria_Java_Book.dtos.AutenticacionDTO.JWTTokenDTO;

public interface AuthService {
    JWTTokenDTO autenticar(DatosAutenticacion datosAutenticacion);
}
