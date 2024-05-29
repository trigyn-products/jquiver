package com.trigyn.jws.dynamicform.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dbutils.service.DataSourceFactory;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.DBExtractor;
import com.trigyn.jws.dbutils.vo.DataSourceVO;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibrary;

@Repository
public class DynamicFormCrudDAO extends DBConnection {

	private final static Logger				logger							= LogManager
			.getLogger(DynamicFormCrudDAO.class);

	@Autowired
	private DataSource						dataSource						= null;

	@Autowired
	private AdditionalDatasourceRepository	additionalDatasourceRepository	= null;
	
	@Autowired
	private IUserDetailsService		detailsService							= null;
	
	@Autowired
	private JwsDynarestDAO					dynarestDAO						= null;

	@Autowired
	public DynamicFormCrudDAO(DataSource dataSource) {
		super(dataSource);
	}

	public DynamicForm findDynamicFormById(String formId) {
		DynamicForm dynamicForm = hibernateTemplate.get(DynamicForm.class, formId);
		return dynamicForm;

	}

	public DynamicForm findDynamicFormByIdWithEvict(String formId) {
		DynamicForm dynamicForm = hibernateTemplate.get(DynamicForm.class, formId);
		if (dynamicForm != null)
			getCurrentSession().evict(dynamicForm);
		return dynamicForm;

	}

	public List<Map<String, Object>> getFormData(String dataSourceId, String selectionQuery) throws Exception {
		List<Map<String, Object>>	data			= null;
		JdbcTemplate				jdbcTemplate	= updateJdbcTemplateDataSource(dataSourceId);
		data = jdbcTemplate.queryForList(selectionQuery);
		return data;
	}

	public Integer saveFormData(String dataSourceId, String saveTemplateQuery, Map<String, Object> parameters)
			throws Exception {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = updateNamedParameterJdbcTemplateDataSource(
				dataSourceId);
		return namedParameterJdbcTemplate.update(saveTemplateQuery, parameters);
	}

	public List<Map<String, Object>> executeQueries(String dataSourceId, String query, Map<String, Object> parameterMap)
			throws Exception {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = updateNamedParameterJdbcTemplateDataSource(
				dataSourceId);
		return namedParameterJdbcTemplate.queryForList(query, parameterMap);
	}

	public void saveDynamicFormData(DynamicForm dynamicForm) {
		getCurrentSession().saveOrUpdate(dynamicForm);
	}

	//@Transactional(readOnly = false)
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveDynamicForm(DynamicForm dynamicForm) {
		List<DynamicFormSaveQuery> dynamicFormSaveQueries = dynamicForm.getDynamicFormSaveQueries();
		dynamicForm.setDynamicFormSaveQueries(null);
		if (dynamicForm.getFormId() == null || findDynamicFormByIdWithEvict(dynamicForm.getFormId()) == null) {
			getCurrentSession().save(dynamicForm);
		} else {
			getCurrentSession().saveOrUpdate(dynamicForm);
		}

		deleteFormQueries(dynamicForm.getFormId());
		if (dynamicFormSaveQueries != null) {
			for (DynamicFormSaveQuery dfsq : dynamicFormSaveQueries) {
				getCurrentSession().save(dfsq);
			}
		}
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

	public List<Map<String, Object>> getTableDetailsByTableName(String additionalDataSourceId, String tableName)
			throws Exception {

		DataSourceVO	dataSourceVO			= additionalDatasourceRepository
				.getDataSourceConfiguration(additionalDataSourceId);

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
	
	public final List<Object> scriptLibExecution(String formQueryId) {
		Query scriptLibQuery = getCurrentSession().createSQLQuery("SELECT jqsl.`template_id` "
				+ "FROM `jq_script_lib_connect` jqs LEFT JOIN `jq_dynamic_form_save_queries` jqf "
				+ "ON jqf.`dynamic_form_query_id` = jqs.`entity_id` "
				+ "LEFT JOIN `jq_script_lib_details` jqsl "
				+ "ON jqs.`script_lib_id` = jqsl.`script_lib_id` "
				+ "LEFT JOIN `jq_dynamic_form` jqd "
				+ "ON jqf.`dynamic_form_id` = jqd.`form_id`  "
				+ "WHERE jqs.`module_type_id` = :moduleId AND jqf.`dynamic_form_query_id` = :formQueryId ");
		
		scriptLibQuery.setParameter("moduleId", Constant.DYNAFORM_MOD_ID);
		scriptLibQuery.setParameter("formQueryId", formQueryId);
		
		List<Object[]> scriptLibList = scriptLibQuery.list();
		List<Object>		resultMap	= new ArrayList<>();
		
		for(int iCounter=0;iCounter<scriptLibList.size();iCounter++){
			Query roleQuery = getCurrentSession().createSQLQuery("SELECT COUNT(*) FROM `jq_entity_role_association` WHERE `role_id` = :roleId AND `is_active` = :isActive AND entity_id = :entityId ");
			roleQuery.setParameter("roleId",   Constant.ANONYMOUS_ROLE_ID);
			roleQuery.setParameter("isActive", Constant.IS_ACTIVE);
			roleQuery.setParameter("entityId", scriptLibList.get(iCounter));
			List<Object[]> roleCount = roleQuery.list();
			if(roleQuery.list().get(0).toString().equalsIgnoreCase("0")) {
				Query templateQuery = getCurrentSession().createSQLQuery("SELECT template FROM jq_template_master WHERE template_id = :templateId ");
				templateQuery.setParameter("templateId", scriptLibList.get(iCounter));
				List<Object[]> listTemplate = templateQuery.list();
				resultMap.add(listTemplate.get(0));
			}
		}
		 
		return resultMap;
	}
	
	public void scriptLibSave(List<String> formSaveQueryIdList,List<String> scriptLibInsertList,List<ScriptLibrary>	scriptLibInsert,String moduleId,Integer sourceTypeId) { 
		UserDetailsVO detailsVO = detailsService.getUserDetails();
		if(null != scriptLibInsertList && scriptLibInsertList.size() != 0 && scriptLibInsertList.isEmpty() == false) {
			for(int iScrInsertCounter=0; iScrInsertCounter<scriptLibInsertList.size(); iScrInsertCounter++) {
				String[] scriptLibId = scriptLibInsertList.get(iScrInsertCounter).split(",");
				if(null != scriptLibId && scriptLibId.length != 0) {
					for(int iscrLibIdCount = 0;iscrLibIdCount<scriptLibId.length;iscrLibIdCount++) {
						ScriptLibrary scriptlibrary = new ScriptLibrary();
						String scriptLibID = scriptLibId[iscrLibIdCount];
						if(sourceTypeId == Constant.REVISION_SOURCE_VERSION_TYPE || sourceTypeId == Constant.IMPORT_SOURCE_VERSION_TYPE) {
							dynarestDAO.scriptLibDeleteById(formSaveQueryIdList.get(iScrInsertCounter));
						}
						if(scriptLibID.isEmpty() == false) {
							scriptlibrary.setScriptLibId(scriptLibID.trim());
							scriptlibrary.setModuletypeId(moduleId);
							scriptlibrary.setEntityId(formSaveQueryIdList.get(iScrInsertCounter));
							scriptlibrary.setCreatedBy(detailsVO.getUserName());
							scriptlibrary.setUpdatedBy(detailsVO.getUserName());
							scriptlibrary.setUpdatedDate(new Date());
							scriptlibrary.setIsCustomUpdated(1);
							scriptLibInsert.add(scriptlibrary);
							getCurrentSession().save(scriptlibrary);
						}
					}
				}
			}
		}
	}
		
	public void scriptLibDelete(List<String> formSaveQueryIdList,List<String> scriptLibDeleteList,String moduleId) { 
		Query deleteScriptLibQuery = null;
		
		if(null != scriptLibDeleteList && scriptLibDeleteList.size() != 0 && scriptLibDeleteList.isEmpty() == false) {
			for(int iScrDeleteCounter=0; iScrDeleteCounter<scriptLibDeleteList.size(); iScrDeleteCounter++) {
				String[] scriptLibId = scriptLibDeleteList.get(iScrDeleteCounter).split(",");
				
				if(null != scriptLibId && scriptLibId.length != 0) {
					for(int iscrLibIdCount = 0;iscrLibIdCount<scriptLibId.length;iscrLibIdCount++) {
						String scriptLibID = scriptLibId[iscrLibIdCount];
						if(scriptLibID.isEmpty() == false) {
							deleteScriptLibQuery = getCurrentSession().createSQLQuery("delete from jq_script_lib_connect where entity_id=:entityid and script_lib_id =  :scriptlibid ");
							deleteScriptLibQuery.setParameter("scriptlibid", scriptLibID);
							deleteScriptLibQuery.setParameter("entityid", formSaveQueryIdList.get(iScrDeleteCounter));
							int resultSetDelete = deleteScriptLibQuery.executeUpdate();
						}
					}
				}
			}
		}
	}
	
	public List<Map<String, Object>> getScriptLibDetatils(String formQueryId) throws Exception {
		Query selectScriptConnQuery = null;
		Query selectScriptLibQuery = null;
		List<Map<String, Object>> scriptLibList = new ArrayList<>();
		selectScriptConnQuery =  getCurrentSession().createSQLQuery("SELECT script_lib_id AS scriptId FROM jq_script_lib_connect WHERE entity_id = :entityid ");
		selectScriptConnQuery.setParameter("entityid", formQueryId);
		List<Object[]> scriptLibConnList = selectScriptConnQuery.list();
		for(int iCounter=0; iCounter<scriptLibConnList.size();iCounter++) {
			Map<String, Object> scriptLibMap = new LinkedHashMap<>();
			selectScriptLibQuery =  getCurrentSession().createSQLQuery("SELECT script_lib_id AS scriptId, library_name AS libraryName FROM jq_script_lib_details WHERE script_lib_id = :scriptlibid ");
			selectScriptLibQuery.setParameter("scriptlibid", scriptLibConnList.get(iCounter));
			List<Object[]> scriptLibDetList = selectScriptLibQuery.list();
			scriptLibMap.put("scriptId", scriptLibDetList.get(0)[0].toString()+"");
			scriptLibMap.put("libraryName", scriptLibDetList.get(0)[1].toString()+"");	
			scriptLibList.add(scriptLibMap);
		}
	
		return scriptLibList;
	}
	
}
