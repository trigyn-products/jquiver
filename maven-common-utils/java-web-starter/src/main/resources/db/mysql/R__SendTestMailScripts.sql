SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS jq_failed_mail_history;
CREATE TABLE jq_failed_mail_history (
  `failed_mail_id` varchar(100) NOT NULL, 
  `mail_sent_to` varchar(200) NOT NULL,
  `mail_sent_by` varchar(150) NOT NULL,
  `eml_file_path` varchar(150) NOT NULL,
  `mail_failed_time` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`failed_mail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE OR REPLACE VIEW jq_mail_history_data  AS
SELECT failed_mail_id  as failed_mail_id,mail_sent_by as mail_sent_by,date_format(`mail_failed_time`,"%d %b %Y") AS `mail_failed_time`,mail_sent_to as mail_sent_to
FROM jq_failed_mail_history;

SET FOREIGN_KEY_CHECKS=1;