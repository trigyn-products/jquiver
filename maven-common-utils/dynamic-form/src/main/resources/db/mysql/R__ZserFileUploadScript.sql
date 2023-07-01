ALTER TABLE jq_property_master MODIFY property_master_id VARCHAR(50) NOT NULL;
ALTER TABLE jq_property_master DROP PRIMARY KEY;
ALTER TABLE jq_property_master ADD PRIMARY KEY (`property_master_id`);
ALTER TABLE jq_property_master ADD UNIQUE INDEX (`owner_type`,`owner_id`,`property_name`); 
