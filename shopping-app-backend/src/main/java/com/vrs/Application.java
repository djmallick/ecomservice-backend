package com.vrs;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Application{
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
