class DynamicRest {

	constructor(formId) {
		this.formId = formId;
    	this.saveUpdateEditors = new Array();
    	this.daoDetailsIds = new Array();
    	this.serviceLogicContent;
    }
    
    populateDetails = function(requestTypeId, prodTypeId, dynaresturl){
    	let context = this;
		$.ajax({
			type : "GET",
			url : contextPath+"/api/dynarestDetails",
			data : {
				url : dynaresturl
			},
			success : function(data) {
				let dynarestDetailsArray = data.dynarestDetails;
				if(dynarestDetailsArray !== null && dynarestDetailsArray.length != 0){
					context.populateServiceLogic(dynarestDetailsArray[0].dynarestServiceLogic);
					for (let counter = 0; counter < dynarestDetailsArray.length; counter++){
						context.addSaveQueryEditor(null, dynarestDetailsArray[counter].daoDetailsId, dynarestDetailsArray[counter].versionDetails
							, dynarestDetailsArray[counter].variableName, dynarestDetailsArray[counter].dynarestDaoQuery, dynarestDetailsArray[counter].dynarestQueryType);
					}
			    	getEntityRoles()
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
				context.hideShowAllowFiles();
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
	        	context.serviceLogicContent.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
					typeOfAction('dynamic-rest-form', $("#savedAction").find("button"), dynarest.saveDynarest.bind(dynarest), dynarest.backToDynarestListingPage);
				});
				context.serviceLogicContent.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M,function() {
	                resizeMonacoEditor(context.serviceLogicContent,"htmlContainer","htmlEditor");
	            });
				context.serviceLogicContent.onDidChangeModelContent( function (){
    				$('#errorMessage').hide();
				});
    	});

    }
    
    addSaveQueryEditor = function(element, daoDetailsId, versionDetails, variableName, saveQueryContent, queryType){
    	const context = this;
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
    	require(["vs/editor/editor.main"], function() {
			let index = context.saveUpdateEditors.length;
			let parentElement;
			if(element != null) {
				parentElement = $(element).parent().parent().parent().parent();
			}
			if(versionDetails != undefined && versionDetails != ""){
	    		$("#saveScriptContainer").append("<div class='col-3'><div id='compareDiv_"+index+"' class='col-inner-form full-form-fields'><label for='versionId'>Compare with </label>");
	    		$("#compareDiv_"+index).append("<select class='form-control' id='versionSelect_"+index+"' onchange='addEdit.getSelectTemplateData();' name='versionId' title='Template Versions'>");
	    		$("#versionSelect_"+index).append("<option value='' selected>Select</option>");
	    	}
	    	
	    	let inputElement = "<div class='col-4'><label for='inputcontainer_"+index+"' style='white-space:nowrap'> Variable Name </label><input id='inputcontainer_"+index+"' type ='text' class='form-control' /></div>";
	    	let selectElement = "<div class='col-4'><label for='selectcontainer_"+index+"' style='white-space:nowrap'>Query Type </label><select id='selectcontainer_"+index+"' class='form-control'><option value='1'>Select Query</option><option value='2'>Insert-Update-Delete Query</option><option value='3'>Stored Procedure</option></select></div>";
	    	let buttonElement = "<div class='btn-icons float-right'><input type='button' id='addEditor_"+index+"' value='Add' class='margin-r-5 btn btn-primary' onclick='dynarest.addSaveQueryEditor(this);'><input type='button' id='removeTemplate_"+index+"' value='Remove' class='btn btn-secondary' onclick='dynarest.removeSaveQueryEditor(this);'></div>";
	    	
	    	let daoContainer = $("<div id='daoContainerDiv_"+index+"' class='margin-t-25'><div class='row'>"+ inputElement +""+ selectElement +"<div class='col-4 margin-t-25 float-right'>"+ buttonElement +"</div></div></div>");
	    	daoContainer.append("<div id='container_"+index+"' class='html_script' style='margin-top: 10px;'><div class='grp_lblinp'><div id='saveSqlContainer_"+index+"' class='ace-editor-container'><div id='saveSqlEditor_"+index+"' class='ace-editor'></div></div></div></div></div>");
	    	
	    	if(parentElement != undefined) {
	    		daoContainer.insertAfter(parentElement);
	    	} else {
	    		$("#saveScriptContainer").append(daoContainer);	
	    	}

			if(variableName){
				$("#inputcontainer_"+index).val(variableName);
			}
		
			if(queryType != undefined){
	    		$("#selectcontainer_"+index).val(queryType);
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
	        	
	        	let editorObj = new Object();
	        	editorObj["index"] = index;
	        	editorObj["editor"] = saveUpdateEditor;
	        	context.saveUpdateEditors.push(editorObj);
	        	
	        	if(daoDetailsId != undefined){
	        		$("#saveScriptContainer").append("<input type='hidden' id='daoDetailsId_"+daoDetailsId+"' value="+daoDetailsId+"/>");
	        		let daoDetailsObject = new Object();
	        		daoDetailsObject["index"] = index;
	        		daoDetailsObject["daoDetailsIds"] = daoDetailsId;
	        		context.daoDetailsIds.push(daoDetailsObject);
	        	}
	        	$("#removeTemplate_0").remove();
	        	saveUpdateEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
					typeOfAction('dynamic-rest-form', $("#savedAction").find("button"), dynarest.saveDynarest.bind(dynarest), dynarest.backToDynarestListingPage);
				});
	        	saveUpdateEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyMod.Shift | monaco.KeyCode.KEY_M,function() {
	                resizeMonacoEditor(saveUpdateEditor,"saveSqlContainer_"+index,"saveSqlEditor_"+index);
	            });
				saveUpdateEditor.onDidChangeModelContent( function (){
    				$('#errorMessage').hide();
				});
	        	context.updateVariableSeq();
	        	disableInputSuggestion();
    	});
	}
	
	removeSaveQueryEditor = function(element){
		let context = this;
		let index = element.id.split("_")[1];
		if(index != 0){
			$("#daoContainerDiv_"+index).remove();
			$("#inputcontainer_"+index).remove();
			$("#compareDiv_"+index).remove();
			removeByAttribute(this.saveUpdateEditors, "index", index);
			if($("#daoDetailsId_"+index).length == 1){
				$("#daoDetailsId_"+index).remove();
				removeByAttribute(this.daoDetailsIds, "index", index);
			}
		}
		context.updateVariableSeq();
	}
	
	saveDynarest = function(){
		let isDataSaved = false;
		let context = this;
		let validData = context.validateDyanrestFields();
		if(validData === false){
			$('#errorMessage').show();
			return false;
		}
		$("#serviceLogic").val(this.serviceLogicContent.getValue().toString());
		let formData = $("#dynamicRestForm").serialize() + "&formId="+context.formId;
		
		$.ajax({
		    type : "POST",
		    url : contextPath+"/cf/sdf",
		    async : false,
			data : formData, 
			success : function(data){
				if(data === true){
					$("#isEdit").val(1);
					isDataSaved = context.saveDAOQueries(context.formId);
				}
			},
			error : function(xhr, data){
				$("#errorMessage").show();
				$("#errorMessage").html("Error occurred");
			},
		});
		return isDataSaved;
	}

	validateDyanrestFields = function(){
		let context = this;
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
		
		let serviceLogicContent = $.trim(context.serviceLogicContent.getValue().toString());
		if(serviceLogicContent === ""){
			$('#errorMessage').html("Service logic can not be blank");
			return false;
		}
		
		let variableNameArray = new Array();
		let saveEditorLength = $("[id^=daoContainerDiv_]").length;
		for(let iCounter = 0; iCounter < saveEditorLength; ++iCounter){
			let index = $("[id^=daoContainerDiv_]")[iCounter].id.split("_")[1];
			let variableName = $.trim($('#inputcontainer_'+index).val());
			variableNameArray.push(variableName);
		}
		if(context.checkIfDuplicateExists(variableNameArray)){
			$('#errorMessage').html("Variable name should be unique");
			return false;
		}
		return true;
	}
	
	checkIfDuplicateExists = function(variableNameArray){
    	return new Set(variableNameArray).size !== variableNameArray.length 
	}
	
	saveDAOQueries = function(formId){
		let context = this;
		let isDataSaved = false;
		let saveUpdateQueryArray = new Array();
		let variableNameArray = new Array();
		let queryTypeArray = new Array();
		let daoQueryArray = new Array();
		
		
		let dashletDetails = new Object();
		
		let form = $('<form id="saveUpdateQueryForm"></form>');
		form.append('<input name="dynarestUrl" id="dynarestUrlDAO" type="hidden" />');
		form.append('<input name="dynarestMethodName" id="dynarestMethodNameDAO" type="hidden" />');
		form.append('<input name="serviceLogic" id="serviceLogicForm" type="hidden" />');
		form.append('<input name="daoDetailsIds" id="daoDetailsIds" type="hidden" />');
		form.append('<input name="variableName" id="variableName" type="hidden" />');
		form.append('<input name="queryType" id="queryType" type="hidden" />');
		form.append('<input name="daoQueryDetails" id="daoQueryDetails" type="hidden" />');
		form.insertAfter($("#dynamicRestForm"));
		
		let saveEditorLength = $("[id^=daoContainerDiv_]").length;
		for(let iCounter = 0; iCounter < saveEditorLength; ++iCounter){
			let index = $("[id^=daoContainerDiv_]")[iCounter].id.split("_")[1];
			let editorObject = context.saveUpdateEditors.find(editors => editors["index"] == index);
			let variableName = $('#inputcontainer_'+index).val();
			let queryType = $('#selectcontainer_'+index).val();
			let daoQuery = (editorObject["editor"].getValue().toString().trim());
			variableNameArray.push(variableName);
			queryTypeArray.push(queryType);
			daoQueryArray.push(daoQuery);
		}
		
		$("#dynarestUrlDAO").val($("#dynarestUrl").val());
		$("#dynarestMethodNameDAO").val($("#dynarestMethodName").val());
		$("#serviceLogicForm").val(this.serviceLogicContent.getValue().toString());
		let daoQueryDetailsIs = context.daoDetailsIds.map(daoQuery => daoQuery["daoDetailsIds"]);
		$("#daoDetailsIds").val(JSON.stringify(this.daoQueryDetailsIs));
		$("#variableName").val(JSON.stringify(variableNameArray));
		$("#queryType").val(JSON.stringify(queryTypeArray));
		$("#daoQueryDetails").val(JSON.stringify(daoQueryArray));
		let formData = $("#saveUpdateQueryForm").serialize();
		$.ajax({
		    type : "POST",
		    async : false,
		    url : contextPath+"/cf/sdq",
		    data : formData,
			success : function(data){
				if(data !== ""){
					isDataSaved = true;
					$("#dynarestId").val(data);
					let versioningData =  $("#dynamicRestForm, #saveUpdateQueryForm").serialize();
					enableVersioning(versioningData);
					saveEntityRoleAssociation(data);
					showMessage("Information saved successfully", "success");
				}
				$("#saveUpdateQueryForm").remove();
			},
	        error : function(xhr, error){
				showMessage("Error occurred while saving", "error");
	        },
			
		});	
		
		return isDataSaved;
	}
	
	hideShowAllowFiles = function(){
		let dynarestProdTypeId = $("#dynarestProdTypeId").val();
        let dynarestRequestTypeId = $("#dynarestRequestTypeId").val();
        if(dynarestRequestTypeId === "1" || dynarestRequestTypeId === "4"){
            $("#allowFilesDiv").show();
        }else{
        	$("#allowFilesCheckbox").prop("checked", false);
        	$("#allowFiles").val(0);
        	$("#allowFilesDiv").hide();
        }
        showMethodSignature($("#dynarestPlatformId").val());
	}
	
	updateVariableSeq = function(){
		let sequenceNumber = 1;
		$("*[id*=inputcontainer_]").each(function(index, elem){
    		$(elem).prev().text(sequenceNumber + "] variable");
    		sequenceNumber++;
		});
	}
	
	hideErrorMessage = function(){
    	$('#errorMessage').hide();
    }
    
	backToDynarestListingPage = function() {
    	window.location = "../cf/dynl";
  	}
	
	
	
  	
}

let saveEntityRoleAssociation = function(dynaRestId){
	let roleIds =[];
	let entityRoles = new Object();
	entityRoles.entityName = $("#dynarestUrl").val();
	entityRoles.moduleId=$("#moduleId").val();
	entityRoles.entityId= dynaRestId;
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
let getEntityRoles = function(){
	$.ajax({
        async : false,
        type : "GET",
        url : contextPath+"/cf/ler", 
        data : {
        	entityId:$("#dynarestId").val(),
        	moduleId:$("#moduleId").val(),
        },
        success : function(data) {
            $.each(data, function(key,val){
            	multiselect.setSelectedObject(val);
            	
            });
	    }
    });
}


var removeByAttribute = function(arr, attr, value){
    var i = arr.length;
        while(i--){
        if( arr[i] 
            && arr[i].hasOwnProperty(attr) 
            && (arguments.length > 2 && arr[i][attr] === value ) ){ 
            arr.splice(i,1);
        }
    }
    return arr;
}