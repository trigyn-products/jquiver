package com.trigyn.jws.webstarter.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.entities.DatasourceLookUp;
import com.trigyn.jws.dbutils.entities.DatasourceLookUpRepository;
import com.trigyn.jws.dbutils.service.DataSourceFactory;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.DataSourceVO;
import com.trigyn.jws.dbutils.vo.DatasourceLookUpVO;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;


public class DataSourceService {

	private final static Logger				logger						= LogManager.getLogger(DataSourceService.class);

	@Autowired
	private DatasourceLookUpRepository		datasourceLookUpRepo		= null;

	@Autowired
	private AdditionalDatasourceRepository	additionalDatasourceRepo	= null;
	
	public Map<String, Object> getAvailableDBDrivers(HttpServletRequest a_httpServletRequest, Map<String, FileInfo> files,
			Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
		logger.debug("Inside DataSourceService.getAvailableDBDrivers()");

		Map<String, Object>		availableDriverMap		= new HashMap<>();
		List<DatasourceLookUp>	datasourceLookUpList	= datasourceLookUpRepo
				.findAll(Sort.by("databaseDisplayProductName"));

		for (DatasourceLookUp datasourceLookUp : datasourceLookUpList) {
			DatasourceLookUpVO dsLookUPVO = new DatasourceLookUpVO();
			dsLookUPVO.setDatasourceName(datasourceLookUp.getDatabaseProductName());
			dsLookUPVO.setDriverClassName(datasourceLookUp.getDriverClassName());
			dsLookUPVO.setDatabaseDisplayProductName(datasourceLookUp.getDatabaseDisplayProductName());
			dsLookUPVO.setConnectionUrlPattern(datasourceLookUp.getConnectionUrlPattern());
			try {
				Class.forName(datasourceLookUp.getDriverClassName());
				dsLookUPVO.setDriverClassAvailable(true);
			} catch (ClassNotFoundException exception) {
				logger.error("No driver class found for: ", datasourceLookUp.getDriverClassName(), exception);
			}
			availableDriverMap.put(datasourceLookUp.getDatasourceLookupId(), dsLookUPVO);
		}
		List<Map.Entry<String, Object>> list = new LinkedList<Map.Entry<String, Object>>(
				availableDriverMap.entrySet());

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
	public ResponseEntity<String> saveDatasourceDetails(HttpServletRequest a_httpServletRequest, Map<String, FileInfo> files,
			Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
		logger.debug("Inside DataSourceService.saveDatasourceDetails()");

		Boolean							driverPresent			= Boolean.FALSE;
		Date							date					= new Date();
		Gson							gson					= new Gson();
		Map<String, String>				dbConfigMap				= new HashMap<>();
		String							additionalDatasourceId	= a_httpServletRequest.getParameter("adi");
		String							dataSourceName			= a_httpServletRequest.getParameter("dn");
		String							datasourceLookupId		= a_httpServletRequest.getParameter("dli");
		String							connectionUrl			= a_httpServletRequest.getParameter("curl");
		String							userName				= a_httpServletRequest.getParameter("un");
		String							password				= a_httpServletRequest.getParameter("pwd");

		Optional<AdditionalDatasource>	additionalDatasourceOp	= additionalDatasourceRepo
				.findById(additionalDatasourceId);
		AdditionalDatasource			additionalDatasource	= new AdditionalDatasource();
		additionalDatasource.setIsCustomUpdated(1);

		if (additionalDatasourceOp.isEmpty() == false) {
			additionalDatasource = additionalDatasourceOp.get();
			if (datasourceLookupId.equals(additionalDatasource.getDatasourceLookupId()) == false) {
				return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Datasource can not be changed");
			}
			additionalDatasource.setLastUpdatedBy(userDetails.getUserName());
		} else {
			additionalDatasource.setCreatedBy(userDetails.getUserName());
			additionalDatasource.setCreatedDate(date);
		}

		if (StringUtils.isBlank(connectionUrl) == false && StringUtils.isBlank(userName) == false
				&& StringUtils.isBlank(password) == false) {
			dbConfigMap.put("url", connectionUrl);
			dbConfigMap.put("userName", userName);
			dbConfigMap.put("password", password);
			if (gson.toJson(dbConfigMap).equals(additionalDatasource.getDatasourceConfiguration())
					&& dataSourceName.equals(additionalDatasource.getDatasourceName())) {
				return ResponseEntity.status(HttpStatus.OK).body("Success");
			}
			additionalDatasource.setDatasourceConfiguration(gson.toJson(dbConfigMap));
		} else {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("All fields are mandatory");
		}

		additionalDatasource.setDatasourceName(dataSourceName);
		additionalDatasource.setLastUpdatedTs(date);
		additionalDatasource.setIsDeleted(Constant.RecordStatus.INSERTED.getStatus());

		Double					dbsv					= 0d;
		List<DatasourceLookUp>	datasourceLookUpList	= datasourceLookUpRepo.findAll();
		for (DatasourceLookUp datasourceLookUp : datasourceLookUpList) {
			if (datasourceLookUp.getDatasourceLookupId().equals(datasourceLookupId) == true) {
				try {
					Class.forName(datasourceLookUp.getDriverClassName());
					driverPresent = Boolean.TRUE;
					additionalDatasource.setDatasourceLookupId(datasourceLookupId);
					dbsv = datasourceLookUp.getDatasourceSupportedVersion();
				} catch (ClassNotFoundException exception) {
					logger.error("No driver class found for: ", datasourceLookUp.getDriverClassName(), exception);
				}
			}
		}

		if (Boolean.FALSE.equals(driverPresent)) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("No driver class found");
		} else {
			Connection con;
			try {
				con = DriverManager.getConnection(connectionUrl, userName, password);

				ResponseEntity<String> res = validateConnectionVersion(con, dbsv);
				if (res.getStatusCodeValue() != HttpStatus.OK.value()) {
					return res;
				}
				con.isClosed();
				additionalDatasourceRepo.save(additionalDatasource);
				getOrInitDataSource(additionalDatasource, dbConfigMap);
				return ResponseEntity.status(HttpStatus.OK).body("Success");
			} catch (SQLException exception) {
				logger.error("Could not connect ", exception);
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not connect to the database");
	}

	public ResponseEntity<String> testDatabaseConnection(HttpServletRequest a_httpServletRequest, Map<String, FileInfo> files,
			Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
		logger.debug("Inside DataSourceService.testDatabaseConnection()");

		String				datasourceLookupId	= a_httpServletRequest.getParameter("dli");
		String				connectionUrl		= a_httpServletRequest.getParameter("curl");
		String				userName			= a_httpServletRequest.getParameter("un");
		String				password			= a_httpServletRequest.getParameter("pwd");
		DatasourceLookUp	datasourceLookUp	= datasourceLookUpRepo.getOne(datasourceLookupId);

		if (StringUtils.isBlank(connectionUrl) == true || StringUtils.isBlank(userName) == true
				|| StringUtils.isBlank(password) == true) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
					.body("Connection URL, Username and Password cannot be blank");
		}
		try {
			Class.forName(datasourceLookUp.getDriverClassName());
		} catch (ClassNotFoundException exception) {
			logger.error("No driver class found for: ", exception);
			ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("No driver class found");
		}
		try {
			Connection con = DriverManager.getConnection(connectionUrl, userName, password);
			con.isClosed();
			return validateConnectionVersion(con, datasourceLookUp.getDatasourceSupportedVersion());
		} catch (SQLException exception) {
			logger.error("Could not connect ", exception);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not connect to the database");
	}

	private ResponseEntity<String> validateConnectionVersion(Connection con, Double dbsv) throws SQLException {
		String	majorVersion	= String.valueOf(con.getMetaData().getDatabaseMajorVersion());
		String	minorVersion	= String.valueOf(con.getMetaData().getDatabaseMinorVersion());

		String	jdbcVersion		= majorVersion + "." + minorVersion;

		if (Double.valueOf(jdbcVersion) < dbsv) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
					.body("Database version should be higher than " + dbsv);
		}
		return ResponseEntity.status(HttpStatus.OK).body("Success");
	}

	private void getOrInitDataSource(AdditionalDatasource a_additionalDatasource, Map<String, String> dbConfigMap) {
		DataSourceVO	dataSourceVO	= new DataSourceVO();
		Gson			gson			= new Gson();
		String			dbConfig		= gson.toJson(dbConfigMap);
		if (a_additionalDatasource.getDatasourceConfiguration().equals(dbConfig) == false) {
			String driverClassName = datasourceLookUpRepo
					.getDriverClassNameById(a_additionalDatasource.getDatasourceLookupId());

			dataSourceVO.setAdditionalDataSourceId(a_additionalDatasource.getAdditionalDatasourceId());
			dataSourceVO.setDriverClassName(driverClassName);
			dataSourceVO.setDataSourceConfiguration(dbConfig);

			DataSourceFactory.getOrInitDataSource(dataSourceVO, true);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.datasourceLookUpRepo		= applicationContext.getBean(DatasourceLookUpRepository.class);
		this.additionalDatasourceRepo	= applicationContext.getBean(AdditionalDatasourceRepository.class);
	}
}
