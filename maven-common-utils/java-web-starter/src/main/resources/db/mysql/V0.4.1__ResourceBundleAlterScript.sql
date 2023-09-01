ALTER TABLE jq_resource_bundle CHANGE COLUMN `resource_key` `resource_key` VARCHAR(255) NOT NULL;
ALTER TABLE jq_resource_bundle CHANGE COLUMN `text` `text` LONGTEXT NOT NULL;