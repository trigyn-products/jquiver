CREATE TABLE `manual_type` (
  `manual_id` varchar(50) NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `is_system_manual` int(2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`manual_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `manual_entry` (
  `manual_entry_id` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `manual_type` varchar(50) NOT NULL,
  `entry_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `entry_content` longtext COLLATE utf8_unicode_ci NOT NULL,
  `sort_index` int(5) DEFAULT NULL,
  `last_modified_on` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `last_updated_by` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`manual_entry_id`),
  KEY `manual_type` (`manual_type`),
  CONSTRAINT `manual_entry_ibfk_1` FOREIGN KEY (`manual_type`) REFERENCES `manual_type` (`manual_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `manual_entry_file_association` (
  `manual_entry_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `file_upload_id` varchar(50) NOT NULL,
  PRIMARY KEY (`manual_entry_id`,`file_upload_id`),
  KEY `file_upload_id` (`file_upload_id`),
  CONSTRAINT `manual_entry_file_association_ibfk_1` FOREIGN KEY (`manual_entry_id`) REFERENCES `manual_entry` (`manual_entry_id`),
  CONSTRAINT `manual_entry_file_association_ibfk_2` FOREIGN KEY (`file_upload_id`) REFERENCES `jws_file_upload` (`file_upload_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;