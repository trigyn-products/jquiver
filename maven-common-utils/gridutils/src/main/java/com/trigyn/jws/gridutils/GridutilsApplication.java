package com.trigyn.jws.gridutils;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GridutilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GridutilsApplication.class, args);
	}

}
