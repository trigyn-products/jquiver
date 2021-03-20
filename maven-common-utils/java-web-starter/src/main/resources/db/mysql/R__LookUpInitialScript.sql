SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'a2523a43-12d9-11eb-9d0d-f48e38ab8cd7','GRID_TYPE',1,0 );

REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'a8ce4fda-12d9-11eb-9d0d-f48e38ab8cd7','GRID_TYPE',2,0 );

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'ad69a32b-12d9-11eb-9d0d-f48e38ab8cd7','a2523a43-12d9-11eb-9d0d-f48e38ab8cd7',1,'Default Grid');

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'ad69a32b-12d9-11eb-9d0d-f48e38ab8cd7','a8ce4fda-12d9-11eb-9d0d-f48e38ab8cd7',1,'System Grid');



REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'5c2c7243-12fc-11eb-9d0d-f48e38ab8cd7','AUTOCOMPLETE_TYPE',1,0 );

REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'60a0b54e-12fc-11eb-9d0d-f48e38ab8cd7','AUTOCOMPLETE_TYPE',2,0 );

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'65ff6ca5-12fc-11eb-9d0d-f48e38ab8cd7','5c2c7243-12fc-11eb-9d0d-f48e38ab8cd7',1,'Default Autocomplete/Multiselect');

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'6907cba1-12fc-11eb-9d0d-f48e38ab8cd7','60a0b54e-12fc-11eb-9d0d-f48e38ab8cd7',1,'System Autocomplete/Multiselect');




REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'a6bb0b9f-1250-11eb-9d0d-f48e38ab8cd7','TEMPLATE_TYPE',1,0 );

REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'aa7a487b-1250-11eb-9d0d-f48e38ab8cd7','TEMPLATE_TYPE',2,0 );

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'f438a3a2-1251-11eb-9d0d-f48e38ab8cd7','a6bb0b9f-1250-11eb-9d0d-f48e38ab8cd7',1,'Default Template');

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'd84c07be-1251-11eb-9d0d-f48e38ab8cd7','aa7a487b-1250-11eb-9d0d-f48e38ab8cd7',1,'System Template');




REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'45555f3a-12ba-11eb-9d0d-f48e38ab8cd7','DYNAMIC_FORM_TYPE',1,0 );

REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'4a298ea2-12ba-11eb-9d0d-f48e38ab8cd7','DYNAMIC_FORM_TYPE',2,0 );

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'4d38886b-12ba-11eb-9d0d-f48e38ab8cd7','45555f3a-12ba-11eb-9d0d-f48e38ab8cd7',1,'Default Form');

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'513ebf08-12ba-11eb-9d0d-f48e38ab8cd7','4a298ea2-12ba-11eb-9d0d-f48e38ab8cd7',1,'System Form');



REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'ab4837da-12fc-11eb-9d0d-f48e38ab8cd7','DYNAREST_TYPE',1,0 );

REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'b038b383-12fc-11eb-9d0d-f48e38ab8cd7','DYNAREST_TYPE',2,0 );

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'b48368b9-12fc-11eb-9d0d-f48e38ab8cd7','ab4837da-12fc-11eb-9d0d-f48e38ab8cd7',1,'Default Dynarest');

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'b722f2f8-12fc-11eb-9d0d-f48e38ab8cd7','b038b383-12fc-11eb-9d0d-f48e38ab8cd7',1,'System Dynarest');




REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'18ee5193-12fc-11eb-9d0d-f48e38ab8cd7','DASHBOARD_TYPE',1,0 );

REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'311151fc-12fc-11eb-9d0d-f48e38ab8cd7','DASHBOARD_TYPE',2,0 );

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'343737f9-12fc-11eb-9d0d-f48e38ab8cd7','18ee5193-12fc-11eb-9d0d-f48e38ab8cd7',1,'Default Dashboard');

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'37f1c643-12fc-11eb-9d0d-f48e38ab8cd7','311151fc-12fc-11eb-9d0d-f48e38ab8cd7',1,'System Dashboard');




REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'3a143466-12da-11eb-9d0d-f48e38ab8cd7','DASHLET_TYPE',1,0 );

REPLACE INTO jq_lookup (lookup_id,lookup_name,record_id,is_deleted) VALUES (
'3f385f5a-12da-11eb-9d0d-f48e38ab8cd7','DASHLET_TYPE',2,0 );

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'434a8749-12da-11eb-9d0d-f48e38ab8cd7','3a143466-12da-11eb-9d0d-f48e38ab8cd7',1,'Default Dashlet');

REPLACE INTO jq_lookup_i18n (id,jws_lookup_id,language_id,record_description) VALUES (
'4f564977-12da-11eb-9d0d-f48e38ab8cd7','3f385f5a-12da-11eb-9d0d-f48e38ab8cd7',1,'System Dashlet');



SET FOREIGN_KEY_CHECKS=1;