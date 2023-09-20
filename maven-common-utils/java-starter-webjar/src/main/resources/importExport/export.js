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
                pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
	let defaultMap = new Map();

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
	  	if(moduleType=="Dashboard"){
		   $("#dashletChkBx").removeAttr('disabled');
	     }else{
		   $("#dashletChkBx").attr("disabled", "disabled");
	     }
	  	let isSelectTypeApplicable;
	  	let additionalParameterKey;
	  	let additionalParameterValue;
	  	var exportObj = map.get(moduleType);
		if(moduleType != undefined &&  moduleType != null && moduleType != "") {
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
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateExportGridFormatter, dataIndx: "" },
                        { title: "Grid Id", width: 130, align: "center", dataIndx: "gridId", align: "left", halign: "center",colIndx:"1",
				          filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
				        { title: "Grid Table Name", width: 200, align: "center", dataIndx: "gridTableName", align: "left", halign: "center",
				          filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
				        { title: "Updated By", width: 200, align: "center", dataIndx: "lastUpdatedBy", align: "left", halign: "center"},
					    { title: "Updated Date", width: 100, align: "center", dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: gridDateRenderer }
					];
					sortIndex = 1;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
                            pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false, render: updateExportTemplateFormatter, dataIndx: "" },
						{ title: "", hidden: true, sortable : false, dataIndx: "templateId" },
			            { title: "Template Name", width: 130, align: "center", sortable : true, dataIndx: "templateName", align: "left", halign: "center",
			            filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
			            { title: "Updated By", width: 160, align: "center", sortable : true, dataIndx: "updatedBy", align: "left", halign: "center" },
			            { title: "Updated Date", width: 200, align: "center", sortable : true, dataIndx: "updatedDate", align: "left", halign: "center", render: templateDateRenderer }
					];
					sortIndex = 2;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
						{title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateRBRenderer, dataIndx: "" },
						{ title: "Resource Key", width: 90, dataIndx: "resourceKey", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
                        { title: "Updated By", width: 160, align: "center", sortable : true, dataIndx: "updatedBy", align: "left", halign: "center"},
			            { title: "Updated Date", width: 200, align: "center", sortable : true, dataIndx: "updatedDate", align: "left", halign: "center", render: resourceBundleDateRenderer }
					];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
					if(selectedType == 1) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: "customResourceBundleListingGrid",
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_resourceKey":"str_jws"}
				 		 });
					} else if(selectedType == 2) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: "customResourceBundleListingGrid",
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            }
				 		 });
					}
					
				} else if(moduleType == "Autocomplete") {
					colM = [
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateExportAutocompleteFormatter, dataIndx: "" },
						{ title: "Autocomplete Id", width: 130, align: "left", dataIndx: "autocompleteId", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Updated By", width: 100, align: "left",  dataIndx: "lastUpdatedBy", halign: "center"},
						 { title: "Updated Date", width: 160, align: "left", dataIndx: "lastUpdatedTs", halign: "center", render: autocompleteDateRenderer }
					];
					sortIndex = 1;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false, render: updateNotificationRenderer, dataIndx: "" },
						{ title: "",hidden: true, width: 130, dataIndx: "notificationId" },
				        { title: "Message Text", width: 100,  dataIndx: "messageText", align: "left", halign: "center",
				        	filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				        { title: "Message Type", width: 160, dataIndx: "messageType", align: "left", halign: "center", render: messageTypeRenderer,
				        	filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						    { title: "Updated By", width: 160, dataIndx: "updatedBy", align: "left", halign: "center"},
							{ title: "Updated Date", width: 200, dataIndx: "updatedDate", align: "left", halign: "center", render: notificationDateRenderer}
					];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "Dashboard") {
					colM = [
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateDashboardRenderer, dataIndx: "" },
						{ title: "Dashboard Name", width: 130, dataIndx: "dashboardName", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Created By", width: 100, dataIndx: "createdBy", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}},
						{ title: "Created Date", width: 100, dataIndx: "createdDate" , align: "left", halign: "center" },
						{ title: "Last Updated Date", width: 100, dataIndx: "lastUpdatedDate" , align: "left", halign: "center", render: dashboardDateRenderer}
					];
					sortIndex = 1;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateDashletRenderer, dataIndx: "" },
						{ title: "Dashlet Name", width: 130, dataIndx: "dashletName" , align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Updated By", width: 100, dataIndx: "updatedBy" , align: "left", halign: "center"},
						{ title: "Updated Date", width: 100, dataIndx: "updatedDate" , align: "left", halign: "center", render: dashletDateRenderer},
					];
					sortIndex = 2;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateDynamicFormRenderer, dataIndx: "" },
						{ title: "Form Name", width: 130, dataIndx: "formName" , align: "left", halign: "center",
						filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
						{ title: "Updated By", width: 100, dataIndx: "lastUpdatedBy" , align: "left", halign: "center"},
						{ title: "Updated Date", width: 100, dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: dynamicFormDateRenderer}
					];
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
							{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateFileManagerRenderer, dataIndx: "" },
							{ title: "File Bin Id", width: 130, dataIndx: "fileBinId", align: "left", halign: "center", 
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					        { title: "Updated By", width: 160, dataIndx: "lastUpdatedBy", align: "left", halign: "center"},
								{ title: "Updated Date", width: 160, dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: fmDateRenderer }
					];
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "DynaRest") {
					colM = [
							{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateDynaRestRenderer, dataIndx: "" },
							{ title: "Dynamic API URL", width: 130, align: "center", dataIndx: "dynarestUrl", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					        { title: "Method Name", width: 100, align: "center",  dataIndx: "methodName", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					       { title: "Updated By", width: 160, align: "center", dataIndx: "lastUpdatedBy", align: "left", halign: "center"},
						        { title: "Updated Date", width: 160, align: "center", dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: dynarestDateRenderer }
					];
					sortIndex = 2;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				           additionalParameters: {"cr_dynarestTypeId":"str_"+selectedType}
				 		 });
					}
				} else if(moduleType == "Permission") {
					colM = [
							{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updatePermissionRenderer, dataIndx: "" },
							{ title: "Module Name", width: 100, align: "center",  dataIndx: "moduleName", align: "left", halign: "center",
						        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
						    { title: "Role Name", width: 100, align: "center",  dataIndx: "roleName", align: "left", halign: "center",
						        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
						    { title: "Entity Name", width: 130, align: "center", dataIndx: "entityName", align: "left", halign: "center",
						        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
						    { title: "Updated By", width: 160, align: "center", dataIndx: "lastUpdatedBy", align: "left", halign: "center" },
							{title: "Updated Date", width: 160, align: "center", dataIndx: "lastUpdatedDate", align: "left", halign: "center", render: permDateRenderer }
					];
					sortIndex = 5, 3;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "SiteLayout") {
					colM = [
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateSiteLayoutRenderer, dataIndx: "" },
						{ title: "", width: 130, align: "center", dataIndx: "moduleId", align: "left", halign: "center", hidden : true },
				        { title: "Module Name", width: 100, align: "center",  dataIndx: "moduleName", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
				        { title: "Module URL", width: 160, align: "center", dataIndx: "moduleURL", align: "left", halign: "center",
								filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
						{title: "Updated Date", width: 160, align: "center", dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: sitelayoutDateRenderer}
					];
					sortIndex = 2;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateAppConfigRenderer, dataIndx: "" },
						{ title: "Property Name", width: 130, dataIndx: "propertyName", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Property Value", width: 130, dataIndx: "propertyValue", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						    { title: "Updated By", width: 160, align: "center", dataIndx: "modifiedBy", align: "left", halign: "center" },
							{title: "Updated Date", width: 160, align: "center", dataIndx: "lastModifiedDate", align: "left", halign: "center", render: appConfDateRenderer}
					];
					sortIndex = 4;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "ManageUsers") {
					colM = [
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateUsersRenderer, dataIndx: "" },
						{ title: "Email", width: 100, align: "center",  dataIndx: "email", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
						{ title: "First Name", width: 130, align: "center", dataIndx: "firstName", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Last Name", width: 130, align: "center", dataIndx: "lastName", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Status", width: 160, align: "center", dataIndx: "status", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "ManageRoles") {
					colM = [
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90, align: "center", sortable: false,  render: updateRolesRenderer, dataIndx: "" },
						{ title: "Role Name", width: 130, align: "center", dataIndx: "roleName", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
					    { title: "Is Active", width: 160, align: "center", dataIndx: "isActive", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "HelpManual") {
					colM = [
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90, align: "center", sortable: false,  render: updateHelpManualRenderer, dataIndx: "" },
						{ title: "Manual Name", width: 130, align: "center", dataIndx: "manualName", align: "left", halign: "center",
					        filter: { type: "textbox", condition: "contain", listeners: ["change"]} }
					];
					sortIndex = 1;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90, align: "center", sortable: false,  render: updateExportApiClientDetailsFormatter, dataIndx: "" },
						{ title: "Client Id", hidden : true, width: 130, dataIndx: "client_id", align: "left", align: "left", halign: "center",
		                    filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Client Name", hidden : false, width: 130, dataIndx: "client_name", align: "left", align: "left", halign: "center",
		                    filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Updated By", hidden : false, width: 130, dataIndx: "updated_by", align: "left", align: "left", halign: "center" },
		                { title: "Updated Date", hidden : false, width: 130, dataIndx: "updated_date", align: "left", align: "left", halign: "center", render: apiClientDateRenderer }
		            ];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "AdditionalDatasource") {
					colM = [
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateExportAdditionalDSFormatter, dataIndx: "" },
						{ title: "Datasource ID", hidden : true, width: 130, dataIndx: "additionalDatasourceId", align: "left", align: "left", halign: "center",
		                	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Datasource Name", hidden : false, width: 130, dataIndx: "datasourceName", align: "left", align: "left", halign: "center",
		                	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
		                { title: "Datasource Product Name", hidden : false, width: 130, dataIndx: "databaseProductName", align: "left", halign: "center",
		                	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
		                { title: "Updated By", hidden : false, width: 130, dataIndx: "lastUpdatedBy", align: "left", halign: "center"  },
		                { title: "Last Updated Date", hidden : false, width: 130, dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: sitelayoutDateRenderer}
		            ];
					sortIndex = 1;
					isSelectTypeApplicable = false;
					additionalParameterKey = "";
				} else if(moduleType == "Scheduler") {
					colM = [
						{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false, render: updateExportSchedulerFormatter, dataIndx: "" },
						{ title: "", hidden: true, sortable : false, dataIndx: "scheduler_id" },
			            { title: "Scheduler Name", width: 130, align: "center", sortable : true, dataIndx: "scheduler_name", align: "left", halign: "center",
			            filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
			            { title: "Updated By", width: 160, align: "center", sortable : true, dataIndx: "modified_by", align: "left", halign: "center" },
			            { title: "Updated Date", width: 200, align: "center", sortable : true, dataIndx: "last_modified_date", align: "left", halign: "center", render: schedulerDateRenderer }
					];
					sortIndex = 2;
					isSelectTypeApplicable = true;
					if(selectedType != 0) {
						grid=  $("#"+moduleType+"").grid({
				      		gridId: gridID,
				      		colModel: colM,
				      		height:300,
                            pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
				            dataModel: {
				            	url: contextPath+"/cf/pq-grid-data",
				                sortIndx: sortIndex,
				                sortDir: "up"
				            },
				            additionalParameters: {"cr_schedulerTypeId":"str_"+selectedType}
				 		 });
					}
				}else if(moduleType == "Files") {
					colM = [
							{ title: "<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",true)'><i class='fa fa-check-square selectallcls' ></i></button>"
						         +"<button type='button' class='select_btn_new'  onclick='selectAll(\""+moduleType+"\",false)'><i class='fa fa-window-close deslectcls' ></i></button> ", width: 90, maxWidth: 90,  align: "center", sortable: false,  render: updateFileExportRenderer, dataIndx: "" },
							{ title: "File Upload Id", width: 160, dataIndx: "file_upload_id", align: "left", halign: "center",filter: { type: "textbox", condition: "contain", listeners: ["change"]}},
							{ title: "File Name", width: 160, dataIndx: "original_file_name", align: "left", halign: "center",
								filter: { type: "textbox", condition: "contain", listeners: ["change"]}},
							{ title: "File Bin Id", width: 130, dataIndx: "file_bin_id", align: "left", halign: "center", 
					        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} }								
					];
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
                        pageModel: { type: "remote", rPP: 10, strRpp: "{0}",rPPOptions:[10,20,50,100,500] },
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
        changeType();
		
	  	document.getElementById(moduleType).style.display = "block";
	  	if(evt != null) {
	  		evt.currentTarget.className += " active";
	  	}
	  	
	}

	function gridDateRenderer(uiObject) {
        return formatDate(uiObject.rowData.lastUpdatedTs);
	}
	
	function templateDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.updatedDate);
	}

	function schedulerDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.isAfterDate);
	}
	
	function autocompleteDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.lastUpdatedTs);
	}
	
	function notificationDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.updatedDate);
	}
	
	function resourceBundleDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.updatedDate);
	}
	 function messageTypeRenderer(uiObject) {
        if(uiObject.rowData.messageType == 0){
            return "Informative";
        } else if (uiObject.rowData.messageType == 1) {
             return "Warning";
        } else if(uiObject.rowData.messageType == 2) {
             return "Error";  
       }
        return "";
    }

	function dashboardDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.lastUpdatedTs);
	}

	function dashletDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.lastUpdatedTs);
	}

	function dynamicFormDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.lastUpdatedTs);
	}

	function fmDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.lastUpdatedTs);
	}
	
	function dynarestDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.lastUpdatedTs);
	}

	function permDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.lastUpdatedDate);
	}

	function sitelayoutDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.lastUpdatedTs);
	}
	
	function appConfDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.lastModifiedDate);
	}

	function apiClientDateRenderer(uiObject) {
		return formatDate(uiObject.rowData.updated_date);
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
		const id = uiObject.rowData.dynarestId;
		const name = uiObject.rowData.dynarestUrl;
		let version = getVersion(uiObject);
		const isSystemVariable =  uiObject.rowData.dynarestTypeId;
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
		const id = uiObject.rowData.roleName;
		const name = uiObject.rowData.roleDescription;
		const version = "NA";
		const isSystemVariable =  2;
		const moduleType = "ManageRoles";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function updateHelpManualRenderer(uiObject) {
		const id = uiObject.rowData.manualId;
		const name = uiObject.rowData.manualName;
		const version = "NA";
		const isSystemVariable =  uiObject.rowData.isSystemManual;
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

	function updateExportSchedulerFormatter(uiObject) {
		const id = uiObject.rowData.scheduler_id;
		const name = uiObject.rowData.scheduler_name;
		const version = "NA";
		const isSystemVariable =  uiObject.rowData.schedulerTypeId;
		const moduleType = "Scheduler";
		
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}

	function renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable) {
		let exportableData = new ExportableData(moduleType, id, name, version, isSystemVariable);
		if(isSystemVariable == 1) {
			if(customConfigExcludeList.indexOf(id) != -1 || isDeselectedAll == true) {
				return '<input type="checkbox" id="'+moduleType+id+'" name="'+moduleType+id+'" onchange="checkCustomVar(this.checked,\'' 
					+  id+ '\',\'' +  moduleType+ '\',\'' +  escape(name)+ '\',\'' +  version+ '\',\'' +  isSystemVariable+ '\');">'.toString();
			} else {
				map.get(moduleType).getExportableDataListMap().set(id, exportableData);
				return '<input type="checkbox" id="'+moduleType+id+'" name="'+moduleType+id+'" onchange="checkCustomVar(this.checked, \'' 
					+  id+ '\',\'' +  moduleType+ '\',\'' +  escape(name)+ '\',\'' +  version+ '\',\'' +  isSystemVariable+ '\');" checked="checked">'.toString();
			}
		} else {
			
			if(systemConfigIncludeList.indexOf(id) != -1) {
				map.get(moduleType).getExportableDataListMap().set(id, exportableData);
				return '<input type="checkbox" id="'+moduleType+id+'" name="'+moduleType+id+'" onchange="checkSystemVar(this.checked, \'' 
					+  id+ '\',\'' +  moduleType+ '\',\'' +   escape(name)+ '\',\'' +  version+ '\',\'' +  isSystemVariable+ '\');" checked="checked">'.toString();
			} else {
				return '<input type="checkbox" id="'+moduleType+id+'" name="'+moduleType+id+'" onchange="checkSystemVar(this.checked, \'' 
					+  id+ '\',\'' +  moduleType+ '\',\'' +   escape(name)+ '\',\'' +  version+ '\',\'' +  isSystemVariable+ '\');">'.toString();
			}
		}
	}
	
	function checkCustomVar(checkedCustom, id, moduleType, name, version, isSystemVariable) {
		isDeselectedAll = false;
		if($('#permissionChkBx').is(':checked'))
		{
		   permissions(checkedCustom, id, moduleType, name, version, isSystemVariable);
		}
		if($('#dashletChkBx').is(':checked') && moduleType=="Dashboard")
		{  
		  selectDashlet(checkedCustom, id, moduleType, name, version, isSystemVariable);
        }
		let exportableData = new ExportableData(moduleType, id, name, version, isSystemVariable);
		
		if (!checkedCustom) {
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
		if($('#permissionChkBx').is(':checked'))
		{
		   permissions(checkedSystem, id, moduleType, name, version, isSystemVariable);
		}
		if($('#dashletChkBx').is(':checked') && moduleType=="Dashboard")
		{
		  selectDashlet(checkedSystem, id, moduleType, name, version, isSystemVariable);
        }
		
		if (checkedSystem) {
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
		$("#expHeader").hide();
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
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_gridTypeId":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_gridTypeId":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "Templates") {
					if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_templateTypeId":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_templateTypeId":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "Autocomplete") {
					if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_autocompleteTypeId":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_autocompleteTypeId":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "Dashboard") {
					if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_dashboardType":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_dashboardType":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "Dashlets") {
					if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_dashletTypeId":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_dashletTypeId":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "DynamicForm") {
					if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_formTypeId":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_formTypeId":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "DynaRest") {
					if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_dynarestTypeId":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_dynarestTypeId":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "HelpManual") {
					if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_isSystemManual":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_isSystemManual":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "ResourceBundle") {
					   if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:"customResourceBundleListingGrid","cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:"customResourceBundleListingGrid"}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:"customResourceBundleListingGrid" ,"cr_resource_type":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:"customResourceBundleListingGrid","cr_resource_type":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "SiteLayout") {
					if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_moduleTypeId":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_moduleTypeId":"str_"+selectedType}
						}
			        }
				}  else if(moduleType == "Scheduler") {
					if(selectedType == 0) {
						if(isSelectAsPerDate == true) {
							postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID}
						}
			        } else {
			        	if(isSelectAsPerDate == true) {
			        		postData = {gridId:gridID,"cr_schedulerTypeId":"str_"+selectedType,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
						} else {
							postData = {gridId:gridID,"cr_schedulerTypeId":"str_"+selectedType}
						}
			        }
				} else if(moduleType == "ManageUsers" || moduleType == "ManageRoles") {
					if(isSelectAsPerDate == true) {
						postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
					} else {
						postData = {gridId:gridID}
					}
				} 
				else if(moduleType == "ApplicationConfiguration" || moduleType == "Permission" || moduleType == "AdditionalDatasource") {
					if(isSelectAsPerDate == true) {
						postData = {gridId:gridID,"cr_isAfterDate":"str_"+formatAfterDate($("#modifiedAfter").val()),"cmp_isAfterDate":"gte"}
					} else {
						postData = {gridId:gridID}
					}
				}else if(moduleType == "Notification") {
					if(isSelectAsPerDate == true) {
						postData = {gridId:gridID}
					} else {
						postData = {gridId:gridID}
					}
				}
				else if(moduleType == "Files" ) {
					if(isSelectAsPerDate == true) {
						postData = {gridId:gridID}
					} else {
						postData = {gridId:gridID}
					}
				}
                else if(moduleType == "ApiClientDetails" || moduleType == "ApiClientDetails") {
					if(isSelectAsPerDate == true) {
						postData = {gridId:gridID}
					} else {
						postData = {gridId:gridID}
					}
				}else if(moduleType == "FileManager") {
					if(isSelectAsPerDate == true) {
						postData = {gridId:gridID}
					} else {
						postData = {gridId:gridID}
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

		
	function selectAll(moduleType, checked) {
		   let pqgridNew = $("#" + moduleType).pqGrid();
		   let gridDataModel = pqgridNew.pqGrid("option", "dataModel");
		   let gridData = gridDataModel.data;
		   let gridRowArray = [];
		   for (let data of gridData.values()) {
		      let exportdata = getGridDetails(data, moduleType);
		      let moduleId = exportdata.moduleId;
		      if (moduleId.includes('.')) {
		         moduleId = moduleId.replace('.', "\\.")
		      }
		      var selected = $('#' + moduleType + moduleId).prop('checked');
		      if (checked) {
		         if (!selected) {
		            gridRowArray.push(data);
		         }
		      } else {
		         if (selected) {
		            gridRowArray.push(data);
		         }
		      }
		      gridRowArray = gridRowArray.sort(function (a, b) {
		         return (a - b);
		      })
		   }
		
		   for (let gridRowData of gridRowArray.values()) {
		      let exportableData = getGridDetails(gridRowData, moduleType);
		      if (exportableData.isSystemVariable == 2) {
		         checkSystemVar(checked, exportableData.moduleId, exportableData.moduleType, exportableData.moduleName, exportableData.moduleVersion, exportableData.isSystemVariable);
		      } else {
		         checkCustomVar(checked, exportableData.moduleId, exportableData.moduleType, exportableData.moduleName, exportableData.moduleVersion, exportableData.isSystemVariable);
		      }
		      let moduleId = exportableData.moduleId;
		      if (moduleId.includes('.')) {
		         moduleId = moduleId.replace('.', "\\.")
		      }
		      $('#' + moduleType + moduleId).prop("checked", checked);
		   }
        }
    function getGridDetails(rowData,modType)
		{
			 let moduleType;
		     let moduleId;
		     let isSystemVariable;
	         let name;
		     let version;
			 switch(modType) {
		     case "Grid":
				  moduleType=modType;
				  moduleId = rowData.gridId
		          isSystemVariable = rowData.gridTypeId;
	              name=rowData.gridName;
		          version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
             break;
             case "Templates":
                  moduleType="Templates";
				  moduleId = rowData.templateId
		          isSystemVariable = rowData.templateTypeId;
	              name=rowData.templateName;
		          version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
             break;
			 case "ResourceBundle":
                  moduleId = rowData.resourceKey
	              name=rowData.resourceBundleText;
 				  version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = 1;
   				 if(moduleId.startsWith("jws.")) {
						isSystemVariable = 2;
					}
                  moduleType="ResourceBundle"; 
             break;
             case "Autocomplete":
				  moduleId = rowData.autocompleteId
	              name=rowData.autocompleteDescription;
 				  version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = rowData.autocompleteTypeId;
                  moduleType="Autocomplete";         
             break;
             case "Dashboard":
				  moduleId = rowData.dashboardId
	              name=rowData.dashboardName;
 				  version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = rowData.dashboardType;
                  moduleType="Dashboard"; 
             break;
			 case "Dashlets":
		 		  moduleId = rowData.dashletId
	              name=rowData.dashletName;
 				  version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = rowData.dashletTypeId;
                  moduleType="Dashlets"; 
             break;
             case "DynamicForm":
				  moduleId = rowData.formId
	              name=rowData.formName;
 				  version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = rowData.formTypeId;
                  moduleType="DynamicForm";
             break;
             case "FileManager":
                  moduleId = rowData.fileBinId
	              name=rowData.fileTypeSupported;
 				  version = "NA";
		          isSystemVariable = 1;
                  moduleType="FileManager"; 
             break;
             case "DynaRest":
                  moduleId = rowData.dynarestId
	              name=rowData.dynarestUrl;
 				  version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = rowData.dynarestTypeId;
                  moduleType="DynaRest"; 
             break;
			 case "Permission":
                  moduleId = rowData.entityRoleId;
	              name=rowData.entityName;
 				  version = "NA";
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = 2;
                  moduleType="Permission"; 
             break;
			 case "SiteLayout":
                  moduleId = rowData.moduleId;
	              name=rowData.moduleName;
 				  version = "NA";
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = rowData.moduleTypeId;
                  moduleType="SiteLayout"; 
             break;
             case "ApplicationConfiguration":
                  moduleId = rowData.propertyMasterId
	              name=rowData.propertyName;
 				  version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = 2;
                  moduleType="ApplicationConfiguration";
             break;
			 case "ManageUsers":
                  moduleId = rowData.user_id
	              name=rowData.first_name+ " " +rowData.last_name;
 				  version = "NA";
		          isSystemVariable = 2;
                  moduleType="ManageUsers"; 
             break;
			 case "HelpManual":
		          moduleId = rowData.manualId
	              name=rowData.manualName;
 				  version = "NA";
		          isSystemVariable = rowData.isSystemManual;
                  moduleType="HelpManual"; 
			 break;
			 case "ApiClientDetails":
				  moduleId = rowData.client_id
	              name=rowData.client_name;
 				  version = "NA";
		          isSystemVariable = 1;
                  moduleType="ApiClientDetails"; 
			 break;
			 case "AdditionalDatasource":
				 moduleId = rowData.additionalDatasourceId
	              name=rowData.datasourceName;
 				  version = "NA";
		          isSystemVariable = 1;
                  moduleType="AdditionalDatasource"; 
			 break;
			 case "Scheduler":
		          moduleId = rowData.scheduler_id
	              name = rowData.scheduler_name;
 				  version = "NA";
		          isSystemVariable = rowData.schedulerTypeId;
                  moduleType="Scheduler"; 
			 break;
		     case "ManageRoles":
		          moduleId = rowData.roleName
	              name = rowData.roleDescription;
 				  version = "NA";
		          isSystemVariable = 2;
                  moduleType="ManageRoles"; 
			 break;
			 case "Files":
		          moduleId = rowData.file_upload_id
	              name = rowData.original_file_name;
 				  version = rowData.max_version_id;
		          if (version == null) {
		            version = "1.0";
		          }
		          isSystemVariable = 1;
                  moduleType="Files";
			 break;

          }
            let exportableData = new ExportableData(moduleType, moduleId, name, version, isSystemVariable);
            return exportableData;
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
				
				if(defaultMap != undefined && defaultMap.get(key) != undefined){
					exportableDataListMap1 = defaultMap.get(key).getExportableDataListMap();
				} else if(map != undefined && map.get(key) != undefined){
					exportableDataListMap1 = map.get(key).getExportableDataListMap();
				}
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

				if(moduleType == "Grid") {
					openTab(event, gridID, moduleType);
				}
			}
		});
		 $("#deselectAllChkBx").attr("disabled", "disabled");
	}
	
	function getPreviewHTMLTable() {
		var jsonArr = [];
		var tableRow="";
		var sortedMap = new Map();
		var keys = [];
			let count=0;
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
							count++;
							tableRow += '<tr>';
							//tableRow += '<td><label>'+count+'</label> </td>';
							tableRow += '<td><label>'+exportData.getModuleType()+'</label> </td>';
							tableRow += '<td><label>'+exportData.getModuleId()+'</label> </td>';
							tableRow += '<td><label>'+unescape(exportData.getModuleName())+'</label> </td>';
							tableRow += '<td><label>'+newVer+'</label> </td>';
							tableRow += '<td><span class= "grid_action_icons" title="Delete" onclick="deleteEntity(this,\''+exportData.getModuleType()+'\',\''+exportData.getModuleId()+'\',\''+unescape(exportData.getModuleName())+'\',+\''+newVer+'\',\''+unescape(exportData.getIsSystemVariable())+'\')"><i class="fa fa-trash "></i></span> </td>';
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
//		   const exportModuleHeader=document.getElementById("exportModuleHeader");
//             exportModuleHeader.innerHTML+="<span> Total Records ( " +jsonArr.length+ " ) <span>";
		return tableRow;
	}

	function gotoPrevPage() {
		$("#nextTab").hide();
		$("#nextTabBtn").hide();
		$("#mainTab").show();
		$("#mainTabBtn").show();
		$("#expHeader").show();
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

	function exportToLocal() {
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
    			url : contextPath+"/cf/etl",
    			async: false,
                contentType: "application/json",
    		    data : JSON.stringify(out),
    			success : function(data) {
    				if(data.startsWith("fail:")){
    					var errorMessageString = data.substring(5);
    					showMessage(errorMessageString, "error");
    				} else {
    					showMessage("Files exported successfully.", "success");
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
	
	function updateFileExportRenderer(uiObject) {
		const id = uiObject.rowData.file_upload_id;
		const name = uiObject.rowData.original_file_name;
		const fileType = uiObject.file_bin_id;
		const version = getVersion(uiObject);
		const isSystemVariable =  1;
		const moduleType = "Files";
		let systemConfigIncludeList = map.get(moduleType).getSystemConfigIncludeList();
		let customConfigExcludeList = map.get(moduleType).getCustomConfigExcludeList();
		
		return renderCheckBox(systemConfigIncludeList, customConfigExcludeList, moduleType, id, name, version, isSystemVariable);
		
	}
	
	function deleteEntity(currentRow,modType,modId,modName,newVer,isSystemVariable) {	
		
		let context = this;		
		$("#deleteEntityConfirm").html("Are you sure you want to delete <span style='font-weight: bold;'> \"" + modName + "\"<\span>?");
		$("#deleteEntityConfirm").dialog({
		bgiframe		 : true,
		autoOpen		 : true, 
		modal		 : true,
		closeOnEscape : true,
		draggable	 : true,
		resizable	 : false,
		title		 : "Delete",
		buttons		 : [{
				text	:"Cancel",
				class   :"btn btn-secondary",
				click	: function() { 
					$(this).dialog('close');
				},
			},
			{
				text    : "Delete",
				class   :"btn btn-primary",
				click	: function(){
					$(this).dialog('close');
					context.removeEntity(currentRow,modType,modId,modName,newVer,isSystemVariable);
				}
           	},
       ],	
		open		: function( event, ui ) {
			 $('.ui-dialog-titlebar')
		   	    .find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
		       .prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
       		}	
	   });
		
		 
		
		
	    
    }

    function removeEntity(currentRow,modType,modId,moduleName,newVer,isSystemVariable){
	     var i = currentRow.parentNode.parentNode.rowIndex;
         var jsonArr =map.get("htmlTableJSON");
         jsonArr.splice(i-1,1);
         map.set("htmlTableJSON", jsonArr);
         if(isSystemVariable== 1){
         //map.get(modType).systemConfigIncludeList = map.get(modType).systemConfigIncludeList.filter((element) => {return element != modId});
             checkCustomVar(false, modId, modType, moduleName,newVer, isSystemVariable);
         }else if(isSystemVariable== 2){
          // map.get(modType).getCustomConfigExcludeList().push(modId);
                checkSystemVar(false, modId, modType, moduleName,newVer, isSystemVariable);
         }
		 map.get(modType).getExportableDataListMap().delete(modId);
         document.getElementById("htmlTable").deleteRow(i);
//         const exportModuleHeader=document.getElementById("exportModuleHeader");
//         exportModuleHeader.remove(span);
//         exportModuleHeader.innerHTML+="<span>Total Records ( " +jsonArr.length+ " ) <span>";
    }


	function formatAfterDate(date) {
	    var d = new Date(date),
	        month = '' + (d.getMonth() + 1),
	        day = '' + d.getDate(),
	        year = d.getFullYear();	
	    if (month.length < 2) 
	        month = '0' + month;
	    if (day.length < 2) 
	        day = '0' + day;	
	    return [year, month, day].join('-');
    }

   function selectPermissions(){
	if($('#permissionChkBx').is(':checked') && !$('#deselectAllChkBx').is(':disabled')) {
		$("#permissionWarningDiv").html("<b>There can be already exisiting entity whose permission has not been selected.</b>");	    
		$("#permissionWarningDiv").dialog({
		                            bgiframe		 : true,
									autoOpen		 : true, 
									modal		 : true,
									clcoseOnEscape : true,
									draggable	 : true,
									resizable	 : false,
				                    title		 : "Select Permission",
                                    position: {
                                             my: "center", at: "center"
                                     },
		                            buttons: [ { text: "OK" ,class   :"btn btn-secondary", click: function() { $( this ).dialog( "close" ); } } ]
		                          });
		      }
	}
   
   function permissions(checked, id, moduleType, name, version, isSystemVariable){
	let modType="Permission";
	let moduleElement=document.getElementsByClassName("tablinks active");
	let moduleId;
	if (moduleElement && moduleElement.length > 0){
         moduleId=moduleElement[0].id;
    }
	 $.ajax({
				async : false,
		        type : "GET",
    			url : contextPath+"/cf/permissions",
    		    data : {entityId:id,moduleId:moduleId},
    			success : function(data) {
				            let exportableData;
							let exportableDataListMapNew;
							let exportObjNew;
							let systemConfigIncludeListNew=[];
							let customConfigExcludeListNew=[];
				           
					        $.each(data, function(key,val){
						    let entityRoleId=val.entityRoleId;
						    if(entityRoleId != null && entityRoleId != "") {
							   exportableData = new ExportableData(modType, entityRoleId, val.entityName, "NA", isSystemVariable);
							   if(map.get(modType) == null) {
								  exportableDataListMapNew = new Map();
							      exportObjNew = new ImportExportConfig(systemConfigIncludeListNew, customConfigExcludeListNew, null, null, modType,exportableDataListMapNew);					
								  map.set(modType, exportObjNew);
								  defaultMap.set(modType, exportObjNew);
								  map.get(modType).getExportableDataListMap().set(entityRoleId, exportableData);
								  defaultMap.get(modType).getExportableDataListMap().set(entityRoleId, exportableData);
						        }
							    if(checked){
								   if(!$('#' + modType + entityRoleId).prop("checked")){
						              map.get(modType).getSystemConfigIncludeList().push(entityRoleId);
                                      map.get(modType).getExportableDataListMap().set(entityRoleId, exportableData);
						              $('#' + modType + entityRoleId).prop("checked", true);
									  let count = $('#selectedCount_'+modType).text();
									  let countInt = parseInt(count);
								      $('#selectedCount_'+modType).text(countInt+1);
								    }
								}else
								    { let count = $('#selectedCount_'+modType).text();
									  let countInt = parseInt(count);
									  if(map.get(modType).getSystemConfigIncludeList().includes(entityRoleId)){
										 map.get(modType).systemConfigIncludeList 
								         = map.get(modType).systemConfigIncludeList.filter((element) => {return element != entityRoleId});
										 map.get(modType).getExportableDataListMap().delete(entityRoleId);
										 $('#selectedCount_'+modType).text(countInt-1);
										 $('#' + modType + entityRoleId).prop("checked", false);
										}
									}
						          }
			                 
					           });
			    },
    		        
    		    error : function(xhr, error){
    		    	showMessage("Error occurred while selecting/deselecting dashlet", "error");
    		    },
    		        	
    		});
      }
   


     function selectDashlet(check, id, moduleType, name, version, isSystemVariable){
	     modType="Dashlets";
	     $.ajax({
		        async : false,
		        type : "GET",
		        url :  contextPath+"/cf/dds", 
		        data : {
		        	dashboardId:id,
		        },
    			success : function(data) {
				            let exportableData;
							let exportableDataListMapNew;
							let exportObjNew;
							let systemConfigIncludeListNew=[];
							let customConfigExcludeListNew=[];
				           
					        $.each(data, function(key,val){
						    let dashletId=val.dashletId;
		                    let dashletName=val.dashletName;
						    if(dashletId != null && dashletId != "") {
							let max_version = val.max_version_id;
							   if (max_version == null||max_version == 'NaN') {
							       max_version = "1.0";
							    }
								   exportableData = new ExportableData(modType, dashletId, dashletName, max_version , isSystemVariable);
								   if(map.get(modType) == null) {
									  exportableDataListMapNew = new Map();
									  exportObjNew = new ImportExportConfig(systemConfigIncludeListNew, customConfigExcludeListNew, null, null, modType,exportableDataListMapNew);					
									  map.set(modType, exportObjNew);
									  defaultMap.set(modType, exportObjNew);
									  map.get(modType).getExportableDataListMap().set(dashletId, exportableData);
									  defaultMap.get(modType).getExportableDataListMap().set(dashletId, exportableData);
						            }
							        if (check) {								
								        //  if(!$('#' + modType + dashletId).prop("checked")){
						                if(map.get(modType).getCustomConfigExcludeList().includes(dashletId)){ 
										   var i = map.get(modType).getCustomConfigExcludeList().indexOf(dashletId);
										   if(i != -1) {
											  map.get(modType).getCustomConfigExcludeList().splice(i, 1);
										   }
											  map.get(modType).getExportableDataListMap().set(dashletId, exportableData);
						                      $('#'+modType+dashletId).prop("checked", true);
									          let count = $('#selectedCount_'+modType).text();
											  let countInt = parseInt(count);
											  $('#selectedCount_'+modType).text(countInt+1);
										   }
									//	}
									}
									else
									{
										let count = $('#selectedCount_'+modType).text();
										let countInt = parseInt(count);
									    if(!map.get(modType).getCustomConfigExcludeList().includes(dashletId)){
										    map.get(modType).getCustomConfigExcludeList().push(dashletId);
										  	map.get(modType).getExportableDataListMap().delete(dashletId);
	                                        $('#'+ modType + dashletId).prop("checked",false);
											$('#selectedCount_'+modType).text(countInt-1);
										}
									 }
						          }
					            });
			    },
    		    error : function(xhr, error){
    		    	showMessage("Error occurred while selecting/deselecting permission", "error");
    		    },
    		});	
	}
			
       


	