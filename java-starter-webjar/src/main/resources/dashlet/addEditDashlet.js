class AddEditDashlet {
    constructor(dashletPropertiesCount, componentArray, dashletSQLEditor, dashletHTMLEditor) {
		this.dashletPropertiesCount = dashletPropertiesCount;
		this.componentArray = componentArray;
		this.dashletSQLEditor = dashletSQLEditor;
		this.dashletHTMLEditor = dashletHTMLEditor;
    }
}

AddEditDashlet.prototype.fn = {
	
	loadAddEditDashletPage : function(){
		let isActive = $("#isActive").val();
		let showHeader = $("#showHeader").val();
		
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
    	require(["vs/editor/editor.main"], function() {
        dashletSQLEditor = monaco.editor.create(document.getElementById("sqlEditor"), {
		        	value: $("#sqlContent").val().trim(),
		            language: "sql",
		            roundedSelection: false,
					scrollBeyondLastLine: false,
					readOnly: false,
					theme: "vs-dark"
	        	});
	        	$("#sqlContent").remove();
    	});
    	
    	require(["vs/editor/editor.main"], function() {
        dashletHTMLEditor = monaco.editor.create(document.getElementById("htmlEditor"), {
		        	value: $("#htmlContent").val().trim(),
		            language: "html",
		            roundedSelection: false,
					scrollBeyondLastLine: false,
					readOnly: false,
					theme: "vs-dark"
	        	});
	        	$("#htmlContent").remove();
    	})
  		
		if (isActive === "1") {
			$("#isActiveCheckbox").prop("checked", true);
		} else if(isActive === "0"){
			$("#isActiveCheckbox").prop("checked", false);
		}
		if (showHeader === "1") {
			$("#showHeaderCheckbox").prop("checked", true);
		} else if(showHeader === "0"){
			$("#showHeaderCheckbox").prop("checked", false);
		}
		
		$(".value").each(function(){
			let value = $(this).closest("tr").find(".divValue").html().trim();
			$(this).val(value);
		});
	
	},
	
	saveDashlet : function() {
		let context = this;
		let isDataSaved = false;
 		let dashlet = new Object();
 		let dashletPropertVOList = new Array();
		let isValid = context.validateDashletMandatoryFields();
		if(isValid){
			$("#errorMessage").hide();
			$("#dashletProps").find('tr.dashlet_property').each (function() {
				let dashletProperty = new Object();
	  			$(this).find('td > input').each (function() {
	  				let fieldName = $(this).prop("name")
	  				if(fieldName == "toDisplay"){
	  					dashletProperty[fieldName] = $(this).prop("checked")?1:0;
	  				}else{
	    				dashletProperty[fieldName] = $(this).val();
	    			}
	    		});
	    		dashletPropertVOList.push(dashletProperty);
			});
			
			dashlet.dashletId = $("#dashletId").val();
			dashlet.dashletName = $("#dashletName").val();
			dashlet.dashletTitle = $("#dashletTitle").val();
			dashlet.xCoordinate = $("#xCoordinate").val();
			dashlet.yCoordinate = $("#yCoordinate").val();
			dashlet.width = $("#width").val();
			dashlet.height = $("#height").val();
			dashlet.contextId = $("#contextId").find(":selected").val();
			dashlet.dashletPropertVOList = dashletPropertVOList;

			if (jQuery("#isActiveCheckbox").prop("checked")) {
				dashlet.isActive = 1;
			} else {
	        	dashlet.isActive = 0;
			}
				
			if (jQuery("#showHeaderCheckbox").prop("checked")) {
				dashlet.showHeader = 1;
			} else {
				dashlet.showHeader = 0;
			}
				
			dashlet.dashletBody = dashletHTMLEditor.getValue();
			dashlet.dashletQuery = dashletSQLEditor.getValue();
				
	
			$.ajax({
				type : "POST",
				async : false,
				url :  contextPath+"/cf/sdl",
	        	contentType : "application/json",
	        	headers: {
	          		"user-id": "admin"
	        	},
				data : JSON.stringify(dashlet),
				success : function(data) {
					$("#dashletId").val(data);
					isDataSaved = true;
					showMessage("Information saved successfully", "success");
		       	},
	        
	        	error : function(xhr, error){
	        		showMessage("Error occurred while saving", "error");
	        	},
	        	
			});
		}else{
			$("#errorMessage").show();
			window.scrollTo(0, 0);
		}
		return isDataSaved;
	},

	validateDashletMandatoryFields : function(){
		let dashletName=$("#dashletName").val().trim();
		if(dashletName ==""){
			$("#dashletName").focus();
			$('#errorMessage').html("Please enter Dashlet name");
			return false;
		}
		
		let dashletTitle=$("#dashletTitle").val().trim();
		if(dashletTitle ==""){
			$("#dashletTitle").focus();
			$('#errorMessage').html("Please enter Dashlet title");
			return false;
		}
		
		let xCoordinate=$("#xCoordinate").val().trim();
		if(xCoordinate ==""){
			$("#xCoordinate").focus();
			$('#errorMessage').html("Please enter Dashlet X Coordinate");
			return false;
		}
		
		let yCoordinate=$("#yCoordinate").val().trim();
		if(yCoordinate ==""){
			$("#yCoordinate").focus();
			$('#errorMessage').html("Please enter Dashlet Y Coordinate");
			return false;
		}
		
		let widthDimension=$("#width").val().trim();
		if(widthDimension ==""){
			$("#width").focus();
			$('#errorMessage').html("Please enter Dashlet width  dimension");
			return false;
		}
		
		let heightDimension=$("#height").val().trim();
		if(heightDimension ==""){
			$("#height").focus();
			$('#errorMessage').html("Please enter Dashlet height dimension");
			return false;
		}
		
		let sqlScriptValidation=dashletSQLEditor.getValue().trim();
		if(sqlScriptValidation ==""){
			$("#sqlContainer").focus();
			$('#errorMessage').html("Please enter sql script");
			return false;
		}
		
		let dashletHTMLEditorValidation=dashletHTMLEditor.getValue().trim();
		if(dashletHTMLEditorValidation ==""){
			$("#htmlEditor").focus();	
			$('#errorMessage').html("Please enter html script");
			return false;
		}
		return true;
	},
	
 	addDashletProperty : function() {
 		let context = this;
 		let moverUpContext;
 		let moverDownContext;
 		let deletePropertyContext;
 		let actionColumn;
 		let propertyRow;
 		let propertyDetails;
 		
        dashletPropertiesCount = dashletPropertiesCount + 1;
       
        var lengthOfTr = jQuery("#dashletProps tbody>tr").length;
        propertyRow = $('<tr class=dashlet_property></tr>');
		propertyDetails = $('<td id=propertyDetails></td>');
		propertyDetails.append('<input type="hidden" name="propertyId" id="'+dashletPropertiesCount+'" class="form-control" />');
        propertyDetails.append('<input type="text" name="placeholderName" id="placeholderName_'+dashletPropertiesCount+'" class="form-control" />');
        propertyDetails.append('<input type="hidden" name="sequence" id="sequence_'+dashletPropertiesCount+'" value="'+(lengthOfTr+1)+'" />');
        propertyRow.append(propertyDetails);
        propertyRow.append('<td><input type="text" name="displayName" id="displayName_'+dashletPropertiesCount+'" class="form-control"></td>');
        propertyRow.append(context.getTypeDropdown());
        propertyRow.append('<td><input type="text" name="value" id="value_'+dashletPropertiesCount+'" class="form-control" ></td>');
        propertyRow.append('<td><input type="text" name="defaultValue" id="defaultValue_'+dashletPropertiesCount+'" class="form-control"></td>');
        propertyRow.append('<td><input type="checkbox" name="toDisplay" id="toDisplay_'+dashletPropertiesCount+'" class="form-control" value="1" checked/></td>');

        
        if(dashletPropertiesCount === 1){
        	moverUpContext = $('<span id="upArrow_'+dashletPropertiesCount+'"  class="tblicon pull-left disable_cls"><i class="fa fa-arrow-up"></i></span>');
        	moverDownContext = $('<span id="downArrow_'+dashletPropertiesCount+'"  class="tblicon pull-left disable_cls"><i class="fa fa-arrow-down"></i></span>');
        			  
		}else{
			moverUpContext = $('<span id="upArrow_'+dashletPropertiesCount+'" class="tblicon pull-left"><i class="fa fa-arrow-up"></i></span>');
        	$("#dashletProps tr:last td:last").find("span[id*='down']").removeClass("disable_cls");
		} 
		moverDownContext = $('<span id="downArrow_'+dashletPropertiesCount+'"  class="tblicon pull-left disable_cls"><i class="fa fa-arrow-down"></i></span>');
		deletePropertyContext = $('<span id="removeProperty_'+dashletPropertiesCount+'" class="tblicon pull-left"><i class="fa fa-trash-o"></i></span>');
		moverUpContext.click(function(){
			let objectId = moverUpContext[0].id;
			context.moveUpDown(objectId);
		});
		
		moverDownContext.click(function(){
			let objectId = moverDownContext[0].id;
			context.moveUpDown(objectId);
		});
		
		deletePropertyContext.click(function(){
			let objectId = deletePropertyContext[0].id;
			context.deleteProperty(objectId);
		});
		actionColumn = $('<td></td>');
		actionColumn.append(moverUpContext);
		actionColumn.append(moverDownContext);
		actionColumn.append(deletePropertyContext);
		propertyRow.append(actionColumn);
        
        $("#dashletProps").append(propertyRow);
    },

 
	getTypeDropdown : function(){
		let context = this;
        let catgoryLength = componentArray.length;
        let propertyColumn = $('<td></td>');
        let selectElement = $('<select id="componentType_'+dashletPropertiesCount+'" class="form-control" name="type"></select>');

        for(let categoryCounter = 0; categoryCounter < catgoryLength; categoryCounter++){
        	let optionElement;
            optionElement = $('<option value="'+componentArray[categoryCounter].categoryId+'">');
            optionElement.append(componentArray[categoryCounter].categoryDescription+'</option>');
            selectElement.append(optionElement);        
        }
        
        selectElement.change(function(){
			let objectId = selectElement[0].id;
			context.defaultValueChange(objectId);
		});
        
        propertyColumn.append(selectElement);
        return propertyColumn;
    },
    
	defaultValueChange : function(selectElementId){
		let selectedPropertyCount = selectElementId.split("_")[1];
		let type = $('#'+selectElementId).find(":selected").text().trim();
		let valueText = $('#value_'+selectedPropertyCount);
		let defaultValue = $('#defaultValue_'+selectedPropertyCount);
		$(defaultValue).val("");
		if(type === "select" || type === "rangeslider" || type === "decimal" || type === "number" || type === "text"){
			valueText.removeAttr("disabled");
			defaultValue.removeAttr("keypress");
		}else{
			valueText.val("");
			valueText.attr("disabled","disabled");
		}
		if(type === "datepicker"){
			defaultValue.val("");
			defaultValue.attr("disabled","disabled");
			defaultValue.removeAttr("keypress");
		}else{
			defaultValue.removeAttr("disabled");
		}
		if(type === "number"){
			$(defaultValue).keypress(function (e) {
			     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
			        return false;
			    }
			});
		}else{
			$(defaultValue).keypress(function (e) {
				$(defaultValue).unbind("keypress");
			});
		}
	},
	
	moveUpDown : function(currentObjectId){
		let currentObjectName = currentObjectId.split("_")[0];
		let sourceTr = $('#'+currentObjectId).closest('tr');
		let sourcePropertyId = $(sourceTr).find("input[name=propertyId]").prop("id");
		let targetTr;
		
		if(currentObjectName === "upArrow"){
			targetTr = $('#'+currentObjectId).closest('tr').prev();
			let targetPropertyId = $(targetTr).find("input[name=propertyId]").prop("id");
			targetTr.insertAfter(sourceTr);
			if($(sourceTr).is(":first-child")){
				$("#upArrow_"+sourcePropertyId).addClass("disable_cls");
				$("#upArrow_"+targetPropertyId).removeClass("disable_cls");
			}

			if($(targetTr).is(":last-child")){
				$("#downArrow_"+targetPropertyId).addClass("disable_cls");
				$("#downArrow_"+sourcePropertyId).removeClass("disable_cls");
			}
		}
	
		if(currentObjectName === "downArrow"){
			targetTr = $('#'+currentObjectId).closest('tr').next();
			let targetPropertyId = $(targetTr).find("input[name=propertyId]").prop("id");
			sourceTr.insertAfter(targetTr);
			if($(sourceTr).is(":last-child")){
				$("#downArrow_"+sourcePropertyId).addClass("disable_cls");
				$("#downArrow_"+targetPropertyId).removeClass("disable_cls");
			}

			if($(targetTr).is(":first-child")){
				$("#upArrow_"+sourcePropertyId).removeClass("disable_cls");
				$("#upArrow_"+targetPropertyId).addClass("disable_cls");
			}
		}

		
		$(sourceTr).find("input[name=sequence]").val(sourceTr.index() + 1)
		$(targetTr).find("input[name=sequence]").val(targetTr.index() + 1)
	},
	
	
	deleteProperty : function(currentObjectId){
		let context = this;
		let selectedPropName = $("#displayName_"+currentObjectId.split("_")[1]).val();
		$("#deletePropertyConfirm").html("Are you sure you want to delete " + selectedPropName + "?");
		$("#deletePropertyConfirm").dialog({
		bgiframe		 : true,
		autoOpen		 : true, 
		modal		 : true,
		closeOnEscape : true,
		draggable	 : true,
		resizable	 : false,
		title		 : "Delete",
		buttons		 : [{
				text		:"Cancel",
				click	: function() { 
					$(this).dialog('close');
				},
			},
			{
				text		: "Delete",
				click	: function(){
					$(this).dialog('close');
					context.removeProperty(currentObjectId);
				}
           	},
       ],	
		open		: function( event, ui ) {
			 $('.ui-dialog-titlebar')
		   	    .find('button').removeClass('ui-dialog-titlebar-close').addClass('ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close')
		       .prepend('<span class="ui-button-icon ui-icon ui-icon-closethick"></span>').append('<span class="ui-button-icon-space"></span>');
       		}	
	   });
	
	},
	
	removeProperty : function(currentObjectId){
		let context = this;
		dashletPropertiesCount = dashletPropertiesCount - 1;
		let currentObjectTr = $('#'+currentObjectId).closest('tr');
		
		$.each( currentObjectTr.nextAll().find('td:first input:last'), function( index, inputElement ) {
			let sequence = inputElement.value;
			inputElement.value = sequence-1;
		});
		currentObjectTr.remove();
		
		$("#dashletProps").find("tbody tr:first span[id^=upArrow]").addClass("disable_cls");
		$("#dashletProps").find("tbody tr:last span[id^=downArrow]").addClass("disable_cls");
	},
	
	backToDashletListing : function() {
		location.href = contextPath+"/cf/dlm";
	},

}
