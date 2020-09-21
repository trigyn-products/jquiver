class AddEditDynamicForm {
    constructor() {
    	
    }
    
    loadAddEditDynamicForm() {
    	const context = this;
    	dashletSQLEditor = ace.edit("sqlEditor");
		dashletSQLEditor.setTheme("ace/theme/monokai");
		dashletSQLEditor.setOption("showInvisibles", false);
		dashletSQLEditor.getSession().setMode("ace/mode/sql");
		dashletSQLEditor.getSession().setValue($("#sqlContent").val());

		dashletHTMLEditor = ace.edit("htmlEditor");
		dashletHTMLEditor.setOption("showInvisibles", false);
		dashletHTMLEditor.setTheme("ace/theme/monokai");
		dashletHTMLEditor.getSession().setMode("ace/mode/html");
		dashletHTMLEditor.getSession().setValue($("#htmlContent").val());
		
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
			
		$("#htmlContent").remove();
		$("#sqlContent").remove();   
		$("#saveSqlContent").remove(); 
    	
    	
    }
    
    backToDynamicFormListing(){
		window.location.href="./dfl"
	}
	
	addSaveQueryEditor(data){
		let index = dashletSQLEditors.length;
		$("#saveScriptContainer").append("<div id='container_"+index+"' class='html_script' style='margin-top: 10px;'><div class='grp_lblinp'><div id='saveSqlContainer_"+index+"' class='ace-editor-container'><div id='saveSqlEditor_"+index+"' class='ace-editor'></div></div></div></div>");
		dashletSAVESQLEditor = ace.edit("saveSqlEditor_"+index);
		dashletSAVESQLEditor.setTheme("ace/theme/monokai");
		dashletSAVESQLEditor.setOption("showInvisibles", false);
		dashletSAVESQLEditor.getSession().setMode("ace/mode/sql");
		dashletSAVESQLEditor.getSession().setValue(data);
		dashletSQLEditors.push(dashletSAVESQLEditor);
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
     	let formHTMLData = dashletHTMLEditor.getSession().getValue().toString();
    	formHTMLData = formHTMLData.replaceAll("</textarea>", "&lt;/textarea&gt;");
		$("#formSelectQuery").val(dashletSQLEditor.getSession().getValue().toString());
		$("#formBody").val(formHTMLData);
		let queries = new Array();	 
		for(let iCounter = 0; iCounter < dashletSQLEditors.length; ++iCounter){
			queries.push(dashletSQLEditors[iCounter].getSession().getValue().toString());
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


    
