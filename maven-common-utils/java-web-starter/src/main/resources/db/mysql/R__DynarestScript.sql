SET FOREIGN_KEY_CHECKS=0;

 REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('8a80cb81749b028401749b062c540002', 'dynarest-details-listing', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/1.0/pqGrid/pqgrid.min.js"></script>     
<link rel="stylesheet" href="/webjars/1.0/pqGrid/pqgrid.min.css" /> 
<script src="/webjars/1.0/gridutils/gridutils.js"></script>  
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>
<div class="container">
		<div class="topband">
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.restAPIBuilder'')}</h2> 
		<div class="float-right">
			<form id="addEditDynarest" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="8a80cb81749ab40401749ac2e7360000"/>
                <input type="hidden" name="primaryId" id="primaryId" value=""/>
                <input type="hidden" name="urlPrefix" id="urlPrefix" value="${urlPrefix}"/>
                <button type="submit" class="btn btn-primary">
                        Add REST API
                </button>
            </form>
			<span onclick="backToWelcomePage();">
  		        <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
  		    </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divDynarestGrid"></table>

</div>
<script>
contextPath = "${(contextPath)!''''}";
let requestType = [{"": "All"}];
let platformValues = [{"": "All"}, {"1": "Java"}, {"2": "FTL"}, {"3": "Javascript"}];
$(function () {
		let formElement = $("#addEditDynarest")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("dynamic-rest-form", formDataJson);
  $.ajax({
			type : "GET",
			url : contextPath+"/api/dynarestDetails",
      async: false,
			success : function(data) {
				for(let counter = 0; counter < data["methodTypes"].length; ++counter) {
					let object = data["methodTypes"][counter];
          let details = new Object()
          details[object["value"]] = object["name"];
					requestType.push(details);
				}
			}
		});
    
	let colM = [
        { title: "Dynamic API Url", width: 130, align: "center", dataIndx: "jws_dynamic_rest_url", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Method Name", width: 100, align: "center",  dataIndx: "jws_method_name", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Method Description", width: 160, align: "center", dataIndx: "jws_method_description", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Method Type", width: 160, align: "center", dataIndx: "jws_request_type_id", align: "left", halign: "center", render: requestTypes,
        filter: { type: "select", condition: "equal", options : requestType, listeners: ["change"]} },
        { title: "Platform", width: 160, align: "center", dataIndx: "jws_platform_id", align: "left", halign: "center", render: platforms,
        filter: { type: "select", condition: "equal", options : platformValues, listeners: ["change"]} },
        { title: "Action", width: 30, align: "center", render: editDynarest, dataIndx: "action" }
	];
    let grid = $("#divDynarestGrid").grid({
      gridId: "dynarestGrid",
      colModel: colM
  });
});

function requestTypes(uiObject){
  let cellValue = uiObject.rowData.jws_request_type_id;
  return requestType.find(el => el[cellValue])[cellValue];
}

function platforms(uiObject){
  let cellValue = uiObject.rowData.jws_platform_id;
  return platformValues.find(el => el[cellValue])[cellValue];
}

function editDynarest(uiObject) {
	let dynarestUrl = uiObject.rowData.jws_dynamic_rest_url;
	let methodName = uiObject.rowData.jws_method_name;
	let dynarestId = uiObject.rowData.jws_dynamic_rest_id;
	let element = "<span id=''"+dynarestUrl+"'' onclick=''submitForm(this)'' class= ''grid_action_icons''><i class=''fa fa-pencil''></i></span>";
	return element;
}

function submitForm(element) {
  $("#primaryId").val(element.id);
  $("#addEditDynarest").submit();
}
	
function backToWelcomePage() {
	location.href = contextPath+"/cf/home";
}

</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL);

replace into dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum) VALUES
('8a80cb81749ab40401749ac2e7360000', 'dynamic-rest-form', 'Form to manage dynamic rest modules.', 'select jdrd.jws_dynamic_rest_id as dynarestId, jdrd.jws_dynamic_rest_url as dynarestUrl, jdrd.jws_method_name as dynarestMethodName, 
jdrd.jws_method_description as dynarestMethodDescription, jdrd.jws_platform_id as dynarestPlatformId,
jdrd.jws_request_type_id as dynarestRequestTypeId, jdrd.jws_response_producer_type_id as dynarestProdTypeId, jdrd.jws_allow_files AS allowFiles 
from jws_dynamic_rest_details as jdrd 
where jdrd.jws_dynamic_rest_url = "${primaryId}"', '<head>
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
	<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
	<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
	<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	<script src="/webjars/1.0/monaco/require.js"></script>
	<script src="/webjars/1.0/monaco/min/vs/loader.js"></script>
	<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
	<script src="/webjars/1.0/dynarest/dynarest.js"></script>
</head>

<div class="container">
	<div class="topband">
		<#if (resultSet)?? && (resultSet)?has_content>
		    <h2 class="title-cls-name float-left">Edit REST API</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Add REST API</h2> 
        </#if>
		<div class="float-right">
			<span onclick="dynarest.backToDynarestListingPage();">
  				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
  		 	</span>
		</div>

		<div class="clearfix"></div>
	</div>

	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>

	<form method="post" name="dynamicRestForm" id="dynamicRestForm">
		<input type="hidden" id="dynarestId" name="dynarestId"/>
		<div class="row">
			<div class="col-6">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="dynarestUrl">REST URL </label>
					<span><label id="urlPrefixLabel" style="background: lightgrey;" class="float-right">${requestDetails?api.get("urlPrefix")!""}<label></span>
					<span id="dynarestURLSapn">
						<input id="dynarestUrl" name= "dynarestUrl" class="dynarestUrl form-control" placeholder="REST URL" />
					</span>
				</div>
			</div>

			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="dynarestMethodName">REST Method Name</label>
					<span id="dynarestMethodNameSpan">
                              <input class="form-control" id="dynarestMethodName" onchange="updateMethodName()"  name= "dynarestMethodName" placeholder="Method Name" />
                       </span>
				</div>
			</div>

			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="dynarestMethodDescription">REST Method Description</label>
					<span id="dynarestMethodDescriptionSpan">
                              <input class="form-control" id="dynarestMethodDescription"  name= "dynarestMethodDescription" placeholder="Method Description" />
                       </span>
				</div>
			</div>

			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="dynarestRequestTypeId">HTTP Method Type  </label>
					<select class="form-control" id="dynarestRequestTypeId" name="dynarestRequestTypeId" onchange="dynarest.hideShowAllowFiles()" title="HTTP Method Type">
                         </select>
				</div>
			</div>

			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="dynarestProdTypeId">HTTP Produces Type  </label>
					<select class="form-control" id="dynarestProdTypeId" onchange="togglePlatform(this)" name="dynarestProdTypeId" title="HTTP Produces Type">
                         </select>
				</div>
			</div>
			
			<div class="col-3">
				<span class="asteriskmark">*</span><label for="dynarestPlatformId">Platform Id</label>
				<select class="form-control" id="dynarestPlatformId" onchange="showMethodSignature(this.value)" name="dynarestPlatformId" title="Platform Id">
                        <option value="2" selected="selected"> FTL </option>
                        <option value="1"> JAVA </option>
                        <option value="3"> Javascript </option>
                </select>
			</div>
			
			<div id="allowFilesDiv" class="col-3" style="display:none;">
				<div class="col-inner-form full-form-fields">
					<label  for="contextType">Allow Files</label>
					<div class="onoffswitch">
						<input type="hidden" id="allowFiles" name="allowFiles" />
						<input type="checkbox" name="allowFilesCheckbox" class="onoffswitch-checkbox" id="allowFilesCheckbox" onchange="toggleAllowFiles()" />
						<label class="onoffswitch-label" for="allowFilesCheckbox">
							<span class="onoffswitch-inner"></span>
							<span class="onoffswitch-switch"></span>
						</label>
					</div>
				</div>
			</div>
			
			</div>
			
			<div id="methodSignatureDiv" class="col-12 method-sign-info-div">
				<h3 class="titlename method-sign-info">
					<i class="fa fa-lightbulb-o" aria-hidden="true"></i><label for="methodSignature">Method Signature</label>
				</h3>
				<span id="methodSignature"><span>
			</div>

		
		<div class="row margin-t-b">
			<div class="col-12">
				<h3 class="titlename"><span class="asteriskmark">*</span>Service Logic/Package Name</h3>
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
				<h3 class="titlename"><span class="asteriskmark">*</span>DAO Queries</h3>
			<div id="saveScriptContainer">

			</div>
		</div>
</div>

	<input type="hidden" name="serviceLogic" id="serviceLogic">
	<input type="hidden" name="saveUpdateQuery" id="saveUpdateQuery">
    </form>
    
    <div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropdown custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''dynamic-rest-form'', this, dynarest.saveDynarest.bind(dynarest), dynarest.backToDynarestListingPage);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls"  id="actionDiv">
                    	<ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="typeOfAction(''dynamic-rest-form'', this, dynarest.saveDynarest.bind(dynarest), dynarest.backToDynarestListingPage);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="typeOfAction(''dynamic-rest-form'', this, dynarest.saveDynarest.bind(dynarest), dynarest.backToDynarestListingPage);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                        </ul>
                    </div> 
                </div>
				<span onclick="dynarest.backToDynarestListingPage();">
					<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
				</span> 
			</div>
		</div>
	</div>
	
</div>
<script type="text/javascript">
	let contextPath = "${contextPath}";
	let dynarest;
	let formId = "${(formId)!''''}";
    let urlPrefix = "${requestDetails?api.get("urlPrefix")!""}";
	function togglePlatform(element) {
    	if(element.value == "8" || element.value == "10") {
        	$("#dynarestPlatformId option[value=1]").prop("selected", true);
            $("#dynarestPlatformId option[value=2]").prop("disabled", true);
        } else {
            $("#dynarestPlatformId option[value=2]").prop("disabled", false);
        }
        dynarest.hideShowAllowFiles();
        showMethodSignature($("#dynarestPlatformId").val());
  	}

	function toggleAllowFiles(){
		let filesAllowed = $("#allowFilesCheckbox").prop("checked");
		if(filesAllowed === true){
			$("#allowFiles").val(1);
		}else{
			$("#allowFiles").val(0);
		}
        showMethodSignature($("#dynarestPlatformId").val());
	}
	 
  function showMethodSignature(value){
         if(value == "1"){
            $("#methodSignatureDiv").show();
            if($("#dynarestProdTypeId").val() == "8" || $("#dynarestProdTypeId").val() == "10") {
                let allowFileInput = $("#allowFilesCheckbox").prop("checked");
                $("#methodSignature").html("public FileInfo "+ $("#dynarestMethodName").val() + "(" +
                (allowFileInput == false ? "" : " Multipart[] files, ") + 
                "HttpServletRequest a_httpServletRequest, Map<String, Object> daoResultSets, UserDetailsVO userDetails) { }")
                let fileUrl = urlPrefix.replaceAll("api/", "file/api/");
                $("#urlPrefixLabel").html(fileUrl);
                $("#urlPrefixLabel").effect( "highlight", {color:"yellow"}, 3000 );
            } else {
                $("#methodSignature").html("public T " + $("#dynarestMethodName").val() + 
                "(HttpServletRequest a_httpServletRequest, Map<String, Object> daoResultSets, UserDetailsVO userDetails) { }");
                $("#urlPrefixLabel").html(urlPrefix);
                $("#urlPrefixLabel").effect( "highlight", {color:"yellow"}, 3000 );
            }
         } else {
              $("#methodSignatureDiv").hide();
         }
         
  }

  function updateMethodName() {
  	showMethodSignature($("#dynarestPlatformId").val());
  }

  $(function () {
      dynarest = new DynamicRest(formId);
	  $("#methodSignatureDiv").hide();
	  <#if (resultSet)?? && resultSet?has_content>
		<#list resultSet as resultSetList>
			<#if resultSetList?is_first>
				$("#dynarestId").val(''${(resultSetList?api.get("dynarestId"))!''''}'');
				$("#dynarestUrl").val(''${(resultSetList?api.get("dynarestUrl"))!''''}'');
				$("#dynarestMethodName").val(''${(resultSetList?api.get("dynarestMethodName"))!''''}'');
				$("#dynarestMethodDescription").val(''${(resultSetList?api.get("dynarestMethodDescription"))!''''}'');
				$("#dynarestPlatformId option[value=''${(resultSetList?api.get("dynarestPlatformId"))!''''}'']").prop("selected", "selected");
				$("#allowFiles").val(''${(resultSetList?api.get("allowFiles"))!''''}'');
                dynarest.populateDetails(''${(resultSetList?api.get("dynarestRequestTypeId"))!''''}'',
                ''${(resultSetList?api.get("dynarestProdTypeId"))!''''}'',''${(resultSetList?api.get("dynarestUrl"))!''''}'');
			</#if>
		</#list>
	<#else>
	    dynarest.populateDetails();
	</#if>
	
	let allowFiles = $("#allowFiles").val();
	if(allowFiles === "1"){
		$("#allowFilesCheckbox").prop("checked", true);
	}else{
		$("#allowFilesCheckbox").prop("checked", false);
	}
	let isEdit = 0;
    <#if (resultSet)?? && resultSet?has_content>
    	isEdit = 1;
    </#if>
	savedAction("dynamic-rest-form", isEdit);
	hideShowActionButtons();
  });
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL);

REPLACE into dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence) VALUES
('8a80cb81749dbc3d01749dc00d2b0001', '8a80cb81749ab40401749ac2e7360000', '<#if  (formData?api.getFirst("dynarestId"))?has_content>
    UPDATE jws_dynamic_rest_details SET 
        jws_dynamic_rest_url = ''${formData?api.getFirst("dynarestUrl")}''
        ,jws_method_name = ''${formData?api.getFirst("dynarestMethodName")}''
        ,jws_method_description = ''${formData?api.getFirst("dynarestMethodDescription")}''
        ,jws_request_type_id = ''${formData?api.getFirst("dynarestRequestTypeId")}''
        ,jws_response_producer_type_id = ''${formData?api.getFirst("dynarestProdTypeId")}''
        ,jws_rbac_id = 1
        ,jws_service_logic = ''${formData?api.getFirst("serviceLogic")}''
        ,jws_platform_id = ''${formData?api.getFirst("dynarestPlatformId")}''
        ,jws_allow_files = ''${formData?api.getFirst("allowFiles")}''
    WHERE jws_dynamic_rest_id = ''${formData?api.getFirst("dynarestId")}''   ;
<#else>
    INSERT INTO jws_dynamic_rest_details(jws_dynamic_rest_url, jws_method_name, jws_method_description, jws_rbac_id, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files) VALUES (
         ''${formData?api.getFirst("dynarestUrl")}''
         , ''${formData?api.getFirst("dynarestMethodName")}''
         , ''${formData?api.getFirst("dynarestMethodDescription")}''
         , 1
         , ''${formData?api.getFirst("dynarestRequestTypeId")}''
         , ''${formData?api.getFirst("dynarestProdTypeId")}''
         ,''${formData?api.getFirst("serviceLogic")}''
         , ''${formData?api.getFirst("dynarestPlatformId")}''
         , ''${formData?api.getFirst("allowFiles")}''
    );
</#if>', 1);

REPLACE INTO jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id) VALUES
(3, 'dynarestDetails', 1, 'getDynamicRestDetails', 'Method to get dynamic rest details', 2, 7, 'Map<String, Object> response = new HashMap<>();
response.put("methodTypes", dAOparameters.get("dynarestMethodType"));
response.put("producerDetails", dAOparameters.get("dynarestProducerDetails"));
response.put("dynarestDetails", dAOparameters.get("dynarestDetails"));
return response;', 1);

REPLACE INTO jws_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence) VALUES
(4, 3, 'dynarestMethodType', 'select jws_request_type_details_id as value, jws_request_type as name from jws_request_type_details', 1), 
(5, 3, 'dynarestProducerDetails', 'select jws_response_producer_type_id as value, jws_response_producer_type as name from jws_response_producer_details', 2);


REPLACE INTO jws_dynamic_rest_dao_details (jws_dao_details_id,jws_dynamic_rest_details_id,jws_result_variable_name,jws_dao_query_template,jws_query_sequence) VALUES (
6,3,"dynarestDetails" ,	'SELECT  
jdrd.jws_service_logic AS dynarestServiceLogic,jdrdd.jws_result_variable_name AS variableName,
jdrdd.jws_dao_query_template AS dynarestDaoQuery, jdrdd.jws_query_sequence AS dynarestQuerySequence ,jdrdd.jws_dao_query_type AS dynarestQueryType
FROM jws_dynamic_rest_details AS jdrd 
LEFT OUTER JOIN jws_dynamic_rest_dao_details AS jdrdd ON jdrdd.jws_dynamic_rest_details_id = jdrd.jws_dynamic_rest_id
WHERE jdrd.jws_dynamic_rest_url = "${(url)!''''}" ORDER BY dynarestQuerySequence ASC', 3);

REPLACE INTO jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id) VALUES
(4, 'defaultTemplates', 1, 'defaultTemplates', 'Method to get dynamic rest details', 2, 7, 'Map<String, Object> response = new HashMap<>();
response.put("defaultTemplates", dAOparameters.get("defaultTemplates"));
return response;', 1);

REPLACE INTO jws_dynamic_rest_dao_details (jws_dao_details_id,jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence) VALUES
(7,4, 'defaultTemplates', 'select template_name as name, template as template from template_master where template_name like ("default-%")', 1);

update jws_dynamic_rest_details SET jws_service_logic = "com.trigyn.jws.dynarest.service.DynaRest" where jws_platform_id = 1;

replace into jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id) VALUES
(1001, 'js-demo', 1, 'jsdemo', 'JS', 2, 7, 'function myFunction(requestDetails, daoResults) {
    return daoResults;
}

myFunction(requestDetails, daoResults);', 3);

replace into jws_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(15, 1001, 'values', 'select * from jws_property_master', 1, 1);

delete from jws_response_producer_details where jws_response_producer_type_id = 11;

SET FOREIGN_KEY_CHECKS=1;