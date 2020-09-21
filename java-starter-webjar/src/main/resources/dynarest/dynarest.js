class DynamicRest {

	constructor() {
    	this.saveUpdateEditors = new Array();
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
						context.addSaveQueryEditor(dynarestDetailsArray[counter].dynarestDaoQuery);
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
    	this.serviceLogicContent = ace.edit("htmlEditor");
		this.serviceLogicContent.setTheme("ace/theme/monokai");
		this.serviceLogicContent.setOption("showInvisibles", false);
		this.serviceLogicContent.getSession().setMode("ace/mode/java");
		this.serviceLogicContent.getSession().setValue(serviceLogicContent);
    }
    
    addSaveQueryEditor = function(saveQueryContent){
		let index = this.saveUpdateEditors.length;
		$("#saveScriptContainer").append("<input id='inputcontainer_"+index+"' type ='text' class='form-control' /><div id='container_"+index+"' class='html_script' style='margin-top: 10px;'><div class='grp_lblinp'><div id='saveSqlContainer_"+index+"' class='ace-editor-container'><div id='saveSqlEditor_"+index+"' class='ace-editor'></div></div></div></div>");
		let saveUpdateEditor = ace.edit("saveSqlEditor_"+index);
		saveUpdateEditor.setTheme("ace/theme/monokai");
		saveUpdateEditor.setOption("showInvisibles", false);
		saveUpdateEditor.getSession().setMode("ace/mode/sql");
		saveUpdateEditor.getSession().setValue(saveQueryContent);
		this.saveUpdateEditors.push(saveUpdateEditor);
	}
	
	removeSaveQueryEditor = function(){
		let index = this.saveUpdateEditors.length - 1;
		if(index != 0){
			$("#container_"+index).remove();
			$("#inputcontainer_"+index).remove();
			this.saveUpdateEditors.pop();
		}
	}
	
	saveDynarest = function(formId){
		let context = this;
		let validData = context.validateDyanrestFields();
		if(validData === false){
			$('#errorMessage').show();
			return false;
		}
		$("#serviceLogic").val(this.serviceLogicContent.getSession().getValue().toString());
		let formData = $("#dynamicRestForm").serialize() + "&formId="+formId;
		
		$.ajax({
		    type : "POST",
		    url : "sdf",
			data : formData, 
			success : function(data){
				if(data === true){
					context.saveDAOQueries();
				}
			}
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
			$("#dynarestUrl").focus();
			$('#errorMessage').html("Method name cannot be blank");
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
	
	saveDAOQueries = function(){
		let saveUpdateQueries = new Object();
		let saveEditorLength = this.saveUpdateEditors.length;
		for(let iCounter = 0; iCounter < saveEditorLength; ++iCounter){
			saveUpdateQueries['variableName'] = $('#inputcontainer_'+iCounter).val();
			saveUpdateQueries['daoQuery'] = (this.saveUpdateEditors[iCounter].getSession().getValue().toString());
		}
		$.ajax({
		    type : "POST",
			success : function(data){
				if(data === true){
					context.saveDAOQueries();
				}
			}
		});	
	}
	
	
	backToDynarestListingPage = function() {
    	window.location = "../cf/dynl";
  	}
	
	
}