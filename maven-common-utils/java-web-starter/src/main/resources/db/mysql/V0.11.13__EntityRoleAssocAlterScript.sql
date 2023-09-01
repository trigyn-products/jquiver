ALTER TABLE jq_entity_role_association 
DROP KEY entity_role_id,
ADD UNIQUE KEY jq_entity_role_association_ibuk_1 (entity_id,role_id,module_id);