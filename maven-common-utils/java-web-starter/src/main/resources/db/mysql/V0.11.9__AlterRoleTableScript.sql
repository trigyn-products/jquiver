ALTER TABLE jq_role DROP IF EXISTS role_priority;
ALTER TABLE jq_role ADD role_priority INT(11) AFTER is_active;

UPDATE jq_role SET role_priority = 1 WHERE role_id = 'b4a0dda1-097f-11eb-9a16-f48e38ab9348';
UPDATE jq_role SET role_priority = 2 WHERE role_id = '2ace542e-0c63-11eb-9cf5-f48e38ab9348';
UPDATE jq_role SET role_priority = 3 WHERE role_id = 'ae6465b3-097f-11eb-9a16-f48e38ab9348';

REPLACE INTO jq_property_master(property_master_id,owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('92b31712-692d-11eb-9737-f48e38ab8cd7','system', 'system', 'default-redirect-url', '/cf/home', 0, NOW(), 'admin', 1.00, 'Default application URL after successful login.');
