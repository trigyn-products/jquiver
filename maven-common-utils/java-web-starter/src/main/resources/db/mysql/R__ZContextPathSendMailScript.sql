SET FOREIGN_KEY_CHECKS=0;

CREATE OR REPLACE VIEW jq_mail_history_data  AS
SELECT failed_mail_id  as failed_mail_id,mail_sent_by as mail_sent_by,date_format(`mail_failed_time`,"%d %b %Y %H:%m:%s") AS `mail_failed_time`,mail_sent_to as mail_sent_to,
  `jq_failed_mail_history`.`eml_file_path` AS eml_file_path
FROM jq_failed_mail_history;
  
SET FOREIGN_KEY_CHECKS=1;