
DROP TABLE IF EXISTS `master_module_role_association`;
DROP TABLE IF EXISTS `master_module`;
CREATE TABLE `master_module` (
  `master_module_id` varchar(50) NOT NULL,
  `master_module_name` varchar(100) NOT NULL,
  `grid_details_id` varchar(50) NOT NULL,
  `module_type` varchar(50) NOT NULL,
  PRIMARY KEY (`master_module_id`),
  UNIQUE KEY `master_module_name` (`master_module_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
