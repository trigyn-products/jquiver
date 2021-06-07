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