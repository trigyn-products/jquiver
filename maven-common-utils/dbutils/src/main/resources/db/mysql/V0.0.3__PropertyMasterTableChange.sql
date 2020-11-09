TRUNCATE jws_property_master;

ALTER TABLE jws_property_master ADD COLUMN property_master_id VARCHAR(50) DEFAULT NULL;

ALTER TABLE jws_property_master MODIFY property_value LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;


REPLACE INTO jws_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('eb7e5c53-1a8e-11eb-98d3-f48e38ab8cd7','system', 'system', 'profile', 'prod', 0, NOW(), 'admin', 1.00, 'Checks the profile in which the application is running.');

REPLACE INTO jws_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('f706687c-1a8e-11eb-98d3-f48e38ab8cd7','system', 'system', 'template-storage-path', 'D:\\commons\\documents', 0, NOW(), 'admin', 1.00, 'Path at which the template will be stored during local development.');

REPLACE INTO jws_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments) 
VALUES ('fc76b7e8-1a8e-11eb-98d3-f48e38ab8cd7','system', 'system', 'acl-jws', 'admin,maintainer', 0, NOW(), 'admin', 1.0000, 'List of roles authorized to access jws admin panel.');


REPLACE INTO jws_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('6966efd5-1a8f-11eb-98d3-f48e38ab8cd7','system', 'system', 'max-version-count', '10', 0, NOW(), 'admin', 1.00, 'Maximum limit to persist versioning data');


REPLACE INTO jws_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('e21f365f-1ce3-11eb-953d-f48e38ab8cd7','system', 'system', 'jws-date-format', '{​​​​ js:"%d-%m-%y %r", java:"%d/%m/%y %r", db:"%d/%m/%y %r" }​​​​', 0, NOW(), 'admin', 1.00, 'Date Format');
