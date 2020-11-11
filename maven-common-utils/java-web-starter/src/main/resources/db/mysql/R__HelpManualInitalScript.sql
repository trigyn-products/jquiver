SET FOREIGN_KEY_CHECKS=0;

replace into resource_bundle VALUES ("jws.helpManuals", 1, "Help Manuals");

replace into jws_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files) VALUES
(1003, 'get-manual-details', 1, 'manualDetails', 'Get manual details', 2, 7, 'function myFunction(requestDetails, daoResults) {
    return daoResults["manualEnitityDetails"];
}

myFunction(requestDetails, daoResults);', 3, 0), 
(1004, 'manual-type', 1, 'manualTypes', 'Get manual Types', 2, 7, 'function myFunction(requestDetails, daoResults) {
    return daoResults["manualTypes"];
}

myFunction(requestDetails, daoResults);', 3, 0), 
(1005, 'validate-manual-details', 1, 'validateManualDetails', 'Validate Manual Details', 2, 7, 'function myFunction(requestDetails, daoResults) {
    if(daoResults["manualDetails"].length > 0) {
        return "Manual entry name already exists";
    } else if(daoResults["sequence"].length > 0) {
        return "Manual entry name already at entered sequence";
    } else {
        return null;
    }
}

myFunction(requestDetails, daoResults);', 3, 0);

replace into jws_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(21, 1003, 'manualEnitityDetails', 'select manual_type, entry_name, entry_content, sort_index, manual_entry_id from manual_entry where manual_type = :manualId order by sort_index asc', 1, 1), 
(23, 1004, 'manualTypes', 'select * from manual_type;', 1, 1), 
(26, 1005, 'manualDetails', 'select * from manual_entry where manual_type = :manualType and entry_name = :entryName', 1, 1), 
(27, 1005, 'sequence', 'select * from manual_entry where manual_type = :manualType and sort_index = :sortIndex', 2, 1);

replace into dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id) VALUES
('8a80cb81754acbf701754ae3d1c2000c', 'manual-entry-form', 'manual-entry Form', 'SELECT me.manual_entry_id, manual_type, entry_name, entry_content, sort_index, GROUP_CONCAT(mefa.file_upload_id) as fileIds FROM manual_entry as me 
left outer join manual_entry_file_association as mefa
on me.manual_entry_id = mefa.manual_entry_id
WHERE me.manual_entry_id = "${manualentryid}"
group by me.manual_entry_id', '<head>
    <link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css" />
    <link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
    <script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
    <script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
    <script src="/webjars/1.0/manuals/helpmanual.js"></script>
    <script type="text/javascript" src="/webjars/1.0/dropzone/dist/dropzone.js"></script>
    <link rel="stylesheet" type="text/css" href="/webjars/1.0/dropzone/dist/dropzone.css" />
    <script type="text/javascript" src="/webjars/1.0/fileupload/fileupload.js"></script>
    <link rel="stylesheet" href="/webjars/1.0/markdown/dist/simplemde.min.css">
    <script src="/webjars/1.0/markdown/dist/simplemde.min.js"></script>
    <link rel="stylesheet" href="/webjars/1.0/markdown/highlight/github.min.css"/>
    <script src="/webjars/1.0/markdown/highlight/highlight.min.js"></script>
</head>

<div class="container">
    <div class="topband">
        <h2 id="title" class="title-cls-name float-left"></h2>
        <div class="clearfix"></div>
    </div>
    <form method="post" name="addEditForm" id="addEditForm">
        <div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>

        <div class="row">
            <input type="hidden" id="manualentryid" name="manualentryid"  value="" maxlength="50" class="form-control">
            <input type="hidden" id="manualtype" name="manualtype"  value="${requestDetails?api.get("manualType")}" maxlength="50" class="form-control">
            <div class="col-3">
                <div class="col-inner-form full-form-fields">
                    <label for="entryname" style="white-space:nowrap"><span class="asteriskmark">*</span>
                      Entry name
                    </label>
                    <input type="text" id="entryname" name="entryname"  value="" maxlength="50" class="form-control">
            </div>
                </div>
                <div class="col-3">
                    <div class="col-inner-form full-form-fields">
                        <label for="sortindex" style="white-space:nowrap"><span class="asteriskmark">*</span>
                        ${messageSource.getMessage("jws.displayIndex")}
                    </label>
                        <input type="number" id="sortindex" name="sortindex"  value="" maxlength="10" class="form-control">
            </div>
                    </div>
                    <div class="col-12" style="height: 100% !important;">
                        <div class="col-inner-form full-form-fields">
                            <span class="asteriskmark">*</span>
                            <label for="entrycontent">Entry content</label>
                            <#if (resultSet)?has_content>
                                <#list resultSet as resultSetList>
                                    <textarea class="form-control" rows="15" cols="90" title="Entry content" id="entrycontent" placeholder="Entry content" name="entrycontent" style="height:80px">${(resultSet?api.get(0)?api.get("entry_content")!"")}</textarea>
                                </#list>
                            <#else>
                                <textarea class="form-control" rows="15" cols="90" title="Entry content" id="entrycontent" placeholder="Entry content" name="entrycontent" style="height:80px"></textarea>
                            </#if>
                            
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="col-6 fileupload dropzone"></div>
                    </div>
                </div>

    </form>
    <div class="row">
        <div class="col-12">
            <div class="float-right">
                <div class="btn-group dropdown custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''${formId}'', this);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls" id="actionDiv">
                        <ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="typeOfAction(''${formId}'', this);">
                                ${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="typeOfAction(''${formId}'', this);">
                                ${messageSource.getMessage("jws.saveAndEdit")}</li>
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
    let manualType = "${requestDetails?api.get("manualType")}";
    let manualObject = new HelpManual();
    let manualdata;

    let dropzoneElement = $(".fileupload").fileUpload({
        fileUploadId : "dynamic-form",
        successcallback: showFileDataInEntryContent.bind(this),
        deletecallback: deleteFileDataInEntryContent.bind(this)
    });

    let simplemde = new SimpleMDE({
        renderingConfig: {
            codeSyntaxHighlighting: true,
        },
        shortcuts: {
            drawTable: "Cmd-Alt-T"
        },
        showIcons: ["code", "table"],
    });
    function showFileDataInEntryContent(fileId) {
        let content = simplemde.value();
        content = content + " ![](http://localhost:8080/cf/files/"+fileId+")";
        simplemde.value(content);
    }

    function deleteFileDataInEntryContent(fileId) {
        let content = $("#entrycontent").val();
        content = content.replaceAll("[file]: /cf/files"+fileId, "");
        $("#entrycontent").val(content);
    }

  $(function(){
    manualdata = manualObject.getManualDetails();
    $("#title").html(manualdata.find(manual => {return manual["manual_id"] == manualType})["name"]);
    let files = "";
    <#if (resultSet)??>
        <#list resultSet as resultSetList>
            $("#manualentryid").val(''${resultSetList?api.get("manual_entry_id")}'');
            $("#sortindex").val(''${resultSetList?api.get("sort_index")}'');
            $("#entryname").val(''${resultSetList?api.get("entry_name")}'');
            files = "${(resultSetList?api.get("fileIds"))!''''}";
        </#list>
    </#if>
    
    let isEdit = 0;
    <#if (resultSet)?? && resultSet?has_content>
        isEdit = 1;
    <#else>
        $("#sortindex").val(''${requestDetails?api.get("sequence")}'');
    </#if>
    
     files = files.split(",");
     dropzoneElement.showSelectedFiles(files);
    savedAction(formId, isEdit);
    hideShowActionButtons();
  });
  
    //Add logic to save form data
    function saveData(){
        let isDataSaved = false;
        let isDataValid = validateData();   
        if(isDataValid === false){
            $("#errorMessage").show();
            return false;
        }
        $("#errorMessage").hide();
        let formData = $("#addEditForm").serializeArray();
        let manualData = new Object();
        for(let counter=0; counter < formData.length; ++counter) {
            let value = formData[counter]["value"];
            let key = formData[counter]["name"];
            manualData[key] = value;
        }
        manualData["entrycontent"] = simplemde.value();
        $.ajax({
          type : "POST",
          async: false,
          url : contextPath+"/cf/shmd",
          data : manualData,
          success : function(data) {
            isDataSaved = true;
            manualObject.saveManualDetails(dropzoneElement.getSelectedFiles());
            showMessage("Information saved successfully", "success");
          },
          error : function(xhr, error){
            showMessage("Error occurred while saving", "error");
          },
        });
        return isDataSaved;
    }
    
    //Basic validation for form fields
    function validateData(){
        let formData = $("#addEditForm").serializeArray();
        let mandatoryFields = ["sortindex","entryname"];
        let isDataBlank = false;
        for(let counter=0; counter < mandatoryFields.length; ++counter) {
             let value = formData.find(element => { return element["name"] == mandatoryFields[counter]})["value"];
             if(value == "") {
                 $("#errorMessage").html("All field are mandatory.");
                 isDataBlank = true;
                break;
             }
        }
        let validData = true;
        return validData;
    }
    
    //Code go back to previous page
    function backToPreviousPage() {
        location.href = contextPath+"/cf/ehme?mt="+manualType;
    }
</script>', 'admin', NOW(), NULL, NULL, 1);

replace into dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence, checksum) VALUES
('8a80cb817554cb5f017555b246110006', '8a80cb81754acbf701754ae3d1c2000c', 'select 1;', 1, NULL);

replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb81754acbf701754ae3d1dd000e', 'manual-entry-template', '<head>
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
<script src="/webjars/1.0/manuals/helpmanual.js"></script>
</head>

<div class="container">
    <div class="topband">
        <h2 id="title" class="title-cls-name float-left"></h2> 
        <div class="float-right">
            <form id="viewManualForm" action="/cf/manual" method="get" target="_blank" class="margin-r-5 pull-left">
  				<input id="manualTypeView" name="mt" type="hidden" value="${mt}">
  				<button type="submit" class="btn btn-primary"> View Manual </button>
			</form>
            <form id="addEditRecords" action="/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="8a80cb81754acbf701754ae3d1c2000c"/>
                <input type="hidden" name="manualentryid" id="manualentryid" value=""/>
                <input type="hidden" name="manualType" id="manualType" value="${mt}"/>
                <input type="hidden" name="sequence" id="sequence" value=""/>
                <button type="submit" class="btn btn-primary"> Create Entry </button>
            </form>

            <span onclick="backToWelcomePage();">
                <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
            </span> 
        </div>
        
        <div class="clearfix"></div>        
    </div>
        
    <div id="manual-entryGrid"></div>

    <div id="snackbar"></div>
</div>

<script>
    contextPath = "";
    let manualType = "${mt}";
    let manualObject = new HelpManual();
    let manualdata;
    $(function () {
        manualdata = manualObject.getManualDetails();
        $("#title").html(manualdata.find(manual => {return manual["manual_id"] == manualType})["name"]);
        //Add all columns that needs to be displayed in the grid
        let colM = [
            { title: "Entry name", width: 130, dataIndx: "entry_name", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            { title: "Sort index", width: 130, dataIndx: "sort_index", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            { title: "Last updated by", width: 130, dataIndx: "last_updated_by", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            { title: "Last Updated Timestamp", width: 130, dataIndx: "last_modified_on", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]} , render: lastUpdatedDate },
            { title: "Action", width: 50, dataIndx: "action", align: "center", halign: "center", render: manageRecord}
        ];
    
    //System will fecth grid data based on gridId
        let grid = $("#manual-entryGrid").grid({
          gridId: "manual-entryGrid",
          colModel: colM,
          additionalParameters: {"manual_type" : manualType},
          loadCallback: function(event, ui) {
              $("#sequence").val(ui.dataModel.data.length + 1);
          }
        });
    });
    
    //Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
    function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
        return ''<span id="''+rowIndx+''" onclick="createNew(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span><span id="''+rowIndx+''" onclick="deleteEntry(this)" class= "grid_action_icons"><i class="fa fa-trash"></i></span>''.toString();
    }
    
    function lastUpdatedDate(uiObject) {
        let lastUpdateDate = uiObject.rowData.last_modified_on;

        
    }
    
    //Add logic to navigate to create new record
    function createNew(element) {
        let rowData = $( "#manual-entryGrid" ).pqGrid("getRowData", {rowIndxPage: element.id});
        $("#manualentryid").val(rowData["manual_entry_id"]);
        $("#addEditRecords").submit();
    }

    //Code go back to previous page
    function backToWelcomePage() {
        location.href = contextPath+"/cf/help";
    }

    function deleteEntry(element) {
        let rowData = $( "#manual-entryGrid" ).pqGrid("getRowData", {rowIndxPage: element.id});
        console.log(rowData["manual_entry_id"]);
    }
</script>', 'admin', 'admin', NOW(), NULL, 1);

replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8175479e47017547d34a8d001c', 'manual-type-template', '<head>
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
<script src="/webjars/1.0/jquery-modal/jquery.modal.min.js"></script> 
<link rel="stylesheet" href="/webjars/1.0/jquery-modal/jquery.modal.min.css" />
</head>

<div class="container">
    <div class="topband">
        <h2 class="title-cls-name float-left">Manual type Listing</h2> 
        <div class="float-right">
            <button type="submit" class="btn btn-primary" onclick="$(''#manual-add-edit'').modal();"> Create New Manual </button>

            <span onclick="backToWelcomePage();">
                <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
            </span> 
        </div>
        
        <div class="clearfix"></div>        
    </div>
        
    <div id="manual-typeGrid"></div>

    <div id="snackbar"></div>
</div>

<form id="manual-add-edit" class="modal addeditmodal">
    <div class="topband">
        <h2 class="title-cls-name float-left">Manual Details</h2> 
        <div class="clearfix"></div>        
    </div>
    <div class="row">
        <div class="col-12">
            <input type="hidden" id="fmanualid" name="fmanualid"  value="" maxlength="10" class="form-control">
            <div class="col-inner-form full-form-fields">
                <label for="name" style="white-space:nowrap"><span class="asteriskmark">*</span>
                    Name
                </label>
                <input type="text" id="name" name="name"  value="" maxlength="50" class="form-control">
            </div>
            <input type="hidden" id="issystemmanual" name="issystemmanual" value="0">
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <div class="float-right">
                <input id="saveBtn" class="btn btn-primary" name="saveBtn" onclick="saveData()" value="Save" type="button">
                <input id="backBtn" class="btn btn-secondary" name="backBtn" onclick="cancelForm()" value="Cancel" type="button">
            </div>
        </div>
    </div>
</form>

<form id="viewManualForm" action="/cf/manual" method="get" target="_blank">
  <input id="manualType" name="mt" type="hidden">
</form>

<script>

    contextPath = "";
    $(function () {
    //Add all columns that needs to be displayed in the grid
        let colM = [
            { title: "Name", dataIndx: "name", width: 800, align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            { title: "Action", dataIndx: "action", align: "center", halign: "center", render: manageRecord}
        ];
    
    //System will fecth grid data based on gridId
        let grid = $("#manual-typeGrid").grid({
          gridId: "manual-typeGrid",
          colModel: colM
        });
    
    });
    
    //Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
    function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
        return ''<span id="''+rowIndx+''" onclick="createNew(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span><span id="''+rowIndx+''" onclick="editEntityManual(this)" class= "grid_action_icons"><i class="fa fa-edit"></i></span><span id="''+rowIndx+''" onclick="viewManual(this)" class= "grid_action_icons"><i class="fa fa-eye"></i></span>''.toString();
    }
    
    //Add logic to navigate to create new record
    function createNew(element) {
        const id = Number.parseInt(element.id);
        let rowData = $( "#manual-typeGrid" ).pqGrid("getRowData", {rowIndxPage: id});
        $("#fmanualid").val(rowData["manual_id"]);
        $("#name").val(rowData["name"]);
        $("#issystemmanual").val(rowData["is_system_manual"]);
        $("#manual-add-edit").modal();
    }

    function editEntityManual(element) {
        const id = Number.parseInt(element.id);
        let rowData = $( "#manual-typeGrid" ).pqGrid("getRowData", {rowIndxPage: id});
        location.href = contextPath + "/cf/ehme?mt="+rowData["manual_id"];
    }

    function viewManual(element) {
        const id = Number.parseInt(element.id);
        let rowData = $( "#manual-typeGrid" ).pqGrid("getRowData", {rowIndxPage: id});
        $("#manualType").val(rowData["manual_id"]);
        $("#viewManualForm").submit();
    }

    function saveData() {
        // save function
        $.ajax({
            type: ''POST'',
            url: contextPath+''/cf/shmt'',
            data: {
                manualId: $("#fmanualid").val(),
                name: $("#name").val()
            },
            success: function(data) {
                showMessage("Information saved successfully", "success");
                $.modal.close();
                $("#manual-typeGrid").pqGrid("refreshDataAndView")
            },
            error: function(data) {
                showMessage("Error while saving data", "error");
                $.modal.close();
            }
        })
    }

    function cancelForm() {
        $.modal.close();
    }

    //Code go back to previous page
    function backToWelcomePage() {
        location.href = contextPath+"/cf/home";
    }
</script>', 'admin', 'admin', NOW(), NULL, 1);

replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8175513bc80175514206ef0000', 'manual-display', '<script src="/webjars/1.0/manuals/helpmanual.js"></script>
<link rel="stylesheet" href="/webjars/1.0/markdown/highlight/github.min.css" />
<script src="/webjars/1.0/markdown/highlight/highlight.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>


	


<div class="container ">
	<div class="pg-manual-display">
		<div class="row">
			<div class="topband">
				<h2 id="title" class="title-cls-name float-left"></h2>
				<div class="float-right"> <span onclick="back();">
                        <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
                    </span> </div>
				<div class="clearfix"></div>
			</div>
		</div>
		    <div class="cm-rightbar">
                <div class="row cm-bottom-border">
                    <span> <i class="icon icon-s-information"></i></span>
			    <div class="col-md-3">
				    <div class="cm-searchwithicon">
                        <div class="form-group has-search clearfix"> <span class="fa fa-search form-control-feedback"></span>
                            <input type="text" class="form-control" placeholder="Search..." onkeyup="search(event, this.value)">
                           <div class="cm-errormsg" id="cm-errormsg"></div> 

                           
                             
                        </div>
                         
                        
                       
					</div>
				</div>
                </div>

                <div class="preview row cm-scrollbar">
                    
					<div id="tabs" class="col-md-3 tabs"></div>
					<div id="previews" class="col-md-9 previews cm-scrollbar"></div>
				</div>

                <div class="row">
                <div class="col-md-3">
                    <div id="previewDiv" style="display:none;">
                        <textarea id="previewContent" style="display:none;"></textarea>
                    </div>
                </div>
            </div>
			</div>
            
				
			
            

		</div>
	</div>

	
			
	<script>
		contextPath = "${contextPath}";
let manual = new HelpManual();
let manualTypes = manual.getManualDetails();
$("#title").html(manualTypes.find(manual => {
	return manual["manual_id"] == "${mt}"
})["name"]);
manual.getManualEntities("${mt}");
for(let counter = 0; counter < manual.helpManualDetails.length; counter++) {
	let data = manual.helpManualDetails[counter];
	$("#tabs").append("<button id=''" + data["manual_entry_id"] + "'' class=''tablinks'' onclick=''manual.loadManualPreview(event, this)''>" + "<i class=''fa fa-table''></i>" + data["entry_name"]  + "</button>");
	let simplemde = new SimpleMDE({
		initialValue: manual.helpManualDetails[counter]["entry_content"],
		renderingConfig: {
			codeSyntaxHighlighting: true,
		}
	});
	manual.helpManualDetails[counter]["divContent"] = $(simplemde.options.previewRender(simplemde.value())).text();
}
$("#tabs button")[0].click();

function back() {
	location.href = contextPath + "/cf/help"
}

function search(event, value) {
	let searchText = value.toLowerCase();
		let manuals = manual.helpManualDetails.filter(details => {
			let divContent = details["divContent"].toLowerCase();
			return divContent.indexOf(searchText) != -1
		});
		$("#tabs").html("");
		$("#previews").html("");
    	$("#cm-errormsg").hide();
		if(manuals.length > 0){
			for(let counter = 0; counter < manuals.length; counter++) {
				let data = manuals[counter];
				$("#tabs").append("<button id=''" + data["manual_entry_id"] + "'' class=''tablinks'' onclick=''manual.loadManualPreview(event, this)''>" + data["entry_name"] + "</button>");
			}
			$("#tabs button")[0].click();
		}else{
			$("#cm-errormsg").show();
			$("#cm-errormsg").text("Sorry no data found");
		}
}
	</script>', 'admin', 'admin', NOW(), NULL, 1);

replace into grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type) VALUES
('manual-entryGrid', 'manual-entryGrid', 'manual-entry Listing', 'manual_entry', 'manual_entry_id,manual_type,entry_name,entry_content,sort_index,last_modified_on,last_updated_by', 1), 
('manual-typeGrid', 'manual-typeGrid', 'manual-type Listing', 'manual_type', 'manual_id,name,is_system_manual', 1);

SET FOREIGN_KEY_CHECKS=1;