UPDATE jq_authentication_type SET authentication_properties ='[{"name":"enableRegistration","type":"boolean","value":"true"},{"name":"enableCaptcha","type":"boolean","value":"true"},{"name":"enableRegex","type":"boolean","value":"true"}]' WHERE authentication_id=2;

ALTER TABLE jq_user
 ADD force_password_change INT(2) NOT NULL DEFAULT '0' AFTER is_active;