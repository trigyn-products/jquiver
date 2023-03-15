package com.trigyn.jws.gridutils.utility;

public class SearchFields implements Cloneable{

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
}
