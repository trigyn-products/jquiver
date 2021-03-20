SET FOREIGN_KEY_CHECKS=0;

replace into jq_dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id) VALUES
('8a80cb81754acbf701754ae3d1c2000c', 'manual-entry-form', 'manual-entry Form', 'SELECT me.manual_entry_id, manual_type, entry_name, entry_content, sort_index, GROUP_CONCAT(mefa.file_upload_id) as fileIds FROM jq_manual_entry as me 
left outer join jq_manual_entry_file_association as mefa
on me.manual_entry_id = mefa.manual_entry_id
WHERE me.manual_entry_id = "${manualentryid}"
group by me.manual_entry_id', '<head>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css" />
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
    <script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
    <script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
    <script src="${(contextPath)!''''}/webjars/1.0/manuals/helpmanual.js"></script>
    <script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/dropzone/dist/dropzone.js"></script>
    <link rel="stylesheet" type="text/css" href="${(contextPath)!''''}/webjars/1.0/dropzone/dist/dropzone.css" />
    <script type="text/javascript" src="${(contextPath)!''''}/webjars/1.0/fileupload/fileupload.js"></script>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/markdown/dist/simplemde.min.css">
    <script src="${(contextPath)!''''}/webjars/1.0/markdown/dist/simplemde.min.js"></script>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/markdown/highlight/github.min.css"/>
    <script src="${(contextPath)!''''}/webjars/1.0/markdown/highlight/highlight.min.js"></script>
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
            <input type="hidden" id="manualtype" name="mt"  value="${requestDetails?api.get("manualType")}" maxlength="50" class="form-control">
            <input type="hidden" id="manualname" name="manualname"  value="${requestDetails?api.get("manualName")}" maxlength="50" class="form-control">
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
                        <div class="col-5 fileupload dropzone"></div>
                    </div>
                </div>

    </form>
    <div class="row">
        <div class="col-12">
            <div class="float-right">
                <div class="btn-group dropup custom-grp-btn">
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
    
    <form id="viewManualEntry" action="${(contextPath)!''''}/cf/ehme" method="post">
    	<input type="hidden" name="mt" id="manualTypeListing" value="${requestDetails?api.get("manualType")}"/>
		<input type="hidden" name="mn" id="manualNameListing" value="${requestDetails?api.get("manualName")}">
    </form>
            
</div>
<script>
    let formId = "${formId}";
    contextPath = "${contextPath}";
    let contextPathUrl = "${requestDetails?api.get("contextPathUrl")}";
    let manualType = "${requestDetails?api.get("manualType")}";
    let manualObject = new HelpManual();
    let manualdata;
    $("#entrycontent").val($("#entrycontent").val().replaceAll("/cf/files/", contextPath + "/cf/files/"));

    let dropzoneElement = $(".fileupload").fileUpload({
        fileBinId : "helpManual",
        fileAssociationId: "${(requestDetails?api.get("manualentryid"))!''''}",
        createFileBin : true,
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
    
    simplemde.codemirror.on("paste", function uploadImg(codeMirror, event){
       handleImgPaste(event)
    });
    
    function showFileDataInEntryContent(fileId) {
        let content = simplemde.value();
        content = content + " ![](/cf/files/"+fileId+")";
        simplemde.value(content);
    }

    function deleteFileDataInEntryContent(fileId) {
        let content = $("#entrycontent").val();
        content = content.replaceAll(" ![](/cf/files"+fileId+")");
        simplemde.value(content);
    }

  $(function(){
    manualdata = manualObject.getManualDetails();
    $("#title").html(manualdata.find(manual => {return manual["manual_id"] == manualType})["name"]);
    <#if (resultSet)??>
        <#list resultSet as resultSetList>
            $("#manualentryid").val(''${resultSetList?api.get("manual_entry_id")}'');
            $("#sortindex").val(''${resultSetList?api.get("sort_index")}'');
            $("#entryname").val(''${resultSetList?api.get("entry_name")}'');
        </#list>
    </#if>
    
    let isEdit = 0;
    <#if (resultSet)?? && resultSet?has_content>
     	dropzoneElement.loadSelectedFiles();
        isEdit = 1;
    <#else>
    	dropzoneElement.disableDropZone("Please save help manual first");
        $("#sortindex").val(''${requestDetails?api.get("sequence")}'');
    </#if>
    
    
    savedAction(formId, isEdit);
    hideShowActionButtons();
  });
  
   function handleImgPaste(event) {
        for (let iCounter = 0 ; iCounter < event.clipboardData.items.length ; iCounter++) {
            let item = event.clipboardData.items[iCounter];
            if (item.type.indexOf("image") != -1) {
                uploadFile(item.getAsFile());
            } 
        }
    }

    function uploadFile(file){
        let allowedExtension = dropzoneElement.dropzone.options.acceptedFiles;
        let fileExtension = file.name.split(".").pop();
        if(allowedExtension.indexOf(fileExtension) != -1){
           let dropZoneObj = dropzoneElement.dropzone;
           dropZoneObj.addFile(file);
        }
    }
    
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
            let value = $.trim(formData[counter]["value"]);
            let key = formData[counter]["name"];
            let isVisible = $("[name =''"+key+"'']").is(":visible"); 
            if(isVisible == true && value === ""){ 
            	$("#errorMessage").html("All fields are mandatory");
            	$("#errorMessage").show();
            	return false;
            }
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
            dropzoneElement.enableDropZone(data);
            $("#manualentryid").val(data);
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
        $("#viewManualEntry").submit();
    }
</script>', 'aar.dev@trigyn.com', NOW(), NULL, NULL, 2);

replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb81754acbf701754ae3d1dd000e', 'manual-entry-template', '<head>
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
<script src="${(contextPath)!''''}/webjars/1.0/manuals/helpmanual.js"></script>
</head>

<div class="container">
    <div class="topband">
        <h2 id="title" class="title-cls-name float-left"></h2> 
        <div class="float-right">
            <form id="viewManualForm" action="${(contextPath)!''''}/cf/manual" method="get" target="_blank" class="margin-r-5 pull-left">
  				<input id="manualTypeView" name="mt" type="hidden" value="${mt}">
  				<button type="submit" class="btn btn-primary"> View Manual </button>
			</form>
            <form id="addEditRecords" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
                <input type="hidden" name="formId" value="8a80cb81754acbf701754ae3d1c2000c"/>
                <input type="hidden" name="manualentryid" id="manualentryid" value=""/>
                <input type="hidden" name="manualType" id="manualType" value="${mt}"/>
                <input type="hidden" name="manualName" id="manualName" value="${mn}">
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
    <div id="deleteEntry"></div>

</div>

<script>
    contextPath = "${(contextPath)!''''}";
    let manualType = "${mt}";
    let dateFormat = "${(dbDateFormat)!''''}";
    let manualObject = new HelpManual();
    let nextEntrySequence = 0;
    let manualdata;
    $(function () {
       	
        let formElement = $("#addEditRecords")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("8a80cb81754acbf701754ae3d1c2000c", formDataJson);
        manualdata = manualObject.getManualDetails();
        $("#title").html(manualdata.find(manual => {return manual["manual_id"] == manualType})["name"]);
        getMaxSequenceNumber();
        
        //Add all columns that needs to be displayed in the grid
        let colM = [
            { title: "Entry name", width: 130, dataIndx: "entry_name", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            { title: "Sort index", width: 130, dataIndx: "sort_index", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            { title: "Last updated by", width: 130, dataIndx: "last_updated_by", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            { title: "Last Updated Timestamp", width: 130, dataIndx: "last_modified_on", align: "left", align: "left", halign: "center" , render: lastUpdatedDate },
            { title: "Action", width: 50, minWidth: 115, dataIndx: "action", align: "center", halign: "center", render: manageRecord, sortable: false}
        ];
    	
    	let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "last_modified_on",
            sortDir: "down"
    	};
    	
    //System will fecth grid data based on gridId
        let grid = $("#manual-entryGrid").grid({
          gridId: "manual-entryGrid",
          colModel: colM,
          dataModel: dataModel,
          additionalParameters: {"cr_manual_type" : "str_"+manualType},
          loadCallback: function(event, ui) {
              $("#sequence").val(nextEntrySequence);
          }
        });
        

    });
    
    //Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
    function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
        return ''<span id="''+rowIndx+''" onclick="createNew(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span><span id="''+rowIndx+''" onclick="deleteEntry(this)" class= "grid_action_icons"><i class="fa fa-trash"></i></span>''.toString();
    }
    
    function lastUpdatedDate(uiObject) {
        let lastModifiedDate = new Date(uiObject.rowData.last_modified_on);
        let lastModifiedDatea = Calendar.printDate(lastModifiedDate,"%d-%m-%Y %H:%M:%S %p");
        return lastModifiedDatea;
    }
    
    //Add logic to navigate to create new record
    function createNew(element) {
        let rowData = $( "#manual-entryGrid" ).pqGrid("getRowData", {rowIndxPage: element.id});
        $("#manualentryid").val(rowData["manual_entry_id"]);
        $("#addEditRecords").submit();
    }
    
    function getMaxSequenceNumber(){
        $.ajax({
		    type : "POST",
			async : true,
			url : contextPath+"/api/manual-entry-sequence",
			data : {
			 	manualType : manualType
			},success: function(maxManualSequence){
				if(maxManualSequence !== ""){ 
                	nextEntrySequence = parseInt(maxManualSequence) + 1;
                }
            },error: function(data, errorMessage, xhr){
                showMessage("Error occurred while fetching max sequence", "error")
            },
	    });
    }
	
    //Code go back to previous page
    function backToWelcomePage() {
        location.href = contextPath+"/cf/help";
    }

    function deleteEntry(element) {
        let rowData = $( "#manual-entryGrid" ).pqGrid("getRowData", {rowIndxPage: element.id});
        let manualEntryId = rowData.manual_entry_id;
        let manualTypeId  = rowData.manual_type;
        let sortIndex = rowData.sort_index;
        let entryName = rowData.entry_name;
		$("#deleteEntry").html("Are you sure you want to delete " + entryName + "?");
		$("#deleteEntry").dialog({
		bgiframe		 : true,
		autoOpen		 : true, 
		modal		 : true,
		closeOnEscape : true,
		draggable	 : true,
		resizable	 : false,
		title		 : "Delete",
		buttons		 : [{
				text		:"Cancel",
				click	: function() { 
					$(this).dialog("close");
				},
			},
			{
				text		: "Delete",
				click	: function(){
					$(this).dialog("close");
					deleteManualEntry(manualEntryId, manualTypeId, sortIndex);
					let gridNew = $("#manual-entryGrid").pqGrid();
        			gridNew.pqGrid("refreshDataAndView" ); 
				}
           	},
       ],	
		open		: function( event, ui ) {
			 $(".ui-dialog-titlebar")
		   	    .find("button").removeClass("ui-dialog-titlebar-close").addClass("ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close")
		       .prepend("<span class=''ui-button-icon ui-icon ui-icon-closethick''></span>").append("<span class=''ui-button-icon-space''></span>");
       		}	
	   });
       console.log(rowData["manual_entry_id"]);
    }
    
    function deleteManualEntry(manualEntryId, manualTypeId, sortIndex){ 
    	$.ajax({
		    type : "DELETE",
			async : true,
			url : contextPath+"/cf/dme",
			data : {
				mt : manualTypeId,
			 	mei : manualEntryId,
			 	si: sortIndex,
			},success: function(data){
                showMessage("Manual entry deleted successfully", "success");
            },error: function(data, errorMessage, xhr){
                showMessage("Error occurred while deleting manual entry", "error");
            },
	    });
    }
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);

replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8175479e47017547d34a8d001c', 'manual-type-template', '<head>
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
</head>

<div class="container">
    <div class="topband">
        <h2 class="title-cls-name float-left">Manual type Listing</h2> 
        <div class="float-right">
        Show:<select id="typeSelect" class="typeSelectDropDown" onchange="changeType()" style="width:100px; height: 32px;">   
                <option value="0">All</option>                   
                <option value="1" selected>Custom</option>                   
                <option value="2">System</option>                 
            </select>
            <button type="submit" class="btn btn-primary" onclick="createNew()"> Create New Manual </button>

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
            <input type="hidden" id="manualId" name="manualId"  value="" maxlength="10" class="form-control">
            <div class="col-inner-form full-form-fields">
                <label for="name" style="white-space:nowrap"><span class="asteriskmark">*</span>
                    Name
                </label>
                <input type="text" id="manualName" name="name"  value="" maxlength="50" class="form-control">
            </div>
            <input type="hidden" id="issystemmanual" name="issystemmanual" value="0">
        </div>
    </div>
    
    <input id="moduleId" value="fcd0df1f-783f-11eb-94ed-f48e38ab8cd7" name="moduleId"  type="hidden">
    <@templateWithoutParams "role-autocomplete"/>
    
    <div class="row">
        <div class="col-12">
            <div class="float-right">
                <input id="saveBtn" class="btn btn-primary" name="saveBtn" onclick="saveData()" value="Save" type="button">
                <input id="backBtn" class="btn btn-secondary" name="backBtn" onclick="cancelForm()" value="Cancel" type="button">
            </div>
        </div>
    </div>
</form>


     
<form id="viewManualForm" action="${(contextPath)!''''}/cf/manual" method="get" target="_blank">
  <input id="manualType" name="mt" type="hidden">
</form>

    <form id="viewManualEntry" action="${(contextPath)!''''}/cf/ehme" method="post">
    	<input type="hidden" name="mt" id="manualTypeListing" />
		<input type="hidden" name="mn" id="manualNameListing" />
    </form>

<script>

    contextPath = "${(contextPath)!''''}";
    let isEdit;
    $(function () {
		$("#typeSelect").each(function () {
	        $(this).val($(this).find("option[selected]").val());
	    });
    	//Add all columns that needs to be displayed in the grid
        let colM = [
            { title: "Name", dataIndx: "name", width: 800, align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            { title: "Action", minWidth: 115, dataIndx: "action", align: "center", halign: "center", render: manageRecord, sortable: false}
        ];
    
    let dataModel = {
       	url: contextPath+"/cf/pq-grid-data",
       	sortIndx: "name",
        sortDir: "up"
    };
    //System will fecth grid data based on gridId
        let grid = $("#manual-typeGrid").grid({
          gridId: "manual-typeGrid",
          colModel: colM,
          dataModel: dataModel,
          additionalParameters: {"cr_is_system_manual":"str_1"}
        });
    
    });
    
	function changeType() {
        var type = $("#typeSelect").val();   
        let postData;
        if(type == 0) {
            postData = {gridId:"manual-typeGrid"}
        } else {
            let typeCondition = "str_"+type;       
   
            postData = {gridId:"manual-typeGrid"
                    ,"cr_is_system_manual":typeCondition
                    }
        }
        
        let gridNew = $( "#manual-typeGrid" ).pqGrid();
        gridNew.pqGrid( "option", "dataModel.postData", postData);
        gridNew.pqGrid( "refreshDataAndView" );  
    }
      
    //Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
    
    function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
    	<#if loggedInUserRoleList?? && loggedInUserRoleList?size gt 0>
        	<#list loggedInUserRoleList as loggedInUserRole>
            	<#if (loggedInUserRole == "ADMIN" || loggedInUserRole == "AUTHENTICATED")>    
        			return ''<span id="''+rowIndx+''" onclick="createNew(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span><span id="''+rowIndx+''" onclick="editEntityManual(this)" class= "grid_action_icons"><i class="fa fa-edit"></i></span><span id="''+rowIndx+''" onclick="viewManual(this)" class= "grid_action_icons"><i class="fa fa-eye"></i></span>''.toString();
        		<#break>
        		<#else>
        			return ''<span id="''+rowIndx+''" onclick="viewManual(this)" class= "grid_action_icons"><i class="fa fa-eye"></i></span>''.toString();
        		</#if>
        	</#list>
        </#if>
    }
    
    //Add logic to navigate to create new record
    <#if loggedInUserRoleList?? && loggedInUserRoleList?size gt 0>
	    <#list loggedInUserRoleList as loggedInUserRole>
        	<#if (loggedInUserRole == "ADMIN" || loggedInUserRole == "AUTHENTICATED")>   
			    function createNew(element) {
			        multiselect.removeAll();
			    	if(element === undefined){ 
			    		isEdit = 0;
			    		$("#manualId").val("");
				        $("#manualName").val("");
			    		$("#manual-add-edit").modal();
						let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
			        	multiselect.setSelectedObject(defaultAdminRole);
			    	}else{
				        isEdit = 1;
				        const id = Number.parseInt(element.id);
				        let rowData = $( "#manual-typeGrid" ).pqGrid("getRowData", {rowIndxPage: id});
				        $("#manualId").val(rowData["manual_id"]);
				        $("#manualName").val(rowData["name"]);
				        $("#issystemmanual").val(rowData["is_system_manual"]);
						$("#manual-add-edit").modal();
						getEntityRoles();
					}
			    }
			
			    function editEntityManual(element) {
			    	const id = Number.parseInt(element.id);
			    	let rowData = $( "#manual-typeGrid" ).pqGrid("getRowData", {rowIndxPage: id});
			    	$("#manualTypeListing").val(rowData["manual_id"]);
			    	$("#manualNameListing").val(rowData["name"]);
			        $("#viewManualEntry").submit();
			    }
        		<#break>
        	</#if>
        </#list>
    </#if>
    
    function viewManual(element) {
        const id = Number.parseInt(element.id);
        let rowData = $( "#manual-typeGrid" ).pqGrid("getRowData", {rowIndxPage: id});
        $("#manualType").val(rowData["manual_id"]);
        $("#viewManualForm").submit();
    }

    function saveData() {
        // save function
        $.ajax({
            type: "POST",
            url: contextPath+"/cf/shmt",
            data: {
                mt: $("#manualId").val(),
                ie: isEdit,
                name: $("#manualName").val(),
            },
            success: function(data) {
            	$("#manualId").val(data);
                saveEntityRoleAssociation();
                $.modal.close();
                showMessage("Information saved successfully", "success");
                $("#manual-typeGrid").pqGrid("refreshDataAndView")
            },
            error: function(xhr, data) {
                showMessage("You do not have enough privilege", "error");
                $.modal.close();
            }
        })
    }

	function saveEntityRoleAssociation(){
		let roleIds =[];
		let entityRoles = new Object();
		entityRoles.entityName = $("#manualName").val().trim();
		entityRoles.moduleId=$("#moduleId").val();
		entityRoles.entityId= $("#manualId").val().trim();
		 $.each($("#rolesMultiselect_selectedOptions_ul span.ml-selected-item"), function(key,val){
			 roleIds.push(val.id);
         	
         });
		
		entityRoles.roleIds=roleIds;
		
		$.ajax({
            async : false,
            type : "POST",
            contentType : "application/json",
            url : contextPath+"/cf/ser", 
            data : JSON.stringify(entityRoles),
            success : function(data) {
		    }
        });
	}
	
	function getEntityRoles(){
		$.ajax({
            async : false,
            type : "GET",
            url : contextPath+"/cf/ler", 
            data : {
            	entityId:$("#manualId").val(),
            	moduleId:$("#moduleId").val(),
            },
            success : function(data) {
                $.each(data, function(key,val){
                	multiselect.setSelectedObject(val);
                	
                });
		    }
        });
	}
	
    function cancelForm() {
        $.modal.close();
    }

    //Code go back to previous page
    function backToWelcomePage() {
        location.href = contextPath+"/cf/home";
    }
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);

replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8175513bc80175514206ef0000', 'manual-display', '<script src="${(contextPath)!''''}/webjars/1.0/manuals/helpmanual.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/markdown/highlight/github.min.css" />
<script src="${(contextPath)!''''}/webjars/1.0/markdown/highlight/highlight.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>

<div class="container ">
    <div class="pg-manual-display">
        <div class="row">
            <div class="topband  help-manual-title-band">
                <h2 id="title" class="title-cls-name float-left help-manual-title"></h2>
                <div class="cm-searchwithicon">
                        <div class="form-group has-search clearfix"> <span class="fa fa-search form-control-feedback"></span>
                            <input type="text" id="searchInputField" class="form-control" placeholder="Search..." onkeyup="search(event, this.value)">
                            <span id="manualSearchClear" onclick="clearManualSearch()" class="manual-clear-txt">
                    			<i class="fa fa-times" aria-hidden="true"></i>
                			</span>  
                        </div>
                          <div id="cm-errormsg-div" class="" style="display: none;">
                 <div class="cm-empty-data clearfix">
                
                 <div class="cm-empty-messege">Your search did not match any documents</div>
                 <p> Please try with different keyword.</p>
                 </div>
            </div>
                        
                       
                    </div>
                <div class="float-right"> <span onclick="back();">
                        <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button" style="display:none">
                    </span> </div>
                <div class="clearfix"></div>
            </div>
        </div>
            <div id="manual-container" class="cm-rightbar">
                <div class="row cm-bottom-border">
                    <span> <i class="icon icon-s-information"></i></span>
                </div>

                <div class="cm-main-wrapper preview cm-scrollbar clearfix">
                    
                    <div class="cm-left-wrapper cm-scrollbar">
                        <div id="tabs" class=" tabs  "></div>

                    </div>
                    <div id="contentDiv" class="cm-right-wrapper ">
                        
                        <div id="previews" class="previews  cm-scrollbar"></div>
                    </div>
                    
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
    <#if !mt?? || !mt?has_content>
        location.href = contextPath+"/cf/not-found";
    </#if>
let manual = new HelpManual();
let manualTypes = manual.getManualDetails();
$("#title").html(manualTypes.find(manual => {
    return manual["manual_id"] == "${mt!''''}"
})["name"]);
manual.getManualEntities("${mt!''''}");
for(let counter = 0; counter < manual.helpManualDetails.length; counter++) {
    let data = manual.helpManualDetails[counter];
    $("#tabs").append("<button id=''" + data["manual_entry_id"] + "'' class=''tablinks'' onclick=''manual.loadManualPreview(event, this)''>" + "<img src=''${(contextPath)!''''}/webjars/1.0/images/s-information1.svg'' >" + data["entry_name"]  + "</button>");
    let simplemde = new SimpleMDE({
        initialValue: manual.helpManualDetails[counter]["entry_content"],
        renderingConfig: {
            codeSyntaxHighlighting: true,
        }
    });
    manual.helpManualDetails[counter]["divContent"] = $(simplemde.options.previewRender(simplemde.value())).text();
}
$("#tabs button")[0].click();
<#if sl?? && sl?has_content && sl == "1">
	$("#backBtn").hide();
<#else>
	$("#backBtn").show();
</#if>
function back() {
    location.href = contextPath + "/cf/help"
}

function search(event, value) {
    let searchText = value.toLowerCase();
        let noOfManualVisible = 0;
        let firstManual;
        $.each( manual.helpManualDetails, function(index, manual){
            let divContent = manual.divContent.toLowerCase();
            if(divContent.indexOf(searchText) !== -1){
                if(firstManual === undefined){
                    firstManual = index;
                }
                $("#"+manual.manual_entry_id).show();
                noOfManualVisible++;
            }else{
                $("#"+manual.manual_entry_id).hide();
            }
        });
        
        if(noOfManualVisible > 0){
            $("#manual-container").show();
            $("#contentDiv").show();
            $("#cm-errormsg").hide();
            $("#cm-errormsg-div").hide();
            $("#tabs button")[firstManual].click();
        }else{
            $("#cm-errormsg").show();
            $("#cm-errormsg-div").show();
            $("#contentDiv").hide();
            $("#cm-errormsg").text("Your search - "+$("#searchInputField").val().trim()+" - did not match any manual documents.");
            $("#manual-container").hide();
        }
}

function clearManualSearch(){
	$("#searchInputField").val("");
	search("", "");
}
    </script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);

REPLACE INTO jq_grid_details (grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id) VALUES
('manual-entryGrid', 'manual-entryGrid', 'manual-entry Listing', 'jq_manual_entry', 'manual_entry_id,manual_type,entry_name,entry_content,sort_index,last_modified_on,last_updated_by', 1, 2), 
('manual-typeGrid', 'manual-typeGrid', 'manual-type Listing', 'jq_manual_type', 'manual_id,name,is_system_manual', 1, 2);

REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id) VALUES
('9a8197cc-f505-40a3-b2a0-94bac583d648', 'manual-entry-sequence', 1, 'getMaxManualSequnece', '', 1, 7, '<#if manualSequenceDetailsList?? && manualSequenceDetailsList?size != 0>
    <#list manualSequenceDetailsList as manualSequenceDetails>
        <#if (manualSequenceDetails.sortIndex)?? && (manualSequenceDetails.sortIndex)?has_content>
        	${manualSequenceDetails.sortIndex}
        </#if>
    </#list>
</#if>', 2, 0, 2);


REPLACE INTO jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(115, '9a8197cc-f505-40a3-b2a0-94bac583d648', 'manualSequenceDetailsList', 'SELECT MAX(sort_index) AS sortIndex FROM jq_manual_entry WHERE manual_type = :manualType', 1, 1);


REPLACE INTO jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('68d89a46-b682-41b7-b15e-57a2b772e4c8', '9a8197cc-f505-40a3-b2a0-94bac583d648', 'manual-entry-sequence', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('94ae24fa-853a-4b0f-a176-59556b2cb108', '9a8197cc-f505-40a3-b2a0-94bac583d648', 'manual-entry-sequence', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0), 
('d1f4ffd6-596e-4563-b9b2-5f3f72412921', '9a8197cc-f505-40a3-b2a0-94bac583d648', 'manual-entry-sequence', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), '111415ae-0980-11eb-9a16-f48e38ab9348', 1, 0);

SET FOREIGN_KEY_CHECKS=1;