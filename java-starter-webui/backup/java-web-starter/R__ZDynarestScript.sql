
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
<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script>
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
                <a href="${(contextPath)!''''}/cf/sl"> 
					<input id="scheduleRest" class="btn btn-primary" value="Scheduler" type="button">
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
		
		<div id="deleteHeader"></div>
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
        { title: "Action", maxWidth: 145, align: "left", render: editDynarest, dataIndx: "action", sortable: false }
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
            		if(uiObject.rowData.dynarestTypeId == 1) { 
		        <#if loggedInUserRoleList?? && loggedInUserRoleList?size gt 0>
		        	<#list loggedInUserRoleList as loggedInUserRole>
		            	<#if (loggedInUserRole == "ADMIN" )>   
		        			actionElement += ''<span onclick=\\''openDeletConfirmation(\"divDynarestGrid",\"''+dynarestId+''\",\"47030ee1-0ecf-11eb-94b2-f48e38ab9348\")\\'' class= "grid_action_icons" title="Delete"><i class="fa fa-trash "></i></span>''.toString();
		        			<#break>
		        		</#if>
		        	</#list>
		        </#if>
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
, jdrd.jws_response_producer_type_id as dynarestProdTypeId, jdrd.jws_allow_files AS allowFiles, jdrd.jws_header_json AS header_json  
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
					<span id="restUrlSpan" onclick="dynarest.copyUrl()">
						<label id="urlPrefixLabel" style="background: lightgrey;" title="<@resourceBundleWithDefault "jws.copyMessageTitle" "Click here to copy path"/>" class="float-right">
						</label>
					</span>
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
					<select class="form-control" id="dynarestRequestTypeId" name="dynarestRequestTypeId" title="HTTP Method Type">
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
			
			<div id="allowFilesDiv" class="col-3" style="display:none;">
				<div class="col-inner-form full-form-fields">
					<label  for="contextType">Allow Files</label>
					<div class="onoffswitch">
						<input type="hidden" id="allowFiles" name="allowFiles"  value="1"/>
						<input type="checkbox" name="allowFilesCheckbox" class="onoffswitch-checkbox" id="allowFilesCheckbox" onchange="toggleAllowFiles()" />
						<label class="onoffswitch-label" for="allowFilesCheckbox">
							<span class="onoffswitch-inner"></span>
							<span class="onoffswitch-switch"></span>
						</label>
					</div>
				</div>
			</div>
			
			</div>
			
			<div class="row">
			 	<div class="col-6">
		            <div class="col-inner-form full-form-fields">
		                    <table id="headerTable"  class="customtblecls">
		                         <tr>
		                            <th colspan="3">   
		                                <div class="displyflx">                             
		                                	<span class="titleclsnm">Response Header </span>                                    
		                                </div>
		                            </th>
		                        </tr>    
		                        <tr>
		                            <td class="bgtd">Name</td>
		                            <td class="bgtd">Value </td>
		                            <td class="bgtd"><span class="plusticon" onclick="addRow()"><i class="fa fa-plus-circle" aria-hidden="true"></i></span></td>
		                        </tr>   
		                                       
		                    </table>
		            </div>
	            </div>
            </div>
			<div id="deleteHeader"></div>
			
			<div id="methodSignatureDiv" class="col-12 method-sign-info-div">
				<h3 class="titlename method-sign-info">
					<i class="fa fa-lightbulb-o" aria-hidden="true"></i><label for="methodSignature">Method Signature</label>
				</h3>
				<span id="methodSignature"><span>
			</div>
			<br>
			<div id="ftlParameterDiv" class="col-12 method-sign-info-div">
				<h3 class="titlename method-sign-info">
				    <i class="fa fa-lightbulb-o" aria-hidden="true"></i><label for="ftlParameter">SQL/FTL Parameters and Macros</label>
			    </h3>
				<span id="ftlParameter">loggedInUserName, loggedInUserRoleList {}, templateWithoutParams {}, templateWithParams {}, resourceBundle {}, resourceBundleWithDefault {}</span>
		    </div>
			<br>
			<div id="xmlFormatDiv" class="col-12 method-sign-info-div" style="display:none;">
				<h3 class="titlename method-sign-info">
				    <i class="fa fa-lightbulb-o" aria-hidden="true"></i><label for="xmlFormat">Email XML Structure</label>
			    </h3>
				<span><textarea id="xmlContent" readonly style="width: 100%;"></textarea></span>
		    </div>

		
		<div class="row margin-t-b">
			<div class="col-12">
				<h3 class="titlename"><span class="asteriskmark">*</span><label id="lblServiceLogic">Service Logic</label></h3>
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
	let savedHeaderJson = "";
	let selectedLanguageId = getCookie("locale");
		
	function togglePlatform(element) {
    	$("#xmlFormatDiv").hide();
    	if(element.value == "8" || element.value == "10") {
        	$("#dynarestPlatformId option[value=1]").prop("selected", true);
            $("#dynarestPlatformId option[value=2]").prop("disabled", true);
        } else {
            $("#dynarestPlatformId option[value=2]").prop("disabled", false);
        }
        if(element.value == "14"){ 
        	dynarest.getEmailXMLStructure();
        }
        //dynarest.hideShowAllowFiles();
        showMethodSignature($("#dynarestPlatformId").val());
        $("#headerTable tbody tr#Content-Type td input.value").each(
	        function(){
	        	if($(''#dynarestProdTypeId'').find(":selected").text() != "email/xml") {
	           		$(this)[0].value = $(''#dynarestProdTypeId'').find(":selected").text(); 
	           	} else {
	           		$(this)[0].value = "application/json"; 
	           	}
	        }
    	);
  	}

  function showMethodSignature(value){
         if(value == "1"){
            $("#methodSignatureDiv").show();
            $("#methodSignature").text("public T " + $("#dynarestMethodName").val() + 
                	"(HttpServletRequest a_httpServletRequest, Map&lt;String, Fileinfo&gt; files, Map&lt;String, Object&gt; daoResultSets, UserDetailsVO userDetails) { }");
         } else {
            $("#methodSignatureDiv").hide();
         }
         
         if(value == "2" || value == "3"){
            $("#lblServiceLogic").html("Service Logic");
         } else {
         	$("#lblServiceLogic").html("Class Name with package");
         }
         $("#copyIcon").remove();
         $("#urlPrefixLabel").append(''<i id="copyIcon" class="fa fa-clipboard" aria-hidden="true"></i>'');
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
                let produceTypeText = dynarest.populateDetails(''${(resultSetList?api.get("dynarestRequestTypeId"))!''''}'',
                ''${(resultSetList?api.get("dynarestProdTypeId"))!''''}'',''${(resultSetList?api.get("dynarestUrl"))!''''}'');
				$("#isEdit").val(1);
	            let jsonStr = ''${resultSetList?api.get("header_json")!""}'';
	          	if(jsonStr !== ''''){
	            	savedHeaderJson = JSON.parse(jsonStr);
	            } else {
	            	let selectedLanguageId = getCookie("locale");
		            let tableRow = ''<tr id="Powered-By"><td><input class="key" type="text" value="Powered-By" readonly></td><td><input class="value" type="text" value="JQuiver" readonly></td></tr>'';
	            	tableRow += ''<tr id="Content-Type"><td><input class="key" type="text" value="Content-Type" readonly></td><td><input class="value" type="text" value="''+produceTypeText+''" readonly></td>'';
	            	tableRow += ''<td></td></tr>'';
	            	tableRow += ''<tr id="Content-Language"><td><input class="key" type="text" value="Content-Language"></td><td><input id="txtContentLang" class="value" type="text" value="''+selectedLanguageId+''"></td>'';
	            	tableRow += ''</tr>'';
	            	
	            	$("#headerTable > tbody ").append(tableRow);
	            }
	            let platform = ''${(resultSetList?api.get("dynarestPlatformId"))!''''}'';
	            if(platform == "2" || platform == "3"){
		            $("#lblServiceLogic").html("Service Logic");
		         } else {
		         	$("#lblServiceLogic").html("Class Name with package");
		         }
			</#if>
		</#list>
		getAllDatasource(1);
	<#else>
        let tableRow = ''<tr id="Powered-By"><td><input class="key" type="text" value="Powered-By" readonly></td><td><input class="value" type="text" value="JQuiver" readonly></td></tr>'';
        tableRow += ''<tr id="Content-Type"><td><input class="key" type="text" value="Content-Type" readonly></td><td><input class="value" type="text" value="text/html" readonly></td>'';
        tableRow += ''<td></td></tr>'';
        tableRow += ''<tr id="Content-Language"><td><input class="key" type="text" value="Content-Language"></td><td><input id="txtContentLang" class="value" type="text" value="''+selectedLanguageId+''"></td>'';
	    tableRow += ''</tr>'';
            	
        $("#headerTable > tbody ").append(tableRow);
		const generatedDynarestId = uuidv4();
		$("#dynarestId").val(generatedDynarestId);
		$("#primaryKey").val(generatedDynarestId);
		$("#isEdit").val(0);
		let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
        multiselect.setSelectedObject(defaultAdminRole);
	    dynarest.populateDetails();
	    getAllDatasource(0);
	</#if>
	
	createHeaderResponseTable();
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
	
	$("#xmlFormat").accordion({
	    collapsible: true,
	    active: false
	});
	
  });
  
  function addRow(key, value){
		let context = this;	
		let row = ''<tr id=''+key+''><td><input class="key" type="text"></td><td><input class="value" type="text" '';
		if(key == "Powered-By" || key == "Content-Type" || key == "Content-Language") {
			row += ''readonly'';
		}
		row += ''></td><td>'';
		if(key != "Powered-By" && key != "Content-Type"|| key != "Content-Language") {
			row+=''<span id="btn_''+key+''" onclick="deleteRow(this)" class="cusrorhandcls"><i class="fa fa-minus-circle" aria-hidden="true"></i></span>'';			
		} 
		row+=''</td></tr>'';
		
		$(''#headerTable tr:last'').after(row);
		if(key !== undefined && value !== undefined){
			if(key == "Content-Language") {
				$(''#headerTable tr:last'').find(''td input.key'').val(key);
				$(''#headerTable tr:last'').find(''td input.value'').val(selectedLanguageId);
			} else {
				$(''#headerTable tr:last'').find(''td input.key'').val(key);
				$(''#headerTable tr:last'').find(''td input.value'').val(value);
			}
		}
	}
    
    function deleteRow(rowElement){
		$("#deleteHeader").html("Are you sure you want to delete?");
		$("#deleteHeader").dialog({
		bgiframe	: true,
		autoOpen	: true, 
		modal		: true,
		closeOnEscape : true,
		draggable	: true,
		resizable	: false,
		title		: "Delete",
		position: {
			my: "center", at: "center"
		},
		buttons : [{
				text		:"Cancel",
				click	: function() { 
					$(this).dialog(''close'');
				},
			},
			{
				text		: "Delete",
				click	: function(){
					$(this).dialog(''close'');
					let rowId = $(rowElement).attr(''id'').split("_")[1];
					$(''#''+rowId).remove();
				}
           	},
       ],	
	   open: function( event, ui ) {
			 $(''.ui-dialog-titlebar'')
		   	    .find(''button'').removeClass(''ui-dialog-titlebar-close'').addClass(''ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close'')
		       .prepend(''<span class="ui-button-icon ui-icon ui-icon-closethick"></span>'').append(''<span class="ui-button-icon-space"></span>'');
       		}	
	   });
		
	}

	function createHeaderResponseTable(){
		if(savedHeaderJson !== ''''){
			Object.keys(savedHeaderJson).forEach(function(key) {
				addRow(key, savedHeaderJson[key]);
			});	
		}
	}
    
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
        ,jws_header_json = ''${formData?api.getFirst("headerJson")}''
        ,last_updated_by = :loggedInUserName
        ,last_updated_ts = NOW()
    WHERE jws_dynamic_rest_id = ''${formData?api.getFirst("dynarestId")}''   ;
<#else>
    INSERT INTO jq_dynamic_rest_details(jws_dynamic_rest_id, jws_dynamic_rest_url, jws_method_name, jws_method_description, jws_rbac_id, jws_request_type_id, 
    jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_header_json ,
    created_by, created_date, last_updated_ts)
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
         , ''${formData?api.getFirst("headerJson")}''
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

REPLACE INTO jq_template_master(template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('f4da2a9a-d5e9-4bf4-8bfc-248a8372af19', 'jq-email-xml-template', '
<emails>
   <email>
	   <header>
	      <property name="">value here</property>
	      <property name="">value here</property>
	   </header>
	   <recepients>
	      <recepient type="to|cc|bcc">
	         <name ></name>
	         <mailid ></mailid>
	      </recepient>
	      <recepient type="to|cc|bcc">
	         <name ></name>
	         <mailid ></mailid>
	      </recepient>
	      <recepient type="to|cc|bcc">
	         <name ></name>
	         <mailid ></mailid>
	      </recepient>
	   </recepients>
	   <subject><!-- Mail subject to be included here --></subject>
	   <body contenttype="text/html | text/plain" >
	   	<![CDATA[
			<!-- Mail Body to be included here -->
		]]>
	   </body>
	   <attachments>
	      <attachment type="1|2|3">
	         <!-- 1=internal uploaded file;2=filesystem;3=uploaded file in this call -->
	          <name ></name>
	         <path></path>
	         <!-- if type=1 then fileuploadid;else absolute file path,else the uploaded file -->
	      </attachment>
	   </attachments>
   </email>
</emails>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);


REPLACE INTO jq_template_master(template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) 
VALUES('c615eedb-f138-46e7-9d5a-fb790733f2b7', 'jq-web-client-call-xml', '<rest>
    <url></url> <!-- mandatory -->
    <type></type> <!-- mandatory -->
    <content-type></content-type>
    <timeout></timeout>
    <request>
        <headers>
            <parameter>
                <name></name>
                <value></value>
            </parameter>
            <parameter>
                <name></name>
                <value></value>
            </parameter>
            <parameter>
                <name></name>
                <value></value>
            </parameter>
        </headers>
        <body>
            <parameter>
                <name></name>
                <value></value>
            </parameter>
            <parameter>
                <name></name>
                <value></value>
            </parameter>
            <parameter>
                <name></name>
                <value></value>
            </parameter>
        </body>
    </request>
</rest>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);


REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('0924c765-d076-486a-96c0-6a6ccfe58cdf', '2db9f5bc-30d7-4b36-9d61-ec1fc8e3a41b', 'rest-api-base-url', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('61c15328-96ce-415a-b26f-2df9867302d1', '2db9f5bc-30d7-4b36-9d61-ec1fc8e3a41b', 'rest-api-base-url', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('6480757d-d802-45ea-840c-7f3630677964', '2db9f5bc-30d7-4b36-9d61-ec1fc8e3a41b', 'rest-api-base-url', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);

REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('3438f314-7bbb-4f19-8260-32b0ee87c605', 'f4da2a9a-d5e9-4bf4-8bfc-248a8372af19', 'jq-email-xml-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'anonymous-user', 1, 1), 
('4f00c03f-65c4-4f7b-8f52-0ad426d8777b', 'f4da2a9a-d5e9-4bf4-8bfc-248a8372af19', 'jq-email-xml-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'anonymous-user', 1, 1), 
('e4c849e7-c558-4c51-82f2-c07151601b40', 'f4da2a9a-d5e9-4bf4-8bfc-248a8372af19', 'jq-email-xml-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'anonymous-user', 1, 1);

REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('4c384557-ffad-4341-bf0c-7c6df340f779', 'c615eedb-f138-46e7-9d5a-fb790733f2b7', 'jq-web-client-call-xml', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'abhay.desai@trigyn.com', 1, 1), 
('8e6c0bda-8a4b-436c-a711-d539f05713c5', 'c615eedb-f138-46e7-9d5a-fb790733f2b7', 'jq-web-client-call-xml', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'abhay.desai@trigyn.com', 1, 1), 
('f986fd46-d138-4fd4-b88a-c26d4443b56e', 'c615eedb-f138-46e7-9d5a-fb790733f2b7', 'jq-web-client-call-xml', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'abhay.desai@trigyn.com', 1, 1);

update jq_dynamic_rest_dao_details SET jws_dao_query_template='SELECT  
jdrd.jws_service_logic AS dynarestServiceLogic,jdrdd.jws_result_variable_name AS variableName,
jdrdd.jws_dao_query_template AS dynarestDaoQuery, jdrdd.jws_query_sequence AS dynarestQuerySequence ,jdrdd.jws_dao_query_type AS dynarestQueryType,jdrdd.datasource_id AS datasourceId
FROM jq_dynamic_rest_details AS jdrd 
LEFT OUTER JOIN jq_dynamic_rest_dao_details AS jdrdd ON jdrdd.jws_dynamic_rest_details_id = jdrd.jws_dynamic_rest_id 
WHERE jdrd.jws_dynamic_rest_url = "${(url)!''''}" ORDER BY dynarestQuerySequence ASC' 
WHERE jws_result_variable_name = "dynarestDetails" AND 
jws_dynamic_rest_details_id = 
(SELECT jws_dynamic_rest_id FROM jq_dynamic_rest_details WHERE jws_dynamic_rest_url = 'dynarestDetails');



REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb81749b028401749b062c541112', 'script-util', '
var jq_scriptUtilClass = Java.type(''com.trigyn.jws.dynarest.cipher.utils.ScriptUtil'');
var jq_scriptUtilObject = new jq_scriptUtilClass();

function jq_updateCookies(a_strKey, a_strValue){
	return jq_scriptUtilObject.updateCookies(a_strKey, a_strValue);
}

function jq_updateCookies(a_strKey, a_strValue, maxAge){
	return jq_scriptUtilObject.updateCookies(a_strKey, a_strValue, maxAge);
}

function jq_updateCookies(a_strKey, a_strValue, httpOnly){
	return jq_scriptUtilObject.updateCookies(a_strKey, a_strValue, httpOnly);
}

function jq_updateCookies(a_strKey, a_strValue, maxAge, httpOnly){
	return jq_scriptUtilObject.updateCookies(a_strKey, a_strValue, maxAge, httpOnly);
}

function jq_updateCookieSecurity(a_strKey, isSecured){
	return jq_scriptUtilObject.updateCookieSecurity(a_strKey, isSecured);
}
	
function jq_getCookiesFromRequest(a_strKey){
	return jq_scriptUtilObject.getCookiesFromRequest(a_strKey);
}
	
function jq_haveCookie(a_strKey){
	return jq_scriptUtilObject.haveCookie(a_strKey);
}
	
function jq_deleteCookie(a_strKey){
	return jq_scriptUtilObject.deleteCookie(a_strKey);
}
	
function jq_updateSession(a_strKey, a_strValue){
	return jq_scriptUtilObject.updateSession(a_strKey, a_strValue);
}
	
function jq_getValueFromSession(a_strKey){
	return jq_scriptUtilObject.getValueFromSession(a_strKey);
}
	
function jq_haveSessionKey(a_strKey){
	return jq_scriptUtilObject.haveSessionKey(a_strKey);
}
	
function jq_deleteSessionKey(a_strKey){
	return jq_scriptUtilObject.deleteSessionKey(a_strKey);
}
	
function jq_getCreationTime(){
	return jq_scriptUtilObject.getCreationTime();
}
	
function jq_getLastAccessedTime(){
	return jq_scriptUtilObject.getLastAccessedTime();
}
		
function jq_getAllFiles(a_filePath){
	return jq_scriptUtilObject.getAllFiles(a_filePath);
}
	
function jq_deleteFile(a_filePath){
	return jq_scriptUtilObject.deleteFile(a_filePath);
}
	
function jq_saveFile(a_strFileContent, a_strTargetFileName){
	return jq_scriptUtilObject.saveFile(a_strFileContent, a_strTargetFileName);
}

function saveFileFromPath(a_strFilePath, a_strFileBinID, a_strcontextID){
	return jq_scriptUtilObject.saveFileFromPath(a_strFilePath, a_strFileBinID, a_strcontextID);
}

function jq_saveFileBin(a_strFileContent, a_strTargetFileName, a_strFileBinID, a_strcontextID){
	return jq_scriptUtilObject.saveFileBin(a_strFileContent, a_strTargetFileName, a_strFileBinID, a_strcontextID);
}

function jq_getFileContent(a_strAbsolutePath){
	return jq_scriptUtilObject.getFileContent(a_strAbsolutePath);
}

function jq_getFileBinContent(a_strfileUploadID){
	return jq_scriptUtilObject.getFileBinContent(a_strfileUploadID);
}

function jq_copyFile(sourceFilePath, destinationFilePath) {
	return jq_scriptUtilObject.copyFile(sourceFilePath, destinationFilePath);
}

function jq_copyFile(sourceFilePath, destinationFilePath, a_strTargetFileName) {
	return jq_scriptUtilObject.copyFile(sourceFilePath, destinationFilePath, a_strTargetFileName);
}

function jq_copyFileBinId(a_strfileUploadID, destinationFilePath) {
	return jq_scriptUtilObject.jq_copyFileBinId(a_strfileUploadID, destinationFilePath);
}

function jq_getDBResult(a_strQuery, a_strdataSourceID, a_requestParams){
	return jq_scriptUtilObject.getDBResult(a_strQuery, a_strdataSourceID, a_requestParams);
}

function jq_callStoredProcedure(a_strQuery, a_strdataSourceID, a_requestParams){
	return jq_scriptUtilObject.callStoredProcedure(a_strQuery, a_strdataSourceID, a_requestParams);
}

function jq_updateDBQuery(a_strQuery, a_strdataSourceID, a_requestParams){
	return jq_scriptUtilObject.updateDBQuery(a_strQuery, a_strdataSourceID, a_requestParams);
}

function jq_executeRESTCall(a_strRestXML){
	return jq_scriptUtilObject.executeRESTCall(a_strRestXML);
}

function jq_sendMail(a_strMailXML, a_requestParams){
	return jq_scriptUtilObject.sendMail(a_strMailXML, a_requestParams);
}

function jq_sendMail(a_strMailXML){
	return jq_scriptUtilObject.sendMail(a_strMailXML);
}', 'admin@jquiver.com', 'admin@jquiver.com', NOW(), NULL, 2);

REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('4c384557-ffad-4341-bf0c-7c6df340f889', '8a80cb81749b028401749b062c541112', 'script-util', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin@jquiver.com', 1, 1), 
('8e6c0bda-8a4b-436c-a711-d539f0571995', '8a80cb81749b028401749b062c541112', 'script-util', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin@jquiver.com', 1, 1), 
('f986fd46-d138-4fd4-b88a-c26d4443b10e', '8a80cb81749b028401749b062c541112', 'script-util', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin@jquiver.com', 1, 1);


REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('1f8f1cfa-14ea-489b-b9be-4c42eaf05ecc', 'delete-entity', 1, 'deleteEntity', 'Delete Entity', 1, 7, 'function  deleteEntityData(requestDetails, daoResults){
    
    if(requestDetails["moduleType"] == "07067149-098d-11eb-9a16-f48e38ab9348") {
        var HashMap = Java.type("java.util.HashMap");
        var params = new HashMap();
        params.put("entityId", requestDetails["entityId"]);
        jq_updateDBQuery("delete from jq_module_version where entity_id=:entityId", null, params);
        jq_updateDBQuery("delete from jq_user_favorite_entity where entity_id=:entityId AND entity_type=''Grid Utils''", null, params);
        jq_updateDBQuery("delete from jq_entity_role_association where entity_id=:entityId AND module_id=''07067149-098d-11eb-9a16-f48e38ab9348''", null, params);        
        jq_updateDBQuery("delete from jq_grid_details where grid_id=:entityId", null, params);

    } else if(requestDetails["moduleType"] == "91a81b68-0ece-11eb-94b2-f48e38ab9348") {
        var HashMap = Java.type("java.util.HashMap");
        var params = new HashMap();
        params.put("entityId", requestDetails["entityId"]);
        jq_updateDBQuery("delete from jq_module_version where entity_id=:entityId", null, params);
        jq_updateDBQuery("delete from jq_user_favorite_entity where entity_id=:entityId AND entity_type=''TypeAhead Autocomplete''", null, params);
        jq_updateDBQuery("delete from jq_entity_role_association where entity_id=:entityId AND module_id=''91a81b68-0ece-11eb-94b2-f48e38ab9348''", null, params);        
        jq_updateDBQuery("delete from jq_autocomplete_details where ac_id=:entityId", null, params);

    } else if(requestDetails["moduleType"] == "1b0a2e40-098d-11eb-9a16-f48e38ab9348") {
        var HashMap = Java.type("java.util.HashMap");
        var params = new HashMap();
        params.put("entityId", requestDetails["entityId"]);
        jq_updateDBQuery("delete from jq_module_version where entity_id=:entityId", null, params);
        jq_updateDBQuery("delete from jq_user_favorite_entity where entity_id=:entityId AND entity_type=''Templating''", null, params);
        jq_updateDBQuery("delete from jq_entity_role_association where entity_id=:entityId AND module_id=''1b0a2e40-098d-11eb-9a16-f48e38ab9348''", null, params);        
        jq_updateDBQuery("delete from jq_template_master where template_id=:entityId", null, params);

    } else if(requestDetails["moduleType"] == "30a0ff61-0ecf-11eb-94b2-f48e38ab9348") {
        var HashMap = Java.type("java.util.HashMap");
        var params = new HashMap();
        params.put("entityId", requestDetails["entityId"]);
        jq_updateDBQuery("delete from jq_module_version where entity_id=:entityId", null, params);
        jq_updateDBQuery("delete from jq_user_favorite_entity where entity_id=:entityId AND entity_type=''Form Builder''", null, params);
        jq_updateDBQuery("delete from jq_entity_role_association where entity_id=:entityId AND module_id=''30a0ff61-0ecf-11eb-94b2-f48e38ab9348''", null, params);    
        jq_updateDBQuery("delete from jq_dynamic_form_save_queries where dynamic_form_id=:entityId", null, params);
        jq_updateDBQuery("delete from jq_dynamic_form where form_id=:entityId", null, params);

    } else if(requestDetails["moduleType"] == "47030ee1-0ecf-11eb-94b2-f48e38ab9348") {
        var HashMap = Java.type("java.util.HashMap");
        var params = new HashMap();
        params.put("entityId", requestDetails["entityId"]);
        jq_updateDBQuery("delete from jq_module_version where entity_id=:entityId", null, params);
        jq_updateDBQuery("delete from jq_user_favorite_entity where entity_id=:entityId AND entity_type=''REST API''", null, params);
        jq_updateDBQuery("delete from jq_entity_role_association where entity_id=:entityId AND module_id=''47030ee1-0ecf-11eb-94b2-f48e38ab9348''", null, params);    
        jq_updateDBQuery("delete from jq_dynamic_rest_dao_details where jws_dynamic_rest_details_id=:entityId", null, params);
        jq_updateDBQuery("delete from jq_dynamic_rest_details where jws_dynamic_rest_id=:entityId", null, params);

    } else if(requestDetails["moduleType"] == "c6cc466a-0ed3-11eb-94b2-f48e38ab9348") {
        var HashMap = Java.type("java.util.HashMap");
        var params = new HashMap();
        params.put("entityId", requestDetails["entityId"]);
        jq_updateDBQuery("delete from jq_entity_role_association where entity_id=:entityId AND module_id=''c6cc466a-0ed3-11eb-94b2-f48e38ab9348''", null, params);    
        jq_updateDBQuery("delete from jq_module_listing_i18n where module_id=:entityId", null, params);
        jq_updateDBQuery("delete from jq_module_listing where module_id=:entityId", null, params);
    } else if(requestDetails["moduleType"] == "additional_datasource") {
        var HashMap = Java.type("java.util.HashMap");
        var params = new HashMap();
        params.put("entityId", requestDetails["entityId"]);
        jq_updateDBQuery("delete from jq_additional_datasource where additional_datasource_id=:entityId", null, params);    
    }
}

if(requestDetails["loggedInUserRoleList"].indexOf("ADMIN") > -1) {
    deleteEntityData(requestDetails, daoResults);
    200;
} else {
    403;
}', 3, 0, 2, 'admin@jquiver.com', NOW(), NOW());

REPLACE INTO jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(2139, '1f8f1cfa-14ea-489b-b9be-4c42eaf05ecc', '','select 1', 1, 1);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('5f7f40f2-7a90-11eb-9439-0242ac132202', '1f8f1cfa-14ea-489b-b9be-4c42eaf05ecc', 'delete-entity', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0);

REPLACE INTO jq_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('b2aadd32-e975-11eb-9a03-0242ac131113','system', 'system', 'file-copy-path', 'D:\\commons\\documents', 0, NOW(), 'admin', 1.43, 'To copy files when passed via rest api');



REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('4e17845c-1fab-4bf4-9330-d53b707ed2bf', 'deleteOlderSchedulerLog', 1, 'deleteOlderSchedulerLog', 'Delete Older Scheduler Log', 1, 7, 'function deleteOlderSchedulerLog(requestDetails, daoResults) {
    if(daoResults["schedulerIds"] != null) {
        var i=0;
        for(i=0; i<daoResults["schedulerIds"].length; i++ ) {
            var schedulerId = daoResults["schedulerIds"][i];
            var HashMap =  Java.type("java.util.HashMap");
            var params = new HashMap();
            params.put(''schedulerId'', schedulerId.scheduler_id);
            jq_updateDBQuery("DELETE FROM jq_job_scheduler_log WHERE scheduler_log_id NOT IN (SELECT * FROM (SELECT scheduler_log_id FROM `jq_job_scheduler_log` WHERE scheduler_id=:schedulerId ORDER BY request_time DESC LIMIT 0,50) AS t) AND scheduler_id=:schedulerId", null, params);
        }
    }
}

deleteOlderSchedulerLog(requestDetails, daoResults);', 3, 1, 2, 'admin@jquiver.com', NOW(), NOW());

REPLACE INTO jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(2353, '4e17845c-1fab-4bf4-9330-d53b707ed2bf', 'schedulerIds','select DISTINCT(scheduler_id) FROM jq_job_scheduler', 1, 1);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('4e17845c-1fab-4bf4-9330-d53b707ed2bf', '4e17845c-1fab-4bf4-9330-d53b707ed2bf', 'deleteOlderSchedulerLog', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0);

REPLACE INTO jq_job_scheduler(scheduler_id, scheduler_name, jws_dynamic_rest_id, cron_scheduler, is_active, last_modified_date, modified_by, header_json, request_param_json, scheduler_type_id, failed_notification_params) VALUES
('e1dd618b-4243-4f97-8d57-622240323985','delete-scheduler-log', '4e17845c-1fab-4bf4-9330-d53b707ed2bf', '0 0 1 ? * * *', 1, NOW(), 'admin@jquiver.io', '{}', '{}', 2, '{}');

SET FOREIGN_KEY_CHECKS=1;
