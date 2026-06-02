package com.trigyn.jws.dbutils.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trigyn.jws.dynamicform.service.UniversalRegexGenerator;
import com.trigyn.jws.dynamicform.utils.Constant;

public final class DBExtractor {

	private DBExtractor() {
		throw new RuntimeException("You are not supposed to instantiate this java class.");
	}

	public Object readResolve() {
		throw new RuntimeException("You are not supposed to instantiate this java class.");
	}

	public static List<Map<String, Object>> getCols(String a_tableName, Connection con) throws Throwable {
		List<Map<String, Object>>	listOfMap	= new ArrayList<>();
		Map<String, Object>			map			= new HashMap<>();
		String						query		= "";
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

	public static List<Map<String, Object>> getDBStructure(String a_tableName, Connection con) throws SQLException {
		List<Map<String, Object>>	dbStructure	= new ArrayList<Map<String, Object>>();
		List<String>				cols		= new ArrayList<String>();
		Map<String, Object>			dbCol		= null;
		try {

			DatabaseMetaData	dbmd			= con.getMetaData();
			String				productName		= dbmd.getDatabaseProductName();
			String				queryTableName	= a_tableName;
			if (productName.equalsIgnoreCase("postgresql")) {
				queryTableName = "\"" + a_tableName + "\"";
			}
			String				executeQueryString	= "select *  from " + queryTableName;
			ResultSet			rs					= con.createStatement().executeQuery(executeQueryString);
			ResultSetMetaData	rsmd				= rs.getMetaData();
			String				colName				= null;
			for (int iColCounter = 1; iColCounter <= rsmd.getColumnCount(); iColCounter++) {
				dbCol	= new HashMap<String, Object>();
				colName	= rsmd.getColumnName(iColCounter);
				cols.add(colName);
				rsmd.getCatalogName(1);
				dbCol.put("tableColumnName", colName);
				dbCol.put("columnName", colName.replace("_", ""));
				dbCol.put("fieldName", toCamelCase(colName.replace("_", " ")));
				dbCol.put("columnKey", "");
				String	a_dataType	= null;
				String	dataType	= null;
				String	tableSchema	= con.getCatalog();
				// Override CHAR with actual ENUM or SET for MariaDB
				if (Constant.MARIA_DB.equalsIgnoreCase(productName)
						&& "CHAR".equalsIgnoreCase(rsmd.getColumnTypeName(iColCounter))) {

					String		queryString	= "SELECT DATA_TYPE,COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS "
							+ "WHERE TABLE_SCHEMA = '" + tableSchema + "' " + "AND TABLE_NAME = '" + queryTableName
							+ "' " + "AND COLUMN_NAME = '" + colName + "'";

					Statement	queryStmt	= con.createStatement();
					ResultSet	rsInfo		= queryStmt.executeQuery(queryString);
					if (rsInfo.next()) {
						String	actualDataType	= rsInfo.getString("DATA_TYPE");
						String	fullColumnType	= rsInfo.getString("COLUMN_TYPE");	// e.g., enum('a','b','c')
						if (actualDataType != null && (actualDataType.equalsIgnoreCase(Constant.ENUM)
								|| actualDataType.equalsIgnoreCase(Constant.SET))) {
							a_dataType = actualDataType;
							// Extract enum/set values and add to map
							List<String>	dbValues	= new ArrayList<>();
							// This line removes the prefix enum( or set( and the suffix ) from the
							// fullColumnType string.
							String			values		= fullColumnType.replaceAll("^(enum|set)\\(|\\)$", "");
							for (String val : values.split(",")) {
								dbValues.add(val.replace("'", "").trim());
							}
							dbCol.put("dbValues", dbValues); // this will be used in FTL
						}
					}
					rsInfo.close();
					queryStmt.close();
				} else if (Constant.POSTGRESQL.equalsIgnoreCase(productName)
						&& "money".equalsIgnoreCase(rsmd.getColumnTypeName(iColCounter))) {

					String		schemaName	= Constant.SCHEMA_POSTGRES;												// Usually
					String		columnName	= colName.toLowerCase();

					String		queryString	= "SELECT column_name, data_type FROM information_schema.columns "
							+ "WHERE table_schema = '" + schemaName + "' " + "AND table_name = '" + queryTableName.toLowerCase().replace("\"", "") + "' "
							+ "AND column_name = '" + columnName + "'";

					Statement	queryStmt	= con.createStatement();
					ResultSet	rsInfo		= queryStmt.executeQuery(queryString);
					if (rsInfo.next()) {
						String	actualDataType	= rsInfo.getString("data_type");
						if (actualDataType != null && actualDataType.equalsIgnoreCase(Constant.MONEY)) {
							a_dataType = actualDataType;
						}
					}
					rsInfo.close();
					queryStmt.close();
				}
				dataType = getDataType(rsmd.getColumnTypeName(iColCounter), productName, a_dataType);
				String columnType = null;
				if (dataType != null) {
					columnType = getDBType(dataType, rsmd.getColumnType(iColCounter),
							rsmd.getColumnDisplaySize(iColCounter), rsmd.getColumnTypeName(iColCounter), productName);
				}

				if (null == dataType || null == columnType) {
					dbCol.put("dataType", rsmd.getColumnTypeName(iColCounter));
					dbCol.put("_unsupported", true);
				} else {
					dbCol.put("dataType", dataType);
				}
				dbCol.put("columnType", columnType);
				dbCol.put("columnSize", rsmd.getColumnDisplaySize(iColCounter));
				dbCol.put("isMandatory", rsmd.isNullable(iColCounter) == 0);
				dbCol.put("precision", rsmd.getPrecision(iColCounter));
				dbCol.put("autoIncrement", rsmd.isAutoIncrement(iColCounter));
				if (null != dataType || null != columnType) {
					dbCol.put("regexValidation",
							UniversalRegexGenerator.generateRegex(rsmd, iColCounter, productName, colName, dataType));
				}

				dbStructure.add(dbCol);
			}

			String	catalogName	= rsmd.getCatalogName(1);
			String	schemaName	= rsmd.getSchemaName(1);

			if (Constant.POSTGRESQL.equalsIgnoreCase(productName)) {
				if (catalogName.isBlank()) {
					catalogName = null;
				}
				if (schemaName.isBlank()) {
					schemaName = "public";
				}
			}

			// Microsoft JDBC Driver 12.6 for SQL Server
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

	private static String getDataType(String a_colTypeName, String productName, String data_type) {
		String colType = a_colTypeName.toUpperCase();
		// Special case for Oracle's LONG type
		if (Constant.LONG.equals(colType) && Constant.ORACLEDB.equalsIgnoreCase(productName)) {
			return "text";
		} else if (Constant.BASETYPE_TIMESTAMP.equalsIgnoreCase(colType)
				&& Constant.MSSQL.equalsIgnoreCase(productName)) {
			return null;
		} else if (Constant.XMLCOL.equalsIgnoreCase(colType) && (Constant.MSSQL.equalsIgnoreCase(productName)
				|| Constant.POSTGRESQL.equalsIgnoreCase(productName))) {
			return Constant.XMLCOL;
		} else if (Constant.UNIQUEID.equalsIgnoreCase(colType) && Constant.MSSQL.equalsIgnoreCase(productName)) {
			return Constant.UNIQUEID;
		} else if (Constant.UUID.equalsIgnoreCase(colType) && Constant.POSTGRESQL.equalsIgnoreCase(productName)) {
			return Constant.UUID;
		} else if ((Constant.JSON.equalsIgnoreCase(colType) || Constant.JSONB.equalsIgnoreCase(colType))
				&& Constant.POSTGRESQL.equalsIgnoreCase(productName)) {
			return Constant.JSON;
		} else if (Constant.ENUM.equalsIgnoreCase(data_type) && Constant.MARIA_DB.equalsIgnoreCase(productName)) {
			return Constant.ENUM;
		} else if (Constant.SET.equalsIgnoreCase(data_type) && Constant.MARIA_DB.equalsIgnoreCase(productName)) {
			return Constant.SET;
		} else if (Constant.MONEY.equalsIgnoreCase(data_type) && Constant.POSTGRESQL.equalsIgnoreCase(productName)) {
			return null;
		} else if (Constant.TINYINT.equalsIgnoreCase(colType) && (Constant.MARIA_DB.equalsIgnoreCase(productName) || Constant.MSSQL.equalsIgnoreCase(productName))) {
			return Constant.TINYINT;
		}
		switch (colType) {
			// Text types
			case "BPCHAR":
			case "CHARACTER":
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
			case "JSON":
			case "ENUM":
			case "NTEXT":
			case "XML":
			case "UNIQUEIDENTIFIER":
			case "UUID":
			case "CLOB":
			case "NCLOB":
				return "text";
			case "INTEGER":
			case "INT":
			case "INT1":
			case "INT2":
			case "INT3":
			case "INT4":
			case "INT8":
			case "SMALLINT":
			case "BIGINT":
			case "LONG":
			case "TINYINT":
			case "SERIAL":
			case "BIGSERIAL":
				return "int";
			case "DECIMAL":
			case "NUMERIC":
			case "NUMBER":
			case "FLOAT":
			case "FLOAT4":
			case "FLOAT8":
			case "DOUBLE":
			case "DOUBLE PRECISION":
			case "REAL":
			case "BINARY_FLOAT":
			case "BINARY_DOUBLE":
			case "MONEY":
			case "SMALLMONEY":
				return "decimal";
			case "BOOLEAN":
			case "BOOL":
			case "BIT":
				return "boolean";
			case "DATE":
				return "date";
			case "TIME":
			case "TIME WITHOUT TIME ZONE":
			case "TIME WITH TIME ZONE":
			case "TIMETZ":
				return "time";
			case "DATETIME":
			case "TIMESTAMP":
			case "TIMESTAMP WITH TIME ZONE":
			case "TIMESTAMP_WITH_TIMEZONE":
			case "TIMESTAMP_WITH_LOCAL_TIMEZONE":
			case "TIMESTAMPTZ":
			case "SMALLDATETIME":
			case "DATETIME2":
				return "datetime";
			// Binary types
			case "BINARY":
			case "VARBINARY":
			case "IMAGE":
			case "ROWVERSION": // alias for TIMESTAMP in SQL Server
				return "binary";
			case "DATETIMEOFFSET":
				return "datetimeoffset";

		}

		return null;
	}

	private static String getDBType(String dataType, int a_colType, int a_colLength, String a_colTypeName,
			String productName) {
		dataType = dataType.toLowerCase();
		if (Constant.XMLCOL.equalsIgnoreCase(dataType) && (Constant.MSSQL.equalsIgnoreCase(productName)
				|| Constant.POSTGRESQL.equalsIgnoreCase(productName))) {
			return Constant.XMLCOL;
		} else if (Constant.UUID.equalsIgnoreCase(dataType) && Constant.POSTGRESQL.equalsIgnoreCase(productName)) {
			return Constant.UUID;
		} else if ((Constant.JSON.equalsIgnoreCase(dataType) || Constant.JSONB.equalsIgnoreCase(dataType))
				&& Constant.POSTGRESQL.equalsIgnoreCase(productName)) {
			return Constant.JSON;
		} else if (Constant.TINYINT.equalsIgnoreCase(dataType) && (Constant.MARIA_DB.equalsIgnoreCase(productName) || Constant.MSSQL.equalsIgnoreCase(productName))) {
			return Constant.TINYINT;
		}
		switch (dataType) {
			case "text": {
				switch (a_colType) {
					case Types.BLOB:
					case Types.CLOB:
					case Types.CHAR:
					case Types.NCHAR:
					case Types.VARCHAR:
					case Types.NVARCHAR:
					case Types.BINARY:
					case Types.VARBINARY:
					case Types.LONGVARBINARY:
					case Types.ARRAY:
					case Types.NCLOB:
					case Types.SQLXML:
					case Types.ROWID:
					case Types.OTHER:
						if (a_colLength > 499) {
							return "textarea";
						} else {
							return "text";
						}
					case Types.LONGVARCHAR:
					case Types.LONGNVARCHAR:
						return "textarea";
				}
			}

			case "int":
				switch (a_colType) {
					case Types.INTEGER:
					case Types.BIGINT:
					case Types.SMALLINT:
					case Types.SQLXML:
					case Types.OTHER:
						return "int";
					case Types.TINYINT:
						if (a_colLength > 1) {
							return "int";
						} else {
							return "boolean";
						}
				}
			case "decimal":
				switch (a_colType) {
					case Types.DECIMAL:
					case Types.NUMERIC:
					case Types.DOUBLE:
					case Types.FLOAT:
					case Types.REAL:
					case Types.BINARY:
					case Types.OTHER:
						return "decimal";
					default:
						if ("BINARY_DOUBLE".equalsIgnoreCase(a_colTypeName)
								|| "BINARY_FLOAT".equalsIgnoreCase(a_colTypeName)) {
							return "decimal";
						}
				}
			case "boolean":
				switch (a_colType) {
					case Types.BOOLEAN:
					case Types.TINYINT:
						if (a_colLength > 1) {
							return "int";
						} else {
							return "boolean";
						}
					case Types.BIT:
					case Types.OTHER:
						return "boolean";
				}
			case "date":
				switch (a_colType) {
					case Types.DATE:
					case Types.OTHER:
						return "date";
				}
			case "time":
				switch (a_colType) {
					case Types.TIME:
					case Types.TIME_WITH_TIMEZONE:
					case Types.OTHER:
						return "time";
				}
			case "datetime":
				switch (a_colType) {
					case Types.DATE:
					case Types.TIMESTAMP:
					case Types.TIME_WITH_TIMEZONE:
					case Types.TIMESTAMP_WITH_TIMEZONE:
					case Types.OTHER:
						return "datetime";
				}
			case "datetimeoffset":
				switch (a_colType) {
					case Constant.MSSQL_DATETIMEOFFSET:
					case Types.TIMESTAMP_WITH_TIMEZONE:
					case Types.OTHER:
						return "datetime";
				}
			case "xml":
				switch (a_colType) {
					case Constant.XML:
					case Types.OTHER:
						return "xml";
				}
			case Constant.UNIQUEID:
				switch (a_colType) {
					case Constant.UNIQUEIDENTIFIER:
					case Types.OTHER:
						return Constant.UNIQUEID;
				}
			case Constant.ENUM:
				switch (a_colType) {
					case Constant.ENUM_COLTYPE:
					case Types.OTHER:
						return Constant.ENUM;
				}
			case Constant.SET:
				switch (a_colType) {
					case Constant.SET_COLTYPE:
					case Types.OTHER:
						return Constant.SET;
				}
			default:
				return null;
		}
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

	public static String getCastExpression(String dataType, String columnName, String formFieldName,
			boolean showColumnName, String columnType, String dbProductName) {

		String	param				= ":" + formFieldName;
		String	expr				= param;

		boolean	isPostgres			= dbProductName.contains(Constant.POSTGRESQL);
		boolean	isMSSQL				= dbProductName.contains(Constant.MSSQLSERVER);
		boolean	isOracle			= dbProductName.contains(Constant.ORACLE);
		boolean	isMySQL				= dbProductName.contains(Constant.DEFAULT)
				|| dbProductName.contains(Constant.MARIADB);

		// Default null-safe parameter for most databases
		String	nullSafeParam		= String.format("NULLIF(%s, null)", param);

		// PostgreSQL-specific null-safe + type-safe pattern
		String	pgNullIntCast		= String.format("COALESCE(NULLIF(%s::text, '')::int, NULL)", param);
		String	pgNullDecimalCast	= String.format("COALESCE(NULLIF(%s::text, '')::numeric, NULL)", param);
		String	pgNullBoolCast		= String.format("COALESCE(NULLIF(%s::text, '')::boolean, NULL)", param);
		String	pgNullJsonCast		= String.format("COALESCE(NULLIF(%s::text, '')::json, NULL)", param);
		String	pgNullXmlCast		= String.format("COALESCE(NULLIF(%s::text, '')::xml, NULL)", param);
		String	pgNullUuidCast		= String.format("COALESCE(NULLIF(%s::text, '')::uuid, NULL)", param);

		switch (dataType.toLowerCase()) {
			case "money":
				if (isPostgres) {
					expr = String.format("CAST(%s AS money)", pgNullDecimalCast);
				} else if (isMSSQL) {
					expr = String.format("CONVERT(money, %s)", nullSafeParam);
				} else if (isMySQL) {
					expr = String.format("CAST(%s AS DECIMAL(19,2))", nullSafeParam);
				}
				break;

			case "time":
				if (isPostgres) {
					expr = String.format("NULLIF(%s::text, '')::time", param);
				} else if (isMSSQL) {
					expr = String.format("CONVERT(time, %s)", nullSafeParam);
				} else if (isOracle) {
					expr = String.format("TO_TIMESTAMP(%s, 'HH24:MI:SS')", nullSafeParam);
				} else if (isMySQL) {
					expr = String.format("STR_TO_DATE(%s, '%%H:%%i:%%s')", nullSafeParam);
				}
				break;

			case "boolean":
				if (isPostgres) {
					expr = pgNullBoolCast;
				} else if (isMySQL) {
					expr = String.format("CAST(NULLIF(%s, '') AS UNSIGNED)", param);
				} else if (isMSSQL) {
					expr = String.format("CAST(%s AS bit)", nullSafeParam);
				} else if (isOracle) {
					expr = String.format("CASE WHEN %s = 'true' THEN 1 ELSE 0 END", param);
				}
				break;

			case "date":
			case "datetime":
				if ("hidden".equalsIgnoreCase(columnType)) {
					if (isMSSQL) {
						expr = "GETDATE()";
					} else if (isOracle) {
						expr = "SYSDATE";
					} else {
						expr = "NOW()";
					}
				} else {
					if (isPostgres || isOracle) {
						expr = String.format("TO_DATE(NULLIF(%s::text, ''), 'DD-MONTH-YYYY')", param);
					} else if (isMSSQL) {
						expr = String.format("CONVERT(datetime, %s)", nullSafeParam);
					} else if (isMySQL) {
						expr = String.format("STR_TO_DATE(%s, '%%d-%%M-%%Y')", nullSafeParam);
					}
				}
				break;

			case "int":
				if (isPostgres) {
					expr = pgNullIntCast;
				} else if (isMySQL) {
					expr = String.format("COALESCE(NULLIF(TRIM(%s), ''), NULL)", param);
				}
				break;
				
			case "tinyint":
				if (isMySQL) {
					expr = String.format("COALESCE(NULLIF(TRIM(%s), ''), NULL)", param);
				}
				break;

			case "decimal":
				if (isPostgres) {
					expr = pgNullDecimalCast;
				} else if (isMySQL) {
					expr = String.format("COALESCE(NULLIF(TRIM(%s), ''), NULL)", param);
				}
				break;
			case "json":
			case "jsonb":
				if (isPostgres) {
					expr = pgNullJsonCast;
				}
				break;

			case "xml":
				if (isPostgres) {
					expr = pgNullXmlCast;
				}
				break;

			case "uuid":
				if (isPostgres) {
					expr = pgNullUuidCast;
				}
				break;

			default:
				expr = param;
				break;
		}

		return showColumnName ? String.format("%s = %s", columnName, expr) : expr;
	}

}
