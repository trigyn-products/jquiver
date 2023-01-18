ALTER TABLE jq_generic_user_notification DROP COLUMN target_platform;
ALTER TABLE jq_generic_user_notification DROP COLUMN message_format;
ALTER TABLE jq_generic_user_notification ADD COLUMN display_once INT(11) DEFAULT 0;
 