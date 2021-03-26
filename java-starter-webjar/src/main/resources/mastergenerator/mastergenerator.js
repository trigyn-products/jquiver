

function backToPreviousPage() {
	location.href = contextPath+"/cf/home";
}

function populateFields(tableName){
    let selectedTable = tableName;
    $.ajax({
        url  : contextPath + "/cf/mtd",
        type : 'GET',
        data : {tableName: selectedTable},
        success : function(data) {
			resetObjects();
            $("#moduleName").val(selectedTable.replaceAll("_", "-"));
            let primaryKey = data.filter(element => element.columnKey == "PRI").map(element => element["columnName"]).toString();
            let columns = data.map(element => element["columnName"]);
            $("#columns").val(columns.toString());
            $("#primaryKey").val(primaryKey);
            createTable(columns);
        }
    });
}

function resetObjects(){
    gridDetails = new Array();
    formDetails = new Array();
    menuDetails = new Object();
    $("#menuDisplayName").val("");
	$("#parentModuleName").val("");
	$("#moduleURL").val("");
}

function createTable(columns) {
    $(".details").remove();
    for(let iCounter = 0; iCounter < columns.length; ++iCounter) {
        let trElement = $("<tr class='details'></tr>");
        $(trElement).append("<td><input id='tenabled_"+iCounter+"' type='checkbox' onchange='addRemoveToGridDetails(this)'></td>");
        $(trElement).append("<td><input id='thidden_"+iCounter+"' type='checkbox' disabled onchange='updateGridDetails(this)'></td>");
        let displayName = capitalizeFirstLetter(columns[iCounter].replaceAll("_", " "));
        $(trElement).append("<td><label id='tcolumn_"+iCounter+"'>"+columns[iCounter]+"</label></td>");
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"_i18n' disabled type='text' data-previous-key='' onchange='updateGridDetailsI18nResourceKey(this.id)' placeholder='I18N Resource Key'></td>");
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"' disabled type='text' onchange='updateGridDetailsDisplayName(this.id)' value='"+displayName+"'></td>");
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"_hd' type='hidden' value='"+displayName+"'></td>");

        $("#listingDetailsTable").append(trElement);
    }

    for(let iCounter = 0; iCounter < columns.length; ++iCounter) {
        let trElement = $("<tr class='details'></tr>");
        $(trElement).append("<td><input id='fenabled_"+iCounter+"' type='checkbox' onchange='addRemoveToFormDetails(this)'></td>");
        $(trElement).append("<td><input id='fhidden_"+iCounter+"' type='checkbox' disabled onchange='updateFormDetails(this)'></td>");
        let displayName = capitalizeFirstLetter(columns[iCounter].replaceAll("_", " "));
        $(trElement).append("<td><label id='fcolumn_"+iCounter+"'>"+columns[iCounter]+"</label></td>");
        $(trElement).append("<td><input id='fdisplay_"+iCounter+"_i18n' disabled type='text' data-previous-key='' onchange='updateFormDetailsI18nResourceKey(this.id)' placeholder='I18N Resource Key'></td>");
        $(trElement).append("<td><input id='fdisplay_"+iCounter+"' disabled type='text' onchange='updateFormDetailsDisplayName(this.id)' value='"+displayName+"'></td>");
		$(trElement).append("<td><input id='fdisplay_"+iCounter+"_hd' type='hidden' value='"+displayName+"'></td>");
		 
        $("#formDetailsTable").append(trElement);
    }
    disableInputSuggestion();
}

function addRemoveToGridDetails(element){
    const counter = element.id.split("_")[1];
    $("#thidden_"+counter).prop("disabled", !element.checked);
    $("#tdisplay_"+counter).prop("disabled", !element.checked);
    $("#tdisplay_"+counter+"_i18n").prop("disabled", !element.checked);

    if(element.checked) {
        let details = new Object();
        details["index"] = counter;
        details["displayName"] = $("#tdisplay_"+counter).val();
        details["hidden"] = $("#thidden_"+counter).prop("checked");
        details["column"] = $("#tcolumn_"+counter).html().trim();
        details["i18nResourceKey"] = $("#tdisplay_"+counter+"_i18n").val().trim();

        gridDetails.push(details);
    } else {
        removeByAttribute(gridDetails, "index", counter);
    }
   
}

function updateGridDetails(element){
	const counter = element.id.split("_")[1];
	$.each(gridDetails, function(iCounter, gridElement){
  		if(gridElement.index == counter){
  			gridElement.hidden = element.checked;
  		}
	});
}

function updateFormDetails(element){
	const counter = element.id.split("_")[1];
	$.each(formDetails, function(iCounter, formElement){
  		if(formElement.index == counter){
  			formElement.hidden = element.checked;
  		}
	});
}

function addRemoveToFormDetails(element){
	const counter = element.id.split("_")[1];
    $("#fhidden_"+counter).prop("disabled", !element.checked);
    $("#fdisplay_"+counter).prop("disabled", !element.checked);
    $("#fdisplay_"+counter+"_i18n").prop("disabled", !element.checked);

    if(element.checked) {
        let details = new Object();
        details["index"] = counter;
        details["displayName"] = $("#fdisplay_"+counter).val();
        details["hidden"] = $("#fhidden_"+counter).prop("checked");
        details["column"] = $("#fcolumn_"+counter).html().trim();
        details["i18nResourceKey"] = $("#fdisplay_"+counter+"_i18n").val().trim();
        formDetails.push(details);
    } else {
        removeByAttribute(formDetails, "index", counter);
    }
}

function updateGridDetailsI18nResourceKey(elementId){
	const counter = elementId.split("_")[1];
	const resourceKey = $("#"+elementId).val().trim();
	
	getMultilingualData(elementId);
	$.each(gridDetails, function(iCounter, gridElement){
		if(gridElement.index == counter){
			gridElement.i18nResourceKey = resourceKey;
		}
	});
	enableDisableFormInput(elementId);
	updateResourceKeyMap(elementId, resourceKey);
}


function updateFormDetailsI18nResourceKey(elementId){
	const counter = elementId.split("_")[1];
	const resourceKey = $("#"+elementId).val().trim();
	
	getMultilingualData(elementId);	
	$.each(formDetails, function(iCounter, formElement){
  		if(formElement.index == counter){
  			formElement.i18nResourceKey = $("#"+elementId).val().trim();
  		}
	});
	updateResourceKeyMap(elementId, resourceKey);
}

function updateGridDetailsDisplayName(elementId){
	const counter = elementId.split("_")[1];
	const displayText = $("#"+elementId).val().trim();
	const resourceKey = $("#"+elementId+"_i18n").val().trim();
	
	$("#"+elementId+"_hd").val(displayText);
	$.each(gridDetails, function(iCounter, gridElement){
  		if(gridElement.index == counter){
  			gridElement.displayName = displayText
  			if(resourceKey !== ""){
  				resourceKeyMap.set(resourceKey, displayText);
  			}
  		}
	});
	
	updateFormI18N(elementId, resourceKey, displayText);
}


function updateFormDetailsDisplayName(elementId){
	const counter = elementId.split("_")[1];
	const displayText = $("#"+elementId).val().trim();
	const resourceKey = $("#"+elementId+"_i18n").val().trim();
	
	$("#"+elementId+"_hd").val(displayText);
	$.each(formDetails, function(iCounter, formElement){
  		if(formElement.index == counter){
  			formElement.displayName = displayText;
  			if(resourceKey !== ""){
  				resourceKeyMap.set(resourceKey, displayText);
  			}
  		}
	});
}


function getMultilingualData(elementId){
	const initialId = elementId.split("_")[0];
	const counter = elementId.split("_")[1];
	const displayId = initialId+"_"+counter;
	const resourceKey = $("#"+elementId).val().trim();
	
	if(resourceKeyMap.get(resourceKey) !== undefined){
		$("#"+displayId).val(resourceKeyMap.get(resourceKey));
		if(existingKey.some(key => key = resourceKey)){
			$("#"+displayId).prop("disabled", true);
		}else{
			enableDisableFormInput(elementId);
		}
	}else{
		if(resourceKey !== ""){
			$.ajax({
		        url: contextPath+ "/cf/rtbkl",
		        type: 'POST',
		        async: false,
		        data: {
		        	resourceBundleKey : resourceKey,
		        },
		        success: function(data) {
		        	if(data !== undefined && data.trim() !== ""){
		        		$("#"+displayId).val(data);
		        		$("#"+displayId).prop("disabled", true);
		        		existingKey.push(resourceKey);
		        	}else {
		        		let initialVal = $("#"+displayId+"_hd").val().trim();
		        		$("#"+displayId).val(initialVal);
		        		$("#"+displayId).prop("disabled", false);
		        		if($("#isDev").val() == "false" && resourceKey.startsWith("jws.") == true){
		        			showMessage("I18N key can not starts with jws.", "error");
		        			return false;
		        		}
		        	}
		        	if(initialId === "tdisplay"){
		        		updateGridDetailsDisplayName(displayId);
		        	}else{
		        		updateFormDetailsDisplayName(displayId);
		        	}
		        	resourceKeyMap.set(resourceKey, $("#"+displayId).val());
		        },
				error : function(xhr, error){
					showMessage("Error occurred while fetching multilingual text", "error");
			   	},
						
		    });
	    }else{
	    	$("#"+displayId).prop("disabled", false);
	    }
	}
}

function updateResourceKeyMap(elementId, resourceKey){
	let previousKey = $("#"+elementId).data("previous-key");
	let keyUsed = false;
	$('input[id$="_i18n"]').each(function(index, element){
		if($(this).attr("id") !== elementId && $(this).val() === previousKey){
			keyUsed = true;
		}
	});
	if(keyUsed == false && previousKey !== resourceKey){
		resourceKeyMap.delete(previousKey);
	}
	$("#"+elementId).data("previous-key", resourceKey);
}

function enableDisableFormInput(elementId){
	const previousKey = $("#"+elementId).data("previous-key").trim();
	const resourceKey = $("#"+elementId).val().trim();
	
	let iCounter = 0;	
	$('input[id$="_i18n"]').each(function(index, element){
		let i18nId = $(this).attr("id");
		let displayId = i18nId.split("_")[0] + "_" + i18nId.split("_")[1];
		let i18nKey = $(this).val().trim();
		if(existingKey.some(key => key = i18nKey) == false && previousKey !== "" && i18nKey === previousKey){
			if(iCounter != 0){
				$("#"+displayId).prop("disabled", false);
			}
			iCounter += 1;
		}
	});
	
	iCounter = 0;
	$('input[id$="_i18n"]').each(function(index, element){
		let i18nId = $(this).attr("id");
		let displayId = i18nId.split("_")[0] + "_" + i18nId.split("_")[1];
		if(resourceKey !== "" && $(this).val().trim() === resourceKey){
			if(iCounter != 0){
				$("#"+displayId).prop("disabled", true);
			}
			iCounter += 1;
		}
		
	});
}

function updateFormI18N(elementId, resourceKey, displayText){
	let selectedDisplayId = elementId + "_i18n";
	$('input[id$="_i18n"]').each(function(index, element){
		if(resourceKey !== "" && $(this).attr("id") !== selectedDisplayId && $(this).val() === resourceKey){
			let i18nId = $(this).attr("id");
			let displayId = i18nId.split("_")[0] + "_" + i18nId.split("_")[1];
			$("#"+displayId).val(displayText);
			$("#"+displayId).prop("disabled", true);
		}
	})
}


function createMaster() {
	let isValidData = validateForm();
	if(isValidData === false){
		return false;
	}
	menuDetails["moduleName"]=$("#menuDisplayName").val();
	menuDetails["parentModuleId"]=$("#parentModuleName").val();
	menuDetails["moduleURL"]=$("#moduleURL").val();
	
	let roleIds =[];
	 $.each($("#rolesMultiselect_selectedOptions_ul span.ml-selected-item"), function(key,val){
		 roleIds.push(val.id);
    	
    });
	
    let formData = $("#createMasterForm").serialize();
    $.ajax({
        url: contextPath+ "/cf/cm",
        data: {
        	formData: formData,
        	gridDetails: JSON.stringify(gridDetails),
        	formDetails: JSON.stringify(formDetails),
        	menuDetails: JSON.stringify(menuDetails),
        	roleIds : JSON.stringify(roleIds),
        },
        type: 'POST',
        success: function(data) {
            showMessage("Master modules created successfully", "success");
            
        },
		error : function(xhr, error){
			showMessage("Error occurred while creating master", "error");
	   	},
				
    })
}


function validateForm(){
	$('#errorMessage').hide();
	
	if($("#showInMenu").prop("checked")){
		if(validateSiteLayoutDetails() == false){
			return false;
		}
	}
	if(gridDetails.length >= 1){
		let isColVisible = false;
		gridDetails.forEach((element) => {
    		if(element.hidden == false){
    			isColVisible = true;
    		}
		});
		if(isColVisible == false){
			showMessage("Please mark at least one column as visible in grid", "error");
			return false;
		}
	}else{
		showMessage("Please include at least one column in grid", "error");
		return false;
	}
	
	if(formDetails.length >= 1){
		let isColVisible = false;
		formDetails.forEach((element) => {
    		if(element.hidden == false){
    			isColVisible = true;
    		}
		});
		if(isColVisible == false){
			showMessage("Please mark at least one column as visible in form", "error");
			return false;
		}
	}else{
		showMessage("Please include at least one column as in form", "error");
		return false;
	}
	
	let isValid = true;
	$.each(gridDetails, function(iCounter, gridElement){
		if(gridElement.hidden === false && gridElement.displayName.trim() === ""){
			showMessage("Please enter valid display name", "error");
			isValid = false;
		}
	});
	
	if(isValid === true){
		$.each(formDetails, function(iCounter, formElement){
			if(formElement.hidden === false && formElement.displayName.trim() === ""){
				$('#errorMessage').html("Please enter valid display name", "error");
				isValid = false;
			}
		});
	}
	return isValid;
}

function validateSiteLayoutDetails(){
	 let isValid = true;
	 $.ajax({
        url: contextPath+ "/cf/ced",
        type: 'GET',
        async: false,
        headers: {
        	"module-name": $("#menuDisplayName").val().trim(),
        	"parent-module-id": $("#parentModuleName").val(),
        	"module-url": $("#moduleURL").val().trim(),
        	"module-id": uuidv4(),
        },
        success: function(data) {
        	if(data !== undefined && data !== ""){
	        	if(data.moduleIdName !== undefined && data.moduleIdName !== ""){
	        		isValid = false;
	        		showMessage("Module Name already exist", "error");
	        	}else if(data.moduleIdURL !== undefined &&  data.moduleIdURL !== ""){
	        		isValid = false;
	        		showMessage("Module URL already exist", "error");
	        	}
        	}
        },
		error : function(xhr, error){
			showMessage("Error occurred while creating master", "error");
	   	},
				
    });	
	return isValid;	
}

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

var removeByAttribute = function(arr, attr, value){
    var i = arr.length;
        while(i--){
        if( arr[i] 
            && arr[i].hasOwnProperty(attr) 
            && (arguments.length > 2 && arr[i][attr] === value ) ){ 
            arr.splice(i,1);
        }
    }
    return arr;
}

function enableDisableMenuAdd(){
		let context = this;
		let isInsideMenu = $("#showInMenu").prop("checked");
		//let targetLookupId = $("#targetLookupType").find(":selected").val();
		if(!isInsideMenu){
			$("#isMenuAddActive").val(0);
			$("#parentModuleName").val("");
			$("#parentModuleName").prop('disabled',true);
			$("#menuDisplayName").prop('disabled',true);
			$("#moduleURL").prop('disabled',true);
		}
		else{
			$("#isMenuAddActive").val(1);
			$("#parentModuleName").val("");
			$("#parentModuleName").prop('disabled',false);
			$("#menuDisplayName").prop('disabled',false);
			$("#moduleURL").prop('disabled',false);
			
   		}
		
	}