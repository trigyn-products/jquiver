SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
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
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.formBuilder'')}</h2> 
		<div class="float-right">
			<#if environment == "dev">
				<input id="downloadForm" class="btn btn-primary" onclick= "downloadForm();" name="downloadForm" value="Download Forms" type="button">
				<input id="uploadForm" class="btn btn-primary" onclick= "uploadForm();" name="uploadForm" value="Upload Forms" type="button">
			</#if>
			<input class="btn btn-primary" name="addNewDynamicForm" value="Add Form" type="button" onclick="submitForm(this)">
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
		<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
			<input type="hidden" id="entityId" name="entityId">
			<input type="hidden" id="moduleName" name="moduleName">
			<input type="hidden" id="moduleType" name="moduleType" value="dynamicForm">
			<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/dfl">
		</form>
</div>

<script>
	contextPath = "${(contextPath)!''''}";
	$(function () {
		let formElement = $("#addEditDynamicForm")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("dynamic-form-manage-details", formDataJson);
		
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
	
	function formType(uiObject){
		const formTypeId = uiObject.rowData.formTypeId;
		if(formTypeId === 1){
			return "Default";
		}else{
			return "System";
		}
	}
	function editDynamicFormFormatter(uiObject) {	
		let dynamicFormId = uiObject.rowData.formId;
		let dynamicFormName = uiObject.rowData.formName;
		const revisionCount = uiObject.rowData.revisionCount;
		let actionElement;
		<#if environment == "dev">
			actionElement = "<span id=''"+dynamicFormId+"'' class= ''grid_action_icons''><i class=''fa fa-pencil''></i></span>";
      		actionElement = actionElement + "<span id=''"+dynamicFormId+"'' class= ''grid_action_icons'' onclick=''downloadFormById(this)''><i class=''fa fa-download''></i></span>";
			actionElement = actionElement + "<span id=''"+dynamicFormId+"_upload'' name=''"+dynamicFormName+"'' class= ''grid_action_icons'' onclick=''uploadFormById(this)''><i class=''fa fa-upload''></i></span>";
		<#else>
			actionElement = ''<span id="''+dynamicFormId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title=""></i></span>'';
			if(revisionCount > 1){
				actionElement = actionElement + ''<span id="''+dynamicFormId+''_entity" name="''+dynamicFormName+''" onclick="submitRevisionForm(this)" class= "grid_action_icons"><i class="fa fa-history"></i></span>''.toString();
			}else{
				actionElement = actionElement + ''<span class= "grid_action_icons disable_cls"><i class="fa fa-history"></i></span>''.toString();
			}
	
		</#if>	
		return actionElement;
	}
		
	function submitForm(thisObj){	
		$("#formId").val(thisObj.id);	
		$("#addEditDynamicForm").submit();	
	}
  
	function submitRevisionForm(sourceElement) {
		let selectedId = sourceElement.id.split("_")[0];
		let moduleName = $("#"+sourceElement.id).attr("name")
		$("#entityId").val(selectedId);
		$("#moduleName").val(moduleName);
		$("#revisionForm").submit();
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
	
	
</script>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id)VALUES ('be46dd5b-09ac-11eb-a027-f48e38ab8cd7','dynamic-form-manage-details','<head>
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
		<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
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
   		<input id="moduleId" value="30a0ff61-0ecf-11eb-94b2-f48e38ab9348" name="moduleId" type="hidden">
     	 <@templateWithoutParams "role-autocomplete"/> 
		<div class="row margin-t-10">
			<div class="col-12">
				<div class="float-right">
					<div class="btn-group dropdown custom-grp-btn">
						<div id="savedAction">
							<button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''dynamic-form-manage-details'', this, addEdit.saveDynamicForm.bind(addEdit), addEdit.backToDynamicFormListing );">${messageSource.getMessage("jws.saveAndReturn")}</button>
						</div>
						<button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
						<div class="dropdown-menu action-cls"  id="actionDiv">
							<ul class="dropdownmenu">
								<li id="saveAndCreateNew" onclick="typeOfAction(''dynamic-form-manage-details'', this, addEdit.saveDynamicForm.bind(addEdit), addEdit.backToDynamicFormListing );">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
								<li id="saveAndEdit" onclick="typeOfAction(''dynamic-form-manage-details'', this, addEdit.saveDynamicForm.bind(addEdit), addEdit.backToDynamicFormListing);">${messageSource.getMessage("jws.saveAndEdit")}</li>
							</ul>
						</div> 
					</div>
					<span onclick="addEdit.backToDynamicFormListing();">
						<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
					</span> 
				</div>
			</div>
		</div>

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
	let formName = "${(dynamicForm?api.getFormName())!''''}";
	let formId = "${(dynamicForm?api.getFormId())!''''}";
	let initialFormData;
	formName = $.trim(formName);
	if(formName !== ""){
		$("#formName").prop("disabled", true);
	}
	const addEdit = new AddEditDynamicForm();
  $(function () {
    AddEditDynamicForm.prototype.loadAddEditDynamicForm();
	savedAction("dynamic-form-manage-details", formId);
    hideShowActionButtons();
    
     <#if !(dynamicForm?api.getFormId())??>
    	let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
        multiselect.setSelectedObject(defaultAdminRole);
    
    </#if>
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
  ','aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2 );  
  
 SET FOREIGN_KEY_CHECKS=1; 