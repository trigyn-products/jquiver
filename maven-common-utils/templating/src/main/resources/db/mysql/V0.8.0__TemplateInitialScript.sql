DROP TABLE IF EXISTS `template_master`;
CREATE TABLE `template_master` (
  `template_id` varchar(50),
  `template_name` varchar(100) NOT NULL,
  `template` mediumtext DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `updated_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE template_master ADD UNIQUE INDEX (template_name);
ALTER TABLE template_master ADD checksum VARCHAR(512) AFTER updated_date;
