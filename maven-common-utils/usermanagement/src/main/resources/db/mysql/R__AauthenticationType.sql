REPLACE  INTO `jq_authentication_type`(`authentication_id`,`authentication_name`,`authentication_properties`) values 
(1,'In Memory Authentication',NULL),
(2,'Database Authentication','{"authenticationType":{"name":"enableDatabaseAuthentication","type":"hidden","textValue":"Database Authentication","value":"true","configurationType":"single"},"authenticationDetail":{"configurations":[[{"name":"enableVerificationStep","type":"hidden","textValue":"Verification Step","required":false,"value":"true","additionalDetails":{"additionalProperties":[[{"name":"verificationType","type":"select","textValue":"Verification","required":true,"value":"0","defaultValue":"0","dropDownData":[{"name":"OTP","value":0},{"name":"Password","value":1},{"name":"TOTP","value":2}]},{"name":"passwordExpiry","type":"select","textValue":"Password Expiry","required":true,"selected":true,"value":"30","defaultValue":"30","dropDownData":[{"name":"30","value":30},{"name":"45","value":45},{"name":"60","value":60},{"name":"75","value":75},{"name":"90","value":90},{"name":"Never","value":0}]},{"name":"enableCaptcha","type":"boolean","textValue":"Captcha","required":false,"value":"false","defaultValue":"true"}]]}},{"name":"enableRegex","type":"boolean","textValue":"Regex","required":true,"value":"false","additionalDetails":{"additionalProperties":[[{"name":"regexPattern","type":"text","textValue":"Expression","required":false,"value":""},{"name":"message","type":"text","textValue":"Message","required":false,"value":"Password length 6-20"}]]}},{"name":"enableRegistration","type":"boolean","textValue":"Registration","required":false,"value":"true"},{"name":"enableDynamicForm","type":"boolean","textValue":"Custom Profile Page","required":false,"value":"false","additionalDetails":{"additionalProperties":[[{"name":"formName","type":"formAutocomplete","textValue":"Form Name","required":true,"value":""},{"name":"templateName","type":"templateAutocomplete","textValue":"Template Name","required":true,"value":""}]]}}]]}}'),
(3,'LDAP Authentication','{"authenticationType":{"name":"enableLdapAuthentication","type":"hidden","textValue":"LDAP Authentication","value":"true","configurationType":"single"},"authenticationDetail":{"configurations":[[{"name":"displayName","type":"text","textValue":"Display Name","required":true,"value":"trigyn.local"},{"name":"ldapAddress","type":"text","textValue":"Server Host","required":true,"value":"192.168.102.6"},{"name":"ldapPort","type":"text","textValue":"Server Port","required":true,"value":"389"},{"name":"ldapSecurityType","type":"select","textValue":"Security Level","required":true,"value":"0","defaultValue":"0","dropDownData":[{"name":"Simple","value":0},{"name":"SASL","value":1},{"name":"Start TLS","value":2}]},{"name":"basedn","type":"text","textValue":"Base Domain Name","required":true,"value":"DC=trigyn,DC=com"},{"name":"userdn","type":"text","textValue":"Bind Domain Name","required":true,"value":"OU=Trigyn_Mumbai_users"},{"name":"adminUserName","type":"text","textValue":"Admin User Name","required":true,"value":"Shrinath.Halki@trigyn.com"},{"name":"adminPassword","type":"password","textValue":"Password","required":true,"value":"mca05@git"},{"name":"loginAttribute","type":"text","textValue":"Login Attribute","required":true,"value":"mail"},{"name":"ldapSearchScope","type":"select","textValue":"Search Scope","required":true,"value":"2","defaultValue":"0","dropDownData":[{"name":"Base","value":0},{"name":"One","value":1},{"name":"Subtree","value":2}]},{"name":"ldapSearchFilter","type":"text","textValue":"Search Filter","required":true,"value":"(&(|(samAccountName={0})(userPrincipalName={0})(cn={0}))(objectClass=user))"},{"name":"testLdapAuth","type":"button","textValue":"Test Connection","required":true,"value":"Test"},{"name":"removeLdapAuth","type":"button","textValue":"Remove LDAP","required":true,"value":"Test"}],[{"name":"displayName","type":"text","textValue":"Display Name","required":true,"value":"localhost"},{"name":"ldapAddress","type":"text","textValue":"Server Host","required":true,"value":"localhost"},{"name":"ldapPort","type":"text","textValue":"Server Port","required":true,"value":"10389"},{"name":"ldapSecurityType","type":"select","textValue":"Security Level","required":true,"value":"0","defaultValue":"0","dropDownData":[{"name":"Simple","value":0},{"name":"SASL","value":1},{"name":"Start TLS","value":2}]},{"name":"basedn","type":"text","textValue":"Base Domain Name","required":true,"value":"DC=trigyn,DC=com"},{"name":"userdn","type":"text","textValue":"Bind Domain Name","required":true,"value":"OU=Trigyn_Mumbai_users"},{"name":"adminUserName","type":"text","textValue":"Admin User Name","required":true,"value":"admin"},{"name":"adminPassword","type":"password","textValue":"Password","required":true,"value":"secret"},{"name":"loginAttribute","type":"text","textValue":"Login Attribute","required":true,"value":"mail"},{"name":"ldapSearchScope","type":"select","textValue":"Search Scope","required":true,"value":"2","defaultValue":"0","dropDownData":[{"name":"Base","value":0},{"name":"One","value":1},{"name":"Subtree","value":2}]},{"name":"ldapSearchFilter","type":"text","textValue":"Search Filter","required":true,"value":"(&(|(samAccountName={0})(userPrincipalName={0})(cn={0}))(objectClass=user))"},{"name":"testLdapAuth","type":"button","textValue":"Test Connection","required":true,"value":"Test"},{"name":"removeLdapAuth","type":"button","textValue":"Remove LDAP","required":true,"value":"Test"}]]}}'),
(4,'Oauth','{"authenticationType":{"name":"oauth-clients","type":"hidden","textValue":"Client","value":"true","configurationType":"multi"},"authenticationDetail":{"configurations":[[{"name":"oauth-client","type":"multiselect","textValue":"Client","required":true,"defaultValue":"-1","dropDownData":[{"name":"google","selected":true,"value":0,"type":"google","additionalDetails":{"additionalProperties":[[{"name":"displayName","type":"text","textValue":"Display Name","required":true,"value":"google"},{"name":"registration-id","type":"text","textValue":"Registration Id","required":true,"value":"google"},{"name":"client-id","type":"text","textValue":"Client Id","required":true,"value":"63432668967-agbbacdk6shuhbo266hlobm3l9rdm1ra.apps.googleusercontent.com"},{"name":"client-secret","type":"text","textValue":"Client Secret","required":true,"value":"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"}]]}},{"name":"facebook","selected":true,"value":1,"type":"facebook","additionalDetails":{"additionalProperties":[[{"name":"displayName","type":"text","textValue":"Display Name","required":true,"value":"facebook"},{"name":"registration-id","type":"text","textValue":"Registration Id","required":true,"value":"facebook"},{"name":"client-id","type":"text","textValue":"Client Id","required":true,"value":"Facebook-apps.facebookeusercontent.com"},{"name":"client-secret","type":"text","textValue":"Client Secret","required":true,"value":"Facebook-tk4aHJiyPd66-EC8-cNy2KH6LLQM"}]]}},{"name":"office-365","selected":true,"value":2,"type":"office365","additionalDetails":{"additionalProperties":[[{"name":"displayName","type":"text","textValue":"Display Name","required":true,"value":"Office-365"},{"name":"registration-id","type":"text","textValue":"Registration Id","required":true,"value":"office365"},{"name":"client-id","type":"text","textValue":"Client Id","required":true,"value":"1bbb2e8a-d6e2-4de3-8f99-1113f7386be1"},{"name":"client-secret","type":"text","textValue":"Client Secret","required":true,"value":"C8B8Q~b9msIKGg4XgRA.mF4e2sGsGf8eAjWY1arA"},{"name":"remove-office-tenant","type":"button","textValue":"Remove tenant","required":true,"value":"removeTenant"}]]}}]}]]}}');