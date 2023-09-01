SET FOREIGN_KEY_CHECKS=0;

UPDATE jq_master_modules 
SET module_name = "REST API" 
WHERE module_id = '47030ee1-0ecf-11eb-94b2-f48e38ab9348';

ALTER TABLE jq_master_modules
 ADD sequence INT(11) AFTER module_name;
 
 ALTER TABLE jq_master_modules
 ADD grid_details_id varchar(50) AFTER module_type_id;
 
 ALTER TABLE jq_master_modules
 ADD module_type varchar(50) AFTER grid_details_id;
 
 ALTER TABLE jq_master_modules
 ADD is_perm_supported INT(11) AFTER module_type;
 
 ALTER TABLE jq_master_modules
 ADD is_entity_perm_supported INT(11) AFTER module_type;
 
 ALTER TABLE jq_master_modules
 ADD is_imp_exp_supported INT(11) AFTER is_entity_perm_supported;
 
 DROP TABLE jq_master_module_role_association;
 DROP table jq_master_module;
 
 UPDATE jq_authentication_type SET authentication_properties ='[{"name":"enableVerificationStep","type":"boolean","textValue":"Verification","required": true,"value":true,"selectedValue":"0"}
,{"name":"enableRegex","type":"boolean","textValue":"Regex","required": false,"value":true}
,{"name":"enableRegistration","type":"boolean","textValue":"Registration","required": false,"value":false}
,{"name":"enableDynamicForm","type":"boolean","textValue":"Custom Profile Page","required": false,"value":false}]' 
WHERE authentication_id=2;
 

SET FOREIGN_KEY_CHECKS=1;