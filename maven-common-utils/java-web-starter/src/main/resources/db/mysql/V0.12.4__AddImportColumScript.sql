ALTER TABLE jq_template_master ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_dynamic_form ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_dynamic_rest_details ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_api_client_details ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_job_scheduler ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_autocomplete_details ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_grid_details ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_role ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_user ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_generic_user_notification ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_additional_datasource ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_module_listing ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_property_master ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_resource_bundle ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_dashboard ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_dashlet ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_dashlet ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_entity_role_association ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;


ALTER TABLE jq_file_upload_config ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;
ALTER TABLE jq_manual_entry ADD COLUMN IF NOT EXISTS is_custom_updated INT(1) DEFAULT 0;