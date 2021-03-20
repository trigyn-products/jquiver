DROP TABLE IF EXISTS jq_grid_details;
CREATE TABLE jq_grid_details (
  grid_id varchar(45) NOT NULL,
  grid_name varchar(45) DEFAULT NULL,
  grid_description varchar(500) DEFAULT NULL,
  grid_table_name varchar(45) NOT NULL,
  grid_column_names varchar(500) NOT NULL,
  PRIMARY KEY (grid_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE jq_grid_details ADD query_type INT(2) NOT NULL DEFAULT '2' AFTER grid_column_names;
