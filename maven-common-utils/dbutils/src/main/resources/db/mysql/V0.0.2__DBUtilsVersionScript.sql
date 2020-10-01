DROP TABLE IF EXISTS `jws_template_version`;
CREATE TABLE `jws_template_version` (
  `template_version_id` varchar(50) NOT NULL,
  `parent_entity_id` varchar(100) DEFAULT NULL,
  `entity_id` varchar(100) NOT NULL,
  `entity_name` varchar(500) NOT NULL,
  `version_id` decimal(7,2) NOT NULL,
  `template_json` longtext NOT NULL,
  `updated_by` varchar(100) NOT NULL,
  `updated_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  PRIMARY KEY (`template_version_id`),
  UNIQUE KEY `entity_id` (`entity_id`,`version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;