SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS jws_ddos_details;
CREATE TABLE  jws_ddos_details (
jws_ddos_details_id VARCHAR(50) NOT NULL,
ip_address VARCHAR(50) NOT NULL,
session_details longtext DEFAULT NULL,
is_blocked INT(2) DEFAULT 0,
last_attacked_date timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
PRIMARY KEY (jws_ddos_details_id)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_security_type;
CREATE TABLE  jws_security_type (
security_type_id VARCHAR(50) NOT NULL,
security_name VARCHAR(50),
is_active INT(2) DEFAULT 0,
PRIMARY KEY (security_type_id),
UNIQUE KEY security_name(security_name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS jws_security_properties;
CREATE TABLE  jws_security_properties (
security_properties_id VARCHAR(50) NOT NULL,
security_type_id VARCHAR(50) NOT NULL,
security_property_name VARCHAR(50),
security_property_value VARCHAR(1000) DEFAULT NULL,
PRIMARY KEY (security_properties_id),
UNIQUE KEY security_property_name(security_property_name),
KEY FK1_security_type_id (security_type_id),
CONSTRAINT FK1_security_type_id FOREIGN KEY (security_type_id) REFERENCES jws_security_type (security_type_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


REPLACE INTO jws_security_type (security_type_id,security_name, is_active) VALUES 
('a1fa90e3-32dd-11eb-a009-f48e38ab8cd7','DDOS', 0);

REPLACE INTO jws_security_properties (security_properties_id, security_type_id,security_property_name, security_property_value) VALUES 
('c238f528-32fa-11eb-a009-f48e38ab8cd7', 'a1fa90e3-32dd-11eb-a009-f48e38ab8cd7', 'In Memory', 1);

SET FOREIGN_KEY_CHECKS=1;
