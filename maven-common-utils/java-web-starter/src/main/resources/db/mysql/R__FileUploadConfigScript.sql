SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
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
                <button type="submit" class="btn btn-primary"> Add Property </button>
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
	
	    var colM = [
	        { title: "File Config Id", width: 130, dataIndx: "fileUploadConfigId", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "File Type Supported", width: 100, dataIndx: "fileTypeSupported", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
	        { title: "Max File Size", width: 160, dataIndx: "maxFileSize", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
			{ title: "Allowed Multiple Files", width: 160, dataIndx: "allowMultipleFiles", align: "left", halign: "center", 
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "${messageSource.getMessage(''jws.action'')}", width: 50, dataIndx: "action", align: "center", halign: "center", render: editFileUploadConfig}
	    ];
	    var grid = $("#divFileConfigListingGrid").grid({
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
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW());


REPLACE INTO dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum) VALUES
('40289d3d750decc701750e3f1e3c0000', 'file-upload-config', 'File Upload Config Form', 'SELECT fuc.file_upload_config_id AS fileUploadConfigId, fuc.file_type_supported AS fileTypeSupported
, fuc.max_file_size AS maxFileSize, fuc.allow_multiple_files AS allowMultipleFiles, fuc.updated_by AS updatedBy,
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
		    <h2 class="title-cls-name float-left">Edit File Config Details</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Add File Config Detailsr</h2> 
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
				<input type="text" id="fileTypeSupported" name="fileTypeSupported"  value="" class="form-control">
			</div>
		</div>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<span class="asteriskmark">*</span>
				<label for="maxFileSize">Max file size(In Bytes)</label>
                <input type="number" id="maxFileSize" name="maxFileSize"  value="" maxlength="10" class="form-control">
			</div>
		</div>
    		<div class="col-3">
			<div class="col-inner-form full-form-fields">
				<label for="allowMultipleFiles" style="white-space:nowrap"><span class="asteriskmark">*</span>
		            Allow multiple files
		        </label>
                <div class="onoffswitch">
                    <input type="checkbox" name="allowMultipleFiles" class="onoffswitch-checkbox" id="allowMultipleFiles">
                    <label class="onoffswitch-label" for="allowMultipleFiles">
                        <span class="onoffswitch-inner"></span>
                        <span class="onoffswitch-switch"></span>
                    </label>
                </div>
                <input type="hidden" id="isMultipleFiles" name="isMultipleFiles"  value="" >
			</div>
		</div>

    	</div>
    
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
	let formId = "${formId}";
	contextPath = "${contextPath}";
	let edit = 0;
  $(function(){
      <#if (resultSet)??>
      	<#list resultSet as resultSetList>
      		$("#fileUploadConfigId").val(''${resultSetList?api.get("fileUploadConfigId")}'');
      		$("#fileTypeSupported").val(''${resultSetList?api.get("fileTypeSupported")}'');
            $("#maxFileSize").val(''${resultSetList?api.get("maxFileSize")}'');
            let isMultipleFileAllowed = ''${resultSetList?api.get("allowMultipleFiles")}'';
            if(isMultipleFileAllowed){
                $("#allowMultipleFiles").attr(''checked'', true);
            }else{
                $("#allowMultipleFiles").attr(''checked'', false);
            }
            
      	</#list>
      </#if>
    
		<#if (requestDetails?api.get("fileUploadConfigId")) != "">
            edit = 1;
        </#if>
  });
  
	function saveData (){
        let isMultipleFiles = $("#allowMultipleFiles").prop("checked");
        $("#isMultipleFiles").val(0);
        if(isMultipleFiles){
            $("#isMultipleFiles").val(1);
        }
		let formData = $("#addEditForm").serialize()+ "&formId="+formId;
		if(edit === 1) {
		    formData = formData + "&edit="+edit;
		}
		$.ajax({
		  type : "POST",
		  url : contextPath+"/cf/sdf",
		  data : formData,
          success : function(data) {
			showMessage("Information saved successfully", "success");
		  },
	      error : function(xhr, error){
			showMessage("Error occurred while saving", "error");
	      },
		});
	}
	
	function backToPreviousPage() {
		location.href = contextPath+"/cf/fucl";
	}
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL);

REPLACE INTO dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('40289d3d750decc701750e3f1e5c0001', '40289d3d750decc701750e3f1e3c0000', '<#if (formData?api.getFirst("edit"))?has_content>
    UPDATE file_upload_config SET 
    file_type_supported = ''${formData?api.getFirst("fileTypeSupported")}''
    ,max_file_size = ''${formData?api.getFirst("maxFileSize")}''
    ,allow_multiple_files = ''${formData?api.getFirst("isMultipleFiles")}''
    ,is_deleted = 0
    ,updated_by = ''admin''
    ,updated_date = NOW() 
    WHERE file_upload_config_id = ''${formData?api.getFirst("fileUploadConfigId")}''
<#else>
    INSERT INTO file_upload_config (file_upload_config_id,file_type_supported,max_file_size,allow_multiple_files,is_deleted,updated_by,updated_date) VALUES (''${formData?api.getFirst("fileUploadConfigId")}'',''${formData?api.getFirst("fileTypeSupported")}'',''${formData?api.getFirst("maxFileSize")}'',''${formData?api.getFirst("isMultipleFiles")}'',0,''admin'',NOW())
</#if>', 1, NULL);


SET FOREIGN_KEY_CHECKS=1;