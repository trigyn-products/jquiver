package com.trigyn.jws.dynarest.spi;

public class DynarestDetails {

	private String dynarestUrl = null;

	public DynarestDetails(String dynarestUrl) {
		super();
		this.dynarestUrl = dynarestUrl;
	}

	public String getDynarestUrl() {
		return dynarestUrl;
	}

	public void setDynarestUrl(String dynarestUrl) {
		this.dynarestUrl = dynarestUrl;
	}

}
