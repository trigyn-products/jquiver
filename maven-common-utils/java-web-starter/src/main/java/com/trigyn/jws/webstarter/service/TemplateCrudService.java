package com.trigyn.jws.webstarter.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.webstarter.utils.DownloadUploadModule;
import com.trigyn.jws.webstarter.utils.DownloadUploadModuleFactory;

@Service
@Transactional
public class TemplateCrudService {

	@Autowired
	private TemplateDAO templateDAO = null;
	
	@Autowired
	private DownloadUploadModuleFactory moduleFactory = null;
	
	public void downloadTemplates() throws Exception {
		DownloadUploadModule downloadUploadModule = moduleFactory.getModule("templates");
		downloadUploadModule.downloadCodeToLocal();
		
	}
	
	public void uploadTemplates() throws Exception {
		DownloadUploadModule downloadUploadModule = moduleFactory.getModule("templates");
		downloadUploadModule.uploadCodeToLocal();
	}

	
	   public String checkVelocityData(String velocityName) throws Exception {
		   return templateDAO.checkVelocityData(velocityName);
	   }
}
