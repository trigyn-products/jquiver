	class ImportExportConfig {
		constructor(systemConfigIncludeList, customConfigExcludeList, gridID, 
				colM, moduleType, exportableDataListMap, isSelectTypeApplicable, additionalParameterKey) {
			this.systemConfigIncludeList = systemConfigIncludeList;
			this.customConfigExcludeList = customConfigExcludeList;
			this.gridID = gridID;
			this.colM = colM;
			this.moduleType = moduleType;
			this.exportableDataListMap = exportableDataListMap;
			this.isSelectTypeApplicable = isSelectTypeApplicable;
			this.additionalParameterKey = additionalParameterKey;
		}
		
		getSystemConfigIncludeList() {
			return this.systemConfigIncludeList;
		}

		getCustomConfigExcludeList() {
			return this.customConfigExcludeList;
		}
		
		getModuleType() {
			return this.moduleType;
		}

		getGridID() {
			return this.gridID;
		}

		getColM() {
			return this.colM;
		}
		
		getExportableDataListMap() {
			return this.exportableDataListMap;
		}
		
		getIsSelectTypeApplicable() {
			return this.isSelectTypeApplicable;
		}
		
		getAdditionalParameterKey() {
			return this.additionalParameterKey;
		}
		
		getGrid(sortIndex) {
			let grid= $("#"+this.moduleType+"").grid({
	      		gridId: this.gridID,
	      		colModel: this.colM,
	            dataModel: {
	            	url: contextPath+"/cf/pq-grid-data",
	                sortIndx: sortIndex,
	                sortDir: "up"
	            }
	 		 });
			return grid;
		}
	}
	
	class ExportableData {
		constructor(moduleType, moduleId, moduleName, moduleVersion, isSystemVariable) {
			this.moduleType = moduleType;
			this.moduleId = moduleId;
			this.moduleName = moduleName;
			this.moduleVersion = moduleVersion;
			this.isSystemVariable = isSystemVariable;
		}

		getModuleType() {
			return this.moduleType;
		}

		getModuleId() {
			return this.moduleId;
		}

		getModuleName() {
			return this.moduleName;
		}

		getModuleVersion() {
			return this.moduleVersion;
		}

		getIsSystemVariable() {
			return this.isSystemVariable;
		}
	}

	let map = new Map();

	function openTab(evt, gridID, moduleType) {
	  	var i, tabcontent, tablinks;
	  	tabcontent = document.getElementsByClassName("tabcontent");
	  	for (i = 0; i < tabcontent.length; i++) {
	    	tabcontent[i].style.display = "none";
	  	}
	  	tablinks = document.getElementsByClassName("tablinks");
	  	for (i = 0; i < tablinks.length; i++) {
	    	tablinks[i].className = tablinks[i].className.replace(" active", "");
	  	}
	  	
	  	let isSelectTypeApplicable;
	  	let additionalParameterKey;
	  	let additionalParameterValue;
	  	var exportObj = map.get(moduleType);
		if(moduleType != "ApplicationConfiguration") {
			if(exportObj == null || (exportObj != null && exportObj.getColM() == null)) {
				let systemConfigIncludeList = [];
				let customConfigExcludeList = [];
				let exportableDataListMap = new Map();
				if(exportObj != null) {
					systemConfigIncludeList=exportObj.getSystemConfigIncludeList();
					if(systemConfigIncludeList == null) {
						systemConfigIncludeList=[];
					}

					customConfigExcludeList=exportObj.getCustomConfigExcludeList();
					if(customConfigExcludeList == null) {
						customConfigExcludeList=[];
					}

					exportableDataListMap = exportObj.getExportableDataListMap();
					if(exportableDataListMap == null) {
						exportableDataListMap = new Map();
					}
					
				}
				let grid = null;
				let sortIndex = 1;
				if(moduleType == "Grid") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateExportGridFormatter, dataIndx: "" },
				        { title: "Grid Id", width: 130, align: "center", dataIndx: "gridId", align: "left", halign: "center",
				        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
				        { title: "Grid Name", width: 100, align: "center",  dataIndx: "gridName", align: "left", halign: "center",
				        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
				        { title: "Grid Description", width: 160, align: "center", dataIndx: "gridDesc", align: "left", halign: "center",
				        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
				        { title: "Grid Table Name", width: 200, align: "center", dataIndx: "gridTableName", align: "left", halign: "center",
				        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
				        { title: "Grid Column Names", width: 100, align: "center", dataIndx: "gridColumnName", align: "left", halign: "center",
				        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_gridTypeId":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "Templates") {
					colM = [
						{ title: "Action", width: 50, maxWidth: 20, align: "center", render: updateExportTemplateFormatter, dataIndx: "" },
						{ title: "", hidden: true, sortable : false, dataIndx: "templateId" },
			            { title: "Template Name", width: 130, align: "center", sortable : true, dataIndx: "templateName", align: "left", halign: "center",
			            filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
			            { title: "Created By", width: 100, align: "center",  sortable : true, dataIndx: "createdBy", align: "left", halign: "center",
			            filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
			            { title: "Updated By", width: 160, align: "center", sortable : true, dataIndx: "updatedBy", align: "left", halign: "center",
			            filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
			            { title: "Updated Date", width: 200, align: "center", sortable : true, dataIndx: "updatedDate", align: "left", halign: "center",
			            filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 2;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_templateTypeId":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "ResourceBundle") {
					colM = [
						{ title: "Action", width: 10, maxWidth: 20, align: "center", render: updateRBRenderer, dataIndx: "" },
						{ title: "Resource Key", width: 90, dataIndx: "resourceKey", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Language Name", width: 90, dataIndx: "languageName", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
					    { title: "Text", width: 90, dataIndx: "resourceBundleText", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "templateTypeId";
					if(selectedType == 1) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: "customResourceBundleListingGrid",
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_resourceKey":"str_jws"}
				 		 });
					} else if(selectedType == 2) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            }
				 		 });
					}
					
				} else if(moduleType == "Autocomplete") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateExportAutocompleteFormatter, dataIndx: "" },
						{ title: "Autocomplete Id", width: 130, align: "left", dataIndx: "autocompleteId", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Autocomplete Description", width: 100, align: "left",  dataIndx: "autocompleteDescription", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Autocomplete Query", width: 160, align: "left", dataIndx: "acQuery", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_autocompleteTypeId":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "Notification") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateNotificationRenderer, dataIndx: "" },
						{ title: "",hidden: true, width: 130, dataIndx: "notificationId" },
				        { title: "Target Platform", width: 100,  dataIndx: "targetPlatform", align: "left", halign: "center",
				        	filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				        { title: "Message Type", width: 160, dataIndx: "messageType", align: "left", halign: "center", 
				        	filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Message Text", width: 200, dataIndx: "messageText", align: "left", halign: "center",
				          filter: { type: "textbox", condition: "contain",  listeners: ["change"] }}
					];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "Dashboard") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateDashboardRenderer, dataIndx: "" },
						{ title: "Dashboard Name", width: 130, dataIndx: "dashboardName", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Dashboard Type", width: 130, dataIndx: "dashboardType" , align: "left", align: "left", halign: "center", 
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}},
						{ title: "Created By", width: 100, dataIndx: "createdBy", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}},
						{ title: "Created Date", width: 100, dataIndx: "createdDate" , align: "left", halign: "center" },
						{ title: "Last Updated Date", width: 100, dataIndx: "lastUpdatedDate" , align: "left", halign: "center" },
						{ title: "Context Description", width: 100, dataIndx: "contextDescription", align: "left", align: "left", halign: "center", 
							filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_dashboardType":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "Dashlets") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateDashletRenderer, dataIndx: "" },
						{ title: "Dashlet Name", width: 130, dataIndx: "dashletName" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Dashlet Title", width: 130, dataIndx: "dashletTitle", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Created By", width: 100, dataIndx: "createdBy" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Created Date", width: 100, dataIndx: "createdDate", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Updated By", width: 100, dataIndx: "updatedBy" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Updated Date", width: 100, dataIndx: "updatedDate" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Status", width: 160, dataIndx: "status" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }}
					];
					sortIndex = 2;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_dashletTypeId":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "DynamicForm") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateDynamicFormRenderer, dataIndx: "" },
							{ title: "Form Id", width: 190, dataIndx: "formId" , align: "left", halign: "center",
								filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Form Name", width: 130, dataIndx: "formName" , align: "left", halign: "center",
						filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Form Description", width: 130, dataIndx: "formDescription", align: "left", halign: "center",
						filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Created By", width: 100, dataIndx: "createdBy" , align: "left", halign: "center",
						filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Created Date", width: 100, dataIndx: "createdDate", align: "left", halign: "center",
						filter: { type: "textbox", condition: "contain",  listeners: ["change"] }}
					];
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_formTypeId":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "FileManager") {
					colM = [
							{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateFileManagerRenderer, dataIndx: "" },
							{ title: "File Bin Id", width: 130, dataIndx: "fileBinId", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					        { title: "File Type Supported", width: 100, dataIndx: "fileTypeSupported", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
					        { title: "Max File Size", width: 160, dataIndx: "maxFileSize", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
							{ title: "No Of Files", width: 160, dataIndx: "noOfFiles", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "DynaRest") {
					colM = [
							{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateDynaRestRenderer, dataIndx: "" },
							{ title: "Dynamic API Url", width: 130, align: "center", dataIndx: "jws_dynamic_rest_url", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					        { title: "Method Name", width: 100, align: "center",  dataIndx: "jws_method_name", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					        { title: "Method Description", width: 160, align: "center", dataIndx: "jws_method_description", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 2;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_jws_dynamic_rest_type_id":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "Permission") {
					colM = [
							{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updatePermissionRenderer, dataIndx: "" },
							{ title: "Module ID", width: 100, align: "center",  dataIndx: "moduleId", align: "left", halign: "center",
								filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
							{ title: "Module Name", width: 100, align: "center",  dataIndx: "moduleName", align: "left", halign: "center",
						        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
						    { title: "Role Name", width: 100, align: "center",  dataIndx: "roleName", align: "left", halign: "center",
						        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
						    { title: "Entity Name", width: 130, align: "center", dataIndx: "entityName", align: "left", halign: "center",
						        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 5, 3;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "SiteLayout") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateSiteLayoutRenderer, dataIndx: "" },
						{ title: "", width: 130, align: "center", dataIndx: "moduleId", align: "left", halign: "center", hidden : true },
				        { title: "Module Name", width: 100, align: "center",  dataIndx: "moduleName", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
				        { title: "Module URL", width: 160, align: "center", dataIndx: "moduleURL", align: "left", halign: "center",
								filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 2;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_moduleTypeId":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "ApplicationConfiguration") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateAppConfigRenderer, dataIndx: "" },
						{ title: "Owner Id", width: 130, dataIndx: "ownerId", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Owner Type", width: 130, dataIndx: "ownerType", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Property Name", width: 130, dataIndx: "propertyName", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Property Value", width: 130, dataIndx: "propertyValue", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Modified By", width: 130, dataIndx: "modifiedBy", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Comments", width: 130, dataIndx: "comments", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  }
					];
					sortIndex = 4;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "ManageUsers") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateUsersRenderer, dataIndx: "" },
						{ title: "${messageSource.getMessage('jws.email')}", width: 100, align: "center",  dataIndx: "email", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
						{ title: "${messageSource.getMessage('jws.firstName')}", width: 130, align: "center", dataIndx: "first_name", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "${messageSource.getMessage('jws.lastName')}", width: 130, align: "center", dataIndx: "last_name", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "${messageSource.getMessage('jws.isActive')}", width: 160, align: "center", dataIndx: "is_active", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "ManageRoles") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateRolesRenderer, dataIndx: "" },
						{ title: "Role Name", width: 130, align: "center", dataIndx: "role_name", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Role Description", width: 100, align: "center",  dataIndx: "role_description", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Is Active", width: 160, align: "center", dataIndx: "is_active", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "HelpManual") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateHelpManualRenderer, dataIndx: "" },
						{ title: "Manual Name", width: 130, align: "center", dataIndx: "name", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_is_system_manual":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "ApiClientDetails") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateExportApiClientDetailsFormatter, dataIndx: "" },
						{ title: "Client Id", hidden : true, width: 130, dataIndx: "client_id", align: "left", align: "left", halign: "center",
		                    filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Client Name", hidden : false, width: 130, dataIndx: "client_name", align: "left", align: "left", halign: "center",
		                    filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Client Key", hidden : false, width: 130, dataIndx: "client_key", align: "left", align: "left", halign: "center",
		                    filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Encryption Algorithm", hidden : false, width: 130, dataIndx: "encryption_algo_name", align: "left", align: "left", halign: "center",
		                    filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Updated By", hidden : false, width: 130, dataIndx: "updated_by", align: "left", align: "left", halign: "center",
		                    filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Created By", hidden : false, width: 130, dataIndx: "created_by", align: "left", align: "left", halign: "center",
		                    filter: { type: "textbox", condition: "contain", listeners: ["change"]}  }
		            ];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "AdditionalDatasource") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateExportAdditionalDSFormatter, dataIndx: "" },
						{ title: "Datasource ID", hidden : true, width: 130, dataIndx: "additionalDatasourceId", align: "left", align: "left", halign: "center",
		                	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Datasource Name", hidden : false, width: 130, dataIndx: "datasourceName", align: "left", align: "left", halign: "center",
		                	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Datasource Product Name", hidden : false, width: 130, dataIndx: "databaseProductName", align: "left", halign: "center",
		                	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
		                { title: "Created By", hidden : false, width: 130, dataIndx: "createdBy", align: "left", halign: "center",
		                	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Last Updated Date", hidden : false, width: 130, dataIndx: "lastUpdatedTs", align: "left", halign: "center"}
		            ];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				}
				exportObj = new ImportExportConfig(systemConfigIncludeList, customConfigExcludeList, gridID, 
						colM, moduleType, exportableDataListMap, isSelectTypeApplicable, additionalParameterKey);
				map.set(moduleType, exportObj);

				if(grid == null	) {
					grid= $("#"+moduleType+"").grid({
			      		gridId: gridID,
			      		colModel: colM,
			      		height:300,
			            dataModel: {
			            	url: contextPath+"/cf/pq-grid-data",
			                sortIndx: sortIndex,
			                sortDir: "up"
			            }
			 		 });
				} 
			}
		}
		let gridNew = $("#"+moduleType+"").pqGrid();
        gridNew.pqGrid( "refreshDataAndView" ); 
		
	  	document.getElementById(moduleType).style.display = "block";
	  	if(evt != null) {
	 	evt.currentTarget.className += " active";}
	  	
	}

	function moduleTypes(uiObject){
	  let cellValue = uiObject.rowData.moduleId;
	  return moduleType.find(el => el[cellValue])[cellValue];
	}

	function getVersion(uiObject) {
		let version = uiObject.rowData.max_version_id;
		if(version == null) {
			version = "1.0";
		} 

		var pattern = ".";
		if (version.toString().indexOf(pattern) == -1) {
			version = version+".0"
		}
		return version;
	}

	
	function updateExportGridFormatter(uiObject) {
		const id = uiObject.rowData.gridId;
		const name = uiObject.rowData.gridName;
		let version = getVersion(uiObject);
		
		const isSystemVariable =  uiObject.rowData.gridTypeId;
		const moduleType = "Grid";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();

		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}

	function updateExportTemplateFormatter(uiObject) {
		const id = uiObject.rowData.templateId;
		const name = uiObject.rowData.templateName;
		let version = getVersion(uiObject);
		const isSystemVariable =  uiObject.rowData.templateTypeId;
		const moduleType = "Templates";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();

		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}

	function updateRBRenderer(uiObject) {
		const id = uiObject.rowData.resourceKey;
		const name = uiObject.rowData.resourceBundleText;
		let version = getVersion(uiObject);
		var isSystemVariable =  1;
		if(id.startsWith("jws.")) {
			isSystemVariable = 2;
		}
		const moduleType = "ResourceBundle";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();

		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
		
	function updateExportAutocompleteFormatter(uiObject) {
		const id = uiObject.rowData.autocompleteId;
		const name = uiObject.rowData.autocompleteDescription;
		let version = getVersion(uiObject);
		const isSystemVariable =  uiObject.rowData.autocompleteTypeId;
		const moduleType = "Autocomplete";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();

		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function updateNotificationRenderer(uiObject) {
		const id = uiObject.rowData.notificationId;
		const name = uiObject.rowData.messageText;
		let version = getVersion(uiObject);
		const isSystemVariable =  1;
		const moduleType = "Notification";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();

		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function updateDashboardRenderer(uiObject) {
		const id = uiObject.rowData.dashboardId;
		const name = uiObject.rowData.dashboardName;
		let version = getVersion(uiObject);
		const isSystemVariable =  uiObject.rowData.dashboardType;
		const moduleType = "Dashboard";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();

		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function updateDashletRenderer(uiObject) {
		const id = uiObject.rowData.dashletId;
		const name = uiObject.rowData.dashletName;
		let version = getVersion(uiObject);
		const isSystemVariable =  uiObject.rowData.dashletTypeId;
		const moduleType = "Dashlets";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();

		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function updateDynamicFormRenderer(uiObject) {
		const id = uiObject.rowData.formId;
		const name = uiObject.rowData.formName;
		let version = getVersion(uiObject);
		const isSystemVariable =  uiObject.rowData.formTypeId;
		const moduleType = "DynamicForm";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();

		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	// no system var
	function updateFileManagerRenderer(uiObject) {
		const id = uiObject.rowData.fileBinId;
		const name = uiObject.rowData.fileTypeSupported;
		const version = "NA";
		const isSystemVariable =  1;
		const moduleType = "FileManager";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function updateDynaRestRenderer(uiObject) {
		const id = uiObject.rowData.jws_dynamic_rest_id;
		const name = uiObject.rowData.jws_dynamic_rest_url;
		let version = getVersion(uiObject);
		const isSystemVariable =  uiObject.rowData.jws_dynamic_rest_type_id;
		const moduleType = "DynaRest";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}

	// dont havae system var
	function updatePermissionRenderer(uiObject) {
		const id = uiObject.rowData.entityRoleId;
		const name = uiObject.rowData.entityName;
		const version = "NA";
		const isSystemVariable =  2;
		const moduleType = "Permission";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	// dont havae system var
	function updateSiteLayoutRenderer(uiObject) {
		const id = uiObject.rowData.moduleId;
		const name = uiObject.rowData.moduleName;
		const version = "NA";
		const isSystemVariable =  uiObject.rowData.moduleTypeId;
		const moduleType = "SiteLayout";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	// dont havae system var
	function updateAppConfigRenderer(uiObject) {
		const id = uiObject.rowData.propertyMasterId;
		const name = uiObject.rowData.propertyName;
		const version = getVersion(uiObject);
		const isSystemVariable =  2;
		const moduleType = "ApplicationConfiguration";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	// dont havae system var
	function updateUsersRenderer(uiObject) {
		const id = uiObject.rowData.user_id;
		const name = uiObject.rowData.first_name + " " + uiObject.rowData.last_name;
		const version = "NA";
		const isSystemVariable =  2;
		const moduleType = "ManageUsers";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	// dont havae system var
	function updateRolesRenderer(uiObject) {
		const id = uiObject.rowData.role_name;
		const name = uiObject.rowData.role_description;
		const version = "NA";
		const isSystemVariable =  2;
		const moduleType = "ManageRoles";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function updateHelpManualRenderer(uiObject) {
		const id = uiObject.rowData.manual_id;
		const name = uiObject.rowData.name;
		const version = "NA";
		const isSystemVariable =  uiObject.rowData.is_system_manual;
		const moduleType = "HelpManual";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function updateExportApiClientDetailsFormatter(uiObject) {
		const id = uiObject.rowData.client_id;
		const name = uiObject.rowData.client_name;
		const version = "NA";
		const isSystemVariable =  1;
		const moduleType = "ApiClientDetails";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}

	function updateExportAdditionalDSFormatter(uiObject) {
		const id = uiObject.rowData.additionalDatasourceId;
		const name = uiObject.rowData.datasourceName;
		const version = "NA";
		const isSystemVariable =  1;
		const moduleType = "AdditionalDatasource";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}

	function renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable) {
		let exportableData = new ExportableData(moduleType, id, name, version, isSystemVariable);
		if(isSystemVariable == 1) {
			if(customConfigExcludeList.indexOf(id) != -1 || isDeselectedAll == true) {
				return '<input type="checkbox" id="'+moduleType+id+'" name="'+moduleType+id+'" onchange="checkCustomVar(this,\'' 
					+  id+ '\',\'' +  moduleType+ '\',\'' +  name+ '\',\'' +  version+ '\',\'' +  isSystemVariable+ '\');">'.toString();
			} else {
				map.get(moduleType).getExportableDataListMap().set(id, exportableData);
				return '<input type="checkbox" id="'+moduleType+id+'" name="'+moduleType+id+'" onchange="checkCustomVar(this, \'' 
					+  id+ '\',\'' +  moduleType+ '\',\'' +  name+ '\',\'' +  version+ '\',\'' +  isSystemVariable+ '\');" checked="checked">'.toString();
			}
		} else {
			if(systemConfigIncludeList.indexOf(id) != -1) {
				map.get(moduleType).getExportableDataListMap().set(id, exportableData);
				return '<input type="checkbox" id="'+moduleType+id+'" name="'+moduleType+id+'" onchange="checkSystemVar(this, \'' 
					+  id+ '\',\'' +  moduleType+ '\',\'' +  name+ '\',\'' +  version+ '\',\'' +  isSystemVariable+ '\');" checked="checked">'.toString();
			} else {
				return '<input type="checkbox" id="'+moduleType+id+'" name="'+moduleType+id+'" onchange="checkSystemVar(this, \'' 
					+  id+ '\',\'' +  moduleType+ '\',\'' +  name+ '\',\'' +  version+ '\',\'' +  isSystemVariable+ '\');">'.toString();
			}
		}
	}
	
	function checkCustomVar(checkedCustom, id, moduleType, name, version, isSystemVariable) {
		isDeselectedAll = false;
		let exportableData = new ExportableData(moduleType, id, name, version, isSystemVariable);
		
		if (!checkedCustom.checked) {
			map.get(moduleType).getCustomConfigExcludeList().push(id);
			map.get(moduleType).getExportableDataListMap().delete(id);
			let count = $('#selectedCount_'+moduleType).text();
			let countInt = parseInt(count);
			$('#selectedCount_'+moduleType).text(countInt-1);
		} else {
			var i = map.get(moduleType).getCustomConfigExcludeList().indexOf(id);
			if(i != -1) {
				map.get(moduleType).getCustomConfigExcludeList().splice(i, 1);
			}
			map.get(moduleType).getExportableDataListMap().set(id, exportableData);
			if($('#deselectAllChkBx').is(':disabled')) {
				$("#deselectAllChkBx").removeAttr('disabled');
				$('#deselectAllChkBx').prop("checked",false);	
			}
			let count = $('#selectedCount_'+moduleType).text();
			let countInt = parseInt(count);
			$('#selectedCount_'+moduleType).text(countInt+1);
			
		}
	}
	
	function checkSystemVar(checkedSystem, id, moduleType, name, version, isSystemVariable){

		isDeselectedAll = false;
		let exportableData = new ExportableData(moduleType, id, name, version, isSystemVariable);
		
		if (checkedSystem.checked) {
			map.get(moduleType).getSystemConfigIncludeList().push(id);
			map.get(moduleType).getExportableDataListMap().set(id, exportableData);
			if($('#deselectAllChkBx').is(':disabled')) {
				$("#deselectAllChkBx").removeAttr('disabled');
				$('#deselectAllChkBx').prop("checked",false);	
			}

			let count = $('#selectedCount_'+moduleType).text();
			let countInt = parseInt(count);
			$('#selectedCount_'+moduleType).text(countInt+1);
		} else {
			map.get(moduleType).systemConfigIncludeList 
				= map.get(moduleType).systemConfigIncludeList.filter((element) => {return element != id});
			map.get(moduleType).getExportableDataListMap().delete(id);
			let count = $('#selectedCount_'+moduleType).text();
			let countInt = parseInt(count);
			$('#selectedCount_'+moduleType).text(countInt-1);
		}
	}
	
	function gotoNextPage() {
		$("#htmlTable > tbody").empty();
		$("#htmlTable > tbody").append(getPreviewHTMLTable());
		$("#mainTab").hide();
		$("#nextTab").show();
		$("#mainTabBtn").hide();
		$("#nextTabBtn").show();
	}

    function changeType() {
        selectedType = $("#typeSelect").val();   
        let postData;
        map.forEach(function callback(value, key, map) {
			if(key != "htmlTableJSON") {
				let moduleType 	= key;
				let gridID		= map.get(key).getGridID();
				
				if(moduleType == "Grid") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else {
			            postData = {gridId:gridID,"cr_gridTypeId":"str_"+selectedType}
			        }
				} else if(moduleType == "Templates") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else {
			            postData = {gridId:gridID,"cr_templateTypeId":"str_"+selectedType}
			        }
				} else if(moduleType == "Autocomplete") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else {
			            postData = {gridId:gridID,"cr_autocompleteTypeId":"str_"+selectedType}
			        }
				} else if(moduleType == "Dashboard") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else {
			            postData = {gridId:gridID,"cr_dashboardType":"str_"+selectedType}
			        }
				} else if(moduleType == "Dashlets") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else {
			            postData = {gridId:gridID,"cr_dashletTypeId":"str_"+selectedType}
			        }
				} else if(moduleType == "DynamicForm") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else {
			            postData = {gridId:gridID,"cr_formTypeId":"str_"+selectedType}
			        }
				} else if(moduleType == "DynaRest") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else {
			            postData = {gridId:gridID,"cr_jws_dynamic_rest_type_id":"str_"+selectedType}
			        }
				} else if(moduleType == "HelpManual") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else {
			            postData = {gridId:gridID,"cr_is_system_manual":"str_"+selectedType}
			        }
				} else if(moduleType == "ResourceBundle") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else if(selectedType == 1) {
			        	postData = {gridId:"customResourceBundleListingGrid"}
					} else {
						postData = {gridId:"resourceBundleListingGrid" ,"cr_resourceKey":"str_jws" }
			        }
				} else if(moduleType == "SiteLayout") {
					if(selectedType == 0) {
			            postData = {gridId:gridID}
			        } else {
			            postData = {gridId:gridID,"cr_moduleTypeId":"str_"+selectedType}
			        }
				}
				
				if(gridID != null && moduleType != null && postData != null && postData.gridId != null) {
			        let gridNew = $("#"+moduleType+"").pqGrid();
			        gridNew.pqGrid( "option", "dataModel.postData", postData);
			        gridNew.pqGrid( "refreshDataAndView" );  
				}

			}
		});

        
        
    }
      
	function deselectAll(){
		isDeselectedAll = true;
		map.forEach(function callback(value, key, map) {
			if(key != "htmlTableJSON") {
				let moduleType 	= key;
				let gridID		= map.get(key).getGridID();
				let colM		= map.get(key).getColM();
				
				let systemConfigIncludeList = [];
				let customConfigExcludeList = [];
				let exportableDataListMap = new Map();
				
				let exportableDataListMap1 = map.get(key).getExportableDataListMap();
				for (let exportData of exportableDataListMap1.values()){
					let id = exportData.getModuleId();
					let modType = exportData.getModuleType();
					let isSystemVar = exportData.getIsSystemVariable();
					
					$('#'+ modType + id).prop("checked",false);	
					if(isSystemVar == 1) {
						customConfigExcludeList.push(id);
					}
				}
				
				let exportObj = new ImportExportConfig(systemConfigIncludeList, customConfigExcludeList, gridID, colM, moduleType, exportableDataListMap);
				map.set(moduleType, exportObj);
				
				$('#selectedCount_'+moduleType).text(0);
			}
		});
		 $("#deselectAllChkBx").attr("disabled", "disabled");
	}
		
	function getPreviewHTMLTable() {
		var jsonArr = [];
		var tableRow="";
		var sortedMap = new Map();
		var keys = [];
		map.forEach(function callback(value, key, map) {
		    keys.push(key);
		});
		keys.sort().map(function(key) {
		    sortedMap.set(key, map.get(key));
		});
		let exportDataList = sortedMap.values()
		if(exportDataList != null) {
			for (let config of exportDataList){
				if(config instanceof ImportExportConfig) {
					if(config.getExportableDataListMap().values() != null) {
						var sortedexportDataMap = new Map();
						var keysExportDataList = [];
						config.getExportableDataListMap().forEach(function callback(value, key, map) {
							keysExportDataList.push(key);
						});
						keysExportDataList.sort().map(function(key) {
							sortedexportDataMap.set(key, config.getExportableDataListMap().get(key));
						});
						for (let exportData of sortedexportDataMap.values()){
							let ver = exportData.getModuleVersion();
							let newVer;
							if(ver != "NA" && ver != "") {
								newVer = String(parseFloat(ver).toFixed(1));
							} else {
								newVer = ver;
							}
							
							tableRow += '<tr>';
							tableRow += '<td><label>'+exportData.getModuleType()+'</label> </td>';
							tableRow += '<td><label>'+exportData.getModuleId()+'</label> </td>';
							tableRow += '<td><label>'+exportData.getModuleName()+'</label> </td>';
							tableRow += '<td><label>'+newVer+'</label> </td>';
							tableRow += '</tr>';
							
							jsonArr.push({
						        moduleType: exportData.getModuleType(),
						        moduleID: exportData.getModuleId(),
						        moduleName: exportData.getModuleName(),
						        moduleVersion: newVer,
						    });
						}
					}
				}
			}
		}
		
		map.set("htmlTableJSON", jsonArr);
		return tableRow;
	}

	function gotoPrevPage() {
		$("#nextTab").hide();
		$("#nextTabBtn").hide();
		$("#mainTab").show();
		$("#mainTabBtn").show();
	}
	
	function exportData() {
		let isDataAvailableForExport = true;
		const out = Object.create(null);
        map.forEach((value, key) => {
        	if(key == "htmlTableJSON" && map.get(key).length == 0) {
        		isDataAvailableForExport = false;
        	}
         if (value instanceof Map) {
           out[key] = map_to_object(value)
         }
         else {
           out[key] = JSON.stringify(value);
         }
       });
       
        if(isDataAvailableForExport == true) {
    		$.ajax({
    			type : "POST",
    			url : contextPath+"/cf/ecd",
    			async: false,
    		     contentType: "application/json",
    		     data : JSON.stringify(out),
    			success : function(data) {
    				if(data.startsWith("fail:")){
    					var errorMessageString = data.substring(5);
    					showMessage(errorMessageString, "error");
    				} else {
    					$("#exportFormDiv")
        				.html(
        						"<form name='exportForm' method='post' id='exportForm' action='"+contextPath+"/cf/downloadExport'> "
        								+ "<input type='hidden' id='filePath' name='filePath' value='"
        								+ data
        								+ "'/>"
        								+ "</form>");
        				$("#exportForm").submit();
        				showMessage("Configuration exported successfully", "success");
    				}
    			},
    		        
    		    error : function(xhr, error){
    		    	showMessage("Error occurred while exporting", "error");
    		    },
    		        	
    		});
        } else {
        	showMessage("Nothing to export. Please select atleast one configuration.", "info");
        }
	}

	function backToPreviousPage(){
		location.href = contextPath+"/cf/home";
	}
	