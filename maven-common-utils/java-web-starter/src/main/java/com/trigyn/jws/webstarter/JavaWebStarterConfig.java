package com.trigyn.jws.webstarter;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
//@EnableJpaRepositories(basePackages = "com.trigyn.jws", includeFilters = {
//		@ComponentScan.Filter(type = FilterType.REGEX, pattern = { ".*entities.*" }),
//		@ComponentScan.Filter(type = FilterType.REGEX, pattern = { ".*dao.*" }),
//		@ComponentScan.Filter(type = FilterType.REGEX, pattern = { ".*impl.*" }),
//		@ComponentScan.Filter(type = FilterType.REGEX, pattern = { ".*repository.*" }) 
//})
public class JavaWebStarterConfig {
	
	@Bean
	public FluentConfiguration atflywayConfiguration(DataSource dataSource) {
		FluentConfiguration configuration = Flyway.configure().dataSource(dataSource).locations("db/mysql", "db/migration");
		configuration.cleanDisabled(Boolean.TRUE);
		configuration.placeholderReplacement(Boolean.FALSE);
//		configuration.ignoreFutureMigrations(Boolean.TRUE);
		configuration.outOfOrder(Boolean.TRUE);
//		configuration.ignorePendingMigrations(Boolean.TRUE);
//		configuration.ignoreMissingMigrations(Boolean.TRUE);
		configuration.baselineOnMigrate(Boolean.TRUE);
		configuration.baselineVersion("0");
		return configuration;
	}

	@Bean
	public Flyway atmigrateScripts(DataSource dataSource) {
		Flyway flyway = new Flyway(atflywayConfiguration(dataSource));
		flyway.repair();
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
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("*")); 
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("MailQueueExecutorPool-");
		executor.initialize();
		return executor;
	}
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	@Bean
	public SessionLocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.US);
		return localeResolver;
	}

	@Bean
	public WebMvcConfigurer configurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
				localeChangeInterceptor.setParamName("lang");
				registry.addInterceptor(localeChangeInterceptor);
			}
		};
	}
	
}
