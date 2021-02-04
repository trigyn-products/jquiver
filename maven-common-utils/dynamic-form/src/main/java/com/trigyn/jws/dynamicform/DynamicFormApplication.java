package com.trigyn.jws.dynamicform;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.trigyn.jws.dynamicform.service.FilesStorageService;

@SpringBootApplication
public class DynamicFormApplication {

	@Autowired
	private FilesStorageService filesStorageService = null;

	public static void main(String[] args) {
		SpringApplication.run(DynamicFormApplication.class, args);
	}

	@PostConstruct
	public void initFileStorage() {
		filesStorageService.init();
	}

}
