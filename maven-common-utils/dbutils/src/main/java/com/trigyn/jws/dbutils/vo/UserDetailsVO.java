package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDetailsVO implements Serializable{

	private static final long serialVersionUID 	= 186468606084940713L;

	private String userId						= null;
	
	private String userName						= null;

	private List<String> roleIdList				= new ArrayList<>();
	
	public UserDetailsVO() {
		
	}
	
	public UserDetailsVO(String userId, String userName, List<String> roleIdList) {
		this.userId 		= userId;
		this.userName 		= userName;
		this.roleIdList 	= roleIdList;
	}
	
	
	public String getUserId() {
		return userId;
	}

	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getUserName() {
		return userName;
	}

	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public List<String> getRoleIdList() {
		return roleIdList;
	}

	
	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleIdList, userId, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserDetailsVO other = (UserDetailsVO) obj;
		return Objects.equals(roleIdList, other.roleIdList) && Objects.equals(userId, other.userId) && Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "UserDetailsVO [userId=" + userId + ", userName=" + userName + ", roleIdList=" + roleIdList + "]";
	}


}
