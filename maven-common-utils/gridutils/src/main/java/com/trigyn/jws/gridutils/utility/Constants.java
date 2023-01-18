package com.trigyn.jws.gridutils.utility;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

	public static final String	DEFAULT_GRID_TEMPLATE_NAME	= "default-grid-template";

	public static final Integer	CUSTOM_GRID					= 1;
	public static final Integer	SYSTEM_GRID					= 2;

	public enum queryImplementationType {
		VIEW(1), STORED_PROCEDURE(2);

		final int type;

		queryImplementationType(int i) {
			type = i;
		}

		public int getType() {
			return type;
		}
	}
	
	public enum Modules {

		GRIDUTILS("Grid Utils"), TEMPLATING("Templating"), DYNAMICFORM("Form Builder"), DYNAMICREST("REST API Builder"),
		AUTOCOMPLETE("TypeAhead Autocomplete"), DASHBOARD("Dashboard"), SITELAYOUT("Site Layout"), FILEBIN("File Bin"),
		HELPMANUAL("Help Manual"),MASTERGENERATOR("Master Generator");

		final String moduleName;

		Modules(String moduleName) {
			this.moduleName = moduleName;
		}

		public String getModuleName() {
			return moduleName;
		}
	}
	public enum Changetype{
		SYSTEM("System"),
		CUSTOM("Custom");
		
		final String changetype;
		
		Changetype(String i) {
			changetype = i;
		} 

		public String getChangetype() {
			return changetype;
		}

		}
	public enum Action{
		ADD("ADD"),
		EDIT("EDIT"),
		DELETE("DELETE"),
		OPEN("OPEN");
		
		final String action;
		
		Action(String i) {
			action = i;
		}

		public String getAction() {
			return action;
		}

		}

	private static Map<String, String> LIMIT_CLAUSE_MAP = new HashMap<String, String>();

	public static final Map<String, String> getLimitClause() {
		LIMIT_CLAUSE_MAP.put("postgresql", " OFFSET ? LIMIT ? ");
		LIMIT_CLAUSE_MAP.put("mysql", " LIMIT ?, ? ");
		LIMIT_CLAUSE_MAP.put("mariadb", " LIMIT ?, ? ");
		LIMIT_CLAUSE_MAP.put("sqlserver", " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
		LIMIT_CLAUSE_MAP.put("oracle:thin", " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
		return LIMIT_CLAUSE_MAP;
	}
}