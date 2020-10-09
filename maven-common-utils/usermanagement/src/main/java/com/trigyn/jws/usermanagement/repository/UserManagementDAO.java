package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class UserManagementDAO  extends DBConnection{

	@Autowired
	public UserManagementDAO(DataSource dataSource) {
		super(dataSource);
	}

	public void deleteUserRoleAssociation(String userId) {
		Query query = getCurrentSession().createQuery("DELETE FROM JwsUserRoleAssociation WHERE userId=:userId");
		query.setParameter("userId", userId);
		query.executeUpdate();
		
	}
	
	public List<String> getRoleIdsByUserId(String userId)throws Exception {
		Query query = getCurrentSession().createQuery("SELECT roleId FROM JwsUserRoleAssociation WHERE userId =:userId");
		query.setParameter("userId", userId);
		List<String> roleIds = (List<String>) query.getResultList();
		return roleIds;
	}
}
