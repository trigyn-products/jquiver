ALTER TABLE jq_dashboard 
CHANGE COLUMN created_date created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
CHANGE COLUMN last_updated_date last_updated_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP();

ALTER TABLE jq_dashlet 
ADD COLUMN datasource_id VARCHAR(50) DEFAULT NULL AFTER dashlet_type_id,
CHANGE COLUMN created_date created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
CHANGE COLUMN updated_date last_updated_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
ADD CONSTRAINT jq_dashlet_ibfk_2 FOREIGN KEY (datasource_id) REFERENCES jq_additional_datasource (additional_datasource_id);

