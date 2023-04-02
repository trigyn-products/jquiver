class HomePage {
	constructor() {
        setTimeout(function() {
    		getNotifications();
        }, 5000);    

	}
}
 
HomePage.prototype.fn = {

	toggleNavigation: function(){
		if($(".hamburgermenu").hasClass("open")){
			$("#mySidenav").css("width", "0");
			$("#closebtni").removeClass("showcls");
			$("#openbtni").removeClass("hidecls");
			$("#openbtni").addClass("showcls");
			$("#closebtni").addClass("hidecls");
			$('body').css('background-color', 'white');
			$("#bodyDiv").removeClass("overlaycls");
			//$(".jws-body-overlay").removeClass("overlaycls");
		}else{
			$("#mySidenav").css("width", "250px");
			$("#closebtni").addClass("showcls");
			$("#openbtni").addClass("hidecls");
			$('body').css('background-color', 'rgba(0,0,0,0.4)');
			$("#bodyDiv").addClass("overlaycls");
			//$(".jws-body-overlay").addClass("overlaycls");
			$('#searchInput').focus();
		}
		$(".hamburgermenu").toggleClass("open");
	},

	populateBodyContent: function(url) {
		$('#bodyDiv').remove();
		$.ajax({
			type: "POST",
			url: contextPathHome + "/cf/mul/" + url,
			dataType: "html",
			headers: {
				"module-url": url,
			},
			success: function(data) {
				delete contextPath;
				let bodyHtml = $('<div id="bodyDiv"></div>');
				bodyHtml.html(data);
				bodyHtml.insertAfter("#titleDiv");
			},

			error: function(xhr, error) {
				showMessage("Error occurred processing request", "error");
			},

		})

	},

	collapsableMenu: function() {
		$('.nav-item a.clickable').on("click", function(e) {
			if ($(this).hasClass('panel-collapsed')) {
				$(this).parents('.nav-item').find('.collapsein').slideDown();
				$(this).removeClass('panel-collapsed');
				$(this).find('i').removeClass('fa fa-caret-down').addClass('fa fa-caret-up');
			}
			else {
				$(this).parents('.nav-item').find('.collapsein').slideUp();
				$(this).addClass('panel-collapsed');
				$(this).find('i').removeClass('fa fa-caret-up').addClass('fa fa-caret-down');
			}
		});
	},

	clearMenuSearch: function() {
		$("#searchInput").val("");
		this.menuSearchFilter();
	},

	menuSearchFilter: function(a_event) {
		if(a_event && ((a_event.originalEvent && a_event.originalEvent.keyCode == 27) || a_event.keyCode == 27)){
			this.toggleNavigation();
			return;
		}
		
		$("#menuUL").find("li").hide();
		$("#menuUL").find("a").each(function(a_index, a_element) {
			if ($(a_element).text().trim().toUpperCase().indexOf(inputText) > -1) {
				let divGroupElement = $(a_element).closest("div");
				let rootElement = $(divGroupElement).prev();
				$(rootElement).removeClass("panel-collapsed");
				$(rootElement).find("i").removeClass('fa fa-caret-down').addClass('fa fa-caret-up');
				$(a_element).parents().show()
			}
		});
	}
}

const uuidv4 = function() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		let r = Math.random() * 16 | 0;
		let v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	});
}

let messageTypeClass = new Object();
messageTypeClass["success"] = "alert-success";
messageTypeClass["info"] = "alert-info";
messageTypeClass["warn"] = "alert-warning";
messageTypeClass["error"] = "alert-danger";

const showMessage = function(a_messageText, a_messageType) {
	$(".jwsValidationDivCls").each(function(index, a_element) {
		$(a_element).css({ top: $(a_element).position().top + 100 })
	});
	const currentDivID = "jwsValidationDiv" + uuidv4();
	let messageType = a_messageType.toLowerCase();
	let validationElement = $('<div id="' + currentDivID + '" class="jwsValidationDivCls alert common-validation-cls jws-alert-div"><div class="notificationcont"></div></div>');
	$("body").append(validationElement);
	let validationDiv = $("#" + currentDivID);
	let innerDiv = $(validationDiv).find(".notificationcont");

	if(a_messageType && messageTypeClass[a_messageType]){
		validationDiv.addClass(messageTypeClass[a_messageType]);
	}
	
	innerDiv.append(a_messageText);
	innerDiv.appendTo(validationDiv);
	setTimeout(function() {
		$("#" + currentDivID).fadeOut();
		$("#" + currentDivID).remove();
	}, 3000);

}

const typeOfAction = function(formId, selectedButton, saveFunction, backFunction) {
	let selectedButtonId = $(selectedButton).prop("id");
	localStorage.setItem("jwsModuleAction", selectedButtonId);

	if (saveFunction !== undefined || backFunction !== undefined) {
		executeDefinedFunc(formId, selectedButtonId, saveFunction, backFunction);
	} else {
		executeCommonFunc(formId, selectedButtonId);
	}
}

const executeDefinedFunc = function(formId, selectedButtonId, saveFunction, backFunction) {
	let isDataSaved;
	if (selectedButtonId === "saveAndReturn") {
		isDataSaved = saveFunction();
		if (isDataSaved) {
			backFunction();
		} else {
			savedAction(formId, 0);
		}
	} else if (selectedButtonId === "saveAndEdit") {
		saveFunction();
		savedAction(formId, 1);
	} else if (selectedButtonId === "saveAndCreateNew") {
		isDataSaved = saveFunction();
		if (isDataSaved) {
			resetForm(formId);
		}
		savedAction(formId, 0);
	}
}

const executeCommonFunc = function(formId, selectedButtonId) {
	let isDataSaved;
	if (selectedButtonId === "saveAndReturn") {
		isDataSaved = saveData();
		if (isDataSaved) {
			backToPreviousPage();
		} else {
			savedAction(formId, 0);
		}
	} else if (selectedButtonId === "saveAndEdit") {
		saveData();
		savedAction(formId, 1);
	} else if (selectedButtonId === "saveAndCreateNew") {
		isDataSaved = saveData();
		if (isDataSaved) {
			resetForm(formId);
		}
		savedAction(formId, 0);
	}
}

const savedAction = function(formId, isEdit) {

	let actionSaved = localStorage.getItem("jwsModuleAction");

	if ((isEdit === 0 || isEdit === "") && actionSaved === "saveAndEdit") {
		$("#actionDiv").find("#saveAndEdit").remove();
		return true;
	}

	if (actionSaved !== null && actionSaved !== "") {
		let defaultAction = $("#savedAction").find("button");
		let savedActionId = $(defaultAction).prop("id");
		if (savedActionId !== actionSaved) {
			let updatedText = $("#actionDiv").find("#" + actionSaved).text();

			let savedActionText = $("#savedAction").text();
			$("#actionDiv").find("#" + actionSaved).html(savedActionText);
			$("#actionDiv").find("#" + actionSaved).prop("id", savedActionId);

			$(defaultAction).prop("id", actionSaved);
			$(defaultAction).html(updatedText);
		}
	}

	if (isEdit === 0 || isEdit === "") {
		$("#actionDiv").find("#saveAndEdit").remove();
	}
}


const actionOptions = function(sourceElement) {
	if (sourceElement !== undefined) {
		$("#actionDropdownBtn").addClass("panel-collapsed");
		$("#actionDiv").slideUp();
		return true;
	}
	if ($("#actionDropdownBtn").hasClass("panel-collapsed")) {
		$("#actionDropdownBtn").removeClass("panel-collapsed");
		$("#actionDiv").slideDown();
	} else {
		$("#actionDropdownBtn").addClass("panel-collapsed");
		$("#actionDiv").slideUp();
	}
}


const resetForm = function(a_formId) {
	let url = contextPath + "/cf/df";
	let formJson = sessionStorage.getItem(a_formId);

	let form = JSON.parse(formJson);
	$(document.body).append(form);
	let formId = $(form);
	$("#" + formId[0].id).submit();
}

const openForm = function(formId, formDetails, formUrl) {
	$("#addEditDynForm").remove();
	var url = contextPath + (formUrl != undefined ? formUrl : "/cf/df");
	var form = $("<form id='addEditDynForm' method='post' action='" + url + "'></form>");
	$('body').append(form);
	$("#addEditDynForm").append("<input type='hidden' name='formId' value='" + formId + "'>");
	for (var counter = 0; counter < Object.keys(formDetails).length; ++counter) {
		let fieldName = Object.keys(formDetails)[counter];
		let fieldValue = formDetails[fieldName];
		$("#addEditDynForm").append("<input type='hidden' name='" + fieldName + "' value='" + fieldValue + "'>");
	}
	$("#addEditDynForm").submit();
}

const openNewForm = function(formId, formDetails, formUrl) {
	$("#addEditDynForm").remove();
	var form = $("<form id='addEditDynForm' method='get'></form>");
	$('body').append(form);
	$("#addEditDynForm").append("<input type='hidden' name='formId' value='" + formId + "'>");
	for (var counter = 0; counter < Object.keys(formDetails).length; ++counter) {
		let fieldName = Object.keys(formDetails)[counter];
		let fieldValue = formDetails[fieldName];
		$("#addEditDynForm").append("<input type='hidden' name='" + fieldName + "' value='" + fieldValue + "'>");
	}
	$("#addEditDynForm").submit();
}

const enableVersioning = function(formData) {
	$.ajax({
		type: "POST",
		url: contextPath + "/cf/smv",
		dataType: "html",
		data: formData,
		success: function(data) {
		},
		error: function(xhr, error) {
			//No need to show this error. It comes when page get's redirected.
			//showMessage("Error occurred saving versioning information", "error");
		},
	});
}

const hideShowActionButtons = function() {
	$(document).on("click", function closeMenu(event) {
		if ("actionDropdownBtn" !== event.target.id) {
			actionOptions(event.target.id);
		}
	});
}

const jqOpenDeletConfirmation = function(a_successCallBack, a_cancelCallBack, deleteMsg) {
	var $temp = $("<div id='deleteHeader'>");
	if (deleteMsg === undefined || deleteMsg.trim() === '') {
		deleteMsg = 'Do you want to delete?';
	}
	$("body").append($temp);

	$("#deleteHeader").html(deleteMsg);
	$("#deleteHeader").dialog({
		bgiframe: true,
		autoOpen: true,
		modal: true,
		closeOnEscape: true,
		dialogClass: "popuptop",
		draggable: true,
		resizable: false,
		title: "Delete",
		position: {
			my: "center",
			at: "center"
		},
		buttons: [{
			text: "Cancel",
			click: function() {
				$(this).dialog('close');
				$temp.remove();
				if (a_cancelCallBack) {
					a_cancelCallBack();
				}
			},
		},
		{
			text: "Delete",
			click: function() {
				$(this).dialog('close');
				$temp.remove();
				if (a_successCallBack) {
					a_successCallBack();
				}
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

const generateHashCode = function(inputStr) {
	let hash = 0;
	let iCounter;
	let char;
	for (iCounter = 0; iCounter < inputStr.length; iCounter++) {
		char = inputStr.charCodeAt(iCounter);
		hash = ((hash << 5) - hash) + char;
		hash |= 0;
	}
	return hash;
}

const disableInputSuggestion = function() {
	$("input[type=text], input[type=email], input[type=tel], input[type=number], input[type=search]").attr("autocomplete", "off");
}

const resizeMonacoEditor = function(monacoEditorObj, containerName, childContainerName) {
	if ("Escape" === containerName) {
		$('.ace-editor').width(1108).height(350);
	}
	if (document.fullscreenElement || document.webkitFullscreenElement
		|| document.mozFullScreenElement || document.msFullscreenElement) {

		$('.ace-editor').width(1108).height(350);
		if (document.exitFullscreen) {
			document.exitFullscreen();
		} else if (document.mozCancelFullScreen) {
			document.mozCancelFullScreen();
		} else if (document.webkitExitFullscreen) {
			document.webkitExitFullscreen();
		} else if (document.msExitFullscreen) {
			document.msExitFullscreen();
		}
	} else {
		let element = document.getElementById(containerName);
		let height = screen.height;
		let width = $(window).width();

		$('#' + childContainerName).width(width).height(height);
		let func = function() {
			onFullScreenChange(monacoEditorObj, containerName, childContainerName);
		};
		if (element.requestFullscreen) {
			document.removeEventListener("fullscreenchange", func);
			element.requestFullscreen();
			document.addEventListener("fullscreenchange", func, false);
		} else if (element.mozRequestFullScreen) {
			document.removeEventListener("mozfullscreenchange", func);
			element.mozRequestFullScreen();
			document.addEventListener("mozfullscreenchange", func, false);
		} else if (element.webkitRequestFullscreen) {
			document.removeEventListener("webkitfullscreenchange", func);
			element.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
			document.addEventListener("webkitfullscreenchange", func, false);
		} else if (element.msRequestFullscreen) {
			document.removeEventListener("msfullscreenchange", func);
			element.msRequestFullscreen();
			document.addEventListener("msfullscreenchange", func, false);
		}
	}

	monacoEditorObj.layout();
};

const onFullScreenChange = function(monacoEditorObj, containerName, childContainerName) {
	var fullscreenElement = document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement;

	if (fullscreenElement == null) {
		$('#' + childContainerName).width(1108).height(350);
		$('#' + containerName).css("overflow-y", "hidden");
		monacoEditorObj.layout();
	}
};

const capitalizeFirstLetter = function(inputStr) {
	let updatedStr = inputStr.toLowerCase().replace(/^[\u00C0-\u1FFF\u2C00-\uD7FF\w]|\s[\u00C0-\u1FFF\u2C00-\uD7FF\w]/g, function(letter) {
		return letter.toUpperCase();
	});
	return updatedStr;
}

const formatDate = function(dateStr) {
	const formattedDate = Calendar.printDate(new Date(Date.parse(dateStr)), jqJSDateFormat);
	return formattedDate;
}

Array.prototype.formatSerializedArray = function() {
	for (let counter = 0; counter < this.length; ++counter) {
		if (this[counter]["valueType"] == undefined) {
			this[counter]["valueType"] = $("#" + this[counter].name).attr('data-type');
		}
	}
	return this;
}

const getCookie = function(cname) {
	let name = cname + "=";
	let decodedCookie = decodeURIComponent(document.cookie);
	let ca = decodedCookie.split(";");
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == " ") {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

const changeLanguage = function() {
	var localeId = $("#languageOptions").find(":selected").val();

	$.ajax({
		async: false,
		type: "GET",
		cache: false,
		url: contextPath + "/cf/cl?lang=" + localeId,
		success: function(data) {

			setCookie("locale", localeId, 1);
			location.reload();
		}
	});
}

const setCookie = function(cname, cvalue, exdays) {
	let d = new Date();
	d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
	var expires = "expires=" + d.toUTCString();
	document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

const getLanguageOption = function() {
	$.ajax({
		type: "POST",
		url: contextPathHome + "/cf/gl",
		success: function(data) {
			let options = "";
			let selectedLanguageId = getCookie("locale");
			for (let i = 0; i < data.length; i++) {
				if (data[i].localeId == selectedLanguageId) {
					options += "<option selected value='" + data[i].localeId + "'>" + data[i].languageName + "</option>";
				} else {
					options += "<option value='" + data[i].localeId + "'>" + data[i].languageName + "</option>";
				}
			}
			$("#languageOptions").empty().append(options);
		},
		error: function(xhr, error) {
			showMessage("Error occurred while getting language list", "error");
		},
	});

}

const copyGenericContent = function(contentComponentId) {
	var $temp = $("<textarea>");
	$("body").append($temp);
	$temp.val($("#" + contentComponentId).text()).select();
	document.execCommand("copy");
	$temp.remove();
	showMessage("Copied successfully.", "success")
}

const copyModuleURL = function(contentComponentId) {
	let input = $("<input>");
	$("body").append(input);
	var moduleURL = $("#" + contentComponentId).val();
	input.val('${contextPath}' + "/view/" + moduleURL).select();
	document.execCommand("copy");
	input.remove();
	showMessage("Copied successfully.", "success");
}

const copyUrlContent = function(urlPrefix, contentComponentId) {
	let $temp = $("<input>");
	$("body").append($temp);
	$temp.val(urlPrefix + $("#" + contentComponentId).val()).select();
	document.execCommand("copy");
	$temp.remove();
	showMessage("Copied successfully.", "success");
}
/*For getting the images for all entities using Quick Jump Autocomplete and User-favourite-Autocomplete*/
function getImageNameByType(entityType) {
	switch (entityType) {
		case "1":
			return "template";
		case "2":
			return "daynamicreport";
		case "3":
			return "grid";
		case "4":
			return "autotype";
		case "5":
			return "API_listing_icon";
		case "6":
			return "dashboard";
		case "7":
			return "dashlet";
		case "8":
			return "upload_management";
		case "9":
			return "schedulericon";
		case "10":
			return "clientApiicon";
		case "11":
			return "additionalresourcesicon";
		case "12":
			return "Menu_icon";
		default:
			return "noimageicon";

	}
}
/* For opening a new tab after selecting the entity from the Quick Jump Autocomplete and User Favourite Autocomplete*/
function submitForm(selectedEntity) {
	let form = $(JSON.parse(selectedEntity.formData));
	$("body").append(form);
	$(form).attr("action", contextPath + $(form).attr("action"));
	$(form).find("input").each(function(index, inputElem) {
		if ($(inputElem).attr("name") !== "formId") {
			$(inputElem).val(selectedEntity.entityId);
		}
	})
	$(form).attr('target', '_blank');
	$('body').append(form);
	$(form).submit();
}
/*For displaying all the entities based on Custom, Select, All and Favourites.*/
function changeType() {
	var type_id = $("#typeSelect").val();
	if (type_id == '3') {
		autocompleteQJ.options["autocompleteId"] = "user-favorite-entity-autocomplete";
		autocompleteQJ.resetAutocomplete();
	} else {
		if (autocompleteQJ.options["autocompleteId"] == "user-favorite-entity-autocomplete") {
			autocompleteQJ.options["autocompleteId"] = "quickJumpAutocomplete";
		}
		autocompleteQJ.options["additionalParamaters"]["type_id"] = type_id;
		autocompleteQJ.resetAutocomplete();
	}
}

function deleteCookie(name) {
	document.cookie = name + '=; Max-Age=-99999999;; path=/';
}

$(function () {
	$.fn.bindFirst = function(name, fn) {
	    this.on(name, fn);
	    this.each(function() {
	        var handlers = $._data(this, 'events')[name.split('.')[0]];
	        var handler = handlers.pop();
	        handlers.splice(0, 0, handler);
	    });
	};
});