SET FOREIGN_KEY_CHECKS=0;

 
replace into dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id) VALUES
('193d770c-1217-11eb-980f-802bf9ae2eda', 'mail-configuration-form', 'Mail configuration form', 'select *, (SELECT COUNT(*) FROM failed_mail_history) as totalFailedMails from jws_property_master where property_name = "mail-configuration"', '<head>
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
<style>

.hm-property {
    margin-top: 10px;
  }

  .hm-fieldset {
    margin-top: 12px;
    margin-bottom:20px;
    border: 1px groove #ddd !important;
    padding: 0 10px 10px 10px !important;
  }

  .hm-legend {
    font-size: 15px;
    font-weight: normal;
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    font-size: 14px;
    color: #555;
    border-bottom: none;
    padding: 0 10px;
    width: auto;
  }
  
.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 18px;
}

.switch input { 
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: -10px;
  left: 9px;
  right: -7px;
  bottom: 0px;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 15px;
  width: 15px;
  left: 4px;
  bottom: 7px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

/* .mailForm {
  width: 55%;
} */
</style>

<div class="container">
  <div class="topband">
    <#if (resultSet)?? && (resultSet)?has_content>
        <h2 class="title-cls-name float-left">Edit Mail Configuration</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Mail Configuration</h2> 
        </#if>
    <div class="clearfix"></div>    
  </div>
  
  <div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>  
  <form method="post"   name="addEditForm" id="addEditForm" >
    
    
    <input type="hidden" id="mailConfigJson" name="mailConfigJson" value="">
    <input type="hidden" id="totalFailedMails" name="totalFailedMails" value="">
    <div class="row">
      <div class="col-4">
        <div class="col-inner-form full-form-fields">
          <label for="smtpHost" style="white-space:nowrap"><span class="asteriskmark">*</span>SMTP Host</label>
          <input type="text" id="smtpHostId" name="smtpHost" value="" placeholder="SMTP Host" required  class="form-control mailForm">
        </div>
      </div>
      
      <div class="col-4 ">
        <div class="col-inner-form full-form-fields">
          <label for="smtpPort" style="white-space:nowrap"><span class="asteriskmark">*</span>SMTP Port</label>
          <input type="text" id="smtpPortId" name="smtpPort" value="" placeholder="SMTP Port" required  class="form-control mailForm">
        </div>
      </div>
      
      <div class="col-4">
        <div class="col-inner-form full-form-fields">
          <label for="smtpSecurityProtocal" style="white-space:nowrap">Security Protocal</label>
          <select id="smtpSecurityProtocalId" name="smtpSecurityProtocal" onchange="populateFields(this)" class="form-control">
              <option value="TLS">TLS</option> 
              <option value="SSL">SSL</option> 
            </select>
        </div>
      </div>
      
      <div class="col-12"> 
        <fieldset class="hm-fieldset">
         <legend class="hm-legend"> Authentication <label class="switch"> 
        <input type="checkbox" id="isSmtpAutheticatedId" onclick="authenticationChecked($(this));">
        <span class="slider round"></span>
     </label></legend>
      
      
      <div class="col-6 float-right">
        <div class="col-inner-form full-form-fields">
  
          <label for="password" style="white-space:nowrap"><span class="asteriskmark">*</span>Password</label>
          <input type="password" id="password" name="password" value=""  placeholder="Password" required class="form-control mailForm RemoveData">
        </div>
      </div>
      
      <div class="col-6">
        <div class="col-inner-form full-form-fields">
                  <label for="userName" style="white-space:nowrap"><span class="asteriskmark">*</span>User Name </label>
          <input type="text" id="userName" name="userName" value="" placeholder="Username"  required class="form-control mailForm RemoveData">
        </div>
      </div>
      </fieldset>
      </div>
      
      
      <div class="col-6 float-right">
        <div class="col-inner-form full-form-fields">
          <label for="email" style="white-space:nowrap"><span class="asteriskmark">*</span>  From-EMail  </label>
          <input type="email" id="mailFrom" name="mailFrom" value=""  placeholder="EMail" required class="form-control mailForm"  >
        </div>
      </div>
      
       <div class="col-6">
        <div class="col-inner-form full-form-fields">
          <label for="mailFromName" style="white-space:nowrap"><span class="asteriskmark">*</span>From-Name</label>
          <input type="text" id="fromNameId" name="mailFromName" value=""  placeholder="Name" required class="form-control mailForm">
        </div>
      </div>
      
      <div class="col-6">
        <div class="col-inner-form full-form-fields">
          <label for="replyToEmailDropdown" style="white-space:nowrap">Reply To</label>
          <select id="replyToEmailDropdown" name="replyToEmailDropdown"  class="form-control mailForm">
              <option value="Same">Same</option> 
              <option value="NoReply">No-Reply</option> 
              <option value="Different">Different</option>
            </select>
        </div>
      </div>
      
       <div class="col-6">
        <div class="col-inner-form full-form-fields">
          <label for="email" style="white-space:nowrap">Reply To Email Id</label>
          <input type="email" id="replyToDifferentEmailId" name="replyToDifferentEmailId" placeholder="EMail" value=""  required class="form-control mailForm">
        </div>
      </div>
      
      <div  class="col-12"> 
      <fieldset class="hm-fieldset"  style="height: 163px;">
         <legend class="hm-legend">
      Mail Footer <label class="switch"> 
        <input type="checkbox" id="isMailFooterRequiredId" onclick="mailFooterReuired($(this));">
        <span class="slider round"></span>
     </label></legend>
     
      
      
      <div class="col-12">
        <div class="col-inner-form full-form-fields">
          
          <textarea id="mailFooterId" name="mailFooter" rows="4" placeholder="Footer text will be appended to all outgoing mail" cols="50" class="form-control" > </textarea> 
        </div>
      </div></fieldset>
      </div>
      <div class="col-6">
        <div class="col-inner-form full-form-fields">
          <label for="internetAddressToArray" style="white-space:nowrap">Send test mail to</label>
          <input type="text" id="internetAddressToArray" name="internetAddressToArray" placeholder="Comma seperated email addresses" value=""  required class="form-control">
        </div>
      </div>
      <div class="col-3">
      <div class="col-inner-form full-form-fields">
        <label for="noOfFailedMails" style="white-space:nowrap; margin-bottom:20px;">
                    <span class="asteriskmark">*</span>No of failed emails (between 10 and 100):
                </label>
                <input type="range" id="noOfFailedMails" name="noOfFailedMails" value="50" min="10" max="100" onchange="showSelectedValue(this.value)">
                <span id="noOfFailedMailsValue" class="no-of-files-counter"></span>
      </div>
    </div>
      
      
    </div>
    <!-- Your form fields end -->
    
    
  </form>
  <div class="row margin-t-10">
        
    <div class="col-12">
      <div class="float-right">
        <div class="btn-group dropup custom-grp-btn">
            <div id="sendTestMail">
                <button type="button" id="sendTestMailId" class="btn btn-primary" onclick="sendMail()">${messageSource.getMessage("jws.sendTestMail")}</button>
             </div>   &nbsp;  
             <div id="savedAction">
                <button type="button" id="saveConfigurationId" class="btn btn-primary" onclick="saveData()">${messageSource.getMessage("jws.saveMailConfiguration")}</button>
             </div> 
              </div>
        <span onclick="backToPreviousPage();">
          <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
        </span> 
      </div>
    </div>
  </div>
    <br> 
<div id="failedMailsListingGrid"></div>
</div>



<script>

  contextPath = "${contextPath}";
  let formId = "${formId}";
  let edit = 0;
    var saveMailConfigDetailsJson=[]; 
    function showSelectedValue(value){
        $("#noOfFailedMailsValue").html(value);
        $("#noOfFailedMailsValue").show();
    }
  
    function sendMail(){

    if(validateFields() == false){
        $("#errorMessage").show();
        return false;
    }

    var jsonobject =getJsonObject();

    saveMailConfigDetailsJson=JSON.stringify(jsonobject);
   
    $.ajax({
            type : "POST",
            async: false,
            url : contextPath+"/cf/stm",

            data : {
                saveMailConfigDetailsJson :saveMailConfigDetailsJson
            },
            success : function(data) {
                if(data){
                    showMessage("You will recieve test mail shortly", "success");
                } else {
                    $("#failedMailsListingGrid").trigger( "reloadGrid" );
                    showMessage("Error occurred while sending mail", "error");
                }
            },
            error : function(xhr, error){
                $("#failedMailsListingGrid").trigger( "reloadGrid" );
                showMessage("Error occurred while sending mail", "error");
            },
        });
        return true;

    }

  function getJsonObject(){
        var jsonobject={};
        jsonobject["smtpHost"]=$("#smtpHostId").val().trim();
        jsonobject["smtpPort"]=$("#smtpPortId").val().trim();
        jsonobject["smtpSecurityProtocal"]=$("#smtpSecurityProtocalId").val();
        jsonobject["userName"]=$("#userName").val().trim();
        jsonobject["password"]=$("#password").val().trim();
        jsonobject["mailFrom"]=$("#mailFrom").val().trim();
        jsonobject["mailFromName"]=$("#fromNameId").val().trim();
    
    
        if ($("#replyToEmailDropdown").val()=="Different"){
            jsonobject["isReplyToDifferentMail"]=true;
            jsonobject["replyToDifferentMailId"]=$("#replyToDifferentEmailId").val().trim();
        }
        
        jsonobject["mailFooter"]=$("#mailFooterId").val().trim();
        jsonobject["internetAddressToArray"]=$("#internetAddressToArray").val();
        jsonobject["isAuthenticated"]=$("#isSmtpAutheticatedId").is(":checked");
        jsonobject["isMailFooterEnabled"]=$("#isMailFooterRequiredId").is(":checked");
        jsonobject["failedMailCounter"]=$("#noOfFailedMails").val();
        return jsonobject;
    }

  function saveData (){
  
    let isDataSaved = false;
      if(validateFields() == false){
          $("#errorMessage").show();
          return false;
      }
  
  var jsonobject =getJsonObject();
  
 
   saveMailConfigDetailsJson=JSON.stringify(jsonobject);
 
    $.ajax({
      type : "POST",
      async: false,
      url : contextPath+"/api/mailConfirationDetails",
      data : {
      saveMailConfigDetailsJson : saveMailConfigDetailsJson
      },
          success : function(data) {
          
            isDataSaved = true;
      showMessage("Information saved successfully", "success");
      },
        error : function(xhr, error){
      showMessage("Error occurred while saving", "error");
        },
      });
      return isDataSaved;
  }
  
    function validateFields(){
        const smtpHostId = $("#smtpHostId").val().trim();
        const smtpPortId = $("#smtpPortId").val().trim();
       
        const mailFrom = $("#mailFrom").val().trim();
        const mailFromName = $("#fromNameId").val().trim();
   
        if($("#isSmtpAutheticatedId").is(":checked")){
            const userName = $("#userName").val().trim();
            const passwordIs = $("#password").val().trim();
            if(userName == "" || passwordIs == ""){
            $("#errorMessage").html("Please enter user name and password");
                return false;
            }
        }       
        
        if(smtpHostId == "" || smtpPortId == "" ||  mailFrom == "" || mailFromName == ""){
            $("#errorMessage").html("Please enter data for mandatory fields");
        return false;
        }    
       
        const emailExpression =  /[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}$/; 
        
        if( !emailExpression.test(mailFrom)){
            $("#errorMessage").html("Please enter valid email id into from email");
            showMessage("Please enter valid email id into from email", "error");
            $("#mailFrom").focus();
        return false;
        }
        
        const emailList = $("#internetAddressToArray").val();

        if( emailList == "" || emailList == undefined || emailList.trim() == undefined || !validateEmailsList(emailList)){
            $("#errorMessage").html("Please enter valid email ids with comma sepearated");
            showMessage("Please enter valid email ids with comma sepearated", "error");
            $("#internetAddressToArray").focus();
        return false;
        }
              
        
        return true;
    }
    
    function validateEmailsList(emailList) {
        var emails = emailList.replace(/s/g,'''').split(",");
        var valid = true;
        const emailExpression = /[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}$/;
        for (var i = 0; i < emails.length; i++) {
            if( emails[i] == "" || !emailExpression.test(emails[i])){
                valid = false;
            }
        }
        return valid;
    }
  
  function backToPreviousPage() {
    location.href = contextPath+"/cf/pml"
  }
  
  function authenticationChecked(t){
  
  
  if (t.is(":checked")) {
        $(".RemoveData").removeAttr("disabled", "disabled"); 
        $("#userName").attr("placeholder", "Username"); 
        $("#password").attr("placeholder", "Password"); 
        $("#userName").focus();
        
    } else {
        $(".RemoveData").attr("placeholder", ""); 
        $(".RemoveData").val("");    
        $(".RemoveData").attr("disabled", "disabled");    
      }
  }
  
    function mailFooterReuired(t){
        if (t.is(":checked")) {
            $("#mailFooterId").removeAttr("disabled", "disabled"); 
            $("#mailFooterId").attr("placeholder", "Footer text will be appended to all outgoing mail"); 
            $("#mailFooterId").focus();        
        } else {
            $("#mailFooterId").attr("placeholder", ""); 
            $("#mailFooterId").val("");    
            $("#mailFooterId").attr("disabled", "disabled");    
        }
    }
  
  
  
    function disableEanbleTextBoxes(){
        if ($("#replyToEmailDropdown").val()=="Different"){
            $("#replyToDifferentEmailId").removeAttr("disabled", "disabled"); 
            $("#replyToDifferentEmailId").attr("placeholder", "EMail");
            $("#replyToDifferentEmailId").focus();
            $("#replyToDifferentEmailId").select();
        }else{
            $("#replyToDifferentEmailId").val("");
            $("#replyToDifferentEmailId").attr("placeholder", "");
            $("#replyToDifferentEmailId").attr("disabled", "disabled"); 
        }
    }
  
    $("#replyToEmailDropdown").change(function() {
            disableEanbleTextBoxes();
    });
    
    
  
  $(function() {      
         <#if (resultSet)??>
          <#list resultSet as resultSetList>
            $("#mailConfigJson").val(''${resultSetList?api.get("property_value")}'');       
                $("#totalFailedMails").val(''${resultSetList?api.get("totalFailedMails")}'');   
          </#list>
        </#if>
        
        if($("#mailConfigJson").val() !="" ){
        var jsonObject = JSON.parse($("#mailConfigJson").val());
        $("#smtpHostId").val(jsonObject.smtpHost); 
        $("#smtpPortId").val(jsonObject.smtpPort);
      
        $("#mailFrom").val(jsonObject.mailFrom);
        $("#fromNameId").val(jsonObject.mailFromName);

        
        $("#internetAddressToArray").val(jsonObject.internetAddressToArray);
       
        $("#replyToDifferentEmailId").val(jsonObject.replyToDifferentMailId);
        
        if(jsonObject.isReplyToDifferentMail == true){
            $("#replyToEmailDropdown").val("Different");  
        }
        
    $("#smtpSecurityProtocalId").val(jsonObject.smtpSecurityProtocal);
        $("#smtpSecurityProtocalId").val(jsonObject.smtpSecurityProtocal);
        $("#noOfFailedMails").val(jsonObject.failedMailCounter);
        $("#noOfFailedMailsValue").html(jsonObject.failedMailCounter);
        if(jsonObject.isAuthenticated ==true){
            $("#isSmtpAutheticatedId").prop("checked", true);
            $("#userName").val(jsonObject.userName);
      $("#password").val(jsonObject.password);
        }else{
            $(".RemoveData").attr("placeholder", ""); 
            $(".RemoveData").val("");
            $(".RemoveData").attr("disabled", "disabled");
            $("#isSmtpAutheticatedId").prop("checked", false);
        }
       
        if(jsonObject.isMailFooterEnabled ==true){
            $("#isMailFooterRequiredId").prop("checked", true);
            $("#mailFooterId").val(jsonObject.mailFooter);
        }else{
            $("#mailFooterId").val("");
            $("#mailFooterId").attr("placeholder", "");
            $("#mailFooterId").attr("disabled", "disabled");
            $("#isMailFooterRequiredId").prop("checked", false);
         
        }
      } else {
            $("#smtpHostId").val("localhost"); 
            $("#smtpPortId").val("25");
            $("#mailFrom").val("admin@jquiver.com");
            $("#fromNameId").val("Admin");
            $("#internetAddressToArray").val("admin@jquiver.com");
            $("#noOfFailedMailsValue").html("50");
            $(".RemoveData").attr("placeholder", ""); 
            $(".RemoveData").val("");    
            $(".RemoveData").attr("disabled", "disabled");
            $("#mailFooterId").attr("placeholder", ""); 
            $("#mailFooterId").val("");    
            $("#mailFooterId").attr("disabled", "disabled"); 
    }
        
       
        
        
        let edit = 0;
        <#if (resultSet)?? && resultSet?has_content>
          edit = 1;
        </#if>
        
        disableEanbleTextBoxes();
          var colM = [
          { dataIndx: "failed_mail_id",hidden: true },
      { title: "Mail Sent By ", width: 130, dataIndx: "mail_sent_by" , align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
      { title: "Mail Sent To", width: 130, dataIndx: "mail_sent_to", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
      { title: "Mail Failed Time", width: 100, dataIndx: "mail_failed_time" , align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
       
        
        
      { title:"Eml File action", width: 50, dataIndx: "action", align: "center", halign: "center", render: downloadEmail}
       
       ];
  
    var grid = $("#failedMailsListingGrid").grid({
      gridId: "failedMailsGrid",
      colModel: colM
    });

    function downloadEmail(uiObject) {
        return ''<span  onclick="downloadFile(this)" ><i class="fa fa-download"  title="Download Email"></i></span>''.toString();
  }
  
    function downloadFile(element){
        
    }
        
        
        
  });
</script>', 'admin', NOW(), NULL, NULL, 2);
  
  
REPLACE INTO dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('a209c7b9-242d-11eb-9c67-f48e38ab8cd7', '193d770c-1217-11eb-980f-802bf9ae2eda', '
UPDATE jws_property_master SET
last_modified_date = NOW()
WHERE property_master_id = "mail-configuration";', 1, NULL);

replace into jws_dynamic_rest_details
(jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_dynamic_rest_type_id) VALUES
('a3caf8fd-1aa8-11eb-a009-e454e805e22f', 'mailConfirationDetails', 1, 'saveMailConfigDetails', 'Get mail json details', 1, 7, '', 2, 2);


replace into jws_dynamic_rest_dao_details
(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(24, 'a3caf8fd-1aa8-11eb-a009-e454e805e22f', 'saveMailConfigDetailsJson', 'REPLACE INTO jws_property_master (property_master_id,owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES (''9d08d633-1f4b-11eb-947d-f48e38ab9348'',''system'', ''system'', ''mail-configuration'', :saveMailConfigDetailsJson, 0, NOW(), ''admin'', 1.00, ''mail Config Details'');', 1, 2);

REPLACE INTO grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type,grid_type_id) VALUES
('failedMailsGrid', 'Failed mails listing', 'Failed mail listing grid', 'mail_history_data', '*', 1,2);

replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8175b1d5710175b1d938fb0000', 'mailTemplate', 'This is test mail body template', 'admin', 'admin', NOW(), NULL, 2);

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum,template_type_id) VALUES
('9378ee23-09fa-11eb-a894-f48e38ab8cdga', 'google-analytics-template', '

<#if innerTemplateObj.enableGoogleAnalytics ?? && innerTemplateObj.googleAnalyticsKey?? 
	&& innerTemplateObj.enableGoogleAnalytics=="true">
					
	<script async src="https://www.googletagmanager.com/gtag/js?id=${innerTemplateObj.googleAnalyticsKey}"></script>
	<script>
	window.dataLayer = window.dataLayer || [];
	function gtag(){dataLayer.push(arguments);}
	gtag("js", new Date());

	gtag("config", "${innerTemplateObj.googleAnalyticsKey}", {
		"page_title" : "Your custom title",
		"page_path": "/${innerTemplateObj.entityType}/${innerTemplateObj.entityName}"
	});

	</script>
</#if>
', 'satishchandra.pandey@trigyn.com', 'satishchandra.pandey@trigyn.com', NOW(), NULL,2);

UPDATE dynamic_form SET form_type_id=2 WHERE form_id='8a80cb81754acbf701754ae3d1c2000c';
UPDATE template_master SET template_type_id=2 WHERE template_id='8a80cb81754acbf701754ae3d1dd000e';
UPDATE template_master SET template_type_id=2 WHERE template_id='8a80cb8175479e47017547d34a8d001c';
UPDATE template_master SET template_type_id=2 WHERE template_id='8a80cb8175513bc80175514206ef0000';
UPDATE template_master SET template_type_id=2 WHERE template_id='8a80cb8175bbf5d00175bbf6b3dc0000';
UPDATE template_master SET template_type_id=2 WHERE template_id='8a80cb8175bbf5d00175bbf8be160005';

replace into grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type,grid_type_id) VALUES
('manual-entryGrid', 'manual-entryGrid', 'manual-entry Listing', 'manual_entry', 'manual_entry_id,manual_type,entry_name,entry_content,sort_index,last_modified_on,last_updated_by', 1,2), 
('manual-typeGrid', 'manual-typeGrid', 'manual-type Listing', 'manual_type', 'manual_id,name,is_system_manual', 1,2);

REPLACE INTO autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id) VALUES
('dashboardListing', 'Dashboard Listing', 'SELECT dashboard_id AS targetTypeId, dashboard_name AS targetTypeName FROM dashboard 
WHERE is_deleted = 0 AND dashboard_name LIKE CONCAT("%", :searchText, "%")',2), 
('dynamicForms', 'Dynamic Forms Autocomplete', 'SELECT form_id AS targetTypeId, form_name AS targetTypeName FROM dynamic_form WHERE form_name LIKE CONCAT("%", :searchText, "%")',2), 
('dynarestListing', 'Autocomplete for dynamic rest', 'SELECT jws_dynamic_rest_id AS targetTypeId, jws_method_name AS targetTypeName 
FROM jws_dynamic_rest_details WHERE `jws_method_name` LIKE CONCAT("%", :searchText, "%")',2), 
('templateListing', 'Template Autocomplete', 'SELECT template_id AS targetTypeId, template_name AS targetTypeName FROM template_master WHERE `template_name` LIKE CONCAT("%", :searchText, "%")',2);


UPDATE template_master SET template_type_id=2 WHERE template_id='55c2db62-0480-11eb-9926-e454e805e22f';


REPLACE INTO autocomplete_details(
   ac_id
  ,ac_description
  ,ac_select_query
  ,ac_type_id
) VALUES (
   'home-page-role'
  ,'Role autocomplete for home page'
  ,'SELECT jr.role_id AS roleId, jr.role_name AS roleName FROM jws_role AS jr
WHERE jr.role_name LIKE CONCAT("%", :searchText, "%")  
AND jr.is_active = 1 
AND jr.role_id NOT IN (SELECT mra.role_id FROM module_role_association AS mra WHERE mra.is_deleted = 0)'
  ,2
);

REPLACE INTO autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id) VALUES
('rolesAutocomplete',' List of roles','SELECT role_name AS roleName, role_id AS roleId FROM  jws_role WHERE  role_name LIKE CONCAT("%", :searchText, "%") AND is_active=1', 2);

replace into jws_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(28, '8dd0e053-1955-11eb-a4c1-e454e805e22f', 'noparam', 'select 1;', 1, 2);

UPDATE template_master SET template_type_id=2 WHERE template_id='8a80cb817570ad44017570b0991c0000';


REPLACE into dashboard (dashboard_id, dashboard_name, context_id, dashboard_type, created_by, created_date, last_updated_date, is_deleted, is_draggable, is_exportable) VALUES
('ab7202bf-eadd-11ea-a036-e454e805e22f', 'Java Stater Usages', (SELECT context_id FROM context_master where context_description='jws'), 2, 'admin', NOW(), NOW(), 0, 1, 0);

UPDATE dashlet SET dashlet_type_id=2 WHERE dashlet_id='76ad58a3-afa3-4efd-a872-9a78a9e01a94';

REPLACE INTO jws_dynamic_rest_details(jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id) VALUES
('8f4d6b36-167e-4d69-a6e7-1f88423f451a', 'dashlet-module-details', 1, 'dashletmoduleDetails', 'Get all modules details for dashlet', 2, 7, 'function dashletmoduleDetails(requestDetails, daoResults) {
    var moduleDetails = new Object();
    moduleDetails.templates = daoResults["templateDetails"];
    moduleDetails.grids = daoResults["gridDetails"];
    moduleDetails.dashboards = daoResults["dashboardDetails"];
    moduleDetails.i18n = daoResults["resourceBundleDetails"];
    moduleDetails.dashlets = daoResults["dashletDetails"];
    moduleDetails.notifications = daoResults["notificationDetails"];
    return moduleDetails;
}

dashletmoduleDetails(requestDetails, daoResults);', 3, 0, 2);




SET FOREIGN_KEY_CHECKS=1;