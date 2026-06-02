CREATE TABLE `jq_manual_editor_lookup` (  
  `editor_id` INT(3) NOT NULL,
  `editor_name` VARCHAR(100),
  PRIMARY KEY (`editor_id`)
);

insert into `jq_manual_editor_lookup` (`editor_id`, `editor_name`) values('1','Markdown Editor');
insert into `jq_manual_editor_lookup` (`editor_id`, `editor_name`) values('2','CK Editor');


ALTER TABLE `jq_manual_type`   
	ADD COLUMN `editor_name` VARCHAR(100) NULL AFTER `last_updated_ts`;
	