package com.trigyn.jws.usermanagement.utils;

public final class Constants {

	private Constants() {

    }
	
	public  final static Integer ISACTIVE = 1; 
	
	public  final static Integer INACTIVE = 0; 
	
	public  final static String ADMIN_ROLE_ID  = "ae6465b3-097f-11eb-9a16-f48e38ab9348";
	
	public  final static String ANONYMOUS_ROLE_NAME = "ANONYMOUS";
	
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
	
	public enum Modules{
		
		GRIDUTILS("Grid Utils"),
		TEMPLATING("Templating"),
		DYNAMICFORM("Form Builder"),
		DYNAMICREST("REST API Builder"),
		AUTOCOMPLETE("TypeAhead Autocomplete"),
		DASHBOARD("Dashboard"),
		SITELAYOUT("Site Layout");
		
		final String moduleName;
		
		Modules(String moduleName) {
			this.moduleName = moduleName;
		}
		
		public String getModuleName() {
			return moduleName;
		}
	}
	
	public  final static String GRIDUTILS = "Grid Utils";
	public  final static String TEMPLATING = "Templating";
	public  final static String DYNAMICFORM = "Form Builder";
	public  final static String DYNAMICREST = "REST API Builder";
	public  final static String AUTOCOMPLETE = "TypeAhead Autocomplete";
	public  final static String DASHBOARD = "Dashboard";
	public  final static String SITELAYOUT = "Site Layout";
}
