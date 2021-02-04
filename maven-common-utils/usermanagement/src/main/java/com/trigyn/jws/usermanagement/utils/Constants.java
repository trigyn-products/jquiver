package com.trigyn.jws.usermanagement.utils;

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

	public enum AuthType {
		INMEMORY(1), DAO(2), LDAP(3), OAUTH(4);

		final Integer authType;

		AuthType(Integer authType) {
			this.authType = authType;
		}

		public Integer getAuthType() {
			return authType;
		}
	}

	public enum Modules {

		GRIDUTILS("Grid Utils"), TEMPLATING("Templating"), DYNAMICFORM("Form Builder"), DYNAMICREST("REST API Builder"),
		AUTOCOMPLETE("TypeAhead Autocomplete"), DASHBOARD("Dashboard"), SITELAYOUT("Site Layout");

		final String moduleName;

		Modules(String moduleName) {
			this.moduleName = moduleName;
		}

		public String getModuleName() {
			return moduleName;
		}
	}

	public final static String	GRIDUTILS				= "Grid Utils";
	public final static String	TEMPLATING				= "Templating";
	public final static String	DYNAMICFORM				= "Form Builder";
	public final static String	DYNAMICREST				= "REST API Builder";
	public final static String	AUTOCOMPLETE			= "TypeAhead Autocomplete";
	public final static String	DASHBOARD				= "Dashboard";
	public final static String	SITELAYOUT				= "Site Layout";

	public final static Integer	COMMON_MODULE_TYPE_ID	= 1; // included templates or anonymous templates
	public final static Integer	DEFAULT_MODULE_TYPE_ID	= 0; // rest all templates shown in entityrole listing

}
