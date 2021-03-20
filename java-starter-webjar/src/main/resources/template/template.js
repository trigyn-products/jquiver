class TemplateEngine {
    constructor(templateId) {
        this.templateId = templateId;
    }

    backToTemplateListingPage = function() {
        location.href = contextPath+"/cf/te";
    }
    
    initPage = function() {
    	const context = this;
    	require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
    	require(["vs/editor/editor.main"], function() {
        	context.editor = monaco.editor.create(document.getElementById("htmlEditor"), {
		        	value: "",
		            language: "html",
		            roundedSelection: false,
					scrollBeyondLastLine: false,
					readOnly: false,
					theme: "vs-dark",
					wordWrap: 'wordWrapColumn',
					wordWrapColumn: 120,
					wordWrapMinified: true,
					wrappingIndent: "indent"
	        	});
			context.setTemplateValue();
			context.editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, function() {
			    typeOfAction('template-manage-details', $("#savedAction").find("button"), 
			    	templateMaster.validateSaveVelocity.bind(templateMaster), templateMaster.backToTemplateListingPage);
			});
			context.editor.onDidChangeModelContent( function (){
    			$('#errorMessage').hide();
			});
    	});
    }
    
    setTemplateValue = function (){
    	const context = this;
    	if(context.templateId != "0") {
    		$.ajax({
    			type: "get",
    			url: contextPath+"/cf/gtbi",
    			data: {
    				templateId: this.templateId
    			},
    			success: function(data) {
    				context.editor.setValue(data);
    				
    				context.getEntityRoles();
    			},
    			error : function(xhr, error){
					showMessage("Error occurred while fetching template content", "error");
	        	},
    		});
    	}
    }

    validateSaveVelocity = function (){
        const context = this;
        let isDataSaved = false;
        const validTemplate = this.validateTemplate();
        if(validTemplate) {
            const templateName = $("#vmName").val();
            $.ajax({
                async : false,
                type : "POST",
                cache : false,
                url : contextPath+"/cf/ctd", 
                data : {
                    templateName : templateName,
                },
                success : function(data) {
                    if(data != ""){
                        if(data == context.templateId) {
                            isDataSaved = context.onSaveAndClose();
                        }else {
                            return false;
                        }
                    } else {
                        isDataSaved = context.onSaveAndClose();
                    }
	            },
	       		error : function(xhr, error){
					showMessage("Error occurred while saving", "error");
	        	},
            });
        }else{
        	$('#errorMessage').html("Template name and content cannot be blank");
        	$('#errorMessage').show();
        }
        return isDataSaved;
    }

    validateTemplate = function() {
    	const context = this;
        let templateName = $.trim($("#vmName").val());
        let velocityTempData = context.editor.getValue().trim(); 
        return templateName !== "" && velocityTempData !== "";
    }
    
    

    onSaveAndClose = function() {
        const context = this;
        let isDataSaved = false;
        const velocityName = $("#vmName").val().trim();
        let velocityTempData = context.editor.getValue().trim();
        $.ajax({
            async : false,
            type : "POST",
            cache : false,
            url : contextPath+"/cf/std", 
            data : {
                velocityId : context.templateId,
                velocityName : velocityName,
                velocityTempData : velocityTempData
            },
            success : function(data) {
            	context.templateId = data;
            	context.saveEntityRoleAssociation(context.templateId);
           		isDataSaved = true;
           		showMessage("Information saved successfully", "success");
		    },
	        error : function(xhr, error){
				showMessage("Error occurred while saving", "error");
	        },
        });
        return isDataSaved;
    }
    
    
	saveEntityRoleAssociation = function (templateId){
		let roleIds =[];
		let entityRoles = new Object();
		entityRoles.entityName = $("#vmName").val().trim();
		entityRoles.moduleId=$("#moduleId").val();
		entityRoles.entityId= templateId;
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
	getEntityRoles = function(){
		$.ajax({
            async : false,
            type : "GET",
            url : contextPath+"/cf/ler", 
            data : {
            	entityId:this.templateId,
            	moduleId:$("#moduleId").val(),
            },
            success : function(data) {
                $.each(data, function(key,val){
                	multiselect.setSelectedObject(val);
                	
                });
		    }
        });
	}
}