package com.trigyn.jws.dynamicform.service;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.trigyn.jws.dynamicform.utils.Constant;

public class UniversalRegexGenerator {

	// Database-specific type mappings
	private static final Map<Integer, String> TYPE_MAPPINGS = new HashMap<>();
	static {
		// Character types
		TYPE_MAPPINGS.put(Types.CHAR, "STRING");
		TYPE_MAPPINGS.put(Types.VARCHAR, "STRING");
		TYPE_MAPPINGS.put(Types.LONGVARCHAR, "STRING");
		TYPE_MAPPINGS.put(Types.CLOB, "STRING");

		// Unicode character types
		TYPE_MAPPINGS.put(Types.NCHAR, "STRING");
		TYPE_MAPPINGS.put(Types.NVARCHAR, "STRING");
		TYPE_MAPPINGS.put(Types.LONGNVARCHAR, "STRING");
		TYPE_MAPPINGS.put(Types.NCLOB, "STRING");

		// Binary types
		TYPE_MAPPINGS.put(Types.BINARY, "STRING");
		TYPE_MAPPINGS.put(Types.VARBINARY, "STRING");
		TYPE_MAPPINGS.put(Types.LONGVARBINARY, "STRING");
		TYPE_MAPPINGS.put(Types.BLOB, "STRING");

		// Date & time types
		TYPE_MAPPINGS.put(Types.DATE, "DATE");
		TYPE_MAPPINGS.put(Types.TIME, "TIME");
		TYPE_MAPPINGS.put(Types.TIMESTAMP, "TIMESTAMP");
		TYPE_MAPPINGS.put(Types.TIME_WITH_TIMEZONE, "TIME");
		TYPE_MAPPINGS.put(Types.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMP");
		TYPE_MAPPINGS.put(Types.TIMESTAMP, "DATETIME");
		// Boolean types
		TYPE_MAPPINGS.put(Types.BOOLEAN, "BOOLEAN");
		TYPE_MAPPINGS.put(Types.BIT, "BOOLEAN");

		// Numeric types
		TYPE_MAPPINGS.put(Types.TINYINT, "INTEGER");
		TYPE_MAPPINGS.put(Types.SMALLINT, "INTEGER");
		TYPE_MAPPINGS.put(Types.INTEGER, "INTEGER");
		TYPE_MAPPINGS.put(Types.BIGINT, "INTEGER");

		TYPE_MAPPINGS.put(Types.FLOAT, "DECIMAL");
		TYPE_MAPPINGS.put(Types.REAL, "DECIMAL");
		TYPE_MAPPINGS.put(Types.DOUBLE, "DECIMAL");
		TYPE_MAPPINGS.put(Types.NUMERIC, "DECIMAL");
		TYPE_MAPPINGS.put(Types.DECIMAL, "DECIMAL");

		// Other & advanced SQL types
		TYPE_MAPPINGS.put(Types.ARRAY, "STRING");
		TYPE_MAPPINGS.put(Types.STRUCT, "STRING");
		TYPE_MAPPINGS.put(Types.REF, "STRING");
		TYPE_MAPPINGS.put(Types.REF_CURSOR, "STRING");
		TYPE_MAPPINGS.put(Types.DATALINK, "STRING");
		TYPE_MAPPINGS.put(Types.SQLXML, "STRING");
		TYPE_MAPPINGS.put(Types.ROWID, "STRING");
		TYPE_MAPPINGS.put(Types.JAVA_OBJECT, "STRING");
		TYPE_MAPPINGS.put(Types.DISTINCT, "STRING");
		TYPE_MAPPINGS.put(Types.NULL, "STRING");
		TYPE_MAPPINGS.put(Types.OTHER, "STRING"); // For things like UUID, JSON, custom PG types
	}

	public static String generateRegex(ResultSetMetaData meta, int columnIndex, String productName, String columnName,
			String dataType) throws SQLException {
		String			baseType		= TYPE_MAPPINGS.getOrDefault(meta.getColumnType(columnIndex), "STRING");
		int				precision		= meta.getPrecision(columnIndex);
		int				scale			= meta.getScale(columnIndex);
		boolean			isNullable		= meta.isNullable(columnIndex) == ResultSetMetaData.columnNullable;

		StringBuilder	strictPattern	= new StringBuilder();
		if(Constant.DECIMAL.equalsIgnoreCase(dataType) && Constant.ORACLEDB.equalsIgnoreCase(productName)) {
			strictPattern.append(Constant.DECIMAL_REGEX);
			return strictPattern.toString();
		}
		switch (baseType) {
			case Constant.BASETYPE_STRING:
				int length = getStringLength(meta, columnIndex, productName);
				if ((Constant.UNIQUEID.equalsIgnoreCase(dataType) && Constant.MSSQL.equalsIgnoreCase(productName)) ||  (Constant.UUID.equalsIgnoreCase(dataType) && Constant.POSTGRESQL.equalsIgnoreCase(productName))) {
					strictPattern
							.append(Constant.UNIQUEID_REGEX);
				} else {
					strictPattern.append(".{1,").append(length).append("}");
				}
				break;

			case Constant.BASETYPE_INT:
				long maxValue = getMaxIntegerValue(meta.getColumnType(columnIndex));
				int maxDigits = String.valueOf(Math.abs(maxValue)).length();
				int digits = Math.min(precision > 0 ? precision : maxDigits, maxDigits);
				if (Constant.TINYINT.equalsIgnoreCase(dataType) && Constant.MARIA_DB.equalsIgnoreCase(productName)) {
					strictPattern.append(Constant.TINYINT_REGEX_MARIADB);
				} else if (Constant.TINYINT.equalsIgnoreCase(dataType)
						&& Constant.MSSQL.equalsIgnoreCase(productName)) {
					strictPattern.append(Constant.TINYINT_REGEX_MSSQL);
				} else {
					strictPattern.append("-?\\d{1,").append(digits).append("}");
				}
				break;

			case Constant.BASETYPE_DECIMAL:
				if (precision <= 0)
					precision = 38; // fallback
				if (scale < 0)
					scale = 8;

				int integerDigits = precision - scale;
				if (integerDigits <= 0)
					integerDigits = 1; // prevent invalid {1,0}
				if ((Constant.MSSQL.equalsIgnoreCase(productName)) && Constant.BASETYPE_DECIMAL.equalsIgnoreCase(baseType)) {
					strictPattern.append(Constant.DECIMAL_REGEX);
				} else if((Constant.POSTGRESQL.equalsIgnoreCase(productName)) && Constant.BASETYPE_DECIMAL.equalsIgnoreCase(baseType)) {
					strictPattern.append(Constant.DECIMAL_REGEX_POST_GRES);
				} else {
					strictPattern.append("-?\\d{1,").append(integerDigits).append("}");
					if (scale > 0) {
						strictPattern.append("(?:\\.\\d{1,").append(scale).append("})?");
					}
				}
				break;

			case Constant.BASETYPE_DATE:
			case Constant.BASETYPE_TIMESTAMP:
			case Constant.BASETYPE_DATETIME:
				strictPattern.append(
						"^$|^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$|^\\d{2}-[A-Za-z]+-\\d{4}( \\d{1,2}:\\d{2}( (AM|PM))?)?$");
				break;
			case Constant.BASETYPE_TIME:
				// 24-hour format: HH:mm:ss (e.g., 19:50:00) or 00:00
				strictPattern.append(Constant.TIME_REGEX);
				break;

			case Constant.BASETYPE_BOOLEAN:
				strictPattern.append(Constant.BOOLEAN_REGEX);
				break;

			default:
				strictPattern.append(".*");
		}

		String pattern;
		if (isNullable) {
			// Allow empty string OR strict pattern
			pattern = "^(|" + strictPattern.toString() + ")$";
		} else {
			// Strict pattern only
			pattern = "^" + strictPattern.toString() + "$";
		}

		return pattern;
	}

	/**
	 * 
	 * @param  meta
	 * @param  columnIndex
	 * @param  dbType
	 * @return
	 * @throws SQLException
	 */
	private static int getStringLength(ResultSetMetaData meta, int columnIndex, String dbType) throws SQLException {
		int	displaySize	= meta.getColumnDisplaySize(columnIndex);
		int	precision	= meta.getPrecision(columnIndex);
		int	fallback	= 65535;									// or a sensible UI/business logic limit

		dbType = dbType.toLowerCase();
		boolean	hasDisplaySize	= displaySize > 0;
		boolean	hasPrecision	= precision > 0;

		if (dbType.contains("postgres") || dbType.contains("oracle")) {
			return hasPrecision ? precision : fallback;

		} else if (dbType.contains("sql server")) {
			return hasDisplaySize ? displaySize : (hasPrecision ? precision : fallback);

		} else if (dbType.contains("mariadb") || dbType.contains("mysql")) {
			// Prevent overly large values like 4294967295 (LONGTEXT)
			if (displaySize > 65535) {
				return fallback;
			}
			return hasDisplaySize ? displaySize : (hasPrecision ? precision : fallback);
		}

		return hasDisplaySize ? displaySize : (hasPrecision ? precision : fallback);
	}

	private static long getMaxIntegerValue(int sqlType) {
		switch (sqlType) {
			case Types.TINYINT:
				return Byte.MAX_VALUE; // 127
			case Types.SMALLINT:
				return Short.MAX_VALUE; // 32,767
			case Types.INTEGER:
				return Integer.MAX_VALUE; // 2,147,483,647
			case Types.BIGINT:
				return Long.MAX_VALUE; // 9,223,372,036,854,775,807

			// Approximate maximum representable whole number values
			case Types.FLOAT: // 32-bit float
			case Types.REAL:
			case Types.DOUBLE: // 64-bit float
			case Types.NUMERIC:
			case Types.DECIMAL:
				return Long.MAX_VALUE; // Conservative bound for floating-point/decimal types

			default:
				return Long.MAX_VALUE; // Fallback for unknown or non-integer types
		}
	}

}
