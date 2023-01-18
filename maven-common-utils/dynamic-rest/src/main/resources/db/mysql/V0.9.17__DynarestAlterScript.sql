ALTER TABLE jq_dynamic_rest_details ADD COLUMN service_logic_checksum VARCHAR(512);
ALTER TABLE jq_dynamic_rest_dao_details ADD COLUMN dao_query_checksum VARCHAR(512);


DELETE FROM jq_property_master WHERE property_name='passwordExpiry';

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('fac78c8f-3c9c-11ec-88bb-9840bb1e8144', 'system', 'system', 'passwordExpiry', '0', 0, NOW(), 'admin', 1.00, 'Password expiry, in days, if password/password + captcha is enabled');

UPDATE jq_property_master SET property_master_id = 'e887b756-1a8f-11eb-98d3-f48e38ab8cd7'  WHERE property_name='file-upload-location';


DELETE FROM jq_property_master WHERE property_name='user-profile-template-details';

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('006d4f2a-3c9d-11ec-88bb-9840bb1e8144', 'system', 'system', 'user-profile-template-details', '{}', 0, NOW(), 'admin', 1.00, 'Custom template for user profile listing page');
