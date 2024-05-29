package com.trigyn.jws.dynamicform.entities;

import java.util.List;
import java.util.Map;

public class HelpManualObjHolder {

	List<ManualEntryDetails> finalManualDataList;
	List<Map<String, String>> formData;
	String searchText;
	private String manualId;

	public List<ManualEntryDetails> getFinalManualDataList() {
		return finalManualDataList;
	}

	public void setFinalManualDataList(List<ManualEntryDetails> finalManualDataList) {
		this.finalManualDataList = finalManualDataList;
	}

	public List<Map<String, String>> getFormData() {
		return formData;
	}

	public void setFormData(List<Map<String, String>> formData) {
		this.formData = formData;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getManualId() {
		return manualId;
	}

	public void setManualId(String manualId) {
		this.manualId = manualId;
	}

}