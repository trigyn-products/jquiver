DROP TABLE IF EXISTS `generic_user_notification`;
CREATE TABLE `generic_user_notification` (
  `notification_id` varchar(50) NOT NULL,
  `target_platform` varchar(30) NULL,
  `message_valid_from` datetime NOT NULL,
  `message_valid_till` datetime NOT NULL,
  `message_text` varchar(140) NOT NULL,
  `message_type` varchar(20) NOT NULL,
  `message_format` varchar(30) NOT NULL,
  `selection_criteria` mediumtext NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

