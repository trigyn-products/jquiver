class AddEditModule {
    constructor(moduleTypeId, parentModuleId) {
		this.moduleTypeId = moduleTypeId;
		this.parentModuleId = parentModuleId;
    }
    
    saveModule = function(){
    	let context = this;		
    	let moduleDetails = new Object();	
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
		
		$.ajax({
				type : "POST",
				url : contextPath+"/cf/sm",
				async: false,
				contentType : "application/json",
				data : JSON.stringify(moduleDetails),
				success : function(data) {
					$("#errorMessage").hide();
					context.parentModuleId = $("#parentModuleName").find(":selected").val();
					$("#moduleId").val(data);
					showMessage("Information saved successfully", "success");
		       	},
	        
	        	error : function(xhr, error){
	        		showMessage("Error occurred while saving", "error");
	        	},
	        	
			});
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
   		if(sequence === ""){
   			$("#sequence").focus();
   			$('#errorMessage').html("Please enter sequence number");
   			return false;
   		}
   		
   		let moduleURL = $("#moduleURL").val().trim();
   		if((moduleURL === "" || moduleURL.indexOf(" ") != -1 
   			|| moduleURL.indexOf("*") != -1 || moduleURL.indexOf("/") == 0) && contextType != "6"){
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
    			"module-name" : moduleName
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
    		return;
    	}else{
    		$("#parentModuleName").val(context.parentModuleId);
    		$("#targetTypeName").prop('disabled',false);
	    	$("#moduleURL").prop('disabled',false);
	    	$("#parentModuleName").prop('disabled',false);
    	}
    	if(targetLookupId == ""){
    		$("#targetTypeName").prop('disabled',true);
		} else if(targetLookupId == 1){
    		$("#targetTypeName").prop('disabled',false);
    		autocomplete.options.autocompleteId = "dashboardListing";
		} else if(targetLookupId == 2){
    		$("#targetTypeName").prop('disabled',false);
    		autocomplete.options.autocompleteId = "dynamicForms";
		} else if(targetLookupId == 3){
    		$("#targetTypeName").prop('disabled',false);
    		autocomplete.options.autocompleteId = "dynarestListing";
		} else if(targetLookupId == 5){
    		$("#targetTypeName").prop('disabled',false);
    		autocomplete.options.autocompleteId = "templateListing";
		}
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
    
    backToModuleListingPage = function() {
		location.href = contextPath+"/cf/mul";
	}
		
}
