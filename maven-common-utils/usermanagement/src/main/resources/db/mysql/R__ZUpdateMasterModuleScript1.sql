DELETE FROM `jq_role_master_modules_association` WHERE module_id = '699ac104-8c8f-11eb-8dcd-0242ac130003';

DELETE FROM `jq_master_modules` WHERE module_id = '699ac104-8c8f-11eb-8dcd-0242ac130003';

UPDATE jq_master_modules SET module_name = "Internalization" WHERE module_id = '5559212c-8c8f-11eb-8dcd-0242ac130003';