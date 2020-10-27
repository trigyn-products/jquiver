SET FOREIGN_KEY_CHECKS=0;


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
('42bf58ce-09fa-11eb-a894-f48e38ab8cd7', 'home', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script> 
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<div class="container">
    <div class="page-header">
        <h2 class="maintitle_name">JQuiver</h2>
        <p>
            <i>
                We take an opinionated view of the Spring platform and third-party 
                libraries so you can get your project started with minimum fuss.
            </i>
        </p>
    </div>

	<div class="list-group custom-list-home">
		<a href="../cf/mg" class="list-group-item list-group-item-action">
			<div class="home_list_icon"><img src="/webjars/1.0/images/manage_master.svg"></div> 
			<div class="home_list_content">
				<div class="d-flex w-100 justify-content-between">
					<h5 class="mb-1"> <span>${messageSource.getMessage(''jws.masterGenerator'')}</span></h5>
					<small>Today</small>
				</div>
				<p class="mb-1">Built using freemarker, supports pq-grid </p>
				<small>Now any master modules will be created without much efforts</small>
			</div>
		</a>
		<a href="../cf/gd" class="list-group-item list-group-item-action">
			<div class="home_list_icon"><img src="/webjars/1.0/images/grid.svg"></div> 
			<div class="home_list_content">
				<div class="d-flex w-100 justify-content-between">
					<h5 class="mb-1"> <span>${messageSource.getMessage(''jws.gridUtils'')}</span></h5>
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
					<h5 class="mb-1">${messageSource.getMessage(''jws.templating'')}</h5>
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
					<h5 class="mb-1">${messageSource.getMessage(''jws.multilingual'')}</h5>
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
					<h5 class="mb-1">${messageSource.getMessage(''jws.typeAheadAutocomplete'')}</h5>
					<small class="text-muted">Today</small>
				</div>
				<p class="mb-1">Built using Jquery plugin, rich-autocomplete to get data lazily</p>
				<small class="text-muted">Now any autocomplete component which handles dynamic creation of query will be created without much efforts</small>
			</div>
		</a>
        
		<a href="../cf/fucl" class="list-group-item list-group-item-action">
			<div class="home_list_icon"><img src="/webjars/1.0/images/manage_master.svg"></div> 
			<div class="home_list_content">
				<div class="d-flex w-100 justify-content-between">
					<h5 class="mb-1"> <span>${messageSource.getMessage(''jws.fileUploadConfig'')}</span></h5>
					<small>Today</small>
				</div>
				<p class="mb-1">Built using freemarker, supports pq-grid </p>
				<small>Configure file upload</small>
			</div>
		</a>
		
		<a href="../cf/dfl" class="list-group-item list-group-item-action">
			<div class="home_list_icon"><img src="/webjars/1.0/images/daynamicreport.svg"></div> 
			<div class="home_list_content">
				<div class="d-flex w-100 justify-content-between">
					<h5 class="mb-1">${messageSource.getMessage(''jws.formBuilder'')}</h5>
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
					<h5 class="mb-1">${messageSource.getMessage(''jws.restAPIBuilder'')}</h5>
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
					<h5 class="mb-1">${messageSource.getMessage(''jws.siteLayout'')}</h5>
					<small class="text-muted">Today</small>
				</div>
				<p class="mb-1">Built using Freemarker templating engine </p>
				<small class="text-muted">Create menu for your application.</small>
			</div>
		</a>
				        
		<a href="../cf/dbm" class="list-group-item list-group-item-action">
			<div class="home_list_icon"><img src="/webjars/1.0/images/dashboard.svg"></div> 
			<div class="home_list_content">
				<div class="d-flex w-100 justify-content-between">
					<h5 class="mb-1">${messageSource.getMessage(''jws.dashboard'')}</h5>
					<small class="text-muted">Today</small>
				</div>
				<p class="mb-1">Built using Freemarker templating engine and spring resource bundles</p>
				<small class="text-muted">Now create the daily reporting, application usage, trends dashboard for your web application and control it with our dashboard admin panel.</small>
			</div>
		</a>
		
		<a href="../cf/nl" class="list-group-item list-group-item-action">
			<div class="home_list_icon"><img src="/webjars/1.0/images/notification.svg"></div> 
			<div class="home_list_content">
				<div class="d-flex w-100 justify-content-between">
					<h5 class="mb-1">${messageSource.getMessage(''jws.notification'')}</h5>
					<small class="text-muted">Today</small>
				</div>
				<p class="mb-1">Built using Freemarker templating engine.</p>
				<small class="text-muted">Create your application notification with ease and control the duration and context where to show it, (cross platform.)</small>
			</div>
		</a>
		
		<a href="../cf/um" class="list-group-item list-group-item-action">
			<div class="home_list_icon"><img src="/webjars/1.0/images/Property_master_icon.svg"></div> 
			<div class="home_list_content">
				<div class="d-flex w-100 justify-content-between">
					<h5 class="mb-1">${messageSource.getMessage(''jws.userManagement'')}</h5>
					<small class="text-muted">Today</small>
				</div>
				<p class="mb-1">Built using Freemarker templating engine,supports pq-grid </p>
				<small class="text-muted">Manage users for your application.</small>
			</div>
		</a>
		
		<a href="../cf/pml" class="list-group-item list-group-item-action">
			<div class="home_list_icon"><img src="/webjars/1.0/images/Property_master_icon.svg"></div> 
			<div class="home_list_content">
				<div class="d-flex w-100 justify-content-between">
					<h5 class="mb-1">${messageSource.getMessage(''jws.applicationConfiguration'')}</h5>
					<small class="text-muted">Today</small>
				</div>
				<p class="mb-1">Built using Freemarker templating engine </p>
				<small class="text-muted">Create menu for your application.</small>
			</div>
		</a>
		
	</div>
</div>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW());

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('1dff39e8-001f-11eb-97bf-e454e805e22f', 'template-listing', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/1.0/pqGrid/pqgrid.min.js"></script>   
<script src="/webjars/1.0/gridutils/gridutils.js"></script>   
<link rel="stylesheet" href="/webjars/1.0/pqGrid/pqgrid.min.css" /> 
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>
<script>

 

</script>
<div class="container">
        <div class="topband">
        <h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.templating'')}</h2> 
        <div class="float-right">
            <#if environment == "dev">
                <input id="downloadTemplate" class="btn btn-primary" onclick= "downloadTemplate();" name="downloadTemplate" value="Download Template" type="button">
                <input id="uploadTemplate" class="btn btn-primary" onclick= "uploadTemplate();" name="uploadTemplate" value="Upload Template" type="button">
            </#if>
            <input id="addFreemarkerTemplate" onclick="submitForm()" class="btn btn-primary" name="addFreemarkerTemplate" value="Add Template" type="button">
            <span onclick="backToWelcomePage();">
            <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
           </span>    
        </div>
        
        <div class="clearfix"></div>        
        </div>
        
        <div id="divTemplateGrid"></table>

 

</div>

 


<form action="${(contextPath)!''''}/cf/aet" method="GET" id="formFMRedirect">
    <input type="hidden" id="vmMasterId" name="vmMasterId">
</form>
<script>
    contextPath = "${(contextPath)!''''}";
    function backToWelcomePage() {
        location.href = contextPath+"/cf/home";
    }
    $(function () {
    	let formElement = $("#formFMRedirect")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("template-manage-details", formDataJson);
		
        let colM = [
            { title: "", hidden: true, sortable : false, dataIndx: "templateId" },
            { title: "Template Name", width: 130, align: "center", sortable : true, dataIndx: "templateName", align: "left", halign: "center",
            filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
            { title: "Created By", width: 100, align: "center",  sortable : true, dataIndx: "createdBy", align: "left", halign: "center",
            filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
            { title: "Updated By", width: 160, align: "center", sortable : true, dataIndx: "updatedBy", align: "left", halign: "center",
            filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
            { title: "Updated Date", width: 200, align: "center", sortable : true, dataIndx: "updatedDate", align: "left", halign: "center",
            filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
            { title: "Action", width: 50, align: "center", render: editTemplate, dataIndx: "action" }
        ];
     let grid = $("#divTemplateGrid").grid({
          gridId: "templateListingGrid",
          colModel: colM
      });
    });
    
    function editTemplate(uiObject) {
        const templateId = uiObject.rowData.templateId;
		const templateName = uiObject.rowData.templateName;
            <#if environment == "dev">
                 let element = "<span id=''"+templateId+"''  class= ''grid_action_icons''><i class=''fa fa-pencil''></i></span>";
                 element = element + "<span id=''"+templateId+"'' class= ''grid_action_icons'' onclick=''downloadTemplateById(this)''><i class=''fa fa-download''></i></span>";
				 element = element + "<span id=''"+templateId+"_upload'' name=''"+templateName+"'' class= ''grid_action_icons'' onclick=''uploadTemplateById(this)''><i class=''fa fa-upload''></i></span>";
                 return element;
            <#else>
                 return ''<span id="''+templateId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
            </#if>
    }
    
    function downloadTemplateById(thisObj){
	  	let templateId = thisObj.id;
	  	$.ajax({
			url:"/cf/dtbi",
			type:"POST",
	        data:{
	        	templateId : templateId,
	        },
			success : function(data) {
			  showMessage("Template downloaded successfully", "success");
			},
			error : function(xhr, error){
			  showMessage("Error occurred while downloading template", "error");
			},
	    });  
  	}
    
	function uploadTemplateById(thisObj){
	  	let templateId = thisObj.id;
		let templateName = $("#"+templateId).attr("name");
	  	$.ajax({
			url:"/cf/utdbi",
			type:"POST",
	        data:{
	        	templateName : templateName,
	        },
			success : function(data) {
			  showMessage("Template uploaded successfully", "success");
			},
			error : function(xhr, error){
			  showMessage("Error occurred while uploading template", "error");
			},
	    });  
  	}
	
    function submitForm(sourceElement) {
		let moduleId = "";
		if(sourceElement !== undefined){
			moduleId = sourceElement.id
		}
      	$("#vmMasterId").val(moduleId);
      	$("#formFMRedirect").submit();
    }
    
    <#if environment == "dev">
        function downloadTemplate(){
            $.ajax({
                url:"/cf/dtl",
                type:"POST",
                success : function(data) {
				  showMessage("Templates downloaded successfully", "success");
				},
				error : function(xhr, error){
				  showMessage("Error occurred while downloading templates", "error");
				},
                
            });
        }
        function uploadTemplate(){
            $.ajax({
                url:"/cf/utd",
                type:"POST",
                success : function(data) {
				  showMessage("Template uploaded successfully", "success");
				},
				error : function(xhr, error){
				  showMessage("Error occurred while uploading template", "error");
				},
                
            });
        }
    </#if>
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL);

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
('4d91fbd8-09fa-11eb-a894-f48e38ab8cd7', 'template-manage-details', '<head>
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
		<#if (templateDetails.templateId)?? && (templateDetails.templateId)?has_content>
		    <h2 class="title-cls-name float-left">Edit Template Details</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Add Template Details</h2> 
        </#if> 
        <div class="float-right">
                                             
        <span onclick="templateMaster.backToTemplateListingPage();">
        	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
        </span>              
        </div>
                              
        <div class="clearfix"></div>                         
    </div>

	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	<div class="row">
	    	<div class="col-9">
				<div class="col-inner-form full-form-fields">
					 <label for="vmName"><span class="asteriskmark">*</span>Template Name </label>                                                                                                                                       
					 <input type="text" class="form-control" value="${(templateDetails.templateName)!}" maxlength="100" name="vmName" id="vmName">                                                                                                                       
				</div>
			</div>
			
		<#if (templateDetails.templateId)?? && (templateDetails.templateId)?has_content
			&& versionDetailsMap?? && versionDetailsMap?has_content>
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="vmName">Compare with </label>
					<select class="form-control" id="versionId" onchange="templateMaster.getTemplateData();" name="versionId" title="Template Versions">
						<option value="" selected>Select</option>
						<#list versionDetailsMap as versionId, updatedDate>
								<option value="${versionId}">${updatedDate}</option>
						</#list>
					</select> 
				</div>
			</div>
		</#if>
      
		<div id="defaultTemplateDiv" class="col-3" style="display: none;">
			<div class="col-inner-form full-form-fields">
				<label for="defaultTemplateId">Default template </label>                                                                                                                                       
				<select class="form-control" id="defaultTemplateId" name="defaultTemplateId" title="Default template"> </select>                                                                                                                    
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
               
		<div class="row margin-t-10">
			<div class="col-12">
				<div class="float-right">
					<div class="btn-group dropdown custom-grp-btn">
			            <div id="savedAction">
		    	            <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''template-manage-details'', this, templateMaster.validateSaveVelocity.bind(templateMaster), templateMaster.backToTemplateListingPage);">${messageSource.getMessage("jws.saveAndReturn")}</button>
		                </div>
		        	<button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
		            	<div class="dropdown-menu action-cls"  id="actionDiv">
		                	<ul class="dropdownmenu">
		                    	<li id="saveAndCreateNew" onclick="typeOfAction(''template-manage-details'', this, templateMaster.validateSaveVelocity.bind(templateMaster), templateMaster.backToTemplateListingPage);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
		                        <li id="saveAndEdit" onclick="typeOfAction(''template-manage-details'', this, templateMaster.validateSaveVelocity.bind(templateMaster), templateMaster.backToTemplateListingPage);">${messageSource.getMessage("jws.saveAndEdit")}</li>
		                    </ul>
	                    </div> 
	                </div>
					<span onclick="templateMaster.backToTemplateListingPage();">
						<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
					</span> 
				</div>
			</div>
		</div>

    <#if versionDetailsMap?? && versionDetailsMap?has_content>
		<div class="row">                                                                                                
			<div class="col-12">
				<div class="html_script">
					<div class="grp_lblinp">
						<div id="diffContainer" class="ace-editor-container">
							<div id="diffEditor" class="ace-editor"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
    </#if>
</div>

<script>
	contextPath = "${(contextPath)!''''}";
	const ftlTemplateId = "${(templateDetails.templateId)!0}";
	const isEdit = "${(templateDetails.templateId)!''''}";
	let templateMaster;
	let defaultTemplates = new Array();
	$(function () {
		templateMaster = new TemplateEngine(ftlTemplateId);
		templateMaster.initPage();
		if(ftlTemplateId == 0) {
			$("#defaultTemplateDiv").show();
			$.ajax({
				type : "GET",
				url : contextPath+"/api/defaultTemplates",
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
		savedAction("template-manage-details", isEdit);
		hideShowActionButtons();
	});
</script>
<script src="/webjars/1.0/template/template.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW());
 


REPLACE INTO  template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
('8ba1a465-09fa-11eb-a894-f48e38ab8cd7', 'menu-module-listing', '<head>
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
		
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.siteLayout'')}</h2> 
		<div class="float-right">
		<span>
  		    <input id="configHomePage" class="btn btn-primary" name="configHomePage" value="Configure Default Page" type="button" onclick="configHomePage(this)">
		</span>
		<span>
  		    <input id="addModule" class="btn btn-primary" name="addGridDetails" value="Add Module" type="button" onclick="submitForm();">
		</span>

         <span onclick="backToWelcomePage();">
          	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
         </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divModuleListing"></div>

</div>


<form action="${(contextPath)!''''}/cf/aem" method="POST" id="formMuRedirect">
	<input type="hidden" id="moduleId" name="module-id">
</form>
<form action="${(contextPath)!''''}/cf/chp" method="POST" id="configHomeForm">
	
</form>
<script>
	contextPath = "${(contextPath)!''''}";
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
	$(function () {
		let formElement = $("#formMuRedirect")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("module-manage-details", formDataJson);
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
	        { title: "Inside Menu", width: 100, align: "center", dataIndx: "isInsideMenu", align: "left", halign: "center", render: formatIsInsideMenu},
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
  
  	function formatIsInsideMenu(uiObject){
  		const isInsideMenu = uiObject.rowData.isInsideMenu;
  		if(isInsideMenu == 1){
  			return "Yes";
  		}
  		return "No";
  	}
  	
  	function submitForm(sourceElement) {
		let moduleId;
		if(sourceElement !== undefined){
			moduleId = sourceElement.id
		}
		$("#moduleId").val(moduleId);
		$("#formMuRedirect").submit();
	}
	
	function configHomePage() {
		$("#configHomeForm").submit();
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW());
 


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('89ee344b-03f6-11eb-a183-e454e805e22f', 'module-manage-details', '<head>
	<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css" />
	<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
	<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
	<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
	<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.js"></script>
    <script src="/webjars/1.0/typeahead/typeahead.js"></script>
    <link rel="stylesheet" href="/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
	<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />

</head>

<div class="container">

	<div class="topband">
		<#if (moduleDetailsVO?api.getModuleId())??>
		    <h2 class="title-cls-name float-left">${messageSource.getMessage("jws.editModule")}</h2> 
        <#else>
            <h2 class="title-cls-name float-left">${messageSource.getMessage("jws.addModule")}</h2> 
        </#if>
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

			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="targetLookupType" style="white-space:nowrap"><span class="asteriskmark">*</span>Context Type</label>
					<select id="targetLookupType" name="targetLookupType" onchange="addEditModule.getTargeTypeNames();" class="form-control">
						<#if (moduleTargetLookupVOList)??>
							<#list moduleTargetLookupVOList as moduleTargetLookupVO>
									<#if (moduleTargetLookupVO?api.getLookupId())?? && (moduleDetailsVO?api.getTargetLookupId())?? 
										&& (moduleTargetLookupVO?api.getLookupId()) == moduleDetailsVO?api.getTargetLookupId()>
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
					<label for="targetTypeName" style="white-space:nowrap">Context Name</label>
						<div class="search-cover">
						<input type="text" id="targetTypeName" value= "" name="targetTypeName" class="form-control">
						<i class="fa fa-search" aria-hidden="true"></i>
				</div>
				<input type="hidden" id="targetTypeNameId" value="${(moduleDetailsVO?api.getTargetTypeId())!''''}" name="targetTypeNameId" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="moduleURL" style="white-space:nowrap"><span class="asteriskmark">*</span>Module URL</label>
					<span><label style="background: lightgrey;" class="float-right">${(urlPrefix)!''''}<label></span>
					<input type="text"  id = "moduleURL" name = "moduleURL" value = "${(moduleDetailsVO?api.getModuleURL())!''''}" maxlength="200" class="form-control">
				</div>
			</div>
			
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="contextType">Inside Menu</label>
					<div class="onoffswitch">
						<input type="hidden" id="isInsideMenu" name="isInsideMenu" value="${(moduleDetailsVO?api.getIsInsideMenu())!''''}">
						<#if (moduleDetailsVO?api.getIsInsideMenu())?? && moduleDetailsVO?api.getIsInsideMenu() == 1>
							<input type="checkbox" id="insideMenuCheckbox" onchange="addEditModule.insideMenuOnChange();" checked name="insideMenuCheckbox" class="onoffswitch-checkbox">
						<#else>
							<input type="checkbox" id="insideMenuCheckbox" onchange="addEditModule.insideMenuOnChange();" name="insideMenuCheckbox" class="onoffswitch-checkbox">
						</#if>
						<label class="onoffswitch-label" for="insideMenuCheckbox">
							<span class="onoffswitch-inner"></span>
							<span class="onoffswitch-switch"></span>
						</label>
					</div>
				</div>
			</div>
			
			<input type="hidden" id = "parentModuleId" name="parentModuleId" value="${(moduleDetailsVO?api.getParentModuleId())!''''}">
			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="parentModuleName" style="white-space:nowrap">${messageSource.getMessage("jws.parentModuleName")}</label>
					<select id="parentModuleName" name="parentModuleName" class="form-control" onchange="addEditModule.getSequenceByParent()">
						<option value="">Root</option>
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
					<label for="sequence" style="white-space:nowrap">${messageSource.getMessage("jws.sequence")}</label>
					<#if (moduleDetailsVO?api.getModuleId())?? && (moduleDetailsVO?api.getModuleId())?has_content>
						<input type="number"  id = "sequence" name = "sequence" value = "${(moduleDetailsVO?api.getSequence())!''''}" maxlength="100" class="form-control">
					<#else>
						<input type="number"  id = "sequence" name = "sequence" value = "${(defaultSequence)!''''}" maxlength="100" class="form-control">
					</#if>
				</div>
			</div>

			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label  class="pull-left label-name-cls full-width"><span class="asteriskmark">*</span>Roles</label>
					<div id="roles">
						<#if userRoleVOs??>
							<#list userRoleVOs as userRoleVO>
								<lable for="${(userRoleVO?api.getRoleId())!''''}">
									${(userRoleVO?api.getRoleName())!''''}
								</label>
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
					<div class="btn-group dropdown custom-grp-btn">
                        <div id="savedAction">
                            <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''module-manage-details'', this, addEditModule.saveModule.bind(addEditModule), addEditModule.backToModuleListingPage);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                        </div>
                        <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();" > </button>
                        <div class="dropdown-menu action-cls"  id="actionDiv">
                            <ul class="dropdownmenu">
                                <li id="saveAndCreateNew" onclick="typeOfAction(''module-manage-details'', this, addEditModule.saveModule.bind(addEditModule), addEditModule.backToModuleListingPage);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                                <li id="saveAndEdit" onclick="typeOfAction(''module-manage-details'', this, addEditModule.saveModule.bind(addEditModule), addEditModule.backToModuleListingPage);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                            </ul>
                        </div>  
                    </div>
                    <input id="cancelBtn" type="button" class="btn btn-secondary" value="${messageSource.getMessage(''jws.cancel'')}" onclick="addEditModule.backToModuleListingPage();">
		        </div>
			</div>
		</div>

	</div>

<script>
contextPath = "${(contextPath)!''''}";
let addEditModule;
let autocomplete;
let sequence = "${(moduleDetailsVO?api.getSequence())!''''}";
$(function() {
    let moduleTypeId = "${(moduleDetailsVO?api.getTargetTypeId())!''''}";
    let moduleName = "${(moduleDetailsVO?api.getTargetLookupName())!''''}";
	let parentModuleId = "${(moduleDetailsVO?api.getParentModuleId())!''''}";
	let targetLookupId = "${(moduleDetailsVO?api.getTargetLookupId())!''''}";
	let moduleId = "${(moduleDetailsVO?api.getModuleId())!''''}";

	savedAction("module-manage-details", moduleId);
	hideShowActionButtons();
	
	if(targetLookupId === ""){
		$("#targetLookupType").val(6);
	}
    let selectedTargetDetails = new Object();
	addEditModule = new AddEditModule(moduleTypeId, parentModuleId);
    if(moduleTypeId != "") {
        selectedTargetDetails["targetTypeId"] = moduleTypeId;
        selectedTargetDetails["targetTypeName"] = moduleName;
    }
	let autocompleteIdByType = addEditModule.getAutocompleteId();
    autocomplete = $(''#targetTypeName'').autocomplete({
        autocompleteId: autocompleteIdByType,
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
            $("#targetTypeName").blur();
            $("#targetTypeNameId").val(item.targetTypeId)
        }, 	
    }, selectedTargetDetails);
      addEditModule.getTargeTypeNames(''isAddEdit'');
	  hideShowActionButtons();
});

</script>
<script src="/webjars/1.0/menu/addEditModule.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL);


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('9378ee23-09fa-11eb-a894-f48e38ab8cd7', 'home-page', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />

</head>
	<nav class="navbar navbar-dark sticky-top blue-bg flex-md-nowrap p-0 shadow ">
		<a class="navbar-brand col-md-3 col-lg-2 mr-0 px-3" href="/cf/home">JQuiver</a>
        <span class="hamburger float-left" id="openbtni" class="closebtn" onclick="homePageFn.openNavigation()">
			<i class="fa fa-bars" aria-hidden="true"></i>
		</span>
        <span id="closebtni" class="closebtn float-left" onclick="homePageFn.closeNavigation()">×</span>
        <ul class="navbar-nav px-3 float-right">
            <#if loggedInUserName?? && loggedInUserName != "anonymous">
                <li class="nav-item text-nowrap">
                    <div class="row margin-r-5 profile-tray">
                        <a class="nav-link" href="/cf/profile">${loggedInUserName}</a>
                        <a class="nav-link signout-icon" href="/logout"><i class="fa fa-sign-out" aria-hidden="true"></i></a>
                    </div>
                </li>
            </#if>
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
							<#if ((moduleDetailsVO?api.getSubModuleCount())?? && (moduleDetailsVO?api.getSubModuleCount()) gte 1) || moduleDetailsVO?api.getModuleURL() == "#">
								<li class="nav-item">
									<a class="nav-link clickable panel-collapsed" >${moduleDetailsVO?api.getModuleName()!''''}<i class="fa fa-caret-down" aria-hidden="true"></i>
                                    </a>
									<div class="collapse collapsein">
										<ul class="subcategory">
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
		<div id="bodyDiv" onclick="homePageFn.closeNavigation();">
			<#include "template-body">
		</div>
	</main>
	
	</div>
</div>
<div class="footer bg-dark">
    <div class="text-center">
        <small>Copyright &copy; JQuiver</small>
        <small class="float-right">Version 1.3.9</small>
    </div>
</div>

<script>
	const contextPathHome = "${contextPath}";
	let homePageFn;
	
	$(function() {
		  
	  const homePage = new HomePage();
	  homePageFn = homePage.fn;
	  homePageFn.collapsableMenu();
	});


</script>
<script src="/webjars/1.0/home/home.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL);


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
('99a707e5-09fa-11eb-a894-f48e38ab8cd7', 'error-page', '<head>
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
	<div class="row">

        <div class="col-8">
           <div class="error_block">
        

        <div class="error-contentblock">
        <div class="error-code">${(statusCode)!''''}</h2></div>
       
        <#if (statusCode)?? && (statusCode)?has_content>
          <#if statusCode == 404>
            <h2 class="errorcontent">Oops!!! Page not found</h2> 
          <#elseif statusCode == 500>
            <h2 class="errorcontent">Oops!!! Something went wrong</h2> 
            <span class="error-alert float-left" onclick="showHideErrorInfo()">
              <i class="fa fa-exclamation-triangle" aria-hidden="true"></i>
            </span>
          </#if>
        <#else>
          <h2 class="errorcontent">Oops!!! Something went wrong</h2> 
        </#if>

        </div>
        

        <div id="errorDetailsDiv" class="errorDetailsDivcls">
          ${(errorMessage)!''''}
        </div>
            
        </div>
    </div>

     <div class="col-4">
             <img class="errorImg" src="/webjars/1.0/images/error1.jpg">
     </div>

	</div>
</div>


<script>
function showHideErrorInfo(){
  $("#errorDetailsDiv").slideToggle();
}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW());


REPLACE INTO  template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
('9ea3cd47-09fa-11eb-a894-f48e38ab8cd7', 'config-home-page', '<head>
	<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
	<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css" />
	<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
	<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
	<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
	<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.js"></script>
    <script src="/webjars/1.0/typeahead/typeahead.js"></script>
    <link rel="stylesheet" href="/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
	<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>

<div class="container">

	<div class="topband">
		<#if (moduleDetailsVO?api.getModuleId())??>
		    <h2 class="title-cls-name float-left">Edit Default Page</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Add Default Page</h2> 
        </#if>
		<div class="float-right">
			<span onclick="backToModuleListingPage();">
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
					<label for="targetLookupType" style="white-space:nowrap"><span class="asteriskmark">*</span>Context Type</label>
					<select id="targetLookupType" name="targetLookupType" onchange="getTargeTypeNames();" class="form-control">
						<option value="">Select</option>
						<#if (targetLookupVOList)??>
							<#list targetLookupVOList as targetLookupVO>
									<#if (targetLookupVO?api.getLookupId())?? && (moduleDetailsVO?api.getTargetLookupId())?? 
                  && (targetLookupVO?api.getLookupId()) == (moduleDetailsVO?api.getTargetLookupId())>
										<option value="${targetLookupVO?api.getLookupId()}" selected>${targetLookupVO?api.getDescription()!''''}</option>
									<#elseif (targetLookupVO?api.getLookupId()) != 6>
										<option value="${targetLookupVO?api.getLookupId()}">${targetLookupVO?api.getDescription()!''''}</option>
									</#if>
							</#list>
						</#if>
					</select>
				</div>
			</div>


			<div class="col-3">
				<div class="col-inner-form full-form-fields">
					<label for="targetTypeName" style="white-space:nowrap">Context Name</label>
						<div class="search-cover">
						<input type="text" id="targetTypeName" value= "" name="targetTypeName" class="form-control">
						<i class="fa fa-search" aria-hidden="true"></i>
				</div>
				<input type="hidden" id="targetTypeNameId" value="${(moduleDetailsVO?api.getTargetTypeId())!''''}" name="targetTypeNameId" class="form-control">
				</div>
			</div>
    </div>
    
    		<div class="row">
			<div class="col-12">
				<div id="buttons" class="pull-right">
					<input id="saveBtn" type="button" class="btn btn-primary" value="${messageSource.getMessage(''jws.save'')}" onclick="saveHomeModule();">
					<input id="cancelBtn" type="button" class="btn btn-secondary" value="${messageSource.getMessage(''jws.cancel'')}" onclick="backToModuleListingPage();">
		        </div>
			</div>
		</div>


</div>
<script>
const contextPath = "${(contextPath)!''''}";
let autocomplete;
$(function() {
	let moduleTypeId = "${(moduleDetailsVO?api.getTargetTypeId())!''''}";
	let moduleName = "${(moduleDetailsVO?api.getTargetLookupName())!''''}";
	let selectedTargetDetails = new Object();
    if(moduleTypeId != "") {
        selectedTargetDetails["targetTypeId"] = moduleTypeId;
        selectedTargetDetails["targetTypeName"] = moduleName;
    }
	let autocompleteIdByType = getTargeTypeNames();
    autocomplete = $(''#targetTypeName'').autocomplete({
        autocompleteId: autocompleteIdByType,
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
            $("#targetTypeName").blur();
            $("#targetTypeNameId").val(item.targetTypeId)
        }, 	
    }, selectedTargetDetails);
});

function backToModuleListingPage() {
	location.href = contextPath+"/cf/mul";
}

function getTargeTypeNames(){
    	let targetLookupId = $("#targetLookupType").find(":selected").val();
		let autocompleteId;
		if(targetLookupId == 1){
    		$("#targetTypeName").prop("disabled",false);
    		autocompleteId = "dashboardListing";
		} else if(targetLookupId == 2){
    		$("#targetTypeName").prop("disabled",false); 
    		autocompleteId = "dynamicForms";
		} else if(targetLookupId == 3){
    		$("#targetTypeName").prop("disabled",false);
    		autocompleteId = "dynarestListing";
		} else if(targetLookupId == 5){
    		$("#targetTypeName").prop("disabled",false);
    		autocompleteId = "templateListing";
		}
		if(autocomplete !== undefined){
			autocomplete.options.autocompleteId = autocompleteId;
		}
		return autocompleteId;
    }


function saveHomeModule(){
	let moduleId = $("#moduleId").val();
	let targetLookupTypeId = $("#targetLookupType").find(":selected").val();
	let targetTypeId = $("#targetTypeNameId").val();
	$.ajax({
		type : "POST",
		url : contextPath+"/cf/schm",
		async: false,
		data : {
			moduleId : moduleId,
			targetLookupTypeId : targetLookupTypeId,
			targetTypeId : targetTypeId
			
		},
		success : function(data) {
			$("#moduleId").val(data);
			$("#errorMessage").hide();
			showMessage("Information saved successfully", "success");
		},
	        
	    error : function(xhr, error){
	    	showMessage("Error occurred while saving", "error");
	    },
	        	
	});
}

</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW());

SET FOREIGN_KEY_CHECKS=1;
