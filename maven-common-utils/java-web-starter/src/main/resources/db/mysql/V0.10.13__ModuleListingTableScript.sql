
ALTER TABLE jq_module_listing ADD COLUMN include_layout BIT(1) DEFAULT 1 AFTER is_inside_menu;
ALTER TABLE jq_module_listing ADD UNIQUE INDEX (module_url);