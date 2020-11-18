
REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Grid Utils"  ,"gridDetailsListing"  ,"Grid");     
  
REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Templating utils"  ,"templateListingGrid"  ,"Templates");     
  
REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"DB Resource Bundle"  ,"resourceBundleListingGrid"  ,"ResourceBundle");     
  
REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"TypeAhead"  ,"autocompleteListingGrid"  ,"Autocomplete");     

REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Notification"  ,"notificationDetailsListing"  ,"Notification");     

REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Dashboard"  ,"dashboardMasterListingGrid"  ,"Dashboard");     

REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Dashlet"  ,"dashletMasterListingGrid"  ,"Dashlets");     

REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Dynamic Form"  ,"dynamicFormListingGrid"  ,"DynamicForm");     

REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"File Manager"  ,"fileUploadConfigGrid"  ,"FileManager");     

REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Dynamic REST"  ,"dynarestGrid"  ,"DynaRest");     

REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Permission"  ,"manageEntityRoleGrid"  ,"Permission");     

REPLACE INTO master_module (master_module_id  ,master_module_name  ,grid_details_id   , module_type) 
VALUES (UUID()  ,"Site Layout"  ,"moduleListingGrid"  ,"SiteLayout");  


REPLACE INTO jws_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('e887b756-1a8f-11eb-98d3-f48e38ab1234','system', 'system', 'version', '1.3.14', 0, NOW(), 'admin', 1.00, 'Application version');