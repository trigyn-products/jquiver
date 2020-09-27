class TemplateEngine {
    constructor(templateId) {
        this.templateId = templateId;
    }

    backToTemplateListingPage = function() {
        location.href = "/cf/te";
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
			    context.validateSaveVelocity("stay");
			});
    	});
    }
    
    setTemplateValue = function (){
    	const context = this;
    	if(context.templateId != "0") {
    		$.ajax({
    		type: "get",
    		url: contextPath+"/cf/gtbi",
    		data: { templateId: this.templateId },
    		success: function(data) {
    				context.editor.setValue(data);
    			}
    		});
    	}
    }

    validateSaveVelocity = function (){
        const context = this;
        const validTemplate = this.validateTemplateName();
        if(validTemplate) {
            const templateName = $("#vmName").val();
            $.ajax({
                async : false,
                type : "POST",
                cache : false,
                url : "/cf/ctd", 
                data : {
                    templateName : templateName,
                },
                success : function(data) {
                    if(data != ""){
                        if(data == context.templateId) {
                            context.onSaveAndClose();
                        }else {
                            return false;
                        }
                    } else {
                        context.onSaveAndClose();
                    }
                }
            });
        }
    }

    validateTemplateName = function() {
        var templateName = $.trim($("#vmName").val());
        return templateName !== "";
    }

    onSaveAndClose = function() {
        const context = this;
        const velocityName = $("#vmName").val();
        let velocityTempData = context.editor.getValue();
        velocityTempData = velocityTempData.replaceAll("</textarea>", "&lt;/textarea&gt;");
        if(velocityTempData == ""){
            return false;
        }
        $.ajax({
            async : false,
            type : "POST",
            cache : false,
            url : "/cf/std", 
            data : {
                velocityId : context.templateId,
                velocityName : velocityName,
                velocityTempData : velocityTempData
            },
            success : function(data) {
                location.href = "/cf/te";
            }
        });
    }
}