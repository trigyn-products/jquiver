ALTER TABLE jq_failed_mail_history   
	ADD COLUMN `failed_log` LONGTEXT NOT NULL AFTER `mail_failed_time`;