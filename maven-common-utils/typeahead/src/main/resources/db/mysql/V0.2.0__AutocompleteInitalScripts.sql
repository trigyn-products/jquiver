DROP TABLE IF EXISTS jq_autocomplete_details;
CREATE TABLE jq_autocomplete_details (
  ac_id varchar(50) NOT NULL,
  ac_description varchar(100) DEFAULT NULL,
  ac_select_query longtext NOT NULL,
  PRIMARY KEY (ac_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;