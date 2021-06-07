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

	private static Map<String, String> LIMIT_CLAUSE_MAP = new HashMap<String, String>();

	public static final Map<String, String> getLimitClause() {
		LIMIT_CLAUSE_MAP.put("postgresql", " OFFSET ? LIMIT ? ");
		LIMIT_CLAUSE_MAP.put("mysql", " LIMIT ?, ? ");
		LIMIT_CLAUSE_MAP.put("mariadb", " LIMIT ? ? ");
		LIMIT_CLAUSE_MAP.put("sqlserver", " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
		LIMIT_CLAUSE_MAP.put("oracle:thin", " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
		return LIMIT_CLAUSE_MAP;
	}
}