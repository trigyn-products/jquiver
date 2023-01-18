
SET FOREIGN_KEY_CHECKS=0;

CREATE  OR REPLACE VIEW `jq_scheduler_view` AS
SELECT
  `jjs`.`scheduler_id`          AS `scheduler_id`,
  `jjs`.`scheduler_name`        AS `scheduler_name`,
  `jjs`.`jws_dynamic_rest_id`   AS `jws_dynamic_rest_id`,
  `jjs`.`cron_scheduler`        AS `cron_scheduler`,
  `jjs`.`is_active`             AS `is_active`,
  jjs.scheduler_type_id 		AS schedulerTypeId,
  (SELECT
     MAX(`jjsl`.`response_time`)
   FROM `jq_job_scheduler_log` `jjsl`
   WHERE `jjsl`.`scheduler_id` = `jjs`.`scheduler_id`) AS `last_response_time`,
  (SELECT
     COUNT(`jjsl`.`response_time`)
   FROM `jq_job_scheduler_log` `jjsl`
   WHERE `jjsl`.`scheduler_id` = `jjs`.`scheduler_id`) AS `reminder_count`,
  `jdrd`.`jws_dynamic_rest_url` AS `jws_dynamic_rest_url`
FROM (`jq_job_scheduler` `jjs` LEFT OUTER JOIN
   `jq_dynamic_rest_details` `jdrd` ON `jjs`.`jws_dynamic_rest_id` = `jdrd`.`jws_dynamic_rest_id`);


REPLACE INTO jq_grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id, created_by, created_date, last_updated_by,last_updated_ts) VALUES
('jq-schedulerGrid', 'jq-schedulerGrid', 'jq-scheduler Listing', 'jq_scheduler_view', '*', 1, 2, 'admin', NOW(), 'admin',NOW());

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('c49cd97b-4aec-45d2-b72f-302a995ac2b6', 'jq-schedulerGrid', 'jq-schedulerGrid', '07067149-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('c49cd97b-4aec-45d2-b72f-302a995ac2b7', 'jq-schedulerGrid', 'jq-schedulerGrid', '07067149-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('c49cd97b-4aec-45d2-b72f-302a995ac2b8', 'jq-schedulerGrid', 'jq-schedulerGrid', '07067149-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);

REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id, created_by, created_date, last_updated_ts) 
VALUES('jq-restapi-autocomplete', 'Get All rest api', 
'SELECT jdrd.jws_dynamic_rest_id AS jws_dynamic_rest_id,jdrd.jws_dynamic_rest_url AS jws_dynamic_rest_url,
jrtd.jws_request_type AS jws_request_type FROM jq_dynamic_rest_details jdrd, jq_request_type_details jrtd 
WHERE jdrd.jws_request_type_id = jrtd.jws_request_type_details_id 
AND CASE WHEN (SELECT (property_value) FROM jq_property_master WHERE property_name = "version") NOT LIKE "%SNAPSHOT%"  
THEN jdrd.jws_dynamic_rest_type_id = 1 ELSE 1 END 
AND jws_dynamic_rest_url LIKE CONCAT("%", :searchText, "%") LIMIT :startIndex, :pageSize'
, 2, 'admin', NOW(), NOW());


REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('17daa349-c85b-4fd3-ab33-7228fc6bf53f', 'jq-restapi-autocomplete', 'jq-restapi-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0), 
('17daa349-c85b-4fd3-ab33-7228fc6bf54f', 'jq-restapi-autocomplete', 'jq-restapi-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0), 
('17daa349-c85b-4fd3-ab33-7228fc6bf55f', 'jq-restapi-autocomplete', 'jq-restapi-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1, 0);


replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('3e9fdbc2-f17d-43e2-9a67-ed39583f96fe', 'jq-scheduler-listing', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script> 
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" /> 
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/lang/en.js"></script> 
</head>

<div class="container">
    <div class="topband">
        <h2 class="title-cls-name float-left">Scheduler</h2> 
        <div class="float-right">
        	Show:<select id="typeSelect" class="typeSelectDropDown" onchange="changeType()">   
                <option value="0">All</option>                   
                <option value="1" selected>Custom</option>                   
                <option value="2">System</option>                 
            </select>
             <button type="submit" class="btn btn-primary" onclick="openAddEditScreen()"> Create New </button>
            <span onclick="backToWelcomePage();">
                <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
            </span> 
        </div>
        
        <div class="clearfix"></div>        
    </div>
        
    <div id="jq-schedulerGrid"></div>
    <div id="log-history-popup" title="Scheduler History" style="display:none;">
        <div id="divSchedulerLogHistory"></div>
    </div> 
	<div id="deleteHeader"></div>
    <div id="snackbar"></div>
</div>

<script>
    contextPath = "${(contextPath)!''''}";
    let primaryKeyDetails = {"schedulerid":""};
    $(function () {
    //Add all columns that needs to be displayed in the grid
        let colM = [
            	{ title: "Scheduler Name", hidden : false, width: 130, dataIndx: "scheduler_name", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            	{ title: "Target API", hidden : false, width: 130, dataIndx: "jws_dynamic_rest_url", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            	{ title: "Cron Expression", hidden : false, width: 130, dataIndx: "cron_scheduler", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
                { title: "Last Execcution Time", hidden : false, width: 130, dataIndx: "last_response_time", align: "left", halign: "center", render: lastExecutionTimeRenderer  },
            	{ title: "Status", hidden : false, width: 90, dataIndx: "is_active", align: "left", halign: "center", render: statusRenderer  },
            	{ title: "<@resourceBundle ''jws.action'' />", width: 100, maxWidth: 145, dataIndx: "action", align: "center", align: "left", render: manageRecord, sortable: false}
        ];
    
    	let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
    	};
    //System will fecth grid data based on gridId
        let grid = $("#jq-schedulerGrid").grid({
          gridId: "jq-schedulerGrid",
          colModel: colM,
          dataModel: dataModel,
          additionalParameters: {"cr_schedulerTypeId":"str_1"}
        });
    
        $("#log-history-popup").dialog({
            bgiframe: true,
            autoOpen: false,
            height: 100,
            modal: true,
            height: $(window).height() / 100 * 80,
            width: $(window).width() / 100 * 50,
        });

    });
    
    function changeType() {
        var type = $("#typeSelect").val();   
        let postData;
        if(type == 0) {
            postData = {gridId:"jq-schedulerGrid"}
        } else {
            let typeCondition = "str_"+type;       
   
            postData = {gridId:"jq-schedulerGrid"
                    ,"cr_schedulerTypeId":typeCondition
                    }
        }
        
        let gridNew = $( "#jq-schedulerGrid" ).pqGrid();
        gridNew.pqGrid( "option", "dataModel.postData", postData);
        gridNew.pqGrid( "refreshDataAndView" );  
    }
        
    function statusRenderer(uiObject){
        let statusElement =  "Active";
        if(uiObject.rowData.is_active == 0){
            statusElement = "Inactive";
        }
        return statusElement;
    }
    
    function lastExecutionTimeRenderer(uiObject){
    	if(uiObject.rowData.last_response_time != null){
        	return formatDate(uiObject.rowData.last_response_time);
        } else {
            return "";
        }
    }

    //Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
    function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
        let schedulerID = uiObject.rowData.scheduler_id;
        action = ''<span id="''+rowIndx+''" onclick=\\''createNew(\"''+ schedulerID +''\")\\'' class= "grid_action_icons" title="<@resourceBundle''jws.edit''/>"><i class="fa fa-pencil"></i></span>''.toString();
        
        if(uiObject.rowData.schedulerTypeId == 1) {
		        <#if loggedInUserRoleList?? && loggedInUserRoleList?size gt 0>
		        	<#list loggedInUserRoleList as loggedInUserRole>
		            	<#if (loggedInUserRole == "ADMIN")>    
		        			action += ''<span id="''+rowIndx+''" onclick=\\''openDeletConfirmation(\"''+schedulerID+''\")\\'' class= "grid_action_icons" title="Delete"><i class="fa fa-trash "></i></span>''.toString();
        					<#break>
		        		</#if>
		        	</#list>
		        </#if>
		        }
        
        if(uiObject.rowData.reminder_count > 0){
            action += ''<span onclick=\\''loadExecutionHistory(0,\"''+schedulerID+''\")\\'' class= "grid_action_icons"><i class="fa fa-history"></i></span>'';
        }
        action += ''<span id="''+rowIndx+''" onclick=\\''openExecuteNowConfirmation(\"''+schedulerID+''\")\\'' class= "grid_action_icons" title="Execute Now"><i class="fa fa-play "></i></span>''.toString();
        
        return action;
    }
    
    //Add logic to navigate to create new record
    function createNew(schedulerID) {
        primaryKeyDetails["schedulerid"] = schedulerID ;
        openAddEditScreen();
    }
    
    function openExecuteNowConfirmation(schedulerID){
		$("#deleteHeader").html("Are you sure you want to execute now?");
		$("#deleteHeader").dialog({
		bgiframe	: true,
		autoOpen	: true, 
		modal		: true,
		closeOnEscape : true,
		draggable	: true,
		resizable	: false,
		title		: "Execute Now",
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
				text		: "Execute Now",
				click	: function(){
					$(this).dialog(''close'');
					executeNow(schedulerID);
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
    
    function executeNow(schedulerID) {
    	$.ajax({
            url: "${contextPath!''''}/cf/execn",
            type: "POST",
            data:{
            "schedulerID": schedulerID},
            async:true,
            success: function(data){
               $( "#jq-schedulerGrid" ).pqGrid().pqGrid( "refreshDataAndView" ); 
               showMessage("Scheduler is executed", "info");
            },
            error: function(jqXHR, exception){
                showMessage("Error occurred while deleting the scheduler", "error");
            }   
        });
    }
    
    function openDeletConfirmation(schedulerID){
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
					deleteScheduler(schedulerID);
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
    
    function deleteScheduler(schedulerID) {
    	$.ajax({
            url: "${contextPath!''''}/cf/delS",
            type: "POST",
            data:{
            "schedulerID": schedulerID},
            async:true,
            success: function(data){
               $( "#jq-schedulerGrid" ).pqGrid().pqGrid( "refreshDataAndView" ); 
               showMessage("Scheduler is deleted", "info");
            },
            error: function(jqXHR, exception){
                showMessage("Error occurred while deleting the scheduler", "error");
            }   
        });
    }

    function openAddEditScreen() {
    	  let formId = "f44ac7ab-c61e-4df3-b40f-190262f79a39";
    	  openForm(formId, primaryKeyDetails);
    }

    function loadExecutionHistory(a_offset, schedulerID){
    	if(a_offset == 0) {
            $("#divSchedulerLogHistory").html("");
        }
        $("#divSchedulerLogHistory").find("button").remove()
        $.ajax({
            url: "${contextPath!''''}/api/getExecutionHistory",
            type: "POST",
            data:{"offset": a_offset,
            "schedulerID": schedulerID},
            async:true,
            success: function(data){
                $("#divSchedulerLogHistory").append(data);
                $("#log-history-popup").dialog( "open" );
            },
            error: function(jqXHR, exception){
                showMessage("Error occurred while fetching text responses", "error");
            }   
        });
    }
    
    //Code go back to previous page
    function backToWelcomePage() {
        location.href = contextPath+"/cf/dynl";
    }
</script>', 'admin', 'admin', NOW(), NULL, 2);

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('ad7f398b-59fc-49b7-aacd-01d6a3af24f3', '3e9fdbc2-f17d-43e2-9a67-ed39583f96fe', 'jq-scheduler-listing', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('ad7f398b-59fc-49b7-aacd-01d6a3af24f4', '3e9fdbc2-f17d-43e2-9a67-ed39583f96fe', 'jq-scheduler-listing', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('ad7f398b-59fc-49b7-aacd-01d6a3af24f5', '3e9fdbc2-f17d-43e2-9a67-ed39583f96fe', 'jq-scheduler-listing', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);



REPLACE INTO jq_dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id, last_updated_ts) VALUES
('f44ac7ab-c61e-4df3-b40f-190262f79a39', 'jq-scheduler-form', 'jq-scheduler Form', 
'SELECT js.scheduler_id,js.scheduler_name, js.header_json, js.request_param_json, js.failed_notification_params, jdrd.jws_dynamic_rest_id AS jws_dynamic_rest_id,jdrd.jws_dynamic_rest_url AS rest_api_url,jrtd.jws_request_type AS jws_request_type,cron_scheduler,js.is_active FROM jq_job_scheduler js, jq_dynamic_rest_details jdrd, jq_request_type_details jrtd WHERE jdrd.jws_dynamic_rest_id=js.jws_dynamic_rest_id AND jdrd.jws_request_type_id = jrtd.jws_request_type_details_id AND js.scheduler_id = ''${schedulerid}''', 
'<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/typeahead/typeahead.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
        
</head>

<div class="container">
               <div class="topband">
                              <h2 class="title-cls-name float-left">Add Edit Details</h2> 
                              <div class="clearfix"></div>                         
               </div>
  <form method="post" name="addEditForm" id="addEditForm">
    <div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
    
               <div class="row">
                                                            <input type="hidden" data-type="text" id="schedulerid" name="schedulerid"  value="${(resultSetObject.scheduler_id)!""}" >
                                             <div class="col-3">
                                                                           <div class="col-inner-form full-form-fields">
                                                                                                      <label for="schedulername" style="white-space:nowrap"><span class="asteriskmark">*</span>
                                                                                                         Scheduler Name
                                                                                                      </label>
                                                                                                                        <input type="text" data-type="text" id="schedulername" name="schedulername"  value="${(resultSetObject.scheduler_name)!""}" maxlength="50" placeholder="Scheduler Name" class="form-control">
                                                                           </div>
                                                            </div>
                                             
                <div class="col-3">
                    <input type="hidden" id="restapiurl" name="restapiurl"  value="${(resultSetObject.jws_dynamic_rest_id)!""}">
                  <div class="col-inner-form full-form-fields">
                        <label for="algoAutocomplete" style="white-space:nowrap"><span class="asteriskmark">*</span>
                            Target API</label>
                        <div class="search-cover">
                            <input class="form-control" id="algoAutocomplete" type="text">
                            <i class="fa fa-search" aria-hidden="true"></i>
                        </div>
                    </div>
                </div>
                                             <div class="col-3">
                                                                           <div class="col-inner-form full-form-fields">
                                                                                                      <label for="cronscheduler" style="white-space:nowrap"><span class="asteriskmark">*</span>
                                                                                                         Cron Scheduler Expression
                                                                                                      </label>
                                                                                                                        <input type="text" data-type="text" id="cronscheduler" name="cronscheduler"  value="${(resultSetObject.cron_scheduler)!""}" maxlength="50" placeholder="Cron Scheduler" class="form-control">
                                                                           </div>
                                                            </div>
                <div class="col-3">
                    <div class="col-inner-form full-form-fields">
                        <label for="isActiveCheckbox"><span class="asteriskmark">*</span>Status</label>
                        <div class="onoffswitch">
                            <input type="hidden" id="isactive" name="isactive" value="${resultSetObject?api.get(''is_active'')!""}"/>
                            <input type="checkbox" name="isActiveCheckbox" class="onoffswitch-checkbox" id="isActiveCheckbox" value="0">
                            <label class="onoffswitch-label" for="isActiveCheckbox">
                                <span class="onoffswitch-inner"></span>
                                <span class="onoffswitch-switch"></span>
                            </label>
                        </div>
                    </div>
                </div> 
                
                   <div class="col-12">
                       <div class="col-inner-form full-form-fields boxblock">
                            <h3 class="smalltitle smalltitlebg">Failed Notification</h3>
                                <div class="failnotificationblock">
                                <label><input onchange="updateReceipents()" id="chkResponseCode" type="checkbox" /> If response code is not 200 </label>
                                <select onchange="updateReceipents()" id="lstConjunction">
                                    <option>N/A</option>
                                    <option>And</option>
                                    <option>Or</option>
                                </select>
                                </div>
                                <div class="row"> 
                                 <div class="col-3">
                                    <div class="col-inner-form full-form-fields">
                                        <label>Content</label>
                                        <select id="lstConditions" class="form-control" disabled>
                                                <option>Equal</option>
                                                <option>Not-Equal</option>
                                            </select>
                                    </div>
                                  </div> 
                                 <div class="col-3">
                                    <div class="col-inner-form full-form-fields">
                                        <label>Regex</label>
                                          <input  class="form-control" type="text" id="txtRegex" disabled/>                                        
                                    </div>                              
                                 </div>
                                 </div>
                                  <div class="row" > 
                                    <div class="col-6">
                                        <div class="col-inner-form full-form-fields">
                                            <label>Send Mail To</label>
                                            <input  class="form-control" type="text" id="txtRecipients" disabled/>                                        
                                        </div>                                  
                                    </div> 
                                 </div>                               
                             
                        </div>
                    </div>
                </div>
  </form>
      
        <div class="row spacebottom">
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
                            <td class="bgtd centercls"  width="10%"><button class="plusticon" onclick="addRow()"><i class="fa fa-plus-circle" aria-hidden="true"></i></button></td>
                        </tr>                     
                    </table>
            </div>
            </div>
            <div class="col-6">
            <div class="col-inner-form full-form-fields">
                    <table id="requestParamTable"  class="customtblecls">
                         <tr>
                            <th colspan="3">   

                                <div class="displyflx">                             
                                 <span class="titleclsnm">Request Paramters </span>                                    

                                </div>
                            </th>
                        </tr>      
                        <tr>
                            <td class="bgtd">Name</td>
                            <td class="bgtd">Value </td>
                            <td class="bgtd centercls"  width="10%"><button class="plusticon" onclick="addRowForRequestParam()"><i class="fa fa-plus-circle" aria-hidden="true"></i></button></td>
                        </tr>                    
                    </table>
                    </div>
            </div>
        </div>
               <div id="deleteHeader"></div>
               <div class="row">
            <div class="col-12">
                
                
                <div class="cornexpcopyblock">
                    Resulting Cron Expression: 
                <div class="cornexpcopy">
                <span class="cronResult">0 0 0 0 0 0 0</span>
                <button onclick="copyToClipboard()" class="copybtn">
                    <i class="fa fa-clipboard" aria-hidden="true"></i>
                    Copy
                 </button>
                </div>
                </div>

                <div class="builderblock">
                <p class="buildertitle" onclick="expandCollapse(this)">Cron Builder<span style="float:right">&#9660;</span></p>
                <div id="crontabs" class="crontabscls ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: none">
                    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" role="tablist">
                        <li class="ui-state-default ui-corner-top ui-tabs-active ui-state-active" role="tab" tabindex="0" aria-controls="tabs-1" aria-labelledby="ui-id-1" aria-selected="true" aria-expanded="true"><a href="#tabs-1" class="ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-1">Seconds</a></li>
                        <li class="ui-state-default ui-corner-top" role="tab" tabindex="-1" aria-controls="tabs-2" aria-labelledby="ui-id-2" aria-selected="false" aria-expanded="false"><a href="#tabs-2" class="ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-2">Minutes</a></li>
                        <li class="ui-state-default ui-corner-top" role="tab" tabindex="-1" aria-controls="tabs-3" aria-labelledby="ui-id-3" aria-selected="false" aria-expanded="false"><a href="#tabs-3" class="ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-3">Hours</a></li>
                        <li class="ui-state-default ui-corner-top" role="tab" tabindex="-1" aria-controls="tabs-4" aria-labelledby="ui-id-4" aria-selected="false" aria-expanded="false"><a href="#tabs-4" class="ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-4">Day</a></li>
                        <li class="ui-state-default ui-corner-top" role="tab" tabindex="-1" aria-controls="tabs-5" aria-labelledby="ui-id-5" aria-selected="false" aria-expanded="false"><a href="#tabs-5" class="ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-5">Month</a></li>
                        <li class="ui-state-default ui-corner-top" role="tab" tabindex="-1" aria-controls="tabs-6" aria-labelledby="ui-id-6" aria-selected="false" aria-expanded="false"><a href="#tabs-6" class="ui-tabs-anchor" role="presentation" tabindex="-1" id="ui-id-6">Year</a></li>
                    </ul>
                    <div id="tabs-1" aria-labelledby="ui-id-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom" role="tabpanel" aria-hidden="false" style="display: block;">
                        <div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronEverySecond" name="cronSecond">
                            <label for="cronEverySecond" class="nofloat">Every second</label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronSecondIncrement" name="cronSecond">
                            <label for="cronSecondIncrement" class="nofloat">
                                Every
                                <select id="cronSecondIncrementIncrement" style="width:50px;">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                    <option value="60">60</option>
                                </select>
                                second(s) starting at second 
                                <select id="cronSecondIncrementStart" style="width:50px;">
                                   <option value="0">00</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                </select>
                            </label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronSecondSpecific" checked="checked" name="cronSecond">
                            <label for="cronSecondSpecific" class="nofloat">Specific second (choose one or many)</label>
                             <div class="chekboxblock">
                                <div class="flexblock">
                                                                                                                        
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond0" name="cronSecondSpecificSpecific" value="0" checked="checked">
                                                                                                                                       <label for="cronSecond0" class="nofloat">00</label>
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond1" name="cronSecondSpecificSpecific" value="1">
                                    <label for="cronSecond1" class="nofloat">01</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond2" name="cronSecondSpecificSpecific" value="2">
                                    <label for="cronSecond2" class="nofloat">02</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond3" name="cronSecondSpecificSpecific" value="3">
                                    <label for="cronSecond3" class="nofloat">03</label>                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond4" name="cronSecondSpecificSpecific" value="4">
                                    <label for="cronSecond4" class="nofloat">04</label>                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">                                 
                                                                                                                                       <input type="checkbox" id="cronSecond5" name="cronSecondSpecificSpecific" value="5">
                                    <label for="cronSecond5" class="nofloat">05</label>                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond6" name="cronSecondSpecificSpecific" value="6">
                                    <label for="cronSecond6" class="nofloat">06</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond7" name="cronSecondSpecificSpecific" value="7">
                                    <label for="cronSecond7" class="nofloat">07</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond8" name="cronSecondSpecificSpecific" value="8">
                                    <label for="cronSecond8" class="nofloat">08</label>
                                                                                                                                                                     
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond9" name="cronSecondSpecificSpecific" value="9"> 
                                    <label for="cronSecond9" class="nofloat">09</label>
                                   
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond10" name="cronSecondSpecificSpecific" value="10">
                                    <label for="cronSecond10" class="nofloat">10</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond11" name="cronSecondSpecificSpecific" value="11">
                                    <label for="cronSecond11" class="nofloat">11</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond12" name="cronSecondSpecificSpecific" value="12">
                                    <label for="cronSecond12" class="nofloat">12</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond13" name="cronSecondSpecificSpecific" value="13">
                                    <label for="cronSecond13" class="nofloat">13</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond14" name="cronSecondSpecificSpecific" value="14">
                                    <label for="cronSecond14" class="nofloat">14</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond15" name="cronSecondSpecificSpecific" value="15">
                                    <label for="cronSecond15" class="nofloat">15</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond16" name="cronSecondSpecificSpecific" value="16">
                                    <label for="cronSecond16" class="nofloat">16</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond17" name="cronSecondSpecificSpecific" value="17">
                                    <label for="cronSecond17" class="nofloat">17</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond18" name="cronSecondSpecificSpecific" value="18">
                                    <label for="cronSecond18" class="nofloat">18</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond19" name="cronSecondSpecificSpecific" value="19">
                                    <label for="cronSecond19" class="nofloat">19</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond20" name="cronSecondSpecificSpecific" value="20">                                                                                                                                
                                    <label for="cronSecond20" class="nofloat">20</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond21" name="cronSecondSpecificSpecific" value="21">
                                    <label for="cronSecond21" class="nofloat">21</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond22" name="cronSecondSpecificSpecific" value="22">
                                    <label for="cronSecond22" class="nofloat">22</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond23" name="cronSecondSpecificSpecific" value="23">
                                    <label for="cronSecond23" class="nofloat">23</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond24" name="cronSecondSpecificSpecific" value="24">        
                                    <label for="cronSecond24" class="nofloat">24</label>
                                   
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond25" name="cronSecondSpecificSpecific" value="25">                                                                                                                                
                                    <label for="cronSecond25" class="nofloat">25</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond26" name="cronSecondSpecificSpecific" value="26">
                                    <label for="cronSecond26" class="nofloat">26</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond27" name="cronSecondSpecificSpecific" value="27">
                                    <label for="cronSecond27" class="nofloat">27</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond28" name="cronSecondSpecificSpecific" value="28">
                                    <label for="cronSecond28" class="nofloat">28</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond29" name="cronSecondSpecificSpecific" value="29">
                                    <label for="cronSecond29" class="nofloat">29</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond30" name="cronSecondSpecificSpecific" value="30">                                                                                                                                
                                    <label for="cronSecond30" class="nofloat">30</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond31" name="cronSecondSpecificSpecific" value="31">
                                    <label for="cronSecond31" class="nofloat">31</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond32" name="cronSecondSpecificSpecific" value="32">
                                    <label for="cronSecond32" class="nofloat">32</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond33" name="cronSecondSpecificSpecific" value="33">
                                    <label for="cronSecond33" class="nofloat">33</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond34" name="cronSecondSpecificSpecific" value="34">
                                    <label for="cronSecond34" class="nofloat">34</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">   
                                                                                                                                       <input type="checkbox" id="cronSecond35" name="cronSecondSpecificSpecific" value="35">
                                    <label for="cronSecond35" class="nofloat">35</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond36" name="cronSecondSpecificSpecific" value="36">
                                    <label for="cronSecond36" class="nofloat">36</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond37" name="cronSecondSpecificSpecific" value="37">
                                    <label for="cronSecond37" class="nofloat">37</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond38" name="cronSecondSpecificSpecific" value="38">
                                    <label for="cronSecond38" class="nofloat">38</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond39" name="cronSecondSpecificSpecific" value="39"> 
                                    <label for="cronSecond39" class="nofloat">39</label>
                                    
                                                                                                                                       </div>


                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond40" name="cronSecondSpecificSpecific" value="40">                                                                                                                                
                                    <label for="cronSecond40" class="nofloat">40</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond41" name="cronSecondSpecificSpecific" value="41">
                                    <label for="cronSecond41" class="nofloat">41</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond42" name="cronSecondSpecificSpecific" value="42">
                                    <label for="cronSecond42" class="nofloat">42</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond43" name="cronSecondSpecificSpecific" value="43">
                                    <label for="cronSecond43" class="nofloat">43</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond44" name="cronSecondSpecificSpecific" value="44">
                                    <label for="cronSecond44" class="nofloat">44</label>                                    
                                                                                                                                       </div>


                                                                                                                                       <div class="cronboxcheck">   
                                                                                                                                       <input type="checkbox" id="cronSecond45" name="cronSecondSpecificSpecific" value="45">                                                                                                                                
                                    <label for="cronSecond45" class="nofloat">45</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">                                    
                                    <input type="checkbox" id="cronSecond46" name="cronSecondSpecificSpecific" value="46">
                                   <label for="cronSecond46" class="nofloat">46</label>
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond47" name="cronSecondSpecificSpecific" value="47">
                                    <label for="cronSecond47" class="nofloat">47</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond48" name="cronSecondSpecificSpecific" value="48">
                                    <label for="cronSecond48" class="nofloat">48</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond49" name="cronSecondSpecificSpecific" value="49">
                                    <label for="cronSecond49" class="nofloat">49</label>                                    
                                                                                                                                       </div>


                                                                                                                                       <div class="cronboxcheck">                  
                                                                                                                                       <input type="checkbox" id="cronSecond50" name="cronSecondSpecificSpecific" value="50">                                                                                                                                
                                    <label for="cronSecond50" class="nofloat">50</label>                                  
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond51" name="cronSecondSpecificSpecific" value="51">
                                    <label for="cronSecond51" class="nofloat">51</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond52" name="cronSecondSpecificSpecific" value="52">
                                    <label for="cronSecond52" class="nofloat">52</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond53" name="cronSecondSpecificSpecific" value="53">
                                    <label for="cronSecond53" class="nofloat">53</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond54" name="cronSecondSpecificSpecific" value="54">
                                    <label for="cronSecond54" class="nofloat">54</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">   
                                                                                                                                       <input type="checkbox" id="cronSecond55" name="cronSecondSpecificSpecific" value="55">                                                                                                                                
                                    <label for="cronSecond55" class="nofloat">55</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond56" name="cronSecondSpecificSpecific" value="56">
                                    <label for="cronSecond56" class="nofloat">56</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond57" name="cronSecondSpecificSpecific" value="57">
                                    <label for="cronSecond57" class="nofloat">57</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                         <input type="checkbox" id="cronSecond58" name="cronSecondSpecificSpecific" value="58">
                                    <label for="cronSecond58" class="nofloat">58</label>                                  
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronSecond59" name="cronSecondSpecificSpecific" value="59">
                                    <label for="cronSecond59" class="nofloat">59</label>                                   
                                                                                                                                       </div>
                                                                                                                                       
                                </div>
                            </div>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronSecondRange" name="cronSecond">
                            <label for="cronSecondRange" class="nofloat">
                                Every second between second 
                                <select id="cronSecondRangeStart" style="width:50px;">
                                    <option value="0">00</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                </select>
                                and second 
                                <select id="cronSecondRangeEnd" style="width:50px;">
                                    <option value="0">00</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                   <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                </select>
                            </label>
                            </div>
                        </div>
                    </div>
                    <div id="tabs-2" aria-labelledby="ui-id-2" class="ui-tabs-panel ui-widget-content ui-corner-bottom" role="tabpanel" style="display: none;" aria-hidden="true">
                        <div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronEveryMinute" name="cronMinute">
                            <label for="cronEveryMinute" class="nofloat">Every minute</label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronMinuteIncrement" name="cronMinute">
                            <label for="cronMinuteIncrement" class="nofloat">
                                Every
                                <select id="cronMinuteIncrementIncrement" style="width:50px;">
                                   <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                    <option value="60">60</option>
                                </select>
                                minute(s) starting at minute 
                                <select id="cronMinuteIncrementStart" style="width:50px;">
                                    <option value="0">00</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                </select>
                            </label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronMinuteSpecific" checked="checked" name="cronMinute">
                            <label for="cronMinuteSpecific" class="nofloat">Specific minute (choose one or many)</label>
                            
                            <div class="chekboxblock">
                                <div class="flexblock">
                                                                                                                        
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute0" name="cronMinuteSpecificSpecific" value="0" checked="checked">
                                    <label for="cronMinute0" class="nofloat">00</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                         <input type="checkbox" id="cronMinute1" name="cronMinuteSpecificSpecific" value="1">
                                    <label for="cronMinute1" class="nofloat">01</label>
                                  
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute2" name="cronMinuteSpecificSpecific" value="2">
                                    <label for="cronMinute2" class="nofloat">02</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute3" name="cronMinuteSpecificSpecific" value="3">
                                    <label for="cronMinute3" class="nofloat">03</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute4" name="cronMinuteSpecificSpecific" value="4">
                                    <label for="cronMinute4" class="nofloat">04</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">                                                                                                                           
                                                                                                                                       <input type="checkbox" id="cronMinute5" name="cronMinuteSpecificSpecific" value="5">
                                    <label for="cronMinute5" class="nofloat">05</label>
                                                                                                                                      </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute6" name="cronMinuteSpecificSpecific" value="6">
                                    <label for="cronMinute6" class="nofloat">06</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute7" name="cronMinuteSpecificSpecific" value="7">
                                    <label for="cronMinute7" class="nofloat">07</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute8" name="cronMinuteSpecificSpecific" value="8">
                                    <label for="cronMinute8" class="nofloat">08</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute9" name="cronMinuteSpecificSpecific" value="9">
                                    <label for="cronMinute9" class="nofloat">09</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute10" name="cronMinuteSpecificSpecific" value="10">
                                    <label for="cronMinute10" class="nofloat">10</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute11" name="cronMinuteSpecificSpecific" value="11">
                                    <label for="cronMinute11" class="nofloat">11</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute12" name="cronMinuteSpecificSpecific" value="12">
                                    <label for="cronMinute12" class="nofloat">12</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute13" name="cronMinuteSpecificSpecific" value="13">
                                    <label for="cronMinute13" class="nofloat">13</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute14" name="cronMinuteSpecificSpecific" value="14">
                                    <label for="cronMinute14" class="nofloat">14</label>
                                    
                                                                                                                                       </div>

<div class="cronboxcheck">                                                                     
                                                                                                                                       <input type="checkbox" id="cronMinute15" name="cronMinuteSpecificSpecific" value="15">
                                    <label for="cronMinute15" class="nofloat">15</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute16" name="cronMinuteSpecificSpecific" value="16">
                                    <label for="cronMinute16" class="nofloat">16</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute17" name="cronMinuteSpecificSpecific" value="17">
                                    <label for="cronMinute17" class="nofloat">17</label>
                                    
                                                                                                                                       </div>
                                                                                                                                      
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute18" name="cronMinuteSpecificSpecific" value="18">
                                    <label for="cronMinute18" class="nofloat">18</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute19" name="cronMinuteSpecificSpecific" value="19">
                                    <label for="cronMinute19" class="nofloat">19</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                         <input type="checkbox" id="cronMinute20" name="cronMinuteSpecificSpecific" value="20">
                                    <label for="cronMinute20" class="nofloat">20</label>                                  
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute21" name="cronMinuteSpecificSpecific" value="21">
                                    <label for="cronMinute21" class="nofloat">21</label>
                                  
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute22" name="cronMinuteSpecificSpecific" value="22">
                                    <label for="cronMinute22" class="nofloat">22</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute23" name="cronMinuteSpecificSpecific" value="23">
                                    <label for="cronMinute23" class="nofloat">23</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute24" name="cronMinuteSpecificSpecific" value="24">
                                    <label for="cronMinute24" class="nofloat">24</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">                                 
                                                                                                                                       <input type="checkbox" id="cronMinute25" name="cronMinuteSpecificSpecific" value="25">
                                    <label for="cronMinute25" class="nofloat">25</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute26" name="cronMinuteSpecificSpecific" value="26">
                                    <label for="cronMinute26" class="nofloat">26</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute27" name="cronMinuteSpecificSpecific" value="27">
                                    <label for="cronMinute27" class="nofloat">27</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                      <input type="checkbox" id="cronMinute28" name="cronMinuteSpecificSpecific" value="28">
                                    <label for="cronMinute28" class="nofloat">28</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute29" name="cronMinuteSpecificSpecific" value="29">
                                    <label for="cronMinute29" class="nofloat">29</label>                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck"> 
                                                                                                                                       <input type="checkbox" id="cronMinute30" name="cronMinuteSpecificSpecific" value="30">                                                                                                                                 
                                    <label for="cronMinute30" class="nofloat">30</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute31" name="cronMinuteSpecificSpecific" value="31">
                                    <label for="cronMinute31" class="nofloat">31</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute32" name="cronMinuteSpecificSpecific" value="32">
                                    <label for="cronMinute32" class="nofloat">32</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute33" name="cronMinuteSpecificSpecific" value="33">
                                    <label for="cronMinute33" class="nofloat">33</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute34" name="cronMinuteSpecificSpecific" value="34">
                                    <label for="cronMinute34" class="nofloat">34</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">                                                
                                                                                                                                       <input type="checkbox" id="cronMinute35" name="cronMinuteSpecificSpecific" value="35">
                                    <label for="cronMinute35" class="nofloat">35</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute36" name="cronMinuteSpecificSpecific" value="36">
                                    <label for="cronMinute36" class="nofloat">36</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute37" name="cronMinuteSpecificSpecific" value="37">
                                    <label for="cronMinute37" class="nofloat">37</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute38" name="cronMinuteSpecificSpecific" value="38">
                                    <label for="cronMinute38" class="nofloat">38</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute39" name="cronMinuteSpecificSpecific" value="39">
                                   <label for="cronMinute39" class="nofloat">39</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute40" name="cronMinuteSpecificSpecific" value="40">
                                    <label for="cronMinute40" class="nofloat">40</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute41" name="cronMinuteSpecificSpecific" value="41">
                                    <label for="cronMinute41" class="nofloat">41</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute42" name="cronMinuteSpecificSpecific" value="42">
                                    <label for="cronMinute42" class="nofloat">42</label>
                                  
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute43" name="cronMinuteSpecificSpecific" value="43">
                                    <label for="cronMinute43" class="nofloat">43</label>                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute44" name="cronMinuteSpecificSpecific" value="44">
                                    <label for="cronMinute44" class="nofloat">44</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">                                                                                             
                                                                                                                                       <input type="checkbox" id="cronMinute45" name="cronMinuteSpecificSpecific" value="45">
                                    <label for="cronMinute45" class="nofloat">45</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute46" name="cronMinuteSpecificSpecific" value="46">
                                    <label for="cronMinute46" class="nofloat">46</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute47" name="cronMinuteSpecificSpecific" value="47">
                                    <label for="cronMinute47" class="nofloat">47</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute48" name="cronMinuteSpecificSpecific" value="48">
                                    <label for="cronMinute48" class="nofloat">48</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute49" name="cronMinuteSpecificSpecific" value="49">
                                    <label for="cronMinute49" class="nofloat">49</label>                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute50" name="cronMinuteSpecificSpecific" value="50">
                                    <label for="cronMinute50" class="nofloat">50</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute51" name="cronMinuteSpecificSpecific" value="51">
                                    <label for="cronMinute51" class="nofloat">51</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute52" name="cronMinuteSpecificSpecific" value="52">
                                    <label for="cronMinute52" class="nofloat">52</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute53" name="cronMinuteSpecificSpecific" value="53">
                                    <label for="cronMinute53" class="nofloat">53</label>                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute54" name="cronMinuteSpecificSpecific" value="54">
                                    <label for="cronMinute54" class="nofloat">54</label>
                                   
                                                                                                                                       </div>

                                                                                                                                                      <div class="cronboxcheck">                                                
                                                                                                                                       <input type="checkbox" id="cronMinute55" name="cronMinuteSpecificSpecific" value="55">
                                    <label for="cronMinute55" class="nofloat">55</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute56" name="cronMinuteSpecificSpecific" value="56">
                                    <label for="cronMinute56" class="nofloat">56</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute57" name="cronMinuteSpecificSpecific" value="57">
                                    <label for="cronMinute57" class="nofloat">57</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute58" name="cronMinuteSpecificSpecific" value="58">
                                    <label for="cronMinute58" class="nofloat">58</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronMinute59" name="cronMinuteSpecificSpecific" value="59">
                                    <label for="cronMinute59" class="nofloat">59</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                </div>
                            </div>

                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronMinuteRange" name="cronMinute">
                            <label for="cronMinuteRange" class="nofloat">
                                Every minute between minute 
                                <select id="cronMinuteRangeStart" style="width:50px;">
                                    <option value="0">00</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                </select>
                                and minute 
                                <select id="cronMinuteRangeEnd" style="width:50px;">
                                    <option value="0">00</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                </select>
                            </label>
                            </div>
                        </div>
                    </div>
                    <div id="tabs-3" aria-labelledby="ui-id-3" class="ui-tabs-panel ui-widget-content ui-corner-bottom" role="tabpanel" style="display: none;" aria-hidden="true">
                        <div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronEveryHour" name="cronHour">
                            <label for="cronEveryHour" class="nofloat">Every hour</label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronHourIncrement" name="cronHour">
                            <label for="cronHourIncrement" class="nofloat">
                                Every
                                <select id="cronHourIncrementIncrement" style="width:50px;">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                </select>
                              hour(s) starting at hour 
                                <select id="cronHourIncrementStart" style="width:50px;">
                                    <option value="0">00</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                </select>
                            </label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronHourSpecific" checked="checked" name="cronHour">
                            <label for="cronHourSpecific" class="nofloat">Specific hour (choose one or many)</label>
                            <div class="chekboxblock">
                                <div class="flexblock">
                                                                                                                                       <div class="cronboxcheck">                                    
                                    <input type="checkbox" id="cronHour0" name="cronHourSpecificSpecific" value="0" checked="checked">
                                                                                                                                       <label for="cronHour0" class="nofloat">00</label>
                                    </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour1" name="cronHourSpecificSpecific" value="1">
                                    <label for="cronHour1" class="nofloat">01</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour2" name="cronHourSpecificSpecific" value="2">
                                    <label for="cronHour2" class="nofloat">02</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour3" name="cronHourSpecificSpecific" value="3">
                                    <label for="cronHour3" class="nofloat">03</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour4" name="cronHourSpecificSpecific" value="4">
                                    <label for="cronHour4" class="nofloat">04</label>
                                   
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour5" name="cronHourSpecificSpecific" value="5">
                                    <label for="cronHour5" class="nofloat">05</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour6" name="cronHourSpecificSpecific" value="6">
                                    <label for="cronHour6" class="nofloat">06</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour7" name="cronHourSpecificSpecific" value="7">
                                    <label for="cronHour7" class="nofloat">07</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour8" name="cronHourSpecificSpecific" value="8">
                                    <label for="cronHour8" class="nofloat">08</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour9" name="cronHourSpecificSpecific" value="9">
                                    <label for="cronHour9" class="nofloat">09</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">    
                                                                                                                                       <input type="checkbox" id="cronHour10" name="cronHourSpecificSpecific" value="10">                                                                                                                          
                                    <label for="cronHour10" class="nofloat">10</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour11" name="cronHourSpecificSpecific" value="11">
                                    <label for="cronHour11" class="nofloat">11</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour12" name="cronHourSpecificSpecific" value="12">
                                    <label for="cronHour12" class="nofloat">12</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour13" name="cronHourSpecificSpecific" value="13">
                                    <label for="cronHour13" class="nofloat">13</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour14" name="cronHourSpecificSpecific" value="14">
                                    <label for="cronHour14" class="nofloat">14</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour15" name="cronHourSpecificSpecific" value="15">                                                                                                                          
                                    <label for="cronHour15" class="nofloat">15</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour16" name="cronHourSpecificSpecific" value="16">
                                    <label for="cronHour16" class="nofloat">16</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour17" name="cronHourSpecificSpecific" value="17">
                                    <label for="cronHour17" class="nofloat">17</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour18" name="cronHourSpecificSpecific" value="18">
                                    <label for="cronHour18" class="nofloat">18</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour19" name="cronHourSpecificSpecific" value="19">
                                    <label for="cronHour19" class="nofloat">19</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour20" name="cronHourSpecificSpecific" value="20">                                                                                                                          
                                    <label for="cronHour20" class="nofloat">20</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour21" name="cronHourSpecificSpecific" value="21">
                                    <label for="cronHour21" class="nofloat">21</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour22" name="cronHourSpecificSpecific" value="22">
                                    <label for="cronHour22" class="nofloat">22</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronHour23" name="cronHourSpecificSpecific" value="23">
                                    <label for="cronHour23" class="nofloat">23</label>                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                </div>
                            </div>
                           </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronHourRange" name="cronHour">
                            <label for="cronHourRange" class="nofloat">
                                Every hour between hour 
                                <select id="cronHourRangeStart" style="width:50px;">
                                    <option value="0">00</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                </select>
                                and hour 
                                <select id="cronHourRangeEnd" style="width:50px;">
                                    <option value="0">00</option>
                                    <option value="1">01</option>
                                    <option value="2">02</option>
                                    <option value="3">03</option>
                                    <option value="4">04</option>
                                    <option value="5">05</option>
                                    <option value="6">06</option>
                                    <option value="7">07</option>
                                    <option value="8">08</option>
                                    <option value="9">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                </select>
                            </label>
                            </div>
                        </div>
                    </div>
                    <div id="tabs-4" aria-labelledby="ui-id-4" class="ui-tabs-panel ui-widget-content ui-corner-bottom" role="tabpanel" style="display: none;" aria-hidden="true">
                        <div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronEveryDay" name="cronDay" checked="checked">
                            <label for="cronEveryDay" class="nofloat">Every day</label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronDowIncrement" name="cronDay">
                            <label for="cronDowIncrement" class="nofloat">
                                Every
                                <select id="cronDowIncrementIncrement" style="width:50px;">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                </select>
                                day(s) starting on 
                                <select id="cronDowIncrementStart" style="width:125px;">
                                    <option value="1">Sunday</option>
                                    <option value="2">Monday</option>
                                    <option value="3">Tuesday</option>
                                    <option value="4">Wednesday</option>
                                    <option value="5">Thursday</option>
                                    <option value="6">Friday</option>
                                    <option value="7">Saturday</option>
                                </select>
                            </label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronDomIncrement" name="cronDay">
                            <label for="cronDomIncrement" class="nofloat">
                                Every
                                <select id="cronDomIncrementIncrement" style="width:50px;">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                </select>
                                day(s) starting on the 
                                <select id="cronDomIncrementStart" style="width:50px;">
                                    <option value="1">1st</option>
                                    <option value="2">2nd</option>
                                    <option value="3">3rd</option>
                                    <option value="4">4th</option>
                                    <option value="5">5th</option>
                                    <option value="6">6th</option>
                                    <option value="7">7th</option>
                                    <option value="8">8th</option>
                                    <option value="9">9th</option>
                                    <option value="10">10th</option>
                                    <option value="11">11th</option>
                                    <option value="12">12th</option>
                                    <option value="13">13th</option>
                                    <option value="14">14th</option>
                                    <option value="15">15th</option>
                                    <option value="16">16th</option>
                                    <option value="17">17th</option>
                                    <option value="18">18th</option>
                                    <option value="19">19th</option>
                                    <option value="20">20th</option>
                                    <option value="21">21st</option>
                                    <option value="22">22nd</option>
                                    <option value="23">23rd</option>
                                    <option value="24">24th</option>
                                    <option value="25">25th</option>
                                    <option value="26">26th</option>
                                    <option value="27">27th</option>
                                    <option value="28">28th</option>
                                    <option value="29">29th</option>
                                    <option value="30">30th</option>
                                    <option value="31">31st</option>
                                </select>
                                of the month
                            </label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronDowSpecific" name="cronDay">
                            <label for="cronDowSpecific" class="nofloat">Specific day of week (choose one or many)</label>
                                                                                                                        <div class="chekboxblock">
                                <div class="flexblock">
                                                                                                                        
                                                                                                                        <div class="cronboxcheck">
                                                                                                                        <input type="checkbox" id="cronDowSun" name="cronDowSpecificSpecific" value="SUN">
                                <label for="cronDowSun" class="nofloat">Sunday</label>
                              
                                                                                                                        </div>
                                                                                                                        
                                                                                                                        <div class="cronboxcheck">
                                                                                                                        <input type="checkbox" id="cronDowMon" name="cronDowSpecificSpecific" value="MON">
                                <label for="cronDowMon" class="nofloat">Monday</label>
                                
                                                                                                                        </div>
                                                                                                                        
                                                                                                                        <div class="cronboxcheck">
                                                                                                                        <input type="checkbox" id="cronDowTue" name="cronDowSpecificSpecific" value="TUE">
                                <label for="cronDowTue" class="nofloat">Tuesday</label>
                                
                                                                                                                        </div>
                                                                                                                        
                                                                                                                        <div class="cronboxcheck">
                                                                                                                        <input type="checkbox" id="cronDowWed" name="cronDowSpecificSpecific" value="WED">
                                <label for="cronDowWed" class="nofloat">Wednesday</label>
                                
                                                                                                                        </div>
                                                                                                                        
                                                                                                                        
                                                                                                                        <div class="cronboxcheck">
                                                                                                                        <input type="checkbox" id="cronDowThu" name="cronDowSpecificSpecific" value="THU">
                                <label for="cronDowThu" class="nofloat">Thursday</label>
                                
                                                                                                                        </div>
                                                                                                                        
                                                                                                                        <div class="cronboxcheck">
                                                                                                                        <input type="checkbox" id="cronDowFri" name="cronDowSpecificSpecific" value="FRI">
                                <label for="cronDowFri" class="nofloat">Friday</label>
                                
                                                                                                                        </div>

                                                                                                                        <div class="cronboxcheck">
                                                                                                                        <input type="checkbox" id="cronDowSat" name="cronDowSpecificSpecific" value="SAT">
                                <label for="cronDowSat" class="nofloat">Saturday</label>
                                
                                                                                                                        </div>
                                                                                                                        
                            </div>
                                                                               </div>                              
                            </div>


                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronDomSpecific" name="cronDay">
                            <label for="cronDomSpecific" class="nofloat">Specific day of month (choose one or many)</label>
                                <div class="chekboxblock">
                                <div class="flexblock">
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom1" name="cronDomSpecificSpecific" value="1">
                                    <label for="cronDom1" class="nofloat">01</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom2" name="cronDomSpecificSpecific" value="2">
                                    <label for="cronDom2" class="nofloat">02</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom3" name="cronDomSpecificSpecific" value="3">
                                    <label for="cronDom3" class="nofloat">03</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom4" name="cronDomSpecificSpecific" value="4">
                                    <label for="cronDom4" class="nofloat">04</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom5" name="cronDomSpecificSpecific" value="5">
                                    <label for="cronDom5" class="nofloat">05</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom6" name="cronDomSpecificSpecific" value="6">
                                    <label for="cronDom6" class="nofloat">06</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom7" name="cronDomSpecificSpecific" value="7">
                                    <label for="cronDom7" class="nofloat">07</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom8" name="cronDomSpecificSpecific" value="8">
                                    <label for="cronDom8" class="nofloat">08</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom9" name="cronDomSpecificSpecific" value="9">
                                    <label for="cronDom9" class="nofloat">09</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom10" name="cronDomSpecificSpecific" value="10">
                                    <label for="cronDom10" class="nofloat">10</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom11" name="cronDomSpecificSpecific" value="11">
                                                                                                                                       <label for="cronDom11" class="nofloat">11</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom12" name="cronDomSpecificSpecific" value="12">
                                    <label for="cronDom12" class="nofloat">12</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom13" name="cronDomSpecificSpecific" value="13">
                                    <label for="cronDom13" class="nofloat">13</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom14" name="cronDomSpecificSpecific" value="14">                                                                                                                          
                                    <label for="cronDom14" class="nofloat">14</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom15" name="cronDomSpecificSpecific" value="15">
                                   <label for="cronDom15" class="nofloat">15</label>
                                   
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom16" name="cronDomSpecificSpecific" value="16">
                                    <label for="cronDom16" class="nofloat">16</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom17" name="cronDomSpecificSpecific" value="17">
                                    <label for="cronDom17" class="nofloat">17</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom18" name="cronDomSpecificSpecific" value="18">
                                    <label for="cronDom18" class="nofloat">18</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom19" name="cronDomSpecificSpecific" value="19">
                                    <label for="cronDom19" class="nofloat">19</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom20" name="cronDomSpecificSpecific" value="20">
                                    <label for="cronDom20" class="nofloat">20</label>
                                    
                                                                                                                                       </div>                                                                                                                            
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom21" name="cronDomSpecificSpecific" value="21">
                                    <label for="cronDom21" class="nofloat">21</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom22" name="cronDomSpecificSpecific" value="22">
                                    <label for="cronDom22" class="nofloat">22</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom23" name="cronDomSpecificSpecific" value="23">
                                    <label for="cronDom23" class="nofloat">23</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom24" name="cronDomSpecificSpecific" value="24">                                                                                                                          
                                    <label for="cronDom24" class="nofloat">24</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom25" name="cronDomSpecificSpecific" value="25">
                                   <label for="cronDom25" class="nofloat">25</label>
                                    
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom26" name="cronDomSpecificSpecific" value="26">
                                    <label for="cronDom26" class="nofloat">26</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom27" name="cronDomSpecificSpecific" value="27">
                                    <label for="cronDom27" class="nofloat">27</label>
                                    
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom28" name="cronDomSpecificSpecific" value="28">
                                    <label for="cronDom28" class="nofloat">28</label>
                                   
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom29" name="cronDomSpecificSpecific" value="29">
                                    <label for="cronDom29" class="nofloat">29</label>
                                   </div>
                                                                                                                                       
                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom30" name="cronDomSpecificSpecific" value="30">
                                    <label for="cronDom30" class="nofloat">30</label>
                                   
                                                                                                                                       </div>

                                                                                                                                       <div class="cronboxcheck">
                                                                                                                                       <input type="checkbox" id="cronDom31" name="cronDomSpecificSpecific" value="31">
                                     <label for="cronDom31" class="nofloat">31</label>
                                   
                                                                                                                                       </div>
                                                                                                                                       
                                                                                                                                       
                                </div>
                            </div>
                            </div>


                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronLastDayOfMonth" name="cronDay">
                            <label for="cronLastDayOfMonth" class="nofloat">On the last day of the month</label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronLastWeekdayOfMonth" name="cronDay">
                            <label for="cronLastWeekdayOfMonth" class="nofloat">On the last weekday of the month</label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronLastSpecificDom" name="cronDay">
                            <label for="cronLastSpecificDom" class="nofloat">
                                On the last 
                                <select id="cronLastSpecificDomDay" style="width:125px;">
                                    <option value="1">Sunday</option>
                                    <option value="2">Monday</option>
                                    <option value="3">Tuesday</option>
                                    <option value="4">Wednesday</option>
                                    <option value="5">Thursday</option>
                                    <option value="6">Friday</option>
                                    <option value="7">Saturday</option>
                                </select>
                                of the month
                            </label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronDaysBeforeEom" name="cronDay">
                            <label for="cronDaysBeforeEom" class="nofloat">
                                <select id="cronDaysBeforeEomMinus" style="width:50px;">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                </select>
                                day(s) before the end of the month
                            </label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronDaysNearestWeekdayEom" name="cronDay">
                            <label for="cronDaysNearestWeekdayEom" class="nofloat">
                                Nearest weekday (Monday to Friday) to the 
                                <select id="cronDaysNearestWeekday" style="width:50px;">
                                    <option value="1">1st</option>
                                    <option value="2">2nd</option>
                                    <option value="3">3rd</option>
                                    <option value="4">4th</option>
                                    <option value="5">5th</option>
                                    <option value="6">6th</option>
                                    <option value="7">7th</option>
                                    <option value="8">8th</option>
                                    <option value="9">9th</option>
                                    <option value="10">10th</option>
                                    <option value="11">11th</option>
                                    <option value="12">12th</option>
                                    <option value="13">13th</option>
                                    <option value="14">14th</option>
                                    <option value="15">15th</option>
                                    <option value="16">16th</option>
                                    <option value="17">17th</option>
                                    <option value="18">18th</option>
                                    <option value="19">19th</option>
                                    <option value="20">20th</option>
                                    <option value="21">21st</option>
                                    <option value="22">22nd</option>
                                    <option value="23">23rd</option>
                                    <option value="24">24th</option>
                                    <option value="25">25th</option>
                                    <option value="26">26th</option>
                                    <option value="27">27th</option>
                                    <option value="28">28th</option>
                                    <option value="29">29th</option>
                                    <option value="30">30th</option>
                                    <option value="31">31st</option>
                                </select>
                                of the month
                            </label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronNthDay" name="cronDay">
                            <label for="cronNthDay" class="nofloat">
                                On the 
                                <select id="cronNthDayNth" style="width:50px;">
                                    <option value="1">1st</option>
                                    <option value="2">2nd</option>
                                    <option value="3">3rd</option>
                                    <option value="4">4th</option>
                                    <option value="5">5th</option>
                                </select>
                                <select id="cronNthDayDay" style="width:125px;">
                                    <option value="1">Sunday</option>
                                    <option value="2">Monday</option>
                                    <option value="3">Tuesday</option>
                                    <option value="4">Wednesday</option>
                                    <option value="5">Thursday</option>
                                    <option value="6">Friday</option>
                                    <option value="7">Saturday</option>
                                </select>
                                of the month
                            </label>
                            </div>
                        </div>
                    </div>
                    <div id="tabs-5" aria-labelledby="ui-id-5" class="ui-tabs-panel ui-widget-content ui-corner-bottom" role="tabpanel" style="display: none;" aria-hidden="true">
                        <div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronEveryMonth" name="cronMonth" checked="checked">
                            <label for="cronEveryMonth" class="nofloat">Every month</label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronMonthIncrement" name="cronMonth">
                            <label for="cronMonthIncrement" class="nofloat">
                                Every
                                <select id="cronMonthIncrementIncrement" style="width:50px;">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                </select>
                                month(s) starting in 
                                <select id="cronMonthIncrementStart" style="width:125px;">
                                    <option value="1">January</option>
                                    <option value="2">February</option>
                                    <option value="3">March</option>
                                    <option value="4">April</option>
                                    <option value="5">May</option>
                                    <option value="6">June</option>
                                    <option value="7">July</option>
                                    <option value="8">August</option>
                                    <option value="9">September</option>
                                    <option value="10">October</option>
                                    <option value="11">November</option>
                                    <option value="12">December</option>
                                </select>
                            </label>
                            </div>


                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronMonthSpecific" name="cronMonth">
                            <label for="cronMonthSpecific" class="nofloat">Specific month (choose one or many)</label>
                            
                                                                                                                                       <div class="chekboxblock">
                                <div class="flexblock">
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                        
                                        <input type="checkbox" id="cronMonth1" name="cronMonthSpecificSpecific" value="JAN">
                                                                                                                                                      <label for="cronMonth1" class="nofloat">January&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                        
                                        <input type="checkbox" id="cronMonth2" name="cronMonthSpecificSpecific" value="FEB">
                                                                                                                                                      <label for="cronMonth2" class="nofloat">February&nbsp;&nbsp;</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                        
                                        <input type="checkbox" id="cronMonth3" name="cronMonthSpecificSpecific" value="MAR">
                                                                                                                                                      <label for="cronMonth3" class="nofloat">March</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                        
                                        <input type="checkbox" id="cronMonth4" name="cronMonthSpecificSpecific" value="APR">
                                                                                                                                                      <label for="cronMonth4" class="nofloat">April</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                        
                                        <input type="checkbox" id="cronMonth5" name="cronMonthSpecificSpecific" value="MAY">
                                                                                                                                                      <label for="cronMonth5" class="nofloat">May</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                        
                                        <input type="checkbox" id="cronMonth6" name="cronMonthSpecificSpecific" value="JUN">
                                                                                                                                                      <label for="cronMonth6" class="nofloat">June</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                        
                                        <input type="checkbox" id="cronMonth7" name="cronMonthSpecificSpecific" value="JUL">
                                                                                                                                                      <label for="cronMonth7" class="nofloat">July</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                       
                                        <input type="checkbox" id="cronMonth8" name="cronMonthSpecificSpecific" value="AUG">
                                                                                                                                                      <label for="cronMonth8" class="nofloat">August</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                       
                                        <input type="checkbox" id="cronMonth0" name="cronMonthSpecificSpecific" value="SEP">
                                                                                                                                                      <label for="cronMonth9" class="nofloat">September</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                        
                                        <input type="checkbox" id="cronMonth10" name="cronMonthSpecificSpecific" value="OCT">
                                                                                                                                                      <label for="cronMonth10" class="nofloat">October</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">                                                
                                                                                                                                                      <input type="checkbox" id="cronMonth11" name="cronMonthSpecificSpecific" value="NOV">
                                                                                                                                                      <label for="cronMonth11" class="nofloat">November</label>
                                                                                                                                                      </div>
                                                                                                                                                      
                                                                                                                                                      <div class="cronboxcheck">
                                                                                                                                                      <input type="checkbox" id="cronMonth12" name="cronMonthSpecificSpecific" value="DEC">
                                        <label for="cronMonth12" class="nofloat">December</label>                                        
                                                                                                                                                      </div>
                                     
                                </div>
                            </div>
                            </div>


                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronMonthRange" name="cronMonth">
                            <label for="cronMonthRange" class="nofloat">
                                Every month between 
                                <select id="cronMonthRangeStart" style="width:125px;">
                                    <option value="1">January</option>
                                    <option value="2">February</option>
                                    <option value="3">March</option>
                                   <option value="4">April</option>
                                    <option value="5">May</option>
                                    <option value="6">June</option>
                                    <option value="7">July</option>
                                    <option value="8">August</option>
                                    <option value="9">September</option>
                                    <option value="10">October</option>
                                    <option value="11">November</option>
                                    <option value="12">December</option>
                                </select>
                                and 
                                <select id="cronMonthRangeEnd" style="width:125px;">
                                    <option value="1">January</option>
                                    <option value="2">February</option>
                                    <option value="3">March</option>
                                    <option value="4">April</option>
                                    <option value="5">May</option>
                                    <option value="6">June</option>
                                    <option value="7">July</option>
                                    <option value="8">August</option>
                                    <option value="9">September</option>
                                    <option value="10">October</option>
                                    <option value="11">November</option>
                                    <option value="12">December</option>
                                </select>
                            </label>
                            </div>
                        </div>
                    </div>
                    <div id="tabs-6" aria-labelledby="ui-id-6" class="ui-tabs-panel ui-widget-content ui-corner-bottom" role="tabpanel" style="display: none;" aria-hidden="true">
                        <div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronEveryYear" name="cronYear" checked="checked">
                            <label for="cronEveryYear" class="nofloat">Any year</label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronYearIncrement" name="cronYear">
                            <label for="cronYearIncrement" class="nofloat">
                                Every
                                <select id="cronYearIncrementIncrement" style="width:50px;">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                    <option value="21">21</option>
                                    <option value="22">22</option>
                                    <option value="23">23</option>
                                    <option value="24">24</option>
                                    <option value="25">25</option>
                                    <option value="26">26</option>
                                    <option value="27">27</option>
                                    <option value="28">28</option>
                                    <option value="29">29</option>
                                    <option value="30">30</option>
                                    <option value="31">31</option>
                                    <option value="32">32</option>
                                    <option value="33">33</option>
                                    <option value="34">34</option>
                                    <option value="35">35</option>
                                    <option value="36">36</option>
                                    <option value="37">37</option>
                                    <option value="38">38</option>
                                    <option value="39">39</option>
                                    <option value="40">40</option>
                                    <option value="41">41</option>
                                    <option value="42">42</option>
                                    <option value="43">43</option>
                                    <option value="44">44</option>
                                    <option value="45">45</option>
                                    <option value="46">46</option>
                                    <option value="47">47</option>
                                    <option value="48">48</option>
                                    <option value="49">49</option>
                                    <option value="50">50</option>
                                    <option value="51">51</option>
                                    <option value="52">52</option>
                                    <option value="53">53</option>
                                    <option value="54">54</option>
                                    <option value="55">55</option>
                                    <option value="56">56</option>
                                    <option value="57">57</option>
                                    <option value="58">58</option>
                                    <option value="59">59</option>
                                    <option value="60">60</option>
                                    <option value="61">61</option>
                                    <option value="62">62</option>
                                    <option value="63">63</option>
                                    <option value="64">64</option>
                                    <option value="65">65</option>
                                    <option value="66">66</option>
                                    <option value="67">67</option>
                                    <option value="68">68</option>
                                    <option value="69">69</option>
                                    <option value="70">70</option>
                                    <option value="71">71</option>
                                    <option value="72">72</option>
                                    <option value="73">73</option>
                                    <option value="74">74</option>
                                    <option value="75">75</option>
                                    <option value="76">76</option>
                                    <option value="77">77</option>
                                    <option value="78">78</option>
                                    <option value="79">79</option>
                                    <option value="80">80</option>
                                    <option value="81">81</option>
                                    <option value="82">82</option>
                                    <option value="83">83</option>
                                </select>
                                years(s) starting in 
                                <select id="cronYearIncrementStart" style="width:80px;">
                                    <option value="2016">2016</option>
                                    <option value="2017">2017</option>
                                    <option value="2018">2018</option>
                                    <option value="2019">2019</option>
                                    <option value="2020">2020</option>
                                    <option value="2021">2021</option>
                                    <option value="2022">2022</option>
                                    <option value="2023">2023</option>
                                    <option value="2024">2024</option>
                                    <option value="2025">2025</option>
                                    <option value="2026">2026</option>
                                    <option value="2027">2027</option>
                                    <option value="2028">2028</option>
                                    <option value="2029">2029</option>
                                    <option value="2030">2030</option>
                                    <option value="2031">2031</option>
                                    <option value="2032">2032</option>
                                    <option value="2033">2033</option>
                                    <option value="2034">2034</option>
                                    <option value="2035">2035</option>
                                    <option value="2036">2036</option>
                                    <option value="2037">2037</option>
                                    <option value="2038">2038</option>
                                    <option value="2039">2039</option>
                                    <option value="2040">2040</option>
                                    <option value="2041">2041</option>
                                    <option value="2042">2042</option>
                                    <option value="2043">2043</option>
                                    <option value="2044">2044</option>
                                    <option value="2045">2045</option>
                                    <option value="2046">2046</option>
                                    <option value="2047">2047</option>
                                    <option value="2048">2048</option>
                                    <option value="2049">2049</option>
                                    <option value="2050">2050</option>
                                    <option value="2051">2051</option>
                                    <option value="2052">2052</option>
                                    <option value="2053">2053</option>
                                    <option value="2054">2054</option>
                                    <option value="2055">2055</option>
                                    <option value="2056">2056</option>
                                    <option value="2057">2057</option>
                                    <option value="2058">2058</option>
                                    <option value="2059">2059</option>
                                    <option value="2060">2060</option>
                                    <option value="2061">2061</option>
                                    <option value="2062">2062</option>
                                    <option value="2063">2063</option>
                                    <option value="2064">2064</option>
                                    <option value="2065">2065</option>
                                    <option value="2066">2066</option>
                                    <option value="2067">2067</option>
                                    <option value="2068">2068</option>
                                    <option value="2069">2069</option>
                                    <option value="2070">2070</option>
                                    <option value="2071">2071</option>
                                    <option value="2072">2072</option>
                                    <option value="2073">2073</option>
                                    <option value="2074">2074</option>
                                    <option value="2075">2075</option>
                                    <option value="2076">2076</option>
                                    <option value="2077">2077</option>
                                    <option value="2078">2078</option>
                                    <option value="2079">2079</option>
                                    <option value="2080">2080</option>
                                    <option value="2081">2081</option>
                                    <option value="2082">2082</option>
                                    <option value="2083">2083</option>
                                    <option value="2084">2084</option>
                                    <option value="2085">2085</option>
                                    <option value="2086">2086</option>
                                    <option value="2087">2087</option>
                                    <option value="2088">2088</option>
                                    <option value="2089">2089</option>
                                    <option value="2090">2090</option>
                                    <option value="2091">2091</option>
                                    <option value="2092">2092</option>
                                    <option value="2093">2093</option>
                                    <option value="2094">2094</option>
                                    <option value="2095">2095</option>
                                    <option value="2096">2096</option>
                                    <option value="2097">2097</option>
                                    <option value="2098">2098</option>
                                    <option value="2099">2099</option>
                                </select>
                            </label>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronYearSpecific" name="cronYear">
                            <label for="cronYearSpecific" class="nofloat">Specific year (choose one or many)</label>
                            <div style="margin-left:50px;">
                                <div>
                                    <label for="cronYear2016" class="nofloat">2016</label>
                                    <input type="checkbox" id="cronYear2016" name="cronYearSpecificSpecific" value="2016">
                                    <label for="cronYear2017" class="nofloat">2017</label>
                                    <input type="checkbox" id="cronYear2017" name="cronYearSpecificSpecific" value="2017">
                                    <label for="cronYear2018" class="nofloat">2018</label>
                                    <input type="checkbox" id="cronYear2018" name="cronYearSpecificSpecific" value="2018">
                                    <label for="cronYear2019" class="nofloat">2019</label>
                                    <input type="checkbox" id="cronYear2019" name="cronYearSpecificSpecific" value="2019">
                                    <label for="cronYear2020" class="nofloat">2020</label>
                                    <input type="checkbox" id="cronYear2020" name="cronYearSpecificSpecific" value="2020">
                                    <label for="cronYear2021" class="nofloat">2021</label>
                                    <input type="checkbox" id="cronYear2021" name="cronYearSpecificSpecific" value="2021">
                                    <label for="cronYear2022" class="nofloat">2022</label>
                                    <input type="checkbox" id="cronYear2022" name="cronYearSpecificSpecific" value="2022">
                                    <label for="cronYear2023" class="nofloat">2023</label>
                                    <input type="checkbox" id="cronYear2023" name="cronYearSpecificSpecific" value="2023">
                                    <label for="cronYear2024" class="nofloat">2024</label>
                                    <input type="checkbox" id="cronYear2024" name="cronYearSpecificSpecific" value="2024">
                                    <label for="cronYear2025" class="nofloat">2025</label>
                                    <input type="checkbox" id="cronYear2025" name="cronYearSpecificSpecific" value="2025">
                                    <label for="cronYear2026" class="nofloat">2026</label>
                                    <input type="checkbox" id="cronYear2026" name="cronYearSpecificSpecific" value="2026">
                                    <label for="cronYear2027" class="nofloat">2027</label>
                                    <input type="checkbox" id="cronYear2027" name="cronYearSpecificSpecific" value="2027">
                                </div>
                                <div>
                                    <label for="cronYear2028" class="nofloat">2028</label>
                                    <input type="checkbox" id="cronYear2028" name="cronYearSpecificSpecific" value="2028">
                                    <label for="cronYear2029" class="nofloat">2029</label>
                                    <input type="checkbox" id="cronYear2029" name="cronYearSpecificSpecific" value="2029">
                                    <label for="cronYear2030" class="nofloat">2030</label>
                                    <input type="checkbox" id="cronYear2030" name="cronYearSpecificSpecific" value="2030">
                                    <label for="cronYear2031" class="nofloat">2031</label>
                                    <input type="checkbox" id="cronYear2031" name="cronYearSpecificSpecific" value="2031">
                                    <label for="cronYear2032" class="nofloat">2032</label>
                                    <input type="checkbox" id="cronYear2032" name="cronYearSpecificSpecific" value="2032">
                                    <label for="cronYear2033" class="nofloat">2033</label>
                                    <input type="checkbox" id="cronYear2033" name="cronYearSpecificSpecific" value="2033">
                                    <label for="cronYear2034" class="nofloat">2034</label>
                                    <input type="checkbox" id="cronYear2034" name="cronYearSpecificSpecific" value="2034">
                                    <label for="cronYear2035" class="nofloat">2035</label>
                                    <input type="checkbox" id="cronYear2035" name="cronYearSpecificSpecific" value="2035">
                                    <label for="cronYear2036" class="nofloat">2036</label>
                                    <input type="checkbox" id="cronYear2036" name="cronYearSpecificSpecific" value="2036">
                                    <label for="cronYear2037" class="nofloat">2037</label>
                                    <input type="checkbox" id="cronYear2037" name="cronYearSpecificSpecific" value="2037">
                                    <label for="cronYear2038" class="nofloat">2038</label>
                                    <input type="checkbox" id="cronYear2038" name="cronYearSpecificSpecific" value="2038">
                                    <label for="cronYear2039" class="nofloat">2039</label>
                                    <input type="checkbox" id="cronYear2039" name="cronYearSpecificSpecific" value="2039">
                                </div>
                                <div>
                                    <label for="cronYear2040" class="nofloat">2040</label>
                                    <input type="checkbox" id="cronYear2040" name="cronYearSpecificSpecific" value="2040">
                                    <label for="cronYear2041" class="nofloat">2041</label>
                                    <input type="checkbox" id="cronYear2041" name="cronYearSpecificSpecific" value="2041">
                                    <label for="cronYear2042" class="nofloat">2042</label>
                                    <input type="checkbox" id="cronYear2042" name="cronYearSpecificSpecific" value="2042">
                                    <label for="cronYear2043" class="nofloat">2043</label>
                                    <input type="checkbox" id="cronYear2043" name="cronYearSpecificSpecific" value="2043">
                                    <label for="cronYear2044" class="nofloat">2044</label>
                                    <input type="checkbox" id="cronYear2044" name="cronYearSpecificSpecific" value="2044">
                                    <label for="cronYear2045" class="nofloat">2045</label>
                                    <input type="checkbox" id="cronYear2045" name="cronYearSpecificSpecific" value="2045">
                                    <label for="cronYear2046" class="nofloat">2046</label>
                                    <input type="checkbox" id="cronYear2046" name="cronYearSpecificSpecific" value="2046">
                                    <label for="cronYear2047" class="nofloat">2047</label>
                                    <input type="checkbox" id="cronYear2047" name="cronYearSpecificSpecific" value="2047">
                                    <label for="cronYear2048" class="nofloat">2048</label>
                                    <input type="checkbox" id="cronYear2048" name="cronYearSpecificSpecific" value="2048">
                                    <label for="cronYear2049" class="nofloat">2049</label>
                                    <input type="checkbox" id="cronYear2049" name="cronYearSpecificSpecific" value="2049">
                                    <label for="cronYear2050" class="nofloat">2050</label>
                                    <input type="checkbox" id="cronYear2050" name="cronYearSpecificSpecific" value="2050">
                                    <label for="cronYear2051" class="nofloat">2051</label>
                                    <input type="checkbox" id="cronYear2051" name="cronYearSpecificSpecific" value="2051">
                                </div>
                                <div>
                                    <label for="cronYear2052" class="nofloat">2052</label>
                                    <input type="checkbox" id="cronYear2052" name="cronYearSpecificSpecific" value="2052">
                                    <label for="cronYear2053" class="nofloat">2053</label>
                                    <input type="checkbox" id="cronYear2053" name="cronYearSpecificSpecific" value="2053">
                                    <label for="cronYear2054" class="nofloat">2054</label>
                                    <input type="checkbox" id="cronYear2054" name="cronYearSpecificSpecific" value="2054">
                                    <label for="cronYear2055" class="nofloat">2055</label>
                                    <input type="checkbox" id="cronYear2055" name="cronYearSpecificSpecific" value="2055">
                                    <label for="cronYear2056" class="nofloat">2056</label>
                                    <input type="checkbox" id="cronYear2056" name="cronYearSpecificSpecific" value="2056">
                                    <label for="cronYear2057" class="nofloat">2057</label>
                                    <input type="checkbox" id="cronYear2057" name="cronYearSpecificSpecific" value="2057">
                                    <label for="cronYear2058" class="nofloat">2058</label>
                                    <input type="checkbox" id="cronYear2058" name="cronYearSpecificSpecific" value="2058">
                                    <label for="cronYear2059" class="nofloat">2059</label>
                                    <input type="checkbox" id="cronYear2059" name="cronYearSpecificSpecific" value="2059">
                                    <label for="cronYear2060" class="nofloat">2060</label>
                                    <input type="checkbox" id="cronYear2060" name="cronYearSpecificSpecific" value="2060">
                                    <label for="cronYear2061" class="nofloat">2061</label>
                                    <input type="checkbox" id="cronYear2061" name="cronYearSpecificSpecific" value="2061">
                                    <label for="cronYear2062" class="nofloat">2062</label>
                                    <input type="checkbox" id="cronYear2062" name="cronYearSpecificSpecific" value="2062">
                                    <label for="cronYear2063" class="nofloat">2063</label>
                                    <input type="checkbox" id="cronYear2063" name="cronYearSpecificSpecific" value="2063">
                                </div>
                                <div>
                                    <label for="cronYear2064" class="nofloat">2064</label>
                                    <input type="checkbox" id="cronYear2064" name="cronYearSpecificSpecific" value="2064">
                                    <label for="cronYear2065" class="nofloat">2065</label>
                                    <input type="checkbox" id="cronYear2065" name="cronYearSpecificSpecific" value="2065">
                                    <label for="cronYear2066" class="nofloat">2066</label>
                                    <input type="checkbox" id="cronYear2066" name="cronYearSpecificSpecific" value="2066">
                                    <label for="cronYear2067" class="nofloat">2067</label>
                                    <input type="checkbox" id="cronYear2067" name="cronYearSpecificSpecific" value="2067">
                                    <label for="cronYear2068" class="nofloat">2068</label>
                                    <input type="checkbox" id="cronYear2068" name="cronYearSpecificSpecific" value="2068">
                                    <label for="cronYear2069" class="nofloat">2069</label>
                                    <input type="checkbox" id="cronYear2069" name="cronYearSpecificSpecific" value="2069">
                                    <label for="cronYear2070" class="nofloat">2070</label>
                                    <input type="checkbox" id="cronYear2070" name="cronYearSpecificSpecific" value="2070">
                                    <label for="cronYear2071" class="nofloat">2071</label>
                                    <input type="checkbox" id="cronYear2071" name="cronYearSpecificSpecific" value="2071">
                                    <label for="cronYear2072" class="nofloat">2072</label>
                                    <input type="checkbox" id="cronYear2072" name="cronYearSpecificSpecific" value="2072">
                                    <label for="cronYear2073" class="nofloat">2073</label>
                                    <input type="checkbox" id="cronYear2073" name="cronYearSpecificSpecific" value="2073">
                                    <label for="cronYear2074" class="nofloat">2074</label>
                                    <input type="checkbox" id="cronYear2074" name="cronYearSpecificSpecific" value="2074">
                                    <label for="cronYear2075" class="nofloat">2075</label>
                                    <input type="checkbox" id="cronYear2075" name="cronYearSpecificSpecific" value="2075">
                                </div>
                                <div>
                                    <label for="cronYear2076" class="nofloat">2076</label>
                                    <input type="checkbox" id="cronYear2076" name="cronYearSpecificSpecific" value="2076">
                                    <label for="cronYear2077" class="nofloat">2077</label>
                                    <input type="checkbox" id="cronYear2077" name="cronYearSpecificSpecific" value="2077">
                                    <label for="cronYear2078" class="nofloat">2078</label>
                                    <input type="checkbox" id="cronYear2078" name="cronYearSpecificSpecific" value="2078">
                                    <label for="cronYear2079" class="nofloat">2079</label>
                                    <input type="checkbox" id="cronYear2079" name="cronYearSpecificSpecific" value="2079">
                                    <label for="cronYear2080" class="nofloat">2080</label>
                                    <input type="checkbox" id="cronYear2080" name="cronYearSpecificSpecific" value="2080">
                                    <label for="cronYear2081" class="nofloat">2081</label>
                                    <input type="checkbox" id="cronYear2081" name="cronYearSpecificSpecific" value="2081">
                                    <label for="cronYear2082" class="nofloat">2082</label>
                                    <input type="checkbox" id="cronYear2082" name="cronYearSpecificSpecific" value="2082">
                                    <label for="cronYear2083" class="nofloat">2083</label>
                                    <input type="checkbox" id="cronYear2083" name="cronYearSpecificSpecific" value="2083">
                                    <label for="cronYear2084" class="nofloat">2084</label>
                                    <input type="checkbox" id="cronYear2084" name="cronYearSpecificSpecific" value="2084">
                                    <label for="cronYear2085" class="nofloat">2085</label>
                                    <input type="checkbox" id="cronYear2085" name="cronYearSpecificSpecific" value="2085">
                                    <label for="cronYear2086" class="nofloat">2086</label>
                                    <input type="checkbox" id="cronYear2086" name="cronYearSpecificSpecific" value="2086">
                                    <label for="cronYear2087" class="nofloat">2087</label>
                                    <input type="checkbox" id="cronYear2087" name="cronYearSpecificSpecific" value="2087">
                                </div>
                                <div>
                                    <label for="cronYear2088" class="nofloat">2088</label>
                                    <input type="checkbox" id="cronYear2088" name="cronYearSpecificSpecific" value="2088">
                                    <label for="cronYear2089" class="nofloat">2089</label>
                                    <input type="checkbox" id="cronYear2089" name="cronYearSpecificSpecific" value="2089">
                                    <label for="cronYear2090" class="nofloat">2090</label>
                                    <input type="checkbox" id="cronYear2090" name="cronYearSpecificSpecific" value="2090">
                                    <label for="cronYear2091" class="nofloat">2091</label>
                                    <input type="checkbox" id="cronYear2091" name="cronYearSpecificSpecific" value="2091">
                                    <label for="cronYear2092" class="nofloat">2092</label>
                                    <input type="checkbox" id="cronYear2092" name="cronYearSpecificSpecific" value="2092">
                                    <label for="cronYear2093" class="nofloat">2093</label>
                                    <input type="checkbox" id="cronYear2093" name="cronYearSpecificSpecific" value="2093">
                                    <label for="cronYear2094" class="nofloat">2094</label>
                                    <input type="checkbox" id="cronYear2094" name="cronYearSpecificSpecific" value="2094">
                                    <label for="cronYear2095" class="nofloat">2095</label>
                                    <input type="checkbox" id="cronYear2095" name="cronYearSpecificSpecific" value="2095">
                                    <label for="cronYear2096" class="nofloat">2096</label>
                                    <input type="checkbox" id="cronYear2096" name="cronYearSpecificSpecific" value="2096">
                                    <label for="cronYear2097" class="nofloat">2097</label>
                                    <input type="checkbox" id="cronYear2097" name="cronYearSpecificSpecific" value="2097">
                                    <label for="cronYear2098" class="nofloat">2098</label>
                                    <input type="checkbox" id="cronYear2098" name="cronYearSpecificSpecific" value="2098">
                                    <label for="cronYear2099" class="nofloat">2099</label>
                                    <input type="checkbox" id="cronYear2099" name="cronYearSpecificSpecific" value="2099">
                                </div>
                                <div>
                                </div>
                            </div>
                            </div>
                            <div class="cron-option" style="padding-bottom:10px">
                            <input type="radio" id="cronYearRange" name="cronYear">
                            <label for="cronYearRange" class="nofloat">
                                Every year between 
                                <select id="cronYearRangeStart" style="width:80px;">
                                    <option value="2016">2016</option>
                                    <option value="2017">2017</option>
                                    <option value="2018">2018</option>
                                    <option value="2019">2019</option>
                                    <option value="2020">2020</option>
                                    <option value="2021">2021</option>
                                    <option value="2022">2022</option>
                                    <option value="2023">2023</option>
                                    <option value="2024">2024</option>
                                    <option value="2025">2025</option>
                                    <option value="2026">2026</option>
                                    <option value="2027">2027</option>
                                    <option value="2028">2028</option>
                                    <option value="2029">2029</option>
                                    <option value="2030">2030</option>
                                    <option value="2031">2031</option>
                                    <option value="2032">2032</option>
                                    <option value="2033">2033</option>
                                    <option value="2034">2034</option>
                                    <option value="2035">2035</option>
                                    <option value="2036">2036</option>
                                    <option value="2037">2037</option>
                                    <option value="2038">2038</option>
                                    <option value="2039">2039</option>
                                    <option value="2040">2040</option>
                                    <option value="2041">2041</option>
                                    <option value="2042">2042</option>
                                    <option value="2043">2043</option>
                                    <option value="2044">2044</option>
                                    <option value="2045">2045</option>
                                    <option value="2046">2046</option>
                                    <option value="2047">2047</option>
                                    <option value="2048">2048</option>
                                    <option value="2049">2049</option>
                                    <option value="2050">2050</option>
                                    <option value="2051">2051</option>
                                    <option value="2052">2052</option>
                                    <option value="2053">2053</option>
                                    <option value="2054">2054</option>
                                    <option value="2055">2055</option>
                                    <option value="2056">2056</option>
                                    <option value="2057">2057</option>
                                    <option value="2058">2058</option>
                                    <option value="2059">2059</option>
                                    <option value="2060">2060</option>
                                    <option value="2061">2061</option>
                                    <option value="2062">2062</option>
                                    <option value="2063">2063</option>
                                    <option value="2064">2064</option>
                                    <option value="2065">2065</option>
                                    <option value="2066">2066</option>
                                    <option value="2067">2067</option>
                                    <option value="2068">2068</option>
                                    <option value="2069">2069</option>
                                    <option value="2070">2070</option>
                                    <option value="2071">2071</option>
                                    <option value="2072">2072</option>
                                    <option value="2073">2073</option>
                                    <option value="2074">2074</option>
                                    <option value="2075">2075</option>
                                    <option value="2076">2076</option>
                                    <option value="2077">2077</option>
                                    <option value="2078">2078</option>
                                    <option value="2079">2079</option>
                                    <option value="2080">2080</option>
                                    <option value="2081">2081</option>
                                    <option value="2082">2082</option>
                                    <option value="2083">2083</option>
                                    <option value="2084">2084</option>
                                    <option value="2085">2085</option>
                                    <option value="2086">2086</option>
                                    <option value="2087">2087</option>
                                    <option value="2088">2088</option>
                                    <option value="2089">2089</option>
                                    <option value="2090">2090</option>
                                    <option value="2091">2091</option>
                                    <option value="2092">2092</option>
                                    <option value="2093">2093</option>
                                    <option value="2094">2094</option>
                                    <option value="2095">2095</option>
                                    <option value="2096">2096</option>
                                    <option value="2097">2097</option>
                                    <option value="2098">2098</option>
                                    <option value="2099">2099</option>
                                </select>
                                and 
                                <select id="cronYearRangeEnd" style="width:80px;">
                                    <option value="2016">2016</option>
                                    <option value="2017">2017</option>
                                    <option value="2018">2018</option>
                                    <option value="2019">2019</option>
                                    <option value="2020">2020</option>
                                    <option value="2021">2021</option>
                                    <option value="2022">2022</option>
                                    <option value="2023">2023</option>
                                    <option value="2024">2024</option>
                                    <option value="2025">2025</option>
                                    <option value="2026">2026</option>
                                    <option value="2027">2027</option>
                                    <option value="2028">2028</option>
                                    <option value="2029">2029</option>
                                    <option value="2030">2030</option>
                                    <option value="2031">2031</option>
                                    <option value="2032">2032</option>
                                    <option value="2033">2033</option>
                                    <option value="2034">2034</option>
                                    <option value="2035">2035</option>
                                    <option value="2036">2036</option>
                                    <option value="2037">2037</option>
                                    <option value="2038">2038</option>
                                    <option value="2039">2039</option>
                                    <option value="2040">2040</option>
                                    <option value="2041">2041</option>
                                    <option value="2042">2042</option>
                                    <option value="2043">2043</option>
                                    <option value="2044">2044</option>
                                    <option value="2045">2045</option>
                                    <option value="2046">2046</option>
                                    <option value="2047">2047</option>
                                    <option value="2048">2048</option>
                                    <option value="2049">2049</option>
                                    <option value="2050">2050</option>
                                    <option value="2051">2051</option>
                                    <option value="2052">2052</option>
                                    <option value="2053">2053</option>
                                    <option value="2054">2054</option>
                                    <option value="2055">2055</option>
                                    <option value="2056">2056</option>
                                    <option value="2057">2057</option>
                                    <option value="2058">2058</option>
                                    <option value="2059">2059</option>
                                    <option value="2060">2060</option>
                                    <option value="2061">2061</option>
                                    <option value="2062">2062</option>
                                    <option value="2063">2063</option>
                                    <option value="2064">2064</option>
                                    <option value="2065">2065</option>
                                    <option value="2066">2066</option>
                                    <option value="2067">2067</option>
                                    <option value="2068">2068</option>
                                    <option value="2069">2069</option>
                                    <option value="2070">2070</option>
                                    <option value="2071">2071</option>
                                    <option value="2072">2072</option>
                                    <option value="2073">2073</option>
                                    <option value="2074">2074</option>
                                    <option value="2075">2075</option>
                                    <option value="2076">2076</option>
                                    <option value="2077">2077</option>
                                    <option value="2078">2078</option>
                                    <option value="2079">2079</option>
                                    <option value="2080">2080</option>
                                    <option value="2081">2081</option>
                                    <option value="2082">2082</option>
                                    <option value="2083">2083</option>
                                    <option value="2084">2084</option>
                                    <option value="2085">2085</option>
                                    <option value="2086">2086</option>
                                    <option value="2087">2087</option>
                                    <option value="2088">2088</option>
                                    <option value="2089">2089</option>
                                    <option value="2090">2090</option>
                                    <option value="2091">2091</option>
                                    <option value="2092">2092</option>
                                    <option value="2093">2093</option>
                                    <option value="2094">2094</option>
                                    <option value="2095">2095</option>
                                    <option value="2096">2096</option>
                                    <option value="2097">2097</option>
                                    <option value="2098">2098</option>
                                    <option value="2099">2099</option>
                                </select>
                            </label>
                            </div>
                        </div>
                    </div>
                </div>
                </div>
            </div>
     </div>

               <div class="row">
                              <div class="col-12">
                                             <div class="float-right">
                                                            <div class="btn-group dropup custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''${formId}'', this);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls"  id="actionDiv">
                              <ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="typeOfAction(''${formId}'', this);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="typeOfAction(''${formId}'', this);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                        </ul>
                    </div>  
                </div>
                                                            <span onclick="backToPreviousPage();">
                                                                           <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
                                                            </span> 
                                             </div>
                              </div>
               </div>
</div>
<script>
               let formId = "${formId}";
               contextPath = "${contextPath}";
               let isEdit = 0;
               let failedNotificationParam="";
     let autocomplete;
    let obj;
   
let savedHeaderJson = "";
let savedRequestParamJson = "";

  
  $(function(){
      $(''#crontabs'').tabs();
        $(''#crontabs input, #crontabs select'').change(FF.cron);
        FF.cron();
    // setting value on edit.
      <#if (resultSet)??>
               <#list resultSet as resultSetList>
          let isActive = ''${resultSetList?api.get("is_active")}'';
            if(isActive == 1){
                $("#isActiveCheckbox").attr("checked", "checked");
            }
            let jsonStr = ''${resultSetList?api.get("header_json")!""}'';
               if(jsonStr !== ''''){
               savedHeaderJson = JSON.parse(jsonStr);
            }
            
            let jsonStr1 = ''${resultSetList?api.get("request_param_json")!""}'';
               if(jsonStr1 !== ''''){
               savedRequestParamJson = JSON.parse(jsonStr1);
            }
            obj = new Object();
            obj["jws_dynamic_rest_id"] = ''${resultSetList?api.get("jws_dynamic_rest_id")!""}'';
            obj["jws_dynamic_rest_url"] = ''${resultSetList?api.get("rest_api_url")!""}'';

            let failedNot = ''${resultSetList?api.get("failed_notification_params")!""}'';
            if(failedNot != '''') {
                let failedNotJson = JSON.parse(failedNot);
                if(failedNotJson !== ''''){
                    Object.keys(failedNotJson).forEach(function(key) {
                        if(key=="responseCodeNot200") {
                            $("#chkResponseCode").prop(''checked'', true);
                        } 
                        
                        if(key == "conjuctionCode") {
                            $("#lstConjunction").prop(''selectedIndex'', failedNotJson["conjuctionCode"]);
                            $("#lstConjunction").attr(''disabled'',false);
                        }
                        if(key == "regexCondition") {
                            $("#lstConditions").prop(''selectedIndex'', failedNotJson["regexCondition"]);
                            $("#lstConditions").attr(''disabled'',false);
                        }
                        if(key == "regex") {
                            $("#txtRegex").val(failedNotJson[key]);
                            $("#txtRegex").attr(''disabled'',false);
                        } 
                        if(key == "recepients") {
                            $("#txtRecipients").val(failedNotJson[key]);
                            $("#txtRecipients").attr(''disabled'',false);
                        }
                    });           
                }
            }

               </#list>
      </#if>
          
      <#if (resultSet)?? && resultSet?has_content>
               isEdit = 1;
      <#else>
        let schedulerId = uuidv4();
        $("#schedulerid").val(schedulerId);
        $("#isActiveCheckbox").attr("checked", "checked");
      </#if>

       autocomplete = $(''#algoAutocomplete'').autocomplete({
            autocompleteId: "jq-restapi-autocomplete",
            pageSize: 10,//Default page size is 10
            prefetch : true,
            render: function(item) {
                var renderStr ='''';
                if(item.emptyMsg == undefined || item.emptyMsg === '''') {
                    renderStr = ''<p>''+item.jws_dynamic_rest_url+''</p>'';
                } else {
                    renderStr = item.emptyMsg;    
                }                                
                return renderStr;
            },
            extractText: function(item) {
                return item.jws_dynamic_rest_url;
            },
           select: function(item) {
                autocomplete.setSelectedObject(item);
            },     
            resetAutocomplete: function(){ 
                //This function will be executed onblur or when user click on clear text button
                //Code to reset dependent JavaScript variables, input fields etc.
            }, 
        });
    
    autocomplete.setSelectedObject(obj);
createHeaderResponseTable();
createRequestParamTable();
               savedAction(formId, isEdit);
               hideShowActionButtons();
  });
  
  function updateReceipents(){
    let isToEnable = false;
    
    isToEnable = $("#chkResponseCode").is(":checked");
    let conjunctionIdx = $("#lstConjunction").prop(''selectedIndex'');
    if(isToEnable === false && conjunctionIdx > 0){
        isToEnable = true;
    }
  
    if(isToEnable){
        $("#txtRecipients").removeAttr(''disabled'');
        if(conjunctionIdx > 0){
        $("#txtRegex").removeAttr(''disabled'');
        $("#lstConditions").removeAttr(''disabled'');
        }    
    }else{
        $("#txtRecipients").attr(''disabled'',''disabled'');
        $("#txtRecipients").val("");
        $("#txtRegex").attr(''disabled'',''disabled'');
        $("#lstConditions").attr(''disabled'',''disabled'');
    }
}

  function addRow(key, value){
                              let context = this;             
                              let trId = uuidv4();
                              $(''#headerTable tr:last'').after(''<tr id=''+trId+''><td><input class="key" type="text"></td><td><input class="value" type="text"></td><td class="centercls"><span id="btn_''+trId+''" onclick="deleteRow(this)" class="cusrorhandcls"><i class="fa fa-minus-circle" aria-hidden="true"></i></span></td></tr>'');
                              if(key !== undefined && value !== undefined){
                                             $(''#headerTable tr:last'').find(''td input.key'').val(key);
                                             $(''#headerTable tr:last'').find(''td input.value'').val(value);
                              }
               }
    
  function addRowForRequestParam(key, value){
                              let context = this;             
                              let trId = uuidv4();
                              $(''#requestParamTable tr:last'').after(''<tr id=''+trId+''><td><input class="key" type="text"></td><td><input class="value" type="text"></td><td class="centercls"><span class="cusrorhandcls" id="btn_''+trId+''" onclick="deleteRow(this)" class="cusrorhandcls"><i class="fa fa-minus-circle" aria-hidden="true"></i></span></td></tr>'');
                              if(key !== undefined && value !== undefined){
                                             $(''#requestParamTable tr:last'').find(''td input.key'').val(key);
                                             $(''#requestParamTable tr:last'').find(''td input.value'').val(value);
                              }
               }
    function deleteRow(rowElement){
                              $("#deleteHeader").html("Are you sure you want to delete?");
                              $("#deleteHeader").dialog({
                              bgiframe              : true,
                              autoOpen            : true, 
                              modal                   : true,
                              closeOnEscape : true,
                              draggable            : true,
                              resizable              : false,
                              title                       : "Delete",
                              position: {
                                             my: "center", at: "center"
                              },
                              buttons : [{
                                                            text                       :"Cancel",
                                                            click       : function() { 
                                                                           $(this).dialog(''close'');
                                                            },
                                             },
                                             {
                                                            text                       : "Delete",
                                                            click       : function(){
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
    
               function createRequestParamTable(){
                              if(savedRequestParamJson !== ''''){
                                             Object.keys(savedRequestParamJson).forEach(function(key) {
                                                            addRowForRequestParam(key, savedRequestParamJson[key]);
                                             });           
                              }
               }
    
               function headerJson(){
                              let headerJson = {};
                              $("#headerTable").find(''tr'').each(function() {
                                             let key = $(this).find("input.key").val();
            if(key == "") {
                showMessage("Key is empty in header param", "error");
                requestParamJson =  -1;
                return requestParamJson;
            }
                                             if(key !== undefined){
                                                            let value = $(this).find("input.value").val();
                if(value == undefined || value == "" || value == null) {
                    showMessage("Value is null or empty in header param", "error");
                    requestParamJson = -1;
                    return requestParamJson;
                }
                                                            headerJson[key] = value;
                                             }
                              });
                              return headerJson;
               }
               
               function requestParamJson(){
                              let requestParamJson = {};
                              $("#requestParamTable").find(''tr'').each(function() {
                                             let key = $(this).find("input.key").val();
            if(key == "") {
                showMessage("Key is empty in request param", "error");
                requestParamJson =  -1;
                return requestParamJson;
            }
                                             if(key !== undefined){
                                                            let value = $(this).find("input.value").val();
                if(value == undefined || value == "" || value == null) {
                    showMessage("Value is null or empty in request param", "error");
                    requestParamJson = -1;
                    return requestParamJson;
                }
                                                            requestParamJson[key] = value;
                                             }
                              });
                              return requestParamJson;
               }
               
               //Add logic to save form data
               function saveData(){
        let context = this;     
        if(autocomplete.getSelectedObject()["jws_dynamic_rest_id"] == null || autocomplete.getSelectedObject()["jws_dynamic_rest_id"] == undefined) {
            showMessage("All fields are mandatory", "INFO");
                                             return false;
        } else {
            $("#restapiurl").val(autocomplete.getSelectedObject()["jws_dynamic_rest_id"]);
        }
        

        failedNotificationParam = {};
        let isToEnable = $("#chkResponseCode").is(":checked");
        let conjunctionIdx = $("#lstConjunction").prop(''selectedIndex'');
        if(isToEnable === false && conjunctionIdx > 0){
            isToEnable = true;
        }
        if(isToEnable){
            if($("#chkResponseCode").is(":checked")) {
                failedNotificationParam["responseCodeNot200"] = "true";
            }
            if($("#lstConjunction").prop(''selectedIndex'')>0) {
                failedNotificationParam["conjuctionCode"] = $("#lstConjunction").prop(''selectedIndex'');
                failedNotificationParam["regexCondition"] = $("#lstConditions").prop(''selectedIndex'');
                failedNotificationParam["regex"] = $("#txtRegex").val();
            }
            failedNotificationParam["recepients"] = $("#txtRecipients").val();
        }

                              let isDataSaved = false;
        let isActive = $("#isActiveCheckbox").prop("checked");
        if(isActive == true){
           $("#isactive").val(1);
        }else{
            $("#isactive").val(0); 
        }
                              let formData = validateData();     
                              if(formData === undefined){
                                             showMessage("All fields are mandatory", "error");
                                             return false;
                              }
                              $("#errorMessage").hide();
                              formData.push({"name": "formId", "value": formId, "valueType": "varchar"});
                              formData.push({"name": "isEdit", "value": (isEdit + ""), "valueType": "int"});

        let headerParam = context.headerJson();
        if(headerParam == -1) {
            return false;
        }
        let headerJson = JSON.stringify(headerParam);
        formData.push({"name": "headerJson", "value": (headerJson + ""), "valueType": "varchar"});

        let reqParam = context.requestParamJson();
        if(reqParam == -1) {
            return false;
        }
        let requestParamJson = JSON.stringify(reqParam);
        formData.push({"name": "requestParamJson", "value": (requestParamJson + ""), "valueType": "varchar"});
        formData.push({"name": "failedNotificationParam", "value": (JSON.stringify(failedNotificationParam) + ""), "valueType": "varchar"});
                              $.ajax({
                                type : "POST",
                                async: false,
                                url : contextPath+"/cf/psdf",
                                data : {
                                             formData: JSON.stringify(formData),
                                             formId: formId
                                },
          success : function(data) {
                                             isDataSaved = true;
            startScheduler();
                                },
                     error : function(xhr, error){
                                             showMessage("Error occurred while saving", "error");
                     },
                              });
                              return isDataSaved;
               }
               
               //Basic validation for form fields
    function validateData(){
                              let serializedForm = $("#addEditForm").serializeArray();
                              for(let iCounter =0, length = serializedForm.length;iCounter<length;iCounter++){
                                             let fieldValue = $.trim(serializedForm[iCounter].value);
                                             let fieldName = $.trim(serializedForm[iCounter].name);
                                             let isFieldVisible = $("#"+fieldName).is(":visible");
            if(fieldValue !== ""){
               serializedForm[iCounter].value = fieldValue;
            }else if(isFieldVisible === true){
               return undefined;
            }  
                              }
                              serializedForm = serializedForm.formatSerializedArray();
                              return serializedForm;
    }
    
               //Code go back to previous page
               function backToPreviousPage() {
                                             location.href = contextPath+"/cf/sl";
               }

    function startScheduler() {
        
            $.ajax({
            type : "POST",
            async: false,
            url : contextPath+"/cf/schedule",
            data : {
                cronscheduler:$("#cronscheduler").val(),
                schedulerId:$("#schedulerid").val(),
                restapiurl:$("#restapiurl").val()
            },
            success : function(data) {
                showMessage("Scheduled successfully", "success");
            },
            error : function(xhr, error){
                showMessage("Error occurred while scheduling", "error");
            },
            });      
    }

    // Content start for cron helper
function expandCollapse(a_element){
    if($("#crontabs").is(":visible")){
        $(a_element).find("span").text("&#9660;");
    }else{
        $(a_element).find("span").text("&#9650;");
    }   
    $("#crontabs").slideToggle();
}

function copyToClipboard() {
    var $temp = $("<input>");
    $("body").append($temp);
    $temp.val($(".cronResult").text()).select();
    document.execCommand("copy");
    $temp.remove();
    showMessage("Cron expression copied...", "info")
}

var l_seconds, l_minutes, l_hours, l_dom, l_month, l_dow, l_year;

var FF = {
               cron: function () {
                              $(this).parents(''.cron-option'').children(''input[type="radio"]'').attr("checked", "checked");
                              FF.seconds();
                              FF.minutes();
                              FF.hours();
                              FF.day();
                              FF.month();
                              FF.year();                            
                              $(''.cronResult'').text(l_seconds + '' '' + l_minutes + '' '' + l_hours + '' '' + l_dom + '' '' + l_month + '' '' + l_dow + '' '' + l_year);
               },
               seconds: function () {
                              var seconds = '''';
                              if ($(''#cronEverySecond:checked'').length) {
                                             seconds = ''*'';
                              } else if ($(''#cronSecondIncrement:checked'').length) {
                                             seconds = $(''#cronSecondIncrementStart'').val();
                                             seconds += ''/'';
                                             seconds += $(''#cronSecondIncrementIncrement'').val();
                              } else if ($(''#cronSecondSpecific:checked'').length) {
                                             $(''[name="cronSecondSpecificSpecific"]:checked'').each(function (i, chck) {
                                                            seconds += $(chck).val();
                                                            seconds += '','';
                                             });
                                             if (seconds === '''') {
                                                            seconds = ''0'';
                                             } else {
                                                            seconds = seconds.slice(0, -1);
                                             }
                              } else {
                                             seconds = $(''#cronSecondRangeStart'').val();
                                             seconds += ''-'';
                                             seconds += $(''#cronSecondRangeEnd'').val();
                              }
                              l_seconds = seconds;
               },
               minutes: function () {
                              var minutes = '''';
                              if ($(''#cronEveryMinute:checked'').length) {
                                             minutes = ''*'';
                              } else if ($(''#cronMinuteIncrement:checked'').length) {
                                             minutes = $(''#cronMinuteIncrementStart'').val();
                                             minutes += ''/'';
                                             minutes += $(''#cronMinuteIncrementIncrement'').val();
                              } else if ($(''#cronMinuteSpecific:checked'').length) {
                                             $(''[name="cronMinuteSpecificSpecific"]:checked'').each(function (i, chck) {
                                                            minutes += $(chck).val();
                                                            minutes += '','';
                                             });
                                             if (minutes === '''') {
                                                            minutes = ''0'';
                                             } else {
                                                            minutes = minutes.slice(0, -1);
                                             }
                              } else {
                                             minutes = $(''#cronMinuteRangeStart'').val();
                                             minutes += ''-'';
                                             minutes += $(''#cronMinuteRangeEnd'').val();
                              }
                              l_minutes = minutes;
               },
               hours: function () {
                              var hours = '''';
                              if ($(''#cronEveryHour:checked'').length) {
                                             hours = ''*'';
                              } else if ($(''#cronHourIncrement:checked'').length) {
                                             hours = $(''#cronHourIncrementStart'').val();
                                             hours += ''/'';
                                             hours += $(''#cronHourIncrementIncrement'').val();
                              } else if ($(''#cronHourSpecific:checked'').length) {
                                             $(''[name="cronHourSpecificSpecific"]:checked'').each(function (i, chck) {
                                                            hours += $(chck).val();
                                                            hours += '','';
                                             });
                                             if (hours === '''') {
                                                            hours = ''0'';
                                             } else {
                                                            hours = hours.slice(0, -1);
                                             }
                              } else {
                                             hours = $(''#cronHourRangeStart'').val();
                                             hours += ''-'';
                                             hours += $(''#cronHourRangeEnd'').val();
                              }
                              l_hours = hours;
              },
               day: function () {
                              var dow = '''';
                              var dom = '''';

                              if ($(''#cronEveryDay:checked'').length) {
                                             dow = ''*'';
                                             dom = ''?'';
                              } else if ($(''#cronDowIncrement:checked'').length) {
                                             dow = $(''#cronDowIncrementStart'').val();
                                             dow += ''/'';
                                             dow += $(''#cronDowIncrementIncrement'').val();
                                             dom = ''?'';
                              } else if ($(''#cronDomIncrement:checked'').length) {
                                             dom = $(''#cronDomIncrementStart'').val();
                                             dom += ''/'';
                                             dom += $(''#cronDomIncrementIncrement'').val();
                                             dow = ''?'';
                              } else if ($(''#cronDowSpecific:checked'').length) {
                                             dom = ''?'';
                                             $(''[name="cronDowSpecificSpecific"]:checked'').each(function (i, chck) {
                                                            dow += $(chck).val();
                                                            dow += '','';
                                             });
                                             if (dow === '''') {
                                                            dow = ''SUN'';
                                             } else {
                                                            dow = dow.slice(0, -1);
                                             }
                              } else if ($(''#cronDomSpecific:checked'').length) {
                                             dow = ''?'';
                                             $(''[name="cronDomSpecificSpecific"]:checked'').each(function (i, chck) {
                                                            dom += $(chck).val();
                                                            dom += '','';
                                             });
                                             if (dom === '''') {
                                                            dom = ''1'';
                                             } else {
                                                            dom = dom.slice(0, -1);
                                             }
                              } else if ($(''#cronLastDayOfMonth:checked'').length) {
                                             dow = ''?'';
                                             dom = ''L'';
                              } else if ($(''#cronLastWeekdayOfMonth:checked'').length) {
                                             dow = ''?'';
                                             dom = ''LW'';
                              } else if ($(''#cronLastSpecificDom:checked'').length) {
                                             dom = ''?'';
                                             dow = $(''#cronLastSpecificDomDay'').val();
                                             dow += ''L'';
                              } else if ($(''#cronDaysBeforeEom:checked'').length) {
                                             dow = ''?'';
                                             dom = ''L-'';
                                             dom += $(''#cronDaysBeforeEomMinus'').val();
                              } else if ($(''#cronDaysNearestWeekdayEom:checked'').length) {
                                             dow = ''?'';
                                             dom = $(''#cronDaysNearestWeekday'').val();
                                             dom += ''W'';
                              } else if ($(''#cronNthDay:checked'').length) {
                                             dom = ''?'';
                                             dow = $(''#cronNthDayDay'').val();
                                             dow += ''#'';
                                             dow += $(''#cronNthDayNth'').val();;
                              }
                              l_dom = dom;
                              l_dow = dow;
               },
               month: function () {
                              var month = '''';
                              if ($(''#cronEveryMonth:checked'').length) {
                                             month = ''*'';
                              } else if ($(''#cronMonthIncrement:checked'').length) {
                                             month = $(''#cronMonthIncrementStart'').val();
                                             month += ''/'';
                                             month += $(''#cronMonthIncrementIncrement'').val();
                              } else if ($(''#cronMonthSpecific:checked'').length) {
                                             $(''[name="cronMonthSpecificSpecific"]:checked'').each(function (i, chck) {
                                                            month += $(chck).val();
                                                            month += '','';
                                             });
                                             if (month === '''') {
                                                            month = ''1'';
                                             } else {
                                                            month = month.slice(0, -1);
                                             }
                              } else {
                                             month = $(''#cronMonthRangeStart'').val();
                                             month += ''-'';
                                             month += $(''#cronMonthRangeEnd'').val();
                              }
                              l_month = month;
               },
               year: function () {
                              var year = '''';
                              if ($(''#cronEveryYear:checked'').length) {
                                             year = ''*'';
                              } else if ($(''#cronYearIncrement:checked'').length) {
                                             year = $(''#cronYearIncrementStart'').val();
                                             year += ''/'';
                                             year += $(''#cronYearIncrementIncrement'').val();
                              } else if ($(''#cronYearSpecific:checked'').length) {
                                             $(''[name="cronYearSpecificSpecific"]:checked'').each(function (i, chck) {
                                                            year += $(chck).val();
                                                            year += '','';
                                             });
                                             if (year === '''') {
                                                            year = ''2016'';
                                             } else {
                                                            year = year.slice(0, -1);
                                             }
                              } else {
                                             year = $(''#cronYearRangeStart'').val();
                                             year += ''-'';
                                             year += $(''#cronYearRangeEnd'').val();
                              }
                              l_year = year;
               }
};            
               
</script>
', 'admin', NOW(), NULL, NULL, 2, NOW());


REPLACE INTO jq_dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('1cae955b-251c-41a4-8a9a-2d43221134e4', 'f44ac7ab-c61e-4df3-b40f-190262f79a39', '<#if isEdit == 1>
    UPDATE jq_job_scheduler SET scheduler_name = :schedulername,jws_dynamic_rest_id = :restapiurl, header_json= :headerJson, request_param_json = :requestParamJson,cron_scheduler = :cronscheduler,is_active = :isactive, modified_by = :loggedInUserId, failed_notification_params=:failedNotificationParam WHERE scheduler_id = :schedulerid
    <#else>
    INSERT INTO jq_job_scheduler (scheduler_id,scheduler_name,jws_dynamic_rest_id,cron_scheduler,is_active,modified_by, header_json, request_param_json, failed_notification_params) VALUES (:schedulerid,:schedulername,:restapiurl,:cronscheduler,:isactive,:loggedInUserId, :headerJson, :requestParamJson, :failedNotificationParam)
    </#if>', 1, NULL);


replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('9501f80e-84dc-401f-a577-4752d8d534a7', 'f44ac7ab-c61e-4df3-b40f-190262f79a39', 'jq-scheduler-form', '30a0ff61-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('9501f80e-84dc-401f-a577-4752d8d534a6', 'f44ac7ab-c61e-4df3-b40f-190262f79a39', 'jq-scheduler-form', '30a0ff61-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('9501f80e-84dc-401f-a577-4752d8d534a5', 'f44ac7ab-c61e-4df3-b40f-190262f79a39', 'jq-scheduler-form', '30a0ff61-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);


replace into jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('b64fbb45-b36c-4a53-ad37-93ae91307600', 'getExecutionHistory', 1, 'getExecutionHistory', 'Method to get scheduler execution history', 1, 7, '<#assign l_offset = offset?number>

<#list reminderResult as reminder>
    <div  class="sheduler_list">
        <div class="sheduler_timedate">
        <span >${reminder.response_time}</span>
        <span> Response Code: <label>${reminder.response_code}</label> </span>
        </div>

        <div class="sheduler_content">
            <textarea readonly style="width:100%;height:40px">${reminder.response_body}</textarea>
        </div>
    </div>
<br/>
</#list>

<#if ((l_offset + 1) * 10 < responseCount[0].responses?number)>
<center><button class="btn btn-primary" onclick="loadExecutionHistory(${l_offset + 1},''${schedulerID}'')">Load More</button></center>
</#if>

', 2, 0, 2, 'admin', NOW(), NOW());

replace into jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(896, 'b64fbb45-b36c-4a53-ad37-93ae91307600', 'reminderResult', '<#assign l_offset = offset?number>
SELECT  * FROM `jq_job_scheduler_log` WHERE scheduler_id=:schedulerID
ORDER BY
	response_time DESC

limit ${l_offset * 10}, 10', 1, 1);

replace into jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(897, 'b64fbb45-b36c-4a53-ad37-93ae91307600', 'responseCount', 'SELECT COUNT(`jjsl`.`response_time`) AS `responses` FROM jq_job_scheduler_log AS jjsl WHERE jjsl.`scheduler_id` = :schedulerID', 2, 1);

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('98397afb-e3ec-479f-8491-3994d6c922d1', 'b64fbb45-b36c-4a53-ad37-93ae91307600', 'getExecutionHistory', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('98397afb-e3ec-479f-8491-3994d6c922d2', 'b64fbb45-b36c-4a53-ad37-93ae91307600', 'getExecutionHistory', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('98397afb-e3ec-479f-8491-3994d6c922d3', 'b64fbb45-b36c-4a53-ad37-93ae91307600', 'getExecutionHistory', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);


REPLACE INTO jq_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('b2aadd32-e975-11eb-9a03-0242ac130003','system', 'system', 'scheduler-url', '7fe53154-e975-11eb-9a03-0242ac130003', 0, NOW(), 'admin', 1.43, 'Scheduler property unique indentifier');



SET FOREIGN_KEY_CHECKS=1;

