package com.capsula.seguridad.Libreria_Java_Book.exceptions;


public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}