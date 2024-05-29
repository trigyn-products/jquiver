class AddEditResourceBundle {
    constructor(resourceBundleFormData) {
		this.resourceBundleFormData = resourceBundleFormData;
    }


	loadAddEditResourceBundlePage = function() {
		let data = $("#resourceBundleKey").val();
		if(data==""){
			$("#resourceBundleKey").prop("disabled",false);
		}
		else{
			$("#resourceBundleKey").prop("disabled",true);
		}
	}

	updateResourceBundleData = function(){
		let context = this;
		let iCounter = context.resourceBundleFormData.length;
		$("textarea").each(function(){
			let resourceBundleKey = $("#resourceBundleKey").val();
			let resourceFormDataObject  = new Object();
	    	let languageIdStr = this.id;
			var languageId = languageIdStr.split("_")[1];
			let languageTextData = $(this).val().trim();
			if(languageTextData != ""){
				let parser = new DOMParser();
	    		let doc = parser.parseFromString(languageTextData,"text/html");
	    		
	    		if(doc){
	    			let parsedDesc= doc.body.innerHTML;
	    			if(parsedDesc.indexOf("<br>") != -1){
	    				parsedDesc = parsedDesc.replace(/<br>/g, "<br></br>");
	    			}
	          		resourceFormDataObject.resourceKey = resourceBundleKey;
	          		resourceFormDataObject.languageId = languageId;
	    			resourceFormDataObject.text = parsedDesc;
	    		}
			}
			
			context.resourceBundleFormData[iCounter] = resourceFormDataObject;
			
			iCounter++;
		});
	}
	
	
	saveResourceBundle = function(addEditFlag){
		let context = this;
		let isDataSaved = false;
		let validData = context.validateKey(addEditFlag);
	    if (validData == false) {
			return false;
		}
		
		validData = context.validateEng();
		if (validData == false) {
			return false;
		} 
	  	
	  	context.updateResourceBundleData();
		$.ajax({
			type : "POST",
			async: false,
			contentType : "application/json",
			url : contextPath+"/cf/srb",
			data :  JSON.stringify(context.resourceBundleFormData),
			dataType : "json",				
			success : function(data) {
				isDataSaved = true;
				context.resourceBundleFormData = new Array();
				showMessage("Information saved successfully", "success");
	        },
	       	error : function(xhr, error){
	       		context.resourceBundleFormData = new Array();
				showMessage("Error occurred while saving", "error");
	        },
		});
		return isDataSaved;
	}
	
	validateKey = function(addEditFlag){
		
		let resourceKey = $("#resourceBundleKey").val().trim();
		if(resourceKey === ""){
			$("#resourceBundleKey").focus();
			$("#errorMessage").show();
			$('#errorMessage').html("Please enter the key");
			return false;
		}else if(addEditFlag == "isAdd"){
			let isKeyExist = true;
			$.ajax({
				async : false,
				type : "GET",
				datatype:'json',
				url : contextPath+"/cf/crbk?resourceKey="+resourceKey,
				success : function(data) {	
					isKeyExist = data;
				}
			});
			
			if (isKeyExist==true) {
				$('#errorMessage').show();
				$('#errorMessage').html("This key already exists. Kindly enter a different key.");
				$('#resourceBundleKey').focus();
			}else{
				$("#errorMessage").html("");
				$("#errorMessage").hide();
			} 
			return !isKeyExist;
		}else{
			return true;
		}
	}
	
	validateEng = function(){
	
		let eng = $("#textBx_1").val().trim();
		if(eng === ""){
			$("#textBx_1").focus();
			$("#errorMessage").show();
			$('#errorMessage').html("Please enter the English Text");
			return false;
		}else{
			$("#errorMessage").html("");
			$("#errorMessage").hide();
			return true;
		}
	}
	
	backToResourceBundleListing = function() {
	  location.href = contextPath+"/cf/rb"
	}
	
}
