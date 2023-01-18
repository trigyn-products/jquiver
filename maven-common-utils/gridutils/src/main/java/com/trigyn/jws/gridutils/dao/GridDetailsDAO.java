package com.trigyn.jws.gridutils.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.gridutils.entities.GridDetails;

@Repository
public class GridDetailsDAO extends DBConnection {

	@Autowired
	public GridDetailsDAO(DataSource dataSource) {
		super(dataSource);
	}

	public GridDetails findGridDetailsById(String gridId) {
		GridDetails grid =  hibernateTemplate.get(GridDetails.class, gridId);
		if(grid != null) getCurrentSession().evict(grid);
		return grid;
	}

	@Transactional(readOnly = false)
	public void saveGridDetails(GridDetails grid) {
		if(grid.getGridId() == null || findGridDetailsById(grid.getGridId()) == null) {
			getCurrentSession().save(grid);			
		}else {
			getCurrentSession().saveOrUpdate(grid);
		}
	}

}
