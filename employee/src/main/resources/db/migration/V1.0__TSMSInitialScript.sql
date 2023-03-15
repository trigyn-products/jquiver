DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `department_id` varchar(50) NOT NULL,
  `department_name` varchar(250) NOT NULL,
  `department_description` varchar(500) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`department_id`)
);

DROP TABLE IF EXISTS `designation`;
CREATE TABLE `designation` (
  `designation_id` varchar(50) NOT NULL,
  `designation_name` varchar(500) NOT NULL,
  `grade_level` int(11) NOT NULL,
  PRIMARY KEY (`designation_id`)
);

DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `employee_id` varchar(50) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email_id` varchar(250) NOT NULL,
  `designation_id` varchar(50) NOT NULL,
  `department_id` varchar(50) NOT NULL,
  `manager_id` varchar(50) DEFAULT NULL,
  `address` text NOT NULL,
  `city` varchar(100) NOT NULL,
  `contact_number` varchar(50) DEFAULT NULL,
  `contact_email_id` varchar(250) DEFAULT NULL,
  `joining_date` datetime DEFAULT current_timestamp(),
  `employe_skill_sets` text DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `updated_by` varchar(50) NOT NULL,
  `updated_date` datetime DEFAULT current_timestamp(),
  `active` int(11) NOT NULL,
  PRIMARY KEY (`employee_id`),
  KEY `employee_ibfk_1` (`designation_id`),
  KEY `employee_ibfk_2` (`department_id`),
  KEY `manager_id` (`manager_id`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`designation_id`) REFERENCES `designation` (`designation_id`),
  CONSTRAINT `employee_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`),
  CONSTRAINT `employee_ibfk_3` FOREIGN KEY (`manager_id`) REFERENCES `employee` (`employee_id`) ,
  CONSTRAINT `employee_ibfk_4` FOREIGN KEY (`manager_id`) REFERENCES `employee` (`employee_id`)
) ;
