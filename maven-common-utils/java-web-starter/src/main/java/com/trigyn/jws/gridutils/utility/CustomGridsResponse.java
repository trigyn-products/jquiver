package com.trigyn.jws.gridutils.utility;

import java.util.List;
import java.util.Map;

public class CustomGridsResponse {

	private String						page		= null;

	private String						total		= null;

	private String						records		= null;

	private List<Map<String, Object>>	rows		= null;

	private Map							userdata	= null;

	public CustomGridsResponse() {
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

	public Map getUserdata() {
		return userdata;
	}

	public void setUserdata(Map userdata) {
		this.userdata = userdata;
	}
}
