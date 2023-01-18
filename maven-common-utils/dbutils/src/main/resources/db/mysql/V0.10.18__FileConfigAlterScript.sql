ALTER TABLE `jq_file_upload_config`   
	ADD COLUMN `select_query_type` INT(11) DEFAULT 1 NULL AFTER `no_of_files`,
	ADD COLUMN `datasource_select_validator` VARCHAR(50) NULL AFTER `select_query_content`,
	ADD COLUMN `upload_query_type` INT(11) DEFAULT 1 NULL AFTER `datasource_select_validator`,
	ADD COLUMN `datasource_upload_validator` VARCHAR(50) NULL AFTER `upload_query_content`,
	ADD COLUMN `view_query_type` INT(11) DEFAULT 1 NULL AFTER `datasource_upload_validator`,
	ADD COLUMN `datasource_view_validator` VARCHAR(50) NULL AFTER `view_query_content`,
	ADD COLUMN `delete_query_type` INT(11) DEFAULT 1 NULL AFTER `datasource_view_validator`,
	CHANGE `delete_query_content` `delete_query_content` LONGTEXT NULL  AFTER `delete_query_type`,
	ADD COLUMN `datasource_delete_validator` VARCHAR(50) NULL AFTER `delete_query_content`;


 