REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id, created_by, created_date, last_updated_ts) 
VALUES('dashboardListing', 'Dashboard Listing', 'SELECT dashboard_id AS targetTypeId, dashboard_name AS targetTypeName 
FROM jq_dashboard WHERE is_deleted = 0 AND dashboard_name LIKE CONCAT("%", :searchText, "%") LIMIT :startIndex, :pageSize'
, 2, 'aar.dev@trigyn.com', NOW(), NOW());

REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id, created_by, created_date, last_updated_ts)
VALUES ('dynamicForms', 'Dynamic Forms Autocomplete', 'SELECT form_id AS targetTypeId, form_name AS targetTypeName 
FROM jq_dynamic_form WHERE form_name LIKE CONCAT("%", :searchText, "%") LIMIT :startIndex, :pageSize'
, 2, 'aar.dev@trigyn.com', NOW(), NOW());

REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id, created_by, created_date, last_updated_ts) 
VALUES ('dynarestListing', 'Autocomplete for dynamic rest', 'SELECT jws_dynamic_rest_id AS targetTypeId, jws_method_name AS targetTypeName 
FROM jq_dynamic_rest_details WHERE `jws_method_name` LIKE CONCAT("%", :searchText, "%") LIMIT :startIndex, :pageSize ' 
, 2, 'aar.dev@trigyn.com', NOW(), NOW());

REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id, created_by, created_date, last_updated_ts)
VALUES('templateListing', 'Template Autocomplete', 'SELECT template_id AS targetTypeId, template_name AS targetTypeName 
FROM jq_template_master WHERE `template_name` LIKE CONCAT("%", :searchText, "%") LIMIT :startIndex, :pageSize'
, 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('55c2db62-0480-11eb-9926-e454e805e22f', 'master-creator', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/1.0/mastergenerator/mastergenerator.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script>
</head>


<div class="pg-master-generator">


<div class="container">

	<div class="cm-card">
	<div class="topband cm-card-header">
	
	
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.masterGenerator'')}</h2> 
		<div class="float-right">
			<span onclick="backToPreviousPage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
		
	<div class="cm-card-body">
	<form method="post" name="createMasterForm" id="createMasterForm">
		<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
		<div class="row">
			<div class="col-4">
				<div class="col-inner-form full-form-fields">
	        		<label for="flammableState" style="white-space:nowrap">Datasource</label>
	        		<select id="dataSource" name="dataSourceId" class="form-control" onchange="updateDataSource()">
	        			<option id="defaultConnection" value="" data-product-name="default">Default Connection</option>
	        		</select>
           		</div>
			</div>
			<div class="col-4">
                <div class="col-inner-form full-form-fields">
                    <label for="flammableState" style="white-space:nowrap">Select Table</label>
                    <div class="search-cover">
                        <input class="form-control" id="tableAutocomplete" type="text">
                    	<i class="fa fa-search" aria-hidden="true"></i>
                    </div>  
                    <input type="hidden" id="selectTable" name="selectTable">
                </div>
            </div>
            <div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="moduleName" style="white-space:nowrap"><span class="asteriskmark">*</span>Module Name</label>
					<input type="text" id="moduleName" name="moduleName" value="" maxlength="100" class="form-control">
				</div>
			</div>
		</div>

        <input type="hidden" id="columns" name="columns" value="" maxlength="1000">
        <input type="hidden" id="primaryKey" name="primaryKey" value="" maxlength="100">


        <div class="row">
            <div class="col-12">            
                <h4>Listing</h4><hr/>
            </div>
            <div class="col-12 margin-t-b">
                <table id="listingDetailsTable">
                    <tr>
                        <th><input type="checkbox" id="chkSelectAllIncludedListing" onclick="selectAllIncludedListing()"/> Included</th>
                        <th><input type="checkbox" id="chkSelectAllHiddenListing" onclick="selectAllHiddenListing()"/> Hidden</th>
                        <th>Column Name</th>
                        <th>I18N Resource Key</th>
                        <th>Display Name</th>
                    </tr>
                </table>
            </div>
            
        </div>




        <div class="cm-menutable">

        <div class="row">
            <div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="showInMenu" style="white-space:nowrap"><span class="asteriskmark">*</span>
                        Show In Menu
                    </label>
					<div class="onoffswitch">
                        <input type="hidden" id="isMenuAddActive" name="isMenuAddActive" value=""/>
                        <input type="checkbox" name="showInMenu" class="onoffswitch-checkbox" id="showInMenu" onchange="enableDisableMenuAdd()" />
                        <label class="onoffswitch-label" for="showInMenu">
                            <span class="onoffswitch-inner"></span>
                            <span class="onoffswitch-switch"></span>
                        </label>
                    </div>
				</div>
			</div>
            
		</div>
        <div class="row">
            <div class="col-4">

				<div class="col-inner-form full-form-fields">
					<label for="menuDisplayName" style="white-space:nowrap">${messageSource.getMessage("jws.moduleName")}</label>
					<input type="text"  id = "menuDisplayName" name = "menuDisplayName" value = "" maxlength="100" class="form-control">
				</div>
			</div>

            <div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="moduleURL" style="white-space:nowrap">${messageSource.getMessage("jws.moduleUrl")}</label>
					<span><label style="background: lightgrey;" class="float-right">${(urlPrefix)!''''}<label></span>
					<input type="text"  id = "moduleURL" name = "moduleURL" value = "" maxlength="200" class="form-control">
				</div>
			</div>
            <div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="parentModuleName" style="white-space:nowrap">${messageSource.getMessage("jws.parentModuleName")}</label>
					<select id="parentModuleName" name="parentModuleName" class="form-control">
						<option value="">Root</option>
						<#if (moduleListingVOList)??>
							<#list moduleListingVOList as moduleListingVO>
										<option value="${moduleListingVO?api.getModuleId()}">${moduleListingVO?api.getModuleName()!''''}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
        </div>

        <div class="row">
            <div class="col-12">
               
                <h4>Form</h4><hr/>
            </div>
            <div class="col-12 margin-t-b">
                <table id="formDetailsTable">
                    <tr>
                        <th><input type="checkbox" id="chkSelectAllIncludedForm" onclick="selectAllIncludedForm()"/> Included</th>
                        <th><input type="checkbox" id="chkSelectAllHiddenForm" onclick="selectAllHiddenForm()"/> Hidden</th>
                        <th>Column Name</th>
                        <th>I18N Resource Key</th>
                        <th>Display Name</th>
                    </tr>
                </table>
            
            </div>
        </div>
        <div class="row">
            <div class="col-4">

				<div class="col-inner-form full-form-fields">
					<label for="formDisplayName" style="white-space:nowrap">Form ${messageSource.getMessage("jws.moduleName")}</label>
					<input type="text"  id = "formDisplayName" name = "formDisplayName" value = "" maxlength="100" class="form-control">
				</div>
			</div>

            <div class="col-4">
				<div class="col-inner-form full-form-fields">
					<label for="formModuleURL" style="white-space:nowrap">Form ${messageSource.getMessage("jws.moduleUrl")}</label>
					<span><label style="background: lightgrey;" class="float-right">${(urlPrefix)!''''}<label></span>
					<input type="text"  id = "formModuleURL" name = "formModuleURL" value = "" maxlength="200" class="form-control">
				</div>
			</div>
        </div>
        </div>
	
	<input id="isDev" type="hidden" value="${(isDev)?c!''''}">
	
	<div class="row">
		<div class="col-4"> 
			<@templateWithoutParams "role-autocomplete"/>
		</div>
	</div> 
		
		
	</form>
    
    </div>
<div class="cm-card-footer">
    <div class="row margin-t-b">
			<div class="col-12">
			
				<div class="float-right">
					<input class="btn btn-primary" name="" value="Create" type="button" onclick="createMaster()">
					<span onclick="backToPreviousPage();">
						<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
					</span> 
				</div>
			
		</div>
	</div>
    </div>
             </div>
    


	<div id="snackbar"></div>
</div>
</div>
</div>



<script>
	contextPath = "${contextPath}";
    let gridDetails = new Array();
    let formDetails = new Array();
    let menuDetails = new Object();
    let dynamicFormModuleDetails = new Object();
    $("#showInMenu").prop("checked",false);
	$("#showInMenu").trigger("change");
	let tableAutocomplete;
	let resourceKeyMap = new Map();
	
	$(function() {
		getAllDatasource(0);
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
	            $("#tableAutocomplete").blur();
	            $("#selectTable").val(item.tableName);
	            populateFields(item.tableName, dbProductID);
	        }, 
			resetAutocomplete: function(autocompleteObj){
                $(".details").remove();
                resetObjects();
            },       
	    });
	    
	    let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
        multiselect.setSelectedObject(defaultAdminRole);
	});

    function selectAllIncludedListing(){
        $("#listingDetailsTable tr td:first-child input").prop(''checked'', $("#chkSelectAllIncludedListing").prop(''checked''));
        $("#listingDetailsTable tr td:first-child input").each(function(a_index, a_element){
            addRemoveToGridDetails(a_element);
        });
    }

    function selectAllHiddenListing(){
        $("#listingDetailsTable tr td:nth-child(2) input").prop(''checked'', $("#chkSelectAllHiddenListing").prop(''checked''));
        $("#listingDetailsTable tr td:nth-child(2) input").each(function(a_index, a_element){
            updateGridDetails(a_element);
        });
    }

    function selectAllIncludedForm(){
        $("#formDetailsTable tr td:first-child input").prop(''checked'', $("#chkSelectAllIncludedForm").prop(''checked''));
        $("#formDetailsTable tr td:first-child input").each(function(a_index, a_element){
            addRemoveToFormDetails(a_element);
        });  
    }

    function selectAllHiddenForm(){
        $("#formDetailsTable tr td:nth-child(2) input").prop(''checked'', $("#chkSelectAllHiddenForm").prop(''checked''));
        $("#formDetailsTable tr td:nth-child(2) input").each(function(a_index, a_element){
            updateFormDetails(a_element);
        });
    }
</script>', 'aar.dev@jquiver.io', 'aar.dev@jquiver.io', NOW(), NULL, 2);
