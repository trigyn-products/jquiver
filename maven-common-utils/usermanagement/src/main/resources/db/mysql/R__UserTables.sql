SET FOREIGN_KEY_CHECKS=0;

REPLACE  INTO  jq_property_master (
property_master_id
  ,owner_type
  ,owner_id
  ,property_name
  ,property_value
  ,is_deleted
  ,last_modified_date
  ,modified_by
  ,app_version
  ,comments
) VALUES (
	'51849604-1a8f-11eb-98d3-f48e38ab8cd7'
  ,'system'
  ,'system'
  ,'enable-user-management'
  ,'false'
  ,0
  ,now()
  ,'admin'
  ,'1.000'
  ,'By default user management will be disabled' 
);


   REPLACE  INTO  jq_property_master (
  property_master_id
  ,owner_type
  ,owner_id
  ,property_name
  ,property_value
  ,is_deleted
  ,last_modified_date
  ,modified_by
  ,app_version
  ,comments
) VALUES (
   '3b2838cc-1a8f-11eb-98d3-f48e38ab8cd7'
  ,'system'
  ,'system'
  ,'authentication-type'
  ,'1'
  ,0
  ,now()
  ,'admin'
  ,'1.000'
  ,'Authentication type - default in memory authentication' 
);

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('006d0d5a-3c9d-11ec-88bb-9840bb1e8144', 'system', 'system', 'base-url', 'http://localhost:8080', 0, NOW(), 'admin', 1.00, 'Base url of application');

SET FOREIGN_KEY_CHECKS=1;