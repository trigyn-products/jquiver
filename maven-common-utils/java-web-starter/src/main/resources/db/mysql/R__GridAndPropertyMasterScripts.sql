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
		return ''<span id="''+gridId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title=""></i></span>''.toString();
	}
	
	function submitForm(element) {
		$("#primaryId").val(element.id);
		$("#addEditGridForm").submit();
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
                <input type="hidden" name="ownerId" id="ownerId" value=""/>
                <input type="hidden" name="ownerType" id="ownerType" value=""/>
                <input type="hidden" name="propertyName" id="propertyName" value=""/>
                <button type="submit" class="btn btn-primary"> Add Property </button>
            </form>
			<span onclick="backToWelcomePage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="propertyMasterListingGrid"></div>

</div>



<script>
	contextPath = "";
	let grid;
	$(function () {
	    let formElement = $("#addEditProperty")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("property-master-form", formDataJson);
		
		let colM = [
			{ title: "Owner Id", width: 130, dataIndx: "owner_id", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Owner Type", width: 130, dataIndx: "owner_type", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Property Name", width: 130, dataIndx: "property_name", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Property Value", width: 130, dataIndx: "property_value", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Modified By", width: 130, dataIndx: "modified_by", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Comments", width: 130, dataIndx: "comments", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Action", width: 50, dataIndx: "action", align: "center", halign: "center", render: manageRecord}
		];
	
		grid = $("#propertyMasterListingGrid").grid({
	      gridId: "propertyMasterGrid",
	      colModel: colM
	  	});
	
	});
	
	function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
		return ''<span id="''+rowIndx+''" onclick="createNew(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
	}
	
	function createNew(element) {
		let rowData = $( "#propertyMasterListingGrid" ).pqGrid("getRowData", {rowIndxPage: element.id});
		$("#ownerId").val(rowData.owner_id);
		$("#ownerType").val(rowData.owner_type);
		$("#propertyName").val(rowData.property_name);
		$("#addEditProperty").submit();
	}

	function backToWelcomePage() {
        location.href = contextPath+"/cf/home";
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);

REPLACE INTO grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id) VALUES
('propertyMasterGrid', 'Property master listing', 'Property master listing grid', 'jws_property_master', '*', 1, 2);


REPLACE INTO dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id) VALUES
('8a80cb8174bf3b360174bfae9ac80006', 'property-master-form', 'Property master form', 'select * from jws_property_master where owner_id = "${ownerId}" and owner_type = "${ownerType}" and property_name = "${propertyName}"
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
		
		<!-- You can include any type of form element over here -->
		<div class="row">
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
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
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="comment" style="white-space:nowrap"><span class="asteriskmark">*</span>Comments</label>
					<input type="text" id="comment" name="comment" value="" maxlength="100" required class="form-control">
				</div>
			</div>
			
			<input type="hidden" id="primaryKey" name="primaryKey">
			<input type="hidden" id="entityName" name="entityName" value="jws_property_master">
		</div>
		<!-- Your form fields end -->
		
		
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
		let primaryKey = $("#ownerId").val() + "-" + $("#ownerType").val() + "-" + $("#propertyName").val();
        $("#primaryKey").val(primaryKey);
	    if(validateFields() == false){
	        $("#errorMessage").show();
	        return false;
	    }
	    let isDataSaved = false;
		let propertyValue = $(''#propertyValue'').val().replace(/\\\\/g, "\\\\\\\\");
        	$("#propertyValue").val(propertyValue);
		let formData = $("#addEditForm").serialize() + "&formId="+formId;
		if(edit === 1) {
		    formData = formData + "&edit="+edit;
		}
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
                || propertyValue == "" || appVersion == "" || comment == ""){
            $("#errorMessage").html("All fields are mandatory");
    		return false;
        }
        return true;
    }
	
	function backToPreviousPage() {
		location.href = contextPath+"/cf/pml"
	}
	
	$(function() {
	    <#if (resultSet)??>
        	<#list resultSet as resultSetList>
        		$("#ownerId").val(''${resultSetList?api.get("owner_id")}'');
        		$("#ownerType").val(''${resultSetList?api.get("owner_type")}'');
        		$("#propertyName").val(''${resultSetList?api.get("property_name")}'');
				<#outputformat "RTF">
					$("#propertyValue").val(''${resultSetList?api.get("property_value")}'');
				</#outputformat>
        		$("#comment").val(''${resultSetList?api.get("comments")}'');
        		$("#appVersion").val(''${resultSetList?api.get("app_version")}'');
        	</#list>
        </#if>
        
        <#if (requestDetails?api.get("ownerId")) != "">
            edit = 1;
        </#if>
        
        savedAction("property-master-form", edit);
        hideShowActionButtons();
	});
</script>
	', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2);
  
REPLACE INTO dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('8a80cb8174bf3b360174bfe666920014', '8a80cb8174bf3b360174bfae9ac80006', '<#if  (formData?api.getFirst("edit"))?has_content>
	update jws_property_master SET
   owner_id = ''${formData?api.getFirst("ownerId")}''  
  ,owner_type = ''${formData?api.getFirst("ownerType")}'' 
  ,property_name = ''${formData?api.getFirst("propertyName")}'' 
  ,property_value = ''${formData?api.getFirst("propertyValue")}'' 
  ,last_modified_date = NOW()
  ,modified_by = ''admin''
  ,app_version = ${formData?api.getFirst("appVersion")} 
  ,comments = ''${formData?api.getFirst("comment")}'' 
WHERE owner_id = ''${formData?api.getFirst("ownerId")}'' and owner_type = ''${formData?api.getFirst("ownerType")}'' and property_name = ''${formData?api.getFirst("propertyName")}'';

<#else>
REPLACE INTO jws_property_master (
   owner_type
  ,owner_id
  ,property_name
  ,property_value
  ,is_deleted
  ,last_modified_date
  ,modified_by
  ,app_version
  ,comments
) VALUES (
   ''${formData?api.getFirst("ownerId")}'' 
  ,''${formData?api.getFirst("ownerType")}''
  ,''${formData?api.getFirst("propertyName")}''
  ,''${formData?api.getFirst("propertyValue")}''
  ,0
  ,NOW()
  ,''admin''
  ,${formData?api.getFirst("appVersion")}
  ,''${formData?api.getFirst("comment")}''
);
</#if>', 1, NULL);

REPLACE INTO jws_property_master (owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('system', 'system', 'max-version-count', '10', 0, NOW(), 'admin', 1.00, 'Maximum limit to persist versioning data');

SET FOREIGN_KEY_CHECKS=1;
