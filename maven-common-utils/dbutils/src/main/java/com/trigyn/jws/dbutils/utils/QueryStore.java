package com.trigyn.jws.dbutils.utils;

public final class QueryStore {
	
    public static final String JPA_QUERY_TO_GET_ALL_ACTIVE_ROLES = "SELECT new com.trigyn.jws.dbutils.vo.UserRoleVO"
    		+ "(ur.roleId AS roleId, ur.roleName AS roleName, ur.roleDescription AS roleDescription) FROM UserRole as ur "
    		+ " WHERE ur.isDeleted = :isDeleted ORDER BY ur.roleName ASC ";
    
}
