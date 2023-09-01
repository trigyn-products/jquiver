package it.pkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "com.trigyn.*", "archetype.it.*" })
public class JQuiverApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(JQuiverApplication.class, args);
	}
}
