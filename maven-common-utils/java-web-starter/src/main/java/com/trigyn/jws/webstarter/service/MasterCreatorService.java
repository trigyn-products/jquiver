package com.trigyn.jws.webstarter.service;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.IDynamicFormQueriesRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.gridutils.dao.GridUtilsDAO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.gridutils.utility.Constants;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

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
	
	public String getModuleDetails() throws Exception{
		Map<String, Object> templateMap 	= new HashMap<>();
		List<String> tables 				= dynamicFormDAO.getAllTablesListInSchema();
		List<String> views 					= dynamicFormDAO.getAllViewsListInSchema();
		if(!CollectionUtils.isEmpty(views)) {
			tables.addAll(views);
		}
		templateMap.put("tables", tables);
		
		return menuService.getTemplateWithSiteLayout("master-creator", templateMap);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, Object> initMasterCreationScript(MultiValueMap<String, String> inputDetails) throws Exception {
		Map<String, Object> createdMasterDetails = new HashMap<>();
		Map<String, Object> formData = processFormData(inputDetails.getFirst("formData"));
		DynamicForm dynamicForm = createDynamicFormDetails(inputDetails, formData);
		GridDetails gridDetails = createGridDetailsInfo(formData);
		TemplateMaster templateMaster = saveTemplateMasterDetails(inputDetails, gridDetails.getGridId(), dynamicForm.getFormId(), formData);
		return createdMasterDetails;
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

	private DynamicForm createDynamicFormDetails(MultiValueMap<String, String> inputDetails, Map<String, Object> formData) throws JsonMappingException, JsonProcessingException {
		String tableName = formData.get("selectTable").toString();
		String primaryKey = formData.get("primaryKey").toString();
		String moduleName = formData.get("moduleName")+"-form";
		String description = formData.get("moduleName") + " Form";
		List<String> formDetailsString = new ObjectMapper().convertValue(inputDetails.get("formDetails"), List.class);
		String jsonString = formDetailsString.get(0).toString();
		List<Map<String, Object>> formDetails = new ObjectMapper().readValue(jsonString, List.class);
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
		List<String> primaryKeysIds = Lists.newArrayList(primaryKey.split(",")).stream().map(element -> element.replaceAll("_", "")).collect(Collectors.toList());
		List<String> primaryKeys = Lists.newArrayList(primaryKey.split(","));
		Map<String, Object> templateMap = new HashMap<String, Object>();
		templateMap.put("formId", formId);
		templateMap.put("gridId", gridId);
		templateMap.put("primaryKeysIds", primaryKeysIds);
		templateMap.put("primaryKeys", primaryKeys);
		templateMap.put("gridDetails", gridDetails);
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
