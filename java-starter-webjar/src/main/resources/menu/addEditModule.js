class AddEditModule {
    constructor(moduleTypeId, parentModuleId) {
		this.moduleTypeId = moduleTypeId;
		this.parentModuleId = parentModuleId;
    }
    
    saveModule = function(){
    	let context = this;		
    	let moduleDetails = new Object();	
    	let isDataSaved = false;
    	if($("#moduleId").val() !== ""){
			moduleDetails.moduleId = $("#moduleId").val();
		}	
		
		if(context.validateMandatoryFileds() == false){
			$("#errorMessage").show();
			return false;
		}
		
		if(context.validateExistingData()){
			return false;
		}
		
		moduleDetails.moduleName = $("#moduleName").val();
		moduleDetails.parentModuleId = $("#parentModuleName").find(":selected").val();
		moduleDetails.moduleURL = $("#moduleURL").val();
		moduleDetails.sequence = $("#sequence").val();
		moduleDetails.targetLookupId = $("#targetLookupType").find(":selected").val();
		moduleDetails.targetTypeId = $("#targetTypeNameId").val();
		moduleDetails.isInsideMenu =  $("#isInsideMenu").val();
		
		$.ajax({
				type : "POST",
				url : contextPath+"/cf/sm",
				async: false,
				contentType : "application/json",
				data : JSON.stringify(moduleDetails),
				success : function(data) {
					$("#errorMessage").hide();
					context.saveEntityRoleAssociation(data);
					context.parentModuleId = $("#parentModuleName").find(":selected").val();
					showMessage("Information saved successfully", "success");
					isDataSaved = true;
		       	},
	        
	        	error : function(xhr, error){
	        		showMessage("Error occurred while saving", "error");
	        	},
	        	
			});
		return 	isDataSaved;
    }
    
    validateMandatoryFileds = function(){
    	$('#errorMessage').html("");
   		let moduleName = $("#moduleName").val().trim();
   		if(moduleName === ""){
   			$("#moduleName").focus();
   			$('#errorMessage').html("Please enter module name");
   			return false;
   		}
   		
   		let contextType = $("#targetLookupType").find(":selected").val();
   		if(contextType === ""){
   			$("#targetLookupType").focus();
   			$('#errorMessage').html("Please select context type");
   			return false;
   		}
   		
   		let contextName = $("#targetTypeName").val();
   		if(contextName === "" && contextType != "6"){
   			$("#targetTypeName").focus();
   			$('#errorMessage').html("Please select context name");
   			return false;
   		}
   		
   		let sequence = $("#sequence").val().trim();
   		if(sequence === "" && ($("#insideMenuCheckbox").prop("checked")) === true){
   			$("#sequence").focus();
   			$('#errorMessage').html("Please enter sequence number");
   			return false;
   		}
   		
   		let moduleURL = $("#moduleURL").val().trim();
   		if((moduleURL === ""  || moduleURL.length > 200	|| moduleURL.indexOf("#") != -1) && contextType != "6"){
   			$("#moduleURL").focus();
   			$('#errorMessage').html("Please enter valid URL");
   			return false;
   		}
   		return true;
   		 
    }
    
    
	validateExistingData = function(){
    	let isDataExist = false;
    	let parentModuleId = $("#parentModuleName").find(":selected").val();
		let sequence = $("#sequence").val();
		let moduleName = $("#moduleName").val();
    	let moduleURL = $("#moduleURL").val();
    	let moduleId = $("#moduleId").val();
    	$.ajax({
			type : "GET",
			url : contextPath+"/cf/ced",
			async: false,
			cache : false,
			headers: {
    			"parent-module-id": parentModuleId,
    			"sequence":  sequence,
    			"module-url" : moduleURL,
    			"module-name" : moduleName,
    			"module-id": moduleId,
    		},
			success : function(data) {
				if(data != ""){
	    			let moduleIdName = data.moduleIdName;
	    			let moduleIdSequence = data.moduleIdSequence;
	    			let moduleIdURL = data.moduleIdURL
	    			if(moduleIdName != undefined && moduleIdName != moduleId){
	    				isDataExist = true;
	    				$('#errorMessage').html("Module name already exist");
	    			}
	    			if(isDataExist == false && moduleIdSequence != undefined && moduleIdSequence != moduleId){
	    				isDataExist = true;
	    				$('#errorMessage').html("Sequence number already exist");
	    			}
	    			if(isDataExist == false && moduleIdURL != undefined && moduleIdURL != moduleId && moduleURL !== "#"){
	    				isDataExist = true;
	    				$('#errorMessage').html("Module URL already exist");
	    			}
	    		}
		   	},
	       	error : function(xhr, error){
	       		showMessage("Error occurred while validating with existing data", "error");
	       	},
	        	
		});
		if(isDataExist === true){
			$("#errorMessage").show();
		}
		return isDataExist;
    }
    
    
    getTargeTypeNames = function(isEditFlag){
    	let context = this;
    	let targetLookupId = $("#targetLookupType").find(":selected").val();
    	$("#targetTypeName").prop('disabled',true);
    	if(targetLookupId === "6") {
	    	$("#targetTypeName").val("");
	    	$("#moduleURL").val("#");
	    	$("#parentModuleName").val("");
	    	$("#targetTypeName").attr('disabled','disabled');
	    	$("#moduleURL").attr('disabled','disabled');
	    	$("#parentModuleName").attr('disabled','disabled');
	    	if(isEditFlag === undefined){
	    		context.getSequenceByGroup();
	    	}
    	}else{
    		$("#parentModuleName").val(context.parentModuleId);
    		$("#targetTypeName").prop('disabled',false);
	    	$("#moduleURL").prop('disabled',false);
	    	if(isEditFlag === undefined){
	    		$("#moduleURL").val("");
	    		$("#targetTypeName").val("");
	    		$("#targetTypeNameId").val("");
	    	}
	    	$("#parentModuleName").prop('disabled',false);
	    	autocomplete.options.autocompleteId = context.getAutocompleteId();
    	}
    	context.insideMenuOnChange();
    }
    
    getAutocompleteId = function(){
    	let context = this;
    	let autocompleteId;
    	let targetLookupId = $("#targetLookupType").find(":selected").val();
        if(targetLookupId == ""){
    		$("#targetTypeName").prop('disabled',true);
		} else if(targetLookupId == 1){
    		$("#targetTypeName").prop('disabled',false);
    		autocompleteId = "dashboardListing";
		} else if(targetLookupId == 2){
    		$("#targetTypeName").prop('disabled',false);
    		autocompleteId = "dynamicForms";
		} else if(targetLookupId == 3){
    		$("#targetTypeName").prop('disabled',false);
    		autocompleteId = "dynarestListing";
		} else if(targetLookupId == 5){
    		$("#targetTypeName").prop('disabled',false);
    		autocompleteId = "templateListing";
		}
    	return autocompleteId;
    }
    
    
    getSequenceByParent = function(){
    	let context = this;
        let parentModuleId = $("#parentModuleName").find(":selected").val();
        if(parentModuleId == ""){
        	context.getSequenceByGroup();
        }else{
	    	$.ajax({
				type : "GET",
				url : contextPath+"/cf/dsp",
				async: false,
				cache : false,
				headers: {
	    			"parent-module-id": parentModuleId,
	    		},
				success : function(data) {
					if(data != ""){
						$("#sequence").val(data);
					}
				},
		       	error : function(xhr, error){
		       		showMessage("Error occurred while fetching sequence number", "error");
		       	},
			});
		}
    }
    
    getSequenceByGroup = function(){
    	$.ajax({
			type : "GET",
			url : contextPath+"/cf/dsg",
			async: false,
			cache : false,
			success : function(data) {
				if(data != ""){
					$("#sequence").val(data);
				}
			},
	       	error : function(xhr, error){
	       		showMessage("Error occurred while fetching sequence number", "error");
	       	},
		});
    }
    
   	insideMenuOnChange = function(){
   		let context = this;
   		let isInsideMenu = $("#insideMenuCheckbox").prop("checked");
   		let targetLookupId = $("#targetLookupType").find(":selected").val();
   		if(isInsideMenu){
   			$("#isInsideMenu").val(1);
   			$("#sequence").prop('disabled',false);
   			$("#parentModuleName").prop('disabled',false);
   			if(sequence !== undefined && sequence !== ""){
   				$("#sequence").val(sequence);
   				return true;
   			}
   			context.getSequenceByParent();
   		}
   		else{
			$("#isInsideMenu").val(0);
   			$("#parentModuleName").val("");
   			$("#sequence").val("");
   			$("#sequence").prop('disabled',true);
   			$("#parentModuleName").prop('disabled',true);
   		}
   		
   	}
   	
    backToModuleListingPage = function() {
		location.href = contextPath+"/cf/mul";
	}
	
    saveEntityRoleAssociation = function (menuId){
		let roleIds =[];
		let entityRoles = new Object();
		entityRoles.entityName = $("#moduleName").val().trim();
		entityRoles.moduleId=$("#masterModuleId").val();
		entityRoles.entityId= menuId;
		 $.each($("#rolesMultiselect_selectedOptions_ul span.ml-selected-item"), function(key,val){
			 roleIds.push(val.id);
         	
         });
		
		entityRoles.roleIds=roleIds;
		
		$.ajax({
            async : false,
            type : "POST",
            contentType : "application/json",
            url : contextPath+"/cf/ser", 
            data : JSON.stringify(entityRoles),
            success : function(data) {
		    }
        });
	}
	getEntityRoles = function(){
		$.ajax({
            async : false,
            type : "GET",
            url : contextPath+"/cf/ler", 
            data : {
            	entityId:$("#moduleId").val(),
            	moduleId:$("#masterModuleId").val(),
            },
            success : function(data) {
                $.each(data, function(key,val){
                	multiselect.setSelectedObject(val);
                	
                });
		    }
        });
	}
    
}
