
REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('d5a5102b-09ab-11eb-a027-f48e38ab8cd7', 'default-listing-template', '<head>
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
		<h2 class="title-cls-name float-left">Your page title here</h2> 
		<div class="float-right">
			<form id="addEditRecords" action="${(contextPath)!''''}/cf/df" method="post" class="margin-r-5 pull-left">
				<input type="hidden" name="formId" value=""/>
				<input type="hidden" name="primaryId" id="primaryId" value=""/>
				<button type="submit" class="btn btn-primary"> Create New </button>
			</form>


			<span onclick="backToWelcomePage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>
		
	<div id="yourGridId"></div>

</div>



<script>
	contextPath = "${contextPath}";
	$(function () {
	//Add all columns that needs to be displayed in the grid
		let colM = [
			{ title: "Column Name to be displayed", width: 130, dataIndx: "columnNameInQuery", align: "left", align: "left", halign: "center",
				filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
			{ title: "Action", width: 50, minWidth: 115, dataIndx: "action", align: "center", halign: "center", render: manageRecord, sortable: false}
		];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
    	};
	
	//System will fecth grid data based on gridId
		let grid = $("#yourGridId").grid({
	      gridId: "",
	      colModel: colM,
          dataModel: dataModel
	  	});
	
	});
	
	//Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
	function manageRecord(uiObject) {
		let primaryId = uiObject.rowData;

		console.log(uiObject);
	}
	
	//Add logic to navigate to create new record
	function createNew(element) {
		$("#primaryId").val(element.id);
		$("#addEditRecords").submit();
	}

	//Code go back to previous page
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW(), 2);


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('dfdf7c4e-09ab-11eb-a027-f48e38ab8cd7', 'default-form-template', '<head>
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


</div>



<script>
	contextPath = "${contextPath}";
	//Add logic to save form data
	function saveData (){
		let formData = $("#addEditForm").serialize();
	}
	
	//Code go back to previous page
	function backToPreviousPage() {
		
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW(), 2);




REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('e700ac42-09ab-11eb-a027-f48e38ab8cd7', 'default-template', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script> 
<script src="${(contextPath)!''''}/webjars/bootstrap/js/bootstrap.min.js"></script>
</head>
<div class="container" style="padding-top: 40px">
    <div class="page-header">
        
    </div>

	
</div>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW(), 2);



replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('ec26a648-09ab-11eb-a027-f48e38ab8cd7', 'system-form-html-template', '<head>
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/1.0/dynamicform/addEditDynamicForm.js"></script>
<script src="<#noparse>${(contextPath)!''''}</#noparse>/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="<#noparse>${(contextPath)!''''}</#noparse>/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/css/starter.style.css" />
<#if (columnDetails)??>
	<#list columnDetails as columnDetailsList>
        <#if columnDetailsList?api.get(''columnType'') == "datetime">
<link rel="stylesheet" type="text/css" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/JSCal2/css/jscal2.css" />
<link rel="stylesheet" type="text/css" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/JSCal2/css/border-radius.css" />
<link rel="stylesheet" type="text/css" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/JSCal2/css/steel/steel.css" />
<script type="text/javascript" src="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/JSCal2/js/jscal2.js"></script>
<script type="text/javascript" src="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/JSCal2/js/lang/en.js"></script>
        	<#break>
        </#if>
	</#list>
</#if>
        
</head>

<div class="container">
	<div class="topband">
		<h2 class="title-cls-name float-left">Add Edit Details</h2> 
		<div class="clearfix"></div>		
	</div>
  <form method="post" name="addEditForm" id="addEditForm">
    <div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
    
    <#if (columnDetails)??>
    	<div class="row">
    	<#list columnDetails as columnDetailsList>
			<#if (columnDetailsList.columnType) != "hidden">
	    		<div class="col-3">
					<div class="col-inner-form full-form-fields">
						<#if (columnDetailsList.i18NPresent)?? && (columnDetailsList.i18NPresent) == true>
							<#if (columnDetailsList.columnType) != "textarea" && (columnDetailsList.columnType) != "datetime">
						            <label for="${(columnDetailsList.columnName)}" style="white-space:nowrap"><span class="asteriskmark">*</span>
						              <#noparse><@resourceBundle</#noparse> "${(columnDetailsList.fieldName)!''''}" <#noparse>/></#noparse>
						            </label>
								<input type="${(columnDetailsList.columnType)}" data-type="${(columnDetailsList.dataType)}" id="${(columnDetailsList.columnName)}" name="${(columnDetailsList.columnName)}"  value="<#noparse>${(resultSetObject.</#noparse>${(columnDetailsList.tableColumnName)}<#noparse>)!""}</#noparse>" maxlength="${(columnDetailsList.columnSize)!""}" placeholder=<#noparse>"<@resourceBundle</#noparse> ''${(columnDetailsList.fieldName)!''''}'' <#noparse>/>"</#noparse> class="form-control">
				            <#elseif (columnDetailsList.columnType) == "datetime">
				                    <span class="asteriskmark">*</span>
				                    <label for="${(columnDetailsList.columnName)}"><#noparse><@resourceBundle</#noparse> "${(columnDetailsList.fieldName)!''''}" <#noparse>/></#noparse></label>
				                    <span>
										<input id="${(columnDetailsList.columnName)}" name="${(columnDetailsList.columnName)}" class="form-control" placeholder=<#noparse><@resourceBundle</#noparse> ''${(columnDetailsList.fieldName)!''''}'' <#noparse>/></#noparse> />
				                        <button id="${(columnDetailsList.columnName)}-trigger" class="calender_icon"><i class="fa fa-calendar" aria-hidden="true"></i></button>
									</span>
							<#else>
								<span class="asteriskmark">*</span>
								<label for="${(columnDetailsList.columnName)!""}"><#noparse><@resourceBundle</#noparse> "${(columnDetailsList.fieldName)!''''}" <#noparse>/></#noparse></label>
								<textarea class="form-control" rows="4" cols="90" data-type="text" <#noparse></#noparse> id="${(columnDetailsList.columnName)!""}" placeholder=<#noparse>"<@resourceBundle</#noparse> ''${(columnDetailsList.fieldName)!''''}'' <#noparse>/>"</#noparse> name="${(columnDetailsList.columnName)!""}" ><#noparse>${(resultSetObject.</#noparse>${(columnDetailsList.tableColumnName)}<#noparse>)!""}</#noparse></textarea>
							</#if>
							
						<#else>	
							<#if (columnDetailsList.columnType) != "textarea" && (columnDetailsList.columnType) != "datetime">
						            <label for="${(columnDetailsList.columnName)}" style="white-space:nowrap"><span class="asteriskmark">*</span>
						            	${columnDetailsList?api.get(''fieldName'')!''''}
						            </label>
								<input type="${(columnDetailsList.columnType)}" data-type="${(columnDetailsList.dataType)}" id="${(columnDetailsList.columnName)}" name="${(columnDetailsList.columnName)}"  value="<#noparse>${(resultSetObject.</#noparse>${(columnDetailsList.tableColumnName)}<#noparse>)!""}</#noparse>" maxlength="${(columnDetailsList.columnSize)!""}" placeholder="${(columnDetailsList.fieldName)!''''}" class="form-control">
				            <#elseif (columnDetailsList.columnType) == "datetime">
				                    <span class="asteriskmark">*</span>
				                    <label for="${(columnDetailsList.columnName)}">${(columnDetailsList.fieldName)!''''}</label>
				                    <span>
										<input id="${(columnDetailsList.columnName)}" name="${(columnDetailsList.columnName)}" class="form-control" placeholder="${(columnDetailsList.fieldName)!''''}">
				                        <button id="${(columnDetailsList.columnName)}-trigger" class="calender_icon"><i class="fa fa-calendar" aria-hidden="true"></i></button>
									</span>
							<#else>
								<span class="asteriskmark">*</span>
								<label for="${(columnDetailsList.columnName)}">${(columnDetailsList.fieldName)!''''}</label>
								<textarea class="form-control" rows="4" cols="90" data-type="text" <#noparse></#noparse> id="${(columnDetailsList.columnName)}" placeholder="${(columnDetailsList.fieldName)!''''}" name="${(columnDetailsList.columnName)}" ><#noparse>${(resultSetObject.</#noparse>${(columnDetailsList.tableColumnName)}<#noparse>)!""}</#noparse></textarea>
							</#if>
						</#if>
					</div>
				</div>
			<#else>
				<input type="${(columnDetailsList.columnType)}" data-type="${(columnDetailsList.dataType)}" id="${(columnDetailsList.columnName)}" name="${(columnDetailsList.columnName)}"  value="<#noparse>${(resultSetObject.</#noparse>${(columnDetailsList.tableColumnName)}<#noparse>)!""}</#noparse>" >
			</#if>
    	</#list>
    	</div>
    </#if>
    
  </form>
   <#noparse>
	<div class="row">
		<div class="col-12">
			<div class="float-right">
				<div class="btn-group dropup custom-grp-btn">
                    <div id="savedAction">
                        <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="onSaveButtonClick(SaveAction.Return);">${messageSource.getMessage("jws.saveAndReturn")}</button>
                    </div>
                    <button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
                    <div class="dropdown-menu action-cls"  id="actionDiv">
                    	<ul class="dropdownmenu">
                            <li id="saveAndCreateNew" onclick="onSaveButtonClick(SaveAction.CreateNew);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
                            <li id="saveAndEdit" onclick="onSaveButtonClick(SaveAction.Edit);">${messageSource.getMessage("jws.saveAndEdit")}</li>
                        </ul>
                    </div>  
                </div>
				<span onclick="backToPreviousPage();">
					<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
				</span> 
			</div>
		</div>
	</div>
	</#noparse>
</div>
<script>
  <#noparse>
	contextPath = "${contextPath}";
	 let isEdit = 0;
	</#noparse>
  
  $(function(){
    // setting value on edit.
    <#if (columnDetails)??>
        <#list columnDetails as columnDetailsList>
        <#if (columnDetailsList.columnType) == "datetime">
        Calendar.setup({
			trigger    : "${(columnDetailsList.columnName)!""}-trigger",
			inputField : "${(columnDetailsList.columnName)!""}",
			dateFormat : "%d-%b-%Y",
			weekNumbers: true,
            showTime: 12,
			onSelect   : function() { 
				let selectedDate = this.selection.get();
				let date = Calendar.intToDate(selectedDate);
				date = Calendar.printDate(date, "%d-%b-%Y");
				$("#"+this.inputField.id).val(date);
				this.hide(); 
			}
		});
        </#if>
        </#list>
    </#if>
    <#noparse>
      <#if (resultSet)??>
      	<#list resultSet as resultSetList>
    </#noparse>  		
      		<#if (columnDetails)??>
    	        <#list columnDetails as columnDetailsList>
                    <#if (columnDetailsList.columnType) == "datetime">
                       $("#"+"${(columnDetailsList.columnName)}") .val(Calendar.printDate(Calendar.parseDate(''<#noparse>${(resultSetList</#noparse>.${(columnDetailsList.tableColumnName)}<#noparse>)}</#noparse>'',false),"%d-%b-%Y"));
                    </#if>
                </#list>    
            </#if>
    <#noparse>	
      	</#list>
      </#if>
    </#noparse>
    
     <#noparse>
      <#if (resultSet)?? && resultSet?has_content>
      	isEdit = 1;
      </#if>
    </#noparse>
    
	hideShowActionButtons();
  });
  
  /**
	* This method should return non null or defined object in order to 
 	* submit the form. return null or undefined in case your custom / additional 
	* validation fails. If you don''t want to perform any validation then either
	* delete the method or return the same object.
	* 
	* If you want to change the name of the form, then implement below method and return 
	* the form name in String
	* 
	*      getFormName();
	*
	* you also want to call below lines to focus and highlight erroneous field
	*      $("#"+fieldName).focus();
	*      $("#"+fieldName).closest("div").parent().effect("highlight", {}, 3000);
	*      showMessage("Issue in input", "warn");
	*/
	function onValidation(a_serializedFormData){
	    
	    return a_serializedFormData;
	}
	
	/**
	* this method is called when there is a successful response from server.
	* you can use this to take other actions in case you need to. Success 
	 * message will be shown before calling this method. 
	 * you can define getSuccessMessage() function and return string for custom success message
	*/
	function onSuccess(a_serverResponse){
	    
	}
	
	/**
	* this method is called when there is any error at server while executing server side 
	 * code.
	* you can use this to take other actions in case you need to.
	* error message will be shown before calling this method
	* you can define getErrorMessage() function and return string for custom success message
	*/
	function onError(jqXHR, exception){
	    
	}

	//Code go back to previous page
	function backToPreviousPage() {
		<#if moduleURL?? && moduleURL?has_content>
			location.href = contextPath+"/view/${(moduleURL)!''''}";
		<#else>
			location.href = contextPath+"/cf/home";
		</#if>
	}
	
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);


REPLACE INTO  jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('f16c057f-09ab-11eb-a027-f48e38ab8cd7', 'system-form-save-query-template', '
  <#noparse>
   <#if isEdit == 1>
  </#noparse>
    ${updateQuery}
  <#noparse>
    <#else>
  </#noparse>
    ${insertQuery}
  <#noparse>
    </#if>
  </#noparse>
', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);



REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('d3cb061d-0743-11eb-9926-e454e805e22f', 'system-listing-template', '<head>
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="<#noparse>${(contextPath)!''''}</#noparse>/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="<#noparse>${(contextPath)!''''}</#noparse>/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/gridutils/gridutils.js"></script> 
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="<#noparse>${(contextPath)!''''}</#noparse>/webjars/1.0/css/starter.style.css" />
</head>

<div class="container">
    <div class="topband">
        <h2 class="title-cls-name float-left">${pageTitle!"Your page title here"}</h2> 
        <div class="float-right">
             <button type="submit" class="btn btn-primary" onclick="upsert(null)"> Create New </button>
            <span onclick="backToWelcomePage();">
                <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
            </span> 
        </div>
        
        <div class="clearfix"></div>        
    </div>
        
    <div id="${gridId}"></div>

    <div id="snackbar"></div>
</div>

<script>
    contextPath = <#noparse>"${(contextPath)!''''}"</#noparse>;
    let primaryKeyDetails = ${primaryKeyObject};
    $(function () {
    //Add all columns that needs to be displayed in the grid
        let colM = [
          <#list gridDetails as gridInfo>
          	<#if (gridInfo.i18nResourceKey)?? && (gridInfo.i18nResourceKey)?has_content>
            	{ title: "<#noparse><@resourceBundle</#noparse> ''${(gridInfo.i18nResourceKey)!''''}'' <#noparse>/></#noparse>", hidden : ${(gridInfo.hidden)?c}, width: 130, dataIndx: "${(gridInfo.column)}", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            <#else>
            	{ title: "${(gridInfo.displayName)!''''}", hidden : ${(gridInfo.hidden)?c}, width: 130, dataIndx: "${(gridInfo.column)}", align: "left", halign: "center",
                filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
            </#if>
          </#list>
            	{ title: "<#noparse><@resourceBundle</#noparse> ''jws.action'' <#noparse>/></#noparse>", width: 50, maxWidth: 145, dataIndx: "action", align: "center", halign: "center", render: manageRecord, sortable: false}
        ];
    
    	let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
    	};
    //System will fecth grid data based on gridId
        let grid = $("#${gridId}").grid({
          gridId: "${gridId}",
          colModel: colM,
          dataModel: dataModel
        });
    
    });
    
    //Customize grid action column. You can add buttons to perform various operations on records like add, edit, delete etc.
    function manageRecord(uiObject) {
        let rowIndx = uiObject.rowIndx;
        return ''<span id="''+rowIndx+''" onclick="upsert(this)" class= "grid_action_icons" title="<#noparse><@resourceBundle</#noparse>''jws.edit''<#noparse>/></#noparse>"><i class="fa fa-pencil"></i></span>''.toString();
    }

    function upsert(element){
    	let redirectURL = contextPath+"/view/${(dfModuleURL)!''''}";
        if(element){
    		let rowData = $( "#${gridId}" ).pqGrid("getData")[element.id];
    	 	<#list primaryKeys as primaryKey>
    			redirectURL += ''?${primaryKey?replace("_", "")}='' + rowData["${primaryKey}"];
        	</#list>
    	}
    	location.href = redirectURL;
    }

    //Code go back to previous page
    function backToWelcomePage() {
        location.href = contextPath+"/cf/home";
        
    }
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW(), 2);
