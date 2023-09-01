SET FOREIGN_KEY_CHECKS=0;


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('42bf58ce-09fa-11eb-a894-f48e38ab8cd7', 'home', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script> 
<script src="${(contextPath)!''''}/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<div class="container">
    <div class="page-header homepageheader">
        <h2 class="maintitle_name">JQuiver</h2>
        <p>
            <i>
                We take an opinionated view of the Spring platform and third-party 
                libraries so you can get your project started with minimum fuss.
            </i>
        </p>
    </div>

    <div class="list-group custom-list-home">
    
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/mg" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/manage_master.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1"> <span>${messageSource.getMessage(''jws.masterGenerator'')}</span></h5>                   
                </div>
                <p class="mb-1">Built using freemarker, supports pq-grid </p>
                <small>Now any master modules will be created without much efforts</small>
            </div>
        </a>
        
        </div>
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/gd" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/grid.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1"> <span>${messageSource.getMessage(''jws.gridUtils'')}</span></h5>
                     
                </div>
                <p class="mb-1"> Built using pq-grid, and supporting it with generic queries to get data for grid based on the target databases. </p>
                <small>Now any master listing page will be created without much efforts</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/adl" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/autotype.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.typeAheadAutocomplete'')}</h5>
                     
                </div>
                <p class="mb-1">Built using Jquery plugin, rich-autocomplete to get data lazily</p>
                <small class="text-muted">Now any autocomplete component which handles dynamic creation of query will be created without much efforts</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/fucl" class="list-group-item list-group-item-action">
           	<div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/upload_management.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1"> <span>${messageSource.getMessage(''jws.fileUploadConfig'')}</span></h5>
                     
                </div>
                <p class="mb-1">Built using freemarker, supports pq-grid </p>
                <small>Configure file upload</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/te" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/template.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.templating'')}</h5>
                 
                </div>
                <p class="mb-1">Built using Freemarker templating engine </p>
                <small class="text-muted">Generates HTML web pages, e-mails, configuration files, etc. from template files and the data your application provides</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/dfl" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/daynamicreport.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.formBuilder'')}</h5>
                     
                </div>
                <p class="mb-1">Built using Freemarker templating engine </p>
                <small class="text-muted">Now create the dynamic forms for your web application, without writing any java code just by using freemarker</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/dynl" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/API_listing_icon.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.restAPIBuilder'')}</h5>
                     
                </div>
                <p class="mb-1">Built using Freemarker templating engine </p>
                <small class="text-muted">Now create the dynamic forms for your web application, without writing any java code just by using freemarker</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/mul" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/Menu_icon.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.siteLayout'')}</h5>
                     
                </div>
                <p class="mb-1">Built using Freemarker templating engine </p>
                <small class="text-muted">Create menu for your application.</small>
            </div>
        </a>
        </div>
        
        
                
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/rb" class="list-group-item list-group-item-action">
            <div class="home_list_icon m_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/database.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.multilingual'')}</h5>
                     
                </div>
                <p class="mb-1">Built using Spring interceptors and Locale Resolvers</p>
                <small class="text-muted">Any web application with users all around the world, i18n or L10n is important for better user interaction, so handle all these from the admin panel by storing it in database.</small>
            </div>
        </a>
        </div>                

        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/dbm" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/dashboard.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.dashboard'')}</h5>
                 
                </div>
                <p class="mb-1">Built using Freemarker templating engine and spring resource bundles</p>
                <small class="text-muted">Now create the daily reporting, application usage, trends dashboard for your web application and control it with our dashboard admin panel.</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/nl" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/notification.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.notification'')}</h5>
                     
                </div>
                <p class="mb-1">Built using Freemarker templating engine.</p>
                <small class="text-muted">Create your application notification with ease and control the duration and context where to show it, (cross platform.)</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/um" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/user_management.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.userManagement'')}</h5>
                 
                </div>
                <p class="mb-1">Built using Freemarker templating engine,supports pq-grid </p>
                <small class="text-muted">Manage users for your application.</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/scm" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/security_manager.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.securityManagement'')}</h5>
                 
                </div>
                <p class="mb-1">Built using Freemarker templating engine</p>
                <small class="text-muted">Secure your web application with some advanced security measures in place</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/pml" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/application_configuration.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.applicationConfiguration'')}</h5>
                     
                </div>
                <p class="mb-1">Built using Freemarker templating engine </p>
                <small class="text-muted">Create menu for your application.</small>
            </div>
        </a>
        </div>
        
        
        <div class="home_block col-12">
        <a href="${(contextPath)!''''}/cf/help" class="list-group-item list-group-item-action">
            <div class="home_list_icon"><img src="${(contextPath)!''''}/webjars/1.0/images/user_help.svg"></div> 
            <div class="home_list_content">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${messageSource.getMessage(''jws.helpManuals'')}</h5>
                     
                </div>
                <p class="mb-1">Help Manuals </p>
                <small class="text-muted">Create help manuals for your application.</small>
            </div>
        </a>
        </div>
		
	    <div class="home_block col-12">
			<div class="cm-iconboxwrap">
	            <a href="${(contextPath)!''''}/cf/vimp">
		            <div class="cm-importicon">
		                <div class="btn cm-impobtn outline"><i class="fa fa-download"></i> Import</div>
		            </div>
	            </a>

                <div class="home_list_content">
                     <small class="text-muted">Manage export and import of configurations.</small>
                </div>
	            <a href="${(contextPath)!''''}/cf/vexp">
		            <div class="cm-exporticon">
		                <div class="btn cm-expobtn outline">Export<i class="fa fa-upload"></i></div>
		            </div>
	             </a>
			</div>
        </div>
    </div>
</div>
<script>
	
	$(function() {
		localStorage.removeItem("imporatableData");
		localStorage.removeItem("importedIdList");
	});

</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);

REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('1dff39e8-001f-11eb-97bf-e454e805e22f', 'template-listing', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" /> 
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>   
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script>   
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/lang/en.js"></script> 
<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script>
</head>
<script>

 

</script>
<div class="container">
        <div class="topband">
        <h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.templating'')}</h2> 
        <div class="float-right">
        	Show:<select id="typeSelect" class="typeSelectDropDown" onchange="changeType()">   
                <option value="0">All</option>                   
                <option value="1" selected>Custom</option>                   
                <option value="2">System</option>                 
            </select>
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
        
		<div id="deleteHeader"></div>
        <div id="divTemplateGrid"></table>

 

</div>

 


<form action="${(contextPath)!''''}/cf/aet" method="GET" id="formFMRedirect">
    <input type="hidden" id="vmMasterId" name="vmMasterId">
</form>
<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
	<input type="hidden" id="entityName" name="entityName" value="jq_template_master">
    <input type="hidden" id="entityId" name="entityId">
	<input type="hidden" id="moduleName" name="moduleName">
	<input type="hidden" id="moduleType" name="moduleType" value="template">
	<input type="hidden" id="saveUrl" name="saveUrl" value="/cf/stdv">
	<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/te">
</form>
<script>
    contextPath = "${(contextPath)!''''}";
	function backToWelcomePage() {
        location.href = contextPath+"/cf/home";
	}
    $(function () {
		$("#typeSelect").each(function () {
	        $(this).val($(this).find("option[selected]").val());
	    });
    	let formElement = $("#formFMRedirect")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("template-manage-details", formDataJson);
		
        let colM = [
            { title: "", hidden: true, sortable : false, dataIndx: "templateId" },
            { title: "Template Name", width: 190, align: "center", sortable : true, dataIndx: "templateName", align: "left", halign: "center",
            	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
            { title: "Created By", width: 100, align: "center", hidden: true,  sortable : true, dataIndx: "createdBy", align: "left", halign: "center",
            	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
            { title: "Updated By", width: 100, align: "center", hidden: false, sortable : true, dataIndx: "updatedBy", align: "left", halign: "center",
            	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
            { title: "Last Updated Date", width: 100, align: "center", sortable : true, dataIndx: "updatedDate", align: "left", halign: "center", render: formatLastUpdatedDate},
            { title: "Action", width: 50, maxWidth: 145, align: "left", render: editTemplate, dataIndx: "action", sortable: false }
        ];
        let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "updatedDate",
        	sortDir: "down",
        };
        
     let grid = $("#divTemplateGrid").grid({
          gridId: "templateListingGrid",
          colModel: colM,
          dataModel: dataModel,
          additionalParameters: {"cr_templateTypeId":"str_1"}
      });
    });
    
	function templateType(uiObject){
		const templateTypeId = uiObject.rowData.templateTypeId;
		if(templateTypeId === 1){
			return "Default";
		}else{
			return "System";
		}
	}
	
	function formatLastUpdatedDate(uiObject){
        const lastUpdatedTs = uiObject.rowData.updatedDate;
        return formatDate(lastUpdatedTs);
    }
    
    function editTemplate(uiObject) {
        const templateId = uiObject.rowData.templateId;
		const templateName = uiObject.rowData.templateName;
		const revisionCount = uiObject.rowData.revisionCount;
            <#if environment == "dev">
                 let element = "<span id=''"+templateId+"''  class= ''grid_action_icons''><i class=''fa fa-pencil''></i></span>";
                 element = element + "<span id=''"+templateId+"'' class= ''grid_action_icons'' onclick=''downloadTemplateById(this)''><i class=''fa fa-download''></i></span>";
				 element = element + "<span id=''"+templateId+"_upload'' name=''"+templateName+"'' class= ''grid_action_icons'' onclick=''uploadTemplateById(this)''><i class=''fa fa-upload''></i></span>";
                 return element;
            <#else>
                let actionElement;
				actionElement = ''<span id="''+templateId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>'';
				if(revisionCount > 1){
					actionElement = actionElement + ''<span id="''+templateId+''_entity" name="''+escape(templateName)+''" onclick="submitRevisionForm(this)" class= "grid_action_icons"><i class="fa fa-history"></i></span>''.toString();
				}else{
					actionElement = actionElement + ''<span class= "grid_action_icons disable_cls"><i class="fa fa-history"></i></span>''.toString();
				}
            		if(uiObject.rowData.templateTypeId == 1) {
		        <#if loggedInUserRoleList?? && loggedInUserRoleList?size gt 0>
		        	<#list loggedInUserRoleList as loggedInUserRole>
		            	<#if (loggedInUserRole == "ADMIN")>    
		        			actionElement += ''<span onclick=\\''openDeletConfirmation(\"divTemplateGrid",\"''+templateId+''\",\"1b0a2e40-098d-11eb-9a16-f48e38ab9348\")\\'' class= "grid_action_icons" title="Delete"><i class="fa fa-trash "></i></span>''.toString();
		        			<#break>
		        		</#if>
		        	</#list>
		        </#if>
		        }
				return actionElement;
            </#if>
    }
    
    function downloadTemplateById(thisObj){
	  	let templateId = thisObj.id;
	  	$.ajax({
			url:contextPath+"/cf/dtbi",
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
			url:contextPath+"/cf/utdbi",
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
	
	function submitRevisionForm(sourceElement) {
		let selectedId = sourceElement.id.split("_")[0];
		let moduleName = $("#"+sourceElement.id).attr("name")
      	$("#entityId").val(selectedId);
		$("#moduleName").val(unescape(moduleName));
      	$("#revisionForm").submit();
    }
    
    function changeType() {
        var type = $("#typeSelect").val();   
        let postData;
        if(type == 0) {
            postData = {gridId:"templateListingGrid"}
        } else {
            let typeCondition = "str_"+type;       
   
            postData = {gridId:"templateListingGrid"
                    ,"cr_templateTypeId":typeCondition
                    }
        }
        
        let gridNew = $( "#divTemplateGrid" ).pqGrid();
        gridNew.pqGrid( "option", "dataModel.postData", postData);
        gridNew.pqGrid( "refreshDataAndView" );  
    }
        
    <#if environment == "dev">
        function downloadTemplate(){
            $.ajax({
                url:contextPath+"/cf/dtl",
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
                url:contextPath+"/cf/utd",
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
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);

REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('4d91fbd8-09fa-11eb-a894-f48e38ab8cd7', 'template-manage-details', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/monaco/require.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/1.0/monaco/min/vs/loader.js"></script>

</head>
<div class="container">

	<div class="row topband">
        <div class="col-8">
			<#if (templateDetails.templateId)?? && (templateDetails.templateId)?has_content>
			    <h2 class="title-cls-name float-left">Edit Template Details</h2> 
	        <#else>
	            <h2 class="title-cls-name float-left">Add Template Details</h2> 
	        </#if> 
	    </div>
       
        <div class="col-4">   
	        <#if (templateDetails.templateId)?? && (templateDetails.templateId)?has_content>                                     
		        <#assign ufAttributes = {
		            "entityType": "Templating",
		            "entityId": "templateId",
		            "entityName": "vmName"
		        }>
		        <@templateWithParams "user-favorite-template" ufAttributes />
	        </#if>
	    </div>
                              
        <div class="clearfix"></div>                         
    </div>

	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	<div class="row">
	    	<div class="col-9">
				<div class="col-inner-form full-form-fields">
					 <input type="hidden" id="templateId" name="templateId" value="${(templateDetails.templateId)!0}"/>
					 <label for="vmName"><span class="asteriskmark">*</span>Template Name </label>                                                                                                                                       
					 <input type="text" class="form-control" value="${(templateDetails.templateName)!}" maxlength="100" name="vmName" id="vmName">                                                                                                                       
				</div>
			</div>
			
		<div id="defaultTemplateDiv" class="col-3" style="display: none;">
			<div class="col-inner-form full-form-fields">
				<label for="defaultTemplateId">Default template </label>                                                                                                                                       
				<select class="form-control" id="defaultTemplateId" name="defaultTemplateId" title="Default template"> </select>                                                                                                                    
	        </div>
		</div>
	</div>
    
    <div id="ftlParameterDiv" class="col-12 method-sign-info-div">
		<h3 class="titlename method-sign-info">
		    <i class="fa fa-lightbulb-o" aria-hidden="true"></i><label for="ftlParameter">FTL Parameters and Macros</label>
	    </h3>
		<span id="ftlParameter">loggedInUserName, loggedInUserRoleList {}, templateWithoutParams {}, templateWithParams {}, resourceBundle {}, resourceBundleWithDefault {}<span>
    </div>
         
	<div class="row margin-t-b">                                                                                                
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
	  
	  	<div class="row">
			<div class="col-3"> 
	  			<input id="moduleId" value="1b0a2e40-098d-11eb-9a16-f48e38ab9348" name="moduleId"  type="hidden">
      			<@templateWithoutParams "role-autocomplete"/>
      		</div>
      	</div> 
               
		<div class="row margin-t-10">
			<div class="col-12">
				<div class="float-right">
					<div class="btn-group dropup custom-grp-btn">
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
			let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
            multiselect.setSelectedObject(defaultAdminRole);
		}
		
		if(typeof getSavedEntity !== undefined && typeof getSavedEntity === "function"){
			getSavedEntity();
		}
		savedAction("template-manage-details", isEdit);
		hideShowActionButtons();
	});
</script>
<script src="${(contextPath)!''''}/webjars/1.0/template/template.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);
 


REPLACE INTO  jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('8ba1a465-09fa-11eb-a894-f48e38ab8cd7', 'menu-module-listing', '<head>
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
<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script>
</head>

<div class="container">
		<div class="topband">
		
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.siteLayout'')}</h2> 
		<div class="float-right">
        	Show:<select id="typeSelect" class="typeSelectDropDown" onchange="changeType()">   
                <option value="0">All</option>                   
                <option value="1" selected>Custom</option>                   
                <option value="2">System</option>                 
            </select>
		<span>
  		    <input id="configHomePage" class="btn btn-primary" name="configHomePage" value="Define Home Page" type="button" onclick="configHomePage(this)">
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
		
		<div id="deleteHeader"></div>
		<div id="divModuleListing"></div>

</div>


<form action="${(contextPath)!''''}/cf/aem" method="POST" id="formMuRedirect">
	<input type="hidden" id="moduleId" name="module-id">
</form>
<form action="${(contextPath)!''''}/cf/chpl" method="GET" id="configHomePageListing">
	
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
	        { title: "Parent Module Name", width: 100, align: "center", dataIndx: "parentModuleName", align: "left", halign: "center",render: parentModule,
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Sequence Number", width: 100, dataIndx: "sequence", align: "right", halign: "right",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Inside Menu", width: 100, align: "center", dataIndx: "isInsideMenu", align: "center", halign: "center", render: formatIsInsideMenu},
	        { title: "Home Page", maxwidth: 150, align: "center", dataIndx: "isHomePage", align: "center", halign: "center", render: formatIsHomePage},
          { title: "${messageSource.getMessage(''jws.action'')}", width: 50, maxWidth: 145, dataIndx: "action", align: "left", halign: "left", render: editModule, sortable: false}
		];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "lastUpdatedTs",
        	sortDir: "down",
    	};
		let grid = $("#divModuleListing").grid({
	      gridId: "moduleListingGrid",
	      colModel: colM,
          dataModel: dataModel,
          additionalParameters: {"cr_moduleTypeId":"str_1"}
	  });
	});
  
    function changeType() {
        var type = $("#typeSelect").val();   
        let postData;
        if(type == 0) {
            postData = {gridId:"moduleListingGrid"}
        } else {
            let typeCondition = "str_"+type;       
   
            postData = {gridId:"moduleListingGrid"
                    ,"cr_moduleTypeId":typeCondition
                    }
        }
        
        let gridNew = $( "#divModuleListing" ).pqGrid();
        gridNew.pqGrid( "option", "dataModel.postData", postData);
        gridNew.pqGrid( "refreshDataAndView" );  
    }
       
    function parentModule(uiObject) {
        const parentModuleName = uiObject.rowData.parentModuleName;
        if(parentModuleName != null && parentModuleName != "") {
            return parentModuleName;
        }
        return "Root";
    }
  
  	function editModule(uiObject) {
		const moduleId = uiObject.rowData.moduleId;
		const moduleURL = uiObject.rowData.moduleURL;
		actionElement = ''<span id="''+moduleId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title="Edit module"></i></span>''.toString();
		
            		if(uiObject.rowData.moduleTypeId == 1) {   
		        <#if loggedInUserRoleList?? && loggedInUserRoleList?size gt 0>
		        	<#list loggedInUserRoleList as loggedInUserRole>
		            	<#if (loggedInUserRole == "ADMIN")> 
		        			actionElement += ''<span onclick=\\''openDeletConfirmation(\"divModuleListing",\"''+moduleId+''\",\"c6cc466a-0ed3-11eb-94b2-f48e38ab9348\")\\'' class= "grid_action_icons" title="Delete"><i class="fa fa-trash "></i></span>''.toString();
		        			<#break>
		        		</#if>
		        	</#list>
		        </#if>
		        }
        actionElement += ''<span onclick=\\''openModuleListing(\"''+moduleURL+''\")\\'' class= "grid_action_icons"><i class="fa fa-eye" title="View module"></i></span>''.toString();
		return actionElement.toString();
	}
  
    function openModuleListing(moduleURL) {
		window.open(contextPath+"/view/"+moduleURL);
    }
    
  	function formatIsInsideMenu(uiObject){
  		const isInsideMenu = uiObject.rowData.isInsideMenu;
  		if(isInsideMenu == 1){
  			return ''<input type="checkbox" disabled tabindex="-1" checked>'';
  		}
  		return ''<input type="checkbox" disabled tabindex="-1">'';
  	}
  	
  	function formatIsHomePage(uiObject){
  		const isHomePage = uiObject.rowData.isHomePage;
  		if(isHomePage == 1){
  			return ''<input type="checkbox" disabled tabindex="-1" checked>'';
  		}
  		return ''<input type="checkbox" disabled tabindex="-1">'';
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
		$("#configHomePageListing").submit();
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);
 


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('89ee344b-03f6-11eb-a183-e454e805e22f', 'module-manage-details', '<head>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
    <script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
    <script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
    <script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.min.js"></script>
    <script src="${(contextPath)!''''}/webjars/1.0/typeahead/typeahead.js"></script>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />

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
            <div class="col-4">
                <div class="col-inner-form full-form-fields">
                    <label for="moduleName" style="white-space:nowrap"><span class="asteriskmark">*</span>${messageSource.getMessage("jws.moduleName")}</label>
                    <input type="text"  id = "moduleName" name = "moduleName" value = "${(moduleDetailsVO?api.getModuleName())!''''}" maxlength="100" class="form-control">
                </div>
            </div>
            <div class="col-4">
                

                <div style="display:Flex">

                <div class="cutombtnradio">
                <div class="col-inner-form full-form-fields">
                                <label for="contextType">Include Layout
                                    </label>                       
                                 <div class="col-inner-form full-form-fields customradionbtn">
                                 <div class="onoffswitch">
                                    <input type="hidden" id="includeLayout" name="includeLayout" value="${(moduleDetailsVO?api.getIncludeLayout())!''0''}">
                                    <#if (moduleDetailsVO?api.getIncludeLayout())?? && moduleDetailsVO?api.getIncludeLayout() == 1>
                                        <input type="checkbox" id="includeLayoutCheckbox" onchange="addEditModule.includeLayout();" checked name="includeLayoutCheckbox" class="onoffswitch-checkbox">
                                    <#else>
                                        <input type="checkbox" id="includeLayoutCheckbox" onchange="addEditModule.includeLayout();" name="includeLayoutCheckbox" class="onoffswitch-checkbox">
                                    </#if>
                                    <label class="onoffswitch-label" for="includeLayoutCheckbox">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>

                     </div>
                    </div>
                    </div>  
                    
            </div>
            </div>

        </div>
        
                
        <div class="row">   
            
            <div class="col-4">
 			<div class="cutombtnradio" >
                <div class="col-inner-form full-form-fields">
                    <label for="contextType">${messageSource.getMessage("jws.includeInMenu")}</label>
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
                </div>
                
                        
            <input type="hidden" id = "parentModuleId" name="parentModuleId" value="${(moduleDetailsVO?api.getParentModuleId())!''''}">
            <div class="col-4">
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
            
            <div class="col-4">
                <div class="col-inner-form full-form-fields">
                    <label for="sequence" style="white-space:nowrap">${messageSource.getMessage("jws.sequence")}</label>
                    <#if (moduleDetailsVO?api.getModuleId())?? && (moduleDetailsVO?api.getModuleId())?has_content>
                        <input type="number"  id = "sequence" name = "sequence" value = "${(moduleDetailsVO?api.getSequence())!''''}" maxlength="100" class="form-control">
                    <#else>
                        <input type="number"  id = "sequence" name = "sequence" value = "${(defaultSequence)!''''}" maxlength="100" class="form-control">
                    </#if>
                </div>
            </div>
            
        </div>
        
        <div class="row">
            
            <div class="col-4">
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

            <div class="col-4">
                <div class="col-inner-form full-form-fields">
                    <label for="targetTypeName" style="white-space:nowrap"><span class="asteriskmark">*</span>Context Name</label>
                        <div class="search-cover">
                        <input type="text" id="targetTypeName" value= "" name="targetTypeName" autocomplete="off" class="form-control">
                        <i class="fa fa-search" aria-hidden="true"></i>
                </div>
                <input type="hidden" id="targetTypeNameId" value="${(moduleDetailsVO?api.getTargetTypeId())!''''}" name="targetTypeNameId" class="form-control">
                </div>
            </div>
            
            <div class="col-4">
                <div class="col-inner-form full-form-fields">
                    <label for="moduleURL" style="white-space:nowrap"><span class="asteriskmark">*</span>Module URL</label>
                    <span onclick="addEditModule.copyUrl()">
                        <label style="background: lightgrey;" class="float-right" title="<@resourceBundleWithDefault "jws.copyMessageTitle" "Click here to copy path"/>">${(urlPrefix)!''''} 
                            <i class="fa fa-clipboard" aria-hidden="true"></i>
                        </label>
                    </span>
                    <input type="text"  id = "moduleURL" name = "moduleURL" value = "${(moduleDetailsVO?api.getModuleURL())!''''}" maxlength="200" class="form-control">
                </div>
            </div>
            
        </div>
        
        <div class="row">
            <div class="col-6">
                
                    <table id="headerTable" class="customtblecls">

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
                            <td class="bgtd"><button class="plusticon" onclick="addEditModule.addRow()"><i class="fa fa-plus-circle" aria-hidden="true"></i>
</button></td>
                        </tr>
                        <#if !(moduleDetailsVO?api.getModuleId())?? && !(moduleDetailsVO?api.getModuleId())?has_content>
                            <tr id="567028a5-3836-4c6a-a3b3-10a36b77c9ad">
                                <td><input class="key" type="text" value="Powered-By" readonly></td>
                                <td><input class="value" type="text" value="JQuiver" readonly></td>
                            </tr>
                            <tr id="2c6e71f7-e68e-4a50-8a7a-43161134290d">
                                <td><input class="key" type="text" value="Content-Type"></td>
                                <td><input class="value" type="text" value="text/html; charset=UTF-8"></td>
                                <td>
                                    
                                    <span id="btn_2c6e71f7-e68e-4a50-8a7a-43161134290d" onclick="addEditModule.deleteRow(this)" class="cusrorhandcls">
                                       <i class="fa fa-minus-circle" aria-hidden="true"></i>
                                        </span>
                                        </td>
                            </tr>
                            <tr id="2aa2812f-b61d-4b5b-b0a3-5ba289df1c3b">
                                <td><input class="key" type="text" value="Content-Language"></td>
                                <td><input id="txtContentLang" class="value" type="text"></td>
                            </tr>
                        </#if>
                    </table>
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
                            <td class="bgtd centercls"  width="10%"><button class="plusticon" onclick="addEditModule.addRowForRequestParam()"><i class="fa fa-plus-circle" aria-hidden="true"></i></button></td>
                        </tr>                    
                    </table>
                    </div>
            </div>
            <div class="col-4">
                <input id="masterModuleId" value="c6cc466a-0ed3-11eb-94b2-f48e38ab9348" name="masterModuleId"  type="hidden">
                <@templateWithoutParams "role-autocomplete"/> 
            </div>
        
        </div>

        <div class="row">
            <div class="col-12">
                <div id="buttons" class="pull-right">
                    <div class="btn-group dropup custom-grp-btn">
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
    
    <div id="deleteHeader"></div>
    </div>

<script>
contextPath = "${(contextPath)!''''}";
let addEditModule;
let autocomplete;
let sequence = "${(moduleDetailsVO?api.getSequence())!''''}";
let savedHeaderJson = "";
let savedRequestParamJson = "";
let selectedLanguageId = getCookie("locale");
<#if (moduleDetailsVO?api.getHeaderJson())?has_content>
    savedHeaderJson = JSON.parse(''${(moduleDetailsVO?api.getHeaderJson())!""}'');
</#if>
<#if (moduleDetailsVO?api.getRequestParamJson())?has_content>
    savedRequestParamJson = JSON.parse(''${(moduleDetailsVO?api.getRequestParamJson())!""}'');
</#if>

$(function() {
    $("#targetLookupType option[value=''4'']").remove();
    let moduleTypeId = "${(moduleDetailsVO?api.getTargetTypeId())!''''}";
    let moduleName = "${(moduleDetailsVO?api.getTargetLookupName())!''''}";
    let parentModuleId = "${(moduleDetailsVO?api.getParentModuleId())!''''}";
    let targetLookupId = "${(moduleDetailsVO?api.getTargetLookupId())!''''}";
    let moduleId = "${(moduleDetailsVO?api.getModuleId())!''''}";

	$("#txtContentLang").val(selectedLanguageId);
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
        enableClearText : true,
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
        resetAutocomplete: function(autocompleteObj){ 
            $("#targetTypeNameId").val("");
        },      
    }, selectedTargetDetails);
    
      addEditModule.createHeaderResponseTable();
      addEditModule.createRequestParamTable();
      addEditModule.getTargeTypeNames(''isAddEdit'');
      hideShowActionButtons();
    <#if (!(moduleDetailsVO?api.getModuleId())??)>
        let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
            multiselect.setSelectedObject(defaultAdminRole);
    <#else>
        addEditModule.getEntityRoles();
    </#if>
});

</script>
<script src="${(contextPath)!''''}/webjars/1.0/menu/addEditModule.js"></script>

', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);




REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('99a707e5-09fa-11eb-a894-f48e38ab8cd7', 'error-page', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />

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
          
          <#if statusCode == 403 && ((!loggedInUserName?? && !loggedInUserName?has_content) || loggedInUserName == ''anonymous'') >
          Please <a href="${(contextPath)!''}/cf/login" style="font-weight: bold;">LOGIN</a> with required access rights to avail this feature.
          </#if>
        </div>
            
        </div>
    </div>

     <div class="col-4">
             <img class="errorImg" src="${(contextPath)!''''}/webjars/1.0/images/error1.jpg">
     </div>

	</div>
</div>


<script>
contextPath = "${(contextPath)!''''}";

function showHideErrorInfo(){
  $("#errorDetailsDiv").slideToggle();
}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW(), 2);


REPLACE INTO jq_autocomplete_details(ac_id,ac_description,ac_select_query,ac_type_id,created_by, created_date, last_updated_ts)  
VALUES ('revisionAutocomplete','List module version detail by entity id'
,'SELECT jmv.module_version_id AS moduleVersionId, jmv.version_id AS versionId, DATE_FORMAT(jmv.updated_date,:dateFormat) AS updatedDate
 FROM jq_module_version AS jmv WHERE jmv.entity_id = :entityId AND jmv.entity_name = :entityName AND DATE_FORMAT(jmv.updated_date, :dateFormat) LIKE CONCAT("%", :searchText, "%")
 AND jmv.version_id <> (SELECT MAX(jmv.version_id)  FROM jq_module_version AS jmv WHERE jmv.entity_id = :entityId AND jmv.entity_name = :entityName) ORDER BY jmv.updated_date DESC 
 LIMIT :startIndex, :pageSize '
, 2, 'aar.dev@trigyn.com', NOW(), NOW());



REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('9edd802d-1851-11eb-a842-f48e38ab8cd7', 'revision-details','<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script> 
<script src="${(contextPath)!''''}/webjars/bootstrap/js/bootstrap.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/monaco/require.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/monaco/min/vs/loader.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/typeahead/typeahead.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />

</head>
<div class="pg-revision-history">
	<div class="container" style="padding-top: 40px">
	     
		 <div class="topband">
			<h2 class="title-cls-name float-left history-label">${messageSource.getMessage("jws.history")}: ${(moduleName)!''''}</h2> 
			<div class="float-right">
				<span onclick="backToPreviousPage();">
	  				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="${messageSource.getMessage(''jws.back'')}" type="button">
	  		 	</span>
			</div>
	
			<div class="clearfix"></div>
		</div>
		
		<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
		<div class="row">
			
			<input type="hidden" id="moduleType" name="moduleType" value="${(moduleType)!''''}">
			<input type="hidden" id="saveUrl" name="saveUrl" value="${(saveUrl)!''''}">
			<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="${(previousPageUrl)!''''}">
			<input type="hidden" id="moduleType" name="moduleType" value="${(moduleType)!''''}">
			<input type="hidden" id="formId" name="formId" value="${(formId)!''''}">
			<input type="hidden" id="dateFormat" name="dateFormat" value="${(dateFormat)!''''}">
			
			<input type="hidden" id="moduleVersionId" value="" name="targetTypeNameId" class="form-control">
			<input type="hidden" id="selectedDateTime" value="" name="selectedDateTime" class="form-control">
			
	
		</div>
	
	
		<div class="row margin-t-b">
	
				<div class="col-6">
					<h3 id="diffEditorHeader" class="titlename">${messageSource.getMessage("jws.currentContent")}</h3>
				</div>
				<div class="col-6">
					<#if isImport == "true"> 
						<label class="versioning-label" >Import Data </label>
					<#else>
						<label for="revisionAutocomplete" class="versioning-label" >${messageSource.getMessage("jws.revisionTime")}: </label>
					</#if>
					<div class="col-inner-form full-form-fields">
						<#if isImport == "false"> 
							<div class="search-cover pull-left">
								<input type="text" id="revisionAutocomplete" value= "" name="revisionAutocomplete" class="form-control">
								<i class="fa fa-search" aria-hidden="true"></i>
							</div>
						</#if>
						<span onclick="copyJsonData()"  class="grid_action_icons pull-left">
	
	                        <span class="cm-iconsvg"><img src="${(contextPath)!''''}/webjars/1.0/images/text-push-right-1.svg"></span>
	                    </span>
					</div>
				</div>
		</div>
		<div class="row">		
			<div class="col-12" id="diffEditor_0">
				<div class="html_script">
					<div class="grp_lblinp">
						<div id="jsonContainer_0" class="ace-editor-container">
							<div id="jsonEditor_0" class="ace-editor"></div>
						</div>
					</div>
				</div>	
			</div>
		</div>
	
		<div class="col-12">
			<div class="float-right margin-b-25">
				<div class="btn-group dropup custom-grp-btn">
					<div id="savedAction">
		    	        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="saveUpdatedJson();">${messageSource.getMessage("jws.save")}</button>
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
contextPath = "${(contextPath)!''''}";
const entityId = "${(entityId)!''''}";
const entityName = "${(entityName)!''''}";
let updatedJson;
let compareJsonEditor;
let diffEditorArray = new Array();
let autocomplete;
let isImport;
let importJson;
let isNonVersioningModule;
let nonVersioningFetchURL;
$(function(){
	isImport = "${(isImport)!''''}";
	isNonVersioningModule = "${(isNonVersioningModule)!''''}";
	
	if(isImport == "false" || isNonVersioningModule == "false") {
		getUpdatedData();
	} else {
		nonVersioningFetchURL = "${(nonVersioningFetchURL)!''''}";
		getNonVersioningModuleLatestData();
	}
	
	if(isImport == "true") {
		importJson = JSON.parse(localStorage.getItem("importJson"));
		localStorage.removeItem("importJson");
		setJsonEditorContent();
	} else {
	  setJsonEditorContent();
	  getUpdatedData();
        autocomplete = $(''#revisionAutocomplete'').autocomplete({
        autocompleteId: ''revisionAutocomplete'',
        prefetch : true,
        enableClearText : true,
        render: function(item) {
        	var renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === '''')
    		{
        		renderStr = ''<p>''+item.updatedDate+''</p>'';
    		}
        	else
    		{
        		renderStr = item.emptyMsg;	
    		}	    		
            return renderStr;
        },
        additionalParamaters: {
			 entityId: entityId
			,entityName: entityName
			,dateFormat: $("#dateFormat").val()
		},
        extractText: function(item) {
            return item.updatedDate;
        },
        select: function(item) {
            $("#revisionAutocomplete").blur();
			$("#errorMessage").hide();
			$("#selectedDateTime").val(item.updatedDate);
			$("#moduleVersionId").val(item.moduleVersionId);
			$("#diffEditor_0").nextAll("div").remove();
			diffEditorArray = new Array();
			getUpdatedData();
			getJsonData(item.moduleVersionId);
        },
        resetAutocomplete: function(){ 
        	$("#selectedDateTime").val("");
			$("#moduleVersionId").val("");	
        }, 	
    });
	}

});

function setJsonEditorContent(){
	require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }, waitSeconds: 120});
    	require(["vs/editor/editor.main"], function() {
        compareJsonEditor = monaco.editor.createDiffEditor(document.getElementById("jsonEditor_0"), {
	       	originalEditable: true,
	   		readOnly: true,
			language: "text/plain",
		    roundedSelection: false,
			scrollBeyondLastLine: false,
			theme: "vs-dark",
			wordWrap: ''on'',
	    });

	    if(isImport == "true") {
	    	processJSONData(importJson);
	    }
    });
	
}

function getNonVersioningModuleLatestData(){
	$.ajax({
		type : "POST",
		url : contextPath + nonVersioningFetchURL,
		async: false,
		data : {
			entityId : entityId,
		},
		success : function(data) {
			updatedJson = data;
		},
	        
	    error : function(xhr, error){
	    	showMessage("Error occurred while fetching data", "error");
	    },
	        	
	});
}

function getUpdatedData(){
	$.ajax({
		type : "POST",
		url : contextPath+"/cf/uj",
		async: false,
		data : {
			entityName : entityName,
			entityId : entityId,
		},
		success : function(data) {
			updatedJson = data;
		},
	        
	    error : function(xhr, error){
	    	showMessage("Error occurred while fetching data", "error");
	    },
	        	
	});
}

function getJsonData(selectedElementId){
	$.ajax({
		type : "POST",
		url : contextPath+"/cf/mj",
		async: false,
		data : {
			moduleVersionId : selectedElementId,
		},
		success : function(selectedVersionData) {
			processJSONData(selectedVersionData);
		},
	        
	    error : function(xhr, error){
	    	showMessage("Error occurred while fetching data", "error");
	    },
	        	
	});
}

function processJSONData(selectedVersionData){
	let selectedVersionObj;
	if(isImport == "true") {
		selectedVersionObj = selectedVersionData;
	} else {
		selectedVersionObj = JSON.parse(selectedVersionData);
	}
	let updatedObj = JSON.parse(updatedJson);
	for (let prop in selectedVersionObj) {
		if (Object.prototype.hasOwnProperty.call(selectedVersionObj, prop)) {
			if(prop === "daoQueryDetails"){
				dynarestDiff(selectedVersionObj, updatedObj);
			}else if(prop === "dynamicFormSaveQueries"){
				formBuilderDiff(selectedVersionObj, updatedObj);
			}else if(prop === "dashletBody"){
				dashletDiff(selectedVersionObj, updatedObj);
			}else if(prop === "template"){
				templateDiff(selectedVersionObj, updatedObj);
			}else if(prop === "autocompleteQuery"){
				autocompleteDiff(selectedVersionObj, updatedObj);
			}
		}
	}

	selectedVersionData = JSON.stringify(selectedVersionObj, null, ''\t'');
	updatedJson = JSON.stringify(updatedObj, null, ''\t'');

	let originalModel = monaco.editor.createModel(updatedJson, "json");
	let modifiedModel = monaco.editor.createModel(selectedVersionData, "json");
					
	compareJsonEditor.setModel({
		original: originalModel,
		modified: modifiedModel
	});
}

function templateDiff(selectedVersionObj, updatedObj){
	let saveTemplate = selectedVersionObj["template"];
	let updatedTemplate = updatedObj["template"];
	
	createDiffEditor(saveTemplate, updatedTemplate, "template", "html");
	
	delete selectedVersionObj["template"];
	delete updatedObj["template"];
}

function autocompleteDiff(selectedVersionObj, updatedObj){
	let saveTemplate = selectedVersionObj["autocompleteQuery"];
	let updatedTemplate = updatedObj["autocompleteQuery"];
	
	createDiffEditor(saveTemplate, updatedTemplate, "autocompleteQuery");
	
	delete selectedVersionObj["autocompleteQuery"];
	delete updatedObj["autocompleteQuery"];
}


function dynarestDiff(selectedVersionObj, updatedObj){
	let daoQueryDetailsArray = JSON.parse(selectedVersionObj["daoQueryDetails"]);
	let updatedDaoQueryDetailsArray = JSON.parse(updatedObj["daoQueryDetails"]);
	
	if(daoQueryDetailsArray.length > updatedDaoQueryDetailsArray.length){
		$.each(daoQueryDetailsArray, function(index, value){
			let saveQueryObj = value;
			let updatedSaveQueryObj = "";
			
			if(updatedDaoQueryDetailsArray[index] !== undefined){
				updatedSaveQueryObj = updatedDaoQueryDetailsArray[index];
			}
			
			createDiffEditor(saveQueryObj, updatedSaveQueryObj, "daoQueryDetails");
		});
	}else{
		$.each(updatedDaoQueryDetailsArray, function(index, value){
			let saveQueryObj = "";
			let updatedSaveQueryObj = value;
			
			if(daoQueryDetailsArray[index] !== undefined){
				saveQueryObj = daoQueryDetailsArray[index];
			}
			createDiffEditor(saveQueryObj, updatedSaveQueryObj, "daoQueryDetails");
		});
	}
	
	delete selectedVersionObj["daoQueryDetails"];
	delete updatedObj["daoQueryDetails"];

}


function formBuilderDiff(selectedVersionObj, updatedObj){
	let saveFormHtml = selectedVersionObj["formBody"];
	let updatedFormHtml = updatedObj["formBody"];
	let saveFormSelectQuery = selectedVersionObj["formSelectQuery"];
	let updatedFormSelectQuery = updatedObj["formSelectQuery"];
	let saveQueries = selectedVersionObj["dynamicFormSaveQueries"];
	let updatedSaveQueries = updatedObj["dynamicFormSaveQueries"];
	
	createDiffEditor(saveFormSelectQuery, updatedFormSelectQuery, "formSelectQuery");
	createDiffEditor(saveFormHtml, updatedFormHtml, "formBody", "html");
	
	if(saveQueries.length > updatedSaveQueries.length){
		$.each(saveQueries, function(index, value){
			let saveQueryObj = value.formSaveQuery;
			let updatedSaveQueryObj = "";
			
			if(updatedSaveQueries[index] !== undefined){
				updatedSaveQueryObj = updatedSaveQueries[index].formSaveQuery;
			}
			createDiffEditor(saveQueryObj, updatedSaveQueryObj, "formSaveQuery");
		});
	}else{
		$.each(updatedSaveQueries, function(index, value){
			let saveQueryObj = "";
			let updatedSaveQueryObj = value.formSaveQuery;
			
			if(saveQueries[index] !== undefined){
				saveQueryObj = saveQueries[index].formSaveQuery;
			}
			createDiffEditor(saveQueryObj, updatedSaveQueryObj, "formSaveQuery");
		});
	}
	
	delete selectedVersionObj["formBody"];
	delete updatedObj["formBody"];
	delete selectedVersionObj["formSelectQuery"];
	delete updatedObj["formSelectQuery"];
	delete selectedVersionObj["dynamicFormSaveQueries"];
	delete updatedObj["dynamicFormSaveQueries"];
}


function dashletDiff(selectedVersionObj, updatedObj){
	let htmlContent = selectedVersionObj["dashletBody"];
	let updatedHtmlContent = updatedObj["dashletBody"];
	
	createDiffEditor(htmlContent, updatedHtmlContent, "dashletBody", "html");
		
	delete selectedVersionObj["dashletBody"];
	delete updatedObj["dashletBody"];
	
	let sqlQuery = selectedVersionObj["dashletQuery"];
	let updatedSqlQuery = updatedObj["dashletQuery"];
	
	createDiffEditor(sqlQuery, updatedSqlQuery, "dashletQuery");
		
	delete selectedVersionObj["dashletQuery"];
	delete updatedObj["dashletQuery"];
}

function createDiffEditor(selectedObj, currentObj, fieldName, a_languageName){
	
	require.config({ paths: { "vs": "../webjars/1.0/monaco/min/vs" }});
	require(["vs/editor/editor.main"], function() {
			
			let diffEditorObj = new Object();
			let diffEditor;
			let index = diffEditorArray.length + 1;
			let diffEditorDiv = $(''<div class="col-12 margin-t-b" id="diffEditor_''+index+''"><div class="html_script"><div class="grp_lblinp"><div id="jsonContainer_''+index+''" class="ace-editor-container"><div id="jsonEditor_''+index+''" class="ace-editor"></div></div></div></div></div>'');
			diffEditorDiv.insertAfter($("#diffEditor_"+(index -1)));

			let languageName = "sql";
			if(a_languageName){
				languageName = a_languageName;
			}
			
			diffEditor = monaco.editor.createDiffEditor(document.getElementById("jsonEditor_"+index), {
				originalEditable: true,
				readOnly: true,
				language: languageName,
				roundedSelection: false,
				scrollBeyondLastLine: false,
				theme: "vs-dark",
				wordWrap: ''on'',
			});
			
			
			let originalModel = monaco.editor.createModel(currentObj, languageName);
			let modifiedModel = monaco.editor.createModel(selectedObj, languageName);
							
			diffEditor.setModel({
				original: originalModel,
				modified: modifiedModel
			});
			
			diffEditorObj.fieldName = fieldName;
			diffEditorObj.editor = diffEditor;
			diffEditorArray.push(diffEditorObj);
	});

}



function copyJsonData(){
	if(isImport == "false") {
		let selectedElementId = $("#moduleVersionId").val().trim();
		if(selectedElementId == ""){
			$("#errorMessage").html("Please select revision time");
			$("#errorMessage").show();
			return false;
		}
	 }
	let modifiedContent = compareJsonEditor.getModifiedEditor().getValue();
	compareJsonEditor.getOriginalEditor().setValue(modifiedContent);

	$.each(diffEditorArray, function(index, value){
		let modifiedContent = diffEditorArray[index].editor.getModifiedEditor().getValue();
		diffEditorArray[index].editor.getOriginalEditor().setValue(modifiedContent);
	})
	showMessage("Content copied successfully", "Success");
	
}

function saveUpdatedJson(){
	let content = compareJsonEditor.getOriginalEditor().getValue();
	let contentJson = JSON.parse(content);
	$.each(contentJson, function(key, value){
	  if(typeof value === "string"){
		 contentJson[key] = value.trim();
	  }
	});
	let isParsable = false;
	$.each(diffEditorArray, function(index, diffEditor){
		let fieldName = diffEditor.fieldName;
		if(contentJson[fieldName] !== undefined){
			let previousValue;
			let propertyArray;
			if(isParsable){
				previousValue = JSON.parse(contentJson[fieldName]);
				propertyArray = previousValue.slice();
			}else{
				previousValue = contentJson[fieldName];
				propertyArray = new Array();
				propertyArray.push(previousValue);
			}
			let currentEditorValue = diffEditor.editor.getOriginalEditor().getValue().trim();
			if(currentEditorValue !== ""){
				propertyArray.push(currentEditorValue);
			}
			contentJson[fieldName] = JSON.stringify(propertyArray);
			isParsable = true;
		}else{
			isParsable = false;
			let fieldVal = diffEditor.editor.getOriginalEditor().getValue().trim();
			if(fieldName === "formSaveQuery" || fieldName === "daoQueryDetails"){
				isParsable = true;
				let propertyArray = new Array();
				propertyArray.push(fieldVal);
				contentJson[fieldName] = JSON.stringify(propertyArray);
			}else{
				contentJson[fieldName] = fieldVal;
			}
		}
	});
	let modifiedContent = JSON.stringify(contentJson);
	let moduleType = $("#moduleType").val();
	let formId = $("#formId").val();
	let saveUrl = $("#saveUrl").val().trim();
	if(saveUrl == ""){
		saveUrl = "/cf/suj";
	}
	
	$.ajax({
		type : "POST",
		url : contextPath+saveUrl,
		async: false,
		data : {
			formId : formId,
			moduleType : moduleType,
			modifiedContent : modifiedContent,
		},
		success : function(data) {
			if(isImport == "true") {
				let idList = new Array();
				let importedIdList = localStorage.getItem("importedIdList");
		    	if(importedIdList != null) {
					idList = JSON.parse(importedIdList);
				}
				if(!idList.includes(moduleType.toLowerCase()+entityId)) {
			    		idList.push(moduleType.toLowerCase()+entityId);
				}
				localStorage.setItem("importedIdList", JSON.stringify(idList));
			}
			showMessage("Information saved successfully", "success");
		},
				
		error : function(xhr, error){
			showMessage("Error occurred while saving data", "error");
		},
	        	
	});

}

function backToPreviousPage(){
	let previousPageUrl = $("#previousPageUrl").val();
	location.href = contextPath+previousPageUrl;
}

</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);




REPLACE INTO jq_grid_details(grid_id,grid_name,grid_description,grid_table_name,grid_column_names, query_type, grid_type_id, created_by, created_date, last_updated_ts)
VALUES ('homePageListingGrid','Home Page Listing','Home Page Listing','homePageListing','moduleId,moduleName,moduleURL,roleName,rolePriority', 2, 2, 'aar.dev@trigyn.com', NOW(), NOW());
  
REPLACE INTO  jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('55b93b76-54a5-11eb-9e7a-f48e38ab8cd7', 'config-home-page-listing', '<head>
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
<script src="${(contextPath)!''''}/webjars/1.0/jquery-modal/jquery.modal.min.js"></script> 
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/jquery-modal/jquery.modal.min.css" />
<script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/typeahead/typeahead.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
    
</head>

<div class="container">
		<div class="topband">
		
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.configHomePageListing'')}</h2> 
		<div class="float-right">
		 <!-- <span>
  		    <input id="configHomePage" class="btn btn-primary" name="configHomePage" value="Add Default Page" type="button" onclick="editHomePage()">
		</span> -->


         <span onclick="backToModuleListingPage();">
          	<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
         </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divConfigHomeListing"></div>

		<form id="edit-home-page" class="modal addeditconfigmodal">
		    <div class="topband">
		        <h2 id="title" class="title-cls-name float-left">Set Home Page for </h2> 
		        <div class="clearfix"></div>        
		    </div>
		    <div class="row">
		       <div class="col-12">
			        <div class="col-inner-form full-form-fields">
			            <input type="hidden" id="roleId" name="roleId">
			            <input type="hidden" id="oldModuleId" name="old-module-id">
			            <input type="hidden" id="moduleId" name="module-id">
			            <input type="hidden" id="moduleName" name="module-name">
			            <label for="flammableState" style="white-space:nowrap">Module Name</label>
			            <div class="search-cover">
							<input class="form-control" id="slAutocomplete" type="text">
							<i class="fa fa-search" aria-hidden="true"></i>
			            </div>
			         </div>
			    </div>
		    </div>
		    
		    
		    <div class="row" style="margin-top: 20px;">
		        <div class="col-12">
		            <div class="float-right">
		                <input id="setDefaultBtn" class="btn btn-primary" name="setDefaultBtn" onclick="setToDefault()" value="Set To Default" type="button">
		                <input id="saveBtn" class="btn btn-primary" name="saveBtn" onclick="saveHomeModule()" value="Save" type="button">
		                <input id="backBtn" class="btn btn-secondary" name="backBtn" onclick="closeForm()" value="Cancel" type="button">
		            </div>
		        </div>
		    </div>
		</form>
</div>


<script>
	contextPath = "${(contextPath)!''''}";
	let slAutocomplete;
	
	function backToModuleListingPage() {
		location.href = contextPath+"/cf/mul";
	}
	$(function () {
		let colM = [
	        { title: "", width: 130, align: "center", dataIndx: "moduleId", align: "left", halign: "center", hidden : true },
	        { title: "", width: 130, align: "center", dataIndx: "roleId", align: "left", halign: "center", hidden : true },
	        { title: "Role", width: 100, align: "center", dataIndx: "roleName", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Module Name", width: 100, align: "center",  dataIndx: "moduleName", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Module URL", width: 160, align: "center", dataIndx: "moduleURL", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Priority", width: 100, align: "center", dataIndx: "rolePriority", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
          	{ title: "${messageSource.getMessage(''jws.action'')}", width: 50, maxWidth: 145, dataIndx: "action", align: "center", halign: "center", render: formatActionCol, sortable: false}
		];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "rolePriority",
        	sortDir: "down",
    	};
		let grid = $("#divConfigHomeListing").grid({
	      gridId: "homePageListingGrid",
	      colModel: colM,
          dataModel: dataModel
	  });
	  
	  	slAutocomplete = $("#slAutocomplete").autocomplete({
	        autocompleteId: "site-layout-url-autocomplete",
	        prefetch : false,
	        enableClearText : true,
	        render: function(item) {
	            var renderStr ="";
	            if(item.emptyMsg == undefined || item.emptyMsg === "")
	            {
	                renderStr = "<p>"+item.text+"</p>";
	            }else
	            {
	                renderStr = item.emptyMsg;    
	            }                                
	            return renderStr;
	        },
	        additionalParamaters: {languageId: 1},
	        extractText: function(item) {
	            return item.text;
	        },
	        select: function(item) {
	        	$("#moduleId").val(item.moduleId);
	            $("#slAutocomplete").blur(); 
	        },
	        resetAutocomplete: function(autocompleObj){ 
	        	$("#moduleId").val("");
	        }     
		});
		
	});
  
  	function formatActionCol(uiObject) {
		const roleId = uiObject.rowData.roleId;
		const moduleId = uiObject.rowData.moduleId;
		const moduleName = uiObject.rowData.moduleName;
		const roleName = uiObject.rowData.roleName;
		const moduleURL = uiObject.rowData.moduleURL;
		let actionElement = "";
		if(moduleId !== null){ 
			actionElement += ''<span id="''+roleId+''" data-module-id="''+moduleId+''" data-module-name="''+moduleName+''" data-role-name="''+roleName+''" onclick="editHomePage(this)" class= "grid_action_icons"><i class="fa fa-pencil" title="Edit module"></i></span>''.toString();
		} else {
			actionElement += ''<span id="''+roleId+''" data-role-name="''+roleName+''" onclick="editHomePage(this)" class= "grid_action_icons"><i class="fa fa-pencil" title="Edit module"></i></span>''.toString();
		}
		actionElement += ''<span onclick=\\''openModuleListing(\"''+moduleURL+''\")\\'' class= "grid_action_icons"><i class="fa fa-eye" title="View module"></i></span>''.toString();
		return actionElement;
	}
  
    function openModuleListing(moduleURL) {
		window.open(contextPath+"/view/"+moduleURL);
    }
    
	function editHomePage(sourceElement) {
		let moduleId;
		let moduleName;
		let roleName;
		let roleId = sourceElement.id;
		let selectedModuleObj = new Object();
		
		moduleId = $("#"+roleId).data("module-id");
		moduleName = $("#"+roleId).data("module-name");
		roleName = $("#"+roleId).data("role-name");
		
		$("#moduleId").val("");
		$("#oldModuleId").val("");
		$("#moduleName").val("");
		if(moduleId !== undefined){ 
			$("#moduleId").val(moduleId);
			$("#oldModuleId").val(moduleId);
			$("#moduleName").val(moduleName);
		}
		$("#roleId").val(roleId);
		$("#roleName").val(roleName);
		$("#title").text("Set Home Page for " + roleName);
		$("#edit-home-page").modal();
		
		selectedModuleObj.moduleId = moduleId;
		selectedModuleObj.text = moduleName;
		slAutocomplete.setSelectedObject(selectedModuleObj);	
	}  	
  	
  	function closeForm() {
        $.modal.close();
    }
    
    function setToDefault(){ 
    	slAutocomplete.resetAutocomplete();
    }
    
    function saveHomeModule(){
    	let selectedModuleObj = slAutocomplete.getSelectedObject();
		let moduleId = $("#moduleId").val().trim();
		let roleId = $("#roleId").val();
		let oldModuleId = $("#oldModuleId").val().trim();
		
		if(moduleId !== "" || oldModuleId !== ""){
			$.ajax({
				type : "POST",
				url : contextPath+"/cf/schm",
				async: false,
				data : {
					oldModuleId: oldModuleId,
					moduleId : moduleId,
					roleId : roleId,
				},
				success : function(moduleId) {
					$("#moduleId").val(moduleId);
					$("#errorMessage").hide();
					showMessage("Information saved successfully", "success");
					closeForm();
					$("#divConfigHomeListing").pqGrid("refreshDataAndView");
				},
			        
			    error : function(xhr, error){
			    	showMessage("Error occurred while saving", "error");
			    },
			        	
			});
		}else{ 
			showMessage("Please select valid Module Name", "error");
		}
	}
	
	function validateModule(){ 
		let moduleId = $("#moduleId").val();
		let roleId = $("#roleId").val();
		$.ajax({
			type : "POST",
			url : contextPath+"/cf/vchm",
			async: false,
			data : {
				moduleId : moduleId,
				roleId : roleId,
			},
			success : function(moduleId) {
				showMessage("Information saved successfully", "success");
			},
		        
		    error : function(xhr, error){
		    	showMessage("Error occurred while saving", "error");
		    },
		        	
		});
	
	}
    
    
	

</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);


REPLACE INTO  jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('0d6041fd-7ffd-11eb-971b-f48e38ab8cd7', 'user-favorite-template', '<head>
<script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/typeahead/typeahead.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />

</head>
        <div class="row"> 
            <div class="col-2">
                <div class="fav-image" data-hover="&#9733" onclick="addRemoveFav()">&#9734;</div>         
            </div>
            <div class="col-10 float-right">
                <div class="col-inner-form full-form-fields">
                    <div class="search-cover">
                    	<input class="form-control" id="rbAutocompletePF" type="text">
                    	<i class="fa fa-search" aria-hidden="true"></i>
                    </div>
                </div>
            </div>          
        </div>
<script>
contextPath = "${(contextPath)!''''}";	
let autocompletePF;

	$(function () {
        let imageName;
        autocompletePF = $("#rbAutocompletePF").autocomplete({
            autocompleteId: "user-favorite-entity-autocomplete",
            prefetch : true,
            enableClearText: true,
            render: function(item) {
                var renderStr = "";
                imageName = getImageNameByType(item.entityType);
                if(item.emptyMsg == undefined || item.emptyMsg === ""){
                   renderStr = ''<div class="user-favorite-img-cls"><img src="${(contextPath)!''''}/webjars/1.0/images/''+imageName+''.svg"><p>''+item.entityName+''</p></div>'';
                }else{
                    renderStr = item.emptyMsg;	
                }	    				        
                return renderStr;
            },
            additionalParamaters: {languageId: 1},
            extractText: function(item) {
                return item.entityName;
            },
            select: function(item) {
                submitForm(item);
                $("#rbAutocompletePF").blur();
            }, 
        });
    });
    
     function getImageNameByType(entityType){
        switch(entityType) {
	        case "Grid Utils":
	            return "grid";
	        case "Templating":
	            return "template";
	        case "TypeAhead Autocomplete":
	            return "autotype";
	        case "File Bin":
	            return "upload_management";
	        case "Form Builder":
	            return "daynamicreport";    
	        case "REST API":
	            return "API_listing_icon";
	        case "Dashboard":
	            return "dashboard";  
	        case "Dashlet":
	            return "dashboard";
	        default:
            	return "daynamicreport";           
        }
    }

    function addRemoveFav(){
	    $(".fav-image").toggleClass("fav-imageFill");
        let isChecked = $(".fav-image").hasClass("fav-imageFill");
        let entityIdVar = "${innerTemplateObj.entityId}";
        let entityNameVar = "${innerTemplateObj.entityName}";
        $.ajax({
			url:contextPath+"/api/user-favorite-entity",
			type:"POST",
	        data:{
                entityType: "${innerTemplateObj.entityType}",
                entityId: $("#"+entityIdVar).val().trim(),
                entityName: $("#"+entityNameVar).val().trim(),
                isChecked: isChecked,
	        }, 
			success : function(data) {
				autocompletePF.resetAutocomplete();
				showMessage("Successfully updated favorite", "success");
			},
			error : function(xhr, error){
				showMessage("Error occurred while updating favorite", "error");
			}, 
	    }); 
	}

    function getSavedEntity(){
        let entityIdVar = "${innerTemplateObj.entityId}";
        let entityNameVar = "${innerTemplateObj.entityName}";
         $.ajax({
			url:contextPath+"/api/user-favorite-entity-by-type",
			type:"POST",
	        data:{
                entityType: "${innerTemplateObj.entityType}",
                entityId: $("#"+entityIdVar).val().trim(),
                entityName: $("#"+entityNameVar).val().trim(),
	        }, 
			success : function(isFavorite) {
                if(isFavorite.trim() == 1){
                    $(".fav-image").addClass("fav-imageFill");
                }
			},
			error : function(xhr, error){
			  showMessage("Error occurred while updating favorite", "error");
			}, 
	    }); 
    }
    
    function submitForm(selectedEntity){
     	let form = $(JSON.parse(selectedEntity.formData));
        $("body").append(form);
       	$(form).attr("action", contextPath+$(form).attr("action"));
        //let formId = form[0].id;
        $(form).find("input").each(function(index,inputElem){
            if($(inputElem).attr("name") !== "formId"){
                $(inputElem).val(selectedEntity.entityId);
            }
        })
        $(form).submit();
    }
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);


REPLACE INTO  jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('52203478-5dc4-4823-999e-cea88ae10cad', 'jq-team-template', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />

</head>
<div class="container text-center">

    <div class="teamtitle">
        <h3>Meet Our Team Members</h3>
    </div>    

    <div class="row  align-items-center justify-content-center" >
        <div class="col-xs-12 col-sm-6 col-lg-3 profilepic">

                 <span class="namcls">The Architect</span>
            <a href="https://www.linkedin.com/in/adchowdhury/" target="_blank">
                <img class="team-img" width="150px" src="${(contextPath)!''''}/cf/files/0cad6a17-9f5f-4d25-b983-b8b691a144d0" alt="aNIRUDDHA" title="The Architect"/>

                <div class="udetails">  
                <span class="desg">Aniruddha Chowdhury</span>  
                </div>
            </a>
        </div>
    </div>
 
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 profilepic">
                 <span class="namcls">Sr Software Engineer</span>
            <a href="https://www.linkedin.com/in/abde17/" target="_blank">
                <img class="team-img" width="150px" height="160px" src="${(contextPath)!''''}/cf/files/756de33e-b86a-11eb-9690-f48e38ab8cd7" alt="Abhay Desai" title="Sr Software Engineer"/>

                 <div class="udetails">             
               <span class="desg">Abhay Desai</span>
                   
                </div>
            </a>
        </div>
        
        <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 profilepic">
            <span class="namcls">Sr Technical Analyst</span>
                   
            <a href="https://www.linkedin.com/in/akram-bhasha-mohammad-80778841/" target="_blank">
                <img class="team-img" width="150px" height="160px" src="${(contextPath)!''''}/cf/files/1f4e7749-3365-400a-98de-2716a1de01fc" alt="Akram Bhasha" title="Sr Technical Analyst"/>
                 <div class="udetails">                
             
                <span class="desg">Akram Bhasha Mohammad</span>
                </div>
            </a>
        </div>
        
        <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 profilepic">

                 <span class="namcls">Sr Technical Analyst</span>
                
            <a href="https://www.linkedin.com/in/aman-prasad-0bba71bb/" target="_blank">
                <img class="team-img" width="150px" height="160px" src="${(contextPath)!''''}/cf/files/4efc83a2-4ef9-40bd-a60b-a5d639d1bea5" alt="Aman Prasad" title="Sr Technical Analyst"/>
                 <div class="udetails">
                 <span class="desg">Aman Prasad</span>
                
                </div>
            </a>
        </div>
    
        <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 profilepic">
                <span class="namcls">Lead Consutant- UI</span>
            <a href="https://www.linkedin.com/in/amit-jadhav-3308782b/" target="_blank">
                <img class="team-img" width="150px" height="160px" src="${(contextPath)!''''}/cf/files/bc03c67f-a5a0-4063-8ef4-c50ed8f076f3" alt="Amit Jadhav" title="Lead Consutant- UI"/>
                 <div class="udetails">
               <span class="desg">Amit Jadhav</span>
                   
               
                </div>
            </a>
        </div>
     
        <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 profilepic">

                <span class="namcls">Sr Technical Analyst</span>
            <a href="https://www.linkedin.com/in/mini-pillai-b01807112/" target="_blank">
                <img class="team-img" width="150px" height="160px" src="${(contextPath)!''''}/cf/files/fa498f47-ba98-4f07-bb61-efc2508b21f4" alt="Mini Pillai" title="Sr Technical Analyst"/>
                  <div class="udetails">
             <span class="desg">Mini Pillai</span>
                      
               
                </div>
            </a>
        </div>

        <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 profilepic">
                    
                <span class="namcls">Sr Software Engineer</span>
            <a href="https://www.linkedin.com/in/ravi-gowda-9a5a6aa9" target="_blank">
                <img class="team-img" width="150px" height="160px" src="${(contextPath)!''''}/cf/files/e3dd0b7c-242a-47b3-9a36-c23ef378b10f" alt="Ravi Gowda" title="Sr Software Engineer"/>
                  <div class="udetails">
               <span class="desg">Ravi Gowda</span>
               
                </div>
            </a>
        </div>
        
        <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 profilepic">
                <span class="namcls">Sr Technical Analyst</span>
            <a href="https://www.linkedin.com/in/satishchandra-pandey-4b490031/" target="_blank">
                <img class="team-img" width="150px" height="160px" src="${(contextPath)!''''}/cf/files/6b268691-ddfc-4d3f-9abd-bbc88bbfe974" alt="Satish Pandey" title="Sr Technical Analyst"/>
                  <div class="udetails">
                     
              <span class="desg">Satish Pandey</span>
               
                </div>
            </a>
        </div>
        
        <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 profilepic">

                <span class="namcls">Designer- UI</span>
            <a href="https://www.linkedin.com/in/satyawan-sawant-06925212a/" target="_blank">
                <img class="team-img" width="150px" height="160px" src="${(contextPath)!''''}/cf/files/c91b58b7-0a29-45c1-960d-3648821d4547" alt="Satyawan Sawant" title="Designer- UI"/>
                <div class="udetails">
                 
                <span class="desg">Satyawan Sawant</span>
               
                </div>
            </a>
        </div>
    </div>
    
</div>
', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);

REPLACE INTO jq_autocomplete_details(ac_id,ac_description,ac_select_query,ac_type_id, created_by, created_date, last_updated_ts)
VALUES ('home-page-role','Role autocomplete for home page'
,'SELECT jr.role_id AS roleId, jr.role_name AS roleName FROM jq_role AS jr 
 WHERE jr.role_name LIKE CONCAT("%", :searchText, "%") 
 AND jr.is_active = 1 
 AND jr.role_id NOT IN (SELECT mra.role_id FROM module_role_association AS mra WHERE mra.is_deleted = 0)
 LIMIT :startIndex, :pageSize'
,2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_autocomplete_details(ac_id, ac_description, ac_select_query, ac_type_id, created_by, created_date, last_updated_ts) VALUES
('user-favorite-entity-autocomplete', '', 
'SELECT jqfe.favorite_id AS favoriteId, jqfe.entity_type AS entityType, jqfe.entity_id AS entityId,
jqfe.entity_name AS entityName, jmm.auxiliary_data AS formData
FROM jq_user_favorite_entity AS jqfe
LEFT OUTER JOIN jq_master_modules AS jmm ON jmm.module_name = jqfe.entity_type
WHERE jqfe.user_email_id = :loggedInUserName AND jqfe.entity_name LIKE CONCAT("%", :searchText, "%")
ORDER BY entity_type, entity_name ASC 
LIMIT :startIndex, :pageSize'
, 2, 'aar.dev@trigyn.com', NOW(), NOW());

REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id, created_by, created_date, last_updated_ts) VALUES
('table-autocomplete', 'table autocomplete', '<#if dbProductName == "default"> 
SELECT TABLE_NAME AS tableName  
FROM information_schema.TABLES  
WHERE table_schema = (SELECT DATABASE())  
AND TABLE_NAME LIKE CONCAT("%", :searchText, "%") AND TABLE_TYPE ="BASE TABLE" 
AND CASE WHEN (SELECT (property_value) FROM jq_property_master WHERE property_name = "version") NOT LIKE "%SNAPSHOT%"  
THEN TABLE_NAME NOT LIKE "jq_%" ELSE 1 END AND TABLE_NAME NOT IN("flyway_schema_history", "persistent_logins") 
ORDER BY tableName
LIMIT :startIndex, :pageSize 

<#elseif dbProductName == "mariadb">
SELECT TABLE_NAME AS tableName  
FROM information_schema.TABLES  
WHERE table_schema = (SELECT DATABASE())  
AND TABLE_NAME LIKE CONCAT("%", :searchText, "%") AND TABLE_TYPE ="BASE TABLE"
ORDER BY tableName 
LIMIT :startIndex, :pageSize 

<#elseif dbProductName == "mysql">
SELECT TABLE_NAME AS tableName  
FROM information_schema.TABLES  
WHERE table_schema = (SELECT DATABASE())  
AND TABLE_NAME LIKE CONCAT("%", :searchText, "%") AND TABLE_TYPE ="BASE TABLE" 
ORDER BY tableName
LIMIT :startIndex, :pageSize 

<#elseif dbProductName == "postgresql">
SELECT table_name AS "tableName"
FROM information_schema.tables
WHERE table_schema = ''public''
AND table_catalog = current_database()
AND table_type =''BASE TABLE''
ORDER BY "tableName"
OFFSET :startIndex LIMIT :pageSize

<#elseif dbProductName == "sqlserver">
SELECT TABLE_NAME AS tableName  
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = ''BASE TABLE'' AND TABLE_CATALOG = DB_NAME()
ORDER BY tableName
OFFSET :startIndex ROWS FETCH NEXT :pageSize ROWS ONLY

<#elseif dbProductName == "oracle:thin">
select object_name as "tableName" from all_objects t where object_type = ''TABLE'' and owner = :dataSourceUserName order by object_name 


</#if>' 
, 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id, created_by, created_date, last_updated_ts) VALUES
('site-layout-url-autocomplete', 'Site layout', 'SELECT jml.module_id AS moduleId, jmli18n.module_name AS text
FROM jq_module_listing AS jml
INNER JOIN jq_module_listing_i18n AS jmli18n ON jmli18n.module_id = jml.module_id AND jmli18n.language_id = :languageId
WHERE jmli18n.module_name LIKE CONCAT("%", :searchText, "%") LIMIT :startIndex, :pageSize', 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_module_listing (module_id, module_url, parent_id, target_lookup_id, target_type_id, sequence, is_inside_menu, is_home_page, module_type_id, request_param_json) VALUES
('4f81625d-c3f1-47ec-88d4-e85be439df19', 'jqhm', NULL, 5, '8a80cb8175513bc80175514206ef0000', NULL, 0, 0, 2, '{"mt":"07cf45ae-2987-11eb-a9be-e454e805e22f","sl":"1"}');

REPLACE INTO jq_module_listing_i18n (module_id, language_id, module_name) VALUES
('4f81625d-c3f1-47ec-88d4-e85be439df19', 1, 'JQuiver Help Manual');

REPLACE INTO jq_module_listing(module_id, module_url, parent_id, target_lookup_id, target_type_id, sequence, is_inside_menu, include_layout, is_home_page, last_modified_date, module_type_id) VALUES
('8dc8f8b5-0f2c-40fe-8148-b90f8c2e3725', 'team', NULL, 5, '52203478-5dc4-4823-999e-cea88ae10cad', NULL, 0, 1, 0, NOW(), 2);

REPLACE INTO jq_module_listing_i18n(module_id, language_id, module_name) VALUES
('8dc8f8b5-0f2c-40fe-8148-b90f8c2e3725', 1, 'jquiver-developer');

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('d8146692-97e8-4cd4-97c0-412ff7a1513f', 'home-page-role', 'home-page-role', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0), 
('2eaea3fc-54c5-11eb-9e7a-f48e38ab8cd7', 'home-page-role', 'home-page-role', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0), 
('ec8750ed-3500-42c0-a925-14b822338273', 'home-page-role', 'home-page-role', '91a81b68-0ece-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0);


REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('586d1b73-c333-47a0-b3d0-93d383cdcf29', '55b93b76-54a5-11eb-9e7a-f48e38ab8cd7', 'config-home-page-listing', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0),
('329eb2de-54c5-11eb-9e7a-f48e38ab8cd7', '55b93b76-54a5-11eb-9e7a-f48e38ab8cd7', 'config-home-page-listing', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0), 
('e3413a71-1707-4620-a17e-032fc4f3ba37', '55b93b76-54a5-11eb-9e7a-f48e38ab8cd7', 'config-home-page-listing', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0);


REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('776c0d05-9339-4a3a-b28b-b42224d3f438', 'homePageListingGrid', 'Home Page Listing', '07067149-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0), 
('369e53f3-54c5-11eb-9e7a-f48e38ab8cd7', 'homePageListingGrid', 'Home Page Listing', '07067149-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0), 
('7b7c93fb-702e-475e-8279-036f4321a7a3', 'homePageListingGrid', 'Home Page Listing', '07067149-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0);


REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('024a85dc-7b39-11eb-9439-0242ac130002', 'customResourceBundleListingGrid', 'Custom DB Resource Bundle Listing', '07067149-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0), 
('024a85dc-7b39-11eb-9439-0242ac130001', 'customResourceBundleListingGrid', 'Custom DB Resource Bundle Listing', '07067149-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0), 
('024a85dc-7b39-11eb-9439-0242ac130012', 'customResourceBundleListingGrid', 'Custom DB Resource Bundle Listing', '07067149-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'e37c55f6-5638-482e-be02-85cc69e5709c', 1, 0);


REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('b338e1c2-1c92-4f15-88f7-9521dea66522', 'user-favorite-entity-autocomplete', 'user-favorite-entity-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('cbc3293f-d7ab-4d93-8c95-a032aef8812c', 'user-favorite-entity-autocomplete', 'user-favorite-entity-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('f381799b-ba9a-4a9a-948a-50f86e7b6a81', 'user-favorite-entity-autocomplete', 'user-favorite-entity-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);

REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('17a65a59-082d-4ece-9702-0e0117be17bd', '0d6041fd-7ffd-11eb-971b-f48e38ab8cd7', 'user-favorite-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('3a301779-a9cd-4434-aef3-cdc0973914d3', '0d6041fd-7ffd-11eb-971b-f48e38ab8cd7', 'user-favorite-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('9d5d82b2-0737-4f5f-b0ed-f4d25069955a', '0d6041fd-7ffd-11eb-971b-f48e38ab8cd7', 'user-favorite-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);

REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('0495ca99-39b9-454d-9857-d683b6a00790', 'table-autocomplete', 'table-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('5c0232e5-5d03-4c90-bbbd-6a1c6b6f7492', 'table-autocomplete', 'table-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('a795f8c0-d65f-422f-a5ed-e036654f0cb6', 'table-autocomplete', 'table-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);


REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('0becc4b4-5a23-4c38-a425-138ce13461ad', 'site-layout-url-autocomplete', 'site-layout-url-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('13e30281-e279-4cb7-abf6-dd6d8cea0243', 'site-layout-url-autocomplete', 'site-layout-url-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('62699ec3-7a9f-468a-ae2c-98ba0d529e9f', 'site-layout-url-autocomplete', 'site-layout-url-autocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);


REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('094f191c-ab00-4801-8ce6-996858fc099d', '52203478-5dc4-4823-999e-cea88ae10cad', 'jq-team-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('9583cb59-b84a-4d23-8ff0-0abbe54b896a', '52203478-5dc4-4823-999e-cea88ae10cad', 'jq-team-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('d3a1d4ca-26ee-4639-8a8f-c21db1498adf', '52203478-5dc4-4823-999e-cea88ae10cad', 'jq-team-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);


REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('8526c66c-4cc6-4280-84ca-f1a62d382287', '8dc8f8b5-0f2c-40fe-8148-b90f8c2e3725', 'jquiver-developer', 'c6cc466a-0ed3-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('b8f4012c-f075-49ae-95fe-86898c608202', '8dc8f8b5-0f2c-40fe-8148-b90f8c2e3725', 'jquiver-developer', 'c6cc466a-0ed3-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('dbfc6290-9678-4ac1-b78c-d33e6f55d82d', '8dc8f8b5-0f2c-40fe-8148-b90f8c2e3725', 'jquiver-developer', 'c6cc466a-0ed3-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);

REPLACE INTO jq_file_upload(file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) VALUES
('0cad6a17-9f5f-4d25-b983-b8b691a144d0', 'default', '/images', 'ADC.png', 'd750854d-92fd-4486-a9f8-13949b05fa16', 'admin@jquiver.com', NOW(), 'default'), 
('1f4e7749-3365-400a-98de-2716a1de01fc', 'default', '/images', 'Akram-Bhasha.jfif', '7b505847-580a-46af-9368-db7c1f46d0ec', 'admin@jquiver.com', NOW(), 'default'),
('4efc83a2-4ef9-40bd-a60b-a5d639d1bea5', 'default', '/images', 'Aman-Prasad.png', '293dc7a1-39ff-4192-bf80-9b06b8d1dffc', 'admin@jquiver.com', NOW(), 'default'), 
('6b268691-ddfc-4d3f-9abd-bbc88bbfe974', 'default', '/images', 'Satish-Pandey.jpg', '6a42bf98-8a8d-4be6-897a-559b256dfe28', 'admin@jquiver.com', NOW(), 'default'), 
('756de33e-b86a-11eb-9690-f48e38ab8cd7', 'default', '/images', 'Abhay-Desai.png', '33283eb3-e224-40e8-aa6c-9ba48cebaaba', 'admin@jquiver.com', NOW(), 'default'),
('bc03c67f-a5a0-4063-8ef4-c50ed8f076f3', 'default', '/images', 'Amit-Jadhav.jpg', 'b7b9c9fd-3bbc-46d3-bf59-d5e343f2e332', 'admin@jquiver.com', NOW(), 'default'), 
('c91b58b7-0a29-45c1-960d-3648821d4547', 'default', '/images', 'Satyawan-Sawant.jpg', '9eb15762-540b-42b1-948b-29242ffcb20e', 'admin@jquiver.com', NOW(), 'default'), 
('e3dd0b7c-242a-47b3-9a36-c23ef378b10f', 'default', '/images', 'Ravi-Gowda.png', '6b7d7cb1-8b51-4626-8135-6181fcb88fe5', 'admin@jquiver.com', NOW(), 'default'),
('fa498f47-ba98-4f07-bb61-efc2508b21f4', 'default', '/images', 'Mini-Pillai.jpg', '960c885c-b4d9-4ea5-a58d-4cbd95ce82f6', 'admin@jquiver.com', NOW(), 'default');

SET FOREIGN_KEY_CHECKS=1;