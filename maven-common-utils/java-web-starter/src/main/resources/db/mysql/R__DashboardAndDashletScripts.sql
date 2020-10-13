REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
('365e2aa5-09ac-11eb-a027-f48e38ab8cd7', 'dashboard-listing', '<head>
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
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.dashboard'')}</h2> 
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
', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW());






REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
('410deeb3-09ac-11eb-a027-f48e38ab8cd7', 'dashboard-manage-details', '<head>
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
		<#if (dashboard?api.getDashboardId())??>
		    <h2 class="title-cls-name float-left">${messageSource.getMessage("jws.editDashboard")}</h2> 
        <#else>
            <h2 class="title-cls-name float-left">${messageSource.getMessage("jws.addDashboard")}</h2>  
        </#if> 
		
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
<script src="/webjars/1.0/dashboard/addEditDashboard.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW());


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
('448f4ab0-09ac-11eb-a027-f48e38ab8cd7', 'dashlet-listing', '<head>
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
				<input id="downloadDashlet" class="btn btn-primary" onclick= "dashletListing.downloadDashlet();" name="downloadDashlet" value="Download Dashlet" type="button">
				<input id="uploadDashlet" class="btn btn-primary" onclick= "dashletListing.uploadDashlet();" name="uploadDashlet" value="Upload Dashlet" type="button">
			</#if>
			<input class="btn btn-primary" name="createDashlet" value="${messageSource.getMessage(''jws.createNewDashlet'')}" type="button" onclick="dashletListing.submitForm(this)">
    		<span onclick="dashletListing.backToDashboarListing();">
    	  		<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
    	 	</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="divdDashletMasterGrid"></div>

</div>

<form action="${(contextPath)!''''}/cf/aedl" method="POST" id="formDMRedirect">
	<input type="hidden" id="dashletId" name="dashlet-id">
</form>
<script>
	let dashletListing;
	$(function () {
	dashletListing = new DashletListing();
		var colM = [
			{ title: "${messageSource.getMessage(''jws.dashletName'')}", width: 80, dataIndx: "dashletName" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.dashletTitle'')}", width: 80, dataIndx: "dashletTitle", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.createdBy'')}", width: 70, dataIndx: "createdBy" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.createdDate'')}", width: 30, dataIndx: "createdDate", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.updatedBy'')}", width: 70, dataIndx: "updatedBy" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.updatedDate'')}", width: 30, dataIndx: "updatedDate" , align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain",  listeners: ["change"] }},
			{ title: "${messageSource.getMessage(''jws.status'')}", width: 50, dataIndx: "status" , align: "left", halign: "center",render: dashletStatus},
			{ title: "${messageSource.getMessage(''jws.action'')}", width: 40, dataIndx: "action", align: "center", halign: "center", render: editDashlet}
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
		const dashletName = uiObject.rowData.dashletName;
		let element;
		<#if environment == "dev">
			element = "<span id=''"+dashletId+"'' class= ''grid_action_icons''><i class=''fa fa-pencil'' title=''${messageSource.getMessage("jws.editDashlet")}''></i></span>";
      		element = element + "<span id=''"+dashletId+"'' class= ''grid_action_icons'' onclick=''dashletListing.downloadDashletById(this)''><i class=''fa fa-download''></i></span>";
          element = element + "<span id=''"+dashletId+"_upload'' name=''"+dashletName+"'' class= ''grid_action_icons'' onclick=''dashletListing.uploadDashletById(this)''><i class=''fa fa-upload''></i></span>";
		<#else>
			element = "<span id=''"+dashletId+"'' class= ''grid_action_icons''  onclick=''dashletListing.submitForm(this)'' title=''${messageSource.getMessage("jws.editDashlet")}''><i class=''fa fa-pencil''></i></span>";		
		</#if>
		return element;
	}
	

	
</script>
<script src="/webjars/1.0/dashlet/dashletListing.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW());


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
('49f6cfd9-09ac-11eb-a027-f48e38ab8cd7', 'dashlet-manage-details', '<head>
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
		<#if (dashletVO.dashletId)??>
		    <h2 class="title-cls-name float-left">${messageSource.getMessage("jws.editDashlet")}</h2> 
        <#else>
            <h2 class="title-cls-name float-left">${messageSource.getMessage("jws.addDashlet")}</h2> 
        </#if> 
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
												<#if (dashletProperty.toDisplay)?? && (dashletProperty.toDisplay) == 1>
													<input type="checkbox" name="toDisplay" id="toDisplay_${(dashletProperty.sequence)!''''}" class="form-control" checked/>
												<#else>
													<input type="checkbox" name="toDisplay" id="toDisplay_${(dashletProperty.sequence)!''''}" class="form-control"/>
												</#if>
								            	
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
	</textarea>
	 
	<textarea id="sqlContent" style="display: none">
	   	${(dashletVO.dashletQuery)!""}
	</textarea>
	
	 <div id="deletePropertyConfirm"></div>
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
<script src="/webjars/1.0/dashlet/addEditDashlet.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW());
  