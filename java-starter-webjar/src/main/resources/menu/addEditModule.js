class AddEditModule {
    constructor(moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
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
		moduleDetails.targetTypeId = $("#targetTypeName").find(":selected").val();
		
		$.ajax({
				type : "POST",
				url : contextPath+"/cf/sm",
				async: false,
				contentType : "application/json",
				data : JSON.stringify(moduleDetails),
				success : function(data) {
					$("#moduleId").val(data);
					$('#snackbar').html("Information saved successfully.");
					context.showSnackbarModule();
		       	},
	        
	        	error : function(xhr, error){
	        		$("#errorMessage").show();
					$('#errorMessage').html("Error occurred while saving");
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
	       		$("#errorMessage").show();
				$('#errorMessage').html("Error occurred while validating module URL.");
	       	},
	        	
		});
		if(isDataExist === true){
			$("#errorMessage").show();
		}
		return isDataExist;
    }
    
    
    getTargeTypeNames = function(){
    	let context = this;
    	let targetLookupId = $("#targetLookupType").find(":selected").val();
    	$("#targetTypeName").empty();
    	let selectOption = $('<option value=" ">Select</option>');
    	$("#targetTypeName").append(selectOption);
    	if(targetLookupId === "6") {
	    	$("#targetTypeName").val(" ");
	    	$("#moduleURL").val("#");
	    	$("#parentModuleName").val("");
	    	$("#targetTypeName").attr('disabled','disabled');
	    	$("#moduleURL").attr('disabled','disabled');
	    	$("#parentModuleName").attr('disabled','disabled');
    		return;
    	}else{
    		$("#parentModuleName").val("");
    		$("#targetTypeName").prop('disabled',false);
	    	$("#moduleURL").prop('disabled',false);
	    	$("#parentModuleName").prop('disabled',false);
    	}
    	if(targetLookupId != 4){
    			$.ajax({
				type : "GET",
				url : contextPath+"/cf/ltlm",
				contentType : "application/json",
				dataType: "json",
				headers: {
    				"target-lookup-id":  targetLookupId,
    			},
				success : function(data) {
					if(data.length > 0){
	    				let moduleTargetTypeArray = data;
	    				let dashletDiv;
	    				for(let iCounter = 0; iCounter < moduleTargetTypeArray.length; ++iCounter){
	    					let optionElement;
	    					let targetTypeId = moduleTargetTypeArray[iCounter].targetTypeId;
	    					
	    					if(targetTypeId == context.moduleTypeId){
	    						optionElement = $('<option value="'+targetTypeId+'" selected>');
	    					}else{
	    						optionElement = $('<option value="'+targetTypeId+'">');
	    					}
							optionElement.append(moduleTargetTypeArray[iCounter].targetTypeName+'</option>');	    					
	    					$("#targetTypeName").append(optionElement);
	    				}
	    			}else {
	    				$("#errorMessage").html("");
	    				$("#errorMessage").html("Sorry target name are available");
	    			}
	       		},
	        	error : function(xhr, error){
	        		$("#errorMessage").show();
					$('#errorMessage').html("Error occurred while fetching target name");
	        	},
	        	
			});
		}
    }
    
    backToModuleListingPage = function() {
		location.href = contextPath+"/cf/mul";
	}
		
	showSnackbarModule = function() {
	   	let snackBar = $("#snackbar");
	   	snackBar.addClass('show');
	   	setTimeout(function(){ 
	   		snackBar.removeClass("show");
	   	}, 3000);
	}
}
