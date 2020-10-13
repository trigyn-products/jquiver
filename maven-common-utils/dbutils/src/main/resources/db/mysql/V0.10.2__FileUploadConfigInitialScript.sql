DROP TABLE IF EXISTS file_upload_config;
CREATE TABLE file_upload_config( 
file_upload_config_id VARCHAR(500)
, file_type_supported VARCHAR(5000) DEFAULT NULL
, max_file_size BIGINT UNSIGNED
, allow_multiple_files INT(2) DEFAULT 0
, is_deleted INT(2) DEFAULT 0
, updated_by VARCHAR(100) NOT NULL
, updated_date datetime DEFAULT  NULL
, PRIMARY KEY (file_upload_config_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;