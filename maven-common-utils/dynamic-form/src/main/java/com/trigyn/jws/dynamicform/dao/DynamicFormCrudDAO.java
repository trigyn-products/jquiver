package com.trigyn.jws.dynamicform.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;

@Repository
public class DynamicFormCrudDAO extends DBConnection {

	@Autowired
	public DynamicFormCrudDAO(DataSource dataSource) {
		super(dataSource);
	}
	
	public DynamicForm findDynamicFormById(String formId){
		return hibernateTemplate.get(DynamicForm.class, formId);
	}
	
	public List<Map<String, Object>> getFormData(String selectionQuery) throws Exception {
		List<Map<String, Object>> data = null;
		data = jdbcTemplate.queryForList(selectionQuery);
		return data;
	}

	public void saveFormData(String saveTemplateQuery, Map<String, Object> parameters) {
		namedParameterJdbcTemplate.update(saveTemplateQuery, parameters);
	}

	public void saveDynamicFormData(DynamicForm dynamicForm) {
		getCurrentSession().saveOrUpdate(dynamicForm);
	}

	public List<DynamicFormSaveQuery> findDynamicFormQueriesById(String formId) {
		Query query = getCurrentSession().createQuery("FROM DynamicFormSaveQuery AS dfs WHERE dfs.dynamicFormId = :formId ORDER BY dfs.sequence ASC ");
		query.setParameter("formId", formId);
		return query.list();
	}

	public void deleteFormQueries(String formId) {
		Query query = getCurrentSession().createQuery("DELETE FROM DynamicFormSaveQuery AS dfs WHERE dfs.dynamicFormId = :formId");
		query.setParameter("formId", formId);
		query.executeUpdate();
	}
	
	public void deleteFormQueriesByIds(String formId) {
		StringBuilder deleteFormQuery = new StringBuilder("DELETE FROM DynamicFormSaveQuery AS dfs WHERE dfs.dynamicFormId = :formId ");
		Query query = getCurrentSession().createQuery(deleteFormQuery.toString());
		query.setParameter("formId", formId);
		query.executeUpdate();
	}
	
	public String checkFormName(String formName) {
		 Query query = getCurrentSession().createQuery("SELECT formId FROM DynamicForm  WHERE lower(formName) = lower(:formName)");
	     query.setParameter("formName", formName);
	     String data = (String) query.uniqueResult();
	     return data;
	}
	
	public List<DynamicForm> getAllDynamicForms(Integer formTypeId){
		Query query = getCurrentSession(). createQuery("FROM DynamicForm AS df WHERE df.formTypeId = :formTypeId");
		query.setParameter("formTypeId", formTypeId);
		return query.list();
	}
	
	public DynamicForm getFormDetailsByName(String formName) {
		 Query query = getCurrentSession().createQuery(" FROM DynamicForm  WHERE lower(formName) = lower(:formName)");
	     query.setParameter("formName", formName);
	     DynamicForm data = (DynamicForm) query.uniqueResult();
	     return data;
	}

	public List<Map<String, Object>> getTableDetailsByTableName(String tableName) {
		String query = "select REPLACE(COLUMN_NAME, '_', '') as columnName, COLUMN_NAME as tableColumnName, COLUMN_KEY as columnKey, DATA_TYPE as dataType, " + 
				"REPLACE(CONCAT(UPPER(SUBSTRING(COLUMN_NAME,1,1)),LOWER(SUBSTRING(COLUMN_NAME,2))), '_', ' ') as fieldName, " + 
				"CASE WHEN DATA_TYPE = \"varchar\" THEN \"text\" WHEN DATA_TYPE = \"int\" THEN \"number\" WHEN DATA_TYPE LIKE (\"date%\") THEN \"datetime\" WHEN DATA_TYPE LIKE (\"time%\") THEN \"datetime\" ELSE \"textarea\" END as columnType, " + 
				"CASE WHEN DATA_TYPE = \"varchar\" THEN CHARACTER_MAXIMUM_LENGTH WHEN DATA_TYPE = \"int\" THEN NUMERIC_PRECISION ELSE CHARACTER_MAXIMUM_LENGTH END as columnSize " + 
				"from information_schema.COLUMNS where TABLE_NAME = :tableName " + 
				"and TABLE_SCHEMA = :schemaName " + 
				"order by ORDINAL_POSITION ASC ";
		List<Map<String, Object>> resultSet = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();){
			String schemaName = connection.getCatalog();
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("tableName", tableName);
			parameterMap.put("schemaName", schemaName);
			resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public List<String> getAllTablesListInSchema() {
		String query = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = :schemaName";
		List<String> resultSet = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();){
			String schemaName = connection.getCatalog();
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("schemaName", schemaName);
			resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap, String.class);
		} catch (SQLException exception) {
			
		}
		return resultSet;
	}
	
	public List<String> getAllViewsListInSchema() {
		String query = "SELECT TABLE_NAME FROM information_schema.VIEWS WHERE TABLE_SCHEMA = :schemaName";
		List<String> resultSet = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();){
			String schemaName = connection.getCatalog();
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("schemaName", schemaName);
			resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap, String.class);
		} catch (SQLException exception) {
			
		}
		return resultSet;
	}

	public List<Map<String, Object>> getTableInformationByName(String tableName) {
		String query = "select COLUMN_NAME as columnName, COLUMN_KEY as columnKey, DATA_TYPE as dataType,"
				+ "CHARACTER_MAXIMUM_LENGTH as characterMaximumLength from information_schema.COLUMNS where TABLE_NAME = :tableName " + 
				"and TABLE_SCHEMA = :schemaName ORDER BY ORDINAL_POSITION ASC ";
		List<Map<String, Object>> resultSet = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();){
			String schemaName = connection.getCatalog();
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("tableName", tableName);
			parameterMap.put("schemaName", schemaName);
			resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

}
