package com.trigyn.jws.security.dao;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.security.entities.DDOSDetails;

@Repository
public class DDOSDao extends DBConnection {

	public DDOSDao(DataSource dataSource) {
		super(dataSource);
	}

	public void saveDDOSDetails(DDOSDetails ddosDetails) throws Exception {
		getCurrentSession().saveOrUpdate(ddosDetails);
	}

}
