SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO  template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('26f2589f-09fa-11eb-a894-f48e38ab8cd7', 'grid-listing', '<head>
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
		
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.gridUtils'')}</h2> 
		<div class="float-right">
		<form id="addEditGridForm" action="/cf/df" method="post" class="margin-r-5 pull-left">
	            <input type="hidden" name="formId" value="8a80cb8174bebc3c0174bec1892c0000"/>
	            <input type="hidden" name="primaryId" id="primaryId" value=""/>
	            <button type="submit" class="btn btn-primary">Add Grid Details</button>
        	</form>

         <span onclick="backToWelcomePage();">
          	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
         </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divGridDetailsListing"></div>

<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
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
		let formElement = $("#addEditGridForm")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("grid-details-form", formDataJson);
		
		let colM = [
	        { title: "Grid Id", width: 130, align: "center", dataIndx: "gridId", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Name", width: 100, align: "center",  dataIndx: "gridName", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Description", width: 160, align: "center", dataIndx: "gridDesc", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Table Name", width: 200, align: "center", dataIndx: "gridTableName", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Column Names", width: 100, align: "center", dataIndx: "gridColumnName", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Action", width: 50, align: "center", render: editGridDetails, dataIndx: "action" }
		];
		let grid = $("#divGridDetailsListing").grid({
	      gridId: "gridDetailsListing",
	      colModel: colM
	  });
	  
	});
	
	function gridType(uiObject){
		const gridTypeId = uiObject.rowData.gridTypeId;
		if(gridTypeId === 1){
			return "Default";
		}else{
			return "System";
		}
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

REPLACE INTO dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id) VALUES
('8a80cb8174bebc3c0174bec1892c0000', 'grid-details-form', 'Form to add edit grid details', 'SELECT grid_id AS gridId, grid_name AS gridName, grid_description AS gridDescription, grid_table_name AS gridTableName , grid_column_names AS gridColumnName
, query_type AS queryType FROM grid_details WHERE grid_id="${primaryId}"', '<head>
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
		<#if (resultSet)?? && (resultSet)?has_content>
		    <h2 class="title-cls-name float-left">Edit Grid Details</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Add Grid Details</h2> 
        </#if> 
		<div class="clearfix"></div>		
	</div>
		
	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	<form method="post" name="addEditForm" id="addEditForm">
		
		<div class="row">
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridId" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Id</label>
					<input type="text" id="gridId" name="gridId" value="" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridName" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Name</label>
					<input type="text" id="gridName" name="gridName" value="" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridDescription" style="white-space:nowrap">Grid Description</label>
					<input type="text" id="gridDescription" name="gridDescription" value="" maxlength="100" class="form-control">
				</div>
			</div>
		</div>
					
		<div class="row">	
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridTableName" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Table Name</label>
					<input type="text" id="gridTableName" name="gridTableName" value="" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="gridColumnName" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Column Names</label>
					<input type="text" id="gridColumnName" name="gridColumnName" value="" maxlength="100" class="form-control">
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
		</div>
		
		<input type="hidden" id="primaryKey" name="primaryKey">
		<input type="hidden" id="entityName" name="entityName" value="grid_details">
		<!-- Your form fields end -->
		
	</form>	
	<input id="moduleId" value="07067149-098d-11eb-9a16-f48e38ab9348" name="moduleId"  type="hidden">
      <@templateWithoutParams "role-autocomplete"/> 
	
	<div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropdown custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''grid-details-form'', this);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls"  id="actionDiv">
                    	<ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="typeOfAction(''grid-details-form'', this);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="typeOfAction(''grid-details-form'', this);">${messageSource.getMessage("jws.saveAndEdit")}</li>
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
	$(function() {
	    <#if (resultSet)??>
    	    <#list resultSet as resultSetList>
        		$("#gridId").val(''${resultSetList?api.get("gridId")}'');
        		$("#gridName").val(''${resultSetList?api.get("gridName")}''); 
        		$("#gridDescription").val(''${resultSetList?api.get("gridDescription")}'');
        		$("#gridTableName").val(''${resultSetList?api.get("gridTableName")}'');
        		$("#gridColumnName").val(''${resultSetList?api.get("gridColumnName")}'');
        		$("#queryType option[value=''${resultSetList?api.get("queryType")}'']").attr("selected", "selected");
    	    </#list>
        </#if>
        
        let isEdit = 0;
      	<#if (resultSet)?? && resultSet?has_content>
      		isEdit = 1;
      		getEntityRoles();
      	<#else>
      		let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
            multiselect.setSelectedObject(defaultAdminRole);
		</#if>      	
      	
		savedAction("grid-details-form", isEdit);
		hideShowActionButtons();
	});   

	function saveData (){
		let primaryKey = $("#gridId").val();
        $("#primaryKey").val(primaryKey);
	    if(validateFields() == false){
	        $("#errorMessage").show();
	        return false;
	    }
	    let isDataSaved = false;
		let formData = $("#addEditForm").serialize()+ "&formId="+formId;
		$.ajax({
		  type : "POST",
		  url : contextPath+"/cf/sdf",
		  async: false,
		  data : formData,
          success : function(data) {
          	isDataSaved = true;
          	saveEntityRoleAssociation($("#gridId").val());
          	enableVersioning(formData);
			showMessage("Information saved successfully", "success");
		  },
	      error : function(xhr, error){
			showMessage("Error occurred while saving", "error");
	      },
		});
		return isDataSaved;
	}
	
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
		        url : "/cf/ser", 
		        data : JSON.stringify(entityRoles),
		        success : function(data) {
			    }
		    });
		}
		let getEntityRoles = function(){
			$.ajax({
		        async : false,
		        type : "GET",
		        url : "/cf/ler", 
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
	
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2);


REPLACE INTO dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('8a80cb8174bebc3c0174bee22fc60005', '8a80cb8174bebc3c0174bec1892c0000', 'REPLACE INTO grid_details (
   grid_id
  ,grid_name
  ,grid_description
  ,grid_table_name
  ,grid_column_names
  ,query_type
) VALUES (
   ''${formData?api.getFirst("gridId")}'' 
  ,''${formData?api.getFirst("gridName")}''  
  ,''${formData?api.getFirst("gridDescription")}''  
  ,''${formData?api.getFirst("gridTableName")}'' 
  ,''${formData?api.getFirst("gridColumnName")}'' 
  ,''${formData?api.getFirst("queryType")}'' 
);', 1, NULL);



DROP PROCEDURE IF EXISTS propertyMasterListing;
CREATE PROCEDURE propertyMasterListing(ownerType VARCHAR(100), ownerId VARCHAR(150)
, propertyName VARCHAR(150), propertyValue VARCHAR(250), modifiedBy VARCHAR(50), appVersion DECIMAL(7,4), comments TEXT
,forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' SELECT jpm.owner_type AS ownerType, jpm.owner_id AS ownerId, jpm.property_name AS propertyName '; 
  SET @resultQuery = CONCAT(@resultQuery, ', jpm.property_value AS propertyValue, jpm.last_modified_date AS lastModifiedDate ');
  SET @resultQuery = CONCAT(@resultQuery, ', jpm.modified_by AS modifiedBy, jpm.app_version AS appVersion, jpm.comments AS comments, jpm.property_master_id AS propertyMasterId, COUNT(jmv.version_id) AS revisionCount ');

  SET @fromString  = ' FROM jws_property_master AS jpm ';
  SET @fromString = CONCAT(@fromString, " LEFT OUTER JOIN jws_module_version AS jmv ON jmv.entity_id = jpm.property_master_id ");
  SET @whereString = ' WHERE jpm.is_deleted = 0 ';
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  SET @groupByString = ' GROUP BY propertyMasterId ';
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY lastModifiedDate DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString, @orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;


REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) 
VALUES ("propertyMasterListingGrid", 'Property Master Listing', 'Property Master Listing', 'propertyMasterListing'
,'ownerType,ownerId,propertyName,propertyValue,modifiedBy,appVersion,comments', 2);



REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8174bf3b360174bf78e6780003', 'property-master-listing', '<head>
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
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.applicationConfiguration'')}</h2> 
		<div class="float-right">
			<form id="addEditProperty" action="/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="8a80cb8174bf3b360174bfae9ac80006"/>
                <input type="hidden" name="propertyMasterId" id="propertyMasterId" value=""/>
                <button type="submit" class="btn btn-primary"> Add Property </button>
            </form>
            <form id="addEditMailConfiguration" action="/cf/df" method="post" class="margin-r-5 pull-left">
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
    <input type="hidden" id="entityId" name="entityId">
	<input type="hidden" id="moduleName" name="moduleName">
	<input type="hidden" id="moduleType" name="moduleType" value="propertyMaster">
	<input type="hidden" id="formId" name="formId" value="8a80cb8174bf3b360174bfae9ac80006">
	<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/pml">
</form>


<script>
	contextPath = "";
	let grid;
	$(function () {
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
			{ title: "Action", width: 50, dataIndx: "action", align: "center", halign: "center", render: manageRecord}
		];
	
		grid = $("#propertyMasterListingGrid").grid({
	      gridId: "propertyMasterListingGrid",
	      colModel: colM
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



REPLACE INTO dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id) VALUES
('8a80cb8174bf3b360174bfae9ac80006', 'property-master-form', 'Property master form', 'SELECT * FROM jws_property_master WHERE property_master_id = "${propertyMasterId}" 
', '<head>
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
		<#if (resultSet)?? && (resultSet)?has_content>
		    <h2 class="title-cls-name float-left">Edit property master</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Add property master</h2> 
        </#if>
		<div class="clearfix"></div>		
	</div>
	
	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>	
	<form method="post" name="addEditForm" id="addEditForm">
		
		<div class="row">
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<input type="hidden" id="propertyMasterId" name="propertyMasterId" value="">
					<label for="ownerId" style="white-space:nowrap"><span class="asteriskmark">*</span>Owner Id</label>
					<input type="text" id="ownerId" name="ownerId" value="" required maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="ownerType" style="white-space:nowrap"><span class="asteriskmark">*</span>Owner Type</label>
					<input type="text" id="ownerType" name="ownerType" value="" required maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="propertyName" style="white-space:nowrap"><span class="asteriskmark">*</span>Property Name</label>
					<input type="text" id="propertyName" name="propertyName" value="" required maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="propertyValue" style="white-space:nowrap"><span class="asteriskmark">*</span>Property Value</label>
					<input type="text" id="propertyValue" name="propertyValue" value="" maxlength="100" required class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="appVersion" style="white-space:nowrap"><span class="asteriskmark">*</span>App Version</label>
					<input type="number" id="appVersion" name="appVersion" value="" maxlength="7" required class="form-control">
				</div>
			</div>
			
			<div class="col-6">
				<div class="col-inner-form full-form-fields">
					<label for="comment" style="white-space:nowrap">Comments</label>
					<textarea id="comment" name="comment" value="" maxlength="100" class="form-control"></textarea>
				</div>
			</div>
			
			<input type="hidden" id="primaryKey" name="primaryKey">
			<input type="hidden" id="entityName" name="entityName" value="jws_property_master">
		</div>
		
		
	</form>
	<div class="row margin-t-10">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropdown custom-grp-btn">
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
     	return isDataSaved;
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
					$("#propertyValue").val(''${resultSetList?api.get("property_value")}'');
				</#outputformat>
        		$("#comment").val(''${resultSetList?api.get("comments")}'');
        		$("#appVersion").val(''${resultSetList?api.get("app_version")}'');
        	</#list>
        <#else>
			const generatedPrimaryKey = uuidv4();
			$("#propertyMasterId").val(generatedPrimaryKey);
			$("#primaryKey").val(generatedPrimaryKey);
        </#if>
        
		savedAction("property-master-form", edit);
        hideShowActionButtons();
	});
</script>
	', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2);
  
REPLACE INTO dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('8a80cb8174bf3b360174bfe666920014', '8a80cb8174bf3b360174bfae9ac80006', '
REPLACE INTO jws_property_master (
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

SET FOREIGN_KEY_CHECKS=1;
