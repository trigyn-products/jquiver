DROP TABLE IF EXISTS jq_manual_type;
CREATE TABLE `jq_manual_type` (
  `manual_id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `is_system_manual` int(2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`manual_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jq_manual_entry;
CREATE TABLE `jq_manual_entry` (
  `manual_entry_id` varchar(50) NOT NULL,
  `manual_type` varchar(50) NOT NULL,
  `entry_name` varchar(50) NOT NULL,
  `entry_content` longtext NOT NULL,
  `sort_index` int(5) DEFAULT NULL,
  `last_modified_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `last_updated_by` varchar(500) NOT NULL,
  PRIMARY KEY (`manual_entry_id`),
  KEY `manual_type` (`manual_type`),
  CONSTRAINT `jq_manual_entry_ibfk_1` FOREIGN KEY (`manual_type`) REFERENCES `jq_manual_type` (`manual_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jq_manual_entry_file_association;
CREATE TABLE `jq_manual_entry_file_association` (
  `manual_entry_id` varchar(50) CHARACTER SET utf8 NOT NULL,
  `file_upload_id` varchar(50) NOT NULL,
  PRIMARY KEY (`manual_entry_id`,`file_upload_id`),
  KEY `file_upload_id` (`file_upload_id`),
  CONSTRAINT `jq_manual_entry_file_association_ibfk_1` FOREIGN KEY (`manual_entry_id`) REFERENCES `jq_manual_entry` (`manual_entry_id`),
  CONSTRAINT `jq_manual_entry_file_association_ibfk_2` FOREIGN KEY (`file_upload_id`) REFERENCES `jq_file_upload` (`file_upload_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;