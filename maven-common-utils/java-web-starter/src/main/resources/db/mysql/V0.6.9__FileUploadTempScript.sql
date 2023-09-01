DROP TABLE IF EXISTS jq_file_upload_temp;
CREATE TABLE jq_file_upload_temp (
   file_upload_temp_id VARCHAR(50) NOT NULL,
   file_upload_id VARCHAR(50) NOT NULL,
   file_bin_id VARCHAR(100) NOT NULL,
   file_path VARCHAR(5000)  NOT NULL,
   original_file_name VARCHAR(255)  NOT NULL,
   physical_file_name VARCHAR(255)  NOT NULL,
   updated_by VARCHAR(50)  NOT NULL,
   last_update_ts TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   file_association_id VARCHAR(50) NOT NULL,
   action INT(10) NOT NULL,
  PRIMARY KEY (file_upload_temp_id)
) ENGINE = innodb ROW_FORMAT = DEFAULT CHARACTER SET utf8;