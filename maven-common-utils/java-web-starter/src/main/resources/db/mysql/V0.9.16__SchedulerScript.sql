ALTER TABLE jq_job_scheduler ADD COLUMN scheduler_type_id int(1) DEFAULT 1;
ALTER TABLE jq_job_scheduler ADD COLUMN failed_notification_params text;