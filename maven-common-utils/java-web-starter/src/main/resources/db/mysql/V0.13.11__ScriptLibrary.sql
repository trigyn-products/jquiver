DROP TABLE IF EXISTS `jq_script_lib_details`;

CREATE TABLE `jq_script_lib_details` (
  `script_lib_id` VARCHAR(50) NOT NULL,
  `template_id` VARCHAR(50) NOT NULL,
  `library_name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(500) DEFAULT NULL,
  `script_type` VARCHAR(100) NOT NULL,
  `created_by` VARCHAR(50) DEFAULT NULL,
  `updated_by` VARCHAR(50) DEFAULT NULL,
  `updated_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
  `is_custom_updated` INT(1) DEFAULT NULL,
  PRIMARY KEY (`script_lib_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `jq_script_lib_connect`;

CREATE TABLE `jq_script_lib_connect` (
  `script_lib_conn_id` VARCHAR(100) NOT NULL,
  `script_lib_id` VARCHAR(100) NOT NULL,
  `module_type_id` VARCHAR(100) DEFAULT NULL,
  `entity_id` VARCHAR(100) DEFAULT NULL,
  `created_by` VARCHAR(50) DEFAULT NULL,
  `updated_by` VARCHAR(50) DEFAULT NULL,
  `updated_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
  `is_custom_updated` INT(1) DEFAULT NULL,
  PRIMARY KEY (`script_lib_conn_id`),
  KEY `mod_type_id` (`module_type_id`),
  KEY `script_lib_id` (`script_lib_id`),
  CONSTRAINT `mod_type_id` FOREIGN KEY (`module_type_id`) REFERENCES `jq_master_modules` (`module_id`),
  CONSTRAINT `script_lib_id` FOREIGN KEY (`script_lib_id`) REFERENCES `jq_script_lib_details` (`script_lib_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb3;

