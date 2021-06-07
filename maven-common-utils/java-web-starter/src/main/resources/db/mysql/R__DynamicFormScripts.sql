SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('ba1845fd-09ac-11eb-a027-f48e38ab8cd7', 'dynamic-form-listing', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script> 
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/lang/en.js"></script>
</head>

<div class="container">
		<div class="topband">
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.formBuilder'')}</h2> 
		<div class="float-right">
            Show:<select id="typeSelect" class="typeSelectDropDown" onchange="changeType()" >   
                <option value="0">All</option>                   
                <option value="1" selected>Custom</option>                   
                <option value="2">System</option>                 
            </select>
			<#if environment == "dev">
				<input id="downloadForm" class="btn btn-primary" onclick= "downloadForm();" name="downloadForm" value="Download Forms" type="button">
				<input id="uploadForm" class="btn btn-primary" onclick= "uploadForm();" name="uploadForm" value="Upload Forms" type="button">
			</#if>
    		<a href="${(contextPath)!''''}/cf/ad"> 
				<input id="additionalDataSource" class="btn btn-primary" value="Additional Datasource" type="button">
			</a>
			<input class="btn btn-primary" name="addNewDynamicForm" value="Add Form" type="button" onclick="submitForm(this)">
    		<span onclick="backToHome();">
    	  		<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
    		</span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divDynamicFormMasterGrid"></div>

		<form action="${(contextPath)!''''}/cf/aedf" method="POST" id="addEditDynamicForm">	
			<input type="hidden" id="formId" name="form-id">	
		</form>
		<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
			<input type="hidden" id="entityName" name="entityName" value="jq_dynamic_form">
			<input type="hidden" id="entityId" name="entityId">
			<input type="hidden" id="moduleName" name="moduleName">
			<input type="hidden" id="moduleType" name="moduleType" value="dynamicForm">
			<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/dfl">
		</form>
</div>

<script>
	contextPath = "${(contextPath)!''''}";
	$(function () {
		$("#typeSelect").each(function () {
	        $(this).val($(this).find("option[selected]").val());
	    });
		let formElement = $("#addEditDynamicForm")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("dynamic-form-manage-details", formDataJson);
		
			let colM = [
				{ title: "Form Id", width: 190, dataIndx: "formId" , align: "left", halign: "center",
					filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Form Name", width: 130, dataIndx: "formName" , align: "left", halign: "center",
					filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Form Description", width: 130, dataIndx: "formDescription", align: "left", halign: "center",
					filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Created By", width: 100, dataIndx: "createdBy" ,hidden: true, align: "left", halign: "center",
					filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Last Updated Date", width: 100, dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: formatLastUpdatedDate},
				{ title: "Action", width: 50, maxWidth: 145, dataIndx: "action", align: "center", halign: "center", render: editDynamicFormFormatter, sortable: false}
			];
			let dataModel = {
        		url: contextPath+"/cf/pq-grid-data",
        		sortIndx: "lastUpdatedTs",
        		sortDir: "down", 
    		};
			let grid = $("#divDynamicFormMasterGrid").grid({
				gridId: "dynamicFormListingGrid",
				colModel: colM,
          		dataModel: dataModel,
                additionalParameters: {"cr_formTypeId":"str_1"}
			});
	});
	
	function changeType() {
        var type = $("#typeSelect").val();   
        let postData;
        if(type == 0) {
            postData = {gridId:"dynamicFormListingGrid"}
        } else {
            let typeCondition = "str_"+type;       
   
            postData = {gridId:"dynamicFormListingGrid"
                    ,"cr_formTypeId":typeCondition
                    }
        }
        
        let gridNew = $( "#divDynamicFormMasterGrid" ).pqGrid();
        gridNew.pqGrid( "option", "dataModel.postData", postData);
        gridNew.pqGrid( "refreshDataAndView" );  
    }
        
	function formType(uiObject){
		const formTypeId = uiObject.rowData.formTypeId;
		if(formTypeId === 1){
			return "Default";
		}else{
			return "System";
		}
	}
	
	function formatLastUpdatedDate(uiObject){
        const lastUpdatedTs = uiObject.rowData.lastUpdatedTs;
        return formatDate(lastUpdatedTs);
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
			url:contextPath+"/cf/ddfbi",
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
			url:contextPath+"/cf/udfbn",
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
		location.href = contextPath+"/cf/home";
	}
	<#if environment == "dev">
		function downloadForm(){
		     $.ajax({
		        url:contextPath+"/cf/ddf",
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
		        url:contextPath+"/cf/udf",
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

REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id)VALUES
 ('be46dd5b-09ac-11eb-a027-f48e38ab8cd7','dynamic-form-manage-details','<head>
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
	<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
	<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
	<script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	<script src="${(contextPath)!''''}/webjars/1.0/monaco/require.js"></script>
	<script src="${(contextPath)!''''}/webjars/1.0/monaco/min/vs/loader.js"></script>
	<script src="${(contextPath)!''''}/webjars/1.0/dynamicform/addEditDynamicForm.js"></script>
	<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script>
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
</head>

<div class="container">
	<div class="row topband">
		<div class="col-8">
			<#if (dynamicForm?api.getFormId())?? && (dynamicForm?api.getFormId())?has_content>
			    <h2 class="title-cls-name float-left">Edit Dynamic Form</h2> 
	        <#else>
	            <h2 class="title-cls-name float-left">Add Dynamic Form</h2> 
	        </#if>
	    </div>
        
       	<div class="col-4">
	        <#if (dynamicForm?api.getFormId())?? && (dynamicForm?api.getFormId())?has_content>
	        <#assign ufAttributes = {
	            "entityType": "Form Builder",
	            "entityId": "formId",
	            "entityName": "formName"
	        }>
	        <@templateWithParams "user-favorite-template" ufAttributes />
	        </#if>
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
					<input type="text" id="formName" name="formName"  placeholder="Enter Form name" onkeyup="addEdit.hideErrorMessage();" class="form-control" value="${(dynamicForm?api.getFormName())!""}" />
				</div>
			</div>
                                                                                                                        
			<div class="col-6">
				<div class="col-inner-form full-form-fields">
					<label for="formDescription" >Form Description: </label>
					<input type="text" id="formDescription" name="formDescription" placeholder="Enter Form Description" class="form-control" value="${(dynamicForm?api.getFormDescription())!""}"/>           
				</div>
			</div>
			
			<div class="col-6">
				<div class="col-inner-form full-form-fields">
	        		<label for="flammableState" style="white-space:nowrap">Datasource</label>
	        		<select id="dataSource" name="dataSourceId" class="form-control" onchange="updateDataSource()">
	        			<option id="defaultConnection" value="" data-product-name="default">Default Connection</option>
	        		</select>
           		</div>
			</div>
                
            <#if !(dynamicForm?api.getFormId())?? && !(dynamicForm?api.getFormId())?has_content>   
	            <div id="tableAutocompleteDiv" class="col-6">
					<div class="col-inner-form full-form-fields">
	                    <label for="flammableState" style="white-space:nowrap">Select Table</label>
	                    <div class="search-cover">
	                        <input class="form-control" id="tableAutocomplete" type="text">
	                    	<i class="fa fa-search" aria-hidden="true"></i>
	                    </div>
	                	<input type="hidden" id="formTemplate" name="formTemplate">
	            	</div>
				</div>
			</#if>
				

		</div>
        
    <div id="ftlParameterDiv" class="col-12 method-sign-info-div">
		<h3 class="titlename method-sign-info">
		    <i class="fa fa-lightbulb-o" aria-hidden="true"></i><label for="ftlParameter">SQL/FTL Parameters and Macros</label>
	    </h3>
		<span id="ftlParameter">loggedInUserName, loggedInUserRoleList {}, templateWithoutParams{}, templateWithParams {}, resourceBundle {}, resourceBundleWithDefault {}<span>
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
				<h3 id="saveQueryDiv" class="titlename" style="display:none"><span class="asteriskmark">*</span>Save/Update Script</h3>
				<div id = "saveScriptContainer"></div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-3">        
   				<input id="moduleId" value="30a0ff61-0ecf-11eb-94b2-f48e38ab9348" name="moduleId" type="hidden">
     	 		<@templateWithoutParams "role-autocomplete"/>
     	 	</div>
     	 </div>
     	  
		<div class="row margin-t-10">
			<div class="col-12">
				<div class="float-right">
					<div class="btn-group dropup custom-grp-btn">
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

    <input type="hidden" id="dataSourceId" value="${(dynamicForm.datasourceId)!''''}"> 
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
	let tableAutocomplete;
	
	formName = $.trim(formName);
	const addEdit = new AddEditDynamicForm();
	
  	$(function () {
	    AddEditDynamicForm.prototype.loadAddEditDynamicForm();
		if(typeof getSavedEntity !== undefined && typeof getSavedEntity === "function"){
			getSavedEntity();
		}
		
		savedAction("dynamic-form-manage-details", formId);
	    hideShowActionButtons();
	    
	     <#if !(dynamicForm.formId)?? && !(dynamicForm.formId)?has_content>
	    	getAllDatasource(0);
	    	let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
	        multiselect.setSelectedObject(defaultAdminRole);
	    	tableAutocomplete = $("#tableAutocomplete").autocomplete({
		        autocompleteId: "table-autocomplete",
		        prefetch : true,
		        render: function(item) {
		            var renderStr ="";
		            if(item.emptyMsg == undefined || item.emptyMsg === ""){
		                renderStr = "<p>"+item.tableName+"</p>";
		            }else{
		                renderStr = item.emptyMsg;    
		            }                                
		            return renderStr;
		        },
		        additionalParamaters: {},
		        requestParameters: {
		        	dbProductName: $("#dataSource").find(":selected").data("product-name"),
		        },
		        extractText: function(item) {
		            return item.tableName;
		        },
		        select: function(item) {
		            let dbProductID = $("#dataSource").find(":selected").val();
					let dbProductName = $("#dataSource").find(":selected").data("product-name");
		            $("#tableAutocomplete").blur();
		            $("#selectTable").val(item.tableName);
		            loadTableTemplate(item.tableName, dbProductID, dbProductName);
		        },     
		    });
		<#else>
			getAllDatasource(1);
			$("#formName").prop("disabled", true);
			$("#dataSource").attr("disabled", true);
	    </#if>
  	});

	function loadTableTemplate(tableName, dbProductID, dbProductName){
    	$.ajax({
        	type: "POST",
        	url: contextPath+"/cf/dfte",
         	data: {
            	tableName: tableName,
				dbProductID: dbProductID,
				dbProductName: dbProductName
          	},
          	success: function(data) {
  		    	dashletHTMLEditor.setValue(data["form-template"].trim());
  		    	dashletSAVESQLEditor.setValue(data["save-template"].trim());
  		    	dashletSQLEditor.setValue("SELECT * FROM "+ tableName);
          	}
        });
	}
   
    
</script>','aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2 );  
  
 SET FOREIGN_KEY_CHECKS=1; 