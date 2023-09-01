package com.trigyn.jws.webstarter;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@EnableJpaRepositories(basePackages = "com.trigyn.jws", includeFilters = {
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = { ".*entities.*" }),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = { ".*dao.*" }),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = { ".*impl.*" }),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = { ".*repository.*" }) 
})
public class JavaWebStarterConfig {
	
	@Bean
	public FluentConfiguration atflywayConfiguration(DataSource dataSource) {
		FluentConfiguration configuration = Flyway.configure().dataSource(dataSource).locations("db/mysql", "db/migration");
		configuration.cleanDisabled(Boolean.TRUE);
		configuration.placeholderReplacement(Boolean.FALSE);
		configuration.ignoreFutureMigrations(Boolean.TRUE);
		configuration.outOfOrder(Boolean.TRUE);
		configuration.ignorePendingMigrations(Boolean.TRUE);
		configuration.ignoreMissingMigrations(Boolean.TRUE);
		configuration.baselineOnMigrate(Boolean.TRUE);
		configuration.baselineVersion("0");
		return configuration;
	}

	@Bean
	public Flyway atmigrateScripts(DataSource dataSource) {
		Flyway flyway = new Flyway(atflywayConfiguration(dataSource));
		flyway.migrate();
		return flyway;
	}

	@ConditionalOnMissingBean
	@Bean
	public DispatcherServlet dispatcherServlet() {
		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return dispatcherServlet;
	}

}
