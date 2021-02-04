
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
        { title: "${messageSource.getMessage(''jws.isActive'')}", width: 160, align: "center", dataIndx: "is_active", render: userStatus , align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Action", width: 30, minWidth: 115, align: "center", render: editUser, dataIndx: "action" }
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

function userStatus(uiObject){
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
	showMessage("You cant edit admin user","error");
}
</script>','aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);