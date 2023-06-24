 SET FOREIGN_KEY_CHECKS=0;
 
 REPLACE INTO jq_master_modules(module_id, module_name, is_system_module, auxiliary_data, module_type_id) VALUES
 
('4461bbae-8c8f-11eb-8dcd-0242ac130003', 'Master Generator', 1, '', 11),
('5559212c-8c8f-11eb-8dcd-0242ac130003', 'Miltilingual', 1, '', 12),
('6ac6a54c-8d3f-11eb-8dcd-0242ac130003', 'Notification', 1, '', 13),
('5f6dd374-8c8f-11eb-8dcd-0242ac130003', 'User Management', 1, '', 14),
('699ac104-8c8f-11eb-8dcd-0242ac130003', 'Security Management', 1, '', 15),
('76270518-8c8f-11eb-8dcd-0242ac130003', 'Application Configuration', 1, '', 16),
('96917c8e-8c8f-11eb-8dcd-0242ac130003', 'Import/Export', 1, '', 18); 
 
 UPDATE jq_master_modules SET sequence = "1" WHERE module_id="4461bbae-8c8f-11eb-8dcd-0242ac130003";
 UPDATE jq_master_modules SET sequence = "2" WHERE module_id="07067149-098d-11eb-9a16-f48e38ab9348";
 UPDATE jq_master_modules SET sequence = "3" WHERE module_id="91a81b68-0ece-11eb-94b2-f48e38ab9348";
 UPDATE jq_master_modules SET sequence = "4" WHERE module_id="248ffd91-7760-11eb-94ed-f48e38ab8cd7";
 UPDATE jq_master_modules SET sequence = "5" WHERE module_id="1b0a2e40-098d-11eb-9a16-f48e38ab9348";
 UPDATE jq_master_modules SET sequence = "6" WHERE module_id="30a0ff61-0ecf-11eb-94b2-f48e38ab9348";
 UPDATE jq_master_modules SET sequence = "7" WHERE module_id="47030ee1-0ecf-11eb-94b2-f48e38ab9348";
 UPDATE jq_master_modules SET sequence = "8" WHERE module_id="c6cc466a-0ed3-11eb-94b2-f48e38ab9348";
 UPDATE jq_master_modules SET sequence = "9" WHERE module_id="5559212c-8c8f-11eb-8dcd-0242ac130003";
 UPDATE jq_master_modules SET sequence = "10" WHERE module_id="b0f8646c-0ecf-11eb-94b2-f48e38ab9348";
 UPDATE jq_master_modules SET sequence = "11" WHERE module_id="19aa8996-80a2-11eb-971b-f48e38ab8cd7";
 UPDATE jq_master_modules SET sequence = "12" WHERE module_id="6ac6a54c-8d3f-11eb-8dcd-0242ac130003";
 UPDATE jq_master_modules SET sequence = "13" WHERE module_id="5f6dd374-8c8f-11eb-8dcd-0242ac130003";
 UPDATE jq_master_modules SET sequence = "14" WHERE module_id="699ac104-8c8f-11eb-8dcd-0242ac130003";
 UPDATE jq_master_modules SET sequence = "15" WHERE module_id="76270518-8c8f-11eb-8dcd-0242ac130003";
 UPDATE jq_master_modules SET sequence = "16" WHERE module_id="8a410fb2-8c8f-11eb-8dcd-0242ac130003";
 UPDATE jq_master_modules SET sequence = "17" WHERE module_id="fcd0df1f-783f-11eb-94ed-f48e38ab8cd7";
 UPDATE jq_master_modules SET sequence = "18" WHERE module_id="96917c8e-8c8f-11eb-8dcd-0242ac130003";
 
 INSERT INTO jq_role_master_modules_association (role_module_id, role_id, module_id, is_active) VALUES
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','4461bbae-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','4461bbae-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','4461bbae-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','5559212c-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','5559212c-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','5559212c-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','19aa8996-80a2-11eb-971b-f48e38ab8cd7', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','19aa8996-80a2-11eb-971b-f48e38ab8cd7', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','19aa8996-80a2-11eb-971b-f48e38ab8cd7', 1),
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','5f6dd374-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','5f6dd374-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','5f6dd374-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','699ac104-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','699ac104-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','699ac104-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','76270518-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','76270518-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','76270518-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','96917c8e-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','96917c8e-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','96917c8e-8c8f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','6ac6a54c-8d3f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','6ac6a54c-8d3f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','6ac6a54c-8d3f-11eb-8dcd-0242ac130003', 1),
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','fcd0df1f-783f-11eb-94ed-f48e38ab8cd7', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','fcd0df1f-783f-11eb-94ed-f48e38ab8cd7', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','fcd0df1f-783f-11eb-94ed-f48e38ab8cd7', 1),
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','248ffd91-7760-11eb-94ed-f48e38ab8cd7', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','248ffd91-7760-11eb-94ed-f48e38ab8cd7', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','248ffd91-7760-11eb-94ed-f48e38ab8cd7', 1);
  
  
  UPDATE jq_master_modules SET grid_details_id = "gridDetailsListing", module_type="Grid",
  	is_perm_supported="1", is_entity_perm_supported=1, is_imp_exp_supported="1" WHERE module_id="07067149-098d-11eb-9a16-f48e38ab9348";
  UPDATE jq_master_modules SET grid_details_id = "dashletMasterListingGrid", module_type="Dashlets",
  	is_perm_supported="1", is_entity_perm_supported=1, is_imp_exp_supported="1" WHERE module_id="19aa8996-80a2-11eb-971b-f48e38ab8cd7";
  UPDATE jq_master_modules SET grid_details_id = "templateListingGrid", module_type="Templates",
  	is_perm_supported="1", is_entity_perm_supported=1, is_imp_exp_supported="1" WHERE module_id="1b0a2e40-098d-11eb-9a16-f48e38ab9348";
  UPDATE jq_master_modules SET grid_details_id = "fileUploadConfigGrid", module_type="FileManager",
  	is_perm_supported="1", is_entity_perm_supported=1, is_imp_exp_supported="1" WHERE module_id="248ffd91-7760-11eb-94ed-f48e38ab8cd7";
  UPDATE jq_master_modules SET grid_details_id = "dynamicFormListingGrid", module_type="DynamicForm",
  	is_perm_supported="1", is_entity_perm_supported=1, is_imp_exp_supported="1" WHERE module_id="30a0ff61-0ecf-11eb-94b2-f48e38ab9348";
  UPDATE jq_master_modules SET grid_details_id = "dynarestListingGrid", module_type="DynaRest",
  	is_perm_supported="1", is_entity_perm_supported=1, is_imp_exp_supported="1" WHERE module_id="47030ee1-0ecf-11eb-94b2-f48e38ab9348";
  UPDATE jq_master_modules SET grid_details_id = "resourceBundleListingGrid", module_type="ResourceBundle",
  	is_perm_supported="1", is_entity_perm_supported=0, is_imp_exp_supported="1" WHERE module_id="5559212c-8c8f-11eb-8dcd-0242ac130003";
  UPDATE jq_master_modules SET grid_details_id = "notificationDetailsListing", module_type="Notification",
  	is_perm_supported="1", is_entity_perm_supported=0, is_imp_exp_supported="1" WHERE module_id="6ac6a54c-8d3f-11eb-8dcd-0242ac130003";
  UPDATE jq_master_modules SET grid_details_id = "propertyMasterListingGrid", module_type="ApplicationConfiguration",
  	is_perm_supported="1", is_entity_perm_supported=0, is_imp_exp_supported="1" WHERE module_id="76270518-8c8f-11eb-8dcd-0242ac130003";
  UPDATE jq_master_modules SET grid_details_id = "autocompleteListingGrid", module_type="Autocomplete",
  	is_perm_supported="1", is_entity_perm_supported=1, is_imp_exp_supported="1" WHERE module_id="91a81b68-0ece-11eb-94b2-f48e38ab9348";
  UPDATE jq_master_modules SET grid_details_id = "dashboardMasterListingGrid", module_type="Dashboard",
  	is_perm_supported="1", is_entity_perm_supported=1, is_imp_exp_supported="1" WHERE module_id="b0f8646c-0ecf-11eb-94b2-f48e38ab9348";
  UPDATE jq_master_modules SET grid_details_id = "moduleListingGrid", module_type="SiteLayout",
  	is_perm_supported="1", is_entity_perm_supported=1, is_imp_exp_supported="1" WHERE module_id="c6cc466a-0ed3-11eb-94b2-f48e38ab9348";
  UPDATE jq_master_modules SET is_perm_supported="1", is_entity_perm_supported=0, is_imp_exp_supported="0" WHERE module_id="4461bbae-8c8f-11eb-8dcd-0242ac130003";
  UPDATE jq_master_modules SET is_perm_supported="1", is_entity_perm_supported=0, is_imp_exp_supported="0" WHERE module_id="5f6dd374-8c8f-11eb-8dcd-0242ac130003";
  UPDATE jq_master_modules SET is_perm_supported="1", is_entity_perm_supported=0, is_imp_exp_supported="0" WHERE module_id="699ac104-8c8f-11eb-8dcd-0242ac130003";
  UPDATE jq_master_modules SET is_perm_supported="1", is_entity_perm_supported=0, is_imp_exp_supported="0" WHERE module_id="96917c8e-8c8f-11eb-8dcd-0242ac130003";
  
 REPLACE INTO jq_master_modules(module_id, module_name, sequence, is_system_module, grid_details_id, module_type, is_perm_supported, is_entity_perm_supported, is_imp_exp_supported) VALUES
('12ec53b3-5669-11eb-9e7a-f48e38ab8cd7', 'Permission', 18, 1, 'manageEntityPermissionListing', 'Permission', 0, 0, 1),
('aa3e68ac-8d42-11eb-98cc-9840bb1e8144', 'Manage Users', 19, 1, 'jwsUserListingGrid', 'ManageUsers', 0, 0, 1);


UPDATE jq_template_master SET created_by='admin', updated_by='admin@jquiver.io';
UPDATE jq_dashlet SET created_by='admin', updated_by='admin@jquiver.io';
UPDATE jq_dynamic_rest_details SET created_by='admin@jquiver.io';
UPDATE jq_dynamic_rest_details SET last_updated_by='admin@jquiver.io';
UPDATE jq_dynamic_form SET created_by='admin@jquiver.io';
UPDATE jq_dynamic_form SET last_updated_by='admin@jquiver.io';
UPDATE jq_grid_details SET created_by='admin@jquiver.io';
UPDATE jq_grid_details SET last_updated_by='admin@jquiver.io';
UPDATE jq_entity_role_association SET last_updated_by='admin@jquiver.io';
UPDATE jq_autocomplete_details SET created_by='admin@jquiver.io';
UPDATE jq_autocomplete_details SET last_updated_by='admin@jquiver.io';
UPDATE jq_file_upload_config SET created_by='admin@jquiver.io';
UPDATE jq_file_upload_config SET last_updated_by='admin@jquiver.io';

SET FOREIGN_KEY_CHECKS=1;