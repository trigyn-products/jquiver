package com.trigyn.jws.gridutils.utility;

public class SearchFields {

	private String	field;
	private String	op;
	private String	data;

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

	public void setField(String field) {
		this.field = field;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
