 ALTER TABLE jq_file_upload_config 
 ADD COLUMN is_file_storage_enable INT(1) DEFAULT 0 NULL AFTER is_custom_updated,
 ADD COLUMN custom_file_storage_class VARCHAR(5000) NULL AFTER is_file_storage_enable;