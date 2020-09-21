DROP TABLE IF EXISTS `jws_property_master`;
CREATE TABLE `jws_property_master` (
  `owner_type` varchar(100) NOT NULL,
  `owner_id` varchar(150) NOT NULL,
  `property_name` varchar(150) NOT NULL,
  `property_value` varchar(250) NOT NULL,
  `is_deleted` int(2) NOT NULL,
  `last_modified_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  `modified_by` varchar(50) NOT NULL,
  `app_version` decimal(7,4) NOT NULL,
  `comments` text DEFAULT NULL,
  PRIMARY KEY (`owner_type`,`owner_id`,`property_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

REPLACE INTO jws_property_master(owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('system', 'system', 'profile', 'dev', 0, NOW(), 'admin', 1.00, 'Checks the profile in which the application is running.');

REPLACE INTO jws_property_master(owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('system', 'system', 'template-storage-path', 'D:\\commons\\documents', 0, NOW(), 'admin', 1.00, 'Path at which the template will be stored during local development.');

REPLACE INTO jws_property_master (owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments) 
VALUES ('system', 'system', 'acl-jws', 'admin,maintainer', 0, NOW(), 'admin', 1.0000, 'List of roles authorized to access jws admin panel.');