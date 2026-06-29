package it.pkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.trigyn.*", "archetype.it.*" })
@ServletComponentScan
@EnableJpaRepositories("com.trigyn.*")
@EntityScan("com.trigyn.*")
public class JQuiverApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(JQuiverApplication.class, args);
	}
}
