
REPLACE INTO template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum, template_type_id) VALUES
('9378ee23-09fa-11eb-a894-f48e38ab8cd7', 'home-page', '<head>
<link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.css"/>
<link rel="stylesheet" href="/webjars/jquery-ui/1.12.1/jquery-ui.theme.css" />
<script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
<script src="/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>
<script src="/webjars/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="/webjars/1.0/css/starter.style.css" />
<title><@resourceBundleWithDefault "projectName" "JQuiver"/></title>
</head>
	<nav class="navbar navbar-dark sticky-top blue-bg flex-md-nowrap p-0 shadow ">
		<a class="navbar-brand col-md-3 col-lg-2 mr-0 px-3" href="/cf/home"><@resourceBundleWithDefault "projectName" "JQuiver"/></a>
        <span class="hamburger float-left" id="openbtni" class="closebtn" onclick="homePageFn.openNavigation()">
			<i class="fa fa-bars" aria-hidden="true"></i>
		</span>
        <span id="closebtni" class="closebtn float-left" onclick="homePageFn.closeNavigation()">×</span>
        <ul class="navbar-nav px-3 float-right">
            <#if loggedInUserName?? && loggedInUserName != "anonymous">
                <li class="nav-item text-nowrap">
                    <div class="row margin-r-5 profile-tray">
                        <ul>
                            <li> <a class="nav-link cm-userid" href="/cf/profile"><i class="fa fa-user-circle-o" aria-hidden="true"></i> ${loggedInUserName}</a></li>
                            <li><a class="nav-link signout-icon" href="/logout"> <i class="fa fa-sign-out" aria-hidden="true"></i></a></li>
                        </ul>
                    </div>
                </li>
            </#if>
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
														<a href = "/view/${moduleDetailsVOChild?api.getModuleURL()!''''}" class="nav-link">${moduleDetailsVOChild?api.getModuleName()!''''}</a> 
													</li>
												</#if>
											</#list>
										</ul>
									</div>
							
							<#elseif !(moduleDetailsVO?api.getParentModuleId())??>
								<li class="nav-item">
									<span data-feather="file"></span>
									<a href = "/view/${moduleDetailsVO?api.getModuleURL()!''''}" class="nav-link">${moduleDetailsVO?api.getModuleName()!''''}</a>
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
            <small>Copyright &copy; JQuiver</small>
            <small class="float-right">${(jquiverVersion)!''1.0.0''}</small>
        </div>
    </div>
</footer>
<script>
	const contextPathHome = "${contextPath}";
	let homePageFn;
	
	$(function() {
		  
	  const homePage = new HomePage();
	  homePageFn = homePage.fn;
	  homePageFn.collapsableMenu();
	  let noOfModules = $("#menuUL li").length;
	  if(noOfModules == 0){
		$("#openbtni").hide();
	  }
	});


</script>
<script src="/webjars/1.0/home/home.js"></script>


<#assign gaAttributes = {
"enableGoogleAnalytics": enableGoogleAnalytics,
"googleAnalyticsKey": googleAnalyticsKey,
"entityType": entityType,
"entityName": entityName
}>
<@templateWithParams "google-analytics-template" gaAttributes />

', 'aar.dev@trigyn.com', 'aar.dev@trigyn.com', NOW(), NULL, 2);