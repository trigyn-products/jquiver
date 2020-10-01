package com.trigyn.jws.webstarter.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.templating.dao.TemplateDAO;

@Service
@Transactional
public class TemplateCrudService {

	@Autowired
	private TemplateDAO templateDAO 					= null;
	
	@Autowired
	@Qualifier("template")
	private DownloadUploadModule downloadUploadModule 	= null;
	
	public void downloadTemplates() throws Exception {
		downloadUploadModule.downloadCodeToLocal();
		
	}
	
	public void uploadTemplates() throws Exception {
		downloadUploadModule.uploadCodeToDB();
	}

	
   public String checkVelocityData(String velocityName) throws Exception {
	   return templateDAO.checkVelocityData(velocityName);
   }
   
}
