var exportedFormatObject;
var imporatableData;
var importedIdList;
function importFile() {
	
	$.blockUI({ message: "<img src='"+contextPathHome+"/webjars/1.0/images/loading.gif' />" });
		
	importedFileData = new FormData(document.getElementById("importForm"))
	$.ajax({
		url: contextPath + '/cf/impF',
		type: "POST",
		data: importedFileData,
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false,
		success: function(data) {
			if (data.startsWith("fail:")) {
				let errorMessageString = data.substring(5);
				showMessage(errorMessageString, "error");
			} else {
//				localStorage.removeItem("imporatableData");
//				localStorage.removeItem("importedIdList");
//				localStorage.removeItem("exportedFormatObject");
				idList = new Array();
				imporatableData = data;
				//localStorage.setItem("imporatableData", imporatableData);
				let jsonObject = JSON.parse(imporatableData);
				for (var value in jsonObject) {
					zipFileJsonDataMap.set(value, jsonObject[value])
				}

				var exportedFormatObject = zipFileJsonDataMap.get("exportedFormatObject")
				//localStorage.setItem("exportedFormatObject", JSON.stringify(exportedFormatObject));
				exportedFormatObject=exportedFormatObject;
				var versionMap = new Map();
				let versionJson = JSON.parse(zipFileJsonDataMap.get("versionMap"));
				for (var value in versionJson) {
					versionMap.set(value, versionJson[value])
				}

				var crcMap = new Map();
				let crcJson = JSON.parse(zipFileJsonDataMap.get("crcMap"));
				for (var value in crcJson) {
					crcMap.set(value, crcJson[value])
				}

				loadTable(zipFileJsonDataMap, versionMap, crcMap);
			}
			
				 $.unblockUI();
		},
		error: function(textStatus, errorThrown) {
			$("#htmlTable > tbody").empty();
			showMessage("Error while importing data", "error");
			
			 $.unblockUI();
		}
	});
}

function loadTable(zipFileJsonDataMap, versionMap, crcMap) {
	let isError = "false";
	if (isError == "false") {

		let completeZipJsonData = zipFileJsonDataMap.get("completeZipJsonData");
		var htmlTableJsonArray = JSON.parse(completeZipJsonData);
		var tableRow = "";
		$("#htmlTable > tbody").empty();
		count=0;
		if (htmlTableJsonArray != null) {
			for (var i = 0; i < htmlTableJsonArray.length; i++) {
				let importVersion = htmlTableJsonArray[i].moduleVersion;
				let moduleType = htmlTableJsonArray[i].moduleType;
				let moduleID = htmlTableJsonArray[i].moduleID;
				let existingVersion = versionMap.get(moduleType.toLowerCase() + moduleID);
				let isNonVersioningModule = "false";
				if (moduleType == "FileManager" || moduleType == "Permission" || moduleType == "SiteLayout"
					|| moduleType == "ManageUsers" || moduleType == "ManageRoles" || moduleType == "HelpManual") {
					isNonVersioningModule = "true";
				}
				let isCheckSumUpdated = crcMap.get(moduleType.toLowerCase() + moduleID);
               if(isCheckSumUpdated){
				let existingVersionDisplay = versionMap.get(moduleType.toLowerCase() + moduleID);
				if (existingVersion == "NE" && isNonVersioningModule == "true") {
					existingVersionDisplay = "NA";
				}
				count++;
				tableRow += '<tr>';
				tableRow += '<td><label>' + count + '</label> </td>';
				tableRow += '<td><label>' + moduleType + '</label> </td>';
				tableRow += '<td><label>' + moduleID + '</label> </td>';
				tableRow += '<td><label s>' + htmlTableJsonArray[i].moduleName + '</label> </td>';
				tableRow += '<td><label id = "lblExistingVersion' + moduleID + '">' + existingVersionDisplay + '</label> </td>';
				tableRow += '<td><label>' + importVersion + '</label> </td>';
				tableRow += '<td>';

				if ((existingVersion == "NA" && isNonVersioningModule == "false")
					|| (existingVersion == "NE" && isNonVersioningModule == "true")
					|| idList.includes(moduleType.toLowerCase() + moduleID) || isCheckSumUpdated == false) {
					tableRow += '<button id="btnCompare' + moduleID + '" class="btn" name="importInd" type="button" disabled="disabled" '
						+ 'onclick="submitRevisionForm(\'' + moduleType + '\','
						+ '\'' + moduleID + '\')" >'
						+ '<i class="fa fa-exchange"></i></button>';
				} else {
					tableRow += '<button id="btnCompare' + moduleID + '" class="btn" name="importInd" type="button" '
						+ 'onclick="submitRevisionForm(\'' + moduleType + '\','
						+ '\'' + moduleID + '\')" >'
						+ '<i class="fa fa-exchange"></i></button>';
				}

				if (idList.includes(moduleType.toLowerCase() + moduleID) || isCheckSumUpdated == false) {
					tableRow += '<button id="btnImport' + moduleID + '" class="btn" name="importInd" type="button"  disabled="disabled" '
						+ 'onclick="importSingle(\'' + moduleType + '\','
						+ '\'' + moduleID + '\')" >'
						+ '<i class="fa fa-download"></i></button>';
					if (!idList.includes(moduleType.toLowerCase() + moduleID)) {
						idList.push(moduleType.toLowerCase() + moduleID);

					}
				} else {
					tableRow += '<button id="btnImport' + moduleID + '" class="btn" name="importInd" type="button" '
						+ 'onclick="importSingle(\'' + moduleType + '\','
						+ '\'' + moduleID + '\')" >'
						+ '<i class="fa fa-download"></i></button>';
					isDataAvailableForImport = true;
				}
				tableRow += '</td>';
				tableRow += '</tr>';
			}
			}
		}

		$("#htmlTable > tbody").append(tableRow);
		
	} else {
		$("#htmlTable > tbody").empty();
	}
}

function submitRevisionForm(moduleType, entityId) {
	let entity;
	if (moduleType == "ResourceBundle") {
		 entity = zipFileJsonDataMap.get(entityId);
	} else{
		 entity = zipFileJsonDataMap.get(moduleType.toLowerCase() + entityId);
	}
	let jsonObject = JSON.parse(entity);
	let formId = "";
	let moduleName = "";
	let saveURL = "";
	let isNonVersioningModule = "false";
	let nonVersioningFetchURL = "";
	let cmvEntityName = ""; 
	//localStorage.setItem("imporatableData", imporatableData);
	imporatableData=imporatableData;
	if (moduleType == "Grid") {
		cmvEntityName = "jq_grid_details";
		formId = jsonObject["formId"];
		moduleName = jsonObject["gridName"];
		moduleTypeStr = "grid";

	} else if (moduleType == "Templates") {
		cmvEntityName = "jq_template_master";
		saveURL = "/cf/stdv";
		moduleName = jsonObject["templateName"];
		moduleTypeStr = "template";

	} else if (moduleType == "ResourceBundle") {
		cmvEntityName = "jq_resource_bundle";
		saveURL = "/cf/srbv";
		moduleName = jsonObject["id"];
		moduleTypeStr = "resourceBundle";

	} else if (moduleType == "Autocomplete") {
		cmvEntityName = "jq_autocomplete_details";
		moduleName = jsonObject["autocompleteDesc"];
		moduleTypeStr = "autocomplete";

	} else if (moduleType == "Notification") {
		cmvEntityName = "jq_generic_user_notification";
		formId = jsonObject["formId"];
		moduleName = jsonObject["targetPlatform"];
		moduleTypeStr = "notification";

	} else if (moduleType == "Dashboard") {
		cmvEntityName = "jq_dashboard";
		saveURL = "/cf/sdbv";
		moduleName = jsonObject["dashboardName"];
		moduleTypeStr = "dashboard";

	} else if (moduleType == "Dashlets") {
		cmvEntityName = "jq_dashlet";
		saveURL = "/cf/sdlv";
		moduleName = jsonObject["dashletName"];
		moduleTypeStr = "dashboard";

	} else if (moduleType == "DynamicForm") {
		cmvEntityName = "jq_dynamic_form";
		moduleName = jsonObject["formName"];
		moduleTypeStr = "dynamicForm";

	} else if(moduleType == "FileManager") { 
			cmvEntityName = "jq_file_upload_config";
			moduleName = jsonObject["fileBinId"];
			moduleTypeStr = "fileManager";
			isNonVersioningModule = "true";
			nonVersioningFetchURL = "/cf/fuj";
			saveURL = "/cf/sfuc";
			
	} else if (moduleType == "DynaRest") {
		cmvEntityName = "jq_dynamic_rest_details";
		formId = jsonObject["formId"];
		moduleName = jsonObject["jwsDynamicRestUrl"];
		moduleTypeStr = "dynarest";

	} else if (moduleType == "Permission") {
		moduleName = jsonObject["cmvEntityName"];
		moduleTypeStr = "permission";
		isNonVersioningModule = "true";
		nonVersioningFetchURL = "/cf/puj";
		saveURL = "/cf/sjra";

	} else if (moduleType == "SiteLayout") {
		moduleName = jsonObject["moduleName"];
		moduleTypeStr = "sitelayout";
		isNonVersioningModule = "true";
		nonVersioningFetchURL = "/cf/muj";
		saveURL = "/cf/sml";
	} else if (moduleType == "ApplicationConfiguration") {
		cmvEntityName = "jq_property_master";
		formId = jsonObject["formId"];
		moduleName = jsonObject["propertyName"];
		moduleTypeStr = "propertyMaster";

	} else if (moduleType == "ManageUsers") {
		moduleName = jsonObject["firstName"] + " " + jsonObject["lastName"];
		moduleTypeStr = "manageusers";
		isNonVersioningModule = "true";
		nonVersioningFetchURL = "/cf/mjwsu";
		saveURL = "/cf/sjwsu";
	} else if (moduleType == "ManageRoles") {
		moduleName = jsonObject["roleName"];
		moduleTypeStr = "manageroles";
		isNonVersioningModule = "true";
		nonVersioningFetchURL = "/cf/mjwsr";
		saveURL = "/cf/sjwsr";
	}
	$("#cmvEntityName").val(cmvEntityName);
	$("#entityId").val(entityId);
	$("#moduleName").val(moduleName);
	$("#moduleType").val(moduleTypeStr);
	$("#formId").val(formId);
	$("#saveUrl").val(saveURL);
	$("#isNonVersioningModule").val(isNonVersioningModule);
	$("#nonVersioningFetchURL").val(nonVersioningFetchURL);
	$("#isImport").val("true");
	localStorage.setItem("importJson", entity);
	$("#importJson").val(entity);
	$("#previousPageUrl").val("/cf/vimp");
	$("#revisionForm").submit();
}

function importSingle(moduleType, entityId) {
	//let exportedFormatObject = localStorage.getItem("exportedFormatObject");
	const out = Object.create(null);
    out["exportedFormatObject"]=exportedFormatObject;
    if(idList.length>0){
		out["entityId"]= entityId;
		out["moduleType"]= moduleType;
    }
	$.ajax({
		url: contextPath + '/cf/importConfig',
		type: "POST",
		async: false,
		data:JSON.stringify(out),
		success: function(data) {
			if (data.startsWith("fail:")) {
				var errorMessageString = data.substring(5);
				showMessage(errorMessageString, "error");
			} else {
				if (!idList.includes(moduleType.toLowerCase() + entityId)) {
					idList.push(moduleType.toLowerCase() + entityId);
					//localStorage.setItem("importedIdList", JSON.stringify(idList));
					importedIdList=JSON.stringify(idList);
				}
				$("#lblExistingVersion" + entityId).text(data);
				$("#btnImport" + entityId).attr("disabled", true);
				$("#btnCompare" + entityId).attr("disabled", true);
				showMessage("Data imported.", "success");
			}
		},
		error: function(textStatus, errorThrown) {
			showMessage("Error while importing data", "error");
		}
	});
}

function importAll() {

	$.blockUI({ message: "<img src='"+contextPathHome+"/webjars/1.0/images/loading.gif' />" });
	
//	let exportedFormatObject = localStorage.getItem("exportedFormatObject");
    const out = Object.create(null);
    out["imporatableData"]=imporatableData;
    if(idList.length>0){
		out["importedIdList"]= idList;
    }
	if (isDataAvailableForImport == true) {
		$.ajax({
			url: contextPath + '/cf/importAll',
			type: "POST",
			async: false,			
			 contentType: "application/json",
			data : JSON.stringify(out),
			success: function(data) {
				if (data.startsWith("fail:")) {
					var errorMessageString = data.substring(5);
					showMessage(errorMessageString, "error");
				} else {
					$("#importAllBtn").attr("disabled", true);
					backToPreviousPage();
					showMessage("All data imported.", "success");
				}
				
			 $.unblockUI();
			},
			error: function(textStatus, errorThrown) {
				if (data.startsWith("fail:")) {
					var errorMessageString = data.substring(5);
					showMessage(errorMessageString, "error");
				} else {
					showMessage("Error while importing data", "error");
				}
				 $.unblockUI();
			}
		});
	} else {
		showMessage("No data available for importing", "info");
	}
	
	
}

function backToPreviousPage() {
	location.href = contextPath + "/cf/home";
}


function showFileName(event) {
	var input = event.srcElement;
	var fileName = input.files[0].name;
	var infoArea = document.getElementById('file-upload-filename');
	infoArea.textContent = fileName;
	$('#parentUploadBox').width($('#file-upload-filename').width() + 160);
	$('#parentUpload').width($('#file-upload-filename').width() + 160);
	$('#uploadBox').width($('#file-upload-filename').width() + 160);
	$('#formUploadBox').width($('#file-upload-filename').width() + 160);
}

function importFromLocal() {
	$.blockUI({ message: "<img src='"+contextPathHome+"/webjars/1.0/images/loading.gif' />" });
	importedFileData = new FormData(document.getElementById("importForm"))
	$.ajax({
		url: contextPath + '/cf/ifl',
		type: "POST",
		data: importedFileData,
		enctype: 'multipart/form-data',	
		processData: false,
		contentType: false,
		success: function(data) {
			if (data.startsWith("fail:")) {
				let errorMessageString = data.substring(5);
				showMessage(errorMessageString, "error");
			} else {
//				localStorage.removeItem("imporatableData");
//				localStorage.removeItem("importedIdList");
//				localStorage.removeItem("exportedFormatObject");
				idList = new Array();
				imporatableData = data;
				//localStorage.setItem("imporatableData", imporatableData);
				let jsonObject = JSON.parse(imporatableData);
				for (var value in jsonObject) {
					zipFileJsonDataMap.set(value, jsonObject[value])
				}

				var exportedFormat= zipFileJsonDataMap.get("exportedFormatObject")
				//localStorage.setItem("exportedFormatObject", JSON.stringify(exportedFormatObject));
				exportedFormatObject=exportedFormat;
				var versionMap = new Map();
				let versionJson = JSON.parse(zipFileJsonDataMap.get("versionMap"));
				for (var value in versionJson) {
					versionMap.set(value, versionJson[value])
				}

				var crcMap = new Map();
				let crcJson = JSON.parse(zipFileJsonDataMap.get("crcMap"));
				for (var value in crcJson) {
					crcMap.set(value, crcJson[value])
				}

				loadTable(zipFileJsonDataMap, versionMap, crcMap);
			}
				 $.unblockUI();
		},
		error: function(textStatus, errorThrown) {
			$("#htmlTable > tbody").empty();
			showMessage("Error while importing data", "error");
				 $.unblockUI();
		}
	});
}

/*For displaying input Text/File upload based on From Local,From File .*/
function changeImportType() {
	var type_id = $("#importTypeSelect").val();
	if (type_id == '1') {
		$("#showFilePath").show();
		$("#filepathLabel").show();
		$("#showFileupload").hide();		
	} else {
		$("#showFilePath").hide();
		$("#filepathLabel").hide();
		$("#showFileupload").show();
	}
}