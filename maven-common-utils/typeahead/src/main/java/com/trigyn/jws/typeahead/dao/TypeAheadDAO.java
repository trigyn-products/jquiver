package com.trigyn.jws.typeahead.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dbutils.service.DataSourceFactory;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.DBExtractor;
import com.trigyn.jws.dbutils.vo.DataSourceVO;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.typeahead.model.AutocompleteParams;
import com.trigyn.jws.typeahead.utility.Constant;

@Repository
public class TypeAheadDAO extends DBConnection {

	private final static Logger				logger							= LogManager.getLogger(TypeAheadDAO.class);

	@Autowired
	private DataSource						dataSource						= null;

	@Autowired
	private IUserDetailsService				detailsService					= null;

	@Autowired
	private TypeAheadRepository				typeAheadRepository				= null;

	@Autowired
	private AdditionalDatasourceRepository	additionalDatasourceRepository	= null;

	@Autowired
	private TemplatingUtils					templatingUtils					= null;

	@Autowired
	public TypeAheadDAO(DataSource dataSource) {
		super(dataSource);
	}

	private static final String AUTOCOMPLETE_QUERY_SELECTOR = "SELECT ac_select_query FROM jq_autocomplete_details WHERE ac_id = :ac_id";

	public List<Map<String, Object>> getAutocompleteData(AutocompleteParams autocompleteParams, Map<String, Object> requestParamMap)
			throws Exception {
		NativeQuery sqlQuery = getCurrentSession().createNativeQuery(AUTOCOMPLETE_QUERY_SELECTOR);
		sqlQuery.setParameter("ac_id", autocompleteParams.getAutocompleteId());
		String						list		= (String) sqlQuery.uniqueResult();
		String						query		= templatingUtils.processTemplateContents(list, "typeAheadQuery", requestParamMap);
		List<Map<String, Object>>	displayList	= getAutocompleteDetails(query, autocompleteParams);
		return displayList;
	}

	private List<Map<String, Object>> getAutocompleteDetails(String a_autocompleteQuery, AutocompleteParams a_autocompleteParams) {
		String						dataSourceId				= typeAheadRepository
				.getDataSourceId(a_autocompleteParams.getAutocompleteId());
		NamedParameterJdbcTemplate	namedParameterJdbcTemplate	= updateNamedParameterJdbcTemplateDataSource(dataSourceId);

		String dataSourceUserName = "";
		if (StringUtils.isBlank(dataSourceId) == false) {
			DataSourceVO dataSourceVO = additionalDatasourceRepository.getDataSourceConfiguration(dataSourceId);
			Gson g = new Gson(); 
			Map<String,String> map = g.fromJson(dataSourceVO.getDataSourceConfiguration(), Map.class);
			dataSourceUserName = map.get("userName");
		} 
		UserDetailsVO				detailsVO					= detailsService.getUserDetails();
		Map<String, Object>			namedParameters				= new HashMap<String, Object>();

		Set<String>					additionalParameter			= a_autocompleteParams.getCriteriaParams().keySet();
		for (Object object : additionalParameter) {
			namedParameters.put(object.toString(), a_autocompleteParams.getCriteriaParams().get(object.toString()));
		}
		namedParameters.put("searchText", escapeSql(a_autocompleteParams.getSearchText()));
		namedParameters.put("startIndex", a_autocompleteParams.getStartIndex());
		namedParameters.put("pageSize", a_autocompleteParams.getPageSize());
		namedParameters.put("loggedInUserName", detailsVO.getUserName());
		namedParameters.put("dataSourceUserName", dataSourceUserName);
		List<Map<String, Object>> displayList = namedParameterJdbcTemplate.queryForList(a_autocompleteQuery, namedParameters);

		return displayList;
	}

	public Integer getCountOfData(AutocompleteParams a_autocompleteParams) {
		String	a_autocompleteQuery	= "SELECT COUNT(*) FROM (" + getQueryForAutoComplete(a_autocompleteParams);
		boolean	is_LimitPresent		= true;
		Integer	startIndex			= a_autocompleteParams.getStartIndex();
		if (startIndex.intValue() == -1) {
			is_LimitPresent = false;
		}

		if (is_LimitPresent) {
			a_autocompleteQuery = a_autocompleteQuery + " LIMIT :startIndex , :pageSize ";
		}
		a_autocompleteQuery += ") as count";
		Map<String, Object>	namedParameters		= new HashMap<String, Object>();

		Set<String>			additionalParameter	= a_autocompleteParams.getCriteriaParams().keySet();
		for (Object object : additionalParameter) {
			namedParameters.put(object.toString(), a_autocompleteParams.getCriteriaParams().get(object.toString()));
		}
		namedParameters.put("searchText", a_autocompleteParams.getSearchText());
		namedParameters.put("startIndex", a_autocompleteParams.getStartIndex());
		namedParameters.put("pageSize", a_autocompleteParams.getPageSize());
		Integer count = namedParameterJdbcTemplate.queryForObject(a_autocompleteQuery, namedParameters, Integer.class);
		return count;
	}

	private String getQueryForAutoComplete(AutocompleteParams autocompleteParams) {
		NativeQuery sqlQuery = getCurrentSession().createNativeQuery(AUTOCOMPLETE_QUERY_SELECTOR);
		sqlQuery.setParameter("ac_id", autocompleteParams.getAutocompleteId());
		String list = (String) sqlQuery.uniqueResult();
		return list;
	}

	private static String escapeSql(String data) {
		data	= data.replace("\\", "\\\\\\\\");
		data	= data.replace("%", "\\%");
		data	= data.replace("_", "\\_");
		data	= data.replace("'", "\\'");
		return data;
	}

	//	public List<String> getAllTablesListInSchema() {
	//		String			query		= "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = :schemaName";
	//		List<String>	resultSet	= new ArrayList<>();
	//		try (Connection connection = dataSource.getConnection();) {
	//			String				schemaName		= connection.getCatalog();
	//			Map<String, Object>	parameterMap	= new HashMap<>();
	//			parameterMap.put("schemaName", schemaName);
	//			resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap, String.class);
	//		} catch (SQLException a_exc) {
	//			logger.error("Error while fetching data from DB ", a_exc);
	//		}
	//		return resultSet;
	//	}

	public List<Map<String, Object>> getColumnNamesByTableName(String additionalDataSourceId, String tableName) {

		DataSourceVO	dataSourceVO			= additionalDatasourceRepository.getDataSourceConfiguration(additionalDataSourceId);
		DataSource		additionalDataSource	= dataSource;
		if (dataSourceVO != null) {
			additionalDataSource = DataSourceFactory.getDataSource(dataSourceVO);
		}
		List<Map<String, Object>>	resultSet					= new ArrayList<>();

		try {
			resultSet =  DBExtractor.getCols(tableName, additionalDataSource);
		} catch (Throwable a_exc) {
			logger.error("Error while fetching data from DB ", a_exc);
		}
		return resultSet;
	}

}