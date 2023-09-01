SET FOREIGN_KEY_CHECKS=0;

UPDATE jq_module_listing 
SET last_modified_date = NOW(), header_json = '{"Powered-By":"JQuiver","Content-Language":"en"}'
WHERE module_type_id = 2 AND header_json IS NULL; 

SET FOREIGN_KEY_CHECKS=1;