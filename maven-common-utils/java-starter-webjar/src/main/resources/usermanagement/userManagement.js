/**
 * 
 */


const AuthType = Object.freeze({
	DAO: '2',
	LDAP: '3',
	OAUTH: '4',
	SAML: '5',
});

class UserManagement {
	constructor() {
	}

}


function removeLdapAuth(thisObj) {
	let buttonId = thisObj.id;
	let deleleteElement = $('<div id="deleteLdapConfirmation"></div>');
	$("body").append(deleleteElement);
	$("#deleteLdapConfirmation").html("Are you sure you want to delete the changes ?");
	$("#deleteLdapConfirmation").dialog({
		bgiframe: true,
		autoOpen: true,
		modal: true,
		closeOnEscape: true,
		draggable: true,
		resizable: false,
		title: "Delete",
		dialogClass: "popup-center",
		position: {
			my: "center", at: "center"
		},
		buttons: [{
			text: "Delete",
			"class": 'btn btn-primary',
			click: function() {
				const [first, ...rest] = buttonId.split('removeLdapAuth');
				const remainder = rest.join('-');
				$("#addSection"+remainder).remove();
				$(this).dialog("destroy");
				$(this).remove();
				var lastIndex = buttonId.split("-").pop();
				var num = document.querySelectorAll('authType-'+lastIndex+' .addSectionDiv').length;
				var numItems = $('#props-'+lastIndex).find('.addSectionDiv').length;
				if(numItems<=0){
					$("#authTypeDiv-"+lastIndex).remove();
					$(this).parent('.dividesection').remove();
				}
				var numOfMainAuthTypes = document.querySelectorAll('.dividesection').length;
				if(numOfMainAuthTypes<=0){
					$("#isAuthenticationEnabled").prop("checked",false).trigger("change");
				}
				
			}
		}, { 
			text: "Cancel",
			"class": "btn btn-secondary",
			click: function() {
				$(this).dialog("destroy");
				$(this).remove();
			},
		},

		],
		open: function(event, ui) {
			$(".ui-dialog-titlebar")
				.find("button").removeClass("ui-dialog-titlebar-close").addClass("ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close")
				.prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
		}

	});

}



function changeLdapSearchScope(thisObj) {

}

function changePasswordExpiry(thisObj) {
}

function changeLdapSecurityType(thisObj) {

}

function closeModalBtn(thisObj) {
	var emailExist = checkAdminEmailExistInPm();
	if (emailExist == undefined || emailExist === '') {
		$("#isAuthenticationEnabled").prop("checked", false).trigger("change");
		$(this).closest('form').trigger("reset");
	}
	
}

function validateUserDetails() {
	let specials = /[A-Za-z 0-9]/g;

	var firstName = $("#firstName").val().trim();
	if (firstName == '') {
		showMessage("First name is required.", "warn");
		$("#firstName").focus();
		return false;
	}
	var lastName = $("#lastName").val().trim();
	if (lastName == '') {
		showMessage("Last name is required.", "warn");
		$("#lastName").focus();
		return false;
	}
	var email = $("#email").val().trim();
	const emailExpression =  /[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}$/; 
	if (email == '' || !emailExpression.test(email)) {
		showMessage("Please enter valid email id. ", "error");
		$("#email").focus();
		return false;
	}

	if (specials.test(firstName) == false) {
		showMessage("Invalid First Name", "warn");
		$("#firstName").focus();
		return false;
	}

	if (specials.test(lastName) == false) {
		showMessage("Invalid Last Name", "warn");
		$("#lastName").focus();
		return false;
	}

	return true;
}

function saveUserDetails() {
	var isFormValid = validateUserDetails();
	if (isFormValid) {

		$('#adminModalDialog').modal('hide');
		$.blockUI({ message: "<img src='" + contextPathHome + "/webjars/1.0/images/loading.gif' />" });


		let userData = new Object();
		userData.firstName = $("#firstName").val().trim();
		userData.lastName = $("#lastName").val().trim();
		userData.email = $("#email").val().trim();
		userData.userId = '';
		userData.roleIds = ['2ace542e-0c63-11eb-9cf5-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348'];
		userData.forcePasswordChange = 1;
		userData.isProfilePage = false;
		userData.isSendMail = false;
		userData.isActive = 1;
		let isEdit = 1;
		let emailExist = saveAdminData();
		if(emailExist == false){
			$('form :input').val('');
			$.unblockUI();
			return false;
		}
		userData.userId = checkEmailIdExist();
		
		$.ajax({
			type: "POST",
			url: contextPath + "/cf/surap",
			data: {
				userData: JSON.stringify(userData),
				isEdit: isEdit
			},
			success: function(data) {
				$.unblockUI();
				updatePropertyDetails();
				showMessage("Information saved successfully", "success");
				isAdminEmailExist = true;
				$('#adminModalDialog').modal('hide');
				//$('#isAuthenticationEnabled' + oAuthId).val('-1').trigger("change");
				$("#isAuthenticationEnabled").prop("checked", true).trigger("change");
			},
			error: function(data) {
				$.unblockUI();
				showMessage("Error occurred while saving details", "error");
			},
		});
	}

}

function saveAdminData() {

	let userId = checkEmailIdExist();
	if (userId != "") {
		showMessage("User email id already exists", "error");
		return false;
	}
	let userData = new Object();

	userData.email = $("#email").val();
	userData.userId = "";
	userData.firstName = $("#firstName").val().trim();
	userData.lastName = $("#lastName").val().trim();
	userData.isProfilePage = false;
	userData.roleIds = ['2ace542e-0c63-11eb-9cf5-f48e38ab9348'];
	userData.forcePasswordChange = 1;
	userData.isSendMail = false;
	userData.isActive = 1;
	let serializedForm = $("#addEditUser").serializeArray();
	userData.formData = JSON.stringify(serializedForm.formatSerializedArray());
	$.ajax({
		type: "POST",
		contentType: "application/json",
		async: false,
		url: contextPath + "/cf/sud",
		data: JSON.stringify(userData),
		success: function(data) {
			return true;
		},
		error: function(data) {
			showMessage("Error occurred while saving details", "error");
			return false;
		},
	});
	return true;

}

function checkEmailIdExist() {
	var userId = "";
	$.ajax({
		type: "GET",
		async: false,
		url: contextPath + "/api/validate-user-email-get-user-id",
		data: {
			email: $("#email").val().trim()
		},
		success: function(data) {
			if(data !=undefined && data[0] != undefined && data[0].userId != undefined){
				userId = data[0].userId;
			}
		}
	});

	return userId;
}

function checkAdminEmailExistInPm() {
	var adminEmail = "";
	$.ajax({
		type: "GET",
		async: false,
		url: contextPath + "/api/checkAdminEmailExist",
		success: function(data) {
			if (data && data.length != 0) {
				adminEmail = data[0].adminEmail;
			}

		}
	});

	return adminEmail;
}


function updatePropertyDetails() {
	let propertyName = "adminEmailId";
	let propertyValue = "";
	if ($("#email").val().trim() != "") {
		$.ajax({
			type: "GET",
			async: false,
			url: contextPath + "/cf/spm",
			data: {
				propertyValue: $("#email").val().trim(),
				propertyName: propertyName,
				ownerId: "system",
				ownerType: "system"
			},
			success: function(data) {
				return true;
			},
			error: function(data) {
				showMessage("Error occurred while saving details", "error");
				return false;
			}
		});
	}
	return true;
}

function testLdapAuth(thisObj) {

	const str = thisObj.id;
	const [first, ...rest] = str.split('-');
	const lastIndex = rest.join('-');
	let formObj = new Object();
	formObj["ldapAddress"] = $("#ldapAddress-" + lastIndex).val();
	formObj["ldapPort"] = $("#ldapPort-" + lastIndex).val();
	formObj["ldapSecurityType"] = $("#ldapSecurityType-" + lastIndex).val();
	formObj["basedn"] = $("#basedn-" + lastIndex).val();
	formObj["userdn"] = $("#userdn-" + lastIndex).val();
	formObj["adminUserName"] = $("#adminUserName-" + lastIndex).val();
	formObj["adminPassword"] = $("#adminPassword-" + lastIndex).val();
	formObj["loginAttribute"] = $("#loginAttribute-" + lastIndex).val();
	$.ajax({
		type: "POST",
		url: contextPath + "/cf/checkLdapConnection",
		data: formObj,
		success: function(data) {
			if (data !== undefined) {
				if (data == false) {
					showMessage("Connection refused. Please contact administrator!", "error");
				} else {
					showMessage("Connection is successful!", "success");
				}
				return false;
			}
		}
	});
	return true;
}


function removeOauthSection(thisObj) {
	var buttonId = thisObj.id;
	let deleleteElement = $('<div id="deleteConfirmation"></div>');
	var thisObj = $(this);
	$("body").append(deleleteElement);
	$("#deleteConfirmation").html("Are you sure you want to delete the changes ?");
	$("#deleteConfirmation").dialog({
		bgiframe: true,
		autoOpen: true,
		modal: true,
		closeOnEscape: true,
		draggable: true,
		resizable: false,
		title: "Delete",
		dialogClass: "popup-center",
		position: {
			my: "center", at: "center"
		},
		buttons: [{
			text: "Delete",
			"class": 'btn btn-primary',
			click: function() {
				const [first, ...rest] = buttonId.split('remove-oauth-section');
				const remainder = rest.join('-');
				$("#addSection"+rest).closest('.addSectionDiv').remove();
				$(this).dialog("destroy");
				$(this).remove();
				var lastIndex = buttonId.split("-").pop();
				var num = document.querySelectorAll('authType-'+lastIndex+' .addSectionDiv').length;
				var numItems = $('#props-'+lastIndex).find('.addSectionDiv').length;
				if(numItems<=0){
					$("#authTypeDiv-"+lastIndex).remove();
					$(this).parent('.dividesection').remove();
				}
				var numOfMainAuthTypes = document.querySelectorAll('.dividesection').length;
				if(numOfMainAuthTypes<=0){
					$("#isAuthenticationEnabled").prop("checked",false).trigger("change");
				}
			}
		}, {
			text: "Cancel",
			"class": "btn btn-secondary",
			click: function() {
				$(this).dialog("destroy");
				$(this).remove();
			},
		},
		],
		open: function(event, ui) {
			$(".ui-dialog-titlebar")
				.find("button").removeClass("ui-dialog-titlebar-close").addClass("ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close")
				.prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
		}

	});
}


function updateRowToJson(parsedProperties, additionalProperties) {
	var existingProperties = [];
	var addProperties = [];
	addProperties.push(additionalProperties);
	if (typeof parsedProperties != 'undefined' && parsedProperties != null) {
		$.each(parsedProperties, function(key, val) {
			if (typeof key != 'undefined' && typeof val != 'undefined') {
				$.each(val, function(addPropKey, addPropVal) {
					if (typeof addPropKey != 'undefined' && typeof addPropVal != 'undefined') {
						addProperties.push(addPropVal);
					}
				});
				existingProperties.push(addProperties);
			}
		});
	}
	return existingProperties;

}


function checkAuthenticatedEnabled() {
	let enabled = false;
	$.ajax({
		type: "GET",
		url: contextPath + "/cf/cae",
		async: false,
		data: {

		},
		success: function(data) {
			enabled = data;
		}
	});
	return enabled;
}


function initTemplateAutocomplete(templateDetails) {
	let savedTemplate = new Object();
	savedTemplate.targetTypeId = templateDetails.templateId;
	savedTemplate.targetTypeName = templateDetails.templateName;
	autocomplete = $('#templateNameAC').autocomplete({
		autocompleteId: "templateListing",
		render: function(item) {
			var renderStr = '';
			if (item.emptyMsg == undefined || item.emptyMsg === '') {
				renderStr = '<p>' + item.targetTypeName + '</p>';
			}
			else {
				renderStr = item.emptyMsg;
			}
			return renderStr;
		},
		additionalParamaters: { languageId: 1 },
		extractText: function(item) {
			return item.targetTypeName;
		},
		select: function(item) {
			$("#templateNameAC").blur();
			$("#templateId").val(item.targetTypeId);
			$("#templateName").val(item.targetTypeName);
		},
	}, savedTemplate);

}


function initFormAutocomplete(formDetails) {
	let savedForm = new Object();
	savedForm.targetTypeId = formDetails.formId;
	savedForm.targetTypeName = formDetails.formName;
	autocomplete = $('#dynamicFormName').autocomplete({
		autocompleteId: "dynamicForms",
		render: function(item) {
			var renderStr = '';
			if (item.emptyMsg == undefined || item.emptyMsg === '') {
				renderStr = '<p>' + item.targetTypeName + '</p>';
			}
			else {
				renderStr = item.emptyMsg;
			}
			return renderStr;
		},
		additionalParamaters: { languageId: 1 },
		extractText: function(item) {
			return item.targetTypeName;
		},
		select: function(item) {
			$("#dynamicFormName").blur();
			$("#formId").val(item.targetTypeId);
			$("#formName").val(item.targetTypeName);
		},
	}, savedForm);

}



function changeVerificationType(thisObj) {
	let verificationType = $("#" + thisObj.id).val();
	var lastIndex = thisObj.id.split("-").pop();
	var elementLastIndex = '-0-' + lastIndex;
	if (onLoad == true) {
		if ($("#enableRegex" + elementLastIndex).is(":checked")) {
			$("#enableRegex" + elementLastIndex).prop("checked", true).trigger("change");
			$("#passwordExpiry" + elementLastIndex).closest('.childElement').show();
		} else {
			$("#enableRegex" + elementLastIndex).prop("checked", false).trigger("change");
			$("#enableRegex" + elementLastIndex).prop("disabled", true);
			$("#passwordExpiry" + elementLastIndex).closest('.childElement').hide();
		}
	} else {
		if (verificationType == 2 || verificationType == 0) {
			$("#enableRegex" + elementLastIndex).prop("checked", false).trigger("change");
			$("#enableRegex" + elementLastIndex).prop("disabled", true);
			$("#passwordExpiry" + elementLastIndex).closest('.childElement').hide();
		} else {
			$("#enableRegex" + elementLastIndex).prop("checked", true).trigger("change");
			$("#enableRegex" + elementLastIndex).prop("disabled", false);
			$("#passwordExpiry" + elementLastIndex).closest('.childElement').show();
		}
	}

}

function changeChildElementStatus(thisObj) {
	if ($(thisObj).is(":checked")) {
		$(thisObj).closest('div.row').find('.childElement').show();
	}
	else {
		$(thisObj).closest('div.row').find('.childElement').hide();
	}
}

function backToHomePage() {
	location.href = contextPath + "/cf/home";
}


function checkSelectedValue(elementName, selectedVal) {
	var keyExist = false;
	$.each(selectedValues, function(key, val) {
		if (typeof key != "undefined" && selectedValues[key].name == elementName && selectedValues[key].selval == selectedVal) {
			keyExist = true;

		}
	});
	return keyExist;
}


function validateOAuthClients(oauthClientId, oAuthTypeVal) {

	var keyExist = false;
	const oAuthElements = document.querySelectorAll(`select[id^="oauth-client-"]`);
	var oAuthSelectedClients = [];
	oAuthSelectedClients.push(oAuthTypeVal);
	oAuthElements.forEach(element => {
		if (element.id != oauthClientId) {
			var oAuthSelClientVal = $("#" + element.id).val();
			var oAuthSelClientId = element.id;
			var oAuthValArry = jQuery.inArray(oAuthSelClientVal, oAuthSelectedClients);
			if (oAuthValArry == -1) {
				oAuthSelectedClients.push(oAuthSelClientVal);
			} else {
				keyExist = true;
				return false;
			}
		}
	});
	return keyExist;
}


function validateDuplicateAuthType(verifyVal, authTypeId) {
	var returnResult = false;
	let validateValues = [];
	const authTypes = document.querySelectorAll(`select[id^="authType-"]`);
	authTypes.forEach(element => {
		var authSelVal = $("#" + element.id).val();
		var properties = $("#" + element.id).find('option:selected').attr("properties");
		let parsedProperties = '';
		if (properties != undefined && properties != "") {
			parsedProperties = getParsedJSONProperties(properties);
		}
		$.each(parsedProperties, function(key, val) {
			if (typeof val != 'undefined' || val != null) {
				if (typeof key != 'undefined' && typeof val != 'undefined') {
					var isExist = checkAuthValueExist(validateValues, authSelVal);
					if (isExist) {
						returnResult = isExist;
					} else {
						validateValues.push({ id: element.id, selectedValue: authSelVal });
					}
					return false;
					
				}
			}
		});
	});
	return returnResult;
}

function checkAuthValueExist(validateValues, selectedVal) {
	var keyExist = false;
	$.each(validateValues, function(key, val) {
		if (typeof key != "undefined" && validateValues[key].selectedValue == selectedVal) {
			keyExist = true;
			return false;
		}
	});
	return keyExist;
}


function updateAuthProperties(attrId, elementValue) {
	let properties = $("#" + attrId + " option[value=" + $('#' + attrId).val() + "]").attr("properties");
	let parsedProperties = null;
	if (properties != undefined && properties != "") {
		parsedProperties = getParsedJSONProperties(properties);
	}
	var lastIndex = attrId.split("-").pop();
	if (typeof parsedProperties != 'undefined' && parsedProperties != null) {
		$.each(parsedProperties, function(key, val) {
			var elementId = "";
			if (typeof key != 'undefined' && key == 'authenticationType' && typeof val != 'undefined' && typeof attrId != 'undefined' && typeof val.name != 'undefined') {
				elementId = val.name + '-' + lastIndex;
				val.value = "true";
				val = updatePropertyValue(val, attrId, elementId);
			}
			if (typeof key != 'undefined' && key == 'authenticationDetail'
				&& val.authenticationDetail != 'undefined') {
				$.each(val, function(authDetailsKey, authDetailsVal) {
					if (typeof authDetailsKey != 'undefined' && authDetailsKey == 'configurations') {
						if (typeof authDetailsVal != 'undefined') {
							$.each(authDetailsVal, function(configKeys, configVals) {
								if (typeof configKeys != 'undefined' && typeof configVals != 'undefined') {
									$.each(configVals, function(configKey, configVal) {
										if (typeof configKey != 'undefined' && typeof configVal != 'undefined') {
											var addRowName = configVal.name;
											var searchPosition = addRowName.search("value");
											if(searchPosition != '-1'){
												elementId = configVal.name;
											}else{
												elementId = configVal.name + '-' + configKeys + '-' + lastIndex;
											}
											configVal = updatePropertyValue(configVal, attrId, elementId);
											$.each(configVal.additionalDetails, function(addPropKeys, addPropVals) {
												if (typeof addPropKeys != 'undefined' && typeof addPropVals != 'undefined') {
													$.each(addPropVals, function(addPropKey, addPropVal) {
														if (typeof addPropKey != 'undefined' && typeof addPropVal != 'undefined') {
															$.each(addPropVal, function(propKey, propVal) {
																if (typeof propKey != 'undefined' && typeof propVal != 'undefined') {
																	elementId = propVal.name + '-' + configKeys + '-' + lastIndex;
																	propVal = updatePropertyValue(propVal, attrId, elementId);

																}

															});

														}

													});

												}

											});

										}
									});

								}
							});

						}

					}

				});

			}

		});
		var finalProp = JSON.stringify(parsedProperties);
		$("#" + attrId + " option[value=" + $('#' + attrId).val() + "]").attr("properties", encodeURIComponent(finalProp));
		return parsedProperties;
	}
}


function updatePropertyValue(val, authTypeId, elementId) {
	var lastIndex = authTypeId.split("-").pop();
	let selectedValue = "";
	var element = val;
	if(element.name != undefined){
		var searchPosition = element.name.search("value");
		if(searchPosition != '-1'){
			const myArray = element.name.split("value");
			var lblId = "key"+myArray[1];
			var addRowTxtEleId = $("#"+lblId).val();
			var labelValue = addRowTxtEleId.split(" ").join("");
			var lblName = labelValue.replace(/[_\W]+/g, "")
			element.textValue = addRowTxtEleId;
			element.name =  lblName;
			element.condition =  "row-added";
		}
	}
	if (val.type == "boolean") {
		var eleValue = ($("#" + elementId).is(":checked")).toString();
		if( eleValue!= undefined)
			element.value = eleValue;
		else
			element.value = "false";
	}
	if (val.type == "select") {
		selectedValue = $("#" + elementId).val();
		if(selectedValue != undefined)
			element.value = selectedValue;
		else
			element.value = "-1";
	}
	if (val.type == "text" || val.type == "hidden" || val.type == "password") {
		var eleValue = $("#" + elementId).val();
		if(eleValue != undefined) 
			element.value = eleValue;
		else {
			if(val.name=="img-path" && (eleValue == undefined || eleValue == "")){
				element.value = "webjars/1.0/images/oauthnotfound.png";
			} else {
				element.value = "";
			}
		}
			
	}
	return element;
}

function validateFormSubmit() {
	var errorFlag = false;
	$("div").find("input").each(function() {
		//var reqClazz = $(this).prev('span').attr('class');
		
		var addPropClazz = $(this).hasClass('additionalProperty');
		if(addPropClazz){
			var dataParent = $(this).closest(".childElement").attr("data-parent");
			var parenElement = $(this).parents().attr('name='+dataParent);
			var parentEleByName = $(this).parents().find("input[name='"+dataParent+"']");
			if(parentEleByName != undefined && parentEleByName.attr('type') == 'checkbox'){
				var attrId = parentEleByName.attr('id');
				var chked = $("#" + attrId).is(":checked");
				if(chked){
					var reqClazz = $(this).prev('span').hasClass('asteriskmark');
					if ($(this).val() === '' && reqClazz) {
						showMessage("Please fill all fields!", "error");
						$(this).focus();
						errorFlag = true;
						return false;
					}
				}
			}
		} else {
			var reqClazz = $(this).prev('span').hasClass('asteriskmark');
			if ($(this).val() === '' && reqClazz) {
				showMessage("Please fill all fields!", "error");
				$(this).focus();
				errorFlag = true;
				return false;
			}
		}
		
	});
	$("tr").find("td input").each(function() { 
		if($(this).hasClass('key')){
			if ($(this).val() === '') {
				showMessage("Please fill all fields!", "error");
				$(this).focus();
				errorFlag = true;
				return false;
			}
		}
		//var reqClazz = $(this).closest('tr').children('td').children('span').attr('class');
		var reqClazz = $(this).closest('tr').children('td').children('span').hasClass('asteriskmark');
		if ($(this).val() === '' && $(this).is(":visible") == true && reqClazz) {
			showMessage("Please fill all fields!", "error");
			$(this).focus();
			errorFlag = true;
			return false;
		}
	});
	$("tr").find("td select").each(function() {
		var reqClazz = $(this).closest('tr').children('td').children('label').children('span').attr('class');
		if ($(this).val() == -1 && reqClazz === 'asteriskmark') {
			showMessage("Please select all fields!", "error");
			$(this).focus();
			errorFlag = true;
			return false;
		}
	});
	
	if ($('input[name = regexPattern]').length) {
		let regexPattern = $('input[name = regexPattern]').val();
		try {
			new RegExp(regexPattern);

		} catch (exeception) {
			showMessage("Invalid regular expression!", "error");
			$('input[name = regexPattern]').focus();
			errorFlag = true;
			return false;
		}
	}
	//Validate for -1 selected type.
	$('.asteriskmark').next('strong').next('select').each(function(e) {
		if ($(this).val() === '' || $(this).val() === '-1') {
			showMessage("Please Select Authentication!", "error");
			$(this).focus();
			errorFlag = true;
			return false;
		}
	});
	if ($("#errors").length) {
		showMessage($("#errors").val() + " !", "error");
		$(this).focus();
		errorFlag = true;
		return false;
	}

	let validateRegisrations = [];
	if ($("input[name='registration-id']").length) {
		const oAuthRegElements = document.querySelectorAll(`input[name="registration-id"]`);
		
		oAuthRegElements.forEach(element => {
			var oAuthRegClientVal = $("#" + element.id).val();
			var oAuthRegValArry = jQuery.inArray(oAuthRegClientVal, validateRegisrations);
			//console.log(oAuthRegValArry+" = "+JSON.stringify(validateRegisrations)+' = '+oAuthRegClientVal)
			if (oAuthRegValArry == -1) {
				validateRegisrations.push(oAuthRegClientVal);
			} else {
				var errorMessage = oAuthRegClientVal + " already exist."
				showMessage(errorMessage, "error");
				errorFlag = true;
				return false;
			}
		});
	}
	return errorFlag;
}

function isValidJSONString(str) {
	try {
		JSON.parse(str);
	} catch (e) {
		return false;
	}
	return errorFlag;
}

function getParsedJSONProperties(properties) {
	let parsedProperties = null;
	if (properties != undefined && properties != "") {
		parsedProperties = JSON.parse(decodeURIComponent(properties));
	}
	parsedProperties = JSON.parse(parsedProperties);
	return parsedProperties;
}




function shutDownServer() {

	$.ajax({
		type: "GET",
		url: contextPath + "/cf/shutdown-app",
		data: {

		},
		success: function(data) {

			var millisecondsToWait = 2000;
			setTimeout(function() {
				showMessage("Server is shutting down.", "warn");
			}, millisecondsToWait);
		},
		error: function(xhr, data) {
			showMessage("Error while closing the application server.", "error");
		}
	});
}


function addRow(thisObj) {
	
	var authTypeParentDivId = $(thisObj).closest('div').closest('.cm-card-body').attr('id');	
	var lastIndex = authTypeParentDivId.split("-").pop();	
	let authId = "authType-"+lastIndex;
	const [first, ...rest] = thisObj.id.split('-');
	const remainderLastIdx = rest.join('-');
	var remTableId = $(thisObj).closest('table').attr('id');
	var rowCount = $('#' + remTableId + ' tr').length;
	var curRowCount = rowCount++;
	var rowIdx = first + '-' + rest;
	var key = "key" + curRowCount + "-"+rowIdx;
	var val = "value" + curRowCount + "-"+rowIdx;
	let addObj = new Object();
	addObj.name = "value"+curRowCount+"-"+rowIdx; 
	addObj.type = "text";
	addObj.textValue 	=  key ;	
	addObj.required 	= false;
	addObj.value = "";
	addObj.condition = "row-added";
	let row = '<tr id=row-' + rowIdx + '><td><input class="key" id="' + key + '" name="' + key + '" type="text" value="" textValue="' + key + '"  require="true"></td><td><input class="configurationType form-control" type="text" id="' + val + '" name="' + val + '" value=""  textValue="' + key + '" require="false" condition="row-added"></td><td><span id="btn_' + key + '" onclick="deleteRow(this)" class="cusrorhandcls"><i class="fa fa-minus-circle" aria-hidden="true"></i></span></td></tr>';
	$('#' + remTableId + ' tr:last').after(row);
	updateRowProps(authId, addObj, first);
}

function updateRowProps(authId, addObj, keyObjUpdate) {
	var lastIndex = authId.split("-").pop();
	var properties = $("#"+authId).find('option:selected').attr('properties');
	let parsedProperties = '';
	if (properties != undefined && properties != "") {
		parsedProperties = getParsedJSONProperties(properties);
	}
	var updateConfigurations = [];
	
	//addProperties.push(addObj);
	if (typeof parsedProperties != 'undefined' && parsedProperties != null) {
		$.each(parsedProperties, function(key, val) {
			if (typeof key != 'undefined' && typeof val != 'undefined' && key != 'undefined') {
				$.each(val, function(authKey, authVal) {
					if (typeof authKey != 'undefined' && authKey == 'configurations'  && typeof authVal != 'undefined') {
						$.each(authVal, function(configKey, configVal) {
							if (typeof configKey != 'undefined' && typeof configVal != 'undefined') {
								if (configKey == keyObjUpdate) {
									var addProperties = [];
									$.each(configVal, function(attrKey, attrVal) {
										addProperties.push(attrVal);
									});
									addProperties.push(addObj);
									updateConfigurations.push(addProperties);	
								} else {
									updateConfigurations.push(configVal);
								}
							}
						});	
					}
				});
			}
		});
	}
	if(typeof parsedProperties != 'undefined' && parsedProperties != null ){
		$.each(parsedProperties, function(key,val){	
			if(typeof key != 'undefined' && key == 'authenticationDetail'){
				val.configurations = updateConfigurations;
			}
		});
	}
	var jsonProperties= JSON.stringify(parsedProperties);
	$("#"+authId+" option[value="+$('#'+authId).val()+"]").attr("properties", encodeURIComponent(JSON.stringify(jsonProperties)));
}

function deleteRow(rowElement) {
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
			text: "Delete",
			class :"btn btn-primary",
			click: function() {
				$(this).dialog('close');
				var authTypeParentDivId = $(rowElement).closest('div').closest('.cm-card-body').attr('id');	
				var lastIndex = authTypeParentDivId.split("-").pop();	
				let authId = "authType-"+lastIndex;
				var keyObjDelete = $(rowElement).attr("id");
				//console.log(keyObjDelete);
				const [first, ...rest] = keyObjDelete.split('-');
				const remainder = rest.join('-');
				//console.log('remainder = '+remainder);
				var displayName = $('#headerTable-'+remainder).find("input[name='displayName']").val();
				var col2Value = $(rowElement).closest('tr').find("td:eq(1) input").attr('name');
				//console.log(authId+' = '+displayName+ ' = '+col2Value);
				deleteRowProps(authId, displayName, col2Value);
				$(rowElement).closest("tr").remove();
			}
		},{
			text: "Cancel",
			class :"btn btn-secondary",
			click: function() {
				$(this).dialog('close');
			},
		}
		],
		open: function(event, ui) {
			$('.ui-dialog-titlebar')
				.find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
				.prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
		}
	});

}

function deleteRowProps(authId, displayName, keyObjUpdate) {
	var lastIndex = authId.split("-").pop();
	var properties = $("#"+authId).find('option:selected').attr('properties');
	let parsedProperties = '';
	if (properties != undefined && properties != "") {
		parsedProperties = getParsedJSONProperties(properties);
	}
	var updateConfiguration = false;
	var updateConfigurations = [];
	var idx = 0;
	var jsonProperties1= JSON.stringify(parsedProperties);
	//console.log(jsonProperties1);
	if (typeof parsedProperties != 'undefined' && parsedProperties != null) {
		$.each(parsedProperties, function(key, val) {
			if (typeof key != 'undefined' && typeof val != 'undefined' && key != 'undefined') {
				$.each(val, function(authKeys, authVals) {
					if (typeof authKeys != 'undefined' && authKeys == 'configurations'  && typeof authVals != 'undefined') {
						$.each(authVals, function(configKeys, configVals) {
							if (typeof configKeys != 'undefined' && typeof configVals != 'undefined') {
								$.each(configVals, function(configKey, configVal) {
									
									if (typeof configKey != 'undefined' && typeof configVal != 'undefined' && configVal.name == 'displayName' 
									&& configVal.value == displayName && configVal.name == 'displayName') {
										//console.log(' displayName = '+ JSON.stringify(configVal));
											updateConfiguration = true;
											idx = configKeys;
									}
								});	
							}	
						});	
					}
				});
			}
		});
	}
	//console.log(updateConfiguration +' = '+ idx);
	var jsonProperties1= JSON.stringify(parsedProperties);
	//console.log(jsonProperties1);
	if (typeof parsedProperties != 'undefined' && parsedProperties != null && updateConfiguration) {
		$.each(parsedProperties, function(key, val) {
			if (typeof key != 'undefined' && typeof val != 'undefined' && key != 'undefined') {
				$.each(val, function(authKey, authVal) {
					if (typeof authKey != 'undefined' && authKey == 'configurations'  && typeof authVal != 'undefined') {
						$.each(authVal, function(configKeys, configVals) {
							if (typeof configKeys != 'undefined' && typeof configVals != 'undefined') {
								$.each(authVal, function(confKeys, confVals) {
									
									if (typeof confKeys != 'undefined' && typeof confVals != 'undefined' ) {
										var updateConfigVals = [];
										$.each(confVals, function(confKey, confVal) {
											if (typeof confKey != 'undefined' && typeof confVal != 'undefined' ) {
											//console.log(confVal.name +'=='+ keyObjUpdate);
												if(confVal.name == keyObjUpdate && confKeys == idx){
													//console.log(JSON.stringify(confVal)+' = '+confKey+' = '+idx+" = "+ keyObjUpdate);
													//console.log(confVal.name+' push -'+keyObjUpdate);
												}else{
													updateConfigVals.push(confVal);
												}
											}
										});	
										updateConfigurations.push(updateConfigVals);
										//return false;
									}
								});	
							}
							
						});	
					}
				});
			}
		});
	}
	//console.log('JSON -'+JSON.stringify(parsedProperties));
	if(typeof parsedProperties != 'undefined' && parsedProperties != null ){
		$.each(parsedProperties, function(key,val){	
			if(typeof key != 'undefined' && key == 'authenticationDetail'){
				val.configurations = updateConfigurations;
			}
		});
	}
	var jsonProperties= JSON.stringify(parsedProperties);
	//console.log(jsonProperties);
	$("#"+authId+" option[value="+$('#'+authId).val()+"]").attr("properties", encodeURIComponent(JSON.stringify(jsonProperties)));
}




function htmlToJson(authDivId, authElementVal){

	var authJson = {};	
	var configurations = [];
	//console.log(configurations+' DIv Id = '+ authDivId);
	$('#'+authDivId).find('.configurations').each(function(){
		$(this).find('.configuration').each(function( ic, vc ) {
			var configuration = [];
			$(this).find('input,select').each(function( i, v ) {
				var input = $(v);
				if($(this).hasClass('configurationType')){				
					let obj = jsonObject(input, authElementVal);	
					configuration.push(obj);
				}
			});	
			if(configuration != undefined) {
				configurations.push(configuration);
			}
		});
	});
	
	$('#'+authDivId).find('.configurations').each(function(){
		var additionalDetails = {};
		
		$(this).find('.additionalProperty').filter(':input').each(function( i, v ) {
			var input = $(v);
			var parentELement = $(this).closest(".childElement").attr("data-parent");
			$.each(configurations, function(key, configuration) {
				var additionalProperties  = [];
				if (typeof key != 'undefined' && typeof configuration != 'undefined') {
					$.each(configuration, function(confKey, confVal) {
						if (typeof confKey != 'undefined' && typeof confVal != 'undefined' && confVal.name == parentELement) {
							//console.log(confVal.name +' = '+confVal.type+' Parent = '+parentELement);
							var additionalDetails = {};
							var additionalProps = [];
							let obj = jsonObject(input, authElementVal);
							if(confVal.additionalDetails != undefined){
								additionalProperties = confVal.additionalDetails['additionalProperties'];
							} else {
								confVal.additionalDetails = {};
							}
							additionalProperties.push(obj);
							confVal.additionalDetails['additionalProperties'] = additionalProperties;
						}
					});
				}
			});
		});	
	});
	//console.log(authElementVal);
	var authenticationType = {};
	if(authElementVal == AuthType.DAO){
		authenticationType.name = "enableDatabaseAuthentication";
		authenticationType.type = "hidden";
		authenticationType.textValue = "Database Authentication";
		authenticationType.value = "true";
		authenticationType.configurationType = "single";
	} else if(authElementVal == AuthType.LDAP){
		authenticationType.name = "enableLdapAuthentication";
		authenticationType.type = "hidden";
		authenticationType.textValue = "LDAP Authentication";
		authenticationType.value = "true";
		authenticationType.configurationType = "multi";
	} else if(authElementVal == AuthType.OAUTH){
		authenticationType.name = "enableOAuthentication";
		authenticationType.type = "hidden";
		authenticationType.textValue = "OAuth Authentication";
		authenticationType.value = "true";
		authenticationType.configurationType = "multi";
	}	
	authJson['authenticationType'] = authenticationType;
	var authenticationDetail = {};
	authenticationDetail['configurations'] = configurations;
	authJson['authenticationDetail'] = authenticationDetail;
	return authJson;
}


function jsonObject(input, authTypeVal) {
	let obj = new Object()
	var attrId 	= input.attr("id");
	var type 	= input.attr("type");
	if(type == 'button')
		obj.name 	= input.attr("nameAttribute");
	else
		obj.name 	= input.attr("name");
	obj.type 	= type;
	
	var condition = input.attr("condition");
   	var labelText = input.attr("textValue");
   	var required = input.attr("require");
   	var defaultValue = input.attr("defaultValue");
   	var dropDownData = {};
   	
   	if(condition != undefined && condition === 'row-added'){
		labelText = $('#'+labelText).val();
		obj.textValue = labelText;
		obj.condition = 'row-added';
		var nameLabel = labelText.replace(/ /g,'');
		obj.name = $.camelCase(nameLabel);
	} else if(labelText != undefined){
		obj.textValue = labelText;
	}
	if(required != undefined){
		obj.required = required;
	}
	
	if (type == "checkbox") {
		obj.type= "boolean";
		var chked = ($("#" + attrId).is(":checked")).toString();
		if(chked == "on" || chked == true || chked == "true") {
			obj.value = "true";
		}
		else {
			obj.value = "false";
		}
	}else {
		obj.value = $("#" + attrId).val();
	}
	
	if(defaultValue != undefined){
		obj.defaultValue = defaultValue;
	}
	if(type == undefined){
		var arr = []; 
        $("#"+attrId).find('option').each(function() { 
			var val = $(this).attr("value");
			var name = $(this).text();
			if(val != "-1"){
				let selObj = new Object()
				selObj.name = name;
				selObj.value = val;
	            arr.push(selObj); 
			}
        }); 
		obj.dropDownData = arr;
		obj.type 	= "select";
	}
	//console.log(type);
	return obj;
}
