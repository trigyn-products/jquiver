package com.trigyn.jws.dbutils;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DButilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DButilsApplication.class, args);
	}

	@Bean
	public FluentConfiguration atflywayConfiguration(DataSource dataSource) {
		FluentConfiguration configuration = Flyway.configure().dataSource(dataSource).locations("db/mysql", "db/migration");
		configuration.cleanDisabled(Boolean.TRUE);
		configuration.placeholderReplacement(Boolean.FALSE);
		configuration.ignoreFutureMigrations(Boolean.TRUE);
		configuration.outOfOrder(Boolean.TRUE);
		configuration.ignorePendingMigrations(Boolean.TRUE);
		configuration.ignoreMissingMigrations(Boolean.TRUE);
		return configuration;
	}

	@Bean
	public Flyway atmigrateScripts(DataSource dataSource) {
		Flyway flyway = new Flyway(atflywayConfiguration(dataSource));
		flyway.migrate();
		return flyway;
	}

}
