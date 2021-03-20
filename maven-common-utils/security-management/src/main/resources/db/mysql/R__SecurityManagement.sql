SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, template_type_id) VALUES 
('fb0f0174-33b3-11eb-a009-f48e38ab8cd7', 'jws-securtity-configuration', '
<head>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<script src="${(contextPath)!''''}/webjars/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
</head>

<div class="pg-sec-mgmt">
	<div class="container">
    	<div class="topband">
        	<h2 class="title-cls-name float-left">Security Management</h2>
        	<div class="clearfix"></div>
         </div>
    

    	<div id="tabs">
        	<ul>
          		<li><a href="#ddos" data-target="/cf/ddosc">${messageSource.getMessage("jws.ddos")}</a></li>
         	</ul>
        	<div id="ddos"></div>
        </div>  
     
    </div>
</div>    
<script>

contextPath = "${contextPath}";
  
  $(function(){
  $("#tabs").tabs();
  $("#tabs").on( "tabsactivate", function( event, ui ) {
    let tabElement = ui.newTab[0].firstElementChild
    getTabData(tabElement); 
  });
 
   function getTabData(tabElement){ 
  	let url = contextPath+tabElement.getAttribute("data-target");
  	
  	  $.ajax({
      type: "POST",
      url: url,
      success: function(data){
         if($(tabElement.getAttribute("href")).html().trim()==""){
              $(tabElement.getAttribute("href")).html(data);
          }
      }    
    });
  }
    getTabData($("a[href=''#ddos'']")[0]);
  
  }); 

	
	function backToPreviousPage() {
		location.href = contextPath+"/cf/home";
	}
</script>','aar.dev@trigyn.com','aar.dev@trigyn.com',now(),2);


REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('fc06d995-2fd7-11eb-a009-f48e38ab8cd7', 'distributed-denial-of-service-configuration', '
<head>
    <script src="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/jquery.richAutocomplete.js"></script>
    <script src="${(contextPath)!''''}/webjars/1.0/typeahead/typeahead.js"></script>
    <link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/rich-autocomplete/richAutocomplete.min.css" />
	<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
</head>
	
<div class="row">
        <div class="col-3 float-left col-inner-form full-form-fields">
            <label for="denialOfService"><span class="asteriskmark">*</span>Distributed Denial Of Service: </label>
	        <div class="onoffswitch">
	        	<input type="checkbox" name="denialOfService" class="onoffswitch-checkbox" id="denialOfService" onchange="showDDOSConfig()" />
	            <label class="onoffswitch-label" for="denialOfService">
	            	<span class="onoffswitch-inner"></span>
	                <span class="onoffswitch-switch"></span>
	            </label>
	        </div>
		</div>

		<div class="col-9" id="ddosConfigDiv">
			<div class="row" >
				<div class="col-4" >
					<label for="ddosPageCount" style="white-space:nowrap"><@resourceBundleWithDefault "jws.ddosPageCount" "DDOS Page Count"/></label>
					<input type="number" id="ddosPageCount" name="ddosPageCount" value="${ddosPageCount}" class="form-control">
				</div>
				<div class="col-4" >
					<label for="ddosRefreshInterval" style="white-space:nowrap"><@resourceBundleWithDefault "jws.ddosRefreshInterval" "DDOS Refersh Interval"/></label>
					<input type="number" id="ddosRefreshInterval" name="ddosRefreshInterval" value="${ddosRefreshInterval}"  class="form-control">
				</div>
				<div class="col-4" >
					<label for="ddosSiteCount" style="white-space:nowrap"><@resourceBundleWithDefault "jws.ddosSiteCount" "DDOS Site Count"/></label>
					<input type="text" id="ddosSiteCount" name="ddosSiteCount" value="${ddosSiteCount}"  class="form-control">
				</div>
                <div class="col-8 margin-t-10 ">
					<label for="ddosExcludedExtensions" style="white-space:nowrap"><@resourceBundleWithDefault "jws.ddosExcludedExtn" "DDOS Exlcuded Extensions"/></label>
					<input type="text" id="ddosExcludedExtensions" name="ddosExcludedExtensions" value="${ddosExcludedExtensions}"  class="form-control">
				</div>
			</div>
		</div>
	</div>
	 
	<div class="row" id="configDiv"> 		
		
		<div class="col-3 float-left col-inner-form full-form-fields">
			<label for="inMemory"><span class="asteriskmark">*</span>In Memory: </label>
			<div class="onoffswitch">
			   	<input type="checkbox" name="inMemory" class="onoffswitch-checkbox" id="inMemory"  onchange="showBlockedIPAddr()"  />
			    <label class="onoffswitch-label" for="inMemory">
				   	<span class="onoffswitch-inner"></span>
			    	<span class="onoffswitch-switch"></span>
				</label>
			</div>
		</div>
		<div id="blockedIPAddrDiv" class="col-3 margin-t-10 float-left">
			<label for="dynamicFormName" style="white-space:nowrap"><@resourceBundleWithDefault "jws.blockedIPAddr" "Blocked IP Address"/></label>	
			<textarea id="blockedIpAddress" name="blockedIpAddress"  class="form-control"></textarea>
		</div>
		<div class="clearfix"></div>
	</div>  
	

		<div id="note" class="margin-t-25 clearfix">
	    	<span class="pull-left"><i>Kindly restart your server to get your security configuration working.</i></span>
		    <div class="btn-icons nomargin-right-cls pull-right">
		    	<input type="button" value="Save" class="btn btn-primary" onclick="saveDDOSDetails();">
		    	<span onclick="backToPreviousPage();">
					<input id="backBtn" class="btn btn-secondary" name="backBtn" value="Cancel" type="button">
				</span>
			</div>
		</div>
	<div id="restartConfirmation"></div>	

<script>
	contextPath = "${contextPath}";
	let isDDOSEnabled = "${isDDOSEnabled!''''}";
	let inMemory = "${inMemory!''''}";

	if(isDDOSEnabled == 1){ 
		$("#denialOfService").prop("checked",true);
		$("#ddosConfigDiv").show();
		$("#configDiv").show();
	}else{ 
		$("#denialOfService").prop("checked",false);
		$("#ddosConfigDiv").hide();
		$("#configDiv").hide();
	}
	if(inMemory == 1){ 
		$("#inMemory").prop("checked",true);
		$("#blockedIPAddrDiv").hide();
	}
	function showDDOSConfig(){
		if($("#denialOfService").is(":checked")){ 
			$("#ddosConfigDiv").show();
			$("#configDiv").show();
		}else{ 
			$("#ddosConfigDiv").hide();
			$("#configDiv").hide();
		}
	}
	
	function showBlockedIPAddr(){
		if($("#inMemory").is(":checked")){ 
			$("#blockedIPAddrDiv").hide();
		}else{ 
			$("#blockedIPAddrDiv").show();
		}
	}

    function restartServer(){
    	 $.ajax({
		     type : "GET",
		     url : contextPath+"/cf/restart",
		     data : { 
			     		
		     },
		     success: function(data) {
             	location.href=contextPath+"/cf/login"
             }   
        });
    }
    
    function saveDDOSDetails(){ 
		$("#restartConfirmation").html("Are you sure you want to save the changes and restart the server?");
		$("#restartConfirmation").dialog({
			bgiframe		: true,
			autoOpen		: true, 
			modal		 	: true,
			closeOnEscape 	: true,
			draggable	 : true,
			resizable	 : false,
			title		 : "Restart Server",
			buttons		 : [{
				text :"Cancel",
				click: function() { 
					$(this).dialog("close");
				},
			},
			{
				text	: "Yes",
				click	: function(){
				   		  	$.ajax({
								type : "POST",
								async: false,
								url : contextPath+"/api/saveDDOSDetails",
								data : {
								   	ddosPageCount : $("#ddosPageCount").val(),
								   	ddosSiteCount : $("#ddosSiteCount").val(),
								   	ddosRefreshInterval : $("#ddosRefreshInterval").val(),
								   	ddosExcludedExtensions : $("#ddosExcludedExtensions").val(),
								   	blockedIpAddress : $("#blockedIpAddress").val(),
								   	inMemoryEnabled : ($("#inMemory").is(":checked") ? 1 : 0),
								   	isDDOSEnabled :  ($("#denialOfService").is(":checked") ? 1 : 0),
								},
								success : function(data) {
								  	showMessage("Information saved successfully", "success");
								   	restartServer();
								},
								error : function(xhr, error){
								   	showMessage("Error occurred while saving", "error");
								},
      						});
      						$(this).dialog("close");
    					}
	       },],
	       open		: function( event, ui ) {	    	
		   	   $(".ui-dialog-titlebar")
		   	    .find("button").removeClass("ui-dialog-titlebar-close").addClass("ui-button ui-corner-all ui-widget ui-button-icon-only ui-dialog-titlebar-close")
		       .prepend(''<span class="ui-button-icon ui-icon ui-icon-closethick"></span>'').append(''<span class="ui-button-icon-space"></span>'');
		   }	
	
		});
	}
    
    function backToPreviousPage(){ 
    	location.href=contextPath+"/cf/home";
    }
</script>', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);

SET FOREIGN_KEY_CHECKS=1;