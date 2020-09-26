package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@Service
@Transactional
public class DynamicFormService {

	@Autowired
	private TemplatingUtils templateEngine = null;

	@Autowired
	private DynamicFormCrudDAO dynamicFormDAO = null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;
	
	@Autowired
	private DBTemplatingService templateService = null;
	
	@Autowired
	private FileUtilities fileUtilities  = null;
	
	
	public String loadDynamicForm(String formId, Map<String, Object> requestParam, Map<String, Object> additionalParam)
			throws Exception {

		String selectTemplateQuery = null;
		String templateHtml = null;
		String selectQuery = null;
		String formBody = null;
		Map<String, Object> formHtmlTemplateMap = new HashMap<>();
		String selectQueryFile = "selectQuery";
		String htmlBodyFile = "hmtlQuery";

		DynamicForm form = dynamicFormDAO.findDynamicFormById(formId);
		String formName = form.getFormName();
		
		 String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
	     if(environment.equalsIgnoreCase("dev") ) {
	         selectQuery = getContentForDevEnvironment(form.getFormName(),selectQueryFile);
	         formBody = getContentForDevEnvironment(form.getFormName(),htmlBodyFile);
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
		templateHtml = templateEngine.processTemplateContents(formBody, formName, formHtmlTemplateMap);

		return templateHtml;
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
			  formSaveQuery = getContentForDevEnvironment(form.getFormName(),saveQuery+dynamicFormSaveQuery.getSequence());
			}else {
			  formSaveQuery = dynamicFormSaveQuery.getDynamicFormSaveQuery();
			}
			saveTemplateQuery = templateEngine.processTemplateContents(formSaveQuery, formName, saveTemplateMap);
			dynamicFormDAO.saveFormData(saveTemplateQuery);
		}
		return true;
	}
	
	
	public  String getContentForDevEnvironment(String formName, String fileName) throws Exception {
		
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "DynamicForm";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory+File.separator+formName;
		File directory = new File(folderLocation);
		if(!directory.exists()) {
			throw new Exception("No such directory present");
		}
		
		File selectFile = new File(folderLocation+File.separator+fileName+ftlCustomExtension);
		if(selectFile.exists()) {
			return fileUtilities.readContentsOfFile(selectFile.getAbsolutePath());
		}else {
			throw new Exception("Please download the forms from dynamic form  listing  " + formName);
		}
	}

	public String createDefaultFormByTableName(String tableName) {
		List<Map<String, Object>> tableDetails = dynamicFormDAO.getTableDetailsByTableName(tableName);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("columnDetails", tableDetails);
		String template = null;
		try {
			TemplateVO templateVO = templateService.getTemplateByName("system-form-html-template");
			template = templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return template;
	}
}
