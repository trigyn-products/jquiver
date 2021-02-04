package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
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

	private static final String	DATE				= "date";

	private static final String	TIMESTAMP			= "timestamp";

	private static final String	DECIMAL				= "decimal";

	private static final String	TEXT				= "text";

	private static final String	INT					= "int";

	private static final String	VARCHAR				= "varchar";

	private static final String	PRIMARY_KEY			= "PRI";

	private static final String	AUTO_INCREMENT		= "AUTO_INCREMENT";

	@Autowired
	private TemplatingUtils		templateEngine		= null;

	@Autowired
	private DynamicFormCrudDAO	dynamicFormDAO		= null;

	@Autowired
	private PropertyMasterDAO	propertyMasterDAO	= null;

	@Autowired
	private DBTemplatingService	templateService		= null;

	@Autowired
	private FileUtilities		fileUtilities		= null;

	@Autowired
	private MenuService			menuService			= null;

	@Autowired
	private IUserDetailsService	userDetailsService	= null;

	private final static Logger	logger				= LogManager.getLogger(DynamicFormService.class);

	public String loadDynamicForm(String formId, Map<String, Object> requestParam,
			Map<String, Object> additionalParam) {

		try {
			String				selectTemplateQuery	= null;
			String				templateHtml		= null;
			String				selectQuery			= null;
			String				formBody			= null;
			Map<String, Object>	formHtmlTemplateMap	= new HashMap<>();
			String				selectQueryFile		= "selectQuery";
			String				htmlBodyFile		= "htmlContent";

			DynamicForm			form				= dynamicFormDAO.findDynamicFormById(formId);
			UserDetailsVO		userDetails			= userDetailsService.getUserDetails();
			String				formName			= form.getFormName();

			String				environment			= propertyMasterDAO.findPropertyMasterValue("system", "system",
					"profile");
			if (environment.equalsIgnoreCase("dev")) {
				selectQuery	= getContentForDevEnvironment(form, form.getFormSelectQuery(), selectQueryFile);
				formBody	= getContentForDevEnvironment(form, form.getFormBody(), htmlBodyFile);
			} else {
				selectQuery	= form.getFormSelectQuery();
				formBody	= form.getFormBody();
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
			if (selectResultSet != null && selectResultSet.size() > 0) {
				formHtmlTemplateMap.put("resultSetObject", selectResultSet.get(0));
			} else {
				formHtmlTemplateMap.put("resultSetObject", new HashMap<>());
			}

			formHtmlTemplateMap.put("formId", formId);
			formHtmlTemplateMap.put("userDetails", userDetails);
			formHtmlTemplateMap.put("requestDetails", requestParam);
			formHtmlTemplateMap.put("entityType", "form");
			formHtmlTemplateMap.put("entityName", formName);
			templateHtml = templateEngine.processTemplateContents(formBody, formName, formHtmlTemplateMap);
			Boolean includeLayout = requestParam.get("includeLayout") == null ? Boolean.TRUE
					: Boolean.parseBoolean(requestParam.get("includeLayout").toString());
			if (Boolean.TRUE.equals(includeLayout)) {
				return menuService.getTemplateWithSiteLayoutWithoutProcess(templateHtml, formHtmlTemplateMap);
			} else {
				return templateHtml;
			}

		} catch (Exception a_exc) {
			logger.error("Error while loading dynamic form ", a_exc);
			throw new RuntimeException(a_exc.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean saveDynamicForm(MultiValueMap<String, String> formData) throws Exception {
		String				saveTemplateQuery	= null;
		String				formId				= formData.getFirst("formId");
		DynamicForm			form				= dynamicFormDAO.findDynamicFormById(formId);
		String				formName			= form.getFormName();
		Map<String, Object>	saveTemplateMap		= new HashMap<>();
		saveTemplateMap.put("formData", formData);
		String						environment				= propertyMasterDAO.findPropertyMasterValue("system",
				"system", "profile");
		String						saveQuery				= "saveQuery-";
		List<DynamicFormSaveQuery>	dynamicFormSaveQueries	= dynamicFormDAO.findDynamicFormQueriesById(formId);
		for (DynamicFormSaveQuery dynamicFormSaveQuery : dynamicFormSaveQueries) {
			String formSaveQuery = null;
			if (environment.equalsIgnoreCase("dev")) {
				formSaveQuery = getContentForDevEnvironment(form, dynamicFormSaveQuery.getDynamicFormSaveQuery(),
						saveQuery + dynamicFormSaveQuery.getSequence());
			} else {
				formSaveQuery = dynamicFormSaveQuery.getDynamicFormSaveQuery();
			}
			saveTemplateQuery = templateEngine.processTemplateContents(formSaveQuery, formName, saveTemplateMap);
			dynamicFormDAO.saveFormData(saveTemplateQuery, new HashMap<>());
		}
		return true;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean saveDynamicForm(List<Map<String, String>> formData) throws Exception {
		String						saveTemplateQuery		= null;
		Map<String, Object>			formDetails				= createParamterMap(formData);
		String						formId					= formDetails.get("formId").toString();
		DynamicForm					form					= dynamicFormDAO.findDynamicFormById(formId);
		String						formName				= form.getFormName();
		String						environment				= propertyMasterDAO.findPropertyMasterValue("system",
				"system", "profile");
		String						saveQuery				= "saveQuery-";
		List<DynamicFormSaveQuery>	dynamicFormSaveQueries	= dynamicFormDAO.findDynamicFormQueriesById(formId);
		for (DynamicFormSaveQuery dynamicFormSaveQuery : dynamicFormSaveQueries) {
			String formSaveQuery = null;
			if (environment.equalsIgnoreCase("dev")) {
				formSaveQuery = getContentForDevEnvironment(form, dynamicFormSaveQuery.getDynamicFormSaveQuery(),
						saveQuery + dynamicFormSaveQuery.getSequence());
			} else {
				formSaveQuery = dynamicFormSaveQuery.getDynamicFormSaveQuery();
			}
			saveTemplateQuery = templateEngine.processTemplateContents(formSaveQuery, formName, formDetails);
			dynamicFormDAO.saveFormData(saveTemplateQuery, formDetails);
		}
		return true;
	}

	public String getContentForDevEnvironment(DynamicForm form, String dbContent, String fileName) throws Exception {

		String	ftlCustomExtension	= ".tgn";
		String	templateDirectory	= "DynamicForm";
		String	folderLocation		= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory + File.separator + form.getFormName();
		File directory = new File(folderLocation);
		if (!directory.exists()) {
			return dbContent;
		}

		File selectFile = new File(folderLocation + File.separator + fileName + ftlCustomExtension);
		if (selectFile.exists()) {
			return fileUtilities.readContentsOfFile(selectFile.getAbsolutePath());
		} else {
			return dbContent;
		}
	}

	public Map<String, String> createDefaultFormByTableName(String tableName, List<Map<String, Object>> tableDetails) {
		Map<String, String>	templatesMap	= new HashMap<>();
		Map<String, Object>	parameters		= new HashMap<>();
		parameters.put("columnDetails", tableDetails);
		try {
			TemplateVO	templateVO	= templateService.getTemplateByName("system-form-html-template");
			String		template	= templateEngine.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), parameters);
			templatesMap.put("form-template", template);
		} catch (Exception a_excep) {
			logger.error(a_excep);
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

	private void createSaveUpdateQueryTemplate(List<Map<String, Object>> tableInformation, String tableName,
			Map<String, String> templatesMap) {

		StringJoiner	insertJoiner		= new StringJoiner(",", "INSERT INTO " + tableName + " (", ")");
		StringJoiner	insertValuesJoiner	= null;
		for (Map<String, Object> info : tableInformation) {
			String	columnName	= info.get("tableColumnName").toString();
			String	dataType	= info.get("dataType").toString();
			String	columnKey	= info.get("columnKey").toString();
			insertValuesJoiner = createInsertQuery(insertValuesJoiner, tableName, columnName, dataType, columnKey);
			if (columnKey != null && columnKey.equals(PRIMARY_KEY)) {
				insertJoiner.add(columnName);
			}
		}
		for (Map<String, Object> info : tableInformation) {
			String	columnName	= info.get("tableColumnName").toString();
			String	dataType	= info.get("dataType").toString();
			String	columnKey	= info.get("columnKey").toString();
			if (StringUtils.isBlank(columnKey) || columnKey.equals(PRIMARY_KEY) == false) {
				insertJoiner.add(columnName);
				joinQueryBuilder(insertValuesJoiner, columnName, dataType, false);
			}

		}
		StringBuilder queryBuilder = new StringBuilder(insertJoiner.toString());
		queryBuilder.append(insertValuesJoiner);

		Map<String, Object> saveQueryparameters = new HashMap<>();
		saveQueryparameters.put("insertQuery", queryBuilder.toString());

		StringJoiner	updateQuery			= new StringJoiner(",", "UPDATE " + tableName + " SET ", "");
		StringJoiner	updateWhereQuery	= new StringJoiner(" AND ", " WHERE ", "");
		for (Map<String, Object> info : tableInformation) {
			String	columnName	= info.get("tableColumnName").toString();
			String	dataType	= info.get("dataType").toString();
			String	columnKey	= info.get("columnKey").toString();
			if ("PRI".equals(columnKey)) {
				joinQueryBuilder(updateWhereQuery, columnName, dataType, true);
			} else {
				joinQueryBuilder(updateQuery, columnName, dataType, true);
			}
		}
		StringBuilder updateQueryBuilder = new StringBuilder(updateQuery.toString());
		updateQueryBuilder.append(updateWhereQuery);
		saveQueryparameters.put("updateQuery", updateQueryBuilder.toString());

		try {
			TemplateVO	templateVO	= templateService.getTemplateByName("system-form-save-query-template");
			String		template	= templateEngine.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), saveQueryparameters);
			templatesMap.put("save-template", template);
		} catch (Exception a_excep) {
			logger.error(a_excep);
		}
	}

	private StringJoiner createInsertQuery(StringJoiner insertValuesJoiner, String tableName, String columnName,
			String dataType, String columnKey) {
		if (columnKey != null && columnKey.equals(PRIMARY_KEY)) {
			if (dataType.contains(VARCHAR) || dataType.contains(TEXT)) {
				if (insertValuesJoiner == null) {
					insertValuesJoiner = new StringJoiner(",", " VALUES (", ")");
				}
				String value = "UUID()";
				insertValuesJoiner.add(value.replace("\\", ""));
			} else if (dataType.contains(INT)) {
				if (insertValuesJoiner == null) {
					String tableQuery = " FROM " + tableName;
					insertValuesJoiner = new StringJoiner(",", " ", tableQuery);
				}
				String value = "SELECT CASE WHEN MAX(" + columnName + ") IS NULL THEN 1 ELSE MAX(" + columnName
						+ ") + 1 END ";
				insertValuesJoiner.add(value.replace("\\", ""));
			}
		}
		return insertValuesJoiner;
	}

	private void joinQueryBuilder(StringJoiner insertValuesJoiner, String columnName, String dataType,
			boolean showColumnName) {
		String formFieldName = columnName.replace("_", "");
		if (dataType.contains(VARCHAR) || dataType.contains(TEXT)) {
			String value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
			insertValuesJoiner.add(value.replace("\\", ""));
		} else if (dataType.contains(INT) || dataType.contains(DECIMAL)) {
			String value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
			insertValuesJoiner.add(value.replace("\\", ""));
		} else if (dataType.contains(DATE) || dataType.contains(TIMESTAMP)) {
			insertValuesJoiner.add(showColumnName ? columnName + " = NOW()" : "" + "NOW()");
		}
	}

	private Map<String, Object> createParamterMap(List<Map<String, String>> formData) {
		Map<String, Object> formParameters = new HashMap<String, Object>();
		for (Map<String, String> data : formData) {
			String	valueType	= data.getOrDefault("valueType", VARCHAR);
			Object	value		= getDataInTypeFormat(data.get("value"), valueType);
			formParameters.put(data.get("name"), value);
		}
		return formParameters;
	}

	private Object getDataInTypeFormat(String value, String valueType) {
		if (valueType.equals(INT)) {
			if (StringUtils.isBlank(value) == false) {
				return Integer.parseInt(value);
			}
		} else if (valueType.equals(DECIMAL)) {
			return Double.parseDouble(value);
		} else if (valueType.equals(DATE) || valueType.equals(TIMESTAMP)) {
			Date dateData = new Date();
			try {
				dateData = DateFormat.getInstance().parse(value);
			} catch (ParseException a_exc) {
				logger.warn("Error paring the date : ", a_exc);
			}
			return dateData;
		}
		return value;
	}
}
