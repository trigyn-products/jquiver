SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS jq_datasource_lookup;
CREATE TABLE jq_datasource_lookup (
   datasource_lookup_id VARCHAR(50) NOT NULL,
   database_product_name VARCHAR(200),
   driver_class_name VARCHAR(200),
   is_deleted INT(5) DEFAULT '0',
  PRIMARY KEY (datasource_lookup_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jq_additional_datasource;
CREATE TABLE jq_additional_datasource (
  additional_datasource_id VARCHAR(50) NOT NULL,
  datasource_name VARCHAR(200) NOT NULL,
  datasource_lookup_id VARCHAR(50) DEFAULT NULL,
  datasource_configuration longtext DEFAULT NULL,
  created_by VARCHAR(500) DEFAULT 'admin@jquiver.com',
  created_date TIMESTAMP DEFAULT 0,
  last_updated_by VARCHAR(500) DEFAULT NULL,
  last_updated_ts TIMESTAMP ON UPDATE current_timestamp(),
  is_deleted INT(4) DEFAULT 0,
  PRIMARY KEY (additional_datasource_id),
  UNIQUE KEY datasource_name (datasource_name),
  KEY jq_additional_datasource_ibfk_1 (datasource_lookup_id),
  CONSTRAINT jq_additional_datasource_ibfk_1 FOREIGN KEY (datasource_lookup_id) REFERENCES jq_datasource_lookup (datasource_lookup_id) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO jq_datasource_lookup(datasource_lookup_id,database_product_name,driver_class_name,is_deleted) VALUES 
('87eeb3a4-9611-11eb-a295-f48e38ab8cd7','postgresql' ,'org.postgresql.Driver',0);

INSERT INTO jq_datasource_lookup(datasource_lookup_id,database_product_name,driver_class_name,is_deleted) VALUES 
('d03753ea-9611-11eb-a295-f48e38ab8cd7','mysql' ,'com.mysql.jdbc.Driver',0);

INSERT INTO jq_datasource_lookup(datasource_lookup_id,database_product_name,driver_class_name,is_deleted) VALUES 
('d4099a61-9611-11eb-a295-f48e38ab8cd7','mariadb' ,'org.mariadb.jdbc.Driver',0);

INSERT INTO jq_datasource_lookup(datasource_lookup_id,database_product_name,driver_class_name,is_deleted) VALUES 
('d880d3ac-9611-11eb-a295-f48e38ab8cd7','sqlserver' ,'com.microsoft.sqlserver.jdbc.SQLServerDriver',0);

INSERT INTO jq_datasource_lookup(datasource_lookup_id,database_product_name,driver_class_name,is_deleted) VALUES 
('db39e0f9-9611-11eb-a295-f48e38ab8cd7','oracle:thin' ,'oracle.jdbc.driver.OracleDriver',0);


SET FOREIGN_KEY_CHECKS=1;