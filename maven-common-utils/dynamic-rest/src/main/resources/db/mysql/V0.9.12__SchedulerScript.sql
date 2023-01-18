SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS jq_job_scheduler(
  scheduler_id varchar(50)NOT NULL,
  scheduler_name varchar(50) NOT NULL,
  jws_dynamic_rest_id varchar(50) NOT NULL,
  cron_scheduler varchar(50) NOT NULL,
  is_active int(11) NOT NULL DEFAULT 1,
  `last_modified_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  `modified_by` varchar(50) NOT NULL,
  PRIMARY KEY (scheduler_id),
  KEY jq_job_scheduler_ibfk1(jws_dynamic_rest_id),
  CONSTRAINT jq_job_scheduler_ibfk1 FOREIGN KEY (jws_dynamic_rest_id) REFERENCES jq_dynamic_rest_details (jws_dynamic_rest_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS jq_job_scheduler_log(
  scheduler_log_id varchar(50)NOT NULL,
  scheduler_id varchar(50) NOT NULL,
  response_code varchar(50) NOT NULL,
  response_body text NOT NULL ,
  request_time timestamp,
  response_time timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  PRIMARY KEY (scheduler_log_id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS=0;