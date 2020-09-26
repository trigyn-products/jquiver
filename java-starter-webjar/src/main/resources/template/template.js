class TemplateEngine {
    constructor(templateId) {
        this.templateId = templateId;
    }

    backToTemplateListingPage = function() {
        location.href = "/cf/te";
    }
    
    initPage = function() {
        let htmlEditor = ace.edit("htmlEditor");
        htmlEditor.setOption("showInvisibles", false);
        htmlEditor.setTheme("ace/theme/monokai");
        htmlEditor.getSession().setMode("ace/mode/html");
    	$.ajax({
    		type: "get",
    		url: contextPath+"/cf/gtbi",
    		data: { templateId: this.templateId },
    		success: function(data) {
    			htmlEditor.getSession().setValue(data);
    			this.htmlEditor = htmlEditor;
    		}
    	});
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
        const htmlEditor = ace.edit("htmlEditor");
        let velocityTempData = htmlEditor.getSession().getValue();
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