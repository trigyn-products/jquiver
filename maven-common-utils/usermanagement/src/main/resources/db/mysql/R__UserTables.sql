SET FOREIGN_KEY_CHECKS=0;

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('d0328a53-138b-11eb-9b1e-f48e38ab9348', 'addEditRole', '
<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>

<div class="container">
	<div class="topband">
		<h2 class="title-cls-name float-left">
			<#if (jwsRole?api.getRoleId())??>
                Edit Role
             <#else>
                Add Role
             </#if>
		</h2> 
		<div class="clearfix"></div>		
	</div>
  <form method="post" name="addEditForm" id="addEditForm">
    
        	<div class="row">
    		<input type="hidden" id="roleId" name="roleId"/>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="rolename" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              Role name
		            </label>
				<input type="text" id="roleName" name="roleName"  value="" maxlength="100" class="form-control">
			</div>
		</div>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="roledescription" style="white-space:nowrap">
		              Role description
		            </label>
				<input type="text" id="roleDescription" name="roleDescription"  value="" maxlength="2000" class="form-control">
			</div>
		</div>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				
				<label for="isActive"><span class="asteriskmark">*</span>Is Active</label>
	            <div class="onoffswitch">
	                <input type="checkbox" name="isActive" class="onoffswitch-checkbox" id="isActive" value="0" />
	                <label class="onoffswitch-label" for="isActive">
	                    <span class="onoffswitch-inner"></span>
	                    <span class="onoffswitch-switch"></span>
	                </label>
	            </div>
				
			</div>
		</div>
    	</div>
    
  </form>
  <div class="row">
			<div class="col-12">
				<div id="buttons" class="pull-right">
					<div class="btn-group dropdown custom-grp-btn">
                        <div id="savedAction">
                            <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''add-edit-role'', this, saveData , backToPreviousPage);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                        </div>
                        <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();" > </button>
                        <div class="dropdown-menu action-cls"  id="actionDiv">
                            <ul class="dropdownmenu">
                                <li id="saveAndCreateNew" onclick="typeOfAction(''add-edit-role'', this, saveData, backToPreviousPage);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                                <li id="saveAndEdit" onclick="typeOfAction(''add-edit-role'', this, saveData, backToPreviousPage);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                            </ul>
                        </div>  
                    </div>
                    <input id="cancelBtn" type="button" class="btn btn-secondary" value="${messageSource.getMessage(''jws.cancel'')}" onclick="backToPreviousPage();">
		        </div>
			</div>
		</div>
</div>
<script>

	contextPath = "${contextPath}";
  let isEdit = "${jwsRole?api.getRoleId()!''''}";
  $(function(){
  
   	savedAction("add-edit-role", isEdit);
	hideShowActionButtons();
    // setting value on edit.
      <#if (jwsRole?api.getRoleId()??)>
      	
      		$("#roleId").val("${jwsRole?api.getRoleId()}");
		    $("#roleName").val("${jwsRole?api.getRoleName()}"); 
		    $("#roleDescription").val("${jwsRole?api.getRoleDescription()}"); 
            if(${jwsRole?api.getIsActive()} == 1){ 
            	 $("#isActive").attr("checked",true);
            }
    
      </#if>
    
  });
  
	//Add logic to save form data
	function saveData (){
    let roleData = new Object();
    roleData.roleId = $("#roleId").val();
    roleData.roleName =  $("#roleName").val();
    roleData.roleDescription = $("#roleDescription").val();
    roleData.isActive = ($("#isActive").is(":checked") ? 1 : 0);
    
		$.ajax({
		     		type : "POST",
            contentType : "application/json",
		     		url : contextPath+"/cf/srd",
		     		data : JSON.stringify(roleData),
		            success: function(data) {
		              showMessage("Information saved successfully", "success");
		            }
		     	});
		     	return true;
	}
	
	//Code go back to previous page
	function backToPreviousPage() {
		location.href = contextPath+"/cf/rl";
	}
</script>','admin','admin',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('e027d3a3-138b-11eb-9b1e-f48e38ab9348', 'role-listing', '
<head>
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
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.roleMaster'')}</h2> 
		<div class="float-right">
			<form id="addEditRole" action="/cf/aedr" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="roleId" id="roleId" value=""/>
                <button type="submit" class="btn btn-primary">
                        ${messageSource.getMessage(''jws.addRole'')}
                </button>
            </form>
    		<span onclick="backToWelcomePage();">
    	  		<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
    	 	</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="divRoleMasterGrid"></div>

	<div id="snackbar"></div>
</div>
<script>
var rolesArray = ["2ace542e-0c63-11eb-9cf5-f48e38ab9348","ae6465b3-097f-11eb-9a16-f48e38ab9348","b4a0dda1-097f-11eb-9a16-f48e38ab9348"];
$(function () {
	let formElement = $("#addEditRole")[0].outerHTML;
	let formDataJson = JSON.stringify(formElement);
	sessionStorage.setItem("add-edit-role", formDataJson);
	let colM = [
        { title: "Role Name", width: 130, align: "center", dataIndx: "role_name", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Role Description", width: 100, align: "center",  dataIndx: "role_description", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Is Active", width: 160, align: "center", dataIndx: "is_active", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Action", width: 30, align: "center", render: editRole, dataIndx: "action" }
	];
    let grid = $("#divRoleMasterGrid").grid({
      gridId: "roleGrid",
      colModel: colM
  });
});
function editRole(uiObject) {
	let roleId = uiObject.rowData.role_id;
	if(rolesArray.includes(roleId)){ 
		return ''<span id="''+roleId+''" onclick="showErrorMessage();" class= "grid_action_icons disable_cls"><i class="fa fa-pencil"></i></span>''.toString();
	}else{ 
	  	return ''<span id="''+roleId+''" onclick="submitForm(this);" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
	}
}	
function submitForm(element) {
	$("#roleId").val(element.id);
 	$("#addEditRole").submit();
}
function backToWelcomePage() {
	location.href = "/cf/um";
}
function showErrorMessage(){ 
	showMessage("You cant edit system roles","error");
}

</script>','admin','admin',now(), 2);



REPLACE INTO grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id)  VALUES 
 ('roleGrid','role listing','List of roles','jws_role','*', 1, 2);

REPLACE  INTO  jws_property_master (
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
	'51849604-1a8f-11eb-98d3-f48e38ab8cd7'
  ,'system'
  ,'system'
  ,'enable-user-management'
  ,'false'
  ,0
  ,now()
  ,'admin'
  ,'1.000'
  ,'By default user management will be disabled' 
);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('e604edf6-138b-11eb-9b1e-f48e38ab9348', 'manageRoleModule', '
<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>

<div class="box-block">
 
  <form method="post" name="addEditModule" id="addEditModule">
    
           
          <#if roles ??>
             <#assign roleIds = roles?map(role -> role?api.getRoleId())>
           <table class="default-table-cls">
              <tr> 
                <th>Modules</th>
                  <#list roles as role>
                  <th>  ${role?api.getRoleName()} </th>
                </#list>
              </tr>
             
              
                <#if masterModules ??>
                  <#list masterModules as module>
                    <tr>
                    <td>${module?api.getModuleName()}</td>
                    <#list roleIds as roleId>
                      <td>
                            <div class="onoffswitch">
                                 <input type="checkbox" onchange="saveModuleData(this);" name="isActiveCheckbox" class="onoffswitch-checkbox" id="${module?api.getModuleId()}_${roleId}" />
                                <label class="onoffswitch-label" for="${module?api.getModuleId()}_${roleId}">
                                    <span class="onoffswitch-inner"></span>
                                    <span class="onoffswitch-switch"></span>
                                 </label>
                             </div> 
                       
                       </td>
                    </#list>
                    </tr>
                  </#list>
                </#if>
              
            </table> 
          </#if>  
         
    
  </form>

<script>

	contextPath = "${contextPath}";
  
  $(function(){
    // setting value on edit.
     <#if roleModulesAssociations ??>
                  <#list roleModulesAssociations as roleModule>
                    <#if roleModule?api.getIsActive() == 1>
                      $("#${roleModule?api.getModuleId()}_${roleModule?api.getRoleId()}")
                        .attr("roleModuleId","${roleModule?api.getRoleModuleId()}").attr("checked",true);
                    <#else>  
                        $("#${roleModule?api.getModuleId()}_${roleModule?api.getRoleId()}")
                        .attr("roleModuleId","${roleModule?api.getRoleModuleId()}").attr("checked",false);
                     </#if> 
                  </#list>
      </#if> 
    
  });
  
	//Add logic to save form data
	function saveModuleData (thisObj){

	  let roleModule = new Object();
	  var roleModuleArray = thisObj.id.split("_")
	  roleModule.roleModuleId = thisObj.getAttribute("roleModuleId");
	  roleModule.moduleId = roleModuleArray[0];
	  roleModule.roleId = roleModuleArray[1];
	  roleModule.isActive = ($(thisObj).is(":checked") ? 1 : 0);

		$.ajax({
		     		type : "POST",
           			contentType : "application/json",
		     		url : contextPath+"/cf/srm",
		     		data : JSON.stringify(roleModule),
		            success: function(data) {
		            }
		     	});
	}
	
	//Code go back to previous page
	function backToPreviousPage() {
		location.href = contextPath+"/cf/um";
	}
</script>','admin','admin',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('f1a476f8-138b-11eb-9b1e-f48e38ab9348', 'addEditJwsUser', '
<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>

<div class="container">
	<div class="topband">
		<h2 class="title-cls-name float-left">
		<#if (jwsUser?api.getUserId())??>
                Edit User
             <#else>
                Add User
             </#if>
		</h2> 
		<div class="clearfix"></div>		
	</div>
  <form method="post" name="addEditUser" id="addEditUser">
    
        	<div class="row">
    		<input type="hidden" id="userId" name="userId"/>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="firstName" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              First name
		            </label>
				<input type="text" id="firstName" name="firstName"  value="" maxlength="100" class="form-control">
			</div>
		</div>
    <div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="lastName" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              Last name
		            </label>
				<input type="text" id="lastName" name="lastName"  value="" maxlength="100" class="form-control">
			</div>
		</div>
    
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="email" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              Email
		            </label>
				<input type="email" id="email" name="email"  value="" maxlength="2000" class="form-control">
			</div>
		</div>
		<#if !isProfilePage >
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label for="allowPasswordChange"><span class="asteriskmark">*</span>Force User to Change Password</label>
	            <div class="onoffswitch">
	                <input type="checkbox" name="forcePasswordChange" class="onoffswitch-checkbox" id="forcePasswordChange" value="0" />
	                <label class="onoffswitch-label" for="forcePasswordChange">
	                    <span class="onoffswitch-inner"></span>
	                    <span class="onoffswitch-switch"></span>
	                </label>
	            </div>
				
			</div>
		</div>
    	</div>
    	<div class="row">
        <#list roles as role>
           	<div class="col-3">
         			<div class="col-inner-form full-form-fields">
         		            <label for="${role?api.getRoleId()}" style="white-space:nowrap">
         		              ${role?api.getRoleName()}
         		            </label>
         		           <div class="onoffswitch">
				                <input type="checkbox" name="rolesAssigned"  class="onoffswitch-checkbox" id="${role?api.getRoleId()}" value="0" />
				                <label class="onoffswitch-label" for="${role?api.getRoleId()}">
				                    <span class="onoffswitch-inner"></span>
				                    <span class="onoffswitch-switch"></span>
				                </label>
	           				</div>
         			</div>
 		        </div>
        
                      
        </#list>
      </div>
      <div class="row">
			<div class="col-12">
				<div id="buttons" class="pull-right">
					<div class="btn-group dropdown custom-grp-btn">
                        <div id="savedAction">
                            <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''add-edit-user'', this, saveData , backToPreviousPage);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                        </div>
                        <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();" > </button>
                        <div class="dropdown-menu action-cls"  id="actionDiv">
                            <ul class="dropdownmenu">
                                <li id="saveAndCreateNew" onclick="typeOfAction(''add-edit-user'', this, saveData, backToPreviousPage);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                                <li id="saveAndEdit" onclick="typeOfAction(''add-edit-user'', this, saveData, backToPreviousPage);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                            </ul>
                        </div>  
                    </div>
                    <input id="cancelBtn" type="button" class="btn btn-secondary" value="${messageSource.getMessage(''jws.cancel'')}" onclick="backToPreviousPage();">
		        </div>
			</div>
		</div>
      <#else>
            </div>
              <div class="row">
			<div class="col-12">
				<div class="float-right">
					<input  class="btn btn-primary" value="Save" type="button" onclick="saveData();">
					<span onclick="backToPreviousPage();">
						<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
					</span> 
				</div>
			</div>
		</div>
    </#if>
  </form>
	
</div>
<script>

	let isProfilePage = ${isProfilePage?c};
	let isEdit = "${jwsUser?api.getUserId()!''''}";
	contextPath = "${contextPath}";
  
  $(function(){
  
  	savedAction("add-edit-user", isEdit);
	hideShowActionButtons();
     // disable authenticated role
        $("#2ace542e-0c63-11eb-9cf5-f48e38ab9348").prop("checked",true);
     	$("#2ace542e-0c63-11eb-9cf5-f48e38ab9348").prop("disabled",true);
     
     
    // setting value on edit.
		 <#if (jwsUser?api.getUserId())??>
        $("#userId").val("${jwsUser?api.getUserId()}");
		 $("#firstName").val("${jwsUser?api.getFirstName()}"); 
        $("#lastName").val("${jwsUser?api.getLastName()}");
        $("#email").val("${jwsUser?api.getEmail()}"); 
         $("#email").attr("disabled",true);
         if(${jwsUser?api.getForcePasswordChange()} == 1){ 
            	 $("#forcePasswordChange").prop("checked",true);
            }
            
        <#if userRoleIds??>    
          <#list userRoleIds as roleId>
              $("#${roleId}").attr("checked",true);
          </#list>    
		    </#if>
        
      </#if> 
    
  });
  
	//Add logic to save form data
	function saveData (){

    let userData = new Object();
    userData.userId = $("#userId").val();
    userData.firstName =  $("#firstName").val();
     userData.lastName =  $("#lastName").val();
    userData.email = $("#email").val();
    userData.roleIds = [];
    userData.isProfilePage = isProfilePage;
    userData.forcePasswordChange = ($("#forcePasswordChange").is(":checked") ? 1 : 0);
    $.each($("input[name=''rolesAssigned'']:checked"),function(key,value){
      userData.roleIds.push(value.id);
    });
    
    
		$.ajax({
		     		type : "POST",
            contentType : "application/json",
		     		url : contextPath+"/cf/sud",
		     		data : JSON.stringify(userData),
		            success: function(data) {
		              showMessage("Information saved successfully", "success");
		            }
		     	});
		     	return true;
	}
	
	//Code go back to previous page
	function backToPreviousPage() {
		<#if !isProfilePage >
			location.href = contextPath+"/cf/ul";
		<#else>
			location.href = contextPath+"/";
		</#if>
		
			
	}
</script>','admin','admin',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('fc1ff685-138b-11eb-9b1e-f48e38ab9348', 'jws-user-listing', '
<head>
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
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.userMaster'')}</h2> 
		<div class="float-right">
			<form id="addEditUser" action="/cf/aedu" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="userId" id="userId" value=""/>
                <button type="submit" class="btn btn-primary">
                        ${messageSource.getMessage(''jws.addUser'')}
                </button>
            </form>
			
			
    		<span onclick="backToWelcomePage();">
    	  		<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
    	 	</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="divUserMasterGrid"></div>

	<div id="snackbar"></div>
</div>
<script>
var userArray = ["111415ae-0980-11eb-9a16-f48e38ab9348"];
$(function () {
 	let formElement = $("#addEditUser")[0].outerHTML;
	let formDataJson = JSON.stringify(formElement);
	sessionStorage.setItem("add-edit-user", formDataJson);
	let colM = [
        { title: "${messageSource.getMessage(''jws.firstName'')}", width: 130, align: "center", dataIndx: "first_name", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "${messageSource.getMessage(''jws.lastName'')}", width: 130, align: "center", dataIndx: "last_name", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "${messageSource.getMessage(''jws.email'')}", width: 100, align: "center",  dataIndx: "email", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "${messageSource.getMessage(''jws.isActive'')}", width: 160, align: "center", dataIndx: "is_active", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Action", width: 30, align: "center", render: editUser, dataIndx: "action" }
	];
    let grid = $("#divUserMasterGrid").grid({
      gridId: "jwsUserListingGrid",
      colModel: colM
  });
});
function editUser(uiObject) {
	let userId = uiObject.rowData.user_id;
	if(userArray.includes(userId)){ 
		return ''<span id="''+userId+''" onclick="showErrorMessage();" class= "grid_action_icons disable_cls"><i class="fa fa-pencil"></i></span>''.toString();
	}else{ 
  		return ''<span id="''+userId+''" onclick="submitForm(this);" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
	}
}	
function submitForm(element) {
	$("#userId").val(element.id);
 	$("#addEditUser").submit();
}
function backToWelcomePage() {
	location.href = "/cf/um";
}
function showErrorMessage(){ 
	showMessage("You cant edit admin user","error");
}
</script>','admin','admin',now(), 2);



REPLACE INTO grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id)  VALUES 
 ('jwsUserListingGrid','user listing','List of users','jws_user','*', 1, 2);

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('cf973388-0991-11eb-9926-e454e805e22f', 'user-management', '
<head>
    <script src="/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.js"></script>
    <script src="/webjars/1.0/typeahead/typeahead.js"></script>
    <link rel="stylesheet" href="/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
	<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>
<div class="container">

	
<div class="cm-card">
<div class="topband cm-card-header">
        <h2 class="title-cls-name float-left">User Management</h2> 
        <div class="float-right">
             <form id="manageRoleModule" action="/cf/mp" method="post" class="margin-r-5 pull-left">
            <button type="submit" class="btn btn-primary"> Manage Permissions </button>
        </form>
        
        <button type="button" class="btn btn-primary" onclick="openManageRole();"> Manage Roles</button>
        
        <button type="button" class="btn btn-primary" onclick="openManageUser();"> Manage Users</button>
        
        <button type="button" id="restartServer" style="display:none" class="btn btn-primary" onclick="restartServer();">Restart Server</button>
        </div>
        
        <div class="clearfix"></div>        
        </div>

	<div class="cm-card-body">
<div class="row">
   
    <div class="col-12 margin-t-25">
        <div class="col-3 float-left col-inner-form full-form-fields">
            <label for="isActiveCheckbox"><span class="asteriskmark">*</span>Enable Authentication</label>
            <div class="onoffswitch">
                <input type="hidden" id="isActive" name="isActive" value=""/>
                <input type="checkbox" name="isActiveCheckbox" class="onoffswitch-checkbox" id="isActiveCheckbox" onchange="showAuthTypeDropDown(this)" />
                <label class="onoffswitch-label" for="isActiveCheckbox">
                    <span class="onoffswitch-inner"></span>
                    <span class="onoffswitch-switch"></span>
                </label>
            </div>
        </div>
        <div id="authTypeDiv" class="col-3 float-left col-inner-form full-form-fields">
        <label for="authType"><span class="asteriskmark">*</span>Authentication Types</label>
         <select id="authType" class="form-control" onchange="changeAuthentication();">
        <#list authenticationTypesVO as authenticationType>
            <option
            <#if authenticationType?api.getAuthenticationProperties()??>
                  properties=''${authenticationType?api.getAuthenticationProperties()}''
            </#if>
            value="${authenticationType?api.getId()}">${authenticationType?api.getAuthenticationName()}
            </option>
        </#list>
        </select>
        </div>
      </div>
    </div>
     <div id="props" class="col-12 margin-t-25">
	</div>
	
</div>
<div class="cm-card-footer">
<div id="note" class="margin-t-10 clearfix">
    <span class="pull-left"><i>Kindly restart your server to get your configuration working.</i></span>
    
    <div class="btn-icons nomargin-right-cls pull-right">
     <input type="button" value="Save" class="btn btn-primary" onclick="saveAuthDetails();">
    </div>
</div>
	</div>
</div>
                    
                    
</div>                  
<script>

contextPath = "${contextPath}";
	// set data
	let autocomplete;
	$("#isActiveCheckbox").attr("checked",${authEnabled?c});
	if(!${authEnabled?c}){
		 $("#authTypeDiv").hide();
	}else{ 
		   $("#restartServer").show();
	}
	$("#authType").val("${authTypeId}").trigger("change");

    function showAuthTypeDropDown(element) {
        if(element.checked) {
            $("#authTypeDiv").show();
            $("#restartServer").show();
        } else {
            $("#authTypeDiv").hide();
            $("#restartServer").hide();
            $("#props").html("");
            $("#authType").val(1);
           
        }
    }
    function changeAuthentication(){ 
      if($("#isActiveCheckbox").prop("checked")){
        let parsedProperties;
        let properties = $("option[value="+$(''#authType'').val()+"]").attr("properties");
        if(properties!= undefined && properties != ""){
          parsedProperties = JSON.parse(properties);
        }
        $("#props").html("");
        $.each(parsedProperties, function(key,val){
           if(val.type=="boolean"){
              $("#props").append(''<div class="row"><div class="col-3 float-left col-inner-form full-form-fields"><label for="''+val.name+''"><span class="asteriskmark">*</span>''+val.name
              +'' </label> <div class="onoffswitch"><input type="checkbox" name="'' +val.name+'' " class="onoffswitch-checkbox" id="'' +val.name+''" onchange="addInputFields(this);" /><label class="onoffswitch-label" for="'' +val.name+''"><span class="onoffswitch-inner"></span><span class="onoffswitch-switch"></span></label></div></div>'');
              $("#"+val.name).prop("checked",val.value).trigger("change");
           }
        });
      }
    }
    
    function saveAuthDetails(){ 
    	let authenticationEnabled= $("#isActiveCheckbox").is(":checked");
    	let authenticationTypeId= $("#authType").val();
    	let regexObj = new Object(); 
    	let formObj = new Object(); 
    	if($("#enableRegex").is(":checked")){ 
	    	regexObj.regexValue  = $("#regexValue").val();
    		regexObj.regexExample  = $("#regexExample").val();
    	 	regexObj = JSON.stringify(regexObj);
    	}else{
    		regexObj = null;
    	}
    	
        let parsedProperties = null;
        let properties = $("option[value="+$(''#authType'').val()+"]").attr("properties");
        if(properties!= undefined && properties != ""){
          parsedProperties = JSON.parse(properties);
        }
       
        $.each(parsedProperties, function(key,val){
          if(val.type=="boolean"){
            val.value = $("#"+val.name).is(":checked");
          }  
        });
        
        if($("#enableDynamicForm").is(":checked")){
        	formObj.formId = $("#formId").val();
        	formObj.formName = $("#formName").val();
        }
        
        
    		$.ajax({
		     		type : "POST",
		     		url : contextPath+"/cf/sat",
		     		data : { 
			     		authenticationTypeId:authenticationTypeId,
			     		authenticationEnabled:authenticationEnabled,
             			propertyJson:JSON.stringify(parsedProperties),
             			regexObj:regexObj,
             			userProfileFormId:JSON.stringify(formObj) 
		     		},
		            success: function(data) {
                showMessage("Information saved successfully", "success");
		            }
		     	});
    
    }
    function openManageRole(){
      location.href="/cf/rl";
    }
    	
    function openManageUser(){
      location.href="/cf/ul";
    }
    
    function restartServer(){
      location.href="/cf/restart";
    }
    
  function addInputFields(thisObj){ 
        let inputId;
        if(thisObj.id=="enableRegex" ){
            if($(thisObj).is(":checked")){
                $(".childElement").show();
                if($("#enableRegex").closest("div.row").children().length == 1){
                    inputId = "regexPattern";
                }else{
                    return false;
                }
                
            }else{
                $(".childElement").hide();
                return false;
            }
             
        }else if(thisObj.id=="enableDynamicForm" ){
			$("#dynamicFormDiv").remove();
			inputId = "user-profile-form-name";
        }else{
			return false;
		}
    	$.ajax({
		    type : "GET",
		    url : contextPath+"/cf/gif",
			data : { 
				inputId:inputId,
		    },
			success: function(data) {
             	if(thisObj.id=="enableRegex" ){
             		$.each(data, function (key, value) {
	 					$("#enableRegex").closest("div.row").append(''<div class="childElement col-3 col-inner-form full-form-fields"><label for=="''+key+''">''+key+''<label><input type="text"  id="''+key+''" name="''+key+''" class="form-control" value="''+value+''"></div>'');
					});
				}
				if(thisObj.id=="enableDynamicForm"){
					$("#enableDynamicForm").closest("div.row").append(''<div id="dynamicFormDiv" class="col-3"><div class="col-inner-form full-form-fields">	<label for="dynamicFormName" style="white-space:nowrap">Form Name</label><div class="search-cover"><input type="text" id="dynamicFormName" name="dynamicFormName" class="form-control"><i class="fa fa-search" aria-hidden="true"></i></div><input type="hidden" id="formName" value= "" name="formName"><input type="hidden" id="formId" value= "" name="formId" ></div></div>'');
					if(data.formId !== undefined){
						$("#formId").val(data.formId);
						$("#formName").val(data.formName);
						$("#dynamicFormName").val(data.formName);
					}
		           	initFormAutocomplete(data);
		           	if($(thisObj).is(":checked")){
						$("#dynamicFormDiv").show();
					}else{
						$("#dynamicFormDiv").hide();
					}
				}
			}, error: function(xhr, data){
				
			},
		});
    
    }
    
    function initFormAutocomplete(formDetails){
    	let savedForm = new Object();
    	savedForm.targetTypeId = formDetails.formId;
    	savedForm.targetTypeName = formDetails.formName;
    	autocomplete = $(''#dynamicFormName'').autocomplete({
	        autocompleteId: "dynamicForms",
	        prefetch : true,
	        render: function(item) {
	        	var renderStr ='''';
	        	if(item.emptyMsg == undefined || item.emptyMsg === '''')
	    		{
	        		renderStr = ''<p>''+item.targetTypeName+''</p>'';
	    		}
	        	else
	    		{
	        		renderStr = item.emptyMsg;	
	    		}	    				        
	            return renderStr;
	        },
	        additionalParamaters: {languageId: 1},
	        extractText: function(item) {
	            return item.targetTypeName;
	        },
	        select: function(item) {
	            $("#dynamicFormName").blur();
	            $("#formId").val(item.targetTypeId);
	            $("#formName").val(item.targetTypeName);
	        }, 	
   		}, savedForm);
    
    }
    
</script>', 'admin', 'admin', NOW(), NULL, 2);


   REPLACE  INTO  jws_property_master (
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
   '3b2838cc-1a8f-11eb-98d3-f48e38ab8cd7'
  ,'system'
  ,'system'
  ,'authentication-type'
  ,'1'
  ,0
  ,now()
  ,'admin'
  ,'1.000'
  ,'Authentication type - default in memory authentication' 
);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('02fb1cb5-138c-11eb-9b1e-f48e38ab9348', 'jws-welcome', '<div>
	<h1>Welcome to TSMS</h1>
	
		<#if loggedInUser == true >
			Welcome ${userName}
			<a href="tsms/users">Users</a>
			<a href="tsms/destinations">Destinations</a>
		<#else>
			<a href="/cf/register">Register here</a>
			<a href="/cf/login">Login here</a>
		</#if>

	
	
</div>

','admin','admin',now(), 2);


Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('0c7acdf9-138c-11eb-9b1e-f48e38ab9348', 'jws-login', ' 
 
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Please sign in</title>
   
   	<@templateWithoutParams "jws-common-css-js"/>
        <script>
           
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
        </script>
  </head>
<body>
<div class="container">

    <div class="row">
        <div class="col-7">
            <div class="loginbg"><img src="/webjars/1.0/images/LoginBg.jpg"></div> 
        </div> 

        <div class="col-5">
            <form class="form-signin" method="post" action="/cf/login" autocomplete="off">
                <h2 class="form-signin-heading text-center">Welcome To <span class="cm-logotext">JQuiver</span>
                </h2>
                <#if queryString?? && queryString == "error">
                    <div class="alert alert-danger" role="alert">Bad Credentials</div>
                <#elseif queryString?? &&  queryString == "logout">
                    <div class="alert alert-success" role="alert">You have been signed out</div> 
                </#if>
                <#if resetPasswordSuccess??>
                    <div class="alert alert-success" role="alert">${resetPasswordSuccess} </div>
                </#if>
                <p class="divdeform">
                    <label for="username" class="formlablename">Email</label>
                    <span class="formicosn"><i class="fa fa-user" aria-hidden="true"></i></span>
                    <input type="email" id="email" name="email" class="form-control" placeholder="Enter Your Email" required autofocus>
                </p>
                <p class="divdeform"> 
                    <label for="password" class="formlablename">Password</label>
                    <span class="formicosn"><i class="fa fa-unlock-alt" aria-hidden="true"></i></span>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Enter Your Password" required>
                    <span class="passview" onclick="showHidePassword(this);"><i class="fa fa-eye" aria-hidden="true"></i></span>
                    <span class="remebermeblock">
                        <input type="checkbox" name="remember-me" id="remember-me">   <label for="remember-me">Remeber Me</label>
                    </span>    
                    <span class="forgotpassword">
                        <a href="/cf/resetPasswordPage">Forgot password?</a> 
                    </span>
                </p>
       
				
                <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
                <#if enableRegistration?? && enableRegistration?string("yes", "no") == "yes" >
	                <p class="registerlink">New User?
	                    <a href="/cf/register"> Click here to register</a>
	                </p>
                </#if>
            </form>
        </div>
    </div>   
</div> 

</body></html>','admin','admin',now(), 2);



Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('11c869d4-138c-11eb-9b1e-f48e38ab9348', 'jws-register', ' 
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Register</title>
    <@templateWithoutParams "jws-common-css-js"/>
    <script>
			<#if error??>
				$("#email").focus();
			</#if>
			
			<#if errorPassword??>
				$("#password").focus();
			</#if>
			 $(function(){
                $("#reloadCaptcha").click(function(event){
                	$("#imgCaptcha").attr("src", $("#imgCaptcha").attr("src")+"#");
           		});
          	});
			
		</script>
</head>


<body>
	<div class="container"> 
	<div class="row">
        <div class="col-7">
            <div class="loginbg"><img src="/webjars/1.0/images/LoginBg.jpg"></div> 
        </div> 
	 <div class="col-5">   
        <form class="form-signin" action="/cf/register"  method="post" autocomplete="off">
        
        <h2 class="form-signin-heading">Please Register</h2>
        	<#if error??>
        		<div class="alert alert-danger" role="alert">${error} </div>
        	</#if>
        	<#if errorPassword??>
        		<div class="alert alert-danger" role="alert">${errorPassword} </div>
        	</#if>
        	 <p>
          		<label for="firstName" class="sr-only">First Name</label>
         	 	<input type="text" id="firstName" name="firstName" class="form-control" placeholder="First Name" <#if firstName??> value="${firstName}"</#if> required autofocus>
        	</p>
        	<p>
          		<label for="lastName" class="sr-only">Last Name</label>
         	 	<input type="text" id="lastName" name="lastName" class="form-control" placeholder="Last Name"  <#if lastName??> value="${lastName}"</#if> required autofocus>
        	</p>
        	<p>
          		<label for="email" class="sr-only">Email</label>
         	 	<input type="email" id="email" name="email" class="form-control" placeholder="Email" required autofocus>
        	</p>
        	<p>
          		<label for="password" class="sr-only">Password</label>
         	 	<input type="password" id="password" name="password" class="form-control" placeholder="Password" required autofocus >
        	</p>
        	<#if enableCaptcha?string("yes", "no") == "yes" >
				<p>
					 <img id="imgCaptcha" name="imgCaptcha" src="/cf/captcha">
					  <span id="reloadCaptcha"><i class="fa fa-refresh" aria-hidden="true"></i></span>
					<label for="captcha" class="sr-only">Enter Captcha</label>
					<input type="text" id="captcha" name="captcha" class="form-control" placeholder="Enter Captcha" required autofocus >
				</p>
			</#if> 
        	 <button class="btn btn-lg btn-primary btn-block cm-mb10" type="submit">Register</button>
			 <p> Already Registered User? <a href="login">Click here to sign in</a></p>
	   </form>
     
	</div>	 
 </div>  </div> 	
</body>
','admin','admin',now(), 2);
	


Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('16e9ddec-138c-11eb-9b1e-f48e38ab9348', 'jws-password-reset-mail', ' 
 
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Forgot password? - TSMS</title>
   <@templateWithoutParams "jws-common-css-js"/>
    	<script>
      
			<#if nonRegisteredUser??>
				$("#email").focus();
			</#if>
			<#if inValidLink??>
				$("#email").focus();
			</#if>
			 $(function(){
                $("#reloadCaptcha").click(function(event){
                	$("#imgCaptcha").attr("src", $("#imgCaptcha").attr("src")+"#");
           		});
          	});
			
		</script>
  </head>
  <body>
     <div class="container">
     	<div class="row">
        <div class="col-7">
            <div class="loginbg"><img src="/webjars/1.0/images/LoginBg.jpg"></div> 
        </div> 
 		<div class="col-5">
      <form class="form-resetpassword" method="post" action="/cf/sendResetPasswordMail" autocomplete="off">
        <h2 class="form-resetpassword-heading">Reset your password</h2>
		<#if nonRegisteredUser??>
        		<div class="alert alert-danger" role="alert">${nonRegisteredUser} </div>
        </#if>
		<#if inValidLink??>
        		<div class="alert alert-danger" role="alert">${inValidLink} </div>
        </#if>
        <#if invalidCaptcha??>
        		<div class="alert alert-danger" role="alert">${invalidCaptcha} </div>
        	</#if>
        <p>
		Enter your user account verified email address and we will send you a password reset link.
		</p>
        <p>
          <label for="username" class="sr-only">Email</label>
          <input type="email" id="email" name="email" class="form-control" placeholder="Email" required autofocus>
        </p>
        <#if enableCaptcha?string("yes", "no") == "yes" >
				<p>
					 <img id="imgCaptcha" name="imgCaptcha" src="/cf/captcha">
					  <span id="reloadCaptcha"><i class="fa fa-refresh" aria-hidden="true"></i></span>
					<label for="captcha" class="sr-only">Enter Captcha</label>
					<input type="text" id="captcha" name="captcha" class="form-control" placeholder="Enter Captcha" required autofocus >
				</p>
			</#if> 
        <button class="btn btn-lg btn-primary btn-block" type="submit">Send password reset email</button>
        	   <p> <a href="login">Click here to sign in</a></p>
      </form> 
</div>
 </div> 
  </div> 
</body></html>','admin','admin',now(), 2);




Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('1ef38eb1-138c-11eb-9b1e-f48e38ab9348', 'jws-password-reset-page', ' 
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Change your password - TSMS</title>
    <@templateWithoutParams "jws-common-css-js"/>
    <script>
      <#if nonValidPassword??>
				$("#password").focus();
			</#if>
			 $(function(){
                $("#reloadCaptcha").click(function(event){
                	$("#imgCaptcha").attr("src", $("#imgCaptcha").attr("src")+"#");
           		});
          	});
			
		</script>
</head>


<body>
	<div class="container">
	   <div class="row">
        <div class="col-7">
            <div class="loginbg"><img src="/webjars/1.0/images/LoginBg.jpg"></div> 
        </div> 
    	<div class="col-5">    
        <form class="form-password-reset" action="/cf/createPassword"  method="post" autocomplete="off">
        
        <h2 class="form-password-reset-heading">Change your password </h2>
        <#if nonValidPassword??>
        		<div class="alert alert-danger" role="alert">${nonValidPassword} </div>
        	</#if>
        	  <#if invalidCaptcha??>
        		<div class="alert alert-danger" role="alert">${invalidCaptcha} </div>
        	</#if>
        	
        	<p>
          		<label for="password" class="sr-only">Create new password</label>
         	 	<input type="password" id="password" name="password" class="form-control" placeholder="Password" required autofocus>
        	</p>
        	<p>
          		<label for="confirmpassword" class="sr-only">Confirm Password</label>
         	 	<input type="password" id="confirmpassword" name="confirmpassword" class="form-control" placeholder="Confirm password" required autofocus >
        	</p>
        	<#if enableCaptcha?string("yes", "no") == "yes" >
				<p>
					 <img id="imgCaptcha" name="imgCaptcha" src="/cf/captcha">
					  <span id="reloadCaptcha"><i class="fa fa-refresh" aria-hidden="true"></i></span>
					<label for="captcha" class="sr-only">Enter Captcha</label>
					<input type="text" id="captcha" name="captcha" class="form-control" placeholder="Enter Captcha" required autofocus >
				</p>
			</#if> 
        	 <button class="btn btn-lg btn-primary btn-block" type="submit">Change password</button>
			 <input type="hidden" id="resetEmailId" name ="resetEmailId" value="${resetEmailId}">
       <input type="hidden" id="tokenId" name="token" value="${token}">
	   </form>
	</div>	
	  </div> 
    </div>   
</body>    
','admin','admin',now(), 2);


Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('28207b8f-138c-11eb-9b1e-f48e38ab9348', 'jws-password-reset-mail-success', ' 
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Reset password mail sent </title>
   <@templateWithoutParams "jws-common-css-js"/>
  </head>
  <body>
     
 
 
 
  <div class="container">
        <div class="cm-success-box">
        <div class="row">
            <div class="col-md-12">

                	<div class="box">							
				<div class="icon">
					<div class="image"><i class="fa fa-thumbs-o-up"></i></div>
					<div class="info">
						<h3 class="title">Successful</h3>
						<p>
                             ${successResetPasswordMsg}
							
						</p>
						
						<a class="btn btn-lg btn-primary " href="/cf/login">  Return to sign in </a>
						
					</div>
				</div>
			
			</div> 

            </div>
        </div>
    </div>
    </div>
</body></html>
 ','admin','admin',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('2d3d1d39-138c-11eb-9b1e-f48e38ab9348', 'jws-successfulRegisteration', ' 
<head>
     <title>Registration confirmation sent </title>
     <@templateWithoutParams "jws-common-css-js"/>
</head>
<body>

    <div class="container">
        <div class="cm-success-box">
        <div class="row">
            <div class="col-md-12">

                	<div class="box">							
				<div class="icon">
					<div class="image"><i class="fa fa-thumbs-o-up"></i></div>
					<div class="info">
						<h3 class="title">Successful</h3>
						<p>
                             A verification email has been sent to: <a href="${emailId}">  ${emailId} </a>
							
						</p>
						
					</div>
				</div>
			
			</div> 

            </div>
        </div>
    </div>
    </div>
           
 </body>  
','admin','admin',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('348d7075-138c-11eb-9b1e-f48e38ab9348', 'jws-accountVerified', '
	<head>
        <title>Congratulations!</title>
         <@templateWithoutParams "jws-common-css-js"/>
    </head>
    <body>


        <div class="container">
        <div class="cm-success-box">
        <div class="row">
            <div class="col-md-12">

                	<div class="box">							
				<div class="icon">
					<div class="image"><i class="fa fa-check"></i></div>
					<div class="info">
						<h3 class="title">Congratulations!</h3>
						<p>
                            Your account has been activated and email is verified!
							
						</p>
						
						<a class="btn btn-lg btn-primary " href="/cf/login">  Click here to Login</a>
						
					</div>
				</div>
			
			</div> 

            </div>
        </div>
    </div>
    </div>


    </body>    
','admin','admin',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('3b92295c-138c-11eb-9b1e-f48e38ab9348', 'my-profile', ' 
<head>
     <title>My Profile </title>
     <script>
		</script>
</head>
<body>
	${userName}
</body>
            
','admin','admin',now(), 2);



REPLACE INTO grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id)  VALUES 
 ('manageEntityRoleGrid','manage entity roles listing','Entities and role association','manageEntityRoleListing','entityName,moduleId',2);

DROP PROCEDURE IF EXISTS manageEntityRoleListing;
CREATE PROCEDURE `manageEntityRoleListing`(entityName varchar(50), moduleId varchar(50),forCount INT, limitFrom INT, limitTo INT
,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN

  SET @selectRoleQuery  =  (SELECT GROUP_CONCAT(CONCAT ('GROUP_CONCAT(CASE WHEN jr.role_name="',jr.role_name,'" THEN CONCAT(jera.role_id,"@::@",jera.is_active) END)  AS `',jr.role_name,'`')) FROM jws_role jr ORDER BY jr.role_name);

  SET @resultQuery = CONCAT(" SELECT jera.entity_role_id AS entityRoleId,jera.entity_id AS entityId,jera.entity_name AS entityName,jera.module_id AS moduleId,jmm.module_name AS moduleName, "
  ,@selectRoleQuery ) ;
  SET @fromString  = ' FROM  jws_role jr RIGHT OUTER JOIN jws_entity_role_association jera ON jera.role_id = jr.role_id INNER JOIN jws_master_modules jmm ON jmm.module_id = jera.module_id ';
  SET @whereString = ' WHERE jr.is_active=1  AND jera.module_type_id=0 ';
  
  IF NOT moduleId IS NULL THEN
    SET @moduleId= REPLACE(moduleId,"'","''");
    SET @whereString = CONCAT(@whereString,' AND jera.module_id like ''%',@moduleId,'%''');
  END IF;
  
  IF NOT entityName IS NULL THEN
    SET @entityName= REPLACE(entityName,"'","''");
    SET @whereString = CONCAT(@whereString,' AND entity_name like ''%',@entityName,'%''');
  END IF;
  
  
  
  SET @limitString = CONCAT(' LIMIT ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY last_updated_date DESC');
  END IF;
  
  SET @groupByString = CONCAT(' GROUP BY jera.entity_id ');
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('SELECT COUNT(*) FROM ( ',@resultQuery, @fromString, @whereString, @groupByString,@orderBy,' ) AS cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @groupByString, @orderBy, @limitString);
  END IF;


  
 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
 
END;

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('446e1b24-138c-11eb-9b1e-f48e38ab9348', 'manageEntityRoles', '
<head>
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

<div class="box-block">
     
        
    <div id="divManageEntityRoleGrid"></div>

    <div id="snackbar"></div>
</div>
<script>
var grid;
var contextPath= "${contextPath}";
var moduleType = [{"": "All"}];

 $.ajax({
			type : "GET",
			url : contextPath+"/cf/modules",
			async: false,
			success : function(data) {
				for(let counter = 0; counter < data.length; ++counter) {
					let object = data[counter];
					let details = new Object()
					details[object["moduleId"]] = object["moduleName"];
					moduleType.push(details);
				}
			}
		});



$(function () {
	let colM = [
		 { title: "Module Name", width: 100, align: "center",  dataIndx: "moduleId", align: "left", halign: "center",render: moduleTypes,
        filter: { type: "select", condition: "equal",options : moduleType, listeners: ["change"]} },
        { title: "Entity Name", width: 130, align: "center", dataIndx: "entityName", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        <#list roles as role>
              { title: "${role?api.getRoleName()}", width: 100, align: "center",  dataIndx: "${role?api.getRoleName()}", halign: "center",
        attr:"${role?api.getRoleId()}", render:addCheckBox,
         filter: {type: "<input type=''button'' class=''cm-select-btn'' name=''${role?api.getRoleName()}'' attr=''${role?api.getRoleId()}''  value=''Select All'' onclick=''checkAllBoxes(this);'' > <input type=''button'' class=''cm-select-btn'' name=''${role?api.getRoleName()}'' attr=''${role?api.getRoleId()}''  value=''Deselect All'' onclick=''checkAllBoxes(this);'' >"}}
        	${(role?is_last)?then("", "," )}
        </#list>
 
	];
     grid = $("#divManageEntityRoleGrid").grid({
      gridId: "manageEntityRoleGrid",
      colModel: colM
  });
});
function moduleTypes(uiObject){
  let cellValue = uiObject.rowData.moduleId;
  return moduleType.find(el => el[cellValue])[cellValue];
}
function checkAllBoxes(uiObject){
    

    let attrChecked = uiObject.value == "Select All"?1:0 ;
    let entityDataList = [];
    let roleId = uiObject.getAttribute("attr");
    let currentPageRecords = grid.pqGrid( "option","dataModel.data");

  $.each(currentPageRecords,function(key,value){
    let entityData = new Object();
    $("#"+value["entityRoleId"]+"_"+roleId).prop("checked",attrChecked);
    entityData.roleId= roleId;
    entityData.entityName = value["entityName"];
    entityData.moduleId = value["moduleId"];
    entityData.entityId = value["entityId"];
    entityData.isActive = attrChecked;
    entityDataList.push(entityData);
});
   saveEntity(entityDataList);
}

function addCheckBox(uiObject) {

	let rowIndxPage = uiObject.rowIndxPage;
  let columnName = uiObject.dataIndx;
  
  let attrChecked = uiObject.rowData[columnName]== null?false:uiObject.rowData[columnName].split("@::@")[1]=="0"?false:true;
  if(attrChecked){
    return "<input type=''checkbox'' id=''"+uiObject.rowData["entityRoleId"]+"_"+uiObject.column.attr+"'' checked onchange=''saveEntityRole(this)'' col=''"+columnName+"'' rowIndxPage=''"+rowIndxPage+"'' >";
  }
  else{
    return "<input type=''checkbox'' id=''"+uiObject.rowData["entityRoleId"]+"_"+uiObject.column.attr+"'' onchange=''saveEntityRole(this)'' col=''"+columnName+"'' rowIndxPage=''"+rowIndxPage+"'' >";
  }
	
}	

function saveEntityRole(thisObj){
  let entityDataList = [];
  let attrChecked = thisObj.checked == true?1:0 ;
  let columnName = thisObj.getAttribute("col");
  let rowData = grid.pqGrid( "getRowData", {rowIndxPage: thisObj.getAttribute("rowIndxPage")} );
  let colData = grid.pqGrid( "getColumn",{ dataIndx: columnName } );
  let entityRoleId  =  rowData[columnName] == null?null:rowData["entityRoleId"];
  let entityData = new Object();
  entityData.roleId= colData["attr"];
  entityData.entityName = rowData["entityName"];
  entityData.moduleId = rowData["moduleId"];
  entityData.entityId = rowData["entityId"];
  entityData.isActive = attrChecked;
  entityDataList.push(entityData);
  saveEntity(entityDataList);
}

function saveEntity(entityDataList){

    $.ajax({
		     		type : "POST",
            contentType : "application/json",
		     		url : contextPath+"/cf/suer",
		     		data : JSON.stringify(entityDataList),
		            success: function(data) {
		              
		            }
		     	});
}
</script>','admin','admin',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('47fa56d2-138c-11eb-9b1e-f48e38ab9348', 'manage-permission', '
<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>

        
<div class="container">

    

    <div class="topband">
        <h2 class="title-cls-name float-left"> Manage Permissions </h2> 
         <div class="float-right">
            <span onclick="backToPreviousPage();">
                <input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
            </span> 
        </div>
        
        <div class="clearfix"></div>        
        </div>
     
     
     <div id="tabs">
        <ul>
          <li><a href="#roleModules" data-target="/cf/mrm">${messageSource.getMessage("jws.manageRoleModules")}</a></li>
          <li><a href="#entityRoles" data-target="/cf/mer">${messageSource.getMessage("jws.manageEntityRoles")}</a></li>
         </ul>
          <div id="roleModules">
          </div>
          <div id="entityRoles">
          </div>
        </div>  
     
     
</div>    
<script>


var contextPath = "${contextPath}";
  
  $(function(){
  $("#tabs").tabs();
  $("#tabs").on( "tabsactivate", function( event, ui ) {
    let tabElement = ui.newTab[0].firstElementChild
    getTabData(tabElement); 
  });
 
   function getTabData(tabElement){ 
  	let url = tabElement.getAttribute("data-target");
  	
  	  $.ajax({
      type: "GET",
      url: url,
      success: function(data){
         if($(tabElement.getAttribute("href")).html().trim()==""){
              $(tabElement.getAttribute("href")).html(data);
          }
      }    
    });
  }
    getTabData($("a[href=''#roleModules'']")[0]);
  
  }); 


	function backToPreviousPage() {
		location.href = contextPath+"/cf/um";
	}
</script>','admin','admin',now(),2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('bef1d368-13be-11eb-9b1e-f48e38ab9348', 'role-autocomplete', '
<head>
<script src="/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.js"></script>
<script src="/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.min.js"></script>
<script src="/webjars/1.0/typeahead/typeahead.js"></script>
<link rel="stylesheet" href="/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
</head>

			<div class="col-inner-form full-form-fields">
				<div class="multiselectcount_clear_block">
					<div id="rolesMultiselect_removeAll" class="pull-right disable_cls">
						<span title="Clear All" class="clearall-cls" onclick="multiselect.removeAllElements(''rolesMultiselect'')" style="pointer-events:none">Clear All</span>
					</div>
					<div id="rolesMultiselect_count" class="multiselectcount pull-right disable_cls">
						<span title="hide show" onclick="multiselect.showHideDataDiv(''rolesMultiselect_selectedOptions'')" style="pointer-events:none">0</span>
					</div>
				</div>
				
				<label for="rolesMultiselect" style="white-space:nowrap">Roles</label>
				<input class="form-control" id="rolesMultiselect" type="text">
			
				<div id="rolesMultiselect_selectedOptions"></div>
 			</div>
		

<script>	
let multiselect;
$(function () {	
	multiselect = $("#rolesMultiselect").multiselect({
        autocompleteId: "rolesAutocomplete",
        multiselectItem: $("#rolesMultiselect_selectedOptions"),
        render: function(item) {
        	var renderStr ="";
        	if(item.emptyMsg == undefined || item.emptyMsg === "")
    		{
        		renderStr = "<p>"+item.roleName+"</p>";
    		}
        	else
    		{
        		renderStr = item.emptyMsg;	
    		}	    				        
            return renderStr;
        },
        additionalParamaters: {languageId: 1},
        extractText: function(item) {
            return item.roleName;
        },
        selectedItemRender: function(item){
            return item.roleName;
        },
        select: function(item) {
            $("#rolesMultiselect").blur();
            multiselect.setSelectedObject(item);
        },
       
    }
    );
	
    multiselect.createElementForMultiselect =  function(context, multiselectId, itemData) {
        if(context.options.duplicateCheckRule(context.selectedObjects, itemData) == false) {
        	context.selectedObject.push(itemData);
            const element = context.options.selectedItemRender(itemData);
            let listsElement = $("<li></li>");
            let itemSpan = $(''<span class="ml-selected-item" id="''+itemData.roleId+''">''+element+''</span>'');
            listsElement.append(itemSpan);
            let deleteItemContext = $(''<span class="float-right closeicon"><i class="fa fa-times-circle-o" aria-hidden="true"></i></span>'');
            if(itemData.roleName != "ADMIN"){
                    deleteItemContext.data("selected-item", itemData);
                listsElement.append(deleteItemContext);
            }
            $(".ml-selected-items-list").append(listsElement);
            if(itemData.roleName != "ADMIN"){
                var deleteItem = function(event) {
                    var data = $(deleteItemContext).data("selected-item");
                    context.deleteItem.apply(deleteItemContext, [multiselectId, data, context]);
                    
                };
                deleteItemContext.click(deleteItem);
            }
            let noOfElements = parseInt($("#"+multiselectId+"_count > span").text());
    		$("#"+multiselectId+"_count > span").text(noOfElements+1);
    		$("#"+multiselectId+"_count").removeClass("disable_cls");
    		$("#"+multiselectId+"_count > span" ).css("pointer-events","auto");
            context.selectedObjects.push(itemData);
        }else{
        	showMessage("Data already present in the list", "info")
        }

        $(context.element).val("");
        
    }

    multiselect.setSelectedObject = function (item){ 
        multiselect.createElementForMultiselect(this, this.element[0].id, item);
        	return this.selectedObject;
    }
    multiselect.options.duplicateCheckRule = function(list, obj) {
        var iCounter;
        for (iCounter = 0; iCounter < list.length; iCounter++) {
            if (list[iCounter].roleId === obj.roleId) {
                return true;
            }
        }
        return false;
       }
    
	});
    
</script>
','admin','admin',now(), 2);

REPLACE INTO autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id) VALUES
('rolesAutocomplete',' List of roles','SELECT role_name AS roleName, role_id AS roleId FROM  jws_role WHERE  role_name LIKE CONCAT("%", :searchText, "%") AND is_active=1', 1);

REPLACE INTO jws_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES (UUID(), 'system', 'system', 'regexPattern', '{"regexValue":"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=])[A-Za-z\\\\d@#$%^&+=]{6,}$","regexExample":"Ex: John@123"}', 0, NOW(), 'admin', 1.00, 'Regex Pattern to validate password');


REPLACE INTO jws_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES (UUID(), 'system', 'system', 'user-profile-form-name', '', 0, NOW(), 'admin', 1.00, 'Dynamic Form for user profile');

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('edcafd25-19b9-11eb-9631-f48e38ab9348', 'jws-change-password', ' 
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Change your password - TSMS</title>
    <@templateWithoutParams "jws-common-css-js"/>
    <script>
	 $(function(){
                $("#reloadCaptcha").click(function(event){
                	$("#imgCaptcha").attr("src", $("#imgCaptcha").attr("src")+"#");
           		});
          	});
			
			
		</script>
</head>


<body>
	<div class="container">
		<div class="row">
        <div class="col-7">
            <div class="loginbg"><img src="/webjars/1.0/images/LoginBg.jpg"></div> 
        </div> 
		<div class="col-5">    
        <form class="form-password-reset" action="/cf/updatePassword"  method="post">
        
        <h2 class="form-password-reset-heading">Change your password </h2>
        <#if errorMessage??>
        		<div class="alert alert-danger" role="alert">${errorMessage} </div>
        	</#if>
        	<#if invalidCaptcha??>
        		<div class="alert alert-danger" role="alert">${invalidCaptcha} </div>
        	</#if>
        	<p>
          		<label for="password" class="sr-only">Enter System generated Password</label>
         	 	<input type="password" id="password" name="password" class="form-control" placeholder="Enter System generated Password" required autofocus>
        	</p>
        	<p>
          		<label for="newPassword" class="sr-only">Enter New Password</label>
         	 	<input type="password" id="newPassword" name="newPassword" class="form-control" placeholder="Enter New password" required autofocus >
        	</p>
        	<#if enableCaptcha?string("yes", "no") == "yes" >
				<p>
					 <img id="imgCaptcha" name="imgCaptcha" src="/cf/captcha">
					  <span id="reloadCaptcha"><i class="fa fa-refresh" aria-hidden="true"></i></span>
					<label for="captcha" class="sr-only">Enter Captcha</label>
					<input type="text" id="captcha" name="captcha" class="form-control" placeholder="Enter Captcha" required autofocus >
				</p>
			</#if>
        	
        	 <button class="btn btn-lg btn-primary btn-block" type="submit">Change password</button>
			 <input type="hidden" id="tokenId" name ="tokenId" value="${tokenId}">
	   </form>
	</div>
	</div>	</div>		   
</body>    
','admin','admin',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('16918f98-19f6-11eb-9631-f48e38ab9348', 'jws-common-css-js', ' 
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />  
','admin','admin',now(), 2);

SET FOREIGN_KEY_CHECKS=1;