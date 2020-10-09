SET FOREIGN_KEY_CHECKS=0;

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'addEditRole', '
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
		<h2 class="title-cls-name float-left">Add Edit Role</h2> 
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
		            <label for="roledescription" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              Role description
		            </label>
				<input type="text" id="roleDescription" name="roleDescription"  value="" maxlength="2000" class="form-control">
			</div>
		</div>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="isActive" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              Is Active
		            </label>   
				<input type="checkbox" id="isActive" name="isActive" value="0" class="form-control">
			</div>
		</div>
    	</div>
    
  </form>
  <div class="row">
			<div class="col-12">
				<div class="float-right">
					<input id="formId" class="btn btn-primary" name="addTemplate" value="Save" type="button" onclick="saveData();">
					<span onclick="backToPreviousPage();">
						<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
					</span> 
				</div>
			</div>
		</div>
</div>
<script>

	contextPath = "${contextPath}";
  
  $(function(){
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
		              backToPreviousPage();
		            }
		     	});
	}
	
	//Code go back to previous page
	function backToPreviousPage() {
		location.href = contextPath+"/cf/role";
	}
</script>','admin','admin',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'role-listing', '
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
$(function () {
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
  	return ''<span id="''+roleId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
}	
function submitForm(element) {
	$("#roleId").val(element.id);
 	$("#addEditRole").submit();
}
function backToWelcomePage() {
	location.href = "/cf/um";
}

</script>','admin','admin',now());



REPLACE INTO grid_details  VALUES ('roleGrid','role listing','List of roles','jws_role','*',1);

REPLACE  INTO  jws_property_master (
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
   'system'
  ,'system'
  ,'enable-user-management'
  ,'false'
  ,0
  ,now()
  ,'admin'
  ,'1.000'
  ,'By default user management will be disabled' 
);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'manageRoleModule', '
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
		<h2 class="title-cls-name float-left">Manage Role Module</h2> 
		<div class="clearfix"></div>		
	</div>
  <form method="post" name="addEditModule" id="addEditModule">
    
        	<div class="row">
          
          <#if roles ??>
             <#assign roleIds = roles?map(role -> role?api.getRoleId())>
           <table>
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
                      <td> <input id="${module?api.getModuleId()}_${roleId}" type="checkbox"> </td>
                    </#list>
                    </tr>
                  </#list>
                </#if>
              
            </table> 
          </#if>  
    	</div>
    
  </form>
  <div class="row">
			<div class="col-12">
				<div class="float-right">
					<input id="formId" class="btn btn-primary" name="addTemplate" value="Save" type="button" onclick="saveData();">
					<span onclick="backToPreviousPage();">
						<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
					</span> 
				</div>
			</div>
		</div>
</div>
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
   let roleModulesList = [];
	function saveData (){
  $.each($("input:checkbox"),function(key,value){
  let roleModule = new Object();
  var roleModuleArray = value.id.split("_")
  roleModule.roleModuleId = value.getAttribute("roleModuleId");
  roleModule.moduleId = roleModuleArray[0];
  roleModule.roleId = roleModuleArray[1];
  roleModule.isActive = ($(value).is(":checked") ? 1 : 0);
  roleModulesList.push(roleModule);
  });
		$.ajax({
		     		type : "POST",
            contentType : "application/json",
		     		url : contextPath+"/cf/srm",
		     		data : JSON.stringify(roleModulesList),
		            success: function(data) {
		              backToPreviousPage();
		            }
		     	});
	}
	
	//Code go back to previous page
	function backToPreviousPage() {
		location.href = contextPath+"/cf/um";
	}
</script>','admin','admin',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'addEditJwsUser', '
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
		<h2 class="title-cls-name float-left">Add Edit User</h2> 
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
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="isActive" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              Is Active
		            </label>   
				<input type="checkbox" id="isActive" name="isActive" value="0" class="form-control">
			</div>
		</div>
    	</div>
        <#list roles as role>
           	<div class="col-3">
         			<div class="col-inner-form full-form-fields">
         		            <label for="${role?api.getRoleId()}" style="white-space:nowrap">
         		              ${role?api.getRoleName()}
         		            </label>   
         				<input type="checkbox" id="${role?api.getRoleId()}" name="rolesAssigned" value="0" class="form-control">
         			</div>
 		        </div>
        
                      
        </#list>
      
    
  </form>
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
</div>
<script>

	contextPath = "${contextPath}";
  
  $(function(){
     // disable admin and anonymous role
         $("#ae6465b3-097f-11eb-9a16-f48e38ab9348").attr("disabled",false);
     	$("#b4a0dda1-097f-11eb-9a16-f48e38ab9348").attr("disabled",false);
     
     
    // setting value on edit.
		 <#if (jwsUser?api.getUserId())??>
        $("#userId").val("${jwsUser?api.getUserId()}");
		 $("#firstName").val("${jwsUser?api.getFirstName()}"); 
        $("#lastName").val("${jwsUser?api.getLastName()}");
        $("#email").val("${jwsUser?api.getEmail()}"); 
         if(${jwsUser?api.getIsActive()} == 1){ 
            	 $("#isActive").attr("checked",true);
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
    userData.isActive = ($("#isActive").is(":checked") ? 1 : 0);
    userData.roleIds = [];
    $.each($("input[name=''rolesAssigned'']:checked"),function(key,value){
      userData.roleIds.push(value.id);
    });
    
    
		$.ajax({
		     		type : "POST",
            contentType : "application/json",
		     		url : contextPath+"/cf/sud",
		     		data : JSON.stringify(userData),
		            success: function(data) {
		              backToPreviousPage();
		            }
		     	});
	}
	
	//Code go back to previous page
	function backToPreviousPage() {
		location.href = contextPath+"/cf/user";
	}
</script>','admin','admin',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'jws-user-listing', '
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
$(function () {
	let colM = [
        { title: "First Name", width: 130, align: "center", dataIndx: "first_name", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Last Name", width: 130, align: "center", dataIndx: "last_name", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Email", width: 100, align: "center",  dataIndx: "email", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Is Active", width: 160, align: "center", dataIndx: "is_active", align: "left", halign: "center",
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
  	return ''<span id="''+userId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
}	
function submitForm(element) {
	$("#userId").val(element.id);
 	$("#addEditUser").submit();
}
function backToWelcomePage() {
	location.href = "/cf/um";
}

</script>','admin','admin',now());



REPLACE INTO grid_details  VALUES ('jwsUserListingGrid','user listing','List of users','jws_user','*',1);

replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('cf973388-0991-11eb-9926-e454e805e22f', 'user-management', '<div class="row">
    <div class="col-12">
        <form id="manageRoleModule" action="/cf/mrm" method="get" class="margin-r-5 pull-left">
            <button type="submit" class="btn btn-primary"> Manage Role Modules </button>
        </form>

        <form id="manageRoles" action="/cf/role" method="get" class="margin-r-5 pull-left">
            <button type="submit" class="btn btn-primary">Manage role </button>
        </form>

        <form id="manageRoles" action="/cf/user" method="get" class="margin-r-5 pull-left">
            <button type="submit" class="btn btn-primary"> Manage Users </button>
        </form>
    </div>
    <div class="col-12 margin-t-25">
        <div class="col-4 float-left col-inner-form full-form-fields">
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
        <div id="authTypeDiv" class="col-4 float-left col-inner-form full-form-fields">
            <label for="authType"><span class="asteriskmark">*</span>Authentication Type</label>
            <select id="authType" class="form-control" onchange="updatePropertyMaster();">
                <option value="in-memory">In Memory Authentication</option>
                <option value="dao-based">Database Authentication</option>
                <option value="ldap">LDAP Authentication</option>
                <option value="oauth">Oauth</option>
            </select>
        </div>
    </div>
</div>
<div id="note" class="margin-t-25">
    <i>Kindly restart your server to get your configuration working.</i>
</div>

<script>
contextPath = "${contextPath}";
	// set data
	$("#isActiveCheckbox").attr("checked",${authEnabled?c}).trigger( "change" );
	$("#authType").val("${authTypeId}");
	

    function showAuthTypeDropDown(element) {
        if(element.checked) {
            $("#authTypeDiv").show();
            updatePropertyMaster();
        } else {
            $("#authTypeDiv").hide();
            $("#authType").val("in-memory");
            updatePropertyMaster();
        }
    }
    function updatePropertyMaster(){ 
    		let authenticationEnabled= $("#isActiveCheckbox").is(":checked");
    		
    		let authenticationTypeId= $("#authType").val();
    		
    		$.ajax({
		     		type : "POST",
		     		url : contextPath+"/cf/sat",
		     		data : { 
			     		authenticationTypeId:authenticationTypeId,
			     		authenticationEnabled:authenticationEnabled
		     		},
		            success: function(data) {
		            }
		     	});
    
    }
    
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL);


   REPLACE  INTO  jws_property_master (
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
   'system'
  ,'system'
  ,'authentication-type'
  ,'in-memory'
  ,0
  ,now()
  ,'admin'
  ,'1.000'
  ,'Authentication type - default in memory authentication' 
);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'jws-welcome', '<div>
	<h1>Welcome to TSMS</h1>
	
		<#if loggedInUser == true >
			Welcome ${userName}
			<a href="tsms/users">Users</a>
			<a href="tsms/destinations">Destinations</a>
		<#else>
			<a href="register">Register here</a>
			<a href="login">Login here</a>
		</#if>

	
	
</div>
<script>
var loggedInUser = ${loggedInUser?c};
if(loggedInUser == false){ 
	$(".nav-link").hide();
} 
</script>

','admin','admin',now());


Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'jws-login', ' 
 
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Please sign in</title>
   
    	<script>
			var loggedInUser = ${loggedInUser?c};
			if(loggedInUser == false){ 
				$(".nav-link").hide();
			}
		</script>
  </head>
  <body>
     <div class="container">
      <form class="form-signin" method="post" action="/cf/login">
        <h2 class="form-signin-heading">Please sign in JWS</h2>
        	<#if queryString?? && queryString == "error">
        		<div class="alert alert-danger" role="alert">Bad Credentials</div>
        	<#elseif queryString?? &&  queryString == "logout">
        		<div class="alert alert-success" role="alert">You have been signed out</div> 
        </#if>
		<#if resetPasswordSuccess??>
        		<div class="alert alert-success" role="alert">${resetPasswordSuccess} </div>
        </#if>
        <p>
          <label for="username" class="sr-only">Email</label>
          <input type="email" id="email" name="email" class="form-control" placeholder="Email" required autofocus>
        </p>
        <p>
          <label for="password" class="sr-only">Password</label>
          <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
        </p>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        <p>
        <a href="resetPasswordPage">Forgot password?</a> 
        </p>
        <p >New to TSMS?
        <a href="register"> Click here to register</a>
        </p>
      </form>
</div>
</body></html>','admin','admin',now());


SET FOREIGN_KEY_CHECKS=1;