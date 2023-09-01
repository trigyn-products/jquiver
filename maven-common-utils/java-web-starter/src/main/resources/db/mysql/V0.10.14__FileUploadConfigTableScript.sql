ALTER TABLE jq_file_upload_config
 ADD COLUMN datasource_id VARCHAR(50) DEFAULT NULL AFTER delete_query_content,
 ADD COLUMN created_by VARCHAR(500) DEFAULT 'admin@jquiver.com' AFTER datasource_id,
 ADD COLUMN created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP() AFTER created_by,
 CHANGE updated_by last_updated_by VARCHAR(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL AFTER created_date,
 CHANGE updated_date last_updated_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP() AFTER last_updated_by,
 ADD CONSTRAINT jq_file_upload_config_ibfk_1 FOREIGN KEY (datasource_id) REFERENCES jq_additional_datasource (additional_datasource_id);