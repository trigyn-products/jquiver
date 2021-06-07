package com.trigyn.jws.typeahead.dao;

public final class QueryStore {

	private QueryStore() {

	}

	public static final StringBuilder	MARIADB_QUERY_TO_GET_COLUMN_NAMES	= new StringBuilder(
			"SELECT GROUP_CONCAT(CONCAT(COLUMN_NAME, ' AS ', camel_case(COLUMN_NAME)) SEPARATOR ', ') AS columnName")
					.append(" FROM information_schema.COLUMNS ").append(" WHERE TABLE_NAME = :tableName AND TABLE_SCHEMA = :schemaName ")
					.append(" ORDER BY ORDINAL_POSITION ASC ");

	public static final StringBuilder	MYSQL_QUERY_TO_GET_COLUMN_NAMES		= new StringBuilder(
			"SELECT GROUP_CONCAT(CONCAT(COLUMN_NAME, ' AS ', COLUMN_NAME) SEPARATOR ', ') AS columnName")
					.append(" FROM information_schema.COLUMNS ").append(" WHERE TABLE_NAME = :tableName AND TABLE_SCHEMA = :schemaName ")
					.append(" ORDER BY ORDINAL_POSITION ASC ");

	public static final StringBuilder	SQLSERVER_QUERY_TO_GET_COLUMN_NAMES	= new StringBuilder(
			"SELECT STRING_AGG(CONCAT(COLUMN_NAME, ' AS ', (COLUMN_NAME)) ,', ') WITHIN GROUP (ORDER BY ORDINAL_POSITION ASC) AS columnName ")
					.append(" FROM INFORMATION_SCHEMA.COLUMNS ").append(" WHERE TABLE_NAME  = :tableName AND TABLE_CATALOG = :schemaName ");

	public static final StringBuilder	POSTGRES_QUERY_TO_GET_COLUMN_NAMES	= new StringBuilder(
			"SELECT STRING_AGG(CONCAT(COLUMN_NAME, ' AS ', (COLUMN_NAME)) ,', ' ORDER BY ORDINAL_POSITION ASC) AS \"columnName\" ")
					.append("FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = :tableName and table_catalog = :schemaName");

	public static final StringBuilder	ORACLE_QUERY_TO_GET_COLUMN_NAMES	= new StringBuilder(
			"SELECT table_name, LISTAGG(column_name, ', ') WITHIN GROUP (ORDER BY COLUMN_ID)  columnName")
					.append("FROM USER_TAB_COLUMNS WHERE table_name =:tableName GROUP BY table_name");

}
