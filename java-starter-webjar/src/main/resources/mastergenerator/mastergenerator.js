

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
    resourceKeyMap = new Map();
    $("#menuDisplayName").val("");
	$("#parentModuleName").val("");
	$("#moduleURL").val("");
	$("#moduleName").val("");
}

function createTable(columns) {
    $(".details").remove();
    for(let iCounter = 0; iCounter < columns.length; ++iCounter) {
        let trElement = $("<tr class='details'></tr>");
        $(trElement).append("<td><input id='tenabled_"+iCounter+"' type='checkbox' onchange='addRemoveToGridDetails(this)'></td>");
        $(trElement).append("<td><input id='thidden_"+iCounter+"' type='checkbox' disabled onchange='updateGridDetails(this)'></td>");
        let displayName = capitalizeFirstLetter(columns[iCounter].replaceAll("_", " "));
        $(trElement).append("<td><label id='tcolumn_"+iCounter+"'>"+columns[iCounter]+"</label></td>");
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"_i18n' disabled type='text' data-previous-key='' value='' onchange='updateGridDetailsI18nResourceKey(this.id)' placeholder='I18N Resource Key'></td>");
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"' disabled type='text' onchange='updateGridDetailsDisplayName(this.id)' value='"+displayName+"'></td>");
        $(trElement).append("<input id='tdisplay_"+iCounter+"_hd' type='hidden' value='"+displayName+"'>");

        $("#listingDetailsTable").append(trElement);
    }

    for(let iCounter = 0; iCounter < columns.length; ++iCounter) {
        let trElement = $("<tr class='details'></tr>");
        $(trElement).append("<td><input id='tfenabled_"+iCounter+"' type='checkbox' onchange='addRemoveToFormDetails(this)'></td>");
        $(trElement).append("<td><input id='tfhidden_"+iCounter+"' type='checkbox' disabled onchange='updateFormDetails(this)'></td>");
        let displayName = capitalizeFirstLetter(columns[iCounter].replaceAll("_", " "));
        $(trElement).append("<td><label id='tfcolumn_"+iCounter+"'>"+columns[iCounter]+"</label></td>");
        $(trElement).append("<td><input id='tfdisplay_"+iCounter+"_i18n' disabled type='text' data-previous-key='' value='' onchange='updateFormDetailsI18nResourceKey(this.id)' placeholder='I18N Resource Key'></td>");
        $(trElement).append("<td><input id='tfdisplay_"+iCounter+"' disabled type='text' onchange='updateFormDetailsDisplayName(this.id)' value='"+displayName+"'></td>");
		$(trElement).append("<input id='tfdisplay_"+iCounter+"_hd' type='hidden' value='"+displayName+"'>");
		 
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
    $("#tfhidden_"+counter).prop("disabled", !element.checked);
    $("#tfdisplay_"+counter).prop("disabled", !element.checked);
    $("#tfdisplay_"+counter+"_i18n").prop("disabled", !element.checked);

    if(element.checked) {
        let details = new Object();
        details["index"] = counter;
        details["displayName"] = $("#tfdisplay_"+counter).val();
        details["hidden"] = $("#tfhidden_"+counter).prop("checked");
        details["column"] = $("#tfcolumn_"+counter).html().trim();
        details["i18nResourceKey"] = $("#tfdisplay_"+counter+"_i18n").val().trim();
        formDetails.push(details);
    } else {
        removeByAttribute(formDetails, "index", counter);
    }
}

function updateGridDetailsI18nResourceKey(elementId){
	const counter = elementId.split("_")[1];
	const resourceKey = $("#"+elementId).val().trim();
	
	$.each(gridDetails, function(iCounter, gridElement){
		if(gridElement.index == counter){
			gridElement.i18nResourceKey = resourceKey;
		}
	});
	getMultilingualData(elementId);
	$("#"+elementId).data("previous-key", resourceKey);
}


function updateFormDetailsI18nResourceKey(elementId){
	const counter = elementId.split("_")[1];
	const resourceKey = $("#"+elementId).val().trim();
	
	$.each(formDetails, function(iCounter, formElement){
  		if(formElement.index == counter){
  			formElement.i18nResourceKey = $("#"+elementId).val().trim();
  		}
	});
	getMultilingualData(elementId);	
	$("#"+elementId).data("previous-key", resourceKey);
}

function updateGridDetailsDisplayName(elementId){
	const counter = elementId.split("_")[1];
	const displayText = $("#"+elementId).val().trim();
	const resourceKey = $("#"+elementId+"_i18n").val().trim();
	
	$("#"+elementId+"_hd").val(displayText);
	$.each(gridDetails, function(iCounter, gridElement){
  		if(gridElement.index == counter){
  			gridElement.displayName = displayText
  		}
	});
	getMultilingualData(elementId+"_i18n", displayText);
}


function updateFormDetailsDisplayName(elementId){
	const counter = elementId.split("_")[1];
	const displayText = $("#"+elementId).val().trim();
	const resourceKey = $("#"+elementId+"_i18n").val().trim();
	
	$("#"+elementId+"_hd").val(displayText);
	$.each(formDetails, function(iCounter, formElement){
  		if(formElement.index == counter){
  			formElement.displayName = displayText;
  		}
	});
	getMultilingualData(elementId+"_i18n", displayText);
}

function updateOtherElements(resourceKey){
	let resourceObj = resourceKeyMap.get(resourceKey);
	let elementIdArray = resourceObj["elementIds"];
	let resourceTxt = resourceObj["i18nText"];
	elementIdArray.sort((a, b) => a > b ? 1: -1);
	for(let iCounter = 0; iCounter < elementIdArray.length; iCounter++){
		let displayId = elementIdArray[iCounter];
		displayId = displayId.substring(0, displayId.lastIndexOf("_"));
		let dCounter = displayId.split("_")[1];
		$("#"+displayId).val(resourceTxt);
		if(iCounter == 0){
			$("#"+displayId).prop("disabled", false);
		}else{
			$("#"+displayId).prop("disabled", true);
		}
		
		if(displayId.startsWith("tdisplay")){
			gridDetails[dCounter].displayName = resourceTxt;
		}else{
			formDetails[dCounter].displayName = resourceTxt;
		}
	} 
}

function getMultilingualData(elementId, resourceTxt){
	const displayId = elementId.substring(0, elementId.lastIndexOf("_"));
	const resourceKey = $("#"+elementId).val().trim();
	const previousKey = $("#"+elementId).data("previous-key");
	
	let oldKeyObj = resourceKeyMap.get(previousKey);
	if(oldKeyObj !== undefined && resourceKey !== previousKey){
		let oldElementIds = resourceKeyMap.get(previousKey)["elementIds"];
		oldElementIds = oldElementIds.filter(element => element !== elementId);
		resourceKeyMap.get(previousKey)["elementIds"] = oldElementIds;
		if(oldElementIds.length == 0){
			resourceKeyMap.delete(previousKey);
		}else if(oldKeyObj.existingKey === false){
			updateOtherElements(previousKey);
		}
	}
	
	let i18nObj = resourceKeyMap.get(resourceKey);
	if(resourceTxt !== undefined){
		i18nObj["i18nText"] = resourceTxt;
		updateOtherElements(resourceKey);
	}else if(i18nObj !== undefined){
		$("#"+displayId).val(i18nObj["i18nText"]);
		resourceKeyMap.get(resourceKey)["elementIds"].push(elementId);
		if(i18nObj.existingKey === false){
			updateOtherElements(resourceKey);
		}else{
			$("#"+displayId).prop("disabled", true);
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
					updateResourceKeyMap(data, elementId, displayId, resourceKey);
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

function updateResourceKeyMap(data, elementId, displayId, resourceKey){
	let i18nObj = new Object();
	let elementIdArray = new Array();
	elementIdArray.push(elementId);
	i18nObj["elementIds"] = elementIdArray;
		        	
	if(data !== undefined && data.trim() !== ""){
		$("#"+displayId).val(data);
		$("#"+displayId).prop("disabled", true);
		i18nObj["existingKey"] = true;
		i18nObj["i18nText"] = data;
		resourceKeyMap.set(resourceKey, i18nObj);
	}else {
		let initialVal = $("#"+displayId+"_hd").val().trim();
		$("#"+displayId).val(initialVal);
		$("#"+displayId).prop("disabled", false);
		        		
		if($("#isDev").val() == "false" && resourceKey.startsWith("jws.") == true){
			$("#"+elementId).val("");
			showMessage("I18N key can not starts with jws.", "error");
			return false;
		}
		i18nObj["existingKey"] = false;
		i18nObj["i18nText"] = initialVal;
		resourceKeyMap.set(resourceKey, i18nObj);
	}
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
            setTimeout(function(){
            	backToPreviousPage();
            }, 1500);
            
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
		showMessage("Please include at least one column in form", "error");
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
	 let moduleName = $("#menuDisplayName").val().trim();
	 let moduleUrl = $("#moduleURL").val().trim();
	 if(moduleName === "" || moduleUrl === ""){
	 	showMessage("Module Name and Module URL cannot be blank", "error");
	 	return false;
	 }
	 $.ajax({
        url: contextPath+ "/cf/ced",
        type: 'GET',
        async: false,
        headers: {
        	"module-name": moduleName,
        	"parent-module-id": $("#parentModuleName").val(),
        	"module-url": moduleUrl,
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