package com.trigyn.jws.templating.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.dao.DBTemplatingRepository;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.utils.Constant;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;

@Service
@Transactional(readOnly = true)
public class DBTemplatingService {

	private static final Logger		logger					= LogManager.getLogger(DBTemplatingService.class);

	@Autowired
	private DBTemplatingRepository	dbTemplatingRepository	= null;

	@Autowired
	private PropertyMasterDAO		propertyMasterDAO		= null;

	@Autowired
	private IUserDetailsService		userDetailsService		= null;

	@Autowired
	private FileUtilities			fileUtilities			= null;

	@Autowired
	private ModuleVersionService	moduleVersionService	= null;

	@Autowired
	@Qualifier("template")
	private TemplateModule			templateModule			= null;

	@Authorized(moduleName = Constants.TEMPLATING)
	public TemplateVO getTemplateByName(String templateName) throws Exception {

		TemplateVO templateVO = dbTemplatingRepository.findByVmName(templateName);
		if (templateVO == null) {
			throw new Exception("No template was found with the  name " + templateName);
		}
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		if (environment.equalsIgnoreCase("dev")) {
			getTemplateContentsForDevEnvironment(templateName, templateVO);
		}
		return templateVO;
	}

	public TemplateVO getVelocityDataById(String templateId) throws Exception {
		TemplateMaster templateMaster = dbTemplatingRepository.findById(templateId)
				.orElseThrow(() -> new Exception("Template not found with id : " + templateId));
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
	public String saveTemplateData(HttpServletRequest request) throws Exception {

		UserDetailsVO	detailsVO		= userDetailsService.getUserDetails();
		String			templateName	= request.getParameter("velocityName");
		String			templateId		= request.getParameter("velocityId");
		String			templateData	= request.getParameter("velocityTempData");

		TemplateMaster	templateDetails	= dbTemplatingRepository.findById(templateId).orElse(new TemplateMaster());
		templateDetails.setTemplate(templateData);
		templateDetails.setTemplateName(templateName);
		templateDetails.setUpdatedDate(new Date());

		if (templateId != null && !templateId.isEmpty() && templateId.equals("0") == false) {
			templateDetails.setUpdatedBy(detailsVO.getUserName());
			templateDetails.setTemplateId(templateId);
		} else {
			templateDetails.setCreatedBy(detailsVO.getUserName());
		}
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		if (environment.equalsIgnoreCase("dev")) {

			String		ftlCustomExtension	= ".tgn";
			String		templateDirectory	= "Templates";
			String		folderLocation		= propertyMasterDAO.findPropertyMasterValue("system", "system",
					"template-storage-path");
			TemplateVO	templateVO			= new TemplateVO();
			templateVO.setTemplate(templateData);
			folderLocation = folderLocation + File.separator + templateDirectory;
			File directory = new File(folderLocation);
			if (!directory.exists()) {
				throw new Exception("No such directory present");
			}
			File templateFile = new File(folderLocation + File.separator + templateName + ftlCustomExtension);
			templateDetails.setChecksum(fileUtilities.writeFileContents(templateVO.getTemplate(), templateFile));
		}

		TemplateMaster	templateMaster	= dbTemplatingRepository.saveAndFlush(templateDetails);
		TemplateVO		templateVO		= new TemplateVO(templateMaster.getTemplateId(),
				templateMaster.getTemplateName(), templateMaster.getTemplate());
		moduleVersionService.saveModuleVersion(templateVO, null, templateMaster.getTemplateId(), "jq_template_master",
				Constant.MASTER_SOURCE_VERSION_TYPE);

		return templateMaster.getTemplateId();
	}

	public List<TemplateMaster> getAllTemplates() {
		List<TemplateMaster> templates = dbTemplatingRepository.findAll();
		return templates;
	}

	public List<TemplateVO> getAllDefaultTemplates() {
		List<TemplateVO> templateVOs = dbTemplatingRepository.getAllDefaultTemplates(Constant.DEFAULT_TEMPLATE_TYPE);
		return templateVOs;
	}

	public void saveAllTemplates(List<TemplateMaster> templates) {
		dbTemplatingRepository.saveAll(templates);
	}

	public TemplateMaster saveTemplateMaster(TemplateMaster templateMaster) {
		return dbTemplatingRepository.save(templateMaster);
	}

	@Transactional(readOnly = false)
	public void saveTemplate(TemplateVO templateVO) throws Exception {
		TemplateMaster templateMaster = dbTemplatingRepository.findById(templateVO.getTemplateId())
				.orElse(new TemplateMaster());
		templateMaster.setTemplate(templateVO.getTemplate());
		dbTemplatingRepository.save(templateMaster);
		moduleVersionService.saveModuleVersion(templateVO, null, templateMaster.getTemplateId(), "jq_template_master",
				Constant.REVISION_SOURCE_VERSION_TYPE);
	}

	private void getTemplateContentsForDevEnvironment(String templateName, TemplateVO templateVO) throws Exception {
		String	ftlCustomExtension	= ".tgn";
		String	templateDirectory	= "Templates";
		String	folderLocation		= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory;

		if (!new File(folderLocation).exists()) {
			logger.warn("Templates not downloaded on system, downloading templates to system.");
			String downloadFolderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system",
					"template-storage-path");
			templateModule.downloadCodeToLocal(null, downloadFolderLocation);
			logger.info("Templates downloaded to local machine");
		}
		File file = new File(folderLocation + File.separator + templateVO.getTemplateName() + ftlCustomExtension);
		if (file.exists()) {
			String content = fileUtilities.readContentsOfFile(file.getAbsolutePath());
			templateVO.setTemplate(content);
		}
	}
}