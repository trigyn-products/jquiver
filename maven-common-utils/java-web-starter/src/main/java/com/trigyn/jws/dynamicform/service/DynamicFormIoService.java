package com.trigyn.jws.dynamicform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.formio.dao.IFormIORepository;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.utils.FormIOUtils;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@Service
@Transactional
public class DynamicFormIoService {

	private final static Logger			logger						= LoggerFactory.getLogger(DynamicFormIoService.class);

	@Autowired
	private JwsDynamicRestDetailService	restDetailService			= null;

	private static final String			DATE						= "date";

	private static final String			TIMESTAMP					= "timestamp";

	private static final String			DECIMAL						= "decimal";

	private static final String			TEXT						= "text";

	private static final String			INT							= "int";

	private static final String			VARCHAR						= "varchar";

	private static final String			PRIMARY_KEY					= "PK";

	private static final String			BOOLEAN						= "boolean";

	private static final String			SMALLDATETIME				= "smalldatetime";

	private static final String			DATETIMEOFFSET				= "datetimeoffset";

	private static final String			TIME						= "time";

	private static final String			DATETIME2					= "datetime2";

	@Autowired
	private TemplatingUtils				templateEngine				= null;

	@Autowired
	private DBTemplatingService			templateService				= null;

	@Autowired
	protected SessionFactory			sessionFactory				= null;

	@Autowired
	private IFormIORepository			formIORepository			= null;

	@Autowired
	private DynamicFormHelperService	dynamicFormHelperService	= null;

	private static final String			DATETIME					= "datetime";

	public Map<String, String> createDefaultFormByTableName(String tableName, List<Map<String, Object>> tableDetails,
			String moduleURL, String additionalDataSourceId, String dbProductName, String formIoId)
			throws CustomStopException, JsonMappingException, JsonProcessingException {
		logger.debug(
				"Inside FormIO DynamicFormService.createDefaultFormByTableName(tableName: {}, tableDetails: {}, moduleURL: {}, additionalDataSourceId: {}, dbProductName: {}, isFormIo: {})",
				tableName, tableDetails, moduleURL, additionalDataSourceId, dbProductName);
		Map<String, Object>	selectParameters		= new HashMap<>();
		Map<String, String>	queryParametersTypes	= new HashMap();
		Map<String, String>	templatesMap			= new HashMap<>();
		Map<String, Object>	parameters				= new HashMap<>();
		Map<String, String>	regexMap				= new HashMap<>();
		parameters.put("columnDetails", tableDetails);
		parameters.put("formName", tableName);
		parameters.put("tableName", tableName);
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("[\n");
		int	inc					= 0;
		int	totalValidFields	= 0;
		try {
			for (Map<String, Object> info : tableDetails) {
				if (info.get("regexValidation") != null && !info.get("regexValidation").toString().isEmpty()
						&& !"hidden".equals(info.get("columnType"))
						&& "false".equalsIgnoreCase(info.get("autoIncrement").toString())) {
					totalValidFields++;
				}
			}
			for (Map<String, Object> info : tableDetails) {
				String	columnName		= info.get("tableColumnName").toString();
				String	columnKey		= info.get("columnKey").toString();
				String	dataType		= info.get("dataType").toString();
				String	regexInfo		= info.get("regexValidation").toString();
				String	columnType		= info.get("columnType").toString();
				String	isAutoIncrement	= info.get("autoIncrement").toString();

				queryParametersTypes.put(columnName, dataType);
				if ("PK".equals(columnKey)) {
					selectParameters.put("primaryKeyColumnName", columnName);
					parameters.put("primaryKeyColumnName", columnName);
					// break;
				}
				if (regexInfo != null && "hidden".equals(columnType) == false
						&& "false".equalsIgnoreCase(isAutoIncrement)) {
					String	displayName		= columnName.replaceAll("_", "");
					String	escapedRegex	= StringEscapeUtils.escapeJson(regexInfo);
					jsonBuilder.append("\t\t{\n");
					jsonBuilder.append("\t\t    \"regexPattern\" : \"").append(escapedRegex).append("\",\n");
					jsonBuilder.append("\t\t    \"fieldName\" : \"").append(columnName).append("\",\n");
					jsonBuilder.append("\t\t    \"dataType\" : \"").append(dataType).append("\"\n");
					jsonBuilder.append("\t\t}");
					inc++;
					if (inc < totalValidFields) {
						jsonBuilder.append(",");
					}
					jsonBuilder.append("\n");
					regexMap.put(columnName.replaceAll("_", ""), regexInfo);
				}
			}
			jsonBuilder.append("\t];\n");
			ObjectMapper	objectMapper	= new ObjectMapper();
			String			queryParamTypes	= objectMapper.writeValueAsString(queryParametersTypes);
			parameters.put("queryParametersType", queryParamTypes);
			objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			String jsonRegex = objectMapper.writeValueAsString(regexMap);
			parameters.put("fieldList", jsonBuilder);
			//For file bin component check
			FormIOUtils	formIOUtils		= new FormIOUtils();
			Object		formMetaData	= formIOUtils.getFormMetaDataById(formIoId);
			if (formMetaData != null) {
				ObjectMapper	objMapper	= new ObjectMapper();
				JsonNode		formIoJson		= objMapper.readTree(formMetaData.toString());
				boolean toggleFileBin = containsFileBin(formIoJson);
				parameters.put("toggleFileBin", toggleFileBin);
			}
			//Done file bin component check
			TemplateVO	templateVO	= templateService.getTemplateByName("formio-default-html-template");
			String		template	= templateEngine.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), parameters);
			templatesMap.put("form-template", template);

		} catch (CustomStopException custStopException) {
			logger.error("Error occured in Form IO : createDefaultFormByTableName.", custStopException);
			throw custStopException;
		} catch (Exception a_excep) {
			logger.error("Error occured in Form IO : createDefaultFormByTableName.", a_excep);
		}

		createFormIOSaveQueryTemplate(tableDetails, tableName, templatesMap, additionalDataSourceId, dbProductName,
				formIoId);
		return templatesMap;
	}

	private void createFormIOSaveQueryTemplate(List<Map<String, Object>> tableInformation, String tableName,
			Map<String, String> templatesMap, String additionalDataSourceId, String dbProductName, String formIoId)
			throws JsonMappingException, JsonProcessingException {

		logger.debug(
				"Inside DynamicFormService.getTableInformationByName(tableInformation: {}, tableName: {}, additionalDataSourceId: {}, dbProductName: {})",
				tableInformation, tableName, templatesMap, additionalDataSourceId, dbProductName);

		Map<String, Object>	saveQueryParameterTypes	= new HashMap<>();
		Map<String, Object>	selectParameters		= new HashMap<>();
		Map<String, Object>	saveQueryparameters		= new HashMap<>();
		List<String>		keyParams				= new ArrayList<>();
		List<String>		colmnsAs				= new ArrayList<>();
		ObjectMapper		objectMapper			= new ObjectMapper();
		List<String>		keys					= new ArrayList<>();
		StringJoiner		insertJoiner			= new StringJoiner(",", "INSERT INTO " + tableName + " (", ")");
		StringJoiner		insertValuesJoiner		= null;
		StringJoiner		queryParams				= new StringJoiner("\n");
		int					totalValidFields		= 0;
		boolean				isIntPK					= false, isAutoID = false;
		int					coloumnCounter			= keys.isEmpty() ? tableInformation.size() : keys.size();
		int					selectColoumnCounter	= coloumnCounter;

		if (formIoId != null) {
			formIORepository.findById(formIoId).ifPresent(form -> {
				try {
					JsonNode formIoJson = objectMapper.readTree(form.getFormIoJson());
					FormIOUtils.traverse(formIoJson, formIoId, keys);
				} catch (Exception e) {
					logger.error("Error parsing formIoJson", e);
				}
			});
		}

		for (Map<String, Object> info : tableInformation) {
			String columnType = (String) info.get("columnType");
			if (columnType == null) {
				continue;
			}
			String	columnName		= String.valueOf(info.get("tableColumnName"));
			String	dataType		= String.valueOf(info.get("dataType"));
			String	columnKey		= String.valueOf(info.get("columnKey"));
			String	isAutoIncrement	= String.valueOf(info.get("autoIncrement"));
			String	regex			= String.valueOf(info.getOrDefault("regexValidation", ""));
			boolean	isKeyPresent	= keys.isEmpty() || keys.contains(columnName);

			if (isKeyPresent) {
				queryParams.add(columnName + " ,");
				insertValuesJoiner = dynamicFormHelperService.createInsertQuery(insertValuesJoiner, tableName,
						columnName, dataType, columnKey, dbProductName, isAutoIncrement, columnType, saveQueryparameters);
				if ("false".equalsIgnoreCase(isAutoIncrement)) {
					insertJoiner.add(columnName);
					saveQueryParameterTypes.put(columnName, columnType);
					keyParams.add("'" + columnName + "'");
					joinFormIoQueryBuilder(insertValuesJoiner, columnName, dataType, false, columnType, dbProductName,
							coloumnCounter, isAutoIncrement, saveQueryparameters);
				}
			}

			if ("PK".equalsIgnoreCase(columnKey)) {
				if ("false".equalsIgnoreCase(isAutoIncrement) && isKeyPresent) {
					insertJoiner.add(columnName);
				}
				if ("INT".equalsIgnoreCase(dataType) || "DECIMAL".equalsIgnoreCase(dataType)) {
					isIntPK = true;
				}
			}

			if (!regex.isEmpty() && !"hidden".equalsIgnoreCase(columnType)
					&& "false".equalsIgnoreCase(isAutoIncrement)) {
				totalValidFields++;
			}
		}

		StringBuilder queryBuilder = new StringBuilder(insertJoiner.toString()).append(insertValuesJoiner);
		if (!isAutoID && !isIntPK) {
			queryBuilder.append(")");
		} else if (!isAutoID && isIntPK) {
			queryBuilder.append(" FROM ").append(tableName);
		} else {
			queryBuilder.append(")");
		}

		String insertQuery = "jq_updateDBQuery('" + queryBuilder + "', null, queryParam);";
		saveQueryparameters.put("insertQuery", insertQuery);
		saveQueryparameters.put("queryParams", insertJoiner.toString());

		StringJoiner	updateQuery			= new StringJoiner(",", "UPDATE " + tableName + " SET ", "");
		StringJoiner	updateWhereQuery	= new StringJoiner(" AND ", " WHERE ", "");
		StringJoiner	selectQueryJoiner	= new StringJoiner("");

		String			pkColumnValueType	= "";
		StringBuilder	jsonBuilder			= new StringBuilder("[\n");
		int				validFieldCounter	= 0;

		for (Map<String, Object> info : tableInformation) {
			String columnType = (String) info.get("columnType");
			if (columnType == null) {
				coloumnCounter--;
				continue;
			}

			String	columnName		= String.valueOf(info.get("tableColumnName"));
			String	dataType		= String.valueOf(info.get("dataType"));
			String	columnKey		= String.valueOf(info.get("columnKey"));
			String	isAutoIncrement	= String.valueOf(info.get("autoIncrement"));
			String	regex			= String.valueOf(info.getOrDefault("regexValidation", ""));

			if ("PK".equalsIgnoreCase(columnKey)) {
				saveQueryparameters.put("primaryKeyColumnName", columnName);
				selectParameters.put("primaryKeyColumnName", columnName);
				pkColumnValueType = extractPrimaryKeyDetails(columnName, dataType, columnKey, dbProductName,
						isAutoIncrement, saveQueryparameters, tableName, columnType);
			}

			joinFormIoQueryBuilder(updateWhereQuery, columnName, dataType, true, columnType, dbProductName,
					coloumnCounter, isAutoIncrement, saveQueryparameters);
			selectFormIoQueryBuilder(selectQueryJoiner, columnName, dataType, true, columnType, dbProductName,
					selectColoumnCounter--, isAutoIncrement, saveQueryparameters, colmnsAs);

			if (!regex.isEmpty() && !"hidden".equals(columnType) && "false".equalsIgnoreCase(isAutoIncrement)) {
				jsonBuilder.append("\t\t{\n").append("\t\t    \"regexPattern\" : \"")
						.append(StringEscapeUtils.escapeJson(regex)).append("\",\n")
						.append("\t\t    \"fieldName\" : \"").append(columnName).append("\",\n")
						.append("\t\t    \"dataType\" : \"").append(dataType).append("\"\n").append("\t\t}");
				if (++validFieldCounter < totalValidFields) {
					jsonBuilder.append(",");
				}
				jsonBuilder.append("\n");
			}
		}

		jsonBuilder.append("\t];\n");

		selectParameters.put("tableName", tableName);
		selectParameters.put("columnNames", String.join(",", colmnsAs));
		selectParameters.put("dataSourceId",
				StringUtils.isNotBlank(additionalDataSourceId) ? additionalDataSourceId : null);
		selectParameters.put("dbProductName", dbProductName.replaceAll("[\\[\\]]", ""));

		try {
			TemplateVO	templateVO	= templateService.getTemplateByName("formio-select-template");
			String		template	= templateEngine.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), selectParameters);
			templatesMap.put("select-template", template);
		} catch (Exception e) {
			logger.error("Error generating select template", e);
		}

		String updateQry = "jq_updateDBQuery('" + updateQuery + updateWhereQuery + "', null, queryParam);";
		saveQueryparameters.put("updateQuery", updateQry);
		saveQueryparameters.put("requestDetails", "");
		saveQueryparameters.put("tableName", tableName);
		saveQueryparameters.put("queryParams", String.join(",", keyParams));
		saveQueryparameters.put("pkColumnValueType", pkColumnValueType);
		saveQueryparameters.put("saveQueryParametersType", saveQueryParameterTypes.toString());
		saveQueryparameters.put("fieldList", jsonBuilder);
		saveQueryparameters.put("dataSourceId",
				StringUtils.isNotBlank(additionalDataSourceId) ? additionalDataSourceId : null);
		saveQueryparameters.put("dbProductName", dbProductName.replaceAll("[\\[\\]]", ""));

		try {
			TemplateVO	templateVO	= templateService.getTemplateByName("system-form-io-save-jquery-template");
			String		template	= templateEngine.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), saveQueryparameters);
			templatesMap.put("save-template", template);
		} catch (CustomStopException custStopException) {
			logger.error("Error occurred in createSaveUpdateQueryTemplate.", custStopException);
			throw custStopException;
		} catch (Exception e) {
			logger.error("Unexpected error", e);
		}
	}

	public void createFormIOSavebkQueryTemplates(List<Map<String, Object>> tableInformation, String tableName,
			Map<String, String> templatesMap, String additionalDataSourceId, String dbProductName, String formIoId)
			throws JsonMappingException, JsonProcessingException {
		logger.debug(
				"Inside DynamicFormService.getTableInformationByName(tableInformation: {}, tableName: {}, additionalDataSourceId: {}, dbProductName: {})",
				tableInformation, tableName, templatesMap, additionalDataSourceId, dbProductName);

		StringJoiner		insertJoiner			= new StringJoiner(",", "INSERT INTO " + tableName + " (", ")");
		StringJoiner		insertValuesJoiner		= null;
		boolean				isIntPK					= false;
		int					coloumnCounter			= tableInformation.size();
		int					selectColoumnCounter	= tableInformation.size();
		boolean				isAutoID				= false;
		StringJoiner		queryParams				= new StringJoiner("\n");
		List<String>		keys					= new ArrayList<>();
		List<String>		keyParams				= new ArrayList<>();
		Map<String, Object>	saveQueryParameterTypes	= new HashMap<>();
		Map<String, Object>	selectParameters		= new HashMap<>();
		Map<String, Object>	saveQueryparameters		= new HashMap<>();
		int					totalValidFields		= 0;
		if (formIoId != null) {
			FormIO form = (FormIO) formIORepository.findById(formIoId).orElse(null);
			if (form != null) {
				ObjectMapper	objectMapper	= new ObjectMapper();
				JsonNode		formIoJson		= objectMapper.readTree(form.getFormIoJson());
				FormIOUtils.traverse(formIoJson, formIoId, keys);
				coloumnCounter			= keys.size();
				selectColoumnCounter	= keys.size();
			}
		}

		for (Map<String, Object> info : tableInformation) {
			String columnType = (String) info.get("columnType");
			if (columnType == null) {
				continue;
			}
			String columnName = info.get("tableColumnName").toString();
			if (keys.contains(columnName)) {
				queryParams.add(columnName + " ,");
			}
			String	dataType		= info.get("dataType").toString();
			String	columnKey		= info.get("columnKey").toString();
			String	isAutoIncrement	= info.get("autoIncrement").toString();
			if ("true".equalsIgnoreCase(isAutoIncrement)) {
				isAutoID = true;
			}
			if (keys.contains(columnName)) {
				insertValuesJoiner = dynamicFormHelperService.createInsertQuery(insertValuesJoiner, tableName,
						columnName, dataType, columnKey, dbProductName, isAutoIncrement, columnType, saveQueryparameters);
			}
			if (columnKey != null && PRIMARY_KEY.equals(columnKey)) {
				if ("false".equalsIgnoreCase(isAutoIncrement) && keys.contains(columnName)) {
					insertJoiner.add(columnName);
				}
				if (INT.equalsIgnoreCase(dataType) || DECIMAL.equalsIgnoreCase(dataType)) {
					isIntPK = true;
				}
			}
			if (info.get("regexValidation") != null && !info.get("regexValidation").toString().isEmpty()
					&& !"hidden".equals(info.get("columnType"))
					&& "false".equalsIgnoreCase(info.get("autoIncrement").toString())) {
				totalValidFields++;
			}
		}

		for (Map<String, Object> info : tableInformation) {
			if (info.get("columnType") == null) {
				coloumnCounter--;
				continue;
			}

			String	columnName		= info.get("tableColumnName").toString();
			String	dataType		= info.get("dataType").toString();
			String	columnKey		= info.get("columnKey").toString();
			String	columnType		= info.get("columnType").toString();
			String	isAutoIncrement	= info.get("autoIncrement").toString();
			String	strColumnName	= "'" + columnName + "'";
			if (columnName != null && columnName.isEmpty() == false && "false".equalsIgnoreCase(isAutoIncrement)
					&& keyParams.contains(strColumnName) == false) {
				keyParams.add(strColumnName);
				saveQueryParameterTypes.put(columnName, columnType);
				insertJoiner.add(columnName);
				joinFormIoQueryBuilder(insertValuesJoiner, columnName, dataType, false, columnType, dbProductName,
						coloumnCounter, isAutoIncrement, saveQueryparameters);
			}
			coloumnCounter--;
		}

		StringBuilder queryBuilder = new StringBuilder(insertJoiner.toString());
		queryBuilder.append(insertValuesJoiner);

		if (!isAutoID) {
			if (isIntPK) {
				queryBuilder.append(" FROM " + tableName);
			} else {
				queryBuilder.append(")");
			}
		} else {
			queryBuilder.append(")");
		}
		String insertQuery = "jq_updateDBQuery('" + queryBuilder.toString() + "', null, queryParam);";

		saveQueryparameters.put("insertQuery", insertQuery);
		saveQueryparameters.put("queryParams", insertJoiner.toString());
		StringJoiner	updateQuery			= new StringJoiner(",", "UPDATE " + tableName + " SET ", "");
		StringJoiner	updateWhereQuery	= new StringJoiner(" AND ", " WHERE ", "");
		StringJoiner	selectQueryJoiner	= new StringJoiner("");
		String			pkColumnValueType	= "";
		List<String>	colmnsAs			= new ArrayList();
		StringBuilder	jsonBuilder			= new StringBuilder();
		jsonBuilder.append("[\n");
		int inc = 0;
		for (Map<String, Object> info : tableInformation) {
			if (info.get("columnType") == null) {
				coloumnCounter--;
				continue;
			}
			selectColoumnCounter--;
			String	columnName		= info.get("tableColumnName").toString();
			String	dataType		= info.get("dataType").toString();
			String	columnKey		= info.get("columnKey").toString();
			String	columnType		= info.get("columnType").toString();
			String	isAutoIncrement	= info.get("autoIncrement").toString();
			String	regexInfo		= info.get("regexValidation").toString();
			if ("PK".equals(columnKey)) {
				saveQueryparameters.put("primaryKeyColumnName", columnName);
				selectParameters.put("primaryKeyColumnName", columnName);
				pkColumnValueType = extractPrimaryKeyDetails(columnName, dataType, columnKey, dbProductName,
						isAutoIncrement, saveQueryparameters, tableName, columnType);
			}
			joinFormIoQueryBuilder(updateWhereQuery, columnName, dataType, true, columnType, dbProductName,
					coloumnCounter, isAutoIncrement, saveQueryparameters);
			selectFormIoQueryBuilder(selectQueryJoiner, columnName, dataType, true, columnType, dbProductName,
					selectColoumnCounter, isAutoIncrement, saveQueryparameters, colmnsAs);
			if (regexInfo != null && !regexInfo.isEmpty() && columnType.equals("hidden") == false
					&& isAutoIncrement.equalsIgnoreCase("false")) {
				String escapedRegex = StringEscapeUtils.escapeJson(regexInfo);

				jsonBuilder.append("\t\t{\n");
				jsonBuilder.append("\t\t    \"regexPattern\" : \"").append(escapedRegex).append("\",\n");
				jsonBuilder.append("\t\t    \"fieldName\" : \"").append(columnName).append("\",\n");
				jsonBuilder.append("\t\t    \"dataType\" : \"").append(dataType).append("\"\n");
				jsonBuilder.append("\t\t}");
				inc++;
				if (inc < totalValidFields) {
					jsonBuilder.append(",");
				}
				jsonBuilder.append("\n");
			}
		}
		jsonBuilder.append("\t];\n");

		String columnNames = StringUtils.join(colmnsAs, ",");
		selectParameters.put("tableName", tableName);
		selectParameters.put("columnNames", columnNames);

		if (additionalDataSourceId != null && !additionalDataSourceId.isBlank()) {
			selectParameters.put("dataSourceId", additionalDataSourceId);
		} else {
			selectParameters.put("dataSourceId", null);
		}
		selectParameters.put("dbProductName", dbProductName.replaceAll("[\\[\\]]", ""));
		TemplateVO	templateVO;
		String		template;
		try {
			templateVO	= templateService.getTemplateByName("formio-select-template");
			template	= templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					selectParameters);
			templatesMap.put("select-template", template);
		} catch (Exception exec) {
			logger.error(exec.getMessage());
		}

		StringBuilder updateQueryBuilder = new StringBuilder(updateQuery.toString());
		updateQueryBuilder.append(updateWhereQuery);
		String updateQry = "jq_updateDBQuery('" + updateQueryBuilder.toString() + "', null, queryParam);";
		saveQueryparameters.put("updateQuery", updateQry);
		StringBuilder requestDetails = new StringBuilder();
		saveQueryparameters.put("requestDetails", requestDetails.toString());
		saveQueryparameters.put("tableName", tableName);
		saveQueryparameters.put("queryParams", String.join(",", keyParams));
		saveQueryparameters.put("pkColumnValueType", pkColumnValueType);
		saveQueryparameters.put("saveQueryParametersType", saveQueryParameterTypes.toString());
		saveQueryparameters.put("fieldList", jsonBuilder);
		if (additionalDataSourceId != null && !additionalDataSourceId.isBlank()) {
			saveQueryparameters.put("dataSourceId", additionalDataSourceId);
		} else {
			saveQueryparameters.put("dataSourceId", null);
		}
		saveQueryparameters.put("dbProductName", dbProductName.replaceAll("[\\[\\]]", ""));
		try {
			templateVO	= templateService.getTemplateByName("system-form-io-save-jquery-template");
			template	= templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					saveQueryparameters);
			templatesMap.put("save-template", template);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in createSaveUpdateQueryTemplate.", custStopException);
			throw custStopException;
		} catch (Exception a_excep) {
			a_excep.printStackTrace();
			logger.error("Error occured in createSaveUpdateQueryTemplate.", a_excep);
		}
	}

	private void joinFormIoQueryBuilder(StringJoiner insertValuesJoiner, String columnName, String dataType,
			boolean showColumnName, String columnType, String dbProductName, int coloumnCounter, String isAutoIncrement,
			Map<String, Object> saveQueryparameters) {
		logger.debug(
				"Inside DynamicFormService.joinFormIoQueryBuilder(insertValuesJoiner: {}, columnName: {}, dataType: {}, showColumnName: {}, columnType: {}, dbProductName: {})",
				insertValuesJoiner, columnName, dataType, showColumnName, columnType, dbProductName);

		if (insertValuesJoiner == null) {
			insertValuesJoiner = new StringJoiner("");
		}

		String			formFieldName	= columnName;
		StringBuilder	formFieldVal	= new StringBuilder();
		String			value			= null;

		if ("false".equalsIgnoreCase(isAutoIncrement)) {
			if (TEXT.equalsIgnoreCase(dataType)) {
				value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
				insertValuesJoiner.add(value.replace("\\", ""));
			} else if (INT.equalsIgnoreCase(dataType) || DECIMAL.equalsIgnoreCase(dataType)
					|| BOOLEAN.equalsIgnoreCase(dataType)) {
				value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
				insertValuesJoiner.add(value.replace("\\", ""));
			} else if (DATE.equalsIgnoreCase(dataType) || TIMESTAMP.equalsIgnoreCase(dataType)
					|| DATETIME.equalsIgnoreCase(dataType) || DATETIMEOFFSET.equalsIgnoreCase(dataType)
					|| SMALLDATETIME.equalsIgnoreCase(dataType) || DATETIME2.equalsIgnoreCase(dataType)) {
				if ("hidden".equals(columnType) == false) {
					value = "";
					if (dbProductName != null && dbProductName.contains(Constant.POSTGRESQL)) {
						value = "TO_DATE(:".concat(formFieldName).concat(", 'DD-MONTH-YYYY') ");
					} else if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
						if ("smalldatetime".equalsIgnoreCase(dataType)) {
							value = "CONVERT(smalldatetime, :".concat(formFieldName).concat(") ");
						} else if ("datetime2".equalsIgnoreCase(dataType)) {
							value = "CONVERT(datetime2, :".concat(formFieldName).concat(") ");
						} else if ("datetimeoffset".equalsIgnoreCase(dataType)) {
							value = "CONVERT(datetimeoffset, :".concat(formFieldName).concat(") ");
						} else {
							value = "CONVERT(datetime, :".concat(formFieldName).concat(") ");
						}

					} else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
						value = "TO_TIMESTAMP(:".concat(formFieldName).concat(", 'DD-MON-YYYY HH24:MI:SS') ");
					} else {
						value = "STR_TO_DATE(:".concat(formFieldName).concat(", \"%d-%M-%Y\") ");
					}
					formFieldVal.append(value);

				} else {
					if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
						formFieldVal.append("GETDATE()");
					} else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
						formFieldVal.append("SYSDATE");
					} else {
						formFieldVal.append("NOW()");
					}
				}
				insertValuesJoiner.add(showColumnName ? columnName + " = " + formFieldVal : "" + formFieldVal);
			} else if (TIME.equalsIgnoreCase(dataType)) {
				if (columnType.equals("hidden") == false) {
					value = "";
					if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
						value = "CONVERT(time,:".concat(formFieldName).concat(") ");
						formFieldVal.append(value);
					}
				}
				insertValuesJoiner.add(showColumnName ? columnName + " = " + formFieldVal : "" + formFieldVal);
			}
		} else {
			value = showColumnName ? columnName + " = :" + formFieldName : ":" + formFieldName;
			insertValuesJoiner.add(value.replace("\\", ""));
		}
		if (coloumnCounter > 2) {
			value = ",";
			insertValuesJoiner.add(value);
		}
	}

	public void selectFormIoQueryBuilder(StringJoiner selectValuesJoiner, String columnName, String dataType,
			boolean showColumnName, String columnType, String dbProductName, int coloumnCounter, String isAutoIncrement,
			Map<String, Object> saveQueryparameters, List<String> colmnsAs) {
		logger.debug(
				"Inside DynamicFormService.selectFormIoQueryBuilder(insertValuesJoiner: {}, columnName: {}, dataType: {}, showColumnName: {}, columnType: {}, dbProductName: {})",
				selectValuesJoiner, columnName, dataType, showColumnName, columnType, dbProductName);

		if (selectValuesJoiner == null) {
			selectValuesJoiner = new StringJoiner("");
		}

		String			formFieldName	= columnName;
		StringBuilder	formFieldVal	= new StringBuilder();
		String			value			= formFieldName;

		if (dbProductName != null
				&& (dbProductName.contains(Constant.MSSQLSERVER) || dbProductName.contains(Constant.ORACLE))) {

			if ("false".equalsIgnoreCase(isAutoIncrement)) {
				if (TEXT.equalsIgnoreCase(dataType) || INT.equalsIgnoreCase(dataType)
						|| DECIMAL.equalsIgnoreCase(dataType) || BOOLEAN.equalsIgnoreCase(dataType)) {
					value = formFieldName + " AS " + value;
					selectValuesJoiner.add(value.replace("\\", ""));
					colmnsAs.add(value.replace("\\", ""));

				} else if (DATE.equalsIgnoreCase(dataType)) {
					if (columnType.equals("hidden") == false) {
						value = "";
						if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
							value = "CONVERT(varchar(10), " + formFieldName + ", 120) AS " + formFieldName;
						} else if (dbProductName != null && dbProductName.contains(Constant.POSTGRESQL)) {
							value = "TO_CHAR(" + formFieldName + ", 'YYYY-MM-DD') AS " + formFieldName;
						} else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
							value = "TO_CHAR(" + formFieldName + ", 'YYYY-MM-DD') AS " + formFieldName;
						} else {
							value = formFieldName;
						}
						selectValuesJoiner.add(value.replace("\\", ""));
						colmnsAs.add(value.replace("\\", ""));
						formFieldVal.append(value);
					}

				} else if (TIME.equalsIgnoreCase(dataType)) {
					if (columnType.equals("hidden") == false) {
						value = "";
						if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
							value = "CONVERT(varchar(8), " + formFieldName + ", 108) AS " + formFieldName;
						} else if (dbProductName != null && dbProductName.contains(Constant.POSTGRESQL)) {
							value = "TO_CHAR(" + formFieldName + ", 'HH24:MI') AS " + formFieldName;
						} else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
							value = "TO_CHAR(" + formFieldName + ", 'HH24:MI') AS " + formFieldName;
						} else {
							value = formFieldName;
						}
						selectValuesJoiner.add(value.replace("\\", ""));
						colmnsAs.add(value.replace("\\", ""));
						formFieldVal.append(value);
					}
				} else if (DATETIME2.equalsIgnoreCase(dataType)) {
					if ("hidden".equals(columnType) == false) {
						value = "";
						if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
							value = "CONVERT(varchar(23), " + formFieldName + ", 121) AS " + formFieldName;
						} else {
							value = formFieldName;
						}
						selectValuesJoiner.add(value.replace("\\", ""));
						colmnsAs.add(value.replace("\\", ""));
						formFieldVal.append(value);
					}

				} else if (SMALLDATETIME.equalsIgnoreCase(dataType) || DATETIMEOFFSET.equalsIgnoreCase(dataType)
						|| TIMESTAMP.equalsIgnoreCase(dataType) || DATETIME.equalsIgnoreCase(dataType)) {
					if ("hidden".equals(columnType) == false) {
						value = "";
						if (dbProductName != null && dbProductName.contains(Constant.POSTGRESQL)) {
							value = "TO_CHAR(" + formFieldName + ", 'DD-MONTH-YYYY') AS " + formFieldName;
						} else if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
							if (dataType.equalsIgnoreCase("smalldatetime")) {
								value = "CONVERT(varchar(19), " + formFieldName + ", 120) AS " + formFieldName; // 'yyyy-mm-dd
																												// hh:mi:ss'
							} else if ("datetime2".equalsIgnoreCase(dataType)) {
								value = "FORMAT(" + formFieldName + ", 'yyyy-MM-dd HH:mm:ss.fff') AS " + formFieldName;
							} else if ("datetimeoffset".equalsIgnoreCase(dataType)) {
								value = "FORMAT(" + formFieldName + ", 'yyyy-MM-dd HH:mm:ss.fff zzz') AS "
										+ formFieldName;
							} else {
								value = "CONVERT(varchar(19), " + formFieldName + ", 120) AS " + formFieldName;
							}

						} else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
							value = "TO_CHAR(" + formFieldName + ", 'YYYY-MM-DD HH24:MI:SS') AS " + formFieldName;
						} else {
							value = "CONVERT(" + formFieldName + ", CHAR) AS " + formFieldName;
						}
						selectValuesJoiner.add(value.replace("\\", ""));
						colmnsAs.add(value.replace("\\", ""));
						formFieldVal.append(value);
					} else {
						if (dbProductName != null && dbProductName.contains(Constant.MSSQLSERVER)) {
							formFieldVal.append("GETDATE()");
						} else if (dbProductName != null && dbProductName.contains(Constant.ORACLE)) {
							formFieldVal.append("SYSDATE");
						} else {
							formFieldVal.append("NOW()");
						}
						colmnsAs.add(formFieldVal.toString());
						selectValuesJoiner.add(formFieldVal);
					}

				} else {
					value = formFieldName + " AS " + formFieldName;
					colmnsAs.add(value);
					selectValuesJoiner.add(value.replace("\\", ""));
				}
			} else {
				value = formFieldName + " AS " + formFieldName;
				colmnsAs.add(value);
				selectValuesJoiner.add(value.replace("\\", ""));
			}

		} else {
			if (isAutoIncrement.equalsIgnoreCase("false")) {
				String castedColumn = FormIOUtils.getCastExpressionForSelect(columnName, dataType, dbProductName);
				colmnsAs.add(castedColumn);
			} else {
				value = formFieldName + " AS " + formFieldName;
				colmnsAs.add(value);
				selectValuesJoiner.add(value.replace("\\", ""));
			}
		}

		if (coloumnCounter > 0) {
			value = ",";
			selectValuesJoiner.add(value);
		}
	}

	private String extractPrimaryKeyDetails(String columnName, String dataType, String columnKey, String dbProductName,
			String isAutoIncrement, Map<String, Object> saveQueryparameters, String tableName, String columnType) {
		saveQueryparameters.put("isAutoIncrement", isAutoIncrement);
		String pkColumnValueType = "";
		if ("false".equalsIgnoreCase(isAutoIncrement)) {
			if (columnKey != null && PRIMARY_KEY.equals(columnKey)) {
				if (TEXT.equalsIgnoreCase(dataType)) {
					pkColumnValueType = "UUID()";
					if (dbProductName.contains(Constant.POSTGRESQL)) {
						if (saveQueryparameters != null) {
							pkColumnValueType = "uuid_generate_v4()";
						}
					} else if (dbProductName.contains(Constant.MSSQLSERVER)) {
						if (saveQueryparameters != null) {
							pkColumnValueType = "NEWID()";
						}
					} else if (dbProductName.contains(Constant.ORACLE)) {
						if (saveQueryparameters != null) {
							pkColumnValueType = "sys_guid()";
						}
					}

				} else if (INT.equalsIgnoreCase(dataType) || DECIMAL.equalsIgnoreCase(dataType)) {
					if ("hidden".equalsIgnoreCase(columnType)) {
						if (dbProductName.contains(Constant.ORACLE)) {
							pkColumnValueType = "(SELECT COALESCE(MAX(" + columnName + "), 0) + 1 FROM " + tableName
									+ " sel" + columnName + ")";
						} else {
							pkColumnValueType = "(SELECT COALESCE(MAX(" + columnName + "), 0) + 1 FROM " + tableName
									+ " AS sel" + columnName + ")";
						}
					} else {
						pkColumnValueType = dataType;
					}
				}
			}
		} else if (columnKey != null && PRIMARY_KEY.equals(columnKey)
				&& (INT.equalsIgnoreCase(dataType) || DECIMAL.equalsIgnoreCase(dataType))) {
			pkColumnValueType = "(SELECT COALESCE(MAX(" + columnName + "), 0) + 1 FROM " + tableName + " AS sel"
					+ columnName + ")";
		}
		saveQueryparameters.put("pkColumnValueType", pkColumnValueType);

		return pkColumnValueType;

	}

	public Map<String, String> createFormIoHtmlByTableName(String tableName, List<Map<String, Object>> tableDetails,
			String moduleURL, String dataSourceId, String dbProductName, Boolean toggleCaptcha, Boolean toggleCsrf,
			Boolean toggleFileBin, String fileBinId, String fileAssociationId) {

		logger.debug(
				"Inside DynamicFormService.createDefaultFormByTableName(tableName: {}, tableDetails: {}, moduleURL: {}, additionalDataSourceId: {}, dbProductName: {})",
				tableName, tableDetails, moduleURL, dataSourceId, dbProductName, toggleCaptcha, toggleCsrf,
				toggleFileBin, fileBinId, fileAssociationId);

		Map<String, String>	templatesMap			= new HashMap<>();
		Map<String, Object>	parameters				= new HashMap<>();
		Map<String, String>	queryParametersTypes	= new HashMap();
		Map<String, Object>	saveQueryparameters		= new HashMap<>();
		List<String>		colmnsAs				= new ArrayList<String>();
		Map<String, String>	regexMap				= new HashMap<>();
		StringBuilder		jsonBuilder				= new StringBuilder();
		jsonBuilder.append("[\n");
		int	inc					= 0;
		int	totalValidFields	= 0;
		try {
			String pkColumnValueType = "";
			for (Map<String, Object> info : tableDetails) {
				if (info.get("regexValidation") != null && !info.get("regexValidation").toString().isEmpty()
						&& !"hidden".equals(info.get("columnType"))
						&& "false".equalsIgnoreCase(info.get("autoIncrement").toString())) {
					totalValidFields++;
				}
			}
			for (Map<String, Object> info : tableDetails) {
				if (info.get("columnType") == null) {
					continue;
				}
				String	columnName		= info.get("tableColumnName").toString();
				String	dataType		= info.get("dataType").toString();
				String	columnKey		= info.get("columnKey").toString();
				String	columnType		= info.get("columnType").toString();
				String	isAutoIncrement	= info.get("autoIncrement").toString();
				String	regexInfo		= info.get("regexValidation").toString();
				queryParametersTypes.put(columnName, columnType);
				if ("PK".equals(columnKey)) {
					templatesMap.put("primaryKeyColumnName", columnName);
					parameters.put("primaryKeyColumnName", columnName);
					pkColumnValueType = extractPrimaryKeyDetails(columnName, dataType, columnKey, dbProductName,
							isAutoIncrement, saveQueryparameters, tableName, columnType);

				}
				if (regexInfo != null && "hidden".equals(columnType) == false
						&& "false".equalsIgnoreCase(isAutoIncrement)) {
					String escapedRegex = StringEscapeUtils.escapeJson(regexInfo);
					jsonBuilder.append("\t\t{\n");
					jsonBuilder.append("\t\t    \"regexPattern\" : \"").append(escapedRegex).append("\",\n");
					jsonBuilder.append("\t\t    \"fieldName\" : \"").append(columnName).append("\",\n");
					jsonBuilder.append("\t\t    \"dataType\" : \"").append(dataType).append("\"\n");
					jsonBuilder.append("\t\t}");
					inc++;
					if (inc < totalValidFields) {
						jsonBuilder.append(",");
					}
					jsonBuilder.append("\n");
					regexMap.put(columnName, regexInfo);
				}
			}
			jsonBuilder.append("\t];\n");
			ObjectMapper	objectMapper	= new ObjectMapper();
			String			queryParamTypes	= objectMapper.writeValueAsString(queryParametersTypes);
			parameters.put("queryParametersType", queryParamTypes);
			String columsAsCsv = StringUtils.join(colmnsAs, ",");
			parameters.put("tableName", tableName);
			parameters.put("columnNames", columsAsCsv);
			if (StringUtils.isBlank(moduleURL) == false) {
				parameters.put("moduleURL", moduleURL);
			}
			parameters.put("fieldList", jsonBuilder);
			//Setting toggleFileBin for fileconfigrenderer condition in formio-default-html-template
			parameters.put("toggleFileBin", toggleFileBin);
			TemplateVO	templateVO	= templateService.getTemplateByName("formio-default-html-template");
			String		template	= templateEngine.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), parameters);
			templatesMap.put("form-template", template);

			Map<String, Object> selectParameters = new HashMap<>();
			selectParameters.put("tableName", tableName);
			templateVO	= templateService.getTemplateByName("system-form-select-template");
			template	= templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					selectParameters);
			templatesMap.put("select-template", template);
			createFormIOSaveQueryTemplate(tableDetails, tableName, templatesMap, dataSourceId, dbProductName, null);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in createDefaultFormByTableName.", custStopException);
			throw custStopException;
		} catch (Exception a_excep) {
			logger.error("Error occured in createDefaultFormByTableName.", a_excep);
		}
		return templatesMap;

	}
	
	private static boolean containsFileBin(JsonNode node) {
	    if (node == null) {
	        return false;
	    }

	    // Check current node
	    if (node.has("type") &&
	        "filebincomponent".equals(node.get("type").asText())) {
	        return true;
	    }

	    // Traverse children
	    if (node.isContainerNode()) {
	        for (JsonNode child : node) {
	            if (containsFileBin(child)) {
	                return true;
	            }
	        }
	    }

	    return false;
	}


}
