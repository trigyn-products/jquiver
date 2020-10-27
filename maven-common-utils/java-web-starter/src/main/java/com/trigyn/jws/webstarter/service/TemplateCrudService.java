package com.trigyn.jws.webstarter.service;



import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.templating.entities.TemplateMaster;

@Service
@Transactional
public class TemplateCrudService {

	@Autowired
	private TemplateDAO templateDAO 					= null;
	
	@Autowired
	@Qualifier("template")
	private DownloadUploadModule downloadUploadModule 	= null;
	
	public void downloadTemplates(String templateId) throws Exception {
		if(!StringUtils.isBlank(templateId)) {
			TemplateMaster templateMaster = templateDAO.findTemplateById(templateId);
			downloadUploadModule.downloadCodeToLocal(templateMaster);
		}else {
			downloadUploadModule.downloadCodeToLocal(null);
		}
	}
	
	public void uploadTemplates(String templateName) throws Exception {
		downloadUploadModule.uploadCodeToDB(templateName);
	}

	
   public String checkVelocityData(String velocityName) throws Exception {
	   return templateDAO.checkVelocityData(velocityName);
   }
   
}
