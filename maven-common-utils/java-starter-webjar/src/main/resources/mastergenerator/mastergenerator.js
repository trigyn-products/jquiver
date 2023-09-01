

function backToPreviousPage() {
	location.href = contextPath+"/cf/home";
}
let  primaryKey = '';
let  primaryKeyCounter = 0;
function populateFields(tableName, dbProductID){
	$.blockUI({ message: "<img src='"+contextPathHome+"/webjars/1.0/images/loading.gif' />" });
    let selectedTable = tableName;
    $.ajax({
        url  : contextPath + "/cf/mtd",
        type : 'GET',
        data : {
        	tableName: selectedTable,
        	dbProductID : dbProductID
        },
        success : function(data) {
        	    
        	$("#moduleName").prop("readonly", false);
        	$("#menuDisplayName").prop("readonly", false);  
        	$("#moduleURL").prop("readonly", false); 
        	$("#formDisplayName").prop("readonly", false); 
        	$("#formModuleURL").prop("readonly", false); 
        	
			resetObjects();
            $("#moduleName").val(selectedTable.replaceAll("_", "-"));
            $("#formDisplayName").val(selectedTable.replaceAll("_", "-")+"-form");
            $("#formModuleURL").val(selectedTable.replaceAll("_", "-")+"-f");
             primaryKey = data.filter(element => element.columnKey == "PK").map(element => element["tableColumnName"]).toString();
             primaryKeyCounter = data.filter(element => {
            	 if(element.columnKey == "PK"){
            		 return true;
            	 }
            	 return false;
             }).length;
            let columns = data.map(element => element["tableColumnName"]);
            $("#columns").val(columns.toString());
            $("#primaryKey").val(primaryKey);
            $("#menuDisplayName").val(selectedTable.replaceAll("_", "-"));
            $("#moduleURL").val(selectedTable.replaceAll("_", "-"));
            createTable(data);
            $.unblockUI();
        },
		error : function(xhr, error){
			if(xhr.status == 406){
				showMessage("Database reference has unsupported datatype, recheck!", "warn");			
			}else if(xhr.status == 409){
				showMessage("Modules with given data already exists! Re enter form", "warn");
			}else{
				showMessage("Error occurred while creating master", "error");
			}
			$.unblockUI();
	   	},
    });
   
}

function resetObjects(){
    gridDetails = new Array();
    formDetails = new Array();
    menuDetails = new Object();
    dynamicFormModuleDetails = new Object();
    resourceKeyMap = new Map();
    $("#menuDisplayName").val("");
	$("#parentModuleName").val("");
	$("#moduleURL").val("");
	$("#moduleName").val("");
	$("#formDisplayName").val("");
    $("#formModuleURL").val("");
    $('#chkSelectAllIncludedListing').prop("checked", false);
    $('#chkSelectAllIncludedForm').prop("checked", false);
    $('#chkSelectAllHiddenListing').prop("checked", false);
    $('#chkSelectAllHiddenForm').prop("checked", false);
}

function createTable(columns) {
    $(".details").remove();
    
    
    for(let iCounter = 0; iCounter < columns.length; ++iCounter) {
        let trElement = null;
        if(columns[iCounter]['_unsupported']){
        	trElement = $("<tr class='details' style='border: 1px solid red;' ></tr>");
        	$(trElement).append("<td>&nbsp;</td>");
        	$(trElement).append("<td>&nbsp;</td>");
        }else{
        	trElement =$("<tr class='details' ></tr>");
        	$(trElement).append("<td><input class='grid_enable' id='tenabled_"+iCounter+"' type='checkbox' onchange='addRemoveToGridDetails(this)'></td>");
            $(trElement).append("<td><input class='grid_visible' id='thidden_"+iCounter+"' type='checkbox' disabled onchange='updateGridDetails(this)'></td>"); 	
        }
        let dataType = columns[iCounter]['columnType']
        let displayName = capitalizeFirstLetter(columns[iCounter]['tableColumnName'].replaceAll("_", " "));
        let nameCol = "<td>";
        if(columns[iCounter]['_unsupported']){
        	nameCol += '<i class="fa fa-exclamation-triangle _unsupported" aria-hidden="true" style="color:red" title="Unsupported Datatype : '+columns[iCounter]['dataType']+'"></i>&nbsp;';
        }
        if(columns[iCounter]['columnKey']=='PK'){
        	nameCol+= ' <i class="fa fa-key" aria-hidden="true" style="filter: drop-shadow(1px 1px 1px black); color:yellow" title="Primary Key';
        	if(columns[iCounter]['autoIncrement'] === true){
        		nameCol += ', Auto Generated';       		
        	}
        	nameCol += '"></i> ';
        }else if(columns[iCounter]['isMandatory']==true){
        	nameCol += '<span class="fromMandatory" title="Mandatory" style="font-size: 16px;color: red;filter: drop-shadow(0px 0px 1px black);"> &#9733; </span>';
        }
        nameCol += "<label id='tcolumn_"+iCounter+"'>" + columns[iCounter]['tableColumnName']+"</label></td>";
        $(trElement).append(nameCol);
        
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"_i18n' disabled type='text' data-previous-key='' value='' onchange='updateGridDetailsI18nResourceKey(this.id)' placeholder='Resource Key'></td>");
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"' disabled type='text' onchange='updateGridDetailsDisplayName(this.id)' value='"+displayName+"'></td>");
        $(trElement).append("<input id='tdisplay_"+iCounter+"_hd' type='hidden' value='"+displayName+"'>");
        $(trElement).append("<input id='tdatatype_"+iCounter+"_datatype' type='hidden' name= 'datatype' value='"+dataType+"'>");

        $("#listingDetailsTable").append(trElement);
    }

    for(let iCounter = 0; iCounter < columns.length; ++iCounter) {
    	 let trElement = null;
         if(columns[iCounter]['_unsupported']){
         	trElement =$("<tr class='details' style='border: 1px solid red;' ></tr>");
         	$(trElement).append("<td>&nbsp;</td>");
        	$(trElement).append("<td>&nbsp;</td>");
         }else{
         	trElement =$("<tr class='details' ></tr>");
         	$(trElement).append("<td><input class='form_enable' id='tfenabled_"+iCounter+"' type='checkbox' onchange='addRemoveToFormDetails(this)'></td>");
            $(trElement).append("<td><input class='form_visible' id='tfhidden_"+iCounter+"' type='checkbox' disabled onchange='updateFormDetails(this)'></td>");
         }
        let dataType = columns[iCounter]['columnType']
        let displayName = capitalizeFirstLetter(columns[iCounter]['tableColumnName'].replaceAll("_", " "));
        let nameCol = "<td>";
        if(columns[iCounter]['_unsupported']){
        	nameCol += '<i class="fa fa-exclamation-triangle _unsupported" aria-hidden="true" style="color:red" title="Unsupported Datatype : '+columns[iCounter]['dataType']+'"></i>&nbsp;';
        }
        
        if(columns[iCounter]['columnKey']=='PK'){
        	nameCol += ' <i class="fa fa-key" aria-hidden="true" style="filter: drop-shadow(1px 1px 1px black); color:yellow" title="Primary Key';
        	if(columns[iCounter]['autoIncrement'] === true){
        		nameCol += ', Auto Generated';       		
        	}
        	nameCol += '"></i> ';
        }else if(columns[iCounter]['isMandatory']==true){
        	nameCol += '<span class="fromMandatory" title="Mandatory" style="font-size: 16px;color: red;filter: drop-shadow(0px 0px 1px black);"> &#9733; </span>';
        }
        nameCol += "<label id='tfcolumn_"+iCounter+"'>" + columns[iCounter]['tableColumnName']+"</label></td>";
        $(trElement).append(nameCol);
        $(trElement).append("<td><input id='tfdisplay_"+iCounter+"_i18n' disabled type='text' data-previous-key='' value='' onchange='updateFormDetailsI18nResourceKey(this.id)' placeholder='Resource Key'></td>");
        $(trElement).append("<td><input id='tfdisplay_"+iCounter+"' disabled type='text' onchange='updateFormDetailsDisplayName(this.id)' value='"+displayName+"'></td>");
		$(trElement).append("<input id='tfdisplay_"+iCounter+"_hd' type='hidden' value='"+displayName+"'>");
		$(trElement).append("<input id='tdatatype_"+iCounter+"_datatype' type='hidden' name= 'datatype' value='"+dataType+"'>");
		
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
        details["column"] = $("#tcolumn_"+counter).val()== undefined ? '' : $("#tcolumn_"+counter).html().trim();
        details["i18nResourceKey"] = $("#tdisplay_"+counter+"_i18n").val()== undefined ? '' : $("#tdisplay_"+counter+"_i18n").val().trim();
        details["datatype"]=  $("#tdatatype_"+counter+"_datatype").val();
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
        details["column"] = $("#tfcolumn_"+counter).val() == undefined ? '' : $("#tfcolumn_"+counter).html().trim();
        details["i18nResourceKey"] = $("#tfdisplay_"+counter+"_i18n").val() == undefined ? '' :$("#tfdisplay_"+counter+"_i18n").val().trim();
        details["datatype"]=  $("#tdatatype_"+counter+"_datatype").val();
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
					showMessage("Error occurred while fetching internationalized text", "error");
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
	menuDetails["moduleUrl"]=$("#moduleURL").val();

	dynamicFormModuleDetails["moduleName"]=$("#formDisplayName").val();
	dynamicFormModuleDetails["moduleUrl"]=$("#formModuleURL").val();
	
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
        	dynamicFormModuleDetails: JSON.stringify(dynamicFormModuleDetails),
        	roleIds : JSON.stringify(roleIds),
        	        	dbProductName : $("#dataSource").find(":selected").data("product-name")
        },
        type: 'POST',
        success: function(data) {
        	
            showMessage("Master modules created successfully", "success");
            setTimeout(function(){
            	backToPreviousPage();
            }, 1500);
            
        },
		error : function(xhr, error){
			if(xhr.status == 406)
				showMessage("Database reference has unsupported datatype, recheck!", "warn");			
			else if(xhr.status == 409)
				showMessage("Modules with given data already exists! Re enter form", "warn");
			else
				showMessage("Error occurred while creating master", "error");
	   	},
				
    })
}

function validateForm(){
	
	$('#errorMessage').hide();
	
	if(primaryKeyCounter > 1){
		showMessage("Please select a table with a single primary key", "warn");
		return false;
	}
	
	if(primaryKey=== '' || primaryKey === undefined){
		showMessage("Please select a table with a primary key", "warn");
		return false;
	}
	
	if($("#tableAutocomplete").val().length < 1){
		showMessage("Please select a valid table to proceed", "warn");
		return false;
    }
	
	
	if($("#showInMenu").prop("checked")){
		if(validateSiteLayoutDetails() == false){
			return false;
		}
	}
		
	if($(".grid_enable:checked").length < 1){
		showMessage("Please mark at least one column as added in grid", "warn");
		return false;
	}
	
	if($(".grid_visible").length == $(".grid_visible:checked").length){
		showMessage("Please mark at least one column as visible in grid", "warn");
		return false;
	}
	
	if($(".form_enable:checked").length < 1){
		showMessage("Please mark at least one column as added in form", "warn");
		return false;
	}
	
	if($(".form_visible").length == $(".form_visible:checked").length){
		showMessage("Please mark at least one column as visible in form", "warn");
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
		 $("#menuDisplayName").focus();
	 	showMessage("Module Name and Module URL cannot be blank", "warn");
	 	return false;
	 }
	 let dfModuleName = $("#formDisplayName").val().trim();
	 let dfModuleUrl = $("#formModuleURL").val().trim();
	 if(dfModuleName === "" || dfModuleUrl === ""){
		 $("#formDisplayName").focus();
	 	showMessage("Form Module Name and Module URL cannot be blank", "want");
	 	return false;
	 }
	 
	 if(moduleName === dfModuleName) {
		 $("#menuDisplayName").focus();
		 showMessage("Listing and Form module name cannot be same.", "warn");
		 	return false;
	 }

	 if(moduleUrl === dfModuleUrl) {
		 $("#moduleURL").focus();
		 showMessage("Listing and Form URL name cannot be same.", "warn");
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
	 
	 if(isValid == false) {
		 return isValid;
	 }
	 
	
	 $.ajax({
        url: contextPath+ "/cf/ced",
        type: 'GET',
        async: false,
        headers: {
        	"module-name": dfModuleName,
        	"parent-module-id": $("#parentModuleName").val(),
        	"module-url": dfModuleUrl,
        	"module-id": uuidv4(),
        },
        success: function(data) {
        	if(data !== undefined && data !== ""){
	        	if(data.moduleIdName !== undefined && data.moduleIdName !== ""){
	        		isValid = false;
	        		showMessage("Module Name already exist for form", "error");
	        	}else if(data.moduleIdURL !== undefined &&  data.moduleIdURL !== ""){
	        		isValid = false;
	        		showMessage("Module URL already exist for form", "error");
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
		// let targetLookupId = $("#targetLookupType").find(":selected").val();
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

