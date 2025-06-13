package com.trigyn.jws.webstarter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.webstarter.dao.GenerateModuleMasterQueries;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.utils.Constant.MasterModuleType;

@Component
public class ModuleMasterExportableDataFactory {

	
	@Autowired
	private TemplateExportableData templateExportableData = null;
	
	@Autowired
	private AutocompleteExportableData autocompleteExportableData = null;
	
	@Autowired
	private RBExportableData rbExportableData = null;
	
	@Autowired
	private DashboardExportableData dashboardExportableData = null;
	
	@Autowired
	private DashletExportableData dashletExportableData = null;
	
	@Autowired
	private DynamicFormExportableData dynamicExportableData = null;

	@Autowired
	private GridUtilsExportableData gridUtilsExportableData = null;

	@Autowired
	private NotificationExportableData notificationExportableData = null;


	@Autowired
	private DynaRestExportableData dynamicRestExportableData = null;

	
	@Autowired
	private FileManagerExportableData fileManagerExportableData = null;
	
	@Autowired
	private PermissionExportableData permissionExportableData = null;


	@Autowired
	private SiteLayoutExportableData siteLayoutExportableData = null;

	@Autowired
	private AppConfigExportableData appConfigExportableData = null;
	
	@Autowired
	private ManageUsersExportableData manageUsersExportableData = null;
	
	@Autowired
	private ManageRolesExportableData manageRolesExportableData = null;
	
	@Autowired
	private HelpManualExportableData helpManualExportableData = null;
	
	@Autowired
	private ApiClientExportableData apiClientExportableData = null;
	
	@Autowired
	private AdditionalDatasourceExportableData additionalDatasourceExportableData = null;
	
	@Autowired
	private SchedulerExportableData schedulerExportableData = null;
	
	@Autowired
	private FilesExportableData filesExportableData = null;

	@Autowired
	private ScriptLibraryExportableData scriptLibraryExportableData = null;


	public GenerateModuleMasterQueries getModuleMaster(String entityType) {

		switch (entityType) {
		case Constant.TEMPLATES:
			return templateExportableData;

		case Constant.AUTOCOMPLETE:
			return autocompleteExportableData;

		case Constant.RESOURCEBUNDLE:
			return rbExportableData;

		case Constant.DASHBOARD:
			return dashboardExportableData;

		case Constant.DASHLET:
			return dashletExportableData;
			
		case Constant.DYNAREST:
			return dynamicRestExportableData;

		case Constant.DYNAMICFORM:
			return dynamicExportableData;

		case Constant.GRID:
			return gridUtilsExportableData;

		case Constant.NOTIFICATION:
			return notificationExportableData;

		case Constant.FILEMANAGER:
			return fileManagerExportableData;

		case Constant.PERMISSION:
			return permissionExportableData;

		case Constant.ROUTER:
			return siteLayoutExportableData;

		case Constant.APPLICATIONCONFIGURATION:
			return appConfigExportableData;

		case Constant.MANAGEUSERS:
			return manageUsersExportableData;

		case Constant.MANAGEROLES:
			return manageRolesExportableData;

		case Constant.HELPMANUAL:
			return helpManualExportableData;

		case Constant.APICLIENTDETAILS:
			return apiClientExportableData;

		case Constant.ADDITIONALDATASOURCE:
			return additionalDatasourceExportableData;

		case Constant.SCHEDULER:
			return schedulerExportableData;

		case Constant.FILEIMPEXPDETAILS:
			return filesExportableData;

		case Constant.SCRIPTLIBRARY:
			return scriptLibraryExportableData;
		default:
			// throw new UnsupportedOperationException("Unsupported type!");
			return null;
		}

	}
}
