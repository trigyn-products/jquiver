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
		const sqlEditor = ace.edit("sqlEditor");
		$("#acSelectQuery").val(context.sqlQuery.getValue().toString());
		const formData = $("#autocompleteForm").serialize();
		$.ajax({
			type : "POST",
			url :  contextPath+"/cf/sacd",
			data : formData,
			success : function(data) {
				$('#snackbar').html("Information saved successfully.");
				context.showSnackbarAutocomplete();
	       	},
	        
        	error : function(xhr, error){
        		$("#errorMessage").show();
        		$('#errorMessage').html("Error occurred while saving");
        	},
	        	
		});
	}
	
	
	backToListingPage = function() {
		location.href = contextPath+"/cf/adl";
	}
    
    
    showSnackbarAutocomplete = function() {
    	let snackBar = $("#snackbar");
    	snackBar.addClass('show');
    	setTimeout(function(){ 
    		snackBar.removeClass("show");
    	}, 3000);
	}
	    
}