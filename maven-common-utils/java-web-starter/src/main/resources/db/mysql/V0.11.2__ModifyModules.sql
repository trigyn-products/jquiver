DELETE FROM jq_master_modules WHERE module_id IN ('f2066c95-098c-11eb-9a16-f48e38ab9348','8588104b-0ece-11eb-94b2-f48e38ab9348','0ef59343-0ecf-11eb-94b2-f48e38ab9348','cc24f9b6-0ecf-11eb-94b2-f48e38ab9348','e8026c40-0ecf-11eb-94b2-f48e38ab9348','dff72449-0ecf-11eb-94b2-f48e38ab9348');


INSERT INTO jq_role_master_modules_association (
   role_module_id
  ,role_id
  ,module_id
  ,is_active
) SELECT 
   UUID() AS role_module_id     
  ,'2ace542e-0c63-11eb-9cf5-f48e38ab9348' AS role_id                
  ,jmm.module_id AS module_id             
  ,1 AS is_active             
FROM jq_master_modules jmm;

INSERT INTO jq_role_master_modules_association (
   role_module_id
  ,role_id
  ,module_id
  ,is_active
) SELECT 
   UUID() AS role_module_id     
  ,'ae6465b3-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,jmm.module_id AS module_id             
  ,1 AS is_active             
FROM jq_master_modules jmm;

INSERT INTO jq_role_master_modules_association (
   role_module_id
  ,role_id
  ,module_id
  ,is_active
) SELECT 
   UUID() AS role_module_id     
  ,'b4a0dda1-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,jmm.module_id AS module_id             
  ,1 AS is_active             
FROM jq_master_modules jmm;

