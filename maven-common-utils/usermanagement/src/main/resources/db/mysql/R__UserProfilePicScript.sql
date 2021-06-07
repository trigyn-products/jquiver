SET FOREIGN_KEY_CHECKS=0;

REPLACE INTO jq_file_upload_config (file_bin_id, file_type_supported, max_file_size, no_of_files, select_query_content, upload_query_content, view_query_content, delete_query_content, is_deleted, created_by, created_date, last_updated_ts) VALUES
('profilePic', '.png, .gif, .jpeg, .jpg', 1048576, 1, 

/* default - SELECT Query start */
'SELECT jfu.file_association_id AS fileAssociationId, jfu.file_bin_id AS fileBinId, jfu.file_path AS filePath, 
jfu.file_upload_id AS fileUploadId, jfu.original_file_name AS originalFileName, 
jfu.physical_file_name AS physicalFileName FROM jq_file_upload_config AS fug 
INNER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id WHERE 
fug.file_bin_id = :fileBinId AND jfu.file_association_id = :fileAssociationId 
ORDER BY jfu.last_update_ts DESC, jfu.original_file_name ASC'
, 


/* default - UPLOAD Query start */
'SELECT COUNT(DISTINCT fug.file_bin_id) AS isAllowed FROM jq_file_upload_config AS fug 
INNER JOIN jq_entity_role_association AS jera ON jera.entity_id = fug.file_bin_id AND 
jera.is_active = 1 INNER JOIN jq_master_modules AS jmm ON jmm.module_id = jera.module_id 
AND jmm.module_name = :moduleName INNER JOIN jq_user_role_association AS jura ON jura.role_id = jera.role_id 
INNER JOIN jq_user AS jw ON jw.user_id = jura.user_id AND jw.email = :loggedInUserName 
LEFT OUTER JOIN jq_file_upload AS jfu ON fug.file_bin_id = jfu.file_bin_id WHERE fug.file_bin_id = :fileBinId'
, 


/* default - VIEW Query start */
'SELECT COUNT(*) AS isAllowed FROM jq_file_upload AS jfu WHERE jfu.file_upload_id = :fileUploadId '


/* default - DELETE Query start */
, 'SELECT COUNT(*) AS isAllowed FROM jq_file_upload AS jfu 
 WHERE jfu.file_upload_id = :fileUploadId AND jfu.updated_by = :loggedInUserName'
, 0,'admin@trigyn.com', NOW(), NOW()); 


REPLACE INTO jq_entity_role_association  (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('166739df-70ef-430e-b795-ef3988d0f4e0', 'profilePic', 'profilePic', '248ffd91-7760-11eb-94ed-f48e38ab8cd7', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1, 0);
REPLACE INTO jq_entity_role_association  (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('73644072-88aa-11eb-8dcd-0242ac130223', 'profilePic', 'profilePic', '248ffd91-7760-11eb-94ed-f48e38ab8cd7', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0);


replace into jq_dynamic_rest_details (jws_dynamic_rest_id, jws_dynamic_rest_url, jws_rbac_id, jws_method_name, jws_method_description, jws_request_type_id, jws_response_producer_type_id, jws_service_logic, jws_platform_id, jws_allow_files, jws_dynamic_rest_type_id) VALUES
('ed51f645-3640-4d74-823f-93eed65c53fe', 'retrieveProfilePic', 1, 'retrieveProfilePic', 'Display Profile Pic', 2, 7, 
'function retrieveProfilePic(requestDetails, daoResults) {
    return daoResults["fileUploadId"];
}

retrieveProfilePic(requestDetails, daoResults);', 3, 0, 2);

replace into jq_dynamic_rest_dao_details (jws_dao_details_id, jws_dynamic_rest_details_id, jws_result_variable_name, jws_dao_query_template, jws_query_sequence, jws_dao_query_type) VALUES
(148, 'ed51f645-3640-4d74-823f-93eed65c53fe', 'fileUploadId', 'SELECT file_upload_id AS fileUploadId FROM jq_file_upload WHERE file_bin_id="profilePic" AND file_association_id=:loggedInUserId', 1, 1);


REPLACE INTO jq_entity_role_association  (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('aeb37198-bbfe-45e9-b9d0-58ee92b5ec4d', 'ed51f645-3640-4d74-823f-93eed65c53fe', 'retrieveProfilePic', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', '2ace542e-0c63-11eb-9cf5-f48e38ab9348', NOW(), 'admin', 1, 0);
REPLACE INTO jq_entity_role_association  (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('aeb37198-bbfe-45e9-b9d0-58ee92b5ec5e', 'ed51f645-3640-4d74-823f-93eed65c53fe', 'retrieveProfilePic', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'ae6465b3-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0);
REPLACE INTO jq_entity_role_association  (entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id) VALUES 
('aeb37198-bbfe-45e9-b9d0-58ee92b5ec6f', 'ed51f645-3640-4d74-823f-93eed65c53fe', 'retrieveProfilePic', '47030ee1-0ecf-11eb-94b2-f48e38ab9348', 'b4a0dda1-097f-11eb-9a16-f48e38ab9348', NOW(), 'admin', 1, 0);


SET FOREIGN_KEY_CHECKS=1;