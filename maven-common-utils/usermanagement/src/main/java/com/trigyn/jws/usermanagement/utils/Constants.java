package com.trigyn.jws.usermanagement.utils;

public final class Constants {

	private Constants() {

    }
	
	public  final static Integer ISACTIVE = 1; 
	
	public  final static Integer INACTIVE = 0; 
	
	public  final static String ADMIN_ROLE  = "ae6465b3-097f-11eb-9a16-f48e38ab9348";
	
	public enum AuthType {
        INMEMORY("in-memory"), DAO("dao-based"), OAUTH("oauth"), LDAP("ldap");

        final String authType;

        AuthType(String authType) {
        	this.authType = authType;
        }

        public String getAuthType() {
            return authType;
        }
    }
	
}
