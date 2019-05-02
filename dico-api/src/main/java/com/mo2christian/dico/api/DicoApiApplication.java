package com.mo2christian.dico.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DicoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DicoApiApplication.class, args);
	}

	@Bean()
	public Logger getLogger(){
		return LogManager.getLogger(this.getClass());
	}
}
