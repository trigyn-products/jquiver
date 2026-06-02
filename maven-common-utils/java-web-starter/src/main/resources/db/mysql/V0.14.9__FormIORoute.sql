ALTER TABLE `jq_form_io`   
	ADD COLUMN `multi_submit` INT(1) DEFAULT 0 NULL AFTER `last_updated_ts`,
	ADD COLUMN `route_name` VARCHAR(50) NULL AFTER `multi_submit`;

DROP TABLE IF EXISTS jq_formio_route;
CREATE TABLE `jq_formio_route` (
  `form_io_id` varchar(50) NOT NULL,
  `module_id` varchar(50) NOT NULL,
  `multi_submit` int(1) DEFAULT NULL,
  PRIMARY KEY (`form_io_id`,`module_id`)
);