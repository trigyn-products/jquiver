package com.trigyn.jws.webstarter.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.service.DashletModule;
import com.trigyn.jws.dashboard.service.DashletService;
import com.trigyn.jws.dashboard.vo.DashboardVO;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.entities.JwsModuleVersion;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.vo.ModuleListingVO;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.formio.dao.IFormIORepository;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.vo.FormIOImportExportVO;
import com.trigyn.jws.formio.vo.FormIOVO;
import com.trigyn.jws.gridutils.dao.GridDetailsDAO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.service.ModuleService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.typeahead.dao.TypeAheadDAO;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.typeahead.model.AutocompleteVO;
import com.trigyn.jws.typeahead.service.TypeAheadService;
import com.trigyn.jws.webstarter.service.DashboardCrudService;
import com.trigyn.jws.webstarter.service.ImportService;
import com.trigyn.jws.webstarter.service.ModuleRevisionService;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.vo.GridDetailsJsonVO;
import com.trigyn.jws.webstarter.vo.PropertyMasterJsonVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 

@RestController
@RequestMapping("/cf")
public class ModuleVersionController {

	private final static Logger			logger						= LoggerFactory
			.getLogger(ModuleVersionController.class);

	@Autowired
	private ModuleVersionService		moduleVersionService		= null;

	@Autowired
	private MenuService					menuService					= null;

	@Autowired
	private ModuleRevisionService		revisionService				= null;

	@Autowired
	private ModuleService				moduleService				= null;

	@Autowired
	private ImportService				importService				= null;

	@Autowired
	private DashletModule				dashletModule				= null;

	@Autowired
	private DashletService				dashletService				= null;

	@Autowired
	private PropertyMasterService		propertyMasterService		= null;

	@Autowired
	private IModuleListingRepository	iModuleListingRepository	= null;

	@Autowired
	private GridDetailsDAO				gridDetailsDAO				= null;

	@Autowired
	private DashboardCrudService		dashboardCrudService		= null;

	@Autowired
	private TypeAheadDAO				typeAheadDAO				= null;
	@Autowired
	private TypeAheadService			typeAheadService			= null;

	@Autowired
	private JqschedulerRepository		schedulerRepository			= null;

	@Autowired
	private PropertyMasterDAO			propertyMasterDAO			= null;

	@Autowired
	private TemplateDAO					templateDAO					= null;

	@Autowired
	private IFormIORepository			iFormIORepository			= null;

	@Autowired
	private IResourceBundleRepository	iResourceBundleRepository	= null;

	@PostMapping(value = "/smv", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Boolean saveModuleVersioning(@RequestBody MultiValueMap<String, String> formData) throws Exception {
		logger.debug("Inside ModuleVersionController.saveModuleVersioning(formData: {})", formData);
		try {
			revisionService.saveModuleVersioning(formData, Constant.MASTER_SOURCE_VERSION_TYPE);
			return true;
		} catch (Exception exception) {
			logger.error("Error occurred while saving versioning information", exception);
		}
		return false;
	}
	
	@PostMapping(value = "/ssmv")
	public Boolean saveScriptModuleVersion(HttpServletRequest a_httpServletRequest) throws Exception {
		logger.debug("Inside ModuleVersionController.saveModuleVersioning(formData: {})");
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object entityData = mapper.readValue(a_httpServletRequest.getParameter("scriptDataArr"), Object.class);
			String entityName = a_httpServletRequest.getParameter("entityName");
			Object entityIdObj = a_httpServletRequest.getParameter("entityIdObj");
			moduleVersionService.saveModuleVersion(entityData, null, entityIdObj, entityName ,Constant.MASTER_SOURCE_VERSION_TYPE);
			return true;
		} catch (Exception exception) {
			logger.error("Error occurred while saving versioning information", exception);
		}
		return false;
	}

	@PostMapping(value = "/cmv", produces = { MediaType.TEXT_HTML_VALUE })
	public String compareModuleVersion(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.compareModuleVersion()");
		Map<String, Object> templateMap = revisionService.getModuleVersioningData(a_httpServletRequest);
		return menuService.getTemplateWithSiteLayout("revision-details", templateMap);
	}

	@PostMapping(value = "/mj")
	public String getModuleJsonById(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception {
		logger.debug("Inside ModuleVersionController.getModuleJsonById()");
		String moduleVersionId = a_httpServletRequest.getParameter("moduleVersionId");
		return moduleVersionService.getModuleJsonById(moduleVersionId);
	}

	@PostMapping(value = "/uj")
	public String getLastUpdatedJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedJsonData()");
		String	entityId	= a_httpServletRequest.getParameter("entityId");
		String	entityName	= a_httpServletRequest.getParameter("cmvEntityName");
		return moduleVersionService.getLastUpdatedJsonData(entityId, entityName);
	}
	
	@PostMapping(value = "/pd")
	public String getLastUpdatedPermissionData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedPermissionData()");
		String	entityId	= a_httpServletRequest.getParameter("entityId");
		String	moduleId	= a_httpServletRequest.getParameter("moduleId");
		return moduleVersionService.getLastUpdatedPermissionData(entityId, moduleId);
	}
	
	@PostMapping(value = "/tvp")
	public String getTagViewRoleNames(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getTagViewRoleNames()");
		String roleIds	= a_httpServletRequest.getParameter("roleIds");
		return moduleVersionService.getTagViewRoleNames(roleIds);
	}
	
	@PostMapping(value = "/utj")
	public String getLastUpdatedTagJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedTagJsonData()");
		String	entityId	= a_httpServletRequest.getParameter("entityId");
		String	moduleId	= a_httpServletRequest.getParameter("moduleId");
		return moduleVersionService.getLastUpdatedTagJsonData(entityId, moduleId);
	}


	@PostMapping(value = "/suj")
	public void saveUpdatedContent(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception {
		logger.debug("Inside ModuleVersionController.saveUpdatedContent()");
		revisionService.saveUpdatedContent(a_httpServletRequest,a_httpServletResponse);
	}
	
	@PostMapping(value = "/spd")
	public void savePermissionDetails(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception {
		logger.debug("Inside ModuleVersionController.savePermissionDetails()");
		String	moduleId		= a_httpServletRequest.getParameter("moduleId");
		String	entityId		= a_httpServletRequest.getParameter("entityId");
		String	permissionJson	= a_httpServletRequest.getParameter("permissionJson");
		moduleVersionService.savePermissionDetails(moduleId,entityId,permissionJson);
	}

	@PostMapping(value = "/muj")
	public String getLastUpdatedSitelayoutJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedSitelayoutJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		return moduleService.getModuleListingJson(entityId);
	}
	
	@PostMapping(value = "/adsd")
	public String getLastUpdatedAdditionalDataSourceJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedSitelayoutJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		return importService.getAdditionalDatasourceJson(entityId);
	}
	
	@PostMapping(value = "/gdvd")
	public String getLastUpdatedDashletJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedSitelayoutJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		Dashlet		dashlet	= dashletService.getDashletById(entityId);
		DashletVO	vo		= dashletModule.convertDashletEntityToVO(dashlet);
		return convertEntityToJson(vo);
	}
	
	@PostMapping(value = "/gn")
	public String getLastUpdatedNotificationJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedSitelayoutJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		return importService.getNotificationJson(entityId);
	}
	
	@PostMapping(value = "/apicd")
	public String getLastUpdatedAPIClientDataJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedSitelayoutJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		return importService.getApiClientDetailseJson(entityId);
	}
	
	@PostMapping(value = "/schd")
	public String getLastUpdatedSchedulerDataSourceJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedSitelayoutJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		return importService.getSchedulerJson(entityId);
	}
	
	@PostMapping(value = "/ggvd")
	public String getLastUpdatedGridUtilJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedSitelayoutJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		GridDetails			gridDetails	= gridDetailsDAO.findGridDetailsById(entityId);
		GridDetailsJsonVO	vo			= importService.convertGridEntityToVO(gridDetails);
		return convertEntityToJson(vo);
	}
 
	@PostMapping(value = "/gdashvd")
	public String getLastUpdatedDashboardJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedSitelayoutJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		Dashboard	dashboard	= dashboardCrudService.findDashboardByDashboardId(entityId);
		DashboardVO	vo			= dashboardCrudService.convertDashboardEntityToVO(dashboard);
		return convertEntityToJson(vo);
	}
	
	@PostMapping(value = "/grbd")
	public String getLastUpdatedResourceBundleJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedResourceBundleJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		List<ResourceBundleVO>	resourceBundleVOList	= iResourceBundleRepository.findResourceBundleByKey(entityId);
		return convertEntityToJson(resourceBundleVOList);
	}
	
	@PostMapping(value = "/gavd")
	public String getLastUpdatedAutocompleteJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedAutocompleteJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		Autocomplete	autocomplete	= typeAheadDAO.findAutocomplete(entityId);
		AutocompleteVO	vo				= typeAheadService.convertEntityToVO(autocomplete);	
		return convertEntityToJson(vo);
	}
	
	@PostMapping(value = "/gtvd")
	public String getLastUpdatedTemplateJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedTemplateJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		TemplateMaster	template	= templateDAO.findTemplateById(entityId);
		TemplateVO		vo			= importService.convertTemplateEntityToVO(template);
		return convertEntityToJson(vo);
	}
 
	@PostMapping(value = "/gacvd")
	public String getLastUpdatedApplicationConfigurationJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedTemplateJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		PropertyMaster			propertyObj	= propertyMasterDAO.findPropertyMasterById(entityId);
 
		PropertyMasterJsonVO	vo			= importService.convertPropertyMasterEntityToVO(propertyObj);
		return convertEntityToJson(vo);
	}
	
 
	@PostMapping(value = "/grvd")
	public String getLastUpdatedRouterJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedRouterJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		ModuleListing	siteLayout	= iModuleListingRepository.findById(entityId).orElse(null);
		ModuleListingVO	vo			= moduleService.convertEntityToVO(siteLayout);
	     return convertEntityToJson(vo);
	}
	
	@PostMapping(value = "/gfiod")
	public String getLastUpdatedFormIOJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		logger.debug("Inside ModuleVersionController.getLastUpdatedTemplateJsonData()");
		String entityId = a_httpServletRequest.getParameter("entityId");
		FormIO		formIoEntity;
		FormIOVO	formIoVo;
		formIoEntity = iFormIORepository.findById(entityId).orElse(null);
		FormIOImportExportVO vo = importService.convertFormIOEntityToVO(formIoEntity);
		return convertEntityToJson(vo);
	}
	
	@PostMapping(value = "/sml")
	public void saveSiteLayout(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception {
		logger.debug("Inside ModuleVersionController.saveSiteLayout()");
		String			modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper	objectMapper	= new ObjectMapper();
		ModuleListing	moduleListing	= objectMapper.readValue(modifiedContent, ModuleListing.class);
		moduleService.saveModuleListing(moduleListing);
	}
	
	private String convertEntityToJson(Object data) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Gson gson = new GsonBuilder().serializeNulls().create(); 
		if (data == null)
			return "{}";
		String		moduleJson		= null;
		String		dbDateFormat	= propertyMasterService.getDateFormatByName(
				com.trigyn.jws.dbutils.utils.Constant.PROPERTY_MASTER_OWNER_TYPE,
				com.trigyn.jws.dbutils.utils.Constant.PROPERTY_MASTER_OWNER_ID,
				com.trigyn.jws.dbutils.utils.Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
				com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat	dateFormat		= new SimpleDateFormat(dbDateFormat);
		mapper.setDateFormat(dateFormat);
		try {
			if (data instanceof Collection<?>) {
				List<Map<String, Object>> list = mapper.convertValue(data, List.class);
				return gson.toJson(list);
			}
			Map<String, Object> map = mapper.convertValue(data, TreeMap.class);
			return gson.toJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}
}
