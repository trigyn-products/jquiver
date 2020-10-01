
REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES
(UUID(), 'autocomplete-demo', '<head>
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
		<h2 class="title-cls-name float-left">Type Demo</h2> 
		<div class="float-right">
			<span onclick="backToListingPage();">
				<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
			</span>	
		</div>
		
		<div class="clearfix"></div>		
	</div>

    <div class="row">
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">Autocomplete</label>
				<input class="form-control" id="rbAutocomplete" type="text">
 			</div>
		</div>
	
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
				
				<label for="flammableState" style="white-space:nowrap">Multiselect</label>
				<input class="form-control" id="rbMultiselect" type="text">
			
				<div id="rbMultiselect_selectedOptions"></div>
 			</div>
		</div>
		
		<div id="rbMultiselect_deleteConfirmation"></div>
    </div>
</div>
<script>
//const contextPath = "${(contextPath)!''''}";
function backToListingPage() {
    location.href = "/cf/adl";
}
let autocomplete;
let multiselect;
$(function () {
    autocomplete = $(''#rbAutocomplete'').autocomplete({
        autocompleteId: "resourcesAutocomplete",
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
});
</script>', 'admin', 'admin', NOW());



REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'autocomplete-listing', '<head>
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
		<h2 class="title-cls-name float-left">Autocomplete Master</h2> 
		<div class="float-right">
		    <a href="${(contextPath)!''''}/cf/da"> 
				<input id="demoAutocomplete" class="btn btn-primary" name="demoAutocomplete" value="Demo" type="button">
			</a>
			<a href="${(contextPath)!''''}/cf/aea"> 
				<input id="addAutocompleteDetails" class="btn btn-primary" name="addAutocompleteDetails" value="Add Autocomplete Details" type="button">
			</a>
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

function editAutocomplete(uiObject) {
	const autocompleteId = uiObject.rowData.autocompleteId;
  	return ''<span id="''+autocompleteId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil"></i></span>''.toString();
}

function submitForm(element) {
  $("#acId").val(element.id);
  $("#formACRedirect").submit();
}

function backToWelcomePage() {
	location.href = contextPath+"/cf/home";
}
</script>', 'admin', 'admin', NOW());


REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date) VALUES 
(UUID(), 'autocomplete-manage-details', '<head>
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
		<h2 class="title-cls-name float-left">Update Autocomplete Details</h2> 
		<div class="float-right">
			 
		<span onclick="addEditAutocomplete.backToListingPage();">
  		  <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
  		 </span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
	
    <form id="autocompleteForm" method="post" >
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
	
	
	<div class="row margin-t-b">
	<div class="col-12">
		 <div class="float-right">
		<input id="addTemplate" class="btn btn-primary" name="addTemplate" value="Save" type="button" onclick="addEditAutocomplete.saveAutocompleteDetail();">
		<span onclick="addEditAutocomplete.backToListingPage();">
			<input id="cancelBtn" class="btn btn-secondary" name="cancelBtn" value="Cancel" type="button">
		</span>	
		</div>
	</div>
	</div>
   <input type="hidden" name="autoCompleteSelectQuery" id="acSelectQuery">
   
  </form>
  
 	<div id="snackbar"></div>
 	
<textarea id="sqlContentDiv" style="display: none">
	${(autocompleteVO.autocompleteQuery)!""}
&lt;/textarea&gt;
  
<script>
	contextPath = "${(contextPath)!''''}";
	const acId = "${(autocompleteVO.autocompleteId)!''''}";

	let addEditAutocomplete;
	$(function() {
	    addEditAutocomplete = new AddEditAutocomplete();
	    addEditAutocomplete.loadAutocompletDetails();
	});
</script>
<script src="/webjars/1.0/autocomplete/addEditAutocomplete.js"></script>', 'admin', 'admin',NOW());

DROP PROCEDURE IF EXISTS autocompleteListing;
CREATE PROCEDURE autocompleteListing (autocompleteId varchar(100), autocompleteDescription varchar(500), forCount INT, limitFrom INT, limitTo INT,sortIndex VARCHAR(100),sortOrder VARCHAR(20))
BEGIN
  SET @resultQuery = ' select ac_id as autocompleteId, ac_description as autocompleteDescription, ac_select_query as acQuery ';
  SET @fromString  = ' FROM autocomplete_details ';
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

REPLACE INTO grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names) VALUES ("autocompleteListingGrid", 'Autocomplete Details Listing', 'Autocomplete Details Listing', 'autocompleteListing', 'autocompleteId,autocompleteDescription');

REPLACE INTO autocomplete_details (ac_id, ac_description, ac_select_query) VALUES
('resourcesAutocomplete', 'List all the keys text resource bundle table', 'select resource_key as `key`, language_id as languageId, `text` as `text` from resource_bundle where language_id = :languageId and `text` LIKE CONCAT("%", :searchText, "%")');