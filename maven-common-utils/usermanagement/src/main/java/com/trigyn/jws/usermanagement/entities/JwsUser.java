package com.trigyn.jws.usermanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.trigyn.jws.usermanagement.vo.JwsUserVO;

@Entity
@Table(name = "jws_user")
public class JwsUser {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "user_id")
	private String	userId				= null;

	@Column(name = "first_name")
	private String	firstName			= null;

	@Column(name = "last_name")
	private String	lastName			= null;

	@Column(name = "email")
	private String	email				= null;

	@Column(name = "force_password_change")
	private Integer	forcePasswordChange	= null;
	// system generated password

	@Column(name = "password")
	private String	password			= null;

	@Column(name = "is_active")
	private Integer	isActive			= null;

	@Column(name = "secret_key")
	private String	secretKey			= null;

	@Column(name = "registered_by")
	private Integer	registeredBy		= null;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getForcePasswordChange() {
		return forcePasswordChange;
	}

	public void setForcePasswordChange(Integer forcePasswordChange) {
		this.forcePasswordChange = forcePasswordChange;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Integer getRegisteredBy() {
		return registeredBy;
	}

	public void setRegisteredBy(Integer registeredBy) {
		this.registeredBy = registeredBy;
	}

	public JwsUser getObject() {
		JwsUser user = new JwsUser();

		user.setEmail(email);
		user.setFirstName(firstName);
		user.setForcePasswordChange(forcePasswordChange);
		user.setIsActive(isActive);
		user.setLastName(lastName);
		user.setPassword(password);
		user.setRegisteredBy(registeredBy);
		user.setSecretKey(secretKey);
		user.setUserId(userId);
		return user;
	}

	public JwsUserVO convertEntityToVO(JwsUser userData) {

		JwsUserVO jwsUser = new JwsUserVO();
		jwsUser.setUserId(StringUtils.isNotEmpty(userData.getUserId()) ? userData.getUserId() : null);
		jwsUser.setFirstName(userData.getFirstName());
		jwsUser.setLastName(userData.getLastName());
		jwsUser.setEmail(userData.getEmail());
		jwsUser.setIsActive(userData.getIsActive());
		jwsUser.setPassword(userData.getPassword());
		return jwsUser;
	}

}
