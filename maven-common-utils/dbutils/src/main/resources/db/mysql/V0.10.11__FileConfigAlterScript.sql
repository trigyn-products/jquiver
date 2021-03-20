ALTER TABLE jq_file_upload_config
 CHANGE file_upload_config_id file_bin_id VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
 
ALTER TABLE jq_file_upload
 CHANGE file_config_id file_bin_id VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER file_upload_id,
 ADD file_association_id VARCHAR(50) AFTER last_update_ts;
 
 
ALTER TABLE jq_file_upload_config
 ADD select_query_content LONGTEXT AFTER no_of_files,
 ADD upload_query_content LONGTEXT AFTER select_query_content,
 ADD view_query_content LONGTEXT AFTER upload_query_content, 
 ADD delete_query_content LONGTEXT AFTER view_query_content,
 CHANGE updated_by updated_by VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
 CHANGE updated_date updated_date DATETIME,
 CHANGE is_deleted is_deleted INT(2) DEFAULT '0';


 