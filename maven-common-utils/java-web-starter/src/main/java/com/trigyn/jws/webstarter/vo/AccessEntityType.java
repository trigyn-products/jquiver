package com.trigyn.jws.webstarter.vo;

public enum AccessEntityType {
	TEMPLATE("template-access"),
	AUTOCOMPLETE("autocomplete-access"),
	DYNAMIC_FORM("dynamic-form-access"),
	GRID("grid-access"),
	DASHBOARD("dashboard-access"),
	DASHLET("dashlet-access"),
	DYNAMIC_REST("dynamic-rest-access"),
	SITE_LAYOUT("site-layout-access"),
	ENTITY("entity-access");

	private final String cacheName;

	AccessEntityType(String cacheName) {
		this.cacheName = cacheName;
	}

	public String getCacheName() {
		return cacheName;
	}
}

