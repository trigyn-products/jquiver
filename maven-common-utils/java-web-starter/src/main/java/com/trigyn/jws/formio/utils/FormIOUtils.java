package com.trigyn.jws.formio.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trigyn.jws.dbutils.service.ApplicationContextProvider;
import com.trigyn.jws.dynamicform.dao.DynamicFormDAO;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.vo.FormIOLogicAction;
import com.trigyn.jws.formio.vo.FormIOVO;
import com.trigyn.jws.formio.vo.Trigger;
import com.trigyn.jws.resourcebundle.dao.ResourceBundleDAO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class FormIOUtils {

	private final static Logger logger = LoggerFactory.getLogger(FormIOUtils.class);

	private DynamicFormDAO dynamicFormDAO = ApplicationContextProvider.getBean(DynamicFormDAO.class);

	private ResourceBundleDAO	resourceBundleDAO	= ApplicationContextProvider
			.getBean(ResourceBundleDAO.class);

	HttpServletRequest request = (HttpServletRequest) ((ServletRequestAttributes) Objects
			.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

	public Object getFormMetaData(String formIoId) throws Exception {
		FormIO form = (FormIO) dynamicFormDAO.getFormIoMetaData(formIoId);

		if (form != null && form.getFormIoJson() != null) {
			Map<String, String> labelMap = new HashMap<>();
			String formJsonMetaData = form.getFormIoJson();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(formJsonMetaData);
			updateLabelMap(jsonNode, labelMap);
			updateLabelJsonNode(jsonNode, labelMap);
			updateKeyJsonNode(jsonNode);
			String modJson = jsonNode.toString();
			return modJson;
		}
		return null;
	}
	
	public Object getFormMetaDataById(String formIoId) throws Exception {
		FormIO form = (FormIO) dynamicFormDAO.getFormIoMetaDataById(formIoId);

		if (form != null && form.getFormIoJson() != null) {
			String formJsonMetaData = form.getFormIoJson();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(formJsonMetaData);
			String modJson = jsonNode.toString();
			return modJson;
		}
		return null;
	}
	
	public void updateLabelMap(JsonNode node, Map<String, String> labelMap) {
		if (node.isObject()) {
			node.fields().forEachRemaining(entry -> {
				String key = entry.getKey().toString();
				String val = entry.getValue().toString();
				if (key != null && key.equalsIgnoreCase("label") && val != null) {
					getLabelDetails(val, labelMap);
				}
				updateLabelMap(entry.getValue(), labelMap);
			});
		} else if (node.isArray()) {
			node.elements().forEachRemaining(element -> updateLabelMap(element, labelMap));
		} else {
			// logger.info("Value: " + node);
		}
	}

	public static void updateLabelJsonNode(JsonNode node, Map<String, String> keyMap) {
		if (node.isObject()) { // checks whether node is a json Object
			Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

			while (fields.hasNext()) { // Loop through each field in the JSON object
				Map.Entry<String, JsonNode>	entry		= fields.next();
				String						fieldName	= entry.getKey();
				JsonNode					value		= entry.getValue();

				// Only update "label" if:
				// 1. The parent is a component (has a "key")
				// 2. Not inside a "data.values" structure
				if ("label".equals(fieldName) && value != null && value.isTextual() && node.has("key") // Ensure it's a
																										// component
						&& isInsideValuesArray(node) == false) {

					String	oldLabel	= value.asText();		// Gets the current label text
					String	newLabel	= keyMap.get(oldLabel);	// Looks up the corresponding new label in keyMap.

					if (newLabel != null) {
						((ObjectNode) node).put("label", newLabel);// If a new label is found, updates the "label" in
																	// the JSON.
					}
				}

				// Recurse
				updateLabelJsonNode(value, keyMap);// This ensures deep traversal through the entire JSON tree.
			}

		} else if (node.isArray()) { // If the current node is an array
			for (JsonNode element : node) {
				updateLabelJsonNode(element, keyMap); // If the node is an array (like "components": [ {...}, {...} ]),
														// apply the same recursive logic for each item.
			}
		}
	}

	// Utility: Check if the node is inside "data.values"
	private static boolean isInsideValuesArray(JsonNode node) {
		JsonNode parent = node;
		while (parent != null) {
			if (parent.has("dataSrc") && "values".equals(parent.get("dataSrc").asText())) {
				return true;
			}
			parent = parent.get("parent"); // This line is symbolic: Jackson nodes don't track parents
			break; // Stop here since we can't actually go up
		}
		return false;
	}

	public static Cookie getCookie(HttpServletRequest req, String name) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public JSONObject parseLabelObject(JSONObject json, String key, Map keyList) throws JSONException {

		String[] keyInitial = StringUtils.substringsBetween(json.get(key).toString(), "{", "}");
		String value = keyInitial.length > 0 ? keyInitial[0] : json.get(key).toString();
		Cookie locale = getCookie(request, "locale");
		try {
			String langText = resourceBundleDAO.findByKeyAndLanguageCode(value, locale.getValue(), "en_US", 0);
			keyList.put(json.get(key), langText);
		} catch (Exception exec) {
			exec.printStackTrace();
		}
		return json;

	}

	public static void traverse(JsonNode root, String key, List<String> keyList) {

		if (root.isObject()) {
			Iterator<String> fieldNames = root.fieldNames();

			while (fieldNames.hasNext()) {
				String fieldName = fieldNames.next();
				JsonNode fieldValue = root.get(fieldName);
				if (fieldName.equalsIgnoreCase("key")) {
					keyList.add(fieldValue.asText());
				}

				traverse(fieldValue, key, keyList);
			}
		} else if (root.isArray()) {
			ArrayNode arrayNode = (ArrayNode) root;
			for (int i = 0; i < arrayNode.size(); i++) {
				JsonNode arrayElement = arrayNode.get(i);
				traverse(arrayElement, key, keyList);
			}
		} else {
			// JsonNode root represents a single value field - do something with it.

		}
	}

	public void getLabelDetails(String label, Map<String, String> labelMap) {
		if (label.length() < 2) {
			labelMap.put(label, label);
		} else {
			label = label.replaceAll("\"", "");
			String separator = "$";
			int sepPos = label.indexOf(separator);
			if (label.startsWith(separator)) {
				String labelStr = label.substring(sepPos + separator.length());
				Pattern pattern = Pattern.compile(".*?\\{(.*)\\}.*");
				Matcher matcher = pattern.matcher(labelStr);
				if (matcher.find() == true) {
					Cookie locale = getCookie(request, "locale");
					try {
						String langText = resourceBundleDAO.findByKeyAndLanguageCode(matcher.group(1),
								locale.getValue(), "en_US", 0);
						labelMap.put(label, langText);
					} catch (Exception exec) {
						exec.printStackTrace();
					}

				}
			} else {
				labelMap.put(label, label);
			}
		}
	}

	public static FormIO convertToFormIoEntity(FormIOVO formIoVo) {
		FormIO formIO = new FormIO();
		formIO.setFormIoId(formIoVo.getFormIoId());
		formIO.setCreatedBy(formIoVo.getCreatedBy());
		formIO.setCreatedDate(formIoVo.getCreatedDate());
		formIO.setFormDescription(formIoVo.getFormDescription());
		formIO.setFormIoChecksum(formIoVo.getFormIoChecksum());
		formIO.setFormIoJson(formIoVo.getFormIoJson());
		formIO.setFormIoType(formIoVo.getFormIoType());
		formIO.setFormName(formIoVo.getFormName());
		formIO.setIsCustomUpdated(formIoVo.getIsCustomUpdated());
		formIO.setLastUpdatedBy(formIoVo.getLastUpdatedBy());
		formIO.setMultiSubmit(formIoVo.getMultiSubmit());
		formIO.setRouteName(formIoVo.getRouteName());
		formIO.setPersistenceType(formIoVo.getPersistenceType());
		// formIO.setLastUpdatedTs(formIoVo.getLastUpdatedTs());
		return formIO;
	}
	
	
	public static FormIOVO convertToFormIoVO(FormIO formIo) {
		FormIOVO formIOVo = new FormIOVO();
		formIOVo.setFormIoId(formIo.getFormIoId());
		formIOVo.setCreatedBy(formIo.getCreatedBy());
		formIOVo.setCreatedDate(formIo.getCreatedDate());
		formIOVo.setFormDescription(formIo.getFormDescription());
		formIOVo.setFormIoChecksum(formIo.getFormIoChecksum());
		formIOVo.setFormIoJson(formIo.getFormIoJson());
		formIOVo.setFormIoType(formIo.getFormIoType());
		formIOVo.setFormName(formIo.getFormName());
		formIOVo.setIsCustomUpdated(formIo.getIsCustomUpdated());
		formIOVo.setLastUpdatedBy(formIo.getLastUpdatedBy());
		formIOVo.setMultiSubmit(formIo.getMultiSubmit());
		formIOVo.setRouteName(formIo.getRouteName());
		formIOVo.setPersistenceType(formIo.getPersistenceType());
		formIOVo.setLastUpdatedTs(formIo.getLastUpdatedTs());
		return formIOVo;
	}

	public static void checkJsonNodeExist(JsonNode root, String key, Map<String, Object> additionalParams) {
		if (root.isObject()) {
			Iterator<String> fieldNames = root.fieldNames();
			while (fieldNames.hasNext()) {
				String fieldName = fieldNames.next().replaceAll("\"", "");
				JsonNode fieldValue = root.get(fieldName);
				if (fieldName.equalsIgnoreCase(key)) {
					additionalParams.put("isExist", Boolean.TRUE);
				} else {
					checkJsonNodeExist(fieldValue, key, additionalParams);
				}
			}
		} else if (root.isArray()) {
			ArrayNode arrayNode = (ArrayNode) root;
			for (int i = 0; i < arrayNode.size(); i++) {
				JsonNode arrayElement = arrayNode.get(i);
				checkJsonNodeExist(arrayElement, key, additionalParams);
			}
		}

	}

	public void updateJsonNode(JsonNode node, String replaceType) {
		try {
			if (node == null)
				return;

			if (node.isObject()) {
				ObjectNode objNode = (ObjectNode) node;

				if (objNode.has("type")) {
					String type = objNode.get("type").asText();

					String contextPath = request.getContextPath();

					if ("filebincomponent".equalsIgnoreCase(type) && objNode.has("fileBinType")) {
						String fileBinType = objNode.get("fileBinType").asText();
						List<FormIOLogicAction> logic = injectLogic(fileBinType, Constants.FILE_BIN_JS_CONTENT,
								"yourFileBinId", fileBinType, contextPath);
						objNode.putPOJO("logic", logic);

					} else if ("gridutilscomponent".equalsIgnoreCase(type) && objNode.has("gridUtilsType")) {
						String gridUtilsType = objNode.get("gridUtilsType").asText();
						if (objNode.has("content")) {
							String updatedContent = objNode.get("content").asText().replace("yourGridId",
									gridUtilsType);
							ObjectMapper mapper = new ObjectMapper();
							JsonNode contentNode = mapper.readTree(updatedContent);
							objNode.set("content", contentNode);
						}

						List<FormIOLogicAction> logic = injectLogic(gridUtilsType, Constants.GRID_UTILS_JS_CONTENT,
								"yourGridId", gridUtilsType, contextPath);
						objNode.putPOJO("logic", logic);

					} else if ("typeautocompletecomponent".equalsIgnoreCase(type) && objNode.has("typeautotype")) {
						String autoCompId = objNode.get("typeautotype").asText();
						if (objNode.has("content")) {
						        String content = objNode.get("content").asText();
						        String updatedContent = content.replaceAll("autocompleteId", autoCompId);
						        objNode.put("content", updatedContent);							

						}

						List<FormIOLogicAction> logic = injectLogic(autoCompId,
								Constants.TYPEAHEADAUTOCOMPLETE_UTILS_JS_CONTENT, "autoCompId", autoCompId,
								contextPath);
					}
				}

				// Recursively check all object fields
				Iterator<Map.Entry<String, JsonNode>> fields = objNode.fields();
				while (fields.hasNext()) {
					Map.Entry<String, JsonNode> entry = fields.next();
					updateJsonNode(entry.getValue(), replaceType);
				}

			} else if (node.isArray()) {
				for (JsonNode arrayElement : node) {
					updateJsonNode(arrayElement, replaceType);
				}
			}

		} catch (JsonProcessingException ex) {
			logger.error("Error while updating JSON node", ex);
			throw new RuntimeException("Error processing JSON: " + ex.getMessage(), ex);
		}
	}
	
	public List<FormIOLogicAction> injectLogic(String name, String jsTemplate, String placeholderKey, String idValue, String contextPath) {
	    String script = jsTemplate
	        .replace(placeholderKey, idValue)
	        .replace("yourContextPath", contextPath);

	    Trigger trigger = new Trigger();
	    trigger.setType("javascript");
	    trigger.setJavascript(script);

	    FormIOLogicAction logicAction = new FormIOLogicAction();
	    logicAction.setName(name);
	    logicAction.setTrigger(trigger);
	    
		if (logicAction.getActions() == null) {
			logicAction.setActions(new String[] {});
		}

	    List<FormIOLogicAction> actions = new ArrayList<>();
	    actions.add(logicAction);
	    return actions;
	}

	public static void updateKeyJsonNode(JsonNode node) {
		if (node.isObject()) {
			node.fields().forEachRemaining(entry -> {
				String val = entry.getValue().toString();
				if (val != null && val.isBlank() == false) {
					String key = entry.getKey().toString().replaceAll("\"", "");
					String result = entry.getValue().toString().replaceAll("\"", "");
					if (key != null && key.equalsIgnoreCase("type") && result != null
							&& (result.equalsIgnoreCase("filebincomponent")
									|| result.equalsIgnoreCase("gridutilscomponent")
									|| result.equalsIgnoreCase("typeautocompletecomponent"))) {
						if(result.equalsIgnoreCase("typeautocompletecomponent")) {
							((ObjectNode) node).put(key, "typeautocompletecomponent");
						}else {
						((ObjectNode) node).put(key, "htmlelement");}
					}
				}
				updateKeyJsonNode(entry.getValue());
			});
		} else if (node.isArray()) {
			node.elements().forEachRemaining(element -> updateKeyJsonNode(element));
		} else {
			// logger.info("Value: " + node);
		}
	}

	public String getCastExpressionForSave(String columnName, String dataType, String dbProductName) {
		if (columnName == null || dataType == null || dbProductName == null) {
			return columnName; // fail-safe default
		}

		dataType = dataType.toLowerCase();

		String nullSafeParam = String.format("NULLIF(%s, null)", columnName);

		String pgNullIntCast = String.format("COALESCE(NULLIF(%s::text, '')::int, NULL)", columnName);
		String pgNullDecimalCast = String.format("COALESCE(NULLIF(%s::text, '')::numeric, NULL)", columnName);
		String pgNullBoolCast = String.format("COALESCE(NULLIF(%s::text, '')::boolean, NULL)", columnName);
		String 	pgNullJsonCast 		= String.format("COALESCE(NULLIF(%s::text, '')::json, NULL)", columnName);
		String 	pgNullXmlCast 		= String.format("COALESCE(NULLIF(%s::text, '')::xml, NULL)", columnName);
		String 	pgNullUuidCast 		= String.format("COALESCE(NULLIF(%s::text, '')::uuid, NULL)", columnName);

		boolean isPostgres = dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.POSTGRESQL);
		boolean isMSSQL = dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.MSSQLSERVER);
		boolean isMySQL = dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.DEFAULT)
				|| dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.MARIADB);
		boolean isOracle = dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.ORACLE);

		switch (dataType) {
		case "money":
			return isPostgres ? String.format("CAST(%s AS money)", columnName) : columnName;
		case "boolean":
			if (isPostgres) {
				return pgNullBoolCast;
			} else if (isMySQL) {
				//return String.format("CAST(COALESCE(NULLIF(%s, ''), '0') AS UNSIGNED)", columnName);
				return String.format("COALESCE(IF(TRIM(%s) = '', 0, CAST(%s AS UNSIGNED)), 0)", columnName, columnName);
			} else if (isMSSQL) {
				return String.format("CAST(%s AS bit)", nullSafeParam);
			} else if (isOracle) {
				return String.format("CASE WHEN %s = 'true' THEN 1 ELSE 0 END", columnName);
			}

		case "json":
			if (isPostgres) {
				return pgNullJsonCast;
			}
		case "xml":
			if (isPostgres) {
			return pgNullXmlCast;
			}
		case "time":
			if (isPostgres) {
				return String.format("NULLIF(%s::text, '')::time", columnName);
			} else if (isMySQL) {
				// MariaDB: handle empty or null values
				return String.format("STR_TO_DATE(%s, '%%H:%%i:%%s')", nullSafeParam);
			}
			break;
		case "datetime":
		case "date":
			if ("hidden".equalsIgnoreCase(dataType)) {
				if (isMSSQL) {
					return "GETDATE()";
				} else if (isOracle) {
					return "SYSDATE";
				} else {
					return "NOW()";
				}
			} else {
				if (isPostgres) {
					return String.format("TO_DATE(NULLIF(%s::text, ''), 'YYYY-MM-DD')", columnName);
				} else if (isMSSQL) {
					return String.format("CONVERT(datetime, %s)", nullSafeParam);
				} else if (isMySQL) {
					return String.format("STR_TO_DATE(%s, '%%Y-%%m-%%dT%%H:%%i:%%s')", nullSafeParam);
				} else if (isOracle) {
					columnName = columnName.replaceFirst("^:", ""); // remove leading colon if present
					return String.format("TO_TIMESTAMP(:%s, 'YYYY-MM-DD\"T\"HH24:MI:SS')", columnName);
				}
			}
			break;
		case "int":
			if (isPostgres) {
				return pgNullIntCast;
			} else if (isMySQL || isOracle) {
				return String.format("COALESCE(NULLIF(TRIM(%s), ''), NULL)", columnName);
			} else if (isMSSQL) {
				return String.format("TRY_CAST(NULLIF(%s, '') AS INT)", columnName);
			}
			break;
		case "decimal":
			if (isPostgres) {
				return pgNullDecimalCast;
			} else if (isMSSQL) {
				return String.format("TRY_CAST(NULLIF(%s, '') AS DECIMAL(18,4))", columnName);
			} else if (isMySQL) {
				return String.format("COALESCE(NULLIF(TRIM(%s), ''), NULL)", columnName);
			}
			break;
		case "uuid":
			if (isPostgres) {
				return pgNullUuidCast;
			} else if (isMSSQL) {
				return String.format("TRY_CAST(NULLIF(TRIM(%s), '') AS uuid)", columnName);
			}
			break;
		case Constant.UNIQUEID:
			if (isMSSQL || isPostgres) {
				return String.format("TRY_CAST(NULLIF(TRIM(%s), '') AS UNIQUEIDENTIFIER)", columnName);
			}
			break;
		case "tinyint":
			if (isMySQL || isMSSQL) {
				return String.format("COALESCE(NULLIF(TRIM(%s), ''), NULL)", columnName);
			}
			break;

		default:
			return columnName;
		}
		return columnName;

	}
	
	public static String getCastExpressionForSelect(String columnName, String dataType, String dbProductName) {
		String columnAlias = columnName + " AS " + columnName;
		dataType = dataType.toLowerCase();

		boolean isPostgres = dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.POSTGRESQL);
		boolean isMSSQL = dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.MSSQLSERVER);
		boolean isMySQL = dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.MYSQL)
				|| dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.DEFAULT)
				|| dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.MARIADB);
		boolean isOracle = dbProductName.contains(com.trigyn.jws.dynamicform.utils.Constant.ORACLE);

		switch (dataType) {
		case "boolean":
			if (isPostgres) {
				return String.format("(CASE WHEN %s IS TRUE THEN 1 ELSE 0 END) AS %s", columnName, columnName);
			} else if (isMySQL || isMSSQL)
				return String.format("(%s + 0) AS %s", columnName, columnName);
			break;
		case "time":
			if (isPostgres) {
				return String.format("TO_CHAR(%s::time, 'HH24:MI:SS') AS %s", columnName, columnName);
			} else if (isMySQL) {
				return String.format("DATE_FORMAT(%s, '%%H:%%i:%%s') AS %s", columnName, columnName);
			} else if (isMSSQL) {
				return "CONVERT(varchar(5), " + columnName + ", 108) AS " + columnName;
			}
			break;

		case "date":
		case "datetime":
		case "smalldatetime":
		case "datetimeoffset":
		case "datetime2":
			if (isPostgres) {
				return String.format("TO_CHAR(%s, 'YYYY-MM-DD HH24:MI:SS') AS %s", columnName, columnName);
			} else if (isMySQL) {
				return String.format("DATE_FORMAT(%s, '%%Y-%%m-%%d %%H:%%i:%%s') AS %s", columnName, columnName);
			} else if (isMSSQL) {
				return "FORMAT(" + columnName + ", 'dd-MMMM-yyyy') AS " + columnName;
			}
			break;
		default:
			return columnAlias;
		}

		return columnAlias;
	}

}
