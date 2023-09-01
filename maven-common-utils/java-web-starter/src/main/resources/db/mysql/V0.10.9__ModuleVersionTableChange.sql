ALTER TABLE jq_module_version  
DROP INDEX entity_id
, ADD UNIQUE KEY `entity_id` (`entity_id`,`entity_name`,`version_id`);