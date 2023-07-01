-- Any permission related scripts will be maintained in Version file, to avoid overriding of the permission in client DB.
 SET FOREIGN_KEY_CHECKS=0;

 REPLACE INTO jq_master_modules(module_id, module_name, sequence, is_system_module, grid_details_id, module_type, is_perm_supported, is_entity_perm_supported, is_imp_exp_supported) VALUES
('ded49cbd-ed7c-40ce-b1f8-c2e34ad33473', 'API Clients', 20, 1, 'jq_api_client_details_grid', 'ApiClientDetails', 0, 0, 1),
('799947cc-b6cb-11eb-8529-0242ac130003', 'Additional Datasource', 21, 1, 'jq-additional-datasourceGrid', 'AdditionalDatasource', 0, 0, 1);

REPLACE INTO `jq_master_modules` (`module_id`, `module_name`, `sequence`, `is_system_module`, `auxiliary_data`, `module_type_id`, `grid_details_id`, `module_type`, `is_entity_perm_supported`, `is_imp_exp_supported`, `is_perm_supported`) VALUES('5c4358e2-b69a-11ed-983d-7c8ae1bb24d8','Files','20','1',NULL,NULL,'filesExportGrid','Files','0','1','0');

REPLACE INTO jq_master_modules (module_id,module_name,is_system_module,auxiliary_data,module_type_id)
VALUES (
   '248ffd91-7760-11eb-94ed-f48e38ab8cd7'
  ,'File Bin'
  ,1
  ,null 
  ,9
);

REPLACE INTO jq_master_modules (module_id,module_name,is_system_module,auxiliary_data,module_type_id)
VALUES (
   'fcd0df1f-783f-11eb-94ed-f48e38ab8cd7'
  ,'Help Manual'
  ,1
  ,null 
  ,9
);

 REPLACE INTO jq_master_modules(module_id, module_name, is_system_module, auxiliary_data, module_type_id) VALUES
 
('4461bbae-8c8f-11eb-8dcd-0242ac130003', 'Master Generator', 1, '', 11),
('5559212c-8c8f-11eb-8dcd-0242ac130003', 'Miltilingual', 1, '', 12),
('6ac6a54c-8d3f-11eb-8dcd-0242ac130003', 'Notification', 1, '', 13),
('5f6dd374-8c8f-11eb-8dcd-0242ac130003', 'User Management', 1, '', 14),
('699ac104-8c8f-11eb-8dcd-0242ac130003', 'Security Management', 1, '', 15),
('76270518-8c8f-11eb-8dcd-0242ac130003', 'Application Configuration', 1, '', 16),
('96917c8e-8c8f-11eb-8dcd-0242ac130003', 'Import/Export', 1, '', 18); 
 
 REPLACE INTO jq_master_modules(module_id, module_name, sequence, is_system_module, grid_details_id, module_type, is_perm_supported, is_entity_perm_supported, is_imp_exp_supported) VALUES
('12ec53b3-5669-11eb-9e7a-f48e38ab8cd7', 'Permission', 18, 1, 'manageEntityPermissionListing', 'Permission', 0, 0, 1),
('aa3e68ac-8d42-11eb-98cc-9840bb1e8144', 'Users', 19, 1, 'userListingGrid', 'ManageUsers', 0, 0, 1),
('aa3e68ac-8d42-11eb-98cc-9840bb1e8155', 'Roles', 22, 1, 'roleListingGrid', 'ManageRoles', 0, 0, 1),
('fcd0df1f-783f-11eb-94ed-f48e38ab8cd6', 'Scheduler', 23, 1, 'jq-schedulerGrid', 'Scheduler', 0, 0, 1);
 
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

DELETE FROM `jq_role_master_modules_association` WHERE module_id = '699ac104-8c8f-11eb-8dcd-0242ac130003';
DELETE FROM jq_role_master_modules_association WHERE role_id = '2ace542e-0c63-11eb-9cf5-f48e38ab9348';

 SET FOREIGN_KEY_CHECKS=1;
  
  