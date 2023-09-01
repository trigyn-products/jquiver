SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS jq_module_target_lookup;
CREATE TABLE jq_module_target_lookup(
lookup_id INT(11) AUTO_INCREMENT,
description VARCHAR(250) NOT NULL,
PRIMARY KEY (lookup_id))ENGINE=InnoDB CHARSET=utf8;


DROP TABLE IF EXISTS jq_module_listing;
CREATE TABLE jq_module_listing (
module_id VARCHAR(50)
, module_url VARCHAR(250) NOT NULL
, parent_id VARCHAR(50) DEFAULT NULL
, target_lookup_id INT(11) NOT NULL 
, target_type_id VARCHAR(50) NOT NULL
, sequence INT(11) NOT NULL
, PRIMARY KEY (module_id)
,KEY target_lookup_id (target_lookup_id)
,CONSTRAINT jq_module_listing_ibfk_1 FOREIGN KEY (`target_lookup_id`) REFERENCES  jq_module_target_lookup (`lookup_id`)
)ENGINE=InnoDB CHARSET=utf8;

DROP TABLE IF EXISTS jq_module_listing_i18n;
CREATE TABLE jq_module_listing_i18n(
module_id VARCHAR(50) NOT NULL
,language_id INT(11) NOT NULL
, module_name VARCHAR(500) NOT NULL
, PRIMARY KEY (module_id, language_id)
,KEY module_id (module_id)
,KEY language_id (module_id)
,CONSTRAINT jq_module_listing_i18n_ibfk_1 FOREIGN KEY (`module_id`) REFERENCES  jq_module_listing (`module_id`)
,CONSTRAINT jq_module_listing_i18n_ibfk_2 FOREIGN KEY (`language_id`) REFERENCES  jq_language (`language_id`)
)ENGINE=InnoDB CHARSET=utf8;

DROP TABLE IF EXISTS jq_module_role_association;
CREATE TABLE jq_module_role_association(
module_id VARCHAR(50) NOT NULL
,role_id VARCHAR(100) NOT NULL
,PRIMARY KEY (module_id,role_id)
,KEY module_id (module_id)
,CONSTRAINT jq_module_role_association_ibfk_1 FOREIGN KEY (`module_id`) REFERENCES  jq_module_listing (`module_id`)
)ENGINE=InnoDB CHARSET=utf8;


DROP TABLE IF EXISTS `jq_master_module_role_association`;
CREATE TABLE `jq_master_module_role_association`(
`master_module_id` VARCHAR(50) NOT NULL
, `role_id` VARCHAR(50) NOT NULL
, PRIMARY KEY (`master_module_id`, `role_id`)
, KEY `master_module_id` (`master_module_id`)
, KEY `role_id` (`role_id`)
, CONSTRAINT `jq_master_module_role_association_ibfk_1` FOREIGN KEY (`master_module_id`) REFERENCES `jq_master_module` (`master_module_id`)
, CONSTRAINT `jq_master_module_role_association_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `jq_user_role` (`role_id`)  
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO jq_module_target_lookup (lookup_id,description) VALUES (1,'Dashboard');
INSERT INTO jq_module_target_lookup (lookup_id,description) VALUES (2,'Dynamic Form');
INSERT INTO jq_module_target_lookup (lookup_id,description) VALUES (3,'Dynamic REST');
INSERT INTO jq_module_target_lookup (lookup_id,description) VALUES (4,'Model and View');
INSERT INTO jq_module_target_lookup (lookup_id,description) VALUES (5,'Template');

SET FOREIGN_KEY_CHECKS = 1;
