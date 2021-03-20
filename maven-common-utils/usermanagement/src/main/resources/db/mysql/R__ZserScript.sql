INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active)
SELECT UUID() AS entity_role_id, db.dashboard_id  AS entity_id, db.dashboard_name AS entity_name           
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "Dashboard") AS module_id 
  ,(SELECT role_id FROM jq_role WHERE role_name = "AUTHENTICATED")  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active 
FROM jq_dashboard AS db ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active)
SELECT UUID() AS entity_role_id, db.dashboard_id  AS entity_id, db.dashboard_name AS entity_name           
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "Dashboard") AS module_id 
  ,(SELECT role_id FROM jq_role WHERE role_name = "ADMIN")  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active 
FROM jq_dashboard AS db  ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active)
SELECT UUID() AS entity_role_id, db.dashboard_id  AS entity_id, db.dashboard_name AS entity_name           
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "Dashboard") AS module_id 
  ,(SELECT role_id FROM jq_role WHERE role_name = "ANONYMOUS")  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active 
FROM jq_dashboard AS db ON DUPLICATE KEY UPDATE last_updated_date = NOW();



INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active)
SELECT UUID() AS entity_role_id, ml.module_id AS entity_id, mli18n.module_name AS entity_name           
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "Site Layout") AS module_id 
  ,(SELECT role_id FROM jq_role WHERE role_name = "AUTHENTICATED")  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active 
FROM jq_module_listing AS ml 
LEFT OUTER JOIN jq_module_listing_i18n AS mli18n ON ml.module_id = mli18n.module_id AND mli18n.language_id = 1 ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active)
SELECT UUID() AS entity_role_id, ml.module_id AS entity_id, mli18n.module_name AS entity_name           
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "Site Layout") AS module_id 
  ,(SELECT role_id FROM jq_role WHERE role_name = "ADMIN")  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active 
FROM jq_module_listing AS ml 
LEFT OUTER JOIN jq_module_listing_i18n AS mli18n ON ml.module_id = mli18n.module_id AND mli18n.language_id = 1 ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (entity_role_id,entity_id,entity_name,module_id,role_id,last_updated_date,last_updated_by,is_active)
SELECT UUID() AS entity_role_id, ml.module_id AS entity_id, mli18n.module_name AS entity_name           
  ,(SELECT module_id FROM jq_master_modules WHERE module_name = "Site Layout") AS module_id 
  ,(SELECT role_id FROM jq_role WHERE role_name = "ANONYMOUS")  AS role_id   
  ,NOW() AS last_updated_date, 'aar.dev@trigyn.com' AS last_updated_by, 1 AS is_active 
FROM jq_module_listing AS ml 
LEFT OUTER JOIN jq_module_listing_i18n AS mli18n ON ml.module_id = mli18n.module_id AND mli18n.language_id = 1 ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (entity_role_id  ,entity_id  ,entity_name  ,module_id  ,role_id  ,last_updated_date  ,last_updated_by  ,is_active
) SELECT  UUID()  AS entity_role_id  ,jdrd.jws_dynamic_rest_id AS entity_id ,jdrd.jws_method_name AS entity_name ,'47030ee1-0ecf-11eb-94b2-f48e38ab9348' AS module_id             
  ,'b4a0dda1-097f-11eb-9a16-f48e38ab9348' AS role_id ,now()  AS last_updated_date ,'aar.dev@trigyn.com' AS last_updated_by ,1 AS is_active             
FROM jq_dynamic_rest_details AS jdrd ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (entity_role_id  ,entity_id  ,entity_name  ,module_id  ,role_id  ,last_updated_date  ,last_updated_by  ,is_active
) SELECT  UUID()  AS entity_role_id  ,jdrd.jws_dynamic_rest_id AS entity_id ,jdrd.jws_method_name AS entity_name ,'47030ee1-0ecf-11eb-94b2-f48e38ab9348' AS module_id             
  ,'2ace542e-0c63-11eb-9cf5-f48e38ab9348' AS role_id ,now()  AS last_updated_date ,'aar.dev@trigyn.com' AS last_updated_by ,1 AS is_active             
FROM jq_dynamic_rest_details AS jdrd ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (entity_role_id  ,entity_id  ,entity_name  ,module_id  ,role_id  ,last_updated_date  ,last_updated_by  ,is_active
) SELECT  UUID()  AS entity_role_id  ,jdrd.jws_dynamic_rest_id AS entity_id ,jdrd.jws_method_name AS entity_name ,'47030ee1-0ecf-11eb-94b2-f48e38ab9348' AS module_id             
  ,'ae6465b3-097f-11eb-9a16-f48e38ab9348' AS role_id ,now()  AS last_updated_date ,'aar.dev@trigyn.com' AS last_updated_by ,1 AS is_active             
FROM jq_dynamic_rest_details AS jdrd ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (entity_role_id  ,entity_id  ,entity_name  ,module_id  ,role_id  ,last_updated_date  ,last_updated_by  ,is_active
) SELECT  UUID()  AS entity_role_id  ,ad.ac_id AS entity_id ,ad.ac_id AS entity_name ,'91a81b68-0ece-11eb-94b2-f48e38ab9348' AS module_id             
  ,'b4a0dda1-097f-11eb-9a16-f48e38ab9348' AS role_id ,now()  AS last_updated_date ,'aar.dev@trigyn.com' AS last_updated_by ,1 AS is_active             
FROM jq_autocomplete_details as ad ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (entity_role_id  ,entity_id  ,entity_name  ,module_id  ,role_id  ,last_updated_date  ,last_updated_by  ,is_active
) SELECT  UUID()  AS entity_role_id  ,ad.ac_id AS entity_id ,ad.ac_id AS entity_name ,'91a81b68-0ece-11eb-94b2-f48e38ab9348' AS module_id             
  ,'2ace542e-0c63-11eb-9cf5-f48e38ab9348' AS role_id ,now()  AS last_updated_date ,'aar.dev@trigyn.com' AS last_updated_by ,1 AS is_active             
FROM jq_autocomplete_details AS ad ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (entity_role_id  ,entity_id  ,entity_name  ,module_id  ,role_id  ,last_updated_date  ,last_updated_by  ,is_active
) SELECT  UUID()  AS entity_role_id  ,ad.ac_id AS entity_id ,ad.ac_id AS entity_name ,'91a81b68-0ece-11eb-94b2-f48e38ab9348' AS module_id             
  ,'ae6465b3-097f-11eb-9a16-f48e38ab9348' AS role_id ,now()  AS last_updated_date ,'aar.dev@trigyn.com' AS last_updated_by ,1 AS is_active             
FROM jq_autocomplete_details AS ad ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,gd.grid_id AS entity_id             
  ,gd.grid_name AS entity_name            
  ,'07067149-098d-11eb-9a16-f48e38ab9348' AS module_id              
  ,'2ace542e-0c63-11eb-9cf5-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_grid_details gd ON DUPLICATE KEY UPDATE last_updated_date = NOW();
INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,gd.grid_id AS entity_id             
  ,gd.grid_name AS entity_name            
  ,'07067149-098d-11eb-9a16-f48e38ab9348' AS module_id              
  ,'ae6465b3-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_grid_details gd ON DUPLICATE KEY UPDATE last_updated_date = NOW();
INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,gd.grid_id AS entity_id             
  ,gd.grid_name AS entity_name            
  ,'07067149-098d-11eb-9a16-f48e38ab9348' AS module_id              
  ,'b4a0dda1-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_grid_details gd ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,tm.template_id AS entity_id             
  ,tm.template_name AS entity_name            
  ,'1b0a2e40-098d-11eb-9a16-f48e38ab9348' AS module_id              
  ,'2ace542e-0c63-11eb-9cf5-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_template_master tm ON DUPLICATE KEY UPDATE last_updated_date = NOW();
INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
 ,tm.template_id AS entity_id             
  ,tm.template_name AS entity_name            
  ,'1b0a2e40-098d-11eb-9a16-f48e38ab9348' AS module_id              
  ,'ae6465b3-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_template_master tm ON DUPLICATE KEY UPDATE last_updated_date = NOW();
INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,tm.template_id AS entity_id             
  ,tm.template_name AS entity_name            
  ,'1b0a2e40-098d-11eb-9a16-f48e38ab9348' AS module_id              
  ,'b4a0dda1-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_template_master tm ON DUPLICATE KEY UPDATE last_updated_date = NOW();


INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,df.form_id AS entity_id             
  ,df.form_name AS entity_name            
  ,'30a0ff61-0ecf-11eb-94b2-f48e38ab9348' AS module_id              
  ,'2ace542e-0c63-11eb-9cf5-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_dynamic_form df ON DUPLICATE KEY UPDATE last_updated_date = NOW();
INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,df.form_id AS entity_id             
  ,df.form_name AS entity_name            
  ,'30a0ff61-0ecf-11eb-94b2-f48e38ab9348' AS module_id              
  ,'ae6465b3-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_dynamic_form df ON DUPLICATE KEY UPDATE last_updated_date = NOW();
INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,df.form_id AS entity_id             
  ,df.form_name AS entity_name           
  ,'30a0ff61-0ecf-11eb-94b2-f48e38ab9348' AS module_id              
  ,'b4a0dda1-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_dynamic_form df ON DUPLICATE KEY UPDATE last_updated_date = NOW();

INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,ml.module_id AS entity_id             
  ,ml_i18n.module_name AS entity_name            
  ,'c6cc466a-0ed3-11eb-94b2-f48e38ab9348' AS module_id              
  ,'2ace542e-0c63-11eb-9cf5-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_module_listing ml INNER JOIN jq_module_listing_i18n ml_i18n ON ml.module_id = ml_i18n.module_id AND ml_i18n.language_id=1 ON DUPLICATE KEY UPDATE last_updated_date = NOW();
INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,ml.module_id AS entity_id             
  ,ml_i18n.module_name AS entity_name             
  ,'c6cc466a-0ed3-11eb-94b2-f48e38ab9348' AS module_id              
  ,'ae6465b3-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_module_listing ml INNER JOIN jq_module_listing_i18n ml_i18n ON ml.module_id = ml_i18n.module_id AND ml_i18n.language_id=1 ON DUPLICATE KEY UPDATE last_updated_date = NOW();
INSERT INTO jq_entity_role_association  (
   entity_role_id
  ,entity_id
  ,entity_name
  ,module_id
  ,role_id
  ,last_updated_date
  ,last_updated_by
  ,is_active
) SELECT 
   UUID()  AS entity_role_id         
  ,ml.module_id AS entity_id             
  ,ml_i18n.module_name AS entity_name           
  ,'c6cc466a-0ed3-11eb-94b2-f48e38ab9348' AS module_id              
  ,'b4a0dda1-097f-11eb-9a16-f48e38ab9348' AS role_id                
  ,now()  AS last_updated_date      
  ,'admin' AS last_updated_by        
  ,1 AS is_active              
FROM jq_module_listing ml INNER JOIN jq_module_listing_i18n ml_i18n ON ml.module_id = ml_i18n.module_id AND ml_i18n.language_id=1 ON DUPLICATE KEY UPDATE last_updated_date = NOW();

