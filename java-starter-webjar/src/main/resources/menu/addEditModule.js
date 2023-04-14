class AddEditModule {
	constructor(moduleTypeId, parentModuleId) {
		this.moduleTypeId = moduleTypeId;
		this.parentModuleId = parentModuleId;
	}

	saveModule = function() {
		let context = this;
		let moduleDetails = new Object();
		let isDataSaved = false;
		let targetLookupId = $("#targetLookupType").find(":selected").val();

		if ($("#moduleId").val() !== "") {
			moduleDetails.moduleId = $("#moduleId").val();
		}
		/** Added validation for not setting Target URL , if the module is already a home-page. */
		if ($("#isHomePage").val() !== "") {
			moduleDetails.isHomePage = $("#isHomePage").val();
		}
		if (moduleDetails.isHomePage == '1') {

			if (targetLookupId === "7") {
				showMessage("Target URLs can't be set as Home Page.", "warn");
				return false;
			}
		}
		/** Ends Here */

		if (context.validateMandatoryFileds() == false) {
			$("#errorMessage").show();
			return false;
		}

		if (context.validateExistingData()) {
			return false;
		}
		if (targetLookupId === "7") {
			moduleDetails.moduleURL = $("#externalURL").val();
			moduleDetails.targetTypeId = $("#targetType").val();
		} else {
			moduleDetails.moduleURL = $("#moduleURL").val();
			moduleDetails.targetTypeId = $("#targetTypeNameId").val();
		}

		moduleDetails.moduleName = $("#moduleName").val();
		moduleDetails.parentModuleId = $("#parentModuleName").find(":selected").val();
		moduleDetails.sequence = $("#sequence").val();
		moduleDetails.targetLookupId = targetLookupId;
		//moduleDetails.targetTypeId = $("#targetTypeNameId").val();
		moduleDetails.isInsideMenu = $("#isInsideMenu").val();
		moduleDetails.includeLayout = $("#includeLayout").val();
		let contextHeaderJson = context.headerJson();
		if (!contextHeaderJson['Powered-By']) {
			contextHeaderJson['Powered-By'] = "JQuiver";
		}
		moduleDetails.headerJson = JSON.stringify(contextHeaderJson);
		if ($('#openInNewTab').is(':checked')) {
			moduleDetails.openInNewTab = 1;
		}
		else {
			moduleDetails.openInNewTab = 0;
		}
		moduleDetails.menuStyle = $("#menuStyle").val();

		let reqParam = context.requestParamJson();
		if (reqParam == -1) {
			return false;
		}
		moduleDetails.requestParamJson = JSON.stringify(reqParam);
		$.ajax({
			type: "POST",
			url: contextPath + "/cf/sm",
			async: false,
			contentType: "application/json",
			data: JSON.stringify(moduleDetails),
			success: function(data) {
				$("#errorMessage").hide();
				context.saveEntityRoleAssociation(data);
				context.parentModuleId = $("#parentModuleName").find(":selected").val();
				showMessage("Information saved successfully", "success");
				isDataSaved = true;
			},

			error: function(xhr, error) {
				showMessage("Error occurred while saving", "error");
			},

		});
		return isDataSaved;
	}

	validateMandatoryFileds = function() {
		$('#errorMessage').html("");
		let moduleName = $("#moduleName").val().trim();
		if (moduleName === "") {
			$("#moduleName").focus();
			$('#errorMessage').html("Please enter module name");
			return false;
		}

		let contextType = $("#targetLookupType").find(":selected").val();
		if (contextType === "") {
			$("#targetLookupType").focus();
			$('#errorMessage').html("Please select context type");
			return false;
		}

		let contextName = $("#targetTypeName").val();
		if (contextName === "" && contextType != "6" && contextType != "7") {
			$("#targetTypeName").focus();
			$('#errorMessage').html("Please select context name");
			return false;
		}

		let sequence = $("#sequence").val().trim();
		if (sequence === "" && ($("#insideMenuCheckbox").prop("checked")) === true) {
			$("#sequence").focus();
			$('#errorMessage').html("Please enter sequence number");
			return false;
		}

		let moduleURL = $("#moduleURL").val().trim();
		if ((moduleURL === "" || moduleURL.length > 200 || moduleURL.indexOf("#") != -1 || moduleURL.indexOf(" ") != -1) && contextType != "7" && contextType != "6") {
			$("#moduleURL").focus();
			$('#errorMessage').html("Please enter valid URL");
			return false;
		}


		if (contextType === "7") {
			let externalURL = $("#externalURL").val().trim();
			if (externalURL === "" || externalURL.indexOf(" ") != -1) {
				$("#externalURL").focus();
				$('#errorMessage').html("Please enter valid URL");
				return false;
			}
		}
		var errorExist = false;
		$.each($('.key'), function() {
			if ($(this).val().length == 0) {
				$('#errorMessage').html("Please enter response header key");
				errorExist = true;
			}

		});
		$.each($('.value'), function() {
			if ($(this).val().length == 0) {
				$('#errorMessage').html("Please enter response header value");
				errorExist = true;
			}

		});
		if (errorExist == true)
			return false;
		return true;

	}


	validateExistingData = function() {
		let isDataExist = false;
		let parentModuleId = $("#parentModuleName").find(":selected").val();
		let sequence = $("#sequence").val();
		let moduleName = $("#moduleName").val();
		let moduleURL = $("#moduleURL").val();
		let targetLookupId = $("#targetLookupType").find(":selected").val();
		if (targetLookupId === "7") {
			moduleURL = $("#externalURL").val();
		}
		let moduleId = $("#moduleId").val();
		$.ajax({
			type: "GET",
			url: contextPath + "/cf/ced",
			async: false,
			cache: false,
			headers: {
				"parent-module-id": parentModuleId,
				"sequence": sequence,
				"module-url": moduleURL,
				"module-name": moduleName,
				"module-id": moduleId,
			},
			success: function(data) {
				if (data != "") {
					let moduleIdName = data.moduleIdName;
					let moduleIdSequence = data.moduleIdSequence;
					let moduleIdURL = data.moduleIdURL;
					let parentModuleURL = data.parentModuleURL;
					let moduleId = $("#moduleId").val();
					if (moduleIdName != undefined && moduleIdName != moduleId) {
						isDataExist = true;
						$('#errorMessage').html("Module name already exist");
					}
					if (isDataExist == false && moduleIdSequence != undefined && moduleIdSequence != moduleId) {
						isDataExist = true;
						$('#errorMessage').html("Sequence number already exist");
					}
					if (isDataExist == false && moduleIdURL != undefined && moduleIdURL != moduleId && moduleURL !== "#" && targetLookupId != "7") {
						isDataExist = true;
						$('#errorMessage').html("Module URL already exist");
					}
					if (moduleId == "" && parentModuleURL != undefined && targetLookupId == "6") {
						isDataExist = false;
						var existModUrl = $("#moduleURL").val();
						existModUrl = parentModuleURL + "#";
						$("#moduleURL").val(existModUrl);
					}
				}
			},
			error: function(xhr, error) {
				showMessage("Error occurred while validating with existing data", "error");
			},

		});
		if (isDataExist === true) {
			$("#errorMessage").show();
		}
		return isDataExist;
	}


	getTargeTypeNames = function(isEditFlag) {
		let context = this;
		let targetLookupId = $("#targetLookupType").find(":selected").val();
		$("#targetTypeName").prop('disabled', true);

		if (targetLookupId === "7") {
			$("#divModuleURL").hide();
			$("#divContextName").hide();
			$("#includeLayoutDiv").hide();
			$("#divTargetType").show();
			$("#divExternalURL").show();
		}
		if (targetLookupId === "6") {
			$("#divModuleURL").show();
			$("#divContextName").show();
			$("#includeLayoutDiv").show();
			$("#divTargetType").hide();
			$("#divExternalURL").hide();

			$("#targetTypeName").val("");
			//$("#moduleURL").val("#");
			$("#parentModuleName").val("");
			$("#targetTypeName").attr('disabled', 'disabled');
			$("#moduleURL").attr('disabled', 'disabled');
			$("#parentModuleName").attr('disabled', 'disabled');
			if (isEditFlag === undefined) {
				$("#includeLayout").val(0);
				$("#includeLayoutCheckbox").prop('checked', false);
				$("#includeLayoutCheckbox").prop('disabled', true);
				context.getSequenceByGroup();
			}
		} else if (targetLookupId !== "6" && targetLookupId !== "7") {
			$("#divModuleURL").show();
			$("#divContextName").show();
			$("#includeLayoutDiv").show();
			$("#divTargetType").hide();
			$("#divExternalURL").hide();
			$("#parentModuleName").val(context.parentModuleId);
			$("#targetTypeName").prop('disabled', false);
			$("#moduleURL").prop('disabled', false);
			if (isEditFlag === undefined) {
				$("#includeLayout").val(1);
				$("#includeLayoutCheckbox").prop('checked', true);
				$("#includeLayoutCheckbox").prop('disabled', false);
				$("#moduleURL").val("");
				$("#targetTypeName").val("");
				$("#targetTypeNameId").val("");
				autocomplete.resetAutocomplete();
			}
			$("#parentModuleName").prop('disabled', false);
			autocomplete.options.autocompleteId = context.getAutocompleteId();
		}
		context.insideMenuOnChange();
	}

	getAutocompleteId = function() {
		let context = this;
		let autocompleteId;
		let targetLookupId = $("#targetLookupType").find(":selected").val();
		if (targetLookupId == "") {
			$("#targetTypeName").prop('disabled', true);
		} else if (targetLookupId == 1) {
			$("#targetTypeName").prop('disabled', false);
			autocompleteId = "dashboardListing";
		} else if (targetLookupId == 2) {
			$("#targetTypeName").prop('disabled', false);
			autocompleteId = "dynamicForms";
		} else if (targetLookupId == 3) {
			$("#targetTypeName").prop('disabled', false);
			autocompleteId = "dynarestListing";
		} else if (targetLookupId == 5) {
			$("#targetTypeName").prop('disabled', false);
			autocompleteId = "templateListing";
		}
		return autocompleteId;
	}


	getSequenceByParent = function() {
		let context = this;
		let parentModuleId = $("#parentModuleName").find(":selected").val();
		if (parentModuleId == "") {
			context.getSequenceByGroup();
		} else {
			$.ajax({
				type: "GET",
				url: contextPath + "/cf/dsp",
				async: false,
				cache: false,
				headers: {
					"parent-module-id": parentModuleId,
				},
				success: function(data) {
					if (data != "") {
						$("#sequence").val(data);
					}
				},
				error: function(xhr, error) {
					showMessage("Error occurred while fetching sequence number", "error");
				},
			});
		}
	}

	getSequenceByGroup = function() {
		$.ajax({
			type: "GET",
			url: contextPath + "/cf/dsg",
			async: false,
			cache: false,
			success: function(data) {
				if (data != "") {
					$("#sequence").val(data);
				}
			},
			error: function(xhr, error) {
				showMessage("Error occurred while fetching sequence number", "error");
			},
		});
	}

	insideMenuOnChange = function() {
		let context = this;
		let isInsideMenu = $("#insideMenuCheckbox").prop("checked");
		let targetLookupId = $("#targetLookupType").find(":selected").val();
		if (isInsideMenu) {
			$("#isInsideMenu").val(1);
			$("#sequence").prop('disabled', false);
			$("#parentModuleName").prop('disabled', false);
			if (sequence !== undefined && sequence !== "") {
				$("#sequence").val(sequence);
				return true;
			}
			context.getSequenceByParent();
		}
		else {
			$("#isInsideMenu").val(0);
			$("#parentModuleName").val("");
			$("#sequence").val("");
			$("#sequence").prop('disabled', true);
			$("#parentModuleName").prop('disabled', true);
		}

	}

	includeLayout = function() {
		let includeLayout = $("#includeLayoutCheckbox").prop("checked");
		if (includeLayout) {
			$("#includeLayout").val(1);
		} else {
			$("#includeLayout").val(0);
		}
	}

	backToModuleListingPage = function() {
		location.href = contextPath + "/cf/mul";
	}

	saveEntityRoleAssociation = function(menuId) {
		let roleIds = [];
		let entityRoles = new Object();
		entityRoles.entityName = $("#moduleName").val().trim();
		entityRoles.moduleId = $("#masterModuleId").val();
		entityRoles.entityId = menuId;
		$.each($("#rolesMultiselect_selectedOptions_ul span.ml-selected-item"), function(key, val) {
			roleIds.push(val.id);

		});

		entityRoles.roleIds = roleIds;

		$.ajax({
			async: false,
			type: "POST",
			contentType: "application/json",
			url: contextPath + "/cf/ser",
			data: JSON.stringify(entityRoles),
			success: function(data) {
			}
		});
	}
	getEntityRoles = function() {
		$.ajax({
			async: false,
			type: "GET",
			url: contextPath + "/cf/ler",
			data: {
				entityId: $("#moduleId").val(),
				moduleId: $("#masterModuleId").val(),
			},
			success: function(data) {
				$.each(data, function(key, val) {
					multiselect.setSelectedObject(val);

				});
			}
		});
	}

	addRow = function(key, value) {
		let selectedLanguageId = getCookie("locale");
		let context = this;
		let trId = uuidv4();
		let row = '<tr id=' + trId + '><td> ';
		if (key !== undefined && value !== undefined) {
			if (key == "Content-Type") {
				row += '<input class="key" type="hidden">' + key + '</td><td><input class="value" type="text">';
			} else if (key == "Content-Language") {
				row += '<input class="key" type="hidden">' + key + '</td><td><input class="value" type="hidden">' + selectedLanguageId;
			} else {
				row += '<input class="key" type="hidden">' + key + '</td><td><input class="value" type="hidden">' + value;
			}
		} else {
			row += '<input class="key" type="text"></td><td><input class="value" type="text" >';
		}

		row += '</td><td>';
		if (key != "Powered-By" && key != "Content-Language" && key != "Content-Type") {
			row += '<span id="btn_' + trId + '" onclick="addEditModule.deleteRow(this)" class="cusrorhandcls"><i class="fa fa-minus-circle" aria-hidden="true"></i></span>';
		}
		row += '</td></tr>';
		$('#headerTable tr:last').after(row);
		if (key !== undefined && value !== undefined) {
			if (key == "Content-Language") {
				$('#headerTable tr:last').find('td input.key').val(key);
				$('#headerTable tr:last').find('td input.value').val(selectedLanguageId);
			} else {
				$('#headerTable tr:last').find('td input.key').val(key);
				$('#headerTable tr:last').find('td input.value').val(value);
			}
		}
	}

	addRowForRequestParam = function(key, value) {
		let context = this;
		let trId = uuidv4();
		$('#requestParamTable tr:last').after('<tr id=' + trId + '><td><input class="key" type="text"></td><td><input class="value" type="text"></td><td class="centercls"><span class="cusrorhandcls" id="btn_' + trId + '" onclick="addEditModule.deleteRow(this)"><i class="fa fa-minus-circle" aria-hidden="true"></i></span></td></tr>');
		if (key !== undefined && value !== undefined) {
			$('#requestParamTable tr:last').find('td input.key').val(key);
			$('#requestParamTable tr:last').find('td input.value').val(value);
		}
	}

	deleteRow = function(rowElement) {
		$("#deleteHeader").html("Are you sure you want to delete?");
		$("#deleteHeader").dialog({
			bgiframe: true,
			autoOpen: true,
			modal: true,
			closeOnEscape: true,
			draggable: true,
			resizable: false,
			title: "Delete",
			position: {
				my: "center", at: "center"
			},
			buttons: [{
				text: "Cancel",
				click: function() {
					$(this).dialog('close');
				},
			},
			{
				text: "Delete",
				click: function() {
					$(this).dialog('close');
					let rowId = $(rowElement).attr('id').split("_")[1];
					$('#' + rowId).remove();
				}
			},
			],
			open: function(event, ui) {
				$('.ui-dialog-titlebar')
					.find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
					.prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
			}
		});


	}

	headerJson = function() {
		let headerJson = {};
		$("#headerTable").find('tr').each(function() {
			let key = $(this).find("input.key").val();
			if (key !== undefined) {
				let value = $(this).find("input.value").val();
				headerJson[key] = value;
			}
		});
		return headerJson;
	}

	requestParamJson = function() {
		let requestParamJson = {};
		$("#requestParamTable").find('tr').each(function() {
			let key = $(this).find("input.key").val();
			if (key == "") {
				showMessage("Key is empty in request param", "error");
				requestParamJson = -1;
				return requestParamJson;
			}
			if (key !== undefined) {
				let value = $(this).find("input.value").val();
				if (value == undefined || value == "" || value == null) {
					showMessage("Value is null or empty in request param", "error");
					requestParamJson = -1;
					return requestParamJson;
				}
				requestParamJson[key] = value;
			}
		});
		return requestParamJson;
	}

	createHeaderResponseTable = function() {
		if (savedHeaderJson !== '') {
			Object.keys(savedHeaderJson).forEach(function(key) {
				addEditModule.addRow(key, savedHeaderJson[key]);
			});
		}
	}

	createRequestParamTable = function() {
		if (savedRequestParamJson !== '') {
			Object.keys(savedRequestParamJson).forEach(function(key) {
				addEditModule.addRowForRequestParam(key, savedRequestParamJson[key]);
			});
		}
	}

	copyUrl = function() {
		let input = $("<input>");
		$("body").append(input);
		input.val('${contextPath}' + "/view/" + $("#moduleURL").val()).select();
		document.execCommand("copy");
		input.remove();
		showMessage("Copied successfully.", "success");
	}

	copyUrlActualPath = function(urlPrefix) {
		var $temp = $("<input>");
		$("body").append($temp);
		//var uriPrefix = $("#moduleURL").text();
		var moduleURL = $("#moduleURL").val();
		$temp.val(urlPrefix + moduleURL).select();
		document.execCommand("copy");
		$temp.remove();
		showMessage("Copied successfully.", "success");
	}

	copyUrlActualPath = function(urlPrefix, elementId) {
		var $temp = $("<input>");
		$("body").append($temp);
		var elementVal = $("#" + elementId).val();
		$temp.val(urlPrefix + elementVal).select();
		document.execCommand("copy");
		$temp.remove();
		showMessage("Copied successfully.", "success");
	}

	copyUrlContextPath = function(elementId) {
		let input = $("<input>");
		$("body").append(input);
		input.val('${contextPath}' + "/view/" + $("#" + elementId).val()).select();
		document.execCommand("copy");
		input.remove();
	}

	copyTargetUrl = function(elementId) {
		let input = $("<input>");
		$("body").append(input);
		input.val($("#" + elementId).val()).select();
		document.execCommand("copy");
		input.remove();
		showMessage("Copied successfully.", "success");
	}
}