package com.trigyn.jws.webstarter.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.templating.entities.TemplateMaster;

@Service
@Transactional
public class TemplateCrudService {

	@Autowired
	private TemplateDAO								templateDAO				= null;

	@Autowired
	private PropertyMasterDAO						propertyMasterDAO		= null;

	@Autowired
	private DownloadUploadModule<TemplateMaster>	downloadUploadModule	= null;

	public void downloadTemplates(String templateId) throws Exception {
		String downloadFolderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system",
				"template-storage-path");
		if (!StringUtils.isBlank(templateId)) {
			TemplateMaster templateMaster = templateDAO.findTemplateById(templateId);
			downloadUploadModule.downloadCodeToLocal(templateMaster, downloadFolderLocation);
		} else {
			downloadUploadModule.downloadCodeToLocal(null, downloadFolderLocation);
		}
	}

	public void uploadTemplates(String templateName) throws Exception {
		downloadUploadModule.uploadCodeToDB(templateName);
	}

	public String checkVelocityData(String velocityName) throws Exception {
		return templateDAO.checkVelocityData(velocityName);
	}

}
