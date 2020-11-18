	class ImportExportConfig {
		constructor(systemConfigIncludeList, customConfigExcludeList, 
				modeuleName, moduleID, gridID, colM, moduleType, exportableDataListMap) {
			this.systemConfigIncludeList = systemConfigIncludeList;
			this.customConfigExcludeList = customConfigExcludeList;
			this.moduleName	= modeuleName;
			this.moduleID = moduleID;
			this.gridID = gridID;
			this.colM = colM;
			this.moduleType = moduleType;
			this.exportableDataListMap = exportableDataListMap;
		}
		
		getSystemConfigIncludeList() {
			return this.systemConfigIncludeList;
		}

		getCustomConfigExcludeList() {
			return this.customConfigExcludeList;
		}

		getModuleName() {
			return this.moduleName;
		}

		getModuleID() {
			return this.moduleID;
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
		
		getGrid() {
			let grid = $("#"+this.moduleType+"").grid({
	      		gridId: this.gridID,
	      		colModel: this.colM
	 		 });
			return grid;
		}
	}
	
	class ExportableData {
		constructor(moduleType, moduleId, moduleName, moduleVersion) {
			this.moduleType = moduleType;
			this.moduleId = moduleId;
			this.moduleName = moduleName;
			this.moduleVersion = moduleVersion;
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
	}

	let map = new Map();

	function openTab(evt, moduleName, moduleID, gridID, moduleType) {
	  	var i, tabcontent, tablinks;
	  	tabcontent = document.getElementsByClassName("tabcontent");
	  	for (i = 0; i < tabcontent.length; i++) {
	    	tabcontent[i].style.display = "none";
	  	}
	  	tablinks = document.getElementsByClassName("tablinks");
	  	for (i = 0; i < tablinks.length; i++) {
	    	tablinks[i].className = tablinks[i].className.replace(" active", "");
	  	}
	  	
	  	var exportObj = map.get(moduleType);
		if(moduleType != "Grid") {
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

					let exportableDataListMap = exportObj.getExportableDataListMap();
					if(exportableDataListMap == null) {
						exportableDataListMap = new Map();
					}
					
				}
				if(moduleType == "Templates") {
					colM = [
						{ title: "", width: 50, align: "center", render: updateExportTemplateFormatter, dataIndx: "" },
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
				} else if(moduleType == "ResourceBundle") {
					colM = [
						{ title: "", width: 10, align: "center", render: updateRBRenderer, dataIndx: "" },
						{ title: "${messageSource.getMessage('jws.resourceKey')}", width: 90, dataIndx: "resourceKey", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "${messageSource.getMessage('jws.languageName')}", width: 90, dataIndx: "languageName", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
					    { title: "${messageSource.getMessage('jws.text')}", width: 90, dataIndx: "resourceBundleText", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
				} else if(moduleType == "Autocomplete") {
					colM = [
						{ title: "", width: 20, align: "center", render: updateExportAutocompleteFormatter, dataIndx: "" },
						{ title: "Autocomplete Id", width: 130, align: "center", dataIndx: "autocompleteId", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Autocomplete Description", width: 100, align: "center",  dataIndx: "autocompleteDescription", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Autocomplete Query", width: 160, align: "center", dataIndx: "acQuery", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
				} else if(moduleType == "Notification") {
					colM = [
						{ title: "", width: 20, align: "center", render: updateNotificationRenderer, dataIndx: "" },
						{ title: "",hidden: true, width: 130, dataIndx: "notificationId" },
				        { title: "Target Platform", width: 100,  dataIndx: "targetPlatform", align: "left", halign: "center",
				        	filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				        { title: "Message Type", width: 160, dataIndx: "messageType", align: "left", halign: "center", 
				        	filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Message Text", width: 200, dataIndx: "messageText", align: "left", halign: "center",
				          filter: { type: "textbox", condition: "contain",  listeners: ["change"] }}
					];
				} else if(moduleType == "Dashboard") {
					colM = [
						{ title: "", width: 20, align: "center", render: updateDashboardRenderer, dataIndx: "" },
						{ title: "${messageSource.getMessage('jws.dashboardName')}", width: 130, dataIndx: "dashboardName", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "${messageSource.getMessage('jws.dashboardType')}", width: 130, dataIndx: "dashboardType" , align: "left", align: "left", halign: "center", 
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}},
						{ title: "${messageSource.getMessage('jws.createdBy')}", width: 100, dataIndx: "createdBy", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}},
						{ title: "${messageSource.getMessage('jws.createdDate')}", width: 100, dataIndx: "createdDate" , align: "left", halign: "center" },
						{ title: "${messageSource.getMessage('jws.lastUpdatedDate')}", width: 100, dataIndx: "lastUpdatedDate" , align: "left", halign: "center" },
						{ title: "${messageSource.getMessage('jws.contextDescription')}", width: 100, dataIndx: "contextDescription", align: "left", align: "left", halign: "center", 
							filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
				} else if(moduleType == "Dashlets") {
					colM = [
						{ title: "", width: 20, align: "center", render: updateDashletRenderer, dataIndx: "" },
						{ title: "${messageSource.getMessage('jws.dashletName')}", width: 130, dataIndx: "dashletName" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "${messageSource.getMessage('jws.dashletTitle')}", width: 130, dataIndx: "dashletTitle", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "${messageSource.getMessage('jws.createdBy')}", width: 100, dataIndx: "createdBy" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "${messageSource.getMessage('jws.createdDate')}", width: 100, dataIndx: "createdDate", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "${messageSource.getMessage('jws.updatedBy')}", width: 100, dataIndx: "updatedBy" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "${messageSource.getMessage('jws.updatedDate')}", width: 100, dataIndx: "updatedDate" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "${messageSource.getMessage('jws.status')}", width: 160, dataIndx: "status" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }}
					];
				} else if(moduleType == "DynamicForm") {
					colM = [
						{ title: "", width: 20, align: "center", render: updateDynamicFormRenderer, dataIndx: "" },
						{ title: "Form Id", width: 190, dataIndx: "formId" , align: "left", halign: "center"},
						{ title: "Form Name", width: 130, dataIndx: "formName" , align: "left", halign: "center",
						filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Form Description", width: 130, dataIndx: "formDescription", align: "left", halign: "center",
						filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Created By", width: 100, dataIndx: "createdBy" , align: "left", halign: "center",
						filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Created Date", width: 100, dataIndx: "createdDate", align: "left", halign: "center",
						filter: { type: "textbox", condition: "contain",  listeners: ["change"] }}
					];
				} else if(moduleType == "FileManager") {
					colM = [
							{ title: "", width: 20, align: "center", render: updateFileManagerRenderer, dataIndx: "" },
							{ title: "File Config Id", width: 130, dataIndx: "fileUploadConfigId", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					        { title: "File Type Supported", width: 100, dataIndx: "fileTypeSupported", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
					        { title: "Max File Size", width: 160, dataIndx: "maxFileSize", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
							{ title: "No Of Files", width: 160, dataIndx: "noOfFiles", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
				} else if(moduleType == "DynaRest") {
					colM = [
						{ title: "", width: 20, align: "center", render: updateDynaRestRenderer, dataIndx: "" },
						{ title: "Dynamic API Url", width: 130, align: "center", dataIndx: "jws_dynamic_rest_url", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					        { title: "Method Name", width: 100, align: "center",  dataIndx: "jws_method_name", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					        { title: "Method Description", width: 160, align: "center", dataIndx: "jws_method_description", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
				} else if(moduleType == "Permission") {
					colM = [
							{ title: "", width: 20, align: "center", render: updatePermissionRenderer, dataIndx: "" },
							{ title: "Module Name", width: 100, align: "center",  dataIndx: "moduleId", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					        { title: "Entity Name", width: 130, align: "center", dataIndx: "entityName", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
				} else if(moduleType == "SiteLayout") {
					colM = [
						{ title: "", width: 20, align: "center", render: updateSiteLayoutRenderer, dataIndx: "" },
						{ title: "", width: 130, align: "center", dataIndx: "moduleId", align: "left", halign: "center", hidden : true },
				        { title: "Module Name", width: 100, align: "center",  dataIndx: "moduleName", align: "left", halign: "center",
				        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
				        { title: "Module URL", width: 160, align: "center", dataIndx: "moduleURL", align: "left", halign: "center",
				        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
				}
				exportObj = new ImportExportConfig(systemConfigIncludeList, customConfigExcludeList, moduleName, 
					moduleID, gridID, colM, moduleType, exportableDataListMap);
				map.set(moduleType, exportObj);

				let grid = $("#"+moduleType+"").grid({
		      		gridId: gridID,
		      		colModel: colM
		 		 });	
			}
		}
		
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
		const id = uiObject.rowData.fileUploadConfigId;
		const name = uiObject.rowData.fileTypeSupported;
		const version = "NA";
		const isSystemVariable =  1;
		const moduleType = "FileManager";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function updateDynaRestRenderer(uiObject) {
		const id = uiObject.rowData.jws_dynamic_rest_url;
		const name = uiObject.rowData.jws_method_name;
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
		const isSystemVariable =  2;
		const moduleType = "SiteLayout";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}

	function renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable) {
		let exportableData = new ExportableData(moduleType, id, name, version);
		if(isSystemVariable == 1) {
			if(customConfigExcludeList.indexOf(id) != -1) {
				return '<input type="checkbox" id="'+id+'" name="'+id+'" onchange="checkCustomVar(this,\'' +  moduleType+ '\',\'' +  name+ '\',\'' +  version+ '\');">'.toString();
			} else {
				map.get(moduleType).getExportableDataListMap().set(id, exportableData);
				return '<input type="checkbox" id="'+id+'" name="'+id+'" onchange="checkCustomVar(this, \'' +  moduleType+ '\',\'' +  name+ '\',\'' +  version+ '\');" checked="checked">'.toString();
			}
		} else {
			if(systemConfigIncludeList.indexOf(id) != -1) {
				map.get(moduleType).getExportableDataListMap().set(id, exportableData);
				return '<input type="checkbox" id="'+id+'" name="'+id+'" onchange="checkSystemVar(this, \'' +  moduleType+ '\',\'' +  name+ '\',\'' +  version+ '\');" checked="checked">'.toString();
			} else {
				return '<input type="checkbox" id="'+id+'" name="'+id+'" onchange="checkSystemVar(this, \'' +  moduleType+ '\',\'' +  name+ '\',\'' +  version+ '\');">'.toString();
			}
		}
	}
	
	function checkCustomVar(checkedCustom,  moduleType, name, version) {
		debugger;
		var id=checkedCustom.id;
		
		let exportableData = new ExportableData(moduleType, id, name, version);
		
		if (!checkedCustom.checked) {
			map.get(moduleType).getCustomConfigExcludeList().push(id);
			map.get(moduleType).getExportableDataListMap().delete(id);
		} else {
			map.get(moduleType).getCustomConfigExcludeList().splice(id, 1);
			map.get(moduleType).getExportableDataListMap().set(id, exportableData);
		}
	}
	
	function checkSystemVar(checkedSystem,  moduleType, name, version){
		debugger;
		var id=checkedSystem.id;

		let exportableData = new ExportableData(moduleType, id, name, version);
		
		if (checkedSystem.checked) {
			map.get(moduleType).getSystemConfigIncludeList().push(id);
			map.get(moduleType).getExportableDataListMap().set(id, exportableData);
		} else {
			map.get(moduleType).getSystemConfigIncludeList().splice(id, 1);
			map.get(moduleType).getExportableDataListMap().delete(id);
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
	
	function getPreviewHTMLTable() {
		var jsonArr = [];
		var tableRow="";
		let exportDataList = map.values()
		if(exportDataList != null) {
			for (let config of exportDataList){
				if(config instanceof ImportExportConfig) {
					let  exportDataList = config.getExportableDataListMap().values();
					if(exportDataList != null) {
						for (let exportData of exportDataList){
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
		const out = Object.create(null)
        map.forEach((value, key) => {
         if (value instanceof Map) {
           out[key] = map_to_object(value)
         }
         else {
           out[key] = JSON.stringify(value);
         }
       })
       
		$.ajax({
			type : "POST",
			url : "/cf/ecd",
			async: false,
		     contentType: "application/json",
		     data : JSON.stringify(out),
			success : function(data) {
				$("#exportFormDiv")
				.html(
						"<form name='exportForm' method='post' id='exportForm' action='/cf/downloadExport'> "
								+ "<input type='hidden' id='filePath' name='filePath' value='"
								+ data
								+ "'/>"
								+ "</form>");
				$("#exportForm").submit();
				showMessage("Configuration exported successfully", "success");
			},
		        
		    error : function(xhr, error){
		    	showMessage("Error occurred while exporting", "error");
		    },
		        	
		});
	}

	function backToPreviousPage(){
		location.href = "/cf/pml";
	}
		