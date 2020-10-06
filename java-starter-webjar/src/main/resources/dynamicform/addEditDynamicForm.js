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
						context.addSaveQueryEditor(data[counter]);
					}
				}
			});
		} else {
			context.addSaveQueryEditor();
		}
			
		$("#saveSqlContent").remove(); 
    }
    
    backToDynamicFormListing(){
		window.location.href="./dfl"
	}
	
	addSaveQueryEditor(data){
		let formQueryId;
		let formBody;
		let formSaveQuery;
		let versionDetailsMap;
		
		if(data != undefined){
			formQueryId = data.formQueryId;
			formBody = data.formBody;
			formSaveQuery = data.formSaveQuery;
			versionDetailsMap = data.versionDetailsMap;
		}
    		
		require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
    	require(["vs/editor/editor.main"], function() {
    		
    		let index = dashletSQLEditors.length;
    		if(versionDetailsMap != undefined && $.isEmptyObject(versionDetailsMap) === false){
    			$("#saveScriptContainer").append("<div class='col-3'><div id='compareDiv_"+index+"' class='col-inner-form full-form-fields'><label for='versionId'>Compare with </label>");
    			$("#compareDiv_"+index).append("<select class='form-control' id="+formQueryId+" onchange='addEdit.getSelectTemplateData(this.id);' name='versionId' title='Template Versions'>");
    			$("#"+formQueryId).append("<option value='' selected>Select</option>");
    			for (let prop in versionDetailsMap) {
   			 		if (Object.prototype.hasOwnProperty.call(versionDetailsMap, prop)) {
        				$("#"+formQueryId).append("<option value="+prop+">"+versionDetailsMap[prop]+"</option>");
    				}
				}
    		}
    		
    		if(formBody != undefined){
    			dashletHTMLEditor.setValue(formBody);
    		}
    		
			$("#saveScriptContainer").append("<div id='container_"+index+"' class='html_script' style='margin-top: 10px;'><div class='grp_lblinp'><div id='saveSqlContainer_"+index+"' class='ace-editor-container'><div id='saveSqlEditor_"+index+"' class='ace-editor'></div></div></div></div>");
        	dashletSAVESQLEditor = monaco.editor.create(document.getElementById("saveSqlEditor_"+index), {
	        	value: formSaveQuery,
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
        	if(formQueryId != undefined){
        		$("#saveScriptContainer").append("<input type='hidden' id='formQueryId_"+index+"' value="+formQueryId+"/>");
        		formQueryIds.push(formQueryId);
        	}
    	});
    	
	}
	
	removeSaveQueryEditor(){
		let index = dashletSQLEditors.length - 1;
		if(index != 0) {
			$("#container_"+index).remove();
			$("#compareDiv_"+index).remove();
			dashletSQLEditors.pop();
			if($("#formQueryId_"+index).length == 1){
				$("#formQueryId_"+index).remove();
				formQueryIds.pop();
			}
		}
	}
    
    saveDynamicForm (){
    	let context = this;
     	let formHTMLData = dashletHTMLEditor.getValue().toString();
		$("#formSelectQuery").val(dashletSQLEditor.getValue().toString());
		$("#formBody").val(formHTMLData);
		let queries = new Array();	
		for(let iCounter = 0; iCounter < dashletSQLEditors.length; ++iCounter){
			queries.push(dashletSQLEditors[iCounter].getValue().toString());
		}
		$("#formSaveQueryId").val(JSON.stringify(formQueryIds));
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
	}
    
    saveFormData(formData){
    const context = this;
	    $.ajax({
			type : "POST",
			url : "sdfd",
			data : formData,
			 success : function(data) {
				$("#snackbar").html("Information saved successfully");
				context.showSnackbar();
			 }
																						  
		});
    }
    
    getSelectTemplateData = function(formQueryId) {
        const context = this;
		let versionId = $('#'+formQueryId).find(":selected").val();
        
        $('#diffEditor').html("");
        if(versionId != ""){
	       	const diffEditor = monaco.editor.createDiffEditor(document.getElementById("diffEditor"),{
				originalEditable: false,
	    		readOnly: false,
	       	});
	        $.ajax({
	            async : false,
	            type : "GET",
	            cache : false,
	            url : "/cf/vdfd", 
	            headers : {
	                "form-id" : formQueryId,
	                "version-id" : versionId,
	            },
	            success : function(data) {
					let modifiedContent = dashletHTMLEditor.getValue();
					let originalModel = monaco.editor.createModel(data, "text/plain");
					let modifiedModel = monaco.editor.createModel(modifiedContent, "text/plain");
					
					diffEditor.setModel({
						original: originalModel,
						modified: modifiedModel
					});
					
	            }
	        });
        }
    }
    
    showSnackbar() {
    	let snackBar = $("#snackbar");
    	snackBar.addClass('show');
    	setTimeout(function(){ 
    		snackBar.removeClass("show");
    	}, 3000);
	}
}


    
