package com.capsula.seguridad.Libreria_Java_Book.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ApplicationException extends RuntimeException{

    private String campo;

    public ApplicationException(String mensaje) {
        super(mensaje);
    }

    public ApplicationException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }
}