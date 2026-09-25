package com.example.pesquisa_ia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PesquisaIaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PesquisaIaApplication.class, args);
	}

}
