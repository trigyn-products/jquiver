ALTER TABLE jq_user
 ADD last_password_updated_date timestamp NOT NULL DEFAULT current_timestamp()
 AFTER failed_attempt;
 
 
 UPDATE jq_authentication_type SET authentication_properties ='[{"name":"enableVerificationStep","type":"boolean","textValue":"Verification","required": true,"value":true,"selectedValue":"0"}
,{"name":"enableRegex","type":"boolean","textValue":"Regex","required": false,"value":true}
,{"name":"enableRegistration","type":"boolean","textValue":"Registration","required": false,"value":false}
,{"name":"enableDynamicForm","type":"boolean","textValue":"Custom Profile Page","required": false,"value":false}
,{"name":"enablePasswordExpiry","type":"boolean","textValue":"Password Expiry","required": false,"value":false,"selectedValue":"30"}]' 
WHERE authentication_id=2;


REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES (UUID(), 'system', 'system', 'passwordExpiry', '0', 0, NOW(), 'admin', 1.00, 'Password expiry, in days, if password/password + captcha is enabled');