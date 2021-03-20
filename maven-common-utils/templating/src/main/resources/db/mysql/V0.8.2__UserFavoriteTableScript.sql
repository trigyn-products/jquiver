DROP TABLE IF EXISTS jq_user_favorite_entity;
CREATE TABLE jq_user_favorite_entity (
  favorite_id varchar(50) NOT NULL,
  user_email_id varchar(500) DEFAULT NULL,
  entity_type varchar(50) DEFAULT NULL,
  entity_id varchar(100) DEFAULT NULL,
  entity_name varchar(100) DEFAULT NULL,
  last_updated_date timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (favorite_id),
  UNIQUE KEY user_email_id (user_email_id,entity_type,entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
