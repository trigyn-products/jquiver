REPLACE INTO master_module (master_module_id  ,master_module_name  ,module_template_id  ,grid_details_id   ,auxiliary_data  ,module_description_template, show_on_dashboard, is_system_module) 
VALUES (UUID()  ,"Grid Utils"  ,"2dda2505-f39f-11ea-97da-e454e805e22f"  ,"gridDetailsListing"  ,NULL  ,'<p class="mb-1"> Built using pq-grid, and supporting it with generic queries to get data for grid based on the target databases. </p>
			<small>Now any master listing page will be created without much efforts</small>', 1, 1);     
      
REPLACE INTO master_module (master_module_id  ,master_module_name  ,module_template_id  ,grid_details_id   ,auxiliary_data  ,module_description_template, show_on_dashboard, is_system_module) 
VALUES (UUID()  ,"Templating utils"  ,"2dda10f5-f39f-11ea-97da-e454e805e22f"  ,"templateListingGrid"  ,NULL  ,'<p class="mb-1">Built using Freemarker templating engine, generates HTML web pages, e-mails, configuration files, etc. from template files and the data your application provides</p>
			<small class="text-muted">Now create views for your project, and leverage all benifits of spring utils on it.</small>', 1, 1);
      
REPLACE INTO master_module (master_module_id  ,master_module_name  ,module_template_id  ,grid_details_id   ,auxiliary_data  ,module_description_template, show_on_dashboard, is_system_module) 
VALUES (UUID()  ,"DB Resource Bundle"  ,"2dda3b32-f39f-11ea-97da-e454e805e22f"  ,"resourceBundleListingGrid"  ,NULL  ,'<p class="mb-1">Built using Spring interceptors, Locale Resolvers and Resource Bundles for different locales</p>
			<small class="text-muted">Any web application with users all around the world, internationalization (i18n) or localization (L10n) is very important for better user interaction, so handle all these from the admin panel itself by storing it in database.</small>', 1, 1);
      
REPLACE INTO master_module (master_module_id  ,master_module_name  ,module_template_id  ,grid_details_id   ,auxiliary_data  ,module_description_template, show_on_dashboard, is_system_module) 
VALUES (UUID()  ,"TypeAhead"  ,"de7a40b1-f38a-11ea-97da-e454e805e22f"  ,"autocompleteListingGrid"  ,NULL  ,'<p class="mb-1">Built using Jquery plugin, rich-autocomplete to get data lazily</p>
			<small class="text-muted">Now any autocomplete component which handles dynamic creation of query will be created without much efforts</small>', 1, 1);
      
REPLACE INTO master_module (master_module_id  ,master_module_name  ,module_template_id  ,grid_details_id   ,auxiliary_data  ,module_description_template, show_on_dashboard, is_system_module) 
VALUES (UUID()  ,"Notification"  ,"2dda9476-f39f-11ea-97da-e454e805e22f"  ,"notificationDetailsListing"  ,NULL  ,'<p class="mb-1">Built using Freemarker templating engine.</p>
			<small class="text-muted">Create your application notification with ease and control the duration and context where to show it, (cross platform.)</small>', 1, 1);
      
REPLACE INTO master_module (master_module_id  ,master_module_name  ,module_template_id  ,grid_details_id   ,auxiliary_data  ,module_description_template, show_on_dashboard, is_system_module) 
VALUES (UUID()  ,"Dashboard"  ,"2dd83c16-f39f-11ea-97da-e454e805e22f"  ,"dashboardMasterListingGrid"  ,NULL  ,'<p class="mb-1">Built using Freemarker templating engine and spring resource bundles</p>
			<small class="text-muted">Now create the daily reporting, application usage, trends dashboard for your web application and control it with our dashboard admin panel.</small>', 1, 1);
      
REPLACE INTO master_module (master_module_id  ,master_module_name  ,module_template_id  ,grid_details_id   ,auxiliary_data  ,module_description_template, show_on_dashboard, is_system_module) 
VALUES (UUID()  ,"Dynamic Form"  ,"2dd9ae9b-f39f-11ea-97da-e454e805e22f"  ,"dynamicFormListingGrid"  ,NULL  ,'<p class="mb-1">Built using Freemarker templating engine </p>
			<small class="text-muted">Now create the dynamic forms for your web application, without writing any java code just by using freemarker</small>', 1, 1);