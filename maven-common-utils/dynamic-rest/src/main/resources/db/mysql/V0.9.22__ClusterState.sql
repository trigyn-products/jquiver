CREATE TABLE `jq_cluster_state` (  
  `instance_id` VARCHAR(50) NOT NULL,
  `is_active` INT(1),
  `is_schedular` INT(1),
  `updated_on` TIMESTAMP,
  PRIMARY KEY (`instance_id`) 
);