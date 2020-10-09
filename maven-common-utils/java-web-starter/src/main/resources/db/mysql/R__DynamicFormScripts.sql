SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
('ba1845fd-09ac-11eb-a027-f48e38ab8cd7', 'dynamic-form-listing', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="/webjars/1.0/gridutils/gridutils.js"></script> 
<link rel="stylesheet" href="/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>
<div class="container">
		<div class="topband">
		<h2 class="title-cls-name float-left">Dynamic Form Master</h2> 
		<div class="float-right">
			<#if environment == "dev">
				<input id="downloadForm" class="btn btn-primary" onclick= "downloadForm();" name="downloadForm" value="Download Form" type="button">
				<input id="uploadForm" class="btn btn-primary" onclick= "uploadForm();" name="uploadForm" value="Upload Form" type="button">
			</#if>
			<input class="btn btn-primary" name="addNewDynamicForm" value="Add New Dynamic Form" type="button" onclick="submitForm(this)">
    	 <span onclick="backToHome();">
    	  	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
    	 </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divDynamicFormMasterGrid"></div>

		<form action="/cf/aedf" method="POST" id="addEditDynamicForm">	
			<input type="hidden" id="formId" name="form-id">	
		</form>
		
</div>

<script>
	contextPath = "${(contextPath)!''''}";
	$(function () {
			let colM = [
				{ title: "Form Id", width: 190, dataIndx: "formId" , align: "left", halign: "center"},
				{ title: "Form Name", width: 130, dataIndx: "formName" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Form Description", width: 130, dataIndx: "formDescription", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Created By", width: 100, dataIndx: "createdBy" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Created Date", width: 100, dataIndx: "createdDate", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Action", width: 50, dataIndx: "action", align: "center", halign: "center", render: editDynamicFormFormatter}
			];
	
			let grid = $("#divDynamicFormMasterGrid").grid({
				gridId: "dynamicFormListingGrid",
				colModel: colM
			});
	});
	
	function editDynamicFormFormatter(uiObject) {	
		let dynamicFormId = uiObject.rowData.formId;
		let dynamicFormName = uiObject.rowData.formName;
		let element;
		<#if environment == "dev">
			element = "<span id=''"+dynamicFormId+"'' class= ''grid_action_icons''><i class=''fa fa-pencil''></i></span>";
      		element = element + "<span id=''"+dynamicFormId+"'' class= ''grid_action_icons'' onclick=''downloadFormById(this)''><i class=''fa fa-download''></i></span>";
          element = element + "<span id=''"+dynamicFormId+"_upload'' name=''"+dynamicFormName+"'' class= ''grid_action_icons'' onclick=''uploadFormById(this)''><i class=''fa fa-upload''></i></span>";
		<#else>
			element = "<span id=''"+dynamicFormId+"'' class= ''grid_action_icons''  onclick=''submitForm(this)''><i class=''fa fa-pencil''></i></span>";		
		</#if>	
		return element;	
	}
		
	function submitForm(thisObj){	
		$("#formId").val(thisObj.id);	
		$("#addEditDynamicForm").submit();	
	}
  
  	function downloadFormById(thisObj){
	  	let formId = thisObj.id;
	  	$.ajax({
			url:"/cf/ddfbi",
			type:"POST",
	        data:{
	        	formId : formId,
	        }, 
			success : function(data) {
			  showMessage("Dynamic Form downloaded successfully", "success");
			},
			error : function(xhr, error){
			  showMessage("Error occurred while downloading dynamic form", "error");
			}, 
	    });  
  	}
	
	function uploadFormById(thisObj){
	  	let formId = thisObj.id;
		let formName = $("#"+formId).attr("name");
	  	$.ajax({
			url:"/cf/udfbn",
			type:"POST",
	        data:{
	        	formName : formName,
	        }, 
			success : function(data) {
			  showMessage("Dynamic Form uploaded successfully", "success");
			},
			error : function(xhr, error){
			  showMessage("Error occurred while uploading dynamic form", "error");
			}, 
	    });  
  	}
	
	function backToHome() {
		location.href = "/cf/home";
	}
	<#if environment == "dev">
		function downloadForm(){
		     $.ajax({
		        url:"/cf/ddf",
		        type:"POST",
				success : function(data) {
				  showMessage("Dynamic Forms downloaded successfully", "success");
				},
				error : function(xhr, error){
				  showMessage("Error occurred while downloading dynamic forms", "error");
				}, 
		        
		    });
		}
		function uploadForm(){
		     $.ajax({
		        url:"/cf/udf",
		        type:"POST",
		        success : function(data) {
				  showMessage("Dynamic Forms uploaded successfully", "success");
				},
				error : function(xhr, error){
				  showMessage("Error occurred while uploading dynamic forms", "error");
				},
		        
		    });
		}
	</#if>
	
	
</script>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now());

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date)VALUES ('be46dd5b-09ac-11eb-a027-f48e38ab8cd7','dynamic-form-manage-details','<head>
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
	<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
	<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
	<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	<script src="/webjars/1.0/monaco/require.js"></script>
	<script src="/webjars/1.0/monaco/min/vs/loader.js"></script>
	<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
	<script src="/webjars/1.0/dynamicform/addEditDynamicForm.js"></script>
</head>

<div class="container">
	<div class="topband">
		<#if (dynamicForm?api.getFormId())??>
		    <h2 class="title-cls-name float-left">Edit Dynamic Form</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Add Dynamic Form</h2> 
        </#if>
		<div class="float-right">				
			<span onclick="addEdit.backToDynamicFormListing();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>              
		</div>
		<div class="clearfix"></div>                         
	</div>
                              
    <form id="dynamicform" method="post" >
		<div class="row">
			<div class="col-6">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span>
					<label for="formName" >Form name: </label>
					<input type="hidden" id="formId" name="formId" value="${(dynamicForm?api.getFormId())!""}"/>
					<input type="text" id="formName" name="formName"  placeholder="Enter Form name" class="form-control" value="${(dynamicForm?api.getFormName())!""}" />
				</div>
			</div>
                                                                                                                        
			<div class="col-6">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span>
					<label for="formDescription" >Form Description: </label>
					<input type="text" id="formDescription" name="formDescription" placeholder="Enter Form Description" class="form-control" value="${(dynamicForm?api.getFormDescription())!""}"/>           
				</div>
			</div>
                
            <#if (tables)?has_content>                                            
				<div class="col-6">
					<div class="col-inner-form full-form-fields">
						<label for="formTemplate" >Form template : </label>
                        <select id="formTemplate" name="formTemplate" placeholder="Select for default template" class="form-control" onchange="loadTableTemplate(event);"/>   
							<option>Select</option>
                        	<#list tables as tableName>
								<option>${tableName}</option>
                            </#list>
						</select>
					</div>
				</div>
			</#if>
				

		</div>
                   
        <div class="row margin-t-b">  
			<div class="col-12">
				<h3 class="titlename"><span class="asteriskmark">*</span>Select Script</h3>
            	<div class="html_script">
					<div class="grp_lblinp">
	                	<div  class="ace-editor-container" id="sqlContainer">
	                		<div class= "ace-editor"  id="sqlEditor">
	                		</div>
	                	</div>
                	</div>
                </div>
			</div>
		</div>                  
                              
        <div class="row margin-t-b">
			<#if (dynamicForm?api.getFormId())?? && (dynamicForm?api.getFormId())?has_content
				&& versionDetailsMap?? && versionDetailsMap?has_content>
				<div class="col-3">
					<div class="col-inner-form full-form-fields">
						<label for="vmName">Compare with </label>
						<select class="form-control" id="${(dynamicForm?api.getFormId())!''''}" onchange="addEdit.getSelectTemplateData(''${(dynamicForm?api.getFormId())!}'');" name="versionId" title="Template Versions">
							<option value="" selected>Select</option>
							<#list versionDetailsMap as versionId, updatedDate>
								<option value="${versionId}">${updatedDate}</option>
							</#list>
						</select> 
					</div>
				</div>
			</#if>		
			<div class="col-12">
				<h3 class="titlename"><span class="asteriskmark">*</span>HTML Script</h3>
				<div class="html_script">
					<div class="grp_lblinp">
						<div id="htmlContainer" class="ace-editor-container">
							<div id="htmlEditor" class="ace-editor">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
               
             
        <div class="row margin-t-b">  
			<div class="col-12">
				<h3 class="titlename"><span class="asteriskmark">*</span>Save/Update Script</h3>
				<div id = "saveScriptContainer"></div>
			</div>
		</div>

		<div class="row margin-t-b">
			<div class="col-12">
				<div class="float-left"> 
					<div class="btn-icons nomargin-right-cls pull-right">
						<input type="button" value="Add" class="btn btn-primary" onclick="addEdit.addSaveQueryEditor();">
						<input type="button" id="removeTemplate" value="Remove" class="btn btn-secondary" onclick="addEdit.removeSaveQueryEditor();">
                    </div>    
				</div>
			</div>
		</div>          
  
		<div class="row margin-t-b">
			<div class="col-12">
				<div class="float-right"> 
					<div class="btn-icons nomargin-right-cls pull-right">
						<input type="button" value="Save" class="btn btn-primary" onclick="addEdit.saveDynamicForm();">
						<input type="button" id="cancelDynamicForm" value="Cancel" class="btn btn-secondary" onclick="addEdit.backToDynamicFormListing();">
					</div>    
				</div>
			</div>
		</div>

		<#if versionDetailsMap?? && versionDetailsMap?has_content>
			<div class="row">                                                                                                
				<div class="col-12">
					<div class="html_script">
						<div class="grp_lblinp">
							<div id="diffContainer" class="ace-editor-container">
								<div id="diffEditor" class="ace-editor"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</#if>
            <input type="hidden" name="formSelectQuery" id="formSelectQuery">
            <input type="hidden" name="formBody" id="formBody">
            <input type="hidden" name="formSaveQueryId" id="formSaveQueryId">
            <input type="hidden" name="formSaveQuery" id="formSaveQuery">                     
        </form>
    </div>


  	<textarea id="sqlContent" style="display: none">
    	${(dynamicForm?api.getFormSelectQuery())!""}
	</textarea>
	
	<textarea id="saveSqlContent" style="display: none">
    	${(dynamicForm?api.getFormSaveQuery())!""}
	</textarea>
<script>
	contextPath = "${(contextPath)!''''}";
	let dashletSQLEditor;
	let dashletHTMLEditor;
	let dashletSAVESQLEditor;
	let dashletSQLEditors = [];
	let formQueryIds = [];
	const addEdit = new AddEditDynamicForm();
  $(function () {
    AddEditDynamicForm.prototype.loadAddEditDynamicForm();
  });

  function loadTableTemplate(event){
  	let tableName = event.currentTarget.value;
      $.ajax({
          type: "POST",
          url: contextPath+"/cf/dfte",
          data: {
            tableName: event.currentTarget.value
          },
          success: function(data) {
  		      dashletHTMLEditor.setValue(data["form-template"].trim());
  		      dashletSAVESQLEditor.setValue(data["save-template"].trim());
  		      dashletSQLEditor.setValue("SELECT * FROM "+ tableName);
          }
        });
    }
    
   </script>
  ','aar.dev@trigyn.com','aar.dev@trigyn.com',now() );  
  
 SET FOREIGN_KEY_CHECKS=1; 