package com.trigyn.jws.gridutils.dao;

import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.gridutils.entities.GridDetails;

@Repository
public class GridDetailsDAO extends DBConnection {

	public GridDetailsDAO(DataSource dataSource) {
		super(dataSource);
	}

	public GridDetails findGridDetailsById(String gridId) {
		GridDetails grid =  getCurrentSession().get(GridDetails.class, gridId);
		if(grid != null) getCurrentSession().evict(grid);
		return grid;
	}

	@Transactional(readOnly = false)
	public GridDetails saveGridDetails(GridDetails grid) {
		if(grid.getGridId() == null || findGridDetailsById(grid.getGridId()) == null) {
			getCurrentSession().persist(grid);			
		}else {
			getCurrentSession().merge(grid);
		}
		return grid;
	}

}
