SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('c0a5553a-0a37-11eb-a894-f48e38ab8cd7', 'file-upload-config-listing', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script>      
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="${contextPath!''''}/webjars/1.0/JSCal2/js/lang/en.js"></script> 
</head>

<div class="container">
        <div class="topband">
        <h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.fileUploadConfig'')}</h2> 
        <div class="float-right">
       		<span onclick="fileUploadMaster();">
				<input id="fileUploadBtn" class="btn btn-primary pull-left margin-r-5" name="fileUploadBtn" value="Common Files" type="button">
			</span>
		  <form id="formFileUpload" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="40289d3d750decc701750e3f1e3c0000"/>
                <input type="hidden" name="fileBinId" id="fileBinId" value=""/>
                <button type="submit" class="btn btn-primary">${messageSource.getMessage(''jws.addFileConfiguration'')}</button>
            </form>
			<span onclick="backToWelcomePage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	   
        </div>
        
        <div class="clearfix"></div>        
        </div>
        
        <div id="divFileConfigListingGrid"></div>

</div>



<script>
	contextPath = "${(contextPath)!''''}";
	
	$(function () {
		let formElement = $("#formFileUpload")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("file-upload-config", formDataJson);
		
	    let colM = [
	        { title: "File Bin Id", width: 130, dataIndx: "fileBinId", align: "left", halign: "center", 
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "File Type Supported", width: 100, dataIndx: "fileTypeSupported", align: "left", halign: "center", 
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
	        { title: "Max File Size", width: 160, dataIndx: "maxFileSize", align: "left", halign: "center", 
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
			{ title: "No Of Files", width: 160, dataIndx: "noOfFiles", align: "left", halign: "center", 
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Last Updated Date", width: 160, dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: formatLastUpdatedDate },
	        { title: "${messageSource.getMessage(''jws.action'')}", maxWidth: 145, dataIndx: "action", align: "center", halign: "center", render: editFileUploadConfig, sortable: false}
	    ];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "lastUpdatedTs",
        	sortDir: "down",
    	};	    
	    let grid = $("#divFileConfigListingGrid").grid({
	      gridId: "fileUploadConfigGrid",
	      colModel: colM,
          dataModel: dataModel
	  	});
  	});
  	
  	function formatLastUpdatedDate(uiObject){
        const lastUpdatedTs = uiObject.rowData.lastUpdatedTs;
        return formatDate(lastUpdatedTs);
    }
    
	function editFileUploadConfig(uiObject) {
		const fileBinId = uiObject.rowData.fileBinId;
		return ''<span id="''+fileBinId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title="Edit file configuration"></i></span>''.toString();
	}
	
	function submitForm(element) {
	  $("#fileBinId").val(element.id);
	  $("#formFileUpload").submit();
	}
	
		
    function fileUploadMaster() {
		location.href = contextPath+"/view/fum";
	}
	
	function backToWelcomePage() {
        location.href = contextPath+"/cf/home";
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);


REPLACE INTO jq_dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id, last_updated_ts) VALUES
('40289d3d750decc701750e3f1e3c0000', 'file-upload-config', 'File Upload Config Form', 'SELECT fuc.file_bin_id AS fileBinId, fuc.file_type_supported AS fileTypeSupported
, fuc.max_file_size AS maxFileSize, fuc.no_of_files AS noOfFiles, fuc.select_query_content AS selectQueryContent
, fuc.upload_query_content AS uploadQueryContent
, fuc.view_query_content AS viewQueryContent
, fuc.delete_query_content AS deleteQueryContent, fuc.last_updated_by AS updatedBy
, fuc.last_updated_ts AS updatedDate,fuc.is_deleted AS isDeleted
FROM jq_file_upload_config AS fuc
WHERE fuc.file_bin_id = "${fileBinId}" AND is_deleted = 0;', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/monaco/require.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/monaco/min/vs/loader.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/1.0/fileupload/fileBinMaster.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script> 
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/markdown/highlight/github.min.css" />
<script src="${(contextPath)!''''}/webjars/1.0/markdown/highlight/highlight.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
</head>

<div class="container">
	<div class="row topband">
		<div class="col-8">
			<#if (resultSet)?? && (resultSet)?has_content>
			    <h2 class="title-cls-name float-left">Edit File Bin</h2> 
	        <#else>
	            <h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.addFileConfiguration'')}</h2> 
	        </#if>
	    </div>
	    
        <div class="col-4">  
	        <#if (resultSet)?? && (resultSet)?has_content>  
		        <#assign ufAttributes = {
		            "entityType": "File Bin",
		            "entityId": "fileBinId",
		            "entityName": "fileBinId"
		        }>
		        <@templateWithParams "user-favorite-template" ufAttributes />
		    </#if>
		 </div>
        
		<div class="clearfix"></div>		
	</div>
  <form method="post" name="addEditForm" id="addEditForm">
    	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
        	<div class="row">
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="fileBinId" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              File bin id
		            </label>
				<input type="text" id="fileBinId" name="fileBinId" onchange="fileBinMaster.updateFileBinTemplate()" value="" maxlength="500" class="form-control">
			</div>
		</div>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="fileTypeSupported" style="white-space:nowrap">
		              File type supported
		            </label>
				<input type="text" id="fileTypeSupported" name="fileTypeSupported" placeholder=".png, .jpg, .gif" value="" class="form-control">
			</div>
		</div>
    		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<span class="asteriskmark">*</span>
				<label for="maxFileSize">Max file size</label>
                <div class="row">
                    <div class="col-9">
                        <input type="number" id="maxFileSizeUi" name="maxFileSizeUi" value="" onkeyup="fileBinMaster.saveCurrentFileSize(event)" onkeydown="fileBinMaster.validateMaxFileSize(event)" maxlength="10" class="form-control">
                        <input type="hidden" id="maxFileSize" name="maxFileSize" value="" maxlength="10" class="form-control">
                    </div>
                    <div class="col-3">
                        <span class="float-right">
                            <select id="size" onChange="fileBinMaster.updateFileSize();" class="form-control">
                                <option value="1">Byte</option>
                                <option value="2">Kilobyte</option>
                                <option value="3">Megabyte</option>
                            </select>
                        </span>
                    </div>
                </div>
			</div>
		</div>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label for="noOfFiles" style="white-space:nowrap">
                    <span class="asteriskmark">*</span>No of files (between 1 and 50):
                </label>
                <input type="hidden" id="noOfFiles" name="noOfFiles">
                <p class="file-slider-span-cls"><span id="fileSliderSpan" class="no-of-files-counter"></span></p>
			    <div class="file-slider-div-cls" id="fileSliderDiv"></div>
			</div>
		</div>

    	</div>
    
    <input type="hidden" id="previousFileSizeScale" value="1">	
    <input type="hidden" id="initialFileSizeScale" value="1">	
    <input type="hidden" id="previousFileSize" value="">
    
    <div id="sqlParameterDiv" class="col-12 method-sign-info-div">
		<h3 class="titlename method-sign-info">
		    <i class="fa fa-lightbulb-o" aria-hidden="true"></i><label for="sqlParameter">SQL/FTL Parameters</label>
	    </h3>
		<span id="sqlParameter">fileBinId, fileUploadId, fileAssociationId, moduleName, loggedInUserName, loggedInUserRoleList{}<span>
    </div>
    
	<div class="row margin-t-b">
		<div class="col-12">
			<h3 class="titlename"><span class="file-validator-title" data-query-name="selectValidator_span">${messageSource.getMessage("jws.selectFileSQL")}</span>
				<label class="select-validator-label switch"> 
                    <input type="checkbox" id="selectValidator_chkbox" onclick="fileBinMaster.enableDisableValidator(''selectValidator'');">
                    <span class="slider round"></span>
                </label>
            </h3>
			<div id="selectValidator_div" class="sql_script" style="opacity: 0.4;">
				<div class="grp_lblinp">
					<div id="selectValidator_container" class="ace-editor-container">
						<div id="selectValidator" class="ace-editor"></div>
					</div>
				</div>
			</div>	
		</div>
	</div>
	
	<div class="row margin-t-b">
		<div class="col-12">
			<h3 class="titlename"><span class="file-validator-title" data-query-name="uploadValidator_span">${messageSource.getMessage("jws.uploadFileSQL")}</span>
				<label class="upload-validator-label switch"> 
                    <input type="checkbox" id="uploadValidator_chkbox" onclick="fileBinMaster.enableDisableValidator(''uploadValidator'');">
                    <span class="slider round"></span>
                </label>
            </h3>
			<div id="uploadValidator_div" class="sql_script" style="opacity: 0.4;">
				<div class="grp_lblinp">
					<div id="uploadValidator_container" class="ace-editor-container">
						<div id="uploadValidator" class="ace-editor"></div>
					</div>
				</div>
			</div>	
		</div>
	</div>
	
	<div class="row margin-t-b">
		<div class="col-12">
			<h3 class="titlename"><span class="file-validator-title" data-query-name="viewValidator_span">${messageSource.getMessage("jws.viewFileSQL")}</span>
            	<label class="view-validator-label switch"> 
                    <input type="checkbox" id="viewValidator_chkbox" onclick="fileBinMaster.enableDisableValidator(''viewValidator'');">
                    <span class="slider round"></span>
                </label>
            </h3>
			<div id="viewValidator_div" class="sql_script" style="opacity: 0.4;">
				<div class="grp_lblinp">
					<div id="viewValidator_container" class="ace-editor-container">
						<div id="viewValidator" class="ace-editor"></div>
					</div>
				</div>
			</div>	
		</div>
	</div>
	
	<div class="row margin-t-b">
		<div class="col-12">
			<h3 class="titlename"><span class="file-validator-title" data-query-name="deleteValidator_span">${messageSource.getMessage("jws.deleteFileSQL")}</span>
				<label class="delete-validator-label switch"> 
                    <input type="checkbox" id="deleteValidator_chkbox" onclick="fileBinMaster.enableDisableValidator(''deleteValidator'');">
                    <span class="slider round"></span>
                </label>    
            </h3>
			<div id="deleteValidator_div" class="sql_script" style="opacity: 0.4;">
				<div class="grp_lblinp">
					<div id="deleteValidator_container" class="ace-editor-container">
						<div id="deleteValidator" class="ace-editor"></div>
					</div>
				</div>
			</div>	
		</div>
	</div>
	
	  <#if (resultSet)?? && resultSet?size !=0>
      	<#list resultSet as resultSetList>
			<textarea id="selectValidator_query" name="selectValidator_query" style="display: none">
				${resultSetList?api.get("selectQueryContent")!''''}
			</textarea>
			<textarea name="uploadValidator_query" id="uploadValidator_query" style="display: none">
				${resultSetList?api.get("uploadQueryContent")!''''}
			</textarea>
			<textarea name="viewValidator_query" id="viewValidator_query" style="display: none">
				${resultSetList?api.get("viewQueryContent")!''''}
			</textarea>
			<textarea name="deleteValidator_query" id="deleteValidator_query" style="display: none">
				${resultSetList?api.get("deleteQueryContent")!''''}
			</textarea>
      		<#break>
      	</#list>
      <#else>
      	 	<textarea id="selectValidator_query" name="selectValidator_query" style="display: none"></textarea>
			<textarea id="uploadValidator_query" name="uploadValidator_query" style="display: none"></textarea>
            <textarea id="viewValidator_query" name="viewValidator_query" style="display: none"></textarea>
            <textarea id="deleteValidator_query" name="deleteValidator_query" style="display: none"></textarea>
      </#if>
	   
  </form>
  
  <div id="tabs">
        <ul>
            <li><a href="#htmlContent" data-target="htmlContent">${messageSource.getMessage("jws.htmlContent")}</a></li>
            <li><a href="#jsContent" data-target="jsContent">${messageSource.getMessage("jws.javaScriptContent")}</a></li>
        </ul>
        <div id="htmlContent">
	        <div class="cm-main-wrapper preview cm-scrollbar clearfix">
	            <div id="contentDiv">
	                <div id="htmlPreview" class="default-previews cm-scrollbar"></div>
	            </div>
	        </div>
        </div>
        <div id="jsContent">
	        <div class="cm-main-wrapper preview cm-scrollbar clearfix">
	            <div id="contentDiv">
	                <div id="jsPreview" class="default-previews cm-scrollbar"></div>
	            </div>
	        </div>
        </div>
    </div> 
    
     <div id="manual-container" class="cm-rightbar">
        <div class="row">
            <div class="col-md-3">
                <div id="previewDiv" style="display:none;">
                    <textarea id="previewContent" style="display:none;"></textarea>
                </div>
            </div>
        </div>
    </div>
    
	<br>
	<div class="row">
		<div class="col-3">	
      		<input id="moduleId" value="248ffd91-7760-11eb-94ed-f48e38ab8cd7" name="moduleId"  type="hidden">
      		<@templateWithoutParams "role-autocomplete"/>
      </div>
	</div>
	
	<div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropup custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''file-upload-config'', this, fileBinMaster.saveData.bind(fileBinMaster), fileBinMaster.backToPreviousPage);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls"  id="actionDiv">
                    	<ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="typeOfAction(''file-upload-config'', this, fileBinMaster.saveData.bind(fileBinMaster), fileBinMaster.backToPreviousPage);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="typeOfAction(''file-upload-config'', this, fileBinMaster.saveData.bind(fileBinMaster), fileBinMaster.backToPreviousPage);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                        </ul>
                    </div> 
                </div>
				<span onclick="fileBinMaster.backToPreviousPage();">
					<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
				</span> 
			</div>
		</div>
	</div>
	
	
</div>
<script>
	let formId = "${formId}";
	contextPath = "${contextPath}";
	let edit = 0;
	let fileBinMaster;

  $(function(){
  	  fileBinMaster = new FileBinMaster();
  	  $("#tabs").tabs();
      <#if (resultSet)??>
      	<#list resultSet as resultSetList>
      		$("#fileBinId").val(''${resultSetList?api.get("fileBinId")}'');
      		$("#fileTypeSupported").val(''${resultSetList?api.get("fileTypeSupported")}'');
            $("#maxFileSize").val(''${resultSetList?api.get("maxFileSize")}'');
            $("#maxFileSizeUi").val(''${resultSetList?api.get("maxFileSize")}'');
            $("#previousFileSize").val(''${resultSetList?api.get("maxFileSize")}'');
            $("#fileSliderSpan").html(''${resultSetList?api.get("noOfFiles")}'');
            $("#noOfFiles").val(''${resultSetList?api.get("noOfFiles")}'');
      	</#list>
      </#if>
    
		<#if (requestDetails?api.get("fileBinId")) != "">
            edit = 1;
        </#if>
    
		if(typeof getSavedEntity !== undefined && typeof getSavedEntity === "function"){
			getSavedEntity();
		}    
		savedAction("file-upload-config", edit);
		hideShowActionButtons();
		fileBinMaster.setQueryContent();
		fileBinMaster.loadFinBinDetails();
		$("a[href=''#htmlContent'']").click();
  });
  
 
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2, NOW());

REPLACE INTO jq_dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('40289d3d750decc701750e3f1e5c0001', '40289d3d750decc701750e3f1e3c0000', '<#if (formData?api.getFirst("edit"))?has_content>
    UPDATE jq_file_upload_config SET 
    file_type_supported = ''${formData?api.getFirst("fileTypeSupported")}''
    ,max_file_size = ''${formData?api.getFirst("maxFileSize")}''
    ,no_of_files = ''${formData?api.getFirst("noOfFiles")}''
    ,select_query_content = <#if formData?api.getFirst("selectValidator_query")?? && formData?api.getFirst("selectValidator_query")?has_content>''${formData?api.getFirst("selectValidator_query")}''<#else>NULL</#if>
    ,upload_query_content = <#if formData?api.getFirst("uploadValidator_query")?? && formData?api.getFirst("uploadValidator_query")?has_content>''${formData?api.getFirst("uploadValidator_query")}''<#else>NULL</#if>
    ,view_query_content = <#if formData?api.getFirst("viewValidator_query")?? && formData?api.getFirst("viewValidator_query")?has_content>''${formData?api.getFirst("viewValidator_query")}''<#else>NULL</#if>
    ,delete_query_content = <#if formData?api.getFirst("deleteValidator_query")?? && formData?api.getFirst("deleteValidator_query")?has_content>''${formData?api.getFirst("deleteValidator_query")}''<#else>NULL</#if>
    ,last_updated_by = :loggedInUserName
    ,last_updated_ts = NOW() 
	,is_deleted = 0
    WHERE file_bin_id = ''${formData?api.getFirst("fileBinId")}''
<#else>
    INSERT INTO jq_file_upload_config (file_bin_id, file_type_supported, max_file_size, no_of_files, select_query_content, upload_query_content, view_query_content, delete_query_content, is_deleted, created_by, created_date, last_updated_ts ) 
    VALUES (''${formData?api.getFirst("fileBinId")}'',''${formData?api.getFirst("fileTypeSupported")}''
    , ''${formData?api.getFirst("maxFileSize")}'',''${formData?api.getFirst("noOfFiles")}''
    , <#if formData?api.getFirst("selectValidator_query")?? && formData?api.getFirst("selectValidator_query")?has_content>''${formData?api.getFirst("selectValidator_query")}''<#else>NULL</#if>
    , <#if formData?api.getFirst("uploadValidator_query")?? && formData?api.getFirst("uploadValidator_query")?has_content>''${formData?api.getFirst("uploadValidator_query")}''<#else>NULL</#if>
    , <#if formData?api.getFirst("viewValidator_query")?? && formData?api.getFirst("viewValidator_query")?has_content>''${formData?api.getFirst("viewValidator_query")}''<#else>NULL</#if>
    , <#if formData?api.getFirst("deleteValidator_query")?? && formData?api.getFirst("deleteValidator_query")?has_content>''${formData?api.getFirst("deleteValidator_query")}''<#else>NULL</#if>
    ,0,:loggedInUserName,NOW(),NOW())
</#if>', 1, NULL);


REPLACE INTO  jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('7df80a38-7740-11eb-94ed-f48e38ab8cd7', 'common-files', '<head>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
    <script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
    <script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
    <script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/dropzone/dist/dropzone.js"></script>
    <link rel="stylesheet" type="text/css" href="${(contextPath)!''''}/webjars/1.0/dropzone/dist/dropzone.css" />
    <script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/fileupload/fileupload.js"></script>
</head>
<div class="container" style="padding-top: 40px">
    <div class="topband">
        <h2 class="title-cls-name float-left">Common Files</h2> 
        <div class="float-right">
       

			<span onclick="backToFileUploadManager();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	 
			<div id="fileIdDiv"></div>    
        </div>
        
        <input type="text" id="copyFilePathInput" name="copyFilePathInput" style="display:none">
        <div class="clearfix"></div>        
        </div>

	<div class="col-12">
        <div id="fileUploadMaster" class="col-8 fileupload dropzone"></div>
    </div>
</div>

<script>
    contextPath = "${(contextPath)!''''}";
    let dropzoneElement = $(".fileupload").fileUpload({
        fileBinId : "default",
        fileAssociationId: "default",
        createFileBin : true,
        renderer: fileConfigRenderer,
    });

	let dropZone = $.fn.fileUpload({
        fileBinId : "default",
        fileAssociationId: "default",
        createFileBin : false,
        renderer: fileListing
    });
    
    $(function () {
        dropZone.getSelectedFiles();
    });

    function fileListing(fileObj){
        let input = $("<input id=''"+fileObj["id"]+"'' value=''"+fileObj["id"]+"'' type=''text''>");
    //    input.insertAfter($("#fileIdDiv"));
    }

    function fileConfigRenderer(fileObj) {
        let fileUploadId = fileObj["id"];
        let fileName = fileObj["name"];
        let btnTxt = resourceBundleData("jws.copyFilePath,jws.fileName");
        let actionElem = "<div><span  title=''"+btnTxt["jws.copyFilePath"]+"''><i class=''fileupload-actions fa fa-copy float-right''  onclick=\\"copyFilePath(''"+fileUploadId+"'')\\"></i></span>" + 
        	"<span  title=''"+btnTxt["jws.fileName"]+"''><i class=''fileupload-actions fa fa-info float-right'' onclick=\\"fileName(''"+fileName+"'')\\"></i></span></div>";
        return actionElem;
	}
	
	function fileName(fileName){
        showMessage("File Name: " + fileName, "success");
    }

    function copyFilePath(fileUploadId){
        let input = $("<input>");
        $("body").append(input);
        input.val(window.location.origin + "/cf/files/" + fileUploadId).select();
        document.execCommand("copy");
        input.remove();
        showMessage("File path copied successfully", "success");
    }



	function backToFileUploadManager() {
		location.href = contextPath+"/cf/fucl";
	}

</script>'

, 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2); 

DELETE FROM jq_dynamic_rest_dao_details WHERE jws_dynamic_rest_details_id = 
(SELECT jws_dynamic_rest_id FROM jq_dynamic_rest_details WHERE jws_method_name = "getFileConfigDetails");

DELETE FROM jq_dynamic_rest_details WHERE jws_method_name = "getFileConfigDetails"; 

REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
("c04bfb18-7a59-11eb-94ed-f48e38ab8cd7", 'fileconfig-details', 1, 'getFileConfigDetails', 'Get file config details', 2, 7, 'function getFileConfigDetails(requestDetails, daoResults) {
    return daoResults["fileConfigs"][0];
}

getFileConfigDetails(requestDetails, daoResults);', 3, 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(19, "c04bfb18-7a59-11eb-94ed-f48e38ab8cd7", 'fileConfigs', 'select fuc.* from jq_file_upload_config as fuc where fuc.file_bin_id IN (:fileBinId, "default")
order by FIELD(file_bin_id, :fileBinId, "default")
LIMIT 1', 1, 1);



REPLACE INTO jq_module_listing (module_id, module_url, parent_id, target_lookup_id, target_type_id, sequence, is_inside_menu, is_home_page, module_type_id) VALUES
('74f78214-3add-4e18-84d6-fb3b76805556', 'fum', NULL, 5, '7df80a38-7740-11eb-94ed-f48e38ab8cd7', NULL, 0, 0, 2);


REPLACE INTO jq_module_listing_i18n (module_id, language_id, module_name) VALUES
('74f78214-3add-4e18-84d6-fb3b76805556', 1, 'common-files');


/***************************************************default File Queries - Start************************************************************/

REPLACE INTO jq_file_upload_config (file_bin_id, file_type_supported, max_file_size, no_of_files, select_query_content, upload_query_content, view_query_content, delete_query_content, is_deleted, created_by, created_date, last_updated_ts) VALUES
('default', '', 32000000, 50, 

/* default - SELECT Query start */
'SELECT jfu.file_association_id AS fileAssociationId, jfu.file_bin_id AS fileBinId, jfu.file_path 
AS filePath, jfu.file_upload_id AS fileUploadId, jfu.original_file_name AS originalFileName, 
jfu.physical_file_name AS physicalFileName 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id 
WHERE fug.file_bin_id = :fileBinId AND jfu.file_association_id = :fileAssociationId 
<#if loggedInUserName != "admin@jquiver.com">
    AND jfu.updated_by = :loggedInUserName
</#if>'
, 


/* default - UPLOAD Query start */
'SELECT COUNT(DISTINCT fug.file_bin_id) AS isAllowed 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_entity_role_association AS jera ON jera.entity_id = fug.file_bin_id  
AND jera.is_active = 1 
INNER JOIN jq_master_modules AS jmm ON jmm.module_id = jera.module_id AND 
jmm.module_name = :moduleName 
INNER JOIN jq_user_role_association AS jura ON jura.role_id = jera.role_id  
INNER JOIN jq_user AS jw ON jw.user_id = jura.user_id AND jw.email = :loggedInUserName 
LEFT OUTER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id 
WHERE fug.file_bin_id = :fileBinId '
, 


/* default - VIEW Query start */
'SELECT COUNT(DISTINCT fug.file_bin_id) AS isAllowed 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_entity_role_association AS jera ON jera.entity_id = fug.file_bin_id  
AND jera.is_active = 1 
INNER JOIN jq_master_modules AS jmm ON jmm.module_id = jera.module_id AND 
jmm.module_name = :moduleName 
INNER JOIN jq_user_role_association AS jura ON jura.role_id = jera.role_id  
INNER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id 
WHERE fug.file_bin_id = :fileBinId '


/* default - DELETE Query start */
, 'SELECT COUNT(DISTINCT fug.file_bin_id) AS isAllowed 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_entity_role_association AS jera ON jera.entity_id = fug.file_bin_id  
AND jera.is_active = 1 
INNER JOIN jq_master_modules AS jmm ON jmm.module_id = jera.module_id AND 
jmm.module_name = :moduleName 
INNER JOIN jq_user_role_association AS jura ON jura.role_id = jera.role_id  
INNER JOIN jq_user AS jw ON jw.user_id = jura.user_id AND jw.email = :loggedInUserName 
INNER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id 
WHERE fug.file_bin_id = :fileBinId 
<#if loggedInUserName != "admin@jquiver.com">
    AND jfu.updated_by = :loggedInUserName 
</#if>'
, 0,'aar.dev@trigyn.com', NOW(), NOW()); 



/***************************************************Help Manual File Queries - Start************************************************************/

REPLACE INTO jq_file_upload_config (file_bin_id, file_type_supported, max_file_size, no_of_files, select_query_content, upload_query_content, view_query_content, delete_query_content, is_deleted, created_by, created_date, last_updated_ts) VALUES
('helpManual', '.png, .jpg, .jpeg, .pdf', 33000000, 20, 

/* Help Manual - SELECT Query start */
'SELECT jfu.file_association_id AS fileAssociationId, jfu.file_bin_id AS fileBinId, jfu.file_path 
AS filePath, jfu.file_upload_id AS fileUploadId, jfu.original_file_name AS originalFileName, 
jfu.physical_file_name AS physicalFileName 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id 
WHERE fug.file_bin_id = :fileBinId AND jfu.file_association_id = :fileAssociationId 
<#if loggedInUserName != "admin@jquiver.com">
    AND jfu.updated_by = :loggedInUserName 
</#if> 
ORDER BY jfu.last_update_ts DESC, jfu.original_file_name ASC ' , 

/* Help Manual - UPLOAD Query start */
'SELECT COUNT(DISTINCT fug.file_bin_id) AS isAllowed 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_entity_role_association AS jera ON jera.entity_id = fug.file_bin_id  
AND jera.is_active = 1 
INNER JOIN jq_master_modules AS jmm ON jmm.module_id = jera.module_id AND  
jmm.module_name = :moduleName 
INNER JOIN jq_user_role_association AS jura ON jura.role_id = jera.role_id  
INNER JOIN jq_user AS jw ON jw.user_id = jura.user_id AND jw.email = :loggedInUserName  
INNER JOIN jq_manual_entry AS jme ON jme.manual_entry_id = :fileAssociationId 
WHERE fug.file_bin_id = :fileBinId '


/* Help Manual - VIEW Query start */
, 'SELECT COUNT(DISTINCT fug.file_bin_id) AS isAllowed 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_entity_role_association AS jera ON jera.entity_id = fug.file_bin_id 
AND jera.is_active = 1 
INNER JOIN jq_master_modules AS jmm ON jmm.module_id = jera.module_id AND 
jmm.module_name = :moduleName 
INNER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id 
WHERE fug.file_bin_id = :fileBinId '


/* Help Manual - DELETE Query start */
, 'SELECT COUNT(DISTINCT fug.file_bin_id) AS isAllowed 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_entity_role_association AS jera ON jera.entity_id = fug.file_bin_id 
AND jera.is_active = 1 
INNER JOIN jq_master_modules AS jmm ON jmm.module_id = jera.module_id AND 
jmm.module_name = :moduleName 
INNER JOIN jq_user_role_association AS jura ON jura.role_id = jera.role_id 
INNER JOIN jq_user AS jw ON jw.user_id = jura.user_id AND jw.email = :loggedInUserName 
INNER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id 
WHERE fug.file_bin_id = :fileBinId 
<#if loggedInUserName != "admin@jquiver.com">
    AND jfu.updated_by = :loggedInUserName 
</#if>'
, 0,'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments ) VALUES
('f07d3931-8d6e-11eb-9f5b-f48e38ab8cd7', 'system', 'system', 'file-bin-default-queries', 
'[{"selectValidator_query": "SELECT jfu.file_association_id AS fileAssociationId, jfu.file_bin_id AS fileBinId, jfu.file_path 
AS filePath, jfu.file_upload_id AS fileUploadId, jfu.original_file_name AS originalFileName, 
jfu.physical_file_name AS physicalFileName 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id 
WHERE fug.file_bin_id = :fileBinId AND jfu.file_association_id = :fileAssociationId 
<#list loggedInUserRoleList as loggedInUserRole>
    <#if loggedInUserRole != \\"ADMIN\\">
        AND jfu.updated_by = :loggedInUserName 
    </#if>
</#list>
ORDER BY jfu.last_update_ts DESC, jfu.original_file_name ASC "},

{"uploadValidator_query":"SELECT COUNT(DISTINCT fug.file_bin_id) AS isAllowed 
FROM jq_file_upload_config AS fug 
INNER JOIN jq_entity_role_association AS jera ON jera.entity_id = fug.file_bin_id 
AND jera.is_active = 1 
INNER JOIN jq_master_modules AS jmm ON jmm.module_id = jera.module_id AND 
jmm.module_name = :moduleName 
INNER JOIN jq_user_role_association AS jura ON jura.role_id = jera.role_id 
INNER JOIN jq_user AS jw ON jw.user_id = jura.user_id AND jw.email = :loggedInUserName 
LEFT OUTER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id 
WHERE fug.file_bin_id = :fileBinId" }

,{"viewValidator_query": "SELECT COUNT(*) AS isAllowed FROM jq_file_upload AS jfu WHERE jfu.file_upload_id = :fileUploadId 
<#list loggedInUserRoleList as loggedInUserRole>
    <#if loggedInUserRole != \\"ADMIN\\">
        AND jfu.updated_by = :loggedInUserName 
    </#if>
</#list>" }

,{"deleteValidator_query": "SELECT COUNT(*) AS isAllowed FROM jq_file_upload AS jfu WHERE jfu.file_upload_id = :fileUploadId 
<#list loggedInUserRoleList as loggedInUserRole>
    <#if loggedInUserRole != \\"ADMIN\\">
        AND jfu.updated_by = :loggedInUserName
    </#if>
</#list>"}]', 0, NOW(), 'admin', 1.41, '');


DELETE FROM jq_property_master WHERE property_master_id IN 
("92b31712-692d-11eb-9737-f48e38ab8cd7","74248905-db1b-4f47-bcf4-2d6894d90f33","e56d9df8-7cd7-11eb-971b-f48e38ab8cd7", "ec37bbbb-7cd7-11eb-971b-f48e38ab8cd7", "f0c81394-7cd7-11eb-971b-f48e38ab8cd7");


REPLACE INTO jq_dynamic_rest_details
(jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('a1938aa4-bb8f-4c10-901f-66c614bedd40', 'file-bin-default-queries', 1, 'getFileBinDefaultQueries', '', 1, 7, 'function getFileBinQueries(requestDetails, daoResults) {
    return daoResults["fileBinQueryList"];
}

getFileBinQueries(requestDetails, daoResults);', 3, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());


REPLACE INTO jq_dynamic_rest_dao_details
(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(120, 'a1938aa4-bb8f-4c10-901f-66c614bedd40', 'fileBinQueryList', 'SELECT jpm.property_value AS defaultQuery, jpm.comments AS selectorId 
FROM jq_property_master AS jpm 
WHERE jpm.property_name = "file-bin-default-queries"', 1, 1);


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('913990cb-b652-11eb-9b9c-f48e38ab8cd7', 'filebin-default-template', '<#if selectedTab == "htmlContent">
```HTML
<#noparse>
<!-- HTML Header -->
<head>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
    <link rel="stylesheet" type="text/css" href="${(contextPath)!''''}/webjars/1.0/dropzone/dist/dropzone.css" />
    <script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/dropzone/dist/dropzone.js"></script>
    <script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/fileupload/fileupload.js"></script>
</head>
<!-- HTML Body -->
	<div id="fileIdDiv"></div>    
       
    <input type="text" id="copyFilePathInput" name="copyFilePathInput" style="display:none">
    <div class="clearfix"></div>        

	<div class="col-12">
        <div id="fileUploadMaster" class="col-8 fileupload dropzone"></div>
    </div>
</#noparse>
```

<#elseif selectedTab == "jsContent">
```JavaScript
<#noparse>
	contextPath = "${(contextPath)!''''}";
    let dropzoneElement = $(".fileupload").fileUpload({
        fileBinId : "yourFileBinId",
        fileAssociationId: "yourFileBinId",
        createFileBin : true,
        renderer: fileConfigRenderer,
    });

	//dropZone without html component
	let dropZone = $.fn.fileUpload({
        fileBinId : "yourFileBinId",
        fileAssociationId: "yourFileBinId",
        createFileBin : false,
        renderer: fileListing
    });
    
    $(function () {
        dropZone.getSelectedFiles();
    });

    function fileConfigRenderer(fileObj) {
        let fileUploadId = fileObj["id"];
        let fileName = fileObj["name"];
        let btnTxt = resourceBundleData("jws.copyFilePath,jws.fileName");
        
        let actionElem = "<div><span  title=''"+btnTxt["jws.copyFilePath"]+"''><i class=''fileupload-actions fa fa-copy float-right''" +
            "onclick=''copyFilePath(''"+fileUploadId+"'')''></i></span><span  title=''"+btnTxt["jws.fileName"]+"''>" + 
        	"<i class=''fileupload-actions fa fa-info float-right'' onclick=''fileName(''"+fileName+"'')''></i></span></div>";	
        return actionElem;
	}
	
	function fileListing(fileObj){
        let input = $("<input id=''"+fileObj["id"]+"'' value=''"+fileObj["id"]+"'' type=''text''>");
    //    input.insertAfter($("#fileIdDiv"));
    }
    
	function fileName(fileName){
        showMessage("File Name: " + fileName, "success");
    }

    function copyFilePath(fileUploadId){
        let input = $("<input>");
        $("body").append(input);
        input.val(window.location.origin + "/cf/files/" + fileUploadId).select();
        document.execCommand("copy");
        input.remove();
        showMessage("File path copied successfully", "success");
    }

	function backToFileUploadManager() {
		location.href = contextPath+"/cf/fucl";
	}
</#noparse>
```
</#if>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);
 
 
REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('684da8d2-af16-49fa-949d-f1c3b8b96b00', 'a1938aa4-bb8f-4c10-901f-66c614bedd40', 'file-bin-default-queries', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('e98713f9-b2d7-4189-8021-806223390db8', 'a1938aa4-bb8f-4c10-901f-66c614bedd40', 'file-bin-default-queries', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('f223d514-8868-468e-9576-93db0cba4c6f', 'a1938aa4-bb8f-4c10-901f-66c614bedd40', 'file-bin-default-queries', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);



REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('81f5c0c3-ae07-4326-9950-1bad61849495', '7df80a38-7740-11eb-94ed-f48e38ab8cd7', 'common-files', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);

REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('84d42605-5a9f-4296-a04c-9d868f086eea', '74f78214-3add-4e18-84d6-fb3b76805556', 'common-files', 'c6cc466a-0ed3-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348',NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);


REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('44e41395-ddfc-4b52-bc2e-d91f0141de2e', 'c04bfb18-7a59-11eb-94ed-f48e38ab8cd7', 'fileconfig-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('60058ff6-790a-406f-9d1d-c9d59cc09e56', 'c04bfb18-7a59-11eb-94ed-f48e38ab8cd7', 'fileconfig-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('8135bc77-cb82-4dda-ac8c-1e7934493a13', 'c04bfb18-7a59-11eb-94ed-f48e38ab8cd7', 'fileconfig-details', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);


REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('729b07de-b652-11eb-9b9c-f48e38ab8cd7', '913990cb-b652-11eb-9b9c-f48e38ab8cd7', 'filebin-default-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('77990f99-b652-11eb-9b9c-f48e38ab8cd7', '913990cb-b652-11eb-9b9c-f48e38ab8cd7', 'filebin-default-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('7b8d0062-b652-11eb-9b9c-f48e38ab8cd7', '913990cb-b652-11eb-9b9c-f48e38ab8cd7', 'filebin-default-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1);
 
SET FOREIGN_KEY_CHECKS=1;