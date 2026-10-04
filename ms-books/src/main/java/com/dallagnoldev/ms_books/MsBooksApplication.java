package com.dallagnoldev.ms_books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MsBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBooksApplication.class, args);
	}

}
