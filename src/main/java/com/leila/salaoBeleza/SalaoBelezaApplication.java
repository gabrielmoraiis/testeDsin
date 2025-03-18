package com.leila.salaoBeleza;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Salão de Beleza",
				description = "API responsável pelo salão de beleza",
				version = "1.0"
)
)
public class SalaoBelezaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalaoBelezaApplication.class, args);
	}

}
