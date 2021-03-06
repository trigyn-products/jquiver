package com.trigyn.jws.usermanagement.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.security.config.TwoFactorGoogleUtil;
import com.trigyn.jws.usermanagement.utils.Constants;

@Repository
public class UserManagementDAO extends DBConnection {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	public UserManagementDAO(DataSource dataSource) {
		super(dataSource);
	}

	public void deleteUserRoleAssociation(String userId) {
		Query query = getCurrentSession().createQuery("DELETE FROM JwsUserRoleAssociation WHERE userId=:userId");
		query.setParameter("userId", userId);
		query.executeUpdate();

	}

	public List<String> getRoleIdsByUserId(String userId) throws Exception {
		Query query = getCurrentSession().createQuery("SELECT roleId FROM JwsUserRoleAssociation WHERE userId =:userId");
		query.setParameter("userId", userId);
		List<String> roleIds = (List<String>) query.getResultList();
		return roleIds;
	}

	@Transactional
	public JwsUser saveUserData(JwsUser jwsUser) {
		jwsUser.setRegisteredBy(Constants.AuthType.OAUTH.getAuthType());
		if (jwsUser.getUserId() == null) {
			TwoFactorGoogleUtil twoFactorGoogleUtil = new TwoFactorGoogleUtil();
			jwsUser.setSecretKey(twoFactorGoogleUtil.generateSecretKey());
		}
		getCurrentSession().saveOrUpdate(jwsUser);
		return jwsUser;
	}

	@Transactional
	public JwsUser updateUserData(JwsUser jwsUser) {
		getCurrentSession().saveOrUpdate(jwsUser);
		return jwsUser;
	}

	@Transactional
	public void saveRoleData(JwsRole jwsRole) {
		getCurrentSession().saveOrUpdate(jwsRole);
	}

	@Transactional
	public void saveAuthenticatedRole(String userId) {
		JwsUserRoleAssociation userRoleAssociation = new JwsUserRoleAssociation();
		userRoleAssociation.setRoleId(Constants.AUTHENTICATED_ROLE_ID);
		userRoleAssociation.setUserId(userId);
		userRoleAssociation.setUpdatedDate(new Date());
		getCurrentSession().save(userRoleAssociation);
	}

}
