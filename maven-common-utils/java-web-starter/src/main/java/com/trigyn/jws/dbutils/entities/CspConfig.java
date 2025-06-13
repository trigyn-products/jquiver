package com.trigyn.jws.dbutils.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CspConfig {

	@JsonProperty("isCSPEnable")
    private boolean isCSPEnable=false;
    private String cspHeader;
    
	public boolean isCSPEnable() {
		return isCSPEnable;
	}
	
	public void setCSPEnable(boolean isCSPEnable) {
		this.isCSPEnable = isCSPEnable;
	}

	public String getCspHeader() {
		return cspHeader;
	}
	public void setCspHeader(String cspHeader) {
		this.cspHeader = cspHeader;
	}

	public CspConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

    
}