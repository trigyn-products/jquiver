DROP TABLE IF EXISTS jq_file_upload;
CREATE TABLE jq_file_upload (
   file_upload_id VARCHAR(50) NOT NULL,
   file_path VARCHAR(5000) CHARACTER SET utf8 COLLATE utf8_general_ci,
   original_file_name VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci,
   physical_file_name VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci,
   updated_by VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci,
   last_update_ts TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (file_upload_id)
) ENGINE = innodb ROW_FORMAT = DEFAULT CHARACTER SET utf8;