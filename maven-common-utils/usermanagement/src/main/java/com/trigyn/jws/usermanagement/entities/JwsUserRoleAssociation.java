package com.trigyn.jws.usermanagement.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jws_user_role_association")
public class JwsUserRoleAssociation {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "user_role_id")
	private String	userRoleId	= null;

	@Column(name = "role_id")
	private String	roleId		= null;

	@Column(name = "user_id")
	private String	userId		= null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date	updatedDate	= null;

	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
	private JwsRole	jwsRole		= null;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	private JwsUser	jwsUser		= null;

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public JwsRole getJwsRole() {
		return jwsRole;
	}

	public void setJwsRole(JwsRole jwsRole) {
		this.jwsRole = jwsRole;
	}

	public JwsUser getJwsUser() {
		return jwsUser;
	}

	public void setJwsUser(JwsUser jwsUser) {
		this.jwsUser = jwsUser;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
