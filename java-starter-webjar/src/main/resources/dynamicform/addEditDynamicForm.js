class AddEditDynamicForm {
    constructor() {
    	
    }
    
    loadAddEditDynamicForm() {
    	const context = this;
		
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
    	require(["vs/editor/editor.main"], function() {
        dashletSQLEditor = monaco.editor.create(document.getElementById("sqlEditor"), {
		        	value: $("#sqlContent").val(),
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
	        	$("#sqlContent").remove();
    	});
    	
    	require(["vs/editor/editor.main"], function() {
        dashletHTMLEditor = monaco.editor.create(document.getElementById("htmlEditor"), {
		        	value: $("#htmlContent").val(),
		            language: "html",
		            roundedSelection: false,
					scrollBeyondLastLine: false,
					readOnly: false,
					theme: "vs-dark",
					wordWrap: 'wordWrapColumn',
					wordWrapColumn: 250,
					wordWrapMinified: true,
					wrappingIndent: "indent"
	        	});
	        	$("#htmlContent").remove();
    	});

		let formId = $("#formId").val();
		
		if(formId != "") {
			$.ajax({
			type : "POST",
			url : "gfsq",
			data : {formId: formId},
			success : function(data) {
					for(let counter = 0; counter < data.length; ++counter) {
						context.addSaveQueryEditor(data[counter].formSaveQuery);
					}
				}
			});
		} else {
			context.addSaveQueryEditor("");
		}
			
		$("#saveSqlContent").remove(); 
    }
    
    backToDynamicFormListing(){
		window.location.href="./dfl"
	}
	
	addSaveQueryEditor(data){
		let index = dashletSQLEditors.length;
		$("#saveScriptContainer").append("<div id='container_"+index+"' class='html_script' style='margin-top: 10px;'><div class='grp_lblinp'><div id='saveSqlContainer_"+index+"' class='ace-editor-container'><div id='saveSqlEditor_"+index+"' class='ace-editor'></div></div></div></div>");

		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
    	require(["vs/editor/editor.main"], function() {
        dashletSAVESQLEditor = monaco.editor.create(document.getElementById("saveSqlEditor_"+index), {
		        	value: data,
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
	        	dashletSQLEditors.push(dashletSAVESQLEditor);
    	});
    	
	}
	
	removeSaveQueryEditor(){
		let index = dashletSQLEditors.length - 1;
		if(index != 0) {
			$("#container_"+index).remove();
			dashletSQLEditors.pop();
		}
	}
    
    saveDynamicForm (){
    	let context = this;
     	let formHTMLData = dashletHTMLEditor.getValue().toString();
    	formHTMLData = formHTMLData.replaceAll("</textarea>", "&lt;/textarea&gt;");
		$("#formSelectQuery").val(dashletSQLEditor.getValue().toString());
		$("#formBody").val(formHTMLData);
		let queries = new Array();	 
		for(let iCounter = 0; iCounter < dashletSQLEditors.length; ++iCounter){
			queries.push(dashletSQLEditors[iCounter].getValue().toString());
		}
		$("#formSaveQuery").val(JSON.stringify(queries));
		var formData = $("#dynamicform").serialize();
		
		 $.ajax({
             async : false,
             type : "GET",
             cache : false,
             url : "/cf/cdd", 
             data : {
                 formName : $("#formName").val(),
             },
             success : function(data) {
                 if(data != ""){
                     if(data != $("#formId").val()) {
                    	 return false;
                     }else{
                    	 AddEditDynamicForm.prototype.saveFormData(formData);
                     }
                 }else{
                	 AddEditDynamicForm.prototype.saveFormData(formData);
                 }
             }
         });
		
		
		//AddEditDynamicForm.prototype.backToDynamicFormListing();
	}
    
    saveFormData(formData){
	    $.ajax({
			type : "POST",
			url : "sdfd",
			data : formData,
			 success : function(data) {
				 AddEditDynamicForm.prototype.backToDynamicFormListing();
			 }
																						  
		});
    }
}


    
