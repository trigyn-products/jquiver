REPLACE INTO autocomplete_details (ac_id, ac_description, ac_select_query) VALUES
('dashboardListing', 'Dashboard Listing', 'SELECT dashboard_id AS targetTypeId, dashboard_name AS targetTypeName FROM dashboard 
WHERE is_deleted = 0 AND dashboard_name LIKE CONCAT("%", :searchText, "%")'), 
('dynamicForms', 'Dynamic Forms Autocomplete', 'SELECT form_id AS targetTypeId, form_name AS targetTypeName FROM dynamic_form WHERE form_name LIKE CONCAT("%", :searchText, "%")'), 
('dynarestListing', 'Autocomplete for dynamic rest', 'SELECT jws_dynamic_rest_id AS targetTypeId, jws_method_name AS targetTypeName 
FROM jws_dynamic_rest_details WHERE `jws_method_name` LIKE CONCAT("%", :searchText, "%")'), 
('templateListing', 'Template Autocomplete', 'SELECT template_id AS targetTypeId, template_name AS targetTypeName FROM template_master WHERE `template_name` LIKE CONCAT("%", :searchText, "%")');

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('55c2db62-0480-11eb-9926-e454e805e22f', 'master-creator', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script src="/webjars/1.0/mastergenerator/mastergenerator.js"></script>
</head>

<div class="container">
	<div class="topband">
		<h2 class="title-cls-name float-left">Create Master</h2> 
		<div class="float-right">
			<span onclick="backToPreviousPage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<form method="post" name="createMasterForm" id="createMasterForm">
		
		<div class="row">
			<div class="col-6">
				<div class="col-inner-form full-form-fields">
					<label for="field1" style="white-space:nowrap">Select Table</label>
						<select id="selectTable" name="selectTable" onchange="populateFields(this)" class="form-control">
							<option value="">Select</option>
                            <#if (tables)?has_content> 
                                <#list tables as tableName>
                                    <option value="${tableName}">${tableName}</option>
                                </#list>
                            </#if>
						</select>
					</label>
				</div>
			</div>
            <div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="moduleName" style="white-space:nowrap"><span class="asteriskmark">*</span>Module Name</label>
					<input type="text" id="moduleName" name="moduleName" value="" maxlength="100" class="form-control">
				</div>
			</div>
		</div>

        <div class="row">
            <div class="col-12">
                <h4>Listing</h4><hr/>
            </div>
            <div class="col-12 margin-t-b">
                <table id="listingDetailsTable">
                    <tr>
                        <th>Display</th>
                        <th>Hidden</th>
                        <th>Column Name</th>
                        <th>Display Name</th>
                    </tr>
                </table>
            </div>
            <div class="col-9">
                <div class="col-inner-form full-form-fields">
					<label for="columns" style="white-space:nowrap"><span class="asteriskmark">*</span>Columns to show in listing.</label>
					<input type="text" id="columns" name="columns" value="" maxlength="1000" class="form-control">
				</div>
            </div>
            <div class="col-3">
                <div class="col-inner-form full-form-fields">
					<label for="primaryKey" style="white-space:nowrap">
                        <span class="asteriskmark">*</span>Primary Key</label>
					<input type="text" id="primaryKey" name="primaryKey" value="" maxlength="100" class="form-control">
				</div>
            </div>
        </div>

        <div class="row">
            <div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="showInMenu" style="white-space:nowrap"><span class="asteriskmark">*</span>
                        Show In Menu
                    </label>
					<div class="onoffswitch">
                        <input type="hidden" id="isActive" name="isActive" value=""/>
                        <input type="checkbox" name="showInMenu" class="onoffswitch-checkbox" id="showInMenu" onchange="" />
                        <label class="onoffswitch-label" for="showInMenu">
                            <span class="onoffswitch-inner"></span>
                            <span class="onoffswitch-switch"></span>
                        </label>
                    </div>
				</div>
			</div>
		</div>

        <div class="row">
            <div class="col-12">
                <h4>Form</h4><hr/>
            </div>
            <div class="col-12">
                <table id="formDetailsTable">
                    <tr>
                        <th>Display</th>
                        <th>Hidden</th>
                        <th>Column Name</th>
                        <th>Display Name</th>
                    </tr>
                </table>
            </div>
        </div>

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
		
	</form>


	<div id="snackbar"></div>
</div>



<script>
	contextPath = "${contextPath}";
    let gridDetails = new Array();
    let formDetails = new Array();
    let menuDetails = new Object();
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL);
