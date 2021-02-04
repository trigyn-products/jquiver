package com.trigyn.jws.typeahead.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.typeahead.model.AutocompleteParams;

@Repository
public class TypeAheadDAO extends DBConnection {

	private final static Logger logger = LogManager.getLogger(TypeAheadDAO.class);

	@Autowired
	public TypeAheadDAO(DataSource dataSource) {
		super(dataSource);
	}

	private static final String AUTOCOMPLETE_QUERY_SELECTOR = "SELECT ac_select_query FROM autocomplete_details WHERE ac_id = :ac_id";

	public List<Map<String, Object>> getAutocompleteData(AutocompleteParams autocompleteParams) {
		NativeQuery sqlQuery = getCurrentSession().createNativeQuery(AUTOCOMPLETE_QUERY_SELECTOR);
		sqlQuery.setParameter("ac_id", autocompleteParams.getAutocompleteId());
		String						list		= (String) sqlQuery.uniqueResult();
		List<Map<String, Object>>	displayList	= getAutocompleteDetails(list, autocompleteParams);
		return displayList;
	}

	private List<Map<String, Object>> getAutocompleteDetails(String a_autocompleteQuery,
			AutocompleteParams a_autocompleteParams) {

		boolean	is_LimitPresent	= true;
		Integer	startIndex		= a_autocompleteParams.getStartIndex();
		if (startIndex.intValue() == -1) {
			is_LimitPresent = false;
		}

		if (is_LimitPresent) {
			a_autocompleteQuery = a_autocompleteQuery + " LIMIT :startIndex , :pageSize ";
		}

		Map<String, Object>	namedParameters		= new HashMap<String, Object>();

		Set<String>			additionalParameter	= a_autocompleteParams.getCriteriaParams().keySet();
		for (Object object : additionalParameter) {
			namedParameters.put(object.toString(), a_autocompleteParams.getCriteriaParams().get(object.toString()));
		}
		namedParameters.put("searchText", escapeSql(a_autocompleteParams.getSearchText()));
		namedParameters.put("startIndex", a_autocompleteParams.getStartIndex());
		namedParameters.put("pageSize", a_autocompleteParams.getPageSize());
		List<Map<String, Object>> displayList = namedParameterJdbcTemplate.queryForList(a_autocompleteQuery,
				namedParameters);

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

	public List<String> getAllTablesListInSchema() {
		String			query		= "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = :schemaName";
		List<String>	resultSet	= new ArrayList<>();
		try (Connection connection = dataSource.getConnection();) {
			String				schemaName		= connection.getCatalog();
			Map<String, Object>	parameterMap	= new HashMap<>();
			parameterMap.put("schemaName", schemaName);
			resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap, String.class);
		} catch (SQLException a_exc) {
			logger.error("Error while fetching data from DB ", a_exc);
		}
		return resultSet;
	}

	public List<Map<String, Object>> getColumnNamesByTableName(String tableName) {
		StringBuilder				query		= new StringBuilder(
				"SELECT GROUP_CONCAT(CONCAT(COLUMN_NAME, ' AS ', camel_case(COLUMN_NAME)) SEPARATOR ', ') AS columnName")
						.append(" FROM information_schema.COLUMNS ")
						.append(" WHERE TABLE_NAME = :tableName AND TABLE_SCHEMA = :schemaName ")
						.append(" ORDER BY ORDINAL_POSITION ASC ");
		List<Map<String, Object>>	resultSet	= new ArrayList<>();
		try (Connection connection = dataSource.getConnection();) {
			String				schemaName		= connection.getCatalog();
			Map<String, Object>	parameterMap	= new HashMap<>();
			parameterMap.put("tableName", tableName);
			parameterMap.put("schemaName", schemaName);
			resultSet = namedParameterJdbcTemplate.queryForList(query.toString(), parameterMap);
		} catch (SQLException a_exc) {
			logger.error("Error while fetching data from DB ", a_exc);
		}
		return resultSet;
	}

}