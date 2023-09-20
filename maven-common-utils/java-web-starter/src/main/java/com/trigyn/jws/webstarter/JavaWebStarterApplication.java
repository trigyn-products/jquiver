package com.trigyn.jws.webstarter;

import java.util.Locale;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.trigyn.jws.dynarest.service.FilesStorageService;

@SpringBootApplication
@EnableAsync
@EnableWebSecurity
public class JavaWebStarterApplication {
	
	@Autowired
	private FilesStorageService filesStorageService = null;


	public static void main(String[] args) {
		SpringApplication.run(JavaWebStarterApplication.class, args);
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
	
	@PostConstruct
	public void initFileStorage() {
		filesStorageService.init();
	}


}
