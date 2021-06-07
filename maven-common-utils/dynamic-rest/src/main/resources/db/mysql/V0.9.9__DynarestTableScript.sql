ALTER TABLE jq_dynamic_rest_details
ADD COLUMN datasource_id VARCHAR(50) DEFAULT NULL AFTER jws_dynamic_rest_type_id,
ADD COLUMN created_by VARCHAR(500) DEFAULT NULL,
ADD COLUMN created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
ADD COLUMN last_updated_by VARCHAR(500) DEFAULT NULL,
ADD COLUMN last_updated_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
ADD CONSTRAINT jq_dynamic_rest_details_ibfk_3 FOREIGN KEY (datasource_id) REFERENCES jq_additional_datasource (additional_datasource_id);