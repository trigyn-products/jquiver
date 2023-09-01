SET FOREIGN_KEY_CHECKS=0;

replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('1f0f42df-62af-4bfe-9168-c5d9a6328405', 'jq-api-client-details-template', '<head>
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
</head>

<div class="container">
    <div class="topband">
        <h2 class="title-cls-name float-left">API Clients</h2> 
        <div class="float-right">
             <button type="submit" class="btn btn-primary" onclick="openAddEditScreen()"> Create New </button>
            <span onclick="backToWelcomePage();">
                <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
            </span> 
        </div>
        
        <div class="clearfix"></div>        
    </div>
        
    <div id="api_client_details_grid"></div>

    <div id="snackbar"></div>
</div>

<script>
    contextPath = "${(contextPath)!''''}";
    let primaryKeyDetails = {"clientid":""};
    $(function () {
    //Add all columns that needs to be displayed in the grid
        let colM = [
            	{ title: "Client Id", hidden : true, width: 130, dataIndx: "client_id", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            	{ title: "Client Name", hidden : false, width: 130, dataIndx: "client_name", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            	{ title: "Client Key", hidden : false, width: 130, dataIndx: "client_key", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            	{ title: "Encryption Algorithm", hidden : false, width: 130, dataIndx: "encryption_algo_name", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            	{ title: "Updated By", hidden : false, width: 130, dataIndx: "updated_by", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            	{ title: "Created By", hidden : false, width: 130, dataIndx: "created_by", align: "left", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            	{ title: "Updated Date", hidden : false, width: 130, dataIndx: "updated_date", align: "left", align: "left", halign: "center",render:dateRenderer,
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            { title: "<@resourceBundle ''jws.action'' />", width: 50, minWidth: 115, dataIndx: "action", align: "center", halign: "center", render: manageRecord, sortable: false}
        ];
    
    	let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
    	};
    //System will fecth grid data based on gridId
        let grid = $("#api_client_details_grid").grid({
          gridId: "jq_api_client_details_grid",
          colModel: colM,
          dataModel: dataModel
        });
    
    });
    
    //Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
    function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
        return ''<span id="''+rowIndx+''" onclick="createNew(this)" class= "grid_action_icons" title="<@resourceBundle''jws.edit''/>"><i class="fa fa-pencil"></i></span>''.toString();
    }
     
    function dateRenderer(ui){
        const lastUpdatedTs = ui.rowData.updated_date;
        return formatDate(lastUpdatedTs);
    }
    
    //Add logic to navigate to create new record
    function createNew(element) {
        let rowData = $( "#api_client_details_grid" ).pqGrid("getRowData", {rowIndxPage: element.id});
        primaryKeyDetails["clientid"] = rowData["client_id"];
        openAddEditScreen();
    }

    function openAddEditScreen() {
    	  let formId = "2acd30d0-ef07-4ce2-b4f4-02cbfd29bf67";
    	  openForm(formId, primaryKeyDetails);
    }

    //Code go back to previous page
    function backToWelcomePage() {
        location.href = contextPath+"/cf/dynl";
    }
</script>', 'admin', 'admin', NOW(), NULL, 2);

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('8a80cb817570ad44017570b099650002', '1f0f42df-62af-4bfe-9168-c5d9a6328405', 'jq-api-client-details-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570ad44017570b099650003', '1f0f42df-62af-4bfe-9168-c5d9a6328405', 'jq-api-client-details-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('8a80cb817570ad44017570b61ca50009', '1f0f42df-62af-4bfe-9168-c5d9a6328405', 'jq-api-client-details-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);


REPLACE INTO jq_grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id) 
VALUES ("jq_api_client_details_grid", 'API Clients Grid', 'API Clients Grid', 'jq_api_client_details_view', 'client_id,client_name,client_key,client_secret,encryption_algo_id,updated_by,created_by,updated_date,encryption_algo_name', 1, 2);

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('1798f964-abf5-406f-9a8e-7f3ec53668e4', 'jq_api_client_details_grid', 'jq_api_client_details_grid', '07067149-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('7b9d9277-ca20-41d9-baa7-f2648face6ff', 'jq_api_client_details_grid', 'jq_api_client_details_grid', '07067149-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('e6c1c53b-3ebb-4300-a896-6e47c376e9cd', 'jq_api_client_details_grid', 'jq_api_client_details_grid', '07067149-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);



replace into jq_dynamic_form (form_id, form_name, form_description, form_select_query, form_body, created_by, created_date, form_select_checksum, form_body_checksum, form_type_id) VALUES
('2acd30d0-ef07-4ce2-b4f4-02cbfd29bf67', 'api-client-details-form', 'API Clients Form', 'SELECT cd.client_id,cd.client_name,cd.client_key,cd.client_secret,cd.client_public_key,cd.encryption_algo_id  AS encryptionAlgoId,cd.updated_by,cd.created_by,cd.updated_date,ed.encryption_algo_name AS encryptionAlgoName, cd.inclusion_url_pattern FROM jq_api_client_details cd, jq_encryption_algorithms_lookup ed WHERE ed.encryption_algo_id = cd.encryption_algo_id AND cd.client_id = "${clientid!''''}"', 
'<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
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
				<input type="hidden" data-type="varchar" id="clientid" name="clientid"  value="${(resultSetObject.client_id)!""}" >
	    		<div class="col-6">
					<div class="col-inner-form full-form-fields">
						            <label for="clientname" style="white-space:nowrap"><span class="asteriskmark">*</span>
						            	Client Name
						            </label>
								<input type="text" data-type="varchar" id="clientname" name="clientname"  value="${(resultSetObject.client_name)!""}" maxlength="100" placeholder="Client Name" class="form-control">
					</div>
				</div>
                <div class="col-6">
                    <div class="col-inner-form full-form-fields">
                        <label for="algoAutocomplete" style="white-space:nowrap"><span class="asteriskmark">*</span>
                            Encryption Algorithm</label>
                        <div class="search-cover">
                            <input class="form-control" id="algoAutocomplete" type="text">
                            <i class="fa fa-search" aria-hidden="true"></i>
                        </div>
                    </div>
				</div>
	    		<div class="col-12">
					<div class="col-inner-form full-form-fields">
						<label for="clientkey" style="white-space:nowrap"><span class="asteriskmark">*</span>
						     	Client Key
						</label>
                        <div class="input-group">
								<input type="text" data-type="varchar" id="clientkey" name="clientkey"  value="${(resultSetObject.client_key)!""}" 
                                    maxlength="100" placeholder="Client Key" class="form-control" readonly style="margin-right: 10px;">
                                <input id="clientkeyCopyBtn" class="btn btn-secondary" name="clientkeyCopyBtn" value="Copy" type="button" onclick="copyToClipboard(''clientkey'',''clientkeyCopyBtn'')">
                        </div>
					</div>
				</div>
	    		<div id="publicKeyDiv" class="col-6" style="display:none;">
					<div class="col-inner-form full-form-fields">
                        <label id="lblPublicKey" for="publicKey" style="white-space:nowrap;"><span id="astr_1" class="asteriskmark">*</span>
			            	Public Key
			            </label>
                        <div class="input-group">
						<textarea rows="3" data-type="varchar" id="publicKey" name="publicKey"  maxlength="1000" placeholder="Public Key"  style="margin-right: 5px;" class="form-control">${(resultSetObject.client_public_key)!""}</textarea>
					
                        <input id="publicKeyCopyBtn" class="btn btn-secondary" name="publicKeyCopyBtn" value="Copy" type="button" onclick="copyToClipboard(''publicKey'',''publicKeyCopyBtn'')">
                        </div>
                    </div>
				</div>
	    		<div class="col-6">
					<div class="col-inner-form full-form-fields">
                        
						    <label id="lblClientSecret" for="clientsecret" style="white-space:nowrap"><span id="astr_2" class="asteriskmark">*</span>
						      	Client Secret
						    </label> 
                        <div class="input-group">
						<textarea rows="3" data-type="varchar" id="clientsecret" name="clientsecret" maxlength="1000" placeholder="Client Secret" style="margin-right: 5px;" class="form-control">${(resultSetObject.client_secret)!""}</textarea>
                                                   
                            <input id="clientsecretCopyBtn" class="btn btn-secondary" name="clientsecretCopyBtn" value="Copy" type="button" onclick="copyToClipboard(''clientsecret'',''clientsecretCopyBtn'')">
                        </div>
					</div>
				</div>
	    		<div class="col-6">
					<div class="col-inner-form full-form-fields">
						    <label id="lblInclusionUrlPattern" for="inclusionUrlPattern" style="white-space:nowrap"><span id="astr_3" class="asteriskmark">*</span>
						      	Inclusion Pattern
						    </label>  
                        <textarea rows="3" data-type="varchar" id="inclusionUrlPattern" name="inclusionUrlPattern" maxlength="1000" placeholder="Inclusion Pattern" class="form-control">${(resultSetObject.inclusion_url_pattern)!""}</textarea>
					            
                    </div>
				</div>
                <div class="col-6">
					<div class="col-inner-form full-form-fields">
						    <label for="inputText" style="white-space:nowrap">
						      	Test Input
						    </label> 
                        <div class="input-group"> 
                        <textarea rows="3" data-type="varchar" id="inputText" name="inputText" style="margin-right: 10px;" maxlength="1000" placeholder="Test Input" class="form-control"></textarea>
                        <div class="btn-group-vertical">
                        <input id="encryptBtn" class="btn btn-secondary" style="margin-bottom: 10px;" name="encryptBtn" value="Encrypt" type="button" onclick="encrypt();">
                        <input id="decryptBtn" class="btn btn-secondary" name="decryptBtn" value="Decrypt" type="button" onclick="decrypt();">
                        </div>
                        </div>
                    </div>
				</div>
                <div class="col-6">
					<div class="col-inner-form full-form-fields">
						    <label for="outputText" style="white-space:nowrap">
						      	Test Output
						    </label>  
                        <textarea rows="3" data-type="varchar" id="outputText" name="outputText" maxlength="1000" placeholder="Test Output" class="form-control"></textarea>
					            
                    </div>
				</div>
				
				<input type="hidden" data-type="varchar" id="updatedby" name="updatedby"  value="${(resultSetObject.updated_by)!""}" >
				<input type="hidden" data-type="varchar" id="createdby" name="createdby"  value="${(resultSetObject.created_by)!""}" >
				<input type="hidden" data-type="timestamp" id="updateddate" name="updateddate"  value="${(resultSetObject.updated_date)!""}" >
    	</div>
    
  </form>
	<div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropup custom-grp-btn">
                    <div id="keyPairGeneratorDiv"style="display:none;">
                        <input id="keyPairGeneratorBtn" style="margin-right:2.5px;" onclick="generateKeyPair();" class="btn btn-primary" name="keyPairGeneratorBtn" value="Generate Key Pair" type="button">
                    </div>
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
    let autocomplete;
    let selectedAlgo;

  $(function(){
      let obj;
    // setting value on edit.
      <#if (resultSet)??>
      	<#list resultSet as resultSetList>
          obj = new Object();
          obj["encryptionAlgoName"] = ''${resultSetList?api.get("encryptionAlgoName")!""}'';
          obj["encryptionAlgoId"] = ''${resultSetList?api.get("encryptionAlgoId")!""}'';
      	</#list>
      </#if>
    
      <#if (resultSet)?? && resultSet?has_content>
      	isEdit = 1;
      </#if>

      if(obj != null && obj["encryptionAlgoName"] == "RSA") {
        $("#publicKeyDiv").show();
        $("#lblClientSecret").html("<span class=''asteriskmark''>*</span> Private Key");
        $("#clientsecret").attr("placeholder", "Private Key");
        $("#keyPairGeneratorDiv").show();
      }

      if(isEdit == 0) {
          const clientid = uuidv4();
		$("#clientid").val(clientid);
		$("#primaryKey").val(clientid);
          const clientkey = uuidv4();
		$("#clientkey").val(clientkey);

        obj = new Object();
          obj["encryptionAlgoName"] = ''NA'';
          obj["encryptionAlgoId"] = ''0'';
      }

if(obj != null && obj["encryptionAlgoName"] == "NA") {
    handleNAElements();
} else  {
    handleNonNAElements();
}

    autocomplete = $(''#algoAutocomplete'').autocomplete({
        autocompleteId: "encryptAlgoAutocomplete",
        pageSize: 10,//Default page size is 10
        prefetch : true,
        render: function(item) {
            var renderStr ='''';
            if(item.emptyMsg == undefined || item.emptyMsg === '''') {
                renderStr = ''<p>''+item.encryptionAlgoName+''</p>'';
            } else {
                renderStr = item.emptyMsg;    
            }                                
            return renderStr;
        },
        extractText: function(item) {
            return item.encryptionAlgoName;
        },
        select: function(item) {
           autocomplete.setSelectedObject(item);
           toggleTextField(item);
        },     
        resetAutocomplete: function(){ 
            //This function will be executed onblur or when user click on clear text button
            //Code to reset dependent JavaScript variables, input fields etc.
        }, 
    });
    
    autocomplete.setSelectedObject(obj);
	savedAction(formId, isEdit);
	hideShowActionButtons();
  });

  function generateKeyPair() {
      
		$.ajax({
		  type : "GET",
		  async: false,
		  url : contextPath+"/cf/grsakp",
          success : function(data) {
              $("#publicKey").text(data.publicKey);
              $("#clientsecret").text(data.privateKey);              
		  },
	      error : function(xhr, error){
			showMessage("Error occurred while saving", "error");
	      },
		});
  }

  function encrypt() {
      if(validateTestEncDec($("#clientsecret").val())) {
        $.ajax({
            type : "GET",
            async: false,
            url : contextPath+"/cf/enc",
            data: {
                inputData: $("#inputText").val(),
                algo: autocomplete.getSelectedObject()["encryptionAlgoName"],
                pk: $("#clientsecret").val()
            },
            success : function(data) {
                $("#outputText").val(data);         
            },
            error : function(xhr, error){
                showMessage("Invalid secret key", "error");
            },
		});
      }
  }

  function decrypt() {
      let secretKey;
      if(autocomplete.getSelectedObject()["encryptionAlgoName"] == "RSA") {
          secretKey = $("#publicKey").val();
      } else {
          secretKey = $("#clientsecret").val();
      }
      if(validateTestEncDec(secretKey)) {
        $.ajax({
            type : "GET",
            async: false,
            url : contextPath+"/cf/dec",
            data: {
                inputData: $("#inputText").val(),
                algo: autocomplete.getSelectedObject()["encryptionAlgoName"],
                pk: secretKey,
            },
            success : function(data) {
                $("#outputText").val(data);          
            },
            error : function(xhr, error){
                showMessage("Error occurred while decryption. Invalid input/secret key.", "error");
            },
        });
      }
  }

  function validateTestEncDec(secretKey) {
       if(autocomplete.getSelectedObject()["encryptionAlgoName"] == "NA") {
            showMessage("No algorithm is selected", "error");
            return false;   
      } else if($("#inputText").val() == null || $("#inputText").val() == undefined || $("#inputText").val() == "") {
            showMessage("Test input is empty.", "error");
            return false;
      } else if(secretKey == null || secretKey == undefined || secretKey == "") {
            showMessage("Client secret key is required to test", "error");
            return false;
      } else if(autocomplete.getSelectedObject()["encryptionAlgoName"] == "DES" && secretKey.length <= 25) {
          	showMessage("Client secret key should be more than 25 characters for DES", "error");
            return false;
      }
      return true;
  }

  function toggleTextField(item) {
      handleNonNAElements();
      if(item.encryptionAlgoName == "RSA") {
        $("#publicKeyDiv").show();
        $("#lblClientSecret").html("<span class=''asteriskmark''>*</span> Private Key");
        $("#clientsecret").attr("placeholder", "Private Key");
        $("#keyPairGeneratorDiv").show();
        
      } else {
          $("#publicKeyDiv").hide();
        $("#keyPairGeneratorDiv").hide();
          $("#publicKey").val("");
          $("#lblClientSecret").html("<span class=''asteriskmark''>*</span> Client Secret");
          $("#clientsecret").attr("placeholder", "Client Secret");

          if(item.encryptionAlgoName == "NA") {
                handleNAElements()
          } 
      }
  }

  function handleNAElements() {
      $("#astr_1").hide();
      $("#astr_2").hide();
      $("#astr_3").hide();
  }
  
  function handleNonNAElements() {
      $("#astr_1").show();
      $("#astr_2").show();
      $("#astr_3").show();
  }
  
  function copyToClipboard(textName, btnName) {
  var copyText = document.getElementById(textName);
  copyText.select();
  copyText.setSelectionRange(0, 99999); /* For mobile devices */
  document.execCommand("copy"); 
  showMessage("Data copied", "success");
}
	//Add logic to save form data
	function saveData(){
		let isDataSaved = false;
        
        if(autocomplete.getSelectedObject()["encryptionAlgoId"] == null || autocomplete.getSelectedObject()["encryptionAlgoId"] == undefined) {
            showMessage("All fields are mandatory", "error");
			return false;
        } else {
            selectedAlgo = autocomplete.getSelectedObject()["encryptionAlgoName"];
        }
		let formData = validateData();	
		if(formData === undefined){
			$("#errorMessage").html("All fields are mandatory");
			$("#errorMessage").show();
			return false;
		}

        
		$("#errorMessage").hide();
		formData.push({"name": "formId", "value": formId, "valueType": "varchar"});
		formData.push({"name": "isEdit", "value": (isEdit + ""), "valueType": "int"});
		
        let formIdObj = new Object();
		formIdObj["name"] = "encryptionalgoid";
		formIdObj["value"] = autocomplete.getSelectedObject()["encryptionAlgoId"]+"";
		formIdObj["valueType"] = "int";
        formData.push(formIdObj);
		
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
		let serializedForm = $("#addEditForm").serializeArray();
		for(let iCounter =0, length = serializedForm.length;iCounter<length;iCounter++){
			let fieldValue = $.trim(serializedForm[iCounter].value);
			let fieldName = $.trim(serializedForm[iCounter].name);
			let isFieldVisible = $("#"+fieldName).is(":visible");
            if(fieldName != "inputText" && fieldName != "outputText") {
                if(selectedAlgo == "NA" && (fieldName=="clientsecret" || fieldName=="publicKey" || fieldName=="inclusionUrlPattern")) {
                    fieldValue = "";
                } else {
                    if(fieldValue !== ""){
                        serializedForm[iCounter].value = fieldValue;
                    }else if(isFieldVisible === true){
                        return undefined;
                    }  
                }
            }
		}
		serializedForm = serializedForm.formatSerializedArray();
		return serializedForm;
    }
    
	//Code go back to previous page
	function backToPreviousPage() {
			location.href = contextPath+"/cf/acd";
	}
	
</script>', 'admin', NOW(), NULL, NULL, 2);


REPLACE into jq_dynamic_form_save_queries (dynamic_form_query_id, dynamic_form_id, dynamic_form_save_query, sequence) VALUES
('19345c0e-8554-4f9a-afb0-275028a43ce3', '2acd30d0-ef07-4ce2-b4f4-02cbfd29bf67', '<#if isEdit == 1>
    UPDATE jq_api_client_details SET client_name = :clientname,client_key = :clientkey,client_secret = :clientsecret,encryption_algo_id = :encryptionalgoid,updated_by = ''admin'',created_by = ''admin'',updated_date = NOW(), inclusion_url_pattern =:inclusionUrlPattern ,client_public_key=:publicKey WHERE client_id = :clientid
    <#else>
    INSERT INTO jq_api_client_details (client_id,client_name,client_key,client_secret,encryption_algo_id,updated_by,created_by,updated_date,inclusion_url_pattern,client_public_key) VALUES (UUID(),:clientname,:clientkey,:clientsecret,:encryptionalgoid,''admin'',''admin'',NOW(),:inclusionUrlPattern,:publicKey)
    </#if>', 1);

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('0b969f0d-4edd-4251-913b-ea9dea7511a1', '2acd30d0-ef07-4ce2-b4f4-02cbfd29bf67', 'api-client-details-form', '30a0ff61-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('66c9de9c-f427-47c7-9713-d8a44d7f1e99', '2acd30d0-ef07-4ce2-b4f4-02cbfd29bf67', 'api-client-details-form', '30a0ff61-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('8962754e-51f6-4d09-8140-ee8baef95a72', '2acd30d0-ef07-4ce2-b4f4-02cbfd29bf67', 'api-client-details-form', '30a0ff61-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);


REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id) VALUES
('encryptAlgoAutocomplete', 'Encyption alogorithm autocomplete', 
'SELECT encryption_algo_id AS encryptionAlgoId, encryption_algo_name AS encryptionAlgoName FROM jq_encryption_algorithms_lookup WHERE `encryption_algo_name` LIKE CONCAT("%", :searchText, "%") LIMIT :startIndex, :pageSize', 2);

replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('4647412b-c048-40fc-b003-973bdb4c9b57', 'encryptAlgoAutocomplete', 'encryptAlgoAutocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('57106832-131e-42ff-8ffe-83eeeed3fd8a', 'encryptAlgoAutocomplete', 'encryptAlgoAutocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('e975d8d6-ea9e-4566-a393-8e2db67e030b', 'encryptAlgoAutocomplete', 'encryptAlgoAutocomplete', '91a81b68-0ece-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);


replace into jq_module_listing_i18n (module_id, language_id, module_name) VALUES ('26b42b12-8815-427c-9945-f1c7aa7064b3', 1, 'API Clients');


replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active) VALUES
('75b145e8-3e5a-45e7-b160-526a6e214623', '26b42b12-8815-427c-9945-f1c7aa7064b3', 'API Clients', 'c6cc466a-0ed3-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1), 
('090a1a2d-b9c5-4cdb-96f2-0abef84103d6', '26b42b12-8815-427c-9945-f1c7aa7064b3', 'API Clients', 'c6cc466a-0ed3-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1), 
('733a7170-23bb-4982-9aa4-3b18e795db61', '26b42b12-8815-427c-9945-f1c7aa7064b3', 'API Clients', 'c6cc466a-0ed3-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1);

 
 REPLACE INTO jq_master_modules(module_id, module_name, sequence, is_system_module, grid_details_id, module_type, is_perm_supported, is_entity_perm_supported, is_imp_exp_supported) VALUES
('ded49cbd-ed7c-40ce-b1f8-c2e34ad33473', 'API Clients', 20, 1, 'jq_api_client_details_grid', 'ApiClientDetails', 0, 0, 1),
('799947cc-b6cb-11eb-8529-0242ac130003', 'Additional Datasource', 21, 1, 'jq-additional-datasourceGrid', 'AdditionalDatasource', 0, 0, 1);


Delete from  jq_encryption_algorithms_lookup  where encryption_algo_id = 4;
SET FOREIGN_KEY_CHECKS=1;