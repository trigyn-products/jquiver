package com.trigyn.jws.webstarter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.trigyn.jws.dynarest.service.FilesStorageService;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableAsync
@EnableWebSecurity
public class JavaWebStarterApplication {

	@Autowired
	private FilesStorageService	filesStorageService	= null;

	@Autowired
	private JQuiverProperties	jQuiverPropeties	= null;

	public static void main(String[] args) {
		SpringApplication.run(JavaWebStarterApplication.class, args);
	}

	@PostConstruct
	public void initFileStorage() {
		filesStorageService.init();
	}

	@PostConstruct
	public void validateMyProperty() {
		if (jQuiverPropeties.getViewPath() != null) {
			if (jQuiverPropeties.getViewPath().isEmpty()
					|| !jQuiverPropeties.getViewPath().matches("^/[A-Za-z0-9]{1,6}$")) {
				throw new IllegalArgumentException(
						"Invalid value for View Path: Expected value should match regex '^/[A-Za-z0-9]{1,6}$'."
								+ jQuiverPropeties.getViewPath());
			}
		}
		if (jQuiverPropeties.getApiPath() != null) {
			if (jQuiverPropeties.getApiPath().isEmpty()
					|| !jQuiverPropeties.getApiPath().matches("^/[A-Za-z0-9]{1,6}$")) {
				throw new IllegalArgumentException(
						"Invalid value for View Path: Expected value should match regex '^/[A-Za-z0-9]{1,6}$'."
								+ jQuiverPropeties.getApiPath());
			}
		}

		if (jQuiverPropeties.getApiPath().equalsIgnoreCase(jQuiverPropeties.getViewPath())) {
			throw new IllegalArgumentException("Invalid value for View Path OR API. Both Cannot be same.");
		}
	}
}
