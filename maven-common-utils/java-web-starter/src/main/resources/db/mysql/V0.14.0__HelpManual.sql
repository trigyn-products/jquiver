ALTER TABLE `jq_manual_type`   
ADD COLUMN `created_by` VARCHAR(500) NULL AFTER `is_system_manual`,
ADD COLUMN `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL AFTER `created_by`,
ADD COLUMN `last_updated_by` VARCHAR(500) NULL AFTER `created_date`,
ADD COLUMN `last_updated_ts` TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP NOT NULL AFTER `last_updated_by`;
	
	

    
ALTER TABLE `jq_manual_entry`   
CHANGE `manual_type` `manual_id` VARCHAR(50) CHARSET utf8 COLLATE utf8_general_ci NOT NULL,
ADD COLUMN `parent_id` VARCHAR(50) NULL AFTER `sort_index`,
CHANGE `is_custom_updated` `is_custom_updated` INT(1) DEFAULT 0 NULL  AFTER `parent_id`,
ADD COLUMN `created_by` VARCHAR(500) NULL AFTER `is_custom_updated`,
ADD COLUMN `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL AFTER `created_by`,
CHANGE `last_modified_on` `last_updated_ts` TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP NOT NULL  AFTER `last_updated_by`, 
DROP INDEX `manual_type`,
ADD  KEY `manual_type` (`manual_id`);
  
ALTER TABLE `jq_manual_entry`  
ADD CONSTRAINT `jq_hmanual_entry_parent_id_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `jq_manual_entry`(`manual_entry_id`) ON DELETE CASCADE;  
  
  