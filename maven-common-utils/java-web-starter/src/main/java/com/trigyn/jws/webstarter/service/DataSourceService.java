package com.trigyn.jws.webstarter.service;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.entities.DatasourceLookUp;
import com.trigyn.jws.dbutils.entities.DatasourceLookUpRepository;
import com.trigyn.jws.dbutils.entities.DatasourcePropertyValidationResult;
import com.trigyn.jws.dbutils.service.DataSourceFactory;
import com.trigyn.jws.dbutils.service.DatasourcePropertyValidator;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.ConnectionPropertyVO;
import com.trigyn.jws.dbutils.vo.DataSourceVO;
import com.trigyn.jws.dbutils.vo.DatasourceLookUpVO;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

import jakarta.servlet.http.HttpServletRequest;

public class DataSourceService {

	private final static Logger logger = LoggerFactory.getLogger(DataSourceService.class);

	@Autowired
	private DatasourceLookUpRepository datasourceLookUpRepo = null;

	@Autowired
	private AdditionalDatasourceRepository additionalDatasourceRepo = null;

	private static final Set<String> RESERVED_PROPERTIES = Set.of("url", "userName", "password", "driverClassName");

	private static final Pattern PROPERTY_KEY_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9._$-]*$");

	private static final int MAX_PROPERTY_KEY_LENGTH = 100;

	private static final int MAX_PROPERTY_VALUE_LENGTH = 1000;

	private static final Gson GSON = new Gson();

	public Map<String, Object> getAvailableDBDrivers(HttpServletRequest a_httpServletRequest,
			Map<String, FileInfo> files, Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
		logger.debug("Inside DataSourceService.getAvailableDBDrivers()");

		Map<String, Object> availableDriverMap = new HashMap<>();
		List<DatasourceLookUp> datasourceLookUpList = datasourceLookUpRepo
				.findAll(Sort.by("databaseDisplayProductName"));

		for (DatasourceLookUp datasourceLookUp : datasourceLookUpList) {
			DatasourceLookUpVO dsLookUPVO = new DatasourceLookUpVO();
			dsLookUPVO.setDatasourceName(datasourceLookUp.getDatabaseProductName());
			dsLookUPVO.setDriverClassName(datasourceLookUp.getDriverClassName());
			dsLookUPVO.setDatabaseDisplayProductName(datasourceLookUp.getDatabaseDisplayProductName());
			dsLookUPVO.setConnectionUrlPattern(datasourceLookUp.getConnectionUrlPattern());
			dsLookUPVO.setConnectionProperties(datasourceLookUp.getConnectionProperties());
			try {
				Class.forName(datasourceLookUp.getDriverClassName());
				dsLookUPVO.setDriverClassAvailable(true);
				availableDriverMap.put(datasourceLookUp.getDatasourceLookupId(), dsLookUPVO);
			} catch (ClassNotFoundException exception) {
//				logger.info("Skipping datasource '{}' because JDBC driver '{}' is not available.",
//						datasourceLookUp.getDatabaseDisplayProductName(), datasourceLookUp.getDriverClassName());
			}

		}
		List<Map.Entry<String, Object>> list = new LinkedList<Map.Entry<String, Object>>(availableDriverMap.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
			public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
				return ((DatasourceLookUpVO) o1.getValue()).getDatabaseDisplayProductName().toLowerCase()
						.compareTo(((DatasourceLookUpVO) o2.getValue()).getDatabaseDisplayProductName().toLowerCase());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Object> temp = new LinkedHashMap<String, Object>();
		for (Map.Entry<String, Object> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	@ResponseBody
	public ResponseEntity<Map<String, String>> saveDatasourceDetails(HttpServletRequest request,
			Map<String, FileInfo> files, Map<String, Object> daoParameters, UserDetailsVO userDetails) {

		logger.debug("Inside DataSourceService.saveDatasourceDetails()");

		Date currentDate = new Date();
		// Gson gson = new Gson();

		String additionalDatasourceId = request.getParameter("adi");
		String datasourceName = request.getParameter("dn");
		String datasourceLookupId = request.getParameter("dli");
		String connectionUrl = request.getParameter("curl");
		String username = request.getParameter("un");
		String password = request.getParameter("pwd");
		String datasourceProperties = request.getParameter("dsp");
		boolean force = Boolean.parseBoolean(request.getParameter("f"));

		if (StringUtils.isAnyBlank(datasourceName, connectionUrl, username, password)) {
			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", "All fields are mandatory");
			// return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("All fields
			// are mandatory");
		}

		Map<String, String> propertyMap;
		try {
			propertyMap = parseDatasourceProperties(datasourceProperties);
			datasourceProperties = normalizeDatasourceProperties(propertyMap);
		} catch (IllegalArgumentException ex) {

			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", ex.getMessage());
			// return
			// ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(ex.getMessage());
		}

		AdditionalDatasource datasource;
		try {
			datasource = prepareDatasource(additionalDatasourceId, datasourceLookupId, datasourceName, userDetails,
					currentDate);
		} catch (IllegalArgumentException ex) {
			// return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response);
			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", ex.getMessage());
		}

		Map<String, String> dbConfig = buildDbConfiguration(connectionUrl, username, password);

		if (!hasDatasourceChanged(datasource, datasourceName, datasourceProperties, GSON.toJson(dbConfig))) {

			// return ResponseEntity.ok(response);
			return buildResponse(HttpStatus.OK, "SUCCESS", "Datasource saved successfully");
		}

		DatasourceLookUp lookup = datasourceLookUpRepo.findById(datasourceLookupId).orElse(null);

		if (lookup == null) {
			// return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response);
			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", "Invalid datasource type.");
		}

		BasicDataSource bds = null;

		try {

			bds = buildDatasource(lookup, connectionUrl, username, password);
			DatasourcePropertyValidationResult validationResult = validateDatasourceProperties(bds, lookup,
					datasourceProperties);

			if ("ERROR".equals(validationResult.getStatus())) {
				return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", validationResult.getMessage());
			}

			if ("WARNING".equals(validationResult.getStatus()) && !force) {
				return buildResponse(HttpStatus.OK, "WARNING", validationResult.getMessage());
			}

			logger.info("Start Connection.");
			try (Connection con = bds.getConnection()) {

				ResponseEntity<Map<String, String>> versionResponse = validateConnectionVersion(con,
						lookup.getDatasourceSupportedVersion());

				if (versionResponse.getStatusCode() != HttpStatus.OK) {
					return versionResponse;
				}
			}

			// Only after all validation succeeds
			logger.info("Saving datasource...");
			logger.info("Entity class = {}", datasource.getClass());
			datasource.setDatasourceConfiguration(GSON.toJson(dbConfig));
			datasource.setDatasourceProperties(datasourceProperties);
			datasource.setDatasourceName(datasourceName);
			datasource.setDatasourceLookupId(datasourceLookupId);
			datasource.setLastUpdatedTs(currentDate);
			datasource.setIsDeleted(Constant.RecordStatus.INSERTED.getStatus());
			additionalDatasourceRepo.save(datasource);

			if (datasource.getAdditionalDatasourceId() != null) {
				DataSourceFactory.clearDataSource(datasource.getAdditionalDatasourceId());
			}

			getOrInitDataSource(datasource, dbConfig, lookup.getConnectionProperties());

			// return ResponseEntity.ok(response);
			return buildResponse(HttpStatus.OK, "SUCCESS", "Datasource saved successfully");

		} catch (IllegalArgumentException ex) {

			// return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response);
			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", ex.getMessage());

		} catch (Throwable ex) {

			logger.error("Datasource save failed.", ex);

			return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", getUserFriendlyErrorMessage(ex));
		} finally {

			if (bds != null) {
				try {
					bds.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	public ResponseEntity<Map<String, String>> testDatabaseConnection(HttpServletRequest request,
			Map<String, FileInfo> files, Map<String, Object> daoParameters, UserDetailsVO userDetails) {

		logger.debug("Inside DataSourceService.testDatabaseConnection()");

		String datasourceLookupId = request.getParameter("dli");
		String connectionUrl = request.getParameter("curl");
		String userName = request.getParameter("un");
		String password = request.getParameter("pwd");
		String datasourceProperties = request.getParameter("dsp");
		boolean force = Boolean.parseBoolean(request.getParameter("f"));
		if (StringUtils.isBlank(connectionUrl) || StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {

			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR",
					"Connection URL, Username and Password cannot be blank");
		}

		DatasourceLookUp datasourceLookUp = datasourceLookUpRepo.findById(datasourceLookupId).orElse(null);

		if (datasourceLookUp == null) {
			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", "Invalid datasource type.");
		}

		// Validate & normalize datasource properties
		try {
			Map<String, String> propertyMap = parseDatasourceProperties(datasourceProperties);

			datasourceProperties = normalizeDatasourceProperties(propertyMap);

		} catch (IllegalArgumentException ex) {

			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", ex.getMessage());
		}

		BasicDataSource datasource = null;

		try {

			datasource = buildDatasource(datasourceLookUp, connectionUrl, userName, password);

			DatasourcePropertyValidationResult validationResult = validateDatasourceProperties(datasource,
					datasourceLookUp, datasourceProperties);

			if ("ERROR".equals(validationResult.getStatus())) {
				return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", validationResult.getMessage());
			}

			if ("WARNING".equals(validationResult.getStatus()) && !force) {
				return buildResponse(HttpStatus.OK, "WARNING", validationResult.getMessage());
			}

			try (Connection con = datasource.getConnection()) {

				return validateConnectionVersion(con, datasourceLookUp.getDatasourceSupportedVersion());
			}

		} catch (IllegalArgumentException ex) {

			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR", ex.getMessage());

		} catch (Throwable ex) {

			logger.error("Database connection failed.", ex);

			return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", getUserFriendlyErrorMessage(ex));

		} finally {

			if (datasource != null) {
				try {
					datasource.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	private ResponseEntity<Map<String, String>> validateConnectionVersion(Connection con, Double dbsv)
			throws SQLException {

		String jdbcVersion = con.getMetaData().getDatabaseMajorVersion() + "."
				+ con.getMetaData().getDatabaseMinorVersion();

		if (Double.parseDouble(jdbcVersion) < dbsv) {
			return buildResponse(HttpStatus.PRECONDITION_FAILED, "ERROR",
					"Database version should be higher than " + dbsv);
		}

		return buildResponse(HttpStatus.OK, "SUCCESS", "Connection successful");
	}

	private ResponseEntity<Map<String, String>> buildResponse(HttpStatus httpStatus, String status, String message) {

		Map<String, String> response = new HashMap<>();
		response.put("status", status);
		response.put("message", message);

		return ResponseEntity.status(httpStatus).body(response);
	}

	private void getOrInitDataSource(AdditionalDatasource additionalDatasource, Map<String, String> dbConfigMap,
			String availableConnectionPropertiesJson) {

		// Gson gson = new Gson();

		DataSourceVO dataSourceVO = new DataSourceVO();

		Type type = new TypeToken<List<ConnectionPropertyVO>>() {
		}.getType();

		List<ConnectionPropertyVO> connectionProperties = StringUtils.isBlank(availableConnectionPropertiesJson)
				? Collections.emptyList()
				: GSON.fromJson(availableConnectionPropertiesJson, type);

		dataSourceVO.setAdditionalDataSourceId(additionalDatasource.getAdditionalDatasourceId());

		dataSourceVO.setDriverClassName(
				datasourceLookUpRepo.getDriverClassNameById(additionalDatasource.getDatasourceLookupId()));

		dataSourceVO.setDataSourceConfiguration(GSON.toJson(dbConfigMap));

		dataSourceVO.setDatasourceProperties(additionalDatasource.getDatasourceProperties());

		dataSourceVO.setConnectionProperties(connectionProperties);

		DataSourceFactory.getOrInitDataSource(dataSourceVO, true);
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.datasourceLookUpRepo = applicationContext.getBean(DatasourceLookUpRepository.class);
		this.additionalDatasourceRepo = applicationContext.getBean(AdditionalDatasourceRepository.class);
	}

	private BasicDataSource buildDatasource(DatasourceLookUp datasourceLookUp, String connectionUrl, String userName,
			String password) {

		try {
			Class.forName(datasourceLookUp.getDriverClassName());
		} catch (ClassNotFoundException ex) {
			throw new IllegalArgumentException("No driver class found.", ex);
		}

		BasicDataSource datasource = new BasicDataSource();

		datasource.setDriverClassName(datasourceLookUp.getDriverClassName());
		validateConnectionUrl(connectionUrl, userName, password);
		datasource.setUrl(connectionUrl);
		datasource.setUsername(userName);
		datasource.setPassword(password);

		datasource.setTestOnBorrow(true);

		return datasource;
	}

	private Map<String, String> parseDatasourceProperties(String datasourceProperties) {

		if (StringUtils.isBlank(datasourceProperties)) {
			return new LinkedHashMap<>();
		}

		try {

			Type type = new TypeToken<Map<String, String>>() {
			}.getType();

			Map<String, String> map = GSON.fromJson(datasourceProperties, type);

			return map == null ? new LinkedHashMap<>() : map;

		} catch (Exception ex) {

			throw new IllegalArgumentException("Invalid datasource properties.");
		}
	}

	private String normalizeDatasourceProperties(Map<String, String> propertyMap) {

		LinkedHashMap<String, String> normalized = new LinkedHashMap<>();

		for (Map.Entry<String, String> entry : propertyMap.entrySet()) {

			String key = StringUtils.trimToEmpty(entry.getKey());
			String value = StringUtils.trimToEmpty(entry.getValue());

			if (StringUtils.isBlank(key)) {
				throw new IllegalArgumentException("Property name cannot be blank.");
			}

			if (StringUtils.isBlank(value)) {
				throw new IllegalArgumentException("Value cannot be blank for property '" + key + "'.");
			}
			String normalizedKey = key.toLowerCase(Locale.ROOT);
			if (RESERVED_PROPERTIES.contains(normalizedKey)) {
				throw new IllegalArgumentException("'" + key + "' is a reserved datasource property.");
			}

			if (!PROPERTY_KEY_PATTERN.matcher(key).matches()) {
				throw new IllegalArgumentException("Invalid property name '" + key + "'.");
			}

			if (key.length() > MAX_PROPERTY_KEY_LENGTH) {
				throw new IllegalArgumentException(
						"Property name '" + key + "' cannot exceed " + MAX_PROPERTY_KEY_LENGTH + " characters.");
			}

			if (value.length() > MAX_PROPERTY_VALUE_LENGTH) {
				throw new IllegalArgumentException(
						"Property value for '" + key + "' cannot exceed " + MAX_PROPERTY_VALUE_LENGTH + " characters.");
			}

			normalized.put(key, value);
		}

		return GSON.toJson(normalized);
	}

	private Map<String, String> buildDbConfiguration(String url, String username, String password) {

		Map<String, String> config = new LinkedHashMap<>();

		config.put("url", url);
		config.put("userName", username);
		config.put("password", password);

		return config;
	}

	private boolean hasDatasourceChanged(AdditionalDatasource datasource, String datasourceName,
			String datasourceProperties, String datasourceConfiguration) {

		boolean configChanged = !datasourceConfiguration.equals(datasource.getDatasourceConfiguration());

		boolean propertiesChanged = !StringUtils.defaultString(datasourceProperties)
				.equals(StringUtils.defaultString(datasource.getDatasourceProperties()));

		boolean nameChanged = !datasourceName.equals(datasource.getDatasourceName());

		return configChanged || propertiesChanged || nameChanged;
	}

	private AdditionalDatasource prepareDatasource(String datasourceId, String datasourceLookupId,
			String datasourceName, UserDetailsVO user, Date date) {

		Optional<AdditionalDatasource> optional = additionalDatasourceRepo.findById(datasourceId);

		AdditionalDatasource datasource;

		if (optional.isPresent()) {

			datasource = optional.get();

			if (!datasourceLookupId.equals(datasource.getDatasourceLookupId())) {

				throw new IllegalArgumentException("Datasource can not be changed");
			}

			datasource.setLastUpdatedBy(user.getUserName());

		} else {

			if (additionalDatasourceRepo.getDatasourceByName(datasourceName) > 0) {
				throw new IllegalArgumentException("Datasource name already exists.");
			}

			datasource = new AdditionalDatasource();
			datasource.setCreatedBy(user.getUserName());
			datasource.setCreatedDate(date);
		}

		datasource.setIsCustomUpdated(1);

		return datasource;
	}

	private void validateConnectionUrl(String url,String userName, String password) {

		if (StringUtils.isBlank(url)) {
			throw new IllegalArgumentException("Connection URL cannot be blank.");
		}

		validateUrlCredential(url, "user", userName);
	    validateUrlCredential(url, "username", userName);
	    validateUrlCredential(url, "password", password);
	}
	
	private void validateUrlCredential(String url, String key, String expectedValue) {

	    String regex = "(?i)(?:[?&;])" + key + "=([^;&]+)";
	    Matcher matcher = Pattern.compile(regex).matcher(url);

	    if (matcher.find()) {

	        String actualValue = matcher.group(1);

	        if (!StringUtils.equals(actualValue, expectedValue)) {
	            throw new IllegalArgumentException(
	                    "The " + key + " specified in the connection URL does not match the value entered.");
	        }
	    }
	}

	private String getUserFriendlyErrorMessage(Throwable ex) {

		Throwable root = ex;
		while (root.getCause() != null) {
			root = root.getCause();
		}

		// First check the exception type
		if (root instanceof java.net.UnknownHostException) {
			return "Database server not found. Please verify the hostname.";
		}

		if (root instanceof java.net.ConnectException) {
			return "Unable to connect to the database server. Please verify the host and port.";
		}

		if (root instanceof java.net.SocketTimeoutException) {
			return "Connection timed out while connecting to the database server.";
		}

		// Then check the exception message
		String message = root.getMessage();

		if (message == null) {
			return "Could not connect to the database.";
		}

		message = message.toLowerCase();

		// Authentication
		if (message.contains("access denied") || message.contains("authentication failed")
				|| message.contains("login failed") || message.contains("invalid authorization specification")
				|| message.contains("password authentication failed") || message.contains("ora-01017")) {

			return "Invalid username or password.";
		}

		// Database not found
		if (message.contains("unknown database") || message.contains("database does not exist")
				|| message.contains("ora-12514") || message.contains("database \"")) {

			return "Database not found. Please verify the database name.";
		}

		// Invalid URL
		if (message.contains("no suitable driver") || message.contains("invalid url") || message.contains("malformed")
				|| message.contains("invalid connection string")) {

			return "Invalid connection URL.";
		}

		// Network
		if (message.contains("connection refused") || message.contains("communications link failure")
				|| message.contains("could not connect") || message.contains("network")) {

			return "Unable to connect to the database server. Please verify the host, port and network connectivity.";
		}

		// SSL
		if (message.contains("ssl")) {
			return "SSL configuration is invalid.";
		}

		// Driver
		if (message.contains("driver")) {
			return "Database driver configuration is invalid.";
		}

		return "Could not connect to the database.";
	}

	private DatasourcePropertyValidationResult validateDatasourceProperties(BasicDataSource datasource,
			DatasourceLookUp datasourceLookUp, String datasourceProperties) {

		Type type = new TypeToken<List<ConnectionPropertyVO>>() {
		}.getType();

		List<ConnectionPropertyVO> supportedProperties = StringUtils.isBlank(datasourceLookUp.getConnectionProperties())
				? Collections.emptyList()
				: GSON.fromJson(datasourceLookUp.getConnectionProperties(), type);

		return DatasourcePropertyValidator.validate(datasource, datasourceProperties, supportedProperties);
	}
}
