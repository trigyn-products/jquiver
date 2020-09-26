package com.trigyn.jws.dynamicform.dao;

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

	public void saveFormData(String saveTemplateQuery) {
		jdbcTemplate.execute(saveTemplateQuery);
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

	public String checkFormName(String formName) {
		 Query query = getCurrentSession().createQuery("SELECT formId FROM DynamicForm  WHERE lower(formName) = lower(:formName)");
	     query.setParameter("formName", formName);
	     String data = (String) query.uniqueResult();
	     return data;
	}
	
	public List<DynamicForm> getAllDynamicForms(){
		Query query = getCurrentSession(). createQuery("FROM DynamicForm");
		return query.list();
	}
	
	public DynamicForm getFormDetailsByName(String formName) {
		 Query query = getCurrentSession().createQuery(" FROM DynamicForm  WHERE lower(formName) = lower(:formName)");
	     query.setParameter("formName", formName);
	     DynamicForm data = (DynamicForm) query.uniqueResult();
	     return data;
	}

	public List<Map<String, Object>> getTableDetailsByTableName(String tableName) {
		String query = "select REPLACE(COLUMN_NAME, '_', '') as columnName, " + 
				"REPLACE(CONCAT(UPPER(SUBSTRING(COLUMN_NAME,1,1)),LOWER(SUBSTRING(COLUMN_NAME,2))), '_', ' ') as fieldName, " + 
				"CASE WHEN DATA_TYPE = \"varchar\" THEN \"text\" WHEN DATA_TYPE = \"int\" THEN \"number\" ELSE DATA_TYPE END as columnType, " + 
				"CASE WHEN DATA_TYPE = \"varchar\" THEN CHARACTER_MAXIMUM_LENGTH WHEN DATA_TYPE = \"int\" THEN NUMERIC_PRECISION ELSE CHARACTER_MAXIMUM_LENGTH END as columnSize " + 
				"from information_schema.COLUMNS where TABLE_NAME = :tableName and DATA_TYPE IN (\"varchar\", \"int\") " + 
				"and TABLE_SCHEMA = :schemaName " + 
				"order by ORDINAL_POSITION ASC ";
		List<Map<String, Object>> resultSet = new ArrayList<>();
		try {
			String schemaName = dataSource.getConnection().getCatalog();
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
		String query = "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = :schemaName";
		List<String> resultSet = new ArrayList<>();
		try {
			String schemaName = dataSource.getConnection().getCatalog();
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("schemaName", schemaName);
			resultSet = namedParameterJdbcTemplate.queryForList(query, parameterMap, String.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

}
