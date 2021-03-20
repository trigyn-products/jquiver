ALTER TABLE jq_grid_details CHANGE COLUMN grid_id grid_id VARCHAR(255) NOT NULL;
ALTER TABLE jq_grid_details CHANGE COLUMN grid_name grid_name VARCHAR(1000) NOT NULL;
ALTER TABLE jq_grid_details CHANGE COLUMN grid_table_name grid_table_name VARCHAR(1000) NOT NULL;
ALTER TABLE jq_grid_details CHANGE COLUMN grid_description grid_description TEXT NOT NULL;
ALTER TABLE jq_grid_details CHANGE COLUMN grid_column_names grid_column_names TEXT NOT NULL;