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
    
    
}