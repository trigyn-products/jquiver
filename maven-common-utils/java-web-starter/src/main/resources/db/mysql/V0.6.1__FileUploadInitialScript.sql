DROP TABLE IF EXISTS jq_file_upload;
CREATE TABLE jq_file_upload (
   file_upload_id VARCHAR(50) NOT NULL,
   file_path VARCHAR(5000),
   original_file_name VARCHAR(255),
   physical_file_name VARCHAR(255),
   updated_by VARCHAR(50),
   last_update_ts TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (file_upload_id)
);