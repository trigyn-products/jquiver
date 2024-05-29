/*Table structure for table `jq_rest_mtd_signature` */

DROP TABLE IF EXISTS `jq_rest_mtd_signature`;

CREATE TABLE `jq_rest_mtd_signature` (
  `help_id` varchar(10) NOT NULL,
  `module_type` varchar(50) DEFAULT NULL,
  `help` mediumtext DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`help_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;
