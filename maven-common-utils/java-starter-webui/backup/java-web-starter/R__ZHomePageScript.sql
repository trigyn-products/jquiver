REPLACE INTO jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('9378ee23-09fa-11eb-a894-f48e38ab8cd7', 'home-page', '<head>
<link rel="icon" type="image/x-icon" href="${(contextPath)!''''}/webjars/1.0/images/favicon.png"/>
<script src="${(contextPath)!''''}/webjars/1.0/home/home.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="${(contextPath)!''''}/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="${(contextPath)!''''}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="${(contextPath)!''''}/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="${(contextPath)!''''}/webjars/1.0/css/starter.style.css" />
<title><@resourceBundleWithDefault "jws.projectName" "JQuiver"/></title>
</head>
		<nav class="navbar navbar-dark sticky-top blue-bg flex-md-nowrap p-0 shadow ">
		<a class="navbar-brand col-md-3 col-lg-2 mr-0 px-3" href="${(contextPath)!''''}/"><@resourceBundleWithDefault "jws.projectName" "JQuiver"/></a>
        <#if moduleDetailsVOList?? && moduleDetailsVOList?size gte 1>
	        <span class="hamburger float-left" id="openbtni" class="closebtn" onclick="homePageFn.openNavigation()">
				<i class="fa fa-bars" aria-hidden="true"></i>
			</span>
		</#if>
        <span id="closebtni" class="closebtn float-left" onclick="homePageFn.closeNavigation()">×</span>
        
        <ul class="navbar-nav px-3 float-right">
			<li class="nav-item text-nowrap">
				<div class="row margin-r-5 profile-tray">
					<ul>
						<li>						
					       <select id="languageOptions" onchange="changeLanguage()">
					        </select>
						</li>
            			<li>
            				<a class="nav-link " href="${(contextPath)!''''}/view/jqhm" title="<@resourceBundleWithDefault ''jws.helpManual'' ''Help Manual''/>" target="_blank">
							<i class="fa fa-question-circle" aria-hidden="true"></i>
							</a>
						</li>
						<li>
            				<a class="nav-link " href="${(contextPath)!''''}/view/health" title="<@resourceBundleWithDefault ''jws.appMetrics'' ''Application Metrics''/>" target="_blank">
							<i class="fa fa-heartbeat" aria-hidden="true"></i>
							</a>
						</li>
            			<#if loggedInUserName?? && loggedInUserName != "anonymous">
                        	<li><a class="nav-link cm-userid" href="${(contextPath)!''''}/cf/profile"><i id="iClass" class="fa fa-user-circle-o"  style="display:none;" aria-hidden="true"></i><img style="width:20px; height:20px;" style="display:none;" id="profileImg" /> ${loggedInUserName}</a></li>
                            <li>
                            	<a class="nav-link signout-icon" href="${(contextPath)!''''}/logout" title="<@resourceBundleWithDefault ''jws.logout'' ''Logout''/>"> 
                            		<i class="fa fa-sign-out" aria-hidden="true"></i>
                            	</a>
                            </li>
                        <#elseif loggedInUserName?? && loggedInUserName == "anonymous" && isAuthEnabled?c == "true" >
                        	<li>
                        		<a class="nav-link signout-icon" href="${(contextPath)!''''}/cf/login" title="<@resourceBundleWithDefault ''jws.login'' ''Login''/>">
                        			<img src="${(contextPath)!''''}/webjars/1.0/images/login.png" class="login-img-cls">
                        		</a>
                        	</li>
            			</#if>
        			</ul>
                  </div>
             </li>
          </ul>
	</nav>

<div class="container-fluid ">
	<div class="row">
		<nav id="mySidenav" class=" bg-dark sidenav sidebar">
			<div class="nav-inside">
				<input class="form-control form-control-dark w-100" id="searchInput" type="text" placeholder="Search" aria-label="Search" onkeyup="homePageFn.menuSearchFilter()">
				<span id="menuSearchClear" onclick="homePageFn.clearMenuSearch()" class="menu-clear-txt">
                    <i class="fa fa-times" aria-hidden="true"></i>
                </span>
				<ul id="menuUL" class="nav flex-column customnav">
					<#if moduleDetailsVOList??>
						<#list moduleDetailsVOList as moduleDetailsVO>
							<#if ((moduleDetailsVO?api.getSubModuleCount())?? && (moduleDetailsVO?api.getSubModuleCount()) gte 1) || moduleDetailsVO?api.getModuleURL() == "#">
								<li class="nav-item">
									<a class="nav-link clickable panel-collapsed" >${moduleDetailsVO?api.getModuleName()!''''}<i class="fa fa-caret-down" aria-hidden="true"></i>
                                    </a>
									<div class="collapse collapsein">
										<ul class="subcategory">
											<#list moduleDetailsVOList as moduleDetailsVOChild>
												<#if (moduleDetailsVOChild?api.getParentModuleId())?? && (moduleDetailsVOChild?api.getParentModuleId()) == (moduleDetailsVO?api.getModuleId())>
													<li>
														<a href = "${(contextPath)!''''}/view/${moduleDetailsVOChild?api.getModuleURL()!''''}" class="nav-link">${moduleDetailsVOChild?api.getModuleName()!''''}</a> 
													</li>
												</#if>
											</#list>
										</ul>
									</div>
							
							<#elseif !(moduleDetailsVO?api.getParentModuleId())??>
								<li class="nav-item">
									<span data-feather="file"></span>
									<a href = "${(contextPath)!''''}/view/${moduleDetailsVO?api.getModuleURL()!''''}" class="nav-link">${moduleDetailsVO?api.getModuleName()!''''}</a>
								</li>
							</#if>
						
						</#list>
					</#if>
				</div>
			</nav>
	
	<main id="main" class="main-container">
		<div id="bodyDiv" onclick="homePageFn.closeNavigation();">
			<#include "template-body">
		</div>
	</main>
	
	</div>
</div>
<div class="clearfix"></div>
<footer class="page-footer font-small blue pt-4">
    <div class="footer bg-dark">
        <div class="text-center">
            <small>Copyright &copy; <a href = "https://www.trigyn.com/" target="_blank">Trigyn Technologies</a></small>
            <small class="float-right"><a href = "${(contextPath)!''''}/view/team" target="_blank">${(jquiverVersion)!''1.0.0''}</a></small>
        </div>
    </div>
</footer>
<script>
	const contextPathHome = "${contextPath}";
	let homePageFn;
	let jqJSDateFormat;
	
	$(function() {
		  
	  const homePage = new HomePage();
	  homePageFn = homePage.fn;
	  homePageFn.collapsableMenu();
	  disableInputSuggestion();
	  
	  <#list systemProperties as key, value>
        <#if key.propertyName == "jws-date-format">
            jqJSDateFormat = JSON.stringify(${value});
        	jqJSDateFormat = JSON.parse(jqJSDateFormat).js;
      	</#if>
      </#list>
      retrieveProfilePic();
      getLanguageOption();     
      
	});

    function retrieveProfilePic() {
        $.ajax({
			type : "GET",
	    	contentType : "application/json",
			url : contextPathHome+"/api/retrieveProfilePic",
            success: function(data) {
				if(data == undefined || data.length==0) {
	                if(document.getElementById("iClass") != null) {
						document.getElementById("iClass").style.display = "inline";
	                }
                    if(document.getElementById("profileImg") != null) {
	                    document.getElementById("profileImg").style.display = "none";
                    }
				} else {
	                $("#profileImg").attr("src", contextPathHome + "/cf/files/" + data[0].fileUploadId);
					document.getElementById("iClass").style.display = "none";
	                document.getElementById("profileImg").style.display = "inline";
	            }
			},
			error: function(data) {
				showMessage("Error occurred while retrieving details", "error");
			},
		});
    }


</script>



<#assign gaAttributes = {
"enableGoogleAnalytics": enableGoogleAnalytics,
"googleAnalyticsKey": googleAnalyticsKey,
"entityType": entityType,
"entityName": entityName
}>
<@templateWithParams "google-analytics-template" gaAttributes />

', 'admin@jquiver.com', 'admin@jquiver.com', NOW(), NULL, 2);
