DELETE FROM jq_master_module WHERE master_module_name = "Permission";

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Grid Utils"  ,"gridDetailsListing"  ,"Grid");     
  
REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Templating utils"  ,"templateListingGrid"  ,"Templates");     
  
REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"DB Resource Bundle"  ,"resourceBundleListingGrid"  ,"ResourceBundle");     
  
REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Autocomplete/TypeAhead"  ,"autocompleteListingGrid"  ,"Autocomplete");     

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Notification"  ,"notificationDetailsListing"  ,"Notification");     

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Dashboard"  ,"dashboardMasterListingGrid"  ,"Dashboard");     

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Dashlet"  ,"dashletMasterListingGrid"  ,"Dashlets");     

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Dynamic Form"  ,"dynamicFormListingGrid"  ,"DynamicForm");     

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"File Manager"  ,"fileUploadConfigGrid"  ,"FileManager");     

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Dynamic REST"  ,"dynarestGrid"  ,"DynaRest");     

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES ("12ec53b3-5669-11eb-9e7a-f48e38ab8cd7"  ,"Permission"  ,"manageEntityPermissionListing"  ,"Permission");     

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Site Layout"  ,"moduleListingGrid"  ,"SiteLayout");  

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Application Configuration"  ,"propertyMasterListingGrid"  ,"ApplicationConfiguration");  

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Manage Users"  ,"jwsUserListingGrid"  ,"ManageUsers");  

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Manage Roles"  ,"roleGrid"  ,"ManageRoles");  

REPLACE INTO jq_master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Help Manual"  ,"manual-typeGrid"  ,"HelpManual");  

REPLACE INTO jq_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('e887b756-1a8f-11eb-98d3-f48e38ab1234','system', 'system', 'version', '1.4.0', 0, NOW(), 'admin', 1.00, 'Application version');