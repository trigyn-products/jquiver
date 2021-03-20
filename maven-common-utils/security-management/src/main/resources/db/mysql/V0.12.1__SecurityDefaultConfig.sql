SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('c15e2e2a-2fb4-11eb-a009-f48e38ab8cd7', 'system', 'system', 'ddos-excluded-extensions', 'jpeg, jpg, png, woff2, svg, gif, ico, css, js', 0, NOW(), 'admin', 1.00, 'Excluded extensions for DDOS attack');

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('c6c14bcd-2fb4-11eb-a009-f48e38ab8cd7', 'system', 'system', 'ddos-refresh-interval', 30, 0, NOW(), 'admin', 1.00, 'DDOS refresh interval in seconds');

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('f5cf07dc-2fb4-11eb-a009-f48e38ab8cd7', 'system', 'system', 'blocked-ip-address', '', 0, NOW(), 'admin', 1.00, 'List of blocked ip address');

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('f9cb8796-2fb4-11eb-a009-f48e38ab8cd7', 'system', 'system', 'ddos-page-count', 30, 0, NOW(), 'admin', 1.00, 'DDOS page count');

REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('f406bf3a-37bd-11eb-a23b-f48e38ab8cd7', 'system', 'system', 'ddos-site-count', 50, 0, NOW(), 'admin', 1.00, 'DDOS site count');

SET FOREIGN_KEY_CHECKS=1;