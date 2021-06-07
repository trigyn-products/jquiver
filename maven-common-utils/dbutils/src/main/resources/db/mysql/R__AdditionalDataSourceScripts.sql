SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id, last_updated_ts) VALUES
('a4eee08e-d5a5-4b8e-8422-69e840be7e13', 'jq-additional-datasource-form', 'jq-additional-db-type Form', 'SELECT additional_datasource_id,datasource_name,datasource_lookup_id,datasource_configuration FROM jq_additional_datasource WHERE additional_datasource_id = "${(additionalDatasourceId)!''''}"', '<head>
<link rel="stylesheet" href="${contextPath!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${contextPath!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${contextPath!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${contextPath!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${contextPath!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${contextPath!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="${contextPath!''''}/webjars/1.0/css/starter.style.css" />
        
</head>

<div class="container">
	<div class="topband">
		<#if (resultSetObject.additional_datasource_id)?? && (resultSetObject.additional_datasource_id)?has_content>
            <h2 class="title-cls-name float-left">Edit Datasoruce</h2>
        <#else>
            <h2 class="title-cls-name float-left">Add Datasource</h2> 
        </#if>
		<div class="clearfix"></div>		
	</div>
  <form method="post" name="addEditForm" id="addEditForm">
    <div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
    
    	<div class="row">
				<input type="hidden" data-type="varchar" id="additionalDatasourceId" name="additionalDatasourceId"  value="${(resultSetObject.additional_datasource_id)!""}">
				<input type="hidden" data-type="varchar" id="datasourceLookupId" name="datasourceLookupId"  value="${(resultSetObject.datasource_lookup_id)!""}" >
	    		<div class="col-4">
					<div class="col-inner-form full-form-fields">
					    <span class="asteriskmark">*</span>
						<label for="datasourcename"><@resourceBundle "jws.datasourceName" /></label>
						<input type="text" data-type="varchar" id="datasourcename" name="datasourcename" class="form-control" value="${(resultSetObject.datasource_name)!""}" >
					</div>
				</div>
	    		
	    		<div class="col-4">
					<div class="col-inner-form full-form-fields">
						<span class="asteriskmark">*</span>
						<label for="allDBDrivers"><@resourceBundle "jws.databaseProductName" /></label>
						<select class="form-control" id="allDBDrivers" onchange="onDBTypeChange()">
						</select>
					</div>
				</div>
	    		
	    		<div class="col-12">
					<div class="col-inner-form full-form-fields">
						<span class="asteriskmark">*</span>
						<label for="connectionurl"><@resourceBundle "jws.connectionURL" /></label>
						<textarea class="form-control" rows="1" cols="10"  data-type="text"  id="connectionurl" placeholder="<@resourceBundle ''jws.connectionURL'' />" name="dbconnectionurl" ></textarea>
					</div>
				</div>

                <div class="col-4">
					<div class="col-inner-form full-form-fields">
					    <span class="asteriskmark">*</span>
						<label for="username"><@resourceBundle "jws.username" /></label>
						<input type="text" data-type="varchar" id="username" name="username" class="form-control" >
					</div>
				</div>

                <div class="col-4">
					<div class="col-inner-form full-form-fields">	
					    <span class="asteriskmark">*</span>
						<label for="password"><@resourceBundle "jws.password" /></label>
						<input type="password" data-type="varchar" id="password" name="password" class="form-control" autocomplete="new-password">
						<span class="passview" onclick="showHidePassword(this);">
                            <i class="fa fa-eye" aria-hidden="true"></i>
                        </span>
					</div>
				</div>
    	</div>
    
  </form>
	<div class="row">
		<div class="col-12">
			<div class="float-right">
				<button type="button" id="testConnBtn" class="btn btn-primary" onclick="testDatabaseConnection()"><@resourceBundleWithDefault "jws.testConnection" "Test Connection" /></button>
				<div class="btn-group dropup custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''${formId}'', this);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls"  id="actionDiv">
                    	<ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="typeOfAction(''${formId}'', this);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="typeOfAction(''${formId}'', this);">${messageSource.getMessage("jws.saveAndEdit")}</li>
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
	 let isEdit = 0;
     let driverList;
  
  $(function(){

      <#if (resultSet)??>
      	<#list resultSet as resultSetList>
          let connectionDetails = JSON.parse(''${(resultSetList.datasource_configuration)!''''}'');
          $("#connectionurl").text(connectionDetails.url);
          $("#username").val(connectionDetails.userName);
          $("#password").val(connectionDetails.password); 
          $("#allDBDrivers").attr("disabled", true);  
      	</#list>
      </#if>
    
      <#if (resultSet)?? && resultSet?has_content>
      	isEdit = 1;
      </#if>
    
	savedAction(formId, isEdit);
	hideShowActionButtons();
    getDBDrivers();
  });

	function getDBDrivers(){
        let dbTypeId = $("#datasourceLookupId").val().trim();
        $.ajax({
		    type : "POST",
		    url : contextPath+"/api/jq-db-driver-list",
            success : function(data) {
                driverList = data;
                for (let property in data) {
                	if(data[property].driverClassAvailable === true){
    					$("#allDBDrivers").append("<option value="+property+">"+data[property].databaseDisplayProductName+"</option>")
    				}
				}
				if(dbTypeId !== ""){ 
					$("#allDBDrivers").val(dbTypeId);
				}else{
					$("#datasourceLookupId").val($("#allDBDrivers option:first").val());
				}
		    },
	        error : function(xhr, error){
		        showMessage("Error occurred while fetching list of available database drivers", "error");
	        },
		});
    }
    

  
  function showHidePassword(thisObj){
        var element = $(thisObj).parent().find("input[name=''password'']");
        if (element.prop("type") === "password") {
            element.prop("type","text");
            $(thisObj).find("i").removeClass("fa-eye");
            $(thisObj).find("i").addClass("fa-eye-slash");
            $("#password").focus();
        } else {
            element.prop("type","password");
            $(thisObj).find("i").removeClass("fa-eye-slash");
            $(thisObj).find("i").addClass("fa-eye");
            $("#password").focus();
        }
    }
    
	function onDBTypeChange(){ 
		let datasourceLookupId = $("#allDBDrivers").find(":selected").val();
		$("#datasourceLookupId").val(datasourceLookupId);
	}
	
	function saveData(){
        let isDataSaved = false;
		let formData = validateData();
        let connectionUrl = $("#connectionurl").val().trim();
        let selectedDBType = $("#allDBDrivers").find(":selected").val();
        let dbName = $("#allDBDrivers").find(":selected").text();
        if(driverList[selectedDBType].driverClassAvailable === false){ 
       		showMessage("No driver class found for: " + dbName , "error");
        	return false;
        }
        if(connectionUrl.includes(driverList[selectedDBType].datasourceName) === false){
        	showMessage("Invalid connection url", "error");
        	return false;
        }
        if(formData === undefined){
			$("#errorMessage").html("All fields are mandatory");
			$("#errorMessage").show();
			return false;
		}
        let additionalDatasourceId = $("#additionalDatasourceId").val().trim();
        let datasourceLookupId = $("#datasourceLookupId").val().trim();
        let userName = $("#username").val().trim();
        let password = $("#password").val().trim();
        let dataSourceName = $("#datasourcename").val().trim();
        $.ajax({
		    type : "POST",
		    async: false,
		    url : contextPath+"/api/jq-db-datasource",
            data:{
                adi: additionalDatasourceId,
                dn: dataSourceName,
                dli: datasourceLookupId,
                curl: connectionUrl,
                un: userName,
                pwd: password,
            },
            success : function(data) {
                isDataSaved = true;
			    showMessage("Information saved successfully", "success");
		    },
	        error : function(xhr, error){
		        showMessage(xhr.responseText, "error");
	        },
		});
		return isDataSaved; 
    }
	

    function validateData(){
		let serializedForm = $("#addEditForm").serializeArray();
		for(let iCounter =0, length = serializedForm.length;iCounter<length;iCounter++){
			let fieldValue = $.trim(serializedForm[iCounter].value);
			let fieldName = $.trim(serializedForm[iCounter].name);
			let isFieldVisible = $("#"+fieldName).is(":visible");
            if(fieldValue !== ""){
            	serializedForm[iCounter].value = fieldValue;
            }else if(isFieldVisible === true){
            	return undefined;
            }  
		}
		serializedForm = serializedForm.formatSerializedArray();
		return serializedForm;
    }
    
	function testDatabaseConnection(){
        let datasourceLookupId = $("#datasourceLookupId").val().trim();
        let connectionUrl = $("#connectionurl").val().trim();
        let userName = $("#username").val().trim();
        let password = $("#password").val().trim();
        
        $.ajax({
		    type : "POST",
            async: false,
		    url : contextPath+"/api/jq-test-database-connection",
		    data:{ 
		    	dli: datasourceLookupId,
		    	curl: connectionUrl,
		    	un: userName,
		    	pwd: password,
		    },
            success : function(data) {
                showMessage("Connection successful", "success");
		    },
	        error : function(xhr, error){
		        showMessage(xhr.responseText, "error"); 
	        },
		});
    }
    
	function backToPreviousPage() {
		location.href = contextPath+"/cf/ad";
	}
	
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2, NOW());


REPLACE INTO jq_dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('cc578c10-6cb9-4267-8570-8ebd40f851fd', 'a4eee08e-d5a5-4b8e-8422-69e840be7e13', 'SELECT 1', 1, NULL);
	
	
REPLACE INTO jq_grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id, created_by, created_date, last_updated_ts) VALUES
('jq-additional-datasourceGrid', 'jq-additional-datasourceGrid', 'jq-additional-datasource Listing', 'datasourceListing', 'datasourceName,databaseProductName,productdisplayname,createdBy,createdDate,lastUpdatedBy,lastUpdatedTs', 2, 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('1dbd61f0-1d5b-4f47-bec2-362e0084b382', 'jq-additional-datasource-template', '<head>
<link rel="stylesheet" href="${contextPath!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${contextPath!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${contextPath!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${contextPath!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${contextPath!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${contextPath!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${contextPath!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="${contextPath!''''}/webjars/1.0/gridutils/gridutils.js"></script> 
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/lang/en.js"></script>
<link rel="stylesheet" href="${contextPath!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${contextPath!''''}/webjars/1.0/css/starter.style.css" />
</head>

<div class="container">
    <div class="topband">
        <h2 class="title-cls-name float-left">Additional Datasource</h2> 
        <div class="float-right">
             <button type="submit" class="btn btn-primary" onclick="openAddEditScreen()"> Add Datasource </button>
            <span onclick="backToWelcomePage();">
                <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="hidden">
            </span> 
        </div>
        
        <div class="clearfix"></div>        
    </div>
        
    <div id="jq-additional-datasourceGrid"></div>

    <div id="snackbar"></div>
</div>

<script>
    contextPath = "${(contextPath)!''''}";
    let primaryKeyDetails = {"additionalDatasourceId":""};
    let dateFormat;
    
    $(function () {

        let colM = [
            	{ title: "<@resourceBundle ''jws.additionalDatasourceId'' />", hidden : true, width: 130, dataIndx: "additionalDatasourceId", align: "left", align: "left", halign: "center",
                	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
                { title: "<@resourceBundle ''jws.datasourceName'' />", hidden : false, width: 130, dataIndx: "datasourceName", align: "left", align: "left", halign: "center",
                	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            	{ title: "<@resourceBundle ''jws.datasourceLookupId'' />", hidden : true, width: 130, dataIndx: "datasourceLookupId", align: "left", align: "left", halign: "center",
                	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
                { title: "<@resourceBundle ''jws.databaseProductName'' />", hidden : false, width: 130, dataIndx: "productDisplayName", align: "left", halign: "center",
                	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
                { title: "<@resourceBundle ''jws.createdBy'' />", hidden : false, width: 130, dataIndx: "createdBy", align: "left", halign: "center",
                	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
                { title: "<@resourceBundle ''jws.updatedDate'' />", hidden : false, width: 130, dataIndx: "lastUpdatedTs", align: "left", halign: "center", render:formatCreatedDate},
                { title: "<@resourceBundle ''jws.action'' />", width: 50, maxWidth: 145, dataIndx: "action", align: "center", halign: "center", render: manageRecord, sortable: false}
        ];
    
    	let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "lastUpdatedTs",
        	sortDir: "down",
    	};

        let grid = $("#jq-additional-datasourceGrid").grid({
          gridId: "jq-additional-datasourceGrid",
          colModel: colM,
          dataModel: dataModel
        });
        

        <#list systemProperties as key, value>
            <#if key.propertyName == "jws-date-format">
                dateFormat = JSON.stringify(${value});
            </#if>
        </#list>
    
    });
    

    function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
        return ''<span id="''+rowIndx+''" onclick="createNew(this)" class= "grid_action_icons" title="<@resourceBundle''jws.edit''/>"><i class="fa fa-pencil"></i></span>''.toString();
    }

    function formatCreatedDate(uiObject){
        const lastUpdatedTs = uiObject.rowData.lastUpdatedTs;
        return formatDate(lastUpdatedTs);
    }
    
    function createNew(element) {
        let rowData = $( "#jq-additional-datasourceGrid" ).pqGrid("getRowData", {rowIndxPage: element.id});
        primaryKeyDetails["additionalDatasourceId"] = rowData["additionalDatasourceId"];
        openAddEditScreen();
    }

    function openAddEditScreen() {
    	  let formId = "a4eee08e-d5a5-4b8e-8422-69e840be7e13";
    	  openForm(formId, primaryKeyDetails);
    }

    function backToWelcomePage() {
        location.href = contextPath+"/cf/adl";
    }
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);


REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('3a66988f-5d5a-4a3a-b784-338f4f20abdc', 'jq-db-driver-list', 1, 'getAvailableDBDrivers', 'Get list of all available drivers', 1, 7, 'com.trigyn.jws.webstarter.service.DataSourceService', 1, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(138, '3a66988f-5d5a-4a3a-b784-338f4f20abdc', 'noVariable', 'SELECT 1', 1, 1);


REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('a3428fae-4ead-4e4c-875e-b66092e82079', 'jq-db-datasource', 1, 'saveDatasourceDetails', 'Save data-source Details', 1, 7, 'com.trigyn.jws.webstarter.service.DataSourceService', 1, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(141, 'a3428fae-4ead-4e4c-875e-b66092e82079', 'noVariable', 'SELECT 1', 1, 1);


REPLACE INTO jq_dynamic_rest_details(jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('b08e6d7b-e823-40f7-af0c-ac259c9c3a8a', 'jq-test-database-connection', 1, 'testDatabaseConnection', 'Get all active datasource', 1, 7, 'com.trigyn.jws.webstarter.service.DataSourceService', 1, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_dynamic_rest_dao_details(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(143, 'b08e6d7b-e823-40f7-af0c-ac259c9c3a8a', 'noVariable', 'SELECT 1', 1, 1);


REPLACE INTO jq_dynamic_rest_details(jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('b7158367-04c4-4f73-a67d-cc5cc5ab571c', 'jq-all-datasource', 1, 'getAllDataSource', 'Get All Datasource', 1, 7, 'function additionalDatsource(requestDetails, daoResults) {
    return daoResults["additionalDatsourceList"];
}

additionalDatsource(requestDetails, daoResults);', 3, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());

REPLACE INTO jq_dynamic_rest_dao_details(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(144, 'b7158367-04c4-4f73-a67d-cc5cc5ab571c', 'additionalDatsourceList', 'SELECT jad.additional_datasource_id AS additionalDatasourceId, jad.datasource_name AS datasourceName, jdl.database_product_name AS databaseProductName, ad.datasource_id AS selectedDataSource  
FROM jq_additional_datasource AS jad
INNER JOIN jq_datasource_lookup AS jdl ON jdl.datasource_lookup_id = jad.datasource_lookup_id 
LEFT OUTER JOIN jq_autocomplete_details AS ad ON ad.datasource_id = jad.additional_datasource_id 
AND ad.ac_id = "table-autocomplete"
WHERE jad.is_deleted = 0 
ORDER BY datasourceName', 1, 1);


REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('1f907bfd-38dc-47de-a745-dccc3112b85d', '3a66988f-5d5a-4a3a-b784-338f4f20abdc', 'jq-db-driver-list', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('3b08dc7b-c3d3-4b9d-b966-fe4be5bb4c5c', '3a66988f-5d5a-4a3a-b784-338f4f20abdc', 'jq-db-driver-list', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('3c3605f1-7346-4067-a0b7-5a36f02ef1d5', '3a66988f-5d5a-4a3a-b784-338f4f20abdc', 'jq-db-driver-list', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1);


REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('0c23c93b-6c47-4821-a79b-c8c3bad7354f', 'a3428fae-4ead-4e4c-875e-b66092e82079', 'jq-db-datasource', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('228de589-3e67-4ba4-9997-1095ddc70cd9', 'a3428fae-4ead-4e4c-875e-b66092e82079', 'jq-db-datasource', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('c4adb9ab-a82c-4e1b-ad57-890291c319d1', 'a3428fae-4ead-4e4c-875e-b66092e82079', 'jq-db-datasource', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('06a7b86b-9680-42ae-9028-8d916963579c', 'b08e6d7b-e823-40f7-af0c-ac259c9c3a8a', 'jq-test-database-connection', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('4a5bb34d-579a-4421-b36a-6d34aa204df1', 'b08e6d7b-e823-40f7-af0c-ac259c9c3a8a', 'jq-test-database-connection', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('bd2f8ad3-052f-4466-be30-d48ed30781b0', 'b08e6d7b-e823-40f7-af0c-ac259c9c3a8a', 'jq-test-database-connection', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('50af0fbe-b843-4a9c-9ccf-45cf42ab808f', 'b7158367-04c4-4f73-a67d-cc5cc5ab571c', 'jq-all-datasource', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348',  NOW(), 'aar.dev@trigyn.com', 1, 1),  
('58da7821-a91d-4b9a-b3dd-25d01ca7d857', 'b7158367-04c4-4f73-a67d-cc5cc5ab571c', 'jq-all-datasource', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348',  NOW(), 'aar.dev@trigyn.com', 1, 1),  
('c527f4a3-272f-4e6d-bdb7-83b916f7cea0', 'b7158367-04c4-4f73-a67d-cc5cc5ab571c', 'jq-all-datasource', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348',  NOW(), 'aar.dev@trigyn.com', 1, 1);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('075e1fcb-cf63-4530-ae0a-0aa6d7d4d94f', '1dbd61f0-1d5b-4f47-bec2-362e0084b382', 'jq-additional-datasource-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('462de01e-e971-4d15-8b74-01afd6905900', '1dbd61f0-1d5b-4f47-bec2-362e0084b382', 'jq-additional-datasource-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('83eb7efc-5fe3-4399-ba58-3764c460583d', '1dbd61f0-1d5b-4f47-bec2-362e0084b382', 'jq-additional-datasource-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1);


REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('18047d26-2ef3-4df2-a7b9-7daad15169bc', 'a4eee08e-d5a5-4b8e-8422-69e840be7e13', 'jq-additional-datasource-form', '30a0ff61-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('50f84f53-a704-4eb0-a0df-2a0722ab2b69', 'a4eee08e-d5a5-4b8e-8422-69e840be7e13', 'jq-additional-datasource-form', '30a0ff61-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('bcf6c158-aa14-4075-b148-364b0e24c2ad', 'a4eee08e-d5a5-4b8e-8422-69e840be7e13', 'jq-additional-datasource-form', '30a0ff61-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1);


REPLACE INTO jq_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('e21f365f-1ce3-11eb-953d-f48e38ab8cd7','system', 'system', 'jws-date-format', '{"js":"%d-%m-%Y %H:%m:%S", "java":"dd-MM-yyyy HH:mm:ss", "db":"%d-%m-%Y %r"}', 0, NOW(), 'admin', 1.00, 'Date Format');


SET FOREIGN_KEY_CHECKS=1;