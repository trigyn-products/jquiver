package com.trigyn.jws.webstarter.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.IDynamicFormQueriesRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.gridutils.dao.GridUtilsDAO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.gridutils.utility.Constants;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.repository.JwsMasterModulesRepository;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleVO;

@Service
@Transactional(readOnly = false)
public class MasterCreatorService {

	@Autowired
	private DynamicFormCrudDAO dynamicFormDAO 					= null;
	
	@Autowired
	private MenuService			menuService						= null;
	
	@Autowired
	private GridUtilsDAO		gridUtilsDAO					= null;
	
	@Autowired
	private DynamicFormService dynamicFormService				= null;
	
	@Autowired
	private IDynamicFormQueriesRepository dynamicFormQueriesRepository 		= null;
	
	@Autowired
	private DBTemplatingService dbTemplatingService				= null;
	
	@Autowired
	private TemplatingUtils		templatingUtils					= null;
	
	@Autowired
	private ModuleService 		moduleService 			= null;
	
	@Autowired
	private ResourceBundleService resourceBundleService 	= null;
	
	@Autowired
	private JwsMasterModulesRepository jwsMasterModulesRepository = null;
	
	@Autowired 
	private UserManagementService  userManagementService = null;
	
	public String getModuleDetails(HttpServletRequest httpServletRequest) throws Exception{
		Map<String, Object> templateMap 			= new HashMap<>();
		List<String> tables 						= dynamicFormDAO.getAllTablesListInSchema();
		List<String> views 							= dynamicFormDAO.getAllViewsListInSchema();
		List<ModuleDetailsVO> moduleListingVOList 	= moduleService.getAllParentModules("");	
		String uri 												= httpServletRequest.getRequestURI();
		String url 												= httpServletRequest.getRequestURL().toString();
		StringBuilder urlPrefix									= new StringBuilder();
		url = url.replace(uri, "");
		urlPrefix.append(url).append("/view/");

		templateMap.put("urlPrefix", urlPrefix);

		if(!CollectionUtils.isEmpty(views)) {
			tables.addAll(views);
		}
		templateMap.put("tables", tables);
		templateMap.put("tables", tables);
		templateMap.put("moduleListingVOList", moduleListingVOList);

		
		return menuService.getTemplateWithSiteLayout("master-creator", templateMap);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> initMasterCreationScript(MultiValueMap<String, String> inputDetails) throws Exception {
		Map<String, Object> createdMasterDetails = new HashMap<>();
		Map<String, Object> formData = processFormData(inputDetails.getFirst("formData"));
		ModuleDetailsVO menuData=processMenu(inputDetails.getFirst("menuDetails"));
		DynamicForm dynamicForm = createDynamicFormDetails(inputDetails, formData);
		GridDetails gridDetails = createGridDetailsInfo(formData);
		TemplateMaster templateMaster = saveTemplateMasterDetails(inputDetails, gridDetails.getGridId(), dynamicForm.getFormId(), formData);
		menuData.setTargetTypeId(templateMaster.getTemplateId());
		menuData.setTargetLookupId(5);
		menuData.setSequence(moduleService.getMaxSequenceByParent(menuData.getTargetTypeId()));
		String menuId = moduleService.saveModuleDetails(menuData);
		menuData.setModuleId(menuId);
		createdMasterDetails.put("dynamicForm", dynamicForm);
		createdMasterDetails.put("gridDetails", gridDetails);
		createdMasterDetails.put("templateMaster", templateMaster);
		createdMasterDetails.put("menuData", menuData);
		return createdMasterDetails;
	}
	
	@Transactional(readOnly = false)
	public void saveEntityRolesForMasterGenerator(Map<String, Object> createdObjDetails, List<String> roleIds) {
		DynamicForm dynamicForm = (DynamicForm) createdObjDetails.get("dynamicForm");
		GridDetails gridDetails = (GridDetails) createdObjDetails.get("gridDetails");
		TemplateMaster templateMaster = (TemplateMaster) createdObjDetails.get("templateMaster"); 
		ModuleDetailsVO menuData = (ModuleDetailsVO) createdObjDetails.get("menuData");
		JwsEntityRoleVO jwsDynamicEntity = new JwsEntityRoleVO();
		jwsDynamicEntity.setEntityId(dynamicForm.getFormId());
		jwsDynamicEntity.setEntityName(dynamicForm.getFormName());
		jwsDynamicEntity.setRoleIds(roleIds);
		String dynamicModuleId = jwsMasterModulesRepository.
				findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.DYNAMICFORM.getModuleName()).getModuleId();
		jwsDynamicEntity.setModuleId(dynamicModuleId);
		userManagementService.deleteAndSaveEntityRole(jwsDynamicEntity);
		
		JwsEntityRoleVO jwsGridEntity = new JwsEntityRoleVO();
		jwsGridEntity.setEntityId(gridDetails.getGridId());
		jwsGridEntity.setEntityName(gridDetails.getGridName());
		jwsGridEntity.setRoleIds(roleIds);
		String gridModuleId = jwsMasterModulesRepository.
				findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.GRIDUTILS.getModuleName()).getModuleId();
		jwsGridEntity.setModuleId(gridModuleId);
		userManagementService.deleteAndSaveEntityRole(jwsGridEntity);
		
		JwsEntityRoleVO jwsTemplateEntity = new JwsEntityRoleVO();
		jwsTemplateEntity.setEntityId(templateMaster.getTemplateId());
		jwsTemplateEntity.setEntityName(templateMaster.getTemplateName());
		jwsTemplateEntity.setRoleIds(roleIds);
		String templateModuleId = jwsMasterModulesRepository.
				findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.TEMPLATING.getModuleName()).getModuleId();
		jwsTemplateEntity.setModuleId(templateModuleId);
		userManagementService.deleteAndSaveEntityRole(jwsTemplateEntity);
		
		if(StringUtils.isNotBlank(menuData.getModuleId())) {
			JwsEntityRoleVO jwsMenuEntity = new JwsEntityRoleVO();
			jwsMenuEntity.setEntityId(menuData.getModuleId());
			jwsMenuEntity.setEntityName(menuData.getModuleName());
			jwsMenuEntity.setRoleIds(roleIds);
			String menuModuleId = jwsMasterModulesRepository.
					findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.SITELAYOUT.getModuleName()).getModuleId();
			jwsMenuEntity.setModuleId(menuModuleId);
			userManagementService.deleteAndSaveEntityRole(jwsMenuEntity);
		}
		
	}

	private ModuleDetailsVO processMenu(String menuDetails){
		
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(menuDetails);// response will be the json String
		ModuleDetailsVO moduleDetailsVO = gson.fromJson(object, ModuleDetailsVO.class); 
		moduleDetailsVO.setIsInsideMenu(Constant.IS_INSIDE_MENU);
		
		return moduleDetailsVO;
	}

	private Map<String, Object> processFormData(String formData) {
		List<String> masterDetails = Lists.newArrayList(formData.split("&"));
		Map<String, Object> masterDetailsMap = new HashMap<String, Object>();
		for (String details : masterDetails) {
			String[] fieldDetails = details.split("=");
			if(fieldDetails.length == 2) {
				masterDetailsMap.put(fieldDetails[0], URLDecoder.decode(fieldDetails[1]));
			}
		}
		return masterDetailsMap;
	}

	private DynamicForm createDynamicFormDetails(MultiValueMap<String, String> inputDetails, Map<String, Object> formData) throws Exception {
		String tableName = formData.get("selectTable").toString();
		String primaryKey = formData.get("primaryKey").toString();
		String moduleName = formData.get("moduleName")+"-form";
		String description = formData.get("moduleName") + " Form";
		List<String> formDetailsString = new ObjectMapper().convertValue(inputDetails.get("formDetails"), List.class);
		
		
		
		String jsonString = formDetailsString.get(0).toString();
		List<Map<String, Object>> formDetails = new ObjectMapper().readValue(jsonString, List.class);
		for(Map<String, Object> map : formDetails) {
			saveResourseKey(map);
			

		}
		String selectQuery = generateSelectQueryForForm(tableName, formDetails, primaryKey);
		Map<String, String> dynamicFormDetails = generateHtmlTemplate(tableName, formDetails);
		String saveQuery = dynamicFormDetails.get("save-template");
		String htmlTemplate = dynamicFormDetails.get("form-template");
		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setFormDescription(description);
		dynamicForm.setFormSelectQuery(selectQuery);
		dynamicForm.setFormBody(htmlTemplate);
		dynamicForm.setFormName(moduleName);
		dynamicForm.setCreatedBy("admin");
		dynamicForm.setCreatedDate(new Date());
		dynamicFormDAO.saveDynamicFormData(dynamicForm);
		DynamicFormSaveQuery dynamicFormSaveQuery = new DynamicFormSaveQuery();
		dynamicFormSaveQuery.setSequence(1);
		dynamicFormSaveQuery.setDynamicFormId(dynamicForm.getFormId());
		dynamicFormSaveQuery.setDynamicFormSaveQuery(saveQuery);
		dynamicFormQueriesRepository.save(dynamicFormSaveQuery);
		return dynamicForm;
	}

	private void saveResourseKey(Map<String, Object> map) throws Exception {
		String i18nResourceKey =map.get("i18nResourceKey").toString();
		String displayName =map.get("displayName").toString();
		if(i18nResourceKey.isBlank()==false) {
			Boolean keyAlreadyExist = resourceBundleService.checkResourceKeyExist(i18nResourceKey);
			if(keyAlreadyExist) {
				
			}
			else {
				List<ResourceBundleVO> dbResourceList=new ArrayList<ResourceBundleVO>();
				ResourceBundleVO resourceBundleVO=new ResourceBundleVO();
				resourceBundleVO.setLanguageId(1);
				resourceBundleVO.setResourceKey(i18nResourceKey);
				resourceBundleVO.setText(displayName);
				dbResourceList.add(resourceBundleVO);
				resourceBundleService.saveResourceBundleDetails(dbResourceList, 1);
				

			}
		}
	}

	private Map<String, String> generateHtmlTemplate(String tableName, List<Map<String, Object>> formDetails) {
		List<Map<String, Object>> tableDetails = dynamicFormDAO.getTableDetailsByTableName(tableName);
		
		Iterator itr = tableDetails.iterator();
		Set<String> matchedColumns = formDetails.stream().map(column -> column.get("column").toString()).collect(Collectors.toSet());
		while(itr.hasNext()) {
			Map<String, Object> columnDetails = (Map<String, Object>) itr.next();
			String columnName = columnDetails.get("tableColumnName").toString();
			if(Boolean.FALSE.equals(matchedColumns.contains(columnName))) {
				itr.remove();
			}
		}
		Map<String, String> templateDetails = dynamicFormService.createDefaultFormByTableName(tableName, tableDetails);
		return templateDetails;
	}

	private String generateSelectQueryForForm(String tableName, List<Map<String, Object>> formDetails, String primaryKey) {
		StringBuilder selectQuery = new StringBuilder("SELECT ");
		StringJoiner columns = new StringJoiner(",");
		for (Map<String, Object> details : formDetails) {
			columns.add(details.get("column").toString());
		}
		selectQuery.append(columns.toString()).append(" FROM ").append(tableName).append(" WHERE ");
		StringJoiner whereClause = new StringJoiner(" AND ");
		List<String> primaryKeys = Lists.newArrayList(primaryKey.split(","));
		for (String key : primaryKeys) {
			String value = key + " = " + "\\\"${"+key.replaceAll("_", "")+"}\\\"";
			whereClause.add(value.replace("\\", ""));
		}
		selectQuery.append(whereClause.toString());
		
		return selectQuery.toString();
	}

	private GridDetails createGridDetailsInfo(Map<String, Object> formData) {
		String moduleName = formData.get("moduleName")+"Grid".replaceAll("-", "");
		String description = formData.get("moduleName") + " Listing";
		String tableName = formData.get("selectTable").toString();
		String columns = formData.get("columns").toString();
		GridDetails details = new GridDetails(moduleName, moduleName, description, tableName, columns, Constants.queryImplementationType.VIEW.getType());
		return gridUtilsDAO.saveGridDetails(details);
	}
	
	private TemplateMaster saveTemplateMasterDetails(MultiValueMap<String, String> inputDetails, String gridId, String formId, Map<String, Object> formData) throws Exception {
		List<String> gridDetailsString = new ObjectMapper().convertValue(inputDetails.get("gridDetails"), List.class);
		String moduleName = formData.get("moduleName")+ "-template";
		String jsonString = gridDetailsString.get(0).toString();
		List<Map<String, Object>> gridDetails = new ObjectMapper().readValue(jsonString, List.class);
		String primaryKey = formData.get("primaryKey").toString();
		List<String> primaryKeysIds = Lists.newArrayList(primaryKey.split(","))
				.stream()
				.map(element -> element.replaceAll("_", "")).collect(Collectors.toList());
		HashMap<String, Object> details = new HashMap<String, Object>();
		for (String ids : primaryKeysIds) {
			details.put(ids, "");
		}
		List<String> primaryKeys = Lists.newArrayList(primaryKey.split(","));
		Map<String, Object> templateMap = new HashMap<String, Object>();
		templateMap.put("formId", formId);
		templateMap.put("gridId", gridId);
		templateMap.put("primaryKeysIds", primaryKeysIds);
		templateMap.put("primaryKeys", primaryKeys);
		templateMap.put("gridDetails", gridDetails);
		templateMap.put("primaryKeyObject", new ObjectMapper().writeValueAsString(details));
		TemplateVO templateVO = dbTemplatingService.getTemplateByName("system-listing-template");
		String template = templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap);
		TemplateMaster templateMaster = new TemplateMaster();
		templateMaster.setTemplateName(moduleName);
		templateMaster.setTemplate(template);
		templateMaster.setUpdatedDate(new Date());
		templateMaster.setCreatedBy("admin");
		templateMaster.setUpdatedBy("admin");
		return dbTemplatingService.saveTemplateMaster(templateMaster);
	}

	public List<Map<String, Object>> getTableDetails(String tableName) {
		return dynamicFormDAO.getTableInformationByName(tableName);
	}
	
}
