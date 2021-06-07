SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id, created_by, created_date, last_updated_ts) VALUES
('eb4b4344-901d-436b-82a3-cc07a8b2223c', 'saveDDOSDetails', 1, 'saveDDOSDetails', 'Save DDOS Details', 1, 7, 'com.trigyn.jws.security.service.SecurityManagementService', 1, 0, 2, 'aar.dev@trigyn.com', NOW(), NOW());



REPLACE INTO jq_dynamic_rest_dao_details 
(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(49, 'eb4b4344-901d-436b-82a3-cc07a8b2223c', 'ddosExcludedExtensions', 'REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES (''c15e2e2a-2fb4-11eb-a009-f48e38ab8cd7'', ''system'', ''system'', ''ddos-excluded-extensions'', :ddosExcludedExtensions, 0, NOW(), ''admin'', 1.00, ''Excluded extensions for DDOS attack'');', 1, 2);

REPLACE INTO jq_dynamic_rest_dao_details 
(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(50, 'eb4b4344-901d-436b-82a3-cc07a8b2223c', 'ddosRefreshInterval', 'REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES (''c6c14bcd-2fb4-11eb-a009-f48e38ab8cd7'', ''system'', ''system'', ''ddos-refresh-interval'', :ddosRefreshInterval, 0, NOW(), ''admin'', 1.00, ''DDOS page interval in seconds'');', 2, 2);


REPLACE INTO jq_dynamic_rest_dao_details 
(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(51, 'eb4b4344-901d-436b-82a3-cc07a8b2223c', 'blockedIpAddress', 'REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES (''f5cf07dc-2fb4-11eb-a009-f48e38ab8cd7'', ''system'', ''system'', ''blocked-ip-address'', :blockedIpAddress, 0, NOW(), ''admin'', 1.00, ''List of blocked ip address'');', 3, 2);


REPLACE INTO jq_dynamic_rest_dao_details 
(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(52, 'eb4b4344-901d-436b-82a3-cc07a8b2223c', 'ddosPageCount', 'REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES (''f9cb8796-2fb4-11eb-a009-f48e38ab8cd7'', ''system'', ''system'', ''ddos-page-count'', :ddosPageCount, 0, NOW(), ''admin'', 1.00, ''DDOS page count'');', 4, 2);


REPLACE INTO jq_dynamic_rest_dao_details 
(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(53, 'eb4b4344-901d-436b-82a3-cc07a8b2223c', 'isDDOSEnabled', 'UPDATE jq_security_type 
SET  is_active = :isDDOSEnabled
WHERE security_type_id = ''a1fa90e3-32dd-11eb-a009-f48e38ab8cd7'';', 5, 2);


REPLACE INTO jq_dynamic_rest_dao_details 
(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(66, 'eb4b4344-901d-436b-82a3-cc07a8b2223c', 'ddosSiteCount', 'REPLACE INTO jq_property_master(property_master_id, owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES (''f406bf3a-37bd-11eb-a23b-f48e38ab8cd7'', ''system'', ''system'', ''ddos-site-count'', :ddosSiteCount, 0, NOW(), ''admin'', 1.00, ''DDOS site count'');', 7, 2);


REPLACE INTO jq_dynamic_rest_dao_details 
(jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(59, 'eb4b4344-901d-436b-82a3-cc07a8b2223c', 'inMemeoryEnabled', 'REPLACE INTO jq_security_properties (security_properties_id, security_type_id,security_property_name, security_property_value) VALUES 
(''c238f528-32fa-11eb-a009-f48e38ab8cd7'', ''a1fa90e3-32dd-11eb-a009-f48e38ab8cd7'', ''In Memory'', :inMemoryEnabled);', 6, 2);

UPDATE jq_dynamic_rest_details SET
jws_service_logic = 'com.trigyn.jws.security.service.SecurityManagementService'
WHERE jws_method_name = 'saveDDOSDetails';


SET FOREIGN_KEY_CHECKS=1;