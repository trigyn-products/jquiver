ALTER TABLE jws_role DROP IF EXISTS role_priority;
ALTER TABLE jws_role ADD role_priority INT(11) AFTER is_active;

UPDATE jws_role SET role_priority = 1 WHERE role_id = 'b4a0dda1-097f-11eb-9a16-f48e38ab9348';
UPDATE jws_role SET role_priority = 2 WHERE role_id = '2ace542e-0c63-11eb-9cf5-f48e38ab9348';
UPDATE jws_role SET role_priority = 3 WHERE role_id = 'ae6465b3-097f-11eb-9a16-f48e38ab9348';

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
		            <label for="rolePriority" style="white-space:nowrap"><span class="asteriskmark">*</span>Role Priority</label>
				<input type="number" id="rolePriority" name="rolePriority"  value="" class="form-control">
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
					<div class="btn-group dropup custom-grp-btn">
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
            $("#rolePriority").val("${jwsRole?api.getRolePriority()!''''}"); 
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
    roleData.rolePriority = $("#rolePriority").val();
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
        { title: "Role Priority", width: 160, align: "center", dataIndx: "role_priority", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Is Active", width: 160, align: "center", dataIndx: "is_active", align: "left", halign: "center", render: roleStatus },
        { title: "Action", width: 30, minWidth: 115, align: "center", render: editRole, dataIndx: "action" }
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

function roleStatus(uiObject){
	const isActive = uiObject.rowData.is_active;
	if(isActive == 1){
		return ''Active'';
	}else{
		return ''Inactive'';
	}
}
	
function backToWelcomePage() {
	location.href = "/cf/um";
}
function showErrorMessage(){ 
	showMessage("You cant edit system roles","error");
}

</script>','admin','admin',now(), 2);