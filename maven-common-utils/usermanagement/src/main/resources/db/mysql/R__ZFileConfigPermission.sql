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


SET FOREIGN_KEY_CHECKS=1;