package com.trigyn.jws.typeahead.utility;

import java.util.HashMap;
import java.util.Map;

import com.trigyn.jws.typeahead.dao.QueryStore;

public final class Constant {

	public static final Integer			MASTER_SOURCE_VERSION_TYPE			= 1;

	public static final String			DEFAULT_DRIVER_CLASS_NAME			= "org.mariadb.jdbc.Driver";

	private static Map<String, String>	AUTOCOMPLETE_QUERY_GENERATION_MAP	= new HashMap<String, String>();

	public static final Map<String, String> getGenerationQuery() {
		AUTOCOMPLETE_QUERY_GENERATION_MAP.put("org.postgresql.Driver", QueryStore.POSTGRES_QUERY_TO_GET_COLUMN_NAMES.toString());
		AUTOCOMPLETE_QUERY_GENERATION_MAP.put("com.mysql.jdbc.Driver", QueryStore.MYSQL_QUERY_TO_GET_COLUMN_NAMES.toString());
		AUTOCOMPLETE_QUERY_GENERATION_MAP.put("org.mariadb.jdbc.Driver", QueryStore.MARIADB_QUERY_TO_GET_COLUMN_NAMES.toString());
		AUTOCOMPLETE_QUERY_GENERATION_MAP.put("com.microsoft.sqlserver.jdbc.SQLServerDriver",
				QueryStore.SQLSERVER_QUERY_TO_GET_COLUMN_NAMES.toString());
		AUTOCOMPLETE_QUERY_GENERATION_MAP.put("oracle.jdbc.driver.OracleDriver", QueryStore.ORACLE_QUERY_TO_GET_COLUMN_NAMES.toString());
		return AUTOCOMPLETE_QUERY_GENERATION_MAP;
	}
}
