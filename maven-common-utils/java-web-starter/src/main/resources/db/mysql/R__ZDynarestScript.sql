SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb81749b028401749b062c540002', 'dynarest-details-listing', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.min.css" />
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
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.restAPIBuilder'')}</h2> 
		<div class="float-right">
			<form id="addEditDynarest" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
                Show:<select id="typeSelect" class="typeSelectDropDown" onchange="changeType()" >   
                        <option value="0">All</option>                   
                        <option value="1" selected>Custom</option>                   
                        <option value="2">System</option>                 
                    </select>
                <input type="hidden" name="formId" value="8a80cb81749ab40401749ac2e7360000"/>
                <input type="hidden" name="primaryId" id="primaryId" value=""/>
                <input type="hidden" name="urlPrefix" id="urlPrefix" value="${urlPrefix}"/>
                <a href="${(contextPath)!''''}/cf/ad"> 
					<input id="additionalDataSource" class="btn btn-primary" value="Additional Datasource" type="button">
				</a>
                <a href="${(contextPath)!''''}/cf/acd"> 
                    <input id="apiClientDetails" class="btn btn-primary" value="API Clients" type="button">
                </a>
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

<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
	<input type="hidden" id="entityName" name="entityName" value="jq_dynamic_rest_details">
    <input type="hidden" id="entityId" name="entityId">
	<input type="hidden" id="moduleName" name="moduleName">
	<input type="hidden" id="moduleType" name="moduleType" value="dynarest">
	<input type="hidden" id="formId" name="formId" value="8a80cb81749ab40401749ac2e7360000">
	<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/dynl">
</form>
<script>
contextPath = "${(contextPath)!''''}";
let requestType = [{"": "All"}];
let platformValues = [{"": "All"}, {"1": "Java"}, {"2": "FTL"}, {"3": "Javascript"}];
$(function () {
		$("#typeSelect").each(function () {
	        $(this).val($(this).find("option[selected]").val());
	    });
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
        { title: "Dynamic API Url", width: 160, align: "center", dataIndx: "dynarestUrl", align: "left", halign: "center",
        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Method Name", width: 160, align: "center",  dataIndx: "methodName", align: "left", halign: "center",
        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Method Description", width: 160, align: "center",hidden: true, dataIndx: "methodDescription", align: "left", halign: "center",
        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Method Type", width: 100, align: "center", dataIndx: "requestTypeId", align: "left", halign: "center", render: requestTypes,
        	filter: { type: "select", condition: "equal", options : requestType, listeners: ["change"]} },
        { title: "Platform", width: 130, align: "center", dataIndx: "platformId", align: "left", halign: "center", render: platforms,
        	filter: { type: "select", condition: "equal", options : platformValues, listeners: ["change"]} },
        { title: "Last Updated Date", width: 130, align: "center", dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: formatLastUpdatedDate},
        { title: "Action", maxWidth: 145, align: "center", render: editDynarest, dataIndx: "action", sortable: false }
	];
	let dataModel = {
       	url: contextPath+"/cf/pq-grid-data",
       	sortIndx: "lastUpdatedTs",
        sortDir: "down", 
    };
    let grid = $("#divDynarestGrid").grid({
      gridId: "dynarestListingGrid",
      colModel: colM,
      dataModel: dataModel,
          additionalParameters: {"cr_dynarestTypeId":"str_1"}
  });
});

	function changeType() {
        var type = $("#typeSelect").val();   
        let postData;
        if(type == 0) {
            postData = {gridId:"dynarestListingGrid"}
        } else {
            let typeCondition = "str_"+type;       
   
            postData = {gridId:"dynarestListingGrid" 
                    ,"cr_dynarestTypeId":typeCondition
                    }
        }
        
        let gridNew = $( "#divDynarestGrid" ).pqGrid();
        gridNew.pqGrid( "option", "dataModel.postData", postData);
        gridNew.pqGrid( "refreshDataAndView" );  
    }
   
	function dynarestType(uiObject){
		const dynarestTypeId = uiObject.rowData.dynarestTypeId;
		if(dynarestTypeId === 1){
			return "Default";
		}else{
			return "System";
		}
	}
		
	function requestTypes(uiObject){
	  let cellValue = uiObject.rowData.requestTypeId;
	  return requestType.find(el => el[cellValue])[cellValue];
	}
	
	function platforms(uiObject){
	  let cellValue = uiObject.rowData.platformId;
	  return platformValues.find(el => el[cellValue])[cellValue];
	}
	
	function formatLastUpdatedDate(uiObject){
        const lastUpdatedTs = uiObject.rowData.lastUpdatedTs;
        return formatDate(lastUpdatedTs);
    }
    
	function editDynarest(uiObject) {
		let dynarestUrl = uiObject.rowData.dynarestUrl;
		let methodName = uiObject.rowData.methodName;
		let dynarestId = uiObject.rowData.dynarestId;
		const revisionCount = uiObject.rowData.revisionCount;
			
		let actionElement;
		actionElement = ''<span id="''+dynarestUrl+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title=""></i></span>'';
		if(revisionCount > 1){
			actionElement = actionElement + ''<span id="''+dynarestId+''_entity" name="''+dynarestUrl+''" onclick="submitRevisionForm(this)" class= "grid_action_icons"><i class="fa fa-history"></i></span>''.toString();
		}else{
			actionElement = actionElement + ''<span class= "grid_action_icons disable_cls"><i class="fa fa-history"></i></span>''.toString();
		}
		return actionElement;	
	}
	
	function submitForm(element) {
	  $("#primaryId").val(element.id);
	  $("#addEditDynarest").submit();
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
('8a80cb81749ab40401749ac2e7360000', 'dynamic-rest-form', 'Form to manage dynamic rest modules.', 'select jdrd.jws_dynamic_rest_id as dynarestId, jdrd.jws_dynamic_rest_url as dynarestUrl, jdrd.jws_method_name as dynarestMethodName, 
jdrd.jws_method_description as dynarestMethodDescription, jdrd.jws_platform_id as dynarestPlatformId, jdrd.jws_request_type_id as dynarestRequestTypeId
, jdrd.jws_response_producer_type_id as dynarestProdTypeId, jdrd.jws_allow_files AS allowFiles, jdrd.datasource_id AS datasourceId 
FROM jq_dynamic_rest_details as jdrd 
WHERE jdrd.jws_dynamic_rest_url = "${primaryId}"', '<head>
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
	<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
	<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
	<script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	<script src="${(contextPath)!''''}/webjars/1.0/monaco/require.js"></script>
	<script src="${(contextPath)!''''}/webjars/1.0/monaco/min/vs/loader.js"></script>
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
	<script src="${(contextPath)!''''}/webjars/1.0/dynarest/dynarest.js"></script>
	<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script>
</head>

<div class="container">
	<div class="row topband">
		<div class="col-8">
			<#if (resultSet)?? && (resultSet)?has_content>
			    <h2 class="title-cls-name float-left">Edit REST API</h2> 
	        <#else>
	            <h2 class="title-cls-name float-left">Add REST API</h2> 
	        </#if>
     	</div>
      
	    <div class="col-4">   
		 	<#if (resultSet)?? && (resultSet)?has_content>  
				<#assign ufAttributes = {
			    	"entityType": "REST API",
			        "entityId": "dynarestUrl",
			        "entityName": "dynarestMethodName"
			    }>
			   	<@templateWithParams "user-favorite-template" ufAttributes />
			</#if>
		</div>
		
		<div class="clearfix"></div>
	</div>

	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>

	<form method="post" name="dynamicRestForm" id="dynamicRestForm">
		<input type="hidden" id="formId" name="formId" value="${(formId)!''''}"/>
		<input type="hidden" id="isEdit" name="isEdit"/>
		<input type="hidden" id="dynarestId" name="dynarestId"/>
		<div class="row">
			<div class="col-6">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="dynarestUrl">REST URL </label>
					<span><label id="urlPrefixLabel" style="background: lightgrey;" class="float-right"></label></span>
					<span id="dynarestURLSapn">
						<input type="text" id="dynarestUrl" name= "dynarestUrl" class="dynarestUrl form-control" onchange="dynarest.hideErrorMessage();"  maxlength="256" placeholder="REST URL" />
					</span>
				</div>
			</div>

			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="dynarestMethodName">REST Method Name</label>
					<span id="dynarestMethodNameSpan">
                    	<input type="text" class="form-control" id="dynarestMethodName" onchange="updateMethodName()"  name= "dynarestMethodName" maxlength="512" placeholder="Method Name" />
                    </span>
				</div>
			</div>

			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="dynarestMethodDescription">REST Method Description</label>
					<span id="dynarestMethodDescriptionSpan">
                    	<input type="text" class="form-control" id="dynarestMethodDescription"  name= "dynarestMethodDescription" placeholder="Method Description" />
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
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="dynarestPlatformId">Platform Type</label>
					<select class="form-control" id="dynarestPlatformId" onchange="showMethodSignature(this.value)" name="dynarestPlatformId" title="Platform Id">
	                        <option value="2" selected="selected"> FTL </option>
	                        <option value="1"> JAVA </option>
	                        <option value="3"> Javascript </option>
	                </select>
	            </div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
	        		<label for="flammableState" style="white-space:nowrap">Datasource</label>
	        		<select id="dataSource" name="dataSourceId" class="form-control" onchange="showHideTableAutocomplete()">
	        			<option id="defaultConnection" value="">Default Connection</option>
	        		</select>
           		</div>
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
	<input type="hidden" id="primaryKey" name="primaryKey">
	<input type="hidden" id="entityName" name="entityName" value="jq_dynamic_rest_details">
    </form>
   	
   	<input type="hidden" id="dataSourceId" value=""> 
   	
   	<div class="row">
		<div class="col-3"> 
    		<input id="moduleId" value="47030ee1-0ecf-11eb-94b2-f48e38ab9348" name="moduleId" type="hidden">
    		<@templateWithoutParams "role-autocomplete"/>
    	</div>
    </div>
    
    <div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropup custom-grp-btn">
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
	contextPath = "${contextPath}";
	let dynarest;
	let formId = "${(formId)!''''}";
    let urlPrefix = getDynarestBAseURL();
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
  	$("#errorMessage").hide();
  	showMethodSignature($("#dynarestPlatformId").val());
  }

	function getDynarestBAseURL(){ 
		$.ajax({
			url:contextPath+"/api/rest-api-base-url",
			type:"GET",
	        data:{
	        	
	        }, 
			success : function(data) {
				urlPrefix = data;
				$("#urlPrefixLabel").text(data);
			},
			error : function(xhr, error){
				showMessage("Error occurred while fetching base url", "error");
			}, 
	    });  	
	}
  $(function () {
      dynarest = new DynamicRest(formId);
	  $("#methodSignatureDiv").hide();
	  <#if (resultSet)?? && resultSet?has_content>
		<#list resultSet as resultSetList>
			<#if resultSetList?is_first>
				$("#dynarestId").val(''${(resultSetList?api.get("dynarestId"))!''''}'');
				$("#primaryKey").val(''${(resultSetList?api.get("dynarestId"))!''''}'');
				$("#dynarestUrl").val(''${(resultSetList?api.get("dynarestUrl"))!''''}'');
				$("#dynarestMethodName").val(''${(resultSetList?api.get("dynarestMethodName"))!''''}'');
				$("#dynarestMethodDescription").val(''${(resultSetList?api.get("dynarestMethodDescription"))!''''}'');
				$("#dynarestPlatformId option[value=''${(resultSetList?api.get("dynarestPlatformId"))!''''}'']").prop("selected", "selected");
				$("#allowFiles").val(''${(resultSetList?api.get("allowFiles"))!''''}'');
				$("#dataSourceId").val(''${(resultSetList?api.get("datasourceId"))!''''}'');
                dynarest.populateDetails(''${(resultSetList?api.get("dynarestRequestTypeId"))!''''}'',
                ''${(resultSetList?api.get("dynarestProdTypeId"))!''''}'',''${(resultSetList?api.get("dynarestUrl"))!''''}'');
				$("#isEdit").val(1);
				$("#dataSource").attr("disabled", true);
			</#if>
		</#list>
		getAllDatasource(1);
	<#else>
		const generatedDynarestId = uuidv4();
		$("#dynarestId").val(generatedDynarestId);
		$("#primaryKey").val(generatedDynarestId);
		$("#isEdit").val(0);
		let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
        multiselect.setSelectedObject(defaultAdminRole);
	    dynarest.populateDetails();
	    getAllDatasource(0);
	</#if>
	
	let allowFiles = $("#allowFiles").val();
	if(allowFiles === "1"){
		$("#allowFilesCheckbox").prop("checked", true);
	}else{
		$("#allowFilesCheckbox").prop("checked", false);
	}
	
	if(typeof getSavedEntity !== undefined && typeof getSavedEntity === "function"){
		getSavedEntity();
	}
	
	savedAction("dynamic-rest-form", Number.parseInt($("#isEdit").val()));
	hideShowActionButtons();
  });
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2, NOW());


REPLACE into jq_dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence) VALUES
('8a80cb81749dbc3d01749dc00d2b0001', '8a80cb81749ab40401749ac2e7360000', '<#if  (formData?api.getFirst("isEdit"))?has_content &&
(formData?api.getFirst("isEdit")) == "1">
    UPDATE jq_dynamic_rest_details SET 
        jws_dynamic_rest_url = ''${formData?api.getFirst("dynarestUrl")}''
        ,jws_method_name = ''${formData?api.getFirst("dynarestMethodName")}''
        ,jws_method_description = ''${formData?api.getFirst("dynarestMethodDescription")}''
        ,jws_request_type_id = ''${formData?api.getFirst("dynarestRequestTypeId")}''
        ,jws_response_producer_type_id = ''${formData?api.getFirst("dynarestProdTypeId")}''
        ,jws_rbac_id = 1
        ,jws_service_logic = ''${formData?api.getFirst("serviceLogic")}''
        ,jws_platform_id = ''${formData?api.getFirst("dynarestPlatformId")}''
        ,jws_allow_files = ''${formData?api.getFirst("allowFiles")}''
        ,last_updated_by = :loggedInUserName
        ,last_updated_ts = NOW()
    WHERE jws_dynamic_rest_id = ''${formData?api.getFirst("dynarestId")}''   ;
<#else>
    INSERT INTO jq_dynamic_rest_details(jws_dynamic_rest_id, jws_dynamic_rest_url, jws_method_name, jws_method_description, jws_rbac_id, jws_request_type_id, 
    jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, 
    datasource_id, created_by, created_date, last_updated_ts)
    VALUES (
		 ''${formData?api.getFirst("dynarestId")}'',
         ''${formData?api.getFirst("dynarestUrl")}''
         , ''${formData?api.getFirst("dynarestMethodName")}''
         , ''${formData?api.getFirst("dynarestMethodDescription")}''
         , 1
         , ''${formData?api.getFirst("dynarestRequestTypeId")}''
         , ''${formData?api.getFirst("dynarestProdTypeId")}''
         ,''${formData?api.getFirst("serviceLogic")}''
         , ''${formData?api.getFirst("dynarestPlatformId")}''
         , ''${formData?api.getFirst("allowFiles")}''
         <#if  (formData?api.getFirst("dataSourceId"))?has_content>
         , ''${formData?api.getFirst("dataSourceId")}''
         <#else>
         ,NULL
         </#if>
         , :loggedInUserName
         , NOW()
         , NOW()
    );
</#if>', 1);

REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('753e3330-e349-4d67-be25-f3824203d522', 'validate-user-email-id', 1, 'validateUserEmailId', 'Validate User Email Id', 2, 7, 'function validateEmailId(requestDetails, daoResults) {
    return daoResults["emailIdCount"];
}

validateEmailId(requestDetails, daoResults);', 3, 2, 'aar.dev@trigyn.com', NOW(), NOW());

REPLACE INTO jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(110, '753e3330-e349-4d67-be25-f3824203d522', 'emailIdCount','SELECT COUNT(ju.email) AS emailCount FROM jq_user AS ju WHERE ju.email = :email', 1, 2);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('121f0f58-6be1-4da4-a660-d4eb17ae30f2', '753e3330-e349-4d67-be25-f3824203d522', 'validate-user-email-id', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1, 1);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('5f7f40f2-7a90-11eb-9439-0242ac130002', '753e3330-e349-4d67-be25-f3824203d522', 'validate-user-email-id', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 1);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('6489d472-7a90-11eb-9439-0242ac130002', '753e3330-e349-4d67-be25-f3824203d522', 'validate-user-email-id', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 1);


REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts)
VALUES('833feb5b-8e60-4cb6-a247-3712efc520db', 'user-favorite-entity', 1, 'saveUserFavoriteEntityDetails', '', 1, 7, '" "', 3, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());
 
REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts)
VALUES('5fd2ea97-2bb1-44f7-ba7f-9784b8e2cbef', 'user-favorite-entity-by-type', 1, 'userFavoriteEnityByType', '', 1, 7, '<#list userFavoriteEnityList as userFavoriteEnity>
    ${userFavoriteEnity.isFavorite}
</#list>', 2, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_dynamic_rest_dao_details(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(125, '833feb5b-8e60-4cb6-a247-3712efc520db', 'noVariable', '<#if isChecked?? && isChecked?has_content && isChecked == "true">
    INSERT INTO  jq_user_favorite_entity VALUES(UUID(), :loggedInUserName, :entityType, :entityId
    , :entityName, NOW())
    ON DUPLICATE KEY UPDATE last_updated_date = NOW();
 <#else>
    DELETE FROM jq_user_favorite_entity WHERE user_email_id = :loggedInUserName AND entity_type =
    :entityType AND entity_id = :entityId AND entity_name = :entityName
 </#if>', 1, 2);
 
 
REPLACE INTO jq_dynamic_rest_dao_details(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(135, '5fd2ea97-2bb1-44f7-ba7f-9784b8e2cbef', 'userFavoriteEnityList', 'SELECT COUNT(*) AS isFavorite
FROM jq_user_favorite_entity AS jufq
WHERE jufq.user_email_id = :loggedInUserName AND jufq.entity_type = 
:entityType AND jufq.entity_id = :entityId AND jufq.entity_name = :entityName', 1, 1);

REPLACE INTO jq_dynamic_rest_details(jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('2db9f5bc-30d7-4b36-9d61-ec1fc8e3a41b', 'rest-api-base-url', 1, 'getDynarestBaseUrl', '', 2, 7, 'com.trigyn.jws.dynarest.service.DynaRest', 1, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());

REPLACE INTO jq_dynamic_rest_dao_details(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(137, '2db9f5bc-30d7-4b36-9d61-ec1fc8e3a41b', 'noVariable', 'SELECT 1', 1, 1);

REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('0924c765-d076-486a-96c0-6a6ccfe58cdf', '2db9f5bc-30d7-4b36-9d61-ec1fc8e3a41b', 'rest-api-base-url', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('61c15328-96ce-415a-b26f-2df9867302d1', '2db9f5bc-30d7-4b36-9d61-ec1fc8e3a41b', 'rest-api-base-url', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('6480757d-d802-45ea-840c-7f3630677964', '2db9f5bc-30d7-4b36-9d61-ec1fc8e3a41b', 'rest-api-base-url', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);

SET FOREIGN_KEY_CHECKS=1;
