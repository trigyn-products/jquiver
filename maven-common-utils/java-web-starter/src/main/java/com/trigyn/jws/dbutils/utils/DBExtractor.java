package com.trigyn.jws.dbutils.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public final class DBExtractor {

	private DBExtractor() {
		throw new RuntimeException("You are not supposed to instantiate this java class.");
	}

	public Object readResolve() {
		throw new RuntimeException("You are not supposed to instantiate this java class.");
	}

	public static List<Map<String, Object>> getCols(String a_tableName, DataSource dataSource) throws Throwable {
		List<Map<String, Object>>	listOfMap	= new ArrayList<>();
		Map<String, Object>			map			= new HashMap<>();
		String						query		= "";
		Connection					con			= dataSource.getConnection();
		ResultSet					rs			= con.createStatement().executeQuery("select *  from " + a_tableName);
		ResultSetMetaData			rsmd		= rs.getMetaData();
		String						colName		= null;
		for (int iColCounter = 1; iColCounter <= rsmd.getColumnCount(); iColCounter++) {
			colName = rsmd.getColumnName(iColCounter);
			if (iColCounter > 1) {
				query += ",";
			}
			query += colName + " AS " + colName;
		}
		map.put("columnName", query);
		listOfMap.add(map);
		return listOfMap;
	}

	public static List<Map<String, Object>> getDBStructure(String a_tableName, DataSource dataSource)
			throws SQLException {
		List<Map<String, Object>>	dbStructure	= new ArrayList<Map<String, Object>>();
		List<String>				cols		= new ArrayList<String>();
		Map<String, Object>			dbCol		= null;
		try {

			Connection			con		= dataSource.getConnection();
			DatabaseMetaData	dbmd	= con.getMetaData();
			String productName = dbmd.getDatabaseProductName();
			String queryTableName = a_tableName;
			if(productName.equalsIgnoreCase("postgresql")) {
				queryTableName = "\"" + a_tableName + "\"";		
			}
			String executeQueryString = "select *  from " + queryTableName;
			ResultSet			rs		= con.createStatement().executeQuery(executeQueryString);
			ResultSetMetaData	rsmd	= rs.getMetaData();
			

			String				colName	= null;
			for (int iColCounter = 1; iColCounter <= rsmd.getColumnCount(); iColCounter++) {
				dbCol	= new HashMap<String, Object>();
				colName	= rsmd.getColumnName(iColCounter);
				cols.add(colName);
				rsmd.getCatalogName(1);
				dbCol.put("tableColumnName", colName);
				dbCol.put("columnName", colName.replace("_", ""));
				dbCol.put("fieldName", toCamelCase(colName.replace("_", " ")));

				dbCol.put("columnKey", "");
				String dataType = getDataType(rsmd.getColumnTypeName(iColCounter));
				if (dataType == null) {
					dbCol.put("dataType", rsmd.getColumnTypeName(iColCounter));
					dbCol.put("_unsupported", true);
				} else {
					dbCol.put("dataType", dataType);
				}

				dbCol.put("columnType",
						getDBType(rsmd.getColumnType(iColCounter), rsmd.getColumnDisplaySize(iColCounter)));
				dbCol.put("columnSize", rsmd.getColumnDisplaySize(iColCounter));
				dbCol.put("isMandatory", rsmd.isNullable(iColCounter) == 0);
				dbCol.put("precision", rsmd.getPrecision(iColCounter));
				dbCol.put("autoIncrement", rsmd.isAutoIncrement(iColCounter));

				dbStructure.add(dbCol);
			}

			String	catalogName	= rsmd.getCatalogName(1);
			String	schemaName	= rsmd.getSchemaName(1);

			if ((dbmd.getDriverName().contains("Microsoft") && dbmd.getDriverName().contains("SQL Server"))
					|| dbmd.getDriverName().contains("Oracle")) {
				catalogName	= null;
				schemaName	= null;
			}
			ResultSet rsPK = dbmd.getPrimaryKeys(catalogName, schemaName, a_tableName);
			while (rsPK.next()) {
				colName = rsPK.getString("COLUMN_NAME");
				dbStructure.get(cols.indexOf(colName)).put("columnKey", "PK");// instead of PRI please use PK
			}
		} catch (Throwable a_th) {
			a_th.printStackTrace();
		}

		return dbStructure;
	}

	private static String getDataType(String a_colTypeName) {
		switch (a_colTypeName.toUpperCase()) {
			case "CHAR":
			case "VARCHAR":
			case "VARCHAR2":
			case "NVARCHAR":
			case "NCHAR":
			case "NVARCHAR2":
			case "LONGVARCHAR":
			case "LONGNVARCHAR":
			case "CHARACTER VARYING":
			case "TEXT":
			case "TINYTEXT":
			case "LONGTEXT":
			case "MEDIUMTEXT":	
			case "UUID":	
				return "text";
			case "INTEGER":
			case "INT1":
			case "INT2":
			case "INT3":	
			case "INT4":
			case "INT8":	
			case "INT":	
			case "SMALLINT":
			case "BIGINT":	
			case "LONG":
			case "TINYINT":
			case "SERIAL" :	
				return "int";
			case "DECIMAL":
			case "NUMERIC":
			case "NUMBER":
			case "FLOAT":
			case "DOUBLE":
			case "BINARY_FLOAT":
			case "BINARY_DOUBLE":
			case "REAL":
			case "FLOAT8":
			case "DOUBLE PRECISION":	
				return "decimal";
			case "BOOLEAN" :
			case "BIT":
				return "boolean";
			case "DATE":
			case "TIME":	
			case "DATETIME":
			case "TIMESTAMP":
			case "TIMESTAMP_WITH_TIMEZONE":
			case "TIMESTAMP_WITH_LOCAL_TIMEZONE":
				return "date";
				
		}
		// Decimal, Float, boolean, bit, tinyint and all are pending
		return null;
	}

	private static String getDBType(int a_colType, int a_colLength) {
		
		switch (a_colType) {
			case Types.CHAR:
			case Types.NCHAR:
			case Types.VARCHAR:
			case Types.NVARCHAR:
				if (a_colLength > 499) {
					return "textarea";
				}else {
					return "text";
				}
			case Types.LONGVARCHAR:
			case Types.LONGNVARCHAR:
				return "textarea";
			case Types.INTEGER:
			case Types.SMALLINT:
				return "int";
			case Types.TINYINT:
				if (a_colLength > 1) {
					return "int";
				}
				else {
					return "boolean";
				}
			case Types.DECIMAL:
			case Types.NUMERIC:
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.REAL:	
				return "decimal";
			case Types.BOOLEAN:
			case Types.BIT:	
				return "boolean";
			case Types.DATE:
			case Types.TIMESTAMP:
			case Types.TIMESTAMP_WITH_TIMEZONE:
				return "date";	
		}
		// boolean, bit, and all are pending
		return null;
	}

	private static String toCamelCase(String a_input) {
		String	returnString	= "";
		boolean	isFirstChar		= true;
		for (char c : a_input.toCharArray()) {
			if (isFirstChar) {
				returnString	+= Character.toUpperCase(c);
				isFirstChar		= false;
			} else {
				returnString += c;
			}
			if (c == ' ') {
				isFirstChar = true;
			}
		}
		return returnString;
	}

}
