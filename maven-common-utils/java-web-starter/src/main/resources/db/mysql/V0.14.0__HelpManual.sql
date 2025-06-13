SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE `jq_manual_type`   
ADD COLUMN  IF NOT EXISTS `created_by` VARCHAR(500) NULL AFTER `is_system_manual`,
ADD COLUMN IF NOT EXISTS `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL AFTER `created_by`,
ADD COLUMN IF NOT EXISTS `last_updated_by` VARCHAR(500) NULL AFTER `created_date`,
ADD COLUMN IF NOT EXISTS `last_updated_ts` TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP NOT NULL AFTER `last_updated_by`;
	
	

    
ALTER TABLE `jq_manual_entry`   
CHANGE COLUMN IF EXISTS `manual_type` `manual_id` VARCHAR(50) NOT NULL,
ADD COLUMN IF NOT EXISTS `parent_id` VARCHAR(50) NULL AFTER `sort_index`,
CHANGE `is_custom_updated` `is_custom_updated` INT(1) DEFAULT 0 NULL  AFTER `parent_id`,
ADD COLUMN IF NOT EXISTS `created_by` VARCHAR(500) NULL AFTER `is_custom_updated`,
ADD COLUMN IF NOT EXISTS `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL AFTER `created_by`,
CHANGE COLUMN IF EXISTS  `last_modified_on` `last_updated_ts` TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP NOT NULL  AFTER `last_updated_by`, 
DROP FOREIGN KEY IF EXISTS jq_manual_entry_ibfk_1,
DROP INDEX IF EXISTS `manual_type`,
ADD  KEY IF NOT EXISTS `manual_type` (`manual_id`);

ALTER TABLE `jq_manual_entry`  
ADD CONSTRAINT `jq_manual_entry_ibfk_1` FOREIGN KEY IF NOT EXISTS (`manual_id`) REFERENCES `jq_manual_type` (`manual_id`) ;
  
ALTER TABLE `jq_manual_entry`  
ADD CONSTRAINT `jq_hmanual_entry_parent_id_ibfk_1` FOREIGN KEY IF NOT EXISTS (`parent_id`) REFERENCES `jq_manual_entry`(`manual_entry_id`) ON DELETE CASCADE;  
  
SET FOREIGN_KEY_CHECKS = 1;