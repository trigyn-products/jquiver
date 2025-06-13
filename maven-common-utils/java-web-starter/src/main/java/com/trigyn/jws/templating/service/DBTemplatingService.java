package com.trigyn.jws.templating.service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.dao.DBTemplatingRepository;
import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.utils.Constant;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.utils.RedissonQueryCacheManagerUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional(readOnly = true)
public class DBTemplatingService {

	private static final Logger		logger					= LoggerFactory.getLogger(DBTemplatingService.class);

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

	@Autowired
	private TemplateDAO								templateDAO							= null;
	
	@Autowired
	private ActivityLog				activitylog				= null;
	
	@Autowired
	private RedissonQueryCacheManagerUtil cacheManager = null;

	@Authorized(moduleName = Constants.TEMPLATING)
	public TemplateVO getTemplateByName(String templateName) throws Exception {
		return getTemplateByNameWithoutAuthorization(templateName);
	}

	public TemplateVO getTemplateByNameWithoutAuthorization(String templateName) throws Exception {

		//TemplateVO templateVO = dbTemplatingRepository.findByVmName(templateName);
		TemplateVO templateVO = getTemplateByVmName(templateName);
		if (templateVO == null) {
			throw new Exception("No template was found with the name " + templateName);
		}
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		if (environment.equalsIgnoreCase("dev")) {
			getTemplateContentsForDevEnvironment(templateName, templateVO);
		}
		return templateVO;
	}

	public TemplateVO getTemplateByVmName(String templateName) {
		String	cacheName	= "templateDtoRegion";
		String	cacheKey	= "vmName=" + templateName;
		return cacheManager.fetchJpaDto(cacheName, cacheKey, () -> dbTemplatingRepository.findByVmName(templateName));
	}

	
	public TemplateVO getVelocityDataById(String templateId) throws Exception {
		/**Written for Preventing Cross Site Scripting*/
		//  Avoid anything between script tags  added - paranoid regex
		Pattern scriptPattern = Pattern.compile("<script(.*?)[\r\n]*(.*?)/script>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		String encodedTemplateId = scriptPattern.matcher(templateId).replaceAll("");
		/**Ends Here*/
		TemplateMaster templateMaster = dbTemplatingRepository.findById(encodedTemplateId)
				.orElseThrow(() -> new Exception("Template not found with id : " + encodedTemplateId));
		return new TemplateVO(templateMaster.getTemplateId(), templateMaster.getTemplateName(),
				templateMaster.getTemplate(), templateMaster.getChecksum(), templateMaster.getTemplateTypeId());
	}

	public String checkVelocityData(String velocityName) throws Exception {
		//TemplateVO templateVO = dbTemplatingRepository.findByVmName(velocityName);
		TemplateVO templateVO = getTemplateByVmName(velocityName);
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
		templateDetails.setIsCustomUpdated(1);

		if (templateId != null && !templateId.isEmpty() && templateId.equals("0") == false) {
			templateDetails.setUpdatedBy(detailsVO.getUserName());
			templateDetails.setTemplateId(templateId);
		} else {
			templateDetails.setCreatedBy(detailsVO.getUserName());
		}
		TemplateMaster	templateMaster	= dbTemplatingRepository.saveAndFlush(templateDetails);
		TemplateVO		templateVO		= new TemplateVO(templateMaster.getTemplateId(), templateMaster.getTemplateName(),
				templateMaster.getTemplate(), new Date());
		Integer			typeSelect		= templateMaster.getTemplateTypeId();
		/* Method called for implementing Activity Log */
		logActivity(templateName, typeSelect, templateId);
		moduleVersionService.saveModuleVersion(templateVO, null, templateMaster.getTemplateId(), "jq_template_master",
				Constant.MASTER_SOURCE_VERSION_TYPE);
		// Invalidate the Redisson cache
				String cacheKey = "vmName=" + templateMaster.getTemplateName(); // or however you build cacheKey
				String cacheName = "templateDtoRegion";
				cacheManager.invalidateDtoORScalarValues(cacheName, cacheKey);
				String templateCacheKey = com.trigyn.jws.webstarter.utils.Constant.TargetLookupId.TEMPLATE.getTargetLookupId() + "::" + templateMaster.getTemplateId();
				cacheManager.invalidateDtoORScalarValues( "targetTypeDetailsCache",templateCacheKey);
		return templateMaster.getTemplateId();
	}
	
	/**
	 * Purpose of this method is to log activities</br>
	 * in Templating Module.
	 * 
	 * @author              Bibhusrita.Nayak
	 * @param  templateName
	 * @param  typeSelect
	 * @throws Exception
	 */
	private void logActivity(String templateName, Integer typeSelect, String templateId) throws Exception {
		Map<String, String>	requestParams	= new HashMap<>();
		UserDetailsVO		detailsVO		= userDetailsService.getUserDetails();
		String				action			= "";
		TemplateMaster		templateDetails	= dbTemplatingRepository.findById(templateId).orElse(null);
		if (templateDetails == null) {
			action = Constants.Action.ADD.getAction();
		} else {
			action = Constants.Action.EDIT.getAction();
		}
		String	masterModuleType	= Constants.Modules.TEMPLATING.getModuleName();
		Date	activityTimestamp	= new Date();
		if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		requestParams.put("action", action);
		requestParams.put("entityName", templateName);
		requestParams.put("masterModuleType", masterModuleType);
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("fullName", detailsVO.getFullName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		activitylog.activitylog(requestParams);
	}

	public List<TemplateMaster> getAllTemplates() {
		List<TemplateMaster> templates = dbTemplatingRepository.findAll();
		return templates;
	}

	public void saveAllTemplates(List<TemplateMaster> templates) {
		dbTemplatingRepository.saveAll(templates);
	}

	public TemplateMaster saveTemplateMaster(TemplateMaster templateMaster) {
		return dbTemplatingRepository.save(templateMaster);
	}

	@Transactional(readOnly = false)
	public void saveTemplate(TemplateVO templateVO) throws Exception {
		TemplateMaster templateMaster = dbTemplatingRepository.findById(templateVO.getTemplateId()).orElse(new TemplateMaster());
		templateMaster.setTemplate(templateVO.getTemplate());
		templateMaster.setIsCustomUpdated(1);
		dbTemplatingRepository.save(templateMaster);
		moduleVersionService.saveModuleVersion(templateVO, null, templateMaster.getTemplateId(), "jq_template_master",
				Constant.REVISION_SOURCE_VERSION_TYPE);
	}

	private void getTemplateContentsForDevEnvironment(String templateName, TemplateVO templateVO) throws Exception {
		String	ftlCustomExtension	= ".tgn";
		String	templateDirectory	= "Templates";
		String	folderLocation		= propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory;

		if (!new File(folderLocation).exists()) {
			logger.warn("Templates not downloaded on system, downloading templates to system.");
			String downloadFolderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
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