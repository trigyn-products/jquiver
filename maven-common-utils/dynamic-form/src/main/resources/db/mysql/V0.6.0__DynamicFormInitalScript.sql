DROP TABLE IF EXISTS dynamic_form;
CREATE TABLE dynamic_form (
  form_id varchar(50) NOT NULL,
  form_name varchar(50) NOT NULL,
  form_description varchar(100) DEFAULT NULL,
  form_select_query text  NULL,
  form_body longtext NOT NULL,
  created_by varchar(100) DEFAULT NULL,
  created_date datetime DEFAULT  NULL,
  form_select_checksum varchar(512) DEFAULT NULL,
  form_body_checksum varchar(512) DEFAULT NULL,
  PRIMARY KEY (form_id),
  UNIQUE KEY (form_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE dynamic_form_save_queries (
   dynamic_form_query_id VARCHAR(50) NOT NULL,
   dynamic_form_id VARCHAR(50) NOT NULL,
   dynamic_form_save_query MEDIUMTEXT ASCII NOT NULL,
   sequence INT(11) NOT NULL,
   checksum varchar(512) DEFAULT NULL,
  FOREIGN KEY (dynamic_form_id) REFERENCES dynamic_form (form_id) ON UPDATE RESTRICT ON DELETE RESTRICT,
  PRIMARY KEY (dynamic_form_query_id)
) ENGINE = InnoDB ROW_FORMAT = DEFAULT CHARACTER SET utf8;

