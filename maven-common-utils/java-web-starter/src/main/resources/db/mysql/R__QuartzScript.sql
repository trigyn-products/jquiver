
DROP TABLE IF EXISTS mail_schedule;
CREATE TABLE `mail_schedule` (
  `mail_schedule_id` varchar(50) NOT NULL,
  `email_xml` mediumtext DEFAULT NULL,
  `mail_schedule_group_id` varchar(50) DEFAULT NULL, 
  PRIMARY KEY (`mail_schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8