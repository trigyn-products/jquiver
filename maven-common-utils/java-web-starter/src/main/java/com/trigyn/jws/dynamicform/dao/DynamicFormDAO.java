package com.trigyn.jws.dynamicform.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class DynamicFormDAO extends DBConnection {

	@Autowired
	public DynamicFormDAO(DataSource dataSource) {
		super(dataSource);
	}

}
