

function backToPreviousPage() {
	location.href = contextPath+"/cf/home";
}

function populateFields(element){
    let selectedTable = element.value;
    $.ajax({
        url  : contextPath + "/cf/mtd",
        type : 'GET',
        data : {tableName: selectedTable},
        success : function(data) {
            $("#moduleName").val(selectedTable.replaceAll("_", "-"));
            let primaryKey = data.filter(element => element.columnKey == "PRI").map(element => element["columnName"]).toString();
            let columns = data.map(element => element["columnName"]);
            $("#columns").val(columns.toString());
            $("#primaryKey").val(primaryKey);
            createTable(columns);
        }
    });
}

function createTable(columns) {
    $(".details").remove();
    for(let iCounter = 0; iCounter < columns.length; ++iCounter) {
        let trElement = $("<tr class='details'></tr>");
        $(trElement).append("<td><input id='tenabled_"+iCounter+"' type='checkbox' onchange='addRemoveToGridDetails(this)'></td>");
        $(trElement).append("<td><input id='thidden_"+iCounter+"' type='checkbox' disabled onchange='updateGridDetails(this)'></td>");
        let displayName = capitalizeFirstLetter(columns[iCounter].replaceAll("_", " "));
        $(trElement).append("<td><label id='tcolumn_"+iCounter+"'>"+columns[iCounter]+"</label></td>");
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"' disabled type='text' onchange='updateGridDetailsDisplayName(this)' value='"+displayName+"'></td>");
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"_i18n' disabled type='text' onchange='updateGridDetailsI18nResourceKey(this)' placeholder='I18N Resource Key'></td>");

        $("#listingDetailsTable").append(trElement);
    }

    for(let iCounter = 0; iCounter < columns.length; ++iCounter) {
        let trElement = $("<tr class='details'></tr>");
        $(trElement).append("<td><input id='fenabled_"+iCounter+"' type='checkbox' onchange='addRemoveToFormDetails(this)'></td>");
        $(trElement).append("<td><input id='fhidden_"+iCounter+"' type='checkbox' disabled onchange='updateFormDetails(this)'></td>");
        let displayName = capitalizeFirstLetter(columns[iCounter].replaceAll("_", " "));
        $(trElement).append("<td><label id='fcolumn_"+iCounter+"'>"+columns[iCounter]+"</label></td>");
        $(trElement).append("<td><input id='fdisplay_"+iCounter+"' disabled type='text' onchange='updateFormDetailsDisplayName(this)' value='"+displayName+"'></td>");
        $(trElement).append("<td><input id='fdisplay_"+iCounter+"_i18n' disabled type='text' onchange='updateFormDetailsI18nResourceKey(this)' placeholder='I18N Resource Key'></td>");

        $("#formDetailsTable").append(trElement);
    }
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

function updateFormDetailsI18nResourceKey(element){
	const counter = element.id.split("_")[1];
	formDetails[counter].i18nResourceKey=element.value;
}

function updateFormDetailsDisplayName(element){
	const counter = element.id.split("_")[1];
	formDetails[counter].displayName=element.value;
}

function updateGridDetailsI18nResourceKey(element){
	const counter = element.id.split("_")[1];
	gridDetails[counter].i18nResourceKey=element.value;
}

function updateGridDetailsDisplayName(element){
	const counter = element.id.split("_")[1];
	gridDetails[counter].displayName=element.value;
}

function createMaster() {
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
			$("#isMenuAddActive").val(1);
			$("#parentModuleName").val("");
			$("#parentModuleName").prop('disabled',true);
			$("#menuDisplayName").prop('disabled',true);
			$("#moduleURL").prop('disabled',true);
		}
		else{
			$("#isMenuAddActive").val(0);
			$("#parentModuleName").val("");
			$("#parentModuleName").prop('disabled',false);
			$("#menuDisplayName").prop('disabled',false);
			$("#moduleURL").prop('disabled',false);
			
   		}
		
	}