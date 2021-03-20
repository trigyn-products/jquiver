 ALTER TABLE jq_user
 ADD registered_by INT(2) AFTER secret_key;
 
 
UPDATE  jq_user SET registered_by=2  WHERE user_id="111415ae-0980-11eb-9a16-f48e38ab9348"; 

UPDATE jq_authentication_type SET authentication_properties ='[{"name":"google","type":"select","selected":false,"client-id":"95167754409-2vlhnv114r2o59hhrier3gpq1ftr1nu9.apps.googleusercontent.com","client-secret":"6Tx6P3oYKDeAGZiSn2Ht9jUh","value":0},
{"name":"facebook","type":"select","selected":false,"client-id":"139431221079289","client-secret":"8ce58d7ef4d7fe821ea83736e77c25c3","value":1},
{"name":"office365","type":"select","selected":true,"client-id":"c266ee35-512a-4045-8ab7-c3c1ed8e8fa2","client-secret":"6D2Z_gI-jdk5H~wKJz8MO3Rm2.8VKs9F6h","value":2}]' WHERE authentication_id=4;


ALTER TABLE jq_user
 ADD failed_attempt INT(2) Default 0 AFTER registered_by;
 