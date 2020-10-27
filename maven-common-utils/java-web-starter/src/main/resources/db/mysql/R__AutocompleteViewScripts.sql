
REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('76e09b33-1061-11eb-a867-f48e38ab8cd7', 'autocomplete-demo', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.js"></script>
<script src="/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.min.js"></script>
<script src="/webjars/1.0/typeahead/typeahead.js"></script>
<link rel="stylesheet" href="/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>
<div class="container">

	<div class="topband">
		<h2 class="title-cls-name float-left"><@resourceBundleWithDefault "jws.typeAheadAutocompleteDemo" "TypeAhead Demo"/></h2> 
		<div class="float-right">
			<span onclick="backToListingPage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>

	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
    <div class="row">
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.autocomplete'')}</label>
				<input class="form-control" id="rbAutocomplete" type="text">
 			</div>
		</div>
	
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.autocompletePrefetch'')}</label>
				<input class="form-control" id="rbAutocompletePF" type="text">
 			</div>
		</div>
		<div class="clearfix"></div>
    </div>
	
    <div class="row">
    
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.autocompleteLocalSotrage'')}</label>
				<input class="form-control" id="rbAutocompleteLS" type="text">
 			</div>
		</div>
	</div>
	
	<div class="row">	
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<div class="multiselectcount_clear_block">
					<div id="rbMultiselect_removeAll" class="pull-right disable_cls">
						<span title="Clear All" class="clearall-cls" onclick="multiselect.removeAllElements(''rbMultiselect'')" style="pointer-events:none">Clear All</span>
					</div>
					<div id="rbMultiselect_count" class="multiselectcount pull-right disable_cls">
						<span title="hide show" onclick="multiselect.showHideDataDiv(''rbMultiselect_selectedOptions'')" style="pointer-events:none">0</span>
					</div>
				</div>
				
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.multiselect'')}</label>
				<input class="form-control" id="rbMultiselect" type="text">
			
				<div id="rbMultiselect_selectedOptions"></div>
 			</div>
		</div>
		
		
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<div class="multiselectcount_clear_block">
					<div id="languages_removeAll" class="pull-right disable_cls">
						<span title="Clear All" class="clearall-cls" onclick="languageSelector.removeAllElements(''languages'')" style="pointer-events:none">Clear All</span>
					</div>
					<div id="languages_count" class="multiselectcount pull-right disable_cls">
						<span title="hide show" onclick="languageSelector.showHideDataDiv(''languages_selectedOptions'')" style="pointer-events:none">0</span>
					</div>
				</div>
				
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.multiselectLocalStorage'')}</label>
				<input class="form-control" id="languages" type="text">
			
				<div id="languages_selectedOptions"></div>
 			</div>
		</div>
		
	</div>
	
</div>
<script>
//const contextPath = "${(contextPath)!''''}";
function backToListingPage() {
    location.href = "/cf/adl";
}
let autocomplete;
let autocompletePF;
let multiselect;
$(function () {
    autocomplete = $(''#rbAutocomplete'').autocomplete({
        autocompleteId: "resourcesAutocomplete",
		prefetch : false,
        render: function(item) {
        	var renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === '''')
    		{
        		renderStr = ''<p>''+item.text+''</p>'';
    		}
        	else
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
            $("#rbAutocomplete").blur();
        }, 	
    }, {key: "jws.action", languageId: 1, text: "Action"});
    
	autocompletePF = $(''#rbAutocompletePF'').autocomplete({
        autocompleteId: "resourcesAutocomplete",
		prefetch : true,
        render: function(item) {
        	var renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === '''')
    		{
        		renderStr = ''<p>''+item.text+''</p>'';
    		}
        	else
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
            $("#rbAutocompletePF").blur();
        }, 	
    });
	
    multiselect = $(''#rbMultiselect'').multiselect({
        autocompleteId: "resourcesAutocomplete",
        multiselectItem: $(''#rbMultiselect_selectedOptions''),
        render: function(item) {
        	var renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === '''')
    		{
        		renderStr = ''<p>''+item.text+''</p>'';
    		}
        	else
    		{
        		renderStr = item.emptyMsg;	
    		}	    				        
            return renderStr;
        },
        additionalParamaters: {languageId: 1},
        extractText: function(item) {
            return item.text;
        },
        selectedItemRender: function(item){
            return item.text;
        },
        select: function(item) {
            $("#rbMultiselect").blur();
            multiselect.setSelectedObject(item);
        },	
    }, [{key: "jws.action", languageId: 1, text: "Action"}]);
    
    
    $("#rbAutocompleteLS").richAutocomplete({
		items: [{
			languageName: "English",
			languageId: 1
		}, {
			languageName: "French",
			languageId: 2
		}, {
			languageName: "Hindi",
			languageId: 3
		}],
		extractText: function(item) {
			return item.languageName;
		},
		filter: function(items, searchTerm) {
			return items.filter(function(item) {
				return item.languageName.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1;
			});
		},
		render: function(item) {
			return "<p>" + item.languageName + "</p><small>" + item.languageId + "</small>";
		}
	});
	
	languageSelector = $("#languages").multiselect({
            multiselectItem: $("#languages_selectedOptions"),
            paging:false,
            items: [{
                languageName: "English",
                languageId: 1
            }, {
                languageName: "French",
                languageId: 2
            }, {
                languageName: "Hindi",
                languageId: 3
            }],

            render: function(item) {
                var renderStr ="";
                if(item.emptyMsg == undefined || item.emptyMsg === ''''){
                    renderStr = "<p>"+item.languageName+"</p>";
                }else{
                    renderStr = item.emptyMsg;    
                }                                
                return renderStr;
            },

            filter: function(items, searchTerm) {
                return items.filter(function(item) {
                	return item.languageName.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1;
                });
            },
            extractText: function(item) {
                return item.languageName;
            },

            selectedItemRender: function(item){
                return item.languageName;
            },

            select: function(item) {
                $("#languages").blur();
                languageSelector.setSelectedObject(item);
            }    

        });
        
        
});
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('7e8438bf-1061-11eb-a867-f48e38ab8cd7', 'autocomplete-listing', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/1.0/pqGrid/pqgrid.min.js"></script>     
<link rel="stylesheet" href="/webjars/1.0/pqGrid/pqgrid.min.css" /> 
<script src="/webjars/1.0/gridutils/gridutils.js"></script>  
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>
<div class="container">
		<div class="topband">
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.typeAheadAutocomplete'')}</h2> 
		<div class="float-right">
		    <a href="${(contextPath)!''''}/cf/da"> 
				<input id="demoAutocomplete" class="btn btn-primary" name="demoAutocomplete" value="Demo" type="button">
			</a>
			<input id="addAutocompleteDetails" onclick="submitForm()" class="btn btn-primary" name="addAutocompleteDetails" value="Add Autocomplete Details" type="button">
			<span onclick="backToWelcomePage();">
  		  <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
  		 </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="divAutocompleteGrid"></table>

		<div id="snackbar"></div>
</div>


<form action="${(contextPath)!''''}/cf/aea" method="GET" id="formACRedirect">
	<input type="hidden" id="acId" name="acId">
</form>
<script>
	contextPath = "${(contextPath)!''''}";
	$(function () {
		let formElement = $("#formACRedirect")[0].outerHTML;
		let formDataJson = JSON.stringify(formElement);
		sessionStorage.setItem("autocomplete-manage-details", formDataJson);
		
		let colM = [
	        { title: "Autocomplete Id", width: 130, align: "center", dataIndx: "autocompleteId", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Autocomplete Description", width: 100, align: "center",  dataIndx: "autocompleteDescription", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Autocomplete Query", width: 160, align: "center", dataIndx: "acQuery", align: "left", halign: "center",
	        filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Action", width: 30, align: "center", render: editAutocomplete, dataIndx: "action" }
		];
	    let grid = $("#divAutocompleteGrid").grid({
	      gridId: "autocompleteListingGrid",
	      colModel: colM
	  });
	});
	
	function autocompleteType(uiObject){
		const autocompleteTypeId = uiObject.rowData.autocompleteTypeId;
		if(autocompleteTypeId === 1){
			return "Default";
		}else{
			return "System";
		}
	}
	
	function editAutocomplete(uiObject) {
		const autocompleteId = uiObject.rowData.autocompleteId;
	  	return ''<span id="''+autocompleteId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
	}

    function submitForm(sourceElement) {
		let moduleId = "";
		if(sourceElement !== undefined){
			moduleId = sourceElement.id
		}
      	$("#acId").val(moduleId);
      	$("#formACRedirect").submit();
    }
    
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('85f44645-1061-11eb-a867-f48e38ab8cd7', 'autocomplete-manage-details', '<head>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="/webjars/1.0/monaco/require.js"></script>
<script src="/webjars/1.0/monaco/min/vs/loader.js"></script>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
</head>

	<div class="container">
		<div class="topband">
		<#if (autocompleteVO.autocompleteId)??>
		    <h2 class="title-cls-name float-left">Edit Autocomplete Details</h2> 
        <#else>
            <h2 class="title-cls-name float-left">Add Autocomplete Details</h2>  
        </#if>  
		<div class="float-right">
			 
		<span onclick="addEditAutocomplete.backToListingPage();">
  		  <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
  		 </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
	
    <form id="autocompleteForm" method="post" >
	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	  <div class="row">
	    <div class = "col-6">
			<div class="col-inner-form full-form-fields">
	        <label for="autoId"><span class="asteriskmark">*</span>Autocomplete Id </label>
	        <input id="autoId" name="autoCompleteId" class="form-control" type="text" value="${(autocompleteVO.autocompleteId)!''""''}">
			</div>
	    </div>
	    <div class="col-6">
			<div class="col-inner-form full-form-fields">
	        <label for="autoDesc">Autocomplete Description </label>
	        <input name="autoCompleteDescription" class="form-control" type="text" value="${(autocompleteVO.autocompleteDesc)!''""''}">
			</div>
	    </div>
	</div>
	
	<div class="row">
	<div class="col-12">
		<div class="sql_script">
			<div class="grp_lblinp">
			    <div id="sqlContainer" class="ace-editor-container">
	                <div id="sqlEditor" class="ace-editor"></div>
					 
                </div>            
            </div>
    </div>
	</div>
	</div>
	
	
   <input type="hidden" name="autoCompleteSelectQuery" id="acSelectQuery">
   
  </form>
  <input id="moduleId" value="91a81b68-0ece-11eb-94b2-f48e38ab9348" name="moduleId" type="hidden">
    <@templateWithoutParams "role-autocomplete"/>
  <div class="row margin-t-10">
			<div class="col-12">
				<div class="float-right">
					<div class="btn-group dropdown custom-grp-btn">
			            <div id="savedAction">
		    	            <button type="button" id="saveAndReturn" class="btn btn-primary" onclick="typeOfAction(''autocomplete-manage-details'', this, addEditAutocomplete.saveAutocompleteDetail.bind(addEditAutocomplete), addEditAutocomplete.backToListingPage);">${messageSource.getMessage("jws.saveAndReturn")}</button>
		                </div>
		        	<button id="actionDropdownBtn" type="button" class="btn btn-primary dropdown-toggle panel-collapsed" onclick="actionOptions();"></button>
		            	<div class="dropdown-menu action-cls"  id="actionDiv">
		                	<ul class="dropdownmenu">
		                    	<li id="saveAndCreateNew" onclick="typeOfAction(''autocomplete-manage-details'', this, addEditAutocomplete.saveAutocompleteDetail.bind(addEditAutocomplete), addEditAutocomplete.backToListingPage);">${messageSource.getMessage("jws.saveAndCreateNew")}</li>
		                        <li id="saveAndEdit" onclick="typeOfAction(''autocomplete-manage-details'', this, addEditAutocomplete.saveAutocompleteDetail.bind(addEditAutocomplete), addEditAutocomplete.backToListingPage);">${messageSource.getMessage("jws.saveAndEdit")}</li>
		                    </ul>
	                    </div> 
	                </div>
					<span onclick="addEditAutocomplete.backToListingPage();">
						<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
					</span> 
				</div>
			</div>
	</div>
 	
<textarea id="sqlContentDiv" style="display: none">
	${(autocompleteVO.autocompleteQuery)!""}
</textarea>
  
<script>
	contextPath = "${(contextPath)!''''}";
	const acId = "${(autocompleteVO.autocompleteId)!''''}";

	let addEditAutocomplete;
	$(function() {
	    addEditAutocomplete = new AddEditAutocomplete();
	    addEditAutocomplete.loadAutocompletDetails();
	    savedAction("autocomplete-manage-details", acId);
	    hideShowActionButtons();
	     if(acId==""){
            let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
            multiselect.setSelectedObject(defaultAdminRole);
        }else{
            addEditAutocomplete.getEntityRoles();
        }
	});
</script>
<script src="/webjars/1.0/autocomplete/addEditAutocomplete.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW(), 2);

DROP PROCEDURE IF EXISTS autocompleteListing;
CREATE PROCEDURE autocompleteListing (autocompleteId varchar(100), autocompleteDescription varchar(500), autocompleteTypeId INT(11) ,forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' select au.ac_id as autocompleteId, au.ac_description as autocompleteDescription, au.ac_select_query as acQuery ';
  SET @resultQuery = CONCAT(@resultQuery, ', au.ac_type_id AS autocompleteTypeId ');
  SET @fromString  = ' FROM autocomplete_details au ';
  SET @whereString = ' ';
  SET @limitString = CONCAT(' limit ','',CONCAT(limitFrom,',',limitTo));
  
  IF NOT sortIndex IS NULL THEN
      SET @orderBy = CONCAT(' ORDER BY ' ,sortIndex,' ',sortOrder);
    ELSE
      SET @orderBy = CONCAT(' ORDER BY ac_id DESC');
  END IF;
  
	IF forCount=1 THEN
  	SET @queryString=CONCAT('select count(*) from ( ',@resultQuery, @fromString, @whereString, @orderBy,' ) as cnt');
  ELSE
  	SET @queryString=CONCAT(@resultQuery, @fromString, @whereString, @orderBy, @limitString);
  END IF;

 PREPARE stmt FROM @queryString;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END;

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) VALUES ("autocompleteListingGrid", 'Autocomplete Details Listing', 'Autocomplete Details Listing', 'autocompleteListing', 'autocompleteId,autocompleteDescription,autocompleteTypeId', 2);

REPLACE INTO autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id) VALUES
('resourcesAutocomplete', 'List all the keys text resource bundle table', 'select resource_key as `key`, language_id as languageId, `text` as `text` from resource_bundle where language_id = :languageId and `text` LIKE CONCAT("%", :searchText, "%")', 2);