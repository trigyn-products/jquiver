Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('0d91402d-1062-11eb-a867-f48e38ab8cd7', 'dashlet-common-div', '
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />

 
<div dashlet-id="${dashletId}" class="grid-stack-item" data-gs-x="${xCord}" data-gs-y="${yCord}" data-gs-width="${width}" data-gs-height="${height}">
    <div class="panel panel-default grid-stack-item-content">
        <div class="row heading panel-heading dashlet-header-panel">
        
          <div class="dashlet_header_block">
              <div class="dashlet_title_name">
                      <span class="float-left"><b>${dashletTitle}</b></span>
              </div>
              
              <div class="dashlet_header_buttons">
                   <span class="dashlet_common_button" onclick="refreshDashletContent(''${dashletId}'');">
                   <i class="fa fa-refresh" aria-hidden="true"></i>
                 </span>
              
                 <span class="dashlet_common_button" onclick="openConfigurationDialog(''${dashletId}'');">
                  <i class="fa fa-cog" aria-hidden="true"></i>
                 </span>
              </div>
          
          </div>
          
        </div>
        <div class="panel-body">
            <div id="dashlet_${dashletId}">
              ${content}
            </div>        
      </div>
    </div>
</div>','aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);
  
Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('1389accc-1062-11eb-a867-f48e38ab8cd7', 'dashlets', '
	<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
	<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
	<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
	<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
	<script src="/webjars/lodash/4.17.15/dist/lodash.js"></script>
	<script src="/webjars/gridstack/0.4.0/dist/gridstack.js"></script>
	<script src="/webjars/gridstack/0.4.0/dist/gridstack.jQueryUI.js"></script>
	<script src="/webjars/jquery-blockui/2.70/jquery.blockUI.js"></script>
	<script type="text/javascript" src="/webjars/jqplot/1.0.9.d96a669/jquery.jqplot.js"></script>
	<script type="text/javascript" src="/webjars/jqplot/1.0.9.d96a669/plugins/jqplot.categoryAxisRenderer.js"></script>
	<script type="text/javascript" src="/webjars/jqplot/1.0.9.d96a669/plugins/jqplot.canvasAxisTickRenderer.js"></script>
	<script type="text/javascript" src="/webjars/jqplot/1.0.9.d96a669/plugins/jqplot.canvasTextRenderer.js"></script>
	<script type="text/javascript" src="/webjars/jqplot/1.0.9.d96a669/plugins/jqplot.pointLabels.js"></script>
  	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="/webjars/jqplot/1.0.9.d96a669/jquery.jqplot.css" />
	<link rel="stylesheet" href="/webjars/gridstack/0.4.0/dist/gridstack.css" />
	<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script>
	let crntdashletId;
	let configDailog;
	const contextPath = "${(contextPath)!''''}";
$(function() {
$(".ui-dialog-content").dialog("destroy").html("");
    let options = {};
    $(".grid-stack").gridstack(options);
    let grid = $(".grid-stack").data("gridstack");
    grid.enableResize(false, true); $(".grid-stack").on("change", function (event, ui) {
    let dashlets = new Array();
    if(ui != undefined)
    	{
    	for(dashCnt=0;dashCnt<ui.length;dashCnt++){
    		let node = ui[dashCnt];
    		let nodeObj={"id":node.el[0].attributes[0].value,"x":node.x, "y":node.y};
    		dashlets.push(JSON.stringify(nodeObj));
    	}
    	}
    	let blankNodeObj={"id":"","x":null, "y":null};
		dashlets.push(JSON.stringify(blankNodeObj));
		let dashboardId= $("#dashboardId").val();
    	let url = contextPath+"/cf/sdc";
     	$.ajax({
     		type : "POST",
     		url : url,
     		data : {
				 dashlets:dashlets,
				 dashboardId:dashboardId
				 }
     	});
    });
});

function openConfigurationDialog(dashletId){
	crntdashletId=dashletId;
	$("#configurationDialog").html(''<div class="loading_gif"></div>'');
	configDailog = $("#configurationDialog").dialog({
		title : "Configuration",
		resizable:false,
    close: function() {configDailog.dialog("destroy").html("");}
	});
	$("#configurationDialog").html(''<div class="loading_gif"></div>'');
  	let dashboardId= $("#dashboardId").val();
	$("#configurationDialog").load(contextPath+"/cf/oc", {"dashletId" : dashletId, "dashboardId": dashboardId});
}
                
function refreshDashletContent(dashletId){ 
	$("#dashlet_"+dashletId).parent().parent().block({ message: null }); //For blocking div of dashlet
	
	let paramArray = new Array();
	paramArray.push("");
	let url = contextPath+"/cf/rdc";
	$.ajax({
		type : "POST",
		url : url,
		data : {dashletId:dashletId, paramArray:paramArray},
		success : function(data) {
			$("#dashlet_"+dashletId).parent().parent().unblock(); //For unblocking div of dashlet
			$("#dashlet_"+dashletId).html("");
			$("#dashlet_"+dashletId).html(data);
		},
	});	
}
        
function closeConfigurationDialog(){
	configDailog.dialog("close");
}

function saveConfiguration(){
	let isValid;
	jQuery.each(jQuery("#formConfiguration").find("input[pattern]"),function(index, inputWithPattern){
	let pattern = inputWithPattern.pattern;
	let inputId = inputWithPattern.id;
	if(pattern != "" || pattern != null || pattern != "undefined"){
		isValid = validateInput(pattern , inputId);
	}

	if(isValid == false){
		jQuery("#"+inputId).css("border","1px solid red");
		jQuery("#"+inputId).attr("error",true);
	}else{
		jQuery("#"+inputId).css("border","1px solid #ccc");
		jQuery("#"+inputId).removeAttr("error");
	}
});

if(jQuery("#formConfiguration").find("input[error]").length <= 0){
	let formData = $("#formConfiguration").serialize();
	let url = contextPath+"/cf/sc";
	$.ajax({
		type : "POST",
			url  : url,
			data : formData,
			success : function(data){
				refreshDashletContent(crntdashletId);
				closeConfigurationDialog();
			}
		});
	}
}

function validateInput(pattern , inputId) {
    let newValue = jQuery("#"+inputId).val();
    if (eval(pattern).test(newValue)) {
        return true;
    } else {
        return false;
    }
}

function onSliderChange(a_elementId){
	$("#slider-value_"+a_elementId).html(jQuery("#" + a_elementId).val());
}

function backToDashboardPage() {
  location.href = contextPath+"/cf/dbm"
}
</script>

<style type="text/css">
.grid-stack-item-content {
	color: #2c3e50;
	text-align: center;
}

.grid-stack .grid-stack-item {
	background: rgba(255, 255, 255, 0.3);
}

.grid-stack .grid-stack-item .grid-stack-item-content {
	background: #e1ebf6;
}

.heading {
    background: #5da9fc;
}

.ui-dialog-titlebar {
    background: #5da9fc !important;
}
</style>
<div class="container">
<input type="hidden" value="${dashboardId}" id="dashboardId"/>
<div class="grid-stack" data-gs-width="12" data-gs-animate="yes">
		<#list dashletUIs as dashletUI>
			${dashletUI}
		</#list>
</div>

<div id="configurationDialog"></div></div>','aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('1f8682c4-1062-11eb-a867-f48e38ab8cd7', 'dashlet-configuration', '
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script>
</script>
<input type="hidden" value="${dashboardId}" id="dashboardId"/>
<div>
	<#if properties?size != 0>
		Configurations
	<#else>
		<h5 class="center">No properties to configure</h5> 
	</#if>
</div>
<div class="float-right">
	<#if properties?size != 0>
		<input type="button" onclick="closeConfigurationDialog();" value="Cancel" class="btn btn-sm btn-secondary"/>
		<input type="button" value="Save" class="btn btn-sm btn-primary"/>
	<#else>
		<input type="button" onclick="closeConfigurationDialog();" value="Ok" class="btn btn-sm btn-primary"/>
	</#if>
</div>
','aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('25e7cb01-1062-11eb-a867-f48e38ab8cd7', 'select', '<script type="text/javascript">
</script>
<div class="col-lg-9 col-md-9 col-sm-8">
<select id="" class="select form-control" name=""></select>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('2ab671a3-1062-11eb-a867-f48e38ab8cd7', 'text', '<script type="text/javascript"></script>
<div class="col-lg-9 col-md-9 col-sm-8">
<input type="text" id="" name="" value="" class="form-control"/>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('2ff2b7ea-1062-11eb-a867-f48e38ab8cd7', 'rangeslider', '<div class="col-lg-9 col-md-9 col-sm-8">
<div class="range-slider">
<input type="range" id="" class="range-slider__range" name="${property.propertyId}" value="" min="0" max="${property.value}" step="1"/>
<span id="slider-value_" class="range-slider__value"></span>
</div>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('369aa97d-1062-11eb-a867-f48e38ab8cd7', 'number', '<script type="text/javascript"></script><div class="col-lg-9 col-md-9 col-sm-8">
<input type="number" min="0" id="" name="" value="" class="form-control"/>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('3b902ade-1062-11eb-a867-f48e38ab8cd7', 'datepicker', '<script src="${contextPath}/resources/jscore/jquery/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
<script type="text/javascript">
jQuery(function() {
	var dates = jQuery(".date").datepicker({
		showOn : "both",
		dateFormat : "dd-M-yy",
		changeMonth : true,
		changeYear : true
	}); 
});
</script>
<div class="col-lg-9 col-md-9 col-sm-8 configurepop">
<input type="text" id="" name="" class="date form-control" value=""/>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('40a58aae-1062-11eb-a867-f48e38ab8cd7', 'checkbox', '<div class="col-lg-9 col-md-9 col-sm-8">
 <div class="control--checkbox">
  <input type="checkbox" class="roles" name="${property.propertyId}" value="true" />
  <label for="checkbox1"><span></span>True</label>
  <input type="checkbox" class="roles" name="${property.propertyId}" value="false"/>
  <label for="checkbox1"><span></span>False</label>
 </div>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('46154204-1062-11eb-a867-f48e38ab8cd7', 'monthpicker', '', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);




SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO dashlet (dashlet_id, dashlet_title, dashlet_name, dashlet_body, dashlet_query, is_active, created_by, created_date, updated_by, updated_date, x_coordinate, y_coordinate, dashlet_width, dashlet_height, context_id, show_header,dashlet_query_checksum,dashlet_body_checksum, dashlet_type_id) VALUES
('09b78b43-eade-11ea-a036-e454e805e22f', 'Grids', 'Grids', '<div>Total of <#list resultSet as queryOutput>${queryOutput.gridCount}</#list> used in application</div>', 'SELECT COUNT(gd.grid_id) AS gridCount FROM grid_details AS gd', 1, 'admin', NOW(), 'admin', NOW(), 0, 0, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2), 
('0eb8adc4-eade-11ea-a036-e454e805e22f', 'Notification', 'Notification', '<div> Notifications </div>', 'select 1', 1, 'admin', NOW(), 'admin', NOW(), 0, 3, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2), 
('31c9ffa9-eadf-11ea-a036-e454e805e22f', 'Templates', 'Templates', '<div>Total of <#list resultSet as queryOutput>${queryOutput.templateCount}</#list> used in application</div>', 'SELECT COUNT(tm.template_id) AS templateCount FROM template_master AS tm', 1, 'admin', NOW(), 'admin', NOW(), 6, 3, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2), 
('37dbbc8d-eadf-11ea-a036-e454e805e22f', 'DB resource bundles', 'DB resource bundles', '<div>Total of <#list resultSet as queryOutput>${queryOutput.resourceBundleCount}</#list> used in application</div>', 'SELECT COUNT(DISTINCT(rb.resource_key)) AS resourceBundleCount FROM resource_bundle AS rb', 1, 'admin', NOW(), 'admin', NOW(), 0, 6, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2), 
('3d97273b-eadf-11ea-a036-e454e805e22f', 'Dashboards', 'Dashboards', '<div>Total of <#list resultSet as queryOutput>${queryOutput.dashboardCount}</#list> used in application</div>', 'SELECT COUNT(db.dashboard_id) AS dashboardCount FROM  dashboard AS db', 1, 'admin', NOW(), 'admin', NOW(), 6, 3, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2),
('44cb330d-eadf-11ea-a036-e454e805e22f', 'Dashlets', 'Dashlets', '<head>

<script src="/webjars/1.0/pqGrid/pqgrid.min.js"></script>     
<link rel="stylesheet" href="/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script src="/webjars/1.0/gridutils/gridutils.js"></script>
</head>

<div class="container">
		
		<div id="divdDashletMasterGrid"></div>

		<div id="snackbar"></div>
</div>


<form action="${(contextPath)!''''}/cf/aedl" method="POST" id="formDMRedirect">
	<input type="hidden" id="dashletId" name="dashlet-id">
</form>
<script>
	$(function () {
		var colM = [
			{ title: "${messageSource.getMessage(''jws.dashletName'')}", width: 130, dataIndx: "dashletName" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.dashletTitle'')}", width: 130, dataIndx: "dashletTitle", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.createdBy'')}", width: 100, dataIndx: "createdBy" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.createdDate'')}", width: 100, dataIndx: "createdDate", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.updatedBy'')}", width: 100, dataIndx: "updatedBy" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.updatedDate'')}", width: 100, dataIndx: "updatedDate" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.status'')}", width: 160, dataIndx: "status" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editDashlet}
		];
	
		var grid = $("#divdDashletMasterGrid").grid({
			gridId: "dashletMasterListingGrid",
			colModel: colM
		});
	});
	function editDashlet(uiObject) {
		const dashletId = uiObject.rowData.dashletId;
		return ''<span id="''+dashletId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"  title="${messageSource.getMessage("jws.editDashlet")}"></i></span>''.toString();
	}
	
	function submitForm(element) {
	  $("#dashletId").val(element.id);
	  $("#formDMRedirect").submit();
	}
	
	function backToDashboarListing() {
		location.href = contextPath+"/cf/dbm";
	}
</script>', 'SELECT dashlet_id AS dashletId, dashlet_title AS dashletTitle,dashlet_name AS dashletName,DATE_FORMAT(created_date,"%d %b %Y") AS createdDate,DATE_FORMAT(updated_date,"%d %b %Y") AS updatedDate, updated_by AS updatedBy, created_by AS createdBy,is_active AS status FROM dashlet', 1, 'admin', NOW(), 'admin', NOW(), 6, 0, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2);

REPLACE into dashboard (dashboard_id, dashboard_name, context_id, dashboard_type, created_by, created_date, last_updated_date, is_deleted, is_draggable, is_exportable) VALUES
('ab7202bf-eadd-11ea-a036-e454e805e22f', 'Java Stater Usages', (SELECT context_id FROM context_master where context_description='jws'), 2, 'admin', NOW(), NOW(), 0, 1, 0);



SET FOREIGN_KEY_CHECKS=1;


