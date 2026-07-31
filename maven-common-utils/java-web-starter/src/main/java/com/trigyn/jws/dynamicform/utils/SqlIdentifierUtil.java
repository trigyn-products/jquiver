package com.trigyn.jws.dynamicform.utils;

import org.springframework.stereotype.Component;

@Component
public class SqlIdentifierUtil {

	public SqlIdentifierUtil() {
	}

	/**
	 * Quotes the identifier only when required. Existing identifiers like emp_id,
	 * employee remain unchanged. Identifiers like "emp id" or "Employee Details"
	 * are quoted.
	 */
	public String quote(String identifier, String dbProductName) {

		if (identifier == null || identifier.isBlank()) {
			return identifier;
		}
		identifier = identifier.trim();
		String db = "";

		if (dbProductName != null) {
			db = dbProductName.trim().replace("[", "").replace("]", "").replace("`", "").replace("\"", "")
					.toLowerCase();
		}
		if (db.contains("postgres")) {
			return "\"" + identifier + "\"";
		}
		if (db.contains("oracle")) {
			return "\"" + identifier + "\"";
		}
		if (db.contains("mysql") || db.contains("mariadb")) {
			return identifier.contains(" ") ? "`" + identifier + "`" : identifier;
		}
		if (db.contains("sql server") || db.contains("mssql") || db.equalsIgnoreCase("sqlserver")) {
			return identifier.contains(" ") ? "[" + identifier + "]" : identifier;
		}
		return identifier.contains(" ") ? "`" + identifier + "`" : identifier;
	}

	/**
	 * Returns true if identifier needs quoting.
	 */
	public boolean requiresQuoting(String identifier) {
		return identifier != null && !identifier.isBlank() && identifier.trim().contains(" ");
	}

	public String quoteIfRequired(String identifier, String dbProductName) {

		if (identifier == null || identifier.isBlank()) {
			return identifier;
		}
		identifier = identifier.trim();
		// Already quoted with `
		if (identifier.startsWith("`") && identifier.endsWith("`")) {
			return identifier;
		}
		// Already quoted with "
		if (identifier.startsWith("\"") && identifier.endsWith("\"")) {
			return identifier;
		}
		// Already quoted with []
		if (identifier.startsWith("[") && identifier.endsWith("]")) {
			return identifier;
		}

		return quote(identifier, dbProductName);
	}

	public String toJsProperty(String columnName) {

		if (columnName == null || columnName.isBlank()) {
			return "";
		}

		// Valid JS identifier
		if (columnName.matches("[A-Za-z_$][A-Za-z0-9_$]*")) {
			return "." + columnName;
		}

		// Contains spaces/special chars
		return "[\"" + columnName + "\"]";
	}

}