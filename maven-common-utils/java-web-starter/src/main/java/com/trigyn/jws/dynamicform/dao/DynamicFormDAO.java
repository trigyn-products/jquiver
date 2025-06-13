package com.trigyn.jws.dynamicform.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.formio.entities.FormIO;

import jakarta.persistence.Query;

@Repository
public class DynamicFormDAO extends DBConnection {

	@Autowired
	public DynamicFormDAO(DataSource dataSource) {
		super(dataSource);
	}
	
	public Object getFormIoMetaData(String formIoId) {
		Query query = getCurrentSession()
				.createQuery(" SELECT fio FROM FormIO fio INNER JOIN DynamicForm df ON fio.formIoId = df.formIoId AND df.formId=:formId");
		query.setParameter("formId", formIoId);		
		Object data = (FormIO) query.getSingleResult();
		if(data != null) getCurrentSession().evict(data);
		return data;
	}

}
