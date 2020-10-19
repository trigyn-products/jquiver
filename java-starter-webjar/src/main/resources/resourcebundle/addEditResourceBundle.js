class AddEditResourceBundle {
    constructor(resourceBundleFormData) {
		this.resourceBundleFormData = resourceBundleFormData;
    }
}
AddEditResourceBundle.prototype.fn = {

	loadAddEditResourceBundlePage : function() {
		let data = $("#resourceBundleKey").val();
		if(data==""){
			$("#resourceBundleKey").prop("disabled",false);
		}
		else{
			$("#resourceBundleKey").prop("disabled",true);
		}
		this.updateResourceBundleData();
	},

	updateResourceBundleData : function(){
		resourceBundleFormData = [];
		let iCounter = resourceBundleFormData.length;
		let dataPresent = false;
		let presentAtIndex=0;
		$("textarea").change(function() {
			let resourceFormDataObject   = new Object();
	    	let languageIdStr = this.id;
			var languageId = languageIdStr.split("_")[1];
			let languageTextData = $(this).val();
			if(languageTextData != ""){
				let parser = new DOMParser();
	    		let doc = parser.parseFromString(languageTextData,"text/html");
	    		
	    		if(doc){
	    			let parsedDesc= doc.body.innerHTML;
	    			if(parsedDesc.indexOf("<br>") != -1){
	    				parsedDesc = parsedDesc.replace(/<br>/g, "<br></br>");
	    			}
	          		resourceFormDataObject.languageId = languageId;
	    			resourceFormDataObject.text = parsedDesc;
	    		}
			}
			
			if(resourceBundleFormData.length > 0){
				$.each(resourceBundleFormData, function(key, value) {
				      if(value[languageId]){
				    	  dataPresent = true;
				    	  presentAtIndex = key;
				    	  return false;
				      }
				});
			}
			if(dataPresent){
				resourceBundleFormData[presentAtIndex] = resourceFormDataObject;
			}else{
				resourceBundleFormData[iCounter] = resourceFormDataObject;
			}  
			
			iCounter++;
		});
	},
	
	
	saveResourceBundle : function(addEditFlag){
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
	  	
		let allTextAreaValues = JSON.stringify(resourceBundleFormData);
		let resourceBundleKey = $("#resourceBundleKey").val();
		
	
		$.ajax({
			type : "POST",
			async: false,
			contentType : "application/json",
			url : contextPath+"/cf/srb?resourceBundleKey="+resourceBundleKey,
			data :  JSON.stringify(resourceBundleFormData),
			dataType : "json",				
			success : function(data) {
				isDataSaved = true;
				showMessage("Information saved successfully", "success");
	        },
	       	error : function(xhr, error){
				showMessage("Error occurred while saving", "error");
	        },
		});
		return isDataSaved;
	},
	
	validateKey : function(addEditFlag){
		
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
	},
	
	validateEng : function(){
	
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
	},
	
	backToResourceBundleListing : function() {
	  location.href = contextPath+"/cf/rb"
	},
	
}
