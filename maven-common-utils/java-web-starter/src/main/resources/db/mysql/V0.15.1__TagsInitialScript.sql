DROP TABLE IF EXISTS jq_tag_data;  
CREATE TABLE `jq_tag_data` (
  `tag_id` varchar(50) NOT NULL,
  `tag_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `created_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_updated_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `last_updated_ts` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`tag_id`)
);

DROP TABLE IF EXISTS jq_tag_entity_details;
CREATE TABLE `jq_tag_entity_details` (
  `tag_entity_details_id` varchar(50) NOT NULL,
  `module_id` varchar(100) NOT NULL,
  `entity_id` varchar(255) NOT NULL,
  `tag_id` varchar(50) NOT NULL,
  `module_json` longtext NOT NULL,
  `module_json_checksum` varchar(512) NOT NULL,
  `permissions` longtext NOT NULL,
  `last_updated_by` varchar(100) NOT NULL,
  `last_updated_ts` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `created_by` varchar(100) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`tag_entity_details_id`),
  KEY `fk_tag_entity_tagid` (`tag_id`),
  CONSTRAINT `fk_tag_entity_tagid` FOREIGN KEY (`tag_id`) REFERENCES `jq_tag_data` (`tag_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;

DROP TABLE IF EXISTS jq_file_upload_tags;
CREATE TABLE `jq_file_upload_tags` (
  `file_upload_id` VARCHAR(50) NOT NULL,
  `file_bin_id` VARCHAR(100) NOT NULL,
  `file_path` VARCHAR(5000) DEFAULT NULL,
  `original_file_name` VARCHAR(255) DEFAULT NULL,
  `physical_file_name` VARCHAR(255) DEFAULT NULL,
  `updated_by` VARCHAR(50) DEFAULT NULL,
  `last_update_ts` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(),
  `file_association_id` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`file_upload_id`)
) ;