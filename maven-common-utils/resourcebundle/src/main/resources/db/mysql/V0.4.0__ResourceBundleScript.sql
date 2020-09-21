DROP TABLE IF EXISTS `language`;
CREATE TABLE `language` (
  `language_id` int(11) NOT NULL DEFAULT 0,
  `language_name` varchar(1000) DEFAULT NULL,
  `language_code` varchar(20) DEFAULT NULL,
  `last_update_ts` datetime DEFAULT NULL,
  `is_deleted` int(11) DEFAULT 0,
  PRIMARY KEY (`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `resource_bundle`;
CREATE TABLE `resource_bundle` (
  `resource_key` varchar(200) NOT NULL,
  `language_id` int(11) NOT NULL DEFAULT 1,
  `text` text NOT NULL,
  PRIMARY KEY (`resource_key`,`language_id`),
  KEY `FK1_resource_bundle_language` (`language_id`),
  CONSTRAINT `FK1_resource_bundle_language` FOREIGN KEY (`language_id`) REFERENCES `language` (`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




INSERT INTO language (language_id,language_name,language_code,last_update_ts,is_deleted) VALUES(1, 'English','en_US', NOW(),0);
INSERT INTO language (language_id,language_name,language_code,last_update_ts,is_deleted) VALUES(2, '&#70;&#114;&#97;&#110;&#231;&#97;&#105;&#115;','fr', NOW(),0);
INSERT INTO language (language_id,language_name,language_code,last_update_ts,is_deleted) VALUES(3, '&#2361;&#2367;&#2344;&#2381;&#2342;&#2368;','hi', NOW(),0);




