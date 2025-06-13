package com.trigyn.jws.usermanagement.repository;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.security.config.TwoFactorGoogleUtil;
import com.trigyn.jws.usermanagement.utils.Constants;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class UserManagementDAO extends DBConnection {

	public UserManagementDAO(DataSource dataSource) {
		super(dataSource);
	}

	@PersistenceContext
	private EntityManager em;

	public void deleteUserRoleAssociation(String userId) {
		MutationQuery query = getCurrentSession().createMutationQuery("DELETE FROM JwsUserRoleAssociation WHERE userId=:userId");
		query.setParameter("userId", userId);
		query.executeUpdate();

	}

	public List<String> getRoleIdsByUserId(String userId) throws Exception {
		Query query = getCurrentSession().createQuery("SELECT roleId FROM JwsUserRoleAssociation WHERE userId =:userId", String.class);
		query.setParameter("userId", userId);
		List<String> roleIds = (List<String>) query.getResultList();
		return roleIds;
	}

	@Transactional
	public JwsUser saveUserData(JwsUser jwsUser) {
		if(jwsUser.getRegisteredBy()==null)
			jwsUser.setRegisteredBy(Constants.AuthType.OAUTH.getAuthType());
		jwsUser.setIsCustomUpdated(1);
		if (jwsUser.getUserId() == null) {
			TwoFactorGoogleUtil twoFactorGoogleUtil = new TwoFactorGoogleUtil();
			jwsUser.setSecretKey(twoFactorGoogleUtil.generateSecretKey());
		}
		getCurrentSession().saveOrUpdate(jwsUser);
		return jwsUser;
	}

	@Transactional
	public JwsUser updateUserData(JwsUser jwsUser) {
		jwsUser.setIsCustomUpdated(1);
		getCurrentSession().merge(jwsUser);
		return jwsUser;
	}

	@Transactional
	public void saveAuthenticatedRole(String userId) {
		JwsUserRoleAssociation userRoleAssociation = new JwsUserRoleAssociation();
		userRoleAssociation.setRoleId(Constants.AUTHENTICATED_ROLE_ID);
		userRoleAssociation.setUserId(userId);
		userRoleAssociation.setUpdatedDate(new Date());
		getCurrentSession().persist(userRoleAssociation);
	}

	public JwsUser findJwsUserById(String userId) {
		JwsUser user = getCurrentSession().get(JwsUser.class, userId);
		if (user != null) {
			user.setIsCustomUpdated(1);
			getCurrentSession().evict(user);
		}
		return user;
	}

	public JwsUserRoleAssociation findJwsUserRoleById(String userRoleId) {
		JwsUserRoleAssociation userRole =  getCurrentSession().get(JwsUserRoleAssociation.class, userRoleId);
		if(userRole != null) getCurrentSession().evict(userRole);
		return userRole;
	
	}

	@Transactional
	public void saveJwsUser(JwsUser user) {
		if(user.getUserId() == null || findJwsUserById(user.getUserId()) == null) {
			getCurrentSession().persist(user);			
		}else {
			getCurrentSession().merge(user);
		}
	}

	public JwsRole findJwsRoleById(String roleId) {
		JwsRole role =  getCurrentSession().get(JwsRole.class, roleId);
		if(role != null) getCurrentSession().evict(role);
		return role;
	
	}

	@Transactional
	public void saveJwsRole(JwsRole role) {
		if(role.getRoleId() == null || findJwsRoleById(role.getRoleId()) == null) {
			getCurrentSession().persist(role);			
		}else {
			getCurrentSession().merge(role);
		}
	}
	
	public Integer getEntityRoleTypeID(String query)
	{
		Query querynew = getCurrentSession().createQuery(query, Integer.class);
		return (Integer) querynew.uniqueResult();
		
	}
}
