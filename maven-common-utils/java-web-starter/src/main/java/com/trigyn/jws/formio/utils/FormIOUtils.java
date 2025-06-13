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
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.vo.FormIOLogicAction;
import com.trigyn.jws.formio.vo.FormIOVO;
import com.trigyn.jws.formio.vo.Trigger;
import com.trigyn.jws.resourcebundle.dao.ResourceBundleDAO;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class FormIOUtils {

	private final static Logger logger = LoggerFactory.getLogger(FormIOUtils.class);

	private DynamicFormDAO dynamicFormDAO = ApplicationContextProvider.getBean(DynamicFormDAO.class);

	private IResourceBundleRepository iResourceBundleRepository = ApplicationContextProvider
			.getBean(IResourceBundleRepository.class);
	
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
			// System.out.println("Value: " + node);
		}
	}

	public static void updateLabelJsonNode(JsonNode node, Map keyMap) {
		if (node.isObject()) {
			node.fields().forEachRemaining(entry -> {
				String val = entry.getValue().toString();
				if (entry.getValue() != null) {
					String result = entry.getValue().toString().replaceAll("\"", "");
					;
					if (result != null && keyMap.get(result) != null) {
						String keyMapKey = (String) keyMap.get(result);
						if (keyMapKey != null) {
							((ObjectNode) node).put("label", keyMapKey);
						}
					}

				}
				updateLabelJsonNode(entry.getValue(), keyMap);
			});
		} else if (node.isArray()) {
			node.elements().forEachRemaining(element -> updateLabelJsonNode(element, keyMap));
		} else {
			// System.out.println("Value: " + node);
		}
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
		// formIO.setCreatedDate(formIoVo.getCreatedDate());
		formIO.setFormDescription(formIoVo.getFormDescription());
		formIO.setFormIoChecksum(formIoVo.getFormIoChecksum());
		formIO.setFormIoJson(formIoVo.getFormIoJson());
		formIO.setFormIoType(formIoVo.getFormIoType());
		formIO.setFormName(formIoVo.getFormName());
		formIO.setIsCustomUpdated(formIoVo.getIsCustomUpdated());
		formIO.setLastUpdatedBy(formIoVo.getLastUpdatedBy());
		// formIO.setLastUpdatedTs(formIoVo.getLastUpdatedTs());
		return formIO;
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
			if (node != null && node.fields() != null && node.isObject()) {
				if (node.has("type")) {
					if (node.has("fileBinType")
							&& node.get("type").toString().replaceAll("\"", "").equalsIgnoreCase("filebincomponent")) {
						String fileBinType = node.get("fileBinType").toString().replaceAll("\"", "");
						String contextPath = request.getContextPath();
						String defaultJsContent = Constants.FILE_BIN_JS_CONTENT;
						List<FormIOLogicAction> fios = new ArrayList<FormIOLogicAction>();
						FormIOLogicAction fio = new FormIOLogicAction();
						fio.setName(fileBinType);
						Trigger trigger = new Trigger();
						trigger.setType("javascript");
						String replaceString = defaultJsContent.replaceAll("yourFileBinId", fileBinType);
						String contextReplaceString = replaceString.replaceAll("yourContextPath", contextPath);
						trigger.setJavascript(contextReplaceString);
						fio.setTrigger(trigger);
						fios.add(fio);
						((ObjectNode) node).putPOJO("logic", fios);
					}
					if (node.has("gridUtilsType") && node.get("type").toString().replaceAll("\"", "")
							.equalsIgnoreCase("gridutilscomponent")) {
						String gridUtilsType = node.get("gridUtilsType").toString().replaceAll("\"", "");

						String contentReplaceString = node.get("content").toString().replaceAll("yourGridId",
								gridUtilsType);
						ObjectMapper mapper = new ObjectMapper();

						JsonNode contentNode = mapper.readTree(contentReplaceString);
						((ObjectNode) node).put("content", contentNode);

						String defaultJsContent = Constants.GRID_UTILS_JS_CONTENT;
						List<FormIOLogicAction> fios = new ArrayList<FormIOLogicAction>();
						FormIOLogicAction fio = new FormIOLogicAction();
						fio.setName(gridUtilsType);
						Trigger trigger = new Trigger();
						trigger.setType("javascript");
						String gridReplaceString = defaultJsContent.replaceAll("yourGridId", gridUtilsType);
						String contextPath = request.getContextPath();
						String contextPathReplaceString = gridReplaceString.replaceAll("yourContextPath", contextPath);
						trigger.setJavascript(contextPathReplaceString);
						fio.setTrigger(trigger);
						fios.add(fio);
						((ObjectNode) node).putPOJO("logic", fios);
					}
					// typeautocompletecomponent
					if (node.has("typeautotype") && node.get("type").toString().replaceAll("\"", "")
							.equalsIgnoreCase("typeautocompletecomponent")) {
						String autocompleteId = node.get("typeautotype").toString().replaceAll("\"", "");

						String contentReplaceString = node.get("content").toString().replaceAll("autocompleteId",
								autocompleteId);
						ObjectMapper mapper = new ObjectMapper();

						JsonNode contentNode = mapper.readTree(contentReplaceString);
						((ObjectNode) node).put("content", contentNode);

						String defaultJsContent = Constants.TYPEAHEADAUTOCOMPLETE_UTILS_JS_CONTENT;
						List<FormIOLogicAction> fios = new ArrayList<FormIOLogicAction>();
						FormIOLogicAction fio = new FormIOLogicAction();
						fio.setName(autocompleteId);
						Trigger trigger = new Trigger();
						trigger.setType("javascript");
						String gridReplaceString = defaultJsContent.replaceAll("autoCompId", autocompleteId);
						String contextPath = request.getContextPath();
						String contextPathReplaceString = gridReplaceString.replaceAll("yourContextPath", contextPath);
						trigger.setJavascript(contextPathReplaceString);
						fio.setTrigger(trigger);
						fios.add(fio);
						((ObjectNode) node).putPOJO("logic", fios);
					}

				} else {
					node.fields().forEachRemaining(entry -> {
						updateJsonNode(entry.getValue(), replaceType);
					});
				}
			} else if (node.isArray()) {
				node.elements().forEachRemaining(element -> updateJsonNode(element, replaceType));
			} else {
				// System.out.println("Value: " + node);
			}
		} catch (JsonMappingException jme) {
			logger.error("Error occured while Update JsonNode)", jme);
			throw new RuntimeException(jme.getMessage());
		} catch (JsonProcessingException jpe) {
			logger.error("Error occured while Update JsonNode)", jpe);
			throw new RuntimeException(jpe.getMessage());
		}
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
						((ObjectNode) node).put(key, "htmlelement");
					}
				}
				updateKeyJsonNode(entry.getValue());
			});
		} else if (node.isArray()) {
			node.elements().forEachRemaining(element -> updateKeyJsonNode(element));
		} else {
			// System.out.println("Value: " + node);
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
				return String.format("CAST(NULLIF(%s, '') AS UNSIGNED)", columnName);
			} else if (isMSSQL) {
				return String.format("CAST(%s AS bit)", nullSafeParam);
			} else if (isOracle) {
				return String.format("CASE WHEN %s = 'true' THEN 1 ELSE 0 END", columnName);
			}

		case "json":
		case "jsonb":
			return isPostgres
					? String.format("CASE WHEN %s IS NULL OR %s = '' THEN NULL ELSE %s::json END", columnName,
							columnName, columnName)
					: columnName;
		case "xml":
			return isPostgres
					? String.format("CASE WHEN %s IS NULL OR %s = '' THEN NULL ELSE xmlparse(document %s) END",
							columnName, columnName, columnName)
					: columnName;
		case "time":
		case "timetz":
			if (isPostgres) {
				return String.format("NULLIF(%s::text, '')::time", columnName);
			} else if (isMySQL) {
				// MariaDB: handle empty or null values
				return String.format("STR_TO_DATE(%s, '%%H:%%i:%%s')", nullSafeParam);
			}
			break;
		case "date":
		case "datetime":
		case "timestamp":
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
		case "integer":
		case "smallint":
		case "tinyint":
		case "bigint":
			if (isPostgres) {
				return pgNullIntCast;
			} else if (isMySQL || isOracle) {
				return String.format("COALESCE(NULLIF(TRIM(%s), ''), NULL)", columnName);
			} else if (isMSSQL) {
				Map<String, String> mssqlNumericCastTypes = Map.of("tinyint", "TINYINT", "smallint", "SMALLINT", "int",
						"INT", "integer", "INT", "bigint", "BIGINT");
				String castType = mssqlNumericCastTypes.getOrDefault(dataType.toLowerCase(), "INT");
				return String.format("TRY_CAST(NULLIF(%s, '') AS %s)", columnName, castType);

			}
			break;
		case "decimal":
		case "numeric":
		case "float":
		case "double":
			if (isPostgres) {
				return pgNullDecimalCast;
			} else if (isMSSQL) {
				Map<String, String> mssqlFloatTypes = Map.of("decimal", "DECIMAL(18,4)", "numeric", "NUMERIC(18,4)",
						"float", "FLOAT", "real", "REAL", "double", "FLOAT");
				String castType = mssqlFloatTypes.getOrDefault(dataType, "DECIMAL");
				return String.format("TRY_CAST(NULLIF(%s, '') AS %s)", columnName, castType);
			}
			break;

		case "uniqueidentifier":
			if (isMSSQL) {
				return String.format("TRY_CAST(NULLIF(TRIM(%s), '') AS UNIQUEIDENTIFIER)", columnName);
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
