package com.capsula.seguridad.Libreria_Java_Book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LibreriaJavaBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibreriaJavaBookApplication.class, args);
	}

}
