SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS jws_request_type_details;
CREATE TABLE `jws_request_type_details` (
  `jws_request_type_details_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_request_type` varchar(20) NOT NULL,
  PRIMARY KEY (`jws_request_type_details_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_response_code_details;
CREATE TABLE `jws_response_code_details` (
  `jws_response_code_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_response_status_code` int(11) NOT NULL,
  `jws_response_code_description` varchar(5000) NOT NULL,
  PRIMARY KEY (`jws_response_code_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_response_producer_details;
CREATE TABLE `jws_response_producer_details` (
  `jws_response_producer_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_response_producer_type` varchar(512) NOT NULL,
  PRIMARY KEY (`jws_response_producer_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_dynamic_rest_details;
CREATE TABLE `jws_dynamic_rest_details` (
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
  CONSTRAINT `jws_dynamic_rest_details_ibfk_1` FOREIGN KEY (`jws_request_type_id`) REFERENCES `jws_request_type_details` (`jws_request_type_details_id`),
  CONSTRAINT `jws_dynamic_rest_details_ibfk_2` FOREIGN KEY (`jws_response_producer_type_id`) REFERENCES `jws_response_producer_details` (`jws_response_producer_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS jws_dynamic_rest_response_params;
CREATE TABLE `jws_dynamic_rest_response_params` (
  `jws_response_param_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_dynamic_rest_details_id` int(11) NOT NULL,
  `jws_response_code_id` int(11) NOT NULL,
  `jws_response_code_message` text NOT NULL,
  PRIMARY KEY (`jws_response_param_id`),
  KEY `jws_dynamic_rest_details_id` (`jws_dynamic_rest_details_id`),
  KEY `jws_response_code_id` (`jws_response_code_id`),
  CONSTRAINT `jws_dynamic_rest_response_params_ibfk_1` FOREIGN KEY (`jws_dynamic_rest_details_id`) REFERENCES `jws_dynamic_rest_details` (`jws_dynamic_rest_id`),
  CONSTRAINT `jws_dynamic_rest_response_params_ibfk_2` FOREIGN KEY (`jws_response_code_id`) REFERENCES `jws_response_code_details` (`jws_response_code_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS jws_dynamic_rest_dao_details;
CREATE TABLE `jws_dynamic_rest_dao_details` (
  `jws_dao_details_id` int(11) NOT NULL AUTO_INCREMENT,
  `jws_dynamic_rest_details_id` int(11) NOT NULL,
  `jws_result_variable_name` varchar(256) NOT NULL,
  `jws_dao_query_template` mediumtext NOT NULL,
  `jws_query_sequence` int(11) NOT NULL,
  `checksum` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`jws_dao_details_id`),
  KEY `jws_dynamic_rest_details_id` (`jws_dynamic_rest_details_id`),
  CONSTRAINT `jws_dynamic_rest_dao_details_ibfk_1` FOREIGN KEY (`jws_dynamic_rest_details_id`) REFERENCES `jws_dynamic_rest_details` (`jws_dynamic_rest_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS=1;

insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (1,200,'Success');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (2,201,'Created');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (3,204,'No Content');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (4,403,'Unauthorized');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (5,404,'Not Found');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (6,409,'Conflict');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (7,412,'Precondition Failed');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (8,500,'Internal Server Error');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (9,501,'Not Implemented');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (10,502,'Bad Gateway');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (11,503,'Service Unavailable');
insert into `jws_response_code_details`(`jws_response_code_id`,`jws_response_status_code`,`jws_response_code_description`) values (12,504,'Gateway Timeout');

insert into `jws_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (1,'POST');
insert into `jws_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (2,'GET');
insert into `jws_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (3,'PATCH');
insert into `jws_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (4,'PUT');
insert into `jws_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (5,'DELETE');
insert into `jws_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (6,'OPTIONS');
insert into `jws_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (7,'HEAD');
insert into `jws_request_type_details`(`jws_request_type_details_id`,`jws_request_type`) values (8,'TRACE');

INSERT INTO `jws_response_producer_details`(`jws_response_producer_type`) VALUES ("application/json");
INSERT INTO `jws_response_producer_details`(`jws_response_producer_type`) VALUES ("application/octet-stream");
INSERT INTO `jws_response_producer_details`(`jws_response_producer_type`) VALUES ("application/xml");
INSERT INTO `jws_response_producer_details`(`jws_response_producer_type`) VALUES ("image/*");
INSERT INTO `jws_response_producer_details`(`jws_response_producer_type`) VALUES ("multipart/form-data");
INSERT INTO `jws_response_producer_details`(`jws_response_producer_type`) VALUES ("text/html");

replace into template_master (template_id, template_name, template, updated_by, created_by, updated_date, checksum) VALUES
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