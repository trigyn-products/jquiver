package com.trigyn.jws.dynarest;

import java.io.File;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;

@SpringBootApplication
public class DynamicRestApplication {
	
	private static final Logger logger = LogManager.getLogger(DynamicRestApplication.class);

	private static final String SERVICE_CLASS_NAME 							= "ServiceLogic";
	private static final String DYNAREST_CLASS_FILE_PATH 					= "dynarest-class-file-path";
	
	@Autowired
	private JwsDynamicRestDetailService dynamicRestDetailService = null;
	
	@Autowired
	private PropertyMasterService propertyMasterService 		 = null;
	
	public static void main(String[] args) {
		SpringApplication.run(DynamicRestApplication.class, args);
	}
	
	@PostConstruct
	public void dynarestApiDetails() throws Exception {
		logger.info("Compiling dynarest classes");
		File sourceFile = File.createTempFile(SERVICE_CLASS_NAME, ".java");
        String className = sourceFile.getName().replaceAll(".java", "");
		dynamicRestDetailService.precompileClassAndGetFileLocation(className, sourceFile);
		logger.info("Compiling dynarest classes successfully.");
	}
	
	@PreDestroy
	public void onDestroy() throws Exception {
		logger.info("Deleting dynarest files");
		String path = propertyMasterService.findPropertyMasterValue(DYNAREST_CLASS_FILE_PATH);
		File file = Paths.get(path).toFile();
		if (file.exists()) {
			file.delete();
		}
		logger.info("Deleting dynarest files deleted successfully");
	}

}
