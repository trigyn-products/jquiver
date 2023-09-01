
REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('76e09b33-1061-11eb-a867-f48e38ab8cd7', 'autocomplete-demo', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
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
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.multiselect'')}</label>
				<div class="search-cover">			
					<input class="form-control" id="bsMultiselect" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>				
 			</div>
		</div>
		
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.dependentMultiselect'')}</label>
				<div class="search-cover">			
					<input class="form-control" id="rbMultiselect" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>				
 			</div>
		</div>
		
		
		<div class="col-6">
			<div class="col-inner-form full-form-fields">
				<label for="flammableState" style="white-space:nowrap">${messageSource.getMessage(''jws.multiselectLocalStorage'')}</label>
				<div class="search-cover">				
					<input class="form-control" id="rbMultiselectLS" type="text">
					<i class="fa fa-search" aria-hidden="true"></i>
            	</div>				
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
        	let renderStr ='''';
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
        	let renderStr ='''';
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
	
    
	autocompleteCT = $(''#rbAutocompleteCT'').autocomplete({
        autocompleteId: "resourcesAutocomplete",
		prefetch : true,
		enableClearText: true,
        render: function(item) {
        	let renderStr ='''';
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
        
    $("#rbAutocompleteLS").richAutocomplete({
		items: [{
			key: "English",
			languageName: "English",
			languageId: 1
		}, {
			key: "French",
			languageName: "French",
			languageId: 2
		}, {
			key: "Hindi",
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
	
	basicMultiselect = $(''#bsMultiselect'').multiselect({
        autocompleteId: "resourcesAutocomplete",
        render: function(item) {
        	let renderStr ='''';
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
        	$("#bsMultiselectLS").blur();
            basicMultiselect.setSelectedObject(item);
        },	
    });
    

    multiselect = $(''#rbMultiselect'').multiselect({
        autocompleteId: "resourcesAutocomplete",
        render: function(item) {
        	let renderStr ='''';
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
    
	rbMultiselectLS = $("#rbMultiselectLS").multiselect({
            paging:false,
            items: [{
            	key: "English",
                languageName: "English",
                languageId: 1
            }, {
            	key: "French",
                languageName: "French",
                languageId: 2
            }, {
            	key: "Hindi",
                languageName: "Hindi",
                languageId: 3
            }],

            render: function(item) {
                let renderStr ="";
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
<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script>


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
				<input id="demoAutocomplete" class="btn btn-primary" value="Demo" type="button">
			</a>
			<a href="${(contextPath)!''''}/cf/ad"> 
				<input id="additionalDataSource" class="btn btn-primary" value="Additional Datasource" type="button">
			</a>
			<input id="addAutocompleteDetails" onclick="submitForm()" class="btn btn-primary" value="Add Autocomplete" type="button">
			<span onclick="backToWelcomePage();">
  		  		<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
  		 	</span>	
		</div>
		
		<div class="clearfix"></div>		
		</div>
		
		<div id="deleteHeader"></div>
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
	        { title: "Description", width: 150, align: "center",  dataIndx: "autocompleteDescription", align: "left", halign: "center",
	        	filter: { type: "textbox", condition: "contain", listeners: ["change"]} },
	        { title: "Last Updated Date",width: 100, align: "center", dataIndx: "lastUpdatedTs", align: "left", halign: "center", render: formatLastUpdatedDate },
	        { title: "Action", width: 30, maxWidth: 145, align: "left", render: editAutocomplete, dataIndx: "action", sortable: false }
		];
		let dataModel = {
        	url: contextPath+"/cf/pq-grid-data",
        	sortIndx: "lastUpdatedTs",
        	sortDir: "down",
    	};
	    let grid = $("#divAutocompleteGrid").grid({
	      gridId: "autocompleteListingGrid",
	      colModel: colM,
          dataModel: dataModel,
          additionalParameters: {"cr_autocompleteTypeId":"str_1"}
	  });
	});
	
	function changeType() {
        let type = $("#typeSelect").val();   
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
	
	function formatLastUpdatedDate(uiObject){
        const lastUpdatedTs = uiObject.rowData.lastUpdatedTs;
        return formatDate(lastUpdatedTs);
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
        if(uiObject.rowData.autocompleteTypeId == 1) {
        <#if loggedInUserRoleList?? && loggedInUserRoleList?size gt 0>
        	<#list loggedInUserRoleList as loggedInUserRole>
            	<#if (loggedInUserRole == "ADMIN")> 
	        			actionElement += ''<span onclick=\\''openDeletConfirmation(\"divAutocompleteGrid",\"''+autocompleteId+''\",\"91a81b68-0ece-11eb-94b2-f48e38ab9348\")\\'' class= "grid_action_icons" title="Delete"><i class="fa fa-trash "></i></span>''.toString();
	        			<#break>
        		</#if>
        	</#list>
        </#if>
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
<script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/monaco/require.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/monaco/min/vs/loader.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/common/jQuiverCommon.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/markdown/highlight/github.min.css" />
<script src="${(contextPath)!''''}/webjars/1.0/markdown/highlight/highlight.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">
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
	        <input id="autoId" name="autocompleteId" onkeyup="addEditAutocomplete.hideErrorMessage();" onkeydown="addEditAutocomplete.updateOldVal()" onkeyup="addEditAutocomplete.updateAutocompleTemplate()" class="form-control" type="text" value="${(autocompleteVO.autocompleteId)!''""''}">
			</div>
	    </div>
	    <div class="col-6">
			<div class="col-inner-form full-form-fields">
	        <label for="autoDesc">Autocomplete Description </label>
	        <input name="autocompleteDesc" class="form-control" type="text" value="${(autocompleteVO.autocompleteDesc)!''""''}">
			</div>
	    </div>

		<div class="col-6">
			<div class="col-inner-form full-form-fields">
	        	<label for="flammableState" style="white-space:nowrap">Datasource</label>
	        	<select id="dataSource" name="dataSourceId" class="form-control" onchange="updateDataSource()">
	        		<option id="defaultConnection" value="" data-product-name="default">Default Connection</option>
	        	</select>
           	</div>
		</div>
			
		<#if !(autocompleteVO.autocompleteId)?? && !(autocompleteVO.autocompleteId)?has_content>   
	         <div id="tableAutocompleteDiv" class="col-6" >
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
			<span id="ftlParameter">loggedInUserName, searchText, additionalParamaters{}, startIndex, pageSize<span>
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
	
   <input type="hidden" name="autocompleteQuery" id="acSelectQuery">
   <input type="hidden" id="dataSourceId" value="${(autocompleteVO.dataSourceId)!''''}">
   
  </form>
  <div class="row">
	<div class="col-3">   
  		<input id="moduleId" value="91a81b68-0ece-11eb-94b2-f48e38ab9348" name="moduleId" type="hidden">
  		<@templateWithoutParams "role-autocomplete"/>
  	</div>
  </div>
  
  <br>
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
	let oldAutocompleteId;
	
	$(function() {
	    $("#tabs").tabs();
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
	        getAllDatasource(0);
	        tableAutocomplete = $("#tableAutocomplete").autocomplete({
		        autocompleteId: "table-autocomplete",
		        prefetch : true,
		        render: function(item) {
		            let renderStr ="";
		            if(item.emptyMsg == undefined || item.emptyMsg === ""){
		                renderStr = "<p>"+item.tableName+"</p>";
		            }else{
		                renderStr = item.emptyMsg;    
		            }                                
		            return renderStr;
		        },
		        additionalParamaters: {},
		        requestParameters: {
		        	dbProductName: $("#dataSource").find(":selected").data("product-name"),
		        },
		        extractText: function(item) {
		            return item.tableName;
		        },
		        select: function(item) {
		            $("#tableAutocomplete").blur();
		            $("#autocompleteTable").val(item.tableName);
		            addEditAutocomplete.createQuery();
		        },
		        resetAutocomplete: function(){ 
		        	$("#autocompleteTable").val("");
		        	addEditAutocomplete.createQuery();
		        }     
		    });
		<#else>
			getAllDatasource(1);
			$("#autoId").attr("readonly", "readonly");
		</#if>
		loadDefaultTab("autocomplete-default-template", addEditAutocomplete.updateAutocompleTemplate);
	    $("a[href=''#htmlContent'']").click();
	});
	
</script>
<script src="${(contextPath)!''''}/webjars/1.0/autocomplete/addEditAutocomplete.js"></script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com',NOW(), 2);


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES
('7748fece-e7b9-41fc-a777-74fb3ab28be5', 'autocomplete-default-template', '<#if selectedTab == "htmlContent">
```HTML
<#noparse>
<!-- HTML Header -->
<script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.min.js"></script>
<script src="${(contextPath)!''''}/webjars/1.0/typeahead/typeahead.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />

<!-- Start - Autocomplete HTML Body -->
<div class="col-6">
    <div class="col-inner-form full-form-fields">
        <label for="flammableState" style="white-space:nowrap"><@resourceBundleWithDefault "jws.autocomplete" "Autocomplete"/></label>
        <div class="search-cover">
            <input class="form-control" id="autocompleteId" type="text">
            <i class="fa fa-search" aria-hidden="true"></i>
        </div>
    </div>
</div>
<!-- End - Autocomplete HTML Body -->

<!-- Start - Multiselect HTML Body -->
<div class="col-6">
    <div class="col-inner-form full-form-fields">
        <label for="flammableState" style="white-space:nowrap"><@resourceBundleWithDefault "jws.multiselect" "Multiselect"/></label>
        <div class="search-cover">                
            <input class="form-control" id="bsMultiselect" type="text">
            <i class="fa fa-search" aria-hidden="true"></i>
        </div>    
    </div>
</div>
<!-- End - Multiselect HTML Body -->

</#noparse>
```

<#elseif selectedTab == "jsContent">
```JavaScript
<#noparse>
/*Start - Autocomplete code*/
contextPath = "${contextPath!''''}";
let autocomplete;

$(function () {
    autocomplete = $("#autocompleteId").autocomplete({
        autocompleteId: "autocompleteId",
        pageSize: 10,//Default page size is 10
        prefetch : false,
        render: function(item) {
            let renderStr ='''';
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
        requestParameters: {},
        extractText: function(item) {
            return item.text;
        },
        select: function(item) {
            $("#autocompleteId").blur();
        },     
        resetAutocomplete: function(){ 
            //This function will be executed onblur or when user click on clear text button
            //Code to reset dependent JavaScript variables, input fields etc.
        }, 
    }, {key: "jws.action", languageId: 1, text: "Action"});

    //You can set default value using setSelectedObject function
    autcomplete.setSelectedObject({key: "jws.action", languageId: 1, text: "Action"});
});

//User can reset any autocomplete component by calling resetAutocomplete function
autocomplete.resetAutocomplete();
/*End - Autocomplete code*/

/*Start - Multiselect code*/

<script>
let basicMultiselect;
$(function () {
    basicMultiselect = $("#bsMultiselect").multiselect({
        autocompleteId: "resourcesAutocomplete",
        enableClearAll: true,//true enables Clear All functionality
        render: function(item) {
            let renderStr ='';
            if(item.emptyMsg == undefined || item.emptyMsg === ''){
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
            $("#bsMultiselectLS").blur();
            basicMultiselect.setSelectedObject(item);
        },    
    });
</script>

/*End - Multiselect code*/
</#noparse>
```
</#if>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), 2);
 
 

REPLACE INTO jq_autocomplete_details (ac_id, ac_description, ac_select_query, ac_type_id, created_by, created_date, last_updated_ts) VALUES
('resourcesAutocomplete', 'List all the keys text resource bundle table', 'SELECT resource_key AS `key`, language_id AS languageId, `text` AS `text` 
FROM jq_resource_bundle WHERE language_id = :languageId AND `text` LIKE CONCAT("%", :searchText, "%") LIMIT :startIndex, :pageSize'
, 2, 'aar.dev@trigyn.com', NOW(), NOW());

REPLACE INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('6da5ae93-3ab6-4dcf-bb36-8b3e66c5feb1', '7748fece-e7b9-41fc-a777-74fb3ab28be5', 'autocomplete-default-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('b897ab5e-a097-406f-9888-25c5f8542e0e', '7748fece-e7b9-41fc-a777-74fb3ab28be5', 'autocomplete-default-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1), 
('d9a4efbe-efb7-4377-a3e6-692b23bd7b13', '7748fece-e7b9-41fc-a777-74fb3ab28be5', 'autocomplete-default-template', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'aar.dev@trigyn.com', 1, 1);
