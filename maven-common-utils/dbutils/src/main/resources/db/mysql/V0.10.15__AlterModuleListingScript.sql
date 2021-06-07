ALTER TABLE jq_module_listing ADD COLUMN module_type_id int(11) DEFAULT 1;
UPDATE jq_module_listing SET module_type_id = 1;