function getAllDatasource(isEdit){
    $.ajax({
	    type : "POST",
        async: false,
	    url : contextPath+"/api/jq-all-datasource",
        success : function(data) {
          	data.forEach(function(dataSourceObj) {
	          	if(dataSourceObj.selectedDataSource !== null && isEdit === 0){
	          		$("#dataSource").append("<option selected value="+dataSourceObj.additionalDatasourceId+" data-product-name="+dataSourceObj.databaseProductName+">"+dataSourceObj.datasourceName+"</option>")
	          	}else{
	          		$("#dataSource").append("<option value="+dataSourceObj.additionalDatasourceId+" data-product-name="+dataSourceObj.databaseProductName+">"+dataSourceObj.datasourceName+"</option>")
	          	}
	        });
           	if($("#dataSourceId") != undefined && $("#dataSourceId").val() != undefined && $("#dataSourceId").val().trim() !== ""){ 
           		$("#dataSource").val($("#dataSourceId").val());
           	}
	    },
        error : function(xhr, error){
	        showMessage("Error occurred while fetching list of active database drivers", "error");
        },
	});
}

function retrieveDatasource() {
	let datasource;
	$.ajax({
	    type : "POST",
        async: false,
	    url : contextPath+"/api/jq-all-datasource",
        success : function(data) {
        	datasource = data;
	    },
        error : function(xhr, error){
	        showMessage("Error occurred while fetching list of active database drivers", "error");
        },
	});
	return datasource;
}
    
function showHideTableAutocomplete(){ 
   	let selectedDataSource = $("#dataSource").find(":selected").text();
   	if(selectedDataSource === "Default Connection"){ 
   		$("#tableAutocompleteDiv").show();
   	}else{
   		$("#tableAutocompleteDiv").hide();
   	}
}

function updateDataSource(){

	tableAutocomplete.resetAutocomplete();
	tableAutocomplete.options.requestParameters.dbProductName = $("#dataSource").find(":selected").data("product-name");
	$.ajax({
	    type : "POST",
        async: false,
	    url : contextPath+"/cf/uad",
	    data: {
	    	autocompleteId: "table-autocomplete",
	    	additionalDataSourceId: $("#dataSource").find(":selected").val(),
	    },
        success : function(data) {
			
	    },
        error : function(xhr, error){
	        showMessage("Error occurred while updating datasource", "error");
        },
	});
}


function loadDefaultTab(templateName, callbackFun){
    $('.ui-tabs-nav li a').click(function(){
    
    	let previewTabName;
		let selectedTab = $(this).attr("data-target");
		if(selectedTab === "htmlContent"){
			previewTabName = "htmlPreview";
			$("#jsContent").hide();
			$("#htmlContent").show();
		}else{
			previewTabName = "jsPreview";
			$("#htmlContent").hide();
			$("#jsContent").show();
		}
		
		if ($("#"+previewTabName).children().length == 0 ) {
			$.ajax({
			    type : "POST",
		        async: false,
			    url : contextPath+"/cf/gadc",
			    data:{
			    	templateName: templateName,
			    	selectedTab: selectedTab
			    },
		        success : function(data) {
		          	displayContent(data, selectedTab, callbackFun);
			    },
		        error : function(xhr, error){
			        showMessage("Error occurred while fetching default content", "error");
		        },
			});
		 }  
	});
   	
}

function displayContent(data, selectedTab, callbackFun){
	let simplemde = new SimpleMDE({
		element: document.getElementById("previewContent"),
	    initialValue : data,
		renderingConfig: {
	   		codeSyntaxHighlighting: true,
		}
	});
	$("#previewContent").css('display', 'none');
	if(selectedTab == "htmlContent"){
		$('#htmlPreview').html("");
		$('#htmlPreview').wrapInner(simplemde.options.previewRender(simplemde.value()));
		$("#htmlPreview").scrollTop(0);
	}else{
		$('#jsPreview').html("");
		$('#jsPreview').wrapInner(simplemde.options.previewRender(simplemde.value()));
		$("#jsPreview").scrollTop(0);
		callbackFun();
	} 
}


function openDeletConfirmation(gridId, entityId, moduleType,templateName,updatedDate){
	$("#deleteHeader").html("Are you sure you want to delete?");
	$("#deleteHeader").dialog({
	bgiframe	: true,
	autoOpen	: true, 
	modal		: true,
	closeOnEscape : true,
	draggable	: true,
	resizable	: false,
	title		: "Delete",
	position: {
		my: "center", at: "center"
	},
	buttons : [{
			text		:"Cancel",
			click	: function() { 
				$(this).dialog('close');
			},
		},
		{
			text		: "Delete",
			click	: function(){
				$(this).dialog('close');
				deleteEntity(gridId, entityId, moduleType,templateName,updatedDate);
			}
       	},
   ],	
   open: function( event, ui ) {
		 $('.ui-dialog-titlebar')
	   	    .find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
	       .prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
   		}	
   });
	
}

function deleteEntity(gridId, entityId, moduleType,templateName,updatedDate) {
	
//	entityName = entityName.replace(new RegExp('<', 'g'), "&lt;");
//	entityName = entityName.replace(new RegExp('>', 'g'), "&gt;");
	$.ajax({
        url: contextPath + "/api/delete-entity",
        type: "POST",
        data:{
        	entityId	: entityId,
        	moduleType 	: moduleType,
        	templateName : templateName,
        	updatedDate : updatedDate,
        	
        	
        	},  
        async:true,
        success: function(data){
        	if(data == 200) {
                $( "#"+gridId).pqGrid( "refreshDataAndView" ); 
                showMessage("Entity deleted successfully.", "info");
        	} else if(data == 403) {
        		showMessage("You don't have privilege to delete the entity.", "error");
        	}
        },
        error: function(jqXHR, exception){
            showMessage("Error occurred while deleting "+ entityId, "error");
        }   
    });
}


function getAllDatasourceForContainer(isEdit, selectedId){			
    $.ajax({
	    type : "POST",
        async: false,
	    url : contextPath+"/api/jq-all-datasource",
        success : function(data) {
          	data.forEach(function(dataSourceObj) {
	          	if(dataSourceObj.selectedDataSource !== null && isEdit === 0){
	          		$("#"+selectedId).append("<option selected value="+dataSourceObj.additionalDatasourceId+" data-product-name="+dataSourceObj.databaseProductName+">"+dataSourceObj.datasourceName+"</option>")
	          	}else{
	          		$("#"+selectedId).append("<option value="+dataSourceObj.additionalDatasourceId+" data-product-name="+dataSourceObj.databaseProductName+">"+dataSourceObj.datasourceName+"</option>")
	          	}
	        });
           	if($("#"+selectedId) != undefined && $("#"+selectedId).val() != undefined && $("#"+selectedId).val().trim() !== ""){ 
           		$("#"+selectedId).val($("#"+selectedId).val());
           	}
	    },
        error : function(xhr, error){
	        showMessage("Error occurred while fetching list of active database drivers", "error");
        },
	});
}