DROP TABLE IF EXISTS `master_module`;
CREATE TABLE `master_module` (
  `master_module_id` varchar(50) NOT NULL,
  `master_module_name` varchar(100) NOT NULL,
  `module_template_id` varchar(50) NOT NULL,
  `grid_details_id` varchar(50) NOT NULL,
  `auxiliary_data` varchar(100) DEFAULT NULL,
  `module_description_template` text CHARACTER SET utf8 DEFAULT NULL,
  `show_on_dashboard` int(11) NOT NULL,
  `is_system_module` int(11) NOT NULL,
  PRIMARY KEY (`master_module_id`),
  UNIQUE KEY `master_module_name` (`master_module_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;