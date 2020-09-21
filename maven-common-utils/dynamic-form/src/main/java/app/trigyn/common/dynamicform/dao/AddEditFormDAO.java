package app.trigyn.common.dynamicform.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dbutils.repository.DBConnection;

@Repository
public class AddEditFormDAO  extends DBConnection{

	@Autowired
	public AddEditFormDAO(DataSource dataSource) {
		super(dataSource);
	}

}
