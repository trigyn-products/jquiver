ALTER TABLE jq_dashlet ADD COLUMN result_variable_name VARCHAR(256) DEFAULT NULL AFTER datasource_id;
ALTER TABLE jq_dashlet ADD COLUMN dao_query_type INT(11) DEFAULT 1 AFTER result_variable_name;