package com.trigyn.jws.webstarter.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.webstarter.utils.DownloadUploadModule;
import com.trigyn.jws.webstarter.utils.DownloadUploadModuleFactory;

@Service
@Transactional
public class DynarestCrudService {
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;
	
	@Autowired
	private JwsDynarestDAO dynarestDAO = null;
	
	@Autowired
	private FileUtilities fileUtilities  = null;
	
	@Autowired
	private DownloadUploadModuleFactory moduleFactory = null;
	
	public void downloadDynamicRestCode() throws Exception {
		
		DownloadUploadModule downloadUploadModule = moduleFactory.getModule("dynarest");
		downloadUploadModule.downloadCodeToLocal();
	}

	public void uploadDynamicRestCode() throws Exception {
		DownloadUploadModule downloadUploadModule = moduleFactory.getModule("dynarest");
		downloadUploadModule.uploadCodeToLocal();
	}

	public  String getContentForDevEnvironment(String formName, String fileName) throws Exception {
		
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "DynamicRest";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory+File.separator+formName;
		File directory = new File(folderLocation);
		if(!directory.exists()) {
			throw new Exception("No such directory present");
		}
		
		File selectFile = new File(folderLocation+File.separator+fileName+ftlCustomExtension);
		if(selectFile.exists()) {
			return fileUtilities.readContentsOfFile(selectFile.getAbsolutePath());
		}else {
			throw new Exception("Please download the forms from dynamic form  listing  " + formName);
		}
	}
}
