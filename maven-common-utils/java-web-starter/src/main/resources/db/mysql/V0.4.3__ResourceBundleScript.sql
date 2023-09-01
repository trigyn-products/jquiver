ALTER TABLE `jq_resource_bundle` ADD COLUMN `created_by` VARCHAR(50) DEFAULT 'admin@jquiver.io'  , ADD COLUMN `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP()  AFTER `created_by`, ADD COLUMN `updated_by` VARCHAR(50)  NULL AFTER `created_date`, ADD COLUMN `updated_date` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NULL AFTER `updated_by`; 



