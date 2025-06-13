CREATE TABLE `jq_form_io` (
  `form_io_id` varchar(50) NOT NULL,
  `form_name` varchar(50) NOT NULL,
  `form_description` varchar(200) DEFAULT NULL,
  `form_io_json` longtext DEFAULT NULL CHECK (json_valid(`form_io_json`)),
  `form_io_checksum` varchar(512) DEFAULT NULL,
  `form_io_type` int(1) DEFAULT 1,
  `is_custom_updated` int(1) DEFAULT 1,
  `created_by` varchar(100) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `last_updated_by` varchar(500) DEFAULT NULL,
  `last_updated_ts` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`form_io_id`)
);

ALTER TABLE `jq_dynamic_form` ADD COLUMN `form_io_id` VARCHAR(50) NULL AFTER `is_custom_updated`,
ADD CONSTRAINT `jq_dynamic_form_formio_fk` FOREIGN KEY (`form_io_id`) REFERENCES `jq_form_io`(`form_io_id`);
insert into `jq_master_modules` (`module_id`, `module_name`, `sequence`, `is_system_module`, `auxiliary_data`, `module_type_id`, `grid_details_id`, `module_type`, `is_entity_perm_supported`, `is_imp_exp_supported`, `is_perm_supported`) values('1faee99c-021c-11ef-a019-7c8ae1bb24d8','Form IO','60','1',NULL,NULL,'form-io-listing-grid','FormIO','1','1','1');

INSERT INTO jq_role_master_modules_association (role_module_id, role_id, module_id, is_active) VALUES
  (UUID(),'2ace542e-0c63-11eb-9cf5-f48e38ab9348','1faee99c-021c-11ef-a019-7c8ae1bb24d8', 1),
  (UUID(),'ae6465b3-097f-11eb-9a16-f48e38ab9348','1faee99c-021c-11ef-a019-7c8ae1bb24d8', 1),
  (UUID(),'b4a0dda1-097f-11eb-9a16-f48e38ab9348','1faee99c-021c-11ef-a019-7c8ae1bb24d8', 1);