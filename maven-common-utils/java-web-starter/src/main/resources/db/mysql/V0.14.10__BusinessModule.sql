DROP TABLE IF EXISTS jq_business_module;
CREATE TABLE `jq_business_module` (
  `business_module_id` varchar(100) NOT NULL,
  `module_name` varchar(250) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`business_module_id`),
  UNIQUE INDEX `UNIQUE` (`module_name`)
);



DROP TABLE IF EXISTS jq_business_module_entity_details;
CREATE TABLE jq_business_module_entity_details (
  business_module_entity_details_id varchar(100) NOT NULL,
  business_module_id varchar(100) DEFAULT NULL,
  module_id varchar(50) DEFAULT NULL,
  entity_id varchar(100) DEFAULT NULL,
  cmv_entity_name VARCHAR(50) NULL DEFAULT NULL,
  created_by varchar(100) DEFAULT NULL,
  created_date timestamp NULL DEFAULT NULL,
  PRIMARY KEY (business_module_entity_details_id),
  KEY business_module_id (business_module_id),
  KEY module_id (module_id),
  CONSTRAINT business_module_id_fk FOREIGN KEY (business_module_id) REFERENCES jq_business_module (business_module_id),
  CONSTRAINT module_id_fk FOREIGN KEY (module_id) REFERENCES jq_master_modules (module_id)
);

