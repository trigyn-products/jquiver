SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('c0a5553a-0a37-11eb-a894-f48e38ab8cd7', 'file-upload-config-listing', '<head>
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
        <h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.fileUploadConfig'')}</h2> 
        <div class="float-right">
       
		  <form id="formFileUpload" action="/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="40289d3d750decc701750e3f1e3c0000"/>
                <input type="hidden" name="fileUploadConfigId" id="fileUploadConfigId" value=""/>
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
	        { title: "File Config Id", width: 130, dataIndx: "fileUploadConfigId", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "File Type Supported", width: 100, dataIndx: "fileTypeSupported", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
	        { title: "Max File Size", width: 160, dataIndx: "maxFileSize", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
			{ title: "No Of Files", width: 160, dataIndx: "noOfFiles", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editFileUploadConfig}
	    ];
	    let grid = $("#divFileConfigListingGrid").grid({
	      gridId: "fileUploadConfigGrid",
	      colModel: colM
	  	});
  	});
	function editFileUploadConfig(uiObject) {
		const fileUploadConfigId = uiObject.rowData.fileUploadConfigId;
		return ''<span id="''+fileUploadConfigId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title="Edit file configuration"></i></span>''.toString();
	}
	
	function submitForm(element) {
	  $("#fileUploadConfigId").val(element.id);
	  $("#formFileUpload").submit();
	}
	
	
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);


REPLACE INTO dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id) VALUES
('40289d3d750decc701750e3f1e3c0000', 'file-upload-config', 'File Upload Config Form', 'SELECT fuc.file_upload_config_id AS fileUploadConfigId, fuc.file_type_supported AS fileTypeSupported
, fuc.max_file_size AS maxFileSize, fuc.no_of_files AS noOfFiles, fuc.updated_by AS updatedBy,
fuc.updated_date AS updatedDate,fuc.is_deleted AS isDeleted
FROM file_upload_config AS fuc
WHERE fuc.file_upload_config_id = "${fileUploadConfigId}" AND is_deleted = 0;', '<head>
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
		<#if (resultSet)?? && (resultSet)?has_content>
		    <h2 class="title-cls-name float-left">Edit File Configuration</h2> 
        <#else>
            <h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.addFileConfiguration'')}</h2> 
        </#if>
		<div class="float-right">	
			<span onclick="backToPreviousPage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span> 
		</div>
		<div class="clearfix"></div>		
	</div>
  <form method="post" name="addEditForm" id="addEditForm">
    
        	<div class="row">
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="fileUploadConfigId" style="white-space:nowrap"><span class="asteriskmark">*</span>
		              File upload config id
		            </label>
				<input type="text" id="fileUploadConfigId" name="fileUploadConfigId"  value="" maxlength="500" class="form-control">
			</div>
		</div>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
		            <label for="fileTypeSupported" style="white-space:nowrap"><span class="asteriskmark">*</span>
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
                        <input type="number" id="maxFileSizeUi" name="maxFileSizeUi" value="" maxlength="10" class="form-control">
                        <input type="hidden" id="maxFileSize" name="maxFileSize" value="" maxlength="10" class="form-control">
                    </div>
                    <div class="col-3">
                        <span class="float-right">
                            <select id="size" class="form-control">
                                <option value="1">Bytes</option>
                                <option value="1000">KiloBytes</option>
                                <option value="1000000">MegaBytes</option>
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
                <input type="range" id="noOfFiles" name="noOfFiles" min="1" max="50" onchange="showSelectedValue(this.value)">
                <span id="noOfFilesValue" class="no-of-files-counter"></span>
			</div>
		</div>

    	</div>
    
  </form>
	<div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropdown custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''file-upload-config'', this);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls"  id="actionDiv">
                    	<ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="typeOfAction(''file-upload-config'', this);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="typeOfAction(''file-upload-config'', this);">${messageSource.getMessage("jws.saveAndEdit")}</li>
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
	let edit = 0;

    function showSelectedValue(value){
        $("#noOfFilesValue").html(value);
        $("#noOfFilesValue").show();
    }

  $(function(){
      <#if (resultSet)??>
      	<#list resultSet as resultSetList>
      		$("#fileUploadConfigId").val(''${resultSetList?api.get("fileUploadConfigId")}'');
      		$("#fileTypeSupported").val(''${resultSetList?api.get("fileTypeSupported")}'');
            $("#maxFileSize").val(''${resultSetList?api.get("maxFileSize")}'');
            $("#maxFileSizeUi").val(''${resultSetList?api.get("maxFileSize")}'');
           	$("#noOfFiles").val(''${resultSetList?api.get("noOfFiles")}'');
            $("#noOfFilesValue").html(''${resultSetList?api.get("noOfFiles")}'');
      	</#list>
      </#if>
    
		<#if (requestDetails?api.get("fileUploadConfigId")) != "">
            edit = 1;
        </#if>
    
		savedAction("file-upload-config", edit);
		hideShowActionButtons();
        if($("#noOfFilesValue").html() == "") {
            $("#noOfFilesValue").hide();
        }
  });
  
	function saveData (){
        let isDataSaved = false;
        let isMultipleFiles = $("#allowMultipleFiles").prop("checked");
        $("#isMultipleFiles").val(0);
        if(isMultipleFiles){
            $("#isMultipleFiles").val(1);
        }
        let maxSize = $("#maxFileSizeUi").val() * Number.parseInt($("#size").val());
        $("#maxFileSize").val(maxSize);
		let formData = $("#addEditForm").serialize()+ "&formId="+formId;
		if(edit === 1) {
		    formData = formData + "&edit="+edit;
		}
		$.ajax({
		  type : "POST",
		  url : contextPath+"/cf/sdf",
		  async: false,
		  data : formData,
          success : function(data) {
          	isDataSaved = true;
			showMessage("Information saved successfully", "success");
		  },
	      error : function(xhr, error){
			showMessage("Error occurred while saving", "error");
	      },
		});
		return isDataSaved;
	}
	
	function backToPreviousPage() {
		location.href = contextPath+"/cf/fucl";
	}
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2);

REPLACE INTO dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('40289d3d750decc701750e3f1e5c0001', '40289d3d750decc701750e3f1e3c0000', '<#if (formData?api.getFirst("edit"))?has_content>
    UPDATE file_upload_config SET 
    file_type_supported = ''${formData?api.getFirst("fileTypeSupported")}''
    ,max_file_size = ''${formData?api.getFirst("maxFileSize")}''
    ,no_of_files = ''${formData?api.getFirst("noOfFiles")}''
    ,is_deleted = 0
    ,updated_by = ''admin''
    ,updated_date = NOW() 
    WHERE file_upload_config_id = ''${formData?api.getFirst("fileUploadConfigId")}''
<#else>
    INSERT INTO file_upload_config (file_upload_config_id,file_type_supported,max_file_size,no_of_files,is_deleted,updated_by,updated_date) VALUES (''${formData?api.getFirst("fileUploadConfigId")}'',''${formData?api.getFirst("fileTypeSupported")}'',''${formData?api.getFirst("maxFileSize")}'',''${formData?api.getFirst("noOfFiles")}'',0,''admin'',NOW())
</#if>', 1, NULL);

replace into jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_dynamic_rest_type_id) VALUES
(1002, 'fileconfig-details', 1, 'getFileConfigDetails', 'Get file config details', 2, 7, 'function getFileConfigDetails(requestDetails, daoResults) {
    return daoResults["fileConfigs"][0];
}

getFileConfigDetails(requestDetails, daoResults);', 3, 2);

replace into jws_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(19, 1002, 'fileConfigs', 'select fuc.* from file_upload_config as fuc where fuc.file_upload_config_id IN (:fileUploadId, "helpManual")
order by FIELD(file_upload_config_id, :fileUploadId, "helpManual")
LIMIT 1', 1, 1);

REPLACE INTO file_upload_config (file_upload_config_id, file_type_supported, max_file_size, no_of_files, is_deleted, updated_by, updated_date) VALUES
('default', '*', 2000000000000, 1, 0, 'admin', NOW());

REPLACE INTO file_upload_config (file_upload_config_id, file_type_supported, max_file_size, no_of_files, is_deleted, updated_by, updated_date) VALUES
('helpManual', '.png, .jpg, .jpeg, .pdf', 5000000000000, 20, 0, 'admin', NOW());

SET FOREIGN_KEY_CHECKS=1;