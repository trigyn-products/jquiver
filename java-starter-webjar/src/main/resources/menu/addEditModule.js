class AddEditModule {
    constructor() {

    }
}

AddEditModule.prototype.fn = {
    
    saveModule: function(){
    	let context = this;		
    	let moduleDetails = new Object();	
    	if($("#moduleId").val() !== ""){
			moduleDetails.moduleId = $("#moduleId").val();
		}	
		
		let isValid = context.validateMandatoryFileds();
		if(isValid === false){
			$("#errorMessage").show();
			return false;
		}
		let isSequenceExist = context.checkSequenceExist();
		if(isSequenceExist === true){
			$("#errorMessage").show();
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
				contentType : "application/json",
				data : JSON.stringify(moduleDetails),
				success : function(data) {
					$('#snackbar').html("Information saved successfully.");
					context.showSnackbarModule();
		       	},
	        
	        	error : function(xhr, error){
	        		$("#errorMessage").show();
					$('#errorMessage').html("Error occurred while saving");
	        	},
	        	
			});
    },
    
    validateMandatoryFileds : function(){
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
   		if(contextName === ""){
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
   		if(moduleURL === "" || moduleURL.indexOf(" ") != -1){
   			$("#moduleURL").focus();
   			$('#errorMessage').html("Please enter valid URL");
   			return false;
   		}
   		return true;
   		 
    },
    
    
    checkSequenceExist: function(){
    	let parentModuleId = $("#parentModuleName").find(":selected").val();
		let sequence = $("#sequence").val();
		let isSequenceExist = true;
		$.ajax({
			type : "GET",
			url : contextPath+"/cf/cms",
			async: false,
			cache : false,
			headers: {
    			"parent-module-id": parentModuleId,
    			"sequence":  sequence,
    		},
			success : function(data) {
				if(data == ""){
					isSequenceExist = false;
				}
		   	},
	       	error : function(xhr, error){
	       		$("#errorMessage").show();
				$('#errorMessage').html("Error occurred while validating sequence number.");
	       	},
	        	
		});
		if(isSequenceExist === true){
			$("#sequence").focus();
			$('#errorMessage').html("Sequence number already exists. Please select different one.");
		}
		return isSequenceExist;
    },
    
    
    
    getTargeTypeNames : function(){
    	let targetLookupId = $("#targetLookupType").find(":selected").val();
    	$("#targetTypeName").empty();
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
	    					optionElement = $('<option value="'+moduleTargetTypeArray[iCounter].targetTypeId+'">');
	            			optionElement.append(moduleTargetTypeArray[iCounter].targetTypeName+'</option>');
	    					$("#targetTypeName").append(optionElement);
	    				}
	    			}else {
	    				$("#errorMessage").html("");
	    				$("#errorMessage").html("Sorry target name are availabel");
	    			}
	       		},
	        	error : function(xhr, error){
	        		$("#errorMessage").show();
					$('#errorMessage').html("Error occurred while fetching target name");
	        	},
	        	
			});
    },
    
    backToModuleListingPage : function() {
		location.href = contextPath+"/cf/mul";
	},
		
	showSnackbarModule : function() {
	   	let snackBar = $("#snackbar");
	   	snackBar.addClass('show');
	   	setTimeout(function(){ 
	   		snackBar.removeClass("show");
	   	}, 3000);
	},
    
}
