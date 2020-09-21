package app.trigyn.common.dynamicform.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dbutils.repository.DBConnection;
import app.trigyn.common.dynamicform.entities.DynamicForm;
import app.trigyn.common.dynamicform.entities.DynamicFormSaveQuery;
import app.trigyn.core.templating.dao.QueryStore;

@Repository
public class DynamicFormDAO extends DBConnection {

	@Autowired
	public DynamicFormDAO(DataSource dataSource) {
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

}
