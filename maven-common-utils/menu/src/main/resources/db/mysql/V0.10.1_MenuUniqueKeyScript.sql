ALTER TABLE module_listing ADD UNIQUE INDEX (`module_url`);
ALTER TABLE module_listing_i18n ADD UNIQUE INDEX (`module_id`, `language_id`, `module_name`);