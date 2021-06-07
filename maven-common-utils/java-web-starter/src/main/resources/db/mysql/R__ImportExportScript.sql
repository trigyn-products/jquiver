
replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8175bbf5d00175bbf6b3dc0000', 'import-config', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js">
</script><script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script> 
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />       
<script src="${(contextPath)!''''}/webjars/1.0/importExport/import.js"></script> 
</head>
<div class="pg-import-config">
<div class="container">
	<form id="homePageForm" action="${(contextPath)!''''}/cf/home" method="post">
		<input type="hidden" name="isRedirect" value="true"/>
	</form>
    <div class="cm-card">
	<div class="topband cm-card-header">
		<h2 class="title-cls-name float-left">Import Configuration</h2>
		<span onclick="backToPreviousPage();" class="float-right">
			<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
		</span>	
		<div class="clearfix"></div>

	</div>
	

    <div class="cm-card-body">
    
     <div class="cm-customimport" id="parentUploadBox">

     <div class="upload" id="parentUpload">
    	<div class="upload-box" id="uploadBox">
            <form id="importForm" id="formUploadBox">
            	<i class="fa fa-download fa-4x"></i>
        	<input class="fileuploaddoc" type="file" id="inputFile" name="inputFile">
        	<div class="edit">
        		<span><div id="file-upload-filename" style="display: inline-block;"></div></span>
        		<button id="btnUpload" type="button" class="btn btn-primary">Import</button>
      		</div>
     	     </form>      
    	</div>
     </div>
     </div>
     
		<div id="divHtmlTable">
			<table class="table table-striped" id="htmlTable">
				<thead class="thead-light">
					<tr>
						<th>Module Type</th>
						<th>Entity ID</th>
						<th>Entity Name</th>
						<th>Existing Version</th>
						<th>Import Version</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody class="cm-scrollbar">
				</tbody>
			</table>
		</div>
    </div>
    <div class="cm-card-footer">
		<input id="importAllBtn" class="btn btn-primary" style="float: right;" name="importAllBtn"
			value="Import All" type="button" onclick="importAll()">

		<form action="${(contextPath)!''''}/cf/cmv" method="POST" id="revisionForm">
		    <input type="hidden" id="entityName" name="entityName">
		    <input type="hidden" id="entityId" name="entityId">
			<input type="hidden" id="moduleName" name="moduleName">
			<input type="hidden" id="moduleType" name="moduleType">
			<input type="hidden" id="formId" name="formId">
			<input type="hidden" id="saveUrl" name="saveUrl">
			<input type="hidden" id="isImport" name="isImport">
			<input type="hidden" id="importJson" name="importJson">
			<input type="hidden" id="isNonVersioningModule" name="isNonVersioningModule">
			<input type="hidden" id="nonVersioningFetchURL" name="nonVersioningFetchURL">
			<input type="hidden" id="previousPageUrl" name="previousPageUrl">
		</form>
</div>
   </div>
   </div>
<script>
	contextPath = "${contextPath}";
	var accepted_file_endings = ["zip"];
	
	var zipFileJsonDataMap = new Map();
	var imporatableData;
	let idList = new Array();
	var isDataAvailableForImport = false;
	
	$(function(){
		var input = document.getElementById(''inputFile'');
		input.addEventListener( ''change'', showFileName );

		imporatableData = localStorage.getItem("imporatableData");
		let importedIdList = localStorage.getItem("importedIdList");
    	if(importedIdList != null) {
			idList = JSON.parse(importedIdList);
		}
    	
		if(imporatableData != null) {
			localStorage.removeItem("imporatableData");
			loadTable(imporatableData);
		}
	});
	
    $(''#btnUpload'').on(''click'', function() {
    	var data= document.getElementById(''inputFile'');
    	console.log(data.files);
    	if(data.files.length==0){
	    	data.focus();
	    	showMessage("Invalid file  imported.", "error");
	    	return false;
    	} else {
    		var fileExtn= data.files[0].name.substr((data.files[0].name.lastIndexOf(''.'') + 1));
    		var extension = data.files[0].type;
    	 	if($.inArray(fileExtn.toLowerCase(),accepted_file_endings) == -1) {
	    		 showMessage("Invalid file  imported.", "error");
	    		 data.focus();
	    		 return false;
	  		}
	    	
	    	importFile();  
    	}
    });
    
</script>', 'admin', 'admin', NOW(), NULL, 2);

replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('8a80cb8175bbf5d00175bbf8be160005', 'export-config', '<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js">
</script><script src="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.js"></script>          
<script src="${(contextPath)!''''}/webjars/1.0/gridutils/gridutils.js"></script> 
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/pqGrid/pqgrid.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/importExport/export.css" />
<script src="${(contextPath)!''''}/webjars/1.0/importExport/export.js"></script> 
</head>

<div class="pg-export-config">

<div class="container">
    <div class="cm-card">
	<div class="topband cm-card-header">
		<h2 class="title-cls-name float-left">Export Configuration</h2>
		<div class="float-right">
        	Show:<select id="typeSelect" class="typeSelectDropDown" onchange="changeType()">   
                <option value="0">All</option>                   
                <option value="1" selected>Custom</option>                   
                <option value="2">System</option>                 
            </select>
            <span onclick="backToPreviousPage();" >
                <input id="backBtn" class="btn btn-secondary" name="backBtn" value="Back" type="button">
            </span>	
        </div>
		<div class="clearfix"></div>	
	</div>
	

    <div class="cm-card-body">
    <div class="cm-boxwrapper"  id="mainTab">
    <div class="cm-boxleft cm-scrollbar">
          
                <div class="tab">
                 <#if (moduleVOList)??> 
                 <#list moduleVOList as moduleVO> 
                        <#if moduleVO.moduleType == "ApplicationConfiguration"> 
                            <button class="tablinks active" id= "${moduleVO.moduleId}" 
                            	onclick="openTab(event, ''${moduleVO.gridDetailsId}'', ''${moduleVO.moduleType}'')">
                            	${(moduleVO.moduleName)}  <div class="jws-count-wrap">	
                                    Selected:	
                                <span id="selectedCount_${moduleVO.moduleType}"></span> / <span id="totalCount_${moduleVO.moduleType}"></span>	
                                <img src="${(contextPath)!''''}/webjars/1.0/images/s-information1.svg">	
                                </div>	
                            </button>
                        <#else>
                            <button class="tablinks" id= "${moduleVO.moduleId}" 
                            	onclick="openTab(event, ''${moduleVO.gridDetailsId}'', ''${moduleVO.moduleType}'')">
                            	${(moduleVO.moduleName)} <div class="jws-count-wrap">	
                                    Selected:	
                                <span id="selectedCount_${moduleVO.moduleType}"></span> / <span id="totalCount_${moduleVO.moduleType}" ></span>	
                                <img src="${(contextPath)!''''}/webjars/1.0/images/s-information1.svg">	
                                </div>	
                            	</button>
                        </#if>
                                	
                            </#list>
                        </#if>
	            </div>

    </div>

    <div class="cm-boxright cm-scrollbar">
         <#if (moduleVOList)??> 
                    <#list moduleVOList as moduleVO> 
                        <#if moduleVO.moduleType == "ApplicationConfiguration"> 
                            <div id="${moduleVO.moduleType}" class="tabcontent">
                            </div>
                        <#else>
                            <div id="${moduleVO.moduleType}" class="tabcontent" style="display:none;">
                            </div>
                        </#if>
                    </#list>
		        </#if>
        
    </div>
    </div>

        <div>
        </div>

        	<div id="nextTab" style="display: none;">
		<div class="cm-scrollbar" id="divHtmlTable">
			<table class="table table-striped" id="htmlTable">
				<thead class="thead-light">
					<tr>
						<th>Module Type</th>
						<th>Entity ID</th>
						<th>Entity Name</th>
						<th>Version</th>
					</tr>
				</thead>
				<tbody class="cm-scrollbar">
				</tbody>
			</table>
		</div>
	</div>

    </div>

    <div class="cm-card-footer"><div class="clearfix"></div>
		<div id="mainTabBtn">
		<input id="nextBtn" class="btn btn-primary" style="float:right;" 
			name="nextBtn" value="Next" type="button" onclick="gotoNextPage()">
		<div class="jws-deselect-btn btn btn-primary">
			<input type="checkbox" id="deselectAllChkBx" name="deselectAllChkBx"
				 onchange="deselectAll();" style="float:left;">
			<label for="deselectAllChkBx">Deselect All</label> 
		</div>
		</div>
		<div id="nextTabBtn" style="display: none;">
            <input id="exportBtn" class="btn btn-primary" style="float: right;" name="exportBtn"
			value="Export" type="button" onclick="exportData()">
		<input id="prevBtn" class="btn btn-primary" style="float: right;" name="prevBtn"
			value="Previous" type="button" onclick="gotoPrevPage()">
		</div>
        </div>
		<div id="exportFormDiv" style = "display:none"></div>
    </div>
</div>
</div>
<script>
let isDeselectedAll = false;
let selectedType = 1;
contextPath = "${contextPath}";
$(function () {
	    localStorage.removeItem("imporatableData");
	    localStorage.removeItem("importedIdList");


	    <#if (customEntityCount)??> 
	    	<#list customEntityCount as entity> 
				<#list entity?keys as key>
					${key} = ''${(entity[key])!""}'' ;
				</#list> 
				$(''#selectedCount_''+enityType).text(count);
			</#list>
	    </#if>
	    <#if (allEntityCount)??> 
	    	<#list allEntityCount as entity> 
				<#list entity?keys as key>
					${key} = ''${(entity[key])!""}'' ;
				</#list> 
				$(''#totalCount_''+enityType).text(totalCount);
			</#list>
	    </#if>

	<#if (customEntities)??> 
		let exportableData;
		let exportableDataListMapNew;
		let exportObjNew;
		let systemConfigIncludeListNew=[];
		let customConfigExcludeListNew=[];
		<#list customEntities as entity> 
			<#list entity?keys as key> 
		    	${key} = ''${(entity[key])!""}'' ;
			</#list> 
			if(id != null && id != "") {
				exportableData = new ExportableData(enityType, id, name, versionID, 1);
				if(map.get(enityType) == null) {
					exportableDataListMapNew = new Map();
					exportObjNew = new ImportExportConfig(systemConfigIncludeListNew, customConfigExcludeListNew, null, null, enityType, 					exportableDataListMapNew);					
					map.set(enityType, exportObjNew);
				}
				map.get(enityType).getExportableDataListMap().set(id, exportableData);
			}
		</#list>
	</#if>
	var exportObj;
	let moduleName;
	let moduleID;
	let gridID;
	let colM;
	let moduleType;
	let systemConfigIncludeList=[];
	let customConfigExcludeList=[];
	let exportableDataListMap;
	<#if (moduleVOList)??> 
		<#list moduleVOList as moduleVO> 
			 moduleName="${moduleVO.moduleName}";
			 moduleID="${moduleVO.moduleId}";
			 gridID="${moduleVO.gridDetailsId}";
			 
			 moduleType = "${moduleVO.moduleType}";
			
			exportObj = map.get(moduleType);
			exportableDataListMap = new Map();
			if(exportObj == null || (exportObj != null && exportObj.getColM() == null)) {
				if(exportObj != null) {
					systemConfigIncludeList=exportObj.getSystemConfigIncludeList();
					if(systemConfigIncludeList == null) {
						systemConfigIncludeList=[];
					}

					customConfigExcludeList=exportObj.getCustomConfigExcludeList();
					if(customConfigExcludeList == null) {
						customConfigExcludeList=[];
					}

					exportableDataListMap = exportObj.getExportableDataListMap();
					if(exportableDataListMap == null) {
						exportableDataListMap = new Map();
					}
					
				}
				if(moduleType == "ApplicationConfiguration") {
					colM = [
						{ title: "Action", width: 20, maxWidth: 20, align: "center", render: updateAppConfigRenderer, dataIndx: "" },
						{ title: "Owner Id", width: 130, dataIndx: "ownerId", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Owner Type", width: 130, dataIndx: "ownerType", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Property Name", width: 130, dataIndx: "propertyName", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Property Value", width: 130, dataIndx: "propertyValue", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Modified By", width: 130, dataIndx: "modifiedBy", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  },
						{ title: "Comments", width: 130, dataIndx: "comments", align: "left", align: "left", halign: "center",
							filter: { type: "textbox", condition: "contain", listeners: ["change"]}  }
					];				

				exportObj = new ImportExportConfig(systemConfigIncludeList, customConfigExcludeList, gridID, colM, 
						moduleType, exportableDataListMap, false, "");

				exportObj.getGrid(1);
				map.set(moduleType, exportObj);
				}
			}
    		
      	</#list>
	</#if>
});

</script>', 'admin', 'admin', NOW(), NULL, 2);


replace into jq_entity_role_association (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES
('8a80cb8175bbf5d00175bbf6b4ba0002', '8a80cb8175bbf5d00175bbf6b3dc0000', 'import-config', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0), 
('8a80cb8175bbf5d00175bbf6b4ba0003', '8a80cb8175bbf5d00175bbf6b3dc0000', 'import-config', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1, 0), 
('8a80cb8175bbf5d00175bbf8be5d0007', '8a80cb8175bbf5d00175bbf8be160005', 'export-config', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0), 
('8a80cb8175bbf5d00175bbf8be5d0008', '8a80cb8175bbf5d00175bbf8be160005', 'export-config', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1, 0), 
('8a80cb8175bbf5d00175bbf8be5e0009', '8a80cb8175bbf5d00175bbf8be160005', 'export-config', '1b0a2e40-098d-11eb-9a16-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0);

