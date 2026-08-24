UPDATE jq_authentication_type
SET
authentication_properties=CASE
WHEN JSON_SEARCH(authentication_properties,'one','testOauthAuth',NULL,'$.authenticationDetail.configurations[0][*].name')IS NULL
THEN JSON_SET(authentication_properties,'$.authenticationDetail.configurations[0]',JSON_ARRAY_APPEND(JSON_EXTRACT(authentication_properties,'$.authenticationDetail.configurations[0]'),'$',JSON_OBJECT('name','testOauthAuth','type','button','textValue','Test','required',true,'value','Test')))
ELSE authentication_properties
END,default_auth_properties=CASE
WHEN JSON_SEARCH(default_auth_properties,'one','testOauthAuth',NULL,'$.authenticationDetail.configurations[0][*].name')IS NULL
THEN JSON_SET(default_auth_properties,'$.authenticationDetail.configurations[0]',JSON_ARRAY_APPEND(JSON_EXTRACT(default_auth_properties,'$.authenticationDetail.configurations[0]'),'$',JSON_OBJECT('name','testOauthAuth','type','button','textValue','Test','required',true,'value','Test')))
ELSE default_auth_properties
END
WHERE authentication_id=4;



