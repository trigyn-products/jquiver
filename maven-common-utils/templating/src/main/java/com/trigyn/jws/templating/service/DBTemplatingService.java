package com.trigyn.jws.templating.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.templating.dao.DBTemplatingRepository;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.vo.TemplateVO;

@Service
@Transactional(readOnly = true)
public class DBTemplatingService {

    
	@Autowired
	private DBTemplatingRepository dbTemplatingRepository = null;

	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;
	
	@Autowired
	private IUserDetailsService userDetailsService = null;

	@Autowired
	private FileUtilities fileUtilities  = null;
	
	public TemplateVO getTemplateByName(String templateName) throws Exception {
	    	
	        TemplateVO templateVO = dbTemplatingRepository.findByVmName(templateName);
			if (templateVO == null) {
	            throw new Exception("No template was found with the  name " + templateName);
	        }
	        String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
	        if(environment.equalsIgnoreCase("dev") && (!templateName.equalsIgnoreCase("template-listing") )) {
	    		getTemplateContentsForDevEnvironment(templateName, templateVO);
	        }
	       
	        return templateVO;
	}

    public TemplateVO getVelocityDataById(String templateId) throws Exception {
        TemplateMaster templateMaster = dbTemplatingRepository.findById(templateId)
                .orElseThrow(() -> new Exception("User not found with id : " + templateId));
        return new TemplateVO(templateMaster.getTemplateId(), templateMaster.getTemplateName(),
                templateMaster.getTemplate());
    }

	public String checkVelocityData(String velocityName) throws Exception {
		TemplateVO templateVO = dbTemplatingRepository.findByVmName(velocityName);
		if (templateVO == null) {
			return null;
		}
		return templateVO.getTemplateId();
	}

	@Transactional(readOnly = false)
	public void saveTemplateData(HttpServletRequest request) throws Exception {

        UserDetailsVO detailsVO = userDetailsService.getUserDetails();
        String templateName = request.getParameter("velocityName");
        String templateId = request.getParameter("velocityId") == null ? "-1" : request.getParameter("velocityId");
        String templateData = request.getParameter("velocityTempData");

        TemplateMaster templateDetails = dbTemplatingRepository.findById(templateId).orElse(new TemplateMaster());
        templateDetails.setTemplate(templateData);
        templateDetails.setTemplateName(templateName);
        templateDetails.setUpdatedDate(new Date());

        if (templateId != null && !templateId.isEmpty()) {
            templateDetails.setUpdatedBy(detailsVO.getUserId());
            templateDetails.setTemplateId(templateId);
        } else {
            templateDetails.setCreatedBy(detailsVO.getUserId());
        }
        String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
        if(environment.equalsIgnoreCase("dev")) {
        	
        	String ftlCustomExtension = ".tgn";
    		String templateDirectory = "Templates";
    		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
    		TemplateVO templateVO = new TemplateVO();
            templateVO.setTemplate(templateData);
    		folderLocation = folderLocation +File.separator+templateDirectory;
    		File directory = new File(folderLocation);
    		if(!directory.exists()) {
    			throw new Exception("No such directory present");
    		}
            File templateFile = new File(folderLocation+File.separator+templateName+ftlCustomExtension);
            templateDetails.setChecksum(fileUtilities.writeFileContents(templateVO.getTemplate(), templateFile));
        }
        
        dbTemplatingRepository.saveAndFlush(templateDetails);
        
    }

	public List<TemplateMaster> getAllTemplates() {
		List<TemplateMaster> templates = dbTemplatingRepository.findAll();
		return templates;
	}

	public void saveAllTemplates(List<TemplateMaster> templates) {
		dbTemplatingRepository.saveAll(templates);
	}
	

	private void getTemplateContentsForDevEnvironment(String templateName, TemplateVO templateVO) throws Exception {
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "Templates";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory;
		
		if(!new File(folderLocation).exists()) {
			throw new Exception("Please download the templates from template master listing  " + templateName);
		}
		File file = new File(folderLocation+ File.separator+templateVO.getTemplateName()+ftlCustomExtension);
		if(file.exists()) {
			String content = fileUtilities.readContentsOfFile(file.getAbsolutePath());
			templateVO.setTemplate(content);
		}else {
			throw new Exception("Please download the templates from template master listing  " + templateName);
		}
	}
}