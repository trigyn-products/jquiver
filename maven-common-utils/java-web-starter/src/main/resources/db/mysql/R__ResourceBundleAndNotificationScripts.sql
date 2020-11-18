SET FOREIGN_KEY_CHECKS=0;
 
REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('561cdf55-09fa-11eb-a894-f48e38ab8cd7', 'resource-bundle-listing', '<head>
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
        <h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.multilingual'')}</h2> 
        <div class="float-right">
       
       <select id="languageOptions" onchange="changeLanguage()" class="btn btn-primary">
          <#list languageVOList as languageVO>
            <#if languageVO??>
              <#if (languageVO?api.getLanguageId())?? && languageVO?api.getLanguageId() == 1>
                <option value="${(languageVO?api.getLocaleId())!''''}" selected>${languageVO?api.getLanguageName()}</option>
              <#else>
                <option value="${(languageVO?api.getLocaleId())!''''}">${languageVO?api.getLanguageName()}</option>
              </#if>
            </#if>
          </#list>
        </select>
             <input name="addResourceBundle" class="btn btn-primary" value="Add Resource Bundle" type="button" onclick="submitForm(this)">
         <span onclick="backToWelcomePage();">
              <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
         </span>    
        </div>
        
        <div class="clearfix"></div>        
        </div>
        
        <div id="divdbResourceBundleGrid"></div>

</div>


<form action="${(contextPath)!''''}/cf/aerb" method="POST" id="formRbRedirect">
	<input type="hidden" id="resource-key" name="resource-key">
</form>
<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
	<input type="hidden" id="entityName" name="entityName" value="resource_bundle">
    <input type="hidden" id="entityId" name="entityId">
	<input type="hidden" id="moduleName" name="moduleName">
	<input type="hidden" id="moduleType" name="moduleType" value="resourceBundle">
	<input type="hidden" id="saveUrl" name="saveUrl" value="/cf/srbv">
	<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/rb">
</form>

<script>
	contextPath = "${(contextPath)!''''}";
	
	$(function () {
		let formElement = $("#formRbRedirect")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("resource-bundle-manage-details", formDataJson);
		
	    let colM = [
	        { title: "${messageSource.getMessage(''jws.resourceKey'')}", width: 130, dataIndx: "resourceKey", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "${messageSource.getMessage(''jws.languageName'')}", width: 100, dataIndx: "languageName", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
	        { title: "${messageSource.getMessage(''jws.text'')}", width: 160, dataIndx: "resourceBundleText", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editDBResource}
	    ];
	    let grid = $("#divdbResourceBundleGrid").grid({
	      gridId: "resourceBundleListingGrid",
	      colModel: colM
	  	});
  	});
	function editDBResource(uiObject) {
		const resourceKey = uiObject.rowData.resourceKey;
		const revisionCount = uiObject.rowData.revisionCount;
		
		let actionElement;
		actionElement = ''<span id="''+resourceKey+''" onclick="submitForm(this)" class= "grid_action_icons" title="${messageSource.getMessage(''jws.editResourceBundle'')}"><i class="fa fa-pencil" title=""></i></span>'';
		if(revisionCount > 1){
			actionElement = actionElement + ''<span id="''+resourceKey+''_entity" name="''+resourceKey+''" onclick="submitRevisionForm(this)" class= "grid_action_icons"><i class="fa fa-history"></i></span>''.toString();
		}else{
			actionElement = actionElement + ''<span class= "grid_action_icons disable_cls"><i class="fa fa-history"></i></span>''.toString();
		}
		return actionElement;
	}
	
	function submitForm(element) {
	  $("#resource-key").val(element.id);
	  $("#formRbRedirect").submit();
	}
	
	function submitRevisionForm(sourceElement) {
		let selectedId = sourceElement.id.split("_")[0];
		let moduleName = $("#"+sourceElement.id).attr("name")
      	$("#entityId").val(selectedId);
		$("#moduleName").val(moduleName);
      	$("#revisionForm").submit();
    }
	
	function getCookie(cname) {
	    let name = cname + "=";
	    let decodedCookie = decodeURIComponent(document.cookie);
	    let ca = decodedCookie.split(";");
	    for(var i = 0; i <ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0) == " ") {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length, c.length);
	        }
	    }
	    return "";
	}
	
	let selectedLanguageId = getCookie("locale");
	if(selectedLanguageId !== ""){
		$("#languageOptions option[value="+selectedLanguageId+"]").prop("selected","selected");
	}
  
	function changeLanguage(){
  	var localeId = $("#languageOptions").find(":selected").val();

  	$.ajax({
          async : false,
          type : "GET",
          cache : false,
          url : "/cf/cl?lang="+localeId, 
          success: function(data){
             
            setCookie("locale", localeId, 1);
            location.reload();
          }
      });
	}

	function setCookie(cname, cvalue, exdays) {
	    let d = new Date();
	    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
	    var expires = "expires="+d.toUTCString();
	    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
	}
	
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);


REPLACE INTO `template_master`(`template_id`,`template_name`,`template`,`updated_by`,`created_by`,`updated_date`, template_type_id) VALUES ('5cbb7388-09fa-11eb-a894-f48e38ab8cd7' ,'resource-bundle-manage-details','<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script> 
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script src="/webjars/1.0/resourcebundle/addEditResourceBundle.js"></script>
<style type="text/css">
.area{
  width:100%;
}
</style>
</head>
<div class="container">

		
		<div class="topband">
		<#if (resourceBundleKey)?? && (resourceBundleKey)?has_content>
		    <h2 class="title-cls-name float-left">${messageSource.getMessage("jws.editResourceBundle")}</h2> 
        <#else>
            <h2 class="title-cls-name float-left">${messageSource.getMessage("jws.addResourceBundle")}</h2> 
        </#if> 
		<div class="float-right">
			<span onclick="addEditResourceBundle.backToResourceBundleListing();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
			</span> 	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
		<form id="resourceBundleForm" method="post">
			<div class="row">
				<div class="col-12">
					<div class="col-inner-form full-form-fields">
						<label for="targetPlatform"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.resourceKey")}</label>
						<input type="text" id="resourceBundleKey" name="resourceBundleKey" class="form-control" maxlength="100" value="${(resourceBundleKey)!''''}" disabled="true" style="width:100%"/>
						</div>
					</div>
			</div>
					
					
					
			<div class="row">
				<#list languageVOList as languages>
					<div class="col-4">
						<div class="col-inner-form full-form-fields">
							<#if (languages.languageId) == 1>
								<label for="targetPlatform"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.resource")} </label>
								<label>${languages.languageName}</label>
							<#else>
								<label for="targetPlatform">${messageSource.getMessage("jws.resource")} </label>
								<label>${languages.languageName}</label>
							</#if>
							<textarea id="textBx_${languages.languageId}" name="${languages.localeId}" rows="8" class="area">${(resourceBundleVOMap?api.get(languages.languageId).getText())!''''}</textarea>
						</div>							  
					</div>
				</#list>
			</div>
		</form>
				
	<div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropdown custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''resource-bundle-manage-details'', this, addEditResourceBundle.saveResourceBundle.bind(addEditResourceBundle), addEditResourceBundle.backToResourceBundleListing);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls"  id="actionDiv">
                    	<ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="typeOfAction(''resource-bundle-manage-details'', this, addEditResourceBundle.saveResourceBundle.bind(addEditResourceBundle), addEditResourceBundle.backToResourceBundleListing);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="typeOfAction(''resource-bundle-manage-details'', this, addEditResourceBundle.saveResourceBundle.bind(addEditResourceBundle), addEditResourceBundle.backToResourceBundleListing);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                        </ul>
                    </div> 
                </div>
				<span onclick="addEditResourceBundle.backToResourceBundleListing();">
					<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
				</span> 
			</div>
		</div>
	</div>
	

 </div>
 <script>
	contextPath = "${(contextPath)!''''}";
	let resourceBundleKey = "${(resourceBundleKey)!''''}";
	var resourceBundleFormData = new Array();
	let addEditResourceBundle;
	$(function() {
	 	addEditResourceBundle = new AddEditResourceBundle(resourceBundleFormData);
		addEditResourceBundle.loadAddEditResourceBundlePage();
		savedAction("resource-bundle-manage-details", resourceBundleKey);
		hideShowActionButtons();
	});
</script>','aar.dev@trigyn.com','aar.dev@trigyn.com',NOW(), 2);




REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('70c2153c-09fa-11eb-a894-f48e38ab8cd7', 'notification-listing', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="/webjars/1.0/pqGrid/pqgrid.min.js"></script>     
<script src="/webjars/1.0/pqGrid/pqgrid.min.js"></script>     
<script src="/webjars/1.0/gridutils/gridutils.js"></script> 
<link rel="stylesheet" href="/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>
<div class="container">
	<div class="topband">
	    <h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.notification'')}</h2> 
    	<div class="float-right">

            <form id="addEditNotification" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
<input type="hidden" name="formId" value="e848b04c-f19b-11ea-9304-f48e38ab9348"/>
<input type="hidden" name="primaryId" id="primaryId" value=""/>

 

<button type="submit" class="btn btn-primary">
                Add Notification
            </button>
</form>

	

        	<button type="button" class="btn btn-primary" onclick = "loadNotifications();" data-toggle="modal" data-target="#exampleModalCenter">
				Notifications
			</button>
             <span onclick="backToWelcomePage();">
              	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
             </span>	
	    </div>
	
	    <div class="clearfix"></div>		
	</div>
	
	<div id="divNotificationListing"></div>
</div>
<div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">Current Notification</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span>
        </button>
      </div>
      <div class="modal-body">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
<form action="${(contextPath)!''''}/cf/aen" method="GET" id="formNFRedirect">
	<input type="hidden" id="notificationId" name="notificationId">
</form>
<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
	<input type="hidden" id="entityName" name="entityName" value="generic_user_notification">
    <input type="hidden" id="entityId" name="entityId">
	<input type="hidden" id="moduleName" name="moduleName">
	<input type="hidden" id="moduleType" name="moduleType" value="notification">
	<input type="hidden" id="formId" name="formId" value="e848b04c-f19b-11ea-9304-f48e38ab9348">
	<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/nl">
</form>
<script>
	contextPath = "${(contextPath)!''''}";
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
	$(function () {
		let formElement = $("#addEditNotification")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("notification-add-edit", formDataJson);
		
		let colM = [
	    	{ title: "",hidden: true, dataIndx: "notificationId" },
	        { title: "Target Platform", width: 100,  dataIndx: "targetPlatform", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
	        { title: "Message Type", width: 160, dataIndx: "messageType", align: "left", halign: "center", 
	        	filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "Message Text", width: 200, dataIndx: "messageText", align: "left", halign: "center",
	          filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
	        { title: "Message Valid From", width: 100, dataIndx: "validFrom" , align: "left", halign: "center",
	          filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
	        { title: "Message Valid Till", width: 100, dataIndx: "validTill" , align: "left", halign: "center",
	          filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
	        { title: "Selection Criteria", width: 100, dataIndx: "selectionCriteria" , align: "left", halign: "center",
	          filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
	        { title: "Updated By", width: 100, dataIndx: "updatedBy", align: "left", halign: "center",
	          filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
	        { title: "Updated Date", width: 100, dataIndx: "updatedData", align: "left", halign: "center",
	          filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
	        { title: "Action", width: 100, dataIndx: "action",align: "center", halign: "center",render: editNotificationFormatter },
		];

	  let grid = $("#divNotificationListing").grid({
	      gridId: "notificationDetailsListing",
	      colModel: colM
	  });
});

	function editNotificationFormatter(uiObject) {
		const notificationId = uiObject.rowData.notificationId;
		const targetPlatform = uiObject.rowData.targetPlatform;
		const revisionCount = uiObject.rowData.revisionCount;
		
		let actionElement;
		actionElement = ''<span id="''+notificationId+''" onclick="editNotification(this)" class= "grid_action_icons"><i class="fa fa-pencil" title=""></i></span>'';
		if(revisionCount > 1){
			actionElement = actionElement + ''<span id="''+notificationId+''_entity" name="''+targetPlatform+''" onclick="submitRevisionForm(this)" class= "grid_action_icons"><i class="fa fa-history"></i></span>''.toString();
		}else{
			actionElement = actionElement + ''<span class= "grid_action_icons disable_cls"><i class="fa fa-history"></i></span>''.toString();
		}
		return actionElement;
	}


function loadNotifications() {
	$.ajax({
				
				async : false,
				type : "GET",
				cache : false,
				url :  contextPath+"/cf/lns?contextName=tsms", 
				data : {
					
				},
				success : function(data) {
					if(data!=""){
					  $(".modal-body").html(data);
				  	}else{ 
					  $(".modal-body").html("No notifications found");
				  	}
        		}
			});
}
function editNotification(thisObj){
	$("#primaryId").val(thisObj.id);
	$("#addEditNotification").submit();
}

	function submitRevisionForm(sourceElement) {
		let selectedId = sourceElement.id.split("_")[0];
		let moduleName = $("#"+sourceElement.id).attr("name")
      	$("#entityId").val(selectedId);
		$("#moduleName").val(moduleName);
      	$("#revisionForm").submit();
    }
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);
 
 REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('79d3522a-09fa-11eb-a894-f48e38ab8cd7', 'loadNotifications', '<div>
	<ul class="notification_list">
		<#list notifications as currentNotf >
			<li>
				<#if  currentNotf?api.getMessageType() == "Error" >
					<div class="notification_list_div">                             
				<#elseif    currentNotf?api.getMessageType() == "Informative" >
					<div class="notification_list_div">
				<#elseif   currentNotf?api.getMessageType() == "Warning" >
					<div class="notification_list_div">                                            
				</#if>
						${ currentNotf?api.getMessageText() }
					</div>                   
			</li>
		</#list>
	</ul>
</div>
', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2); 

REPLACE INTO dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id) VALUES
('e848b04c-f19b-11ea-9304-f48e38ab9348', 'notification', 'notification add/edit', 'select * from generic_user_notification where notification_id="${(primaryId)!''''}"', '<head>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<link rel="stylesheet" type="text/css" href="/webjars/1.0/JSCal2/css/jscal2.css" />
<link rel="stylesheet" type="text/css" href="/webjars/1.0/JSCal2/css/border-radius.css" />
<link rel="stylesheet" type="text/css" href="/webjars/1.0/JSCal2/css/steel/steel.css" />
<script type="text/javascript" src="/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="/webjars/1.0/JSCal2/js/lang/en.js"></script>
<script type="text/javascript" src="/webjars/1.0/dropzone/dist/dropzone.js"></script>
<link rel="stylesheet" type="text/css" href="/webjars/1.0/dropzone/dist/dropzone.css" />
<script type="text/javascript" src="/webjars/1.0/fileupload/fileupload.js"></script>

</head>
<div class="container">
	<div class="topband">
		<#if (resultSet)?? && (resultSet)?has_content>
		    <h2 class="title-cls-name float-left">Edit Notification</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Add Notification</h2> 
        </#if>  
		<div class="float-right">
                                             
		<span onclick="backToPreviousPage();">
			<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
		</span>              
	</div>
                              
	<div class="clearfix"></div>                         
	</div>

<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
<form method="post" name="genericNotificationForm" id="genericNotificationForm">
	<input type="hidden" id="notificationId" name="notificationId"/>
		<div class="row">
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="fromDate">From </label>
					<span id="fromDateTd">
						<input id="fromDate" name= "fromDate" class="form-control" placeholder="From Date" /><button id="fromDate-trigger" class="calender_icon"><i class="fa fa-calendar" aria-hidden="true"></i></button>
					</span>
				</div>
			</div>
                                                                                                        
                                                                                                        
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="toDate">To </label>
					<span id="toDateTd">
						<input class="form-control" id="toDate"  name= "toDate" placeholder="To Date" /><button id="toDate-trigger" class="calender_icon"><i class="fa fa-calendar" aria-hidden="true"></i></button>
					</span>
				</div>
			</div>
                                                                                                                                      
                              
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="targetPlatform">Target Platform </label>
						<select class="form-control" id="targetPlatform" title="Target Platform" name="targetPlatform" onchange="populateOptions();">
							<option value="" selected="selected"> --- select --- </option>
                            <option value="wipo">WIPO</option>
                            <option value="tsms">TSMS</option>
							<option value="un">UN</option>
                        </select>
				</div>
			</div>
                                                                                                        
                                                                                                         
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="messageType">Message Type  </label>
						<select class="form-control" id="messageType" name="messageType" title="Message Type">
							<option value="" selected="selected"> --- select --- </option>
							<option value="Informative">Informative</option>
							<option value="Warning">Warning</option>
							<option value="Error">Error</option>
						</select>
				</div>
			</div>
                                                                                                         
                                                                                                         
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="selectionCriteria">Selection Criteria</label>
						<textarea class="form-control" rows="15" cols="60" title="Select Criteria" id="selectionCriteria" placeholder="Select Criteria" name="selectionCriteria" style="height:80px"></textarea>
				</div>
			</div>
                                                                                                        
                                                                                                         
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="messageText"> Message Text :</label>
						<textarea class="form-control" rows="15" cols="60" title="Message Text" id="messageText" name="messageText" style="height:80px"></textarea>
				</div>
			</div>
                                                                                                         
                                                                                                         
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="messageFormat"> Message Format</label>
					<select class="form-control" id="messageFormat" name="messageFormat">
						<option value="Text/Plain" selected>Text/Plain</option>
						<option value="Text/HTML">Text/HTML</option>
					</select>
				</div>
			</div>  
	</div>
                              
                              
	<div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropdown custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''notification-add-edit'', this);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                        </div>
                        <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                        <div class="dropdown-menu action-cls"  id="actionDiv">
                            <ul class="dropdownmenu">
                                <li id="saveAndCreateNew" onclick="typeOfAction(''notification-add-edit'', this);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                                <li id="saveAndEdit" onclick="typeOfAction(''notification-add-edit'', this);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                            </ul>
                        </div>  
                    </div>
				<span onclick="backToPreviousPage();">
					<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
				</span> 
			</div>
		</div>
	</div>
	
		<input type="hidden" id="primaryKey" name="primaryKey">
		<input type="hidden" id="entityName" name="entityName" value="generic_user_notification">
</form>   

</div>
        

    
               
 
<script>
contextPath = "${(contextPath)!''''}";
let formId = "${formId}";
let isEdit = 0;

$(function() {
	
	Calendar.setup({
			trigger    : "fromDate-trigger",
			inputField : "fromDate",
			dateFormat : "%d-%b-%Y",
			weekNumbers: true,
            showTime: 12,
			onSelect   : function() { 
				let selectedDate = this.selection.get();
				let date = Calendar.intToDate(selectedDate);
				date = Calendar.printDate(date, "%d-%b-%Y");
				$("#"+this.inputField.id).val(date);
				this.hide(); 
			}
		});
		Calendar.setup({
			trigger    : "toDate-trigger",
			inputField : "toDate",
			dateFormat : "%d-%b-%Y",
			weekNumbers: true,
            showTime: 12,
			onSelect   : function() {
				let selectedDate = this.selection.get();
				let date = Calendar.intToDate(selectedDate);
				date = Calendar.printDate(date, "%d-%b-%Y");
				$("#"+this.inputField.id).val(date);
				this.hide(); 
			}
		});



	<#if (resultSet)?? && resultSet?has_content>
		<#list resultSet as resultSetList>
			isEdit = 1;
			$("#targetPlatform option[value=''${resultSetList?api.get("target_platform")}'']").attr("selected", "selected");
			$("#messageFormat option[value=''${resultSetList?api.get("message_format")}'']").attr("selected", "selected");		
			$("#messageType option[value=''${resultSetList?api.get("message_type")}'']").attr("selected", "selected");
			$("#fromDate").val(Calendar.printDate(Calendar.parseDate(''${resultSetList?api.get("message_valid_from")}'',false),"%d-%b-%Y"));
			$("#toDate").val(Calendar.printDate(Calendar.parseDate(''${resultSetList?api.get("message_valid_till")}'',false),"%d-%b-%Y"));
			$("#messageText").val(''${resultSetList?api.get("message_text")}'');
			$("#selectionCriteria").val(''${resultSetList?api.get("selection_criteria")}''); 
			$("#notificationId").val(''${resultSetList?api.get("notification_id")}'');
			$("#primaryKey").val(''${resultSetList?api.get("notification_id")}'');
		</#list>
	<#else>
		const generatedNotificationId = uuidv4();
		$("#notificationId").val(generatedNotificationId);
		$("#primaryKey").val(generatedNotificationId);
	</#if>

    
	savedAction("notification-add-edit", isEdit);
	hideShowActionButtons();
});  

function saveData (){
    let isDataSaved = false;
	if(validateFields() == false){
        $("#errorMessage").show();
        return false;
    }
	$("#errorMessage").hide();
	const form = $("#genericNotificationForm");
	let serializedForm = form.serializeArray();
	for(let iCounter =0, length = serializedForm.length;iCounter<length;iCounter++){
		serializedForm[iCounter].value = $.trim(serializedForm[iCounter].value);
	}
	serializedForm = $.param(serializedForm);
	let formData = serializedForm + "&formId="+formId;
    
    $.ajax({
	  type : "POST",
	  url : "sdf",
	  async: false,
	  data : formData, 
	  success : function(data) {
		isDataSaved = true;
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
    const fromDate = $("#fromDate").val();
	if(fromDate == ""){
		$("#errorMessage").html("Invalid From date");
		return false;
	}
    const toDate = $("#toDate").val();
	if(toDate == ""){
		$("#errorMessage").html("Invalid To date");
		return false;
	}
    const targetPlatform = $("#targetPlatform").val();
    if(targetPlatform == ""){
		$("#errorMessage").html("Please select target platform");
		return false;
	}
	const messageType = $("#messageType").val();
	if(messageType == ""){
		$("#errorMessage").html("Please select message type");
		return false;
	}
    const selectionCriteria = $("#selectionCriteria").val().trim();
	if(selectionCriteria == ""){
		$("#errorMessage").html("Please enter selection criteria");
		return false;
	}
    const messageText = $("#messageText").val().trim();
	if(messageText == ""){
		$("#errorMessage").html("Please enter message text");
		return false;
	}
    const messageFormat = $("#messageFormat").val();
	if(messageFormat == ""){
		$("#errorMessage").html("Please select message format");
		return false;
	}
    return true;
}

function backToPreviousPage(){
	window.location.href=contextPath+"/cf/nl";
}     
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2);

REPLACE INTO dynamic_form_save_queries(dynamic_form_query_id ,dynamic_form_id  ,dynamic_form_save_query  ,sequence,checksum) VALUES (
   'daf459b9-f82f-11ea-97b6-e454e805e22f' ,'e848b04c-f19b-11ea-9304-f48e38ab9348' ,'
REPLACE INTO generic_user_notification (
   notification_id
  ,target_platform
  ,message_valid_from
  ,message_valid_till
  ,message_text
  ,message_type
  ,message_format
  ,selection_criteria
  ,created_by
  ,creation_date
  ,updated_by
  ,updated_date
) VALUES (
   ''${formData?api.getFirst("notificationId")}''   
  ,''${formData?api.getFirst("targetPlatform")}'' 
  , STR_TO_DATE( "${formData?api.getFirst("fromDate") }","%d-%b-%Y") 
  ,STR_TO_DATE( "${formData?api.getFirst("toDate") }","%d-%b-%Y") 
  ,''${formData?api.getFirst("messageText")}'' 
  ,''${formData?api.getFirst("messageType")}''
  ,''${formData?api.getFirst("messageFormat")}''
  ,''${formData?api.getFirst("selectionCriteria")}'' 
  ,''admin''
  ,now()  
  ,''admin''  
  ,now()  
);',1,null);

SET FOREIGN_KEY_CHECKS=1;