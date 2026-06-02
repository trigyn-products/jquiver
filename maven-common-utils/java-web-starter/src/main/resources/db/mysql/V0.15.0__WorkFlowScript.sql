DROP TABLE IF EXISTS jq_workflow_definition;

CREATE TABLE `jq_workflow_definition` (
  `definition_id` varchar(50) NOT NULL,
  `definition_name` varchar(100) NOT NULL,
  `bpmn_xml` text NOT NULL,
  `version` int(11) DEFAULT 1,
  `uploaded_by` varchar(50) DEFAULT NULL,
  `uploaded_at` timestamp NULL DEFAULT current_timestamp(),
  `is_active` int(2) NOT NULL DEFAULT 1,
  `created_by` varchar(50) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`definition_id`)
) ;

DROP TABLE IF EXISTS jq_workflow_status;
CREATE TABLE `jq_workflow_status` (
  `status_id` varchar(50) NOT NULL,
  `status_name` varchar(50) NOT NULL,
  `status_order` int(11) DEFAULT NULL,
  `is_final` tinyint(1) NOT NULL DEFAULT 0,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `definition_id` varchar(50) DEFAULT NULL,
  `order_no` int(11) NOT NULL,
  `last_updated_by` varchar(50) DEFAULT NULL,
  `last_updated_ts` timestamp NULL DEFAULT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `is_user_task` int(50) DEFAULT 0,
  `form_key` varchar(50) DEFAULT NULL,
  `status_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`status_id`),
  KEY `fk_status_definition` (`definition_id`),
  CONSTRAINT `fk_status_definition` FOREIGN KEY (`definition_id`) REFERENCES `jq_workflow_definition` (`definition_id`)
) ;


DROP TABLE IF EXISTS jq_workflow_transition;
CREATE TABLE `jq_workflow_transition` (
  `transition_id` varchar(50) NOT NULL,
  `from_status_id` varchar(50) NOT NULL,
  `to_status_id` varchar(50) NOT NULL,
  `allowed_roles` varchar(50) DEFAULT NULL,
  `transition_label` varchar(50) DEFAULT NULL,
  `definition_id` varchar(50) NOT NULL,
  `order_no` int(11) NOT NULL,
  `last_updated_by` varchar(50) DEFAULT NULL,
  `last_updated_ts` timestamp NULL DEFAULT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `action` varchar(100) DEFAULT NULL,
  `raw_action_expression` varchar(100) DEFAULT NULL,
  `is_default` int(11) DEFAULT NULL,
  PRIMARY KEY (`transition_id`),
  KEY `fk_from_status` (`from_status_id`),
  KEY `fk_to_status` (`to_status_id`),
  KEY `fk_definition` (`definition_id`),
  CONSTRAINT `fk_definition` FOREIGN KEY (`definition_id`) REFERENCES `jq_workflow_definition` (`definition_id`),
  CONSTRAINT `fk_from_status` FOREIGN KEY (`from_status_id`) REFERENCES `jq_workflow_status` (`status_id`),
  CONSTRAINT `fk_to_status` FOREIGN KEY (`to_status_id`) REFERENCES `jq_workflow_status` (`status_id`)
) ;

DROP TABLE IF EXISTS jq_workflow_instance;
CREATE TABLE `jq_workflow_instance` (
  `instance_id` varchar(36) NOT NULL,
  `definition_id` varchar(36) DEFAULT NULL,
  `current_status_id` varchar(36) DEFAULT NULL,
  `started_by` varchar(50) DEFAULT NULL,
  `started_at` timestamp NULL DEFAULT NULL,
  `last_updated_by` varchar(50) DEFAULT NULL,
  `last_updated_ts` timestamp NULL DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  `version` int(11) DEFAULT 1,
  `entity_id` varchar(255) NOT NULL,
  `end_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`instance_id`),
  KEY `definition_id` (`definition_id`),
  KEY `current_status_id` (`current_status_id`),
  CONSTRAINT `jq_workflow_instance_ibfk_1` FOREIGN KEY (`definition_id`) REFERENCES `jq_workflow_definition` (`definition_id`),
  CONSTRAINT `jq_workflow_instance_ibfk_2` FOREIGN KEY (`current_status_id`) REFERENCES `jq_workflow_status` (`status_id`)
) ;

DROP TABLE IF EXISTS jq_workflow_task;
CREATE TABLE `jq_workflow_task` (
  `task_id` varchar(36) NOT NULL,
  `instance_id` varchar(36) DEFAULT NULL,
  `status_id` varchar(36) DEFAULT NULL,
  `assigned_to` varchar(50) DEFAULT NULL,
  `role_id` varchar(50) DEFAULT NULL,
  `task_name` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `completed_by` varchar(50) DEFAULT NULL,
  `is_completed` tinyint(1) DEFAULT 0,
  `outcome` varchar(50) DEFAULT NULL,
  `completed_on` timestamp NULL DEFAULT NULL,
  `is_active` int(1) DEFAULT NULL,
  `due_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  KEY `instance_id` (`instance_id`),
  KEY `status_id` (`status_id`),
  CONSTRAINT `jq_workflow_task_ibfk_1` FOREIGN KEY (`instance_id`) REFERENCES `jq_workflow_instance` (`instance_id`),
  CONSTRAINT `jq_workflow_task_ibfk_2` FOREIGN KEY (`status_id`) REFERENCES `jq_workflow_status` (`status_id`)
) ;
