class AddEditDashlet {
    constructor(dashletPropertiesCount, componentArray, dashletSQLEditor, dashletHTMLEditor) {
		this.dashletPropertiesCount = dashletPropertiesCount;
		this.componentArray = componentArray;
		this.dashletSQLEditor = dashletSQLEditor;
		this.dashletHTMLEditor = dashletHTMLEditor;
    }
}

AddEditDashlet.prototype.fn = {
	
	loadAddEditDashletPage : function(variableName, datasourceId, queryType){
		let context = this;
		let isActive = $("#isActive").val();
		let showHeader = $("#showHeader").val();
		
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120});
    	require(["vs/editor/editor.main"], function() {

        dashletSQLEditor = monaco.editor.create(document.getElementById("sqlEditor"), {
		        	value: $("#sqlContent").val().trim(),
		            language: "sql",
		            roundedSelection: false,
					scrollBeyondLastLine: false,
					readOnly: false,
					theme: "vs-dark"
	        	});
	        	dashletSQLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
			    	typeOfAction('dashlet-manage-details',  $("#savedAction").find("button"), 
			    		addEditDashletFn.saveDashlet.bind(addEditDashletFn),addEditDashletFn.backToDashletListing);
				});
	        	dashletSQLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M,function() {
	                resizeMonacoEditor(dashletSQLEditor,"sqlContainer", "sqlEditor");
	            });
		    	dashletSQLEditor.onDidChangeModelContent( function (){
		    		$('#errorMessage').hide();
				});
	        	$("#sqlContent").remove();
	        	

	    		let index = dashletSQLEditor.length;
				
				let datasource = retrieveDatasource();
		    	let inputElement = "<div class='col-3'><label for='inputcontainer_0"+"' style='white-space:nowrap'> Variable Name </label><input id='inputcontainer_0"+"' type ='text' class='form-control' /></div>";
		    	let selectElement = "<div class='col-3'><label for='selectcontainer_0"+"' style='white-space:nowrap'>Query Type </label><select id='selectcontainer_0"+"' class='form-control' onchange='addEdit.updateDatasourceState(this);'><option value='1'>Select Query</option><option value='2'>Javascript</option></select></div>";
		    	let datasourceEditor = "<div class='col-3'><div><label for='datasourcecontainer_0"+"' style='white-space:nowrap'>Datasource </label><select id='datasourcecontainer_0"+"' name='dataSourceId' class='form-control' onchange='showHideTableAutocomplete()'><option id='defaultConnection' value=''>Default Connection</option>";
		    	
		    	if(datasource != null || datasource != undefined || datasource != "") {
		    		datasource.forEach(function(dataSourceObj) {
		    			datasourceEditor +="<option value="+dataSourceObj.additionalDatasourceId+" data-product-name="+dataSourceObj.databaseProductName+">"+dataSourceObj.datasourceName+"</option>";
			        });
		    	}
		    	datasourceEditor +='</select></div></div>'
		    	
	    		let daoContainer = $("<div id='daoContainerDiv_0"+"' class='margin-t-25'><div class='row'>"+ inputElement +""+ selectElement +""+datasourceEditor +"</div></div>");
		    	
		    	daoContainer.insertBefore($("#sqlContainer"));

				if(variableName){
					$("#inputcontainer_0").val(variableName);
				}

				if(datasourceId != undefined && datasourceId != "" ){
		    		$("#datasourcecontainer_0").val(datasourceId);
		    	}
				
				if(queryType != undefined){
		    		$("#selectcontainer_0").val(queryType);
		    		if(queryType == 4) {
						$("#datasourcecontainer_0").closest('div').hide();
		    		}
		    	}
		    	

    	});

    	require(["vs/editor/editor.main"], function() {
        dashletHTMLEditor = monaco.editor.create(document.getElementById("htmlEditor"), {
		        	value: $("#htmlContent").val().trim(),
		            language: "html",
		            roundedSelection: false,
					scrollBeyondLastLine: false,
					readOnly: false,
					theme: "vs-dark",
					"autoIndent": true
	        	});
	        	dashletHTMLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
			    	typeOfAction('dashlet-manage-details',  $("#savedAction").find("button"), 
			    		addEditDashletFn.saveDashlet.bind(addEditDashletFn),addEditDashletFn.backToDashletListing);
				});
	        	dashletHTMLEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M,function() {
	                resizeMonacoEditor(dashletHTMLEditor,"htmlContainer", "htmlEditor");
	            });
		    	dashletHTMLEditor.onDidChangeModelContent( function (){
		    		$('#errorMessage').hide();
				});
	        	$("#htmlContent").remove();
    	});

  		
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
		let sequenceCounter = 0;
		if(isValid){
			$("#errorMessage").hide();
			$("#dashletProps").find('tr.dashlet_property').each (function() {
				let dashletProperty = new Object();
	  			$(this).find('td > input').each (function() {
	  				let fieldName = $(this).prop("name")
	  				if(fieldName == "toDisplay"){
	  					dashletProperty[fieldName] = $(this).prop("checked")?1:0;
	  				}else if(fieldName == "sequence"){
	    				dashletProperty[fieldName] = sequenceCounter++;
	    			}else{
	    				dashletProperty[fieldName] = $(this).val();
	    			}
	    		});
	    		dashletProperty["type"] =  $(this).find('td > select').find(":selected").val();
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
			dashlet.resultVariableName = $('#inputcontainer_0').val();
			dashlet.daoQueryType = $('#selectcontainer_0').val();
			
			if($('#datasourcecontainer_0').val() != "") {
				dashlet.dataSourceId = $('#datasourcecontainer_0').val();
			}

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
				data : JSON.stringify(dashlet),
				success : function(data) {
					$("#dashletId").val(data);
					isDataSaved = true;
					context.saveEntityRoleAssociation(data);
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
		
    	if($("#inputcontainer_0") .val()=== "") {
			showMessage("Variable name can not be blank", "error");
    		return false;
		}

		let dashletSQLEditorValidation=dashletSQLEditor.getValue().trim();
		if(dashletSQLEditorValidation ==""){
			$("#htmlEditor").focus();	
			$('#errorMessage').html("Please enter SQL/JS script");
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
 		let propertyMasterId = uuidv4();
 		
        dashletPropertiesCount = dashletPropertiesCount + 1;
       
        var lengthOfTr = jQuery("#dashletProps tbody>tr").length;
        propertyRow = $('<tr class=dashlet_property></tr>');
		propertyDetails = $('<td id=propertyDetails></td>');
		propertyDetails.append('<input type="hidden" name="propertyId" id="'+dashletPropertiesCount+'" value="'+propertyMasterId+'"  />');
        propertyDetails.append('<input type="text" name="placeholderName" id="placeholderName_'+dashletPropertiesCount+'" class="form-control" />');
        propertyDetails.append('<input type="hidden" name="isDeleted" id="deleted_'+dashletPropertiesCount+'" value="0" />');
        propertyDetails.append('<input type="hidden" name="sequence" id="sequence_'+dashletPropertiesCount+'" value="'+(lengthOfTr+1)+'" />');
        propertyRow.append(propertyDetails);
        propertyRow.append('<td><input type="text" name="displayName" id="displayName_'+dashletPropertiesCount+'" class="form-control"></td>');
        propertyRow.append(context.getTypeDropdown());
        propertyRow.append('<td><input type="text" name="value" id="value_'+dashletPropertiesCount+'" class="form-control" ></td>');
        propertyRow.append('<td><input type="text" name="defaultValue" id="defaultValue_'+dashletPropertiesCount+'" class="form-control"></td>');
        propertyRow.append('<td><input type="checkbox" name="toDisplay" id="toDisplay_'+dashletPropertiesCount+'" class="form-control" value="1" checked/></td>');

        
        if(dashletPropertiesCount === 1){
        	moverUpContext = $('<span id="upArrow_'+dashletPropertiesCount+'"  class="tblicon pull-left disable_cls"><i class="fa fa-chevron-up"></i></span>');
        	moverDownContext = $('<span id="downArrow_'+dashletPropertiesCount+'"  class="tblicon pull-left disable_cls"><i class="fa fa-chevron-down"></i></span>');
        			  
		}else{
			moverUpContext = $('<span id="upArrow_'+dashletPropertiesCount+'" class="tblicon pull-left"><i class="fa fa-chevron-up"></i></span>');
        	$("#dashletProps tr:last td:last").find("span[id*='down']").removeClass("disable_cls");
		} 
		moverDownContext = $('<span id="downArrow_'+dashletPropertiesCount+'"  class="tblicon pull-left disable_cls"><i class="fa fa-chevron-down"></i></span>');
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
		let context = this;
		let selectedPropertyCount = selectElementId.split("_")[1];
		context.disableConfigProperties(selectedPropertyCount, true);
	},
	
	moveUpDown : function(currentObjectId){
		let currentObjectName = currentObjectId.split("_")[0];
		let sourceTr = $('#'+currentObjectId).closest('tr');
		let sourceSequence = $(sourceTr).find("td:last > span[id^=upArrow_]").attr("id").split("_")[1];
		let targetTr;
		
		if(currentObjectName === "upArrow"){
			targetTr = $('#'+currentObjectId).closest('tr').prev();
			let targetSequence = $(targetTr).find("td:last > span[id^=upArrow_]").attr("id").split("_")[1];
			targetTr.insertAfter(sourceTr);
			if($(sourceTr).is(":first-child")){
				$("#upArrow_"+sourceSequence).addClass("disable_cls");
				$("#upArrow_"+targetSequence).removeClass("disable_cls");
			}

			if($(targetTr).is(":last-child")){
				$("#downArrow_"+targetSequence).addClass("disable_cls");
				$("#downArrow_"+sourceSequence).removeClass("disable_cls");
			}
		}
	
		if(currentObjectName === "downArrow"){
			targetTr = $('#'+currentObjectId).closest('tr').next();
			let targetSequence = $(targetTr).find("td:last > span[id^=upArrow_]").attr("id").split("_")[1];
			sourceTr.insertAfter(targetTr);
			if($(sourceTr).is(":last-child")){
				$("#downArrow_"+sourceSequence).addClass("disable_cls");
				$("#downArrow_"+targetSequence).removeClass("disable_cls");
			}

			if($(targetTr).is(":first-child")){
				$("#upArrow_"+sourceSequence).removeClass("disable_cls");
				$("#upArrow_"+targetSequence).addClass("disable_cls");
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
		position: {
		 my: "center", at: "center", of: "#dashletProps"
		},
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
		let currentObjectTr = $('#'+currentObjectId).closest('tr');
		let deletedSequence = $(currentObjectTr).find("td [id^=sequence]").val();
		
		currentObjectTr.hide();
		$("#deleted_"+deletedSequence).val("1");
		$(currentObjectTr).find("td [id^=sequence]").val("-1");
		
		$("#dashletProps").find("tbody tr:visible:first span[id^=upArrow]").addClass("disable_cls");
		$("#dashletProps").find("tbody tr:visible:last span[id^=downArrow]").addClass("disable_cls");
	},
	
	disableInputFields : function(){
		let context = this;
		$("#dashletProps > tbody > tr").each( function(index , trElement){
    		let configurationId = $(this).find("select").prop("id").split("_")[1];
    		context.disableConfigProperties(configurationId);
		})
	},
	
    disableConfigProperties : function(selectedPropertyCount, isAddEdit){
    	let type = $('#componentType_'+selectedPropertyCount).find(":selected").text().trim();
		let valueText = $('#value_'+selectedPropertyCount);
		let defaultValue = $('#defaultValue_'+selectedPropertyCount);
		if(isAddEdit === true){
			$(defaultValue).val("");
		}
		
		if(type !== "datepicker"){
			valueText.removeAttr("disabled");
			defaultValue.removeAttr("keypress");
			defaultValue.removeAttr("disabled");
		}else{
			valueText.val("");
			valueText.attr("disabled","disabled");
			defaultValue.val("");
			defaultValue.attr("disabled","disabled");
			defaultValue.removeAttr("keypress");
		}
		
		if(type === "number"){
			$(defaultValue).keypress(function (event) {
			     if (event.which != 8 && event.which != 0 && (event.which < 48 || event.which > 57)) {
			        return false;
			    }
			});
		}else{
			$(defaultValue).keypress(function (event) {
				$(defaultValue).unbind("keypress");
			});
		}
    },
    
    saveEntityRoleAssociation : function(dashletId){
		let roleIds =[];
		let entityRoles = new Object();
		entityRoles.entityName = $("#dashletName").val().trim();
		entityRoles.moduleId=$("#moduleId").val();
		entityRoles.entityId= dashletId;
		 $.each($("#rolesMultiselect_selectedOptions_ul span.ml-selected-item"), function(key,val){
			 roleIds.push(val.id);
	     	
	     });
		
		entityRoles.roleIds=roleIds;
		
		$.ajax({
	        async : false,
	        type : "POST",
	        contentType : "application/json",
	        url :  contextPath+"/cf/ser", 
	        data : JSON.stringify(entityRoles),
	        success : function(data) {
		    }
	    });
	},
	
	getEntityRoles : function(){
		$.ajax({
	        async : false,
	        type : "GET",
	        url :  contextPath+"/cf/ler", 
	        data : {
	        	entityId:$("#dashletId").val(),
	        	moduleId:$("#moduleId").val(),
	        },
	        success : function(data) {
	            $.each(data, function(key,val){
	            	multiselect.setSelectedObject(val);
	            });
		    }
	    });
	},
   
	backToDashletListing : function() {
		location.href = contextPath+"/cf/dlm";
	},

}

