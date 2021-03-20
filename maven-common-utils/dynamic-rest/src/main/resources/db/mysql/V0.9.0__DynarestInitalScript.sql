SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS jq_request_type_details;
CREATE TABLE `jq_request_type_details` (
  `jws_request_type_details_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_request_type` varchar(20) NOT NULL,
  PRIMARY KEY (`jws_request_type_details_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jq_response_code_details;
CREATE TABLE `jq_response_code_details` (
  `jws_response_code_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_response_status_code` int(11) NOT NULL,
  `jws_response_code_description` varchar(5000) NOT NULL,
  PRIMARY KEY (`jws_response_code_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jq_response_producer_details;
CREATE TABLE `jq_response_producer_details` (
  `jws_response_producer_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_response_producer_type` varchar(512) NOT NULL,
  PRIMARY KEY (`jws_response_producer_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jq_dynamic_rest_details;
CREATE TABLE `jq_dynamic_rest_details` (
  `jws_dynamic_rest_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_dynamic_rest_url` varchar(256) NOT NULL,
  `jws_rbac_id` int(11) NOT NULL,
  `jws_method_name` varchar(512) NOT NULL,
  `jws_method_description` text DEFAULT NULL,
  `jws_request_type_id` int(11) NOT NULL,
  `jws_response_producer_type_id` int(11) NOT NULL,
  `jws_service_logic` mediumtext NOT NULL,
  `jws_service_logic_checksum`varchar(512) DEFAULT NULL,
  `jws_platform_id` int(11) NOT NULL,
  PRIMARY KEY (`jws_dynamic_rest_id`),
  UNIQUE KEY (`jws_method_name`),
  KEY `jws_request_type_id` (`jws_request_type_id`),
  KEY `jws_response_producer_type_id` (`jws_response_producer_type_id`),
  CONSTRAINT `jq_dynamic_rest_details_ibfk_1` FOREIGN KEY (`jws_request_type_id`) REFERENCES `jq_request_type_details` (`jws_request_type_details_id`),
  CONSTRAINT `jq_dynamic_rest_details_ibfk_2` FOREIGN KEY (`jws_response_producer_type_id`) REFERENCES `jq_response_producer_details` (`jws_response_producer_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jq_dynamic_rest_response_params;
CREATE TABLE `jq_dynamic_rest_response_params` (
  `jws_response_param_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_dynamic_rest_details_id` int(11) NOT NULL,
  `jws_response_code_id` int(11) NOT NULL,
  `jws_response_code_message` text NOT NULL,
  PRIMARY KEY (`jws_response_param_id`),
  KEY `jws_dynamic_rest_details_id` (`jws_dynamic_rest_details_id`),
  KEY `jws_response_code_id` (`jws_response_code_id`),
  CONSTRAINT `jq_dynamic_rest_response_params_ibfk_1` FOREIGN KEY (`jws_dynamic_rest_details_id`) REFERENCES `jq_dynamic_rest_details` (`jws_dynamic_rest_id`),
  CONSTRAINT `jq_dynamic_rest_response_params_ibfk_2` FOREIGN KEY (`jws_response_code_id`) REFERENCES `jq_response_code_details` (`jws_response_code_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS jq_dynamic_rest_dao_details;
CREATE TABLE `jq_dynamic_rest_dao_details` (
  `jws_dao_details_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_dynamic_rest_details_id` int(11) NOT NULL,
  `jws_result_variable_name` varchar(256) NOT NULL,
  `jws_dao_query_template` mediumtext NOT NULL,
  `jws_query_sequence` int(11) NOT NULL,
  `checksum` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`jws_dao_details_id`),
  KEY `jws_dynamic_rest_details_id` (`jws_dynamic_rest_details_id`),
  CONSTRAINT `jq_dynamic_rest_dao_details_ibfk_1` FOREIGN KEY (`jws_dynamic_rest_details_id`) REFERENCES `jq_dynamic_rest_details` (`jws_dynamic_rest_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `jq_dynamic_rest_role_association`;
CREATE TABLE `jq_dynamic_rest_role_association`(
`jws_dynamic_rest_id` INT(11) NOT NULL
, `role_id` VARCHAR(50) NOT NULL
, PRIMARY KEY (`jws_dynamic_rest_id`, `role_id`)
, KEY `jws_dynamic_rest_id` (`jws_dynamic_rest_id`)
, KEY `role_id` (`role_id`)
, CONSTRAINT `jq_dynamic_rest_role_association_ibfk_1` FOREIGN KEY (`jws_dynamic_rest_id`) REFERENCES `jq_dynamic_rest_details` (`jws_dynamic_rest_id`)
, CONSTRAINT `jq_dynamic_rest_role_association_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `jq_user_role` (`role_id`)  
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


SET FOREIGN_KEY_CHECKS=1;

INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (1,200,'Success');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (2,201,'Created');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (3,204,'No Content');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (4,403,'Unauthorized');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (5,404,'Not Found');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (6,409,'Conflict');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (7,412,'Precondition Failed');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (8,500,'Internal Server Error');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (9,501,'Not Implemented');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (10,502,'Bad Gateway');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (11,503,'Service Unavailable');
INSERT INTO `jq_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (12,504,'Gateway Timeout');

INSERT INTO `jq_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (1,'POST');
INSERT INTO `jq_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (2,'GET');
INSERT INTO `jq_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (3,'PATCH');
INSERT INTO `jq_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (4,'PUT');
INSERT INTO `jq_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (5,'DELETE');
INSERT INTO `jq_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (6,'OPTIONS');
INSERT INTO `jq_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (7,'HEAD');
insert into `jq_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (8,'TRACE');

INSERT INTO `jq_response_producer_details`(`jws_response_producer_type`) VALUES ("application/json");
INSERT INTO `jq_response_producer_details`(`jws_response_producer_type`) VALUES ("application/octet-stream");
INSERT INTO `jq_response_producer_details`(`jws_response_producer_type`) VALUES ("application/xml");
INSERT INTO `jq_response_producer_details`(`jws_response_producer_type`) VALUES ("image/*");
INSERT INTO `jq_response_producer_details`(`jws_response_producer_type`) VALUES ("multipart/form-data");
INSERT INTO `jq_response_producer_details`(`jws_response_producer_type`) VALUES ("text/html");

replace into jq_template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
('8a80cb8174922d6b01749235bd840000', 'dynarest-class-template-structure', 'import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ${className} {
    
    /**
    * 
    * ${methodDescription}
    *
    */
    public Map<String, Object> ${methodName}(Map<String, Object> parameters, Map<String, Object> dAOparameters) {
        ${serviceLogic}
    }
    
}', 'admin', 'admin', NOW(), NULL);