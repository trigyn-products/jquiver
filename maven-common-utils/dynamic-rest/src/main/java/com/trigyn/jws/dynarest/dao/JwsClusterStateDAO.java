package com.trigyn.jws.dynarest.dao;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynarest.entities.JwsClusterState;

/**
 * @author Shrinath.Halki
 *
 */
@Repository
public class JwsClusterStateDAO extends DBConnection {

	public JwsClusterStateDAO(DataSource dataSource) {
		super(dataSource);
	}

	@Transactional
	public void saveClusterState(JwsClusterState clusterState) {
		getCurrentSession().save(clusterState);
		getCurrentSession().flush();
	}
	
	@Transactional
	public void updateClusterState(JwsClusterState clusterState) {
		getCurrentSession().saveOrUpdate(clusterState);
		getCurrentSession().flush();
	}

}
