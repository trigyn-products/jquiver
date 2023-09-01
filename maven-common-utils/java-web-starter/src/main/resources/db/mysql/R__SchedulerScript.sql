
SET FOREIGN_KEY_CHECKS=0;

CREATE  OR REPLACE VIEW `jq_scheduler_view` AS
SELECT
  `jjs`.`scheduler_id`          AS `scheduler_id`,
  `jjs`.`scheduler_name`        AS `scheduler_name`,
  `jjs`.`jws_dynamic_rest_id`   AS `jws_dynamic_rest_id`,
  `jjs`.`cron_scheduler`        AS `cron_scheduler`,
  `jjs`.`is_active`             AS `is_active`,
  `jjs`.`scheduler_type_id`     AS `schedulerTypeId`,
  `jjs`.`last_modified_date`    AS `isAfterDate`,
  (SELECT
     MAX(`jjsl`.`response_time`)
   FROM `jq_job_scheduler_log` `jjsl`
   WHERE `jjsl`.`scheduler_id` = `jjs`.`scheduler_id`) AS `last_response_time`,
  (SELECT
     COUNT(`jjsl`.`response_time`)
   FROM `jq_job_scheduler_log` `jjsl`
   WHERE `jjsl`.`scheduler_id` = `jjs`.`scheduler_id`) AS `reminder_count`,
  `jdrd`.`jws_dynamic_rest_url` AS `jws_dynamic_rest_url`,
   COALESCE(CONCAT(`jus`.`first_name`, ' ', `jus`.`last_name` ),`jjs`.`modified_by`) AS `modified_by` 
FROM (`jq_job_scheduler` `jjs`
LEFT JOIN `jq_user` `jus` ON(
        `jus`.`email` = `jjs`.`modified_by`)
   LEFT JOIN `jq_dynamic_rest_details` `jdrd`
     ON (`jjs`.`jws_dynamic_rest_id` = `jdrd`.`jws_dynamic_rest_id`));


REPLACE INTO jq_grid_details(grid_id, grid_name, grid_description, grid_table_name, grid_column_names, query_type, grid_type_id, created_by, created_date, last_updated_ts) 
VALUES ("jq-schedulerGrid", 'jq-schedulerGrid', 'jq-scheduler Listing', 'jq_scheduler_view'
,'*', 1, 2, 'admin@jquiver.io', NOW(), NOW());

ALTER TABLE jq_job_scheduler_log MODIFY response_body longtext;

SET FOREIGN_KEY_CHECKS=1;

