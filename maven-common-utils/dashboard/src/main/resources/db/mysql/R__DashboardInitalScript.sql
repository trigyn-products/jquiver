Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'dashlet-common-div', '
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
</div>','aar.dev@trigyn.com','aar.dev@trigyn.com',now());
  
Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'dashlets', '
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

<div id="configurationDialog"></div></div>','aar.dev@trigyn.com','aar.dev@trigyn.com',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'dashlet-configuration', '
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
','aar.dev@trigyn.com','aar.dev@trigyn.com',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'select', '<script type="text/javascript">
</script>
<div class="col-lg-9 col-md-9 col-sm-8">
<select id="" class="select form-control" name=""></select>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'text', '<script type="text/javascript"></script>
<div class="col-lg-9 col-md-9 col-sm-8">
<input type="text" id="" name="" value="" class="form-control"/>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'rangeslider', '<div class="col-lg-9 col-md-9 col-sm-8">
<div class="range-slider">
<input type="range" id="" class="range-slider__range" name="${property.propertyId}" value="" min="0" max="${property.value}" step="1"/>
<span id="slider-value_" class="range-slider__value"></span>
</div>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'number', '<script type="text/javascript"></script><div class="col-lg-9 col-md-9 col-sm-8">
<input type="number" min="0" id="" name="" value="" class="form-control"/>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'datepicker', '<script src="${contextPath}/resources/jscore/jquery/jquery-ui-1.12.1.custom/jquery-ui.js"></script>
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
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'checkbox', '<div class="col-lg-9 col-md-9 col-sm-8">
 <div class="control--checkbox">
  <input type="checkbox" class="roles" name="${property.propertyId}" value="true" />
  <label for="checkbox1"><span></span>True</label>
  <input type="checkbox" class="roles" name="${property.propertyId}" value="false"/>
  <label for="checkbox1"><span></span>False</label>
 </div>
</div>', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now());

Replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'monthpicker', '', 'aar.dev@trigyn.com','aar.dev@trigyn.com',now());
