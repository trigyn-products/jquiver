package com.trigyn.jws.usermanagement.utils;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

	private Constants() {

	}

	public final static Integer	IS_NEW_USER				= 0;

	public final static Integer	ISACTIVE				= 1;

	public final static Integer	INACTIVE				= 0;

	public final static String	ADMIN_ROLE_ID			= "ae6465b3-097f-11eb-9a16-f48e38ab9348";

	public final static String	ADMIN_USER_ID			= "111415ae-0980-11eb-9a16-f48e38ab9348";

	public final static String	AUTHENTICATED_ROLE_ID	= "2ace542e-0c63-11eb-9cf5-f48e38ab9348";

	public final static String	ANONYMOUS_ROLE_NAME		= "ANONYMOUS";

	public final static String	AUTHENTICATED_ROLE_NAME	= "AUTHENTICATED";

	public final static String	ADMIN_ROLE_NAME			= "ADMIN";

	public final static String	ISEDIT					= "0";

	public final static Integer	OPEN					= 3;

	public final static String	ACTION					= "1";

	public final static String	MULTI_AUTH_TYPE			= "multi";

	public final static String	SINGLE_AUTH_TYPE		= "single";

	public final static String	TRUE					= "true";

	public final static String	FALSE					= "false";
	
	public static final String	PASSWORD_VERFICATION	= "1";
	
	public static final String	OTP_VERFICATION			= "0";
	
	public static final String	TOTP_VERFICATION		= "2";
	
	public final static String	ANONYMOUS_USER_NAME		= "ANONYMOUS";
	
	public final static Integer	CLUSTER_ACTIVE_TIME		= 1;
	
	public enum AuthType {
		@Deprecated
		INMEMORY("inmemore", 1), 
		DAO("7d1dba821", 2), 
		LDAP("7d1ldap821", 3), 
		OAUTH("7d1oa821", 4);
		//TODO : SAML will be uncommented once its implemented.
		//,SAML("", 5);

		private final Integer authType;
		private final String authAtID;
		private static Map<String, AuthType> authTypeMap = new HashMap<>();
		
		static {
	        for (AuthType value : values()) {
	            authTypeMap.put(value.authAtID, value);
	        }
	    }

		AuthType(String a_strAuthID, Integer a_authType) {
			authAtID = a_strAuthID;
			authType = a_authType;
		}

		public Integer getAuthType() {
			return authType;
		}
		
		public static AuthType valueOfAt(String at) {
	        return authTypeMap.get(at);
	    }
	}
	
	public enum AuthTypeByName {
		@Deprecated
		INMEMORY("inmemore", 1), 
		DAO("enableDatabaseAuthentication", 2), 
		LDAP("enableLdapAuthentication", 3), 
		OAUTH("enableOAuthentication", 4);
		//TODO : SAML will be uncommented once its implemented.
		//,SAML("", 5);
		final String authTypeByName;
		final Integer authTypeById;
		private static Map<String, AuthTypeByName> authTypeMap = new HashMap<>();
		static {
	        for (AuthTypeByName value : values()) {
	            authTypeMap.put(value.authTypeByName, value);
	        }
	    }

		AuthTypeByName(String authTypeByName, Integer authTypeById) {
			this.authTypeByName = authTypeByName;
			this.authTypeById = authTypeById;
		}
		
		public String getAuthTypeByName() {
			return authTypeByName;
		}
		
		public Integer getAuthTypeById() {
			return authTypeById;
		}
		
		public static AuthTypeByName valueOfAt(String at) {
	        return authTypeMap.get(at);
	    }
	}
	
	

	public enum VerificationType {
		OTP("0"), PASSWORD("1"), TOTP("2");

		final String verificationType;

		VerificationType(String verificationType) {
			this.verificationType = verificationType;
		}

		public String getVerificationType() {
			return verificationType;
		}
	}

	public enum Modules {

		GRIDUTILS("Grid Utils"), TEMPLATING("Templating"), DYNAMICFORM("Form Builder"), DYNAMICREST("REST API Builder"),
		AUTOCOMPLETE("TypeAhead Autocomplete"), DASHBOARD("Dashboard"), SITELAYOUT("Site Layout"), FILEBIN("File Bin"),
		HELPMANUAL("Help Manual"), MULTILINGUAL("MultiLingual"), NOTIFICATION("Notification"),
		APPLICATIONCONFIGURATION("Application Configuration"), USERMANAGEMENT("User Management"),
		APICLIENTS("API Clients"), SCHEDULER("Scheduler");

		final String moduleName;

		Modules(String moduleName) {
			this.moduleName = moduleName;
		}

		public String getModuleName() {
			return moduleName;
		}
	}

	public enum Changetype {
		SYSTEM("System", 2), CUSTOM("Custom", 1);

		final String	changetype;
		final int		typeInt;

		Changetype(String typeName, int a_typeInt) {
			changetype	= typeName;
			typeInt		= a_typeInt;
		}

		public String getChangetype() {
			return changetype;
		}

		public int getChangeTypeInt() {
			return typeInt;
		}

	}

	public enum Action {
		ADD("ADD"), EDIT("EDIT"), DELETE("DELETE"), OPEN("OPEN"), PERMISSIONACTIVATED("PERMISSION ACTIVATED"),
		PERMISSIONINACTIVATED("PERMISSION INACTIVATED"), SCHEDULEREXECUTIONFAILED("SCHEDULER EXECUTION FAILED"),
		SCHEDULEREXECUTED("SCHEDULER EXECUTED SUCCESSFULLY"), APIEXECUTED("Api Executed"),
		APIEXECFAILED("Api Execution Failed");

		final String action;

		Action(String i) {
			action = i;
		}

		public String getAction() {
			return action;
		}

	}

	public final static String	GRIDUTILS					= "Grid Utils";
	public final static String	TEMPLATING					= "Templating";
	public final static String	DYNAMICFORM					= "Form Builder";
	public final static String	DYNAMICREST					= "REST API Builder";
	public final static String	AUTOCOMPLETE				= "TypeAhead Autocomplete";
	public final static String	DASHBOARD					= "Dashboard";
	public final static String	SITELAYOUT					= "Site Layout";
	public final static String	FILEBIN						= "File Bin";
	public final static String	HELPMANUAL					= "Help Manual";
	public final static String	MULTILINGUAL				= "MultiLingual";
	public final static String	NOTIFICATION				= "Notification";
	public final static String	APPLICATIONCONFIGURATION	= "Application Configuration";
	public final static String	MANAGEROLEMODULES			= "Manage Role Modules";
	public final static String	MANAGEENTITYROLES			= "Manage Entity Roles";
	public final static String	USERMANAGEMENT				= "User Management";
	public final static String	APICLIENTS					= "api-client-details-form";

	public final static Integer	COMMON_MODULE_TYPE_ID		= 1;							// included templates or
																							// anonymous
	// templates
	public final static Integer	DEFAULT_MODULE_TYPE_ID		= 0;							// rest all templates shown
																							// in
	// entityrole listing

	public enum AuthTypeHeaderKey {
		@Deprecated
		INMEMORY(""), DAO("7d1dba821"), LDAP("7d1ldap821"), OAUTH("7d1oa821"), SAML("");

		final String authTypeHeaderKey;

		AuthTypeHeaderKey(String authTypeHeaderKey) {
			this.authTypeHeaderKey = authTypeHeaderKey;
		}

		public String getAuthTypeHeaderKey() {
			return authTypeHeaderKey;
		}
	}

	public static final String	LDAP_ID		= "3";

	public static final String	OAUTH_ID	= "4";

	public static final String	DAO_ID		= "2";

}
