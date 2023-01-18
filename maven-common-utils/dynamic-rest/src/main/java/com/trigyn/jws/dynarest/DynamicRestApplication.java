package com.trigyn.jws.dynarest;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.trigyn.jws.dynarest.service.FilesStorageService;

@SpringBootApplication
public class DynamicRestApplication {

	@Autowired
	private FilesStorageService filesStorageService = null;

	public static void main(String[] args) {
		SpringApplication.run(DynamicRestApplication.class, args);
	}

	@PostConstruct
	public void initFileStorage() {
		filesStorageService.init();
	}
}
