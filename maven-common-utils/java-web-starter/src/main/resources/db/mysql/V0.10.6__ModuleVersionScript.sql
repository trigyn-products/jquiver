ALTER TABLE jws_template_version RENAME jq_module_version;
ALTER TABLE jq_module_version ADD COLUMN module_json_checksum VARCHAR(512) DEFAULT NULL;
ALTER TABLE jq_module_version CHANGE template_version_id module_version_id VARCHAR(50) NOT NULL;
ALTER TABLE jq_module_version CHANGE template_json module_json longtext NOT NULL;