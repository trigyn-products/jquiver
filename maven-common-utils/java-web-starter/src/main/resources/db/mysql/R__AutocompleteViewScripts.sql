
REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('76e09b33-1061-11eb-a867-f48e38ab8cd7', 'autocomplete-demo', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/typeahead/typeahead.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
</head>
<div class="container">

	<div class="topband">
		<h2 class="title-cls-name float-left">
			<@resourceBundleWithDefault "jws.typeAheadAutocompleteDemo" "TypeAhead Demo"/>
		</h2> 
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
				<label for="flammableState" style="white-space:nowrap"><@resourceBundle "jws.autocomplete" /></label>
				<div class="search-cover">
					<input class="form-control" id="rbAutocomplete" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>
 			</div>
		</div>
	
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.autocompletePrefetch'')}</label>
				<div class="search-cover">
					<input class="form-control" id="rbAutocompletePF" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>
 			</div>
		</div>
		<div class="clearfix"></div>
    </div>
	
    <div class="row">
    
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.autocompleteLocalSotrage'')}</label>
				<div class="search-cover">
					<input class="form-control" id="rbAutocompleteLS" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>					
 			</div>
		</div>
		
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.autocompleteClearText'')}</label>
				<div class="search-cover">			
					<input class="form-control" id="rbAutocompleteCT" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>					
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
				<div class="search-cover">			
					<input class="form-control" id="rbMultiselect" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>				
				<div id="rbMultiselect_selectedOptions"></div>
 			</div>
		</div>
		
		
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<div class="multiselectcount_clear_block">
					<div id="rbMultiselectLS_removeAll" class="pull-right disable_cls">
						<span title="Clear All" class="clearall-cls" onclick="rbMultiselectLS.removeAllElements(''rbMultiselectLS'')" style="pointer-events:none">Clear All</span>
					</div>
					<div id="rbMultiselectLS_count" class="multiselectcount pull-right disable_cls">
						<span title="hide show" onclick="rbMultiselectLS.showHideDataDiv(''rbMultiselectLS_selectedOptions'')" style="pointer-events:none">0</span>
					</div>
				</div>
				
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.multiselectLocalStorage'')}</label>
				<div class="search-cover">				
					<input class="form-control" id="rbMultiselectLS" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>				
				<div id="rbMultiselectLS_selectedOptions"></div>
 			</div>
		</div>
		
	</div>
	
</div>
<script>
contextPath = "${(contextPath)!''''}";
function backToListingPage() {
    location.href = contextPath+"/cf/adl";
}
let autocomplete;
let autocompletePF;
let autocompleteCT;
let multiselect;
$(function () {
    autocomplete = $(''#rbAutocomplete'').autocomplete({
        autocompleteId: "resourcesAutocomplete",
		prefetch : false,
        render: function(item) {
        	var renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === ''''){
        		renderStr = ''<p>''+item.text+''</p>'';
    		}else{
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
        	if(item.emptyMsg == undefined || item.emptyMsg === ''''){
        		renderStr = ''<p>''+item.text+''</p>'';
    		}else{
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
        	if(item.emptyMsg == undefined || item.emptyMsg === ''''){
        		renderStr = ''<p>''+item.text+''</p>'';
    		}else{
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
        	let dependArray = new Array();
        	let dependObj = new Object();
        	dependObj.componentId = "rbMultiselectLS";
        	dependObj.context = rbMultiselectLS;
        	dependArray.push(dependObj);
        	let dependentCompUpdated = multiselect.resetDependent(dependArray);
        	if(dependentCompUpdated === true){ 
	            multiselect.setSelectedObject(item);
	        }
	        $("#rbMultiselect").blur();
	        $("#rbMultiselect").val("");
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
			return "<div class=''jws-rich-autocomplete-multiple''> <div class=''jws-rich-autocomplete-text'' ><label>Language Name: </label>" + item.languageName
			 + "</div> <div class=''jws-rich-autocomplete-id''><label>Language Id: </label>" + item.languageId 
			 + "<div class=''clearfix''></div> </div>";
		}
	});
	
	autocompleteCT = $(''#rbAutocompleteCT'').autocomplete({
        autocompleteId: "resourcesAutocomplete",
		prefetch : true,
		enableClearText: true,
        render: function(item) {
        	var renderStr ='''';
        	if(item.emptyMsg == undefined || item.emptyMsg === ''''){
        		renderStr = ''<p>''+item.text+''</p>'';
    		}else{
        		renderStr = item.emptyMsg;	
    		}	    				        
            return renderStr;
        },
        additionalParamaters: {languageId: 1},
        extractText: function(item) {
            return item.text;
        },
        select: function(item) {
            $("#rbAutocompleteCT").blur();
        }, 	
    });
    
	rbMultiselectLS = $("#rbMultiselectLS").multiselect({
            multiselectItem: $("#rbMultiselectLS_selectedOptions"),
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
                    return "<div class=''jws-rich-autocomplete-multiple''> <div class=''jws-rich-autocomplete-text'' ><label>Language Name: </label>" + item.languageName
			 			+ "</div> <div class=''jws-rich-autocomplete-id''><label>Language Id: </label>" + item.languageId 
			 			+ "<div class=''clearfix''></div> </div>";
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
                $("#rbMultiselectLS").blur();
                rbMultiselectLS.setSelectedObject(item);
            }    

        });
        
        
});
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('7e8438bf-1061-11eb-a867-f48e38ab8cd7', 'autocomplete-listing', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.min.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>     
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" /> 
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script>  
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
</head>
<div class="container">
		<div class="topband">
		<h2 class="title-cls-name float-left">${messageSource.getMessage(''jws.typeAheadAutocomplete'')}</h2> 
		<div class="float-right">
		    Show:<select id="typeSelect" class="typeSelectDropDown" onchange="changeType()" >   
                <option value="0">All</option>                   
                <option value="1" selected>Custom</option>                   
                <option value="2">System</option>                 
            </select>
            <a href="${(contextPath)!''''}/cf/da"> 
				<input id="demoAutocomplete" class="btn btn-primary" name="demoAutocomplete" value="Demo" type="button">
			</a>
			<input id="addAutocompleteDetails" onclick="submitForm()" class="btn btn-primary" name="addAutocompleteDetails" value="Add Autocomplete" type="button">
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
<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
    <input type="hidden" id="entityName" name="entityName" value="jq_autocomplete_details">
    <input type="hidden" id="entityId" name="entityId">
	<input type="hidden" id="moduleName" name="moduleName">
	<input type="hidden" id="moduleType" name="moduleType" value="autocomplete">
	<input type="hidden" id="previousPageUrl" name="previousPageUrl" value="/cf/adl">
</form>
<script>
	contextPath = "${(contextPath)!''''}";
	$(function () {
		$("#typeSelect").each(function () {
	        $(this).val($(this).find("option[selected]").val());
	    });
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
	        { title: "Action", width: 30, minWidth: 115, align: "center", render: editAutocomplete, dataIndx: "action", sortable: false }
		];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "autocompleteId",
        	sortDir: "up",
    	};
	    let grid = $("#divAutocompleteGrid").grid({
	      gridId: "autocompleteListingGrid",
	      colModel: colM,
          dataModel: dataModel,
          additionalParameters: {"cr_autocompleteTypeId":"str_1"}
	  });
	});
	
	function changeType() {
        var type = $("#typeSelect").val();   
        let postData;
        if(type == 0) {
            postData = {gridId:"autocompleteListingGrid"}
        } else {
            let typeCondition = "str_"+type;       
   
            postData = {gridId:"autocompleteListingGrid"
                    ,"cr_autocompleteTypeId":typeCondition
                    }
        }
        
        let gridNew = $( "#divAutocompleteGrid" ).pqGrid();
        gridNew.pqGrid( "option", "dataModel.postData", postData);
        gridNew.pqGrid( "refreshDataAndView" );  
    }
        
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
		const revisionCount = uiObject.rowData.revisionCount;
		
		let actionElement;
		actionElement = ''<span id="''+autocompleteId+''" onclick="submitForm(this)" class= "grid_action_icons"><i class="fa fa-pencil" title=""></i></span>'';
		if(revisionCount > 1){
			actionElement = actionElement + ''<span id="''+autocompleteId+''_entity" name="''+autocompleteId+''" onclick="submitRevisionForm(this)" class= "grid_action_icons"><i class="fa fa-history"></i></span>''.toString();
		}else{
			actionElement = actionElement + ''<span class= "grid_action_icons disable_cls"><i class="fa fa-history"></i></span>''.toString();
		}
		return actionElement;
		
	}

    function submitForm(sourceElement) {
		let moduleId = "";
		if(sourceElement !== undefined){
			moduleId = sourceElement.id
		}
      	$("#acId").val(moduleId);
      	$("#formACRedirect").submit();
    }
    
	function submitRevisionForm(sourceElement) {
		let selectedId = sourceElement.id.split("_")[0];
		let moduleName = $("#"+sourceElement.id).attr("name")
      	$("#entityId").val(selectedId);
		$("#moduleName").val(moduleName);
      	$("#revisionForm").submit();
    }
	
	function backToWelcomePage() {
		location.href = contextPath+"/cf/home";
	}
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('85f44645-1061-11eb-a867-f48e38ab8cd7', 'autocomplete-manage-details', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/monaco/require.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/monaco/min/vs/loader.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
</head>

	<div class="container">
		<div class="row topband">
			<div class="col-8">
				<#if (autocompleteVO.autocompleteId)??>
				    <h2 class="title-cls-name float-left">Edit Autocomplete</h2> 
		        <#else>
		            <h2 class="title-cls-name float-left">Add Autocomplete</h2>  
		        </#if>
		    </div>
	    
	        <div class="col-4">    
				<#if (autocompleteVO.autocompleteId)?? && (autocompleteVO.autocompleteId)?has_content>	 
			        <#assign ufAttributes = {
			            "entityType": "TypeAhead Autocomplete",
			            "entityId": "autoId",
			            "entityName": "autoId"
			        }>
			        <@templateWithParams "user-favorite-template" ufAttributes />
		        </#if>
		     </div>
		
			<div class="clearfix"></div>		
		</div>
	
    <form id="autocompleteForm" method="post" >
	<div id="errorMessage" class="alert errorsms alert-danger alert-dismissable" style="display:none"></div>
	  <div class="row">
	    <div class = "col-6">
			<div class="col-inner-form full-form-fields">
	        <label for="autoId"><span class="asteriskmark">*</span>Autocomplete Id </label>
	        <input id="autoId" name="autocompleteId" onkeyup="addEditAutocomplete.hideErrorMessage();" class="form-control" type="text" value="${(autocompleteVO.autocompleteId)!''""''}">
			</div>
	    </div>
	    <div class="col-6">
			<div class="col-inner-form full-form-fields">
	        <label for="autoDesc">Autocomplete Description </label>
	        <input name="autocompleteDesc" class="form-control" type="text" value="${(autocompleteVO.autocompleteDesc)!''""''}">
			</div>
	    </div>

		
		<#if !(autocompleteVO.autocompleteId)?? && !(autocompleteVO.autocompleteId)?has_content>   
	         <div class="col-6">
				<div class="col-inner-form full-form-fields">
	                <label for="flammableState" style="white-space:nowrap">Autocomplete Table</label>
	                <div class="search-cover">
	                    <input class="form-control" id="tableAutocomplete" type="text">
	                  	<i class="fa fa-search" aria-hidden="true"></i>
	                </div>
	               	<input type="hidden" id="autocompleteTable" name="autocompleteTable">
	           	</div>
			</div>
		</#if>
			
	</div>
	
		<div id="ftlParameterDiv" class="col-12 method-sign-info-div">
			<h3 class="titlename method-sign-info">
		    	<i class="fa fa-lightbulb-o" aria-hidden="true"></i><label for="ftlParameter">SQL Parameters</label>
	    	</h3>
			<span id="ftlParameter">loggedInUserName, searchText, additionalParamaters {}<span>
		</div>
		
	<div class="row margin-t-b">
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
	
	
   <input type="hidden" name="autocompleteQuery" id="acSelectQuery">
   
  </form>
  <div class="row">
	<div class="col-3">   
  		<input id="moduleId" value="91a81b68-0ece-11eb-94b2-f48e38ab9348" name="moduleId" type="hidden">
  		<@templateWithoutParams "role-autocomplete"/>
  	</div>
  </div>
  <div class="row margin-t-10">
			<div class="col-12">
				<div class="float-right">
					<div class="btn-group dropup custom-grp-btn">
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
	let tableAutocomplete;
	
	$(function() {
	    addEditAutocomplete = new AddEditAutocomplete();
	    addEditAutocomplete.loadAutocompletDetails();
		if(typeof getSavedEntity !== undefined && typeof getSavedEntity === "function"){
			getSavedEntity();
		}
	    
	    savedAction("autocomplete-manage-details", acId);
	    hideShowActionButtons();
	    
	    if(acId==""){
            let defaultAdminRole= {"roleId":"ae6465b3-097f-11eb-9a16-f48e38ab9348","roleName":"ADMIN"};
            multiselect.setSelectedObject(defaultAdminRole);
        }else{
            addEditAutocomplete.getEntityRoles();
        }
        
        <#if !(autocompleteVO.autocompleteId)?? && !(autocompleteVO.autocompleteId)?has_content>
	        tableAutocomplete = $("#tableAutocomplete").autocomplete({
		        autocompleteId: "table-autocomplete",
		        prefetch : true,
		        render: function(item) {
		            var renderStr ="";
		            if(item.emptyMsg == undefined || item.emptyMsg === ""){
		                renderStr = "<p>"+item.tableName+"</p>";
		            }else{
		                renderStr = item.emptyMsg;    
		            }                                
		            return renderStr;
		        },
		        additionalParamaters: {},
		        extractText: function(item) {
		            return item.tableName;
		        },
		        select: function(item) {
		            $("#tableAutocomplete").blur();
		            $("#autocompleteTable").val(item.tableName);
		            addEditAutocomplete.createQuery(item.tableName);
		        },     
		    });
		</#if>
	    
	});
</script>
<script src="${(contextPath)!''''}/webjars/1.0/autocomplete/addEditAutocomplete.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW(), 2);


REPLACE INTO jq_grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, grid_type_id) VALUES ("autocompleteListingGrid", 'Autocomplete Details Listing', 'Autocomplete Details Listing', 'autocompleteListing', 'autocompleteId,autocompleteDescription,acQuery,autocompleteTypeId', 2);

REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id) VALUES
('resourcesAutocomplete', 'List all the keys text resource bundle table', 'select resource_key as `key`, language_id as languageId, `text` as `text` from jq_resource_bundle where language_id = :languageId and `text` LIKE CONCAT("%", :searchText, "%")', 2);