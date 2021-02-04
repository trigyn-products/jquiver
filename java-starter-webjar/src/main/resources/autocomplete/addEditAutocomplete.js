class AddEditAutocomplete{

    constructor() {

    }

	loadAutocompletDetails = function(){
		const context = this;
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
    	require(["vs/editor/editor.main"], function() {
        context.sqlQuery = monaco.editor.create(document.getElementById("sqlEditor"), {
		        	value: $("#sqlContentDiv").val().trim(),
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
	        	$("#contentDiv").remove();
    	});
	}
	
	saveAutocompleteDetail = function(){
		const context = this;
		let isDataSaved = false;
		const validData = this.validateData();
        if(validData == false) {
        	return false;
        }
		const sqlEditor = context.sqlQuery.getValue();
		$("#acSelectQuery").val(context.sqlQuery.getValue().toString());
		
		const form = $("#autocompleteForm");
		let serializedForm = form.serializeArray();
		for(let iCounter =0, length = serializedForm.length;iCounter<length;iCounter++){
  			serializedForm[iCounter].value = $.trim(serializedForm[iCounter].value);
		}
		serializedForm = $.param(serializedForm);
		
		$.ajax({
			type : "POST",
			async : false,
			url :  contextPath+"/cf/sacd",
			data : serializedForm,
			success : function(data) {
				isDataSaved = true;
				context.saveEntityRoleAssociation(data);
				showMessage("Information saved successfully", "success");
	       	},
        	error : function(xhr, error){
        		showMessage("Error occurred while saving", "error");
        	},
	        	
		});
		return isDataSaved;
	}
	
	validateData = function() {
		$('#errorMessage').hide();
		let context = this;
        let autocompleteId = $.trim($("#autoId").val());
		let autocompleteQuery = $.trim(context.sqlQuery.getValue().toString());

        if(autocompleteId === ""){
        	$('#errorMessage').html("Please enter valid autocomplete id");
        	$('#errorMessage').show();
        	return false;
        }
        if(autocompleteQuery === ""){
        	$('#errorMessage').html("Please enter valid autocomplete query");
        	$('#errorMessage').show();
        	return false;
        }
        return true;
    }
	
	backToListingPage = function() {
		location.href = contextPath+"/cf/adl";
	}
	saveEntityRoleAssociation = function(autoCompleteId){
		let roleIds =[];
		let entityRoles = new Object();
		entityRoles.entityName = autoCompleteId;
		entityRoles.moduleId=$("#moduleId").val();
		entityRoles.entityId= autoCompleteId;
		 $.each($("#rolesMultiselect_selectedOptions_ul span.ml-selected-item"), function(key,val){
			 roleIds.push(val.id);
	     	
	     });
		
		entityRoles.roleIds=roleIds;
		
		$.ajax({
	        async : false,
	        type : "POST",
	        contentType : "application/json",
	        url : "/cf/ser", 
	        data : JSON.stringify(entityRoles),
	        success : function(data) {
		    }
	    });
	}
	getEntityRoles = function(){
		$.ajax({
	        async : false,
	        type : "GET",
	        url : "/cf/ler", 
	        data : {
	        	entityId:$("#autoId").val(),
	        	moduleId:$("#moduleId").val(),
	        },
	        success : function(data) {
	            $.each(data, function(key,val){
	            	multiselect.setSelectedObject(val);
	            	
	            });
		    }
	    });
	}
	
	createQuery = function(){
		const context = this;
	  	let tableName = $("#autocompleteTableSelect").find(":selected").val();
	    $.ajax({
	    	type: "POST",
	        url: contextPath+"/cf/cnbtn",
	        data: {
	        	tableName: tableName
	        },
	        success: function(data) {
	  			context.sqlQuery.setValue("SELECT "+ data[0].columnName + " FROM " + tableName);
	        }
        });
    }
    
}