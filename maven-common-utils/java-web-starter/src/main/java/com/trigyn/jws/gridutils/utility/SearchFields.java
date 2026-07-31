package com.trigyn.jws.gridutils.utility;

import com.nimbusds.oauth2.sdk.util.StringUtils;

public class SearchFields implements Cloneable{

	private String	field;
	private String	op;
	private String	data;
	private String logicalOp;
	private String type;

	public SearchFields() {
		super();
	}

	public SearchFields(String fields, String op, String data) {
		super();
		this.field	= fields;
		this.op		= op;
		this.data	= data;
	}
	
	public String getField() {
		return field;
	}

	public String getField(String field, String dbProductName) {
		return getSearchFieldName(field, dbProductName);
	}

	private String getSearchFieldName(String fieldName, String dbProductName) {
		if(fieldName != null && fieldName.contains(" ")) {
			if (StringUtils.isBlank(dbProductName) == false && (dbProductName.equals("postgresql") == true || dbProductName.equals("oracle") == true)) {
				fieldName = "\"" + fieldName + "\"";
			} else if (StringUtils.isBlank(dbProductName) == false && (dbProductName.equals("mysql") == true || dbProductName.equals("mariadb") == true)) {
				fieldName = "`" + fieldName + "`";
			} else if (StringUtils.isBlank(dbProductName) == false && dbProductName.equals("sqlserver") == true) {
				fieldName = "[" + fieldName + "]";
			} else {
				fieldName = "`" + fieldName + "`";
			}
		}
		return fieldName;
	}
	
	public SearchFields setField(String field) {
		this.field = field;
		return this;
	}

	public String getOp() {
		return op;
	}

	public SearchFields setOp(String op) {
		this.op = op;
		return this;
	}

	public String getData() {
		return data;
	}

	public SearchFields setData(String data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		if(field == null) {
			return  data;
		}
		if(data == null) {
			return  field;
		}
		return field + " " + op + " " + data;
	}
	
	@Override
	public SearchFields clone() throws CloneNotSupportedException {
		return new SearchFields(field, op, data);
	}

	public String getLogicalOp() {
		return logicalOp;
	}

	public void setLogicalOp(String logicalOp) {
		this.logicalOp = logicalOp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}