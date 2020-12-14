ALTER TABLE jws_module_version CHANGE COLUMN entity_id entity_id VARCHAR(255) NOT NULL;
ALTER TABLE jws_entity_role_association CHANGE COLUMN entity_id entity_id VARCHAR(255) NOT NULL;
ALTER TABLE jws_entity_role_association CHANGE COLUMN entity_name entity_name VARCHAR(1000) NOT NULL;