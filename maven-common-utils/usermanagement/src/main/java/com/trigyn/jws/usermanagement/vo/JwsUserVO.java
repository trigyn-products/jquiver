package com.trigyn.jws.usermanagement.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.trigyn.jws.usermanagement.entities.JwsUser;

public class JwsUserVO implements Serializable{
	
	private static final long serialVersionUID = 5522267590792143057L;

	private String userId = null;
	
	private String firstName = null;
	
	private String lastName = null;
	
	private String email = null;
	
	private Integer password = null;
	
	private Integer isActive = null;
	
	private List<String> roleIds = null;

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

	public Integer getPassword() {
		return password;
	}

	public void setPassword(Integer password) {
		this.password = password;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public JwsUser convertVOToEntity(JwsUserVO userData) {
		
		JwsUser jwsUser = new JwsUser();
		jwsUser.setUserId(StringUtils.isNotEmpty(userData.getUserId())?userData.getUserId():null);
		jwsUser.setFirstName(userData.getFirstName());
		jwsUser.setLastName(userData.getFirstName());
		jwsUser.setEmail(userData.getEmail());
		jwsUser.setIsActive(userData.getIsActive());
		return jwsUser;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	
	
}
