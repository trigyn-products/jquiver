UPDATE jws_authentication_type SET authentication_properties ='[{"name":"enableRegistration","type":"boolean","textValue":"Registration","value":true},{"name":"enableVerificationStep","type":"boolean","textValue":"Verification","value":true,"selectedValue":"0"},{"name":"enableDynamicForm","type":"boolean","textValue":"Custom Profile Page","value":true},{"name":"enableRegex","type":"boolean","textValue":"Regex","value":true}]' WHERE authentication_id=2;

ALTER TABLE jws_user
 ADD secret_key VARCHAR(50) AFTER force_password_change;
 
UPDATE  jws_user SET secret_key = "XJZI7XO2YD2PEZJQU3RLS4TBDTPL47AJ"  WHERE user_id="111415ae-0980-11eb-9a16-f48e38ab9348"; 

