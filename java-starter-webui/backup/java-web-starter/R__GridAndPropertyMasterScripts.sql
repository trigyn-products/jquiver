SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO  jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('26f2589f-09fa-11eb-a894-f48e38ab8cd7', 'grid-listing', '<head>
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
<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script>

</head>

<div class="container">
		<div class="topband">
		
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.gridUtils'')}</h2> 
		<div class="float-right">
		<form id="addEditGridForm" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
		Show:<select id="typeSelect" class="typeSelectDropDown" onchange="changeType()">   
                <option value="0">All</option>                   
                <option value="1" selected>Custom</option>                   
                <option value="2">System</option>                 
            </select>
	            <input type="hidden" name="formId" value="8a80cb8174bebc3c0174bec1892c0000"/>
	            <input type="hidden" name="primaryId" id="primaryId" value=""/>
	            <a href="${(contextPath)!''''}/cf/ad"> 
					<input id="additionalDataSource" class="btn btn-primary" value="Additional Datasource" type="button">
				</a>
	            <button type="submit" class="btn btn-primary">Add Grid</button>
        	</form>

         <span onclick="backToWelcomePage();">
          	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
         </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="deleteHeader"></div>
		<div id="divGridDetailsListing"></div>

<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
    <input type="hidden" id="entityName" name="entityName" value="jq_grid_details">
	<input type="hidden" id="entityId" name="entityId">
	<input type="hidden" id="moduleName" name="moduleName">
	<input type="hidden" id="moduleType" name="moduleType" value="grid">
	<input type="hidden" id="formId" name="formId" value="8a80cb8174bebc3c0174bec1892c0000">
	<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/gd">
</form>

</div>


<script>
	contextPath = "${(contextPath)!''''}";
	function backToWelcomePage() {
			location.href = contextPath+"/cf/home";
	}
	$(function () {
		$("#typeSelect").each(function () {
	        $(this).val($(this).find("option[selected]").val());
	    });
		let formElement = $("#addEditGridForm")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("grid-details-form", formDataJson);
		
		let colM = [
	        { title: "Grid Id", width: 130, align: "center", dataIndx: "gridId", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Name", width: 100, align: "center",  dataIndx: "gridName", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Description", width: 160, align: "center",hidden: true, dataIndx: "gridDesc", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Table Name", width: 200, align: "center", dataIndx: "gridTableName", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Column Names", width: 100, align: "center", dataIndx: "gridColumnName", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Last Updated Date", width: 100, align: "center", dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: formatLastUpdatedDate},
	        { title: "Action", maxWidth: 145, align: "left", render: editGridDetails, dataIndx: "action", sortable: false }
		];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "lastUpdatedTs",
        	sortDir: "down",
    	};
		let grid = $("#divGridDetailsListing").grid({
	      gridId: "gridDetailsListing",
	      colModel: colM,
          dataModel: dataModel,
          additionalParameters: {"cr_gridTypeId":"str_1"}
	  });
	  
	});
	
	function changeType() {
        var type = $("#typeSelect").val();   
        let postData;
        if(type == 0) {
            postData = {gridId:"gridDetailsListing"}
        } else {
            let typeCondition = "str_"+type;       
            postData = {
            	gridId:"gridDetailsListing" 
            	,"cr_gridTypeId":typeCondition
            }
        }
        
        let gridNew = $( "#divGridDetailsListing" ).pqGrid();
        gridNew.pqGrid( "option", "dataModel.postData", postData);
        gridNew.pqGrid( "refreshDataAndView" );  
    }
        
	function gridType(uiObject){
		const gridTypeId = uiObject.rowData.gridTypeId;
		if(gridTypeId === 1){
			return "Default";
		}else{
			return "System";
		}
	}
	
	function formatLastUpdatedDate(uiObject){
        const lastUpdatedTs = uiObject.rowData.lastUpdatedTs;
        return formatDate(lastUpdatedTs);
    }
	
	function editGridDetails(uiObject) {
		const gridId = uiObject.rowData.gridId;
		const gridName = uiObject.rowData.gridName;
		const revisionCount = uiObject.rowData.revisionCount;
		
		let actionElement;
		actionElement = ''<span id="''+gridId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title=""></i></span>'';
		if(revisionCount > 1){
			actionElement = actionElement + ''<span id="''+gridId+''_entity" name="''+gridName+''" onclick="submitRevisionForm(this)" class= "grid_action_icons"><i class="fa fa-history"></i></span>''.toString();
		}else{
			actionElement = actionElement + ''<span class= "grid_action_icons disable_cls"><i class="fa fa-history"></i></span>''.toString();
		}
            		if(uiObject.rowData.gridTypeId == 1) {
        <#if loggedInUserRoleList?? && loggedInUserRoleList?size gt 0>
        	<#list loggedInUserRoleList as loggedInUserRole>
            	<#if (loggedInUserRole == "ADMIN")>    
        			actionElement += ''<span onclick=\\''openDeletConfirmation(\"divGridDetailsListing",\"''+gridId+''\",\"07067149-098d-11eb-9a16-f48e38ab9348\")\\'' class= "grid_action_icons" title="Delete"><i class="fa fa-trash "></i></span>''.toString();
        			<#break>
        		</#if>
        	</#list>
        </#if>
        }
		return actionElement;
	}
	
	function submitForm(element) {
		$("#primaryId").val(element.id);
		$("#addEditGridForm").submit();
	}
	
	function submitRevisionForm(sourceElement) {
		let selectedId = sourceElement.id.split("_")[0];
		let moduleName = $("#"+sourceElement.id).attr("name")
      	$("#entityId").val(selectedId);
		$("#moduleName").val(moduleName);
      	$("#revisionForm").submit();
    }
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);

REPLACE INTO jq_dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id, last_updated_ts) VALUES
('8a80cb8174bebc3c0174bec1892c0000', 'grid-details-form', 'Form to add edit grid details', 'SELECT grid_id AS gridId, grid_name AS gridName, grid_description AS gridDescription, grid_table_name AS gridTableName, grid_column_names AS gridColumnName, custom_filter_criteria AS customFilterCriteria
, query_type AS queryType, datasource_id AS datasourceId FROM jq_grid_details WHERE grid_id="${primaryId}"', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script> 
<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/markdown/highlight/highlight.min.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/markdown/highlight/github.min.css" />
<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/addEditGridDetails.js"></script>
</head>

<div class="container">
	<div class="row topband">
        <div class="col-8">
			<#if (resultSet)?? && (resultSet)?has_content>
			    <h2 class="title-cls-name float-left">Edit Grid</h2> 
	        <#else>
	            <h2 class="title-cls-name float-left">Add Grid</h2> 
	        </#if>
     	</div>
        
        <div class="col-4">
	        <#if (resultSet)?? && (resultSet)?has_content>   
		        <#assign ufAttributes = {
		            "entityType": "Grid Utils",
		            "entityId": "gridId",
		            "entityName": "gridName"
		        }>
		        <@templateWithParams "user-favorite-template" ufAttributes />
		    </#if>
		 </div>
         
		<div class="clearfix"></div>		
	</div>
		
	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	<form method="post" name="addEditForm" id="addEditForm">
		
		<div class="row">
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridId" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Id</label>
					<input type="text" id="gridId" name="gridId" value="" maxlength="255" class="form-control" onkeydown="updateOldVal()" onkeyup="updateDefaultTemplate()">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridName" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Name</label>
					<input type="text" id="gridName" name="gridName" value="" maxlength="1000" class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridDescription" style="white-space:nowrap">Grid Description</label>
					<input type="text" id="gridDescription" name="gridDescription" value="" class="form-control">
				</div>
			</div>
		</div>
					
		<div class="row">	
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
		        	<label for="flammableState" style="white-space:nowrap">Datasource</label>
		        	<select id="dataSource" name="dataSourceId" class="form-control" onchange="showHideTableAutocomplete()">
		        		<option id="defaultConnection" value="">Default Connection</option>
		        	</select>
	           	</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridTableName" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Table Name</label>
					<input type="text" id="gridTableName" name="gridTableName" value="" maxlength="1000" class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridColumnName" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Column Names</label>
					<input type="text" id="gridColumnName" name="gridColumnName" value="" class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="queryType" style="white-space:nowrap">Query Type</label>
						<select id="queryType" name="queryType"  class="form-control">
							<option value="1">View/Table</option>
							<option value="2">Stored Procedure</option>
						</select>
					</label>
				</div>
			</div>
		
			<div class="col-4">   
				<input id="moduleId" value="07067149-098d-11eb-9a16-f48e38ab9348" name="moduleId"  type="hidden">
		    	<@templateWithoutParams "role-autocomplete"/>
		    </div>
		    
		    <div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="customFilterCriteria" style="white-space:nowrap">Additional Filter Criteria</label>
					<textarea id="customFilterCriteria" rows="4" cols="50" class="form-control">${(resultSetObject.customFilterCriteria)!""}</textarea>
					<input id="customFilterCriteriaHidden" name="customFilterCriteria" type="hidden">
				</div>
			</div>
    
    	</div>
    	
    	<input type="hidden" id="primaryKey" name="primaryKey">
		<input type="hidden" id="entityName" name="entityName" value="jq_grid_details">
		<input type="hidden" id="isEdit" name="isEdit" value="0">
	</form>	
	
	<div id="tabs">
        <ul>
            <li><a href="#htmlContent" data-target="htmlContent">${messageSource.getMessage("jws.htmlContent")}</a></li>
            <li><a href="#jsContent" data-target="jsContent">${messageSource.getMessage("jws.javaScriptContent")}</a></li>
        </ul>
        <div id="htmlContent">
	        <div class="cm-main-wrapper preview cm-scrollbar clearfix">
	            <div id="contentDiv">
	                <div id="htmlPreview" class="default-previews cm-scrollbar"></div>
	            </div>
	        </div>
        </div>
        <div id="jsContent">
	        <div class="cm-main-wrapper preview cm-scrollbar clearfix">
	            <div id="contentDiv">
	                <div id="jsPreview" class="default-previews cm-scrollbar"></div>
	            </div>
	        </div>
        </div>
    </div> 
    
     <div id="manual-container" class="cm-rightbar">
        <div class="row">
            <div class="col-md-3">
                <div id="previewDiv" style="display:none;">
                    <textarea id="previewContent" style="display:none;"></textarea>
                </div>
            </div>
        </div>
    </div>
    
	<input type="hidden" id="dataSourceId" value="">
	
	<br>
	<div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropup custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''grid-details-form'', this, saveData, backToPreviousPage);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls"  id="actionDiv">
                    	<ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="typeOfAction(''grid-details-form'', this, saveData,backToPreviousPage);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="typeOfAction(''grid-details-form'', this, saveData, backToPreviousPage);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                        </ul>
                    </div> 
                </div>
				<span onclick="backToPreviousPage();">
					<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
				</span> 
			</div>
		</div>
	</div>

</div>

<script>
	let formId = "${formId}";
	contextPath = "${contextPath}";
	let oldGridId = "yourGridId";
	$(function() {
		$("#tabs").tabs();
		loadDefaultTab("grid-default-template", updateDefaultTemplate);
	    <#if (resultSet)??>
    	    <#list resultSet as resultSetList>
        		$("#gridId").val(''${resultSetList?api.get("gridId")}'');
        		$("#gridName").val(''${resultSetList?api.get("gridName")}''); 
        		$("#gridDescription").val(''${resultSetList?api.get("gridDescription")}'');
        		$("#gridTableName").val(''${resultSetList?api.get("gridTableName")}'');
        		$("#gridColumnName").val(''${resultSetList?api.get("gridColumnName")}'');
        		$("#dataSourceId").val(''${(resultSetList?api.get("datasourceId"))!""}'');
        		$("#queryType option[value=''${resultSetList?api.get("queryType")}'']").attr("selected", "selected");
    	    </#list>
        </#if>
        
        let isEdit = 0;
      	<#if (resultSet)?? && resultSet?has_content>
      		isEdit = 1;
      		$("#isEdit").val(1);
      		getEntityRoles();
      	<#else>
      		let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
            multiselect.setSelectedObject(defaultAdminRole);
		</#if>      	
		
		if(typeof getSavedEntity !== undefined && typeof getSavedEntity === "function"){
			getSavedEntity();
		}
		
		getAllDatasource(isEdit);
		savedAction("grid-details-form", isEdit);
		hideShowActionButtons();
		$("a[href=''#htmlContent'']").click();
	});   

	function validateFields(){
        const gridId = $("#gridId").val().trim();
        const gridName = $("#gridName").val().trim();
        const gridTableName = $("#gridTableName").val().trim();
        const gridColumnName = $("#gridColumnName").val().trim();
        const queryType = $("#queryType").val();
        if(gridId == "" || gridName == "" ||
                gridTableName == "" || gridColumnName == ""){
            $("#errorMessage").html("All fields are mandatory");
    		return false;
        }
        return true;
    }
    
	function backToPreviousPage() {
		location.href = contextPath+"/cf/gd";
	}
	
	let saveEntityRoleAssociation =   function(gridId){
			let roleIds =[];
			let entityRoles = new Object();
			entityRoles.entityName = $("#gridName").val();
			entityRoles.moduleId=$("#moduleId").val();
			entityRoles.entityId= gridId;
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
		let getEntityRoles = function(){
			$.ajax({
		        async : false,
		        type : "GET",
		        url : contextPath+"/cf/ler", 
		        data : {
		        	entityId:$("#gridId").val(),
		        	moduleId:$("#moduleId").val(),
		        },
		        success : function(data) {
		            $.each(data, function(key,val){
		            	multiselect.setSelectedObject(val);
		            	
		            });
			    }
		    });
		}
		
		function updateDefaultTemplate(){ 
			let gridId = $("#gridId").val().trim();
			if(oldGridId === ""){ 
				oldGridId = "yourGridId";
			}
			if(gridId !== ""){
				let htmlGridId = $("pre span").filter(function() { 
					return ($(this).text() === ''"''+oldGridId+''"'') 
				});
				let jsGridId = $("pre span").filter(function() { 
					return ($(this).text() === "#"+oldGridId) 
				});
				
				$(htmlGridId).text(''"''+gridId+''"'');
				$(jsGridId).text(''"#''+gridId+''"'');
			}
			oldGridId = $("#gridId").val().trim();
		}
		
		function updateOldVal(){ 
			oldGridId = $("#gridId").val().trim();
		}
	
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2, NOW());


REPLACE INTO jq_dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('8a80cb8174bebc3c0174bee22fc60005', '8a80cb8174bebc3c0174bec1892c0000', '
<#if  (formData?api.getFirst("isEdit"))?has_content &&
(formData?api.getFirst("isEdit")) == "1">
    UPDATE jq_grid_details SET 
    	grid_name = ''${formData?api.getFirst("gridName")}'' 
  		,grid_description = ''${formData?api.getFirst("gridDescription")}''
  		,grid_table_name = ''${formData?api.getFirst("gridTableName")}'' 
  		,grid_column_names = ''${formData?api.getFirst("gridColumnName")}''
  		,query_type = ''${formData?api.getFirst("queryType")}''
  		,<#if  (formData?api.getFirst("customFilterCriteria"))?has_content>
			custom_filter_criteria =''${formData?api.getFirst("customFilterCriteria")}''
		<#else>
			custom_filter_criteria = NULL
	  	</#if>
        ,last_updated_by = :loggedInUserName
        ,last_updated_ts = NOW()
  	WHERE grid_id = ''${formData?api.getFirst("gridId")}''; 
<#else>
	INSERT INTO jq_grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, datasource_id, custom_filter_criteria, created_by, created_date, last_updated_ts)
	VALUES (
   ''${formData?api.getFirst("gridId")}'' 
  ,''${formData?api.getFirst("gridName")}''  
  ,''${formData?api.getFirst("gridDescription")}''  
  ,''${formData?api.getFirst("gridTableName")}'' 
  ,''${formData?api.getFirst("gridColumnName")}'' 
  ,''${formData?api.getFirst("queryType")}'' 
  , <#if  (formData?api.getFirst("datasourceId"))?has_content>
     ''${formData?api.getFirst("datasourceId")}''
  <#else>
     NULL
  </#if>
  ,<#if  (formData?api.getFirst("customFilterCriteria"))?has_content>
     ''${formData?api.getFirst("customFilterCriteria")}''
  <#else>
     NULL
  </#if>
  , :loggedInUserName
  , NOW()
  , NOW()
);
</#if>', 1, NULL);



REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8174bf3b360174bf78e6780003', 'property-master-listing', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script> 
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
</head>

<div class="container">
	<div class="topband">
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.applicationConfiguration'')}</h2> 
		<div class="float-right">
			<form id="addEditProperty" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="8a80cb8174bf3b360174bfae9ac80006"/>
                <input type="hidden" name="propertyMasterId" id="propertyMasterId" value=""/>
                <button type="submit" class="btn btn-primary"> Add Property </button>
            </form>

            <form id="addEditMailConfiguration" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="193d770c-1217-11eb-980f-802bf9ae2eda"/>
                <input type="hidden" name="ownerId" id="ownerId" value=""/>
                <input type="hidden" name="ownerType" id="ownerType" value=""/>
                <input type="hidden" name="propertyName" id="propertyName" value=""/>
                <button type="submit" class="btn btn-primary"> Mail Configuration</button>
            </form>
			<span onclick="backToWelcomePage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="propertyMasterListingGrid"></div>

</div>

<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
    <input type="hidden" id="entityName" name="entityName" value="jq_property_master">
	<input type="hidden" id="entityId" name="entityId">
	<input type="hidden" id="moduleName" name="moduleName">
	<input type="hidden" id="moduleType" name="moduleType" value="propertyMaster">
	<input type="hidden" id="formId" name="formId" value="8a80cb8174bf3b360174bfae9ac80006">
	<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/pml">
</form>


<script>
	contextPath = "${(contextPath)!''''}";
	let grid;
	$(function () {
	    localStorage.removeItem("imporatableData");
	    localStorage.removeItem("importedIdList");
	    let formElement = $("#addEditProperty")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("property-master-form", formDataJson);
		
		let colM = [
			{ title: "Owner Id", width: 130, dataIndx: "ownerId", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Owner Type", width: 130, dataIndx: "ownerType", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Property Name", width: 130, dataIndx: "propertyName", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Property Value", width: 130, dataIndx: "propertyValue", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Modified By", width: 130, dataIndx: "modifiedBy", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Comments", width: 130, dataIndx: "comments", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Modified Date", hidden: true, dataIndx: "lastModifiedDate", align: "left", align: "left", halign: "center"  },
			{ title: "Action", maxWidth: 145, dataIndx: "action", align: "center", halign: "center", render: manageRecord, sortable: false}
		];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "propertyName",
        	sortDir: "up",
    	};	
		grid = $("#propertyMasterListingGrid").grid({
	      gridId: "propertyMasterListingGrid",
	      colModel: colM,
          dataModel: dataModel
	  	});
	
	});
	
	function manageRecord(uiObject) {
        let propertyMasterId = uiObject.rowData.propertyMasterId;
		const propertyName = uiObject.rowData.propertyName;		
		const revisionCount = uiObject.rowData.revisionCount;
		
		let actionElement = ''<span id="''+propertyMasterId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" ></i></span>'';
		if(revisionCount > 1){
			actionElement = actionElement + ''<span id="''+propertyMasterId+''_entity" name="''+propertyName+''" onclick="submitRevisionForm(this)" class= "grid_action_icons"><i class="fa fa-history"></i></span>''.toString();
		}else{
			actionElement = actionElement + ''<span class= "grid_action_icons disable_cls"><i class="fa fa-history"></i></span>''.toString();
		}
		return actionElement;
	}
	
	function submitForm(element) {
		$("#propertyMasterId").val(element.id);
		$("#addEditProperty").submit();
	}
	
	function submitRevisionForm(sourceElement) {
		let selectedId = sourceElement.id.split("_")[0];
		let moduleName = $("#"+sourceElement.id).attr("name")
      	$("#entityId").val(selectedId);
		$("#moduleName").val(moduleName);
      	$("#revisionForm").submit();
    }

	function backToWelcomePage() {
        location.href = contextPath+"/cf/home";
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);



REPLACE INTO jq_dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id, last_updated_ts) VALUES
('8a80cb8174bf3b360174bfae9ac80006', 'property-master-form', 'Property master form', 'SELECT * FROM jq_property_master WHERE property_master_id = "${propertyMasterId}" 
', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script> 
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
</head>

<div class="container">
	<div class="topband">
		<#if (resultSet)?? && (resultSet)?has_content>
		    <h2 class="title-cls-name float-left"><@resourceBundleWithDefault "jws.editProperty" "Edit property"/></h2> 
        <#else>
            <h2 class="title-cls-name float-left"><@resourceBundleWithDefault "jws.addProperty" "Add property"/></h2> 
        </#if>
		<div class="clearfix"></div>		
	</div>
	
	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>	
	<form method="post" name="addEditForm" id="addEditForm">
		
		<div class="row">
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<input type="hidden" id="propertyMasterId" name="propertyMasterId" value="">
					<label for="ownerId" style="white-space:nowrap"><span class="asteriskmark">*</span>Owner Id</label>
					<input type="text" id="ownerId" name="ownerId" value="" required maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="ownerType" style="white-space:nowrap"><span class="asteriskmark">*</span>Owner Type</label>
					<input type="text" id="ownerType" name="ownerType" value="" required maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="appVersion" style="white-space:nowrap"><span class="asteriskmark">*</span>App Version</label>
					<input type="number" id="appVersion" name="appVersion" value="" maxlength="7" required class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="propertyName" style="white-space:nowrap"><span class="asteriskmark">*</span>Property Name</label>
					<input type="text" id="propertyName" name="propertyName" value="" required maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="propertyValue" style="white-space:nowrap"><span class="asteriskmark">*</span>Property Value</label>
					<textarea id="propertyValue" name="propertyValue" rows="4" required class="form-control">${(resultSetObject?api.get("property_value"))!""}</textarea>
				</div>
			</div>

			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="comment" style="white-space:nowrap">Comments</label>
					<textarea id="comment" name="comment" value="" rows="4" maxlength="100" class="form-control"></textarea>
				</div>
			</div>
			
			<input type="hidden" id="primaryKey" name="primaryKey">
			<input type="hidden" id="entityName" name="entityName" value="jq_property_master">
		</div>
		
		
	</form>
	<div class="row margin-t-10">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropup custom-grp-btn">
			      <div id="savedAction">
		    	  		<button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''property-master-form'', this);">${messageSource.getMessage("jws.saveAndReturn")}</button>
		     	   </div>
		        	<button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
		           	<div class="dropdown-menu action-cls"  id="actionDiv">
		               	<ul class="dropdownmenu">
		                  	<li id="saveAndCreateNew" onclick="typeOfAction(''property-master-form'', this);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
		    	            <li id="saveAndEdit" onclick="typeOfAction(''property-master-form'', this);">${messageSource.getMessage("jws.saveAndEdit")}</li>
			            </ul>
		            </div> 
	            </div>
				<span onclick="backToPreviousPage();">
					<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
				</span> 
			</div>
		</div>
	</div>
		

</div>



<script>
	contextPath = "${contextPath}";
	let formId = "${formId}";
	let edit = 0;
	
	function saveData (){
	    if(validateFields() == false){
	        $("#errorMessage").show();
	        return false;
	    }
	    let isDataSaved = false;
		let propertyValue = $(''#propertyValue'').val().replace(/\\\\/g, "\\\\\\\\");
        $("#propertyValue").val(propertyValue);
		let formData = $("#addEditForm").serialize() + "&formId="+formId;
		$.ajax({
		  type : "POST",
		  async: false,
		  url : contextPath+"/cf/sdf",
		  data : formData,
          success : function(data) {
          	isDataSaved = true;
			showMessage("Information saved successfully", "success");
			enableVersioning(formData);
		  },
	      error : function(xhr, error){
			showMessage("Error occurred while saving", "error");
	      },
     	});
     	refreshMailConfiguration();
     	return isDataSaved;
	}
	
	function refreshMailConfiguration(){
		$.ajax({
			type : "POST",
        	async: false,
        	url : contextPath+"/cf/rp",
        	data : {
            	ownerId: $("#ownerId").val(),
            	ownerType: $("#ownerType").val(),
            	propertyName: $("#propertyName").val(),
            	propertyValue: $("#propertyValue").val(),
        	},
        	success : function(data) {
	
        	},
        	error : function(xhr, error){
            	showMessage("Error occurred while updating property configuration", "error");
        	},
      	});
  	}
	
	function updatePropertyMaster(){
		$.ajax({
        	type : "GET",
            async: false,
            url : contextPath+"/cf/upml",
            success : function(data) {
            
            },error : function(xhr, error){
				showMessage("Error occurred while updating property master details", "error");
	      	},
        });  
	}
	
    function validateFields(){
        const ownerId = $("#ownerId").val().trim();
        const ownerType = $("#ownerType").val().trim();
        const propertyName = $("#propertyName").val().trim();
        const propertyValue = $("#propertyValue").val().trim();
        const appVersion = $("#appVersion").val().trim();
        const comment = $("#comment").val().trim();
        if(ownerId == "" || ownerType == "" || propertyName == "" 
                || propertyValue == "" || appVersion == "" ){
            $("#errorMessage").html("Fields marked with an * are required");
    		return false;
        }
        return true;
    }
	
	function backToPreviousPage() {
		location.href = contextPath+"/cf/pml"
	}
	
	$(function() {
	    <#if (resultSet)?? && resultSet?has_content>
        	<#list resultSet as resultSetList>
				edit = 1;
				$("#propertyMasterId").val(''${resultSetList?api.get("property_master_id")}'');
				$("#primaryKey").val(''${resultSetList?api.get("property_master_id")}'');
        		$("#ownerId").val(''${resultSetList?api.get("owner_id")}'');
        		$("#ownerType").val(''${resultSetList?api.get("owner_type")}'');
        		$("#propertyName").val(''${resultSetList?api.get("property_name")}'');
				<#outputformat "RTF">
					
				</#outputformat>
        		$("#comment").val(''${resultSetList?api.get("comments")}'');
        		$("#appVersion").val(''${resultSetList?api.get("app_version")}'');
        	</#list>
        <#else>
			const generatedPrimaryKey = uuidv4();
			let version = 0;
            <#list systemProperties as key, value>
                <#if key.propertyName == "version">
                    version = "${value}".substring(8, 13).trim();
                </#if>
            </#list>
            let arr = version.split("."); // Split the string using dot as separator
            arr.pop(); // Remove the last element

            $("#appVersion").val(arr.join("."));
			$("#propertyMasterId").val(generatedPrimaryKey);
			$("#primaryKey").val(generatedPrimaryKey);
        </#if>
        
		savedAction("property-master-form", edit);
        hideShowActionButtons();
	});
</script>
	', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2, NOW());
  
REPLACE INTO jq_dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('8a80cb8174bf3b360174bfe666920014', '8a80cb8174bf3b360174bfae9ac80006', '
REPLACE INTO jq_property_master (
   property_master_id
  ,owner_type
  ,owner_id
  ,property_name
  ,property_value
  ,is_deleted
  ,last_modified_date
  ,modified_by
  ,app_version
  ,comments
) VALUES (
  ''${formData?api.getFirst("propertyMasterId")}'' 
  ,''${formData?api.getFirst("ownerId")}'' 
  ,''${formData?api.getFirst("ownerType")}''
  ,''${formData?api.getFirst("propertyName")}''
  ,''${formData?api.getFirst("propertyValue")}''
  ,0
  ,NOW()
  ,''admin''
  ,${formData?api.getFirst("appVersion")}
  ,''${formData?api.getFirst("comment")}''
);', 1, NULL);


REPLACE INTO jq_grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id, created_by, created_date, last_updated_ts) 
VALUES ("customResourceBundleListingGrid", 'Custom DB Resource Bundle Listing', 'Custom DB Resource Bundle Listing', 'jq_customResourceBundleListingView'
,'resourceKey,languageName,resourceBundleText', 1, 2, 'aar.dev@trigyn.com', NOW(), NOW());



REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('1867d3dd-b652-11eb-9b9c-f48e38ab8cd7', 'grid-default-template', '<#if selectedTab == "htmlContent">
```HTML
<#noparse>
<!-- HTML Head -->
<head>
<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script> 
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
</head>

<!-- HTML Body-->
<div class="container">
	<div class="topband">
		<h2 class="title-cls-name float-left">Your page title here</h2> 
		<div class="float-right">
			<form id="addEditRecords" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
				<input type="hidden" name="formId" value=""/>
				<input type="hidden" name="primaryId" id="primaryId" value=""/>
				<button type="submit" class="btn btn-primary"> Create New </button>
			</form>


			<span onclick="backToWelcomePage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="yourGridId"></div>

</div>
</#noparse>
```

<#elseif selectedTab == "jsContent">
```JavaScript
<#noparse>
	contextPath = "${contextPath!''''}";
	$(function () {
	//Add all columns that needs to be displayed in the grid
		let colM = [
			{ title: "Column Name to be displayed", width: 130, dataIndx: "columnNameInQuery", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Action", width: 50, minWidth: 115, dataIndx: "action", align: "center", halign: "center", render: manageRecord, sortable: false}
		];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
    	};
	
	//System will fecth grid data based on gridId
		let grid = $("#yourGridId").grid({
	      gridId: "yourGridId",
	      colModel: colM,
          dataModel: dataModel
	  	});
	
	});
	
	//Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
	function manageRecord(uiObject) {
		let primaryId = uiObject.rowData;

		console.log(uiObject);
	}
	
	//Add logic to navigate to create new record
	function createNew(element) {
		$("#primaryId").val(element.id);
		$("#addEditRecords").submit();
	}

	//Code go back to previous page
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
</#noparse>
```
</#if>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);
 
 
REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('3035581b-b652-11eb-9b9c-f48e38ab8cd7', '1867d3dd-b652-11eb-9b9c-f48e38ab8cd7', 'grid-default-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1),  
('34bd7219-b652-11eb-9b9c-f48e38ab8cd7', '1867d3dd-b652-11eb-9b9c-f48e38ab8cd7', 'grid-default-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('388aa491-b652-11eb-9b9c-f48e38ab8cd7', '1867d3dd-b652-11eb-9b9c-f48e38ab8cd7', 'grid-default-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1);
 
SET FOREIGN_KEY_CHECKS=1;
