SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'dashboard-listing', '<head>
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
		<h2 class="title-cls-name float-left">${messageSource.getMessage("jws.dashboardMaster")}</h2> 
		<div class="float-right">
			<input class="btn btn-primary" name="createDashboard" value="${messageSource.getMessage(''jws.createNewDashboard'')}" type="button" onclick="submitForm(this)">
			<input class="btn btn-primary" name="manageDashlets" value="${messageSource.getMessage(''jws.manageDashblet'')}" type="button" onclick="openDashlets(this)">
			<span onclick="backToWelcomePage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="dashboardMasterGrid"></div>

	<div id="snackbar"></div>
</div>

<form action="${(contextPath)!''''}/cf/aedb" method="POST" id="formDBRedirect">
	<input type="hidden" id="dashboardId" name="dashboard-id">
</form>
<form action="${(contextPath)!''''}/cf/dls" method="POST" id="formDLSRedirect" target="_blank">
	<input type="hidden" id="dashboardIdView" name="dashboardId">
</form>

<script>
	contextPath = "${(contextPath)!''''}";
	$(function () {
		let colM = [
			{ title: "", dataIndx: "dashboardId", hidden: true},
			{ title: "${messageSource.getMessage(''jws.dashboardName'')}", width: 130, dataIndx: "dashboardName", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "${messageSource.getMessage(''jws.dashboardType'')}", width: 130, dataIndx: "dashboardType" , align: "left", align: "left", halign: "center", 
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}},
			{ title: "${messageSource.getMessage(''jws.createdBy'')}", width: 100, dataIndx: "createdBy", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}},
			{ title: "${messageSource.getMessage(''jws.createdDate'')}", width: 100, dataIndx: "createdDate" , align: "left", halign: "center" },
			{ title: "${messageSource.getMessage(''jws.lastUpdatedDate'')}", width: 100, dataIndx: "lastUpdatedDate" , align: "left", halign: "center" },
			{ title: "${messageSource.getMessage(''jws.contextDescription'')}", width: 100, dataIndx: "contextDescription", align: "left", align: "left", halign: "center", 
				filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
			{ title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editDashboard}
		];
	
		let grid = $("#dashboardMasterGrid").grid({
	      gridId: "dashboardMasterListingGrid",
	      colModel: colM
	  	});
	
	});
	
	function editDashboard(uiObject) {
		const dashboardId = uiObject.rowData.dashboardId;
		return ''<span id="''+dashboardId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title="${messageSource.getMessage("jws.editDashboard")}"></i></span>&nbsp;&nbsp;<span id="''+dashboardId+''" onclick="viewDashlets(this)" class= "grid_action_icons"><i class="fa fa-eye"  title="${messageSource.getMessage("jws.viewDashboard")}"></i></span>''.toString();
	}
	
	function submitForm(element) {
		$("#dashboardId").val(element.id);
		$("#formDBRedirect").submit();
	}
	function viewDashlets(element){
		$("#dashboardIdView").val(element.id);
		$("#formDLSRedirect").submit();
	}
	
	function openDashlets(element) {
		location.href = contextPath+"/cf/dlm";
	}
	
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
</script>
', 'admin', 'admin', NOW());






REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'dashboard-manage-details', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />

</head> 

<div class="container"> 

	<div class="topband">
		<h2 class="title-cls-name float-left">${messageSource.getMessage("jws.addEditDashboard")}</h2> 
		<div class="float-right">
			<span onclick="addEditDashboardFn.backToDashboardListingPage();">
  				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
  		 	</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>

	<div id="formDiv">
		<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	 
	 	<div class="row">
			<input type="hidden" id = "dashboardId" name = "dashboardId" value = "${(dashboard?api.getDashboardId())!''''}">
			<div class="col-12">
	     		<div class="col-inner-form full-form-fields"> 
		  			<label  class="pull-left label-name-cls full-width"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.dashlets")}</label>
		  			<div id = "associatedDashlets"></div>
		  			<div class="clearfix"></div>
				</div>
			</div>	

			<div class="col-3" id="dashboardTypeDiv">
				<div class="col-inner-form full-form-fields">
					<label for="flammableState" style="white-space:nowrap"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.dashboardName")}</label>
					<input type="text"  id = "dashboardName" name = "dashboardName" value = "${(dashboard?api.getDashboardName())!''''}" maxlength="100" class="form-control">
				</div>
			</div>
	 
			<div class="col-3" id="dashboardTypeDiv">
				<div class="col-inner-form full-form-fields">
					<label for="flammableState" style="white-space:nowrap"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.dashboardType")}</label>
					<select id="dashboardTypeId" name="dashboardType" class="form-control">
						<option value="">SELECT</option>
							<security:authorize access="hasAnyRole(''''ROLE_ADMIN'''')">
								<option value="system">${messageSource.getMessage("jws.system")}</option>
							</security:authorize>
						<option value="personal">${messageSource.getMessage("jws.personal")}</option>
					</select>
				</div>
			</div>
							
			<div class="col-3" id="contextIdDiv" >
				<div class="col-inner-form full-form-fields">
					<label for="flammableState" style="white-space:nowrap"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.contextName")}</label>
					<select id="contextId" name="contextId" onchange="addEditDashboardFn.populateDashlets();" class="form-control">
						<option value="">Select</option>
						<#if (contextDetails)??>
							<#list contextDetails as contextId, contextName>
								<#if contextId??>
									<#if (dashboard?api.getContextId())?? && contextId == dashboard?api.getContextId()>
										<option value="${contextId}" selected>${contextName}</option>
									<#elseif contextId??>
										<option value="${contextId}">${contextName}</option>
									</#if>
								</#if>
							</#list>
						</#if>
					</select>								
				</div>
			</div>
							
		    <div class="col-3 margin-t-20">
				<div id="draagableDiv">
					<input class="pull-left comman_checkbox form-control" type="checkbox" id="isDraggableId"  name="isDraggable" value="${(dashboard?api.getIsDraggable())!''''}" ${(dashboard?api.getIsDraggable()==1)?then("checked" , "")} onclick="addEditDashboardFn.changeDraggableValue();">
					<label class="pull-left" for="isDraggableId">${messageSource.getMessage("jws.draggableDashboard")}</label>
				</div>	
				<div class="clearfix"></div>
		    </div>
	
			<div class="col-3"> 			
				<div class="inpugrp pull-left" id="exportableDiv" >
					<input type="checkbox" id="isExportableId" class="pull-left comman_checkbox form-control"  name="isExportable" value="${(dashboard?api.getIsExportable())!''''}" ${(dashboard?api.getIsExportable()==1)?then("checked" , "")} onclick="addEditDashboardFn.changeExportableValue();">
					<label class="pull-left" for="isExportableId">${messageSource.getMessage("jws.exportableDashboard")}</label>
      			</div>
				<div class="clearfix"></div>
    		</div>
		 
    		<div class="clearfix"></div>
     	</div>
	 
	 
		<div class="row">
			<div class="col-12">
				<div id="buttons" class="pull-right">		 
	 				<input id="saveBtn" type="button" class="btn btn-primary" value="${messageSource.getMessage(''jws.save'')}" onclick="addEditDashboardFn.saveDashboard();">	 
	 				<input id="cancelBtn" type="button" class="btn btn-secondary" value="${messageSource.getMessage(''jws.cancel'')}" onclick="addEditDashboardFn.backToDashboardListingPage();">
				</div>
	 		</div>
		</div>	
	
	</div>
	
	<div id="snackbar"></div>
</div>

<script>
	contextPath = "${(contextPath)!''''}";
	const contextId ="${(dashboard?api.getContextId())!''''}";
	const dashboardType = "${(dashboard?api.getDashboardType())!''''}"; 
	const dashboardId = "${(dashboard?api.getDashboardId())!''''}"; 
	let addEditDashboardFn;
	
	$(function() {
		  
	  const addEditDashboard = new AddEditDashboard(contextId, dashboardType, dashboardId);
	  addEditDashboardFn = addEditDashboard.fn;
	  addEditDashboardFn.loadDashboardPage();
	  
	});

</script>
<script src="/webjars/1.0/dashboard/addEditDashboard.js"></script>', 'admin', 'admin',NOW());


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'dashlet-listing', '<head>
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
<script>
  contextPath = "${(contextPath)!''''}";
</script>
</head>
<div class="container">
	<div class="topband">
		<h2 class="title-cls-name float-left">${messageSource.getMessage("jws.dashletMaster")}</h2> 
		<div class="float-right">
			<#if environment == "dev">
				<input id="downloadDashlet" class="btn btn-primary" onclick= "downloadDashlet();" name="downloadDashlet" value="Download Dashlet" type="button">
				<input id="uploadDashlet" class="btn btn-primary" onclick= "uploadDashlet();" name="uploadDashlet" value="Upload Dashlet" type="button">
			</#if>
			<input class="btn btn-primary" name="createDashlet" value="${messageSource.getMessage(''jws.createNewDashlet'')}" type="button" onclick="submitForm(this)">
    		<span onclick="backToDashboarListing();">
    	  		<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
    	 	</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
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
			{ title: "${messageSource.getMessage(''jws.status'')}", width: 160, dataIndx: "status" , align: "left", halign: "center",render: dashletStatus},
			{ title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editDashlet}
		];
	
		var grid = $("#divdDashletMasterGrid").grid({
			gridId: "dashletMasterListingGrid",
			colModel: colM
		});
	});
	function dashletStatus(uiObject){
		const status = uiObject.rowData.status;
		if(status == 1){
			return ''Active'';
		}else{
			return ''Inactive'';
		}
	}

	function editDashlet(uiObject) {
		const dashletId = uiObject.rowData.dashletId;
		<#if environment == "dev">
			return ''<span id="''+dashletId+''"  class= "grid_action_icons"><i class="fa fa-pencil"  title="${messageSource.getMessage("jws.editDashlet")}"></i></span>''.toString();
		<#else>
			return ''<span id="''+dashletId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"  title="${messageSource.getMessage("jws.editDashlet")}"></i></span>''.toString();
		</#if>
	}
	
	function submitForm(element) {
	  $("#dashletId").val(element.id);
	  $("#formDMRedirect").submit();
	}
	
	function backToDashboarListing() {
		location.href = contextPath+"/cf/dbm";
	}
	<#if environment == "dev">
		function downloadDashlet(){
		     $.ajax({
		        url:"/cf/ddl",
		        type:"POST",
		        success:function(data){
		            debugger;
		            alert("Downloaded Successfully");
		        }
		        
		    });
		}
		function uploadDashlet(){
		     $.ajax({
		        url:"/cf/udl",
		        type:"POST",
		        success:function(data){
		            debugger;
		            alert("Uploaded Successfully");
		        }
		        
		    });
		}
	</#if>
</script>', 'admin', 'admin', NOW());


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
(UUID(), 'dashlet-manage-details', '<head>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<link rel="stylesheet" href="/webjars/1.0/dashboard/dashboard.css" />
<script src="/webjars/1.0/monaco/require.js"></script>
<script src="/webjars/1.0/monaco/min/vs/loader.js"></script>


<div class="container">

	<div class="topband">
		<h2 class="title-cls-name float-left">${messageSource.getMessage("jws.addEditDashlet")}</h2> 
		<div class="float-right">
			<span onclick="addEditDashletFn.backToDashletListing();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>

	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	<div class="row">
		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label for="dashletName" ><span class="asteriskmark">*</span>${messageSource.getMessage("jws.dashletName")}</label>									 
				<input type="hidden" id="dashletId" name="dashletId" value="${(dashletVO.dashletId)!''''}"/>
				<input type="text" id="dashletName" name="dashletName" value="${(dashletVO.dashletName)!''''}" maxlength="100" placeholder="Enter Dashlet name" class="form-control"/>									
			</div>
		</div>
						
		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label for="dashletTitle" ><span class="asteriskmark">*</span>${messageSource.getMessage("jws.dashletTitle")}</label>									 
				<input type="text" id="dashletTitle" name="dashletTitle" value="${(dashletVO.dashletTitle)!''''}" maxlength="100" placeholder="Enter Dashlet Title" class="form-control"/>	
			</div>
		</div>
								
								
		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label for="dashletTitle"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.dashletCoordinates")}</label>
				<div class="co-ordinates-block-cover">
					<div class="co-ordinates-block"><input type="text" id="xCoordinate" name="xCoordinate"  class="numberValidator form-control"  value="${(dashletVO.xCoordinate)!''''}"  placeholder="X Coordinate" maxlength="2" /></div>
					<div class="co-ordinates-block"><input type="text" id="yCoordinate" name="yCoordinate" class="numberValidator form-control" value="${(dashletVO.yCoordinate)!''''}"    placeholder="Y Coordinate" maxlength="2" /></div>
				</div>
			</div>
		</div>
							
							
		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label for="dashletTitle"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.dashletDimesions")}</label>
				<div class="co-ordinates-block-cover">	
					<div class="co-ordinates-block"><input type="text" id="width" name="width" class="numberValidator form-control" value="${(dashletVO.width)!''''}" placeholder="Dashlet Width" /></div>
					<div class="co-ordinates-block"><input type="text" id="height" name="height" class="numberValidator form-control" value="${(dashletVO.height)!''''}" placeholder="Dashlet Height" /></div>
				</div>
			</div>
		</div>
							
		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label for="contextType"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.contextType")}</label>
				<select id="contextId" name="contextId" class="form-control">
					<option value="">Select</option>
					<#if (contextDetailsMap)??>
						<#list contextDetailsMap as contextId, contextDescription>
							<#if (dashletVO?api.getContextId())?? && contextId == dashletVO?api.getContextId()>
								<option value="${contextId}" selected>${contextDescription}</option>
							<#else>
								<option value="${contextId}">${contextDescription}</option>
							</#if>
						</#list>
					</#if>
				</select>
			</div>
		</div>
								
								
		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label for="isActiveCheckbox"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.dashletActive")}</label>
				<div class="onoffswitch">
					<input type="hidden" id="isActive" name="isActive" value="${(dashletVO.isActive)!''''}"/>
					<input type="checkbox" name="isActiveCheckbox" class="onoffswitch-checkbox" id="isActiveCheckbox" onchange="" />
					<label class="onoffswitch-label" for="isActiveCheckbox">
						<span class="onoffswitch-inner"></span>
						<span class="onoffswitch-switch"></span>
					</label>
				</div>
			</div>
		</div>
								
		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label  for="contextType">${messageSource.getMessage("jws.dashletHeader")}</label>
				<div class="onoffswitch">
					<input type="hidden" id="showHeader" name="showHeader" value="${(dashletVO.showHeader)!''''}"/>
					<input type="checkbox" name="showHeaderCheckbox" class="onoffswitch-checkbox" id="showHeaderCheckbox" onchange="" />
					<label class="onoffswitch-label" for="showHeaderCheckbox">
						<span class="onoffswitch-inner"></span>
						<span class="onoffswitch-switch"></span>
					</label>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-12">
			<div class="customdashletblock">
				<div class="row">
					<div class="col-12">
						<h3 class="pull-left titlename">${messageSource.getMessage("jws.configurableProperties")} </h3>
						<div class=" float-right">
			   				<input type="button" value="${messageSource.getMessage(''jws.addProperty'')}" class="btn btn-primary" onclick="addEditDashletFn.addDashletProperty();">	    
						</div>
					</div>
				</div>	
   				
   				<div class="row">	    
    				<div class="col-12">
    					<table id="dashletProps" class="defaulttable ">
							<thead class="thead-light">
								<tr>
									<th>${messageSource.getMessage("jws.placeholderName")}</th>
									<th>${messageSource.getMessage("jws.displayName")}</th>
									<th>${messageSource.getMessage("jws.type")}</th>
									<th>${messageSource.getMessage("jws.value")}</th>
									<th>${messageSource.getMessage("jws.defaultValue")}</th>
									<th>${messageSource.getMessage("jws.show")}</th>
					        		<th>${messageSource.getMessage("jws.action")}</th>
								</tr>
							</thead>
							<tbody>
    							<#if (dashletVO?api.getDashletPropertVOList())??>
									<#list dashletVO?api.getDashletPropertVOList() as dashletProperty>
										<tr class="dashlet_property">
											<td id="propertyDetails">
							  					<input type="hidden" name="propertyId" id="${(dashletProperty.propertyId)!''''}" class="form-control" value="${(dashletProperty.propertyId)!''''}"/>
							  					<input type="hidden" name="dashletId" id="${(dashletVO.dashletId)!''''}" class="form-control" value="${(dashletVO.dashletId)!''''}"/>
							  					<input type="text" name="placeholderName" id="placeholderName_${(dashletProperty.sequence)!''''}" class="form-control" value="${(dashletProperty.placeholderName)!''''}"/>
							  				    <input type="hidden" name="sequence" id="sequence_${(dashletProperty.sequence)!''''}" value="${(dashletProperty.sequence)!''''}" class="form-control"/>
							          		</td>
          
											<td>
									        	<input type="text" name="displayName" id="displayName_${(dashletProperty.sequence)!''''}" class="form-control" value="${(dashletProperty.displayName)!''''}"/>
											</td>
          
        									<td>
				  								<select id="componentType" class="form-control" name="componentType" onchange="addEditDashletFn.defaultValueChange()">
				    								<#list componentMap as categoryId, categoryDescription>
														<#if (dashletProperty?api.getType())?? && categoryId == dashletProperty?api.getType()>
															<option value="${categoryId}" selected = "selected" >${categoryDescription}</option>	
               											<#else>
                   											<option value="${categoryId}">${categoryDescription}</option>
               											</#if>
													</#list>
												</select>
											</td>
  			
        									<td>
         										<input type="text" name="value" id="value_${(dashletProperty.sequence)!''''}" class="form-control" value="${(dashletProperty.value)!''''}"/>
											</td>
          
											<td>
												<input type="text" name="defaultValue" id="defaultValue_${(dashletProperty.sequence)!''''}" class="form-control" value="${(dashletProperty.defaultValue)!''''}"/>
				   							</td>
          
          									<td>
								            	<input type="checkbox" name="toDisplay" id="toDisplay_${(dashletProperty.sequence)!''''}" class="form-control" ${(dashletProperty?is_first)?then("checked" , "")}/>
								            </td>
			 
											<td>
												<span id="upArrow_${dashletProperty.sequence}" onclick="addEditDashletFn.moveUpDown(this.id);"   class="tblicon pull-left ${(dashletProperty?is_first)?then("disable_cls" , "")}" ><i class="fa fa-arrow-up" title="${messageSource.getMessage(''jws.moveUp'')}"></i></span>				  
												<span id="downArrow_${dashletProperty.sequence}" onclick="addEditDashletFn.moveUpDown(this.id);"  class="tblicon pull-left ${(dashletProperty?is_last)?then("disable_cls", "" )}" ><i class="fa fa-arrow-down" title="${messageSource.getMessage(''jws.moveDown'')}"></i></span>
												<span id="removeProperty_${dashletProperty.sequence}" onclick="addEditDashletFn.deleteProperty(this.id);" class="tblicon pull-left" ><i class="fa fa-trash-o" title="${messageSource.getMessage(''jws.deleteProperty'')}"></i></span>
											</td>
			
										</tr>
									</#list>
								</#if>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
   
	<div class="row margin-t-b">
		<div class="col-12">
			<h3 class="titlename"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.htmlScript")}</h3>
			<div class="html_script">
				<div class="grp_lblinp">
					<div id="htmlContainer" class="ace-editor-container">
						<div id="htmlEditor" class="ace-editor"></div>
					</div>
				</div>
			</div>
		</div> 
	</div>
	 

	<div class="row margin-t-b">
		<div class="col-12">
			<h3  class="titlename"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.sqlScript")}</h3>
			<div class="html_script">
				<div class="grp_lblinp">
					<div id="sqlContainer" class="ace-editor-container">
						<div id="sqlEditor" class="ace-editor"></div>
					</div>
				</div>
			</div>	
		</div>
	</div>
	 
	<div class="row ">
		<div class="col-12">	
			<div class="pull-right">
				<input type="button" value="${messageSource.getMessage(''jws.save'')}" class="btn btn-primary" onclick="addEditDashletFn.saveDashlet();">
				<input type="button" id="cancelDashlet" value="${messageSource.getMessage(''jws.cancel'')}" class="btn btn-secondary" onclick="addEditDashletFn.backToDashletListing();">
			</div>	
		</div>
	</div>
	
	<textarea id="htmlContent" style="display: none">
	   	${(dashletVO.dashletBody)!""}
	&lt;/textarea&gt;
	 
	<textarea id="sqlContent" style="display: none">
	   	${(dashletVO.dashletQuery)!""}
	&lt;/textarea&gt;
	
	 <div id="deletePropertyConfirm"></div>
	<div id="snackbar"></div>
</div>
<script>
	contextPath = "${(contextPath)!''''}";
	let dashletSQLEditor;
	var dashletHTMLEditor;
	let componentArray = new Array();
  	<#list componentMap as categoyId, categoryDescription>
    	<#if categoyId??>
        var componentObj = new Object();
    	  componentObj.categoryId = "${categoyId}";
    	  componentObj.categoryDescription = "${categoryDescription}";
    	  componentArray.push(componentObj);
      </#if>
  	</#list>
	let dashletPropertiesCount = parseInt("${(dashletVO?api.getDashletPropertVOList()?size)!''0''}");
  	let addEditDashletFn;
	$(function() {
	 	const addEditDashlet = new AddEditDashlet(dashletPropertiesCount, componentArray, dashletSQLEditor, dashletHTMLEditor);
		addEditDashletFn = addEditDashlet.fn;
		addEditDashletFn.loadAddEditDashletPage();
	});

</script>
<script src="/webjars/1.0/dashlet/addEditDashlet.js"></script>', 'admin', 'admin', NOW());
  
REPLACE INTO dynamic_form(form_id,form_name,form_description,form_select_query,form_body,created_by,created_date,form_select_checksum,form_body_checksum) VALUES ('e848b04c-f19b-11ea-9304-f48e38ab9348','notification','notification add/edit','select * from generic_user_notification where notification_id="${(primaryId)!''''}"','<head>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<link rel="stylesheet" type="text/css" href="/webjars/1.0/JSCal2/css/jscal2.css" />
<link rel="stylesheet" type="text/css" href="/webjars/1.0/JSCal2/css//border-radius.css" />
<link rel="stylesheet" type="text/css" href="/webjars/1.0/JSCal2/css/steel/steel.css" />
<script type="text/javascript" src="/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="/webjars/1.0/JSCal2/js/lang/en.js"></script>

</head>
<div class="container">
	<div class="topband">
		<h2 class="title-cls-name float-left">Add Notification</h2> 
		<div class="float-right">
                                             
		<span onclick="backToTemplateListingPage();">
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
						<textarea class="form-control" rows="15" cols="60" title="Select Criteria" id="selectionCriteria" placeholder="Select Criteria" name="selectionCriteria" style="height:80px">&lt;/textarea&gt;
				</div>
			</div>
                                                                                                        
                                                                                                         
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<span class="asteriskmark">*</span><label for="messageText"> Message Text :</label>
						<textarea class="form-control" rows="15" cols="60" title="Message Text" id="messageText" name="messageText" style="height:80px">&lt;/textarea&gt;
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
				<input id="addNotification" class="btn btn-primary" name="addTemplate" value="Save" type="button" onclick="saveNotification();">
				<span onclick="backToTemplateListingPage();">
					<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
				</span> 
			</div>
		</div>
	</div>
</form>                             

</div>
        

    
               
 
<script>
contextPath = "${(contextPath)!''''}";
let formId = "${formId}";
$(function() {
	
	Calendar.setup({
			trigger    : "fromDate-trigger",
			inputField : "fromDate",
			dateFormat : "%d-%b-%Y",
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
			onSelect   : function() {
				let selectedDate = this.selection.get();
				let date = Calendar.intToDate(selectedDate);
				date = Calendar.printDate(date, "%d-%b-%Y");
				$("#"+this.inputField.id).val(date);
				this.hide(); 
			}
		});



<#if (resultSet)??>
	<#list resultSet as resultSetList>
		$("#targetPlatform option[value=''${resultSetList?api.get("target_platform")}'']").attr("selected", "selected");
		$("#messageFormat option[value=''${resultSetList?api.get("message_format")}'']").attr("selected", "selected");		
		$("#messageType option[value=''${resultSetList?api.get("message_type")}'']").attr("selected", "selected");
		$("#fromDate").val(Calendar.printDate(Calendar.parseDate(''${resultSetList?api.get("message_valid_from")}'',false),"%d-%b-%Y"));
		$("#toDate").val(Calendar.printDate(Calendar.parseDate(''${resultSetList?api.get("message_valid_till")}'',false),"%d-%b-%Y"));
		$("#messageText").val(''${resultSetList?api.get("message_text")}'');
		$("#selectionCriteria").val(''${resultSetList?api.get("selection_criteria")}''); 
		$("#notificationId").val(''${resultSetList?api.get("notification_id")}'');
	</#list>
</#if>


});  

function saveNotification (){
    if(validateFields() == false){
        $("#errorMessage").show();
        return false;
    }
	let formData = $("#genericNotificationForm").serialize() + "&formId="+formId;
    
    $.ajax({
     		type : "POST",
     		url : "sdf",
     		data : formData 
 	});
    backToTemplateListingPage();
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

function backToTemplateListingPage(){
	window.location.href=contextPath+"/cf/nl";
}     
</script>
','admin',NOW(),null,null);

REPLACE INTO dynamic_form_save_queries(dynamic_form_query_id ,dynamic_form_id  ,dynamic_form_save_query  ,sequence,checksum) VALUES (
   'daf459b9-f82f-11ea-97b6-e454e805e22f' ,'e848b04c-f19b-11ea-9304-f48e38ab9348' ,'<#if  (formData?api.getFirst("notificationId"))?has_content>
	UPDATE generic_user_notification SET
  target_platform = ''${formData?api.getFirst("targetPlatform")}''  
  ,message_valid_from = STR_TO_DATE( "${formData?api.getFirst("fromDate") }","%d-%b-%Y") 
  ,message_valid_till = STR_TO_DATE( "${formData?api.getFirst("toDate") }","%d-%b-%Y") 
  ,message_text = ''${formData?api.getFirst("messageText")}'' 
  ,message_type = ''${formData?api.getFirst("messageType")}''
  ,message_format =''${formData?api.getFirst("messageFormat")}''
  ,selection_criteria = ''${formData?api.getFirst("selectionCriteria")}'' 
  ,updated_by = ''admin'' 
  ,updated_date = now()
WHERE notification_id = ''${formData?api.getFirst("notificationId")}''   ;

<#else>
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
   uuid()   
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
);
</#if>'
  ,1,null
);

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'dynamic-form-listing', '<head>
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
		<h2 class="title-cls-name float-left">Dynamic Form Master</h2> 
		<div class="float-right">
			<#if environment == "dev">
				<input id="downloadForm" class="btn btn-primary" onclick= "downloadForm();" name="downloadForm" value="Download Form" type="button">
				<input id="uploadForm" class="btn btn-primary" onclick= "uploadForm();" name="uploadForm" value="Upload Form" type="button">
			</#if>
			<input class="btn btn-primary" name="addNewDynamicForm" value="Add New Dynamic Form" type="button" onclick="submitForm(this)">
    	 <span onclick="backToHome();">
    	  	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
    	 </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divDynamicFormMasterGrid"></div>

		<div id="snackbar"></div>
		<form action="/cf/aedf" method="POST" id="addEditDynamicForm">	
			<input type="hidden" id="formId" name="form-id">	
		</form>
		
</div>

<script>
	contextPath = "${(contextPath)!''''}";
	$(function () {
			let colM = [
				{ title: "Form Name", width: 130, dataIndx: "formName" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Form Description", width: 130, dataIndx: "formDescription", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Created By", width: 100, dataIndx: "createdBy" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Created Date", width: 100, dataIndx: "createdDate", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
				{ title: "Action", width: 50, dataIndx: "action", align: "center", halign: "center", render: editDynamicFormFormatter}
			];
	
			let grid = $("#divDynamicFormMasterGrid").grid({
				gridId: "dynamicFormListingGrid",
				colModel: colM
			});
	});
	
	function editDynamicFormFormatter(uiObject) {	
		var dynamicFormId = uiObject.rowData.formId;
		<#if environment == "dev">
			var element = "<span id=''"+dynamicFormId+"'' class= ''grid_action_icons''><i class=''fa fa-pencil''></i></span>";
		<#else>
			var element = "<span id=''"+dynamicFormId+"'' class= ''grid_action_icons''  onclick=''submitForm(this)''><i class=''fa fa-pencil''></i></span>";		
		</#if>	
		return element;	
	}	
	function submitForm(thisObj){	
		$("#formId").val(thisObj.id);	
		$("#addEditDynamicForm").submit();	
	}
	
	function backToHome() {
		location.href = "/cf/home";
	}
	<#if environment == "dev">
		function downloadForm(){
		     $.ajax({
		        url:"/cf/ddf",
		        type:"POST",
		        success:function(data){
		            debugger;
		            alert("Downloaded Successfully");
		        }
		        
		    });
		}
		function uploadForm(){
		     $.ajax({
		        url:"/cf/udf",
		        type:"POST",
		        success:function(data){
		            debugger;
		            alert("Uploaded Successfully");
		        }
		        
		    });
		}
	</#if>
	
	
</script>', 'admin','admin',now());

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date)
VALUES (UUID(),'dynamic-form-manage-details','<head>
   <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
   <script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
   <script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
   <script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
   <script src="/webjars/1.0/monaco/require.js"></script>
   <script src="/webjars/1.0/monaco/min/vs/loader.js"></script>
   <link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
   <link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
   <script src="/webjars/1.0/dynamicform/addEditDynamicForm.js"></script>


    <div class="container">
                              
                              
		<div class="topband">
			<h2 class="title-cls-name float-left">Add/Edit Dynamic Form</h2> 
		<div class="float-right">				
			<span onclick="addEdit.backToDynamicFormListing();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>              
		</div>
		<div class="clearfix"></div>                         
		</div>
                              
                              

        <form id="dynamicform" method="post" >
            <div class="row">
                <div class="col-6">
                    <div class="col-inner-form full-form-fields">
                    	<span class="asteriskmark">*</span>
						<label for="formName" >Form name: </label>
                        <input type="hidden" id="formId" name="formId" value="${(dynamicForm?api.getFormId())!""}"/>
                        <input type="text" id="formName" name="formName"  placeholder="Enter Form name" class="form-control" value="${(dynamicForm?api.getFormName())!""}" />
                     </div>
                </div>
                                                                                                                        
               <div class="col-6">
                   <div class="col-inner-form full-form-fields">
                   		<span class="asteriskmark">*</span>
						<label for="formDescription" >Form Description: </label>
                        <input type="text" id="formDescription" name="formDescription" placeholder="Enter Form Description" class="form-control" value="${(dynamicForm?api.getFormDescription())!""}"/>           
                    </div>
                </div>
                
                <#if (tables)?has_content>                                            
                  <div class="col-6">
	                   <div class="col-inner-form full-form-fields">
					<label for="formTemplate" >Form template : </label>
                        		<select id="formTemplate" name="formTemplate" placeholder="Select for default template" class="form-control" onchange="loadTableTemplate(event);"/>   
                              <option>Select</option>
                        			<#list tables as tableName>
                							<option>${tableName}</option>
                            
						                  </#list>
            </select>
	                    </div>
                	</div>
                </#if>
            </div>
                   
           <div class="row margin-t-b">  
				<div class="col-12">
				<h3 class="titlename"><span class="asteriskmark">*</span>Select Script</h3>
            	    <div class="html_script">
                		<div class="grp_lblinp">
	                		<div  class="ace-editor-container" id="sqlContainer">
	                			<div class= "ace-editor"  id="sqlEditor">
	                			</div>
	                		</div>
                		</div>
                	</div>
                </div>
			</div>                  
                              
            <div class="row margin-t-b">                 
	            <div class="col-12">
	            <h3 class="titlename"><span class="asteriskmark">*</span>HTML Script</h3>
					<div class="html_script">
						<div class="grp_lblinp">
							<div id="htmlContainer" class="ace-editor-container">
								<div id="htmlEditor" class="ace-editor">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
               
             
             <div class="row margin-t-b">  
             	<div class="col-12">
             	<h3 class="titlename"><span class="asteriskmark">*</span>Save/Update Script</h3>
	                	<div id = "saveScriptContainer">
		                	
	                	</div>
                	</div>
		</div>

		<div class="row margin-t-b">
                <div class="col-12">
                    <div class="float-left"> 
                        <div class="btn-icons nomargin-right-cls pull-right">
                            <input type="button" value="Add" class="btn btn-primary" onclick="addEdit.addSaveQueryEditor();">
                            <input type="button" id="removeTemplate" value="Remove" class="btn btn-secondary" onclick="addEdit.removeSaveQueryEditor();">
                        </div>    
                    </div>
                </div>
		</div>          
  
            <div class="row margin-t-b">
                <div class="col-12">
                    <div class="float-right"> 
                        <div class="btn-icons nomargin-right-cls pull-right">
                            <input type="button" value="Save" class="btn btn-primary" onclick="addEdit.saveDynamicForm();">
                            <input type="button" id="cancelDynamicForm" value="Cancel" class="btn btn-secondary" onclick="addEdit.backToDynamicFormListing();">
                        </div>    
                    </div>
                </div>
		</div>	
            <input type="hidden" name="formSelectQuery" id="formSelectQuery">
            <input type="hidden" name="formBody" id="formBody">
            <input type="hidden" name="formSaveQuery" id="formSaveQuery">                     
        </form>
    </div>              
	<textarea id="htmlContent" style="display: none">
    	${(dynamicForm?api.getFormBody())!""}
	&lt;/textarea&gt;
  
  	<textarea id="sqlContent" style="display: none">
    	${(dynamicForm?api.getFormSelectQuery())!""}
	&lt;/textarea&gt;
	
		<textarea id="saveSqlContent" style="display: none">
    	${(dynamicForm?api.getFormSaveQuery())!""}
	&lt;/textarea&gt;
<script>
	contextPath = "${(contextPath)!''''}";
	let dashletSQLEditor;
	let dashletHTMLEditor;
	let dashletSAVESQLEditor;
  let dashletSQLEditors = [];
	const addEdit = new AddEditDynamicForm();
  $(function () {
    AddEditDynamicForm.prototype.loadAddEditDynamicForm();
    function loadTableTemplate(event){
      $.ajax({
          type: "POST",
          url: contextPath+"/cf/dfte",
          data: {
            tableName: event.currentTarget.value
          },
          success: function(data) {
  		      dashletHTMLEditor.setValue(data);
          }
        });
      }
  });
   </script>
  ','admin','admin',now() );
  
 REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
(UUID(), 'home', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script> 
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<div class="container">
    <div class="page-header">
        <h2 class="maintitle_name"> Java Web Starter </h2>
        <p>
            <i>
                We take an opinionated view of the Spring platform and third-party 
                libraries so you can get your project started with minimum fuss.
            </i>
        </p>
    </div>

               <div class="list-group custom-list-home">
                              <a href="../cf/gd" class="list-group-item list-group-item-action">
                                  <div class="home_list_icon"><img src="/webjars/1.0/images/grid.svg"></div> 
                                  <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1"> <span>Grid Utils</span></h5>
                                             <small>Today</small>
                                             </div>
                                             <p class="mb-1"> Built using pq-grid, and supporting it with generic queries to get data for grid based on the target databases. </p>
                                             <small>Now any master listing page will be created without much efforts</small>
                                             </div>
                              </a>
                              <a href="../cf/te" class="list-group-item list-group-item-action">
                                             <div class="home_list_icon"><img src="/webjars/1.0/images/template.svg"></div> 
                                  <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1">Templating utils</h5>
                                             <small class="text-muted">Today</small>
                                             </div>
                                             <p class="mb-1">Built using Freemarker templating engine, generates HTML web pages, e-mails, configuration files, etc. from template files and the data your application provides</p>
                                             <small class="text-muted">Now create views for your project, and leverage all benifits of spring utils on it.</small>
                                             </div>
                              </a>
                              <a href="../cf/rb" class="list-group-item list-group-item-action">
                                             <div class="home_list_icon m_icon"><img src="/webjars/1.0/images/database.svg"></div> 
                                  <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1">DB Resource Bundle</h5>
                                             <small class="text-muted">Today</small>
                                             </div>
                                             <p class="mb-1">Built using Spring interceptors, Locale Resolvers and Resource Bundles for different locales</p>
                                             <small class="text-muted">Any web application with users all around the world, internationalization (i18n) or localization (L10n) is very important for better user interaction, so handle all these from the admin panel itself by storing it in database.</small>
                                             </div>
                              </a>
                                             <a href="../cf/adl" class="list-group-item list-group-item-action">
                                             <div class="home_list_icon"><img src="/webjars/1.0/images/autotype.svg"></div> 
                                  <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1">TypeAhead</h5>
                                             <small class="text-muted">Today</small>
                                             </div>
                                             <p class="mb-1">Built using Jquery plugin, rich-autocomplete to get data lazily</p>
                                             <small class="text-muted">Now any autocomplete component which handles dynamic creation of query will be created without much efforts</small>
                                             </div>
                              </a>
                              <a href="../cf/nl" class="list-group-item list-group-item-action">
                              <div class="home_list_icon"><img src="/webjars/1.0/images/notification.svg"></div> 
                                  <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1">Notification</h5>
                                             <small class="text-muted">Today</small>
                                             </div>
                                             <p class="mb-1">Built using Freemarker templating engine.</p>
                                             <small class="text-muted">Create your application notification with ease and control the duration and context where to show it, (cross platform.)</small>
                                             </div>
                                             
                              </a>
                              <a href="../cf/dbm" class="list-group-item list-group-item-action">
                              <div class="home_list_icon"><img src="/webjars/1.0/images/dashboard.svg"></div> 
                                  <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1">Dashboard</h5>
                                             <small class="text-muted">Today</small>
                                             </div>
                                             <p class="mb-1">Built using Freemarker templating engine and spring resource bundles</p>
                                             <small class="text-muted">Now create the daily reporting, application usage, trends dashboard for your web application and control it with our dashboard admin panel.</small>
                                             </div>
                              </a>
                              <a href="../cf/dfl" class="list-group-item list-group-item-action">
                              <div class="home_list_icon"><img src="/webjars/1.0/images/daynamicreport.svg"></div> 
                                  <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1">Dynamic Form</h5>
                                             <small class="text-muted">Today</small>
                                             </div>
                                             <p class="mb-1">Built using Freemarker templating engine </p>
                                             <small class="text-muted">Now create the dynamic forms for your web application, without writing any java code just by using freemarker</small>
                                             </div>
                              </a>
                              <a href="../cf/dynl" class="list-group-item list-group-item-action">
                              <div class="home_list_icon"><img src="/webjars/1.0/images/API_listing_icon.svg"></div> 
                                  <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1">Dynamic API Listing</h5>
                                             <small class="text-muted">Today</small>
                                             </div>
                                             <p class="mb-1">Built using Freemarker templating engine </p>
                                             <small class="text-muted">Now create the dynamic forms for your web application, without writing any java code just by using freemarker</small>
                                             </div>
                              </a>
                              <a href="../cf/mul" class="list-group-item list-group-item-action">
                              <div class="home_list_icon"><img src="/webjars/1.0/images/Menu_icon.svg"></div> 
                                  <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1">Menu</h5>
                                             <small class="text-muted">Today</small>
                                             </div>
                                             <p class="mb-1">Built using Freemarker templating engine </p>
                                             <small class="text-muted">Create menu for your application.</small>
                                             </div>
                              </a>
                              <a href="../cf/pml" class="list-group-item list-group-item-action">
                                             <div class="home_list_icon"><img src="/webjars/1.0/images/Property_master_icon.svg"></div> 
                                             <div class="home_list_content">
                                             <div class="d-flex w-100 justify-content-between">
                                             <h5 class="mb-1">Property Master</h5>
                                             <small class="text-muted">Today</small>
                                             </div>
                                             <p class="mb-1">Built using Freemarker templating engine </p>
                                             <small class="text-muted">Create menu for your application.</small>
                                             </div>
                              </a>
               </div>
</div>', 'admin', 'admin', NOW());

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
(UUID(), 'template-manage-details', '<head>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="/webjars/1.0/monaco/require.js"></script>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script src="/webjars/1.0/monaco/min/vs/loader.js"></script>

</head>
<div class="container">

	<div class="topband">
    	<h2 class="title-cls-name float-left">Update Template Details</h2> 
        <div class="float-right">
                                             
        <span onclick="templateMaster.backToTemplateListingPage();">
        	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
        </span>              
        </div>
                              
        <div class="clearfix"></div>                         
    </div>

	<div class="row">
	    	<div class="col-9">
	        <div class="col-inner-form full-form-fields">
	             <label for="vmName"><span class="asteriskmark">*</span>Template Name </label>                                                                                                                                       
	             <input type="text" class="form-control" value="${(templateDetails.templateName)!}" maxlength="100" name="vmName" id="vmName">                                                                                                                       
	        </div>
	    </div>
	    <div id="defaultTemplateDiv" class="col-3" style="display: none;">
	        <div class="col-inner-form full-form-fields">
	             <label for="defaultTemplateId"><span class="asteriskmark">*</span>Default template </label>                                                                                                                                       
	             <select class="form-control" id="defaultTemplateId" name="defaultTemplateId" title="Default template">
                   </select>                                                                                                                    
	        </div>
	    </div>
      </div>
         
         	<div class="row">                                                                                                
				<div class="col-12">
				<div class="html_script">
						<div class="grp_lblinp">
				<div id="htmlContainer" class="ace-editor-container">
					<div id="htmlEditor" class="ace-editor"></div>
				</div>
            </div>
					</div>
            </div>
			
			
			</div>
               
       <div class="row margin-t-b">                      
        <div class="col-12">
        	<div class="float-right">
            	<input id="addTemplate" class="btn btn-primary" name="addTemplate" value="Save" type="button" onclick="templateMaster.validateSaveVelocity();">
                <span onclick="templateMaster.backToTemplateListingPage();">
                	<input id="cancelBtn" class="btn btn-secondary" name="cancelBtn" value="Cancel" type="button">
                </span>              
            </div>
            </div>
        </div>

    
    <div id="snackbar"></div>
</div>

<script>
	contextPath = "${(contextPath)!''''}";
	const ftlTemplateId = "${(templateDetails.templateId)!0}";
	let templateMaster;
	let defaultTemplates = new Array();
	$(function () {
		templateMaster = new TemplateEngine(ftlTemplateId);
		templateMaster.initPage();
		if(ftlTemplateId == 0) {
			$("#defaultTemplateDiv").show();
			$.ajax({
				type : "GET",
				url : contextPath+"/dyn/api/defaultTemplates",
				success : function(data){
					defaultTemplates = data["defaultTemplates"];
					for(let counter = 0; counter < defaultTemplates.length; ++counter) {
						$("#defaultTemplateId").append("<option>"+defaultTemplates[counter]["name"]+"</option>");
					}
					$("#defaultTemplateId").change(function(event){
						templateMaster.editor.setValue(defaultTemplates.find(te => te.name == event.currentTarget.value).template);
					});
				}
			});
		}
	});
</script>
<script src="/webjars/1.0/template/template.js"></script>', 'admin', 'admin', NOW());
 
 REPLACE INTO  template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
(UUID(), 'grid-listing', '<head>
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
		
		<h2 class="title-cls-name float-left">Grid Details Master</h2> 
		<div class="float-right">
		<form id="addEditNotification" action="/cf/df" method="post" class="margin-r-5 pull-left">
	            <input type="hidden" name="formId" value="8a80cb8174bebc3c0174bec1892c0000"/>
	            <input type="hidden" name="primaryId" id="primaryId" value=""/>
	            <button type="submit" class="btn btn-primary">Add Grid Details</button>
        	</form>

         <span onclick="backToWelcomePage();">
          	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
         </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divGridDetailsListing"></div>

		<div id="snackbar"></div>
</div>


<script>
	contextPath = "${(contextPath)!''''}";
	function backToWelcomePage() {
			location.href = contextPath+"/cf/home";
	}
	$(function () {
		let colM = [
	        { title: "Grid Id", width: 130, align: "center", dataIndx: "gridId", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Name", width: 100, align: "center",  dataIndx: "gridName", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Description", width: 160, align: "center", dataIndx: "gridDesc", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Table Name", width: 200, align: "center", dataIndx: "gridTableName", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Grid Column Names", width: 100, align: "center", dataIndx: "gridColumnName", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Action", width: 50, align: "center", render: editGridDetails, dataIndx: "action" }
		];
		let grid = $("#divGridDetailsListing").grid({
	      gridId: "gridDetailsListing",
	      colModel: colM
	  });
	  
	});
	function editGridDetails(uiObject) {
		const gridId = uiObject.rowData.gridId;
		return ''<span id="''+gridId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title=""></i></span>''.toString();
	}
	
	function submitForm(element) {
		$("#primaryId").val(element.id);
		$("#addEditNotification").submit();
	}
</script>', 'admin', 'admin', NOW());

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'resource-bundle-listing', '<head>
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
        <h2 class="title-cls-name float-left">${messageSource.getMessage("jws.resourceBundleTitle")}</h2> 
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


        <div id="snackbar"></div>
</div>


<form action="${(contextPath)!''''}/cf/aerb" method="POST" id="formRbRedirect">
	<input type="hidden" id="resource-key" name="resource-key">
</form>
<script>
	contextPath = "${(contextPath)!''''}";
	
	$(function () {
	
	    var colM = [
	        { title: "${messageSource.getMessage(''jws.resourceKey'')}", width: 130, dataIndx: "resourceKey", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "${messageSource.getMessage(''jws.languageName'')}", width: 100, dataIndx: "languageName", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
	        { title: "${messageSource.getMessage(''jws.text'')}", width: 160, dataIndx: "resourceBundleText", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editDBResource}
	    ];
	    var grid = $("#divdbResourceBundleGrid").grid({
	      gridId: "resourceBundleListingGrid",
	      colModel: colM
	  	});
  	});
	function editDBResource(uiObject) {
		const resourceKey = uiObject.rowData.resourceKey;
		return ''<span id="''+resourceKey+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title="${messageSource.getMessage(''jws.editResourceBundle'')}"></i></span>''.toString();
	}
	
	function submitForm(element) {
	  $("#resource-key").val(element.id);
	  $("#formRbRedirect").submit();
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
</script>', 'admin', 'admin', NOW());


REPLACE INTO `template_master`(`template_id`,`template_name`,`template`,`updated_by`,`created_by`,`updated_date`) VALUES (UUID() ,'resource-bundle-manage-details','<head>
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
		<h2 class="title-cls-name float-left">${messageSource.getMessage("jws.addEditResourceBundle")}</h2> 
		<div class="float-right">
			<span onclick="addEditResourceBundleFn.backToResourceBundleListing();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
			</span> 	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
		
		<div class="row">
			<div class="col-12">
				<div class="col-inner-form full-form-fields">
					<label for="targetPlatform"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.resourceKey")}</label>									 
					<input type="text" id="resourceBundleKey" class="form-control" maxlength="100" value="${(resourceBundleKey)!''''}" disabled="true"  name="resourceKey" style="width:100%"/>
								 
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
						<textarea id="textBx_${languages.languageId}" rows="8" class="area">${(resourceBundleVOMap?api.get(languages.languageId).getText())!''''}&lt;/textarea&gt;
					</div>							  
				</div>
			</#list>
		</div>
				
		<div class="row">
			<div class="col-12">
				<div class="float-right">
					<#if resourceBundleKey ?? && resourceBundleKey?has_content>
						<input id="addResourceBundle" class="btn btn-primary" name="addResourceBundle" value="${messageSource.getMessage(''jws.save'')}" type="button" onclick="addEditResourceBundleFn.saveResourceBundle(''isEdit'');">
					<#else>
						<input id="addResourceBundle" class="btn btn-primary" name="addResourceBundle" value="${messageSource.getMessage(''jws.save'')}" type="button" onclick="addEditResourceBundleFn.saveResourceBundle(''isAdd'');">
					</#if>
					<span onclick="addEditResourceBundleFn.backToResourceBundleListing();">
						<input id="cancelBtn" class="btn btn-secondary" name="${messageSource.getMessage(''jws.cacel'')}" value="Cancel" type="button">
					</span> 
				</div>
			</div>
		</div>

 		<div id="snackbar"></div>
 </div>
 <script>
	contextPath = "${(contextPath)!''''}";
	var resourceBundleFormData = new Array();
	let addEditResourceBundleFn;
	$(function() {
	 	const addEditResourceBundle = new AddEditResourceBundle(resourceBundleFormData);
		addEditResourceBundleFn = addEditResourceBundle.fn;
		addEditResourceBundleFn.loadAddEditResourceBundlePage();
	});
</script>','admin','admin',NOW());

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'notification-listing', '<head>
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
	    <h2 class="title-cls-name float-left">Notification Master</h2> 
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
<script>
	contextPath = "${(contextPath)!''''}";
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
	$(function () {
		let colM = [
	    	{ title: "",hidden: true, width: 130, dataIndx: "notificationId" },
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
	        { title: "Action", width: 50, dataIndx: "action",align: "center", halign: "center",render: editNotificationFormatter },
		];

	  let grid = $("#divNotificationListing").grid({
	      gridId: "notificationDetailsListing",
	      colModel: colM
	  });
});

function editNotificationFormatter(uiObject) {
	const notificationId = uiObject.rowData.notificationId;
	let element = "<span id=''"+notificationId+"'' class= ''grid_action_icons''  onclick=''editNotification(this)''><i class=''fa fa-pencil''></i></span>";
	return element;
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
</script>', 'admin', 'admin', NOW());
 
 REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'loadNotifications', '<div>
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
', 'admin', 'admin', NOW()); 


 REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('8a80cb81749b028401749b062c540002', 'dynarest-details-listing', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/1.0/pqGrid/pqgrid.min.js"></script>     
<link rel="stylesheet" href="/webjars/1.0/pqGrid/pqgrid.min.css" /> 
<script src="/webjars/1.0/gridutils/gridutils.js"></script>  
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>
<div class="container">
		<div class="topband">
		<h2 class="title-cls-name float-left">Dynamic API Master</h2> 
		<div class="float-right">
			<#if environment == "dev">
				<input id="downloadDynamicForm" class="btn btn-primary" onclick= "downloadDynamicForm();" name="downloadDynamicForm" value="Download Dynamic Form" type="button">
				<input id="uploadDynamicForm" class="btn btn-primary" onclick= "uploadDynamicForm();" name="uploadDynamicForm" value="Upload Dynamic Form" type="button">
			</#if>
			<form id="addEditNotification" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="8a80cb81749ab40401749ac2e7360000"/>
                <input type="hidden" name="primaryId" id="primaryId" value=""/>
                <button type="submit" class="btn btn-primary">
                        Add Dynamic API Details
                </button>
            </form>
			<span onclick="backToWelcomePage();">
  		        <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
  		    </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divDynarestGrid"></table>

		<div id="snackbar"></div>
</div>
<script>
contextPath = "${(contextPath)!''''}";
let requestType = [{"": "All"}];
let platformValues = [{"": "All"}, {"1": "Java"}, {"2": "FTL"}];
$(function () {
  $.ajax({
			type : "GET",
			url : contextPath+"/dyn/api/dynarestDetails",
      async: false,
			success : function(data) {
				for(let counter = 0; counter < data["methodTypes"].length; ++counter) {
					let object = data["methodTypes"][counter];
          let details = new Object()
          details[object["value"]] = object["name"];
					requestType.push(details);
				}
			}
		});
    
	let colM = [
        { title: "Dynamic API Url", width: 130, align: "center", dataIndx: "jws_dynamic_rest_url", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Method Name", width: 100, align: "center",  dataIndx: "jws_method_name", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Method Description", width: 160, align: "center", dataIndx: "jws_method_description", align: "left", halign: "center",
        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
        { title: "Method Type", width: 160, align: "center", dataIndx: "jws_request_type_id", align: "left", halign: "center", render: requestTypes,
        filter: { type: "select", condition: "equal", options : requestType, listeners: ["change"]} },
        { title: "Platform", width: 160, align: "center", dataIndx: "jws_platform_id", align: "left", halign: "center", render: platforms,
        filter: { type: "select", condition: "equal", options : platformValues, listeners: ["change"]} },
        { title: "Action", width: 30, align: "center", render: editDynarest, dataIndx: "action" }
	];
    let grid = $("#divDynarestGrid").grid({
      gridId: "dynarestGrid",
      colModel: colM
  });
});

function requestTypes(uiObject){
  let cellValue = uiObject.rowData.jws_request_type_id;
  return requestType.find(el => el[cellValue])[cellValue];
}

function platforms(uiObject){
  let cellValue = uiObject.rowData.jws_platform_id;
  return platformValues.find(el => el[cellValue])[cellValue];
}

function editDynarest(uiObject) {
	let dynarestUrl = uiObject.rowData.jws_dynamic_rest_url;
  	return ''<span id="''+dynarestUrl+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
}

function submitForm(element) {
  $("#primaryId").val(element.id);
  $("#addEditNotification").submit();
}

function backToWelcomePage() {
	location.href = contextPath+"/cf/home";
}
<#if environment == "dev">
	function downloadDynamicForm(){
		 $.ajax({
			url:"/cf/ddr",
			type:"POST",
			success:function(data){
				alert("Downloaded Successfully");
			}
			
		});
	}
	function uploadDynamicForm(){
		 $.ajax({
			url:"/cf/udr",
			type:"POST",
			success:function(data){
				alert("Uploaded Successfully");
			}
			
		});
	}
</#if>
</script>', 'admin', 'admin', NOW(), NULL);

REPLACE into dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date) VALUES
('8a80cb81749ab40401749ac2e7360000', 'dynamic-rest-form', 'Form to manage dynamic rest modules.', 'select jdrd.jws_dynamic_rest_id as dynarestId, jdrd.jws_dynamic_rest_url as dynarestUrl, jdrd.jws_method_name as dynarestMethodName, 
jdrd.jws_method_description as dynarestMethodDescription, jdrd.jws_platform_id as dynarestPlatformId,
jdrd.jws_request_type_id as dynarestRequestTypeId, jdrd.jws_response_producer_type_id as dynarestProdTypeId 
from jws_dynamic_rest_details as jdrd 
where jdrd.jws_dynamic_rest_url = "${primaryId}"', 
'<head>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="/webjars/1.0/monaco/require.js"></script>
<script src="/webjars/1.0/monaco/min/vs/loader.js"></script>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script src="/webjars/1.0/dynarest/dynarest.js"></script>
</head>

<div class="container">
	<div class="topband">
		<h2 class="title-cls-name float-left">Dynamic Rest API</h2> 
		<div class="float-right">
			<span onclick="dynarest.backToDynarestListingPage();">
  				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
  		 	</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>

	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	
    <form method="post" name="dynamicRestForm" id="dynamicRestForm">
       <input type="hidden" id="dynarestId" name="dynarestId"/>
       <div class="row">                        
              <div class="col-3">
                    <div class="col-inner-form full-form-fields">
                       <span class="asteriskmark">*</span><label for="dynarestUrl">Dynamic Rest URL </label>
                       <span id="dynarestURLSapn">
                            <input id="dynarestUrl" name= "dynarestUrl" class="form-control" placeholder="Dynamic Rest URL" />
                       </span>
                    </div>
              </div>
                                                                                                                                                                     
             <div class="col-3">
                    <div class="col-inner-form full-form-fields">
                       <span class="asteriskmark">*</span><label for="dynarestMethodName">Dynamic Rest Method Name</label>
                       <span id="dynarestMethodNameSpan">
                              <input class="form-control" id="dynarestMethodName"  name= "dynarestMethodName" placeholder="Method Name" />
                       </span>
                    </div>
             </div>
                                                                                                                           
             <div class="col-3">
                    <div class="col-inner-form full-form-fields">
                        <span class="asteriskmark">*</span><label for="dynarestMethodDescription">Dynamic Rest Method Description</label>
                       <span id="dynarestMethodDescriptionSpan">
                              <input class="form-control" id="dynarestMethodDescription"  name= "dynarestMethodDescription" placeholder="Method Description" />
                       </span>
                    </div>
             </div>
                                                                                             
             <div class="col-3">
                    <div class="col-inner-form full-form-fields">
                         <span class="asteriskmark">*</span><label for="dynarestRequestTypeId">HTTP Method Type  </label>
                         <select class="form-control" id="dynarestRequestTypeId" name="dynarestRequestTypeId" title="HTTP Method Type">
                         </select>
                    </div>
             </div>
                                                                 
           <div class="col-3">
                  <div class="col-inner-form full-form-fields">
                         <span class="asteriskmark">*</span><label for="dynarestProdTypeId">HTTP Produces Type  </label>
                         <select class="form-control" id="dynarestProdTypeId" name="dynarestProdTypeId" title="HTTP Produces Type">
                         </select>
                    </div>
           </div>

           <div class="col-3">
                    <span class="asteriskmark">*</span><label for="dynarestPlatformId">Platform Id</label>
                     <select class="form-control" id="dynarestPlatformId" name="dynarestPlatformId" title="Platform Id">
                                    <option value="2" selected="selected"> FTL </option>
                                    <option value="1"> JAVA </option>
                     </select>
           </div>

      </div>
      
        <div class="row margin-t-b">                 
	            <div class="col-12">
	            <h3 class="titlename"><span class="asteriskmark">*</span>Service Logic</h3>
					<div class="html_script">
						<div class="grp_lblinp">
							<div id="htmlContainer" class="ace-editor-container">
								<div id="htmlEditor" class="ace-editor">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
               
             
             <div class="row margin-t-b">  
             	<div class="col-12">
             	<h3 class="titlename"><span class="asteriskmark">*</span>DAO Queries</h3>
                	<div id = "saveScriptContainer">
	                	
                	</div>
                </div>
		    </div>
		    
       <div class="row margin-t-b">
            <div class="col-12">
                    <div class="float-left"> 
                        <div class="btn-icons nomargin-right-cls pull-right">
                            <input type="button" value="Add" class="btn btn-primary" onclick="dynarest.addSaveQueryEditor();">
                            <input type="button" id="removeTemplate" value="Remove" class="btn btn-secondary" onclick="dynarest.removeSaveQueryEditor();">
                        </div>    
                    </div>
            </div>
	</div>

       <div class="row">
              <div class="col-12">
                    <div class="float-right">
                        <input id="addDynarest" class="btn btn-primary" name="addDynarest" value="Save" type="button" onclick="dynarest.saveDynarest(''${formId}'');">
                        <span onclick="dynarest.backToDynarestListingPage();">
                            <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
                        </span> 
                    </div>
              </div>
      </div> 
      <input type="hidden" name="serviceLogic" id="serviceLogic">
      <input type="hidden" name="saveUpdateQuery" id="saveUpdateQuery">        
    </form>
	<div id="snackbar"></div>
</div>
<script type="text/javascript">

  
  let contextPath = "${contextPath}";
  let dynarest;
  $(function () {
      dynarest = new DynamicRest();
	  
	  <#if (resultSet)?? && resultSet?has_content>
		<#list resultSet as resultSetList>
			<#if resultSetList?is_first>
				$("#dynarestId").val(''${(resultSetList?api.get("dynarestId"))!''''}'');
				$("#dynarestUrl").val(''${(resultSetList?api.get("dynarestUrl"))!''''}'');
				$("#dynarestMethodName").val(''${(resultSetList?api.get("dynarestMethodName"))!''''}'');
				$("#dynarestMethodDescription").val(''${(resultSetList?api.get("dynarestMethodDescription"))!''''}'');
				$("#dynarestPlatformId option[value=''${(resultSetList?api.get("dynarestPlatformId"))!''''}'']").prop("selected", "selected");
				
        dynarest.populateDetails(''${(resultSetList?api.get("dynarestRequestTypeId"))!''''}'',
        ''${(resultSetList?api.get("dynarestProdTypeId"))!''''}'',''${(resultSetList?api.get("dynarestUrl"))!''''}'');
			</#if>
		</#list>
	<#else>
	    dynarest.populateDetails();
	</#if>
	
  });
</script>
	
	', 'admin', NOW());

REPLACE into dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence) VALUES
('8a80cb81749dbc3d01749dc00d2b0001', '8a80cb81749ab40401749ac2e7360000', '<#if  (formData?api.getFirst("dynarestId"))?has_content>
    UPDATE jws_dynamic_rest_details SET 
        jws_dynamic_rest_url = ''${formData?api.getFirst("dynarestUrl")}''
        ,jws_method_name = ''${formData?api.getFirst("dynarestMethodName")}''
        ,jws_method_description = ''${formData?api.getFirst("dynarestMethodDescription")}''
        ,jws_request_type_id = ''${formData?api.getFirst("dynarestRequestTypeId")}''
        ,jws_response_producer_type_id = ''${formData?api.getFirst("dynarestProdTypeId")}''
        ,jws_rbac_id = 1
        ,jws_service_logic = ''${formData?api.getFirst("serviceLogic")}''
        ,jws_platform_id = ''${formData?api.getFirst("dynarestPlatformId")}''
    WHERE jws_dynamic_rest_id = ''${formData?api.getFirst("dynarestId")}''   ;
<#else>
    INSERT INTO jws_dynamic_rest_details(jws_dynamic_rest_url, jws_method_name, jws_method_description, jws_rbac_id, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id) VALUES (
         ''${formData?api.getFirst("dynarestUrl")}''
         , ''${formData?api.getFirst("dynarestMethodName")}''
         , ''${formData?api.getFirst("dynarestMethodDescription")}''
         , 1
         , ''${formData?api.getFirst("dynarestRequestTypeId")}''
         , ''${formData?api.getFirst("dynarestProdTypeId")}''
         ,' '${formData?api.getFirst("serviceLogic")}''
         , ''${formData?api.getFirst("dynarestPlatformId")}''
    );
</#if>', 1);

REPLACE INTO jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id) VALUES
(3, 'dynarestDetails', 1, 'getDynamicRestDetails', 'Method to get dynamic rest details', 2, 7, 'Map<String, Object> response = new HashMap<>();
response.put("methodTypes", dAOparameters.get("dynarestMethodType"));
response.put("producerDetails", dAOparameters.get("dynarestProducerDetails"));
response.put("dynarestDetails", dAOparameters.get("dynarestDetails"));
return response;', 1);

REPLACE INTO jws_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence) VALUES
(4, 3, 'dynarestMethodType', 'select jws_request_type_details_id as value, jws_request_type as name from jws_request_type_details', 1), 
(5, 3, 'dynarestProducerDetails', 'select jws_response_producer_type_id as value, jws_response_producer_type as name from jws_response_producer_details', 2);


REPLACE INTO jws_dynamic_rest_dao_details (jws_dao_details_id,jws_dynamic_rest_details_id,jws_result_variable_name,jws_dao_query_template,jws_query_sequence) VALUES (
6,3,"dynarestDetails" ,	'SELECT  
jdrd.jws_service_logic AS dynarestServiceLogic,jdrdd.jws_result_variable_name AS variableName,
jdrdd.jws_dao_query_template AS dynarestDaoQuery, jdrdd.jws_query_sequence AS dynarestQuerySequence 
FROM jws_dynamic_rest_details AS jdrd 
LEFT OUTER JOIN jws_dynamic_rest_dao_details AS jdrdd ON jdrdd.jws_dynamic_rest_details_id = jdrd.jws_dynamic_rest_id
WHERE jdrd.jws_dynamic_rest_url = "${(url)!''''}" ORDER BY dynarestQuerySequence ASC', 3);

REPLACE INTO jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id) VALUES
(4, 'defaultTemplates', 1, 'defaultTemplates', 'Method to get dynamic rest details', 2, 7, 'Map<String, Object> response = new HashMap<>();
response.put("defaultTemplates", dAOparameters.get("defaultTemplates"));
return response;', 1);

REPLACE INTO jws_dynamic_rest_dao_details (jws_dao_details_id,jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence) VALUES
(7,4, 'defaultTemplates', 'select template_name as name, template as template from template_master where template_name like ("default-%")', 1);

REPLACE INTO  template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
(UUID(), 'menu-module-listing', '<head>
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
		
		<h2 class="title-cls-name float-left">Menu Master</h2> 
		<div class="float-right">
		<span>
  		    <input id="addModule" class="btn btn-primary" name="addGridDetails" value="Add Module" type="button" onclick="submitForm(this)">
		</span>

         <span onclick="backToWelcomePage();">
          	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
         </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divModuleListing"></div>

		<div id="snackbar"></div>
</div>


<form action="${(contextPath)!''''}/cf/aem" method="POST" id="formMuRedirect">
	<input type="hidden" id="moduleId" name="module-id">
</form>
<script>
	contextPath = "${(contextPath)!''''}";
	function backToWelcomePage() {
			location.href = contextPath+"/cf/home";
	}
	$(function () {
		let colM = [
	        { title: "", width: 130, align: "center", dataIndx: "moduleId", align: "left", halign: "center", hidden : true },
	        { title: "Module Name", width: 100, align: "center",  dataIndx: "moduleName", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Module URL", width: 160, align: "center", dataIndx: "moduleURL", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Parent Module Name", width: 200, align: "center", dataIndx: "parentModuleName", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Sequence Number", width: 100, align: "center", dataIndx: "sequence", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
          { title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editModule}
		];
		let grid = $("#divModuleListing").grid({
	      gridId: "moduleListingGrid",
	      colModel: colM
	  });
	});
  
  function editModule(uiObject) {
		const moduleId = uiObject.rowData.moduleId;
		return ''<span id="''+moduleId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title="Edit module"></i></span>''.toString();
	}
  
  function submitForm(element) {
		$("#moduleId").val(element.id);
		$("#formMuRedirect").submit();
	}
</script>', 'admin', 'admin', NOW());
   
 
 

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'addEditModule', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />

</head> 

<div class="container"> 

	<div class="topband">
		<h2 class="title-cls-name float-left">${messageSource.getMessage("jws.addEditModule")}</h2> 
		<div class="float-right">
			<span onclick="addEditModule.backToModuleListingPage();">
  				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
  		 	</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>

	<div id="formDiv">
		<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	 
	 	<div class="row">
			<input type="hidden" id = "moduleId" name="moduleId" value="${(moduleDetailsVO?api.getModuleId())!''''}">
	     	<div class="col-3">
				  <div class="col-inner-form full-form-fields">
					<label for="moduleName" style="white-space:nowrap"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.moduleName")}</label>
					<input type="text"  id = "moduleName" name = "moduleName" value = "${(moduleDetailsVO?api.getModuleName())!''''}" maxlength="100" class="form-control">
				</div>
			</div>	

			<input type="hidden" id = "parentModuleId" name="parentModuleId" value="${(moduleDetailsVO?api.getParentModuleId())!''''}">
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="parentModuleName" style="white-space:nowrap">${messageSource.getMessage("jws.parentModuleName")}</label>
					<select id="parentModuleName" name="parentModuleName" onchange="addEditModuleFn();" class="form-control">
						<option value="">Select</option>
						<#if (moduleListingVOList)??>
							<#list moduleListingVOList as moduleListingVO>
									<#if (moduleListingVO?api.getModuleId())?? && (moduleDetailsVO?api.getParentModuleId())?? && (moduleDetailsVO?api.getParentModuleId()) == moduleListingVO?api.getModuleId()>
										<option value="${moduleListingVO?api.getModuleId()}" selected>${moduleListingVO?api.getModuleName()!''''}</option>
									<#else>
										<option value="${moduleListingVO?api.getModuleId()}">${moduleListingVO?api.getModuleName()!''''}</option>
									</#if>
							</#list>
						</#if>
					</select>
				</div>
			</div>
      
      
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="targetLookupType" style="white-space:nowrap"><span class="asteriskmark">*</span>Context Type</label>
					<select id="targetLookupType" name="targetLookupType" onchange="addEditModule.getTargeTypeNames();" class="form-control">
						<option value="">Select</option>
						<#if (moduleTargetLookupVOList)??>
							<#list moduleTargetLookupVOList as moduleTargetLookupVO>
									<#if (moduleTargetLookupVO?api.getLookupId())?? && (moduleDetailsVO?api.getTargetLookupId())?? && (moduleTargetLookupVO?api.getLookupId()) == moduleDetailsVO?api.getTargetLookupId()>
										<option value="${moduleTargetLookupVO?api.getLookupId()}" selected>${moduleTargetLookupVO?api.getDescription()!''''}</option>
									<#else>
										<option value="${moduleTargetLookupVO?api.getLookupId()}">${moduleTargetLookupVO?api.getDescription()!''''}</option>
									</#if>
							</#list>
						</#if>
					</select>
				</div>
			</div>
	 
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="targetTypeName" style="white-space:nowrap"><span class="asteriskmark">*</span>Context Name</label>
          				<select id="targetTypeName" name="targetTypeName"  class="form-control">
						<option value="Select">Select</option>
					</select>
				</div>
			</div>
      
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="sequence" style="white-space:nowrap"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.sequence")}</label>
					<input type="number"  id = "sequence" name = "sequence" value = "${(moduleDetailsVO?api.getSequence())!''''}" maxlength="100" class="form-control">
				</div>
			</div>
	 
      
      		<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="moduleURL" style="white-space:nowrap"><span class="asteriskmark">*</span>Module URL</label>
					<input type="text"  id = "urlPrefix" name = "urlPrefix" value = "${(urlPrefix)!''''}" readOnly="readonly" class="form-control">
					<input type="text"  id = "moduleURL" name = "moduleURL" value = "${(moduleDetailsVO?api.getModuleURL())!''''}" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
	     		<div class="col-inner-form full-form-fields"> 
		  			<label  class="pull-left label-name-cls full-width"><span class="asteriskmark">*</span>Roles</label>
		  			<div id = "roles">
						<#if userRoleVOs??>
							<#list userRoleVOs as userRoleVO>
								<lable for="${(userRoleVO?api.getRoleId())!''''}">${(userRoleVO?api.getRoleName())!''''}</label>
								<input type="checkbox" id="${(userRoleVO?api.getRoleId())!''''}" name="${(userRoleVO?api.getRoleId())!''''}"/>
							</#list>
						</#if>
					</div>
		  			<div class="clearfix"></div>
				</div>
			</div>	
		</div>
							
	 
	 
		<div class="row">
			<div class="col-12">
				<div id="buttons" class="pull-right">		 
	 				<input id="saveBtn" type="button" class="btn btn-primary" value="${messageSource.getMessage(''jws.save'')}" onclick="addEditModule.saveModule();">	 
	 				<input id="cancelBtn" type="button" class="btn btn-secondary" value="${messageSource.getMessage(''jws.cancel'')}" onclick="addEditModule.backToModuleListingPage();">
				</div>
	 		</div>
		</div>	
	

	
	<div id="snackbar"></div>
</div>

<script>
	contextPath = "${(contextPath)!''''}";
	let addEditModule;
	$(function() {

      let moduleTypeId = "${(moduleDetailsVO?api.getTargetTypeId())!''''}";
	    addEditModule = new AddEditModule(moduleTypeId);
      addEditModule.getTargeTypeNames();
	});

</script>
<script src="/webjars/1.0/menu/addEditModule.js"></script>', 'admin', 'admin',NOW());






REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'home-page', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />

</head>
	<nav class="navbar navbar-dark sticky-top blue-bg flex-md-nowrap p-0 shadow ">
		<a class="navbar-brand col-md-3 col-lg-2 mr-0 px-3" href="/cf/home">Java Web Starter</a>
        <span class="hamburger float-left" id="openbtni" class="closebtn" onclick="homePageFn.openNavigation()">
			<i class="fa fa-bars" aria-hidden="true"></i>
		</span>
        <span id="closebtni" class="closebtn float-left" onclick="homePageFn.closeNavigation()">×</span>
        <ul class="navbar-nav px-3 float-right">
			<li class="nav-item text-nowrap">
				<a class="nav-link" href="#">Sign out</a>
            </li>
        </ul>
	</nav>

<div class="container-fluid ">
	<div class="row">
		<nav id="mySidenav" class=" bg-dark sidenav sidebar">
			<div class="nav-inside">
				<input class="form-control form-control-dark w-100" id="searchInput" type="text" placeholder="Search" aria-label="Search" onkeyup="homePageFn.menuSearchFilter()">
				<ul id="menuUL" class="nav flex-column customnav">
					<#if moduleDetailsVOList??>
						<#list moduleDetailsVOList as moduleDetailsVO>
							
							<#if (moduleDetailsVO?api.getSubModuleCount())?? && (moduleDetailsVO?api.getSubModuleCount()) gte 1>
								<li class="nav-item">
									<a class="nav-link active"  data-toggle="collapse" href="#subModule_${moduleDetailsVO?index}" aria-expanded="false" aria-controls="collapseExample">${moduleDetailsVO?api.getModuleName()!''''}   
										<i class="fa fa-caret-down" aria-hidden="true"></i>
                                    </a>
									<div class="collapse" id="subModule_${moduleDetailsVO?index}">
										<ul class="subcategory">
											<li>
												<a href = "/view/${moduleDetailsVO?api.getModuleURL()!''''}" class="nav-link">Manage ${moduleDetailsVO?api.getModuleName()!''''}</a>
											</li>
											<#list moduleDetailsVOList as moduleDetailsVOChild>
												<#if (moduleDetailsVOChild?api.getParentModuleId())?? && (moduleDetailsVOChild?api.getParentModuleId()) == (moduleDetailsVO?api.getModuleId())>
													<li>
														<a href = "/view/${moduleDetailsVOChild?api.getModuleURL()!''''}" class="nav-link">${moduleDetailsVOChild?api.getModuleName()!''''}</a> 
													</li>
												</#if>
											</#list>
										</ul>
									</div>
							
							<#elseif !(moduleDetailsVO?api.getParentModuleId())??>
								<li class="nav-item">
									<span data-feather="file"></span>
									<a href = "/view/${moduleDetailsVO?api.getModuleURL()!''''}" class="nav-link">${moduleDetailsVO?api.getModuleName()!''''}</a>
								</li>
							</#if>
						
						</#list>
					</#if>
				</div>
			</nav>
	
	<main id="main" class="main-container">
		<div id="bodyDiv">
			<#include "template-body">
		</div>
	</main>
	
	</div>
</div>

<div id="snackbar"></div>

<script>
	const contextPathHome = "${(contextPath)!''''}";
	let homePageFn;
	
	$(function() {
		  
	  const homePage = new HomePage();
	  homePageFn = homePage.fn;
	  
	});


</script>
<script src="/webjars/1.0/home/home.js"></script>', 'admin', 'admin',NOW());


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'error-page', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />

</head>

<div class="container"> 

	<div class="error_block">
		<img src="/webjars/1.0/images/error1.jpg">
		<h2 class="errorcontent">Opps!!! Something went wrong</h2> 
	</div>
    
</div>


</script>', 'admin', 'admin',NOW());


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'default-listing-template', '<head>
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
		<h2 class="title-cls-name float-left">Your page title here</h2> 
		<div class="float-right">
			<input class="btn btn-primary" name="createDashboard" value="Create new" type="button" onclick="createNew()">
			<span onclick="backToWelcomePage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="yourGridId"></div>

	<div id="snackbar"></div>
</div>



<script>
	contextPath = "${(contextPath)!''}";
	$(function () {
	//Add all columns that needs to be displayed in the grid
		let colM = [
			{ title: "Column Name to be displayed", width: 130, dataIndx: "columnNameInQuery", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Action", width: 50, dataIndx: "action", align: "center", halign: "center", render: manageRecord}
		];
	
	//System will fecth grid data based on gridId
		let grid = $("#yourGridId").grid({
	      gridId: "",
	      colModel: colM
	  	});
	
	});
	
	//Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
	function manageRecord(uiObject) {
		console.log(uiObject);
	}
	
	//Add logic to navigate to create new record
	function createNew() {
		
	}

	//Code go back to previous page
	function backToWelcomePage() {

	}
</script>', 'admin', 'admin',NOW());


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'default-form-template', '<head>
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
		<h2 class="title-cls-name float-left">Your page title here</h2> 
		<div class="float-right">
			<input class="btn btn-primary" name="createDashboard" value="Create new" type="button" onclick="createNew()">
			<span onclick="backToPreviousPage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<form method="post" name="addEditForm" id="addEditForm">
		
		<!-- You can include any type of form element over here -->
		<div class="row">
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="field1" style="white-space:nowrap"><span class="asteriskmark">*</span>Field 1</label>
					<input type="text" id="" name="field1" value="" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="field1" style="white-space:nowrap">Field 2</label>
						<select id="" name="field1"  class="form-control">
							<option value="">Select</option>
						</select>
					</label>
				</div>
			</div>
		</div>
		<!-- Your form fields end -->
		
		
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
		
	</form>


	<div id="snackbar"></div>
</div>



<script>
	contextPath = "${(contextPath)!''}";
	
	//Add logic to save form data
	function saveData (){
		let formData = $("#addEditForm").serialize();
	}
	
	//Code go back to previous page
	function backToPreviousPage() {
		
	}
</script>', 'admin', 'admin',NOW());




REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'default-template', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script> 
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<div class="container" style="padding-top: 40px">
    <div class="page-header">
        
    </div>

	
</div>', 'admin', 'admin',NOW());

replace into dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum) VALUES
('8a80cb8174bebc3c0174bec1892c0000', 'grid-details-form', 'Form to add edit grid details', 'SELECT grid_id AS gridId, grid_name AS gridName, grid_description AS gridDescription, grid_table_name AS gridTableName , grid_column_names AS gridColumnName
, query_type AS queryType FROM grid_details WHERE grid_id="${primaryId}"', '<head>
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
		<h2 class="title-cls-name float-left">Add Edit Grid Details</h2> 
		<div class="clearfix"></div>		
	</div>
		
	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	<form method="post" name="addEditForm" id="addEditForm">
		
		<!-- You can include any type of form element over here -->
		<div class="row">
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="gridId" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Id</label>
					<input type="text" id="gridId" name="gridId" value="" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="gridName" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Name</label>
					<input type="text" id="gridName" name="gridName" value="" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="gridDescription" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Description</label>
					<input type="text" id="gridDescription" name="gridDescription" value="" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="gridTableName" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Table Name</label>
					<input type="text" id="gridTableName" name="gridTableName" value="" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="gridColumnName" style="white-space:nowrap"><span class="asteriskmark">*</span>Grid Column Names</label>
					<input type="text" id="gridColumnName" name="gridColumnName" value="" maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="queryType" style="white-space:nowrap">Query Type</label>
						<select id="queryType" name="queryType"  class="form-control">
							<option value="1">View/Table</option>
							<option value="2">Stored Prod.</option>
						</select>
					</label>
				</div>
			</div>
		</div>
		<!-- Your form fields end -->
		
		
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
		
	</form>


	<div id="snackbar"></div>
</div>



<script>
	let formId = "${formId}";
	contextPath = "${contextPath}";
	
	$(function() {
	    <#if (resultSet)??>
    	    <#list resultSet as resultSetList>
        		$("#gridId").val(''${resultSetList?api.get("gridId")}'');
        		$("#gridName").val(''${resultSetList?api.get("gridName")}''); 
        		$("#gridDescription").val(''${resultSetList?api.get("gridDescription")}'');
        		$("#gridTableName").val(''${resultSetList?api.get("gridTableName")}'');
        		$("#gridColumnName").val(''${resultSetList?api.get("gridColumnName")}'');
        		$("#queryType option[value=''${resultSetList?api.get("queryType")}'']").attr("selected", "selected");
    	    </#list>
        </#if>
	});   

	//Add logic to save form data
	function saveData (){
		if(validateFields() == false){
	        $("#errorMessage").show();
	        return false;
	    }
		let formData = $("#addEditForm").serialize()+ "&formId="+formId;
		$.ajax({
		     		type : "POST",
		     		url : contextPath+"/cf/sdf",
		     		data : formData 		 
		     	});
		backToPreviousPage();
	}
	
	function validateFields(){
        const gridId = $("#gridId").val().trim();
        const gridName = $("#gridName").val().trim();
        const gridDescription = $("#gridDescription").val().trim();
        const gridTableName = $("#gridTableName").val().trim();
        const gridColumnName = $("#gridColumnName").val().trim();
        const queryType = $("#queryType").val();
        if(gridId == "" || gridName == "" || gridDescription == "" 
                || gridTableName == "" || gridColumnName == ""){
            $("#errorMessage").html("All fields are mandatory");
    		return false;
        }
        return true;
    }
    
	//Code go back to previous page
	function backToPreviousPage() {
		location.href = contextPath+"/cf/gd";
	}
</script>', 'admin', NOW(), NULL, NULL);


replace into dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('8a80cb8174bebc3c0174bee22fc60005', '8a80cb8174bebc3c0174bec1892c0000', 'REPLACE INTO grid_details (
   grid_id
  ,grid_name
  ,grid_description
  ,grid_table_name
  ,grid_column_names
  ,query_type
) VALUES (
   ''${formData?api.getFirst("gridId")}'' 
  ,''${formData?api.getFirst("gridName")}''  
  ,''${formData?api.getFirst("gridDescription")}''  
  ,''${formData?api.getFirst("gridTableName")}'' 
  ,''${formData?api.getFirst("gridColumnName")}'' 
  ,''${formData?api.getFirst("queryType")}'' 
);', 1, NULL);

replace into dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum) VALUES
('8a80cb8174bf3b360174bfae9ac80006', 'property-master-form', 'Property master form', 'select * from jws_property_master where owner_id = "${ownerId}" and owner_type = "${ownerType}" and property_name = "${propertyName}"
', '<head>
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
		<h2 class="title-cls-name float-left">Add edit property master.</h2>
		<div class="clearfix"></div>		
	</div>
	
	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>	
	<form method="post" name="addEditForm" id="addEditForm">
		
		<!-- You can include any type of form element over here -->
		<div class="row">
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="ownerId" style="white-space:nowrap"><span class="asteriskmark">*</span>Owner Id</label>
					<input type="text" id="ownerId" name="ownerId" value="" required maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="ownerType" style="white-space:nowrap"><span class="asteriskmark">*</span>Owner Type</label>
					<input type="text" id="ownerType" name="ownerType" value="" required maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="propertyName" style="white-space:nowrap"><span class="asteriskmark">*</span>Property Name</label>
					<input type="text" id="propertyName" name="propertyName" value="" required maxlength="100" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="propertyValue" style="white-space:nowrap"><span class="asteriskmark">*</span>Property Value</label>
					<input type="text" id="propertyValue" name="propertyValue" value="" maxlength="100" required class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="appVersion" style="white-space:nowrap"><span class="asteriskmark">*</span>App Version</label>
					<input type="number" id="appVersion" name="appVersion" value="" maxlength="7" required class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="comment" style="white-space:nowrap"><span class="asteriskmark">*</span>Comments</label>
					<input type="text" id="comment" name="comment" value="" maxlength="100" required class="form-control">
				</div>
			</div>
			
		</div>
		<!-- Your form fields end -->
		
		
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
		
	</form>


	<div id="snackbar"></div>
</div>



<script>
	contextPath = "${contextPath}";
	let formId = "${formId}";
	let edit = 0;
	
	//Add logic to save form data
	function saveData (){
	    if(validateFields() == false){
	        $("#errorMessage").show();
	        return false;
	    }
		let formData = $("#addEditForm").serialize() + "&formId="+formId;
		if(edit === 1) {
		    formData = formData + "&edit="+edit;
		}
		$.ajax({
     		type : "POST",
     		url : contextPath+"/cf/sdf",
     		data : formData,
     		success: function(data){
     		    backToPreviousPage();
     		}
     	});
	}
	
    function validateFields(){
        const ownerId = $("#ownerId").val().trim();
        const ownerType = $("#ownerType").val().trim();
        const propertyName = $("#propertyName").val().trim();
        const propertyValue = $("#propertyValue").val().trim();
        const appVersion = $("#appVersion").val().trim();
        const comment = $("#comment").val().trim();
        if(ownerId == "" || ownerType == "" || propertyName == "" 
                || propertyValue == "" || appVersion == "" || comment == ""){
            $("#errorMessage").html("All fields are mandatory");
    		return false;
        }
        return true;
    }
	
	//Code go back to previous page
	function backToPreviousPage() {
		location.href = contextPath+"/cf/pml"
	}
	
	$(function() {
	    <#if (resultSet)??>
        	<#list resultSet as resultSetList>
        		$("#ownerId").val(''${resultSetList?api.get("owner_id")}'');
        		$("#ownerType").val(''${resultSetList?api.get("owner_type")}'');
        		$("#propertyName").val(''${resultSetList?api.get("property_name")}'');
        		$("#propertyValue").val(''${resultSetList?api.get("property_value")}'');
        		$("#comment").val(''${resultSetList?api.get("comments")}'');
        		$("#appVersion").val(''${resultSetList?api.get("app_version")}'');
        	</#list>
        </#if>
        
        <#if (requestDetails?api.get("ownerId")) != "">
            edit = 1;
        </#if>
	});
</script>
	', 'admin', NOW(), NULL, NULL);
  
replace into dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('8a80cb8174bf3b360174bfe666920014', '8a80cb8174bf3b360174bfae9ac80006', '<#if  (formData?api.getFirst("edit"))?has_content>
	update jws_property_master SET
   owner_id = ''${formData?api.getFirst("ownerId")}''  
  ,owner_type = ''${formData?api.getFirst("ownerType")}'' 
  ,property_name = ''${formData?api.getFirst("propertyName")}'' 
  ,property_value = ''${formData?api.getFirst("propertyValue")}'' 
  ,last_modified_date = NOW()
  ,modified_by = ''admin''
  ,app_version = ${formData?api.getFirst("appVersion")} 
  ,comments = ''${formData?api.getFirst("comment")}'' 
WHERE owner_id = ''${formData?api.getFirst("ownerId")}'' and owner_type = ''${formData?api.getFirst("ownerType")}'' and property_name = ''${formData?api.getFirst("propertyName")}'';

<#else>
REPLACE INTO jws_property_master (
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
   ''${formData?api.getFirst("ownerId")}'' 
  ,''${formData?api.getFirst("ownerType")}''
  ,''${formData?api.getFirst("propertyName")}''
  ,''${formData?api.getFirst("propertyValue")}''
  ,0
  ,NOW()
  ,''admin''
  ,${formData?api.getFirst("appVersion")}
  ,''${formData?api.getFirst("comment")}''
);
</#if>', 1, NULL);

replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('8a80cb8174bf3b360174bf78e6780003', 'property-master-listing', '<head>
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
		<h2 class="title-cls-name float-left">Property Master Listing</h2> 
		<div class="float-right">
			<form id="addEditProperty" action="/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="8a80cb8174bf3b360174bfae9ac80006"/>
                <input type="hidden" name="ownerId" id="ownerId" value=""/>
                <input type="hidden" name="ownerType" id="ownerType" value=""/>
                <input type="hidden" name="propertyName" id="propertyName" value=""/>
                <button type="submit" class="btn btn-primary"> Add Property </button>
            </form>
			<span onclick="backToWelcomePage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="propertyMasterListingGrid"></div>

	<div id="snackbar"></div>
</div>



<script>
	contextPath = "";
	let grid;
	$(function () {
	//Add all columns that needs to be displayed in the grid
		let colM = [
			{ title: "Owner Id", width: 130, dataIndx: "owner_id", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Owner Type", width: 130, dataIndx: "owner_type", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Property Name", width: 130, dataIndx: "property_name", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Property Value", width: 130, dataIndx: "property_value", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Modified By", width: 130, dataIndx: "modified_by", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Comments", width: 130, dataIndx: "comments", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Action", width: 50, dataIndx: "action", align: "center", halign: "center", render: manageRecord}
		];
	
	//System will fecth grid data based on gridId
		grid = $("#propertyMasterListingGrid").grid({
	      gridId: "propertyMasterGrid",
	      colModel: colM
	  	});
	
	});
	
	//Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
	function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
		return ''<span id="''+rowIndx+''" onclick="createNew(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
	}
	
	//Add logic to navigate to create new record
	function createNew(element) {
		let rowData = $( "#propertyMasterListingGrid" ).pqGrid("getRowData", {rowIndxPage: element.id});
		$("#ownerId").val(rowData.owner_id);
		$("#ownerType").val(rowData.owner_type);
		$("#propertyName").val(rowData.property_name);
		$("#addEditProperty").submit();
	}

	//Code go back to previous page
	function backToWelcomePage() {
        location.href = contextPath+"/cf/home";
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL);

replace into grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type) VALUES
('propertyMasterGrid', 'Property master listing', 'Property master listing grid', 'jws_property_master', '*', 1);

replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
(UUID(), 'system-form-html-template', '
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
		<h2 class="title-cls-name float-left">Add Edit Details</h2> 
		<div class="clearfix"></div>		
	</div>
  <form method="post" name="addEditForm" id="addEditForm">
    
    <#if (columnDetails)??>
    	<#list columnDetails as columnDetailsList>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="${columnDetailsList?api.get(''columnName'')}" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              ${columnDetailsList?api.get("fieldName")!""}
		            </label>
				<input type="${columnDetailsList?api.get(''columnType'')}" id="${columnDetailsList?api.get(''columnName'')}" name="${columnDetailsList?api.get(''columnName'')}"  value="" maxlength="${columnDetailsList?api.get(''columnSize'')}" class="form-control">
			</div>
		</div>
    	</#list>
    </#if>
    
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
	let formId;
	contextPath = "${contextPath}";
	
	//Add logic to save form data
	function saveData (){
		let formData = $("#addEditForm").serialize()+ "&formId="+formId;
		$.ajax({
		     		type : "POST",
		     		url : contextPath+"/cf/sdf",
		     		data : formData,
            success: function(data) {
              backToPreviousPage();
            }
		     	});
	}
	
	//Code go back to previous page
	function backToPreviousPage() {
		location.href = contextPath+"/cf/home";
	}
</script>
', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL);

SET FOREIGN_KEY_CHECKS=1;