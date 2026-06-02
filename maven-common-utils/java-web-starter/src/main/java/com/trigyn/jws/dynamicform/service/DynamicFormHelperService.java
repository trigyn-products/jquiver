package com.trigyn.jws.dynamicform.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.formio.dao.IFormIORepository;
import com.trigyn.jws.formio.entities.FormIO;

@Service
@Transactional
public class DynamicFormHelperService {

	private final static Logger				logger						= LoggerFactory
			.getLogger(DynamicFormHelperService.class);
	
	private static final String				DATE						= "date";
	
	private static final String				TIMESTAMP					= "timestamp";
	
	private static final String				DECIMAL						= "decimal";

	private static final String				TEXT						= "text";

	private static final String				INT							= "int";

	private static final String				VARCHAR						= "varchar";

	private static final String				PRIMARY_KEY					= "PK";

	private static final String				BOOLEAN						= "boolean";
	
	private static final String				TIME						= "time";
	
	@Autowired
	private DynamicFormCrudDAO				dynamicFormDAO				= null;

	@Autowired
	private IFormIORepository				formIORepository			= null;
	
	public void getMatchedColumnTableDetails(List<Map<String, Object>> formDetails,
			List<Map<String, Object>> tableDetails, boolean isFormIo) {
		Iterator itr = tableDetails.iterator();
		Set<String> matchedColumns = formDetails.stream().map(map -> map.get("column"))				
				.filter(Objects::nonNull)																								
				.map(Object::toString)																									
				.collect(Collectors.toSet());
		while (itr.hasNext()) {
			Map<String, Object> columnDetails = (Map<String, Object>) itr.next();
			String columnName = columnDetails.get("tableColumnName").toString();
			if (Boolean.FALSE.equals(matchedColumns.contains(columnName)) || null == columnDetails.get("columnType")) {
				itr.remove();
			} else {
				for (Map<String, Object> formDetailsMap : formDetails) {
					String colName = (String) formDetailsMap.get("column");
					String i18nResourceKey = (String) formDetailsMap.get("i18nResourceKey");
					String displayName = (String) formDetailsMap.get("displayName");
					if (StringUtils.isNotBlank(colName) && colName.equals(columnName)) {
						if (isFormIo) {
							columnDetails.put("columnName", columnName);
						}
						Boolean hiddenValue = (Boolean) formDetailsMap.get("hidden");
						if (hiddenValue != null && hiddenValue.equals(true)) {
							columnDetails.put("columnType", "hidden");
						}
						if (StringUtils.isBlank(i18nResourceKey) == false) {
							columnDetails.put("i18NPresent", true);
							if (isFormIo) {
								columnDetails.put("fieldName", "${" + i18nResourceKey + "}");
							} else {
								columnDetails.put("fieldName", i18nResourceKey);
							}
						} else {
							columnDetails.put("i18NPresent", false);
							columnDetails.put("fieldName", displayName);
						}
					}
				}
			}
		}
	}
	
	public StringJoiner createInsertQuery(StringJoiner insertValuesJoiner, String tableName, String columnName,
			String dataType, String columnKey, String dbProductName, String isAutoIncrement, String isHidden,
			Map<String, Object> saveQueryparameters) {

		logger.debug(
				"Inside DynamicFormService.createInsertQuery(insertValuesJoiner: {}, tableName: {}, columnName: {}, dataType: {}, columnKey: {}, dbProductName: {})",
				insertValuesJoiner, tableName, columnName, dataType, columnKey, dbProductName);
		if (insertValuesJoiner == null) {
			insertValuesJoiner = new StringJoiner("");
		}
		if (columnKey != null && columnKey.equals(PRIMARY_KEY)) {
			if ("false".equalsIgnoreCase(isAutoIncrement)) {
				if ("hidden".equalsIgnoreCase(isHidden) == true) {
					if (dataType.equalsIgnoreCase(TEXT)) {

						String value = " VALUES (UUID(),";
						if (dbProductName.contains(Constant.POSTGRESQL)) {
							value = " VALUES (uuid_generate_v4(),";
						} else if (dbProductName.contains(Constant.MSSQLSERVER)) {
							value = " VALUES (NEWID(),";
						} else if (dbProductName.contains(Constant.ORACLE)) {
							value = " VALUES (sys_guid(),";
						}

						insertValuesJoiner.add(value.replace("\\", ""));
					} else if (dataType.equalsIgnoreCase(INT) || dataType.equalsIgnoreCase(DECIMAL)) {

						String value = " SELECT  COALESCE(MAX(" + columnName + "),0) + 1 ,";
						insertValuesJoiner.add(value.replace("\\", ""));
					}
				} else {
					String	formFieldName	= columnName.replace("_", "");
					String value = " VALUES(:" + formFieldName + ",";
					insertValuesJoiner.add(value.replace("\\", ""));
				}
			} else {
				String value = " VALUES(";
				insertValuesJoiner.add(value.replace("\\", ""));
			}
		}
		return insertValuesJoiner;
	}
	
	public Map<String, Object> createParamterMap(List<Map<String, String>> formData) {
		logger.debug("Inside DynamicFormService.createParamterMap(formData: {})", formData);
		List				tmpValue		= null;

		Map<String, Object>	formParameters	= new HashMap<String, Object>();
		String formId = null;
		String saveQueryParametersType = null;
		for (Map<String, String> fmData : formData) {
			if(fmData.get("name").toString().equalsIgnoreCase("formId")) {
				formId = fmData.get("value").toString();
				break;
			}
		}
		//
		DynamicForm form = dynamicFormDAO.findDynamicFormById(formId);
		if (formData != null) {
			for (Map<String, String> data : formData) {
				String	valueType	= data.getOrDefault("valueType", VARCHAR);
				String	tmpName		= data.get("name");
				boolean isFormIO = false;
				HashMap<String,String> saveQueryParamMap = new HashMap<>();
				if(form!= null && form.getFormIoId() !=null && form.getFormIoId().isBlank() == false && form.getFormIoId().isEmpty() == false) {
					for (Map<String, String> fmData : formData) {
						if(fmData.get("name").toString().equalsIgnoreCase("saveQueryParametersType")) {
							saveQueryParametersType = fmData.get("value").toString();
							saveQueryParamMap = new Gson().fromJson(saveQueryParametersType, new TypeToken<HashMap<String, String>>(){}.getType());
							break;
						}
					}
					isFormIO = true;
				}
				Object	value		= getDataInTypeFormat(data.get("value"), valueType);
				if(isFormIO && form.getFormIoId() != null) {
					formParameters = setParamaterMapForFormIO(formParameters, data, form.getFormIoId(), tmpName, value, tmpValue, saveQueryParamMap);
				}
				if (formParameters.containsKey(tmpName)) {
					tmpValue = (List) formParameters.get(tmpName + "_");
					if (tmpValue == null) {
						tmpValue = new ArrayList();
						formParameters.put(tmpName + "_", tmpValue);
						tmpValue.add(formParameters.get(tmpName));
					}
					tmpValue.add(value);

				} else {
					formParameters.put(tmpName, value);
				}
			}
		}
		return formParameters;
	}
	
	public Object getDataInTypeFormat(Object value, String valueType) {

		logger.debug("Inside DynamicFormService.getDataInTypeFormat(value: {}, valueType: {})", value, valueType);
		if (value == null) {
			return null;
		}

		String tmpValue = String.valueOf(value);
		if(valueType !=null) {
			if (valueType.equalsIgnoreCase(INT)) {
				if (StringUtils.isBlank(tmpValue) == false) {
					return (int) Double.parseDouble(tmpValue);
				}
			} else if (valueType.equalsIgnoreCase(DECIMAL)) {
				return Double.parseDouble(tmpValue);
			} else if (valueType.equalsIgnoreCase(DATE) || valueType.equalsIgnoreCase(TIMESTAMP)) {
				Date dateData = new Date();
				try {
					if (null != tmpValue && false == tmpValue.isEmpty()) {
						dateData = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(tmpValue);
						// dateData = DateFormat.getInstance().parse(value);
					}
				} catch (ParseException a_exc) {
					logger.warn("Error parsing the date : " + tmpValue + " Expected format is dd-MMM-yyyy hh:mm:ss a");
				}
				return dateData;
			} else if (valueType.equalsIgnoreCase(TIME)) {
			    Date timeData = new Date();
			    try {
			        if (tmpValue != null && !tmpValue.isEmpty()) {
			            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			            timeFormat.setLenient(false);
			            timeData = timeFormat.parse(tmpValue);
			        }
			    } catch (ParseException e) {
			        logger.warn("Error parsing the time: " + tmpValue + " Expected format is HH:mm:ss");
			    }
			    return timeData;
			} else if (valueType.equalsIgnoreCase(BOOLEAN)) {
				if(tmpValue.equalsIgnoreCase("false") || tmpValue.equalsIgnoreCase("0")) {
					return 0;
				} else {
					return 1;
				}
				
				
			}
		}
		return value;
	}
	
	private Map<String, Object> setParamaterMapForFormIO(Map<String, Object> formParameters, Map<String, String> data,
			String formIoId, String tmpName, Object value, List tmpValue, HashMap<String, String> saveQueryParamMap) {

		String item = tmpName;
		boolean isInputElement = false;
		String rePattern = "\\[(.*?)]";
	    Pattern p = Pattern.compile(rePattern);
	    Matcher m = p.matcher(item);
		if (item != null) {
			while (m.find()) {
				for (Map.Entry<String, String> entry : saveQueryParamMap.entrySet()) {
					String key = entry.getKey();
					String val = entry.getValue();
					if (m.group(1).contentEquals(key)) {
						tmpName = m.group(1);
						isInputElement = true;
						break;
					}

				}
			}
		}
		if (isInputElement == false) {
			return formParameters;
		}
		// TODO :: Can we move out side of for loop.
		FormIO formIo = (FormIO) formIORepository.findById(formIoId).orElse(null);
		if (formIo != null && tmpName!=null && tmpName.isEmpty()== false) {
			String fieldName = tmpName;
			String dataType = saveQueryParamMap.get(fieldName);
			value		= getDataInTypeFormat(data.get("value"), dataType);
		}
		if (formParameters!=null && formParameters.containsKey(tmpName)) {
			tmpValue = (List) formParameters.get(tmpName + "_");
			if (tmpValue == null) {
				tmpValue = new ArrayList();
				formParameters.put(tmpName + "_", tmpValue);
				tmpValue.add(formParameters.get(tmpName));
			}
			tmpValue.add(value);

		} else {
			formParameters.put(tmpName, value);
		}
		return formParameters;
	}
	
}
