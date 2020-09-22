package com.trigyn.jws.templating;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;

@SpringBootApplication
public class TemplatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplatingApplication.class, args);
	}

	@Bean
	public FreeMarkerConfigurer freemarkerConfig() {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		Properties properties = new Properties();
		properties.put("auto_import", "spring.ftl as spring");
		freeMarkerConfigurer.setFreemarkerSettings(properties);
		freeMarkerConfigurer.setConfiguration(getFreemarkerConfigDetails());
		return freeMarkerConfigurer;
	}

	public Configuration getFreemarkerConfigDetails() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setAPIBuiltinEnabled(Boolean.TRUE);
        return cfg;
    }

}
