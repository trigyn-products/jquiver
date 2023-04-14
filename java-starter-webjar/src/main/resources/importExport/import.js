
function importFile() {
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
				localStorage.removeItem("imporatableData");
				localStorage.removeItem("importedIdList");
				localStorage.removeItem("exportedFormatObject");
				idList = new Array();
				imporatableData = data;
				localStorage.setItem("imporatableData", imporatableData);
				let jsonObject = JSON.parse(imporatableData);
				for (var value in jsonObject) {
					zipFileJsonDataMap.set(value, jsonObject[value])
				}

				var exportedFormatObject = zipFileJsonDataMap.get("exportedFormatObject")
				localStorage.setItem("exportedFormatObject", JSON.stringify(exportedFormatObject));
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
		},
		error: function(textStatus, errorThrown) {
			$("#htmlTable > tbody").empty();
			showMessage("Error while importing data", "error");
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

				let existingVersionDisplay = versionMap.get(moduleType.toLowerCase() + moduleID);
				if (existingVersion == "NE" && isNonVersioningModule == "true") {
					existingVersionDisplay = "NA";
				}
				tableRow += '<tr>';
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
	let entityName = "";
	localStorage.setItem("imporatableData", imporatableData);

	if (moduleType == "Grid") {
		entityName = "jq_grid_details";
		formId = jsonObject["formId"];
		moduleName = jsonObject["gridName"];
		moduleTypeStr = "grid";

	} else if (moduleType == "Templates") {
		entityName = "jq_template_master";
		saveURL = "/cf/stdv";
		moduleName = jsonObject["templateName"];
		moduleTypeStr = "template";

	} else if (moduleType == "ResourceBundle") {
		entityName = "jq_resource_bundle";
		saveURL = "/cf/srbv";
		moduleName = jsonObject["id"];
		moduleTypeStr = "resourceBundle";

	} else if (moduleType == "Autocomplete") {
		entityName = "jq_autocomplete_details";
		moduleName = jsonObject["autocompleteDesc"];
		moduleTypeStr = "autocomplete";

	} else if (moduleType == "Notification") {
		entityName = "jq_generic_user_notification";
		formId = jsonObject["formId"];
		moduleName = jsonObject["targetPlatform"];
		moduleTypeStr = "notification";

	} else if (moduleType == "Dashboard") {
		entityName = "jq_dashboard";
		saveURL = "/cf/sdbv";
		moduleName = jsonObject["dashboardName"];
		moduleTypeStr = "dashboard";

	} else if (moduleType == "Dashlets") {
		entityName = "jq_dashlet";
		saveURL = "/cf/sdlv";
		moduleName = jsonObject["dashletName"];
		moduleTypeStr = "dashboard";

	} else if (moduleType == "DynamicForm") {
		entityName = "jq_dynamic_form";
		moduleName = jsonObject["formName"];
		moduleTypeStr = "dynamicForm";

	} else if(moduleType == "FileManager") { 
			entityName = "jq_file_upload_config";
			moduleName = jsonObject["fileBinId"];
			moduleTypeStr = "fileManager";
			isNonVersioningModule = "true";
			nonVersioningFetchURL = "/cf/fuj";
			saveURL = "/cf/sfuc";
			
	} else if (moduleType == "DynaRest") {
		entityName = "jq_dynamic_rest_details";
		formId = jsonObject["formId"];
		moduleName = jsonObject["jwsDynamicRestUrl"];
		moduleTypeStr = "dynarest";

	} else if (moduleType == "Permission") {
		moduleName = jsonObject["entityName"];
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
		entityName = "jq_property_master";
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

	$("#entityName").val(entityName);
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
	let exportedFormatObject = localStorage.getItem("exportedFormatObject");
	$.ajax({
		url: contextPath + '/cf/importConfig',
		type: "POST",
		async: false,
		data: {
			exportedFormatObject: exportedFormatObject,
			importId: entityId,
			moduleType: moduleType
		},
		success: function(data) {
			if (data.startsWith("fail:")) {
				var errorMessageString = data.substring(5);
				showMessage(errorMessageString, "error");
			} else {
				if (!idList.includes(moduleType.toLowerCase() + entityId)) {
					idList.push(moduleType.toLowerCase() + entityId);
					localStorage.setItem("importedIdList", JSON.stringify(idList));
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
	let exportedFormatObject = localStorage.getItem("exportedFormatObject");
	if (isDataAvailableForImport == true) {
		$.ajax({
			url: contextPath + '/cf/importAll',
			type: "POST",
			async: false,
			data: {
				imporatableData: imporatableData,
				importedIdList: JSON.stringify(idList)
			},
			success: function(data) {
				if (data.startsWith("fail:")) {
					var errorMessageString = data.substring(5);
					showMessage(errorMessageString, "error");
				} else {
					$("#importAllBtn").attr("disabled", true);
					backToPreviousPage();
					showMessage("All data imported.", "success");
				}
			},
			error: function(textStatus, errorThrown) {
				if (data.startsWith("fail:")) {
					var errorMessageString = data.substring(5);
					showMessage(errorMessageString, "error");
				} else {
					showMessage("Error while importing data", "error");
				}
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
	$.ajax({
		url: contextPath + '/cf/ifl',
		type: "POST",
		success: function(data) {
			if (data.startsWith("fail:")) {
				let errorMessageString = data.substring(5);
				showMessage(errorMessageString, "error");
			} else {
				localStorage.removeItem("imporatableData");
				localStorage.removeItem("importedIdList");
				localStorage.removeItem("exportedFormatObject");
				idList = new Array();
				imporatableData = data;
				localStorage.setItem("imporatableData", imporatableData);
				let jsonObject = JSON.parse(imporatableData);
				for (var value in jsonObject) {
					zipFileJsonDataMap.set(value, jsonObject[value])
				}

				var exportedFormatObject = zipFileJsonDataMap.get("exportedFormatObject")
				localStorage.setItem("exportedFormatObject", JSON.stringify(exportedFormatObject));
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
		},
		error: function(textStatus, errorThrown) {
			$("#htmlTable > tbody").empty();
			showMessage("Error while importing data", "error");
		}
	});
}
