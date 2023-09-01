SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_master_modules (module_id,module_name,is_system_module,auxiliary_data,module_type_id)
VALUES (
   '248ffd91-7760-11eb-94ed-f48e38ab8cd7'
  ,'File Bin'
  ,1
  ,null 
  ,9
);

REPLACE INTO jq_role_master_modules_association (role_module_id,role_id,module_id,is_active)
VALUES(
   "a9041473-7767-11eb-94ed-f48e38ab8cd7"     
  ,'2ace542e-0c63-11eb-9cf5-f48e38ab9348'                
  ,"248ffd91-7760-11eb-94ed-f48e38ab8cd7"              
  ,1);

REPLACE INTO jq_role_master_modules_association(role_module_id,role_id,module_id,is_active)
VALUES(
   "af02e712-7767-11eb-94ed-f48e38ab8cd7"     
  ,'ae6465b3-097f-11eb-9a16-f48e38ab9348'                
  ,"248ffd91-7760-11eb-94ed-f48e38ab8cd7"              
  ,1);

REPLACE INTO jq_role_master_modules_association (role_module_id,role_id,module_id,is_active)
VALUES(
   "b4f5ff81-7767-11eb-94ed-f48e38ab8cd7"     
  ,'b4a0dda1-097f-11eb-9a16-f48e38ab9348'                
  ,"248ffd91-7760-11eb-94ed-f48e38ab8cd7"              
  ,1);

INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id)
SELECT UUID() AS entity_role_id, jfu.file_bin_id  AS entity_id, jfu.file_bin_id AS entity_name           
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "File Bin") AS module_id 
  ,(SELECT role_id FROM jq_role WHERE role_name = "ADMIN")  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active, 9 AS module_type_id
FROM jq_file_upload AS jfu GROUP BY jfu.file_bin_id ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id)
SELECT UUID() AS entity_role_id, jfu.file_bin_id  AS entity_id, jfu.file_bin_id AS entity_name           
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "File Bin") AS module_id 
  ,(SELECT role_id FROM jq_role WHERE role_name = "AUTHENTICATED")  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active, 9 AS module_type_id
FROM jq_file_upload AS jfu GROUP BY jfu.file_bin_id ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id)
SELECT UUID() AS entity_role_id, jfu.file_bin_id  AS entity_id, jfu.file_bin_id AS entity_name           
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "File Bin") AS module_id 
  ,(SELECT role_id FROM jq_role WHERE role_name = "Anonymous")  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active, 9 AS module_type_id
FROM jq_file_upload AS jfu GROUP BY jfu.file_bin_id ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id)
VALUES( UUID(), "default", "default"          
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "File Bin")
  ,(SELECT role_id FROM jq_role WHERE role_name = "ADMIN")   
  ,NOW(), 'aar.dev@trigyn.com', 1, 9) 
 ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id)
VALUES( UUID(), "default", "default"          
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "File Bin")
  ,(SELECT role_id FROM jq_role WHERE role_name = "AUTHENTICATED")   
  ,NOW(), 'aar.dev@trigyn.com', 1, 9) 
 ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id)
VALUES( UUID(), "default", "default"          
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "File Bin")
  ,(SELECT role_id FROM jq_role WHERE role_name = "Anonymous")   
  ,NOW(), 'aar.dev@trigyn.com', 1, 9) 
 ON DUPLICATE KEY UPDATE last_updated_date = NOW();



REPLACE INTO jq_master_modules (module_id,module_name,is_system_module,auxiliary_data,module_type_id)
VALUES (
   'fcd0df1f-783f-11eb-94ed-f48e38ab8cd7'
  ,'Help Manual'
  ,1
  ,null 
  ,9
);

REPLACE INTO jq_role_master_modules_association (role_module_id,role_id,module_id,is_active)
VALUES(
   "d0639cca-7840-11eb-94ed-f48e38ab8cd7"     
  ,'2ace542e-0c63-11eb-9cf5-f48e38ab9348'                
  ,"fcd0df1f-783f-11eb-94ed-f48e38ab8cd7"              
  ,1);

REPLACE INTO jq_role_master_modules_association(role_module_id,role_id,module_id,is_active)
VALUES(
   "d5517117-7840-11eb-94ed-f48e38ab8cd7"     
  ,'ae6465b3-097f-11eb-9a16-f48e38ab9348'                
  ,"fcd0df1f-783f-11eb-94ed-f48e38ab8cd7"              
  ,1);

REPLACE INTO jq_role_master_modules_association (role_module_id,role_id,module_id,is_active)
VALUES(
   "da40e6d4-7840-11eb-94ed-f48e38ab8cd7"     
  ,'b4a0dda1-097f-11eb-9a16-f48e38ab9348'                
  ,"fcd0df1f-783f-11eb-94ed-f48e38ab8cd7"              
  ,1);

INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id)
SELECT UUID() AS entity_role_id, mt.manual_id  AS entity_id, mt.name AS entity_name           
  ,"fcd0df1f-783f-11eb-94ed-f48e38ab8cd7" AS module_id 
  ,"2ace542e-0c63-11eb-9cf5-f48e38ab9348"  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active, 10 AS module_type_id
FROM jq_manual_type AS mt ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id)
SELECT UUID() AS entity_role_id, mt.manual_id  AS entity_id, mt.name AS entity_name           
  ,"fcd0df1f-783f-11eb-94ed-f48e38ab8cd7" AS module_id 
  ,"ae6465b3-097f-11eb-9a16-f48e38ab9348"  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active, 10 AS module_type_id
FROM jq_manual_type AS mt ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active,module_type_id)
SELECT UUID() AS entity_role_id, mt.manual_id  AS entity_id, mt.name AS entity_name            
  ,"fcd0df1f-783f-11eb-94ed-f48e38ab8cd7" AS module_id 
  ,"b4a0dda1-097f-11eb-9a16-f48e38ab9348"  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active, 10 AS module_type_id
FROM jq_manual_type AS mt ON DUPLICATE KEY UPDATE last_updated_date = NOW();



SET FOREIGN_KEY_CHECKS=1;