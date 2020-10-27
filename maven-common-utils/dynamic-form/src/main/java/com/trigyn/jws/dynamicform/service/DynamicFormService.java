package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@Service
@Transactional
public class DynamicFormService {

	@Autowired
	private TemplatingUtils templateEngine 					= null;

	@Autowired
	private DynamicFormCrudDAO dynamicFormDAO 				= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO 			= null;
	
	@Autowired
	private DBTemplatingService templateService 			= null;
	
	@Autowired
	private FileUtilities fileUtilities  					= null;
	
	@Autowired
	private MenuService menuService							= null;
	
	
	public String loadDynamicForm(String formId, Map<String, Object> requestParam, Map<String, Object> additionalParam){

		try {
			String selectTemplateQuery = null;
			String templateHtml = null;
			String selectQuery = null;
			String formBody = null;
			Map<String, Object> formHtmlTemplateMap = new HashMap<>();
			String selectQueryFile = "selectQuery";
			String htmlBodyFile = "htmlContent";

			DynamicForm form = dynamicFormDAO.findDynamicFormById(formId);
			String formName = form.getFormName();
			
			 String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			 if(environment.equalsIgnoreCase("dev") ) {
			     selectQuery = getContentForDevEnvironment(form, form.getFormSelectQuery(), selectQueryFile);
			     formBody = getContentForDevEnvironment(form, form.getFormBody(), htmlBodyFile);
			  }else {
			     selectQuery = form.getFormSelectQuery();
			     formBody = form.getFormBody();
			  }
			
			List<Map<String, Object>> selectResultSet = null;

			if (additionalParam != null) {
				requestParam.putAll(additionalParam);
			}

			selectTemplateQuery = templateEngine.processTemplateContents(selectQuery, formName, requestParam);

			if (StringUtils.isNotEmpty(selectTemplateQuery)) {
				selectResultSet = dynamicFormDAO.getFormData(selectTemplateQuery.toString());
			}
			formHtmlTemplateMap.put("resultSet", selectResultSet);

			formHtmlTemplateMap.put("formId", formId);
			formHtmlTemplateMap.put("requestDetails", requestParam);
			formHtmlTemplateMap.put("entityType", "form");
			formHtmlTemplateMap.put("entityName", formName);
			templateHtml = templateEngine.processTemplateContents(formBody, formName, formHtmlTemplateMap);
			Boolean includeLayout = requestParam.get("includeLayout") == null ? Boolean.TRUE 
						: Boolean.parseBoolean(requestParam.get("includeLayout").toString());
			if(Boolean.TRUE.equals(includeLayout)) {
				return menuService.getTemplateWithSiteLayoutWithoutProcess(templateHtml, new HashMap<>());
			} else {
				return templateHtml;
			}
			
		}  catch (Exception exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean saveDynamicForm(MultiValueMap<String, String> formData) throws Exception {
		String saveTemplateQuery = null;
		DynamicForm form = dynamicFormDAO.findDynamicFormById(formData.getFirst("formId"));
		String formName = form.getFormName();
		Map<String, Object> saveTemplateMap = new HashMap<>();
		saveTemplateMap.put("formData", formData);
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		String saveQuery = "saveQuery-";
		List<DynamicFormSaveQuery> dynamicFormSaveQueries = dynamicFormDAO.findDynamicFormQueriesById(formData.getFirst("formId"));
		for (DynamicFormSaveQuery dynamicFormSaveQuery : dynamicFormSaveQueries) {
			String formSaveQuery = null;
			if(environment.equalsIgnoreCase("dev") ) {
			  formSaveQuery = getContentForDevEnvironment(form, dynamicFormSaveQuery.getDynamicFormSaveQuery(), saveQuery+dynamicFormSaveQuery.getSequence());
			}else {
			  formSaveQuery = dynamicFormSaveQuery.getDynamicFormSaveQuery();
			}
			saveTemplateQuery = templateEngine.processTemplateContents(formSaveQuery, formName, saveTemplateMap);
			dynamicFormDAO.saveFormData(saveTemplateQuery);
		}
		return true;
	}
	
	
	public  String getContentForDevEnvironment(DynamicForm form, String dbContent, String fileName) throws Exception {
		
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "DynamicForm";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory+File.separator+form.getFormName();
		File directory = new File(folderLocation);
		if(!directory.exists()) {
			return dbContent;
		}
		
		File selectFile = new File(folderLocation+File.separator+fileName+ftlCustomExtension);
		if(selectFile.exists()) {
			return fileUtilities.readContentsOfFile(selectFile.getAbsolutePath());
		}else {
			return dbContent;
		}
	}

	public Map<String, String> createDefaultFormByTableName(String tableName, List<Map<String, Object>> tableDetails) {
		Map<String, String> templatesMap = new HashMap<>();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("columnDetails", tableDetails);
		try {
			TemplateVO templateVO = templateService.getTemplateByName("system-form-html-template");
			String template = templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), parameters);
			templatesMap.put("form-template", template);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		createSaveUpdateQueryTemplate(tableDetails, tableName, templatesMap);
		return templatesMap;
	}
	
	public List<Map<String, Object>> getTableInformationByName(String tableName) {
		return dynamicFormDAO.getTableInformationByName(tableName);
	}
	
	public List<Map<String, Object>> getTableDetailsByTableName(String tableName) {
		return dynamicFormDAO.getTableDetailsByTableName(tableName);
	}

	private void createSaveUpdateQueryTemplate(List<Map<String, Object>> tableInformation, String tableName, Map<String, String> templatesMap) {
		
		StringJoiner insertJoiner = new StringJoiner(",", "INSERT INTO "+tableName+" (", ")");
		StringJoiner insertValuesJoiner = new StringJoiner(",", " VALUES (", ")");
		for (Map<String, Object> info : tableInformation) {
			String columnName = info.get("tableColumnName").toString();
			String dataType = info.get("dataType").toString();
			String columnKey = info.get("columnKey").toString();
			insertJoiner.add(columnName);
			joinQueryBuilder(insertValuesJoiner, columnName, dataType);
			
		}
		StringBuilder queryBuilder = new StringBuilder(insertJoiner.toString());
		queryBuilder.append(insertValuesJoiner);
		
		Map<String, Object> saveQueryparameters = new HashMap<>();
		saveQueryparameters.put("insertQuery",queryBuilder.toString());
		
		StringJoiner updateQuery = new StringJoiner(",", "UPDATE "+tableName+" SET ", "");
		StringJoiner updateWhereQuery = new StringJoiner(" AND ", " WHERE ", "");
		for (Map<String, Object> info : tableInformation) {
			String columnName = info.get("tableColumnName").toString();
			String dataType = info.get("dataType").toString();
			String columnKey = info.get("columnKey").toString();
			if("PRI".equals(columnKey)) {
				joinQueryBuilder(updateWhereQuery, columnName, dataType);
			} else {
				joinQueryBuilder(updateQuery, columnName, dataType);
			}
		}
		StringBuilder updateQueryBuilder = new StringBuilder(updateQuery.toString());
		updateQueryBuilder.append(updateWhereQuery);
		saveQueryparameters.put("updateQuery", updateQueryBuilder.toString());
		
		try {
			TemplateVO templateVO = templateService.getTemplateByName("system-form-save-query-template");
			String template =  templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), saveQueryparameters);
			templatesMap.put("save-template", template);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void joinQueryBuilder(StringJoiner insertValuesJoiner, String columnName, String dataType) {
		if(dataType.contains("varchar") || dataType.contains("text")) {
			String value = "'${formData?api.getFirst(\\\"" + columnName.replaceAll("_", "") + "\\\")}'";
			insertValuesJoiner.add(value.replace("\\", ""));
		} else if (dataType.contains("int") || dataType.contains("decimal")) {
			String value = "${formData?api.getFirst(\\\"" + columnName.replaceAll("_", "") + "\\\")}";
			insertValuesJoiner.add(value.replace("\\", ""));
		} else if (dataType.contains("date") || dataType.contains("timestamp")) {
			insertValuesJoiner.add("NOW()");
		}
	}
}
