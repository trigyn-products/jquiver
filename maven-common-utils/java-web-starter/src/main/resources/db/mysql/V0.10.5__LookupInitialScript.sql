CREATE TABLE jq_lookup(
 lookup_id VARCHAR(50)
 ,lookup_name VARCHAR(1000)
, record_id INT(11) NOT NULL
, is_deleted tinyint(4) DEFAULT 0
, PRIMARY KEY (lookup_id)
, UNIQUE KEY(`lookup_name`,`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE jq_lookup_i18n(
id VARCHAR(50) NOT NULL 
, jws_lookup_id VARCHAR(50) NOT NULL 
, language_id INT(11) NOT NULL 
, record_description VARCHAR(5000) NOT NULL
, PRIMARY KEY (id)
, UNIQUE KEY (`jws_lookup_id`, `language_id`)
, KEY `lookup_id` (`jws_lookup_id`)
, CONSTRAINT `jq_lookup_i18n_ibfk_1` FOREIGN KEY (`jws_lookup_id`) REFERENCES `jq_lookup` (`lookup_id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
