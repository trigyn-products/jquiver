package com.trigyn.jws.usermanagement.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.utils.Constants;

@Component
public class EntityValidatorFactory {

	@Autowired
	private DynamicFormEntityValidator	dynamicEntityValidator		= null;

	@Autowired
	private GridEntityValidator			gridEntityValidator			= null;

	@Autowired
	private TemplateEntityValidator		templateEntityValidator		= null;

	@Autowired
	private DynamicRestEntityValidator	dynamicRestEntityValidator	= null;

	@Autowired
	private DashboardEntityValidator	dashboardEntityValidator	= null;

	@Autowired
	private AutocompleteEntityValidator	autocompleteEntityValidator	= null;

	@Autowired
	private SiteLayoutEntityValidator	siteLayoutEntityValidator	= null;

	@Autowired
	private FileBinEntityValidator		fileBinEntityValidator		= null;

	@Autowired
	private HelManualEntityValidator	helManualEntityValidator	= null;

	public EntityValidator createEntityValidator(String entityType) {

		switch (entityType) {
			case Constants.DYNAMICFORM:
				return dynamicEntityValidator;

			case Constants.GRIDUTILS:
				return gridEntityValidator;

			case Constants.TEMPLATING:
				return templateEntityValidator;

			case Constants.AUTOCOMPLETE:
				return autocompleteEntityValidator;

			case Constants.DASHBOARD:
				return dashboardEntityValidator;

			case Constants.DYNAMICREST:
				return dynamicRestEntityValidator;

			case Constants.SITELAYOUT:
				return siteLayoutEntityValidator;

			case Constants.FILEBIN:
				return fileBinEntityValidator;

			case Constants.HELPMANUAL:
				return helManualEntityValidator;

			default:
				// throw new UnsupportedOperationException("Unsupported type!");
				return null;
		}

	}
}
