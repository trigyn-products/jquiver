package com.trigyn.jws.gridutils.utility;

import java.util.List;
import java.util.Map;

public class DataGridResponse {
	private String						total	= null;
	private String						curPage	= null;
	private List<Map<String, Object>>	rows	= null;

	public DataGridResponse(List<Map<String, Object>> list, Integer total, Integer currentPage) {
		this.rows		= list;
		this.total		= total + "";
		this.curPage	= currentPage + "";
	}

	public DataGridResponse(List<Map<String, Object>> list, Integer total, Map<String, String> userData) {
		this.rows	= list;
		this.total	= total + "";
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

	public String getCurPage() {
		return curPage;
	}

	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}

}
