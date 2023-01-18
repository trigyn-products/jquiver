REPLACE INTO jq_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('e887b756-1a8f-11eb-98d3-f48e38ab1234','system', 'system', 'version', 'JQuiver 1.4.5-SNAPSHOT', 0, NOW(), 'admin', 1.00, 'Application version');

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('f706687c-1a8e-11eb-98d3-f48e38ab8cd7','system', 'system', 'template-storage-path', 'D:\\commons\\documents', 0, NOW(), 'admin', 1.00, 'Path at which the template will be stored during local development.');


REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('eb7e5c53-1a8e-11eb-98d3-f48e38ab8cd7','system', 'system', 'profile', 'prod', 0, NOW(), 'admin', 1.00, 'Checks the profile in which the application is running.');

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('eb7e5c53-1a8e-11eb-98d3-f48e38ab8123','system', 'system', 'isStaticImportDone', 'false', 0, NOW(), 'admin', 1.4, 'Check whether database is imported for the first time.');
