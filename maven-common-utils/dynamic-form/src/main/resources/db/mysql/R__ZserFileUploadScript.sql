ALTER TABLE jq_property_master MODIFY property_master_id VARCHAR(50) NOT NULL;
ALTER TABLE jq_property_master DROP PRIMARY KEY;
ALTER TABLE jq_property_master ADD PRIMARY KEY (`property_master_id`);
ALTER TABLE jq_property_master ADD UNIQUE INDEX (`owner_type`,`owner_id`,`property_name`); 


UPDATE jq_module_listing SET last_updated_by = 'admin@jquiver.io';
UPDATE jq_dynamic_rest_details SET last_updated_by = 'admin@jquiver.io';
UPDATE jq_dynamic_form SET last_updated_by = 'admin@jquiver.io';
UPDATE `jq_file_upload_config` SET last_updated_by = 'admin@jquiver.io';
UPDATE `jq_autocomplete_details` SET last_updated_by = 'admin@jquiver.io';
UPDATE `jq_grid_details` SET last_updated_by = 'admin@jquiver.io';