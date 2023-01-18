ALTER TABLE jq_dynamic_form ADD COLUMN query_type int(1) DEFAULT 1;
ALTER TABLE jq_dynamic_form_save_queries ADD COLUMN result_variable_name varchar(256);
ALTER TABLE jq_dynamic_form_save_queries ADD COLUMN dao_query_type int(11) DEFAULT 2;
ALTER TABLE jq_dynamic_form_save_queries ADD COLUMN datasource_id varchar(50);


UPDATE jq_dynamic_form SET query_type =1;
UPDATE jq_dynamic_form_save_queries SET dao_query_type=1;
