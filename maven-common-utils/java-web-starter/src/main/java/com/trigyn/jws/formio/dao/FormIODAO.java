package com.trigyn.jws.formio.dao;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;

@Repository
public class FormIODAO extends DBConnection {

	public FormIODAO(DataSource dataSource) {
		super(dataSource);
	}
	
	@Transactional
	public List<Object[]> getAllFormsByDynaFormId(String formIoId) {
		Query<Object[]> formIoQuery = getCurrentSession().createNativeQuery("SELECT DISTINCT fio.form_io_id AS formIoId, fio.form_name AS formName, fio.form_description AS formDescription FROM `jq_dynamic_form` df, `jq_form_io` fio WHERE df.`form_io_id` = fio.`form_io_id` AND df.form_io_id IN (:formIoId) ");
		formIoQuery.setParameter("formIoId", formIoId);
		return formIoQuery.list();
	}
	
	@Transactional
	public List<Object[]> getAllFormsByDynamicFormId(String formIoId) {
		Query<Object[]> formIoQuery = getCurrentSession().createNativeQuery("SELECT DISTINCT\r\n"
				+ "    fio.form_io_id,\r\n"
				+ "    fio.form_name,\r\n"
				+ "    fio.form_io_json,\r\n"
				+ "    fio.form_description,\r\n"
				+ "    fio.form_io_type,\r\n"
				+ "    fio.persistence_type,\r\n"
				+ "    fio.is_custom_updated,\r\n"
				+ "    fio.last_updated_by,\r\n"
				+ "    fio.last_updated_ts\r\n"
				+ "FROM jq_dynamic_form df\r\n"
				+ "JOIN jq_form_io fio\r\n"
				+ "ON df.form_io_id = fio.form_io_id\r\n"
				+ "WHERE df.form_io_id = :formIoId");
		formIoQuery.setParameter("formIoId", formIoId);
		return formIoQuery.list();
	}
	
	public void deleteAllFormIoRolesByEntity(String formIoId) throws Exception {
		MutationQuery query = getCurrentSession().createMutationQuery(QueryStore.HQL_QUERY_TO_DELETE_FORMIO_PAGE_ROLES.toString());
		query.setParameter("formIoId", formIoId);
		query.executeUpdate();
	}
	
	public void saveOrUpdateFormIo(FormIO formIo) {
		if (formIo.getFormIoId() == null || findFormIOByIdWithEvict(formIo.getFormIoId()) == null) {
			getCurrentSession().persist(formIo);
		} else {
			getCurrentSession().merge(formIo);
		}
	}

	private Object findFormIOByIdWithEvict(String formIoId) {
		FormIO formIo = getCurrentSession().get(FormIO.class, formIoId);
		if (formIo != null)
			getCurrentSession().evict(formIo);
		return formIo;
	}

}
