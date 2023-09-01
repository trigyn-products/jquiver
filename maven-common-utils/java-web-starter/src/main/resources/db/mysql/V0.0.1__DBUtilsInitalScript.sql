DROP TABLE IF EXISTS `jq_property_master`;
CREATE TABLE `jq_property_master` (
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

DROP TABLE IF EXISTS `jq_user_role`;
CREATE TABLE `jq_user_role` (
`role_id` VARCHAR(50) NOT NULL
, `role_name` VARCHAR(100) NOT NULL
, `role_description` VARCHAR(2000)
, `is_deleted` INT(2) DEFAULT 0
, PRIMARY KEY (`role_id`)
,UNIQUE KEY `role_name` (`role_name`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


REPLACE INTO jq_property_master(owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('system', 'system', 'profile', 'prod', 0, NOW(), 'admin', 1.00, 'Checks the profile in which the application is running.');

REPLACE INTO jq_property_master(owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments)
VALUES ('system', 'system', 'template-storage-path', 'D:\\commons\\documents', 0, NOW(), 'admin', 1.00, 'Path at which the template will be stored during local development.');

REPLACE INTO jq_property_master (owner_type, owner_id, property_name, property_value, is_deleted, last_modified_date, modified_by, app_version, comments) 
VALUES ('system', 'system', 'acl-jws', 'admin,maintainer', 0, NOW(), 'admin', 1.0000, 'List of roles authorized to access jws admin panel.');

INSERT INTO jq_user_role (role_id,role_name,role_description,is_deleted) VALUES ('ab751695-fcb9-11ea-954a-f48e38ab8cd7','ADMIN','' ,0);
   
INSERT INTO jq_user_role (role_id,role_name,role_description,is_deleted) VALUES ('b18cf436-fcb9-11ea-954a-f48e38ab8cd7','Anonymous','' ,0);