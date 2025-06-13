CREATE TABLE jq_salt_details (
  request_id varchar(50) NOT NULL,
  salt varchar(50) DEFAULT NULL,
  request_time timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (request_id)
)