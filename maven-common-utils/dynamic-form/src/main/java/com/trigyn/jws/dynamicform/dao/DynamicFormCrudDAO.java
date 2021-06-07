package com.trigyn.jws.dynamicform.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dbutils.service.DataSourceFactory;
import com.trigyn.jws.dbutils.utils.DBExtractor;
import com.trigyn.jws.dbutils.vo.DataSourceVO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.utils.Constant;

@Repository
public class DynamicFormCrudDAO extends DBConnection {

	private final static Logger				logger							= LogManager
			.getLogger(DynamicFormCrudDAO.class);

	@Autowired
	private DataSource						dataSource						= null;

	@Autowired
	private AdditionalDatasourceRepository	additionalDatasourceRepository	= null;

	@Autowired
	public DynamicFormCrudDAO(DataSource dataSource) {
		super(dataSource);
	}

	public DynamicForm findDynamicFormById(String formId) {
		return hibernateTemplate.get(DynamicForm.class, formId);
	}

	public List<Map<String, Object>> getFormData(String dataSourceId, String selectionQuery) throws Exception {
		List<Map<String, Object>>	data			= null;
		JdbcTemplate				jdbcTemplate	= updateJdbcTemplateDataSource(dataSourceId);
		data = jdbcTemplate.queryForList(selectionQuery);
		return data;
	}

	public void saveFormData(String dataSourceId, String saveTemplateQuery, Map<String, Object> parameters) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = updateNamedParameterJdbcTemplateDataSource(
				dataSourceId);
		namedParameterJdbcTemplate.update(saveTemplateQuery, parameters);
	}

	public void saveDynamicFormData(DynamicForm dynamicForm) {
		getCurrentSession().saveOrUpdate(dynamicForm);
	}

	public List<DynamicFormSaveQuery> findDynamicFormQueriesById(String formId) {
		Query query = getCurrentSession().createQuery(
				"FROM DynamicFormSaveQuery AS dfs WHERE dfs.dynamicFormId = :formId ORDER BY dfs.sequence ASC ");
		query.setParameter("formId", formId);
		return query.list();
	}

	public void deleteFormQueries(String formId) {
		Query query = getCurrentSession()
				.createQuery("DELETE FROM DynamicFormSaveQuery AS dfs WHERE dfs.dynamicFormId = :formId");
		query.setParameter("formId", formId);
		query.executeUpdate();
	}

	public void deleteFormQueriesByIds(String formId) {
		StringBuilder	deleteFormQuery	= new StringBuilder(
				"DELETE FROM DynamicFormSaveQuery AS dfs WHERE dfs.dynamicFormId = :formId ");
		Query			query			= getCurrentSession().createQuery(deleteFormQuery.toString());
		query.setParameter("formId", formId);
		query.executeUpdate();
	}

	public String checkFormName(String formName) {
		Query query = getCurrentSession()
				.createQuery("SELECT formId FROM DynamicForm  WHERE lower(formName) = lower(:formName)");
		query.setParameter("formName", formName);
		String data = (String) query.uniqueResult();
		return data;
	}

	public List<DynamicForm> getAllDynamicForms(Integer formTypeId) {
		Query query = getCurrentSession().createQuery("FROM DynamicForm AS df WHERE df.formTypeId = :formTypeId");
		query.setParameter("formTypeId", formTypeId);
		return query.list();
	}

	public DynamicForm getFormDetailsByName(String formName) {
		Query query = getCurrentSession().createQuery(" FROM DynamicForm  WHERE lower(formName) = lower(:formName)");
		query.setParameter("formName", formName);
		DynamicForm data = (DynamicForm) query.uniqueResult();
		return data;
	}

	public List<Map<String, Object>> getTableDetailsByTableName(String additionalDataSourceId, String tableName) {
		DataSourceVO	dataSourceVO			= additionalDatasourceRepository.getDataSourceConfiguration(additionalDataSourceId);
		DataSource		additionalDataSource	= dataSource;
		if (dataSourceVO != null) {
			additionalDataSource = DataSourceFactory.getDataSource(dataSourceVO);
		}
		List<Map<String, Object>> resultSet = new ArrayList<>();
		resultSet = DBExtractor.getDBStructure(tableName, additionalDataSource);

		return resultSet;
	}

	// public List<String> getAllTablesListInSchema() {
	// String query = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE
	// TABLE_SCHEMA = :schemaName";
	// List<String> resultSet = new ArrayList<>();
	// try (Connection connection = dataSource.getConnection();) {
	// String schemaName = connection.getCatalog();
	// Map<String, Object> parameterMap = new HashMap<>();
	// parameterMap.put("schemaName", schemaName);
	// resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap,
	// String.class);
	// } catch (SQLException a_exc) {
	// logger.error("Error while fetching data from DB ", a_exc);
	// }
	// return resultSet;
	// }
	//
	// public List<String> getAllViewsListInSchema() {
	// String query = "SELECT TABLE_NAME FROM information_schema.VIEWS WHERE
	// TABLE_SCHEMA = :schemaName";
	// List<String> resultSet = new ArrayList<>();
	// try (Connection connection = dataSource.getConnection();) {
	// String schemaName = connection.getCatalog();
	// Map<String, Object> parameterMap = new HashMap<>();
	// parameterMap.put("schemaName", schemaName);
	// resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap,
	// String.class);
	// } catch (SQLException a_exc) {
	// logger.error("Error while fetching data from DB ", a_exc);
	// }
	// return resultSet;
	// }

	public List<Map<String, Object>> getTableInformationByName(String tableName) {
		String						query		= "select COLUMN_NAME as columnName, COLUMN_KEY as columnKey, DATA_TYPE as dataType, EXTRA as additionalInfo, "
				+ "CHARACTER_MAXIMUM_LENGTH as characterMaximumLength from information_schema.COLUMNS where TABLE_NAME = :tableName "
				+ "and TABLE_SCHEMA = :schemaName ORDER BY ORDINAL_POSITION ASC ";
		List<Map<String, Object>>	resultSet	= new ArrayList<>();
		try (Connection connection = dataSource.getConnection();) {
			String				schemaName		= connection.getCatalog();
			Map<String, Object>	parameterMap	= new HashMap<>();
			parameterMap.put("tableName", tableName);
			parameterMap.put("schemaName", schemaName);
			resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap);
		} catch (SQLException a_exc) {
			logger.error("Error while fetching data from DB ", a_exc);
		}
		return resultSet;
	}

	public Long getDynamicFormCount(String formId) {
		StringBuilder	stringBuilder	= new StringBuilder(
				"SELECT count(*) FROM DynamicForm AS d WHERE d.formId = :formId");
		Query			query			= getCurrentSession().createQuery(stringBuilder.toString());
		query.setParameter("formId", formId);
		return (Long) query.uniqueResult();
	}
}
