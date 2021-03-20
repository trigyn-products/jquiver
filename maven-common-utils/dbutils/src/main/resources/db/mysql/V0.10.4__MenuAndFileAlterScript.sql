ALTER TABLE jq_file_upload_config CHANGE allow_multiple_files no_of_files INT(4) DEFAULT 1;
ALTER TABLE jq_module_listing ADD COLUMN is_inside_menu BIT(1) DEFAULT 0;
ALTER TABLE jq_module_listing MODIFY sequence INT(11) DEFAULT NULL;