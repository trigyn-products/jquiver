Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('0d91402d-1062-11eb-a867-f48e38ab8cd7', 'dashlet-common-div', '
<script src="${(contextPath)!''''}/webjars/jquery-blockui/2.70/jquery.blockUI.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />

 
<div dashlet-id="${dashletId}" class="grid-stack-item" data-gs-x="${xCord}" data-gs-y="${yCord}" data-gs-width="${width}" data-gs-height="${height}" style="position: absolute;">
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
  
Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('1389accc-1062-11eb-a867-f48e38ab8cd7', 'dashlets', '
	<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
	<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
	<script src="${(contextPath)!''''}/webjars/lodash/4.17.15/lodash.js"></script>
	<script src="${(contextPath)!''''}/webjars/gridstack/0.4.0/dist/gridstack.js"></script>
	<script src="${(contextPath)!''''}/webjars/gridstack/0.4.0/dist/gridstack.jQueryUI.js"></script>
	<script src="${(contextPath)!''''}/webjars/jquery-blockui/2.70/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${(contextPath)!''''}/webjars/jqplot/1.0.9.d96a669/jquery.jqplot.js"></script>
	<script type="text/javascript" src="${(contextPath)!''''}/webjars/jqplot/1.0.9.d96a669/plugins/jqplot.categoryAxisRenderer.js"></script>
	<script type="text/javascript" src="${(contextPath)!''''}/webjars/jqplot/1.0.9.d96a669/plugins/jqplot.canvasAxisTickRenderer.js"></script>
	<script type="text/javascript" src="${(contextPath)!''''}/webjars/jqplot/1.0.9.d96a669/plugins/jqplot.canvasTextRenderer.js"></script>
	<script type="text/javascript" src="${(contextPath)!''''}/webjars/jqplot/1.0.9.d96a669/plugins/jqplot.pointLabels.js"></script>
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jqplot/1.0.9.d96a669/jquery.jqplot.css" />
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/gridstack/0.4.0/dist/gridstack.css" />
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script>
	let crntdashletId;
	let configDailog;
	contextPath = "${(contextPath)!''''}";
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
		modal: true,
		maxHeight: 300,
		closeOnEscape : true,
		draggable	 : true,
		resizable	 : false,
		bgiframe		 : true,
		autoOpen		 : true, 
		position: {
		 my: "center", at: "center", of: window
		},
    	close: function() {
    		configDailog.dialog("close");
    	},
    	open		: function( event, ui ) {
               
			 $(".ui-dialog-titlebar")
		   	    .find("button").removeClass("ui-dialog-titlebar-close").addClass("ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close")
		       .prepend(''<span class="ui-button-icon ui-icon ui-icon-closethick"></span>'').append(''<span class="ui-button-icon-space"></span>'');
       	}	
	});
	$("#configurationDialog").html(''<div class="loading_gif"></div>'');
  	let dashboardId= $("#dashboardId").val();
	$("#configurationDialog").load(contextPath+"/cf/oc", {"dashletId" : dashletId, "dashboardId": dashboardId});
}
                
function refreshDashletContent(dashletId){ 
	//$("#dashlet_"+dashletId).parent().parent().block({ message: null }); //For blocking div of dashlet
	let paramArray = new Array();
	paramArray.push("");
	let url = contextPath+"/cf/rdc";
	$.ajax({
		type : "POST",
		url : url,
		data : {dashletId:dashletId, paramArray:paramArray,dashboardId:$("#dashboardId").val()},
		success : function(data) {
			//$("#dashlet_"+dashletId).parent().parent().unblock(); //For unblocking div of dashlet
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
				closeConfigurationDialog();
				refreshDashletContent(crntdashletId);
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

Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('1f8682c4-1062-11eb-a867-f48e38ab8cd7', 'dashlet-configuration', '



<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script>
</script>
<form id="formConfiguration">
	<input type="hidden" value="${dashboardId}" id="dashboardId" name="dashboardId"/>
	<div>
	    <#if dashletPropertyVOs?? && dashletPropertyVOs?has_content && dashletPropertyVOs?size != 0>
	        <#list dashletPropertyVOs as dashletPropertyVO>
	            <#assign templateName = dashletPropertyVO.getType()>
	            <#if dashletPropertyVO.getDefaultValue()?? && dashletPropertyVO.getDefaultValue()?has_content>
	                <#assign templateParam = {
	              	    "propertyId": dashletPropertyVO.getPropertyId(),
	              	    "displayName": dashletPropertyVO.getDisplayName(),
	              	    "propertyValue": dashletPropertyVO.getValue(),
	               		"defaultValue": dashletPropertyVO.getDefaultValue()
	           		}>
	                <@templateWithParams templateName templateParam/>
	            <#else>
	                <#assign templateParam = {
	               	    "propertyId": dashletPropertyVO.getPropertyId(),
	               	    "displayName": dashletPropertyVO.getDisplayName(),
	               	    "propertyValue": dashletPropertyVO.getValue(),
	               		"defaultValue": ""
	           		}>
	                <@templateWithParams templateName templateParam/>
	            </#if>
	        </#list> 
	    <#else>
	        <h5 class="center">No properties to configure</h5> 
		</#if>
	</div>
</form>
<div class="float-right margin-t-25">
	<#if properties?size != 0>
		<input type="button" onclick="closeConfigurationDialog();" value="Cancel" class="btn btn-sm btn-secondary"/>
		<input type="button" value="Save" onclick="saveConfiguration();" class="btn btn-sm btn-primary"/>
	<#else>
		<input type="button" onclick="closeConfigurationDialog();" value="Ok" class="btn btn-sm btn-primary"/>
	</#if>
</div>


','aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('25e7cb01-1062-11eb-a867-f48e38ab8cd7', 'select', '<script type="text/javascript">
	var selectOptions =  "${innerTemplateObj.propertyValue!''''}";
	var selectId = "${innerTemplateObj.propertyId!''''}";
	if(selectOptions.trim() !== ""){ 
		let optionArray = selectOptions.split(", "); 
		$.each(optionArray, function(index, optionValue){
			$("#"+selectId).append("<option value=''"+optionValue+"''>"+optionValue+"</option>");
		})
	}
	$("#"+selectId).val("${innerTemplateObj.defaultValue!''''}");
</script>
<div class="col-12 margin-b-10">
	<label for="${innerTemplateObj.propertyId!''''}" style="white-space:nowrap">${innerTemplateObj.displayName!''''}</label>
	<select id="${innerTemplateObj.propertyId!''''}" class="select form-control" name="${innerTemplateObj.propertyId!''''}"></select>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('2ab671a3-1062-11eb-a867-f48e38ab8cd7', 'text', '<script type="text/javascript"></script>
<div class="col-12 margin-b-10">
	<label for="${innerTemplateObj.propertyId!''''}" style="white-space:nowrap">${innerTemplateObj.displayName!''''}</label>
	<input type="text" id="${innerTemplateObj.propertyId!''''}" name="${innerTemplateObj.propertyId!''''}" value="${innerTemplateObj.defaultValue!''''}" class="form-control"/>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('2ff2b7ea-1062-11eb-a867-f48e38ab8cd7', 'rangeslider', '<div class="col-12 margin-b-10">
<div class="range-slider">
	<label for="${innerTemplateObj.propertyId!''''}" style="white-space:nowrap">${innerTemplateObj.displayName!''''}</label>
	<input type="range" id="" class="range-slider__range" name="${innerTemplateObj.propertyId!''''}" value="" min="0" max="${innerTemplateObj.defaultValue!''0''}" step="1"/>
	<span id="slider-value_" class="range-slider__value"></span>
</div>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('369aa97d-1062-11eb-a867-f48e38ab8cd7', 'number', '<script type="text/javascript"></script>
<div class="col-12 margin-b-10">
	<label for="${innerTemplateObj.propertyId!''''}" style="white-space:nowrap">${innerTemplateObj.displayName!''''}</label>
	<input type="number" min="0" id="${innerTemplateObj.propertyId!''''}" name="${innerTemplateObj.propertyId!''''}" value="${innerTemplateObj.defaultValue!''''}" class="form-control"/>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);


Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('91f4ce5c-4e85-11eb-9c1c-f48e38ab8cd7', 'decimal', '<script type="text/javascript"></script>
<div class="col-12 margin-b-10">
	<label for="${innerTemplateObj.propertyId!''''}" style="white-space:nowrap">${innerTemplateObj.displayName!''''}</label>
	<input type="number" min="0" id="${innerTemplateObj.propertyId!''''}" name="${innerTemplateObj.propertyId!''''}" value="${innerTemplateObj.defaultValue!''''}" step=".01" class="form-control"/>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);


Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('3b902ade-1062-11eb-a867-f48e38ab8cd7', 'datepicker', '
<link rel="stylesheet" type="text/css" href="${(contextPath)!''''}/webjars/1.0/JSCal2/css/jscal2.css" />
<link rel="stylesheet" type="text/css" href="${(contextPath)!''''}/webjars/1.0/JSCal2/css/border-radius.css" />
<link rel="stylesheet" type="text/css" href="${(contextPath)!''''}/webjars/1.0/JSCal2/css/steel/steel.css" />
<script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/JSCal2/js/lang/en.js"></script>
<script type="text/javascript">
jQuery(function() {
	Calendar.setup({
			trigger    : "${innerTemplateObj.propertyId!''''}-trigger",
			inputField : "${innerTemplateObj.propertyId!''''}",
			dateFormat : "%d-%b-%Y",
			weekNumbers: true,
			onSelect   : function() {
				let selectedDate = this.selection.get();
				let date = Calendar.intToDate(selectedDate);
				date = Calendar.printDate(date, "%d-%b-%Y");
				$("#"+this.inputField.id).val(date);
				this.hide(); 
			}
		});
});
var dateInputId = "${innerTemplateObj.propertyId!''''}";
var defaultDate = "${innerTemplateObj.defaultValue!''''}";
if(defaultDate.trim() !== ""){ 
	$("#"+dateInputId).val(Calendar.printDate(Calendar.parseDate(defaultDate,false),"%d-%b-%Y"));
}
</script>
<div class="col-12 margin-b-10">
	<div class="dashlet-config-calendar">
		<label for="${innerTemplateObj.propertyId!''''}" style="white-space:nowrap">${innerTemplateObj.displayName!''''}</label>
		<input class="form-control" id="${innerTemplateObj.propertyId!''''}"  name= "${innerTemplateObj.propertyId!''''}" placeholder="" />
		<button id="${innerTemplateObj.propertyId!''''}-trigger" class="calender_icon">
			<i class="fa fa-calendar" aria-hidden="true"></i>
		</button>
	</div>
</div>
					
', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('40a58aae-1062-11eb-a867-f48e38ab8cd7', 'checkbox', '
<script type="text/javascript">
	
</script>
<div class="col-12 margin-b-10">
 <div class="control--checkbox">
  <label for="${innerTemplateObj.propertyId!''''}" style="white-space:nowrap">${innerTemplateObj.displayName!''''}</label>
  <input type="checkbox" class="roles" name="${innerTemplateObj.propertyId!''''}" value="true" />
  <label for="checkbox1"><span></span>True</label>
  <input type="checkbox" class="roles" name="${innerTemplateObj.propertyId!''''}" value="false"/>
  <label for="checkbox1"><span></span>False</label>
 </div>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);

Replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('46154204-1062-11eb-a867-f48e38ab8cd7', 'monthpicker', '', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now(), 2);


DELETE FROM jq_dashboard_lookup_category WHERE lookup_category_id = 'c1f03803-c862-11e7-a62a-f48e38ab9229';
DELETE FROM jq_template_master WHERE template_id = '46154204-1062-11eb-a867-f48e38ab8cd7';


SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_dashlet (dashlet_id, dashlet_title, dashlet_name, dashlet_body, dashlet_query, is_active, created_by, created_date, updated_by, updated_date, x_coordinate, y_coordinate, dashlet_width, dashlet_height, context_id, show_header,dashlet_query_checksum,dashlet_body_checksum, dashlet_type_id) VALUES
('09b78b43-eade-11ea-a036-e454e805e22f', 'Grids', 'Grids', '<div class="jws-dashlet-view">Total of <#if resultSet?? && resultSet?has_content><#list resultSet as queryOutput>${queryOutput.gridCount}</#list></#if> used in application</div>', 'SELECT COUNT(gd.grid_id) AS gridCount FROM jq_grid_details AS gd', 1, 'aar.dev@trigyn.com', NOW(), 'aar.dev@trigyn.com', NOW(), 0, 0, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2), 
('0eb8adc4-eade-11ea-a036-e454e805e22f', 'Notification', 'Notification', '<div class="jws-dashlet-view"> Total of <#if resultSet?? && resultSet?has_content><#list resultSet as queryOutput>${queryOutput.notificationCount}</#list></#if> used in application</div>', 'SELECT COUNT(gun.notification_id) AS notificationCount FROM jq_generic_user_notification AS gun ', 1, 'aar.dev@trigyn.com', NOW(), 'aar.dev@trigyn.com', NOW(), 0, 3, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2), 
('31c9ffa9-eadf-11ea-a036-e454e805e22f', 'Templates', 'Templates', '<div class="jws-dashlet-view">Total of <#if resultSet?? && resultSet?has_content><#list resultSet as queryOutput>${queryOutput.templateCount}</#list></#if> used in application</div>', 'SELECT COUNT(tm.template_id) AS templateCount FROM jq_template_master AS tm WHERE tm.updated_date > STR_TO_DATE("${startDate}","%d-%b-%Y") AND tm.updated_date <= STR_TO_DATE("${endDate}","%d-%b-%Y")', 1, 'aar.dev@trigyn.com', NOW(), 'aar.dev@trigyn.com', NOW(), 6, 3, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2), 
('37dbbc8d-eadf-11ea-a036-e454e805e22f', 'DB resource bundles', 'DB resource bundles', '<div class="jws-dashlet-view">Total of <#if resultSet?? && resultSet?has_content><#list resultSet as queryOutput>${queryOutput.resourceBundleCount}</#list></#if> used in application</div>', 'SELECT COUNT(DISTINCT(rb.resource_key)) AS resourceBundleCount FROM jq_resource_bundle AS rb', 1, 'aar.dev@trigyn.com', NOW(), 'aar.dev@trigyn.com', NOW(), 0, 6, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2), 
('3d97273b-eadf-11ea-a036-e454e805e22f', 'Dashboards', 'Dashboards', '<div class="jws-dashlet-view">Total of <#if resultSet?? && resultSet?has_content><#list resultSet as queryOutput>${queryOutput.dashboardCount}</#list></#if> used in application</div>', 'SELECT COUNT(db.dashboard_id) AS dashboardCount FROM  jq_dashboard AS db', 1, 'aar.dev@trigyn.com', NOW(), 'aar.dev@trigyn.com', NOW(), 6, 3, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2),
('44cb330d-eadf-11ea-a036-e454e805e22f', 'Dashlets', 'Dashlets', '<head>

<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>     
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script>
</head>

<div class="jws-dashlet-view">
	<div class="container">
			
			<div id="divdDashletMasterGrid"></div>
	
			<div id="snackbar"></div>
	</div>
</div>


<form action="${(contextPath)!''''}/cf/aedl" method="POST" id="formDMRedirect">
	<input type="hidden" id="dashletId" name="dashlet-id">
</form>
<script>
	contextPath = "${contextPath}";
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
			{ title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editDashlet, sortable: false}
		];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        };
	
		var grid = $("#divdDashletMasterGrid").grid({
			gridId: "dashletMasterListingGrid",
			colModel: colM,
          dataModel: dataModel
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
</script>', 'SELECT dashlet_id AS dashletId, dashlet_title AS dashletTitle,dashlet_name AS dashletName,DATE_FORMAT(created_date,"%d %b %Y") AS createdDate,DATE_FORMAT(updated_date,"%d %b %Y") AS updatedDate, updated_by AS updatedBy, created_by AS createdBy,is_active AS status FROM jq_dashlet', 1, 'aar.dev@trigyn.com', NOW(), 'aar.dev@trigyn.com', NOW(), 6, 0, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1,null,null, 2);

REPLACE INTO jq_dashlet(dashlet_id, dashlet_name, dashlet_title, x_coordinate, y_coordinate, dashlet_width, dashlet_height, context_id, show_header, dashlet_query, dashlet_body, created_by, created_date, updated_by, updated_date, is_active, dashlet_query_checksum, dashlet_body_checksum, dashlet_type_id) VALUES
('76ad58a3-afa3-4efd-a872-9a78a9e01a94', 'All Modules', 'Modules', 0, 0, 6, 3, 'a0bb79ce-eadd-11ea-a036-e454e805e22f', 1, 'Select 1', '<script src="${(contextPath)!''''}/webjars/1.0/chartjs/chart.js"></script>
<div class="jws-dashlet-view">
      <div class="row">
          
         <div class="col-12">
            <div class="bs-example">
               <div class="accordion" id="accordionExample">
                  <div class="card">
                     <div class="card-header" id="headingOne">
                        <h2 class="mb-0">
                           <button type="button" class="btn btn-link collapsed" data-toggle="collapse" data-target="#collapseOne">Modules Count <i class="fa fa-plus"></i></button>									
                        </h2>
                     </div>
                     <div id="collapseOne" class="collapse show" aria-labelledby="headingOne" data-parent="#accordionExample">
                        <div class="card-body">
                           <div id="dashlet-module-content">
                              <canvas id="dashlet-module-chart" width="500" height="200"></canvas>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
</div>

<script>
$(function() {
	contextPath = "${(contextPath)!''''}";
    let startDate = "${startDate!''''}";
    let endDate = "${endDate!''''}";
    let gridName = "${gridName!''''}";
    

    $.ajax({
        type : "GET",
        async : false,
        url : contextPath+"/api/dashlet-module-details",
        data : {
            startDate: startDate,
            endDate: endDate,
            gridName : gridName
        }, 
        success : function(data) {
           createModuleChart(data);
        }
    });
});

function createModuleChart(data){
    let labels = new Array();
    $.each(Object.keys(data), function(index, label){
        labels.push(label.charAt(0).toUpperCase() + label.slice(1));
    });
    let datasets = new Array();
    let dataValue = {
	  label: "Module count",
	  backgroundColor: "rgba(0, 0, 0, 1)",
	  borderColor: "rgba(0, 0, 0, 1)",
	  pointColor: "rgba(0, 0, 0, 1)",
	}
    let pointValues = new Array();
	for(let counter = 0; counter < Object.keys(data).length; counter++) {
		let pointValue = Object.keys(data[Object.keys(data)[counter]][0]);
        let moduleObj = data[Object.keys(data)[counter]][0];
        let moduleKey = Object.keys(moduleObj)[0];
	    pointValues.push(moduleObj[moduleKey]);
	}
	dataValue["data"] = pointValues;
	datasets.push(dataValue);
    let chartData = {labels: labels, datasets: datasets}
	let ctx = document.getElementById("dashlet-module-chart").getContext("2d");
	let dashletModuleChart = new Chart(ctx, {
		type: "bar",
		data: chartData,
		options: {
			animation: false
		}
	}); 
}
</script>', 'aar.dev@trigyn.com', NOW(), 'aar.dev@trigyn.com', NOW(), 1, NULL, NULL, 2);

REPLACE INTO jq_dynamic_rest_details(jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id) VALUES
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


REPLACE INTO jq_dynamic_rest_dao_details(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(102, '8f4d6b36-167e-4d69-a6e7-1f88423f451a', 'templateDetails', 'SELECT COUNT(tm.template_id) AS templateCount 
FROM jq_template_master AS tm 
WHERE tm.updated_date > STR_TO_DATE(:startDate,"%d-%b-%Y") 
AND tm.updated_date <= STR_TO_DATE(:endDate,"%d-%b-%Y")', 1, 1), 
(103, '8f4d6b36-167e-4d69-a6e7-1f88423f451a', 'gridDetails', 'SELECT COUNT(gd.grid_id) AS gridCount 
FROM jq_grid_details AS gd 
WHERE gd.grid_name LIKE "%":gridName"%"', 2, 1), 
(104, '8f4d6b36-167e-4d69-a6e7-1f88423f451a', 'dashboardDetails', 'SELECT COUNT(db.dashboard_id) AS dashboardCount
FROM  jq_dashboard AS db', 3, 1), 
(105, '8f4d6b36-167e-4d69-a6e7-1f88423f451a', 'resourceBundleDetails', 'SELECT COUNT(DISTINCT(rb.resource_key)) AS resourceBundleCount 
FROM jq_resource_bundle AS rb', 4, 1), 
(106, '8f4d6b36-167e-4d69-a6e7-1f88423f451a', 'dashletDetails', 'SELECT COUNT(dl.dashlet_id) AS dashletCount
FROM jq_dashlet AS dl', 5, 1), 
(107, '8f4d6b36-167e-4d69-a6e7-1f88423f451a', 'notificationDetails', 'SELECT COUNT(gun.notification_id) AS notificationCount 
FROM jq_generic_user_notification AS gun', 6, 1);


REPLACE into jq_dashboard (dashboard_id, dashboard_name, context_id, dashboard_type, created_by, created_date, last_updated_date, is_deleted, is_draggable, is_exportable) VALUES
('ab7202bf-eadd-11ea-a036-e454e805e22f', 'Java Stater Usages', (SELECT context_id FROM jq_context_master where context_description='jws'), 2, 'admin', NOW(), NOW(), 0, 1, 0);

REPLACE INTO jq_dashlet_properties (property_id, dashlet_id, placeholder_name, display_name, type_id, value, default_value, configuration_script, is_deleted, to_display, sequence) VALUES
('8178b7ea-b874-4c31-8732-db76419a4f0f', '31c9ffa9-eadf-11ea-a036-e454e805e22f', 'startDate', 'Start Date', '368747b0-1e8b-11e8-8d69-000d3a173cc5', '', '30-11-2020', NULL, 0, 1, 0), 
('10126791-74e7-48de-8778-2f5c344e2cc5', '31c9ffa9-eadf-11ea-a036-e454e805e22f', 'endDate', 'End Date', '368747b0-1e8b-11e8-8d69-000d3a173cc5', '', '05-01-2021', NULL, 0, 1, 1);

REPLACE INTO jq_dashlet_properties (property_id, dashlet_id, placeholder_name, display_name, type_id, value, default_value, configuration_script, is_deleted, to_display, sequence) VALUES
('e7af6a9e-9b32-4c06-bcb5-d107723b3fcf', '3d97273b-eadf-11ea-a036-e454e805e22f', 'startDate', 'Start Date', '368747b0-1e8b-11e8-8d69-000d3a173cc5', '', '05-11-2018', NULL, 0, 1, 0),
('9d3d7bc9-8b06-4b08-95b2-7d0a786381ab', '3d97273b-eadf-11ea-a036-e454e805e22f', 'endDate', 'End Date', '368747b0-1e8b-11e8-8d69-000d3a173cc5', '', '05-10-2019', NULL, 0, 1, 1);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('9e36a1b9-ad7e-4f0f-b5c5-f2f925f9ef57', '91f4ce5c-4e85-11eb-9c1c-f48e38ab8cd7', 'decimal', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('bd4bc2cb-d843-4600-a304-7cc7a8528ec9', '91f4ce5c-4e85-11eb-9c1c-f48e38ab8cd7', 'decimal', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('c4d4dbf6-256f-4e1d-9bff-b8159620ac4c', '91f4ce5c-4e85-11eb-9c1c-f48e38ab8cd7', 'decimal', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('0476a8e6-8dfb-11eb-9688-f48e38ab8cd7', '3d97273b-eadf-11ea-a036-e454e805e22f', 'Dashboards', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('0c125048-8dfb-11eb-9688-f48e38ab8cd7', '3d97273b-eadf-11ea-a036-e454e805e22f', 'Dashboards', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('10046f32-8dfb-11eb-9688-f48e38ab8cd7', '3d97273b-eadf-11ea-a036-e454e805e22f', 'Dashboards', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('13b68c56-8dfb-11eb-9688-f48e38ab8cd7', '76ad58a3-afa3-4efd-a872-9a78a9e01a94', 'All Modules', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('176a6f5a-8dfb-11eb-9688-f48e38ab8cd7', '76ad58a3-afa3-4efd-a872-9a78a9e01a94', 'All Modules', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('1b6b26e7-8dfb-11eb-9688-f48e38ab8cd7', '76ad58a3-afa3-4efd-a872-9a78a9e01a94', 'All Modules', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('211a54b9-8dfb-11eb-9688-f48e38ab8cd7', '09b78b43-eade-11ea-a036-e454e805e22f', 'Grids', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('25b4a22f-8dfb-11eb-9688-f48e38ab8cd7', '09b78b43-eade-11ea-a036-e454e805e22f', 'Grids', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('29fb6b4a-8dfb-11eb-9688-f48e38ab8cd7', '09b78b43-eade-11ea-a036-e454e805e22f', 'Grids', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('2dc6374f-8dfb-11eb-9688-f48e38ab8cd7', '31c9ffa9-eadf-11ea-a036-e454e805e22f', 'Templates', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('320e8044-8dfb-11eb-9688-f48e38ab8cd7', '31c9ffa9-eadf-11ea-a036-e454e805e22f', 'Templates', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('3639eceb-8dfb-11eb-9688-f48e38ab8cd7', '31c9ffa9-eadf-11ea-a036-e454e805e22f', 'Templates', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('3a311ac2-8dfb-11eb-9688-f48e38ab8cd7', '44cb330d-eadf-11ea-a036-e454e805e22f', 'Dashlets', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('3e71557b-8dfb-11eb-9688-f48e38ab8cd7', '44cb330d-eadf-11ea-a036-e454e805e22f', 'Dashlets', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('420bb52e-8dfb-11eb-9688-f48e38ab8cd7', '44cb330d-eadf-11ea-a036-e454e805e22f', 'Dashlets', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('469c22ff-8dfb-11eb-9688-f48e38ab8cd7', '0eb8adc4-eade-11ea-a036-e454e805e22f', 'Notification', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('4d86c897-8dfb-11eb-9688-f48e38ab8cd7', '0eb8adc4-eade-11ea-a036-e454e805e22f', 'Notification', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('52efd1f0-8dfb-11eb-9688-f48e38ab8cd7', '0eb8adc4-eade-11ea-a036-e454e805e22f', 'Notification', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('5747c1af-8dfb-11eb-9688-f48e38ab8cd7', '37dbbc8d-eadf-11ea-a036-e454e805e22f', 'DB resource bundles', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('5b4438cd-8dfb-11eb-9688-f48e38ab8cd7', '37dbbc8d-eadf-11ea-a036-e454e805e22f', 'DB resource bundles', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0), 
('5f4414d1-8dfb-11eb-9688-f48e38ab8cd7', '37dbbc8d-eadf-11ea-a036-e454e805e22f', 'DB resource bundles', '19aa8996-80a2-11eb-971b-f48e38ab8cd7', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e9caf125-648e-42f8-a05d-c4bb21f100f8', 1, 0);


SET FOREIGN_KEY_CHECKS=1;


