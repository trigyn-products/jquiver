package com.trigyn.jws.usermanagement.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jws_user")
public class JwsUser {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name="user_id")
	private String userId = null;
	
	@Column(name="first_name")
	private String firstName = null;
	
	@Column(name="last_name")
	private String lastName = null;
	
	@Column(name="email")
	private String email = null;
	
	@Column(name="force_password_change")
	private Integer forcePasswordChange = null;
	// system generated password
	
	@Column(name="password")
	private String password = null;
	
	@Column(name="is_active")
	private Integer isActive = null;
	
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

	
	
}
