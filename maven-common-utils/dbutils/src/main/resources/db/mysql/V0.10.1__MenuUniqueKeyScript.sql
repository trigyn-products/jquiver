ALTER TABLE module_listing MODIFY COLUMN target_lookup_id  int(11) DEFAULT NULL;
ALTER TABLE module_listing MODIFY COLUMN target_type_id varchar(50) DEFAULT NULL;
ALTER TABLE module_listing_i18n ADD UNIQUE INDEX (`module_id`, `language_id`, `module_name`);