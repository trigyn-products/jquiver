REPLACE INTO jq_property_master (property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('e887b756-1a8f-11eb-98d3-f48e38ab1234','system', 'system', 'version', 'JQuiver 1.5.0', 0, NOW(), 'admin', 1.00, 'Application version');

UPDATE jq_user SET email = "admin@jquiver.io", force_password_change = 1 WHERE email="admin@trigyn.com";
