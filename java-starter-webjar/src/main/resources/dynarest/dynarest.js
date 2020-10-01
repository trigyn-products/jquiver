class DynamicRest {

	constructor() {
    	this.saveUpdateEditors = new Array();
    	this.daoDetailsIds = new Array();
    	this.serviceLogicContent;
    }
    
    populateDetails = function(requestTypeId, prodTypeId, dynaresturl){
    	let context = this;
		$.ajax({
			type : "GET",
			url : contextPath+"/dyn/api/dynarestDetails",
			data : {
				url : dynaresturl
			},
			success : function(data) {
				let dynarestDetailsArray = data.dynarestDetails;
				if(dynarestDetailsArray !== null && dynarestDetailsArray.length != 0){
					context.populateServiceLogic(dynarestDetailsArray[0].dynarestServiceLogic);
					for (let counter = 0; counter < dynarestDetailsArray.length; counter++){
						context.addSaveQueryEditor(dynarestDetailsArray[counter].daoDetailsId, dynarestDetailsArray[counter].versionDetails
							, dynarestDetailsArray[counter].variableName, dynarestDetailsArray[counter].dynarestDaoQuery);
					}
				}else{
					context.populateServiceLogic();
					context.addSaveQueryEditor();
				}
				
				for(let counter = 0; counter < data["methodTypes"].length; ++counter) {
					let object = data["methodTypes"][counter];
					$("#dynarestRequestTypeId").append("<option value='"+object["value"]+"'>"+object["name"]+"</option>");
					
				}
				
				for(let counter = 0; counter < data["producerDetails"].length; ++counter) {
					let object = data["producerDetails"][counter];
					$("#dynarestProdTypeId").append("<option value='"+object["value"]+"'>"+object["name"]+"</option>");
				}
				
				$('#dynarestProdTypeId').val(prodTypeId);
				$('#dynarestRequestTypeId').val(requestTypeId); 
			}
		});
		
    }
    
    populateServiceLogic = function(serviceLogicContent){
    	const context = this;
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
    	require(["vs/editor/editor.main"], function() {
        context.serviceLogicContent = monaco.editor.create(document.getElementById("htmlEditor"), {
		        	value: serviceLogicContent,
		            language: "java",
		            roundedSelection: false,
					scrollBeyondLastLine: false,
					readOnly: false,
					theme: "vs-dark",
					wordWrap: 'wordWrapColumn',
					wordWrapColumn: 100,
					wordWrapMinified: true,
					wrappingIndent: "indent"
	        	});
    	});
    }
    
    addSaveQueryEditor = function(daoDetailsId, versionDetails, variableName, saveQueryContent){
    	const context = this;
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
    	require(["vs/editor/editor.main"], function() {
			let index = context.saveUpdateEditors.length;
			
			if(versionDetails != undefined && versionDetails != ""){
	    		$("#saveScriptContainer").append("<div class='col-3'><div id='compareDiv_"+index+"' class='col-inner-form full-form-fields'><label for='versionId'>Compare with </label>");
	    		$("#compareDiv_"+index).append("<select class='form-control' id='versionSelect_"+index+"' onchange='addEdit.getSelectTemplateData();' name='versionId' title='Template Versions'>");
	    		$("#versionSelect_"+index).append("<option value='' selected>Select</option>");
	    	}
	    	
	    	$("#saveScriptContainer").append("<input id='inputcontainer_"+index+"' type ='text' class='form-control' />");	
			$("#saveScriptContainer").append("<div id='container_"+index+"' class='html_script' style='margin-top: 10px;'><div class='grp_lblinp'><div id='saveSqlContainer_"+index+"' class='ace-editor-container'><div id='saveSqlEditor_"+index+"' class='ace-editor'></div></div></div></div>");
			if(variableName){
				$("#inputcontainer_"+index).val(variableName);
			}
		
        	let saveUpdateEditor = monaco.editor.create(document.getElementById("saveSqlEditor_"+index), {
		        	value: saveQueryContent,
		            language: "sql",
		            roundedSelection: false,
					scrollBeyondLastLine: false,
					readOnly: false,
					theme: "vs-dark",
					wordWrap: 'wordWrapColumn',
					wordWrapColumn: 100,
					wordWrapMinified: true,
					wrappingIndent: "indent"
	        	});
	        	context.saveUpdateEditors.push(saveUpdateEditor);
	        	if(daoDetailsId != undefined){
	        		$("#saveScriptContainer").append("<input type='hidden' id='daoDetailsId_"+daoDetailsId+"' value="+daoDetailsId+"/>");
	        		context.daoDetailsIds.push(daoDetailsId);
	        	}
    	});
	}
	
	removeSaveQueryEditor = function(){
		let index = this.saveUpdateEditors.length - 1;
		if(index != 0){
			$("#container_"+index).remove();
			$("#inputcontainer_"+index).remove();
			$("#compareDiv_"+index).remove();
			this.saveUpdateEditors.pop();
			if($("#daoDetailsId_"+index).length == 1){
				$("#daoDetailsId_"+index).remove();
				this.daoDetailsIds.pop();
			}
		}
	}
	
	saveDynarest = function(formId){
		let context = this;
		let validData = context.validateDyanrestFields();
		if(validData === false){
			$('#errorMessage').show();
			return false;
		}
		$("#serviceLogic").val(this.serviceLogicContent.getValue().toString());
		let formData = $("#dynamicRestForm").serialize() + "&formId="+formId;
		
		$.ajax({
		    type : "POST",
		    url : "sdf",
			data : formData, 
			success : function(data){
				if(data === true){
					context.saveDAOQueries(formId);
				}
			},
			error : function(xhr, data){
				$("#errorMessage").show();
				$("#errorMessage").html("Error occurred");
			},
		});
	}

	validateDyanrestFields = function(){
		let dynarestUrl = $("#dynarestUrl").val();
		if(dynarestUrl === "" || dynarestUrl.indexOf(" ") != -1){
			$("#dynarestUrl").focus();
			$('#errorMessage').html("Please enter valid URL");
			return false;
		}
		
		let dynarestMethodName =  $("#dynarestMethodName").val().trim();
		if(dynarestMethodName === "" || dynarestMethodName.indexOf(" ") != -1){
			$("#dynarestMethodName").focus();
			$('#errorMessage').html("Please enter valid method name");
			return false;
		}
		
		let dynarestMethodDesc =  $("#dynarestMethodDescription").val().trim();
		if(dynarestMethodDesc === ""){
			$("#dynarestMethodDescription").focus();
			$('#errorMessage').html("Method description cannot be blank");
			return false;
		}
		return true;
	}
	
	saveDAOQueries = function(formId){
		let context = this;
		let saveUpdateQueryArray = new Array();
		let variableNameArray = new Array();
		let daoQueryArray = new Array();
		
		let dashletDetails = new Object();
		
		let form = $('<form id="saveUpdateQueryForm"></form>');
		form.append('<input name="dynarestUrl" id="dynarestUrlDAO" type="hidden" />');
		form.append('<input name="dynarestMethodName" id="dynarestMethodNameDAO" type="hidden" />');
		form.append('<input name="daoDetailsIds" id="daoDetailsIds" type="hidden" />');
		form.append('<input name="variableName" id="variableName" type="hidden" />');
		form.append('<input name="daoQueryDetails" id="daoQueryDetails" type="hidden" />');
		form.insertAfter($("#dynamicRestForm"));
		
		let saveEditorLength = this.saveUpdateEditors.length;
		for(let iCounter = 0; iCounter < saveEditorLength; ++iCounter){
			let variableName = $('#inputcontainer_'+iCounter).val();
			let daoQuery = (this.saveUpdateEditors[iCounter].getValue().toString());
			variableNameArray.push(variableName);
			daoQueryArray.push(daoQuery);
		}
		$("#dynarestUrlDAO").val($("#dynarestUrl").val());
		$("#dynarestMethodNameDAO").val($("#dynarestMethodName").val());
		$("#daoDetailsIds").val(JSON.stringify(this.daoDetailsIds));
		$("#variableName").val(JSON.stringify(variableNameArray));
		$("#daoQueryDetails").val(JSON.stringify(daoQueryArray));
		
		let formData = $("#saveUpdateQueryForm").serialize();
		$.ajax({
		    type : "POST",
		    url : contextPath+"/cf/sdq",
		    data : formData,
			success : function(data){
				$("#saveUpdateQueryForm").remove();
				if(data === true){
					$("#snackbar").html("Information saved successfully");
					context.showSnackbarDynarest();
				}
			}
		});	
	}
	
	
	backToDynarestListingPage = function() {
    	window.location = "../cf/dynl";
  	}
  	
  	showSnackbarDynarest = function() {
    	let snackBar = $("#snackbar");
    	snackBar.addClass('show');
    	setTimeout(function(){ 
    		snackBar.removeClass("show");
    	}, 3000);
	}
	
	
}