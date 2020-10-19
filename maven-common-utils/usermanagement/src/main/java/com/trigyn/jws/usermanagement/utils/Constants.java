package com.trigyn.jws.usermanagement.utils;

public final class Constants {

	private Constants() {

    }
	
	public  final static Integer ISACTIVE = 1; 
	
	public  final static Integer INACTIVE = 0; 
	
	public  final static String ADMIN_ROLE  = "ae6465b3-097f-11eb-9a16-f48e38ab9348";
	
	public enum AuthType {
        INMEMORY(1), DAO(2), OAUTH(3), LDAP(4);

        final Integer authType;

        AuthType(Integer authType) {
        	this.authType = authType;
        }

        public Integer getAuthType() {
            return authType;
        }
    }
	
}
