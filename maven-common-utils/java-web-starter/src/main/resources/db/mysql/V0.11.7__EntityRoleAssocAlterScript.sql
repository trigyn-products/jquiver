ALTER TABLE jq_module_version CHANGE COLUMN entity_id entity_id VARCHAR(255) NOT NULL;
ALTER TABLE jq_entity_role_association  CHANGE COLUMN entity_id entity_id VARCHAR(255) NOT NULL;
ALTER TABLE jq_entity_role_association  CHANGE COLUMN entity_name entity_name VARCHAR(1000) NOT NULL;