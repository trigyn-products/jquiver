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
        $(trElement).append("<td><input id='tdisplay_"+iCounter+"' disabled type='text' onchange='updateGridDetails(this)' value='"+displayName+"'></td>");
        $("#listingDetailsTable").append(trElement);
    }

    for(let iCounter = 0; iCounter < columns.length; ++iCounter) {
        let trElement = $("<tr class='details'></tr>");
        $(trElement).append("<td><input id='fenabled_"+iCounter+"' type='checkbox' onchange='addRemoveToFormDetails(this)'></td>");
        $(trElement).append("<td><input id='fhidden_"+iCounter+"' type='checkbox' disabled onchange='updateFormDetails(this)'></td>");
        let displayName = capitalizeFirstLetter(columns[iCounter].replaceAll("_", " "));
        $(trElement).append("<td><label id='fcolumn_"+iCounter+"'>"+columns[iCounter]+"</label></td>");
        $(trElement).append("<td><input id='fdisplay_"+iCounter+"' disabled type='text' onchange='updateFormDetails(this)' value='"+displayName+"'></td>");
        $("#formDetailsTable").append(trElement);
    }
}

function addRemoveToGridDetails(element){
    const counter = element.id.split("_")[1];
    $("#thidden_"+counter).prop("disabled", !element.checked);
    $("#tdisplay_"+counter).prop("disabled", !element.checked);
    if(element.checked) {
        let details = new Object();
        details["index"] = counter;
        details["displayName"] = $("#tdisplay_"+counter).val();
        details["hidden"] = $("#thidden_"+counter).prop("checked");
        details["column"] = $("#tcolumn_"+counter).html().trim();
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
    if(element.checked) {
        let details = new Object();
        details["index"] = counter;
        details["displayName"] = $("#fdisplay_"+counter).val();
        details["hidden"] = $("#fhidden_"+counter).prop("checked");
        details["column"] = $("#fcolumn_"+counter).html().trim();
        formDetails.push(details);
    } else {
        removeByAttribute(formDetails, "index", counter);
    }
}

function updateFormDetails(element){

}

function createMaster() {
    let formData = $("#createMasterForm").serialize();
    $.ajax({
        url: contextPath+ "/cf/cm",
        data: {
        	formData: formData,
        	gridDetails: JSON.stringify(gridDetails),
        	formDetails: JSON.stringify(formDetails),
        	menuDetails: JSON.stringify(menuDetails),
        },
        type: 'POST',
        success: function(data) {
            showMessage("Information saved successfully", "success");
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